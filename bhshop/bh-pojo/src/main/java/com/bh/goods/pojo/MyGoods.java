package com.bh.goods.pojo;

public class MyGoods extends Goods{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String value;
	
	private Integer groupCount;

	private double realPrice;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(Integer groupCount) {
		this.groupCount = groupCount;
	}

	public double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}
	
	
	
}
