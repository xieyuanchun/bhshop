package com.bh.admin.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.order.ShopOrderRecordVo;
import com.bh.admin.pojo.order.ShopWithdraw;
import com.bh.admin.pojo.order.ShopWithdrawVo;
import com.bh.utils.PageBean;

public interface ShopManageService {

	int withdraw(ShopWithdraw shopWithdraw);

	int mWithdraw(ShopWithdraw shopWithdraw);

	PageBean<ShopWithdrawVo> withdrawRecordListByShop(ShopWithdraw entity);

	PageBean<ShopWithdrawVo> mWithdrawRecordListByShop(ShopWithdraw entity);

	PageBean<ShopWithdrawVo> withdrawRecordListByPt(ShopWithdraw entity);

	int update(ShopWithdraw entity,Integer userId);

	Map<Object, Object> lastWithdrawRecord(ShopWithdraw entity);

	Map<Object, Object> mLastWithdrawRecord(ShopWithdraw entity);

	void excelExport(String startTime, String endTime, String isPay, String type, String state, String shopId,
			HttpServletRequest request, HttpServletResponse response) throws Exception;

	PageBean<ShopOrderRecordVo> shopOrderRecord(OrderSku orderSku) throws Exception;

	Map<Object, Object> countWithdraMoney(Integer shopId);

	Map<Object, Object> mCountWithdraMoney(int intValue);

	boolean isWithdrawByToday(Integer shopId);

	boolean mIsWithdrawByToday(int intValue);

	Map<Object, Object> mCountMoney(String startTime, String endTime, int intValue) throws Exception;

	Map<Object, Object> countMoney(String startTime, String endTime, Integer shopId) throws Exception;

	Map<Object, Object> lastWithdrawInfo(ShopWithdraw entity);

	Map<Object, Object> mLastWithdrawInfo(ShopWithdraw entity);


	
	
	
	


	
	

	


}
