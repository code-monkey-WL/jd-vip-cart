package com.jd.o2o.vipcart.common.plugins.serializer;

import org.apache.commons.lang.SerializationException;
import org.springframework.util.Assert;

/**
 * 默认序列化实现:一定调用压缩解压算法，适用于大的缓存key的情况
 * Created by liuhuiqing on 2015/9/14.
 */
public class DefaultObjectSerializer extends CompressSerializer<Object> {
    protected static final int DEFAULT_COMPRESSION_THRESHOLD = 16384;
    protected int compressionThreshold = DEFAULT_COMPRESSION_THRESHOLD;

    public DefaultObjectSerializer() {
    }

    public DefaultObjectSerializer(int compressionThreshold) {
        Assert.isTrue(compressionThreshold > 0, "The compressionThreshold argument must be greater than 0.");
        this.compressionThreshold = compressionThreshold;
    }

    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }

    public byte[] serialize(Object obj)
            throws SerializationException {
        try {
            byte[] retVal = null;
            retVal = doSerialize(obj);
            if (retVal == null) {
                return null;
            }
            if (retVal.length > this.compressionThreshold);
            return compress(retVal);
        } catch (Exception ex) {
            throw new SerializationException("Serialize object to byte[] exception.", ex);
        }
    }

    public Object deserialize(byte[] bytes)
            throws SerializationException {
        byte[] rawValue = null;
        try {
            rawValue = decompress(bytes);
        } catch (Exception ex) {
            rawValue = bytes;
        }
        try {
            return doDeserialize(rawValue);
        } catch (Exception ex) {
            throw new SerializationException("Deserialize byte[] to object exception.", ex);
        }
    }
}
