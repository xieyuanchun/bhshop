package com.bh.goods.pojo;

import java.util.Map;

public class CartGoodsList {
	//购物车的id
	private Integer id;
	//数量
    private Integer num;
    //图片
    private String gImage;
    //商品名称
    private String goodName;//cheng添加
    private String goodsSkuName;
    private String value;
    //单价
    private double realsellPrice;
    //商品的id
    private Integer gId;
   //商品的skuid
    private Integer gskuid;
    private Map<String, Object> goodsSkus;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getgImage() {
		return gImage;
	}
	public void setgImage(String gImage) {
		this.gImage = gImage;
	}
	
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getGoodsSkuName() {
		return goodsSkuName;
	}
	public void setGoodsSkuName(String goodsSkuName) {
		this.goodsSkuName = goodsSkuName;
	}
	public double getRealsellPrice() {
		return realsellPrice;
	}
	public void setRealsellPrice(double realsellPrice) {
		this.realsellPrice = realsellPrice;
	}
	public Integer getGskuid() {
		return gskuid;
	}
	public void setGskuid(Integer gskuid) {
		this.gskuid = gskuid;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getgId() {
		return gId;
	}
	public void setgId(Integer gId) {
		this.gId = gId;
	}
	public Map<String, Object> getGoodsSkus() {
		return goodsSkus;
	}
	public void setGoodsSkus(Map<String, Object> goodsSkus) {
		this.goodsSkus = goodsSkus;
	}
	
    
}
