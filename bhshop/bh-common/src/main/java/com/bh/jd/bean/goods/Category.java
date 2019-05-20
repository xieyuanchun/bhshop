package com.bh.jd.bean.goods;

import java.io.Serializable;

public class Category implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer catId; //分类Id
	private Integer count;//分类下商品数量
	private String name;//分类名称
	private Integer weight;//分类权重
	public Integer getCatId() {
		return catId;
	}
	public void setCatId(Integer catId) {
		this.catId = catId;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
}
