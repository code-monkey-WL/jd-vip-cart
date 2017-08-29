package com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 缓存操作来源解析（注解或xml等）
 * Created by liuhuiqing on 2015/10/30.
 */
public interface CacheOperationSource {
    /**
     * 返回方法上的缓存注解信息
     * @param method
     * @param targetClass
     * @return
     */
    Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass);
}
