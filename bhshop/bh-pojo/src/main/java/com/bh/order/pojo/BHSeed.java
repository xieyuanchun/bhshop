package com.bh.order.pojo;

public class BHSeed {
	
	private Integer isAllow;//0不允许使用滨惠豆下单，1允许
	
	private Integer ownSeedNum;//用户有的滨惠豆
	
	private Integer needSeedNum;//该商品需要的滨惠豆
	
	private Integer isMustOpen;//是否必须开启

	public Integer getIsAllow() {
		return isAllow;
	}

	public void setIsAllow(Integer isAllow) {
		this.isAllow = isAllow;
	}

	public Integer getOwnSeedNum() {
		return ownSeedNum;
	}

	public void setOwnSeedNum(Integer ownSeedNum) {
		this.ownSeedNum = ownSeedNum;
	}

	public Integer getNeedSeedNum() {
		return needSeedNum;
	}

	public void setNeedSeedNum(Integer needSeedNum) {
		this.needSeedNum = needSeedNum;
	}

	public Integer getIsMustOpen() {
		return isMustOpen;
	}

	public void setIsMustOpen(Integer isMustOpen) {
		this.isMustOpen = isMustOpen;
	}
	
	
	
	
}
