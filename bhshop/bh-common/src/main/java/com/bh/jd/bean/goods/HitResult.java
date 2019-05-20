package com.bh.jd.bean.goods;

import java.io.Serializable;

public class HitResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private String brand; //品牌名称
	private String imageUrl;//商品图片url
	private String wareName;//商品名称
	private String wareId;//商品名称
	private String warePId;//商品名称
	private String brandId;//商品名称
	private String catId;//商品名称
	private String cid1;//商品名称
	private String cid2;//商品名称
	private String wstate;//商品名称
	private String wyn;//商品名称
	private String catName;//商品名称
	private String cid1Name;//商品名称
	private String cid2Name;//商品名称
	private String synonyms;//商品名称
	
	private Boolean isGet; //是否已入库
	

	public Boolean getIsGet() {
		return isGet;
	}
	public void setIsGet(Boolean isGet) {
		this.isGet = isGet;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getWareName() {
		return wareName;
	}
	public void setWareName(String wareName) {
		this.wareName = wareName;
	}
	public String getWareId() {
		return wareId;
	}
	public void setWareId(String wareId) {
		this.wareId = wareId;
	}
	public String getWarePId() {
		return warePId;
	}
	public void setWarePId(String warePId) {
		this.warePId = warePId;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getCid1() {
		return cid1;
	}
	public void setCid1(String cid1) {
		this.cid1 = cid1;
	}
	public String getCid2() {
		return cid2;
	}
	public void setCid2(String cid2) {
		this.cid2 = cid2;
	}
	public String getWstate() {
		return wstate;
	}
	public void setWstate(String wstate) {
		this.wstate = wstate;
	}
	public String getWyn() {
		return wyn;
	}
	public void setWyn(String wyn) {
		this.wyn = wyn;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getCid1Name() {
		return cid1Name;
	}
	public void setCid1Name(String cid1Name) {
		this.cid1Name = cid1Name;
	}
	public String getCid2Name() {
		return cid2Name;
	}
	public void setCid2Name(String cid2Name) {
		this.cid2Name = cid2Name;
	}
	public String getSynonyms() {
		return synonyms;
	}
	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
	}
	
}
