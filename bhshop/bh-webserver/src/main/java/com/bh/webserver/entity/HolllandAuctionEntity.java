package com.bh.webserver.entity;

import java.io.Serializable;

import javax.websocket.Session;

public class HolllandAuctionEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private Session session;
	private Integer mId;//用户Id
	private String goodsId;//拍卖商品
	private String currentPeriods;//拍卖期数
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getCurrentPeriods() {
		return currentPeriods;
	}
	public void setCurrentPeriods(String currentPeriods) {
		this.currentPeriods = currentPeriods;
	}
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
