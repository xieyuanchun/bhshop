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
import com.bh.admin.pojo.goods.TopicSavemoneyLog;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.TopicSaveMoneyService;
import com.bh.admin.util.JedisUtil;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/saveMoney")
public class TopicSaveMoneyController {

	/**
	 * 惠省钱
	 */
	
	@Autowired	
	private TopicSaveMoneyService saveMoneyService;
	
	/**
	 * 惠省钱活动报名操作
	 */
	@RequestMapping("/addTopicSaveMoney")
	@ResponseBody
	public BhResult addTopicSaveMoney(@RequestBody TopicGoods topicGoods){
		   BhResult  result = null;
		  
		   try{
			   int num = saveMoneyService.addTopicSaveMoney(topicGoods);
		       if(num>0){
		    	   result = new BhResult(BhResultEnum.SUCCESS, num);
		       }else{
		    	   result = new BhResult(BhResultEnum.FAIL, null);
		       }
			  
		   }catch(Exception e){
			   result = new BhResult(BhResultEnum.FAIL);
			   e.printStackTrace();
		   }
		   return result;
	}
	
	/**
	 * 惠省钱报名列表
	 */
	@RequestMapping("/listSaveMoney")
	@ResponseBody
	public BhResult listSaveMoney(@RequestBody TopicGoods topicGoods,@RequestHeader(value="token") String token){
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
			PageBean<TopicGoods> pageTopicSaveMoney = saveMoneyService.listTopicSaveMoney(topicGoods);
			if (pageTopicSaveMoney != null) {
				result = new BhResult(BhResultEnum.SUCCESS, pageTopicSaveMoney);
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
	 * 移动端惠省钱商品列表
	 */
	@RequestMapping("/apiTopicGoodsList")
	@ResponseBody
	public BhResult apiTopicGoodsList(@RequestBody TopicGoods entity,HttpServletRequest request) { 
		BhResult result = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			Map<String,Object> map= saveMoneyService.apiTopicGoodsList(entity,member);
			result = new BhResult();
			result.setStatus(BhResultEnum.SUCCESS.getStatus());
			result.setMsg(BhResultEnum.SUCCESS.getMsg());
			result.setData(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "操作异常!");
		}
		return result;
	} 
	
	/**
	 * @Description: 我的惠省钱活动列表
	 * @author 
	 * @date 
	 */
	@RequestMapping("/apiMyTopicSaveMoney")
	@ResponseBody
	public BhResult apiMyTopicSaveMoney(@RequestBody TopicGoods entity, HttpServletRequest request) { 
		BhResult  result = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			if(member==null){
				result = new BhResult();
				result.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				result.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return result;
			}
			PageBean<Map<String, Object>> t= saveMoneyService.apiMyTopicSaveMoney(entity, member.getId());
			//PageBean<Map<String, Object>> t= topicPrizeService.apiMyTopicPrize(entity,1);//TODO  先定义死
			result = new BhResult();
			result.setStatus(BhResultEnum.SUCCESS.getStatus());
			result.setMsg(BhResultEnum.SUCCESS.getMsg());
			result.setData(t);
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "操作异常!");
		}
		return result;
	}
	
	
	/**
	 * @Description: 惠省钱活动开卖
	 * @author 
	 * @date 
	 */
	@RequestMapping("/doTopicSaveMoney")
	@ResponseBody
	public BhResult doTopicSaveMoney(@RequestBody TopicSavemoneyLog entity) {
		   BhResult result = null;
		   try{
			   Integer row = saveMoneyService.doSaveMoney(entity);
			   if(row==null){
				    result = new BhResult(BhResultEnum.FAIL,"操作失败");
			   }else if(row.equals(1)){
				    result = new BhResult(BhResultEnum.SUCCESS,"团活动成功");
			   }else if(row.equals(0)){
				    result = new BhResult(BhResultEnum.FAIL,"人数不够，解散活动");
			   }
			   
		   }catch(Exception e){
			   result = new BhResult(BhResultEnum.VISIT_FAIL,null);
			   e.printStackTrace();
		   }
		   
		   return result; 
	}
	
	
}
