package com.bh.jd.bean.goods;

import java.io.Serializable;

public class getSellPriceResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long skuId; //商品编号
	private double price;//客户购买价格/协议价
	private double jdPrice;//:京东价格
	
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getJdPrice() {
		return jdPrice;
	}
	public void setJdPrice(double jdPrice) {
		this.jdPrice = jdPrice;
	}
	

}
