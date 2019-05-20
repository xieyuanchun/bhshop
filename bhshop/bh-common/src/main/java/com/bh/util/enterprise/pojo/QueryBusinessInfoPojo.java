package com.bh.util.enterprise.pojo;

import java.io.Serializable;

public class QueryBusinessInfoPojo implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer status;
	private String msg;
	private QueryBusinessInfoPojoResult result;
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
	public QueryBusinessInfoPojoResult getResult() {
		return result;
	}
	public void setResult(QueryBusinessInfoPojoResult result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "QueryBusinessInfoPojo [status=" + status + ", msg=" + msg + ", result=" + result + "]";
	}
	
	
	

}
