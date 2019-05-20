package com.wechat.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WechatAttentionService {
	public String processRequest(HttpServletRequest request,HttpServletResponse response);
}
