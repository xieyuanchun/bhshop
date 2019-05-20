package com.bh.user.api.service;


import java.util.List;

import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberSend;

public interface SendService {
	int insertSendMsg(MemberSend memberSend) throws Exception;
	/**
	 * CHENG-20171124-01
	 * 速达-个人中心
	 * @return MemberSend对象
	 */
	MemberSend selectMemberSendCenter(MemberSend memberSend) throws Exception;
	MemberSend getMsg(String phone) throws Exception;
	List<Member> getMsg1(String phone,Integer id) throws Exception;
	
	/*配送员详情*/
	MemberSend sendDetails(String mId) throws Exception;
	
	/*获取用户身份证*/
	String getIdentity(String mId) throws Exception;
}
