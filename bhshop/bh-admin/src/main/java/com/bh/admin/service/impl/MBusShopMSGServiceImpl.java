package com.bh.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.order.OrderRefundDocMapper;
import com.bh.admin.mapper.order.OrderShopMapper;
import com.bh.admin.pojo.order.OrderRefundDoc;
import com.bh.admin.service.MBusShopMSGService;
import com.bh.admin.pojo.user.MBusEntity;
import com.bh.admin.pojo.user.MShopInCome;

@Service
public class MBusShopMSGServiceImpl implements MBusShopMSGService{
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private OrderRefundDocMapper orderRefundDocMapper;
	
	
	
	//查询商家的收入
	public MShopInCome selectMShopInCome(MBusEntity entity) throws Exception{
		MShopInCome inCome = new MShopInCome();
		//商家收入=总收入-退款-拼单失败
		Integer i2 = 0;
		if (entity.getShopId() != null) {
			Long l1 = entity.getShopId();
			long l2 = l1;
			int i1 = (int)l2;
			i2 = i1; 
		}
		
		//今日总收入
		Integer thisDayTotalInCome = orderShopMapper.selectMShopThisDayInCome(i2);	
		//本月总收入
		Integer thisMonthTotalInCome = orderShopMapper.selectMShopThisMonthInCome(i2);
		
		//Integer thisDayRe = orderRefundDocMapper.selectMThisDayRefundMoney(i2);
		//Integer thisMonthRe = orderRefundDocMapper.selectMThisMonthRefundMoney(i2);
		
		//拼单失败的钱
		//Integer mRe1 = orderRefundDocMapper.selectMRe(i2, "1");
		//Integer mRe2 = orderRefundDocMapper.selectMRe(i2, "2");
		
		
		//Integer thisDayInCome = thisDayTotalInCome - thisDayRe - mRe1;
		Integer thisDayInCome = thisDayTotalInCome;
		//Integer lastMonthInCome = thisMonthTotalInCome - thisMonthRe - mRe2;
		Integer lastMonthInCome = thisMonthTotalInCome;
		double thisDayInCome1 = 0;
		thisDayInCome1 = (double) thisDayInCome/100;
		double lastMonthInCome1 = 0;
		lastMonthInCome1 = (double) lastMonthInCome/100;
		
		inCome.setThisDayInCome(thisDayInCome1);
		inCome.setLastMonthInCome(lastMonthInCome1);
		
		return inCome;
	}
}
