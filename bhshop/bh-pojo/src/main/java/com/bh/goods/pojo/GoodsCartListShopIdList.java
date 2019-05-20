package com.bh.goods.pojo;

import java.io.Serializable;
import java.util.List;

public class GoodsCartListShopIdList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -173729060407027657L;

	List<GoodsCart> goodsCartLists;
	
	Integer shopId ;
	
	String shopName;
	private double price;
	private double deliveryPrice ;//物流价格
	private double totalPrice ;//每个商家下面的总结
	
	private Integer num;//数量
	
	private Integer fz; 
	
	private String teamNo;

	

	public Integer getFz() {
		return fz;
	}

	public void setFz(Integer fz) {
		this.fz = fz;
	}

	public String getTeamNo() {
		return teamNo;
	}

	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	public List<GoodsCart> getGoodsCartLists() {
		return goodsCartLists;
	}

	public void setGoodsCartLists(List<GoodsCart> goodsCartLists) {
		this.goodsCartLists = goodsCartLists;
	}

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

	public double getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(double deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
