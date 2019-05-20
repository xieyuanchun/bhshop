package com.bh.goods.pojo;

import java.util.Date;
import java.util.List;

public class GoodsMsg {
    private Integer id;

    private String msg;

    private Integer shopId;

    private Date createTime;

    private Date updateTime;

    private Integer isfalgbypt;

    private Integer msgtype;

    private Integer isfalgbyshop;
    
    private int currentPage;
    private int pageSize;
    
    private boolean isRead;
	
    private List<InteractiveRecord> listRecord;
    
    
    public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public List<InteractiveRecord> getListRecord() {
		return listRecord;
	}

	public void setListRecord(List<InteractiveRecord> listRecord) {
		this.listRecord = listRecord;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsfalgbypt() {
        return isfalgbypt;
    }

    public void setIsfalgbypt(Integer isfalgbypt) {
        this.isfalgbypt = isfalgbypt;
    }

    public Integer getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(Integer msgtype) {
        this.msgtype = msgtype;
    }

    public Integer getIsfalgbyshop() {
        return isfalgbyshop;
    }

    public void setIsfalgbyshop(Integer isfalgbyshop) {
        this.isfalgbyshop = isfalgbyshop;
    }

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
    
}