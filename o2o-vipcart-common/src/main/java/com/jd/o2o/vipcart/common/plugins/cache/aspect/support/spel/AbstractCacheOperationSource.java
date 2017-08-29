package com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuhuiqing on 2015/10/30.
 */
public abstract class AbstractCacheOperationSource implements CacheOperationSource {
    private static final Logger logger = LoggerFactory.getLogger(AbstractCacheOperationSource.class);
    /**
     * 缓存空配置方法
     */
    private final static Collection<CacheOperation> NULL_CACHING_ATTRIBUTE = Collections.emptyList();

    /**
     * 缓存方法注解信息
     */
    final Map<Object, Collection<CacheOperation>> attributeCache = new ConcurrentHashMap<Object, Collection<CacheOperation>>();


    /**
     * 获得方法缓存属性，如果没有则获得方法所在类的
     *
     * @param method
     * @param targetClass
     * @return {@link CacheOperation} for this method, or {@code null} if the method
     * is not cacheable
     */
    public Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass) {
        Object cacheKey = getCacheKey(method, targetClass);
        Collection<CacheOperation> cached = this.attributeCache.get(cacheKey);
        if (cached != null) {
            if (cached == NULL_CACHING_ATTRIBUTE) {
                return null;
            }
            return cached;
        } else {
            Collection<CacheOperation> cacheOps = computeCacheOperations(method, targetClass);
            if (cacheOps == null) {
                this.attributeCache.put(cacheKey, NULL_CACHING_ATTRIBUTE);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("添加缓存方法[" + method.getName() + "] 配置: " + cacheOps);
                }
                this.attributeCache.put(cacheKey, cacheOps);
            }
            return cacheOps;
        }
    }

    /**
     * 根据给定方法和目标类类型生成缓存key
     * 要保证重载方法不能生成一样的key，不同的实例的同一个方法生成相同的key
     *
     * @param method      the method (never {@code null})
     * @param targetClass the target class (may be {@code null})
     * @return the cache key (never {@code null})
     */
    protected Object getCacheKey(Method method, Class<?> targetClass) {
        return new DefaultCacheKey(method, targetClass);
    }

    private Collection<CacheOperation> computeCacheOperations(Method method, Class<?> targetClass) {
        // Don't allow no-public methods as required.
        if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
            return null;
        }

        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        // First try is the method in the target class.
        Collection<CacheOperation> opDef = findCacheOperations(specificMethod);
        if (opDef != null) {
            return opDef;
        }

        // Second try is the caching operation on the target class.
        opDef = findCacheOperations(specificMethod.getDeclaringClass());
        if (opDef != null) {
            return opDef;
        }

        if (specificMethod != method) {
            // Fall back is to look at the original method.
            opDef = findCacheOperations(method);
            if (opDef != null) {
                return opDef;
            }
            // Last fall back is the class of the original method.
            return findCacheOperations(method.getDeclaringClass());
        }
        return null;
    }


    /**
     * 子类实现缓存属性解析（方法）
     * @param method
     * @return 所有方法关联的缓存属性
     * (or {@code null} if none)
     */
    protected abstract Collection<CacheOperation> findCacheOperations(Method method);

    /**
     * 子类实现缓存属性解析（类）
     *
     * @param clazz
     * @return 所有类定义关联的缓存属性
     * (or {@code null} if none)
     */
    protected abstract Collection<CacheOperation> findCacheOperations(Class<?> clazz);

    /**
     * 是否只有公共方法才允许缓存
     * 默认实现为 {@code false}.
     */
    protected boolean allowPublicMethodsOnly() {
        return false;
    }


    /**
     * 对象CacheOperation缓存的默认key
     */
    private static class DefaultCacheKey {

        private final Method method;

        private final Class<?> targetClass;

        public DefaultCacheKey(Method method, Class<?> targetClass) {
            this.method = method;
            this.targetClass = targetClass;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof DefaultCacheKey)) {
                return false;
            }
            DefaultCacheKey otherKey = (DefaultCacheKey) other;
            return (this.method.equals(otherKey.method) && ObjectUtils.nullSafeEquals(this.targetClass,
                    otherKey.targetClass));
        }

        @Override
        public int hashCode() {
            return this.method.hashCode() * 29 + (this.targetClass != null ? this.targetClass.hashCode() : 0);
        }
    }
}
