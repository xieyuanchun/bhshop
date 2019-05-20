package com.bh.user.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.result.BhResult;
import com.bh.user.pojo.Address;
import com.bh.user.pojo.DayWeather;
import com.bh.user.pojo.Weather;
import com.bh.utils.AddressUtils;
import com.bh.utils.HttpUtils;
import com.bh.utils.IPUtils;
@Controller
public class WeatherController {
	private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
	@RequestMapping(value = "/weather", method = RequestMethod.POST)
	@ResponseBody
	public BhResult selectWeather(HttpServletRequest request) {
		BhResult bhResult = null;
		DayWeather dayWeather = new DayWeather();
		try {
			String city = "";
			String IP = IPUtils.getIpAddr(request);
			String address = AddressUtils.returnAddressByIp(IP,"utf-8");
			
			if (StringUtils.isNotEmpty(address)) {
				String ad = AddressUtils.decodeUnicode(address);
				Address address2 = JSON.parseObject(ad,Address.class);
				if (StringUtils.isNotEmpty(address2.getCity())) {
					city = address2.getCity();
				}else if (StringUtils.isNotEmpty(address2.getProvince())) {
					city = address2.getProvince();
				}else if (StringUtils.isNotEmpty(address2.getCountry())) {
					city = address2.getCountry();
				}
				
				// 拼地址
				String path = String.format("/open/api/weather/json.shtml?city=%s", city);

				String host = "https://www.sojson.com";
				String appcode = "232d013ef8244587a9a4f69cb2fcca47";
				Map<String, String> headers = new HashMap<String, String>();
				// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE
				// 83359fd73fe94948385f570e3c139105
				headers.put("Authorization", "APPCODE " + appcode);
				// 根据API的要求，定义相对应的Content-Type
				headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				Map<String, String> querys = new HashMap<String, String>();

				HttpResponse response = HttpUtils.doGet(host, path, headers, querys);
				String json = EntityUtils.toString(response.getEntity());
				Weather w = JSON.parseObject(json, Weather.class);
				if (w.getMessage().equals("Success !")) {
					dayWeather = w.getData().getForecast().get(0);
				}
				bhResult = new BhResult(200, "查询成功", dayWeather);
			}else{
				bhResult = new BhResult(400, "查询失败，请检查网络问题", dayWeather);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######weather#######" + e);
			bhResult = BhResult.build(500, "数据库搜索失败!");
		}
		return bhResult;

	}
}