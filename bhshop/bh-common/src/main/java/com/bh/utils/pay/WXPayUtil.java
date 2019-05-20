package com.bh.utils.pay;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;
import java.security.MessageDigest;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WXPayUtil {


    /**
     * 获取当前时间戳，单位秒
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis()/1000;
    }

    /**
     * 获取当前时间戳，单位毫秒
     * @return
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    /**
     * 生成 uuid， 即用来标识一笔单，也用做 nonce_str
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }
    public static String getOpenid(String appid, String appSecret, String code) throws Exception {
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + appid
                + "&secret="
                + appSecret
                + "&code=" + code + "&grant_type=authorization_code";
        String res = HttpService.doGet(requestUrl);
        Map<String, Object> resultMap = JsonUtil.fromJson(res, HashMap.class);
        if (resultMap.get("openid") == null) {
            return null;
        }

        return resultMap.get("openid").toString();
    }
    
    public static String getSmallAppOpenid(String appid, String appSecret, String code) throws Exception {
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session?appid="
                + appid
                + "&secret="
                + appSecret
                + "&js_code=" + code + "&grant_type=authorization_code";
        String res = HttpService.doGet(requestUrl);
        Map<String, Object> resultMap = JsonUtil.fromJson(res, HashMap.class);
        if (resultMap.get("openid") == null) {
            return null;
        }

        return resultMap.get("openid").toString();
    }

    //2.通过code换取网页授权access_token、openid
    public static Map<String, Object> getAccessToken(String appid, String appSecret, String code) throws Exception {
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + appid
                + "&secret="
                + appSecret
                + "&code=" 
                + code + "&grant_type=authorization_code";
        String res = HttpService.doGet(requestUrl);
        Map<String, Object> resultMap = JsonUtil.fromJson(res, HashMap.class);
        if (resultMap == null) {
            return null;
        }

        return resultMap;
    }
    
    //4.拉取用户信息(需scope为 snsapi_userinfo)
    public static Map<String, Object> getUser(String accessToken, String openId, String lang) throws Exception {
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + accessToken
                + "&openid="
                + openId
                + "&lang=" 
                + lang ;
        String res = HttpService.doGet(requestUrl);
       
        Map<String, Object> resultMap = JsonUtil.fromJson(res, HashMap.class);
        if (resultMap== null) {
            return null;
        }

        return resultMap;
    }
    
    //5.小程序 通过code换取网页授权session_key、openid
    public static Map<String, Object> getOpenId(String appid, String appSecret, String code) throws Exception {
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session?appid="
                + appid
                + "&secret="
                + appSecret
                + "&js_code=" 
                + code + "&grant_type=authorization_code";
        String res = HttpService.doGet(requestUrl);
        Map<String, Object> resultMap = JsonUtil.fromJson(res, HashMap.class);
        if (resultMap == null) {
            return null;
        }

        return resultMap;
    }
    
    //6.小程序 通过code换取网页授权unionid
    public static Map<String, Object> getUnionid(String appid, String appSecret, String code) throws Exception {
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + appid
                + "&secret="
                + appSecret
                + "&code=" 
                + code + "&grant_type=authorization_code";
        String res = HttpService.doGet(requestUrl);
        Map<String, Object> resultMap = JsonUtil.fromJson(res, HashMap.class);
        if (resultMap == null) {
            return null;
        }

        return resultMap;
    }
    
    
    //获取access_token
    public static Map<String, Object> getAccess_Token(String appid, String appSecret) throws Exception {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + appid
                + "&secret="
                + appSecret;
        String res = HttpService.doGet(requestUrl);
        Map<String, Object> resultMap = JsonUtil.fromJson(res, HashMap.class);
        if (resultMap == null) {
            return null;
        }

        return resultMap;
    }
}
