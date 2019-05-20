package com.bh.goods.pojo;

public class TopicDauction {
	//主键id
    private Integer id;
    //活动商品明细id
    private Integer tgId;
    //最低价
    private Integer lowPrice;
    //降价时间，单位分钟
    private Integer timeSection;
    //降价值，单位分
    private Integer scopePrice;
    //拍卖价 单位分 
    private Integer dauctionPrice;
    //活动商品id（scj_增）
    private Integer goodsId;
    //活动id（scj_增）
    private Integer actId;
    //备注（scj_增）
    private String remark;
    //最低价 单位元（scj_增）
    private Double realLowPrice;
    //降价值 单位元（scj_增）
    private Double realScopePrice;
    
    private Double realDauctionPrice;
    
    private Integer kuNums; //拍卖库存
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTgId() {
        return tgId;
    }

    public void setTgId(Integer tgId) {
        this.tgId = tgId;
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

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getRealLowPrice() {
		return realLowPrice;
	}

	public void setRealLowPrice(Double realLowPrice) {
		this.realLowPrice = realLowPrice;
	}

	public Double getRealScopePrice() {
		return realScopePrice;
	}

	public void setRealScopePrice(Double realScopePrice) {
		this.realScopePrice = realScopePrice;
	}

	public Integer getDauctionPrice() {
		return dauctionPrice;
	}

	public void setDauctionPrice(Integer dauctionPrice) {
		this.dauctionPrice = dauctionPrice;
	}

	public Double getRealDauctionPrice() {
		return realDauctionPrice;
	}

	public void setRealDauctionPrice(Double realDauctionPrice) {
		this.realDauctionPrice = realDauctionPrice;
	}

	public Integer getKuNums() {
		return kuNums;
	}

	public void setKuNums(Integer kuNums) {
		this.kuNums = kuNums;
	}
    
}