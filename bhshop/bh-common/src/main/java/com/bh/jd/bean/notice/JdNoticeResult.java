package com.bh.jd.bean.notice;

import java.io.Serializable;

public class JdNoticeResult<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	//消息ID
	private String id;
	private String time;
	private T result;
	private Integer type;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

}
