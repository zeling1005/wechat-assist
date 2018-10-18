package com.zeling.wa.utils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.zeling.wa.dao.CommonDao;
import com.zeling.wa.vo.ZlOrderVO;

/**
 * 测试工具类
 * 
 * @author chenbd 2018年10月11日
 */
public class TestUtils {

	public static void testInsert() {
		try {
			CommonDao.insertH2(ZlOrderVO.TABLE_NAME, Utils.getZlOrderVOKeysNoPK(), new String[] {"111", "111", "111", "111"});
			CommonDao.insertH2(ZlOrderVO.TABLE_NAME, Utils.getZlOrderVOKeysNoPK(), new String[] {"222", "222", "222", "222"});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void testDelete() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put(ZlOrderVO.VPHONE, "111");
			CommonDao.deleteH2(ZlOrderVO.TABLE_NAME, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void testUpdate() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put(ZlOrderVO.VPHONE, "333");
			CommonDao.updateH2(ZlOrderVO.TABLE_NAME, new String[] {ZlOrderVO.VSTATUS}, new String[] {"Z"}, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private TestUtils() {
		throw new AssertionError(TestUtils.class.getName() + ": 禁止实例化");
	}
}
