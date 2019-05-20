package com.bh.goods.pojo;

import java.util.List;

public class GoodsCategory {
    private Long id;

    private String name;

    private Long reid;

    private Short sortnum;

    private String image;

    private Boolean flag;
    
    private Boolean isLast;

    private Short series;
    
    private Integer adId;
    
    private String [] keyword; //首页分类关键字
    
    private List<GoodsCategory> arry;//分类列表
    
    private List<Goods> goodsList; //分类商品列表
    
    private String parentName; //父类名称
    
    private String godParentName; //祖父类名称
    
    private Integer count; //三级分类下的商品数量
    
    private Integer isJd;
    
    private List<GoodsCategory> childList;//下级分类列表
    
    
    

	public Integer getIsJd() {
		return isJd;
	}

	public void setIsJd(Integer isJd) {
		this.isJd = isJd;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
   
	public List<GoodsCategory> getChildList() {
		return childList;
	}

	public void setChildList(List<GoodsCategory> childList) {
		this.childList = childList;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<Goods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<Goods> goodsList) {
		this.goodsList = goodsList;
	}

	public List<GoodsCategory> getArry() {
		return arry;
	}

	public void setArry(List<GoodsCategory> arry) {
		this.arry = arry;
	}

	public Integer getAdId() {
		return adId;
	}

	public void setAdId(Integer adId) {
		this.adId = adId;
	}

	public String[] getKeyword() {
		return keyword;
	}

	public void setKeyword(String[] keyword) {
		this.keyword = keyword;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReid() {
		return reid;
	}

	public void setReid(Long reid) {
		this.reid = reid;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
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

	public String getGodParentName() {
		return godParentName;
	}

	public void setGodParentName(String godParentName) {
		this.godParentName = godParentName;
	}

    
}