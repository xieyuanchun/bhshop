package com.bh.admin.service;

import com.bh.admin.pojo.order.RechargePhone;
import com.bh.utils.PageBean;

public interface RechargePhoneService {
	int add(RechargePhone record);
	
	int update(RechargePhone record);
	
	int delete(RechargePhone record);
	
	PageBean<RechargePhone> listPage(RechargePhone r);
}
