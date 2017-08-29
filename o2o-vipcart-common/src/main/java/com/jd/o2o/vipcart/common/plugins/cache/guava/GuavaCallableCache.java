package com.jd.o2o.vipcart.common.plugins.cache.guava;

import com.google.common.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * 抽象Guava缓存类、缓存模板。
 * 子类需要实现fetchData(key)，从数据库或其他数据源（如Redis）中获取数据。
 * 子类调用getValue(key)方法，从缓存中获取数据，并处理不同的异常，比如value为null时的InvalidCacheLoadException异常。
 *
 * @param <K> key 类型
 * @param <V> value 类型
 * Created by liuhuiqing on 2015/7/29.
 */
public abstract class GuavaCallableCache<K, V, P> extends AbstractGuavaCache<K, V>{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static final Map<String, GuavaCallableCache> cacheMap = new ConcurrentHashMap<String, GuavaCallableCache>();


    /**
     * 获取缓存对象
     * @return cache
     */
    @Override
    public Cache<K, V> getCache() {
        if(cache == null){  //使用双重校验锁保证只有一个cache实例
            synchronized (this) {
                if(cache == null){
                    cache = getCallableCache();
                }
            }
        }
        return cache;
    }

    /**
     * 从缓存中获取数据，并处理异常
     * @param key
     * @return Value
     */
//    protected V fromCache(K key, Callable<V> callable){
//        V result = null;
//        try {
//            result = getCache().get(key, callable);
//        } catch (ExecutionException e) {
//            LOGGER.error(String.format("key=[%s]调用本地缓存失败", key), e);
//        }
//        return result;
//    }

    /**
     * 从缓存中获取数据，并处理异常
     * @param key
     * @return Value
     */
    protected V fromCache(K key, final P param){
        V result = null;
        try {
            result = getCache().get(key, new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return fetchData(param);
                }
            });
        } catch (ExecutionException e) {
            LOGGER.error(String.format("key=[%s]调用本地缓存失败", key), e);
        }
        return result;
    }

    /**
     * 根据key从数据库或其他数据源中获取一个value，并被自动保存到缓存中。
     * @param key
     * @return value,连同key一起被加载到缓存中的。
     */
    protected abstract V fetchData(P key);

    /**
     * 为缓存对象命名并进行对象缓存
     *
     * @param key
     * @return
     */
    @Override
    public GuavaCallableCache<K,V,P> named(String key) {
        cacheMap.put(key, this);
        return this;
    }

    public static Map<String, GuavaCallableCache> getCacheMap() {
        return cacheMap;
    }
}
