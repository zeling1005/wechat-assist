package com.zeling.wa.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

/**
 * 密钥工具类
 * 
 * @author chenbd 2018年10月11日
 */
public class SecretUtils {
	
//	private static final String filePath = SecretUtils.class.getResource("zeling/resources/secretinfo").getFile();
	private static final String filePath = "zeling/resources/secretinfo";
	
	/**
	 * 保存密钥
	 * 
	 * @param secret
	 */
	public static void saveSecret(String secret) {
		secret = "zeling" + secret;
		byte[] encodeBytes = Base64.encodeBase64(secret.getBytes());
        String encode = new String (encodeBytes);
//        System.out.println(encode);
        // 写入文件
        try {
			FileUtils.writeStringToFile(new File(filePath), encode, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取密钥
	 * 
	 * @return
	 */
	public static String getSecret() {
		String secret = null;
		try {
			secret = FileUtils.readFileToString(new File(filePath), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (secret == null || secret.trim().equals("")) {
//			return null;
			throw new Error("验证失败，退出");
		}
		byte[] decodeBytes = Base64.decodeBase64(secret);
		String decode = new String(decodeBytes);
		if (!decode.contains("zeling")) {
//			return null;
			throw new Error("验证失败，退出");
		}
		decode = decode.replace("zeling", "");
//		System.out.println(decode);
		return decode;
	}
	
	public static void main(String[] args) {
		saveSecret("");
		getSecret();
	}

	private SecretUtils() {
		throw new AssertionError(SecretUtils.class.getName() + ": 禁止实例化");
	}
}
