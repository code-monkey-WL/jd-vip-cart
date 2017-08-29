package com.jd.o2o.vipcart.common.plugins.cache.aspect;

import com.jd.o2o.vipcart.common.plugins.serializer.Serializer;

import java.util.List;
import java.util.Map;

/**
 * 通用缓存服务
 * 优点：简单 通用 效率高
 * 缺点：对象流存储，存储对象属性不能新增或删除
 * Created by liuhuiqing on 2015/8/14.
 */
public interface Cache<T> {

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
     * 按前缀移除
     *
     * @param pattern
     */
    long remove(String pattern);

    /**
     * 获取缓存中以key作为key的元素
     *
     * @param key
     * @return
     */
    <T> T get(Object key);

    /**
     * 批量获取缓存数据
     *
     * @param keys
     * @return
     */
    <P, T> Map<P, T> get(P[] keys);

    /**
     * 批量获取缓存数据
     *
     * @param keys
     * @return
     */
    <T> List<T> getList(Object[] keys);

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
     * 清空缓存
     *
     * @throws Exception
     */
    void clear();

    /**
     * 关闭缓存
     */
    void close();

    /**
     * 获得序列化对象
     *
     * @return
     */
    public Serializer getSerializer();

}
