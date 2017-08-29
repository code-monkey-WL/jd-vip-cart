package com.jd.o2o.vipcart.common.plugins.serializer;


import org.apache.commons.lang.SerializationException;

/**
 * 压缩配置个性化序列化实现类
 * Created by liuhuiqing on 2015/9/17.
 */
public class CustomObjectSerializer extends CompressSerializer<Object> {
    protected boolean enableCompress = false;

    public CustomObjectSerializer() {
    }

    public CustomObjectSerializer(boolean enableCompress) {
        this.enableCompress = enableCompress;
    }

    @Override
    public byte[] serialize(Object obj) throws SerializationException {
        try {
            byte[] retVal = null;
            retVal = this.doSerialize(obj);
            if (retVal == null){
                return null;
            }
            if (enableCompress) {
                retVal = this.compress(retVal);
            }
            return retVal;
        } catch (Exception ex) {
            throw new SerializationException("Serialize object to byte[] exception.", ex);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        byte[] rawValue = bytes;
        if (enableCompress) {
            try {
                rawValue = this.decompress(bytes);
            } catch (Exception ex) {
                rawValue = bytes;
            }
        }
        try {
            return this.doDeserialize(rawValue);
        } catch (Exception ex) {
            throw new SerializationException("Deserialize byte[] to object exception.", ex);
        }
    }

    public void setEnableCompress(boolean enableCompress) {
        this.enableCompress = enableCompress;
    }
}
