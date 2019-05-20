package com.bh.admin.service;

import java.util.List;
import java.util.Map;
import com.bh.admin.pojo.goods.CouponLog;
import com.bh.admin.pojo.goods.ReturnCouponLog;


public interface CouponLogService {

	//用户领取优惠劵
	int addCouponLog(CouponLog couponLog);
	
	//我的移动端列表
	Map<String,Object> listCouponLog(CouponLog couponLog);
	
	//我的移动端当前商品可以使用的列表
	Map<String,Object> goodListCouponLog(CouponLog couponLog);
	
	//获取所有的优惠劵
	Map<String, Object> allListCouponLog(CouponLog couponLog);
	
	//根据优惠劵id和用户id获取当前数据，判断用户是否领取过优惠劵
	List<CouponLog> getByMidAndCouponId(CouponLog couponLog);
	
}
