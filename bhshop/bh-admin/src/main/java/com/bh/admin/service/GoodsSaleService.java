package com.bh.admin.service;

import java.util.List;
import java.util.Map;
import com.bh.admin.pojo.goods.GoodsSaleP;


public interface GoodsSaleService {
	int add(GoodsSaleP coupon);
	
	
	int update(GoodsSaleP coupon);
	
	int delete(GoodsSaleP coupon);
	//PC端分页显示
	Map<String,Object> list(GoodsSaleP coupon);
	
	List<GoodsSaleP> get(GoodsSaleP g);
}
