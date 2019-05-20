package com.bh.order.pojo;

import java.util.List;

public class MyOrderShop {
	private Integer id;
	private Integer orderId;
	private Integer shopId;
	private String shopName;
	//拼团仅剩waitNum个名额
	private Integer waitNum;
	private Integer mId;
	//共goodsNumber件商品
	private Integer goodsNumber;
	//合计allPrice元
	private String allPrice;
	//运费realgDeliveryPrice元
	private String realgDeliveryPrice;
	//如果teamStatus=0,则邀请好友拼单,teamStatus的值有-1 拼团失败, 0拼团中, 1成功,2该单不是拼团单
	private Integer teamStatus;
	//1待付款，2待发货，3已发货，5待评价、6已取消、7已评价、8已删除、9备货中
	//去支付：status=1
	//取消订单:status=1
	//确认收货:status=3
	//删除订单:status=7||status=4||status=5||status=6
	//查看物流:status=3&&orderSku.get(i).getMystatus==null
	//已取消:status=6
	//已评价:status=7
	//已删除:status=8
	private Integer status;
	//fullName用teamNo代替
	private String teamNo;
	//右上角的中文
	private String mystatus;
	
	private List<MyOrderSku> orderSku;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Integer getWaitNum() {
		return waitNum;
	}

	public void setWaitNum(Integer waitNum) {
		this.waitNum = waitNum;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getAllPrice() {
		return allPrice;
	}

	public void setAllPrice(String allPrice) {
		this.allPrice = allPrice;
	}

	public String getRealgDeliveryPrice() {
		return realgDeliveryPrice;
	}

	public void setRealgDeliveryPrice(String realgDeliveryPrice) {
		this.realgDeliveryPrice = realgDeliveryPrice;
	}

	public Integer getTeamStatus() {
		return teamStatus;
	}

	public void setTeamStatus(Integer teamStatus) {
		this.teamStatus = teamStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTeamNo() {
		return teamNo;
	}

	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	public String getMystatus() {
		return mystatus;
	}

	public void setMystatus(String mystatus) {
		this.mystatus = mystatus;
	}

	public List<MyOrderSku> getOrderSku() {
		return orderSku;
	}

	public void setOrderSku(List<MyOrderSku> orderSku) {
		this.orderSku = orderSku;
	}
	
	
}
