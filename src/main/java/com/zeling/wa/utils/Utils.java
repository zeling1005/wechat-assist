package com.zeling.wa.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.zeling.wa.vo.ZlOrderVO;

import sun.misc.BASE64Encoder;

/**
 * 工具类
 * 
 * @author chenbd 2018年10月11日
 */
public class Utils {
	
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static String[] getZlOrderVOKeysNoPK() {
		return new String[] {ZlOrderVO.VPHONE, ZlOrderVO.VORDER_ID, ZlOrderVO.VSTATUS,
				ZlOrderVO.VSECRET_KEY, ZlOrderVO.TIME };
	}
	
	public static String getImgBase64Str(String path) {
		if (path == null) {
			return null;
		}
		byte[] data = null;
		try {
			data = FileUtils.readFileToByteArray(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		} 
	    BASE64Encoder encoder = new BASE64Encoder();
	    return encoder.encode(data);
	}
	
	public static void main(String[] args) {
		String path = "C:\\Programs\\eclipse-workspace\\zeling-workspace\\wechat-assist\\src\\main\\java\\com\\zeling\\wa\\utils\\233.png";
		System.out.println(getImgBase64Str(path));
	}

	private Utils() {
		throw new AssertionError(Utils.class.getName() + ": 禁止实例化");
	}
}
