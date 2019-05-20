package com.bh.goods.pojo;

public class SimpleGoodsSku {
	private String id;
	
	private String marketPrice;//市场价
	
	private String sellPrice;//销售价
	
	private String teamPrice;//拼团价
	
	private String stockPrice;//进货价
	private String deliveryPrice;//邮费

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getTeamPrice() {
		return teamPrice;
	}

	public void setTeamPrice(String teamPrice) {
		this.teamPrice = teamPrice;
	}

	public String getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(String stockPrice) {
		this.stockPrice = stockPrice;
	}

	public String getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(String deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}
	
	
}
