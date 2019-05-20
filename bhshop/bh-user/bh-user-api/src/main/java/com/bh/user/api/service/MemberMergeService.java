package com.bh.user.api.service;

import java.util.Map;


import com.bh.user.pojo.Member;

public interface MemberMergeService {
	Map<Object, Object> wxBindingInfo(String openid);
	
	Map<Object, Object> phoneBindingInfo(String string);

	int changePhone(String newPhone, Integer id);

	int svaePhoneBindingWx(String string, Map<String, Object> mapWx,String openid) throws Exception;

	int unBindPhone(Integer id);

	int saveWxBindingPhone(String string, Map<String, Object> mapWx) throws Exception;

	Map<Object, Object> bindingInfo(Integer id);

	int changePhoneBefore(Integer id);
		 
			
}
