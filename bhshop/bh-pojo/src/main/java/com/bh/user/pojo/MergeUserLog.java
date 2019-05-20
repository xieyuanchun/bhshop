package com.bh.user.pojo;

import java.util.Date;

public class MergeUserLog {
    private Integer mergeId;

    private Integer srcUserId;

    private String srcOpenid;

    private Integer destUserId;

    private String couponId;

    private String orderId;

    private Date addTime;

    private Date editTime;

    public Integer getMergeId() {
        return mergeId;
    }

    public void setMergeId(Integer mergeId) {
        this.mergeId = mergeId;
    }

    public Integer getSrcUserId() {
        return srcUserId;
    }

    public void setSrcUserId(Integer srcUserId) {
        this.srcUserId = srcUserId;
    }

    public String getSrcOpenid() {
        return srcOpenid;
    }

    public void setSrcOpenid(String srcOpenid) {
        this.srcOpenid = srcOpenid == null ? null : srcOpenid.trim();
    }

    public Integer getDestUserId() {
        return destUserId;
    }

    public void setDestUserId(Integer destUserId) {
        this.destUserId = destUserId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId == null ? null : couponId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
}