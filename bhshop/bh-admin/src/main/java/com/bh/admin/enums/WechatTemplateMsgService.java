package com.bh.admin.enums;

import com.bh.admin.vo.TemplateMsgResult;

public interface WechatTemplateMsgService {

	//发送打印验证码消息模板
	 public TemplateMsgResult sendTemplate(String accessToken, String data);
	 
	//发送拼团成功消息模板
	 public TemplateMsgResult sendGroupTemplate(String id, String orderId,String goodId);
}
