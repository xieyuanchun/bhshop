package com.bh.admin.pojo.user;

import java.util.Date;

public class ShopInfoByBusiness {
	private Integer id;

    private Integer applyType;

    private String companyName;

    private String legalPersonPhone;

    private String creditNo;

    private Integer shopId;
    
    private String legalPersonName;

    private Date addTime;
    
    private Integer step;
    
    private String busiPayPre;//商家支付前缀

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApplyType() {
		return applyType;
	}

	public void setApplyType(Integer applyType) {
		this.applyType = applyType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLegalPersonPhone() {
		return legalPersonPhone;
	}

	public void setLegalPersonPhone(String legalPersonPhone) {
		this.legalPersonPhone = legalPersonPhone;
	}

	public String getCreditNo() {
		return creditNo;
	}

	public void setCreditNo(String creditNo) {
		this.creditNo = creditNo;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getLegalPersonName() {
		return legalPersonName;
	}

	public void setLegalPersonName(String legalPersonName) {
		this.legalPersonName = legalPersonName;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getBusiPayPre() {
		return busiPayPre;
	}

	public void setBusiPayPre(String busiPayPre) {
		this.busiPayPre = busiPayPre;
	}

    
    

}
