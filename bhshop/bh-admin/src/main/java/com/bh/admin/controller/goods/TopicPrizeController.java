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
import com.bh.admin.pojo.goods.TopicPrizeLog;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.TopicPrizeService;
import com.bh.admin.util.JedisUtil;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;


/**
 * @Description: 抽奖活动
 * @author xieyc
 * @date 2018年1月17日 下午3:34:15 
 */
@Controller
@RequestMapping("/topicPrize")
public class TopicPrizeController {
	@Autowired
	private TopicPrizeService topicPrizeService;
	
	/**
	 * @Description:抽奖活动立即报名
	 * @author xieyc
	 * @date 2018年1月17日 下午3:35:28 
	 */
	@RequestMapping("/addTopicPrize")
	@ResponseBody
	public BhResult addTopicBargain(@RequestBody TopicGoods entity ){ 
		BhResult result = null;
		try {
			
			int row=topicPrizeService.addTopicPrize(entity);
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
	 * @Description: 抽奖活动报名列表
	 * @author  xieyc
	 * @date 2018年1月17日 下午4:07:32 
	 */
	@RequestMapping("/listTopicPrize")
	@ResponseBody
	public BhResult listTopicPrize(@RequestBody TopicGoods topicGoods,@RequestHeader(value="token") String token) { 
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
			PageBean<TopicGoods> pageTopicGood = topicPrizeService.listTopicPrize(topicGoods);
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
	 * @Description: 移动端抽奖活动商品列表
	 * @author xieyc
	 * @date 2018年1月18日 下午3:00:12 
	 */
	@RequestMapping("/apiTopicGoodsList")
	@ResponseBody
	public BhResult apiTopicGoodsList(@RequestBody TopicGoods entity,HttpServletRequest request) { 
		BhResult r = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			Map<String,Object> map=topicPrizeService.apiTopicGoodsList(entity,member);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(map);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
	/**
	 * @Description: 我的抽奖活动列表
	 * @author xieyc
	 * @date 2018年1月18日 下午3:26:06 
	 */
	@RequestMapping("/apiMyTopicPrize")
	@ResponseBody
	public BhResult apiMyTopicPrize(@RequestBody TopicGoods entity, HttpServletRequest request) { 
		BhResult r = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			if(member==null){
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return r;
			}
			PageBean<Map<String, Object>> t= topicPrizeService.apiMyTopicPrize(entity, member.getId());
			//PageBean<Map<String, Object>> t= topicPrizeService.apiMyTopicPrize(entity,192);//TODO  先定义死
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(t);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
	}
	/**
	 * @Description: 开奖接口：首先判断参加人数是否和配置人数相等，不相等，将参加的人的钱原路返回，
	 * 						相等的话,才进行开奖————>抽奖活动随机取一个抽奖号码,并更改抽奖初始化状态0（ 是否中奖 :0 初始化  1 否  2 是）和将没中奖的钱退回至个人钱包				
	 * @author xieyc
	 * @date 2018年1月18日 下午5:00:17  
	 */
	@RequestMapping("/beganPrize")
	@ResponseBody
	public BhResult beganPrize(@RequestBody TopicPrizeLog entity) {
		BhResult result = null;
		try {
			Integer row=topicPrizeService.beganPrize(entity);
			if (row == null) {
				result = new BhResult(BhResultEnum.FAIL,"操作失败");
			} else if(row==0) {
				result = new BhResult(BhResultEnum.FAIL,"参加人数不够，金额原路退回");
			} else if(row==1){
				result = new BhResult(BhResultEnum.SUCCESS,"开奖，没中奖的金额退至个人钱包");
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL,null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
}
