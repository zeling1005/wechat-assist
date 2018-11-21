package com.zeling.wa.vo;

/**
 * 订单vo
 * 
 * @author chenbd 2018年10月11日
 */
public class ZlOrderVO extends BaseVO {
	private static final long serialVersionUID = -630509654098307107L;	
	
	private String vorderId;
	private String vstatus;
	private String vphone;
	private String vsecretKey;
	private String time;
	
	public static final String VORDER_ID = "vorder_id";// 订单id
	public static final String VSTATUS = "vstatus"; // 辅助成功：Y,辅助失败：N,辅助进行中：Z
	public static final String VPHONE = "vphone"; // 手机号
	public static final String VSECRET_KEY = "vsecret_key";
	public static final String TIME = "time";
	public static final String TABLE_NAME = "zl_order";
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getVphone() {
		return vphone;
	}
	public void setVphone(String vphone) {
		this.vphone = vphone;
	}
	public String getVorderId() {
		return vorderId;
	}
	public void setVorderId(String vorderId) {
		this.vorderId = vorderId;
	}
	public String getVstatus() {
		return vstatus;
	}
	public void setVstatus(String vstatus) {
		this.vstatus = vstatus;
	}
	public String getVsecretKey() {
		return vsecretKey;
	}
	public void setVsecretKey(String vsecretKey) {
		this.vsecretKey = vsecretKey;
	}
}
