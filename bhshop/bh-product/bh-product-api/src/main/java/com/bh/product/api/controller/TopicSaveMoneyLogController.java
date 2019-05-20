package com.bh.product.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.mapper.TopicSavemoneyLogMapper;
import com.bh.goods.pojo.TopicPrizeLog;
import com.bh.goods.pojo.TopicSavemoneyLog;
import com.bh.product.api.service.TopicSaveMoneyLogService;
import com.bh.product.api.util.JedisUtil;
import com.bh.product.api.util.JedisUtil.Strings;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/saveMoneyLog")
public class TopicSaveMoneyLogController {

	
	@Autowired
	private TopicSaveMoneyLogService topicSaveMoneyLogService;
	
	@Autowired
	private TopicSavemoneyLogMapper topicSavemoneyLogMapper;
	/**
	 * @Description: 惠省钱活动新增日志
	 * @author 
	 * @date 
	 */
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody TopicSavemoneyLog entity, HttpServletRequest request) {
		BhResult result = null;
		try {
			/*Member member=new Member();
			member.setId(35);//TODO 先定义死 
*/			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			if(member==null){
				result = new BhResult();
				result.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				result.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return result;
			}
			Integer row = topicSaveMoneyLogService.add(entity, member);
			result = new BhResult();
			if (row == null) {
				result.setStatus(BhResultEnum.SAVEMONEY.getStatus());
				result.setMsg(BhResultEnum.SAVEMONEY.getMsg());
			} else if(row==1) {
				result.setStatus(BhResultEnum.SUCCESS.getStatus());
				result.setMsg(BhResultEnum.SUCCESS.getMsg());
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "操作异常!");
		}
		return result;
	}
	
	/**
	 * @Description: 惠省钱活动新增日志的权限提醒
	 * @author 
	 * @date 
	 */
	@RequestMapping("/addControl")
	@ResponseBody
	public BhResult addControl(@RequestBody TopicSavemoneyLog entity, HttpServletRequest request) {
		//1.传团号(my_no)、tgid(topicGood 表的id)、活动编号(邀请码act_no),
		//2.获取当前的团号并验证邀请码是否正确 ，如果不正确就提示不能参加活动，如果正确,就验证是否参加过活动，没参加过就提示可以参加，
		//有参加过，提示已经参加过。不能再参加。
		//
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			if(member==null){
				result = new BhResult();
				result.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				result.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return result;
			}
			if(entity.getMyNo()==null){
				result = new BhResult(200, "可以开团!",null);
			}else{
				  //判断邀请码是否正确
				 TopicSavemoneyLog topicSavemoneyLog = new TopicSavemoneyLog();
				 topicSavemoneyLog.setmId(member.getId());
				 topicSavemoneyLog.setMyNo(entity.getMyNo());
			     List<TopicSavemoneyLog> list = topicSavemoneyLogMapper.getByMyNoAndMid(topicSavemoneyLog);
			     
			     if(list.size()>0){//判断有没有参加过活动
			    	 result = BhResult.build(400, "已经参加过活动，不能再参加!");
			     }else{
			    	 TopicSavemoneyLog topicSavemoneyLog2 = new TopicSavemoneyLog();
			    	 topicSavemoneyLog2.setMyNo(entity.getMyNo());
			    	 List<TopicSavemoneyLog> list2 = topicSavemoneyLogMapper.getByMyNoAndMid(topicSavemoneyLog2);
			    	 if(list2.get(0).getActNo().equals(entity.getActNo())){//判断邀请码
			    		 result = new BhResult(200, "没有参加过活动,邀请码正确,可以参团!",null);
			    	 }else{
			    		 result = BhResult.build(400, "邀请码不正确!");
			    	 }
			    	 
			     }
		    }
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "操作异常!");
		}
		  return result;
	}
	
	
	
	
	/**
	 * @Description: 惠省钱日志管理列表
	 * @author 
	 * @date 
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody TopicSavemoneyLog entity ,@RequestHeader(value="token") String token) {
		BhResult result = null;
		try {
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapUser = JSON.parseObject(userJson, Map.class);
			Integer shopId =  (Integer) mapUser.get("shopId");
			if (shopId!=null) {
				entity.setShopId(shopId);
			}
			PageBean<TopicSavemoneyLog> page = topicSaveMoneyLogService.listPage(entity);
			result = new BhResult();
			result.setStatus(BhResultEnum.SUCCESS.getStatus());
			result.setMsg(BhResultEnum.SUCCESS.getMsg());
			result.setData(page);
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "操作异常!");
		}
		return result;
	}
	
}
