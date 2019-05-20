package com.bh.goods.pojo;

public class GoodsCommentWithBLOBs extends GoodsComment {
    private String skuValue;

    private String desc;

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue == null ? null : skuValue.trim();
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }
}