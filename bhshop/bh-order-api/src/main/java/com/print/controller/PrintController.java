package com.print.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.bean.Sms;
import com.bh.config.Contants;
import com.bh.config.WxTeamplate;
import com.bh.goods.pojo.GoodsSku;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.result.BhResult;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.LoggerUtil;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.SmsUtil;
import com.bh.utils.pay.WXPayUtil;
import com.bh.wxpayjsapi.common.JsonUtil;
import com.ctc.wstx.util.StringUtil;
import com.print.service.PrintsService;
import com.wechat.service.WechatTemplateMsgService;
import com.wechat.vo.TemplateMsgResult;
import com.wechat.vo.WechatTemplateMsg;


@Controller
@RequestMapping("/print")
public class PrintController {

	@Autowired
	private PrintsService service;
	@Autowired
	WechatTemplateMsgService wtService;
	//获取订单详情,商家订单批量打印销售单接口，有验证是否打印过
	@RequestMapping("/selectById")
	@ResponseBody
	public BhResult selectById(@RequestBody Map<String, String> map) {
		   BhResult result = null;
		   try {
			    
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
			    
			    for(String id:list){
			         //根据商家订单号 获取oderShop表的信息
			         OrderShop orderShop = service.getOrderById(id);

			         if(orderShop.getPrintCount()>0){
			    	     list2.add(orderShop.getShopOrderNo());  //把打印过得订单的 商家订单号保存在集合
			         }
			         
			         
			    	//根据order_shop_id获取order_sku表的商品名称
//			        List<OrderSku> sku = service.getByOrderShopId(id);
			        List<OrderSku> sku = service.getByOrderShopId(String.valueOf(orderShop.getId()));
			        //根据订单号获取order_main
			        Order order = service.getOrderMainById(orderShop.getOrderNo());
			    
			        //根据order_main 表的m_addr_id获取 member_user_address表信息
			        MemberUserAddress mua = service.getUserById(order.getmAddrId());
			     
			        //根据order_sku表的sku_id 获取 GoodsSku表的信息			
			        GoodsSku goodsSku = service.getGoodsSku(sku.get(0).getSkuId());

			        orderShop.setAddress(mua.getProvname()+mua.getCityname()+mua.getAreaname()+mua.getAddress());
			        orderShop.setFullName(mua.getFullName());
			        orderShop.setTel(mua.getTel());
			        
			        orderShop.setGoodName(sku.get(0).getGoodsName()
					        +" "+goodsSku.getKeyOne()+" "+goodsSku.getValueOne());
			        
			        if(StringUtils.isNotBlank(goodsSku.getKeyTwo())){
			        	orderShop.setGoodName(sku.get(0).getGoodsName()
						        +" "+goodsSku.getKeyOne()+" "+goodsSku.getValueOne()
						        +" "+goodsSku.getKeyTwo()+" "+goodsSku.getValueTwo());
			        }else if(StringUtils.isNotBlank(goodsSku.getKeyThree())){
			        	orderShop.setGoodName(sku.get(0).getGoodsName()
			 			        +" "+goodsSku.getKeyOne()+" "+goodsSku.getValueOne()
			 			        +" "+goodsSku.getKeyTwo()+" "+goodsSku.getValueTwo()	
			 			        +" "+goodsSku.getKeyThree()+" "+goodsSku.getValueThree());
			        }
			      
			        orderShop.setSkuNum(sku.get(0).getSkuNo());
			        orderShop.setGoodNum(sku.get(0).getSkuNum());
			        orderShop.setCode(goodsSku.getJdUpc());
			        orderShop.setPrintState("备货中");
			        listShop.add(orderShop);
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
					   result = new BhResult(200, "操作成功", listShop);
					} else {
					   result = BhResult.build(400, "暂无数据！");
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
	public BhResult updateOrderShopById(@RequestBody Map<String, String> map) {
		   BhResult result = null;
		   try {
			    List<Object> obj = new ArrayList<Object>();
			    String idS = map.get("shopOrderNo");
			    if(StringUtils.isEmpty(idS)){
			    	result = new BhResult(400, "参数不能为空", null);
			    }
			    String[] arr = idS.split(",");
			    for(String id :arr){
			    	OrderShop orderShop = service.getOrderById(id);
			    	if(orderShop.getJdorderid().equals("0")){ //京东的订单不改状态
			    	    orderShop.setStatus(9);
				        int row = service.updatePrintByPrimaryKey(orderShop);
			    	    obj.add(row);
			    	}
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
	public BhResult updatePrintCount(@RequestBody Map<String, String> map) {
		   BhResult result = null;
		   try {
			    List<Object> obj = new ArrayList<Object>();
			    String idS = map.get("shopOrderNo");
			    if(StringUtils.isEmpty(idS)){
			    	result = new BhResult(400, "参数不能为空", null);
			    }
			    String[] arr = idS.split(",");
			    for(String id :arr){
			    	OrderShop orderShop = service.getOrderById(id);
				    orderShop.setPrintCount(orderShop.getPrintCount()+1);
				    int row = service.updatePrintByPrimaryKey(orderShop);
				    OrderShop orderShop2 = service.getOrderById(id);
				    
			    	obj.add(orderShop2.getPrintCount());
			    }
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
	public BhResult sendCode(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try{
			 String  shopOrderNo= map.get("shopOrderNo");
			 if(StringUtils.isEmpty(shopOrderNo)){
				return result = new  BhResult(400,"参数不能为空",null);
			 }
			 String[] arr = shopOrderNo.split(",");
			 for(String no:arr){
				 OrderShop orderShop = service.getOrderById(no);
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
   //            wechatTemplateMsg.setTemplate_id("JWsnH2yuqSgkz6uBWC4GQ_tyvOxQk5OtdzNAYoI79dU");  //模板ID
                 wechatTemplateMsg.setTemplate_id(WxTeamplate.t4);  //模板ID 
                 wechatTemplateMsg.setTouser("oj9MyxAHaJMMaL8b5MfexVNyPHPU");  //openID
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
