package com.bh.admin.pojo.user;

import java.util.Date;

public class MemberSend {
    private Integer mId;//用好的id

    private String fullName;//用户的真实姓名

    private String identity;//用户的身份证号
    
    private String idcardImgUrl;//身份证的正面照

    private String idcardHandImgUrl;//与身份证的正面照的合照

    private String address;//配送员的住处

    private Date time;//配送员注册时间

    private String scope;//接单的范围

    private String income;//接单的范围

    private Integer tool;//配送员的交通工具：1自行车；2.三轮车；3.电动车；4.大踏板电动车；5.摩托车;6小汽车；7小货车

    private Double lon;//

    private Double lat;//

    private Integer status;//状态：0待审核，1审核成功，2审核失败

    private Integer sex;//1男2女：性别

    private Integer age;//年龄
    
    private Integer type;//接单类型1全部，2近距离，3远距离，4高价，5低价
    
    private Integer totalNum;//已完成单量
    
    private Integer totalIncome;//总收入
    
    private Integer balance;//账户余额
    
    private Integer cashPledge; //押金
    
    private Integer online;//-1下线 0在线 1收工
    
    
    
    
    private String sendType; //接单类型
    
    private String sendTool; //交通工具
    
    private double realTotalIncome; //总收入，单位元
    
    private double realBalance; //账户余额，单位元
    
    private double realCashPledge; //押金， 单位元
    
    private String auditStatus; //审核状态
    
    private double realScope; //配送范围 单位 km
    
    private String headImage; //头像
    
    private String phoneNumber; //联系方式
    
    
    

    private String levelName;//配送元旦饿级别名称
    private int mark;//服务分
    private Member member;
    private String phone;

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity == null ? null : identity.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope == null ? null : scope.trim();
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income == null ? null : income.trim();
    }

    public Integer getTool() {
        return tool;
    }

    public void setTool(Integer tool) {
        this.tool = tool;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(Integer totalIncome) {
		this.totalIncome = totalIncome;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getSendTool() {
		return sendTool;
	}

	public void setSendTool(String sendTool) {
		this.sendTool = sendTool;
	}

	public double getRealTotalIncome() {
		return realTotalIncome;
	}

	public void setRealTotalIncome(double realTotalIncome) {
		this.realTotalIncome = realTotalIncome;
	}

	public double getRealBalance() {
		return realBalance;
	}

	public void setRealBalance(double realBalance) {
		this.realBalance = realBalance;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public double getRealScope() {
		return realScope;
	}

	public void setRealScope(double realScope) {
		this.realScope = realScope;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getIdcardImgUrl() {
		return idcardImgUrl;
	}

	public void setIdcardImgUrl(String idcardImgUrl) {
		this.idcardImgUrl = idcardImgUrl;
	}

	public String getIdcardHandImgUrl() {
		return idcardHandImgUrl;
	}

	public void setIdcardHandImgUrl(String idcardHandImgUrl) {
		this.idcardHandImgUrl = idcardHandImgUrl;
	}

	public Integer getCashPledge() {
		return cashPledge;
	}

	public void setCashPledge(Integer cashPledge) {
		this.cashPledge = cashPledge;
	}

	public double getRealCashPledge() {
		return realCashPledge;
	}

	public void setRealCashPledge(double realCashPledge) {
		this.realCashPledge = realCashPledge;
	}

	public Integer getOnline() {
		return online;
	}

	public void setOnline(Integer online) {
		this.online = online;
	}
	
}