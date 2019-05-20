package com.bh.admin.service;


import java.util.List;

import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberSend;
import com.bh.utils.PageBean;

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
	/*scj-后台配送员管理*/
	PageBean<MemberSend> pageList(String pageSize, String currentPage, String status, String name, String type) throws Exception;
	
	/*审核配送员*/
	int auditSend(String mId, String status) throws Exception;
	
	/*配送员详情*/
	MemberSend sendDetails(String mId) throws Exception;
	
	/*获取用户身份证*/
	String getIdentity(String mId) throws Exception;
}
