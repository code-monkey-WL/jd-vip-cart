package com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 缓存操作来源解析器合并
 * Created by liuhuiqing on 2015/10/30.
 */
public class CompositeCacheOperationSource implements CacheOperationSource, Serializable {

    private final CacheOperationSource[] cacheOperationSources; // 缓存来源

    public CompositeCacheOperationSource(CacheOperationSource... cacheOperationSources) {
        Assert.notEmpty(cacheOperationSources, "cacheOperationSources array must not be empty");
        this.cacheOperationSources = cacheOperationSources;
    }

    public final CacheOperationSource[] getCacheOperationSources() {
        return this.cacheOperationSources;
    }

    public Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass) {
        Collection<CacheOperation> ops = null;

        for (CacheOperationSource source : this.cacheOperationSources) {
            Collection<CacheOperation> cacheOperations = source.getCacheOperations(method, targetClass);
            if (cacheOperations != null) {
                if (ops == null) {
                    ops = new ArrayList<CacheOperation>();
                }

                ops.addAll(cacheOperations);
            }
        }
        return ops;
    }
}