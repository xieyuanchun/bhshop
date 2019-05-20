package com.order.user.service;

import java.util.List;

import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;

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
	
   //根据用户id 获取信息
   List<Wallet> getByUid(Wallet entity);
   //小程序-钱包-充值功能  zlk WalletLog
   int addWalletLog(WalletLog record);

void insertAdminAndMemberSend(MemberShop memberShop1, Integer getmId);
 
}
