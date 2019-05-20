package com.order.user.service;

import java.util.List;

import com.bh.order.pojo.RechargePhone;
import com.bh.utils.PageBean;

public interface RechargePhoneService {
	int add(RechargePhone record);
	
	int update(RechargePhone record);
	
	int delete(RechargePhone record);
	
	PageBean<RechargePhone> listPage(RechargePhone r);
	
	List<RechargePhone> getByOrderNo(RechargePhone r);
	
	void getOrderDetails() throws Exception;
}
