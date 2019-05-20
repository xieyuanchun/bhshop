package com.bh.jd.bean.goods;

import java.util.Date;


public class SkuImage {
	
	private Integer id;
	
	private String skuId;//池内商品编号
	
	private String path;//地址
	
	private String created;
	
	private String modified;
	
	private Integer yn;
	
	private Integer isPrimary;//是否是主图，1为主图，0为附图
	
	private String orderSort;//排序图片路径，商品详情页面返回的图片地址一致。
	
	private String position;
	
	private Integer type;
	
	private String features;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public Integer getYn() {
		return yn;
	}

	public void setYn(Integer yn) {
		this.yn = yn;
	}

	public Integer getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Integer isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getOrderSort() {
		return orderSort;
	}

	public void setOrderSort(String orderSort) {
		this.orderSort = orderSort;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	@Override
	public String toString() {
		return "SkuImage [id=" + id + ", skuId=" + skuId + ", path=" + path + ", created=" + created + ", modified="
				+ modified + ", yn=" + yn + ", isPrimary=" + isPrimary + ", orderSort=" + orderSort + ", position="
				+ position + ", type=" + type + ", features=" + features + "]";
	}
	
	
	
	
	
}
