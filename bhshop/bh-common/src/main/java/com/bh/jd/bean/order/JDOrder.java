package com.bh.jd.bean.order;

import java.util.List;

import com.bh.jd.bean.SkuPojo;

public class JDOrder {
	//订单总金额 = 商品总金额(orderPrice) + 总运费(freight);
	
	private String jdOrderId;//京东订单号(可以理解为父单号，拆单后，会出现子单号;不拆单的，为个即是父单号也是子单号),
	
	private String freight;//总运费(freight)= 基础运费(baseFreight) + 每个sku的购买数量(num) * 该sku的超重偏远附加运费（remoteRegionFreight）(大部分sku是0);
	//总运费;这个是订单总运费 = 基础运费 + 总的超重偏远附加运费
	
	private String orderPrice;//商品总金额(orderPrice)
	
	private String orderNakedPrice;
	
	private List<SkuPojo> sku;
	
	private String orderTaxPrice;

	public String getJdOrderId() {
		return jdOrderId;
	}

	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}

	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getOrderNakedPrice() {
		return orderNakedPrice;
	}

	public void setOrderNakedPrice(String orderNakedPrice) {
		this.orderNakedPrice = orderNakedPrice;
	}

	public List<SkuPojo> getSku() {
		return sku;
	}

	public void setSku(List<SkuPojo> sku) {
		this.sku = sku;
	}

	public String getOrderTaxPrice() {
		return orderTaxPrice;
	}

	public void setOrderTaxPrice(String orderTaxPrice) {
		this.orderTaxPrice = orderTaxPrice;
	}
	
	
}
