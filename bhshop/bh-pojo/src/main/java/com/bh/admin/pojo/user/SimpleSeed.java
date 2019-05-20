package com.bh.admin.pojo.user;

public class SimpleSeed {
	//memberSeed表的id
	private Integer id;
	
	//当前阶段
	private Integer currentJieDuan;
	
	//总共的阶段
	private Integer totalJieDuan;
	
	//当前阶段图
	private String currentJisDuanImage;
	
	//当前阶段的名称
	private String currentJieduanName;
	
	//该种子现在是否需要浇水0不需要,1需要
	private Integer isNeedWater;

	public Integer getCurrentJieDuan() {
		return currentJieDuan;
	}

	public void setCurrentJieDuan(Integer currentJieDuan) {
		this.currentJieDuan = currentJieDuan;
	}

	public Integer getTotalJieDuan() {
		return totalJieDuan;
	}

	public void setTotalJieDuan(Integer totalJieDuan) {
		this.totalJieDuan = totalJieDuan;
	}

	public String getCurrentJisDuanImage() {
		return currentJisDuanImage;
	}

	public void setCurrentJisDuanImage(String currentJisDuanImage) {
		this.currentJisDuanImage = currentJisDuanImage;
	}

	public String getCurrentJieduanName() {
		return currentJieduanName;
	}

	public void setCurrentJieduanName(String currentJieduanName) {
		this.currentJieduanName = currentJieduanName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsNeedWater() {
		return isNeedWater;
	}

	public void setIsNeedWater(Integer isNeedWater) {
		this.isNeedWater = isNeedWater;
	}
	
	
	
	
	
}
