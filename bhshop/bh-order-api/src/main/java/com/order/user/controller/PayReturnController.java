package com.order.user.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.internal.util.AlipaySignature;
import com.bh.config.AlipayConfig;
import com.bh.enums.OrderStatusEnum;
import com.bh.result.BhResult;

@Controller
@RequestMapping("/payReturn")
public class PayReturnController {
	@RequestMapping(value = "/alipayReturn")
	public String alipayReturn(HttpServletRequest request) {
		   System.out.println("#######alipayReturn#######");
			try {
				    Map<String,String> params = new HashMap<String,String>();
					Map<String,String[]> requestParams = request.getParameterMap();
					for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
						String name = (String) iter.next();
						String[] values = (String[]) requestParams.get(name);
						String valueStr = "";
						for (int i = 0; i < values.length; i++) {
							valueStr = (i == values.length - 1) ? valueStr + values[i]
									: valueStr + values[i] + ",";
						}
						params.put(name, valueStr);
					}
				 
					boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipayPublicKey, AlipayConfig.charset, AlipayConfig.signType); 
					if(signVerified) {
						System.out.println("######alipayReturn success#######");
						return "success";
					}else {//验证失败
						System.out.println("######alipayReturn fail#######");
						return "fail";
						
						
					}
					
			} catch (Exception e) {
				System.out.println("######alipayReturn error#######");
				return "error";
			}

			   
		   }
}
