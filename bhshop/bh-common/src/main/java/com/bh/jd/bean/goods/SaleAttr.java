package com.bh.jd.bean.goods;

public class SaleAttr {
	private String imagePath;
	private String saleValue;
	private String[] skuIds;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getSaleValue() {
		return saleValue;
	}

	public void setSaleValue(String saleValue) {
		this.saleValue = saleValue;
	}

	public String[] getSkuIds() {
		return skuIds;
	}

	public void setSkuIds(String[] skuIds) {
		this.skuIds = skuIds;
	}
}
