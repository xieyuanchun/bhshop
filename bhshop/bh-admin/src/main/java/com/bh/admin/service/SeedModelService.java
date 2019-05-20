package com.bh.admin.service;


import java.util.List;

import com.bh.admin.pojo.order.OrderSeed;
import com.bh.admin.pojo.user.MemberUser;
import com.bh.admin.pojo.user.MemerScoreLog;
import com.bh.admin.pojo.user.SeedModel;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

public interface SeedModelService {
	//已累计签到的天数接口
	List<SeedModel> attendanceDays(MemberUser memberUser) throws Exception;
	
	//是否签到：1，已签到，0为签到
	int isAttendances(MemerScoreLog log) throws Exception;
	
	//签到的接口
	int attendances(MemerScoreLog log) throws Exception;
	
	//插入seedModel表
	int insertSeedModel(SeedModel model) throws Exception;
	
	//种子模型列表
	PageBean<SeedModel> seedModelList(SeedModel model,Integer page,Integer rows) throws Exception;
	PageBean<SeedModel> deleteSeedList(SeedModel model,Integer page,Integer rows) throws Exception;
	
	//修改种子模型
	int updateSeedModel(SeedModel seedModel) throws Exception;
	
	//用户的收益
	BhResult getBalance(MemerScoreLog log) throws Exception;
	
	//删除种子的模型
	int deleteSeedModel(SeedModel seedModel) throws Exception;
	
	
	//判断用户是否购买种子
	BhResult isBuy(MemberUser memberUser) throws Exception;
	
	MemberUser selectMemberUserBymId(Integer mId) throws Exception;
	
	PageBean<MemberUser> selectMemberUserCard(MemberUser card,Integer page,Integer size) throws Exception;
	
	List<OrderSeed> selectOrderSeedBymId(OrderSeed orderSeed) throws Exception;
	//pc种子模型列表
	PageBean<SeedModel> seedModelListByPc(SeedModel model,Integer page,Integer rows) throws Exception;
	
	//通过种子订单号查询该用户的信息
	BhResult getUsermsgByOrderNo(String orderNo) throws Exception;
	
}
