package com.bh.user.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.utils.JsonUtils;


public class LoginInterceptor implements HandlerInterceptor{
	
	@Value("${URL}")
	private String URL;
	@Value("${USERINFO}")
	private String USERINFO;	//用户信息
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		//此处认证用户的身份
		//从cookie获取token 
		HttpSession session = request.getSession(false);
		if(session!=null) {
			Member token =  (Member) session.getAttribute(USERINFO);
			if(StringUtils.isEmpty(token)){
				response.setContentType("application/json;charset=UTF-8");
				//拦截请求
				BhResult rlt = new BhResult();
				rlt.setStatus(100);
				rlt.setMsg("您还未登录,请重新登录");
				response.getWriter().write(JsonUtils.objectToJson(rlt));
				return false;
			}
		}else {
			response.setContentType("application/json;charset=UTF-8");
			//拦截请求
			BhResult rlt = new BhResult();
			rlt.setStatus(100);
			rlt.setMsg("您还未登录,请重新登录");
			response.getWriter().write(JsonUtils.objectToJson(rlt));
			return false;
		}
		return true;
	}

}
