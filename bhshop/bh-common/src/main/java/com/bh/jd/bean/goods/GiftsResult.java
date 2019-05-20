package com.bh.jd.bean.goods;

import java.io.Serializable;
import java.util.List;

public class GiftsResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<Gift> gifts;
	private String maxNum;
	private String minNum;
	private long promoStartTime;
	private long promoEndTime;
	public List<Gift> getGifts() {
		return gifts;
	}
	public void setGifts(List<Gift> gifts) {
		this.gifts = gifts;
	}
	public String getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(String maxNum) {
		this.maxNum = maxNum;
	}
	public String getMinNum() {
		return minNum;
	}
	public void setMinNum(String minNum) {
		this.minNum = minNum;
	}
	public long getPromoStartTime() {
		return promoStartTime;
	}
	public void setPromoStartTime(long promoStartTime) {
		this.promoStartTime = promoStartTime;
	}
	public long getPromoEndTime() {
		return promoEndTime;
	}
	public void setPromoEndTime(long promoEndTime) {
		this.promoEndTime = promoEndTime;
	}
	
}
