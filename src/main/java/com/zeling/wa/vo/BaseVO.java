package com.zeling.wa.vo;

import java.io.Serializable;

public class BaseVO implements Serializable {
	
	private static final long serialVersionUID = 4608730213489639828L;

	private String hid;
	
	public static final String HID = "hid";
	
	public String getHid() {
		return hid;
	}
	public void setHid(String hid) {
		this.hid = hid;
	}
}
