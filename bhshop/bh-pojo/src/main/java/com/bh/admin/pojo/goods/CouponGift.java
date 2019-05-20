package com.bh.admin.pojo.goods;

import java.util.Date;
import java.util.List;

public class CouponGift {
	//主键id
    private Integer id;
    //礼包名称
    private String name;
    //优惠券id
    private String couponIds;
    //状态：0开启，1关闭
    private Integer giftStatus;
    //剩余数量
    private Integer giftStock;
    //发行总量
    private Integer giftSend;
    //开始领取时间
    private Date startTime;
    //截止领取时间
    private Date endTime;
    //添加时间
    private Date addTime;
    
    private Integer isWhiteList;
    //起始页
    private String currentPage;
    //大礼包优惠券中间表
    private Integer shopId;
    
    private List<CouponAndGift> couponAndGiftList;
    
    public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

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

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public List<CouponAndGift> getCouponAndGiftList() {
		return couponAndGiftList;
	}

	public void setCouponAndGiftList(List<CouponAndGift> couponAndGiftList) {
		this.couponAndGiftList = couponAndGiftList;
	}

	public Integer getIsWhiteList() {
		return isWhiteList;
	}

	public void setIsWhiteList(Integer isWhiteList) {
		this.isWhiteList = isWhiteList;
	}
	
}