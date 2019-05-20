package com.bh.jd.bean.goods;

import java.io.Serializable;

public class CommentSummarys implements Serializable{
	
	private static final long serialVersionUID = 1L;
		
	private String averageScore;//商品评分 (5颗星，4颗星)
	
	private String generalRate;//好评度
	
	private String goodRate;//中评度
	
	private String skuId;//商品池编号
	
	private String poorRate;//差评度

	
	public String getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(String averageScore) {
		this.averageScore = averageScore;
	}

	public String getGeneralRate() {
		return generalRate;
	}

	public void setGeneralRate(String generalRate) {
		this.generalRate = generalRate;
	}

	public String getGoodRate() {
		return goodRate;
	}

	public void setGoodRate(String goodRate) {
		this.goodRate = goodRate;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getPoorRate() {
		return poorRate;
	}

	public void setPoorRate(String poorRate) {
		this.poorRate = poorRate;
	}

	
	
	
}
