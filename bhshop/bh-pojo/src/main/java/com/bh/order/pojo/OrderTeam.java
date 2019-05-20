package com.bh.order.pojo;

import java.util.Date;

public class OrderTeam {
    private Integer id;

    private Integer mId;

    private Date createTime;

    private Date endTime;

    private Integer isOwner;

    private String teamNo;

    private String orderNo;

    private Integer goodsSkuId;

    private Integer status;
    
    private Date createTeamTime;

    private Integer type;
    
    private Integer goodsId;
    
    private String mName; //下单用户昵称
    
    private String goodsSku; //商品规格
    
    private String goodsName; //商品名称
    
    private double price; //单价
    
    private Integer num; //数量
    
    private double totalPrice; //订单总价
    
    private String imageUrl; //商品图片
    
    private Integer teamNum; //开团总人数
    
    private Integer waitNum; //离开团还差几人
    
    private String statusName;
    
    private String waitTime; //倒计时
    
    private String username; //用户昵称
    
    private String headimgurl; //用户头像
    
    private Integer orderId; //订单id
    
    private Integer shopId;
    
    private TeamLastOne teamLastOne;//其他的头像
    
    private String currentPage;
    
    private String shopOrderNo;
    
    private String phone;
    
	private double realSavePrice;//滨惠豆抵扣的钱
	private double realCouponsPrice;//优惠卷抵扣的钱
	private double realPrice;//金额（总价-优惠卷-滨惠豆）
	private double realgDeliveryPrice;//邮费
	
    public double getRealgDeliveryPrice() {
		return realgDeliveryPrice;
	}

	public void setRealgDeliveryPrice(double realgDeliveryPrice) {
		this.realgDeliveryPrice = realgDeliveryPrice;
	}

	public double getRealSavePrice() {
		return realSavePrice;
	}

	public void setRealSavePrice(double realSavePrice) {
		this.realSavePrice = realSavePrice;
	}

	public double getRealCouponsPrice() {
		return realCouponsPrice;
	}

	public void setRealCouponsPrice(double realCouponsPrice) {
		this.realCouponsPrice = realCouponsPrice;
	}

	public double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getShopOrderNo() {
		return shopOrderNo;
	}

	public void setShopOrderNo(String shopOrderNo) {
		this.shopOrderNo = shopOrderNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getTeamNum() {
		return teamNum;
	}

	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}

	public Integer getWaitNum() {
		return waitNum;
	}

	public void setWaitNum(Integer waitNum) {
		this.waitNum = waitNum;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Integer isOwner) {
        this.isOwner = isOwner;
    }

    public String getTeamNo() {
        return teamNo;
    }

    public void setTeamNo(String teamNo) {
        this.teamNo = teamNo == null ? null : teamNo.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Integer getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Integer goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public Date getCreateTeamTime() {
		return createTeamTime;
	}

	public void setCreateTeamTime(Date createTeamTime) {
		this.createTeamTime = createTeamTime;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getGoodsSku() {
		return goodsSku;
	}

	public void setGoodsSku(String goodsSku) {
		this.goodsSku = goodsSku;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public TeamLastOne getTeamLastOne() {
		return teamLastOne;
	}

	public void setTeamLastOne(TeamLastOne teamLastOne) {
		this.teamLastOne = teamLastOne;
	}
    
	
}