//package com.jd.o2o.vipcart.common.plugins.cache.aspect.impl;
//
//import com.jd.o2o.vipcart.common.plugins.encode.MD5Util;
//import com.jd.o2o.vipcart.common.plugins.serializer.DefaultObjectSerializer;
//import com.jd.o2o.vipcart.common.plugins.serializer.Serializer;
//import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
//import org.apache.commons.lang.ArrayUtils;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.*;
//import java.util.*;
//
///**
// * redis缓存
// * Created by liuhuiqing on 2015/8/13.
// */
//public class RedisCache implements Cache<ShardedXCommands> {
//    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCache.class);
//    /**
//     * 缓存名称
//     */
//    private String name = "defaultName";
//    /**
//     * 缓存版本
//     */
//    private String version = "v1-";
//    /**
//     * 京东缓存客户端
//     */
//    private ShardedXCommands client;
//    /**
//     * 一次批量操作条数
//     */
//    private int batchSize = 1000;
//    /**
//     * 屏蔽异常，对缓存抛出的异常进行屏蔽不对外抛出
//     */
//    private boolean maskException = false;
//    /**
//     * 序列化方式
//     */
//    private Serializer<Object> serializer = new DefaultObjectSerializer();
//
//    @Override
//    public String getName() {
//        return this.name;
//    }
//
//    @Override
//    public ShardedXCommands getNativeCache() {
//        return client;
//    }
//
//    @Override
//    public long evict(Object key) {
//        int result = 0;
//        if (key == null) {
//            return result;
//        }
//        try {
//            final byte[] keyArray = getSerializedKey(key.toString());
//            return client.execute(keyArray, new JedisOperation<Long>() {
//                @Override
//                public Long call(Jedis jedis) {
//                    return jedis.del(keyArray);
//                }
//            });
//        } catch (Exception e) {
//            doMaskException(String.format("del缓存key=%s操作出错", key),e);
//            return result;
//        }
//    }
//
//    @Override
//    public long evict(Object[] keys) {
//        if (ArrayUtils.isEmpty(keys)) {
//            return 0;
//        }
//        int length = keys.length;
//        byte[][] byteKeys = new byte[length][];
//        int index = 0;
//        for (Object key : keys) {
//            byteKeys[index++] = getSerializedKey(key.toString());
//        }
//        try {
//            client.execute(byteKeys, new JedisMultiKeysOperation<byte[]>() {
//                public void call(Jedis jedis, List<byte[]> ownedKeys) throws Throwable {
//                    Pipeline pipeline = jedis.pipelined();
//                    int index = 0;
//                    for (byte[] ownedKey : ownedKeys) {
//                        pipeline.del(ownedKey);
//                        if (++index % batchSize == 0) {       //批次大小为1000条
//                            pipeline.sync();
//                        }
//                    }
//                    pipeline.sync();
//                }
//            });
//            return length;
//        } catch (Exception e) {
//            doMaskException(String.format("批量删除缓存key=%s操作出错", JsonUtils.toJson(keys)), e);
//        }
//        return 0;
//    }
//
//    @Override
//    public long remove(String pattern) {
//        try {
//            final byte[] keyArray = getSerializedKey(pattern.toString());
//            client.execute(new JedisOperation<Void>() {
//                @Override
//                public Void call(Jedis jedis) throws Throwable {
//                    Set<byte[]> keys = jedis.keys(keyArray);
//                    if (keys != null && keys.size() > 0) {
//                        jedis.del(keys.toArray(new byte[0][0]));
//                    }
//                    return null;
//                }
//
//            });
//            return 1;
//        } catch (Exception e) {
//            doMaskException(String.format("模糊del缓存key=%s操作出错", pattern), e);
//        }
//        return 0;
//    }
//
//    @Override
//    public <T> T get(Object key) {
//        T returnValue = null;
//        if (key == null) {
//            return returnValue;
//        }
//        try {
//            byte[] buf = client.get(getSerializedKey(key.toString()));
//            if (buf == null) {
//                return null;
//            }
//            return (T) serializer.deserialize(buf);
//        } catch (Exception e) {
//            doMaskException(String.format("get缓存key=%s出错", key), e);
//        }
//        return returnValue;
//    }
//
//    @Override
//    public <P,T> Map<P,T> get(P[] keys){
//        if (ArrayUtils.isEmpty(keys)) {
//            return null;
//        }
//        byte[][] byteKeys = new byte[keys.length][];
//        int index = 0;
//        for (Object key : keys) {
//            byteKeys[index++] = getSerializedKey(key.toString());
//        }
//        final Map<P,T> map = new LinkedHashMap<P, T>();
//        try {
//            final P[] tempKeys = keys;
//            client.execute(byteKeys, new JedisMultiKeysOperation<byte[]>() {
//                int count = 0;
//                @Override
//                public void call(Jedis jedis, List<byte[]> ownedKeys) throws Throwable {
//                    Pipeline pipeline = jedis.pipelined();
//                    int index = 0;
//                    for (byte[] ownedKey : ownedKeys) {
//                        T value = (T) serializer.deserialize(pipeline.get(ownedKey).get());
//                        map.put(tempKeys[count++],value);
//                        if (++index % batchSize == 0) {       //批次大小为1000条
//                            pipeline.sync();
//                        }
//                    }
//                    pipeline.sync();
//                }
//            });
//        } catch (Exception e) {
//            doMaskException(String.format("批量get缓存keys=%s操作出错", JsonUtils.toJson(keys)), e);
//            if(map == null || map.isEmpty()){
//                return null;
//            }
//        }
//        return map;
//    }
//
//    @Override
//    public <T> List<T> getList(Object[] keys){
//        Map<Object,T> map = this.get(keys);
//        if(map==null){
//            return null;
//        }
//        return new ArrayList<T>(map.values());
//    }
//
//    @Override
//    public boolean put(Object key, Object value, int seconds) {
//        if(key == null){
//            throw new IllegalArgumentException("缓存key不能为空");
//        }
//        if(value == null){
//            throw new IllegalArgumentException("缓存value不能为空");
//        }
//        boolean result = false;
//        try {
//            String redisResult = client.setex(getSerializedKey(key.toString()), seconds, serializer.serialize(value));
//            result = StringUtils.equals("OK", redisResult);
//        } catch (Exception e) {
//            doMaskException(String.format("set缓存key=%s出错", key), e);
//        }
//        return result;
//    }
//
//    @Override
//    public boolean put(Object[] keys, Object[] values, final int seconds) {
//        if (ArrayUtils.isEmpty(keys) || ArrayUtils.isEmpty(values)) {
//            throw new IllegalArgumentException("缓存keys或values不能为空");
//        }
//        if (keys.length != values.length) {
//            throw new IllegalArgumentException("keys与values长度不一致");
//        }
//        final Map<byte[], Object> cacheItems = new HashMap<byte[], Object>();
//        byte[][] byteKeys = new byte[keys.length][];
//        int index = 0;
//        for (Object key : keys) {
//            byte[] byteKey = getSerializedKey(key.toString());
//            byteKeys[index] = byteKey;
//            cacheItems.put(byteKey, values[index]);
//            index++;
//        }
//        try {
//            client.execute(byteKeys, new JedisMultiKeysOperation<byte[]>() {
//                public void call(Jedis jedis, List<byte[]> ownedKeys) throws Throwable {
//                    Pipeline pipeline = jedis.pipelined();
//                    int index = 0;
//                    for (byte[] ownedKey : ownedKeys) {
//                        byte[] ownedValue = serializer.serialize(cacheItems.get(ownedKey));
//                        pipeline.setex(ownedKey, seconds, ownedValue);
//                        if (++index % batchSize == 0) {       //一批插入1000条
//                            pipeline.sync();
//                        }
//                    }
//                    pipeline.sync();
//                }
//            });
//            return true;
//        } catch (Exception e) {
//            doMaskException(String.format("sets缓存key=%s出错", JsonUtils.toJson(keys)), e);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean put(Map<Object,Object> tuplesMap){
//        if(tuplesMap == null && tuplesMap.isEmpty()){
//            throw new IllegalArgumentException("设置缓存入参不能为空");
//        }
//        return this.put(tuplesMap.keySet().toArray(),tuplesMap.values().toArray(),Integer.MAX_VALUE);
//    }
//
//    @Override
//    public void clear() {
//        client.execute(new JedisOperation<Void>() {
//            @Override
//            public Void call(Jedis jedis) {
//                jedis.flushAll();
//                return null;
//            }
//        });
//    }
//
//    @Override
//    public void close() {
//        client.destroy();
//    }
//
//    /**
//     * 获取序列化以后的缓存key
//     *
//     * @param key
//     * @return
//     */
//    private byte[] getSerializedKey(String key) {
//        try {
//            return getKey(key).getBytes("utf-8");
//        } catch (UnsupportedEncodingException e) {
//            doMaskException(String.format("序列化key=%s出现错误",key),e);
//        }
//        return null;
//    }
//    /**
//     * 获取缓存key 自动添加缓存版本前缀
//     *
//     * @param key
//     * @return
//     */
//    private String getKey(String key) {
//        if(key.length() > 1024){
//            key = MD5Util.encode(key);
//        }
//        if (StringUtils.isNotEmpty(version)) {
//            key = new StringBuilder().append(version).append(key).toString();
//        }
//        return key;
//    }
//
//    /**
//     * 屏蔽异常处理
//     * @param e 异常
//     */
//    private void doMaskException(String msg, Throwable e) {
//        String errMsg = String.format("缓存服务出现异常：%s",msg);
//        LOGGER.error(errMsg,e);
//        if (!this.maskException) {
//            throw new RuntimeException(errMsg,e);
//        }
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getVersion() {
//        return version;
//    }
//
//    public void setVersion(String version) {
//        this.version = version;
//    }
//
//    public ShardedXCommands getClient() {
//        return client;
//    }
//
//    public void setClient(ShardedXCommands client) {
//        this.client = client;
//    }
//
//    public boolean isMaskException() {
//        return maskException;
//    }
//
//    public void setMaskException(boolean maskException) {
//        this.maskException = maskException;
//    }
//    @Override
//    public Serializer<Object> getSerializer() {
//        return serializer;
//    }
//
//    public void setSerializer(Serializer<Object> serializer) {
//        this.serializer = serializer;
//    }
//}
