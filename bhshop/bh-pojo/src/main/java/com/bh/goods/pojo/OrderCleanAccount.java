package com.bh.goods.pojo;

import java.util.List;

import com.bh.order.pojo.BHSeed;
import com.bh.user.pojo.MemberUserAddress;

public class OrderCleanAccount {
	
	public List<OrderGoodsCartListShopIdList> goodsCartsList ;
	private Integer goodsNum;
	private double price;
	private double totalCount;
	private double deliveryPrice;//2017-10-23 添加，chengfengyun物流原价单位分（前端不取）
	public List<MemberUserAddress> userAddressesList;	
	private String cartIds;//2017-10-18购物车的id
	private String orderCartIds;
	private BHSeed bhSeed;//滨惠豆的信息
	private Integer point;
	private double orderPrice;
    
    private AuctionPojo  auctionPojo;//荷兰式拍卖pojo ==>xieyc add 
    
    private List<CouponLog> couponLogList;
    
    private List<UsableCoupon> usableCouponListAll;

    private Integer is_lock;  //是否锁定 滨惠豆、优惠劵  0不锁，1锁
	
	
	public List<UsableCoupon> getUsableCouponListAll() {
		return usableCouponListAll;
	}
	public void setUsableCouponListAll(List<UsableCoupon> usableCouponListAll) {
		this.usableCouponListAll = usableCouponListAll;
	}
	public AuctionPojo getAuctionPojo() {
		return auctionPojo;
	}
	public void setAuctionPojo(AuctionPojo auctionPojo) {
		this.auctionPojo = auctionPojo;
	}
	public List<OrderGoodsCartListShopIdList> getGoodsCartsList() {
		return goodsCartsList;
	}
	public void setGoodsCartsList(List<OrderGoodsCartListShopIdList> goodsCartsList) {
		this.goodsCartsList = goodsCartsList;
	}
	public Integer getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(double totalCount) {
		this.totalCount = totalCount;
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
	public String getCartIds() {
		return cartIds;
	}
	public void setCartIds(String cartIds) {
		this.cartIds = cartIds;
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
		return couponLogList;
	}
	public void setCouponLogList(List<CouponLog> couponLogList) {
		this.couponLogList = couponLogList;
	}
	public Integer getIs_lock() {
		return is_lock;
	}
	public void setIs_lock(Integer is_lock) {
		this.is_lock = is_lock;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public double getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}

	
	
	
}
