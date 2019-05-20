package com.bh.goods.pojo;

import java.util.List;

public class MyGoodsCategoryPojo {
	//分类的id
	   private Long id;
	//分类的图片
	private String image;
	//分类的名称
	private String name;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<MyPingtaiGoodsPojo> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<MyPingtaiGoodsPojo> goodsList) {
		this.goodsList = goodsList;
	}
	private List<MyPingtaiGoodsPojo> goodsList;
	
}
