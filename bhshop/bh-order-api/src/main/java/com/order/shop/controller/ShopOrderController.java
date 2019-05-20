package com.order.shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderSku;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;
import com.order.shop.service.ShopOrderService;
import com.order.util.JedisUtil;
import com.order.util.JedisUtil.Strings;

@Controller
@RequestMapping("/shopOrder")
public class ShopOrderController {
	@Autowired
	private ShopOrderService service;
	
	@Value(value = "${pageSize}")
	private String pageSize;
	
	/**
	 * Test
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectById")
	@ResponseBody
	public BhResult selectById(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String id = map.get("id");
			Order order = service.getShopOrderById(id);
			   if (order !=null) {
				   result = new BhResult(200, "操作成功", order);
				} else {
				   result = BhResult.build(400, "暂无数据！");
				}
			} catch (Exception e) {
				result = BhResult.build(500, "数据库搜索失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20170913-01
	 * 商家后台订单列表
	 * @return
	 */
	@RequestMapping("/orderList")
	@ResponseBody
	public BhResult orderList(@RequestBody Map<String, String> map, HttpSession session) {
	   BhResult result = null;
	   try {
		   String order_no = map.get("order_no"); //查询条件
		   String startTime = map.get("startTime");//查询条件、起始时间
		   String endTime = map.get("endTime");//查询条件、起始时间
		   String status = map.get("status"); //订单状态-1待付款，2待发货，3已发货，4已收货,5待评价,6已取消,7已评价、8已删除',9退款单
		   String currentPage = map.get("currentPage");//当前第几页
		   if(StringUtils.isEmptyOrWhitespaceOnly(currentPage)){
			   currentPage = "1";
		   }
		    String id = "1";
		    PageBean<Order> page = service.orderList(id, order_no, status, currentPage, pageSize, startTime, endTime);
			   if (page !=null) {
				   result = new BhResult(BhResultEnum.SUCCESS, page);
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
	 * SCJ-20171019-01
	 * 平台后台全部订单列表
	 * @return
	 */
	@RequestMapping("/pageAll")
	@ResponseBody
	public BhResult pageAll(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   String order_no = map.get("order_no"); //查询条件
		   String startTime = map.get("startTime");//查询条件、起始时间
		   String endTime = map.get("endTime");//查询条件、起始时间
		   String currentPage = map.get("currentPage");//当前第几页
		   String status = map.get("status"); //订单状态-1待付款，2已付款，3完成	
		   if(StringUtils.isEmptyOrWhitespaceOnly(currentPage)){
			   currentPage = "1";
		   }
		    PageBean<Order> page = service.pageAll(order_no, status, currentPage, pageSize, startTime, endTime);
		    if (page !=null) {
				   result = new BhResult(BhResultEnum.SUCCESS, page);
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
	 * SCJ-20170913-02
	 * 商家后台接单操作
	 * @return
	 */
	@RequestMapping("/orderReceiving")
	@ResponseBody
	public BhResult orderReceiving(@RequestBody Map<String, String> map, HttpSession session) {
	   BhResult result = null;
	   try {
		    String id = map.get("orderId");
		    String adminId = "1";
			int row = service.orderReceiving(id, adminId);
			   if (row == 0) {
				   result = new BhResult(400, "操作失败", null);
				} else {
				   result = BhResult.build(200, "接单成功",null);
				}
			} catch (Exception e) {
				result = BhResult.build(500, "数据库搜索失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20170913-03
	 * 商家后台针对整个订单抛单
	 * @return
	 */
	@RequestMapping("/castSheet")
	@ResponseBody
	public BhResult castSheet(@RequestBody Map<String, String> map, HttpSession session,HttpServletRequest request) {
	   BhResult result = null;
	   try {
		    String id = map.get("orderId");
		    String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		   
		    String deliveryPrice = map.get("deliveryPrice"); //物流价
			Order row = service.castSheet(id, userJson, deliveryPrice);
			   if (row == null) {
				   result = new BhResult(BhResultEnum.FAIL, null);
				} else {
					result = new BhResult(BhResultEnum.SUCCESS, row);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20171027-01
	 * 商家后台针对订单商品抛单
	 * @return
	 */
	@RequestMapping("/castSheetOne")
	@ResponseBody
	public BhResult castSheetOne(@RequestBody Map<String, String> map, HttpSession session) {
	   BhResult result = null;
	   try {
		    String id = map.get("id"); //订单商品id
		    String adminId = "1";
			Order row = service.castSheetOne(id);
			   if (row == null) {
				   result = new BhResult(BhResultEnum.FAIL, null);
				} else {
					result = new BhResult(BhResultEnum.SUCCESS, row);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20170913-04
	 * 订单商品列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/getOrderGoodsList")
	@ResponseBody
	public BhResult getOrderGoodsList(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String orderId = map.get("orderId");
		    String currentPage = map.get("currentPage");
		    int adminId = 1;
		    PageBean<OrderSku> list = service.getOrderGoodsList(adminId, orderId, currentPage, pageSize);
			   if (list != null) {
				   result = new BhResult(200, "操作成功", list);
				} else {
				   result = BhResult.build(400, "操作失败",null);
				}
			} catch (Exception e) {
				result = BhResult.build(500, "数据库搜索失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20171011-08
	 * 商家后台订单详情
	 * @param map
	 * @return
	 */
	@RequestMapping("/getOrderGoodsDetails")
	@ResponseBody
	public BhResult getOrderGoodsDetails(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String id = map.get("id"); //订单id
		    String adminId = "1";
		    String fz = map.get("fz"); //1商家后台，2平台后台
		    Order order = service.getOrderGoodsDetails(id, adminId, fz);
			   if (order != null) {
				   result = new BhResult(200, "操作成功", order);
				} else {
				   result = BhResult.build(400, "操作失败",null);
				}
			} catch (Exception e) {
				result = BhResult.build(500, "数据库搜索失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	
	/**
	 * SCJ-20170914-02
	 * 退款订单商品列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/getRefundGoodsList")
	@ResponseBody
	public BhResult getRefundGoodsList(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String orderId = map.get("orderId");
		    String currentPage = map.get("currentPage");
		    PageBean<OrderRefundDoc> list = service.getRefundGoodsList(orderId, currentPage, pageSize) ;
			   if (list != null) {
				   result = new BhResult(200, "操作成功", list);
				} else {
				   result = BhResult.build(400, "操作失败",null);
				}
			} catch (Exception e) {
				result = BhResult.build(500, "数据库搜索失败!");
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	
	/**
	 * SCJ-20170914-01
	 * 订单批量删除
	 * @param id
	 * @return
	 */
	@RequestMapping("/batchDelete")
	@ResponseBody
	public BhResult batchDelete(@RequestBody Map<String, String> map, HttpSession session) {
		BhResult result = null;
		   try {
			   String id = map.get("id");
			   //int userId = session.getAttribute("userId");
			   List<Order> row = service.batchDelete(id);
				   if (row == null) {
					   result = BhResult.build(400, "请求失败！", row);
					} else {
					   result = BhResult.build(200, "删除成功", row);
					}
				} catch (Exception e) {
					result = BhResult.build(500, "数据库删除失败!");
					LoggerUtil.getLogger().error(e.getMessage());
				}
		return result;
	}
	
	/**
	 * SCJ-20170914-03
	 * 商家取消订单
	 * @param id
	 * @return
	 */
	@RequestMapping("/cancelOrder")
	@ResponseBody
	public BhResult cancelOrder(@RequestBody Map<String, String> map, HttpSession session) {
		BhResult result = null;
		   try {
			   String id = map.get("id");
			   String adminId = "1";;
			   Order row = service.cancelOrder(id ,adminId);
				   if (row == null) {
					   result = BhResult.build(400, "请求失败！", row);
					} else {
					   result = BhResult.build(200, "取消成功", row);
					}
				} catch (Exception e) {
					result = BhResult.build(500, "数据库取消失败!");
					LoggerUtil.getLogger().error(e.getMessage());
				}
		return result;
	}
	
	/**
	 * SCJ-20170914-05
	 * 订单批量抛单
	 * @param id
	 * @return
	 */
	@RequestMapping("/batchCastSheet")
	@ResponseBody
	public BhResult batchCastSheet(@RequestBody Map<String, String> map) {
		BhResult result = null;
		   try {
			   String id = map.get("id");
			   List<Order> row = service.batchCastSheet(id);
				   if (row == null) {
					   result = BhResult.build(400, "请求失败！", row);
					} else {
					   result = BhResult.build(200, "抛单成功", row);
					}
				} catch (Exception e) {
					result = BhResult.build(500, "数据库抛单失败!");
					LoggerUtil.getLogger().error(e.getMessage());
				}
		return result;
	}
	
	/**
	 * SCJ-20170915-03
	 * 商家审核退款
	 * @param map
	 * @return
	 */
	@RequestMapping("/auditRefund")
	@ResponseBody
	public BhResult auditRefund(@RequestBody Map<String, String> map) {
		BhResult result = null;
		   try {
			   String id = map.get("orderId");
			   String adminId = "1";
			   String status = map.get("status");//status:1退款失败，2退款成功 
			   Order row = service.auditRefund(id, status, adminId);
				   if (row == null) {
					   result = new BhResult(BhResultEnum.FAIL, null);
					} else {
						result = new BhResult(BhResultEnum.SUCCESS, row);
					}
				} catch (Exception e) {
					result = new BhResult(BhResultEnum.VISIT_FAIL, null);
					LoggerUtil.getLogger().error(e.getMessage());
				}
		return result;
	}
	
	/**
	 * SCJ-20170919-01
	 * 配送端订单列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/deliveryList")
	@ResponseBody
	public BhResult deliveryList(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String orderNo = map.get("orderNo");//查询条件订单号
			   String currentPage = map.get("currentPage");//当前第几页
			   String fz = map.get("fz");//1-待配送，2-配送中，3待结算，4已完成
			   if(StringUtils.isEmptyOrWhitespaceOnly(currentPage)){
				   currentPage = "1";
			   }
			   int mId = 1; //获取当前登录配送员id
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   mId = member.getId();
			   }

			   PageBean<Order> page = service.deliveryWaitList(orderNo, fz, currentPage, pageSize, mId);
				   if (page!=null) {
					   result = BhResult.build(200, "请求成功！", page);
					} else {
					   result = BhResult.build(400, "请求失败！", null);
					}
				} catch (Exception e) {
					result = BhResult.build(500, "数据库访问失败!");
					LoggerUtil.getLogger().error(e.getMessage());
				}
		return result;
	}
	
	/**
	 * SCJ-20170919-02
	 * 配送端抢单操作
	 * @param id
	 * @return
	 */
	@RequestMapping("/robOrder")
	@ResponseBody
	public BhResult robOrder(@RequestBody Map<String, String> map) {
		BhResult result = null;
		   try {
			   String id = map.get("orderId");
			   String sId = "1"; //配送员id
			   Order row = service.robOrder(id, sId);
				   if (row == null) {
					   result = BhResult.build(400, "请求失败！", row);
					} else {
					   result = BhResult.build(200, "抢单成功", row);
					}
				} catch (Exception e) {
					result = BhResult.build(500, "数据库抢单失败!");
					LoggerUtil.getLogger().error(e.getMessage());
				}
		return result;
	}
	
	
	/**
	 * SCJ-20170919-03
	 * 配送端商品已送达
	 * @param id
	 * @return
	 */
	@RequestMapping("/alreadyDelivery")
	@ResponseBody
	public BhResult alreadyDelivery(@RequestBody Map<String, String> map) {
		BhResult result = null;
		   try {
			   String id = map.get("orderId");
			   Order row = service.alreadyDelivery(id);
				   if (row == null) {
					   result = BhResult.build(400, "请求失败！", row);
					} else {
					   result = BhResult.build(200, "操作成功", row);
					}
				} catch (Exception e) {
					result = BhResult.build(500, "数据库操作失败!");
					LoggerUtil.getLogger().error(e.getMessage());
				}
		return result;
	}
	
	/**
	 * SCJ-20171101-05
	 * 后台结算模块订单统计
	 * @param id
	 * @return
	 */
	@RequestMapping("/OrderAccount")
	@ResponseBody
	public BhResult OrderAccount() {
		BhResult result = null;
		   try {
			   int adminId = 1;
			   Map<Integer, Object> row = service.OrderAccount(adminId);
				   if (row == null) {
					   result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
					} else {
						result = new BhResult(BhResultEnum.SUCCESS, row);
					}
				} catch (Exception e) {
					result = new BhResult(BhResultEnum.VISIT_FAIL, null);
					LoggerUtil.getLogger().error(e.getMessage());
				}
		return result;
	}
	
	/**
	 * SCJ-20171101-06
	 * 后台订单&商品模块订单统计
	 * @param id
	 * @return
	 */
	@RequestMapping("/OrderWaitAccount")
	@ResponseBody
	public BhResult OrderWaitAccount() {
		BhResult result = null;
		   try {
			   int adminId = 1;
			   Map<Integer, Object> row = service.OrderWaitAccount(adminId);
			   if (row == null) {
				   result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
				} else {
					result = new BhResult(BhResultEnum.SUCCESS, row);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20171103-01
	 * 后台退款模块订单统计
	 * @param id
	 * @return
	 */
	@RequestMapping("/OrderRefundAccount")
	@ResponseBody
	public BhResult OrderRefundAccount() {
		BhResult result = null;
		   try {
			   int adminId = 1;
			   Map<Integer, Object> row = service.OrderRefundAccount(adminId);
			   if (row == null) {
				   result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
				} else {
					result = new BhResult(BhResultEnum.SUCCESS, row);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	
	/**
	 * SCJ-20171106-01
	 * 后台交易金额统计
	 * @param id
	 * @return
	 */
	@RequestMapping("/OrderMoneyAccount")
	@ResponseBody
	public BhResult OrderMoneyAccount() {
		BhResult result = null;
		   try {
			   int adminId = 1;
			   Map<String, String> row = service.OrderMoneyAccount(adminId);
			   if (row == null) {
				   result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
				} else {
					result = new BhResult(BhResultEnum.SUCCESS, row);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20171106-02
	 * 后台订单数量统计管理
	 * @param id
	 * @return
	 */
	@RequestMapping("/OrderNumAccount")
	@ResponseBody
	public BhResult OrderNumAccount() {
		BhResult result = null;
		   try {
			   int adminId = 1;
			   Map<String, Object> row = service.OrderNumAccount(adminId);
			   if (row == null) {
				   result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
				} else {
					result = new BhResult(BhResultEnum.SUCCESS, row);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20171107-01
	 * 平台后台订单数量统计
	 * @param id
	 * @return
	 */
	@RequestMapping("/backgroundOrderAccount")
	@ResponseBody
	public BhResult backgroundOrderAccount() {
		BhResult result = null;
		   try {
			   Map<String, Object> row = service.backgroundOrderAccount();
			   if (row == null) {
				   result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
				} else {
					result = new BhResult(BhResultEnum.SUCCESS, row);
				}
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
}
