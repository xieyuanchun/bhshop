package com.order.job;

import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.pojo.Goods;
import com.bh.order.mapper.OrderTeamMapper;
import com.bh.order.pojo.OrderTeam;
import com.bh.order.pojo.SysTeam;
import com.bh.user.mapper.MemberNoticeMapper;
import com.bh.user.pojo.MemberNotice;
import com.bh.utils.pay.HttpService;
import com.order.sys.service.SysService;
import com.order.user.service.UtilService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 任务Handler的一个Demo（Bean模式）
 * 
 * 开发步骤：
 * 1、新建一个继承com.xxl.job.core.handler.IJobHandler的Java类；
 * 2、该类被Spring容器扫描为Bean实例，如加“@Component”注解；
 * 3、添加 “@JobHander(value="自定义jobhandler名称")”注解，注解的value值为自定义的JobHandler名称，该名称对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 * 
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHander(value="wxTicketJob")
@Component
public class WxTicketJob extends IJobHandler {
	@Autowired
	private UtilService utilService;
	@Override
	public synchronized ReturnT<String> execute(String... params) throws Exception {
		utilService.getTicket();
		XxlJobLogger.log("beat 2 seconds");
		TimeUnit.SECONDS.sleep(2);
		return ReturnT.SUCCESS;
	}
	
}
