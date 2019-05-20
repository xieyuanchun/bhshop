package com.bh.goods.pojo;

import java.util.Map;

public class OrderCartGoodsList {
	private String goodName;
	private String gImage;
	private Map<String, Object> goodsSkus;
	private Integer num;
	private double realsellPrice;
	private Long catIdOne;
	
	public Long getCatIdOne() {
		return catIdOne;
	}
	public void setCatIdOne(Long catIdOne) {
		this.catIdOne = catIdOne;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getgImage() {
		return gImage;
	}
	public void setgImage(String gImage) {
		this.gImage = gImage;
	}
	
	public Map<String, Object> getGoodsSkus() {
		return goodsSkus;
	}
	public void setGoodsSkus(Map<String, Object> goodsSkus) {
		this.goodsSkus = goodsSkus;
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
	
}
