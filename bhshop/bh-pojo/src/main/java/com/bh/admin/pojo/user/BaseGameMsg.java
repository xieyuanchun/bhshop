package com.bh.admin.pojo.user;

public class BaseGameMsg {
	private Integer mId;//用户的id
	
	private String username;//用户名
	
	private String headimgurl;//用户的头像
	
	private Integer gold;//用户的金币
	
	private Integer currentRank;//用户的当前等级
	
	private Integer experience;//用户的经验值
	
	private Integer currentRankExexperience;//该等级需要的经验值

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getCurrentRank() {
		return currentRank;
	}

	public void setCurrentRank(Integer currentRank) {
		this.currentRank = currentRank;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public Integer getCurrentRankExexperience() {
		return currentRankExexperience;
	}

	public void setCurrentRankExexperience(Integer currentRankExexperience) {
		this.currentRankExexperience = currentRankExexperience;
	}
	
	
}
