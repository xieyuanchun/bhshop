package com.bh.goods.pojo;

import java.util.Date;

public class TopicSavemoneyLog {
	private Integer id;

	private Integer tgId;

	private Integer mId;

	private String orderNo;

	private String myNo;

	private String actNo;

	private Date addTime;

	private Integer goodsSkuId;

	private String currentPage;

	private String mName; // 用户昵称

	private String goodsName; // 商品名称

	private String topicName; // 活动名称

	private TopicGoods topicGood;

	private Integer shopId;

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	private TopicSavemoneyConfig topicSavemoneyConfig;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTgId() {
		return tgId;
	}

	public void setTgId(Integer tgId) {
		this.tgId = tgId;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getActNo() {
		return actNo;
	}

	public void setActNo(String actNo) {
		this.actNo = actNo;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getGoodsSkuId() {
		return goodsSkuId;
	}

	public void setGoodsSkuId(Integer goodsSkuId) {
		this.goodsSkuId = goodsSkuId;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public TopicGoods getTopicGood() {
		return topicGood;
	}

	public void setTopicGood(TopicGoods topicGood) {
		this.topicGood = topicGood;
	}

	public TopicSavemoneyConfig getTopicSavemoneyConfig() {
		return topicSavemoneyConfig;
	}

	public void setTopicSavemoneyConfig(TopicSavemoneyConfig topicSavemoneyConfig) {
		this.topicSavemoneyConfig = topicSavemoneyConfig;
	}

	public String getMyNo() {
		return myNo;
	}

	public void setMyNo(String myNo) {
		this.myNo = myNo;
	}

}