package com.bh.jd.bean.order;


public class NewStock{
	private Integer skuId;
	
	private String areaId;
	
	private Integer stockStateId;
	
	private String stockStateDesc;
	
	private Integer remainNum;

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public Integer getStockStateId() {
		return stockStateId;
	}

	public void setStockStateId(Integer stockStateId) {
		this.stockStateId = stockStateId;
	}

	public String getStockStateDesc() {
		return stockStateDesc;
	}

	public void setStockStateDesc(String stockStateDesc) {
		this.stockStateDesc = stockStateDesc;
	}

	public Integer getRemainNum() {
		return remainNum;
	}

	public void setRemainNum(Integer remainNum) {
		this.remainNum = remainNum;
	}
	
	
}
