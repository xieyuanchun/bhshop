package com.order.shop.service;

import java.util.List;

import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponLog;
import com.bh.goods.pojo.GoodsSku;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberUserAddress;

public interface ExchangeGoodsService {

	 int exchangeGoods(String phone);
	
	 Member getById(int mId);
	
	 //根据mId选择
	 List<MemberUserAddress> selectAllAddressByisDel(MemberUserAddress address) throws Exception;
	 
     //插入
     int insertSelective(MemberUserAddress memberUserAddress) throws Exception;
     
     GoodsSku getBySkuNo(Long skuNo);
     
     //2018-6-6程凤云获得cartId
     BhResult selectCartId(Long skuNo,Integer mId,String addressId)throws Exception;
     
     Coupon getById(Coupon c); 
     
     int add(CouponLog c);
}
