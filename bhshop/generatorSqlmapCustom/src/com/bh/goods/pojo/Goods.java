package com.bh.goods.pojo;

public class Goods {
    private Integer id;

    private String name;

    private Integer modelId;

    private Integer catId;

    private Integer shopCatId;

    private Integer brandId;

    private Integer sellPrice;

    private Integer marketPrice;

    private Integer upTime;

    private Integer downTime;

    private Integer addtime;

    private Integer edittime;

    private String image;

    private Integer storeNums;

    private String unit;

    private Boolean status;

    private Integer visit;

    private Integer favorite;

    private Short sortnum;

    private Integer comments;

    private Integer sale;

    private Integer shopId;

    private Boolean isHot;

    private Boolean isNew;

    private Boolean isFlag;

    private Boolean isShopFlag;

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

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public Integer getShopCatId() {
        return shopCatId;
    }

    public void setShopCatId(Integer shopCatId) {
        this.shopCatId = shopCatId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Integer marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Integer getUpTime() {
        return upTime;
    }

    public void setUpTime(Integer upTime) {
        this.upTime = upTime;
    }

    public Integer getDownTime() {
        return downTime;
    }

    public void setDownTime(Integer downTime) {
        this.downTime = downTime;
    }

    public Integer getAddtime() {
        return addtime;
    }

    public void setAddtime(Integer addtime) {
        this.addtime = addtime;
    }

    public Integer getEdittime() {
        return edittime;
    }

    public void setEdittime(Integer edittime) {
        this.edittime = edittime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getStoreNums() {
        return storeNums;
    }

    public void setStoreNums(Integer storeNums) {
        this.storeNums = storeNums;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getVisit() {
        return visit;
    }

    public void setVisit(Integer visit) {
        this.visit = visit;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public Short getSortnum() {
        return sortnum;
    }

    public void setSortnum(Short sortnum) {
        this.sortnum = sortnum;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getSale() {
        return sale;
    }

    public void setSale(Integer sale) {
        this.sale = sale;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(Boolean isFlag) {
        this.isFlag = isFlag;
    }

    public Boolean getIsShopFlag() {
        return isShopFlag;
    }

    public void setIsShopFlag(Boolean isShopFlag) {
        this.isShopFlag = isShopFlag;
    }
}