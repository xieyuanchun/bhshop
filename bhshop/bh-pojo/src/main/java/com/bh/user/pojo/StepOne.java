package com.bh.user.pojo;

public class StepOne {
	
	private String name ;//联系人姓名
	private String phone;//联系人电话
	private String email ;//联系人电子邮箱
	private Integer isInvite ;//是否接受其他商场邀请0否，1是
	private String shopId ;//如果接受了其他商场的邀请则传推荐商家的id,如果没有则传0 
	private Integer step;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public Integer getIsInvite() {
		return isInvite;
	}
	public void setIsInvite(Integer isInvite) {
		this.isInvite = isInvite;
	}
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	
	
	
	
}
