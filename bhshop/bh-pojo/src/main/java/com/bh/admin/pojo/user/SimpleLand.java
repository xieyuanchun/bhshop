package com.bh.admin.pojo.user;

public class SimpleLand {
	//土地的id
	private Integer id;
	
	//土地的名称
	private String landName;
	
	//土地的图片
	private String landImage;
	
	//是否免费-1免费，否则不免费
	private Integer isFree;
	
	//使用该土地的等级
	private Integer rand;

	public String getLandName() {
		return landName;
	}

	public void setLandName(String landName) {
		this.landName = landName;
	}

	public String getLandImage() {
		return landImage;
	}

	public void setLandImage(String landImage) {
		this.landImage = landImage;
	}

	public Integer getIsFree() {
		return isFree;
	}

	public void setIsFree(Integer isFree) {
		this.isFree = isFree;
	}
	

	public Integer getRand() {
		return rand;
	}

	public void setRand(Integer rand) {
		this.rand = rand;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	
	
}
