package com.bh.auc.vo;


public class AuctionApiGoods {
	
	private String sysCode;

	private Integer goodsId;

	private Integer goodsSkuId;

	private String goodsName;

	private String goodsImage;
	
	private Integer currentPeriods;


	public Integer getCurrentPeriods() {
		return currentPeriods;
	}

	public void setCurrentPeriods(Integer currentPeriods) {
		this.currentPeriods = currentPeriods;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getGoodsSkuId() {
		return goodsSkuId;
	}

	public void setGoodsSkuId(Integer goodsSkuId) {
		this.goodsSkuId = goodsSkuId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}


}
