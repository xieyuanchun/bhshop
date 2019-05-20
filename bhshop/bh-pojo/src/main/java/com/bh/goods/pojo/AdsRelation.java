package com.bh.goods.pojo;

public class AdsRelation {
    private Integer id;

    private Integer adsId;

    private Integer targetId;

    private Integer adsType;

    private Byte status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdsId() {
        return adsId;
    }

    public void setAdsId(Integer adsId) {
        this.adsId = adsId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getAdsType() {
        return adsType;
    }

    public void setAdsType(Integer adsType) {
        this.adsType = adsType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}