package com.bh.admin.pojo.goods;
/**
 * @Description: 滨惠商城拍卖商品信息
 * @author xieyc
 * @date 2018年7月11日 下午3:31:10 
 */
public class AuctionGoodsInfo {
	private Integer skuId;

    private Integer goodsId;

    private Integer storeNums;

    private String  value;
	
    private String goodsName;
    
   
	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getStoreNums() {
		return storeNums;
	}

	public void setStoreNums(Integer storeNums) {
		this.storeNums = storeNums;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	} 
    
    
    
}
