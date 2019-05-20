package com.order.shop.service;

import java.util.List;
import java.util.Map;

import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;

public interface OrderDeliveryService {
	/*配送端订单列表*/
	PageBean<OrderShop> deliveryOrder(String currentPage, String pageSize, String lat, String lng, int mId) throws Exception;
	
	/*配送端订单详情*/
	OrderShop deliveryOrderDetails(String id, String lat, String lng, int mId, String fz) throws Exception;
	
	/*配送员接单*/
	int receiveOrder(String id, int sId) throws Exception;
	
	/*确认发货*/
	int sendGoods(String id, int sId) throws Exception;
	
	/*取消配送*/
	int cancelOrder(String id, String reason, int sId) throws Exception;
	
	/*配送任务列表*/
	PageBean<OrderSendInfo> orderTask(String currentPage, String pageSize, String status, int mId, String lat, String lng) throws Exception;
	
	/*加载取消原因列表*/
	List<Map<String, Object>> getReasonList() throws Exception;
	
	/*交通工具列表*/
	List<Map<String, Object>> getVehicleList(int sId) throws Exception;
	
	/*订单类型列表*/
	List<Map<String, Object>> sendOrderType(int sId) throws Exception;
	
	/*交通工具的保存*/
	int saveVehicle(String code, int sId) throws Exception;
	
	/*设置订单类型的保存*/
	int saveType(String code, int sId) throws Exception;
	
	/*订单已送达*/
	int orderAreadlySend(String id, int sId) throws Exception;
	
	/*接单数量和总金额统计*/
	Map<String, Object> sendAccount(int sId) throws Exception;
	
	/*是否配送员验证*/
	boolean isDeliveryman(int mId) throws Exception;
	
	/*是否配送员验证*/
	List<Member> orderArrange(int mId) throws Exception;
	
	/*接单数量和总金额统计*/
	Map<String, Object> mySendWallet(int sId) throws Exception;
	
	/*收工*/
	int knockOff(int sId, String fz) throws Exception;
	
	/*评价记录*/
	Map<String, Object> evaluateRecord(String fz, int sId) throws Exception;
}
