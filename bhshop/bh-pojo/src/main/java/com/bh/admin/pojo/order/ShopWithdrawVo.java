package com.bh.admin.pojo.order;

import java.util.Date;

public class ShopWithdrawVo {
    private Integer id;

    private Integer type;

    private Date applyTime;

    private double serviceCharge;

    private double withdrawAmount;
    
    private double arrivalAmount;

    private Integer shopId;

    private String bankName;

    private String bankCardNo;

    private String bankCardOwner;

    private String linkmanPhone;

    private Integer state;

    private String refuseReason;
    
    private Integer isPay;
    
    private String transferPeople;
    private String auditor;
    
                     
    public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getTransferPeople() {
		return transferPeople;
	}

	public void setTransferPeople(String transferPeople) {
		this.transferPeople = transferPeople;
	}
	public Integer getIsPay() {
		return isPay;
	}

	public void setIsPay(Integer isPay) {
		this.isPay = isPay;
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

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public double getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(double serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public double getWithdrawAmount() {
		return withdrawAmount;
	}

	public void setWithdrawAmount(double withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}

	public double getArrivalAmount() {
		return arrivalAmount;
	}

	public void setArrivalAmount(double arrivalAmount) {
		this.arrivalAmount = arrivalAmount;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankCardOwner() {
		return bankCardOwner;
	}

	public void setBankCardOwner(String bankCardOwner) {
		this.bankCardOwner = bankCardOwner;
	}

	public String getLinkmanPhone() {
		return linkmanPhone;
	}

	public void setLinkmanPhone(String linkmanPhone) {
		this.linkmanPhone = linkmanPhone;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}          
    
}