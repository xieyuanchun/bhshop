package com.bh.jd.bean;

import java.io.Serializable;

public class JdResult<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean success;
	private String resultMessage;
	private String resultCode;
	private T result;
	public boolean getSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public  T  getResult() {
		return  result;
	}
	public void setResult(T result) {
		this.result = result;
	}


}
