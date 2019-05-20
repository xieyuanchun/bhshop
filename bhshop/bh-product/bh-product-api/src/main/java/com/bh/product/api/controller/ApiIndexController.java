package com.bh.product.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bh.config.Contants;
import com.bh.config.SwiftpassConfig;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.pay.WXPayConstants;
import com.bh.wxpayjsapi.service.WxLoginApi;

@RestController
public class ApiIndexController {
	/**
	 * SCJ-20180119-01
	 * 授权登录获取用户信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/index")
	public BhResult authorize(HttpServletRequest request, HttpServletResponse response){
		BhResult r = null;
		try {
			String code = request.getParameter("code");
			if(com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(code)){
				String appid=  Contants.appId;
			    String REDIRECT_URI = Contants.BIN_HUI_URL+"/bh-product-api/getOpenid";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
				url  = url.replace("APPID", java.net.URLEncoder.encode(appid, "utf-8")); 
			    url  = url.replace("REDIRECT_URI", java.net.URLEncoder.encode(REDIRECT_URI, "utf-8"));
			    response.setHeader("Access-Control-Allow-Origin", "*");
			    response.sendRedirect(url);
			}else{
				getOpenid(code, request, response);
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		}  catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	//获取openId
	@RequestMapping("/getOpenid")
    public BhResult getOpenid(String code,HttpServletRequest request, HttpServletResponse response) throws Exception {
		BhResult r = null;
		try {
			String retUrl = Contants.BIN_HUI_URL+"/binhuiApp/home";
			com.alibaba.fastjson.JSONObject jsonTexts = WxLoginApi.getOpenid(code);
			String accessToken = "";
	        if (jsonTexts.get("access_token")!=null) {
	        	accessToken = jsonTexts.get("access_token").toString();
	        }
	        String openid = "";
	        if (jsonTexts.get("openid")!=null) {
	        	openid = jsonTexts.get("openid").toString();
	        }
			request.getSession(false).setAttribute("openid", openid);
			String userInfo = WxLoginApi.getUserInfo(accessToken, openid); //获取用户基本信息
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(userInfo.toString());
			String url = retUrl+"?userInfo="+userInfo;
			response.sendRedirect(url);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
    }
	
	
	
	/**
	 * SCJ-20180125-01
	 * 注册授权
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/registers")
	public BhResult registers(HttpServletRequest request, HttpServletResponse response){
		BhResult r = null;
		try {
			String code = request.getParameter("code");
			if(com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(code)){
				String appid=  Contants.appId;
			    String REDIRECT_URI = Contants.BIN_HUI_URL+"/bh-product-api/getRegisters";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
				url  = url.replace("APPID", java.net.URLEncoder.encode(appid, "utf-8")); 
			    url  = url.replace("REDIRECT_URI", java.net.URLEncoder.encode(REDIRECT_URI, "utf-8"));
			    response.setHeader("Access-Control-Allow-Origin", "*");
			    response.sendRedirect(url);
			}else{
				getRegisters(code, request, response);
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		}  catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	//获取openId
	@RequestMapping("/getRegisters")
    public BhResult getRegisters(String code,HttpServletRequest request, HttpServletResponse response) throws Exception {
		BhResult r = null;
		try {
			String retUrl = Contants.BIN_HUI_URL+"/binhuiApp/registerstatus";
			com.alibaba.fastjson.JSONObject jsonTexts = WxLoginApi.getOpenid(code);
			String accessToken = "";
	        if (jsonTexts.get("access_token")!=null) {
	        	accessToken = jsonTexts.get("access_token").toString();
	        }
	        String openid = "";
	        if (jsonTexts.get("openid")!=null) {
	        	openid = jsonTexts.get("openid").toString();
	        }
			request.getSession().setAttribute("openid", openid); 	//session 去掉false
			//String userInfo = WxLoginApi.getUserInfo(accessToken, openid); //获取用户基本信息
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			//r.setData(userInfo.toString());
			r.setData(openid.toString());
			String url = retUrl+"?openid="+openid;
			response.sendRedirect(url);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
    }
	
	
	/**
	 * SCJ-20180125-02
	 * 注册授权
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/bargain")
	public BhResult bargain(HttpServletRequest request, HttpServletResponse response){
		BhResult r = null;
		try {
			String code = request.getParameter("code");
			String productId = request.getParameter("productId");
			String bargainNo = request.getParameter("bargainNo");
			String mId = request.getParameter("mId");
			if(com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(code)){
				String appid=  Contants.appId;
			    String REDIRECT_URI = Contants.BIN_HUI_URL+"/bh-product-api/getBargain";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect"
			    +"&productId="+productId+"&bargainNo="+bargainNo+"&mId="+mId;
				url  = url.replace("APPID", java.net.URLEncoder.encode(appid, "utf-8")); 
			    url  = url.replace("REDIRECT_URI", java.net.URLEncoder.encode(REDIRECT_URI, "utf-8"));
			    response.setHeader("Access-Control-Allow-Origin", "*");
			    response.sendRedirect(url);
			}else{
				getBargain(code, request, response);
			}
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		}  catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	//获取openId
	@RequestMapping("/getBargain")
    public BhResult getBargain(String code,HttpServletRequest request, HttpServletResponse response) throws Exception {
		BhResult r = null;
		try {
			String productId = request.getParameter("productId");
			String bargainNo = request.getParameter("bargainNo");
			String mId = request.getParameter("mId");
			String retUrl = Contants.BIN_HUI_URL+"/binhuiApp/helpfriendcut";
			com.alibaba.fastjson.JSONObject jsonTexts = WxLoginApi.getOpenid(code);
			String accessToken = "";
	        if (jsonTexts.get("access_token")!=null) {
	        	accessToken = jsonTexts.get("access_token").toString();
	        }
	        String openid = "";
	        if (jsonTexts.get("openid")!=null) {
	        	openid = jsonTexts.get("openid").toString();
	        }
			request.getSession(false).setAttribute("openid", openid);
			String userInfo = WxLoginApi.getUserInfo(accessToken, openid); //获取用户基本信息
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(userInfo.toString());
			String url = retUrl+"?userInfo="+userInfo+"&productId="+productId+"&bargainNo="+bargainNo+"&mId="+mId;
			response.sendRedirect(url);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
    }
}
