package com.bh.admin.service;

import java.util.List;
import java.util.Map;

import com.bh.admin.pojo.goods.ExchangeGroup;

public interface ExchangeGroupService {

	
	
	int add(ExchangeGroup e);
	
	int update(ExchangeGroup e);
	
	Map<String, Object> listPage(ExchangeGroup e);
	
	int delete(ExchangeGroup e);
	
	List<ExchangeGroup> list();
	
}
