package com.bh.admin.pojo.goods;

import java.io.Serializable;
import java.util.Date;

public class Topic implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer type;

    private Integer typeid;

    private String name;

    private Date applyStime;

    private Date applyEtime;

    private Date startTime;

    private Date endTime;

    private Integer status;

    private Integer listorder;

    private String posid;
    
    private String thumb;

    private String thumbs;

    private String catid;
    
    private Integer isDelete;
    
    
    
	private Date applyTime;
	
    private Date actTime;
    
    private String currentPage;
    
    private String typeName;
    
    private Integer num;
    private Integer appTimeStatus;//报名:0代表没有活动时间，1代表活动未开始，2代表活动进行中，3代表活动结束
    private Integer topicTimeStatus;//活动:0代表没有活动时间，1代表活动未开始，2代表活动进行中，3代表活动结束
    
    private String goodsStrs ; //活动下的商品id--"1,2,3"
    
    
    private Integer shopId;
    
  	public Integer getShopId() {
  		return shopId;
  	}

  	public void setShopId(Integer shopId) {
  		this.shopId = shopId;
  	}
  	
    public String getGoodsStrs() {
		return goodsStrs;
	}

	public void setGoodsStrs(String goodsStrs) {
		this.goodsStrs = goodsStrs;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getActTime() {
		return actTime;
	}

	public void setActTime(Date actTime) {
		this.actTime = actTime;
	}
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getApplyStime() {
        return applyStime;
    }

    public void setApplyStime(Date applyStime) {
        this.applyStime = applyStime;
    }

    public Date getApplyEtime() {
        return applyEtime;
    }

    public void setApplyEtime(Date applyEtime) {
        this.applyEtime = applyEtime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getListorder() {
        return listorder;
    }

    public void setListorder(Integer listorder) {
        this.listorder = listorder;
    }

    public String getPosid() {
        return posid;
    }

    public void setPosid(String posid) {
        this.posid = posid == null ? null : posid.trim();
    }

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getThumbs() {
		return thumbs;
	}

	public void setThumbs(String thumbs) {
		this.thumbs = thumbs;
	}

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	public Integer getAppTimeStatus() {
		return appTimeStatus;
	}

	public void setAppTimeStatus(Integer appTimeStatus) {
		this.appTimeStatus = appTimeStatus;
	}

	public Integer getTopicTimeStatus() {
		return topicTimeStatus;
	}

	public void setTopicTimeStatus(Integer topicTimeStatus) {
		this.topicTimeStatus = topicTimeStatus;
	}
   
	
	
    
}