package com.bh.product.api.service;

import java.util.List;
import java.util.Map;

import com.bh.pojo.User;

public interface userService {

	List<Map<String,Object>> selectAll(Integer id) throws Exception;
	
	int selectinsert(String id, String seq, String name, String mobile) throws Exception;
	
	int selectdelete(Integer id) throws Exception;
	
	int selectupdate(User usermap) throws Exception;
}
