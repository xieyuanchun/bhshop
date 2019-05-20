package com.bh.goods.pojo;

import java.util.Date;

import com.google.gson.JsonArray;

import net.sf.json.JSONArray;

public class ItemModelValue {
	//主键id
    private Integer id;
    //商品id
    private Integer goodsId;
    //添加时间
    private Date addTime;
    //商品规格属性值
    private String paramData;
    //转化后的商品规格属性值
    private JSONArray value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
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

	public JSONArray getValue() {
		return value;
	}

	public void setValue(JSONArray value) {
		this.value = value;
	}
    
    
}