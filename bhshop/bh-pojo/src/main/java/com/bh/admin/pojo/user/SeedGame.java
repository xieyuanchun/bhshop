package com.bh.admin.pojo.user;

import java.util.List;

public class SeedGame {
	private Integer mId;
	
	private BaseGameMsg baseGameMsg;
	
	private LandList landList;//已有的土地
	
	private List<SimpleSeed> seedList;//已购买的种子的列表

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public LandList getLandList() {
		return landList;
	}

	public void setLandList(LandList landList) {
		this.landList = landList;
	}

	public List<SimpleSeed> getSeedList() {
		return seedList;
	}

	public void setSeedList(List<SimpleSeed> seedList) {
		this.seedList = seedList;
	}

	public BaseGameMsg getBaseGameMsg() {
		return baseGameMsg;
	}

	public void setBaseGameMsg(BaseGameMsg baseGameMsg) {
		this.baseGameMsg = baseGameMsg;
	}
	
	
	
}
