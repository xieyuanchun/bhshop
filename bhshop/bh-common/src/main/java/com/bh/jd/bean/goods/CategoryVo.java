package com.bh.jd.bean.goods;

public class CategoryVo {
	private Integer catId;
	
	private Integer parentId;
	
	private String name;
	
	private Integer catClass;
	
	private Integer state;

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCatClass() {
		return catClass;
	}

	public void setCatClass(Integer catClass) {
		this.catClass = catClass;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	
	
	
}
