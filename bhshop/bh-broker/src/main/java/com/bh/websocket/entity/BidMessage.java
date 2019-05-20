package com.bh.websocket.entity;

import java.io.Serializable;

public class BidMessage implements Serializable {
    private String periodTag;  //拍卖标签
    private String goodsTag;  //拍卖标签
    private int type;    //消息类型:0-价格下降;1-流拍;2-有人出价;3-待支付
    private String sysCode;    //系统来源标识
    private int goodsId;    //商品id
    private String goodsName;  //商品名称
    private String goodsImage;  //商品图片
    private int invertSecond; //倒计时
    private int currentPeriods;    //当前第几期
    private long sysTime;    //系统当前时间
    private long changeTime;    //变化时间
    private long curTime;    //用户出价的时间

    private int prePrice; //上次价格
    private int auctionPrice; //用户出价

    private float floatPrePrice; //上次价格
    private float floatAuctionPrice; //用户出价

    private Integer orderId;    //订单id

    private int mid;//用户Id
    private String userName; //用户昵称
    private String headImg;    //用户头像
    private String message;    //消息

    public String getPeriodTag() {
        return periodTag;
    }

    public void setPeriodTag(String periodTag) {
        this.periodTag = periodTag;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public int getInvertSecond() {
        return invertSecond;
    }

    public void setInvertSecond(int invertSecond) {
        this.invertSecond = invertSecond;
    }

    public int getCurrentPeriods() {
        return currentPeriods;
    }

    public void setCurrentPeriods(int currentPeriods) {
        this.currentPeriods = currentPeriods;
    }

    public long getSysTime() {
        return sysTime;
    }

    public void setSysTime(long sysTime) {
        this.sysTime = sysTime;
    }

    public long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(long changeTime) {
        this.changeTime = changeTime;
    }

    public long getCurTime() {
        return curTime;
    }

    public void setCurTime(long curTime) {
        this.curTime = curTime;
    }

    public int getPrePrice() {
        return prePrice;
    }

    public void setPrePrice(int prePrice) {
        this.prePrice = prePrice;
    }

    public int getAuctionPrice() {
        return auctionPrice;
    }

    public void setAuctionPrice(int auctionPrice) {
        this.auctionPrice = auctionPrice;
    }

    public float getFloatPrePrice() {
        return floatPrePrice;
    }

    public void setFloatPrePrice(float floatPrePrice) {
        this.floatPrePrice = floatPrePrice;
    }

    public float getFloatAuctionPrice() {
        return floatAuctionPrice;
    }

    public void setFloatAuctionPrice(float floatAuctionPrice) {
        this.floatAuctionPrice = floatAuctionPrice;
    }

    public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BidMessage{" +
                "periodTag='" + periodTag + '\'' +
                ", type=" + type +
                ", sysCode='" + sysCode + '\'' +
                ", goodsId=" + goodsId +
                ", goodsImage='" + goodsImage + '\'' +
                ", invertSecond=" + invertSecond +
                ", currentPeriods=" + currentPeriods +
                ", sysTime=" + sysTime +
                ", changeTime=" + changeTime +
                ", curTime=" + curTime +
                ", prePrice=" + prePrice +
                ", auctionPrice=" + auctionPrice +
                ", orderId=" + orderId +
                ", mid=" + mid +
                ", userName='" + userName + '\'' +
                ", headImg='" + headImg + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
