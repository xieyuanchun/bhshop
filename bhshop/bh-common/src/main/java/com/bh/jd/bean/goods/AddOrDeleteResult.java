package com.bh.jd.bean.goods;

import java.io.Serializable;

public class AddOrDeleteResult implements Serializable{
	private static final long serialVersionUID = 1L;
	//状态1添加，2删除
	private Integer state;
	//商品池编号
	private Integer page_num;
	//商品sku码
	private Long skuId;

	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getPage_num() {
		return page_num;
	}
	public void setPage_num(Integer page_num) {
		this.page_num = page_num;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
}
