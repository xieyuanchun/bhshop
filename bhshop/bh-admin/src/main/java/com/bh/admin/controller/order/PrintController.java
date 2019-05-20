package com.bh.admin.controller.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.admin.mapper.goods.GoodsOperLogMapper;
import com.bh.admin.mapper.order.OrderShopMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.goods.GoodsOperLog;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderRefundDoc;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.user.MemberUserAddress;
import com.bh.admin.service.PrintsService;
import com.bh.admin.service.WechatTemplateMsgService;
import com.bh.admin.util.JedisUtil;
import com.bh.admin.util.JedisUtil.Strings;
import com.bh.admin.vo.TemplateMsgResult;
import com.bh.admin.vo.WechatTemplateMsg;
import com.bh.bean.Sms;
import com.bh.config.Contants;
import com.bh.config.WxTeamplate;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.SmsUtil;
import com.bh.utils.pay.WXPayUtil;
import com.bh.wxpayjsapi.common.JsonUtil;
import com.ctc.wstx.util.StringUtil;



@Controller
@RequestMapping("/print")
public class PrintController {

	@Autowired
	private PrintsService service;
	@Autowired
	WechatTemplateMsgService wtService;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private GoodsOperLogMapper goodsOperLogMapper;
    @Autowired
	private MemberShopMapper memberShopMapper;
    @Autowired
	private OrderSkuMapper orderSkuMapper;
	//获取订单详情,商家订单批量打印销售单接口，有验证是否打印过
	@RequestMapping("/selectById")
	@ResponseBody
	public BhResult selectById(@RequestBody Map<String, String> map, HttpServletRequest request) {
		   BhResult result = null;
		   try {
			    //2018.6.5 zlk
			    String token = request.getHeader("token");
			    JedisUtil jedisUtil= JedisUtil.getInstance();  
			    Strings strings=jedisUtil.new Strings();
			    String userJson = strings.get(token);
			    Map mapOne = JSON.parseObject(userJson, Map.class);
			    Integer userId = (Integer)mapOne.get("userId");
			    if(StringUtils.isBlank(String.valueOf(userId))){
				    userId = 1;
				}
			    Object sId = mapOne.get("shopId");
			    Integer shopId = 1;
			    if(sId!=null){
			    	shopId = (Integer)sId;
			    } 
			     //end
			   
			   
			    List<OrderShop> listShop = new ArrayList<OrderShop>();
			    //订单号
			    String idS = map.get("shopOrderNo");  //商家订单号
			    String printCode = map.get("printCode"); //打印的验证码
			    if(StringUtils.isEmpty(idS)){
			    	result = new BhResult(400, "参数不能为空", null);
			    }
			    String[] arr = idS.split(",");
			    
			    List<String> list = new ArrayList<String>(); //将传来的商家订单号转成集合
			    for(int i =0;i<arr.length;i++){
			    	list.add(arr[i]);
			    }
			    
			    List<String> list2 = new ArrayList<String>();   //保存已经打印过的商家订单号
			    List<String> afterSale = new ArrayList<String>(); //申请售后的订单
			    for(String id:list){
			         //根据商家订单号 获取oderShop表的信息
			         //OrderShop orderShop = service.getOrderById(id);
			         OrderShop orderShop = service.getByOrderNo(id,shopId);//2018.6.1
			         if(orderShop.getPrintCount()>0){
			    	     list2.add(orderShop.getOrderNo());  //把打印过得订单的 平台订单号保存在集合
			         }
			         
			         
			    	//根据order_shop_id获取order_sku表的商品名称
//			        List<OrderSku> sku = service.getByOrderShopId(id);
			        List<OrderSku> sku = service.getByOrderShopId(String.valueOf(orderShop.getId()));
			       
			       
			        //根据订单号获取order_main
			        Order order = service.getOrderMainById(orderShop.getOrderNo());
			        
			        //根据order_main 表的m_addr_id获取 member_user_address表信息
			        MemberUserAddress mua = service.getUserById(order.getmAddrId());
			     
			        for(OrderSku o:sku) {
			        	//如果订单有申请退款，就不打印
						List<OrderRefundDoc> ord = service.getOrderRefundDoc(o.getId());
						if(ord.size()>0) {
							afterSale.add(orderShop.getOrderNo());
							continue;
						}
			            //OrderShop orderShop2 = service.getOrderById(id);
						OrderShop orderShop2 = service.getByOrderNo(id,shopId);//2018.6.1
			           //根据order_sku表的sku_id 获取 GoodsSku表的信息			
			            GoodsSku goodsSku = service.getGoodsSku(o.getSkuId());
			       
			            orderShop2.setAddress(mua.getProvname()+mua.getCityname()+mua.getAreaname()+mua.getFourname()+mua.getAddress());
			            orderShop2.setFullName(mua.getFullName());
			            orderShop2.setTel(mua.getTel());
			          
			            StringBuffer sb=new StringBuffer(o.getGoodsName()
			            		+" "+goodsSku.getKeyOne()+" "+goodsSku.getValueOne());
			            
			            if (StringUtils.isNotBlank(goodsSku.getKeyTwo())) {
			             sb.append(" "+goodsSku.getKeyTwo()+" "+goodsSku.getValueTwo());
			            }
			            if (StringUtils.isNotBlank(goodsSku.getKeyThree())) {
			             sb.append(" "+goodsSku.getKeyThree()+" "+goodsSku.getValueThree());
			            }
			            if (StringUtils.isNotBlank(goodsSku.getKeyFour())) {
			             sb.append(" "+goodsSku.getKeyFour()+" "+goodsSku.getValueFour());
			            }
			            if (StringUtils.isNotBlank(goodsSku.getKeyFive())) {
			             sb.append(" "+goodsSku.getKeyFive()+" "+goodsSku.getValueFive());
			            }
			            orderShop2.setGoodName(sb.toString());
			
			            orderShop2.setSkuNum(o.getSkuNo());
			            orderShop2.setGoodNum(o.getSkuNum());
			            orderShop2.setCode(goodsSku.getJdUpc());
			            orderShop2.setPrintState("备货中");
			            listShop.add(orderShop2);
			        }
			    }
			    
			    if(list2.size()>0){ //传来的订单有打印过的
			    	if(!StringUtils.isEmpty(printCode)){
			    	   if(listShop.get(0).getPrintCode()!=null){
			        	  if(listShop.get(0).getPrintCode().equals(printCode)){ //验证码一样
			        		 return result = new BhResult(200, "验证码正确", listShop);
			    	      }else{
			    	    	 return  result = new BhResult(400, "验证码错误", null);
			    	      }
			    	   }else{
			    		     return  result = new BhResult(400, "请发送验证码", null);
			    	   }
			    	}else{
			    	     return  result = new BhResult(500, "该订单已经打印过，请输入验证码", list2);
			    	}
			    }
			    
				if (listShop.size()!=0) {
					String[] orderNo=idS.split(",");
					GoodsOperLog goodsOperLog = new GoodsOperLog();
					StringBuffer sb=new StringBuffer();
					StringBuffer sb1=new StringBuffer();
					for (int i = 0; i < orderNo.length; i++) {
						List<OrderShop> orderShopList= orderShopMapper.selectByOrderNos(orderNo[i],shopId.toString());
						for (int j = 0; j < orderShopList.size(); j++) {
							sb.append(orderShopList.get(j).getId()+",");
						}
					}
					String orderNos=sb.substring(0, sb.length()-1);
					String[] orderNo1=orderNos.split(",");
					for (int i = 0; i < orderNo1.length; i++) {
					     List<OrderSku> orderSkuList= orderSkuMapper.selectByGoodsId(Integer.parseInt(orderNo1[i]),shopId);
					     for (int j = 0; j < orderSkuList.size(); j++) {
					    	 sb1.append(orderSkuList.get(j).getGoodsId()+",");
						}
					}
					String goodsIds=sb1.substring(0, sb1.length()-1);
					goodsOperLog.setOpType("打印销售单");
					goodsOperLog.setGoodId(goodsIds);
					goodsOperLog.setOrderId(orderNos);
					goodsOperLog.setUserId(userId.toString());
					goodsOperLog.setOpTime(new Date());
					String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
					goodsOperLog.setAdminUser(userName);
					goodsOperLogMapper.insertSelective(goodsOperLog);
                     listShop.get(0).setOrderNoList(afterSale);
					 result = new BhResult(200, "操作成功", listShop);
				} else {
					 result = BhResult.build(300, "订单可能已经申请退款！",afterSale);
				}
		    } catch (Exception e) {
				result = BhResult.build(500, "数据库搜索失败!");
				LoggerUtil.getLogger().error(e.getMessage());
		    }
			return result;
		}
	
	
	
	//打印后更改订单状态
	@RequestMapping("/updateOrderShopById")
	@ResponseBody
	public BhResult updateOrderShopById(@RequestBody Map<String, String> map,HttpServletRequest request) {
		   BhResult result = null;
		   try {
			     
			    //2018.6.5 zlk
			    String token = request.getHeader("token");
			    JedisUtil jedisUtil= JedisUtil.getInstance();  
			    Strings strings=jedisUtil.new Strings();
			    String userJson = strings.get(token);
			    Map mapOne = JSON.parseObject(userJson, Map.class);
			    Object sId = mapOne.get("shopId");
			    Integer shopId = 1;
			    if(sId!=null){
			    	shopId = (Integer)sId;
			    } 
			     //end
			    List<Object> obj = new ArrayList<Object>();
			    String idS = map.get("shopOrderNo");
			    if(StringUtils.isEmpty(idS)){
			    	result = new BhResult(400, "参数不能为空", null);
			    }
			    String[] arr = idS.split(",");
			    for(String id :arr){
			    	//OrderShop orderShop = service.getOrderById(id);
			    	OrderShop orderShop = service.getByOrderNo(id,shopId);//2018.6.1
			    	//if(orderShop.getJdorderid().equals("0")){ //京东的订单不改状态
			    	    orderShop.setStatus(9);
			    		orderShop.setPrintTime(new Date());//打印销售单时间
				        int row = service.updatePrintByPrimaryKey(orderShop);
			    	    obj.add(row);
			    	//}
			    }
			    if (obj.size()!=0) {
					   result = new BhResult(200, "操作成功",obj);
					} else {
					   result = new BhResult(200, "修改数据失败！",null);
					}
		   }catch(Exception e){
			   e.printStackTrace();
			   result = BhResult.build(500, "数据库搜索失败!");
		   }
		   
		   return result;
	}
	
	
	//打印后更改打印次数
	@RequestMapping("/updatePrintCount")
	@ResponseBody
	public BhResult updatePrintCount(@RequestBody Map<String, String> map,HttpServletRequest request) {
		   BhResult result = null;
		   try {
			    
			    //2018.6.5 zlk
			    String token = request.getHeader("token");
			    JedisUtil jedisUtil= JedisUtil.getInstance();  
			    Strings strings=jedisUtil.new Strings();
			    String userJson = strings.get(token);
			    Map mapOne = JSON.parseObject(userJson, Map.class);
			    Object sId = mapOne.get("shopId");
			    Integer shopId = 1;
			    if(sId!=null){
			    	shopId = (Integer)sId;
			    } 
			     //end
			   
			   
			    List<Object> obj = new ArrayList<Object>();
			    String idS = map.get("shopOrderNo");
			    if(StringUtils.isEmpty(idS)){
			    	result = new BhResult(400, "参数不能为空", null);
			    }
			    String[] arr = idS.split(",");
			    
			    //去重
			    List<String> list = new ArrayList<>();  
			    list.add(arr[0]);
			    for(int i=0;i<arr.length;i++) {
			    	if(list.toString().indexOf(arr[i])==-1) {
			    		list.add(arr[i]);
			    	}
			    }
			    
			    for(int j=0;j<list.size();j++){
			    	
			    	OrderShop orderShop = service.getByOrderNo(list.get(j),shopId);//2018.6.1
				    orderShop.setPrintCount(orderShop.getPrintCount()+1);
				    int row = service.updatePrintByPrimaryKey(orderShop);
				   
				    OrderShop orderShop2 = service.getByOrderNo(list.get(j),shopId);//2018.6.1
			    	obj.add(orderShop2.getPrintCount());
			    }
			    
//			    for(String id :arr){
//			    	
//			    	OrderShop orderShop = service.getByOrderNo(id,shopId);//2018.6.1
//				    orderShop.setPrintCount(orderShop.getPrintCount()+1);
//				    int row = service.updatePrintByPrimaryKey(orderShop);
//				   
//				    OrderShop orderShop2 = service.getByOrderNo(id,shopId);//2018.6.1
//			    	obj.add(orderShop2.getPrintCount());
//			    }
			    if (obj.size()!=0) {
					   result = new BhResult(200, "操作成功",obj);
					} else {
					   result = BhResult.build(400, "修改数据失败！");
					}
		   }catch(Exception e){
			   e.printStackTrace();
			   result = BhResult.build(500, "数据库搜索失败!");
		   }
		   
		   return result;
	}
	
	//发送验证码
	@RequestMapping("/sendCode")
	@ResponseBody
	public BhResult sendCode(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult result = null;
		try{
			
		     //2018.6.21 zlk
		     String token = request.getHeader("token");
		     JedisUtil jedisUtil= JedisUtil.getInstance();  
		     Strings strings=jedisUtil.new Strings();
		     String userJson = strings.get(token);
		     Map mapOne = JSON.parseObject(userJson, Map.class);
		     Object sId = mapOne.get("shopId");
		     Integer shopId = 1;
		     if(sId!=null){
		    	shopId = (Integer)sId;
		     } 
		      //end
			
			 String  shopOrderNo= map.get("shopOrderNo");
			 if(StringUtils.isEmpty(shopOrderNo)){
				return result = new  BhResult(400,"参数不能为空",null);
			 }
			 String[] arr = shopOrderNo.split(",");
			 for(String no:arr){
				// OrderShop orderShop = service.getOrderById(no);
				 OrderShop orderShop = service.getByOrderNo(no,shopId);//2018.6.21
				 orderShop.setPrintCode(MixCodeUtil.getCode(8));
         	     service.updatePrintByPrimaryKey(orderShop); //生成8位数字验证码
//         	     Sms sms = new Sms();
//         	     sms.setPhoneNo("18933088603"); //领导手机
//         	     sms.setShopName(orderShop.getShopOrderNo());
//         	     sms.setVerification(orderShop.getPrintCode());
//         	     SmsUtil.aliPushPrintCode(sms); //调用短信接口
 	    	     Map<String, Object> map3 =  WXPayUtil.getAccess_Token(Contants.appId, Contants.appSecret);
                 TreeMap<String,TreeMap<String,String>> params = new TreeMap<String,TreeMap<String,String>>(); 
                 Date currentTime = new Date();
                 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 String dateString = formatter.format(currentTime);
                 //根据具体模板参数组装  
                 params.put("first",WechatTemplateMsg.item("打印销售单验证码", "#000000"));  
                 params.put("keyword1",WechatTemplateMsg.item("滨惠商城后台管理系统", "#000000"));  
                 params.put("keyword2",WechatTemplateMsg.item(orderShop.getShopOrderNo(), "#000000"));  
                 params.put("keyword3",WechatTemplateMsg.item(orderShop.getPrintCode(), "#000000"));  
                 params.put("keyword4",WechatTemplateMsg.item(dateString, "#000000"));  
                 params.put("remark",WechatTemplateMsg.item("", "#000000"));  
                 WechatTemplateMsg wechatTemplateMsg = new WechatTemplateMsg();  
//               wechatTemplateMsg.setTemplate_id("JWsnH2yuqSgkz6uBWC4GQ_tyvOxQk5OtdzNAYoI79dU");  //模板ID
                 wechatTemplateMsg.setTemplate_id(WxTeamplate.t4);  //模板ID
                 wechatTemplateMsg.setTouser(Contants.printOpenId);  //openID
                 wechatTemplateMsg.setUrl("https://bh2015.com/binhuiApp/home");  //模板跳转链接
                 wechatTemplateMsg.setData(params);  
                 String data = JsonUtil.toJson(wechatTemplateMsg);
                 TemplateMsgResult templateMsgResult =wtService.sendTemplate(String.valueOf(map3.get("access_token")), data);
			 } 
			result = new  BhResult(200,"发送成功",null);
		}catch(Exception e){
			e.printStackTrace();
			result = new  BhResult(400,"发送失败",null);
		}
		return result;
		
	}
}
