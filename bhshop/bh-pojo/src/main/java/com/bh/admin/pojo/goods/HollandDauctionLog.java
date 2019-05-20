package com.bh.admin.pojo.goods;

import java.util.Date;

public class HollandDauctionLog {
	//主键id
    private Integer id;
    //用户id
    private Integer mId;
    //商品id
    private Integer goodsId;
    //配置id
    private Integer hId;
    //当前库存
    private Integer currentStore;
    //出价
    private Integer price;
    //出价时间
    private Date addTime;
    //拍卖状态
    private Integer dStatus;
    //当前第几期
    private Integer currentPeriods;
    //支付时间
    private Date payTime;
    
    private double realPrice;
    
    private String orderNo;
    
	private int currentPage;
    private int pageSize;
    private String goodsName;
    //支付状态
    private Integer payStatus;
    
    private String imgUrl;
    
    private String userName;
    
    private Date endTime;
    
    
    public double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}

    
    public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

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
    
}