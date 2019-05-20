package com.bh.goods.pojo;

import java.util.List;

public class GoodsModel {
    private Integer id;

    private String name;

    private Boolean status;

    private String catId;
    
    private List<GoodsModelAttr> value; //属性名称列表
    
    private String categoryName; //分类名称
    
    private String catName;
    

    public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public List<GoodsModelAttr> getValue() {
		return value;
	}

	public void setValue(List<GoodsModelAttr> value) {
		this.value = value;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId == null ? null : catId.trim();
    }
}