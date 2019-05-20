package com.bh.admin.pojo.goods;

import java.util.Date;

public class TopicSeckillLog {
	private Integer id;
    private Integer mId;
    private Integer tgId;
    private Date addTime;
    private String orderNo;
    private String actNo;
    private Integer goodsSkuId;
    private String currentPage;
    
    private String topicNo; //活动团号
    
    private String mName;
    private TopicGoods topicGoods;
    private String  TopicName;
    private TopicSeckillConfig topicSeckillConfig;
    
    private Integer shopId;
    
  	public Integer getShopId() {
  		return shopId;
  	}

  	public void setShopId(Integer shopId) {
  		this.shopId = shopId;
  	}
    
    public String getTopicNo() {
		return topicNo;
	}
	public void setTopicNo(String topicNo) {
		this.topicNo = topicNo;
	}
	public TopicSeckillConfig getTopicSeckillConfig() {
		return topicSeckillConfig;
	}
	public void setTopicSeckillConfig(TopicSeckillConfig topicSeckillConfig) {
		this.topicSeckillConfig = topicSeckillConfig;
	}
	public String getTopicName() {
		return TopicName;
	}
	public void setTopicName(String topicName) {
		TopicName = topicName;
	}
	public TopicGoods getTopicGoods() {
		return topicGoods;
	}
	public void setTopicGoods(TopicGoods topicGoods) {
		this.topicGoods = topicGoods;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
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
	public Integer getGoodsSkuId() {
		return goodsSkuId;
	}
	public void setGoodsSkuId(Integer goodsSkuId) {
		this.goodsSkuId = goodsSkuId;
	}


  
}