package com.bh.goods.pojo;

import java.util.Date;

public class GoodsOperLog {
    private Integer id;

    private String userId;

    private String adminUser;

    private Date opTime;

    private String goodId;

    private String name;

    private String title;

    private Integer modelId;

    private String opType;

    private Long catId;

    private String catName;

    private Integer shopCatId;

    private Long brandId;

    private Integer sellPrice;

    private Integer marketPrice;

    private Date upTime;

    private Date downTime;

    private Date addtime;

    private Date edittime;

    private String image;

    private Integer storeNums;

    private String unit;

    private Integer status;

    private Integer visit;

    private Integer favorite;

    private Short sortnum;

    private Integer comments;

    private Integer sale;

    private Integer shopId;

    private Boolean isHot;

    private Boolean isNew;

    private Integer isHotShop;

    private Integer isNewShop;

    private Boolean isFlag;

    private Boolean isShopFlag;

    private Integer deliveryPrice;

    private Integer refundDays;

    private String reason;

    private String publicimg;

    private Integer saleType;

    private Integer teamNum;

    private Integer teamEndTime;

    private Integer isCreate;

    private Integer isPromote;

    private Integer timeUnit;

    private Integer teamPrice;

    private Integer auctionPrice;

    private Integer isJd;

    private Integer topicType;

    private String tagIds;

    private Short shopsortnum;

    private Integer fixedSale;

    private Integer isPopular;

    private Long catIdOne;

    private Long catIdTwo;

    private Date applyTime;

    private String outReason;

    private Integer topicGoodsId;

    private Integer goodBuyLimit;

    private Integer visible;

    private String tagName;

    private String sendArea;

    private String orderId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser == null ? null : adminUser.trim();
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId == null ? null : goodId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType == null ? null : opType.trim();
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName == null ? null : catName.trim();
    }

    public Integer getShopCatId() {
        return shopCatId;
    }

    public void setShopCatId(Integer shopCatId) {
        this.shopCatId = shopCatId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
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

    public Date getUpTime() {
        return upTime;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    public Date getDownTime() {
        return downTime;
    }

    public void setDownTime(Date downTime) {
        this.downTime = downTime;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Date getEdittime() {
        return edittime;
    }

    public void setEdittime(Date edittime) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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

    public Integer getIsHotShop() {
        return isHotShop;
    }

    public void setIsHotShop(Integer isHotShop) {
        this.isHotShop = isHotShop;
    }

    public Integer getIsNewShop() {
        return isNewShop;
    }

    public void setIsNewShop(Integer isNewShop) {
        this.isNewShop = isNewShop;
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

    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Integer deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public Integer getRefundDays() {
        return refundDays;
    }

    public void setRefundDays(Integer refundDays) {
        this.refundDays = refundDays;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public String getPublicimg() {
        return publicimg;
    }

    public void setPublicimg(String publicimg) {
        this.publicimg = publicimg == null ? null : publicimg.trim();
    }

    public Integer getSaleType() {
        return saleType;
    }

    public void setSaleType(Integer saleType) {
        this.saleType = saleType;
    }

    public Integer getTeamNum() {
        return teamNum;
    }

    public void setTeamNum(Integer teamNum) {
        this.teamNum = teamNum;
    }

    public Integer getTeamEndTime() {
        return teamEndTime;
    }

    public void setTeamEndTime(Integer teamEndTime) {
        this.teamEndTime = teamEndTime;
    }

    public Integer getIsCreate() {
        return isCreate;
    }

    public void setIsCreate(Integer isCreate) {
        this.isCreate = isCreate;
    }

    public Integer getIsPromote() {
        return isPromote;
    }

    public void setIsPromote(Integer isPromote) {
        this.isPromote = isPromote;
    }

    public Integer getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Integer getTeamPrice() {
        return teamPrice;
    }

    public void setTeamPrice(Integer teamPrice) {
        this.teamPrice = teamPrice;
    }

    public Integer getAuctionPrice() {
        return auctionPrice;
    }

    public void setAuctionPrice(Integer auctionPrice) {
        this.auctionPrice = auctionPrice;
    }

    public Integer getIsJd() {
        return isJd;
    }

    public void setIsJd(Integer isJd) {
        this.isJd = isJd;
    }

    public Integer getTopicType() {
        return topicType;
    }

    public void setTopicType(Integer topicType) {
        this.topicType = topicType;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds == null ? null : tagIds.trim();
    }

    public Short getShopsortnum() {
        return shopsortnum;
    }

    public void setShopsortnum(Short shopsortnum) {
        this.shopsortnum = shopsortnum;
    }

    public Integer getFixedSale() {
        return fixedSale;
    }

    public void setFixedSale(Integer fixedSale) {
        this.fixedSale = fixedSale;
    }

    public Integer getIsPopular() {
        return isPopular;
    }

    public void setIsPopular(Integer isPopular) {
        this.isPopular = isPopular;
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

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getOutReason() {
        return outReason;
    }

    public void setOutReason(String outReason) {
        this.outReason = outReason == null ? null : outReason.trim();
    }

    public Integer getTopicGoodsId() {
        return topicGoodsId;
    }

    public void setTopicGoodsId(Integer topicGoodsId) {
        this.topicGoodsId = topicGoodsId;
    }

    public Integer getGoodBuyLimit() {
        return goodBuyLimit;
    }

    public void setGoodBuyLimit(Integer goodBuyLimit) {
        this.goodBuyLimit = goodBuyLimit;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
    }

    public String getSendArea() {
        return sendArea;
    }

    public void setSendArea(String sendArea) {
        this.sendArea = sendArea == null ? null : sendArea.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }
}