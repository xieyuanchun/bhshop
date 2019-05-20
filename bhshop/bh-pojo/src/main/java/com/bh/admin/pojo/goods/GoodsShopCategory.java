package com.bh.admin.pojo.goods;

import java.util.List;

public class GoodsShopCategory {
    private Integer id;

    private String name;

    private Integer reid;

    private Short sortnum;

    private String image;

    private Integer shopId;
    
    private Boolean isLast;

    private Short series;
    
    private String shopName;//店铺名称
    
    private String parentName;//父级名称
    
    private List<GoodsShopCategory> childList; //下一级分类列表

    
    public List<GoodsShopCategory> getChildList() {
		return childList;
	}

	public void setChildList(List<GoodsShopCategory> childList) {
		this.childList = childList;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
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

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public Short getSortnum() {
        return sortnum;
    }

    public void setSortnum(Short sortnum) {
        this.sortnum = sortnum;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Boolean getIsLast() {
		return isLast;
	}

	public void setIsLast(Boolean isLast) {
		this.isLast = isLast;
	}

	public Short getSeries() {
		return series;
	}

	public void setSeries(Short series) {
		this.series = series;
	}
    
}