package com.wechat.service;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface WechatServices {
     //自动回复
	public String weixinPost(HttpServletRequest request) ;

	public void saveOrUpdateUserLogin(String openid, Map<String, Object> map, String json, String json2,
			HttpServletRequest request,HttpServletResponse response);

	public void wxMergePhone(int i, int j);

	public int saveMergeLog(Integer valueOf, Integer valueOf2, String openid);
}
