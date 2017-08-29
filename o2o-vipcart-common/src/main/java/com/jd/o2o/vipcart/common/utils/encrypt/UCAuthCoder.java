package com.jd.o2o.vipcart.common.utils.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * UCenter认证加解密帮助类
 */
public class UCAuthCoder {
    public static final String OPERATION_ENCODE = "ENCODE";
    public static final String OPERATION_DECODE = "DECODE";
    private static final String DEFAULT_KEY = "12345qwert";

    /**
     *  加解密
     * @param content   内容
     * @param operation 操作方式 ENCODE/DECODE
     * @param key 密钥
     * @param expiry		密文有效期, 加密时候有效， 单位 秒，0 为永久有效
     *
     * @example
     *
     * 	$a = coder('abc', 'ENCODE', 'key');
     * 	$b = coder($a, 'DECODE', 'key');  // $b(abc)
     *
     * 	$a = coder('abc', 'ENCODE', 'key', 3600);
     * 	$b = coder('abc', 'DECODE', 'key'); // 在一个小时内，$b(abc)，否则 $b 为空
     * @return
     */
    public String coder(String content, String operation, String key, int expiry) {
        if (operation.equals(OPERATION_DECODE)){
            content = decodeBase64Param(content);
        }

        int ckey_length = 4;    //note 随机密钥长度 取值 0-32;
        //note 加入随机密钥，可以令密文无任何规律，即便是原文和密钥完全相同，加密结果也会每次不同，增大破解难度。
        //note 取值越大，密文变动规律越大，密文变化 = 16 的 ckey_length 次方
        //note 当此值为 0 时，则不产生随机密钥

        key = md5(key != null ? key : DEFAULT_KEY);
        String keya = md5(subStr(key, 0, 16));
        String keyb = md5(subStr(key, 16, 16));
        String keyc = ckey_length > 0 ? (operation.equals(OPERATION_DECODE) ? subStr(content, 0, ckey_length) : subStr(md5(microTime()), -ckey_length)) : "";

        String cryptkey = keya + md5(keya + keyc);
        int key_length = cryptkey.length();

        content = operation.equals(OPERATION_DECODE) ? base64Decode(subStr(content, ckey_length)) : sprintf("%010d", expiry > 0 ? expiry + time() : 0) + subStr(md5(content + keyb), 0, 16) + content;
        int string_length = content.length();

        StringBuffer result1 = new StringBuffer();

        int[] box = new int[256];
        for (int i = 0; i < 256; i++) {
            box[i] = i;
        }

        int[] rndkey = new int[256];
        for (int i = 0; i <= 255; i++) {
            rndkey[i] = (int) cryptkey.charAt(i % key_length);
        }

        int j = 0;
        for (int i = 0; i < 256; i++) {
            j = (j + box[i] + rndkey[i]) % 256;
            int tmp = box[i];
            box[i] = box[j];
            box[j] = tmp;
        }

        j = 0;
        int a = 0;
        for (int i = 0; i < string_length; i++) {
            a = (a + 1) % 256;
            j = (j + box[a]) % 256;
            int tmp = box[a];
            box[a] = box[j];
            box[j] = tmp;

            result1.append((char) (((int) content.charAt(i)) ^ (box[(box[a] + box[j]) % 256])));

        }

        if (operation.equals(OPERATION_DECODE)) {
            String result = result1.substring(0, result1.length());
            if ((Integer.parseInt(subStr(result.toString(), 0, 10)) == 0 || Long.parseLong(subStr(result.toString(), 0, 10)) - time() > 0) && subStr(result.toString(), 10, 16).equals(subStr(md5(subStr(result.toString(), 26) + keyb), 0, 16))) {
                return subStr(result.toString(), 26);
            } else {
                return "";
            }
        } else {
            return encodeBase64Param(keyc + base64Encode(result1.toString()).replaceAll("=", ""));
        }
    }

    private String encodeBase64Param(String base64Param) {
        if (base64Param == null || base64Param.trim().length() == 0) {
            return base64Param;
        }
        return base64Param.replace("+", "*").replace("/", "-").replace("=", ".");
    }

    private String decodeBase64Param(String base64Param) {
        if (base64Param == null || base64Param.trim().length() == 0) {
            return base64Param;
        }
        return base64Param.replace("*", "+").replace("-", "/").replace(".", "=");
    }

    private String base64Encode(String input) {
        try {
            return new String(Base64.encode(input.getBytes("iso-8859-1")));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String base64Decode(String input) {
        try {
            return new String(Base64.decode(input.toCharArray()),"iso-8859-1");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String md5(String input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return byte2Hex(md.digest(input.getBytes()));
    }

    private String md5(long input) {
        return md5(String.valueOf(input));
    }

    private String byte2Hex(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString();
    }


    private String subStr(String input, int begin, int length) {
        return input.substring(begin, begin + length);
    }

    private String subStr(String input, int begin) {
        if (begin > 0) {
            return input.substring(begin);
        } else {
            return input.substring(input.length() + begin);
        }
    }

    private long microTime() {
        return System.currentTimeMillis();
    }

    private long time() {
        return System.currentTimeMillis() / 1000;
    }

    private String sprintf(String format, long input) {
        String temp = "0000000000" + input;
        return temp.substring(temp.length() - 10);
    }

    public static void main(String[] args) {
        String code = "0b60CbDhFwRgod3WEgHN4IBvIZ35sp1cjhykQylxpeGpXwNf6TZfYTXBF9pdR9US9CJqG*pv0Zo7kJcSOeFDuUs";
        System.out.println(new UCAuthCoder().coder(code, OPERATION_DECODE, "123456", 0));
        String param = "syncAction=syncLogin&loginName=zjjie";
        String encodeString = new UCAuthCoder().coder(param, OPERATION_ENCODE, "123456", 0);
        System.out.println("encodeString = " + encodeString);
        String decodeString = new UCAuthCoder().coder(encodeString, OPERATION_DECODE, "123456", 0);
        System.out.println("decodeString = " + decodeString);
    }
}
