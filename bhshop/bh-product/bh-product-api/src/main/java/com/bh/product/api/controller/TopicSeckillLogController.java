package com.bh.product.api.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.TopicSeckillLog;
import com.bh.product.api.service.TopicSeckillLogService;
import com.bh.product.api.util.JedisUtil;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

@RestController
@RequestMapping("/seckillLog")
public class TopicSeckillLogController {
	@Autowired
	private TopicSeckillLogService service;
	/**
	 * @Description: 秒杀活动新增日志
	 * @author xieyc
	 * @date 2018年1月18日 上午9:31:21 
	 */
	@ResponseBody
	@RequestMapping("/add")
	public BhResult add(@RequestBody TopicSeckillLog entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			Integer row = service.add(entity);
			r = new BhResult();
			if (row == 1) {
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
			} else {
				r = new BhResult(BhResultEnum.FAIL,"操作失败！");
			} 
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
	}
	/**
	 * @Description: 秒杀日志管理列表
	 * @author xieyc
	 * @date 2018年1月18日 上午9:46:36 
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody TopicSeckillLog entity,@RequestHeader(value="token") String token) {
		BhResult r = null;
		try {
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapUser = JSON.parseObject(userJson, Map.class);
			Integer shopId =  (Integer) mapUser.get("shopId");
			if (shopId!=null) {
				entity.setShopId(shopId);
			}
			
			PageBean<TopicSeckillLog> page =service.listPage(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
	}
}
