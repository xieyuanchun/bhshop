package com.bh.jd.bean.goods;

import java.io.Serializable;

public class AreaLimit implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String skuId;
	private String isAreaRestrict;
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getIsAreaRestrict() {
		return isAreaRestrict;
	}
	public void setIsAreaRestrict(String isAreaRestrict) {
		this.isAreaRestrict = isAreaRestrict;
	}
	
	
	
	
	
}
