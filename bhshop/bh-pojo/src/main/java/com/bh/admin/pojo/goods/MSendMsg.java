package com.bh.admin.pojo.goods;


public class MSendMsg {
	//商家的id
	private String shopId;
	//收件人的名字
	private String receiverName;
	//收件人的手机号
	private String receiverPhone;
	//收件人的地址
	private String receiverAddress;
	//订单的id(OrderShop表的id)
	private Integer orderShopId;
	//商品的id
	private Integer goodsId;
	//商品的名称
	private String goodsName;
	//销售价展示
	private double realPrice; 
	//改商品的规格属性
	private Object valueObj;
	//购买的数量
	private Integer num;
	//商品的图片
	private String goodsImage;
	
	
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public Integer getOrderShopId() {
		return orderShopId;
	}
	public void setOrderShopId(Integer orderShopId) {
		this.orderShopId = orderShopId;
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
	public double getRealPrice() {
		return realPrice;
	}
	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}
	public Object getValueObj() {
		return valueObj;
	}
	public void setValueObj(Object valueObj) {
		this.valueObj = valueObj;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getGoodsImage() {
		return goodsImage;
	}
	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}
	
	
}
