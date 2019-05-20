package com.bh.admin.service;

import java.util.List;
import java.util.Map;

import com.bh.admin.pojo.goods.ExchangeSku;
import com.bh.admin.pojo.goods.GoodsSku;

public interface ExchangeSkuService {

	int add(ExchangeSku e);
	
	int update(ExchangeSku e);
	
	Map<String,Object> listPage(ExchangeSku e);
	
	int delete(ExchangeSku e);
	
	List<GoodsSku> getByjdsku(GoodsSku g);
}
