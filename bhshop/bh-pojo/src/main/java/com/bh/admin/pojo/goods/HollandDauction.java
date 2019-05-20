package com.bh.admin.pojo.goods;

import java.util.Date;

public class HollandDauction {
	//主键id
    private Integer id;
    //商品id
    private Integer goodsId;
    //最低价
    private Integer lowPrice;
    //降价时间区间
    private Integer timeSection;
    //降价价格区间
    private Integer scopePrice;
    //拍卖价
    private Integer dauctionPrice;
    //库存
    private Integer storeNums;
    //总期数
    private Integer totalPeriods;
    //当前第几期
    private Integer currentPeriods;
    //起始页
    private String currentPage;
    //流拍时间
    private Date loseTime;
    //商品上架时间
    private Date goodsUpTime;
    
    private double realLowPrice;
    private double realDauctionPrice;
    private double realScopePrice;
    //本期拍卖开始时间
    private Date startTime;
    
    

    public double getRealLowPrice() {
		return realLowPrice;
	}

	public void setRealLowPrice(double realLowPrice) {
		this.realLowPrice = realLowPrice;
	}

	public double getRealDauctionPrice() {
		return realDauctionPrice;
	}

	public void setRealDauctionPrice(double realDauctionPrice) {
		this.realDauctionPrice = realDauctionPrice;
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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Integer lowPrice) {
        this.lowPrice = lowPrice;
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

    public Integer getDauctionPrice() {
        return dauctionPrice;
    }

    public void setDauctionPrice(Integer dauctionPrice) {
        this.dauctionPrice = dauctionPrice;
    }

    public Integer getStoreNums() {
        return storeNums;
    }

    public void setStoreNums(Integer storeNums) {
        this.storeNums = storeNums;
    }

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getTotalPeriods() {
		return totalPeriods;
	}

	public void setTotalPeriods(Integer totalPeriods) {
		this.totalPeriods = totalPeriods;
	}

	public Integer getCurrentPeriods() {
		return currentPeriods;
	}

	public void setCurrentPeriods(Integer currentPeriods) {
		this.currentPeriods = currentPeriods;
	}

	public Date getLoseTime() {
		return loseTime;
	}

	public void setLoseTime(Date loseTime) {
		this.loseTime = loseTime;
	}

	public Date getGoodsUpTime() {
		return goodsUpTime;
	}

	public void setGoodsUpTime(Date goodsUpTime) {
		this.goodsUpTime = goodsUpTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
    
}