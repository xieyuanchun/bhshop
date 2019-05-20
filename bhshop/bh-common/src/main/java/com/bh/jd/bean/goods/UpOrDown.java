package com.bh.jd.bean.goods;

import java.io.Serializable;

public class UpOrDown implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer state;
	
	private Long skuId;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

}
