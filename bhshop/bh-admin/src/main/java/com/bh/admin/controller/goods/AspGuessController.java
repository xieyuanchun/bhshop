package com.bh.admin.controller.goods;


import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.bh.admin.controller.goods.AspGuessController;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.goods.AspUserGuess;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.service.AspGuessService;
import com.bh.admin.util.JedisUtil;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;



@Controller
@RequestMapping("/aspGuess")
public class AspGuessController {
	private static final Logger logger = LoggerFactory.getLogger(AspGuessController.class);
	@Autowired
	private AspGuessService aspGuessService;
	
	/**
	 * @Description: 亚运会提现记录导出
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34
	 */
	@RequestMapping(value = "/excelExport", method = RequestMethod.GET)
	public void excelExport(AspUserGuess aspUserGuess, HttpServletRequest request, HttpServletResponse response) {
		try {
			if(StringUtils.isNotBlank(aspUserGuess.getStartTime())){
				String startTime=aspUserGuess.getStartTime()+" 00:00:00.0";
				aspUserGuess.setStartTime(startTime);
			}else {
				aspUserGuess.setStartTime(null);
			}
			if(StringUtils.isNotBlank(aspUserGuess.getEndTime())){
				String endTime=aspUserGuess.getEndTime()+" 00:00:00.0";
				aspUserGuess.setEndTime(endTime);
			}else {
				aspUserGuess.setEndTime(null);
			}
			aspGuessService.excelExport(aspUserGuess, request,
					response);
		} catch (Exception e) {
			logger.error("excelExport" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @Description: 获取提现列表的信息
	 * @author fanjh
	 * @date 2018年8月24日 上午10:28:40
	 */
	@RequestMapping("/getWinUser")
	@ResponseBody
	public BhResult getWinUser(@RequestBody AspUserGuess aspUserGuess) {
		BhResult result = null;
		try {
			if(StringUtils.isNotBlank(aspUserGuess.getStartTime())){
				String startTime=aspUserGuess.getStartTime()+" 00:00:00.0";
				aspUserGuess.setStartTime(startTime);
			}else {
				aspUserGuess.setStartTime(null);
			}
			if(StringUtils.isNotBlank(aspUserGuess.getEndTime())){
				String endTime=aspUserGuess.getEndTime()+" 00:00:00.0";
				aspUserGuess.setEndTime(endTime);
			}else {
				aspUserGuess.setEndTime(null);
			}
			PageBean<AspUserGuess> msg=aspGuessService.getWinUser(aspUserGuess);
			if (msg != null) {
				result = new BhResult(200, "查询成功", msg);
			} else {
				result = BhResult.build(400, "查询失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 
	 * @Description: 获取中奖信息
	 * @author fanjh
	 * @date 2018年8月24日 上午10:28:40
	 */
	@RequestMapping("/getWin")
	@ResponseBody
	public BhResult getWin(@RequestBody AspUserGuess aspUserGuess) {
		BhResult result = null;
		try {
			/*if(StringUtils.isNotBlank(aspUserGuess.getStartTime())){
				String startTime=aspUserGuess.getStartTime()+" 00:00:00.0";
				aspUserGuess.setStartTime(startTime);
			}else {
				aspUserGuess.setStartTime(null);
			}
			if(StringUtils.isNotBlank(aspUserGuess.getEndTime())){
				String endTime=aspUserGuess.getEndTime()+" 00:00:00.0";
				aspUserGuess.setEndTime(endTime);
			}else {
				aspUserGuess.setEndTime(null);
			}*/
			PageBean<AspUserGuess> msg=aspGuessService.getWin(aspUserGuess);
			if (msg != null) {
				result = new BhResult(200, "查询成功", msg);
			} else {
				result = BhResult.build(400, "查询失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 
	 * @Description: 审核
	 * @author fanjh
	 * @date 2018年8月24日 上午10:28:40
	 */
	@RequestMapping("/changeStatus")
	@ResponseBody
	public BhResult changeStatus(@RequestBody AspUserGuess entiy, HttpServletRequest request) {
		BhResult result = null;
		try {
			 String token = request.getHeader("token");
			 JedisUtil jedisUtil= JedisUtil.getInstance();  
			 JedisUtil.Strings strings=jedisUtil.new Strings();
			 String userJson = strings.get(token);
			 Map mapOne = JSON.parseObject(userJson, Map.class);
			 Integer userId = (Integer)mapOne.get("userId");
			    if(StringUtils.isBlank(String.valueOf(userId))){
				    userId = 1;
			 }
		
		    int row=aspGuessService.updateStatus(entiy,userId);
	          if (row == 1) {
				result = new BhResult(200, "审核成功", row);
			  } else {
				result = BhResult.build(400, "审核失败！");
			  }
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
}
