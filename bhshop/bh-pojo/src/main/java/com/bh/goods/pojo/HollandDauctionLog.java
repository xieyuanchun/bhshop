package com.bh.goods.pojo;

import java.util.Date;

public class HollandDauctionLog {
    private Integer id;

    private Integer mId;

    private Integer goodsId;

    private Integer hId;

    private Integer currentStore;

    private Integer price;

    private Date addTime;

    private Integer dStatus;
    
    private String currentPage;
    
    private String mNick;
    //当前第几期
    private Integer currentPeriods;
    //支付时间
    private Date payTime;
    
    private Integer payStatus;
    
    private Double realPrice;
    
    private String headImg;
    
    private String orderNo;
    
    private Date endTime;
    
    private Integer currentPageIndex;
    
    private Integer pageSize;
    //当前最高价
    private Double highPrice; 

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

    public Integer gethId() {
        return hId;
    }

    public void sethId(Integer hId) {
        this.hId = hId;
    }

    public Integer getCurrentStore() {
        return currentStore;
    }

    public void setCurrentStore(Integer currentStore) {
        this.currentStore = currentStore;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getdStatus() {
        return dStatus;
    }

    public void setdStatus(Integer dStatus) {
        this.dStatus = dStatus;
    }

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getmNick() {
		return mNick;
	}

	public void setmNick(String mNick) {
		this.mNick = mNick;
	}

	public Integer getCurrentPeriods() {
		return currentPeriods;
	}

	public void setCurrentPeriods(Integer currentPeriods) {
		this.currentPeriods = currentPeriods;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getCurrentPageIndex() {
		return currentPageIndex;
	}

	public void setCurrentPageIndex(Integer currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Double getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(Double highPrice) {
		this.highPrice = highPrice;
	}
	
}