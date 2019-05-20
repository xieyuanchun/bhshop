package com.bh.goods.pojo;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public class MyGoodsDetail {
	//商品的id
	private Integer id;
	//是否是京东商品:0否,1是
	private Integer isJd;
	//商品类型，0-普通商品，1砍价，2秒杀，3抽奖，4超级滨惠豆，5惠省钱，6荷兰拍卖
	private Integer topicType;
	//销售模式 1单卖 2拼团单卖皆可 3只拼团
	private Integer saleType;
	//商品销量或者是已拼的数量
	private Integer groupCount;
	//商品的名称
	private String name;
	//拼团人数
	private Integer teamNum;
	//多个tag id以逗号隔开
	private String[] tagIdsValue;
	//商品详情描述
	private String appdescription;
	//商品的模型
	private JSONArray modelValue;
	//商品的图片
	private String image;
	//商家的id
	private Integer shopId;
	//商品状态 0正常 1已删除 2下架 3申请上架4拒绝5上架
	private Integer status;
	//商品标签 1奢侈品牌大折扣 2大牌好货 3超级滨惠豆 4节假日活动 5周末大放送
	private String tagName;
	//折扣率
	private String deductibleRate;
	//是否是活动专区
	private String IsAct;
	//滨惠豆价格
	private String bhdPrice;
	//滨惠豆价格
	private Integer score;
	
	//根据经纬度获取地址
	private String address;
	
	private Map<String, Object> dauctionDetail;
	private Map<String, Object> shopInfo;
	private List<MyGoodsSku> skuList;
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIsJd() {
		return isJd;
	}
	public void setIsJd(Integer isJd) {
		this.isJd = isJd;
	}
	public Integer getTopicType() {
		return topicType;
	}
	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}
	public Integer getSaleType() {
		return saleType;
	}
	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}
	public Integer getGroupCount() {
		return groupCount;
	}
	public void setGroupCount(Integer groupCount) {
		this.groupCount = groupCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTeamNum() {
		return teamNum;
	}
	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}
	
	public String getAppdescription() {
		return appdescription;
	}
	public void setAppdescription(String appdescription) {
		this.appdescription = appdescription;
	}
	
	public JSONArray getModelValue() {
		return modelValue;
	}
	public void setModelValue(JSONArray modelValue) {
		this.modelValue = modelValue;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Map<String, Object> getDauctionDetail() {
		return dauctionDetail;
	}
	public void setDauctionDetail(Map<String, Object> dauctionDetail) {
		this.dauctionDetail = dauctionDetail;
	}
	public Map<String, Object> getShopInfo() {
		return shopInfo;
	}
	public void setShopInfo(Map<String, Object> shopInfo) {
		this.shopInfo = shopInfo;
	}
	
	public List<MyGoodsSku> getSkuList() {
		return skuList;
	}
	public void setSkuList(List<MyGoodsSku> skuList) {
		this.skuList = skuList;
	}
	public String[] getTagIdsValue() {
		return tagIdsValue;
	}
	public void setTagIdsValue(String[] tagIdsValue) {
		this.tagIdsValue = tagIdsValue;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getDeductibleRate() {
		return deductibleRate;
	}
	public void setDeductibleRate(String deductibleRate) {
		this.deductibleRate = deductibleRate;
	}
	public String getIsAct() {
		return IsAct;
	}
	public void setIsAct(String isAct) {
		IsAct = isAct;
	}
	public String getBhdPrice() {
		return bhdPrice;
	}
	public void setBhdPrice(String bhdPrice) {
		this.bhdPrice = bhdPrice;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
	
}
