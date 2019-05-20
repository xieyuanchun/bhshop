package com.order.job;

import com.bh.config.Contants;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberNoticeMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberNotice;
import com.bh.utils.pay.HttpService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
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
@JobHander(value="teamMsgJob")
@Component
public class TeamMsgJob extends IJobHandler {
	private final static String ENCODE = "UTF-8"; 
	@Autowired
	private MemberNoticeMapper memberNoticeMapper;//消息的mapper
	@Autowired
	private MemberMapper memberMapper;
	@Override
	public synchronized ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("XXL-JOB, Hello World.");
		// String baseUrl = "http://192.168.0.10/bh-webserver/webPush/push";
		String baseUrl = Contants.BIN_HUI_URL+"/bh-webserver/webPush/pushTeamMsg";
		MemberNotice noticeReq = new MemberNotice();
		noticeReq.setMsgType(2);
		List<MemberNotice> noticeList = memberNoticeMapper.selectMemberNoticeList(noticeReq);
	//	List<Member> memberList = memberMapper.selectAll();

		XxlJobLogger.log("noticeList size:" + noticeList.size());
		for (MemberNotice mn:noticeList) {
			try {
				
				String url = baseUrl+"?message="+mn.getMessage()+"&goodsId="+mn.getGoodsId();
			//	url =URLEncoder.encode(url, ENCODE);
				String ret = HttpService.doGet(url);
				XxlJobLogger.log("pushTeamMsg ret:" +ret);

				if(ret.equals("true")){
					MemberNotice notice = new MemberNotice();
					notice.setId(mn.getId());
					notice.setIsRead(1);
					Date curDate = new Date();
					notice.setLastExcuTime(curDate);
					notice.setUpdateTime(curDate);
					memberNoticeMapper.updateByPrimaryKeySelective(notice);
				}else{
					MemberNotice notice = new MemberNotice();
					notice.setId(mn.getId());
					notice.setIsRead(0);
					Date curDate = new Date();
					notice.setLastExcuTime(curDate);
					memberNoticeMapper.updateByPrimaryKeySelective(notice);
				}
				MemberNotice notice = new MemberNotice();
				notice.setId(mn.getId());
				notice.setIsRead(1);
				Date curDate = new Date();
				notice.setLastExcuTime(curDate);
				notice.setUpdateTime(curDate);
				memberNoticeMapper.updateByPrimaryKeySelective(notice);
				
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}

		}

	
		for (int i = 0; i < 5; i++) {
			XxlJobLogger.log("beat at:" + i);
			TimeUnit.SECONDS.sleep(2);
		}
		return ReturnT.SUCCESS;
	}
	
}
