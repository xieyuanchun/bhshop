package com.order.shop.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONArray;
//import org.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.JdOrderTrackMapper;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.JdOrderTrack;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.order.OrderStock;
import com.bh.jd.bean.order.OtherStock;
import com.bh.jd.bean.order.OtherTrack;
import com.bh.jd.bean.order.Track;
import com.bh.order.mapper.OrderRefundDocMapper;
import com.bh.order.mapper.OrderRefundDocStepMapper;
import com.bh.order.mapper.OrderSendInfoMapper;
import com.bh.order.pojo.MyOrderSkuPojo;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderRefundDocStep;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.result.BhResult;
import com.bh.user.pojo.MBusEntity;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberNotice;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.EmojiFilter;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;
import com.order.annotaion.OrderLogAnno;
import com.order.express.vo.Express;
import com.order.shop.service.OrderMainService;
import com.order.user.controller.JDOrderController;
import com.order.user.service.JDOrderService;
import com.order.util.JedisUtil;
import com.order.util.JedisUtil.Strings;
import com.print.controller.HttpUtils;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.CainiaoCloudprintMystdtemplatesGetRequest;
import com.taobao.api.response.CainiaoCloudprintMystdtemplatesGetResponse;

@Controller
@RequestMapping("/orderMain")
public class OrderMainController {
	@Autowired
	private OrderMainService service;
	@Autowired
	private JDOrderService jdOrderService;
	@Autowired
	private OrderSendInfoMapper orderSendInfoMapper;
	@Autowired
    private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private JdOrderTrackMapper jdOrderTrackMapper;
	@Autowired
	private OrderRefundDocMapper refundMapper; //退款单
	@Value(value = "${pageSize}")
	private String pageSize;
	@Autowired
	private OrderRefundDocStepMapper orderRefundDocStepMapper;
	//电子面单 api 的获取用户个人模板的url
    //正式环境：http://gw.api.taobao.com/router/rest
	//沙箱环境：http://gw.api.tbsandbox.com/router/rest
    public static final String url ="http://gw.api.taobao.com/router/rest";
		
	//TOP 发的唯一标识,用于商家登入 菜鸟云打印  凭证,appkey,相当于账号名，沙箱：1023426785
    public static final String appkey = "23426785";
		
    //TOP 发的密钥，用于商家登入 菜鸟云打印  验证，沙箱：sandbox0dbba26a9c4115459c9e104fc
    public static final String secret = "8822a821d64ceae9a6828781eab2e5f1";
		
    //SessionKey是用户身份的标识，应用获取到了SessionKey即意味着应用取得了用户的授权，可以替用户向TOP请求用户
    public static final String sessionKey = "6201c07c169e9a4ZZ9f428d93f24f8954bda971932a30412911781131";
		
	
	
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
	public BhResult orderList(@RequestBody Map<String, String> map, HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   String order_no = map.get("order_no"); //查询条件
		   String startTime = map.get("startTime");//查询条件、起始时间
		   String endTime = map.get("endTime");//查询条件、起始时间
		   String status = map.get("status"); //订单状态-1待付款，2待发货，3已发货，4已收货,5待评价,6已取消,7已评价、8已删除',9退款单
		   String currentPage = map.get("currentPage");//当前第几页
		   String pageSize = map.get("pageSize"); //每页显示多少条
		   String isJd = map.get("isJd"); //京东订单id
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
		    Integer shopId = 1;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
		    PageBean<OrderShop> page = service.orderList(shopId, order_no, status, currentPage, pageSize, startTime, endTime, isJd);
			   if (page !=null) {
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
	
	
	/**
	 * SCJ-20180124-01
	 * 商家后台订单列表
	 * @return
	 */
	@RequestMapping("/backGroundOrderList")
	@ResponseBody
	public BhResult backGroundOrderList(@RequestBody OrderShop entity, HttpServletRequest request) {
	   BhResult r = null;
	   try {
		    String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map map = JSON.parseObject(userJson, Map.class);
		    Object sId = map.get("shopId");
		    Integer shopId = 0;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
		    entity.setShopId(shopId);
		    PageBean<OrderShop> page = service.backGroundOrderList(entity);
		    r = new BhResult();
		    r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		    return r;
	   }catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
	}
	
	
	/**
	 * xxj
	 * 京东订单列表
	 * @return
	 */
	@RequestMapping("/bgJdOrderList")
	@ResponseBody
	public BhResult bgJdOrderList(@RequestBody OrderShop entity, HttpServletRequest request) {
	   BhResult r = null;
	   try {
		    String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map map = JSON.parseObject(userJson, Map.class);
		    Object sId = map.get("shopId");
		    Integer shopId = 1;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
		    entity.setShopId(shopId);
		    PageBean<OrderShop> page = service.bgJdOrderList(entity);
		    r = new BhResult();
		    r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		    return r;
	   }catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
	}
	
	/**
	 * xxj
	 * 重新下京东订单
	 * @return
	 */
	@RequestMapping("/reSubmitOrder")
	@ResponseBody
	public BhResult reSubmitOrder(@RequestBody OrderShop entity, HttpServletRequest request) {
	   BhResult r = null;
	   try {
		    Order order = service.selectByPrimaryKey(entity.getOrderId());
		    jdOrderService.updateJDOrderId(order);
		    r = new BhResult();
		    r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
		    return r;
	   }catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
	}
	/**
	 * SCJ-20180125-01
	 * 平台后台全部订单列表
	 * @return
	 */
	@RequestMapping("/backGroundAllOrderList")
	@ResponseBody
	public BhResult backGroundAllOrderList(@RequestBody Order entity) {
	   BhResult r = null;
	   try {
		    PageBean<Order> page = service.backGroundAllOrderList(entity);
		    r = new BhResult();
		    r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		    return r;
	   }catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
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
		   String pageSize = map.get("pageSize"); //每页显示多少条
		   if(StringUtils.isEmptyOrWhitespaceOnly(currentPage)){
			   currentPage = "1";
		   }
		   if(StringUtils.isEmptyOrWhitespaceOnly(pageSize)){
			   pageSize = Contants.PAGE_SIZE+"";
		   }
		    PageBean<Order> page = service.pageAll(order_no, status, currentPage, pageSize, startTime, endTime);
		    if (page !=null) {
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
	
	
	/**
	 * SCJ-20170913-03
	 * 商家后台针对整个订单抛单
	 * @return
	 */
	@RequestMapping("/castSheet")
	@ResponseBody
	//@OrderLogAnno("抛单")
	public BhResult castSheet(@RequestBody Map<String, String> map, HttpSession session,HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   //从redis中取当前操作者的信息
		    String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		
		   
		    String id = map.get("orderId");
		    String deliveryPrice = map.get("deliveryPrice"); //物流价
		    OrderShop row = service.castSheet(id, deliveryPrice,userJson);
		        
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
	 * SCJ-20171011-08
	 * 商家后台订单详情    PC端的订单物流信息
	 * @param map
	 * @return
	 */
	@RequestMapping("/getOrderGoodsDetails")
	@ResponseBody
	public BhResult getOrderGoodsDetails(@RequestBody Map<String, String> map) {
	       BhResult result = null;
	   try{
		   TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		   CainiaoCloudprintMystdtemplatesGetRequest req = new CainiaoCloudprintMystdtemplatesGetRequest();
		   CainiaoCloudprintMystdtemplatesGetResponse rsp = client.execute(req, sessionKey);   
		   String id = map.get("id"); //商家订单id
		       
		   OrderShop order = service.getOrderGoodsDetails(id);
		   order.setTaobaoTemplate(rsp.getBody());
		   if (rsp.getBody() != null) {
			   result = new BhResult(BhResultEnum.SUCCESS, order);
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
	 * SCJ-20171011-09
	 * 商家后台退款订单详情
	 * @param map
	 * @return
	 */
	@RequestMapping("/getOrderRefundDetails")
	@ResponseBody
	public BhResult getOrderRefundDetails(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String id = map.get("id");
		    OrderShop order = service.getOrderRefundDetails(id);
			   if (order != null) {
				   result = new BhResult(BhResultEnum.SUCCESS, order);
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
	 * SCJ-20171110-03
	 * 平台后台订单详情 
	 * @param map
	 * @return
	 */
	@RequestMapping("/getOrderDetails")
	@ResponseBody
	public BhResult getOrderDetails(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		    String id = map.get("id"); //订单id
		    Order order = service.getOrderDetails(id);
			   if (order != null) {
				   result = new BhResult(BhResultEnum.SUCCESS, order);
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
	 * SCJ-20170914-03
	 * 商家取消订单
	 * @param id
	 * @return
	 */
	@RequestMapping("/cancelOrder")
	@ResponseBody
	@OrderLogAnno("商家取消订单")
	public BhResult cancelOrder(@RequestBody Map<String, String> map, HttpSession session) {
		BhResult result = null;
		   try {
			   String id = map.get("id");
			   OrderShop row = service.cancelOrder(id);
				   if (row == null) {
					   result = BhResult.build(400, "取消失败！", row);
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
					   result = BhResult.build(400, "抛单失败！", row);
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
	 * zlk 2018.4.17
	 * 商家审核退货
	 */
	@RequestMapping("/returnGoods")
	@ResponseBody
	public BhResult returnGoods(@RequestBody Map<String, String> map){
		 BhResult result = null;
		 try{
		 
		    //0审核退货成功，把状态改为申请退款。6拒绝退货
		     OrderRefundDoc doc = new OrderRefundDoc();
		     doc.setId(Integer.valueOf(map.get("id")));
		     doc.setStatus(Integer.valueOf(map.get("status")));
		     refundMapper.updateByPrimaryKeySelective(doc);
		     result = new BhResult(200,"审核成功",null);
		 }catch(Exception e){
			 e.printStackTrace();
			 return result = new BhResult(400,"审核失败",null);
		 }
		return result;
		
	}
	
	/**
	 * zlk 2018.4.17
	 * 消费者按照平台给出的退货资料录入快递信息
	 */
	@RequestMapping("/addExpress")
	@ResponseBody
	public BhResult addExpress(@RequestBody Map<String, String> map){
		
		BhResult result = null;
		try{
			OrderRefundDoc o = refundMapper.getByOrderSkuId(Integer.valueOf(map.get("id")));
		    OrderRefundDoc doc = new OrderRefundDoc();
		    doc.setId(o.getId());
		    doc.setReturnAddress("广东省  潮州市  绿榕南路   绿榕楼12楼");
		    if(!org.apache.commons.lang.StringUtils.isBlank(map.get("reason"))){
		    	String reason = map.get("reason");
		    	if (EmojiFilter.containsEmoji(reason)) {
		    		return result = new BhResult(400, "信息不要包含表情哦", null);
				}else{
					doc.setNote(map.get("reason"));
				}
		    }
		    if(!org.apache.commons.lang.StringUtils.isBlank(map.get("expressName"))){
		    	//程凤云
		    	String expressName = map.get("expressName");
		    	if (EmojiFilter.containsEmoji(expressName)) {
		    		return result = new BhResult(400, "信息不要包含表情哦", null);
				}else{
					doc.setExpressName(map.get("expressName"));
				}
		       
		    }
		    if(!org.apache.commons.lang.StringUtils.isBlank(map.get("expressNo"))){
		    	String expressNo = map.get("expressNo");
		    	if (EmojiFilter.containsEmoji(expressNo)) {
		    		return result = new BhResult(400, "信息不要包含表情哦", null);
				}else{
					 doc.setExpressNo(map.get("expressNo"));
				}		      
		    }
		    if(!org.apache.commons.lang.StringUtils.isBlank(map.get("m_name"))){
		    	String mName = map.get("m_name");
		    	if (EmojiFilter.containsEmoji(mName)) {
		    		return result = new BhResult(400, "信息不要包含表情哦", null);
				}else{
					doc.setmName(map.get("m_name"));
				}     
		    }
		    if(!org.apache.commons.lang.StringUtils.isBlank(map.get("m_phone"))){
		    	String mPhone = map.get("m_phone");
		    	if (EmojiFilter.containsEmoji(mPhone)) {
		    		return result = new BhResult(400, "信息不要包含表情哦", null);
				}else{
					doc.setmPhone(map.get("m_phone"));
				}
		    }
		    refundMapper.updateByPrimaryKeySelective(doc);
		     OrderRefundDocStep od = new OrderRefundDocStep();
			 od.setAddtime(new Date());
			 od.setMid(o.getmId());
			 od.setRefundType(o.getRefundType());
			 od.setOrderRefundDocId(o.getId());
			 List<OrderRefundDocStep> list = orderRefundDocStepMapper.getByOrderRefundDocId(od);
			 
			 
		     if(list.size()>0){
		    	 od.setStep(list.get(0).getStep()+1); //第几步
		    	 orderRefundDocStepMapper.insertSelective(od);
		     }
		    result = new BhResult(200,"添加物流信息成功",null);
		}catch(Exception e){
			e.printStackTrace();
			result = new BhResult(400,"添加物流信息失败",null);
		}
		return result;
		
	}
	
	/**
	 * zlk 2018.4.17
	 * 消费者按照平台给出的退货资料录入快递信息
	 */
	@RequestMapping("/getExpress")
	@ResponseBody
	public BhResult getExpress(){
		
		BhResult result = null;
		try{
			Express e = new Express();
			e.setDBWL("德邦物流");
			e.setGT("国通快递");
			e.setHT("汇通快递");
			e.setKJKD("快捷快递");
			e.setLHTWL("联昊通");
			e.setPOSTB("邮政包裹");
			e.setQFKD("全峰快递");
			e.setSF("顺丰快递");
			e.setSTO("申通快递");
			e.setTT("天天快递");
			e.setWXWL("万象物流");
			e.setYD("韵达快递");
			e.setYSKD("优速物流");
			e.setYTO("圆通快递");
			e.setZTO("中通快递");
		    result = new BhResult(200,"查找物流信息成功",e);
		}catch(Exception e){
			e.printStackTrace();
			result = new BhResult(400,"查找物流信息失败",null);
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
	@OrderLogAnno("退款审核")
	public BhResult auditRefund(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String id = map.get("id");
			   String status = map.get("status");//status:1退款失败，2退款成功 3：已拒绝
			   String refuseReason = map.get("refuseReason"); //退款原因
			   
			    String token = request.getHeader("token");
			    JedisUtil jedisUtil= JedisUtil.getInstance();  
			    JedisUtil.Strings strings=jedisUtil.new Strings();
			    String userJson = strings.get(token);
			    Map mapOne = JSON.parseObject(userJson, Map.class);
			    Integer userId = (Integer)mapOne.get("userId");
			    String username = (String) mapOne.get("username");
			    if(StringUtils.isEmptyOrWhitespaceOnly(userId+"")){
			    	userId = 1;
			    }
			   OrderShop row = service.auditRefund(id, status, userId, refuseReason,username);
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
	 * SCJ-20171101-05
	 * 后台结算模块订单统计
	 * @param id
	 * @return
	 */
	@RequestMapping("/OrderAccount")
	@ResponseBody
	public BhResult OrderAccount(HttpServletRequest request) {
		BhResult result = null;
		   try {
			    String token = request.getHeader("token");
			    JedisUtil jedisUtil= JedisUtil.getInstance();  
			    JedisUtil.Strings strings=jedisUtil.new Strings();
			    String userJson = strings.get(token);
			    Map map = JSON.parseObject(userJson, Map.class);
			    Object sId = map.get("shopId");
			    Integer shopId = 0;
			    if(sId!=null){
			    	shopId = (Integer)sId;
			    }
			   Map<Integer, Object> row = service.OrderAccount(shopId);
				   if (row == null) {
					   result = new BhResult(BhResultEnum.GAIN_FAIL, null);
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
	public BhResult OrderWaitAccount(HttpServletRequest request) {
		BhResult result = null;
		   try {
			    String token = request.getHeader("token");
			    JedisUtil jedisUtil= JedisUtil.getInstance();  
			    JedisUtil.Strings strings=jedisUtil.new Strings();
			    String userJson = strings.get(token);
			    Map map = JSON.parseObject(userJson, Map.class);
			    Object sId = map.get("shopId");
			    Integer shopId = 0;
			    if(sId!=null){
			    	shopId = (Integer)sId;
			    }
			   Map<Integer, Object> row = service.OrderWaitAccount(shopId);
			   if (row == null) {
				   result = new BhResult(BhResultEnum.GAIN_FAIL, null);
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
	public BhResult OrderRefundAccount(HttpServletRequest request, @RequestBody Map<String, String> map1) {
		BhResult result = null;
		   try {
			   String token = request.getHeader("token");
			    JedisUtil jedisUtil= JedisUtil.getInstance();  
			    JedisUtil.Strings strings=jedisUtil.new Strings();
			    String userJson = strings.get(token);
			    Map map = JSON.parseObject(userJson, Map.class);
			    Object sId = map.get("shopId");
			    Integer shopId = 0;
			    if(sId!=null){
			    	shopId = (Integer)sId;
			    }
			  String startTime = map1.get("startTime");// 查询条件：起始时间
			  String endTime = map1.get("endTime");// 查询条件：结束时间
			   Map<String, Object> row = service.OrderRefundAccount(shopId,startTime,endTime);
			   if (row == null) {
				   result = new BhResult(BhResultEnum.GAIN_FAIL, null);
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
	public BhResult OrderMoneyAccount(HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String token = request.getHeader("token");
			    JedisUtil jedisUtil= JedisUtil.getInstance();  
			    JedisUtil.Strings strings=jedisUtil.new Strings();
			    String userJson = strings.get(token);
			    Map map = JSON.parseObject(userJson, Map.class);
			    Object sId = map.get("shopId");
			    Integer shopId = 0;
			    if(sId!=null){
			    	shopId = (Integer)sId;
			    }
			   Map<String, String> row = service.OrderMoneyAccount(shopId);
			   if (row == null) {
				   result = new BhResult(BhResultEnum.GAIN_FAIL, null);
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
	 * SCJ-20171106-02 后台订单数量统计管理
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/OrderNumAccount")
	@ResponseBody
	public BhResult OrderNumAccount(HttpServletRequest request, @RequestBody Map<String, String> map1) {
		BhResult result = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map map = JSON.parseObject(userJson, Map.class);
			Object sId = map.get("shopId");
			Integer shopId = 0;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			String startTime = map1.get("startTime");// 查询条件：起始时间
			String endTime = map1.get("endTime");// 查询条件：结束时间
			Map<String, Object> row = service.OrderNumAccount(shopId,startTime,endTime);
			if (row == null) {
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
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
	 * SCJ-20180305-01
	 * 后台订单数量统计管理(移动端后台-下四)
	 * @param id
	 * @return
	 */
	@RequestMapping("/mOrderNumAccount")
	@ResponseBody
	public BhResult mOrderNumAccount(HttpServletRequest request) {
		BhResult r = null;
		   try {
			   MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			   Integer shopId = 0;
			   if(user!=null){
				   if(user.getShopId()!=null){
					  shopId = user.getShopId().intValue();
				   }
				   Map<String, Object> row = service.mOrderNumAccount(shopId);
				   r = new BhResult();
				   r.setStatus(BhResultEnum.SUCCESS.getStatus());
				   r.setMsg(BhResultEnum.SUCCESS.getMsg());
				   r.setData(row);
				   return r;
			   }else{
				   r = new BhResult();
				   r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				   r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				   return r;
			   }
			} catch (Exception e) {
			    e.printStackTrace();
			    r = BhResult.build(500, "操作异常!");
			    return r;
			}
	}
	
	/**
	 * SCJ-20180306-01
	 * 后台订单数量统计管理(移动端后台-上三)
	 * @param id
	 * @return
	 */
	@RequestMapping("/mNumAccount")
	@ResponseBody
	public BhResult mNumAccount(HttpServletRequest request) {
		BhResult r = null;
		   try {
			   MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			   Integer shopId = 0;
			   if(user!=null){
				   if(user.getShopId()!=null){
					  shopId = user.getShopId().intValue();
				   }
				   Map<String, Object> row = service.mNumAccount(shopId);
				   r = new BhResult();
				   r.setStatus(BhResultEnum.SUCCESS.getStatus());
				   r.setMsg(BhResultEnum.SUCCESS.getMsg());
				   r.setData(row);
				   return r;
			   }else{
				   r = new BhResult();
				   r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				   r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				   return r;
			   }
			} catch (Exception e) {
			    e.printStackTrace();
			    r = BhResult.build(500, "操作异常!");
			    return r;
			}
	}
	
	/**
	 * SCJ-20180305-02
	 * 后台商家订单管理(移动端后台)
	 * @param id
	 * @return
	 */
	@RequestMapping("/mOrderListPage")
	@ResponseBody
	public BhResult mOrderListPage(@RequestBody OrderShop entity, HttpServletRequest request) {
		BhResult r = null;
		   try {
			   MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			   Integer shopId = 0;
			   if(user!=null){
				   if(user.getShopId()!=null){
					  shopId = user.getShopId().intValue();
				   }
				   entity.setShopId(shopId);
				   PageBean<OrderShop> page = service.mOrderListPage(entity);
				   r = new BhResult();
				   r.setStatus(BhResultEnum.SUCCESS.getStatus());
				   r.setMsg(BhResultEnum.SUCCESS.getMsg());
				   r.setData(page);
				   return r;
			   }else{
				   r = new BhResult();
				   r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				   r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				   return r;
			   }
			}catch (Exception e) {
			    e.printStackTrace();
				r = BhResult.build(500, "操作异常!");
				return r;
			}
	}
	
	/**
	 * SCJ-20180306-02
	 * 后台订单详情(移动端后台)
	 * @param id
	 * @return
	 */
	@RequestMapping("/mOrderDetails")
	@ResponseBody
	public BhResult mOrderDetails(@RequestBody OrderShop entity, HttpServletRequest request) {
		BhResult r = null;
		   try {
			   MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			   if(user!=null){
				   OrderShop orderShop = service.mOrderDetails(entity);
				   r = new BhResult();
				   r.setStatus(BhResultEnum.SUCCESS.getStatus());
				   r.setMsg(BhResultEnum.SUCCESS.getMsg());
				   r.setData(orderShop);
				   return r;
			   }else{
				   r = new BhResult();
				   r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				   r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				   return r;
			   }
			} catch (Exception e) {
			    e.printStackTrace();
			    r = BhResult.build(500, "操作异常!");
			    return r;
			}
	}
	
	/**
	 * SCJ-20180307-01
	 * 订单发货走速达流程(移动端后台)
	 * @param id
	 * @return
	 */
	@RequestMapping("/mSportTech")
	@ResponseBody
	public BhResult mSportTech(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult r = null;
		   try {
			   MBusEntity user = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			   if(user!=null){
				   String id = map.get("id");
				   String deliveryPrice = map.get("deliveryPrice");
				   OrderShop row = service.mSportTech(id, deliveryPrice);
				   r = new BhResult();
				   r.setStatus(BhResultEnum.SUCCESS.getStatus());
				   r.setMsg(BhResultEnum.SUCCESS.getMsg());
				   r.setData(row);
				   return r;
			   }else{
				   r = new BhResult();
				   r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				   r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				   return r;
			   }
			} catch (Exception e) {
			    e.printStackTrace();
			    r = BhResult.build(500, "操作异常!");
			    return r;
			}
	}
	
	/**
	 * SCJ-20180307-02
	 * 订单发货商家自配(移动端后台)
	 * @param id
	 * @return
	 */
	@RequestMapping("/mDistributeByself")
	@ResponseBody
	public BhResult mDistributeByself(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult r = null;
		   try {
			   String id = map.get("id");
			   MBusEntity entity = (MBusEntity) request.getSession(false).getAttribute(Contants.MUSER);
			   if(entity!=null){
				   if (entity.getShopId()  == null ) {
					entity.setShopId((long) 1);
				}
				   int row = service.mDistributeByself(id, entity.getShopId());
				   r = new BhResult();
				   r.setStatus(BhResultEnum.SUCCESS.getStatus());
				   r.setMsg(BhResultEnum.SUCCESS.getMsg());
				   r.setData(row);
				   return r;
			   }else{
				   r = new BhResult();
				   r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				   r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				   return r;
			   }
			} catch (Exception e) {
			    e.printStackTrace();
			    r = BhResult.build(500, "操作异常!");
			    return r;
			}
	}
	
	/**
	 * xxj
	 * 京东订单统计
	 * @param id
	 * @return
	 */
	@RequestMapping("/jdOrderCount")
	@ResponseBody
	public BhResult jdOrderCount(HttpServletRequest request) {
		BhResult result = null;
		   try {
			   String token = request.getHeader("token");
			    JedisUtil jedisUtil= JedisUtil.getInstance();  
			    JedisUtil.Strings strings=jedisUtil.new Strings();
			    String userJson = strings.get(token);
			    Map map = JSON.parseObject(userJson, Map.class);
			    Object sId = map.get("shopId");
			    Integer shopId = 1;
			    if(sId!=null){
			    	shopId = (Integer)sId;
			    }
			   Map<String, Object> row = service.jdOrderCount(shopId);
			   if (row == null) {
				   result = new BhResult(BhResultEnum.GAIN_FAIL, null);
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
				   result = new BhResult(BhResultEnum.GAIN_FAIL, null);
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
	 * SCJ-20171109-03 后台首页订单交易总额和订单数量统计
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/totalNumAccount")
	@ResponseBody
	public BhResult totalNumAccount(HttpServletRequest request, @RequestBody Map<String, String> map1) {
		BhResult result = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map map = JSON.parseObject(userJson, Map.class);
			Object sId = map.get("shopId");
			Integer shopId = 0;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			String startTime = map1.get("startTime");// 查询条件：起始时间
			String endTime = map1.get("endTime");// 查询条件：结束时间
			Map<String, Object> row = service.totalNumAccount(shopId,startTime,endTime);
			if (row == null) {
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
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
	 * SCJ-20171110-02
	 * 商家后台退款订单列表
	 * @return
	 */
	@RequestMapping("/orderRefundList")
	@ResponseBody
	public BhResult orderRefundList(@RequestBody Map<String, String> map, HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   String order_no = map.get("order_no"); //查询条件
		   String startTime = map.get("startTime");//查询条件、起始时间
		   String endTime = map.get("endTime");//查询条件、起始时间
		   String status = map.get("status"); //订单状态-0:退款中 1:退款失败 2:退款成功
		   String currentPage = map.get("currentPage");//当前第几页
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
		    PageBean<OrderRefundDoc> page = service.orderRefundList(shopId, order_no, status, currentPage, pageSize, startTime, endTime);
			   if (page !=null) {
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
	
	/**
	 * SCJ-20171110-02
	 * 商家后台退款订单列表
	 * @return
	 */
	@RequestMapping("/memberNoticeList")
	@ResponseBody
	public BhResult memberNoticeList(@RequestBody Map<String, String> map, HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   MemberNotice notice = new MemberNotice();
		   
		   List<MemberNotice> list = service.selectMemberNoticeList(notice);
		   result = new BhResult(BhResultEnum.SUCCESS, list);
			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	
	/**
	 * SCJ-20171130-02
	 * 商家后台配送订单列表
	 * @return
	 */
	@RequestMapping("/orderSendPage")
	@ResponseBody
	public BhResult orderSendPage(@RequestBody Map<String, String> map, HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   String shopOrderNo = map.get("shopOrderNo"); //查询条件-商家订单号
		   String dStatus = map.get("dStatus"); //'配送状态  0(初始状态)待发货   1发货中   2已送达，3已取消，4已结算',
		   String currentPage = map.get("currentPage");//当前第几页
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
		    PageBean<OrderSendInfo> page = service.orderSendPage(shopId, shopOrderNo, dStatus, currentPage, pageSize);
			result = new BhResult(BhResultEnum.SUCCESS, page);

			} catch (Exception e) {
				result = new BhResult(BhResultEnum.VISIT_FAIL, null);
				LoggerUtil.getLogger().error(e.getMessage());
			}
		return result;
	}
	
	/**
	 * SCJ-20171130-03
	 * 商家后台配送订单详情
	 * @return
	 */
	@RequestMapping("/getOrderSendDetails")
	@ResponseBody
	public BhResult getOrderSendDetails(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   	String id = map.get("id"); //配送单id
		    OrderSendInfo info = service.getOrderSendDetails(id);
			   if (info !=null) {
				   result = new BhResult(BhResultEnum.SUCCESS, info);
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
	 * SCJ-20171130-04
	 * 商家后台配送订单审核打款
	 * @return
	 */
	@RequestMapping("/auditOrderSend")
	@ResponseBody
	public BhResult auditOrderSend(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   	String id = map.get("id"); //配送单id
		    int row = service.auditOrderSend(id);
			   if (row == 1) {
				   result = new BhResult(BhResultEnum.SUCCESS, null);
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
	 * SCJ-20180104-01
	 * 商家后台订单添加备注
	 * @return
	 */
	@RequestMapping("/insertRemark")
	@ResponseBody
	public BhResult insertRemark(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   	String id = map.get("id"); //商家订单id
		   	String remark = map.get("remark"); //备注
		    int row = service.insertRemark(id, remark);
			   if (row ==1 ) {
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
	 * SCJ-20180104-02
	 * 商家后台订单自配
	 * @return
	 */
	@RequestMapping("/deliveryOwner")
	@ResponseBody
	public BhResult deliveryOwner(@RequestBody Map<String, String> map, HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   	String id = map.get("id"); //商家订单id
		   	String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Object sId = mapOne.get("shopId");
		    Integer shopId = 1;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
		    int row = service.deliveryOwner(id, shopId);
			   if (row ==1 ) {
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
	 * SCJ-20180106-02
	 * 获取用户会员信息
	 * @return
	 */
	@RequestMapping("/getMemerUserInfo")
	@ResponseBody
	public BhResult getMemerUserInfo(@RequestBody Map<String, String> map) {
	   BhResult result = null;
	   try {
		   	String mId = map.get("mId"); //会员	id
		   	Map<String, Object> row = service.getMemerUserInfo(mId);
			   if (row!=null) {
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
	 *zlk-20180201-02
	 * PC端 获取订单的物流信息
	 * @return
	 */
	@RequestMapping("/getTrackInfo")
	@ResponseBody
	public BhResult getTrackInfo(@RequestBody Map<String, String> map,HttpServletRequest request,HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			String id = map.get("id");
			if(StringUtils.isNullOrEmpty(id)){
				bhResult = new BhResult(BhResultEnum.REQUEST_FAIL, "参数不能为空");
			}else{
				OrderStock orderStock  = new OrderStock();
				/* 获取订单详情*/
				//根据主键获取oderShop表的信息
			    OrderShop orderShop1 = jdOrderService.getOrderById(id);
			    //根据订单号获取order_main
			    Order order = jdOrderService.getOrderMainById(orderShop1.getOrderNo());
			    //根据order_main 表的m_addr_id获取 member_user_address表信息
			    MemberUserAddress mua = jdOrderService.getUserById(order.getmAddrId());
			    orderStock.setOrderNo(orderShop1.getOrderNo()); //订单号
			    orderStock.setAddress(mua.getProvname()+mua.getCityname()+mua.getAreaname()+mua.getAddress());//收货地址
			    //图片地址
			    List<OrderSku> listOrderSku = jdOrderService.getOrderSkuByOrderId(orderShop1.getOrderId());
			    GoodsSku skuList = goodsSkuMapper.selectByPrimaryKey(listOrderSku.get(0).getSkuId());
			    JSONObject jsonObj = JSONObject.fromObject(skuList.getValue()); 
				JSONArray personList = jsonObj.getJSONArray("url");
				String url = (String) personList.get(0);
				orderStock.setImgeUrl(url); 
				
			    //查询当前order_send_info,jd_order_id 为0，不是京东的物流信息，是商家自配的物流
				OrderSendInfo orderSendInfo3 = new OrderSendInfo();
				orderSendInfo3.setOrderShopId(Integer.valueOf(id));
				OrderSendInfo orderSendInfo2 = orderSendInfoMapper.selectByOrderShopId(orderSendInfo3);
				
				Track track = new Track();
				List<Track> listTrack2 = new ArrayList<Track>();
				
				if(orderShop1.getDeliveryWay()==1){
					//商家自配
					if(orderShop1.getdState()==4){   //已送达
					    orderStock.setJd("0");
					    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    if(orderShop1.getReceivedtime()!=null){
					        String dateString = formatter.format(orderShop1.getReceivedtime());
					        track.setMsgTime(dateString);
					    }else{
					        track.setMsgTime("");
					    }

				    	track.setOperator("");
					    track.setContent("已送达");//内容
						listTrack2.add(track);
					}else{  //已发货
						orderStock.setJd("0");
						Track track2 = new Track();
					    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    if(orderShop1.getSendTime()!=null){
					         String dateString = formatter.format(orderShop1.getSendTime());
					         track2.setMsgTime(dateString);
					    }else{
					    	 track2.setMsgTime("");
					    }
					    track2.setOperator("");
					    track2.setContent("已发货");
						listTrack2.add(track2);
					}
				
					orderStock.setOrderTrack(listTrack2);
					return bhResult = new BhResult(200,"获取信息成功，商家自配！", orderStock);
					
				}else if(orderShop1.getDeliveryWay()==0){
					  //速达
					if(orderSendInfo2==null||orderSendInfo2.equals("")){
						//没有信息
						  orderStock.setJd("0");
						  track.setMsgTime("");
						  track.setOperator("");
						  track.setContent("待接单");//内容
						  listTrack2.add(track);
						  orderStock.setOrderTrack(listTrack2);
						  return bhResult = new BhResult(200,"待接单！", orderStock);
					}
					if(orderSendInfo2.getdState()==0){
					
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    track.setMsgTime("");//时间
					    track.setOperator(orderSendInfo2.getsName());//配送员
					    track.setContent("待发货");//内容
					    listTrack2.add(track);
					    orderStock.setOrderTrack(listTrack2);
					    orderStock.setJd("0"); //自营物流
					    bhResult = new BhResult(200,"待发货！", orderStock);
			    	    return bhResult;
			    	}else if(orderSendInfo2.getdState()==1){
				
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        track.setMsgTime("");//时间
				        track.setOperator(orderSendInfo2.getsName());//配送员
					    track.setContent("待发货");//内容
					    listTrack2.add(track);
					    
					    Track track2 = new Track();
					    if(orderSendInfo2.getDeliverTime()!=null){
					         track2.setMsgTime(sdf.format(orderSendInfo2.getDeliverTime()));//时间
					    }else{
					    	 track2.setMsgTime("");//时间
					    }
					    track2.setOperator(orderSendInfo2.getsName());//配送员
					    track2.setContent("发货中");//内容
					    listTrack2.add(track2);
					    
					    orderStock.setOrderTrack(listTrack2);
					    orderStock.setJd("0"); //自营物流
					    bhResult = new BhResult(200,"发货中！", orderStock);
			        	return bhResult;
				    }else if(orderSendInfo2.getdState()==2){
					
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        track.setMsgTime("");//时间
				        track.setOperator(orderSendInfo2.getsName());//配送员
					    track.setContent("待发货");//内容
					    listTrack2.add(track);
					    
					    Track track2 = new Track();
					    if(orderSendInfo2.getDeliverTime()!=null){
					         track2.setMsgTime(sdf.format(orderSendInfo2.getDeliverTime()));//时间
					    }else{
					    	 track2.setMsgTime("");//时间
					    }
					    track2.setOperator(orderSendInfo2.getsName());//配送员
					    track2.setContent("发货中");//内容
					    listTrack2.add(track2);
				        
					    Track track3 = new Track();
					    track3.setMsgTime(sdf.format(orderSendInfo2.getSendTime()));//时间
					    track3.setOperator(orderSendInfo2.getsName());//配送员
					    track3.setContent("已送达");//内容
					    listTrack2.add(track3);
					    
				    	orderStock.setOrderTrack(listTrack2);
					    orderStock.setJd("0"); //自营物流
					    bhResult = new BhResult(200,"已送达！", orderStock);
			    	    return bhResult;
				    }else if(orderSendInfo2.getdState()==3){
					
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        track.setMsgTime("");//时间
					    track.setOperator(orderSendInfo2.getsName());//配送员
					    track.setContent("已取消");//内容
					    listTrack2.add(track);
					    orderStock.setOrderTrack(listTrack2);
					    orderStock.setJd("0"); //自营物流
					    bhResult = new BhResult(200,"已取消！", orderStock);
			    	    return bhResult;
				    }else if(orderSendInfo2.getdState()==4){
					
				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        track.setMsgTime("");//时间
				        track.setOperator(orderSendInfo2.getsName());//配送员
					    track.setContent("待发货");//内容
					    listTrack2.add(track);
					    
					    Track track2 = new Track();
					    if(orderSendInfo2.getDeliverTime()!=null){
					         track2.setMsgTime(sdf.format(orderSendInfo2.getDeliverTime()));//时间
					    }else{
					    	 track2.setMsgTime("");//时间
					    }
					    track2.setOperator(orderSendInfo2.getsName());//配送员
					    track2.setContent("发货中");//内容
					    listTrack2.add(track2);
				        
					    Track track3 = new Track();
					    track3.setMsgTime(sdf.format(orderSendInfo2.getSendTime()));//时间
					    track3.setOperator(orderSendInfo2.getsName());//配送员
					    track3.setContent("已送达");//内容
					    listTrack2.add(track3);
				        
					    Track track4 = new Track();
					    track4.setMsgTime("");//时间
					    track4.setOperator(orderSendInfo2.getsName());//配送员
					    track4.setContent("已结算");//内容
					    listTrack2.add(track4);
					    
					    orderStock.setOrderTrack(listTrack2);
					    orderStock.setJd("0"); //自营物流
				  	    bhResult = new BhResult(200,"已结算！", orderStock);
			        	return bhResult;
				    }
					
				}else if(orderShop1.getDeliveryWay()==3){
					  //其他物流
					  String host = "https://wuliu.market.alicloudapi.com";
			   	      String path = "/kdi";
			   	      String method = "GET";
			   	      String appcode = "232d013ef8244587a9a4f69cb2fcca47";
			   	      Map<String, String> headers = new HashMap<String, String>();
			   	      headers.put("Authorization", "APPCODE " + appcode);
			   	      Map<String, String> querys = new HashMap<String, String>();
			   	      querys.put("no", order.getExpressNo());
			   	      //获取物流配送信息
			   	      HttpResponse response2 = HttpUtils.doGet(host, path, method, headers, querys);
			   	      String Logistics = EntityUtils.toString(response2.getEntity());
			   	      JSONObject jsonObject2 = JSONObject.fromObject(Logistics);
			   	      orderStock.setJd("1"); //其他物流
			   	      orderStock.setLogistics(jsonObject2);//返回给前端的物流配送信息
			   	      
			   	      if(jsonObject2.get("status").equals("0")){
			   	        //把返回的json转换成实体类字段
			   	        Gson gson = new Gson();
					    JdResult<OtherStock> ret = gson.fromJson(Logistics, new TypeToken<JdResult<OtherStock>>(){}.getType());
			   	        //其他物流信息
					    OtherStock otherStock =  ret.getResult();
					    List<OtherTrack> list = otherStock.getOtherTrack();
					    //把物流信息保存至数据库中
					    for(int i=0;i<list.size();i++){
						  
						  JdOrderTrack jdOrderTrack = new JdOrderTrack();
				    	  jdOrderTrack.setOrderId(orderShop1.getOrderId());//订单ID
				    		
				    	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    	  jdOrderTrack.setMsgTime(sdf.parse(list.get(i).getTime()));//时间
				    	  jdOrderTrack.setContent(list.get(i).getStatus()); //内容
				    	  //根据时间、内容、操作人、订单id 获取JdOrderTrack表的数据
				    	  List<JdOrderTrack> jdOrderTrackList  = jdOrderTrackMapper.getByOrderId(jdOrderTrack);
				    	  if(jdOrderTrackList!=null&&jdOrderTrackList.size()>0){
				    			//有值就不保存
				    		}else{
				    			//没值就保存
				    			jdOrderTrack.setOrderNo(orderShop1.getOrderNo());
				    			jdOrderTrack.setStatus("1");
				    			int row = jdOrderTrackMapper.insertSelective(jdOrderTrack);
				    	  }
						
					    }
			   	      }
			   	      return bhResult = new BhResult(200,"当前为其他物流信息！", orderStock);
				}else if(orderShop1.getDeliveryWay()==2){
				      //京东物流
				      OrderShop orderShop = new OrderShop();
				      orderShop.setId(Integer.parseInt(id));
				      //查询京东物流信息，获取当前订单妥投信息，如果为妥投，就更改订单状态
			          orderStock  = jdOrderService.orderTrack(orderShop,null); 
			          orderStock.setOrderNo(orderShop1.getOrderNo()); //订单号
			          orderStock.setAddress(mua.getProvname()+mua.getCityname()+mua.getAreaname()+mua.getAddress());//收货地址
				      orderStock.setImgeUrl(url); 
			          orderStock.setJd("2"); //是京东
			    
			          if(StringUtils.isNullOrEmpty(orderStock.getJdOrderId())){
			    	     bhResult = new BhResult(200,"目前京东没有配送信息！", orderStock);
			    	     return bhResult;
			          }
			          //把京东物流信息保存到jd_order_track表
			          List<Track> listTrack = orderStock.getOrderTrack();
			          //判断当前的订单的京东JdOrderId和返回的京东JdOrderId是不是一样的
			          if(listTrack.size()>0&&orderShop1.getJdorderid().equals(orderStock.getJdOrderId())){
			    	
			    	     for(int i=0;i<listTrack.size();i++){
			    		
			    		   JdOrderTrack jdOrderTrack = new JdOrderTrack();
			    		   jdOrderTrack.setOrderId(orderShop1.getOrderId());//订单ID
			    		
			    		   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    		   jdOrderTrack.setMsgTime(sdf.parse(listTrack.get(i).getMsgTime()));//时间
			    		   jdOrderTrack.setContent(listTrack.get(i).getContent()); //内容
			    		   jdOrderTrack.setOperator(listTrack.get(i).getOperator());//操作人
			    		   //根据时间、内容、操作人、订单id 获取JdOrderTrack表的数据
			    		   List<JdOrderTrack> jdOrderTrackList  = jdOrderTrackMapper.getByOrderId(jdOrderTrack);
			    		   if(jdOrderTrackList!=null&&jdOrderTrackList.size()>0){
			    			   //有值就不保存
			    		   }else{
			    			   //没值就保存
			    			   jdOrderTrack.setOrderNo(orderShop1.getOrderNo());
			    			   int row = jdOrderTrackMapper.insertSelective(jdOrderTrack);
			    		   }
			    	    }
			          }
			          bhResult = new BhResult(BhResultEnum.SUCCESS, orderStock);
			    }
			 }
		}catch (Exception e) {
			e.printStackTrace();
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return bhResult;
	}
	
	/**
	 * 测试拼团代码
	 */
	@RequestMapping("/teamFail")
	@ResponseBody
	public void teamFail() {
		 service.teamFail("31941802081224356440");
	}
	
	/**
	 * 根据订单获取订单
	 * @param xieyc
	 * @return
	 */
	@RequestMapping("/getOrderByOrderNo")
	@ResponseBody
	public BhResult getOrderByOrderNo(@RequestBody Map<String, String> map) {
		String orderNo=map.get("orderNo");
		BhResult result = null;
		try {
			Order order = service.getOrderByOrderNo(orderNo);
			result = new BhResult(BhResultEnum.SUCCESS,order);	
	    } catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取所有店铺名称
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/getAllShopName")
	@ResponseBody
	public BhResult getAllShopName() {
		BhResult r = null;
		try {
			List<Map<String, Object>> data = service.getAllShopName();
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
	
	
	
	
	/**
	 * 
	 * @Description: 统计某个时段的销售排行
	 * @author xieyc
	 * @date 2018年3月27日 下午2:37:24 
	 */
	@RequestMapping("/getSaleOrderList")
	@ResponseBody
	public BhResult getTopTenGoodsList(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult result = null;
		try {
			String startTime = map.get("startTime");//查询条件、起始时间
			String endTime = map.get("endTime");//查询条件、起始时间
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 0;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			PageBean<OrderSku> pageGoods = service.getSaleOrderList(shopId,startTime,endTime);
			if (pageGoods == null) {
				result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
			} else {
				result = new BhResult(BhResultEnum.SUCCESS, pageGoods);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * @Description: 退款售后详情查看
	 * @author fanjh
	 * @date 2018年8月29日 下午4:37:24 
	 */
	@RequestMapping("/selectOrderDetail")
	@ResponseBody
	public BhResult selectOrderDetail(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult result = null;
	try {
		String skuId = map.get("skuId"); //商家订单id
		MyOrderSkuPojo orderSku=service.selectOrderDetail(Integer.valueOf(skuId));
		if (orderSku == null) {
			result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
		} else {
			result = new BhResult(BhResultEnum.SUCCESS, orderSku);
		}
	} catch (Exception e) {
		e.printStackTrace();
		LoggerUtil.getLogger().error(e.getMessage());
	}
	return result;
    }
	
	/**
	 * @Description: 填写物流信息
	 * @author fanjh
	 * @date 2018年8月30日 上午10:23:24 
	 */
	@RequestMapping("/writeLogistics")
	@ResponseBody
	public BhResult writeLogistics(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult result = null;
		try {
			String orderSkuId = map.get("orderSkuId"); //订单skuId
			MyOrderSkuPojo orderSku=service.writeLogistics(Integer.valueOf(orderSkuId));
			if (orderSku == null) {
				result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
			} else {
				result = new BhResult(BhResultEnum.SUCCESS, orderSku);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * @Description: 提交物流信息
	 * @author fanjh
	 * @date 2018年8月30日 上午10:23:24 
	 */
	@RequestMapping("/saveLogistics")
	@ResponseBody
	public BhResult saveLogistics(@RequestBody OrderRefundDoc entiy,HttpServletRequest request) {
		BhResult result = null;
		try {
			/*String expressNo = map.get("expressNo"); //用户发货物流单号
			String expressName = map.get("expressName"); //用户发货快递公司
			String mName = map.get("mName"); //联系人
			String mPhone = map.get("mPhone"); //联系电话
			String specifications = map.get("specifications"); //规格
			String voucherImage = map.get("voucherImage"); //凭证图片
			String orderSkuId = map.get("orderSkuId"); //商家订单skud
*/			int row=service.saveLogistics(entiy);
			if (row == 0) {
				result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
			} else {
				result = new BhResult(BhResultEnum.SUCCESS, row);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * @Description: 查看物流信息
	 * @author fanjh
	 * @date 2018年8月30日 上午10:23:24 
	 */
	@RequestMapping("/selectLogistics")
	@ResponseBody
	public BhResult selectLogistics(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String orderSkuId = map.get("orderSkuId"); //用户发货物流单号
   		    MyOrderSkuPojo orderSku=service.selectLogistics(Integer.valueOf(orderSkuId));
			if (orderSku == null) {
				result = new BhResult(BhResultEnum.REQUEST_FAIL, null);
			} else {
				result = new BhResult(BhResultEnum.SUCCESS, orderSku);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 
	 * @Description: 根据物流单号查询物流信息
	 * @author xieyc
	 * @date 2018年9月11日 上午10:08:43 
	 * @param   
	 * @return  
	 *
	 */
	@RequestMapping("/queryLogistics")
	@ResponseBody
	public BhResult queryLogistics(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String expressNo = map.get("expressNo");
			String type = map.get("type");
			JSONObject jsonObject=service.queryLogistics(expressNo,type);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(jsonObject);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * @Description: 将order_main的物流信息迁移到order_shop里面
	 * @author xieyc
	 * @date 2018年9月11日 上午10:08:43 
	 * @param   
	 * @return  
	 */
	@RequestMapping("/moveLogistics")
	@ResponseBody
	public BhResult moveLogistics(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String strId = map.get("id");//商家订单id
			Integer id=null;
			if(org.apache.commons.lang.StringUtils.isNotBlank(strId)){
				id=Integer.valueOf(strId);
			}
			int row=service.moveLogistics(id);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			e.printStackTrace();
		}
		return r;
	}
	
}
	
