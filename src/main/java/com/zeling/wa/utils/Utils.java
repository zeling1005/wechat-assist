package com.zeling.wa.utils;

import com.zeling.wa.vo.ZlOrderVO;

/**
 * 工具类
 * 
 * @author chenbd 2018年10月11日
 */
public class Utils {

	public static String[] getZlOrderVOKeysNoPK() {
		return new String[] {ZlOrderVO.VPHONE, ZlOrderVO.VORDER_ID, ZlOrderVO.VSTATUS,
				ZlOrderVO.VSECRET_KEY };
	}

	private Utils() {
		throw new AssertionError(Utils.class.getName() + ": 禁止实例化");
	}
}
