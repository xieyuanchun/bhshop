package com.bh.admin.pojo.order;

import java.util.Date;

public class MsnApply {
    private Integer apymsnId;

    private Integer shopId;

    private String signature;

    private String paramter;

    private Integer sendStatus;

    private Date addTime;

    private Date editTime;

    private Date sendTime;

    private Long successCount;

    private String template;

    private Integer tempReviewStatus;

    private String tempReviewRemark;

    private Integer groupNum;

    private Date payTime;

    private Integer money;

    private Integer payStatus;

    private String orderNo;

    private Date validTime;

    private Integer memberType;
    
    private Integer isFreeNum;

    public Integer getIsFreeNum() {
		return isFreeNum;
	}

	public void setIsFreeNum(Integer isFreeNum) {
		this.isFreeNum = isFreeNum;
	}

	public Integer getApymsnId() {
        return apymsnId;
    }

    public void setApymsnId(Integer apymsnId) {
        this.apymsnId = apymsnId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public String getParamter() {
        return paramter;
    }

    public void setParamter(String paramter) {
        this.paramter = paramter == null ? null : paramter.trim();
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
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

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template == null ? null : template.trim();
    }

    public Integer getTempReviewStatus() {
        return tempReviewStatus;
    }

    public void setTempReviewStatus(Integer tempReviewStatus) {
        this.tempReviewStatus = tempReviewStatus;
    }

    public String getTempReviewRemark() {
        return tempReviewRemark;
    }

    public void setTempReviewRemark(String tempReviewRemark) {
        this.tempReviewRemark = tempReviewRemark == null ? null : tempReviewRemark.trim();
    }

    public Integer getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(Integer groupNum) {
        this.groupNum = groupNum;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

   
}