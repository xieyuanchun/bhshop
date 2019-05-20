package com.wechat.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.result.BhResult;
import com.bh.user.mapper.WXMSgTemplate;
import com.bh.utils.pay.WXPayUtil;
import com.bh.wxpayjsapi.common.JsonUtil;
import com.wechat.service.WechatTemplateMsgService;
import com.wechat.vo.TemplateMsgResult;
import com.wechat.vo.WechatTemplateMsg;


@Controller
@RequestMapping("/sendTemplateMsg")
public class WechatTemplateMsgController {

	public static final Logger logger = Logger.getLogger(WechatTemplateMsgController.class); 
	
	@Autowired
	WechatTemplateMsgService wtService;
	
	@RequestMapping("/send")
	@ResponseBody
	public BhResult send(){
		   BhResult  result =null;
	    try{
	    	    Map<String, Object> map3 =  WXPayUtil.getAccess_Token(Contants.appId, Contants.appSecret);
                TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>(); 
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(currentTime);
                //根据具体模板参数组装  
                params.put("first",WechatTemplateMsg.item("打印销售单验证码", "#000000"));  
                params.put("keyword1",WechatTemplateMsg.item("滨惠商城后台管理系统", "#000000"));  
                params.put("keyword2",WechatTemplateMsg.item("", "#000000"));  
                params.put("keyword3",WechatTemplateMsg.item("", "#000000"));  
                params.put("keyword4",WechatTemplateMsg.item(dateString, "#000000"));  
                params.put("remark",WechatTemplateMsg.item("", "#000000"));  
                WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();  
                wechatTemplateMsg.setTemplate_id("JWsnH2yuqSgkz6uBWC4GQ_tyvOxQk5OtdzNAYoI79dU");  //模板ID 
                wechatTemplateMsg.setTouser("oj9MyxAHaJMMaL8b5MfexVNyPHPU");  //openID
                wechatTemplateMsg.setUrl(Contants.BIN_HUI_URL+"/binhuiApp/home");  //模板跳转链接
                wechatTemplateMsg.setData(params);  
                String data = JsonUtil.toJson(wechatTemplateMsg);
                TemplateMsgResult templateMsgResult = new TemplateMsgResult();
                templateMsgResult =  wtService.sendTemplate(String.valueOf(map3.get("access_token")), data); 
                result = new BhResult(200,"发送成功",templateMsgResult);
	    }catch(Exception e){
	    	e.printStackTrace();
	    	result = new BhResult(200,"发送失败",null);
	    }
       return result;  
	}
	
	@RequestMapping("/sendTeamFail")
	@ResponseBody
	public void testTeamFail(){	
		try {
			//wtService.sendTeamFailTemplate("oj9MyxE6qOyXvyJts5nd6UrPS2q4","哈哈哈哈哈哈哈哈哈或哈哈哈或哈哈哈或哈哈哈或或或或或或或或或或或或或或或或或或或或或或或或或或"
					//,12,"37691804231427449981",12);
			//wtService.sendGroupTemplate("15188","4682","15154");
			//wtService.sendStartGroupTemplate("37691804231427449981");
			wtService.sendJoinGroupTemplate("37691804241844447248");
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
