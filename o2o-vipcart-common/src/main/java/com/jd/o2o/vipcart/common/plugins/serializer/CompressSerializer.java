package com.jd.o2o.vipcart.common.plugins.serializer;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 序列化对象压缩解压模板方法类
 * Created by liuhuiqing on 2015/9/17.
 */
public abstract class CompressSerializer<T> implements Serializer<T> {

    protected byte[] doSerialize(Object o) {
        if (o == null) {
            return null;
        }
        byte[] rv = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        try {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(o);
            os.close();
            bos.close();
            rv = bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("Non-serializable object.", e);
        } finally {
            try {
                if (os != null)
                    os.close();
            } catch (IOException ioe) {
            }
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException ioe) {
            }
        }
        return rv;
    }

    protected Object doDeserialize(byte[] in) {
        if (in == null) {
            return null;
        }
        Object rv = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try {
            bis = new ByteArrayInputStream(in);
            is = new ObjectInputStream(bis);
            rv = is.readObject();
            is.close();
            bis.close();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't-deserialize object", e);
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException ioe) {
            }
            try {
                if (bis != null)
                    bis.close();
            } catch (IOException ioe) {
            }
        }
        return rv;
    }

    protected byte[] compress(byte[] in) {
        if (in == null) {
            return null;
        }
        byte[] rv = null;
        ByteArrayOutputStream bos = null;
        GZIPOutputStream gz = null;
        try {
            bos = new ByteArrayOutputStream();
            gz = new GZIPOutputStream(bos);
            gz.write(in);
            gz.close();
            bos.close();
            rv = bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("IO exception compressing data.", e);
        } finally {
            try {
                if (gz != null)
                    gz.close();
            } catch (IOException ioe) {
            }
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException ioe) {
            }
        }
        return rv;
    }

    protected byte[] decompress(byte[] in) {
        if (in == null) {
            return null;
        }
        byte[] rv = null;
        ByteArrayOutputStream bos = null;
        ByteArrayInputStream bis = null;
        GZIPInputStream gis = null;
        try {
            bis = new ByteArrayInputStream(in);
            gis = new GZIPInputStream(bis);
            bos = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int r = -1;
            while ((r = gis.read(buf)) > 0) {
                bos.write(buf, 0, r);
            }
            bos.close();
            rv = bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("IO exception decompress data.", e);
        } finally {
            try {
                if (gis != null)
                    gis.close();
            } catch (IOException ioe) {
            }
            try {
                if (bis != null)
                    bis.close();
            } catch (IOException ioe) {
            }
            try {
                if (bos != null)
                    bos.close();
            } catch (IOException ioe) {
            }
        }
        return rv;
    }
}
