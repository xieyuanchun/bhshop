package com.bh.admin.controller.goods;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.admin.pojo.goods.GoodsShareLog;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.goods.GoodsShareLog;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.GoodsShareLogService;
import com.bh.admin.util.JedisUtil;
import com.bh.admin.util.JedisUtil.Strings;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/goodsShareLog")
public class GoodsShareLogController {
	@Autowired
	private GoodsShareLogService service;
	
	/**
	 * SCJ-20171211-01
	 * 插入分享日志
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/insertLog")
	@ResponseBody
	public BhResult insertLog(@RequestBody Map<String, String> map, HttpServletRequest request) {
	   BhResult result = null;
	   try {
		    String rMId = map.get("rMId");
		    String reMId = map.get("reMId");
		    String shareUrl = map.get("shareUrl");
		    String orderNo = map.get("orderNo");
		    String teamNo = map.get("teamNo");
		    String skuId = map.get("skuId");
		    String shareType = map.get("shareType");
		    String orderType = map.get("orderType");
		    String openId = map.get("openId");
		    
		    Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
			if(member!=null){
				int row = service.insertLog(member.getId(), rMId, reMId, shareUrl, orderNo, teamNo, skuId, shareType, orderType, openId);
			    if (row == 1) {
			    	result = new BhResult(BhResultEnum.SUCCESS, null);
				} else {
					result = new BhResult(BhResultEnum.FAIL, null);
				}
			}else{
				result = new BhResult(BhResultEnum.LOGIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171211-02
	 * 后台分享订单日志管理
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/pageList")
	@ResponseBody
	public BhResult pageList(@RequestBody Map<String, String> map, HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   String token = request.getHeader("token");
           JedisUtil jedisUtil = JedisUtil.getInstance();
           JedisUtil.Strings strings = jedisUtil.new Strings();
           String userJson = strings.get(token);
           Map mapOne = JSON.parseObject(userJson, Map.class);
           Object sId = mapOne.get("shopId");
           Integer shopId = 1;
           if (sId != null) {
               shopId = (Integer) sId;
           }
		    String orderNo = map.get("orderNo");//'订单号',
		    String goodsName = map.get("goodsName"); //商品名称
		    String orderType = map.get("orderType"); //'单类型 1团单  2普通订单  3其它',
		    String currentPage = map.get("currentPage"); //起始页
		    if(StringUtils.isEmptyOrWhitespaceOnly(currentPage)){
		    	currentPage = "1";
		    }
			PageBean<GoodsShareLog> page = service.pageList(orderNo, goodsName, orderType, Contants.PAGE_SIZE, currentPage,shopId);
		    if (page != null) {
		    	result = new BhResult(BhResultEnum.SUCCESS, page);
			} else {
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
}
