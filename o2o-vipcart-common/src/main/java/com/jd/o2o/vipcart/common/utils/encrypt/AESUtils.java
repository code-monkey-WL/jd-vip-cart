package com.jd.o2o.vipcart.common.utils.encrypt;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;


/**
 * AES加密解密工具包(对称加密算法，实现代价小)
 */
public class AESUtils {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;
    private static final String CHARSET = "UTF-8";

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     */
    public static byte[] encrypt(String content, String password) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes());
        keyGenerator.init(KEY_SIZE, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器
        byte[] byteContent = content.getBytes(CHARSET);
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(byteContent);
        return result; // 加密
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes());
        keyGenerator.init(KEY_SIZE, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(content);
        return result; // 加密
    }

    public static void main(String[] args) throws Exception {
        String key = "123456";
        String content = "syncAction=syncLogin&loginName=wuqm";
        byte[] encryptByte = encrypt(content, key);
        String encryptString = new String(encryptByte, "UTF-8");
        String encryptBase32String = new Base32().encodeAsString(encryptByte);
        String decryptString = new String(decrypt(encryptByte, key));
        String decryptBase32String = new String(AESUtils.decrypt(new Base32().decode(encryptBase32String), key));
        System.out.println("encryptString = " + encryptString);
        System.out.println("encryptBase32String = " + encryptBase32String);
        System.out.println("decryptString = " + decryptString);
        System.out.println("decryptBase32String = " + decryptBase32String);
        String code = "E65GOSOJVJCV3XRVZ33VJJRGFOCIK6KKLZGLSE3KHJ3WW5HD2NQ7M3L3IBFCF5X24HDHLWDPQKRTS===";
        code = "S7UKMT5NQEUCDHQAJIEP5FPN5M======";
        key = "JOSL_USER_NAME";
        String paramURL = new String(AESUtils.decrypt(new Base32().decode(code), key));
        System.out.println("paramURL = " + paramURL);
        //System.out.println("encode paramURL = " + URLUtils.encode(code));

//        String urlParams = "syncAction=syncLogin&loginName=wuqm";
//        String appToken = key;
//        String url = null;
//        try {
//            url = URLUtils.addParameter("http://192.168.192.199:8082/o2o-store-web/o2o/privilege/appSync", "code", new Base32().encodeAsString(AESUtils.encrypt(urlParams, appToken)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("url = " + url);
    }

}
