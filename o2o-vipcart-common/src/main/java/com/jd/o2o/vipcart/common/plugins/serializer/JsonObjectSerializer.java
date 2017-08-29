package com.jd.o2o.vipcart.common.plugins.serializer;

import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.apache.commons.lang.SerializationException;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * json序列化
 * Created by liuhuiqing on 2017/7/20.
 */
public class JsonObjectSerializer implements JSerializer {
    @Override
    public byte[] serialize(Object paramT) throws SerializationException {
        if (paramT == null) {
            return null;
        }
        return toBytes(JsonUtils.toJson(paramT));
    }

    @Override
    public String deserialize(byte[] paramArrayOfByte) throws SerializationException {
        return bytesToString(paramArrayOfByte);
    }

    @Override
    public <T> T deserialize(byte[] paramArrayOfByte, Class<T> target) throws SerializationException {
        return JsonUtils.fromJson(bytesToString(paramArrayOfByte), target);
    }

    @Override
    public <C extends Collection> C deserializeCollection(byte[] paramArrayOfByte, Class target, Class collectionClazz) throws SerializationException {
        return JsonUtils.fromJsonArrayBy(bytesToString(paramArrayOfByte),target,collectionClazz);
    }

    private byte[] toBytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException(String.format("序列化key=%s to byte[]出现错误", str), e);
        }
    }

    private String bytesToString(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("序列化byte[] to String出现错误", e);
        }
    }

}
