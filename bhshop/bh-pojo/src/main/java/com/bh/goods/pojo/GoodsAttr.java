package com.bh.goods.pojo;

import java.util.List;

public class GoodsAttr {
    private Integer id;

    private Integer goodsId;

    private Integer attrId;

    private String attrValue;

    private Integer modelId;
    
    private String modelName; //模型名称
    
    private String attrName;//属性名字 xieyc 2018.01.17
    
    private List<GoodsAttr> listGoodsAttr; //xieyc
    
	public String getAttrName() {
		return attrName;
	}

	public List<GoodsAttr> getListGoodsAttr() {
		return listGoodsAttr;
	}

	public void setListGoodsAttr(List<GoodsAttr> listGoodsAttr) {
		this.listGoodsAttr = listGoodsAttr;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
    
    
    public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

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

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue == null ? null : attrValue.trim();
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }
}