package com.gw.smstransmit.model;

import com.quanta.async.QuantaBaseModel;

public class SMSMsg extends QuantaBaseModel{

	public static final String COL_NUMBER = "phone";
	
	private String phone;

	
	public SMSMsg() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public SMSMsg(String phone) {
		super();
		this.phone = phone;
	}

	public static String getColNumber() {
		return COL_NUMBER;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
