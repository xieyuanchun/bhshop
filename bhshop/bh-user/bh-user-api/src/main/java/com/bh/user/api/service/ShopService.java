package com.bh.user.api.service;

import java.util.List;

import com.bh.result.BhResult;
import com.bh.user.pojo.BusBankCard;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.utils.PageBean;

public interface ShopService {
	int insertMemberShopByStepOne(MemberShop memberShop) throws Exception;
	
	List<MemberShop> selectMemberShop(MemberShop memberShop) throws Exception;
	
	BhResult insertStep(MemberShop memberShop) throws Exception;
	
	MemberShop getMemberShop(Integer token) throws Exception;
	
	BhResult updateMemberShop(MemberShop memberShop) throws Exception;
	
	BhResult checkStepOne(MemberShop memberShop) throws Exception;
	
	BhResult insertMemberShop(MemberShop memberShop) throws Exception;
	
	PageBean<MemberShop> selectShopList(MemberShop memberShop,Integer page,Integer size) throws Exception;
	
	int updateStep(MemberShop memberShop) throws Exception;
	
	BhResult insertStepByPc(MemberShop memberShop) throws Exception;
	
	MemberShop selectMemberShopByPrimaryKey(Integer id) throws Exception;
	
	int insertAdmin(Integer mId) throws Exception;
	
	int insertMemberSend(MemberShop entity) throws Exception;
	
	String selectUsernameBymId(Integer mId) throws Exception;
	
	int insertBankCard(BusBankCard card) throws Exception;
	
	//将店铺设为自营店
	int updateShop(MemberShop memberShop) throws Exception;

}
