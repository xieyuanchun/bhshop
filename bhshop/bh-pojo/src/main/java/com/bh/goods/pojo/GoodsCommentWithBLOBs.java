package com.bh.goods.pojo;

public class GoodsCommentWithBLOBs extends GoodsComment {
    private String skuValue;

    private String description;

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue == null ? null : skuValue.trim();
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


}