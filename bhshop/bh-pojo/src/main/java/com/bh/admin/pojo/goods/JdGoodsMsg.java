package com.bh.admin.pojo.goods;

import java.util.Date;

public class JdGoodsMsg {
	//主键id
    private Integer id;
    //京东消息id
    private Long jdMsgId;
    //消息类型
    private Integer msgType;
    //京东商品编码
    private Long jdSkuNo;
    //内容详情
    private String msgContent;
    //是否已阅
    private Integer isLook;
    //添加时间
    private Date addTime;
    //京东消息推送时间
    private Date sendTime;
    //起始页
    private String currentPage;
    //页大小
    private String pageSize;
    //jdSkuNo数组，测试传参用
    private String[] jdSkuNoStr;

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

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public Long getJdSkuNo() {
		return jdSkuNo;
	}

	public void setJdSkuNo(Long jdSkuNo) {
		this.jdSkuNo = jdSkuNo;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String[] getJdSkuNoStr() {
		return jdSkuNoStr;
	}

	public void setJdSkuNoStr(String[] jdSkuNoStr) {
		this.jdSkuNoStr = jdSkuNoStr;
	}
}