package com.bh.user.api.service;

import java.util.List;

import com.bh.goods.pojo.GoodsShareLog;
import com.bh.result.BhResult;
import com.bh.user.pojo.BankSimple;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberBankCard;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemberUserSimple;
import com.bh.user.pojo.TbBank;

public interface MemberUserService {
	
	Member getMember(Integer Id) throws Exception;
	Member getMemberById(Integer Id) throws Exception;
	Member updateByPrimaryKeySelective(MemberUser memberUser) throws Exception;
	
	int updateEmailBymId(MemberUser memberUser) throws Exception;
	
	//更新Member用户的头像
	int updateMemberImg(Member member) throws Exception;
	
	MemberUser selectByPrimaryKey(Integer mId) throws Exception;
	
	/********2017-11-09cheng更新支付密码**********/
	int updatePayCode(MemberUser memberUser) throws Exception;
	
	MemberUser selectMember(MemberUser memberUser) throws Exception;
	
	/********2017-12-09cheng用户认证1**********/
	MemberUser toAuth1u(MemberUser memberUser) throws Exception;
	
	/********2017-12-11cheng用户认证2:添加银行卡**********/
	int toAuth2(MemberBankCard memberUser) throws Exception;
	
	
	
	//用户分享
	int goodShare(GoodsShareLog log) throws Exception;
	
	//用户商城钱包接口
	MemberUserSimple userBalance(Member member) throws Exception;
	
	//用户选择银行卡
	List<BankSimple> selectBankCard(Integer mId) throws Exception;
	
	List<TbBank> selectTbBankList(TbBank tbBank) throws Exception;
	//2018-4-23修改昵称
	BhResult updateUsername(Member member) throws Exception;
	
	MemberUser selectUserPoint(Integer mId) throws Exception;
}
