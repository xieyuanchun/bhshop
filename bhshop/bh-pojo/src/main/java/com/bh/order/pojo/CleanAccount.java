package com.bh.order.pojo;

import java.io.Serializable;
import java.util.List;

import com.bh.goods.pojo.CouponLog;
import com.bh.goods.pojo.GoodsCartListShopIdList;
import com.bh.user.pojo.MemberUserAddress;

public class CleanAccount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4558618362005902221L;
	
	public List<GoodsCartListShopIdList> goodsCartsList ;
	private Integer goodsNum;
	private double price;
	private double totalCount;
	private double deliveryPrice;//2017-10-23 添加，chengfengyun物流原价单位分
	public List<MemberUserAddress> userAddressesList;
	

	
	private List<OrderPayment> orderPayment;
	
	private List<OrderExpressType> orderExpressType;
	
	private String cartIds;//2017-10-18购物车的id
	private String orderCartIds;
	private BHSeed bhSeed;//滨惠豆的信息
	
    private List<CouponLog> CouponLogList; //当前的商品可以使用的优惠劵信息

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}


	public double getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(double totalCount) {
		this.totalCount = totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<GoodsCartListShopIdList> getGoodsCartsList() {
		return goodsCartsList;
	}

	public void setGoodsCartsList(List<GoodsCartListShopIdList> goodsCartsList) {
		this.goodsCartsList = goodsCartsList;
	}

	

	public List<OrderPayment> getOrderPayment() {
		return orderPayment;
	}

	public void setOrderPayment(List<OrderPayment> orderPayment) {
		this.orderPayment = orderPayment;
	}

	public List<OrderExpressType> getOrderExpressType() {
		return orderExpressType;
	}

	public void setOrderExpressType(List<OrderExpressType> orderExpressType) {
		this.orderExpressType = orderExpressType;
	}

	public String getCartIds() {
		return cartIds;
	}

	public void setCartIds(String cartIds) {
		this.cartIds = cartIds;
	}

	public double getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(double deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}

	public List<MemberUserAddress> getUserAddressesList() {
		return userAddressesList;
	}

	public void setUserAddressesList(List<MemberUserAddress> userAddressesList) {
		this.userAddressesList = userAddressesList;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getOrderCartIds() {
		return orderCartIds;
	}

	public void setOrderCartIds(String orderCartIds) {
		this.orderCartIds = orderCartIds;
	}

	public BHSeed getBhSeed() {
		return bhSeed;
	}

	public void setBhSeed(BHSeed bhSeed) {
		this.bhSeed = bhSeed;
	}

	public List<CouponLog> getCouponLogList() {
		return CouponLogList;
	}

	public void setCouponLogList(List<CouponLog> couponLogList) {
		CouponLogList = couponLogList;
	}

	

	
}
