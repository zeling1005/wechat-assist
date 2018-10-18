package com.zeling.wa;

import java.sql.SQLException;

import com.zeling.wa.dao.CommonDao;
import com.zeling.wa.ui.WechatAssist;
import com.zeling.wa.vo.ZlOrderVO;

/**
 * 微信辅助
 * 
 * @author chenbd 2018年10月9日
 */
public class App {

	public static void main(String[] args) {
		init();
		new WechatAssist();
	}
	
	/**
	 * 初始化
	 */
	public static void init() {
		// 新建表
		try {
			CommonDao.crateTable("zl_order", new String[] {ZlOrderVO.VORDER_ID, ZlOrderVO.VSTATUS, ZlOrderVO.VPHONE, ZlOrderVO.VSECRET_KEY});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}