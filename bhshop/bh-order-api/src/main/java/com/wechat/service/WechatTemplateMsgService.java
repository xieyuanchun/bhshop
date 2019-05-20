package com.wechat.service;

import com.bh.user.mapper.WXMSgTemplate;
import com.wechat.vo.TemplateMsgResult;



public interface WechatTemplateMsgService {

	//发送打印验证码消息模板
	 public TemplateMsgResult sendTemplate(String accessToken, String data);
	 
	//发送拼团成功消息模板
	 public TemplateMsgResult sendGroupTemplate(String id, String orderId,String goodId);

	 
	//发送发起团成功消息模板
	 public TemplateMsgResult sendStartGroupTemplate(String orderNo);
	 
	//给团主发送某某加入团成功消息模板
	 public TemplateMsgResult sendJoinGroupTemplate(String orderNo);
	 
	//给参团人发送参团成功消息模板
    public TemplateMsgResult sendJoinedGroupTemplate(String orderNo);
	 
	 //已发货消息模板
	 public TemplateMsgResult sendGroupGoodTemplate(WXMSgTemplate template);
	 
	 //已收获消息模板
	 public TemplateMsgResult receiGroupGoodsGoodTemplate(WXMSgTemplate template);

	 
	//发送拼团失败消息模板
	public TemplateMsgResult sendTeamFailTemplate(String opendId,String goodsName,double refundMoney,String orderNo,double goodsPrice);

}
