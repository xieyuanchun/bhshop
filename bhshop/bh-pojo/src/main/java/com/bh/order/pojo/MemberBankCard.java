package com.bh.order.pojo;

public class MemberBankCard {
    private Integer id;

    private String realName;//真实姓名

    private String idNo;//身份证号

    private String bankCardNo;//银行卡号

    private String bankName;//银行名称

    private String phone;//手机号

    private Integer mId;//用户id
    
    private String bankKind;//银行种类 如：中国银行信用卡

    private String bankType;//卡类型 如:信用卡

    private String bankCode;//卡的编码:如 CMB
    private Integer tbbankId;

    //zlk
    private String logoImg; //图片
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo == null ? null : idNo.trim();
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo == null ? null : bankCardNo.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

	public String getBankKind() {
		return bankKind;
	}

	public void setBankKind(String bankKind) {
		this.bankKind = bankKind;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public Integer getTbbankId() {
		return tbbankId;
	}

	public void setTbbankId(Integer tbbankId) {
		this.tbbankId = tbbankId;
	}

	public String getLogoImg() {
		return logoImg;
	}

	public void setLogoImg(String logoImg) {
		this.logoImg = logoImg;
	}
    
    
}