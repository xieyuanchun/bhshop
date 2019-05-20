package com.order.user.service;

import java.util.Map;

import com.bh.user.pojo.Member;

public interface PayCallbackService {
	
	
	/***
	 * 支付成功后的操作
	 * out_trade_no:订单号，即order_main表的orderNo
	 * transaction_id:第三方交易号
	 * strs:数组，该参数是orderBody的值
	 * **/
	void paySesessUnion(String out_trade_no,String transaction_id,String[] strs);
}
