package com.bh.wxpayjsapi.service;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bh.config.Contants;
import com.bh.utils.HttpClientConnectionManager;
import com.bh.utils.pay.WXPayConstants;
import com.bh.wxpayjsapi.common.HttpService;
import com.bh.wxpayjsapi.common.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author scj
 *
 */
public class WxLoginApi {

    private static Log logger = LogFactory.getLog(WxLoginApi.class); 
    private static final CloseableHttpClient httpclient;  
    public static final String CHARSET = "UTF-8";  
    static{  
        RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(3000).build();  
        httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();  
    }
    //获取openId
    public static JSONObject getOpenid(String code) throws Exception {
    	String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=AppId&secret=AppSecret&code=CODE&grant_type=authorization_code";
        url = url.replace("AppId", Contants.appId).replace("AppSecret", Contants.appSecret).replace("CODE", code);
        HttpGet get = HttpClientConnectionManager.getGetMethod(url);
        HttpResponse res = httpclient.execute(get);
        String jsonStr = EntityUtils.toString(res.getEntity(), "utf-8");
        JSONObject jsonTexts = (JSONObject) JSON.parse(jsonStr);
        return jsonTexts;
    }
    
  //获取access_token
    public static String getAccessToken(String code) throws Exception {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=AppId&secret=AppSecret&code=CODE&grant_type=authorization_code";
        url = url.replace("AppId", Contants.appId).replace("AppSecret", Contants.appSecret).replace("CODE", code);
        /*String res = HttpService.doGet(url);
        Map<String, Object> resultMap = JsonUtil.fromJson(res, HashMap.class);
        if (resultMap.get("access_token") == null) {
            return null;
        }*/
        HttpGet get = HttpClientConnectionManager.getGetMethod(url);
        HttpResponse res = httpclient.execute(get);
        String jsonStr = EntityUtils.toString(res.getEntity(), "utf-8");
        JSONObject jsonTexts = (JSONObject) JSON.parse(jsonStr);
        String accessToken = "";
        if (jsonTexts.get("access_token")!=null) {
        	accessToken = jsonTexts.get("access_token").toString();
        }
        return accessToken;
    }
    
    //获取授权登录用户基本信息
    public static String getUserInfo(String accessToken, String openId) throws Exception {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCSESS_TOKEN&openid=OPENID&lang=zh_CN";
        url  = url.replace("ACCSESS_TOKEN", java.net.URLEncoder.encode(accessToken, "utf-8"));
        url  = url.replace("OPENID", java.net.URLEncoder.encode(openId, "utf-8"));
        HttpGet get = HttpClientConnectionManager.getGetMethod(url);
        HttpResponse res = httpclient.execute(get);
        String jsonStr = EntityUtils.toString(res.getEntity(), "utf-8");
        JSONObject jsonTexts = (JSONObject) JSON.parse(jsonStr);
        /*String nickname = "";
        if (jsonTexts.get("nickname")!=null) {
        	nickname = jsonTexts.get("nickname").toString();
        }
        String headimgurl = "";
        if (jsonTexts.get("headimgurl")!=null) {
        	headimgurl = jsonTexts.get("headimgurl").toString();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("openId", openId);
		map.put("nickname", nickname);
		map.put("headimgurl", headimgurl);*/
        return jsonTexts.toString();
    }
}
