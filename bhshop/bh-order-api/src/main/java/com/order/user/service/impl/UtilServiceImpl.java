package com.order.user.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.utils.WxJsApiUtil;
import com.order.user.service.UtilService;
import com.order.util.JedisUtil;
@Service
public class UtilServiceImpl implements UtilService {

	@Override
	public String getTicket() {
		// TODO Auto-generated method stub
		// 110分钟
		int sec = 6600;
		String key = Contants.getRedisKey("jsapi_ticket");
		JedisUtil jedisUtil= JedisUtil.getInstance();  
		JedisUtil.Strings strings=jedisUtil.new Strings();
		String ticket = strings.get(key);
		if(StringUtils.isBlank(ticket)){
			ticket = WxJsApiUtil.getTicket();
			if(StringUtils.isNotEmpty(ticket)) {
                strings.setEx(key, sec, ticket);
            }
		}
        System.out.println("#############redis ticket----->"+ticket);
		return ticket;
	}

	@Override
	public Map<String, String> getWxSign(String url) {
		// TODO Auto-generated method stub
		String jsapiTicket = getTicket();
		//String url = Contants.BIN_HUI_URL+"/binhuiApp/home";
		
		System.out.println("jsapi_ticket-->"+jsapiTicket);
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = WxJsApiUtil.create_nonce_str();
        String timestamp = WxJsApiUtil.create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapiTicket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = WxJsApiUtil.byteToHex(crypt.digest());
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
        ret.put("jsapi_ticket", jsapiTicket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", Contants.appId);

        return ret;
		// return WxJsApiUtil.sign(url);
	}

}
