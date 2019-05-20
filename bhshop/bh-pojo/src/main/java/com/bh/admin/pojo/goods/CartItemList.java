package com.bh.admin.pojo.goods;

import java.io.Serializable;
import java.util.List;

public class CartItemList implements Serializable{
    //
	/**
	 * 
	 */
	private static final long serialVersionUID = 7922102058216055211L;
	
	private List<GoodsCart> list ;
	
	private String shopName;
	
	private Integer shopId;

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public List<GoodsCart> getList() {
		return list;
	}

	public void setList(List<GoodsCart> list) {
		this.list = list;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	

}
