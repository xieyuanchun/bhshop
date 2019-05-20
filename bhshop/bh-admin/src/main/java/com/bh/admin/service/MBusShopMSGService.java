package com.bh.admin.service;

import com.bh.admin.pojo.user.MBusEntity;
import com.bh.admin.pojo.user.MShopInCome;

public interface MBusShopMSGService {
	
	//查询商家的收入
	MShopInCome selectMShopInCome(MBusEntity entity) throws Exception;

}
