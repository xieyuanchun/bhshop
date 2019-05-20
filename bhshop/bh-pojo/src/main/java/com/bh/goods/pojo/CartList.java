package com.bh.goods.pojo;

import java.util.List;

public class CartList {
	//购物车列表
	private Integer shopId ;
	private String shopName;
	private List<CartGoodsList> goodsCartLists;
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
	public List<CartGoodsList> getGoodsCartLists() {
		return goodsCartLists;
	}
	public void setGoodsCartLists(List<CartGoodsList> goodsCartLists) {
		this.goodsCartLists = goodsCartLists;
	}
	
	
	
}
