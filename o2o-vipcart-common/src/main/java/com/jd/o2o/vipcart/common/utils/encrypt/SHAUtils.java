package com.jd.o2o.vipcart.common.utils.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>文件名称: SHAUtils.java</p>
 * <p>文件描述: 安全散列算法 SHA (Secure Hash Algorithm)
 * <p>
 * 是美国国家标准和技术局发布的
 * 国家标准FIPS PUB 180-1，一般称为SHA-1。<br>
 * 其对长度不超过264二进制位的消息产生160位的消息摘要输出。<br>
 * SHA是一种数据加密算法，该算法经过加密专家多年来的发展和改进已日益完善，<br>
 * 现在已成为公认的最安全的散列算法之一，并被广泛使用。<br>
 * 该加密算法是单向加密，即加密的数据不能再通过解密还原。<br>
 */

public final class SHAUtils {
	private static final String SHA_ALGORITHM = "SHA-1";

	private SHAUtils() {
	}
	
	/**
	 * 获取基于SHA-1算法的信息摘要。
	 * @return MessageDigest	为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。<br>
	 * 							信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	private static MessageDigest getSHADigestAlgorithm()
			throws NoSuchAlgorithmException {
		return MessageDigest.getInstance(SHA_ALGORITHM);
	}
	
	/**
	 * 将明文字节加密为密文字节。
	 * @param source 输入的明文字节。
	 * @return	密文字节。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static byte[] getSHADigest(byte source[])
			throws NoSuchAlgorithmException {
		return getSHADigestAlgorithm().digest(source);
	}
	
	/**
	 * 将明输入的明文字符串加密为密文字节。
	 * @param source	输入的明文字符串。
	 * @return	输出的密文字节。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static byte[] getSHADigest(String source)
			throws NoSuchAlgorithmException {
		return getSHADigest(source.getBytes());
	}
	
	/**
	 * 将输入的明文字节加密为16进制的密文字符串。
	 * @param source	输入的明文字节。
	 * @return	输出的16进制的密文字符串。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String getSHADigestHex(byte source[])
			throws NoSuchAlgorithmException {
		return new String(Hex.encodeHex(getSHADigest(source)));
	}
	
	/**
	 * 将输入的明文字符串加密为16进制的密文字符串。
	 * @param source	输入的明文字符串。
	 * @return	输出的16进制的密文字符串。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String getSHADigestHex(String source)
			throws NoSuchAlgorithmException {
		return new String(Hex.encodeHex(getSHADigest(source.getBytes())));
	}
	
	/**
	 * 将输入的明文字节加密为基于Base64的密文字符串。
	 * @param source	输入的明文字节。
	 * @return	输出基于Base64的密文字符串。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String getSHADigestBase64(byte source[])
			throws NoSuchAlgorithmException {
		return new String(Base64.encodeBase64(getSHADigest(source)));
	}
	
	/**
	 *  将输入的明文字符串加密为基于Base64的密文字符串。
	 * @param source	输入的明文字符串。
	 * @return	输出基于Base64的密文字符串。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String getSHADigestBase64(String source)
			throws NoSuchAlgorithmException {
		return new String(Base64.encodeBase64(getSHADigest(source.getBytes())));
	}

}
