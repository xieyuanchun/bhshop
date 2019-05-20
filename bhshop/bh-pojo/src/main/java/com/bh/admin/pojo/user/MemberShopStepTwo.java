package com.bh.admin.pojo.user;

public class MemberShopStepTwo {
	private String licenseTypeName;
	
	   private Integer prov;//省

	    private Integer city;//市

	    private Integer area;//区
	    
	    private String provName;//省

	    private String cityName;//市

	    private String areaName;//区
	    
	    private String address;
	    
	    private String tel;
	    
	    private String emergencyContactPerson;
	    
	    private String emergencyContactPhone;

	    private Integer cardType;//法定代表人证件类型：0身份证，1户口本，2军官证、3护照

	    private String card;//法定代表人证件号

	    private String beginTime;//营业期限（开始时间）

	    private String endTime;//营业期限(结束时间)
	    
	    private Integer step;//申请步骤

		public String getLicenseTypeName() {
			return licenseTypeName;
		}

		public void setLicenseTypeName(String licenseTypeName) {
			this.licenseTypeName = licenseTypeName;
		}

		public Integer getProv() {
			return prov;
		}

		public void setProv(Integer prov) {
			this.prov = prov;
		}

		public Integer getCity() {
			return city;
		}

		public void setCity(Integer city) {
			this.city = city;
		}

		public Integer getArea() {
			return area;
		}

		public void setArea(Integer area) {
			this.area = area;
		}

		public String getProvName() {
			return provName;
		}

		public void setProvName(String provName) {
			this.provName = provName;
		}

		public String getCityName() {
			return cityName;
		}

		public void setCityName(String cityName) {
			this.cityName = cityName;
		}

		public String getAreaName() {
			return areaName;
		}

		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
		}

		public String getEmergencyContactPerson() {
			return emergencyContactPerson;
		}

		public void setEmergencyContactPerson(String emergencyContactPerson) {
			this.emergencyContactPerson = emergencyContactPerson;
		}

		public String getEmergencyContactPhone() {
			return emergencyContactPhone;
		}

		public void setEmergencyContactPhone(String emergencyContactPhone) {
			this.emergencyContactPhone = emergencyContactPhone;
		}

		public Integer getCardType() {
			return cardType;
		}

		public void setCardType(Integer cardType) {
			this.cardType = cardType;
		}

		public String getCard() {
			return card;
		}

		public void setCard(String card) {
			this.card = card;
		}

		public String getBeginTime() {
			return beginTime;
		}

		public void setBeginTime(String beginTime) {
			this.beginTime = beginTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public Integer getStep() {
			return step;
		}

		public void setStep(Integer step) {
			this.step = step;
		}
	
	    
}
