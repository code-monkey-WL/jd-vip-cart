package com.jd.o2o.vipcart.common.plugins.cache.aspect.impl;

import com.jd.o2o.vipcart.common.plugins.cache.aspect.LocalCached;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 方法调用结果缓存
 * Created by liuhuiqing on 2015/8/13.
 */
@Aspect
public final class LocalCachedAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalCachedAspect.class);
    /**
     * Calling tunnels.
     */
    private final transient ConcurrentMap<Key, Tunnel> tunnels = new ConcurrentHashMap<Key, Tunnel>(0);

    /**
     * 缓存清除worker
     */
    private final transient ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    public LocalCachedAspect() {
        this.cleaner.scheduleWithFixedDelay(
                new Runnable() {
                    @Override
                    public void run() {
                        LocalCachedAspect.this.clean();
                    }
                }
                ,
                1L, 1L, TimeUnit.SECONDS
        );
    }

    /**
     * 调用服务方法或直接冲缓存中获取数据
     * 不要修改方法签名，否则可能返回值可能不兼容
     *
     * @param point Joint point
     * @return The result of call
     * @throws Throwable
     */
    @Around("execution(* *(..)) && @annotation(com.jd.o2o.vipcart.common.plugins.cache.aspect.LocalCached)")
    public Object cache(final ProceedingJoinPoint point) throws Throwable {
        final Key key = new Key(point);
        Tunnel tunnel;
        final Method method = MethodSignature.class
                .cast(point.getSignature())
                .getMethod();
        final LocalCached annot = method.getAnnotation(LocalCached.class);
        synchronized (this.tunnels) {
            for (final Class<?> before : annot.before()) {
                final boolean flag = Boolean.class.cast(
                        before.getMethod("flushBefore").invoke(method.getClass())
                );
                if (flag) {
                    this.preflush(point);
                }
            }
            tunnel = this.tunnels.get(key);
            if (tunnel == null || tunnel.expired()) {
                tunnel = new Tunnel(point, key);
                this.tunnels.put(key, tunnel);
            }
            for (final Class<?> after : annot.after()) {
                final boolean flag = Boolean.class.cast(
                        after.getMethod("flushAfter").invoke(method.getClass())
                );
                if (flag) {
                    this.postflush(point);
                }
            }
        }
        return tunnel.through();
    }

    /**
     * 清空缓存，不要修改方法签名，否则可能返回值可能不兼容
     *
     * @param point Joint point
     */
    @Before("execution(* *(..)) && @annotation(com.jd.o2o.vipcart.common.plugins.cache.aspect.LocalCached.FlushBefore)")
    public void preflush(final JoinPoint point) {
        this.flush(point, "before the call");
    }

    /**
     * 方法执行后清空缓存
     * 不要修改方法签名，否则可能返回值可能不兼容
     * @param point Joint point
     */
    @After(
            "execution(* *(..))"
                    + " && @annotation(com.jd.o2o.vipcart.common.plugins.cache.aspect.LocalCached.FlushAfter)"
    )
    public void postflush(final JoinPoint point) {
        this.flush(point, "after the call");
    }

    /**
     * 清空缓存
     *
     * @param point Joint point
     * @param when  When it happens
     */
    private void flush(final JoinPoint point, final String when) {
        synchronized (this.tunnels) {
            for (final Key key : this.tunnels.keySet()) {
                if (!key.sameTarget(point)) {
                    continue;
                }
                final Tunnel removed = this.tunnels.remove(key);
                final Method method = MethodSignature.class
                        .cast(point.getSignature())
                        .getMethod();

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(new StringBuilder(method.getDeclaringClass().getName())
                            .append(".")
                            .append(method.getName())
                            .append(String.format(" %s: %s:%s removed from cache %s",
                                    new Object[]{JsonUtils.toJson(point.getArgs()), key, removed, when})).toString());
                }
            }
        }
    }

    /**
     * Clean cache.
     */
    private void clean() {
        synchronized (this.tunnels) {
            for (final Key key : this.tunnels.keySet()) {
                if (this.tunnels.get(key).expired()) {
                    final Tunnel tunnel = this.tunnels.remove(key);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(String.format("%s:%s expired in cache", new Object[]{key, tunnel}));
                    }
                }
            }
        }
    }

    /**
     * Mutable caching/calling tunnel, it is thread-safe.
     */
    private static final class Tunnel {
        /**
         * Proceeding join point.
         */
        private final transient ProceedingJoinPoint point;
        /**
         * Key related to this tunnel.
         */
        private final transient Key key;
        /**
         * Was it already executed?
         */
        private transient boolean executed;
        /**
         * When will it expire (moment in time).
         */
        private transient long lifetime;
        /**
         * Cached value.
         */
        private transient Object cached;

        /**
         * Public ctor.
         *
         * @param pnt  Joint point
         * @param akey The key related to it
         */
        Tunnel(final ProceedingJoinPoint pnt, final Key akey) {
            this.point = pnt;
            this.key = akey;
        }

        @Override
        public String toString() {
            return JsonUtils.toJson(this.cached);
        }

        /**
         * 从tunnel中获得缓存结果
         *
         * @return The result
         * @throws Throwable If something goes wrong inside
         */
        public synchronized Object through() throws Throwable {
            if (!this.executed) {
                this.cached = this.point.proceed();
                final Method method = MethodSignature.class
                        .cast(this.point.getSignature())
                        .getMethod();
                final LocalCached annot = method.getAnnotation(LocalCached.class);
                if(!isUseCache(annot)){
                    return this.cached;
                }
                final long start = System.currentTimeMillis();
                final String suffix;
                if (annot.forever()) {
                    this.lifetime = Long.MAX_VALUE;
                    suffix = "valid forever";
                } else if (annot.lifetime() == 0) {
                    this.lifetime = 0L;
                    suffix = "invalid immediately";
                } else {
                    final long msec = annot.unit().toMillis(
                            (long) annot.lifetime()
                    );
                    this.lifetime = start + msec;
                    suffix = String.format("valid for [%s]ms", msec);
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(new StringBuilder(method.getDeclaringClass().getName())
                            .append(".")
                            .append(String.format(" %s: %s cached in [%s]ms, %s",
                                    new Object[]{method.getName(),JsonUtils.toJson(this.cached), System.currentTimeMillis() - start, suffix})).toString());
                }
                this.executed = true;
            }
            return this.key.through(this.cached);
        }

        /**
         * 是否将缓存结果添加到缓存中去
         * @param annot
         * @return
         */
        private boolean isUseCache(LocalCached annot){
            boolean isSuccess = true;
            String methodName = annot.successMethod();
            if(StringUtils.isNotEmpty(methodName)){
                try{
                    Method m = this.cached.getClass().getMethod(methodName);
                    isSuccess = Boolean.class.cast(m.invoke(this.cached,null));
                }catch (Exception e){
                    LOGGER.warn(String.format("执行业务返回对象结果判断方法[%s]出现错误",methodName),e);
                }
            }
            return isSuccess;
        }

        /**
         * Is it expired already?
         *
         * @return TRUE if expired
         */
        public boolean expired() {
            return this.executed && this.lifetime < System.currentTimeMillis();
        }
    }

    /**
     * Key of a callable target.
     */
    private static final class Key {
        /**
         * When instantiated.
         */
        private final transient long start = System.currentTimeMillis();
        /**
         * How many times the key was already accessed.
         */
        private final transient AtomicInteger accessed = new AtomicInteger();
        /**
         * Method.
         */
        private final transient Method method;
        /**
         * Object callable (or class, if static method).
         */
        private final transient Object target;
        /**
         * Arguments.注意方法执行过程中可能会有变更
         */
        private final transient Object[] arguments;
        /**
         * 原始入参字符串
         */
        private final transient String argsString;

        /**
         * Public ctor.
         *
         * @param point Joint point
         */
        Key(final JoinPoint point) {
            this.method = MethodSignature.class
                    .cast(point.getSignature()).getMethod();
            this.target = Key.targetize(point);
            this.arguments = point.getArgs();
            this.argsString = JsonUtils.toJson(arguments);
        }

        @Override
        public String toString() {
            return new StringBuilder(this.method.getName()).append(" ").append(argsString).toString();
        }

        @Override
        public int hashCode() {
            return this.method.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            final boolean equals;
            if (this == obj) {
                equals = true;
            } else if (obj instanceof Key) {
                final Key key = Key.class.cast(obj);
                equals = key.method.equals(this.method)
                        && this.target.equals(key.target)
                        && JsonUtils.toJson(key.argsString).equals(JsonUtils.toJson(argsString));
//                && Arrays.deepEquals(key.arguments, this.arguments);
            } else {
                equals = false;
            }
            return equals;
        }

        /**
         * Send a result through, with necessary logging.
         *
         * @param result The result to send through
         * @return The same result/object
         */
        public Object through(final Object result) {
            final int hit = this.accessed.getAndIncrement();
            if (hit > 0) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(new StringBuilder(method.getDeclaringClass().getName())
                            .append(".")
                            .append(method.getName())
                            .append(String.format(" %s: %s from cache (hit #%s, [%s]ms old",
                                    new Object[]{method.getName(), JsonUtils.toJson(result), hit, System.currentTimeMillis() - start})).toString());
                }
            }
            return result;
        }

        /**
         * Is it related to the same target?
         *
         * @param point Proceeding point
         * @return True if the target is the same
         */
        public boolean sameTarget(final JoinPoint point) {
            return Key.targetize(point).equals(this.target);
        }

        /**
         * Calculate its target.
         *
         * @param point Proceeding point
         * @return The target
         */
        private static Object targetize(final JoinPoint point) {
            final Object tgt;
            final Method method = MethodSignature.class
                    .cast(point.getSignature()).getMethod();
            if (Modifier.isStatic(method.getModifiers())) {
                tgt = method.getDeclaringClass();
            } else {
                tgt = point.getTarget();
            }
            return tgt;
        }

    }
}
