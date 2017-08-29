package com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel;

import com.jd.o2o.vipcart.common.plugins.cache.aspect.Cache;

import java.lang.reflect.Method;

/**
 * SPEL表达式缓存上下文对象
 * Created by liuhuiqing on 2015/10/30.
 */
public class CacheExpressionRootObject extends ExpressionRootObject {
    private final Cache cache;

    public CacheExpressionRootObject(
            Cache cache, Method method, Object[] args, Object target, Class<?> targetClass) {
        super(method, args, target, targetClass);
        this.cache = cache;
    }

    public Cache getCache() {
        return this.cache;
    }
}
