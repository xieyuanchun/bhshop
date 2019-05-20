package com.bh.user.api.service;

import com.bh.user.pojo.MemberSend;
import com.bh.user.pojo.MemberShop;
import com.bh.utils.PageBean;


public interface CheckUserService {
	
	//查找未审核的商家信息
	PageBean<MemberShop> selectAllShopByStatus(String pageSize,String currentPage) throws Exception;
	
	
	//查找未审核的配送员信息
	PageBean<MemberSend> selectAllSendByStatus (String pageSize,String currentPage) throws Exception;
	//更新审核的状态
	
	int updateShopStatus(MemberShop memberShop) throws Exception;
	int updateSendStatus(MemberSend memberSend) throws Exception;
}
