package com.bh.admin.vo;

import java.util.Date;

/**
 * 群发记录
 */
public class MsnList {
    private Integer shopId;
    private Date addTime;
    private Double money;
    private Integer payStatus;
    private Integer groupNum;
    private String paramter;
    private Integer tempReviewStatus;
    private String tempReviewRemark;
    private Integer platformReviewResult;
    private String reviewResultRemkar;
    private Integer apymsnId;

    public void setMoney(Double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "MsnList{" +
                "shopId=" + shopId +
                ", addTime=" + addTime +
                ", money=" + money +
                ", payStatus=" + payStatus +

                ", groupNum=" + groupNum +
                ", paramter='" + paramter + '\'' +
                ", tempReviewStatus=" + tempReviewStatus +
                ", tempReviewRemark='" + tempReviewRemark + '\'' +
                ", platformReviewResult=" + platformReviewResult +
                ", reviewResultRemkar='" + reviewResultRemkar + '\'' +
                ", apymsnId=" + apymsnId +
                '}';
    }

    public Integer getApymsnId() {
        return apymsnId;
    }

    public void setApymsnId(Integer apymsnId) {
        this.apymsnId = apymsnId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public double getMoney() {
        return money;
    }


    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public void setTempReviewStatus(Integer tempReviewStatus) {
        this.tempReviewStatus = tempReviewStatus;
    }

    public Integer getShopId() {

        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }


    public String getParamter() {
        return paramter;
    }

    public void setParamter(String paramter) {
        this.paramter = paramter;
    }


    public String getTempReviewRemark() {
        return tempReviewRemark;
    }

    public void setTempReviewRemark(String tempReviewRemark) {
        this.tempReviewRemark = tempReviewRemark;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public Integer getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(Integer groupNum) {
        this.groupNum = groupNum;
    }

    public Integer getTempReviewStatus() {
        return tempReviewStatus;
    }

    public Integer getPlatformReviewResult() {
        return platformReviewResult;
    }

    public void setPlatformReviewResult(Integer platformReviewResult) {
        this.platformReviewResult = platformReviewResult;
    }

    public String getReviewResultRemkar() {
        return reviewResultRemkar;
    }

    public void setReviewResultRemkar(String reviewResultRemkar) {
        this.reviewResultRemkar = reviewResultRemkar;
    }


}
