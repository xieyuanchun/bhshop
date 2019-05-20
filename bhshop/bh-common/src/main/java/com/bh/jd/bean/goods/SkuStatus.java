package com.bh.jd.bean.goods;

import java.io.Serializable;

public class SkuStatus implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer state;
	
	private Long sku;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getSku() {
		return sku;
	}

	public void setSku(Long sku) {
		this.sku = sku;
	}

}
