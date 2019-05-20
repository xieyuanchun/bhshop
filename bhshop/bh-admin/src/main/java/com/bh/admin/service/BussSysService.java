package com.bh.admin.service;

import java.util.List;

import com.bh.admin.pojo.order.OrderPayment;
import com.bh.result.BhResult;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberUser;
import com.bh.admin.pojo.user.MemberUserDetail;
import com.bh.admin.pojo.user.ShowTiaoZheng;
import com.bh.utils.PageBean;

public interface BussSysService {
	
	//2018-1-9
	PageBean<OrderPayment> selectOrderPayment(OrderPayment orderPayment,Integer page,Integer size) throws Exception;
	
	//新增支付方式
	int insertOrderPayment(OrderPayment orderPayment) throws Exception;
	
	OrderPayment selectOrderPayment(OrderPayment orderPayment) throws Exception;
	
	int updateOrderPayment(OrderPayment orderPayment) throws Exception;
	
	int deleteOrderPayment(OrderPayment orderPayment) throws Exception;
	
	/***************************************以下是会员**********************************************************/
	PageBean<MemberUser> memberUserList(MemberUser memberUser) throws Exception;
	//会员详情
	MemberUserDetail selectMemberUserDetail(MemberUser memberUser) throws Exception;
	BhResult addMember(MemberUser memberUser) throws Exception;
	BhResult updateMember(MemberUser memberUser) throws Exception;
	List<MemberUser> selectMemberUserByFullName(MemberUser memberUser) throws Exception;
	List<Member> selectMember(Member member) throws Exception;
	ShowTiaoZheng selecttiaozheng(MemberUser memberUser) throws Exception;
	int updatetiaozheng(ShowTiaoZheng showTiaoZheng) throws Exception;
}
