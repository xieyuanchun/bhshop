package com.bh.admin.pojo.goods;

import java.util.Date;

public class CashDeposit {
    private Integer id;

    private Integer mId;

    private Integer goodsId;

    private Integer hId;

    private Integer depositPrice;

    private Integer isrefund;

    private Date payTime;

    private Integer currentPeriods;

    private Date refundTime;

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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer gethId() {
        return hId;
    }

    public void sethId(Integer hId) {
        this.hId = hId;
    }

    public Integer getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(Integer depositPrice) {
        this.depositPrice = depositPrice;
    }

    public Integer getIsrefund() {
        return isrefund;
    }

    public void setIsrefund(Integer isrefund) {
        this.isrefund = isrefund;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getCurrentPeriods() {
        return currentPeriods;
    }

    public void setCurrentPeriods(Integer currentPeriods) {
        this.currentPeriods = currentPeriods;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }
}