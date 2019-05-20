package com.bh.admin.pojo.user;

import java.util.List;

public class Areas {
	private int areaId;
	private int parentId;//上一级的id值
	private String areaName;//地区名称
	private int sortnum;//排序,默认99
	
	private List<Areas> areas;
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public int getSortnum() {
		return sortnum;
	}
	public void setSortnum(int sortnum) {
		this.sortnum = sortnum;
	}
	public List<Areas> getAreas() {
		return areas;
	}
	public void setAreas(List<Areas> areas) {
		this.areas = areas;
	}
	
	
}
