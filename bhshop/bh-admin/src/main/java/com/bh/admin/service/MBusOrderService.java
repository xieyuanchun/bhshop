package com.bh.admin.service;

import com.bh.admin.pojo.goods.MSendMsg;
import com.bh.admin.pojo.order.OrderShop;

public interface MBusOrderService {
	
	MSendMsg selectMSendMsg(MSendMsg msg) throws Exception;
	
	/*scj-商家后台针对整个订单抛单*/
	OrderShop castSheet(String id, String deliveryPrice) throws Exception;

}
