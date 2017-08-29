//package com.jd.o2o.vipcart.common.plugins.cache.aspect.impl;
//
//import com.jd.o2o.vipcart.common.plugins.encode.MD5Util;
//import com.jd.o2o.vipcart.common.plugins.serializer.JSerializer;
//import com.jd.o2o.vipcart.common.plugins.serializer.JsonObjectSerializer;
//import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
//import org.apache.commons.lang.ArrayUtils;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.UnsupportedEncodingException;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
///**
// * Json序列化缓存实现
// * Created by liuhuiqing on 2015/8/13.
// */
//public class JsonCache implements JCache<Cluster> {
//    private static final Logger LOGGER = LoggerFactory.getLogger(JimDBCache.class);
//    /**
//     * 缓存名称
//     */
//    private String name = "defaultName";
//    /**
//     * 缓存版本
//     */
//    private String version = "v1-";
//    /**
//     * 京东jimDB缓存客户端
//     */
//    private Cluster client;
//    /**
//     * 屏蔽异常，对缓存抛出的异常进行屏蔽不对外抛出
//     */
//    private boolean maskException = false;
//    /**
//     * 序列化方式
//     */
//    private JSerializer serializer = new JsonObjectSerializer();
//
//
//    @Override
//    public String getName() {
//        return this.name;
//    }
//
//    @Override
//    public Cluster getNativeCache() {
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
//            return client.del(keyArray);
//        } catch (Exception e) {
//            doMaskException(String.format("del缓存key=%s操作出错", key), e);
//            return result;
//        }
//    }
//
//    @Override
//    public long evict(Object[] keys) {
//        if (ArrayUtils.isEmpty(keys)) {
//            return 0;
//        }
//        byte[][] byteKeys = new byte[keys.length][];
//        int index = 0;
//        for (Object key : keys) {
//            byteKeys[index++] = getSerializedKey(key.toString());
//        }
//        try {
//            return client.del(byteKeys);
//        } catch (Exception e) {
//            doMaskException(String.format("批量删除缓存key=%s操作出错", JsonUtils.toJson(keys)), e);
//        }
//        return 0;
//    }
//
//    @Override
//    public String get(Object key) {
//        if (key == null) {
//            return null;
//        }
//        try {
//            byte[] buf = client.get(getSerializedKey(key.toString()));
//            if (buf == null) {
//                return null;
//            }
//            return serializer.deserialize(buf);
//        } catch (Exception e) {
//            doMaskException(String.format("get缓存key=%s出错", key), e);
//        }
//        return null;
//    }
//
//    @Override
//    public <T> T get(Object key, Class<T> target) {
//        T returnValue = null;
//        if (key == null) {
//            return returnValue;
//        }
//        try {
//            byte[] buf = client.get(getSerializedKey(key.toString()));
//            if (buf == null) {
//                return null;
//            }
//            return serializer.deserialize(buf, target);
//        } catch (Exception e) {
//            doMaskException(String.format("get缓存key=%s出错", key), e);
//        }
//        return returnValue;
//    }
//
//    @Override
//    public <P, T> Map<P, T> get(P[] keys, Class<T> target) {
//        if (ArrayUtils.isEmpty(keys)) {
//            return null;
//        }
//        final Map<P, T> map = new LinkedHashMap<P, T>();
//        for (P key : keys) {
//            Object v = this.get(key, target);
//            map.put(key, v == null ? null : (T) v);
//        }
//        return map;
//    }
//
//    @Override
//    public <T> List<T> list(Object key, Class target) {
//        List<T> returnValue = null;
//        if (key == null) {
//            return returnValue;
//        }
//        try {
//            byte[] buf = client.get(getSerializedKey(key.toString()));
//            if (buf == null) {
//                return null;
//            }
//            return serializer.deserializeCollection(buf, target, List.class);
//        } catch (Exception e) {
//            doMaskException(String.format("list缓存key=%s出错", key), e);
//        }
//        return returnValue;
//    }
//
//    @Override
//    public <C extends Collection> C collection(Object key, Class target, Class collectionClazz) {
//        if (key == null) {
//            return null;
//        }
//        try {
//            byte[] buf = client.get(getSerializedKey(key.toString()));
//            if (buf == null) {
//                return null;
//            }
//            return serializer.deserializeCollection(buf, target, collectionClazz);
//        } catch (Exception e) {
//            doMaskException(String.format("list缓存key=%s出错", key), e);
//        }
//        return null;
//    }
//
//    @Override
//    public boolean put(Object key, Object value, int seconds) {
//        if (key == null) {
//            throw new IllegalArgumentException("缓存key不能为空");
//        }
//        if (value == null) {
//            throw new IllegalArgumentException("缓存value不能为空");
//        }
//        boolean result = false;
//        try {
//            client.setEx(getSerializedKey(key.toString()), serializer.serialize(value), seconds, TimeUnit.SECONDS);
//            result = true;
//        } catch (Exception e) {
//            doMaskException(String.format("set缓存key=%s出错", key), e);
//        }
//        return result;
//    }
//
//    @Override
//    public boolean put(Object[] keys, Object[] values, int seconds) {
//        if (ArrayUtils.isEmpty(keys) || ArrayUtils.isEmpty(values)) {
//            throw new IllegalArgumentException("缓存keys或values不能为空");
//        }
//        if (keys.length != values.length) {
//            throw new IllegalArgumentException("keys与values长度不一致");
//        }
//        boolean r = false;
//        for (int i = 0; i < keys.length; i++) {
//            r = this.put(keys[i], values[i], seconds);
//        }
//        return r;
//    }
//
//    @Override
//    public boolean put(Map<Object, Object> tuplesMap) {
//        if (tuplesMap == null || tuplesMap.isEmpty()) {
//            throw new IllegalArgumentException("缓存设置入参不能为空");
//        }
//        Map<byte[], byte[]> btMap = new HashMap<byte[], byte[]>();
//        try {
//            for (Map.Entry<Object, Object> entry : tuplesMap.entrySet()) {
//                byte[] k = getSerializedKey(entry.getKey().toString());
//                byte[] v = serializer.serialize(entry.getValue());
//                btMap.put(k, v);
//            }
//            client.mSet(btMap);
//            return true;
//        } catch (Exception e) {
//            doMaskException(String.format("调用缓存put服务器操作出错tuplesMap=%s", JsonUtils.toJson(tuplesMap)), e);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean put(Object key, Object value, Date expireDate) {
//        if (key == null) {
//            throw new IllegalArgumentException("缓存key不能为空");
//        }
//        if (value == null) {
//            throw new IllegalArgumentException("缓存value不能为空");
//        }
//        boolean result = false;
//        try {
//            long expireTime = expireDate.getTime()-new Date().getTime();
//            client.setEx(getSerializedKey(key.toString()), serializer.serialize(value), expireTime, TimeUnit.MILLISECONDS);
//            result = true;
//        } catch (Exception e) {
//            doMaskException(String.format("set缓存key=%s出错", key), e);
//        }
//        return result;
//    }
//
//    @Override
//    public long incrBy(Object key, long num) {
//        return client.incrBy(getSerializedKey(key.toString()), num);
//    }
//
//    @Override
//    public long incrBy(Object key, long num, Date expireDate) {
//        long returnValue = client.incrBy(getSerializedKey(key.toString()), num);
//        if (returnValue == num) {
//            client.expireAt(getSerializedKey(key.toString()), expireDate);
//        }
//        return returnValue;
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
//            doMaskException(String.format("序列化key=%s出现错误", key), e);
//        }
//        return null;
//    }
//
//    /**
//     * 获取缓存key 自动添加缓存版本前缀
//     *
//     * @param key
//     * @return
//     */
//    private String getKey(String key) {
//        if (key.length() > 1024) {
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
//     *
//     * @param e 异常
//     */
//    private void doMaskException(String msg, Throwable e) {
//        String errMsg = String.format("缓存服务出现异常：%s", msg);
//        LOGGER.error(errMsg, e);
//        if (!this.maskException) {
//            throw new RuntimeException(errMsg, e);
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
//    public Cluster getClient() {
//        return client;
//    }
//
//    public void setClient(Cluster client) {
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
//
//    public JSerializer getSerializer() {
//        return serializer;
//    }
//
//    public void setSerializer(JSerializer serializer) {
//        this.serializer = serializer;
//    }
//}
