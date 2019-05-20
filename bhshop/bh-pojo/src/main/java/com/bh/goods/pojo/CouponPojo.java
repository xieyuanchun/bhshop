package com.bh.goods.pojo;

public class CouponPojo {
	
	private int num;//优惠卷数量
	private String name;//优惠卷数量
	private double amount;//优惠金额
	private String effectiveTime;//有效时间段
	private int type;//优惠券类型，1普通券，2免邮券，3红包券
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getEffectiveTime() {
		return effectiveTime;
	}
	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	
	
	

}
