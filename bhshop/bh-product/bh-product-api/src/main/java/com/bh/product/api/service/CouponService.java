package com.bh.product.api.service;

import java.util.Map;

import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponLog;

public interface CouponService {

	
	int addCoupon(Coupon coupon);
	
	
	int updateCoupon(Coupon coupon);
	
	int deleteCoupon(Coupon coupon);
	//PC端分页显示
	Map<String,Object> listCoupon(Coupon coupon);
	
	Coupon selectByPrimaryKey(Integer id);
	
	//移动端分页显示，可领取
    Map<String,Object> moblieListCoupon(Coupon coupon);
}
