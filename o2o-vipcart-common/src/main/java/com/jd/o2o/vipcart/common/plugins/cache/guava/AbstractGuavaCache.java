package com.jd.o2o.vipcart.common.plugins.cache.guava;

import com.google.common.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 抽象Guava基础信息缓存类
 *
 * @param <K> key 类型
 * @param <V> value 类型
 *            Created by liuhuiqing on 2015/7/29.
 */
public abstract class AbstractGuavaCache<K, V> {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected Cache<K, V> cache;

    //用于初始化cache的参数及其缺省值
    protected int maximumSize = 10000;                 //最大缓存条数，子类在构造方法中调用setMaximumSize(int size)来更改
    protected int expireAfterWriteDuration = 2 * 60;      //数据存在时长，子类在构造方法中调用setExpireAfterWriteDuration(int duration)来更改
    protected TimeUnit timeUnit = TimeUnit.MINUTES;   //时间单位（分钟）
    protected Date resetTime;     //Cache初始化或被重置的时间

    /**
     * 构造cacheLoader方法缓存对象
     *
     * @return cache
     */
    protected LoadingCache<K, V> getLoaderCache(CacheLoader<K, V> cacheLoader) {
        LoadingCache cache = CacheBuilder.newBuilder().maximumSize(maximumSize)      //缓存数据的最大条目，也可以使用.maximumWeight(weight)代替
                .expireAfterWrite(expireAfterWriteDuration, timeUnit)   //数据被创建多久后被移除
                .recordStats()                                          //启用统计
                .removalListener(new RemovalListener<K, V>() {
                    @Override
                    public void onRemoval(RemovalNotification<K, V> rn) {
                        System.out.println(rn.getKey() + "被移除");

                    }
                })
                .build(cacheLoader);
        this.resetTime = new Date();
        LOGGER.debug("本地缓存{}初始化成功", this.getClass().getSimpleName());
        return cache;
    }

    /**
     * 构造callable callback方式缓存对象
     *
     * @return cache
     */
    protected Cache<K, V> getCallableCache() {
        Cache cache = CacheBuilder.newBuilder().maximumSize(maximumSize)      //缓存数据的最大条目，也可以使用.maximumWeight(weight)代替
                .expireAfterWrite(expireAfterWriteDuration, timeUnit)   //数据被创建多久后被移除
                .recordStats()                                          //启用统计
                .removalListener(new RemovalListener<K, V>() {
                    @Override
                    public void onRemoval(RemovalNotification<K, V> rn) {
                        System.out.println(rn.getKey() + "被移除");

                    }
                })
                .build();
        this.resetTime = new Date();
        LOGGER.debug("本地缓存{}初始化成功", this.getClass().getSimpleName());
        return cache;
    }

    /**
     * 管理cache实体对象
     * @param key cache对象名称
     * @param <T>
     * @return
     */
    public abstract <T extends AbstractGuavaCache<K,V>> T named(String key);

    public Date getResetTime() {
        return resetTime;
    }

    public void setResetTime(Date resetTime) {
        this.resetTime = resetTime;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public int getExpireAfterWriteDuration() {
        return expireAfterWriteDuration;
    }

    /**
     * 设置最大缓存条数
     *
     * @param maximumSize
     */
    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    /**
     * 设置数据存在时长（分钟）
     *
     * @param expireAfterWriteDuration
     */
    public void setExpireAfterWriteDuration(int expireAfterWriteDuration) {
        this.expireAfterWriteDuration = expireAfterWriteDuration;
    }

    public Cache<K, V> getCache() {
        return cache;
    }
}
