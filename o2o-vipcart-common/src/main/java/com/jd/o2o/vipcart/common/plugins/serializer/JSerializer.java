package com.jd.o2o.vipcart.common.plugins.serializer;

import org.apache.commons.lang.SerializationException;

import java.util.Collection;

/**
 * 序列化接口
 * Created by liuhuiqing on 2017/7/20.
 */
public interface JSerializer {
    public byte[] serialize(Object paramT) throws SerializationException;

    public String deserialize(byte[] paramArrayOfByte) throws SerializationException;

    public <T> T deserialize(byte[] paramArrayOfByte, Class<T> target) throws SerializationException;

    public <C extends Collection> C deserializeCollection(byte[] paramArrayOfByte, Class target, Class collectionClazz) throws SerializationException;
}
