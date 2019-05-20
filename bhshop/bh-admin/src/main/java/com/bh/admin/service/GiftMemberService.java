package com.bh.admin.service;

import java.util.Map;


import com.bh.admin.pojo.user.GiftMember;



public interface GiftMemberService {

	
	int add(GiftMember e);
	
	int update(GiftMember e);
	
	Map<String, Object> listPage(GiftMember e);
	
	int delete(GiftMember e);
	
	
}
