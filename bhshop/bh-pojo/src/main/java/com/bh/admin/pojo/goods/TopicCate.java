package com.bh.admin.pojo.goods;

import java.util.List;

public class TopicCate {
    private Integer id;

    private String catname;

    private Integer parentid;

    private Boolean child;

    private String title;

    private String keywords;

    private String description;

    private Short listorder;

    private String thumb;

    private String url;

    private Short ismenu;

    private Boolean isrec;

    private String thumbRec;
    
    private String arrparentid;

    private String arrchildid;
    
    private Integer series;
    
    private Integer isDelete;
    
    private List<TopicCate> childList; //子类列表
    
    private String currentPage;
    
    private Integer shopId;
    
  	public Integer getShopId() {
  		return shopId;
  	}

  	public void setShopId(Integer shopId) {
  		this.shopId = shopId;
  	}
    
    
    
    public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getSeries() {
		return series;
	}

	public void setSeries(Integer series) {
		this.series = series;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname == null ? null : catname.trim();
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public Boolean getChild() {
        return child;
    }

    public void setChild(Boolean child) {
        this.child = child;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Short getListorder() {
        return listorder;
    }

    public void setListorder(Short listorder) {
        this.listorder = listorder;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb == null ? null : thumb.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Short getIsmenu() {
        return ismenu;
    }

    public void setIsmenu(Short ismenu) {
        this.ismenu = ismenu;
    }

    public Boolean getIsrec() {
        return isrec;
    }

    public void setIsrec(Boolean isrec) {
        this.isrec = isrec;
    }

    public String getThumbRec() {
        return thumbRec;
    }

    public void setThumbRec(String thumbRec) {
        this.thumbRec = thumbRec == null ? null : thumbRec.trim();
    }
    
    public String getArrparentid() {
        return arrparentid;
    }

    public void setArrparentid(String arrparentid) {
        this.arrparentid = arrparentid == null ? null : arrparentid.trim();
    }

    public String getArrchildid() {
        return arrchildid;
    }

    public void setArrchildid(String arrchildid) {
        this.arrchildid = arrchildid == null ? null : arrchildid.trim();
    }

	public List<TopicCate> getChildList() {
		return childList;
	}

	public void setChildList(List<TopicCate> childList) {
		this.childList = childList;
	}
    
}