package com.bh.user.pojo;

import java.util.Date;

public class WalletWithdraw {
    private Integer id;

    private Integer mId;  //使用者ID

    private Integer amount; //提现金额 分

    private Date withdrawTime; //提现时间

    private Date addTime; //申请时间

    private Integer aId; //管理者id

    private Integer status; //状态：0申请，1成功，2失败

    private String bankCardNo; //银行卡号

    private String bankName; //银行名字
    
    private Integer walletLogId;
    
    private String money2; //zlk 前端传来的金额 字符串
    
    private String currentPage;
    
    //MemberBankCard 表
    private String real_name;//真实姓名 

    private String id_no;//身份证号
    
    private String phone;//手机号
    
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getWithdrawTime() {
        return withdrawTime;
    }

    public void setWithdrawTime(Date withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getaId() {
        return aId;
    }

    public void setaId(Integer aId) {
        this.aId = aId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public String getMoney2() {
		return money2;
	}

	public void setMoney2(String money2) {
		this.money2 = money2;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getId_no() {
		return id_no;
	}

	public void setId_no(String id_no) {
		this.id_no = id_no;
	}

	public Integer getWalletLogId() {
		return walletLogId;
	}

	public void setWalletLogId(Integer walletLogId) {
		this.walletLogId = walletLogId;
	}
    
	
    
}