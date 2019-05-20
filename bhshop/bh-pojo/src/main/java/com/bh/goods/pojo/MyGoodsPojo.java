package com.bh.goods.pojo;

public class MyGoodsPojo {
	//商品的ID
	private Integer id;
	//商品的名称
	private String name;
	//商品的价格(元为单位)
	private double realPrice;
	//商品的图片
	private String image;
	//商品的销量=拼团成功+fixedSale
	private Integer groupCount;
	//商品类型，0-普通商品，1砍价，2秒杀，3抽奖，4超级滨惠豆，5惠省钱，6荷兰拍卖,默认为0
	private Integer topicType;
	//goods_sku 表的value值
	private String value;
	//折扣率
	private String deductibleRate;
	
	private double jdPrice;
	
	private Integer shopId;
	
	
	
	public double getJdPrice() {
		return jdPrice;
	}
	public void setJdPrice(double jdPrice) {
		this.jdPrice = jdPrice;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public double getRealPrice() {
		return realPrice;
	}
	public void setRealPrice(double realPrice) {
		this.realPrice = realPrice;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Integer getGroupCount() {
		return groupCount;
	}
	public void setGroupCount(Integer groupCount) {
		this.groupCount = groupCount;
	}
	public Integer getTopicType() {
		return topicType;
	}
	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDeductibleRate() {
		return deductibleRate;
	}
	public void setDeductibleRate(String deductibleRate) {
		this.deductibleRate = deductibleRate;
	}
	
	
}
