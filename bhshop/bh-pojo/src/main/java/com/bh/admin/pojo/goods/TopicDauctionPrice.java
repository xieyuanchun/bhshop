package com.bh.admin.pojo.goods;

import java.util.Date;

public class TopicDauctionPrice {
    private Integer id;

    private Date curTime;

    private Integer price;
    
    private Integer goodsId;
    
    private Integer code;
    
    private String pointTime;
    
    private Double realPrice;
    
    private Integer dauctionId;
    
    private Integer tgId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    
    public Date getCurTime() {
		return curTime;
	}

	public void setCurTime(Date curTime) {
		this.curTime = curTime;
	}

	public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getPointTime() {
		return pointTime;
	}

	public void setPointTime(String pointTime) {
		this.pointTime = pointTime;
	}

	public Double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
	}

	public Integer getDauctionId() {
		return dauctionId;
	}

	public void setDauctionId(Integer dauctionId) {
		this.dauctionId = dauctionId;
	}

	public Integer getTgId() {
		return tgId;
	}

	public void setTgId(Integer tgId) {
		this.tgId = tgId;
	}
    
}