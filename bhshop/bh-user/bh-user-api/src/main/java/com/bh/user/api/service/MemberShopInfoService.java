package com.bh.user.api.service;

import java.util.Map;

import com.bh.pojo.ShopMsg;
import com.bh.user.pojo.MemberShopInfo;

public interface MemberShopInfoService {

	int saveOrUpdateByPerson(MemberShopInfo entity) throws Exception;

	int saveOrUpdateByBusiness(MemberShopInfo entity) throws Exception;

	int updateByBusiness(MemberShopInfo entity) throws Exception;

	Map<Object, Object> getApplyDetails(String string, String string2, String string3);

	ShopMsg selectStep(String string);

	int  getApplyType(String string);
	
			
}
