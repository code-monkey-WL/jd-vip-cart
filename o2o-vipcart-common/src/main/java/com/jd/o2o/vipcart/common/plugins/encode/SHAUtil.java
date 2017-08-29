package com.jd.o2o.vipcart.common.plugins.encode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Created by liuhuiqing on 2014/12/22.
 */
public class SHAUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SHAUtil.class);

    /**
     * SHA加密 生成40位SHA码
     *
     * @param inStr 待加密字符串
     * @return 返回40位SHA码
     * @throws Exception
     */
    public static String encode(String inStr) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            LOGGER.error("字符串[" + inStr + "]加密出错！", e);
        }

        byte[] byteArray = new byte[0];
        try {
            byteArray = inStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("字符串[" + inStr + "]字节编码出错！", e);
        }
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 测试主函数
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        String str = new String("123");
        System.out.println("原始：" + str);
        System.out.println("SHA后：" + encode(str));
    }
}
