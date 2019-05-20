package com.order.shop.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.config.Contants;
import com.bh.order.pojo.OrderLog;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;
import com.order.shop.service.OrderLogService;
import com.order.util.JedisUtil;
import com.order.util.JedisUtil.Strings;

@Controller
@RequestMapping("/orderLog")
public class OrderLogController {
	@Autowired
	private OrderLogService service;
	
	@Value(value = "${pageSize}")
	private String pageSize;
	
	/**
	 * SCJ-20171016-03
	 * 后台获取日志分页列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/getAllList")
	@ResponseBody
	public BhResult getAllList(@RequestBody Map<String, String> map, HttpServletRequest request){
		BhResult result = null;
		try {
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Object sId = mapOne.get("shopId");
		    Integer shopId = 0;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
			String currentPage = map.get("currentPage");
			String orderNo = map.get("orderNo");
			String action = map.get("action");
			String pageSize = map.get("pageSize"); //每页显示多少条
			if(StringUtils.isEmptyOrWhitespaceOnly(currentPage)){
				   currentPage = "1";
			   }
		   if(StringUtils.isEmptyOrWhitespaceOnly(pageSize)){
			   pageSize = Contants.PAGE_SIZE+"";
		   }
			PageBean<OrderLog> page = service.PageAll(orderNo, action, currentPage, pageSize, shopId);
			if(page!=null){
				result = new BhResult(200, "查询成功", page);
			}else{
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库访问失败");
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}
