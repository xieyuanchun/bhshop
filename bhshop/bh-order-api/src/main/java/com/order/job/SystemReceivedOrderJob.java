package com.order.job;

import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.pojo.OrderShop;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 任务Handler的一个Demo（Bean模式） 开发步骤：
 * 1、新建一个继承com.xxl.job.core.handler.IJobHandler的Java类；
 * 2、该类被Spring容器扫描为Bean实例，如加“@Component”注解； 3、添加
 * “@JobHander(value="自定义jobhandler名称")”注解，注解的value值为自定义的JobHandler名称，
 * 该名称对应的是调度中心新建任务的JobHandler属性的值。 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 * @Description: 系统自动确认收货定时器
 * @author xieyc
 * @date 2018年8月16日 下午2:36:26 
 */
@JobHander(value ="systemReceivedOrderJob")
@Component
public class SystemReceivedOrderJob extends IJobHandler {
    private final static String ENCODE = "UTF-8";
    @Autowired
    private  OrderShopMapper orderShopMapper;
  
	@Override
	public synchronized ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("XXL-JOB, Hello World.");
		try {
			List<OrderShop> list=orderShopMapper.systemReceivedOrder();
			for (OrderShop orderShop : list) {
				orderShop.setStatus(5);
				orderShop.setConfirmOrder(1);
				orderShop.setReceivedtime(new Date());
				orderShopMapper.updateByPrimaryKeySelective(orderShop);
			}
			//orderShopMapper.updateBatch(list);//批量更新（confirm_order变成1，status变5，receivedtime变为当前时间）
		} catch (Exception e) {
			XxlJobLogger.log("系统自动确认收货定时器  error=" + e.getMessage());
			e.printStackTrace();
		}
		return ReturnT.SUCCESS;
	}
}
