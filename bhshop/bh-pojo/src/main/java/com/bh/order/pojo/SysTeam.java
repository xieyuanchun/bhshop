package com.bh.order.pojo;

import java.io.Serializable;
import java.util.Date;

public class SysTeam implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer skuId;//skuId
	private Integer teamTime;//开团持续时间
	private Integer timeUnit;//时间单位
	private Integer teamNum;//参团总人数
	private Integer hasNum;//已参团人数
	private String teamNo;//团号
	private Date createTeamTime; //创团时间
	private Date endTime;//开团时间
	public Date getCreateTeamTime() {
		return createTeamTime;
	}

	public void setCreateTeamTime(Date createTeamTime) {
		this.createTeamTime = createTeamTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getTeamNo() {
		return teamNo;
	}

	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getTeamTime() {
		return teamTime;
	}

	public void setTeamTime(Integer teamTime) {
		this.teamTime = teamTime;
	}

	public Integer getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(Integer timeUnit) {
		this.timeUnit = timeUnit;
	}

	public Integer getTeamNum() {
		return teamNum;
	}

	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}

	public Integer getHasNum() {
		return hasNum;
	}

	public void setHasNum(Integer hasNum) {
		this.hasNum = hasNum;
	}

}
