package com.bh.admin.pojo.goods;

import java.util.Date;

public class TopicPrizeLog {
	private Integer id;

	private Integer mId;

	private Integer tgId;

	private Date addTime;

	private Integer isPrize;

	private String prizeNo;

	private String orderNo;

	private Integer orderId;

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

	private String topicNo;

	public Integer getOrderId() {
		return orderId;
	}

	public String getTopicNo() {
		return topicNo;
	}

	public void setTopicNo(String topicNo) {
		this.topicNo = topicNo;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public TopicGoods getTopicGood() {
		return topicGood;
	}

	public void setTopicGood(TopicGoods topicGood) {
		this.topicGood = topicGood;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public Integer getTgId() {
		return tgId;
	}

	public void setTgId(Integer tgId) {
		this.tgId = tgId;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getIsPrize() {
		return isPrize;
	}

	public void setIsPrize(Integer isPrize) {
		this.isPrize = isPrize;
	}

	public String getPrizeNo() {
		return prizeNo;
	}

	public void setPrizeNo(String prizeNo) {
		this.prizeNo = prizeNo == null ? null : prizeNo.trim();
	}
}