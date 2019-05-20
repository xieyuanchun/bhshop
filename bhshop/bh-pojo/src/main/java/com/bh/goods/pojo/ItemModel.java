package com.bh.goods.pojo;

import java.util.Date;

import net.sf.json.JSONArray;

public class ItemModel {
	//主键id
    private Integer id;
    //所属分类id
    private Long catId;
    //添加时间
    private Date addTime;
    //数据
    private String paramData;
    //所属一级分类id
    private Long catIdOne;
    //所属二级分类id
    private Long catIdTwo;
    //起始页
    private String currentPage;
    //分类名称
    private String catName;
    //jsonArray转化
    private JSONArray value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getParamData() {
        return paramData;
    }

    public void setParamData(String paramData) {
        this.paramData = paramData == null ? null : paramData.trim();
    }

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public JSONArray getValue() {
		return value;
	}

	public void setValue(JSONArray value) {
		this.value = value;
	}

	public Long getCatIdOne() {
		return catIdOne;
	}

	public void setCatIdOne(Long catIdOne) {
		this.catIdOne = catIdOne;
	}

	public Long getCatIdTwo() {
		return catIdTwo;
	}

	public void setCatIdTwo(Long catIdTwo) {
		this.catIdTwo = catIdTwo;
	}
    
}