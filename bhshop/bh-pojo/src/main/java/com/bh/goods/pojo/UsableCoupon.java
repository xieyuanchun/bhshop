package com.bh.goods.pojo;

public class UsableCoupon {
	
	private double amount;//优惠金额
	
	private String title;//标题
	
	private String shopName;//商家名字
	
	private String effectiveTime;//有效时间段 
	
	private String applyStr;
	
	private String type;
	
	private int id;//优惠卷id
	
	private int couponType;
	
	
	public int getCouponType() {
		return couponType;
	}

	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getApplyStr() {
		return applyStr;
	}

	public void setApplyStr(String applyStr) {
		this.applyStr = applyStr;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	
}
