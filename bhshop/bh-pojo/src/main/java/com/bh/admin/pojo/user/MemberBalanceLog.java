package com.bh.admin.pojo.user;

import java.util.Date;

public class MemberBalanceLog {
	//主键id
    private Integer id;
    //用户id
    private Integer mId;
    //金额 分
    private Integer money;
    //收支类型
    private Integer balanceType;
    //目标ID
    private Integer targetId;
    //目标ID类型
    private Integer targetType;
    //发生时间
    private Date ocTime;
    //起始页
    private String currentPage;
    //消费图片
    private String image;
    //消费描述
    private String name;
    //金额 元
    private Double realMoney;

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

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(Integer balanceType) {
        this.balanceType = balanceType;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public Date getOcTime() {
        return ocTime;
    }

    public void setOcTime(Date ocTime) {
        this.ocTime = ocTime;
    }

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(Double realMoney) {
		this.realMoney = realMoney;
	}
    
}