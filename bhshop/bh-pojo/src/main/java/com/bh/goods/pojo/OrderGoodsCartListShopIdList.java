package com.bh.goods.pojo;

import java.util.List;

public class OrderGoodsCartListShopIdList {
	private static final long serialVersionUID = -173729060407027657L;
	private List<OrderCartGoodsList> goodsCartLists;
	Integer shopId ;
	String shopName;
	private double price;
	private double deliveryPrice ;//物流价格
	private double totalPrice ;//每个商家下面的总结
	private Integer num;//数量
	private Integer fz; 
	private String teamNo;
	private Integer couponId;
	private String couponName;
	private String couponAmount;
	private List<UsableCoupon> usableCouponList; //当前的商品可以使用的优惠劵信息
	
	public List<UsableCoupon> getUsableCouponList() {
		return usableCouponList;
	}
	public void setUsableCouponList(List<UsableCoupon> usableCouponList) {
		this.usableCouponList = usableCouponList;
	}
	public List<OrderCartGoodsList> getGoodsCartLists() {
		return goodsCartLists;
	}
	public void setGoodsCartLists(List<OrderCartGoodsList> goodsCartLists) {
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getDeliveryPrice() {
		return deliveryPrice;
	}
	public void setDeliveryPrice(double deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
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
	public Integer getCouponId() {
		return couponId;
	}
	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public String getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(String couponAmount) {
		this.couponAmount = couponAmount;
	}
	
}
