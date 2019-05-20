package com.bh.admin.pojo.order;

import java.util.Date;

public class OrderRefundDocStep {
    private Integer id;

    private Integer orderRefundDocId;

    private Date addtime;

    private Integer mid;

    private Integer refundType;

    private Integer step;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderRefundDocId() {
        return orderRefundDocId;
    }

    public void setOrderRefundDocId(Integer orderRefundDocId) {
        this.orderRefundDocId = orderRefundDocId;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getRefundType() {
        return refundType;
    }

    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }
}