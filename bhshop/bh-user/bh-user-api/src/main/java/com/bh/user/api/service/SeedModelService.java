package com.bh.user.api.service;


import java.util.List;

import com.bh.order.pojo.OrderSeed;
import com.bh.result.BhResult;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.SeedModel;

public interface SeedModelService {
	//已累计签到的天数接口
	List<SeedModel> attendanceDays(MemberUser memberUser) throws Exception;
	
	//是否签到：1，已签到，0为签到
	int isAttendances(MemerScoreLog log) throws Exception;
	
	//签到的接口
	int attendances(MemerScoreLog log) throws Exception;
		
	//用户的收益
	BhResult getBalance(MemerScoreLog log) throws Exception;
	
	//判断用户是否购买种子
	BhResult isBuy(MemberUser memberUser) throws Exception;
	
	MemberUser selectMemberUserBymId(Integer mId) throws Exception;
	
	List<OrderSeed> selectOrderSeedBymId(OrderSeed orderSeed) throws Exception;
		
	//通过种子订单号查询该用户的信息
	BhResult getUsermsgByOrderNo(String orderNo) throws Exception;
	
}
