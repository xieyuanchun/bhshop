package com.bh.order.pojo;

import java.util.Date;

public class OrderSku {
    private Integer id;
    //订单ID
    private Integer orderId;
    //商家订单id
    private Integer orderShopId;
    //商品id
    private Integer goodsId;
    //商品名称
    private String goodsName;
    //skuid
    private Integer skuId;
    //sku编码
    private String skuNo;
    //商品图片
    private String skuImage;
    //商品数量
    private Integer skuNum;
    //市场价格单位分
    private Integer skuMarketPrice;
    //支付价格单位分
    private Integer skuSellPriceReal;
    //商品重量
    private Integer skuWeight;
    //规格属性数组
    private String skuValue;
    //是否已发货 :0未发货,1已发货
    private Boolean isSend;
    //是否评价0未评价.1已评价
    private Integer isRefund;
    //商品所属店铺id
    private Integer shopId;
    //配送状态0初始化，1抛单，2配送中，3待结算，4已完成
    private Integer dState;
    //配送员id
    private Integer sId;
    //是否退款0为正常,1退款中,2退款完成
    private Integer refund;
    //团购价格单位分(实际存储的是滨惠豆的数量)
    private Integer teamPrice;
    
    private String shopName; //店铺名称
    private double realMarketPrice; //市场价转换为‘元’的价格
    
    private double realSellPrice; //支付价转换为‘元’的价格  
    
    private Object valueObj;//2017-11-06程凤云添加
	private String shopOrderNo;//2017-11-16程凤云添加 商家订单号
    private double totalPrice ;//2017-11-16程凤云添加 总价
    private Date addtime;//下单时间
    private String mystatus;// 2017-11-07cheng添加
   
    private Integer storeNums;//库存
    private Integer shopNumAmount;//某种商品的销售总数   xieyc
    
    private Integer top;//排序  （xieyc）
    //用来控制前端的售后按钮,当值为1,3,4时前端显示售后服务按钮,当值为0时申请退款中,1退款失败,2退款成功,3退款被拒绝
    private Integer status;
    //是否显示物流按钮：0不显示,1显示(该字段是售后服务时用到：显示填写物流按钮)
    private Integer isShowlogButton;
    //评论按钮是否显示:不为空并且不为空串并且值为1的时候显示去评论的按钮(该字段是待评论时用到)
    private Integer isShowCommentButton;
    

    private Integer savePrice;
    
    private Long orderCatId;

    private Integer couponPrice;//优惠卷抵扣金额（单位分）(不同券按比例计算分配到这里抵扣

    private Integer skuPayPrice;//商品实际支付价格,等于 sku_num * sku_sell_price_real - save_price - coupon_price ' AFTER `coupon_price
    
    
    private Integer commentOwner;
    
    
    private String note;
    private String reason;
    private String afterSaleReasons;
    
    
    
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAfterSaleReasons() {
		return afterSaleReasons;
	}

	public void setAfterSaleReasons(String afterSaleReasons) {
		this.afterSaleReasons = afterSaleReasons;
	}

	public Integer getCommentOwner() {
		return commentOwner;
	}

	public void setCommentOwner(Integer commentOwner) {
		this.commentOwner = commentOwner;
	}

	public Long getOrderCatId() {
		return orderCatId;
	}

	public void setOrderCatId(Long orderCatId) {
		this.orderCatId = orderCatId;
	}

	public Integer getSavePrice() {
		return savePrice;
	}

	public void setSavePrice(Integer savePrice) {
		this.savePrice = savePrice;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getShopNumAmount() {
		return shopNumAmount;
	}

	public void setShopNumAmount(Integer shopNumAmount) {
		this.shopNumAmount = shopNumAmount;
	}

	public String getMystatus() {
		return mystatus;
	}

	public void setMystatus(String mystatus) {
		this.mystatus = mystatus;
	}

	public Object getValueObj() {
		return valueObj;
	}

	public void setValueObj(Object valueObj) {
		this.valueObj = valueObj;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public double getRealMarketPrice() {
		return realMarketPrice;
	}

	public void setRealMarketPrice(double realMarketPrice) {
		this.realMarketPrice = realMarketPrice;
	}

	public double getRealSellPrice() {
		return realSellPrice;
	}

	public void setRealSellPrice(double realSellPrice) {
		this.realSellPrice = realSellPrice;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderShopId() {
		return orderShopId;
	}

	public void setOrderShopId(Integer orderShopId) {
		this.orderShopId = orderShopId;
	}

	public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo == null ? null : skuNo.trim();
    }

    public String getSkuImage() {
        return skuImage;
    }

    public void setSkuImage(String skuImage) {
        this.skuImage = skuImage == null ? null : skuImage.trim();
    }

    public Integer getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(Integer skuNum) {
        this.skuNum = skuNum;
    }

    public Integer getSkuMarketPrice() {
        return skuMarketPrice;
    }

    public void setSkuMarketPrice(Integer skuMarketPrice) {
        this.skuMarketPrice = skuMarketPrice;
    }

    public Integer getSkuSellPriceReal() {
        return skuSellPriceReal;
    }

    public void setSkuSellPriceReal(Integer skuSellPriceReal) {
        this.skuSellPriceReal = skuSellPriceReal;
    }

    public Integer getSkuWeight() {
        return skuWeight;
    }

    public void setSkuWeight(Integer skuWeight) {
        this.skuWeight = skuWeight;
    }

    public Boolean getIsSend() {
        return isSend;
    }

    public void setIsSend(Boolean isSend) {
        this.isSend = isSend;
    }

    public Integer getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Integer isRefund) {
        this.isRefund = isRefund;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getSkuValue() {
        return skuValue;
    }

    public void setSkuValue(String skuValue) {
        this.skuValue = skuValue == null ? null : skuValue.trim();
    }
    
    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }

    public Integer getdState() {
        return dState;
    }

    public void setdState(Integer dState) {
        this.dState = dState;
    }

	public String getShopOrderNo() {
		return shopOrderNo;
	}

	public void setShopOrderNo(String shopOrderNo) {
		this.shopOrderNo = shopOrderNo;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getRefund() {
		return refund;
	}

	public void setRefund(Integer refund) {
		this.refund = refund;
	}

	public Integer getStoreNums() {
		return storeNums;
	}

	public void setStoreNums(Integer storeNums) {
		this.storeNums = storeNums;
	}

	public Integer getIsShowlogButton() {
		return isShowlogButton;
	}

	public void setIsShowlogButton(Integer isShowlogButton) {
		this.isShowlogButton = isShowlogButton;
	}

	public Integer getIsShowCommentButton() {
		return isShowCommentButton;
	}

	public void setIsShowCommentButton(Integer isShowCommentButton) {
		this.isShowCommentButton = isShowCommentButton;
	}

	public Integer getTeamPrice() {
		return teamPrice;
	}

	public void setTeamPrice(Integer teamPrice) {
		this.teamPrice = teamPrice;
	}

	public Integer getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(Integer couponPrice) {
		this.couponPrice = couponPrice;
	}

	public Integer getSkuPayPrice() {
		return skuPayPrice;
	}

	public void setSkuPayPrice(Integer skuPayPrice) {
		this.skuPayPrice = skuPayPrice;
	}
	
	
	
	
    
}