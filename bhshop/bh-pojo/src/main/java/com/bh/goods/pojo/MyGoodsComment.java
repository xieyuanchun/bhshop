package com.bh.goods.pojo;

public class MyGoodsComment {
	//用户头像
	private String mHead;
	//用户名称
	private String mName;
	//属性
	private Object skuValueObj;
	//商品的名称
	private String goodsName;
	private String goodsImage;
	//评论的描述
	private String description;
	//等级的名称
	private String levelName;
	//等级
	private Byte level;
	//单价
	private String skuPrice;
	private String[] imageStr; //评价图转数组
	private String myImage;
	public String getmHead() {
		return mHead;
	}
	public void setmHead(String mHead) {
		this.mHead = mHead;
	}
	
	
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public Object getSkuValueObj() {
		return skuValueObj;
	}
	public void setSkuValueObj(Object skuValueObj) {
		this.skuValueObj = skuValueObj;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
	public Byte getLevel() {
		return level;
	}
	public void setLevel(Byte level) {
		this.level = level;
	}
	public String getSkuPrice() {
		return skuPrice;
	}
	public void setSkuPrice(String skuPrice) {
		this.skuPrice = skuPrice;
	}
	public String getGoodsImage() {
		return goodsImage;
	}
	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}
	public String[] getImageStr() {
		return imageStr;
	}
	public void setImageStr(String[] imageStr) {
		this.imageStr = imageStr;
	}
	public String getMyImage() {
		return myImage;
	}
	public void setMyImage(String myImage) {
		this.myImage = myImage;
	}
	
	
	
}
