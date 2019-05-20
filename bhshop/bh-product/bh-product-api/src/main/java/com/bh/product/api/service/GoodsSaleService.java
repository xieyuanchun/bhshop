package com.bh.product.api.service;

import java.util.List;
import java.util.Map;

import com.bh.goods.pojo.GoodsSaleP;


public interface GoodsSaleService {
	int add(GoodsSaleP coupon);
	
	
	int update(GoodsSaleP coupon);
	
	int delete(GoodsSaleP coupon);
	//PC端分页显示
	Map<String,Object> list(GoodsSaleP coupon);
	
	List<GoodsSaleP> get(GoodsSaleP g);
}
