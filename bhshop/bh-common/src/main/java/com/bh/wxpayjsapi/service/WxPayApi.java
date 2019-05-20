package com.bh.wxpayjsapi.service;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.bh.wxpayjsapi.common.Configure;
import com.bh.wxpayjsapi.common.HttpService;
import com.bh.wxpayjsapi.common.JsonUtil;
import com.bh.wxpayjsapi.common.XMLParser;
import com.bh.wxpayjsapi.protocol.UnifiedOrderReqData;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xxj
 *
 */
public class WxPayApi {

    private static Log logger = LogFactory.getLog(WxPayApi.class);

    public static Map<String, Object> unifiedOrder(UnifiedOrderReqData reqData) throws IOException, SAXException, ParserConfigurationException {
        String res = HttpService.doPost(Configure.UNIFIED_ORDER_API, reqData);
        logger.debug("UnifiedOrder get response:" + res);
        return XMLParser.getMapFromXML(res);
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

    public static void main(String[] args) throws Exception {
    	String openid = getOpenid(Configure.getAppid(),Configure.getAppSecret(),"23a6a9cf524478f239dc48ebefdcfb72");
        UnifiedOrderReqData reqData = new UnifiedOrderReqData.UnifiedOrderReqDataBuilder("wxd3441d36a44400de", "1456013002",
                "微信支付测试", "1217752501201407033233368018", 1, "123.12.12.123", "http://www.baidu.com", "JSAPI").setOpenid("openid").build();
        System.out.println(unifiedOrder(reqData));


    }
}
