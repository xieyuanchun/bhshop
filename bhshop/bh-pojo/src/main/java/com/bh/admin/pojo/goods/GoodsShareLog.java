package com.bh.admin.pojo.goods;

public class GoodsShareLog {
    private Integer id;

    private Integer mId;

    private Integer rMId;

    private Integer reMId;

    private String shareUrl;

    private String orderNo;

    private String teamNo;

    private Integer skuId;

    private Integer shareType;

    private Integer orderType;

    private String openId;
    
    private Integer visit;
    
    private Integer shareNum;
    
    private String mName; //用户昵称
    
    private String reMName; // 推荐人昵称
    
    private String goodsName; //商品名称
    
    private String skuValue; //商品sku值
    
    private String goodsImage; // 商品图片

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

    public Integer getrMId() {
        return rMId;
    }

    public void setrMId(Integer rMId) {
        this.rMId = rMId;
    }

    public Integer getReMId() {
        return reMId;
    }

    public void setReMId(Integer reMId) {
        this.reMId = reMId;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl == null ? null : shareUrl.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getTeamNo() {
        return teamNo;
    }

    public void setTeamNo(String teamNo) {
        this.teamNo = teamNo == null ? null : teamNo.trim();
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getShareType() {
        return shareType;
    }

    public void setShareType(Integer shareType) {
        this.shareType = shareType;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getReMName() {
		return reMName;
	}

	public void setReMName(String reMName) {
		this.reMName = reMName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSkuValue() {
		return skuValue;
	}

	public void setSkuValue(String skuValue) {
		this.skuValue = skuValue;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public Integer getVisit() {
		return visit;
	}

	public void setVisit(Integer visit) {
		this.visit = visit;
	}

	public Integer getShareNum() {
		return shareNum;
	}

	public void setShareNum(Integer shareNum) {
		this.shareNum = shareNum;
	}
    
}