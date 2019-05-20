package com.bh.goods.pojo;

import java.sql.Timestamp;

public class GoodsFavorite {
    private Integer id;

    private Integer mId;
    
    private Integer skuId;

    private Integer goodsId;

    private Timestamp addtime;
    
    
    
    private String shopName;//2017-11-20cheng添加
    private String goodName;//2017-9-15cheng添加
    private String image ;
    private double realsellPrice;//单价
    private Object valueObj;
    private Long categoryId;//分类的id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

	public Timestamp getAddtime() {
		return addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public double getRealsellPrice() {
		return realsellPrice;
	}

	public void setRealsellPrice(double realsellPrice) {
		this.realsellPrice = realsellPrice;
	}

	public Object getValueObj() {
		return valueObj;
	}

	public void setValueObj(Object valueObj) {
		this.valueObj = valueObj;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	

	
	
}