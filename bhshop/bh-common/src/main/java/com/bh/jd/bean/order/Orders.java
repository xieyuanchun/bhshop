package com.bh.jd.bean.order;

public class Orders {

	
	//妥投订单信息
	
	private String jdOrderId; //京东订单编号
	
	private String state; //订单状态 0 是新建  1是妥投   2是拒收
	
	private String hangUpState; //是否挂起   0为为挂起    1为挂起

	private String invoiceState; //开票方式(1为随货开票，0为订单预借，2为集中开票 )
	
	private String orderPrice; //订单金额

	
	
	
	
	public String getJdOrderId() {
		return jdOrderId;
	}

	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getHangUpState() {
		return hangUpState;
	}

	public void setHangUpState(String hangUpState) {
		this.hangUpState = hangUpState;
	}

	public String getInvoiceState() {
		return invoiceState;
	}

	public void setInvoiceState(String invoiceState) {
		this.invoiceState = invoiceState;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}
	
	
	
}
