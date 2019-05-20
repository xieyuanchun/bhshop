package com.bh.admin.pojo.user;

import java.util.Date;

public class ShopInfoByPerson {
		
		private Integer id;
		
	 	private Integer shopId;

	    private Integer applyType;

	    private String applicantName;

	    private String applicantPhone;
	    
	    private Date  addTime;
	    
	    private Integer step;
	    
	    private String busiPayPre;//商家支付前缀

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getShopId() {
			return shopId;
		}

		public void setShopId(Integer shopId) {
			this.shopId = shopId;
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
			this.applicantName = applicantName;
		}

		public String getApplicantPhone() {
			return applicantPhone;
		}

		public void setApplicantPhone(String applicantPhone) {
			this.applicantPhone = applicantPhone;
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
