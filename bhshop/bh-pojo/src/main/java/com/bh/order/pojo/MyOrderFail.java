package com.bh.order.pojo;

import java.util.Date;

public class MyOrderFail {

	private Integer id;//order_shop id 
	
	private Integer orderId;// 订单ID
	
	private Integer status;// order_shop 商家订单状态：1待付，2待发货，3已发货，4已收货，5待评价、6已取消、7已评价、8已删除
	
	private Date addtime;// 下单时间 order_main

	private Date validLen; //order_shop 订单有效时间
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Date getValidLen() {
		return validLen;
	}

	public void setValidLen(Date validLen) {
		this.validLen = validLen;
	}
	
	
	
	
}
