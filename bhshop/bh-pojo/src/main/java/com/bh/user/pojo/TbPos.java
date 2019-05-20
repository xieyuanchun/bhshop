package com.bh.user.pojo;

import java.util.Date;

public class TbPos {
	//pos机id
    private Integer id;
    //是否存在pos机，1有,2无，3无(默认)
    private Integer existPos;
    //姓名
    private String name;
    //手机号
    private String phone;
    //处理状态handle_status(0未处理，1已处理)
    private Integer handleStatus;
    
    private Integer shopId;
    
    private Integer mId;//相当与shopId
    private String shopName;//相当与商家名称
    private String posMsg;//信息=姓名+手机号
    
    private String licenseNumber; //营业执照
    
    private String bankCardNo;//银行卡号
    
    private String identity; //省份证
    
    private Date addTime;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExistPos() {
        return existPos;
    }

    public void setExistPos(Integer existPos) {
        this.existPos = existPos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getPosMsg() {
		return posMsg;
	}

	public void setPosMsg(String posMsg) {
		this.posMsg = posMsg;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}