package com.bh.utils;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.bh.config.Contants;
import com.bh.config.SwiftpassConfig;
import com.bh.utils.pay.HttpService;
import com.bh.utils.pay.WXPayConstants;

import java.util.Map;
import java.util.HashMap;
import java.util.Formatter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;  
public class WxJsApiUtil {
	public static void main(String[] args) {

        
        // 注意 URL 一定要动态获取，不能 hardcode
        String url = "http://www.baidu.com";
        Map<String, String> ret = sign(url);
    };
    public static synchronized String getAccessToken(String appId,String appSecret){
    	String baseUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    	StringBuffer sb = new StringBuffer();
    	sb.append(baseUrl);
    	sb.append("&appid="+appId);
    	sb.append("&secret="+appSecret);
    	try {
    		String ret = HttpService.doGet(sb.toString());
    		Map map = JSON.parseObject(ret, Map.class);
    		String accessToken = (String)map.get("access_token");
    		return accessToken;
		} catch (Exception e) {
			return null;
		}
    	
    }
    public static synchronized String getTicket(){ //（更换公共号后，accessToken获取不到） ,需设置白名单 ，设置安全域名
    	String accessToken = getAccessToken(Contants.appId, Contants.appSecret);
    	 System.out.println("accessToken-->"+accessToken);
    	String baseUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    	StringBuffer sb = new StringBuffer();
    	sb.append(baseUrl);
    	sb.append("?access_token="+accessToken);
    	sb.append("&type=jsapi");
    	try {
    		String ret = HttpService.doGet(sb.toString());
    		System.out.println("tiket ret--->"+ret);
    		Map map = JSON.parseObject(ret, Map.class);
    		String ticket = null;
    		if((Integer)map.get("errcode")==0){
    			ticket= (String)map.get("ticket");
    		}else{
    			System.out.println("errmsg-->"+(String)map.get("errmsg"));
    		}
    		return ticket;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    //https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
    
    public static Map<String, String> sign(String url) {
    	String jsapi_ticket = getTicket();
    	System.out.println("jsapi_ticket-->"+jsapi_ticket);
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", Contants.appId);

        return ret;
    }

    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    
    
}
