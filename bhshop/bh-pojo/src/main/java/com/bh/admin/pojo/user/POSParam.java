package com.bh.admin.pojo.user;

public class POSParam {
	private String mId;
	
	private Integer type;//1已有pos机;2无post机需提交押金2000;3无pos机，立即申请
	
	private String busNo;//商户号
	
	private String busName;//商户名称
	
	private String fullName;//姓名
	
	private String idNo;//身份证号
	
	private String bankCardNo;//银行卡号
	
	private String phone;//手机号
	
	private String linkManName;//联系人姓名
	
	private String linkManPhone;//联系人手机号
	
	private String token;
	
    private String currentPage;
    
    private String posMsg;
    
    private Integer payStatus;//付状态1未支付，2已支付
    private String shopName;
    
    private String handleStatus; //处理状态handle_status(0未处理，1审核通过，2审核拒绝)
    
    

	
    

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLinkManName() {
		return linkManName;
	}

	public void setLinkManName(String linkManName) {
		this.linkManName = linkManName;
	}

	public String getLinkManPhone() {
		return linkManPhone;
	}

	public void setLinkManPhone(String linkManPhone) {
		this.linkManPhone = linkManPhone;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getPosMsg() {
		return posMsg;
	}

	public void setPosMsg(String posMsg) {
		this.posMsg = posMsg;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}
	
}
