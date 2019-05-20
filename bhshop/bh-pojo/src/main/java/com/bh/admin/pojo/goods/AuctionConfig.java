package com.bh.admin.pojo.goods;

import java.util.Date;

public class AuctionConfig {
    private Integer id;

    private String sysCode;

    private Integer goodsId;

    private Integer currentPeriods;

    private Integer goodsSkuId;

    private String goodsName;

    private String goodsImage;

    private Integer goodsMarketPrice;

    private Integer goodsSellPrice;

    private Integer storeNum;

    private Integer soldNum;

    private Integer actPrice;

    private Integer lowPrice;

    private Integer highPrice;

    private Date startTime;

    private Date loseTime;

    private Integer timeSection;

    private Integer scopePrice;

    private Date addTime;

    private Integer upDownStatus;

    private Integer cashDeposit;

    private Integer sort;

    private Integer mId;

    private double realCashDeposit;
    private double realHighPrice;
    private double realActPrice;
    private double realLowPrice;
    private double realScopePrice;

    private int currentPage;
    private int pageSize;
    private int currentPageIndex;


    public Integer getGoodsMarketPrice() {
        return goodsMarketPrice;
    }

    public void setGoodsMarketPrice(Integer goodsMarketPrice) {
        this.goodsMarketPrice = goodsMarketPrice;
    }

    public Integer getGoodsSellPrice() {
        return goodsSellPrice;
    }

    public void setGoodsSellPrice(Integer goodsSellPrice) {
        this.goodsSellPrice = goodsSellPrice;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
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

    public double getRealCashDeposit() {
        return realCashDeposit;
    }

    public void setRealCashDeposit(double realCashDeposit) {
        this.realCashDeposit = realCashDeposit;
    }

    public double getRealHighPrice() {
        return realHighPrice;
    }

    public void setRealHighPrice(double realHighPrice) {
        this.realHighPrice = realHighPrice;
    }


    public double getRealActPrice() {
        return realActPrice;
    }

    public void setRealActPrice(double realActPrice) {
        this.realActPrice = realActPrice;
    }

    public double getRealLowPrice() {
        return realLowPrice;
    }

    public void setRealLowPrice(double realLowPrice) {
        this.realLowPrice = realLowPrice;
    }

    public double getRealScopePrice() {
        return realScopePrice;
    }

    public void setRealScopePrice(double realScopePrice) {
        this.realScopePrice = realScopePrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode == null ? null : sysCode.trim();
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Integer goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
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

    public Integer getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(Integer storeNum) {
        this.storeNum = storeNum;
    }

    public Integer getSoldNum() {
        return soldNum;
    }

    public void setSoldNum(Integer soldNum) {
        this.soldNum = soldNum;
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

    public Integer getCurrentPeriods() {
        return currentPeriods;
    }

    public void setCurrentPeriods(Integer currentPeriods) {
        this.currentPeriods = currentPeriods;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getLoseTime() {
        return loseTime;
    }

    public void setLoseTime(Date loseTime) {
        this.loseTime = loseTime;
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

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getUpDownStatus() {
        return upDownStatus;
    }

    public void setUpDownStatus(Integer upDownStatus) {
        this.upDownStatus = upDownStatus;
    }

    public Integer getCashDeposit() {
        return cashDeposit;
    }

    public void setCashDeposit(Integer cashDeposit) {
        this.cashDeposit = cashDeposit;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    @Override
    public String toString() {
        return "AuctionConfig{" +
                "id=" + id +
                ", sysCode='" + sysCode + '\'' +
                ", goodsId=" + goodsId +
                ", currentPeriods=" + currentPeriods +
                ", goodsSkuId=" + goodsSkuId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsImage='" + goodsImage + '\'' +
                ", goodsMarketPrice=" + goodsMarketPrice +
                ", goodsSellPrice=" + goodsSellPrice +
                ", storeNum=" + storeNum +
                ", soldNum=" + soldNum +
                ", actPrice=" + actPrice +
                ", lowPrice=" + lowPrice +
                ", highPrice=" + highPrice +
                ", startTime=" + startTime +
                ", loseTime=" + loseTime +
                ", timeSection=" + timeSection +
                ", scopePrice=" + scopePrice +
                ", addTime=" + addTime +
                ", upDownStatus=" + upDownStatus +
                ", cashDeposit=" + cashDeposit +
                ", sort=" + sort +
                ", mId=" + mId +
                ", realCashDeposit=" + realCashDeposit +
                ", realHighPrice=" + realHighPrice +
                ", realActPrice=" + realActPrice +
                ", realLowPrice=" + realLowPrice +
                ", realScopePrice=" + realScopePrice + " }";
    }
}