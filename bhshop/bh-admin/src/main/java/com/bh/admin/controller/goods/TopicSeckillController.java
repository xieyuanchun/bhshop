package com.bh.admin.controller.goods;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.bh.admin.pojo.goods.TopicGoods;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.TopicSeckillService;
import com.bh.admin.util.JedisUtil;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
/**
 * @Description: 秒杀活动
 * @author xxj
 * @date 2018年1月17日 下午3:34:15 
 */
@Controller
@RequestMapping("/topicSeckill")
public class TopicSeckillController {
	@Autowired
	private TopicSeckillService topicSeckillService;
	
	/**
	 * @Description:秒杀活动立即报名
	 * @author xxj
	 * @date 2018年1月17日 下午3:35:28 
	 */
	@ResponseBody
	@RequestMapping("/addTopicSeckill")
	public BhResult addTopicSeckill(@RequestBody TopicGoods topicGoods){ 
		BhResult result = null;
		try {
			int row=topicSeckillService.addTopicSeckill(topicGoods);
			if (row == 1) {
				result = new BhResult(BhResultEnum.SUCCESS, row);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	/**
	 * @Description: 秒杀活动报名列表
	 * @author  xxj
	 * @date 2018年1月17日 下午4:07:32 
	 */
	@RequestMapping("/listTopicSeckill")
	@ResponseBody
	public BhResult listTopicSeckill(@RequestBody TopicGoods topicGoods,@RequestHeader(value="token") String token) { 
		BhResult result = null;
		try {
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapUser = JSON.parseObject(userJson, Map.class);
			Integer shopId =  (Integer) mapUser.get("shopId");
			if (shopId!=null) {
				topicGoods.setShopId(shopId);
			}
			PageBean<TopicGoods> pageTopicGood = topicSeckillService.listTopicSeckill(topicGoods);
			if (pageTopicGood != null) {
				result = new BhResult(BhResultEnum.SUCCESS, pageTopicGood);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;

	}
	/**
	 * @Description: 移动端今日秒杀列表
	 * @author  xieyc
	 * @date 2018年1月17日 下午4:07:32 
	 */
	@RequestMapping("/todayListTopicSeckill")
	@ResponseBody
	public BhResult todayListTopicSeckill(@RequestBody TopicGoods topicGoods,HttpServletRequest request) { 
		BhResult r = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			Map<String,Object> map = topicSeckillService.todayListTopicSeckill(topicGoods,member);
			if (map != null) {
				r = new BhResult(BhResultEnum.SUCCESS, map);
			} else {
				r = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			r= new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return r;
	}
	
	/**
	 * @Description: 移动端明日秒杀列表
	 * @author  xieyc
	 * @date 2018年1月17日 下午4:07:32 
	 */
	@RequestMapping("/tomorrowListTopicSeckill")
	@ResponseBody
	public BhResult tomorrowListTopicSeckill(@RequestBody TopicGoods topicGoods,HttpServletRequest request) { 
		BhResult r = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			Map<String,Object> map = topicSeckillService.tomorrowListTopicSeckill(topicGoods,member);
			if (map != null) {
				r = new BhResult(BhResultEnum.SUCCESS, map);
			} else {
				r = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			r= new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return r;

	}
	
	/**
	 * @Description: 移动端后日秒杀列表
	 * @author  xieyc
	 * @date 2018年1月17日 下午4:07:32 
	 */
	@RequestMapping("/houdayListTopicSeckill")
	@ResponseBody
	public BhResult houdayListTopicSeckill(@RequestBody TopicGoods topicGoods,HttpServletRequest request) { 
		BhResult r = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			Map<String,Object> map = topicSeckillService.houdayListTopicSeckill(topicGoods,member);
			if (map != null) {
				r = new BhResult(BhResultEnum.SUCCESS,map);
			} else {
				r = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			r= new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return r;

	}

	
}
