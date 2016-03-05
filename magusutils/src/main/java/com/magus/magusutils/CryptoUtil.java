package com.magus.magusutils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密工具
 */
public class CryptoUtil {

	/**
	 * byte数组转为16进制字符串
	 * 
	 * @param input
	 * @return
	 */
	public static String bytesToHexString(byte[] input) {
		StringBuffer sb = new StringBuffer(input.length);
		String sTemp;
		for (int i = 0; i < input.length; i++) {
			sTemp = Integer.toHexString(0xFF & input[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 16进制字符串转为byte数组
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToBytes(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (hexToByte(achar[pos]) << 4 | hexToByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte hexToByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	//---------------------------------------------------------------------------
	public static class AES {

		/**
		 * 使用AES加密，使用utf8编码
		 * 
		 * @param clearText
		 * @return
		 * @throws Exception
		 */
		public static String encrypt(String key, String clearText) throws Exception {
			byte[] rawKey = getRawKey(key);
			byte[] result = encrypt(rawKey, clearText.getBytes(Charset.UTF8));
			return bytesToHexString(result);
		}

		/**
		 * 使用AES加密
		 * 
		 * @param clearText
		 * @return
		 * @throws Exception
		 */
		public static byte[] encrypt(byte[] key, byte[] clearText) throws Exception {
			SecretKeySpec skeySpec = new SecretKeySpec(key,0, Math.min(key.length, Cipher.getMaxAllowedKeyLength("AES") / 8), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivParam = new IvParameterSpec(key,0,cipher.getBlockSize());
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec,ivParam);
			byte[] encrypted = cipher.doFinal(clearText);
			return encrypted;
		}

		/**
		 * 使用AES解密
		 * 
		 * @param encryptedText
		 * @return
		 * @throws Exception
		 */
		public static String decrypt(String key, String encryptedText) throws Exception {
			byte[] rawKey = getRawKey(key);
			byte[] enc = hexStringToBytes(encryptedText);
			byte[] result = decrypt(rawKey, enc);
			return new String(result, "UTF-8");
		}

		/**
		 * 使用AES解密
		 * 
		 * @param encryptedText
		 * @return
		 * @throws Exception
		 */
		public static byte[] decrypt(byte[] key, byte[] encryptedText) throws Exception {
			SecretKeySpec skeySpec = new SecretKeySpec(key,0, Math.min(key.length, Cipher.getMaxAllowedKeyLength("AES") / 8), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ivParam = new IvParameterSpec(key,0,cipher.getBlockSize());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec,ivParam);
			byte[] decrypted = cipher.doFinal(encryptedText);
			return decrypted;
		}

		private static byte[] getRawKey(String key) throws Exception {
			return MessageDigest.getInstance("MD5").digest(key.getBytes("UTF-8"));
		}
	}
	public static class MD5 {
		/**
		 * MD5加密，使用uft8编码
		 * 
		 * @param input
		 * @return
		 * @throws Exception
		 */
		public static byte[] toMd5(String input) {
			try {
				return toMd5(input, Charset.UTF8);
			} catch (UnsupportedEncodingException e) {
				return new byte[0];
			}
		}

		/**
		 * MD5加密
		 * 
		 * @param input
		 * @throws java.io.UnsupportedEncodingException
		 * @throws Exception
		 */
		public static byte[] toMd5(String input, String charsetName) throws UnsupportedEncodingException {
			byte[] data = input.getBytes(charsetName);
			return toMd5(data);
		}

		/**
		 * MD5加密
		 *
		 * @param data
		 * @return
		 * @throws java.security.NoSuchAlgorithmException
		 */
		public static byte[] toMd5(byte[] data) {
			byte[] messageDigest;
			MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
				digest.update(data);
				messageDigest = digest.digest();
				return messageDigest;
			} catch (NoSuchAlgorithmException e) {
				return new byte[0];
			}
		}
	}

	public static class Base64 {

		/**
		 * 转为Base64字符串
		 * 
		 * @param input
		 * @return
		 */
		public static String toBase64(byte[] input) {
			return android.util.Base64.encodeToString(input, android.util.Base64.DEFAULT);
		}

		/**
		 * 转为Base64字符串
		 * 
		 * @param input
		 * @return
		 */
		public static String toBase64(byte[] input, int flags) {
			return android.util.Base64.encodeToString(input, flags);
		}

		/**
		 * 将Base64数据解码
		 * 
		 * @param input
		 * @return
		 */
		public static byte[] fromBase64(byte[] input) {
			return android.util.Base64.decode(input, android.util.Base64.DEFAULT);
		}

		/**
		 * 将Base64数据解码
		 * 
		 * @param input
		 * @return
		 */
		public static byte[] fromBase64(byte[] input, int flags) {
			return android.util.Base64.decode(input, flags);
		}

		/**
		 * 将Base64字符串解码
		 * 
		 * @param input
		 * @return
		 */
		public static byte[] fromBase64String(String input) {
			return android.util.Base64.decode(input, android.util.Base64.DEFAULT);
		}

		/**
		 * 将Base64字符串解码
		 * 
		 * @param input
		 * @return
		 */
		public static byte[] fromBase64String(String input, int flags) {
			return android.util.Base64.decode(input, flags);
		}

	}
}
