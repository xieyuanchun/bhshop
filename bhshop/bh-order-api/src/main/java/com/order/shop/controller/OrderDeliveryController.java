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

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;
import com.order.annotaion.OrderLogAnno;
import com.order.shop.service.OrderDeliveryService;
import com.order.shop.service.OrderMainService;
import com.order.shop.service.ShopOrderService;
//
@Controller
@RequestMapping("/orderDelivery")
public class OrderDeliveryController {
	@Autowired
	private OrderDeliveryService service;
	
	@Value(value = "${pageSize}")
	private String pageSize;
	
	/**
	 * SCJ-20171116-01
	 * 配送端可接单
	 * @param map
	 * @return
	 */
	@RequestMapping("/deliveryOrder")
	@ResponseBody
	public BhResult deliveryOrder(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String currentPage = map.get("currentPage");//当前第几页
			   String lat = map.get("lat"); //配送员当前经度
			   String lng = map.get("lng"); //纬度
			   if(StringUtils.isEmptyOrWhitespaceOnly(currentPage)){
				   currentPage = "1";
			   }
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   int mId = member.getId();
				   PageBean<OrderShop> page = service.deliveryOrder(currentPage, pageSize, lat, lng, mId);
				   if (page!=null) {
					   result = new BhResult(BhResultEnum.SUCCESS, page);
					} else {
						result = new BhResult(BhResultEnum.FAIL, null);
					}
			   }else{
				   //result = new BhResult(BhResultEnum.LOGIN_FAIL, null);
				   PageBean<OrderShop> page = service.deliveryOrder(currentPage, pageSize, lat, lng, 14765);
				   if (page!=null) {
					   result = new BhResult(BhResultEnum.SUCCESS, page);
					} else {
						result = new BhResult(BhResultEnum.FAIL, null);
					}
			   }
			} catch (Exception e) {
				e.printStackTrace();
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20171116-02
	 * 接单操作
	 * @param map
	 * @return
	 */
	@RequestMapping("/receiveOrder")
	@ResponseBody
	public BhResult receiveOrder(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String id = map.get("id"); //商家订单id
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				  int mId = member.getId();
				  int row = service.receiveOrder(id, mId);
			      if (row == 1) {
				     result = new BhResult(BhResultEnum.SUCCESS, null);
				  }else if(row == 300){
					  result = new BhResult(BhResultEnum.ILLEGALITY_FAIL, null);
				  }else if(row == 600){
					  result = new BhResult(BhResultEnum.ORDER_FAIL, null);
				  }else {
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
	 * SCJ-20171116-03
	 * 确认发货
	 * @param map
	 * @return
	 */
	@RequestMapping("/sendGoods")
	@ResponseBody
	public BhResult sendGoods(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String id = map.get("id"); //配送单id
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   int row = service.sendGoods(id, member.getId());
				   if (row == 1) {
					   result = new BhResult(BhResultEnum.SUCCESS, null);
					} else if(row == 300){
						result = new BhResult(BhResultEnum.ILLEGALITY_FAIL, null);
					}else {
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
	 * SCJ-20171120-02
	 * 取消配送
	 * @param map
	 * @return
	 */
	@RequestMapping("/cancelOrder")
	@ResponseBody
	public BhResult cancelOrder(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String id = map.get("id"); //配送单id
			   String reason = map.get("reason"); //取消原因
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   int row = service.cancelOrder(id, reason, member.getId());
				   if (row == 1) {
					   result = new BhResult(BhResultEnum.SUCCESS, null);
					}else if(row == 300){
						result = new BhResult(BhResultEnum.ILLEGALITY_FAIL, null);
					}else {
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
	 * SCJ-20171120-01
	 * 配送任务列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/orderTask")
	@ResponseBody
	public BhResult orderTask(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String currentPage = map.get("currentPage");//当前第几页
			   String status = map.get("status"); //配送状态0(初始状态)待发货   1发货中   2已签收，3已取消'
			   String lat = map.get("lat"); //配送员当前经度
			   String lng = map.get("lng"); //纬度
			   if(StringUtils.isEmptyOrWhitespaceOnly(currentPage)){
				   currentPage = "1";
			   }
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   int mId = member.getId();
				   PageBean<OrderSendInfo> page = service.orderTask(currentPage, pageSize, status, mId, lat, lng);
				   if (page!=null) {
					   result = new BhResult(BhResultEnum.SUCCESS, page);
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
	 * SCJ-20171120-03
	 * 加载取消原因列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/getReasonList")
	@ResponseBody
	public BhResult getReasonList() {
		BhResult result = null;
		   try {
			   List<Map<String, Object>> list = service.getReasonList();
				   if (list.size()>0) {
					   result = new BhResult(BhResultEnum.SUCCESS, list);
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
	 * SCJ-20171121-01
	 * 交通工具列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/getVehicleList")
	@ResponseBody
	public BhResult getVehicleList(HttpServletRequest request) {
		BhResult result = null;
		   try {
			   int mId = 73; //获取当前登录配送员id
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   mId = member.getId();
			   }
			   List<Map<String, Object>> list = service.getVehicleList(mId);
				   if (list.size()>0) {
					   result = new BhResult(BhResultEnum.SUCCESS, list);
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
	 * SCJ-20171121-02
	 * 订单类型列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/sendOrderType")
	@ResponseBody
	public BhResult sendOrderType(HttpServletRequest request) {
		BhResult result = null;
		   try {
			   int mId = 14765; //获取当前登录配送员id
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   mId = member.getId();
			   }
			   List<Map<String, Object>> list = service.sendOrderType(mId);
				   if (list.size()>0) {
					   result = new BhResult(BhResultEnum.SUCCESS, list);
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
	 * SCJ-20171122-01
	 * 设置交通工具的保存
	 * @param map
	 * @return
	 */
	@RequestMapping("/saveVehicle")
	@ResponseBody
	public BhResult saveVehicle(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String code = map.get("code"); //交通工具：1自行车，2三轮车，3电动车，4大踏板电动车，5摩托车，6小汽车，7小货车
			   int mId = 14765; //获取当前登录配送员id
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   mId = member.getId();
			   }
			   int row = service.saveVehicle(code, mId);
				   if (row == 1) {
					   result = new BhResult(BhResultEnum.SUCCESS, null);
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
	 * SCJ-20171122-02
	 * 设置订单类型的保存
	 * @param map
	 * @return
	 */
	@RequestMapping("/saveType")
	@ResponseBody
	public BhResult saveType(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String code = map.get("code"); //接单类型：1全部，2近距离，3远距离，4高价，5低价
			   int mId = 14765; //获取当前登录配送员id
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   mId = member.getId();
			   }
			   int row = service.saveType(code, mId);
				   if (row == 1) {
					   result = new BhResult(BhResultEnum.SUCCESS, null);
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
	 * SCJ-20171123-01
	 * 订单已送达
	 * @param map
	 * @return
	 */
	@RequestMapping("/orderAreadlySend")
	@ResponseBody
	public BhResult orderAreadlySend(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String id = map.get("id"); //配送单id
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   int row = service.orderAreadlySend(id, member.getId());
				   if (row == 1){
					   result = new BhResult(BhResultEnum.SUCCESS, null);
				   }else if(row == 300){
					   result = new BhResult(BhResultEnum.ILLEGALITY_FAIL, null);
				   }else {
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
	 * SCJ-20171124-01
	 * 接单数量和总金额统计
	 * @param map
	 * @return
	 */
	@RequestMapping("/sendAccount")
	@ResponseBody
	public BhResult sendAccount(HttpServletRequest request) {
		BhResult result = null;
		   try {
			   int mId = 14765; //获取当前登录配送员id
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   mId = member.getId();
			   }
			   Map<String, Object> row = service.sendAccount(mId);
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
	 * SCJ-20171127-01
	 * 配送端订单详情
	 * @param map
	 * @return
	 */
	@RequestMapping("/deliveryOrderDetails")
	@ResponseBody
	public BhResult deliveryOrderDetails(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String id = map.get("id"); //商家订单id
			   String lat = map.get("lat"); //配送员当前经度
			   String lng = map.get("lng"); //纬度
			   String fz = map.get("fz"); //8步行，9驾车
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   int mId = member.getId();
				   OrderShop order = service.deliveryOrderDetails(id, lat, lng, mId, fz);
				   if (order!=null) {
					   result = new BhResult(BhResultEnum.SUCCESS, order);
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
	 * SCJ-20171127-02
	 * 是否配送员验证
	 * @param map
	 * @return
	 */
	@RequestMapping("/isDeliveryman")
	@ResponseBody
	public BhResult isDeliveryman(HttpServletRequest request) {
		BhResult result = null;
		   try {
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   int mId = member.getId();
				   boolean flag = service.isDeliveryman(mId);
				   result = new BhResult(BhResultEnum.SUCCESS, flag);
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
	 * SCJ-20171129-01
	 * 接单排行
	 * @param map
	 * @return
	 */
	@RequestMapping("/orderArrange")
	@ResponseBody
	public BhResult orderArrange(HttpServletRequest request) {
		BhResult result = null;
		   try {
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   int mId = member.getId();
				   List<Member> list = service.orderArrange(mId);
				   if(list!=null){
					   result = new BhResult(BhResultEnum.SUCCESS, list);
				   }else{
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
	 * SCJ-20171201-02
	 * 我的钱包
	 * @param map
	 * @return
	 */
	@RequestMapping("/mySendWallet")
	@ResponseBody
	public BhResult mySendWallet(HttpServletRequest request) {
		BhResult result = null;
		   try {
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   Map<String, Object> map= service.mySendWallet(member.getId());
				   if (map != null) {
					   result = new BhResult(BhResultEnum.SUCCESS, map);
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
	 * SCJ-20171201-03
	 * 收工
	 * @param map
	 * @return
	 */
	@RequestMapping("/knockOff")
	@ResponseBody
	public BhResult knockOff(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String fz = map.get("fz"); //1开工,2收工
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   int row = service.knockOff(member.getId(), fz);
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
	 * SCJ-20171201-04
	 * 评价记录
	 * @param map
	 * @return
	 */
	@RequestMapping("/evaluateRecord")
	@ResponseBody
	public BhResult evaluateRecord(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String fz = map.get("fz"); //1本月，2上个月
			   Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			   if(member!=null){
				   Map<String, Object> row= service.evaluateRecord(fz, member.getId());
				   if (row !=null) {
					   result = new BhResult(BhResultEnum.SUCCESS, row);
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
}
