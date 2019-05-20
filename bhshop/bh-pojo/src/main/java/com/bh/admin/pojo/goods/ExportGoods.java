package com.bh.admin.pojo.goods;

public class ExportGoods {
	private Integer id;
    private String goodsName; //商品名称
    private String skuNo;
    private String goodsSkuName;//商品sku名称
    
    private String valueOne;
    
    private String valueTwo;
    
    private String valueThree;
    
    private String valueFour;
    
    private String valueFive;
    
    private String categoryName;
    
    private double marketRealPrice; //市场价展示
    private double realSellPrice; //销售价展示
    private double realJdProtocolPrice; //协议价格单位元
    private double realStockPrice; //进货价
    private double realJdBuyPrice;//客户购买价格单位元
    private double realTeamPrice;//拼团加
    
    private Integer storeNums;
    private Integer sale;
    private Integer havedSale;//g.fixed_sale+g.sale :已售数量
    private Long jdSkuNo; //京东商品编码
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSkuNo() {
		return skuNo;
	}
	public void setSkuNo(String skuNo) {
		this.skuNo = skuNo;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getValueOne() {
		return valueOne;
	}
	public void setValueOne(String valueOne) {
		this.valueOne = valueOne;
	}
	public String getValueTwo() {
		return valueTwo;
	}
	public void setValueTwo(String valueTwo) {
		this.valueTwo = valueTwo;
	}
	public String getValueThree() {
		return valueThree;
	}
	public void setValueThree(String valueThree) {
		this.valueThree = valueThree;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public double getMarketRealPrice() {
		return marketRealPrice;
	}
	public void setMarketRealPrice(double marketRealPrice) {
		this.marketRealPrice = marketRealPrice;
	}
	public double getRealSellPrice() {
		return realSellPrice;
	}
	public void setRealSellPrice(double realSellPrice) {
		this.realSellPrice = realSellPrice;
	}
	public double getRealJdProtocolPrice() {
		return realJdProtocolPrice;
	}
	public void setRealJdProtocolPrice(double realJdProtocolPrice) {
		this.realJdProtocolPrice = realJdProtocolPrice;
	}
	public double getRealStockPrice() {
		return realStockPrice;
	}
	public void setRealStockPrice(double realStockPrice) {
		this.realStockPrice = realStockPrice;
	}
	public double getRealJdBuyPrice() {
		return realJdBuyPrice;
	}
	public void setRealJdBuyPrice(double realJdBuyPrice) {
		this.realJdBuyPrice = realJdBuyPrice;
	}
	public double getRealTeamPrice() {
		return realTeamPrice;
	}
	public void setRealTeamPrice(double realTeamPrice) {
		this.realTeamPrice = realTeamPrice;
	}
	public Integer getStoreNums() {
		return storeNums;
	}
	public void setStoreNums(Integer storeNums) {
		this.storeNums = storeNums;
	}
	public Integer getSale() {
		return sale;
	}
	public void setSale(Integer sale) {
		this.sale = sale;
	}
	public Integer getHavedSale() {
		return havedSale;
	}
	public void setHavedSale(Integer havedSale) {
		this.havedSale = havedSale;
	}
	public Long getJdSkuNo() {
		return jdSkuNo;
	}
	public void setJdSkuNo(Long jdSkuNo) {
		this.jdSkuNo = jdSkuNo;
	}
	public String getGoodsSkuName() {
		return goodsSkuName;
	}
	public void setGoodsSkuName(String goodsSkuName) {
		this.goodsSkuName = goodsSkuName;
	}
	public String getValueFour() {
		return valueFour;
	}
	public void setValueFour(String valueFour) {
		this.valueFour = valueFour;
	}
	public String getValueFive() {
		return valueFive;
	}
	public void setValueFive(String valueFive) {
		this.valueFive = valueFive;
	}
}
