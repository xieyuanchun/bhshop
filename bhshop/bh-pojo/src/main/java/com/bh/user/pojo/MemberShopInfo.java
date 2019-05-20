package com.bh.user.pojo;

import java.util.Date;

public class MemberShopInfo {
    private Integer id;

    private Integer applyType;

    private String applicantName;

    private String applicantIdcard;

    private String idcardImage;

    private String applicantPhone;

    private String bankCardNo;

    private String bankName;

    private String bankReservationNumber;

    private String cardRealName;

    private String companyName;

    private String legalPersonPhone;

    private String legalPersonIdcard;

    private String legalPersonIdcardImage;

    private String businessLicenseImage;

    private String creditNo;

    private String openid;

    private Integer shopId;
    
    
    private String legalPersonName;

    private Date addtime;

    private Date updatetime;
    
    

    public String getLegalPersonName() {
		return legalPersonName;
	}

	public void setLegalPersonName(String legalPersonName) {
		this.legalPersonName = legalPersonName;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

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

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName == null ? null : applicantName.trim();
    }

    public String getApplicantIdcard() {
        return applicantIdcard;
    }

    public void setApplicantIdcard(String applicantIdcard) {
        this.applicantIdcard = applicantIdcard == null ? null : applicantIdcard.trim();
    }

    public String getIdcardImage() {
        return idcardImage;
    }

    public void setIdcardImage(String idcardImage) {
        this.idcardImage = idcardImage == null ? null : idcardImage.trim();
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone == null ? null : applicantPhone.trim();
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

    public String getBankReservationNumber() {
        return bankReservationNumber;
    }

    public void setBankReservationNumber(String bankReservationNumber) {
        this.bankReservationNumber = bankReservationNumber == null ? null : bankReservationNumber.trim();
    }

    public String getCardRealName() {
        return cardRealName;
    }

    public void setCardRealName(String cardRealName) {
        this.cardRealName = cardRealName == null ? null : cardRealName.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getLegalPersonPhone() {
        return legalPersonPhone;
    }

    public void setLegalPersonPhone(String legalPersonPhone) {
        this.legalPersonPhone = legalPersonPhone == null ? null : legalPersonPhone.trim();
    }

    public String getLegalPersonIdcard() {
        return legalPersonIdcard;
    }

    public void setLegalPersonIdcard(String legalPersonIdcard) {
        this.legalPersonIdcard = legalPersonIdcard == null ? null : legalPersonIdcard.trim();
    }

    public String getLegalPersonIdcardImage() {
        return legalPersonIdcardImage;
    }

    public void setLegalPersonIdcardImage(String legalPersonIdcardImage) {
        this.legalPersonIdcardImage = legalPersonIdcardImage == null ? null : legalPersonIdcardImage.trim();
    }

    public String getBusinessLicenseImage() {
        return businessLicenseImage;
    }

    public void setBusinessLicenseImage(String businessLicenseImage) {
        this.businessLicenseImage = businessLicenseImage == null ? null : businessLicenseImage.trim();
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo == null ? null : creditNo.trim();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}