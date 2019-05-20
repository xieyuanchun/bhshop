package com.bh.user.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bh.bean.Sms;
import com.bh.result.BhResult;
import com.bh.user.pojo.*;
import com.bh.utils.SmsUtil;

@Controller
@RequestMapping("/SMS")
public class SMSController {
	private static final Logger logger = LoggerFactory.getLogger(SMSController.class);
	@Value("${SmsContent}")
	private String SmsContent;
	@Value("${SmsContentTime}")
	private String SmsContentTime; // 过期时间，默认是10分钟

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	@ResponseBody
	public BhResult send(String mobile, HttpServletRequest request, HttpSession session) {
		try {
			Sms sms = new Sms();
			int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
			sms.setPhoneNo(mobile);
			sms.setSmsContent(Integer.toString(mobile_code));

			VilidatePhone vilidatePhone = new VilidatePhone();
			vilidatePhone.setPhone(mobile);
			vilidatePhone.setCode(mobile_code);
			// 将对象存进去
			request.getSession(true).setAttribute(SmsContent, vilidatePhone);
			request.getSession(true).setMaxInactiveInterval(Integer.parseInt(SmsContentTime) * 60);// 60s就是1分钟
			BhResult bhres = SmsUtil.aliPushUserReg(sms);
			if (bhres.getStatus().equals(-1)) {
				bhres.setMsg("请勿频繁发送短信");
			}
			return bhres;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######send#######" + e);
			return BhResult.build(500, "请勿频繁发送短信");
		}
		
	}

	// 2018.6.20 zlk
	@RequestMapping(value = "/sendSmallApp", method = RequestMethod.GET)
	@ResponseBody
	public BhResult sendSmallApp(String mobile, HttpServletRequest request, HttpSession session) {
		try {
			BhResult r = null;
			Sms sms = new Sms();
			int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
			sms.setPhoneNo(mobile);
			sms.setSmsContent(Integer.toString(mobile_code));

			BhResult bhres = SmsUtil.aliPushUserReg(sms);
			if (bhres.getStatus().equals(-1)) {
				bhres.setMsg("请勿频繁发送短信");
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("mobile", mobile);
			map.put("mobile_code", new Sha256Hash(Integer.toString(mobile_code), "YKD1vVuPJEbrJdRyNeEr").toHex());
			r = new BhResult(200, "短信发送成功", map);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######sendSmallApp#######" + e);
			return BhResult.build(500, "请勿频繁发送短信");
		}
	}

}
