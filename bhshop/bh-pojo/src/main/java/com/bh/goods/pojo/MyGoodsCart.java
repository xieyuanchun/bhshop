package com.bh.goods.pojo;

import java.util.Map;

public class MyGoodsCart {
	private Integer shopId;
	 private String shopName;
	 private String goodName;//2017-9-15cheng添加
	 private Integer num;
	 private double realsellPrice;
	 private Map<String, Object> goodsSkus;
	 private String gImage;
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public double getRealsellPrice() {
		return realsellPrice;
	}
	public void setRealsellPrice(double realsellPrice) {
		this.realsellPrice = realsellPrice;
	}
	public Map<String, Object> getGoodsSkus() {
		return goodsSkus;
	}
	public void setGoodsSkus(Map<String, Object> goodsSkus) {
		this.goodsSkus = goodsSkus;
	}
	public String getgImage() {
		return gImage;
	}
	public void setgImage(String gImage) {
		this.gImage = gImage;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	 
	 
}
