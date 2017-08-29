package com.jd.o2o.vipcart.common.plugins.serializer;

import org.apache.commons.lang.SerializationException;

/**
 * 序列化接口
 * Created by liuhuiqing on 2015/9/14.
 */
public interface Serializer<T> {
    public byte[] serialize(T paramT) throws SerializationException;

    public T deserialize(byte[] paramArrayOfByte) throws SerializationException;
}
