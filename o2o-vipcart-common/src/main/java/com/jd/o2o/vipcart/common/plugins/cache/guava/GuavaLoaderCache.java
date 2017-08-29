package com.jd.o2o.vipcart.common.plugins.cache.guava;

import com.google.common.cache.*;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
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
public abstract class GuavaLoaderCache<K, V> extends AbstractGuavaCache<K, V>{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static final Map<String, GuavaLoaderCache> cacheMap = new ConcurrentHashMap<String, GuavaLoaderCache>();

    /**
     * 获得缓存对象
     * @return cache
     */
    @Override
    public LoadingCache<K, V> getCache() {
        if(cache == null){  //使用双重校验锁保证只有一个cache实例
            synchronized (this) {
                if(cache == null){
                    cache = getLoaderCache(new CacheLoader<K, V>() {
                        @Override
                        public V load(K key) throws Exception {
                            return fetchData(key);
                        }

                        @Override
                        public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception {
                            return fetchData(keys);
                        }
                    });
                }
            }
        }
        return (LoadingCache<K, V>) cache;
    }

    /**
     * 根据key从数据库或其他数据源中获取一个value，并被自动保存到缓存中。
     * @param key
     * @return value,连同key一起被加载到缓存中的。
     */
    protected abstract V fetchData(K key);

    /**
     * 批量获取数据方法默认实现，最好在实现类中重写此方法
     * @param keys
     * @return
     */
    protected Map<K, V> fetchData(Iterable<? extends K> keys){
        Map<K,V> map = new HashMap<K, V>();
        for(K k:keys){
            map.put(k,fetchData(k));
        }
        return map;
    }

    /**
     * 从缓存中获取数据，并处理异常
     * @param key
     * @return Value
     */
    public V fromCache(K key) {
        V result = null;
        try {
            result = getCache().get(key);
        } catch (ExecutionException e) {
            LOGGER.error(String.format("key=[%s]调用本地缓存失败",key),e);
        }
        return result;
    }

    /**
     * 批量获取
     * @param keys
     * @return
     */
    public Map<K, V> fromCache(Iterable<? extends K> keys) {
        Map<K, V> result = null;
        try {
            result = getCache().getAll(keys);
        } catch (ExecutionException e) {
            LOGGER.error(String.format("keys=[%s]调用本地缓存失败", JsonUtils.toJson(keys)),e);
        }
        return result;
    }

    /**
     * 调用缓存对象引用
     *
     * @param key
     * @return
     */
    @Override
    public GuavaLoaderCache<K,V> named(String key) {
        cacheMap.put(key, this);
        return this;
    }

    public static Map<String, GuavaLoaderCache> getCacheMap() {
        return cacheMap;
    }
}
