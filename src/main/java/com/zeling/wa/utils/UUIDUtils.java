package com.zeling.wa.utils;

import java.util.UUID;

/**
 * uuid工具类
 * 
 * @author chenbd 2018年10月11日
 */
public class UUIDUtils {
	
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}
	
	public static void main(String[] args) {
		System.out.println(getUUID());
	}

	private UUIDUtils() {
		throw new AssertionError(UUIDUtils.class.getName() + ": 禁止实例化");
	}
}
