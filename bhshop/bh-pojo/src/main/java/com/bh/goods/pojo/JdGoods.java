package com.bh.goods.pojo;

import java.util.Date;

public class JdGoods {
    private Integer id;

    private Long jdSkuNo;

    private String poolNum;

    private String goodsName;

    private String goodsImage;

    private String brandName;

    private String catId;

    private Integer jdPrice;

    private Integer stockPrice;

    private Integer isUp;

    private Integer isDelete;

    private Integer isGet;

    private Date addTime;

    private Date editTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getJdSkuNo() {
		return jdSkuNo;
	}

	public void setJdSkuNo(Long jdSkuNo) {
		this.jdSkuNo = jdSkuNo;
	}

	public String getPoolNum() {
        return poolNum;
    }

    public void setPoolNum(String poolNum) {
        this.poolNum = poolNum == null ? null : poolNum.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage == null ? null : goodsImage.trim();
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId == null ? null : catId.trim();
    }

    public Integer getJdPrice() {
        return jdPrice;
    }

    public void setJdPrice(Integer jdPrice) {
        this.jdPrice = jdPrice;
    }

    public Integer getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Integer stockPrice) {
        this.stockPrice = stockPrice;
    }

    public Integer getIsUp() {
        return isUp;
    }

    public void setIsUp(Integer isUp) {
        this.isUp = isUp;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getIsGet() {
        return isGet;
    }

    public void setIsGet(Integer isGet) {
        this.isGet = isGet;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}