package com.bh.admin.pojo.order;

public class MyOrder extends Order{
	private String shopName;
	private String shopPhone;
	    
	private String provname;
    private String cityname;
    private String areaname; 
    private String fourname;//第四级地址
    
    private String userNick;
    private String phone;
    private Integer userStatus;
    
    private String jdSkuNo;
    private double jdPirce;
    private double jdProtocolPrice;
    
    private double price;//单价
    private String value;
    private Integer goodsNum;
    
    private String remark;
    private Integer isRefund;
    private Integer orderShopId;
    private Integer orderShopStatus;
    private String statusName;//状态值的名称
    private Integer orderSkuId;
    
	public String getProvname() {
		return provname;
	}
	public void setProvname(String provname) {
		this.provname = provname;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public String getFourname() {
		return fourname;
	}
	public void setFourname(String fourname) {
		this.fourname = fourname;
	}
	public String getUserNick() {
		return userNick;
	}
	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
	public String getJdSkuNo() {
		return jdSkuNo;
	}
	public void setJdSkuNo(String jdSkuNo) {
		this.jdSkuNo = jdSkuNo;
	}
	public double getJdPirce() {
		return jdPirce;
	}
	public void setJdPirce(double jdPirce) {
		this.jdPirce = jdPirce;
	}
	public double getJdProtocolPrice() {
		return jdProtocolPrice;
	}
	public void setJdProtocolPrice(double jdProtocolPrice) {
		this.jdProtocolPrice = jdProtocolPrice;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopPhone() {
		return shopPhone;
	}
	public Integer getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}
	public void setShopPhone(String shopPhone) {
		this.shopPhone = shopPhone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIsRefund() {
		return isRefund;
	}
	public void setIsRefund(Integer isRefund) {
		this.isRefund = isRefund;
	}
	public Integer getOrderShopId() {
		return orderShopId;
	}
	public void setOrderShopId(Integer orderShopId) {
		this.orderShopId = orderShopId;
	}
	public Integer getOrderShopStatus() {
		return orderShopStatus;
	}
	public void setOrderShopStatus(Integer orderShopStatus) {
		this.orderShopStatus = orderShopStatus;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public Integer getOrderSkuId() {
		return orderSkuId;
	}
	public void setOrderSkuId(Integer orderSkuId) {
		this.orderSkuId = orderSkuId;
	}
    
    
}
