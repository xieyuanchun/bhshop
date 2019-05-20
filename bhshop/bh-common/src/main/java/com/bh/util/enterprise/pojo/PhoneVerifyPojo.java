package com.bh.util.enterprise.pojo;

import java.io.Serializable;

public class PhoneVerifyPojo implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer status;
	private String msg;
	private PhoneVerifyPojoResult result;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public PhoneVerifyPojoResult getResult() {
		return result;
	}
	public void setResult(PhoneVerifyPojoResult result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "PhoneVerifyPojo [status=" + status + ", msg=" + msg + ", result=" + result + "]";
	}
	
	

}
