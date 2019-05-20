package com.bh.admin.service;

import java.util.List;

import com.bh.admin.pojo.user.MemberShop;



public interface ShopMService {
	List<MemberShop> selectMemberShop(MemberShop memberShop) throws Exception;
	
	//商家信息的更新
	int updateMemberShop(MemberShop memberShop) throws Exception;
	
	//押金支付成功
	int updateMemberShopByOrderNo (MemberShop memberShop) throws Exception;
	
	//根据主键查找memberShop
	MemberShop selectMemberShopById(Integer shopId) throws Exception;
	
	//更新订单号(未支付)
	int updateMemberShopByDespo(MemberShop memberShop) throws Exception;
	
	//免审核押金支付成功
	int updateMemberShopByDescNo (MemberShop memberShop) throws Exception;
	
	//通过depositNo查询该订单是否支付成功
	MemberShop checkIsPaySeccuss(String depositNo) throws Exception;
}
