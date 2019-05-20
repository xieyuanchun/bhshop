package com.bh.jd.bean.order;

public class StockParams {
	private String skuId;
	
	private Integer num;
	
	private boolean bNeedAnnex;
	
	private boolean bNeedGift;

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public boolean isbNeedAnnex() {
		return bNeedAnnex;
	}

	public void setbNeedAnnex(boolean bNeedAnnex) {
		this.bNeedAnnex = bNeedAnnex;
	}

	public boolean isbNeedGift() {
		return bNeedGift;
	}

	public void setbNeedGift(boolean bNeedGift) {
		this.bNeedGift = bNeedGift;
	}
	
	
	
	
}
