
package com.order.job;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bh.config.Contants;
import com.bh.user.mapper.MemberNoticeMapper;
import com.bh.user.pojo.MemberNotice;
import com.bh.utils.pay.HttpService;

@Component
public class BhJob {
	@Autowired
	private MemberNoticeMapper memberNoticeMapper;//消息的mapper
	public synchronized void schedule() {
		// String baseUrl = "http://192.168.0.10/bh-webserver/webPush/push";
		String baseUrl = Contants.BIN_HUI_URL+"/bh-webserver/webPush/push";
		List<MemberNotice> list = memberNoticeMapper.selectMemberNoticeList(null);
		for (MemberNotice mn:list) {
			try {
				String url = baseUrl+"?mId="+mn.getmId()+"&message="+mn.getMessage();
				String ret = HttpService.doGet(url);

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
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}

		}

	}
}
