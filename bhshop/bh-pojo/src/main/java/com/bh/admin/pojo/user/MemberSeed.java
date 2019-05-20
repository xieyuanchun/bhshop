package com.bh.admin.pojo.user;

import java.util.Date;

public class MemberSeed {
    private Integer id;

    private Integer mId;//用户ID

    private Integer smId;//种子模型ID

    private Integer gainRate;//生成值  0<值<=100
    
    private Integer mytimes;//这是第几次购买种子

    private Date getTime;//领取时间
    
    private Integer status;//0未付款1签到中2已收益
    
    private Integer orderseedId;//orderSeed表的id
    
    private Integer bouns;//可领取的金额
    
    private Integer type;//0代表种子，1代表土地，2代表鱼苗,3代表工具

    private String note;//备注

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

    public Integer getSmId() {
        return smId;
    }

    public void setSmId(Integer smId) {
        this.smId = smId;
    }

    public Integer getGainRate() {
        return gainRate;
    }

    public void setGainRate(Integer gainRate) {
        this.gainRate = gainRate;
    }

    public Date getGetTime() {
        return getTime;
    }

    public void setGetTime(Date getTime) {
        this.getTime = getTime;
    }

	public Integer getMytimes() {
		return mytimes;
	}

	public void setMytimes(Integer mytimes) {
		this.mytimes = mytimes;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOrderseedId() {
		return orderseedId;
	}

	public void setOrderseedId(Integer orderseedId) {
		this.orderseedId = orderseedId;
	}

	public Integer getBouns() {
		return bouns;
	}

	public void setBouns(Integer bouns) {
		this.bouns = bouns;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
    
	
	
    
}