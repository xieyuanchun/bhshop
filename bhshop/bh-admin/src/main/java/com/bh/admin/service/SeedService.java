package com.bh.admin.service;

import java.util.List;

import com.bh.admin.pojo.order.OrderSeed;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.pojo.user.SeedModel;



public interface SeedService {
	
	//插入seedModel表
	int insertSeedModel(SeedModel model) throws Exception;
	
	//查询member
	Member selectMemberBymId(Integer mId) throws Exception;
	
	OrderSeed insertOrderSeed(OrderSeed seed) throws Exception;
	int updateByStatus(OrderSeed seed) throws Exception;
	
	//isBhshop:是否是滨惠自营店0不是,1是滨惠自营店
	List<MemberShop> selectBhShop(Integer isBhshop) throws Exception;
}
