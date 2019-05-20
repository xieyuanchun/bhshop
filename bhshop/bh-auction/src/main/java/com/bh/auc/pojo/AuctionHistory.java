package com.bh.auc.pojo;

import java.util.Date;

public class AuctionHistory {
    private Integer id;

    private Integer confId;
    
    private Integer goodsId;

    private Integer type;

    private Integer currentPeriods;

    private Integer orderId;

    private String orderNo;

    private Integer schedulNum;

    private Integer bargainPrice;

    private Integer bargainMid;

    private Date loseTime;

    private Date startTime;

    private Integer actPrice;

    private Integer lowPrice;

    private Integer highPrice;

    private Integer timeSection;

    private Integer scopePrice;

    private Integer cashDeposit;

    private Date addTime;

    private Date updateTime;

    private Integer mId;

    private int currentPage;
    private int pageSize;
    private String goodsName;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConfId() {
        return confId;
    }

    public void setConfId(Integer confId) {
        this.confId = confId;
    }
    
    public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCurrentPeriods() {
        return currentPeriods;
    }

    public void setCurrentPeriods(Integer currentPeriods) {
        this.currentPeriods = currentPeriods;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getSchedulNum() {
        return schedulNum;
    }

    public void setSchedulNum(Integer schedulNum) {
        this.schedulNum = schedulNum;
    }

    public Integer getBargainPrice() {
        return bargainPrice;
    }

    public void setBargainPrice(Integer bargainPrice) {
        this.bargainPrice = bargainPrice;
    }

    public Integer getBargainMid() {
        return bargainMid;
    }

    public void setBargainMid(Integer bargainMid) {
        this.bargainMid = bargainMid;
    }

    public Date getLoseTime() {
        return loseTime;
    }

    public void setLoseTime(Date loseTime) {
        this.loseTime = loseTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getActPrice() {
        return actPrice;
    }

    public void setActPrice(Integer actPrice) {
        this.actPrice = actPrice;
    }

    public Integer getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Integer lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Integer getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Integer highPrice) {
        this.highPrice = highPrice;
    }

    public Integer getTimeSection() {
        return timeSection;
    }

    public void setTimeSection(Integer timeSection) {
        this.timeSection = timeSection;
    }

    public Integer getScopePrice() {
        return scopePrice;
    }

    public void setScopePrice(Integer scopePrice) {
        this.scopePrice = scopePrice;
    }

    public Integer getCashDeposit() {
        return cashDeposit;
    }

    public void setCashDeposit(Integer cashDeposit) {
        this.cashDeposit = cashDeposit;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}