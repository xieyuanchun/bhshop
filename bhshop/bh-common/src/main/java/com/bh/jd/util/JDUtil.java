package com.bh.jd.util;
import java.net.URLEncoder;
import java.util.Date;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bh.jd.bean.JdResult;
import com.bh.jd.config.JDConfig;
import com.bh.jd.enums.UtilEnum;
import com.bh.utils.DateUtil;
import com.bh.utils.HttpUtils;
import com.bh.utils.MD5Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class JDUtil {
	public static String getAccessToken() {

		String grantType = "access_token";
		String clientId = JDConfig.CLIENT_ID;
		String username = "";
		try {
			username = URLEncoder.encode(JDConfig.USER_NAME, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			username = "";
			// TODO: handle exception
		}
		String password = MD5Util.getMD5Str(JDConfig.PASSWORD);
		String timestamp = DateUtil.format(new Date(), DateUtil.DATE_TIME_PATTERN);
		String sign = getSign(timestamp, grantType);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("grant_type", grantType);
		jsonObject.put("client_id", clientId);
		jsonObject.put("username", username);
		jsonObject.put("password", password);
		jsonObject.put("timestamp", timestamp);
		jsonObject.put("sign", sign);
		String	retJsonStr = HttpUtils.doPost(UtilEnum.ACCESS_TOKEN.getMethod(), jsonObject);
		JdResult<JSONObject> jdResult = JSON.parseObject(retJsonStr, JdResult.class);
		return jdResult.getResult().getString("access_token");
		
	}
	private static String getSign(String timestamp, String grantType) {
		String sign = JDConfig.CLIENT_SECRET + timestamp + JDConfig.CLIENT_ID + JDConfig.USER_NAME
				+ MD5Util.getMD5Str(JDConfig.PASSWORD) + grantType + JDConfig.CLIENT_SECRET;
		sign = MD5Util.getMD5Str(sign).toUpperCase();
		return sign;
	}

	public static void main(String args[])throws Exception{

	
	}
	
	public static Gson getGson(){
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson;
	}
	
}
