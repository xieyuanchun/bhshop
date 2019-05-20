package com.bh.order.pojo;

public class MyOrderShopDetailOrderSku {
	
	private String skuImage;
	private String goodsName;
	private String realSellPrice;
	private Object valueObj;
	private Integer skuNum;
	private String mystatus;
	
	//申请售后按钮status==1||status==3||status==4
	private Integer status;
	
	//该属性值为1时显示 去评价 按钮
	private Integer isShowCommentButton;
	//该属性值为1时显示 填写物流信息
	private Integer isShowlogButton;
	
	//去详情页
	private Integer goodsId;
	//退款操作
	private Integer id;
	
	private Long jdSkuNo;
	
	private boolean isAfterSale;
	
	
	
	public boolean isAfterSale() {
		return isAfterSale;
	}
	public void setAfterSale(boolean isAfterSale) {
		this.isAfterSale = isAfterSale;
	}
	public Long getJdSkuNo() {
		return jdSkuNo;
	}
	public void setJdSkuNo(Long jdSkuNo) {
		this.jdSkuNo = jdSkuNo;
	}
	public String getSkuImage() {
		return skuImage;
	}
	public void setSkuImage(String skuImage) {
		this.skuImage = skuImage;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getRealSellPrice() {
		return realSellPrice;
	}
	public void setRealSellPrice(String realSellPrice) {
		this.realSellPrice = realSellPrice;
	}
	public Object getValueObj() {
		return valueObj;
	}
	public void setValueObj(Object valueObj) {
		this.valueObj = valueObj;
	}
	public Integer getSkuNum() {
		return skuNum;
	}
	public void setSkuNum(Integer skuNum) {
		this.skuNum = skuNum;
	}
	public String getMystatus() {
		return mystatus;
	}
	public void setMystatus(String mystatus) {
		this.mystatus = mystatus;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getIsShowCommentButton() {
		return isShowCommentButton;
	}
	public void setIsShowCommentButton(Integer isShowCommentButton) {
		this.isShowCommentButton = isShowCommentButton;
	}
	public Integer getIsShowlogButton() {
		return isShowlogButton;
	}
	public void setIsShowlogButton(Integer isShowlogButton) {
		this.isShowlogButton = isShowlogButton;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
