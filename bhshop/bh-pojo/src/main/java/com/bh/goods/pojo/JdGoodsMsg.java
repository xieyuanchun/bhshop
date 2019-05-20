package com.bh.goods.pojo;

import java.util.Date;

public class JdGoodsMsg {
    private Integer id;

    private Long jdMsgId;

    private Integer msgType;

    private Long jdSkuNo;

    private String msgContent;

    private Integer isLook;

    private Date addTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getJdMsgId() {
		return jdMsgId;
	}

	public void setJdMsgId(Long jdMsgId) {
		this.jdMsgId = jdMsgId;
	}

	public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }
    
    public Long getJdSkuNo() {
		return jdSkuNo;
	}

	public void setJdSkuNo(Long jdSkuNo) {
		this.jdSkuNo = jdSkuNo;
	}

	public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent == null ? null : msgContent.trim();
    }

    public Integer getIsLook() {
        return isLook;
    }

    public void setIsLook(Integer isLook) {
        this.isLook = isLook;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}