package com.order.shop.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.enums.OrderStatusEnum;
import com.bh.goods.pojo.Topic;
import com.bh.goods.pojo.TopicDauctionLog;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderTeam;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;
import com.order.shop.service.OrderTeamService;
import com.order.util.JedisUtil;
import com.order.util.JedisUtil.Strings;

@Controller
@RequestMapping("/orderTeam")
public class OrderTeamController {
	@Autowired
	private OrderTeamService service;
	
	/**
	 * SCJ-20171207-03
	 * 后台拼团订单管理列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult pageList(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String currentPage = map.get("currentPage");
			   String teamNo = map.get("teamNo");
			   String status = map.get("status");
			   String orderNo = map.get("orderNo");
			   String pageSize = map.get("pageSize"); //每页显示多少条
			   if(StringUtils.isEmptyOrWhitespaceOnly(currentPage)){
				   currentPage = "1";
			   }
			   if(StringUtils.isEmptyOrWhitespaceOnly(pageSize)){
				   pageSize = Contants.PAGE_SIZE+"";
			   }
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
			   PageBean<OrderTeam> page = service.pageList(pageSize, currentPage, teamNo, status, shopId, orderNo);
			   if (page!=null) {
				   result = new BhResult(BhResultEnum.SUCCESS, page);
				} else {
					result = new BhResult(BhResultEnum.GAIN_FAIL, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20180206-03
	 * 后台拼团订单管理列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/pageList")
	@ResponseBody
	public BhResult listPage(@RequestBody OrderTeam entity, HttpServletRequest request) {
		BhResult r = null;
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
			entity.setShopId(shopId);
			PageBean<OrderTeam> page = service.listPage(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	/**
	 * SCJ-20171207-04
	 * 后台拼团订单详情
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/getDetails")
	@ResponseBody
	public BhResult getDetails(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String id = map.get("id");
			   OrderTeam obj = service.getDetails(id);
			   if (obj!=null) {
				   result = new BhResult(BhResultEnum.SUCCESS, obj);
				} else {
					result = new BhResult(BhResultEnum.GAIN_FAIL, null);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20171206-03
	 * 移动端商品详情获取团购人数和前十条列表
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/getGroupNumAndList")
	@ResponseBody
	public BhResult getGroupNumAndList(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String goodsId = map.get("goodsId");
			   if (org.apache.commons.lang.StringUtils.isEmpty(goodsId)) {
					return result = BhResult.build(400, "参数不能为空");
				}
			   //2018.6.1 zlk
			   HttpSession session = request.getSession(false);
			   Member member = new Member();
			   if(session!=null) {
			       member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			   }
			   //end
			   Map<String, Object> mapList = service.getGroupNumAndList(goodsId, member);
			   if (mapList!=null) {
				   result = new BhResult(BhResultEnum.SUCCESS, mapList);
				} else {
					result = new BhResult(BhResultEnum.GAIN_FAIL, null);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * test
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/testInsert")
	@ResponseBody
	public BhResult testInsert(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String teamNo = map.get("teamNo");
			   int row = service.testInsert(teamNo);
			   if (row == 1) {
				   result = new BhResult(BhResultEnum.SUCCESS, row);
				} else {
					result = new BhResult(BhResultEnum.GAIN_FAIL, null);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20171212-01
	 * 团购单的插入
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/insertGroupOrder")
	@ResponseBody
	public BhResult insertGroupOrder(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String orderNo = map.get("orderNo");
			   String teamNo = map.get("teamNo");
			   int row = service.insertGroupOrder(orderNo, teamNo);
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
	 * SCJ-20171219-02
	 * 参团预览获取用户头像
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/getGroupUserHead")
	@ResponseBody
	public BhResult getGroupUserHead(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String teamNo = map.get("teamNo");
			   int row = service.testInsert(teamNo);
			   if (row == 1) {
				   result = new BhResult(BhResultEnum.SUCCESS, row);
				} else {
					result = new BhResult(BhResultEnum.GAIN_FAIL, null);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20171221-02
	 * 判断当前用户是否在团中
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/isInGroup")
	@ResponseBody
	public BhResult isInGroup(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String teamNo = map.get("teamNo");
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			   boolean flag = service.isInGroup(member, teamNo);
			   result = new BhResult(BhResultEnum.SUCCESS, flag);
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * 拍卖日志插入（测试用）
	 * @param entity
	 * @return
	 */
	@RequestMapping("/insertDauctionLog")
	@ResponseBody
	public BhResult insertDauctionLog(@RequestBody TopicDauctionLog entity){
		BhResult r = null;
		try {
			service.insertDauctionLog(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	
	/**
	 * 拍卖日志插入（测试用）
	 * @param entity
	 * @return
	 */
	@RequestMapping("/xyc")
	@ResponseBody
	public BhResult xyc(@RequestBody Map<String, String> map){
		BhResult r = null;
		try {
			service.updateAuctionStatus(map.get("orderNo"));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
}
