package com.bh.user.pojo;

import java.util.Date;

public class MemerScoreLog {
   /**用户积分日志表**/
	private Integer id;

    private Integer mId;//用户ID
    
    private Integer smId;//种子模型ID

    private Integer ssrId;//积分规则ID

    private Date createTime;//时间
    
    private Integer times;//累计签到的天数
    
    private Integer score;//本次签到的积分是多少
    private String param;
    
    private Integer isDel;//0代表该记录未删除，1代表该记录已删除
    
    private Integer orderseedId;//现在支持多个商品对次购买的需求

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

    public Integer getSsrId() {
        return ssrId;
    }

    public void setSsrId(Integer ssrId) {
        this.ssrId = ssrId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Integer getSmId() {
		return smId;
	}

	public void setSmId(Integer smId) {
		this.smId = smId;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getOrderseedId() {
		return orderseedId;
	}

	public void setOrderseedId(Integer orderseedId) {
		this.orderseedId = orderseedId;
	}
    
    
}