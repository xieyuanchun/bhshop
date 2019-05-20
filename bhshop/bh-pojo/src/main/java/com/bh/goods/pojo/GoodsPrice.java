package com.bh.goods.pojo;

public class GoodsPrice {
	private Integer id;
	private String replyNo;//审请编号
	private Integer status;//状态 0未审核  1通过  2未通过

	private String newVal;//新值

	private String oldVal;//旧值
	
	private Integer goodsSkuId ; 

	public String getReplyNo() {
		return replyNo;
	}

	public void setReplyNo(String replyNo) {
		this.replyNo = replyNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNewVal() {
		return newVal;
	}

	public void setNewVal(String newVal) {
		this.newVal = newVal;
	}

	public String getOldVal() {
		return oldVal;
	}

	public void setOldVal(String oldVal) {
		this.oldVal = oldVal;
	}

	public Integer getGoodsSkuId() {
		return goodsSkuId;
	}

	public void setGoodsSkuId(Integer goodsSkuId) {
		this.goodsSkuId = goodsSkuId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	
}
