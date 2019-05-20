package com.bh.admin.service;

import com.bh.admin.pojo.goods.CouponGift;
import com.bh.utils.PageBean;

public interface CouponGiftService {
	//新增
	int insert(CouponGift entity) throws Exception;
	
	//编辑
	int edit(CouponGift entity) throws Exception;
	
	//删除
	int delete(CouponGift entity) throws Exception;
	
	//详情
	CouponGift get(CouponGift entity) throws Exception;
	
	//管理列表
    PageBean<CouponGift> listPage(CouponGift entity) throws Exception;
    
    //大礼包编辑时删除优惠券
  	int deleteAndGift(int id) throws Exception;
  	
  	int update(CouponGift entity);
}
