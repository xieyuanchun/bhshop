package com.bh.goods.pojo;

import java.util.Date;

public class CouponGift {
    private Integer id;

    private String name;

    private String couponIds;

    private Integer giftStatus;

    private Integer giftStock;

    private Integer giftSend;

    private Date startTime;

    private Date endTime;

    private Date addTime;

    private Integer isWhiteList;
    
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

    public String getCouponIds() {
        return couponIds;
    }

    public void setCouponIds(String couponIds) {
        this.couponIds = couponIds == null ? null : couponIds.trim();
    }

    public Integer getGiftStatus() {
        return giftStatus;
    }

    public void setGiftStatus(Integer giftStatus) {
        this.giftStatus = giftStatus;
    }

    public Integer getGiftStock() {
        return giftStock;
    }

    public void setGiftStock(Integer giftStock) {
        this.giftStock = giftStock;
    }

    public Integer getGiftSend() {
        return giftSend;
    }

    public void setGiftSend(Integer giftSend) {
        this.giftSend = giftSend;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

	public Integer getIsWhiteList() {
		return isWhiteList;
	}

	public void setIsWhiteList(Integer isWhiteList) {
		this.isWhiteList = isWhiteList;
	}
    
    
}