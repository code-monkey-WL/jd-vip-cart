package com.jd.o2o.vipcart.common.plugins.cache.aspect.impl;

import com.jd.o2o.vipcart.common.plugins.cache.aspect.Backup;
import com.jd.o2o.vipcart.common.plugins.cache.aspect.Cache;
import com.jd.o2o.vipcart.common.plugins.cache.aspect.support.DefaultKeyGenerator;
import com.jd.o2o.vipcart.common.plugins.cache.aspect.support.KeyGenerator;
import com.jd.o2o.vipcart.common.plugins.thread.ThreadPoolFactory;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 方法调用结果缓存备份
 * Created by liuhuiqing on 2017/7/3.
 */
@Aspect
public final class BackupAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackupAspect.class);
    private KeyGenerator keyGenerator = new DefaultKeyGenerator();
    private static final Object NULL_HOLDER = new NullHolder();// 空值对象
    private final boolean allowNullValues;// 空值标记
    private volatile boolean openRead = false;// 是否从缓存中读取数据
    private volatile boolean openWrite = true;// 是否往缓存中写数据
    private Cache cache; // 缓存实例对象
    private transient ThreadPoolTaskExecutor threadPoolTaskExecutor;// 缓存异步处理线程池
    private final transient ConcurrentMap<Object, Tunnel> tunnels = new ConcurrentHashMap<Object, Tunnel>(0); // 请求队列，防业务并发
    private final transient ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor(); // 缓存开关控制
    private final static String OPEN_READ_KEY = "openReadBackup"; // 备份缓存读开关key
    private final static String OPEN_WRITE_KEY = "openWriteBackup"; // 备份缓存写开关key
    private final static Map<String, SoftReference<Date>> SOFT_REFERENCE_MAP = new HashMap<String, SoftReference<Date>>();

    public BackupAspect(boolean allowNullValues, final Cache cache) {
        this.allowNullValues = allowNullValues;
        this.cache = cache;
        this.schedule.scheduleWithFixedDelay(
                new Runnable() {
                    @Override
                    public void run() {
                        BackupAspect.this.clean();
                        Boolean isRead = syncCacheConfig(OPEN_READ_KEY);
                        if (isRead != null) {
                            openRead = isRead;
                        }
                        Boolean isWrite = syncCacheConfig(OPEN_WRITE_KEY);
                        if (isWrite != null) {
                            openWrite = isWrite;
                        }
                    }
                }
                ,
                1L, 60L, TimeUnit.SECONDS
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
    @Around("execution(* *(..)) && @annotation(com.jd.o2o.vipcart.common.plugins.cache.aspect.Backup)")
    public Object backup(final ProceedingJoinPoint point) throws Throwable {
        final Object key = generateKey(point);
        Object obj = readCache(key);
        if (obj != null) {
            return fromStoreValue(obj);
        }
        Tunnel tunnel;
        synchronized (this.tunnels) {
            tunnel = this.tunnels.get(key);
            if (tunnel == null) {
                tunnel = new Tunnel(point);
                this.tunnels.put(key, tunnel);
            }
        }
        // 执行业务处理逻辑
        obj = tunnel.proceed();
        writeCache(point, key, obj);
        return obj;
    }

    /**
     * 读缓存
     *
     * @param key
     * @return
     */
    private Object readCache(Object key) {
        Object obj = null;
        if (!openRead) {
            return obj;
        }
        try {
            obj = cache.get(key);
        } catch (Exception e) {
            LOGGER.warn(String.format("缓存中取值key=[%s]出现异常", key), e);
        }
        return obj;
    }

    /**
     * 写缓存
     *
     * @param point
     * @param key
     * @param obj
     */
    private void writeCache(final ProceedingJoinPoint point, final Object key, Object obj) {
        if (!openWrite) {
            return;
        }
        try {
            final Method method = MethodSignature.class.cast(point.getSignature()).getMethod();
            final Backup annot = method.getAnnotation(Backup.class);
            long refreshInterval = annot.refreshInterval();
            if (timeToUpdate(key, (int) annot.unit().toSeconds(refreshInterval)) && isUseCache(annot, obj)) {
                final Object value = obj;
                updateCaching(key);
                getThreadPoolTaskExecutor().submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        putCache(key, value, method, annot);
                        return null;
                    }
                });
            }
        } catch (Exception e) {
            LOGGER.warn(String.format("缓存中存值key=[%s]出现异常", key), e);
        }
    }

    /**
     * 执行写缓存操作
     *
     * @param key
     * @param value
     * @param method
     * @param annot
     */
    private void putCache(Object key, Object value, Method method, Backup annot) {
        try {
            // 计算缓存时间
            int lifeTime = getLifeTime(annot);
            cache.put(key, toStoreValue(value), lifeTime);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(new StringBuilder(method.getDeclaringClass().getName())
                        .append(".")
                        .append(String.format(" %s: %s cached in [%s]s",
                                new Object[]{method.getName(), JsonUtils.toJson(value), lifeTime})).toString());
            }
        } catch (Exception e) {
            LOGGER.warn(String.format("缓存中存值key=[%s]出现异常", key), e);
        }
    }

    /**
     * 计算缓存生命周期
     *
     * @param annot
     * @return
     */
    private int getLifeTime(Backup annot) {
        int lifeTime;
        if (annot.forever()) {
            lifeTime = Integer.MAX_VALUE;
        } else if (annot.lifetime() == 0) {
            lifeTime = 0;
        } else {
            TimeUnit units = annot.unit();
            long seconds = units.toSeconds((long) annot.lifetime());
            if (Integer.MAX_VALUE < seconds) {
                lifeTime = Integer.MAX_VALUE;
            } else {
                lifeTime = (int) seconds;
            }
        }
        return lifeTime;
    }

    /**
     * 是否将缓存结果添加到缓存中去
     *
     * @param annot
     * @param result
     * @return
     */
    private boolean isUseCache(Backup annot, Object result) {
        boolean isSuccess = true;
        String methodName = annot.successMethod();
        if (StringUtils.isNotEmpty(methodName) && result != null) {
            try {
                Method m = result.getClass().getMethod(methodName);
                isSuccess = Boolean.class.cast(m.invoke(result, null));
            } catch (Exception e) {
                LOGGER.warn(String.format("执行业务返回对象结果判断方法[%s](结果数据是否需要缓存)出现错误", methodName), e);
            }
        }
        return isSuccess;
    }

    /**
     * 读缓存取值空数据转换
     *
     * @param storeValue
     * @return
     */
    protected Object fromStoreValue(Object storeValue) {
        if (this.allowNullValues && NULL_HOLDER.equals(storeValue)) {
            return null;
        }
        return storeValue;
    }

    /**
     * 写缓存空值数据转换
     *
     * @param userValue
     * @return
     */
    protected Object toStoreValue(Object userValue) {
        if (this.allowNullValues && userValue == null) {
            return NULL_HOLDER;
        }
        return userValue;
    }

    /**
     * 获得线程池
     *
     * @return
     */
    private ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        if (this.threadPoolTaskExecutor == null) {
            // 如果没有个性化制定，则使用工厂类生成线程池，要保证工厂类受spring管理（否则部分功能会失效）
            this.threadPoolTaskExecutor = ThreadPoolFactory.getInstance().getThreadPoolTaskExecutor("backupAspect", 10000);
        }
        return this.threadPoolTaskExecutor;
    }

    /**
     * 配置信息key：主要针对openRead和openWrite
     *
     * @param key
     * @return
     */
    private String buildCacheConfigKey(String key) {
        return new StringBuilder().append(this.cache.getName()).append("-backup").append(":").append(key).toString();
    }

    /**
     * 从缓存中获取缓存配置信息：主要针对openRead和openWrite
     *
     * @param key
     * @return
     */
    private Boolean syncCacheConfig(String key) {
        Boolean value = null;
        try {
            key = buildCacheConfigKey(key);
            Object obj = this.cache.get(key);
            if (obj instanceof Boolean) {
                value = (Boolean) obj;
            }
        } catch (Exception e) {
            LOGGER.error(String.format("同步缓存key[%s]为[%s]出现异常", new Object[]{key, value}), e);
        }
        return value;
    }

    /**
     * 设置缓存配置信息：主要针对openRead和openWrite
     *
     * @param key
     * @param value
     * @return
     */
    private Boolean setCacheConfig(String key, Object value) {
        if (value == null) {
            value = NULL_HOLDER;
        }
        try {
            key = buildCacheConfigKey(key);
            this.cache.put(key, value, Integer.MAX_VALUE);
            return true;
        } catch (Exception e) {
            LOGGER.error(String.format("设置缓存key[%s]为[%s]出现异常", new Object[]{key, JsonUtils.toJson(value)}), e);
        }
        return false;
    }

    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public boolean isOpenRead() {
        return openRead;
    }

    public boolean isOpenWrite() {
        return openWrite;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public void setOpenRead(boolean openRead) {
        this.openRead = openRead;
        if (!setCacheConfig(OPEN_READ_KEY, openRead)) {
            throw new RuntimeException(new StringBuilder(openRead ? "打开" : "关闭").append("读缓存开关失败").toString());
        }
    }

    public void setOpenWrite(boolean openWrite) {
        this.openWrite = openWrite;
        if (!setCacheConfig(OPEN_WRITE_KEY, openWrite)) {
            throw new RuntimeException(new StringBuilder(openWrite ? "打开" : "关闭").append("写缓存开关失败").toString());
        }
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    /**
     * 清除防并发缓存对象
     */
    private void clean() {
        synchronized (this.tunnels) {
            for (final Map.Entry<Object, Tunnel> entry : this.tunnels.entrySet()) {
                if (entry.getValue().executed) {
                    this.tunnels.remove(entry.getKey());
                }
            }
        }
    }

    /**
     * 业务逻辑执行
     */
    private static final class Tunnel implements Serializable {
        private final transient ProceedingJoinPoint point;
        private volatile boolean executed;
        private Object value;

        Tunnel(final ProceedingJoinPoint pnt) {
            this.point = pnt;
        }

        public synchronized Object proceed() throws Throwable {
            if (!this.executed) {
                value = point.proceed();
                this.executed = true;
            }
            return value;
        }
    }

    /**
     * 缓存key
     */
    protected Object generateKey(ProceedingJoinPoint point) {
        return "b_"+keyGenerator.generate(point.getTarget(), MethodSignature.class
                .cast(point.getSignature()).getMethod(), point.getArgs());
    }

    /**
     * 空值对象
     */
    private static class NullHolder implements Serializable {
        @Override
        public boolean equals(Object obj) {
            if(obj==null){
                return false;
            }
            if(this==obj){
                return true;
            }
            if(obj instanceof NullHolder){
                return true;
            }
            return false;
        }
    }

    /**
     * 是否需要更新缓存
     *
     * @param key
     * @param refreshInterval
     * @return
     */
    public boolean timeToUpdate(Object key, int refreshInterval) {
        SoftReference<Date> reference = SOFT_REFERENCE_MAP.get(String.valueOf(key));
        if (reference == null) {
            return true;
        }
        // 已过期
        if (reference.get().before(new DateTime().minusSeconds(refreshInterval).toDate())) {
            SOFT_REFERENCE_MAP.remove(key);
            return true;
        }
        return false;
    }

    /**
     * 更新缓存为未失效状态
     *
     * @param key
     */
    public void updateCaching(Object key) {
        SOFT_REFERENCE_MAP.put(String.valueOf(key), new SoftReference<Date>(new Date()));
    }
}
