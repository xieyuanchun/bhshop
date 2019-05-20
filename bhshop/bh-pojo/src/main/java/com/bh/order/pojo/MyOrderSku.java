package com.bh.order.pojo;

public class MyOrderSku {
	//订单列表的每个商品的附带内容:
	/***
	 * 去评价:isShowCommentButton==1
	 * 再次购买：所有的
	 * 查看物流:orderShop.status==3&&orderSku.mystatus==null
	 * 否则mystatus
	 * **/
	private Integer id;
	private Integer goodsId;
	private String goodsName;
	private Object valueObj;
	private Integer skuId;
	private String skuImage;
	//商品的内容：
	private String mystatus;
	private Integer skuNum;
	private String realSellPrice;
	//如果isShowCommentButton的值为1,前端显示去评价按钮
	private Integer isShowCommentButton;
	
	private Long jdSkuNo;
	
	
	
	public Long getJdSkuNo() {
		return jdSkuNo;
	}
	public void setJdSkuNo(Long jdSkuNo) {
		this.jdSkuNo = jdSkuNo;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Object getValueObj() {
		return valueObj;
	}
	public void setValueObj(Object valueObj) {
		this.valueObj = valueObj;
	}
	public Integer getSkuId() {
		return skuId;
	}
	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}
	public String getSkuImage() {
		return skuImage;
	}
	public void setSkuImage(String skuImage) {
		this.skuImage = skuImage;
	}
	public String getMystatus() {
		return mystatus;
	}
	public void setMystatus(String mystatus) {
		this.mystatus = mystatus;
	}
	public Integer getSkuNum() {
		return skuNum;
	}
	public void setSkuNum(Integer skuNum) {
		this.skuNum = skuNum;
	}
	public String getRealSellPrice() {
		return realSellPrice;
	}
	public void setRealSellPrice(String realSellPrice) {
		this.realSellPrice = realSellPrice;
	}
	public Integer getIsShowCommentButton() {
		return isShowCommentButton;
	}
	public void setIsShowCommentButton(Integer isShowCommentButton) {
		this.isShowCommentButton = isShowCommentButton;
	}
	
	
	
	
	
}
