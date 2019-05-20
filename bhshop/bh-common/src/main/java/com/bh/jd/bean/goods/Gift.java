package com.bh.jd.bean.goods;

import java.io.Serializable;

public class Gift implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long skuId; 
	private Integer num;
	private Integer giftType;
	
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getGiftType() {
		return giftType;
	}
	public void setGiftType(Integer giftType) {
		this.giftType = giftType;
	}
}
