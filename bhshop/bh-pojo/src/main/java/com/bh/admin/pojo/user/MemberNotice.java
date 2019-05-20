package com.bh.admin.pojo.user;

import java.util.Date;

public class MemberNotice {
    private Long id;

    private Integer mId;

    private String message;

    private Integer msgType;

    private Integer isRead;

    private Date addTime;

    private Date updateTime;

    private Date lastExcuTime;
    
    private Integer goodsId;

    private Integer goodsskuId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLastExcuTime() {
        return lastExcuTime;
    }

    public void setLastExcuTime(Date lastExcuTime) {
        this.lastExcuTime = lastExcuTime;
    }

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getGoodsskuId() {
		return goodsskuId;
	}

	public void setGoodsskuId(Integer goodsskuId) {
		this.goodsskuId = goodsskuId;
	}
    
    
}