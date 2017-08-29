package com.jd.o2o.vipcart.common.plugins.cache.aspect;

import com.jd.o2o.vipcart.common.plugins.serializer.JSerializer;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 基于Json序列化实现缓存服务
 * 优点：跨语言，属性动态变更后序列化也不会有问题
 * 缺点：通用性不佳，复杂对象类型存储反序列化支持的不够好，执行效率低
 * Created by liuhuiqing on 2017/7/20.
 */
public interface JCache<T> {
    /**
     * 获得缓存名称
     */
    String getName();

    /**
     * 获得缓存客户端.
     */
    T getNativeCache();

    /**
     * 移除缓存中以key作为标识的元素
     *
     * @param key
     * @return 移除条数成功
     */
    long evict(Object key);

    /**
     * 一次性批量移除缓存中以key作为标识的元素
     *
     * @param keys
     */
    long evict(Object[] keys);

    /**
     * 获取缓存中以key作为key的元素Json字符串
     *
     * @param key
     * @return
     */
    String get(Object key);

    /**
     * 获取缓存中以key作为key的元素
     *
     * @param key
     * @param target 目标类型
     * @param <T>
     * @return
     */
    <T> T get(Object key, Class<T> target);

    /**
     * 批量获取缓存数据
     *
     * @param keys
     * @return
     */
    <P, T> Map<P, T> get(P[] keys, Class<T> target);

    /**
     * 获取集合对象缓存数据
     *
     * @param key
     * @return
     */
    <T> List<T> list(Object key, Class target);

    /**
     * 获取集合对象缓存数据
     *
     * @param key
     * @param target
     * @param collectionClazz
     * @param <C>
     * @return
     */
    <C extends Collection> C collection(Object key, Class target, Class collectionClazz);

    /**
     * 将obj放入缓存，并以key作为标志它的key
     *
     * @param key
     * @param value
     * @param seconds 保存时间（秒），0标识立即失效，-1标识永不失效
     * @return true-成功， false-失败
     */
    boolean put(Object key, Object value, int seconds);

    /**
     * 一次性存入多个缓存
     *
     * @param keys
     * @param values
     * @param seconds 保存时间（秒），0标识立即失效，-1标识永不失效
     */
    boolean put(Object[] keys, Object[] values, int seconds);

    /**
     * 批量存入key:value键值对，永不过期
     *
     * @param tuplesMap
     * @return
     */
    boolean put(Map<Object, Object> tuplesMap);

    /**
     * 将obj放入缓存，并以key作为标志它的key
     * @param key
     * @param value
     * @param expireDate 过期日期
     * @return
     */
    boolean put(Object key, Object value, Date expireDate);

    /**
     * 计数器
     *
     * @param key
     * @param num
     * @return
     */
    long incrBy(Object key, long num);

    /**
     * 计数器
     *
     * @param key
     * @param num
     * @param expireDate 过期时间
     * @return
     */
    long incrBy(Object key, long num, Date expireDate);

    /**
     * 获得序列化对象
     *
     * @return
     */
    JSerializer getSerializer();

}
