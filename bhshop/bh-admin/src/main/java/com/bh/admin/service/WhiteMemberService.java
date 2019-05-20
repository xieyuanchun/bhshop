package com.bh.admin.service;

import java.util.List;
import java.util.Map;
import com.bh.admin.pojo.user.WhiteMember;

public interface WhiteMemberService {

	int add(WhiteMember e);
	
	int update(WhiteMember e);
	
	Map<String, Object> listPage(WhiteMember e);
	
	int delete(WhiteMember e);
	
	List<WhiteMember> getByMid(WhiteMember e);
	
	String getAll();
}
