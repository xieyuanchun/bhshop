package com.bh.admin.pojo.goods;

import java.util.Date;

public class TopicBargainLog {
    private Integer id;

    private Integer mId;

    private Integer tgId;

    private Integer bargainPrice;

    private Integer isOwner;
    
    private Date addTime;
    
    private Integer actBalance;
    
    private Integer status;
    
    private String bargainNo;
    
    private String openId;
    
    private String currentPage; //起始页
    
    private String mName; //用户昵称
    
    private String goodsName; //商品名称
    
    private String topicName; //活动名称
    
    private Double realBargainPrice;
    
    private Double realactBalance;
    
    private Integer addressId;//用户地址id
    
    private Integer skuId;//商品skuId
    
    private String nickName; //wx用户昵称
    
    private String headImgUrl; //wx用户头像
    
    private Integer shopId;
    
	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBargainNo() {
		return bargainNo;
	}

	public void setBargainNo(String bargainNo) {
		this.bargainNo = bargainNo;
	}

	public Double getRealBargainPrice() {
		return realBargainPrice;
	}

	public void setRealBargainPrice(Double realBargainPrice) {
		this.realBargainPrice = realBargainPrice;
	}

	public Double getRealactBalance() {
		return realactBalance;
	}

	public void setRealactBalance(Double realactBalance) {
		this.realactBalance = realactBalance;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
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

    public Integer getTgId() {
        return tgId;
    }

    public void setTgId(Integer tgId) {
        this.tgId = tgId;
    }

    public Integer getBargainPrice() {
        return bargainPrice;
    }

    public void setBargainPrice(Integer bargainPrice) {
        this.bargainPrice = bargainPrice;
    }

    public Integer getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Integer isOwner) {
        this.isOwner = isOwner;
    }
    

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getActBalance() {
		return actBalance;
	}

	public void setActBalance(Integer actBalance) {
		this.actBalance = actBalance;
	}
    
}