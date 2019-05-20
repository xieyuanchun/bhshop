package com.bh.jd.bean.goods;

import java.io.Serializable;

public class Stock implements Serializable{
	private static final long serialVersionUID = 1L;
	private String area; 
	private String desc;
	private String sku;
	private Integer state;
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
}
