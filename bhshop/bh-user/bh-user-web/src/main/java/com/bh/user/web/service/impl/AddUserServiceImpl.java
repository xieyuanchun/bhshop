package com.bh.user.web.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.bh.result.BhResult;
import com.bh.user.web.service.AddUserService;
import com.bh.utils.HttpClientUtil;
import com.bh.utils.LoggerUtil;

@Service
public class AddUserServiceImpl implements AddUserService {

	@Value(value = "${REST_BH_URL}")
	private String REST_BH_URL;
	//
	@Value(value = "${REST_USER_URL}")
	private String REST_USER_URL;
	
	@Override
	public BhResult adduserid(Map<String, String> map) {
		BhResult result = null;

		try {
			String httpstr = REST_BH_URL + REST_USER_URL;
			String json = HttpClientUtil.doPostJson(httpstr, JSONUtils.toJSONString(map));
			result = BhResult.formatToPojo(json, null);
		} catch (Exception e) {
			LoggerUtil.getLogger().error(e.getMessage());
			return null;
		}
		return result;
	}

}
