package com.bh.auc.vo;

import java.util.Date;

/**
 * @author xieyc
 * @Description:
 * @date 2018/7/23 16:02
 */
public class AuctionHistoryVo {

    private Integer id;

    private Integer goodsId;

    private String goodsName;

    private String goodsImage;

    private Integer type;

    private Integer currentPeriods;
    
    private String orderNo;

    private Double bargainPrice;

    private Integer bargainMid;

    private Date loseTime;

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

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCurrentPeriods() {
		return currentPeriods;
	}

	public void setCurrentPeriods(Integer currentPeriods) {
		this.currentPeriods = currentPeriods;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public Double getBargainPrice() {
		return bargainPrice;
	}

	public void setBargainPrice(Double bargainPrice) {
		this.bargainPrice = bargainPrice;
	}

	public Integer getBargainMid() {
		return bargainMid;
	}

	public void setBargainMid(Integer bargainMid) {
		this.bargainMid = bargainMid;
	}

	public Date getLoseTime() {
		return loseTime;
	}

	public void setLoseTime(Date loseTime) {
		this.loseTime = loseTime;
	}

	

    
}
