package com.bh.util.enterprise.pojo;

import java.io.Serializable;

public class PhoneVerifyPojoResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private String mobile;//手机号
	private String realname;//真实姓名
	private String idcard;//证件号码
	private String verifystatus;//验证状态 0一致 1不一致
	private String verifymsg;//验证信息
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getVerifystatus() {
		return verifystatus;
	}
	public void setVerifystatus(String verifystatus) {
		this.verifystatus = verifystatus;
	}
	public String getVerifymsg() {
		return verifymsg;
	}
	public void setVerifymsg(String verifymsg) {
		this.verifymsg = verifymsg;
	}
	public String toString() {
		return "PhoneVerifyResult [mobile=" + mobile + ", realname=" + realname + ", idcard=" + idcard
				+ ", verifystatus=" + verifystatus + ", verifymsg=" + verifymsg + "]";
	}
	
	
}
