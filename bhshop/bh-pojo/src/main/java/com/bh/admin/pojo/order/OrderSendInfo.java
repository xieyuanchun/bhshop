package com.bh.admin.pojo.order;

import java.util.Date;

public class OrderSendInfo {
	/********订单配送信息*********/
    private Integer id;

    private Integer orderShopId;//商家订单ID'

    private Integer mId;//用户id

    private Integer sId;//配送员ID

    private String sName;//配送员名称

    private Integer dState;//配送状态  0(初始状态)待发货   1发货中   2已签收

    private Date ocTime;//发生时间

    private Integer speedLevel;//送货速度评级  0默认未评级 1一星级 2二星级 3三星级 4四星级 5五星级

    private Integer sServiceLevel;//配送员服务评级 0默认未评级 1一星级 2二星级 3三星级 4四星级 5五星级

    private Integer packLevel;//快递包装评级 0默认未评级 1一星级 2二星级 3三星级 4四星级 5五星级'
    
    private String cancelReason; //取消原因
    
    private Integer tool; //交通工具1自行车；2.三轮车；3.电动车；4.大踏板电动车；5.摩托车;6小汽车；7小货车',
    
    private String jdOrderId; //京东id
    
    private String shopAddress; //商家地址
    
    private String userAddress; //买家用户地址
    
    private String shopName; //店铺名称
    
    private double realDeliveryPrice; //配送费
    
    private Integer deliveryTime ; //配送所需时间
    
    private double shopDistance; //配送员到商家距离单位km
    
    private double shoptoUserDistance; //商家到用户距离单位km
    
    private String deliveryStatus; //配送状态
    
    private String shopOrderNo; //商家订单号
    
    private String sendTool; //交通工具
    
    private Date sendTime;// 送达时间
    
    private Date deliverTime; //发货时间

    public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderShopId() {
        return orderShopId;
    }

    public void setOrderShopId(Integer orderShopId) {
        this.orderShopId = orderShopId;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName == null ? null : sName.trim();
    }

    public Integer getdState() {
        return dState;
    }

    public void setdState(Integer dState) {
        this.dState = dState;
    }

    public Date getOcTime() {
        return ocTime;
    }

    public void setOcTime(Date ocTime) {
        this.ocTime = ocTime;
    }

    public Integer getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(Integer speedLevel) {
        this.speedLevel = speedLevel;
    }

    public Integer getsServiceLevel() {
        return sServiceLevel;
    }

    public void setsServiceLevel(Integer sServiceLevel) {
        this.sServiceLevel = sServiceLevel;
    }

    public Integer getPackLevel() {
        return packLevel;
    }

    public void setPackLevel(Integer packLevel) {
        this.packLevel = packLevel;
    }

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Integer getTool() {
		return tool;
	}

	public void setTool(Integer tool) {
		this.tool = tool;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}

	public double getRealDeliveryPrice() {
		return realDeliveryPrice;
	}

	public void setRealDeliveryPrice(double realDeliveryPrice) {
		this.realDeliveryPrice = realDeliveryPrice;
	}

	public Integer getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Integer deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public double getShopDistance() {
		return shopDistance;
	}

	public void setShopDistance(double shopDistance) {
		this.shopDistance = shopDistance;
	}

	public double getShoptoUserDistance() {
		return shoptoUserDistance;
	}

	public void setShoptoUserDistance(double shoptoUserDistance) {
		this.shoptoUserDistance = shoptoUserDistance;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public String getShopOrderNo() {
		return shopOrderNo;
	}

	public void setShopOrderNo(String shopOrderNo) {
		this.shopOrderNo = shopOrderNo;
	}

	public String getSendTool() {
		return sendTool;
	}

	public void setSendTool(String sendTool) {
		this.sendTool = sendTool;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getJdOrderId() {
		return jdOrderId;
	}

	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}
    
	
}