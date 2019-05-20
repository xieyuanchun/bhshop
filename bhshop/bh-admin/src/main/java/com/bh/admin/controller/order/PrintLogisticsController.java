package com.bh.admin.controller.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bh.admin.pojo.order.OrderTeam;
import com.bh.admin.mapper.goods.GoodsOperLogMapper;
import com.bh.admin.mapper.order.BhDictItemMapper;
import com.bh.admin.mapper.order.OrderShopMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.goods.GoodsOperLog;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.order.BhDictItem;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderRefundDoc;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.pojo.user.MemberUserAddress;
import com.bh.admin.pojo.user.WXMSgTemplate;
import com.bh.admin.service.PrintsService;
import com.bh.admin.service.WechatTemplateMsgService;
import com.bh.admin.util.JedisUtil;
import com.bh.admin.util.JedisUtil.Strings;
import com.bh.admin.vo.HttpUtils;
import com.bh.admin.vo.LogisticsOrder;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.CainiaoCloudprintCustomaresGetRequest;
import com.taobao.api.request.CainiaoCloudprintMystdtemplatesGetRequest;
import com.taobao.api.request.CainiaoWaybillIiGetRequest;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.AddressDto;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.Item;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.OrderInfoDto;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.PackageInfoDto;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.TradeOrderInfoDto;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.UserInfoDto;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.WaybillCloudPrintApplyNewRequest;
import com.taobao.api.request.CainiaoWaybillIiSearchRequest;
import com.taobao.api.response.CainiaoCloudprintCustomaresGetResponse;
import com.taobao.api.response.CainiaoCloudprintMystdtemplatesGetResponse;
import com.taobao.api.response.CainiaoWaybillIiGetResponse;
import com.taobao.api.response.CainiaoWaybillIiSearchResponse;

@Controller
@RequestMapping("/printLogistics")
public class PrintLogisticsController {

	@Autowired
	private PrintsService service;
	
	@Autowired 
	private WechatTemplateMsgService wechatTemplateMsgService;
	
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private GoodsOperLogMapper goodsOperLogMapper;
    @Autowired
	private MemberShopMapper memberShopMapper;
    @Autowired
   	private OrderSkuMapper orderSkuMapper;

	
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
	
	//获取物流配送信息的账号
	public static final String AppKey = "24718458";  
	
	//获取物流配送信息的密码
	public static final String AppSecret="1e09db12d95c3070379408d1c5efdc58";
	/**
	 * 获取模板、获取订单详情，物流订单打印接口，获取发货地址
	 * @param map
	 * @return
	 * @throws ApiException
	 */
	@RequestMapping("/getOrderById")
	@ResponseBody
	public BhResult getOrderById(@RequestBody Map<String,String> map, HttpServletRequest request) throws ApiException{
		   BhResult result = null;
		   
		   try{
			   String token = request.getHeader("token");
			   JedisUtil jedisUtil= JedisUtil.getInstance();  
			   JedisUtil.Strings strings=jedisUtil.new Strings();
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
			   
		       String code = map.get("code");
		       String standard_template_id = map.get("standard_template_id"); //快递模板id
		       String standard_template_url = map.get("standard_template_url");//快递模板URL
		       if(StringUtils.isEmpty(code)||StringUtils.isEmpty(standard_template_id)||StringUtils.isEmpty(standard_template_url)){
			    	result = new BhResult(400, "参数不能为空", null);
			    	return result;
			   }

		       StringBuffer urlTemplate = new StringBuffer();
		       urlTemplate.append(standard_template_url);
		       urlTemplate.append("?template_id=");
		       urlTemplate.append(standard_template_id);
		       
//		       System.out.println("前端传过来的url===="+urlTemplate.toString());
		       
		      
		       //1.发送选用的模板URl和商品信息到淘宝云接口，接口返回 快递单号
		       TaobaoClient client2 = new DefaultTaobaoClient(url, appkey, secret);
		       CainiaoWaybillIiGetRequest req2 = new CainiaoWaybillIiGetRequest();
		       WaybillCloudPrintApplyNewRequest obj1 = new WaybillCloudPrintApplyNewRequest();
		       obj1.setCpCode(code);
		       
		       
		       //2.通过search 接口获取发货地址
		       TaobaoClient clientAdd = new DefaultTaobaoClient(url, appkey, secret);
		       CainiaoWaybillIiSearchRequest reqAdd = new CainiaoWaybillIiSearchRequest();
		       reqAdd.setCpCode(code);
		       CainiaoWaybillIiSearchResponse rspAdd = clientAdd.execute(reqAdd, sessionKey);
		       //System.out.println("====================发货地址"+rspAdd.getBody());
		       //解析json字符串
		       JSONArray  jsonArray3 =JSONObject.parseObject(rspAdd.getBody())
		       .getJSONObject("cainiao_waybill_ii_search_response")
		       .getJSONObject("waybill_apply_subscription_cols")
		       .getJSONArray("waybill_apply_subscription_info");
		    
		       String city = "潮州市";
		       String detail = "绿榕南路绿榕楼";
		       String district = "枫溪区";
		       String province = "广东省";
		       if(!code.equals("SF")) {
		         try{
		    	    jsonArray3.isEmpty();
		         }catch(NullPointerException e){
		    	    result = BhResult.build(400, "卖家没有设置地址！",null);
		    	    e.getStackTrace();
		    	    return result;
		         }
		       
		         for(int s =0;s<jsonArray3.size();s++){
		    	   JSONObject jsonObject5 = jsonArray3.getJSONObject(s);
		    	   JSONArray  jsonArray5 =JSONObject.parseObject(jsonObject5.getString("branch_account_cols")).getJSONArray("waybill_branch_account");
		    	   for(int s1=0;s1<jsonArray5.size();s1++){
		    		   JSONObject jsonObject6 = jsonArray5.getJSONObject(s1);
		    		   JSONArray jsonArray7 = JSONObject.parseObject(jsonObject6.getString("shipp_address_cols")).getJSONArray("address_dto");  
		    		   for(int c=0;c<jsonArray7.size();c++){
		    			  JSONObject jsonObject7 = jsonArray7.getJSONObject(c);
		    			  city = jsonObject7.getString("city");
		    			  detail= jsonObject7.getString("detail");
		    			  district= jsonObject7.getString("district");
		    			  province= jsonObject7.getString("province");
		    			  //System.out.println("发货地址："+city+detail+district+province);
		    		   }
		    		}
		         }
		       }
//		       System.out.println("=====================================================");
		       
		       //3.获取订单信息
		       //根据商家订单号 获取oderShop表的信息,改根据订单号获取 信息
		       String ids = map.get("shopOrderNo");
		       if(StringUtils.isEmpty(ids)){
		    	   result = new BhResult(400, "参数不能为空", null);
		    	   return result;
		       }
		  
		       String[] arr=StringUtils.split(ids, ",\r\n ");
		       List<String> listId = new ArrayList<String>();
		       for(int d=0;d<arr.length;d++){
		    	   listId.add(arr[d]);
		       }
		       
		       List<LogisticsOrder> listLogistics = new ArrayList<LogisticsOrder>();
		       List<String> afterSale = new ArrayList<String>(); //申请售后的订单
		       for(String id:listId){

			   OrderShop orderShop = service.getByOrderNo(id,shopId);//2018.6.1
			   //根据order_shop_id获取order_sku表的商品名称

			   List<OrderSku> sku = service.getByOrderShopId(String.valueOf(orderShop.getId()));
			   //一个订单 一个商品 ，判断订单有没有申请退款 0没有，1有
			   int afterSaleNum =0;
			   //一个订单多个商品 的申请售后 的数量
			   int afterNum =0;
			   for(OrderSku ordersku :sku) {
			         List<OrderRefundDoc> ord = service.getOrderRefundDoc(ordersku.getId());
			         if(ord.size()>0) {
			        	if(sku.size()==1) {//一个订单一个商品的情况
			        		afterSaleNum =1;
			        	}
			        	afterSale.add(orderShop.getOrderNo());
			        	afterNum+=1;
			         }
			   }
			   //如果当前订单只有一个商品，并且申请售后，不改状态不打单
			   if(afterSaleNum==1) {
				   continue;
			   }
			   //一个订单多个商品，所有都申请售后，不打印出单不改状态
			   if(afterNum==sku.size()){
				   continue;
			   }
			   
			   //根据订单号获取order_main
			   Order order = service.getOrderMainById(orderShop.getOrderNo());
			    
			   //根据order_main 表的m_addr_id获取 member_user_address表信息
			   MemberUserAddress mua = service.getUserById(order.getmAddrId());
			    
			   //根据order_sku表的sku_id 获取 GoodsSku表的信息			
			   GoodsSku goodsSku = service.getGoodsSku(sku.get(0).getSkuId());

			   //根据order 表的shopId 获取 MemberShop的信息
			   MemberShop memberShop = service.getMemberShopById(orderShop.getShopId());
			   
//			   System.out.println(memberShop.getLinkmanName()+memberShop.getLinkmanPhone());
			   orderShop.setAddress(mua.getAddress());
			   orderShop.setFullName(mua.getFullName());
			   orderShop.setTel(mua.getTel());
			   orderShop.setGoodName(sku.get(0).getGoodsName());
			   orderShop.setSkuNum(sku.get(0).getSkuNo());
			   orderShop.setGoodNum(sku.get(0).getSkuNum());
			   orderShop.setCode(goodsSku.getJdUpc());
		       
		       if(StringUtils.isEmpty(memberShop.getLinkmanName())||StringUtils.isEmpty(memberShop.getLinkmanPhone())){
		    	   result = BhResult.build(400, "发货人名字或者联系方式没设置！");
		    	   return result;
		       }
		       
		       try {
		           if(code.equals("SF")) {
		        
		              if(StringUtils.isNotBlank(orderShop.getExpressNo())&&"顺丰".equals(orderShop.getExpressName())) {
		    	            Map<String,Object> mp = new HashMap<String,Object>();
		    	            mp.put("orderNo", orderShop.getOrderNo());
		    	            mp.put("j_city", city);
		    	            mp.put("j_address", detail);
		    	            mp.put("j_county", district);
		    	            mp.put("j_province", province);
		    	            mp.put("j_contact", memberShop.getLinkmanName());
		    	            mp.put("j_tel", memberShop.getLinkmanPhone());
		    	   
		    	            mp.put("d_city", mua.getCityname());
		    	            mp.put("d_address", mua.getAddress());
		    	            mp.put("d_contact", mua.getFullName());
		    	            mp.put("d_province", mua.getProvname());
		    	            mp.put("d_county", mua.getAreaname());
		    	            mp.put("d_tel", mua.getTel());
		    	            if(StringUtils.isNotBlank(mua.getFourname())) {
		    	            	mp.put("d_four", mua.getFourname());
		    	            }
		                    
		    	            mp.put("code", code);
		    	            //Map<String,Object> m= service.getCode(mp);
		    	            mp.put("expressNo", orderShop.getExpressNo());
		    	            mp.put("templateURL", standard_template_url);
		    	            String taoBao = service.getTaobao(mp);
		                    LogisticsOrder lo = new  LogisticsOrder();
                            lo.setCpCode(code);  
	    	                lo.setCitySend(city);
	    	                lo.setDetailSend(detail);
	    	                lo.setDistrictSend(district);
	    	                //lo.setTownSend(town);
	    	                lo.setProvinceSend(province);
                            lo.setPhoneSend(memberShop.getLinkmanPhone());
	    	                lo.setNameSend(memberShop.getLinkmanName());	   
                            lo.setWaybill_code(order.getExpressNo());
                            lo.setTemplateURL(standard_template_url);
	    	                //lo.setSignature(jsonObject13.getString("signature"));
	                        lo.setOrderNum(orderShop.getOrderNo());
	                        lo.setCityRecipient(mua.getCityname());
	                        lo.setDetailRecipient(orderShop.getAddress());
	                        lo.setDistrictRecipient(mua.getAreaname());
	                        lo.setProvinceRecipient(mua.getProvname());
	                        if (StringUtils.isNotEmpty(mua.getFourname())) {
	    	                    lo.setTownRecipient(mua.getFourname());
		                    }
	                        lo.setNameRecipient(orderShop.getFullName());
	                        lo.setPhoneRecipient(orderShop.getTel());
	                        lo.setTownSend("");
	                        lo.setItemName(orderShop.getGoodName());
	                        lo.setItemCount(orderShop.getGoodNum());
	                        //lo.setTaobaoUserId(obj6.getUserId().toString());
	                        lo.setShopOrderNo(orderShop.getShopOrderNo());
	                        lo.setTaobao(String.valueOf(taoBao));
	                        lo.setOrderId(order.getId()); //2018.5.18
//                          order.setExpressName("顺丰");
//	                        order.setExpressNo(order.getExpressNo());
//	                        service.updateExpressByOrderNo(order);//更改快递公司信息
	                        listLogistics.add(lo);		       
		    	            continue;
		              }else {
		    	            Map<String,Object> mp = new HashMap<String,Object>();
		    	            mp.put("orderNo", orderShop.getOrderNo());
		    	            mp.put("j_city", city);
		    	            mp.put("j_address", detail);
		    	            mp.put("j_county", district);
		    	            mp.put("j_province", province);
		    	            mp.put("j_contact", memberShop.getLinkmanName());
		    	            mp.put("j_tel", memberShop.getLinkmanPhone());
		    	            
		    	            mp.put("d_city", mua.getCityname());
		    	            mp.put("d_address", mua.getAddress());
		    	            mp.put("d_contact", mua.getFullName());
		    	            mp.put("d_province", mua.getProvname());
		    	            mp.put("d_county", mua.getAreaname());
		    	            mp.put("d_tel", mua.getTel());
		                    if(StringUtils.isNotBlank(mua.getFourname())) {
		    	            	mp.put("d_four", mua.getFourname());
		    	            }
//		                    System.out.println("sql5"+" 寄件方city: "+city +" " +" 寄件方的手机: "+memberShop.getLinkmanPhone()
//		                    +" 收件方city: " +mua.getCityname()+" "+" 收件方手机: "+mua.getTel());
		    	            mp.put("code", code);
		    	            Map<String,Object> m= service.getCode(mp);
		    	            mp.put("expressNo", m.get("expressNo"));
		    	            mp.put("templateURL", standard_template_url);
		    	            String taoBao = service.getTaobao(mp);
		                    LogisticsOrder lo = new  LogisticsOrder();
                            lo.setCpCode(code);  
	    	                lo.setCitySend(city);
	    	                lo.setDetailSend(detail);
	    	                lo.setDistrictSend(district);
	    	                //lo.setTownSend(town);
	    	                lo.setProvinceSend(province);
                            lo.setPhoneSend(memberShop.getLinkmanPhone());
	    	                lo.setNameSend(memberShop.getLinkmanName());	   
                            lo.setWaybill_code(m.get("expressNo").toString());
                            lo.setTemplateURL(standard_template_url);
	    	                //lo.setSignature(jsonObject13.getString("signature"));
	                        lo.setOrderNum(orderShop.getOrderNo());
	                        lo.setCityRecipient(mua.getCityname());
	                        lo.setDetailRecipient(orderShop.getAddress());
	                        lo.setDistrictRecipient(mua.getAreaname());
	                        lo.setProvinceRecipient(mua.getProvname());
	                        if (StringUtils.isNotEmpty(mua.getFourname())) {
	    	                    lo.setTownRecipient(mua.getFourname());
		                    }
	                        lo.setNameRecipient(orderShop.getFullName());
	                        lo.setPhoneRecipient(orderShop.getTel());
	                        lo.setTownSend("");
	                        lo.setItemName(orderShop.getGoodName());
	                        lo.setItemCount(orderShop.getGoodNum());
	                        //lo.setTaobaoUserId(obj6.getUserId().toString());
	                        lo.setShopOrderNo(orderShop.getShopOrderNo());
	                        lo.setTaobao(String.valueOf(taoBao));
	                        lo.setOrderId(order.getId()); //2018.5.18
                            order.setExpressName("顺丰");
	                        order.setExpressNo(m.get("expressNo").toString());
	                       
	                        listLogistics.add(lo);		       
		    	            continue;
		        	  
		               }
		          }
		       }catch(Exception e) {
		    	  e.printStackTrace();
		    	  return result = BhResult.build(400, "重复下单！",null);
		       }
		       
		       UserInfoDto obj2 = new UserInfoDto(); //发货人信息
		       AddressDto obj3 = new AddressDto();   //发货地址 通过search接口获取
		       obj3.setCity(city);          //城市
		       obj3.setDetail(detail);      //详细地址
		       obj3.setDistrict(district);  //区
		       obj3.setProvince(province);  //省份
		      // obj3.setTown("望京街道");
		       obj2.setAddress(obj3);
		       obj2.setMobile(memberShop.getLinkmanPhone());
		       obj2.setName(memberShop.getLinkmanName());   //姓名
		       //obj2.setPhone(memberShop.getLinkmanPhone());
		       obj1.setSender(obj2);
		
		       //请求面单信息
		       List<TradeOrderInfoDto> list5 = new ArrayList<TradeOrderInfoDto>();
		       TradeOrderInfoDto obj6 = new TradeOrderInfoDto(); 
		       //obj6.setLogisticsServices("如不需要特殊服务，该值为空");
		       //请求ID,与业务字段无关，在批量调用时，需要保证每个对象的 objectid不同，在获取到返回值后，可以通过比对出参中的objectId,可以得到与入参的对应关系。只需要在一次请求中保证不同即可。可以用索引下标代替
		       obj6.setObjectId("1");
	           
		       List<String> listOrderNum = new ArrayList<String>();
		       listOrderNum.add(orderShop.getOrderNo());   //订单号放进List
		       
		       //订单信息
		       OrderInfoDto obj8 = new OrderInfoDto();     
		       obj8.setOrderChannelsType("OTHERS");         //订单渠道平台编码
		       obj8.setTradeOrderList(listOrderNum);       //订单号，数量限制一百
		       //list7 
		       obj6.setOrderInfo(obj8);
	           
	           //包裹信息  list12
		       PackageInfoDto obj10 = new PackageInfoDto();   
		       obj10.setId("1");
		      
		       List<Item> list12 = new ArrayList<Item>();     
		       
		       //商品信息
		       Item obj13 = new Item(); 
		       obj13.setCount(Integer.valueOf(orderShop.getGoodNum()).longValue());        //商品数量
		       obj13.setName(orderShop.getGoodName());      //商品名字
		       list12.add(obj13);
		       obj10.setItems(list12);
		       obj10.setVolume(1L);
		       obj10.setWeight(1L);
		       
		       //list9
		       obj6.setPackageInfo(obj10);
		     
		       //收件人信息
		       UserInfoDto obj15 = new UserInfoDto();  
		       AddressDto obj16 = new AddressDto();     //地址
		       obj16.setCity(mua.getCityname());
		       obj16.setDetail(mua.getAddress());       //详细地址
		       obj16.setDistrict(mua.getAreaname());
		       obj16.setProvince(mua.getProvname());    //省份
		       if(StringUtils.isNotBlank(mua.getFourname())) {
		          obj16.setTown(mua.getFourname());
		       }
		       obj15.setAddress(obj16);
		       //obj15.setMobile(orderShop.getTel());
		       obj15.setName(orderShop.getFullName());  //姓名
		       obj15.setPhone(orderShop.getTel());
		       //list14
		       obj6.setRecipient(obj15);
		      
		       //4.获取快递单号，标准模板模板URL+模板id
		       obj6.setTemplateUrl(urlTemplate.toString());      
		       obj6.setUserId(2911781131L);           //使用者ID 换取access_token时，同时返回的taobao_user_id 就是卖家id。
		       list5.add(obj6);
		       obj1.setTradeOrderInfoDtos(list5);
		       obj1.setStoreCode("553323");
		       obj1.setResourceCode("DISTRIBUTOR_978324");
		       obj1.setDmsSorting(false);
		       req2.setParamWaybillCloudPrintApplyNewRequest(obj1);
		       CainiaoWaybillIiGetResponse rsp2 = client2.execute(req2, sessionKey);
		       
//		       System.out.println("=====================================================");
//		       System.out.println("返回的物流账号"+rsp2.getBody());
		       //5.把订单信息、快递单号保存到实体类里发到前端
		       LogisticsOrder lo = new  LogisticsOrder();
		       
		       JSONObject  jsonObject =JSONObject.parseObject(rsp2.getBody())
		       .getJSONObject("cainiao_waybill_ii_get_response");
		       if(jsonObject==null) {
		    	   
		    	   Object jsonError =JSONObject.parseObject(rsp2.getBody())
		    		       .getJSONObject("error_response").get("sub_msg");
		    	   return result = BhResult.build(400, jsonError.toString(),null);
		       }
		       JSONArray  jsonArray9 = jsonObject.getJSONObject("modules")
		    		   .getJSONArray("waybill_cloud_print_response");
		       //解析json字符串
//		       JSONArray  jsonArray9 =JSONObject.parseObject(rsp2.getBody())
//		       .getJSONObject("cainiao_waybill_ii_get_response")
//		       .getJSONObject("modules")
//		       .getJSONArray("waybill_cloud_print_response");
		      
      
		    	
		       //获取request_id 
		       JSONObject  jsonObject18 =JSONObject.parseObject(rsp2.getBody())
			   .getJSONObject("cainiao_waybill_ii_get_response");
		       lo.setRequest_id(jsonObject18.getString("request_id"));
		       
		       for(int b=0;b<jsonArray9.size();b++){
		    	   //物流公司代号
		    	   JSONObject jsonObject9 = JSONObject.parseObject(jsonArray9.getJSONObject(b)
		    	   .getString("print_data"))
		    	   .getJSONObject("data");
		    	   lo.setCpCode(jsonObject9.getString("cpCode"));  
		    	   //寄件人地址
		    	   JSONObject jsonObject11 =  JSONObject.parseObject(jsonArray9.getJSONObject(b)
		    	   .getString("print_data"))
		    	   .getJSONObject("data")
		    	   .getJSONObject("sender")
		    	   .getJSONObject("address");
		    	   
		    	   lo.setCitySend(jsonObject11.getString("city"));
		    	   lo.setDetailSend(jsonObject11.getString("detail"));
		    	   lo.setDistrictSend(jsonObject11.getString("district"));
		    	   lo.setTownSend(jsonObject11.getString("town"));
		    	   lo.setProvinceSend(jsonObject11.getString("province"));
		    	   
		    	   //寄件人联系方式和名称
		    	   JSONObject jsonObject15 =  JSONObject.parseObject(jsonArray9.getJSONObject(b)
		    	   .getString("print_data"))
		    	   .getJSONObject("data")
		    	   .getJSONObject("sender");
		    	   lo.setPhoneSend(jsonObject15.getString("mobile"));
		    	   lo.setNameSend(jsonObject15.getString("name"));	   
		    	  
		    	   //快递单号
		    	   JSONObject jsonObject12 =  JSONObject.parseObject(jsonArray9.getJSONObject(b)
		    	   .getString("print_data"))
		    	   .getJSONObject("data");
		           lo.setWaybill_code(jsonObject12.getString("waybillCode"));
		    	  
		           //快递模板
		           JSONObject jsonObject13 =  JSONObject.parseObject(jsonArray9.getJSONObject(b)
		    	   .getString("print_data"));
		           lo.setTemplateURL(jsonObject13.getString("templateURL"));
		    	   lo.setSignature(jsonObject13.getString("signature"));
		       }
		       lo.setOrderNum(orderShop.getOrderNo());
		       lo.setCityRecipient(mua.getCityname());
		       lo.setDetailRecipient(orderShop.getAddress());
		       lo.setDistrictRecipient(mua.getAreaname());
		       lo.setProvinceRecipient(mua.getProvname());
		       if (StringUtils.isNotEmpty(mua.getFourname())) {
		    	   lo.setTownRecipient(mua.getFourname());
			   }
		      
		       lo.setNameRecipient(orderShop.getFullName());
		       lo.setPhoneRecipient(orderShop.getTel());
		       lo.setTownSend("");
		       lo.setItemName(orderShop.getGoodName());
		       lo.setItemCount(orderShop.getGoodNum());
		       lo.setTaobaoUserId(obj6.getUserId().toString());
		       lo.setTaobao(rsp2.getBody());
		       lo.setPrintState("已发货");
		       lo.setShopOrderNo(orderShop.getShopOrderNo());
		       lo.setOrderId(order.getId()); //2018.5.18
		       listLogistics.add(lo);
		       }
		       if (listLogistics.size()>0) {	    	 
		    	   listLogistics.get(0).setOrderNoList(afterSale);
			       result = new BhResult(200, "操作成功", listLogistics);
			   } else {
			       result = BhResult.build(300, "订单可能申请退款！",afterSale);
			   }
		   }catch(NullPointerException e){
			   result = BhResult.build(400, "可能是电子面单余额不足,或者卖家没有设置地址！");
			   e.printStackTrace();
		   }catch(Exception e){
			   e.printStackTrace();
		   }
		   return result;
	}	
	
	
	/**
	 * 获取商家的所有模板返回前端
	 * @return
	 */
	@RequestMapping("/getTemplate")
	@ResponseBody
	public BhResult getTemplate(){
		   BhResult  result = null;
		   try{
			   TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
			   CainiaoCloudprintMystdtemplatesGetRequest req = new CainiaoCloudprintMystdtemplatesGetRequest();
			   CainiaoCloudprintMystdtemplatesGetResponse rsp = client.execute(req, sessionKey);
			  
		       if (rsp.getBody()!=null) {
			       result = new BhResult(200, "操作成功", rsp.getBody());
			   } else {
			       result = BhResult.build(400, "暂无数据！");
			   }
		   }catch(Exception e){
			   e.printStackTrace();
		   }
		   return result;
	}
	
	/**
	 * 获取自定义请求模板 
	 * @param map
	 * @return
	 */
	@RequestMapping("/getMyTemplate")
	@ResponseBody
	public BhResult getMyTemplate(@RequestBody Map<String,String> map){
		   BhResult  result = null;
		   try{
			   String tempalte_id = map.get("tempalte_id");
			   if(StringUtils.isEmpty(tempalte_id)){
				   result = BhResult.build(400, "请求参数不能为空！");
			   }
			   TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
			   CainiaoCloudprintCustomaresGetRequest req = new CainiaoCloudprintCustomaresGetRequest();
			   req.setTemplateId(Long.parseLong(tempalte_id));
			   CainiaoCloudprintCustomaresGetResponse rsp = client.execute(req, sessionKey);
//			   System.out.println(rsp.getBody());
		       if (rsp.getBody()!=null) {
			       result = new BhResult(200, "操作成功", rsp.getBody());
			   } else {
			       result = BhResult.build(400, "暂无数据！");
			   }
		   }catch(Exception e){
			   e.printStackTrace();
		   }
		   return result;
	}
	
	
	/**
	 * 打印后更改订单状态、把物流单号和物流公司保存到订单
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/updateOrderShopById")
	@ResponseBody
	public BhResult updateOrderShopById(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Object id = mapOne.get("userId");
			Integer userId=1;
			if (id != null) {
				userId = (Integer) id;
			}
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			List<Object> obj = new ArrayList<Object>();

			String expressName = map.get("expressName"); // 物流公司

			String expressNo = map.get("expressNo"); // 物流单号

			String idS = map.get("shopOrderNo"); // 商家订单号

			String[] expressNameList;
			if (expressName.contains(",")) {
				expressNameList = expressName.split(",");
			} else {
				expressNameList = expressName.split("\n");
			}

			String[] expressNoList;
			if (expressNo.contains(",")) {
				expressNoList = expressNo.split(",");
			} else {
				expressNoList = expressNo.split("\n");
			}

			String[] arr;
			if (idS.contains(",")) {
				arr = idS.split(",");
			} else {
				arr = idS.split("\n");
			}

			if (StringUtils.isEmpty(idS) && StringUtils.isEmpty(expressName) && StringUtils.isEmpty(expressNo)) {
				result = new BhResult(400, "参数不能为空", null);
			}

			for (int i = 0; i < arr.length; i++) {

				OrderShop orderShop = service.getByOrderNo(arr[i], shopId);// 2018.6.4
				orderShop.setStatus(Contants.shopStatu3); // 已发货
				orderShop.setDeliveryWay(3); // 其他物流
				orderShop.setSendTime(new Date()); // 打印时间为发货时间

				WXMSgTemplate template = new WXMSgTemplate();
				template.setOrderShopId(orderShop.getId() + "");
				wechatTemplateMsgService.sendGroupGoodTemplate(template);

				Order order = new Order();
				order.setDeliveryStatus(1); // 已发货
				order.setOrderNo(orderShop.getOrderNo()); // 平台订单号
				service.updateExpressByOrderNo(order);

				orderShop.setShopOrderNo(orderShop.getShopOrderNo());
				if (expressNameList[i].equals("YTO")) {
					orderShop.setExpressName("圆通快递");
				} else if (expressNameList[i].equals("ZTO")) {
					orderShop.setExpressName("中通快递");
				} else if (expressNameList[i].equals("POSTB")) {
					orderShop.setExpressName("邮政包裹");
				} else if (expressNameList[i].equals("SF")) {
					orderShop.setExpressName("顺丰快递");
				}
				orderShop.setExpressNo(expressNoList[i]);
				int row=orderShopMapper.updateByPrimaryKeySelective(orderShop);
				obj.add(row);
			}
			if (obj.size() != 0) {
				String[] orderNo = StringUtils.split(idS, ",\r\n ");
				GoodsOperLog goodsOperLog = new GoodsOperLog();
				StringBuffer sb = new StringBuffer();
				StringBuffer sb1 = new StringBuffer();
				for (int i = 0; i < orderNo.length; i++) {
					List<OrderShop> orderShopList = orderShopMapper.selectByOrderNos(orderNo[i], shopId.toString());
					for (int j = 0; j < orderShopList.size(); j++) {
						sb.append(orderShopList.get(j).getId() + ",");
					}
				}
				String orderNos = sb.substring(0, sb.length() - 1);
				String[] orderNo1 = orderNos.split(",");
				for (int i = 0; i < orderNo1.length; i++) {
					List<OrderSku> orderSkuList = orderSkuMapper.selectByGoodsId(Integer.parseInt(orderNo1[i]),
							shopId);
					for (int j = 0; j < orderSkuList.size(); j++) {
						sb1.append(orderSkuList.get(j).getGoodsId() + ",");
					}
				}
				String goodsIds = sb1.substring(0, sb1.length() - 1);
				goodsOperLog.setOpType("打印快递单");
				goodsOperLog.setGoodId(goodsIds);
				goodsOperLog.setOrderId(orderNos);
				goodsOperLog.setUserId(userId.toString());
				goodsOperLog.setOpTime(new Date());
				String userName = memberShopMapper.selectUsernameBymId(userId); // 查找操作人
				goodsOperLog.setAdminUser(userName);
				goodsOperLogMapper.insertSelective(goodsOperLog);
				result = new BhResult(200, "操作成功", obj);
			} else {
				result = BhResult.build(400, "修改数据失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库搜索失败!");
		}
		return result;
	}

    /**
     * 更改订单物流信息 (商家自配)，商家可能要重新发货，换其他的快递,把 order_main 的快递号和公司修改了
     * @param map
     * @return
     */
    @RequestMapping("/updateOrderByOrderNo")
    @ResponseBody
    public BhResult updateOrderByOrderNo(@RequestBody Map<String, String> map,HttpServletRequest request) {
		   BhResult result = null;
		   try {
			    //2018.6.21
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
			    //end
			    String shopOrderNo = map.get("shopOrderNo");
			    String expressName = map.get("expressName");
			    String expressNo = map.get("expressNo");
			    if(StringUtils.isEmpty(shopOrderNo)||StringUtils.isEmpty(expressName)||StringUtils.isEmpty(expressNo)){
			    	result = new BhResult(400, "参数不能为空", null);
			    }else{
			    	  OrderShop orderShop = service.getOrderById(shopOrderNo,shopId);
			    	  orderShop.setStatus(Contants.shopStatu3);      //已发货
			    	  orderShop.setDeliveryWay(3); //其他物流
			    	  orderShop.setSendTime(new Date());
			    	  //更改order 的快递信息
			          /*Order order = new Order();
			          order.setOrderNo(orderShop.getOrderNo());
			          order.setExpressName(expressName);
			          order.setExpressNo(expressNo);*/
			    	  /*OrderShop orderShop1=new OrderShop();
			    	  orderShop1.setShopOrderNo(orderShop.getShopOrderNo());*/
			    	  orderShop.setExpressName(expressName);
			    	  orderShop.setExpressNo(expressNo);
			    			   
			    	  
			          
			          try{
			        	  //程凤云
//			        	  List<OrderTeam> teamList = service.selectOrderTeam(orderShop.getOrderNo());
//					      if (teamList.size()>0) {
					    	  WXMSgTemplate template = new WXMSgTemplate();
					    	  template.setOrderShopId(orderShop.getId()+"");
					    	  wechatTemplateMsgService.sendGroupGoodTemplate(template);
//						  }
			        	  
			              //service.updateExpressByOrderNo(order);
			              //service.updateExpressByOrderNo1(orderShop1);
			              service.updateByPrimaryKey(orderShop);
			              //返回给前端的快递公司和单号和当前时间
			              orderShop.setExpressName(expressName);
			              orderShop.setExpressNo(expressNo);
			              orderShop.setSendTime(new Date());
			              result = new BhResult(200, "操作成功",orderShop);
			          }catch(Exception e){
			    	      result = BhResult.build(400, "修改数据失败！");
			    	      e.printStackTrace();
			          }
			    }
		   }catch(Exception e){
			   e.printStackTrace();
			   result = BhResult.build(500, "数据库搜索失败!");
		   }
		   
		   return result;
	}
    
    
    /**
     * 获取物流配送信息
     */
    @RequestMapping("/getLogistics")
    @ResponseBody
    public BhResult getLogistics(@RequestBody Map<String, String> map){
    	   BhResult result = null;
    	   String expressNo = map.get("expressNo");
    	   if(StringUtils.isEmpty(expressNo)){
    		   result = new BhResult(400,"参数不能为空",null);
    		   return result;
    	   }
    	   
    	   String host = "https://wuliu.market.alicloudapi.com";
   	       String path = "/kdi";
   	       String method = "GET";
   	       String appcode = "232d013ef8244587a9a4f69cb2fcca47";
   	       Map<String, String> headers = new HashMap<String, String>();
   	       //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
   	       headers.put("Authorization", "APPCODE " + appcode);
   	       Map<String, String> querys = new HashMap<String, String>();
   	       querys.put("no", expressNo);
   	       //querys.put("type", "zto");   //可不填

   	       String Logistics2 =null;
   	       try{
   	    	/**
   	    	* 重要提示如下:
   	    	* HttpUtils请从
   	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
   	    	* 下载
   	    	*
   	    	* 相应的依赖请参照
   	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
   	    	*/
   	    	   HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
   	           //System.out.println("================================="+response.toString());
   	         	//获取response的body
   	    	   String Logistics = EntityUtils.toString(response.getEntity());
   	    	   System.out.println("================================="+Logistics);
   	    	   JSONObject jsonObject = JSONObject.parseObject(Logistics);
   	    	   System.out.println("================================="+jsonObject);
   	    	   Logistics2 = jsonObject.getString("result");
   	    	   if(!StringUtils.isEmpty(Logistics2)){
   	    		 result = new BhResult(200,"获取信息成功",jsonObject);
   	    	   }else{
   	    		 result = new BhResult(400,"获取信息失败",null);
   	    	   }
   	        }catch(Exception e) {
   	           result = new BhResult(400,"操作异常",null);
   	    	   e.printStackTrace();
   	        }   
    	   
           return result;
    }
    
    /**
     * 商家自配 按钮 更改订单状态为  已发货
     * @param map
     * @return
     */
    @RequestMapping("/updateByOrderNo")
    @ResponseBody
    public BhResult updateByOrderNo(@RequestBody Map<String, String> map,HttpServletRequest request) {
		   BhResult result = null;
		   try {
			   
			    //2018.6.21
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
			    //end
			   
			   
			    String shopOrderNo = map.get("shopOrderNo");
	
			    if(StringUtils.isEmpty(shopOrderNo)){
			    	result = new BhResult(400, "参数不能为空", null);
			    }else{
			    	  OrderShop orderShop = service.getOrderById(shopOrderNo,shopId);
			    	  orderShop.setStatus(Contants.shopStatu3);      //已发货
			    	  orderShop.setSendTime(new Date()); //发货时间
			          Order order = new Order();
			          order.setOrderNo(orderShop.getOrderNo());
			          order.setDeliveryStatus(1);
			          try{
			        	  //程凤云 2018-4-11添加
//			        	  List<OrderTeam> teamList = service.selectOrderTeam(orderShop.getOrderNo());
//					      if (teamList.size()>0) {
					    	  WXMSgTemplate template = new WXMSgTemplate();
					    	  template.setOrderShopId(orderShop.getId()+"");
					    	  wechatTemplateMsgService.sendGroupGoodTemplate(template);
//						  }
			              service.updateExpressByOrderNo(order);
			              service.updateByPrimaryKey(orderShop);
			              result = new BhResult(200, "操作成功",1);
			          }catch(Exception e){
			    	      result = BhResult.build(400, "修改数据失败！");
			    	      e.printStackTrace();
			          }
			    }
		   }catch(Exception e){
			   e.printStackTrace();
			   result = BhResult.build(500, "数据库搜索失败!");
		   }
		   
		   return result;
	}
    
    /**
     * 点击 配送完成  按钮 更改订单 dState状态 为已送达
     * @param map
     * @return
     */
    @RequestMapping("/updateDstateByOrderNo")
    @ResponseBody
    public BhResult updateDstateByOrderNo(@RequestBody Map<String, String> map,HttpServletRequest request) {
		   BhResult result = null;
		   try {
			    //2018.6.21
			    String token = request.getHeader("token");
			    JedisUtil jedisUtil= JedisUtil.getInstance();  
			    JedisUtil.Strings strings=jedisUtil.new Strings();
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
			   
			    String shopOrderNo = map.get("shopOrderNo");
	
			    if(StringUtils.isEmpty(shopOrderNo)){
			    	result = new BhResult(400, "参数不能为空", null);
			    }else{
			    	  OrderShop orderShop = service.getOrderById(shopOrderNo,shopId);
			    	  orderShop.setdState(4);  //已送达
			    	  orderShop.setReceivedtime(new Date()); //收货时间
			          try{
			        	    StringBuffer sb=new StringBuffer();
							GoodsOperLog goodsOperLog = new GoodsOperLog();
							OrderShop orderShops= orderShopMapper.selectByShopOrderNo(shopOrderNo);
							List<OrderSku> orderSkuList= orderSkuMapper.selectByGoodsId(orderShops.getId(),shopId);
							for (int j = 0; j < orderSkuList.size(); j++) {
						    	 sb.append(orderSkuList.get(j).getGoodsId()+",");
							}
							String goodsIds=sb.substring(0, sb.length()-1);
							goodsOperLog.setGoodId(goodsIds);
							goodsOperLog.setOpType("配送完成");
							goodsOperLog.setOrderId(orderShops.getId().toString());
							goodsOperLog.setUserId(userId.toString());
							goodsOperLog.setOpTime(new Date());
							String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
							goodsOperLog.setAdminUser(userName);
							goodsOperLogMapper.insertSelective(goodsOperLog);
			              service.updateByPrimaryKey(orderShop);
			              result = new BhResult(200, "操作成功",1);
			          }catch(Exception e){
			    	      result = BhResult.build(400, "修改数据失败！");
			    	      e.printStackTrace();
			          }
			    }
		   }catch(Exception e){
			   e.printStackTrace();
			   result = BhResult.build(500, "数据库搜索失败!");
		   }
		   
		   return result;
	}
	/**
	 * @Description:快递公司信息查询     
	 * @author xieyc
	 * @date 2018年9月12日 下午5:44:50
	 *
	 */
	@RequestMapping("/getExpressInfo")
	@ResponseBody
	public BhResult getExpressInfo() {
		BhResult r = null;
		try {
			List<BhDictItem>list=service.getExpressInfo();
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(list);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			e.printStackTrace();
		}
		return r;
	}
    
}