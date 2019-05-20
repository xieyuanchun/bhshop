package com.print.controller;

public class LogisticsOrder {

	private String  cpCode;       //快递公司编码
	
	private String  cityRecipient;  //收货市名称（二级地址）
	
	private String  detailRecipient;  //收货 详细地址
	
	private String  districtRecipient;  //收货区名称（三级地址）
	
	private String  provinceRecipient;  //收货省名称（一级地址）

	private String  townRecipient;     //收货街道\镇名称（四级地址）
	
	private String  mobileRecipient;   //收件人电话
	
	private String  nameRecipient;   //收件人名字
	
	private String  phoneRecipient;   //收件人电话
	
	private String  citySend;  //发货市名称（二级地址）
	
	private String  detailSend;  //发货 详细地址
	
	private String  districtSend;  //发货区名称（三级地址）
	
	private String  provinceSend;  //发货省名称（一级地址）

	private String  townSend;     //发货街道\镇名称（四级地址）
	
    private String  mobileSend;   //发货人电话
	
	private String  nameSend;   //发货人名字
	
	private String  phoneSend;   //发货人电话
	
	private String  itemName;    //商品名称
	
	private Integer itemCount;    //商品数量
	
	private String  orderNum;    //订单号
	
	private String  shopOrderNo;    //商家订单号
	
	private String  taobaoUserId; //卖家ID

	private String  waybill_code;  //快递单号
	
	private String  templateURL;   //物流模板URl
	
	private String  request_id;    //请求的id
	
	private String  signature;     //
	
	private String  taobao;   
	
	private String  printState;   
	
	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	public String getCityRecipient() {
		return cityRecipient;
	}

	public void setCityRecipient(String cityRecipient) {
		this.cityRecipient = cityRecipient;
	}

	public String getDetailRecipient() {
		return detailRecipient;
	}

	public void setDetailRecipient(String detailRecipient) {
		this.detailRecipient = detailRecipient;
	}

	public String getDistrictRecipient() {
		return districtRecipient;
	}

	public void setDistrictRecipient(String districtRecipient) {
		this.districtRecipient = districtRecipient;
	}

	public String getProvinceRecipient() {
		return provinceRecipient;
	}

	public void setProvinceRecipient(String provinceRecipient) {
		this.provinceRecipient = provinceRecipient;
	}

	public String getTownRecipient() {
		return townRecipient;
	}

	public void setTownRecipient(String townRecipient) {
		this.townRecipient = townRecipient;
	}

	public String getMobileRecipient() {
		return mobileRecipient;
	}

	public void setMobileRecipient(String mobileRecipient) {
		this.mobileRecipient = mobileRecipient;
	}

	public String getNameRecipient() {
		return nameRecipient;
	}

	public void setNameRecipient(String nameRecipient) {
		this.nameRecipient = nameRecipient;
	}

	public String getPhoneRecipient() {
		return phoneRecipient;
	}

	public void setPhoneRecipient(String phoneRecipient) {
		this.phoneRecipient = phoneRecipient;
	}

	public String getCitySend() {
		return citySend;
	}

	public void setCitySend(String citySend) {
		this.citySend = citySend;
	}

	public String getDetailSend() {
		return detailSend;
	}

	public void setDetailSend(String detailSend) {
		this.detailSend = detailSend;
	}

	public String getDistrictSend() {
		return districtSend;
	}

	public void setDistrictSend(String districtSend) {
		this.districtSend = districtSend;
	}

	public String getProvinceSend() {
		return provinceSend;
	}

	public void setProvinceSend(String provinceSend) {
		this.provinceSend = provinceSend;
	}

	public String getTownSend() {
		return townSend;
	}

	public void setTownSend(String townSend) {
		this.townSend = townSend;
	}

	public String getMobileSend() {
		return mobileSend;
	}

	public void setMobileSend(String mobileSend) {
		this.mobileSend = mobileSend;
	}

	public String getNameSend() {
		return nameSend;
	}

	public void setNameSend(String nameSend) {
		this.nameSend = nameSend;
	}

	public String getPhoneSend() {
		return phoneSend;
	}

	public void setPhoneSend(String phoneSend) {
		this.phoneSend = phoneSend;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(String taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getWaybill_code() {
		return waybill_code;
	}

	public void setWaybill_code(String waybill_code) {
		this.waybill_code = waybill_code;
	}

	public String getTemplateURL() {
		return templateURL;
	}

	public void setTemplateURL(String templateURL) {
		this.templateURL = templateURL;
	}

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTaobao() {
		return taobao;
	}

	public void setTaobao(String taobao) {
		this.taobao = taobao;
	}

	public String getPrintState() {
		return printState;
	}

	public void setPrintState(String printState) {
		this.printState = printState;
	}

	public String getShopOrderNo() {
		return shopOrderNo;
	}

	public void setShopOrderNo(String shopOrderNo) {
		this.shopOrderNo = shopOrderNo;
	}
	
	
	
	
}
