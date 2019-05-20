package com.bh.webserver.entity;

import java.io.Serializable;

import javax.websocket.Session;

public class UserEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private Session session;
	private Integer mId;
	//用户标识
	private String flag;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Integer getmId() {
		return mId;
	}
	public void setmId(Integer mId) {
		this.mId = mId;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	
}
