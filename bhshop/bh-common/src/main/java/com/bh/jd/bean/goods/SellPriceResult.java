package com.bh.jd.bean.goods;

import java.io.Serializable;

public class SellPriceResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long skuId; //商品编号
	private Double price;//客户购买价格/协议价
	private Double jdPrice;//:京东价格
	
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getJdPrice() {
		return jdPrice;
	}
	public void setJdPrice(Double jdPrice) {
		this.jdPrice = jdPrice;
	}
	
}
