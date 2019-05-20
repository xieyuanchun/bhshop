package com.print.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.bh.config.Contants;
import com.bh.goods.pojo.GoodsSku;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.goods.PageNum;
import com.bh.order.mapper.OrderTeamMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.order.pojo.OrderTeam;
import com.bh.result.BhResult;
import com.bh.user.mapper.WXMSgTemplate;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.wxpayjsapi.common.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.print.service.PrintsService;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.CainiaoCloudprintCustomaresGetRequest;
import com.taobao.api.request.CainiaoCloudprintMystdtemplatesGetRequest;
import com.taobao.api.request.CainiaoCloudprintStdtemplatesGetRequest;
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
import com.taobao.api.response.CainiaoCloudprintStdtemplatesGetResponse;
import com.taobao.api.response.CainiaoWaybillIiGetResponse;
import com.taobao.api.response.CainiaoWaybillIiSearchResponse;
import com.wechat.service.WechatTemplateMsgService;

@Controller
@RequestMapping("/printLogistics")
public class PrintLogisticsController {

	@Autowired
	private PrintsService service;
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	@Autowired
	private WechatTemplateMsgService wechatTemplateMsgService;
	
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
	public BhResult getOrderById(@RequestBody Map<String,String> map) throws ApiException{
		   BhResult result = null;
		   
		   try{
			   
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
//		       System.out.println("====================发货地址"+rspAdd.getBody());
		       //解析json字符串
		       JSONArray  jsonArray3 =JSONObject.parseObject(rspAdd.getBody())
		       .getJSONObject("cainiao_waybill_ii_search_response")
		       .getJSONObject("waybill_apply_subscription_cols")
		       .getJSONArray("waybill_apply_subscription_info");
		    
		       String city = null;
		       String detail = null;
		       String district = null;
		       String province = null;
		       
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
//		    			  System.out.println("发货地址："+city+detail+district+province);
		    		   }
		    		}
		       }
		        
//		       System.out.println("=====================================================");
		       
		       //3.获取订单信息
		       //根据商家订单号 获取oderShop表的信息,改根据订单号获取 信息
//		       String ids = map.get("id");
		       String ids = map.get("shopOrderNo");
		       if(StringUtils.isEmpty(ids)){
		    	   result = new BhResult(400, "参数不能为空", null);
		    	   return result;
		       }
		       String[] arr = ids.split(",");
		       List<String> listId = new ArrayList<String>();
		       for(int d=0;d<arr.length;d++){
		    	   listId.add(arr[d]);
		       }
		       
		       List<LogisticsOrder> listLogistics = new ArrayList<LogisticsOrder>();
		       for(String id:listId){
			   OrderShop orderShop = service.getOrderById(id);
			    
			   //根据order_shop_id获取order_sku表的商品名称
//			   List<OrderSku> sku = service.getByOrderShopId(id);
			   List<OrderSku> sku = service.getByOrderShopId(String.valueOf(orderShop.getId()));	    
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
		    	   
		      
		       UserInfoDto obj2 = new UserInfoDto(); //发货人信息
		       AddressDto obj3 = new AddressDto();   //发货地址 通过search接口获取
		       obj3.setCity(city);
		       obj3.setDetail(detail);      //详细地址
		       obj3.setDistrict(district);
		       obj3.setProvince(province);  //省份
		      // obj3.setTown("望京街道");
		       obj2.setAddress(obj3);
		       obj2.setMobile(memberShop.getLinkmanPhone());
		       obj2.setName(memberShop.getLinkmanName());   //姓名
		       obj2.setPhone(memberShop.getLinkmanPhone());
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
		     //  obj16.setTown("望京街道");
		       obj15.setAddress(obj16);
		       obj15.setMobile(orderShop.getTel());
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
		       
		       //解析json字符串
		       JSONArray  jsonArray9 =JSONObject.parseObject(rsp2.getBody())
		       .getJSONObject("cainiao_waybill_ii_get_response")
		       .getJSONObject("modules")
		       .getJSONArray("waybill_cloud_print_response");
		      
      
		    	
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
		       lo.setNameRecipient(orderShop.getFullName());
		       lo.setPhoneRecipient(orderShop.getTel());
		       lo.setTownSend("");
		       lo.setItemName(orderShop.getGoodName());
		       lo.setItemCount(orderShop.getGoodNum());
		       lo.setTaobaoUserId(obj6.getUserId().toString());
		       lo.setTaobao(rsp2.getBody());
		       lo.setPrintState("已发货");
		       lo.setShopOrderNo(orderShop.getShopOrderNo());
//		       orderShop.setStatus(3);
//		       service.updateByPrimaryKey(orderShop); //更改订单状态
		       
		       if(lo.getCpCode().equals("YTO")){
		    	   order.setExpressName("圆通");
		       }else if(lo.getCpCode().equals("ZTO")){
		    	   order.setExpressName("中通");
		       }else if(lo.getCpCode().equals("POSTB")){
		    	   order.setExpressName("邮政");
		       }
		       order.setExpressNo(lo.getWaybill_code());
		       service.updateExpressByOrderNo(order);//更改快递公司信息
		       listLogistics.add(lo);
		       }
		       if (listLogistics.size()>0) {
			       result = new BhResult(200, "操作成功", listLogistics);
			   } else {
			       result = BhResult.build(400, "暂无数据！");
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
	 * @param map
	 * @return
	 */
    @RequestMapping("/updateOrderShopById")
    @ResponseBody
    public BhResult updateOrderShopById(@RequestBody Map<String, String> map) {
		   BhResult result = null;
		   try {
			    List<Object> obj = new ArrayList<Object>();
//			    String idS = map.get("id");
			    //物流公司
			    String expressName = map.get("expressName");
			    //物流单号
			    String expressNo = map.get("expressNo");
			    //商家订单号
			    String idS = map.get("shopOrderNo");
			   
			    String[] expressNameList = expressName.split(",");
			    String[] expressNoList = expressNo.split(",");
			    String[] arr = idS.split(",");
			    
			    if(StringUtils.isEmpty(idS)&&StringUtils.isEmpty(expressName)&&StringUtils.isEmpty(expressNo)){
			    	result = new BhResult(400, "参数不能为空", null);
			    }
			   
			    for(int i=0;i<arr.length;i++){
			    	OrderShop orderShop = service.getOrderById(arr[i]);
			    	orderShop.setStatus(Contants.shopStatu3);      //已发货
			    	orderShop.setDeliveryWay(3); //其他物流
			    	orderShop.setSendTime(new Date()); //打印时间为发货时间
			    	
			    	 //程凤云 2018-4-11添加
		        	 List<OrderTeam> teamList = orderTeamMapper.selectOrderTeanByOrderNoAndStatus(orderShop.getOrderNo());
				     if (teamList.size()>0) {
				    	  WXMSgTemplate template = new WXMSgTemplate();
				    	  template.setOrderShopId(orderShop.getId()+"");
				    	  wechatTemplateMsgService.sendGroupGoodTemplate(template);
					 }
			    	
			    	Order order = new Order();
			    	if(expressNameList[i].equals("YTO")){
				    	   order.setExpressName("圆通");
				    }else if(expressNameList[i].equals("ZTO")){
				    	   order.setExpressName("中通");
				    }else if(expressNameList[i].equals("POSTB")){
				    	   order.setExpressName("邮政");
				    }
			    	order.setDeliveryStatus(1); //已发货
			    	order.setExpressNo(expressNoList[i]);
			    	order.setOrderNo(orderShop.getOrderNo()); //平台订单号
			    	service.updateExpressByOrderNo(order);//更改快递公司信息
			    	
			    	int row = service.updateByPrimaryKey(orderShop);
			    	obj.add(row);
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
	
    /**
     * 更改订单物流信息 (商家自配)，商家可能要重新发货，换其他的快递,把 order_main 的快递号和公司修改了
     * @param map
     * @return
     */
    @RequestMapping("/updateOrderByOrderNo")
    @ResponseBody
    public BhResult updateOrderByOrderNo(@RequestBody Map<String, String> map) {
		   BhResult result = null;
		   try {
			    String shopOrderNo = map.get("shopOrderNo");
			    String expressName = map.get("expressName");
			    String expressNo = map.get("expressNo");
			    if(StringUtils.isEmpty(shopOrderNo)||StringUtils.isEmpty(expressName)||StringUtils.isEmpty(expressNo)){
			    	result = new BhResult(400, "参数不能为空", null);
			    }else{
			    	  OrderShop orderShop = service.getOrderById(shopOrderNo);
			    	  orderShop.setStatus(Contants.shopStatu3);      //已发货
			    	  orderShop.setDeliveryWay(3); //其他物流
			    	  orderShop.setSendTime(new Date());
			    	  //更改order 的快递信息
			          Order order = new Order();
			          order.setOrderNo(orderShop.getOrderNo());
			          order.setExpressName(expressName);
			          order.setExpressNo(expressNo);
			          try{
			        	  //程凤云 2018-4-11添加
				          List<OrderTeam> teamList = orderTeamMapper.selectOrderTeanByOrderNoAndStatus(orderShop.getOrderNo());
						   if (teamList.size()>0) {
						    	  WXMSgTemplate template = new WXMSgTemplate();
						    	  template.setOrderShopId(orderShop.getId()+"");
						    	  wechatTemplateMsgService.sendGroupGoodTemplate(template);
						   }
			        	  
			              service.updateExpressByOrderNo(order);
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
    public BhResult updateByOrderNo(@RequestBody Map<String, String> map) {
		   BhResult result = null;
		   try {
			    String shopOrderNo = map.get("shopOrderNo");
	
			    if(StringUtils.isEmpty(shopOrderNo)){
			    	result = new BhResult(400, "参数不能为空", null);
			    }else{
			    	  OrderShop orderShop = service.getOrderById(shopOrderNo);
			    	  orderShop.setStatus(Contants.shopStatu3);      //已发货
			    	  
			          Order order = new Order();
			          order.setOrderNo(orderShop.getOrderNo());
			          order.setDeliveryStatus(1);
			          try{
			        	  //程凤云 2018-4-11添加
				        	List<OrderTeam> teamList = orderTeamMapper.selectOrderTeanByOrderNoAndStatus(orderShop.getOrderNo());
						    if (teamList.size()>0) {
						    	  WXMSgTemplate template = new WXMSgTemplate();
						    	  template.setOrderShopId(orderShop.getId()+"");
						    	  wechatTemplateMsgService.sendGroupGoodTemplate(template);
							}
			        	  
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
    public BhResult updateDstateByOrderNo(@RequestBody Map<String, String> map) {
		   BhResult result = null;
		   try {
			    String shopOrderNo = map.get("shopOrderNo");
	
			    if(StringUtils.isEmpty(shopOrderNo)){
			    	result = new BhResult(400, "参数不能为空", null);
			    }else{
			    	  OrderShop orderShop = service.getOrderById(shopOrderNo);
			    	  orderShop.setdState(4);  //已送达
			    	  orderShop.setReceivedtime(new Date()); //收货时间
			          try{
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
    
//要解析的时候直接复制    
//    TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
     //获取商家全部模板
//    CainiaoCloudprintStdtemplatesGetRequest req = new CainiaoCloudprintStdtemplatesGetRequest();
//    CainiaoCloudprintStdtemplatesGetResponse rsp = client.execute(req, sessionKey);
    
    //解析json字符串
//    JSONArray  jsonArray =JSONObject.parseObject(rsp.getBody())
//    .getJSONObject("cainiao_cloudprint_stdtemplates_get_response")
//    .getJSONObject("result")
//    .getJSONObject("datas")
//    .getJSONArray("standard_template_result");
//    String url2 = null;
//    List<String> list3 = new ArrayList<String>();
//    for(int i=0;i<jsonArray.size();i++){
//       JSONObject jsonObject = jsonArray.getJSONObject(i);
// 	  String cp_code = jsonObject.getString("cp_code");
// 	  if(cp_code.equals(code)){
// 		 JSONArray jsonArray2 = (JSONArray) JSONObject.parseObject(jsonObject.getString("standard_templates")).get("standard_template_do");
// 
// 		 for(int j =0;j<jsonArray2.size();j++){
// 			JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
// 			url2 = jsonObject2.getString("standard_template_url")+"?template_id="+jsonObject2.getString("standard_template_id");
// 			list3.add(url2);
// 			System.out.println("===="+url2);
// 		 }
// 	  }
//    }
//    pu.setUrlId(list3);
//	   System.out.println(pu.getUrlId().get(0));
//    System.out.println("=====================================================");
    
    
}