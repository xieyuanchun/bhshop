package com.bh.goods.pojo;

public class JdGoodsCopy {
    private Integer goodsId;

    private String goodsName;

    private Long categoryId;

    private String categoryName;

    private String marketPrice;

    private String stockPrice;

    private String sellPrice;

    private String teamPrice;

    private Integer storeNums;

    private Long jdSkuNo;

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
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice == null ? null : marketPrice.trim();
    }

    public String getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(String stockPrice) {
        this.stockPrice = stockPrice == null ? null : stockPrice.trim();
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice == null ? null : sellPrice.trim();
    }

    public String getTeamPrice() {
        return teamPrice;
    }

    public void setTeamPrice(String teamPrice) {
        this.teamPrice = teamPrice == null ? null : teamPrice.trim();
    }

    public Integer getStoreNums() {
        return storeNums;
    }

    public void setStoreNums(Integer storeNums) {
        this.storeNums = storeNums;
    }

	public Long getJdSkuNo() {
		return jdSkuNo;
	}

	public void setJdSkuNo(Long jdSkuNo) {
		this.jdSkuNo = jdSkuNo;
	}
}