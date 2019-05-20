package com.bh.admin.pojo.goods;

import java.util.Date;

public class AspUserGuess {
    private Integer mId;

    private Integer reqUserId;

    private Integer isFull;

    private Integer guessStatus;

    private Integer retPrice;

    private Integer backPrice;

    private Integer guessOne;

    private Integer guessTwo;

    private Date applyTime;
    
    private double retMoney;
    
	private double backMoney;
	
	

    public double getRetMoney() {
		return retMoney;
	}

	public void setRetMoney(double retMoney) {
		this.retMoney = retMoney;
	}

	public double getBackMoney() {
		return backMoney;
	}

	public void setBackMoney(double backMoney) {
		this.backMoney = backMoney;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	private String bankName;

    private String bankCardNo;

    private String bankCardOwner;

    private String idcard;

    private String phone;

    private Integer state;

    private String note;

    private Date auditTime;

    private String auditor;

    private Date confirmTime;

    private String transferPeople;

    private Date editTime;

    private Date addTime;
    
    private String startTime;

	private String endTime;
    private Integer startPrice;
    private Integer endPrice;
    private String pageSize;
	private String curePage;
	private String userName;
	
	private Integer isTransfer;
	private Integer isApply;
    
    

    public Integer getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(Integer isTransfer) {
		this.isTransfer = isTransfer;
	}

	public Integer getIsApply() {
		return isApply;
	}

	public void setIsApply(Integer isApply) {
		this.isApply = isApply;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getCurePage() {
		return curePage;
	}

	public void setCurePage(String curePage) {
		this.curePage = curePage;
	}

	public Integer getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(Integer startPrice) {
		this.startPrice = startPrice;
	}

	public Integer getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(Integer endPrice) {
		this.endPrice = endPrice;
	}

	public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getReqUserId() {
        return reqUserId;
    }

    public void setReqUserId(Integer reqUserId) {
        this.reqUserId = reqUserId;
    }

    public Integer getIsFull() {
        return isFull;
    }

    public void setIsFull(Integer isFull) {
        this.isFull = isFull;
    }

    public Integer getGuessStatus() {
        return guessStatus;
    }

    public void setGuessStatus(Integer guessStatus) {
        this.guessStatus = guessStatus;
    }

    public Integer getRetPrice() {
        return retPrice;
    }

    public void setRetPrice(Integer retPrice) {
        this.retPrice = retPrice;
    }

    public Integer getBackPrice() {
        return backPrice;
    }

    public void setBackPrice(Integer backPrice) {
        this.backPrice = backPrice;
    }

    public Integer getGuessOne() {
        return guessOne;
    }

    public void setGuessOne(Integer guessOne) {
        this.guessOne = guessOne;
    }

    public Integer getGuessTwo() {
        return guessTwo;
    }

    public void setGuessTwo(Integer guessTwo) {
        this.guessTwo = guessTwo;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo == null ? null : bankCardNo.trim();
    }

    public String getBankCardOwner() {
        return bankCardOwner;
    }

    public void setBankCardOwner(String bankCardOwner) {
        this.bankCardOwner = bankCardOwner == null ? null : bankCardOwner.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor == null ? null : auditor.trim();
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getTransferPeople() {
        return transferPeople;
    }

    public void setTransferPeople(String transferPeople) {
        this.transferPeople = transferPeople == null ? null : transferPeople.trim();
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}