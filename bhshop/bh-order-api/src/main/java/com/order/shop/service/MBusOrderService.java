package com.order.shop.service;

import com.bh.goods.pojo.MSendMsg;
import com.bh.order.pojo.OrderShop;

public interface MBusOrderService {
	
	MSendMsg selectMSendMsg(MSendMsg msg) throws Exception;
	
	/*scj-商家后台针对整个订单抛单*/
	OrderShop castSheet(String id, String deliveryPrice) throws Exception;

}
