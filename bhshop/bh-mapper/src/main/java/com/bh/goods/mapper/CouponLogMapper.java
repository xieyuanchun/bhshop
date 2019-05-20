package com.bh.goods.mapper;

import java.util.Date;
import java.util.List;

import com.bh.goods.pojo.CouponLog;


public interface CouponLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponLog record);

    int insertSelective(CouponLog record);

    CouponLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponLog record);

    int updateByPrimaryKey(CouponLog record);
    //移动端我的优惠劵
    List<CouponLog> listPage(CouponLog record);
    
    //移动端我的优惠劵(未使用)
    List<CouponLog> notUseListPage(CouponLog record);
    
    //当前商品可以用的优惠劵(商家的)
    List<CouponLog> goodListPage(CouponLog record);
    
    //当前商品可以用的优惠劵(平台的)
    List<CouponLog> goodAllListPage(CouponLog record);
    
    //根据id和时间去获取没过期的优惠劵
    CouponLog selectByPrimaryKeyAndTime(CouponLog record);
    
    
    //所有的优惠劵
    List<CouponLog> allListPage(CouponLog record);
     
    //根据优惠劵id和用户id获取当前数据，判断用户是否领取过优惠劵
  	List<CouponLog> getByMidAndCouponId(CouponLog couponLog);
  	
  	
    //当前商品可以用的优惠劵(商家的)，优惠价格由高到低
  	List<CouponLog> goodListOrderByAmount(CouponLog couponLog);
  	
  	 //当前商品可以用的优惠劵(平台的)，优惠价格由高到低
  	List<CouponLog> getAllListOrderByAmount(CouponLog couponLog);
  	
  	
  	//根据订单id改状态
  	int updateByOrderNo(CouponLog record);
  	
  	//根据订单id获取当前的优惠劵金额信息
  	List<CouponLog> getByOrderId(CouponLog record);

	List<CouponLog> getLogByMid(Integer mId);


	List<CouponLog> getFreeOrRedCoupon(CouponLog findCouponLogFree);

	List<CouponLog> getPtCouponWithCatId(Date date, int mId, long catId, int shopId);

	String getCouponLogIdByMid(int srcUserId);

	CouponLog getByIdAndCouType(Integer couponId, int i);
  	
  	
  	
  	
}