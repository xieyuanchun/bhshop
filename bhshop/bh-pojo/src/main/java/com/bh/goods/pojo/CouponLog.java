package com.bh.goods.pojo;

import java.util.Date;

public class CouponLog {
    private Integer id;

    private Integer mId;

    private Integer couponId;

    private Integer orderId;

    private Date useTime;

    private Date createTime;

    private Date expireTime;

    private Integer status;

    private Integer shopId;

    private String currentPage; //分页
    
    private String statusLog; //当前的前端要显示的优惠劵1 未使用 2已使用 3已过期
    
    private String percentage; //剩余百分比
    
    private int num;//某种优化卷的数量、
    
    private int couponType;
    
    
    private String typeStr;
    
    private String applyStr;
    
    private String applyName;
    
    private String effectiveTime;
    
    //获取途径，0平台发放，1兑换
    private Integer getWay;
    
    
    
	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public String getApplyStr() {
		return applyStr;
	}

	public void setApplyStr(String applyStr) {
		this.applyStr = applyStr;
	}

	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public int getCouponType() {
		return couponType;
	}

	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	//Coupon表信息
    private Integer type;

    private String title;

    private String remark;

    private Integer amount;//分数据库

    private String needAmount;//元 前端使用

    private Integer stock;

    private Integer sended;

    private Integer used;

    private Date startTime;

    private Date endTime;

    private Integer need_amount; //分数据库
    
    private Date start_time;

    private Date end_time;
    
    private String amount2;//元 前端使用

   
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    
    
    public Date getUseTime() {
		return useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

    

	public String getNeedAmount() {
		return needAmount;
	}

	public void setNeedAmount(String needAmount) {
		this.needAmount = needAmount;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getSended() {
		return sended;
	}

	public void setSended(Integer sended) {
		this.sended = sended;
	}

	public Integer getUsed() {
		return used;
	}

	public void setUsed(Integer used) {
		this.used = used;
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

	public String getStatusLog() {
		return statusLog;
	}

	public void setStatusLog(String statusLog) {
		this.statusLog = statusLog;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public Integer getNeed_amount() {
		return need_amount;
	}

	public void setNeed_amount(Integer need_amount) {
		this.need_amount = need_amount;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public String getAmount2() {
		return amount2;
	}

	public void setAmount2(String amount2) {
		this.amount2 = amount2;
	}

	public Integer getGetWay() {
		return getWay;
	}

	public void setGetWay(Integer getWay) {
		this.getWay = getWay;
	}
}