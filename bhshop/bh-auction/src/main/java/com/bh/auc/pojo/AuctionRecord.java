package com.bh.auc.pojo;

import java.util.Date;

public class AuctionRecord {
    private Integer id;

    private String sysCode;

    private Integer goodsId;

    private Integer goodsSkuId;

    private Integer mId;

    private String headImg;

    private String userName;

    private Integer aucId;

    private Integer currentPeriods;

    private Integer aucPrice;

    private Date addTime;

    private int currentPage;
    private int pageSize;
    private int currentPageIndex;
    private double realAucPrice;

    public double getRealAucPrice() {
        return realAucPrice;
    }

    public void setRealAucPrice(double realAucPrice) {
        this.realAucPrice = realAucPrice;
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

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
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

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getAucId() {
        return aucId;
    }

    public void setAucId(Integer aucId) {
        this.aucId = aucId;
    }

    public Integer getCurrentPeriods() {
        return currentPeriods;
    }

    public void setCurrentPeriods(Integer currentPeriods) {
        this.currentPeriods = currentPeriods;
    }

    public Integer getAucPrice() {
        return aucPrice;
    }

    public void setAucPrice(Integer aucPrice) {
        this.aucPrice = aucPrice;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}