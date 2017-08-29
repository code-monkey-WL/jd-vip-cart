package com.jd.o2o.vipcart.common.utils.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>文件名称: MD5Utils.java</p>
 * <p>文件描述: MD5加密工具</p>
 * @author  Wuqingming
 */

public final class MD5Utils {
	private static final String MD5_ALGORITHM = "MD5";

	private MD5Utils() {
	}

	private static MessageDigest getMD5DigestAlgorithm()
			throws NoSuchAlgorithmException {
		return MessageDigest.getInstance(MD5_ALGORITHM);
	}
	
	/**
	 * 将明文字节加密为密文字节。
	 * @Title: getMD5Digest 
	 * @Description:  将明文字节加密为密文字节。
	 * @param source	明文字节。
	 * @return	密文字节。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static byte[] getMD5Digest(byte source[])
			throws NoSuchAlgorithmException {
		return getMD5DigestAlgorithm().digest(source);
	}
	
	/**
	 * 将明文字符串加密为密文字节。
	 * @Title: getMD5Digest 
	 * @Description: 将明文字符串加密为密文字节。
	 * @param source	明文字符串。
	 * @return	密文字节。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static byte[] getMD5Digest(String source)
			throws NoSuchAlgorithmException {
		return getMD5Digest(source.getBytes());
	}
	
	/**
	 * 用16进制编码将明文字节加密为密文字符串。
	 * @Title: getMD5DigestHex 
	 * @Description: 将明文字节加密为密文字符串。
	 * @param source	明文字节。
	 * @return	密文字符串。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String getMD5DigestHex(byte source[])
			throws NoSuchAlgorithmException {
		return new String(Hex.encodeHex(getMD5Digest(source)));
	}
	
	/**
	 * 用16进制编码将明文字符串加密为密文字符串。
	 * @Title: getMD5DigestHex 
	 * @Description: 将明文字符串加密为密文字符串。 
	 * @param source	明文字符串。
	 * @return	密文字符串。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String getMD5DigestHex(String source)
			throws NoSuchAlgorithmException {
		return new String(Hex.encodeHex(getMD5Digest(source)));
	}
	
	/**
	 * 用Base64编码将明文字节加密为密文字符串。
	 * @Title: getMD5DigestBase64 
	 * @Description: Base64将明文字节加密为密文字符串。
	 * @param source	明文字节。
	 * @return	密文字符串。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String getMD5DigestBase64(byte source[])
			throws NoSuchAlgorithmException {
		return new String(Base64.encodeBase64(getMD5Digest(source)));
	}
	
	/**
	 * 用Base64编码将明文字符串加密为密文字符串。
	 * @Title: getMD5DigestBase64 
	 * @Description: 用Base64编码将明文字符串加密为密文字符串。 
	 * @param source	明文字符串。
	 * @return	密文字符串。
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String getMD5DigestBase64(String source)
			throws NoSuchAlgorithmException {
		return new String(Base64.encodeBase64(getMD5Digest(source)));
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(getMD5DigestHex("abc"));
		System.out.println(getMD5DigestBase64("abc"));
	}
}
