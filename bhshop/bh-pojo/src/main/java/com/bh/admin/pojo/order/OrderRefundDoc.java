package com.bh.admin.pojo.order;

import java.util.Date;

public class OrderRefundDoc {
    private Integer id;

    private Integer orderId;

    private Integer mId;

    private Integer goodsId;

    private Integer skuId;

    private Integer amount;

    private Date addtime;

    private Integer status;

    private Date disposeTime;

    private String adminUser;

    private Integer shopId;

    private String note;
    
    private String reason;
    
    private Integer orderShopId;
    
    private String refuseReason;
    
    private String userAddress;
    
    private String returnAddress;

    private String expressName;

    private String expressNo;
    
    private int payWay;
    
    private Integer refundType;
    
    private String shopExpressName;

    private String shopExpressNo;
    
    
    private String mName; //用户名称
    
    private String goodsName; //商品名称
    
    private double realPrice; //转化分‘元’的价格
    
    private String orderShopNo; //商家订单编号
    
    private Date ordersTime; //下单时间
    
    private String mPhone;

    private String img;

    private Integer orderAmount;
    
    private Integer orderSkuId;
    private String statusName;//退款状态，0:申请退款 1:退款失败 2:退款成功
    private String shopName;
    private double realOrderAmount;
    private double realAmount;
    private String paymentNo;//支付单号 XIEYC
    private String orderNo;//订单号 XIEYC
    private Order order;//订单pojo  XIEYC
    
    private String afterSaleReasons;//售后理由
    private Date  logisticsValidTime; 
    private Date  refundValidTime;
    private Date  saveTime;
    private String specifications;
    private String voucherImage;
    
  

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getVoucherImage() {
		return voucherImage;
	}

	public void setVoucherImage(String voucherImage) {
		this.voucherImage = voucherImage;
	}

	public Date getLogisticsValidTime() {
		return logisticsValidTime;
	}

	public void setLogisticsValidTime(Date logisticsValidTime) {
		this.logisticsValidTime = logisticsValidTime;
	}

	public Date getRefundValidTime() {
		return refundValidTime;
	}

	public void setRefundValidTime(Date refundValidTime) {
		this.refundValidTime = refundValidTime;
	}

	public int getPayWay() {
		return payWay;
	}

	public void setPayWay(int payWay) {
		this.payWay = payWay;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	private Date addtime1;//cheng
    
    
    private OrderSku orderSku;
    private Integer reasonCode;
    

	public String getOrderShopNo() {
		return orderShopNo;
	}

	public void setOrderShopNo(String orderShopNo) {
		this.orderShopNo = orderShopNo;
	}

	
	public Date getOrdersTime() {
		return ordersTime;
	}

	public void setOrdersTime(Date ordersTime) {
		this.ordersTime = ordersTime;
	}

	public Integer getOrderShopId() {
		return orderShopId;
	}

	public void setOrderShopId(Integer orderShopId) {
		this.orderShopId = orderShopId;
	}

	public double getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
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

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public Date getDisposeTime() {
        return disposeTime;
    }

    public void setDisposeTime(Date disposeTime) {
        this.disposeTime = disposeTime;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser == null ? null : adminUser.trim();
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getmPhone() {
		return mPhone;
	}

	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Integer getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Date getAddtime1() {
		return addtime1;
	}

	public void setAddtime1(Date addtime1) {
		this.addtime1 = addtime1;
	}

	public OrderSku getOrderSku() {
		return orderSku;
	}

	public void setOrderSku(OrderSku orderSku) {
		this.orderSku = orderSku;
	}

	public Integer getOrderSkuId() {
		return orderSkuId;
	}

	public void setOrderSkuId(Integer orderSkuId) {
		this.orderSkuId = orderSkuId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Integer getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(Integer reasonCode) {
		this.reasonCode = reasonCode;
	}

	public double getRealOrderAmount() {
		return realOrderAmount;
	}

	public void setRealOrderAmount(double realOrderAmount) {
		this.realOrderAmount = realOrderAmount;
	}

	public double getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(double realAmount) {
		this.realAmount = realAmount;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public String getReturnAddress() {
		return returnAddress;
	}

	public void setReturnAddress(String returnAddress) {
		this.returnAddress = returnAddress;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getShopExpressName() {
		return shopExpressName;
	}

	public void setShopExpressName(String shopExpressName) {
		this.shopExpressName = shopExpressName;
	}

	public String getShopExpressNo() {
		return shopExpressNo;
	}

	public void setShopExpressNo(String shopExpressNo) {
		this.shopExpressNo = shopExpressNo;
	}

	public String getAfterSaleReasons() {
		return afterSaleReasons;
	}

	public void setAfterSaleReasons(String afterSaleReasons) {
		this.afterSaleReasons = afterSaleReasons;
	}

	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}

    
	
	
}