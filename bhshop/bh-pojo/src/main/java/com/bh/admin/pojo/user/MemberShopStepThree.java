package com.bh.admin.pojo.user;

public class MemberShopStepThree {
	
    private String businessLicense;//营业执照(照片)
    private String cardImage;//请按顺序依次上传身份证正面图，身份证背面图，手持身份证图

    private String licenseImage;//银行开户许可证图片
    
    private String organizationImage;//组织机构代码证电子版（图片）
    
    private String taxImage;//纳税资质图片上传（图片）
    
    private Integer step;//申请步骤

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public String getCardImage() {
		return cardImage;
	}

	public void setCardImage(String cardImage) {
		this.cardImage = cardImage;
	}

	public String getLicenseImage() {
		return licenseImage;
	}

	public void setLicenseImage(String licenseImage) {
		this.licenseImage = licenseImage;
	}

	public String getOrganizationImage() {
		return organizationImage;
	}

	public void setOrganizationImage(String organizationImage) {
		this.organizationImage = organizationImage;
	}

	public String getTaxImage() {
		return taxImage;
	}

	public void setTaxImage(String taxImage) {
		this.taxImage = taxImage;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}
    
    
}
