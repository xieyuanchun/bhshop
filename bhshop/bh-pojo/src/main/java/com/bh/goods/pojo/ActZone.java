package com.bh.goods.pojo;

import java.util.Date;
import java.util.List;

public class ActZone {
	private Integer id;

    private String name;

    private Integer reid;

    private Integer isNormalShow;

    private Integer isCart;

    private Integer isCoupon;

    private Integer isRefund;

    private Integer isLockScore;

    private Integer freePostage;

    private String actUuid;

    private String imageUrl;

    private Integer levelNum;

    private Integer isLast;

    private Date addtime;

    private Date edittime;

    private String remark;

    private Integer sortNum;
    
    private String failuretime; //失效时间  2018.7.24 添加
    
    private List childList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsNormalShow() {
		return isNormalShow;
	}

	public void setIsNormalShow(Integer isNormalShow) {
		this.isNormalShow = isNormalShow;
	}

	public Integer getReid() {
		return reid;
	}

	public void setReid(Integer reid) {
		this.reid = reid;
	}

	public Integer getFreePostage() {
		return freePostage;
	}

	public void setFreePostage(Integer freePostage) {
		this.freePostage = freePostage;
	}

	public String getActUuid() {
		return actUuid;
	}

	public void setActUuid(String actUuid) {
		this.actUuid = actUuid;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getLevelNum() {
		return levelNum;
	}

	public void setLevelNum(Integer levelNum) {
		this.levelNum = levelNum;
	}

	public Integer getIsLast() {
		return isLast;
	}

	public void setIsLast(Integer isLast) {
		this.isLast = isLast;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getEdittime() {
		return edittime;
	}

	public void setEdittime(Date edittime) {
		this.edittime = edittime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public List getChildList() {
		return childList;
	}

	public void setChildList(List childList) {
		this.childList = childList;
	}

	public Integer getIsCart() {
		return isCart;
	}

	public void setIsCart(Integer isCart) {
		this.isCart = isCart;
	}

	public Integer getIsCoupon() {
		return isCoupon;
	}

	public void setIsCoupon(Integer isCoupon) {
		this.isCoupon = isCoupon;
	}

	public Integer getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(Integer isRefund) {
		this.isRefund = isRefund;
	}

	public Integer getIsLockScore() {
		return isLockScore;
	}

	public void setIsLockScore(Integer isLockScore) {
		this.isLockScore = isLockScore;
	}

	public String getFailuretime() {
		return failuretime;
	}

	public void setFailuretime(String failuretime) {
		this.failuretime = failuretime;
	}

    
	
}