package com.order.user.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.order.user.service.UserSubmitOrderService;



@Controller
public class UserSubmitOrderController {
	private static final Logger logger = LoggerFactory.getLogger(UserSubmitOrderController.class);
	
	@Autowired
	private UserSubmitOrderService userSubmitOrderService;
	
	@RequestMapping(value = "/userSubmitOrderServiceTest")
	@ResponseBody
	public BhResult aliAppAay(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
		BhResult bhResult = null;
		try {
			Map<String, Object> myMap=userSubmitOrderService.rendOrder(map, member, request);
			bhResult=new BhResult(200, "操作成功", myMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######userSubmitOrderServiceTest#######" + e);
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}
	
	
}
