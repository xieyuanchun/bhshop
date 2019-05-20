package com.bh.admin.pojo.order;

import java.util.Date;

public class MsnTemplate {
    private Integer tempId;

    private Integer shopId;

    private Integer apymsnId;

    private String contains;

    private Integer reviewResult;

    private Integer platformReviewResult;

    private String reviewResultRemkar;

    private Date platformReviewSubmitTime;

    private Date platformReviewEndTime;

    private Date editTime;

    private Date addTime;

    private String reviewerName;

    public Integer getTempId() {
        return tempId;
    }

    public void setTempId(Integer tempId) {
        this.tempId = tempId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getApymsnId() {
        return apymsnId;
    }

    public void setApymsnId(Integer apymsnId) {
        this.apymsnId = apymsnId;
    }

    public String getContains() {
        return contains;
    }

    public void setContains(String contains) {
        this.contains = contains == null ? null : contains.trim();
    }

    public Integer getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Integer reviewResult) {
        this.reviewResult = reviewResult;
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
        this.reviewResultRemkar = reviewResultRemkar == null ? null : reviewResultRemkar.trim();
    }

    public Date getPlatformReviewSubmitTime() {
        return platformReviewSubmitTime;
    }

    public void setPlatformReviewSubmitTime(Date platformReviewSubmitTime) {
        this.platformReviewSubmitTime = platformReviewSubmitTime;
    }

    public Date getPlatformReviewEndTime() {
        return platformReviewEndTime;
    }

    public void setPlatformReviewEndTime(Date platformReviewEndTime) {
        this.platformReviewEndTime = platformReviewEndTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName == null ? null : reviewerName.trim();
    }
}