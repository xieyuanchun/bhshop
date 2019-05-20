package com.bh.jd.bean.notice;

import java.io.Serializable;

public class GoodsPriceNotice implements Serializable{
	private static final long serialVersionUID = 1L;
	private String skuId;
	private String price;
	private String jdPrice;
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getJdPrice() {
		return jdPrice;
	}
	public void setJdPrice(String jdPrice) {
		this.jdPrice = jdPrice;
	}

	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

}
