package com.order.user.controller;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.order.user.service.UtilService;
@Controller
@RequestMapping("/util")
public class UtilController {
	@Autowired
	private UtilService utilService;
	@RequestMapping(value = "/getWxJsSign")
	@ResponseBody
	public Map<String, String> getWxJsSign(String url, HttpServletRequest request,
			HttpServletResponse response) {
		return utilService.getWxSign(url);
		// return WxJsApiUtil.sign(url);
	}
	
	
}
