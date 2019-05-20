package com.bh.admin.pojo.goods;

public class GoodsBrand {
    private Long id;

    private String name;

    private String logo;

    private Short sortnum;

    private String catId;
    
    private String catName;
    
    private Integer isJd;
    
    private String categoryName; //品牌所属分类名称

   

    public Integer getIsJd() {
		return isJd;
	}

	public void setIsJd(Integer isJd) {
		this.isJd = isJd;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public Short getSortnum() {
        return sortnum;
    }

    public void setSortnum(Short sortnum) {
        this.sortnum = sortnum;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId == null ? null : catId.trim();
    }

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}
    
}