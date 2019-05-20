package com.bh.goods.pojo;

public class GoodsDesc {
    private Integer goodsId;

    private String description;
    
    private Integer isPc;
    
    

    public Integer getIsPc() {
		return isPc;
	}

	public void setIsPc(Integer isPc) {
		this.isPc = isPc;
	}

	public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}