package com.bh.union.controller;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bh.union.config.UnionConfig;
import com.bh.union.util.Contants;
import com.bh.union.util.HttpService;
import com.bh.union.util.PayUtil;
import com.bh.union.vo.UnionPayAppVO;
import com.bh.union.vo.UnionPayRefundVO;
import com.bh.union.vo.UnionPayVO;
import com.chinaums.netpay.util.SignUtil;
@RestController
@RequestMapping("/union")
public class UnionPayController {
	private static final Logger logger = LoggerFactory.getLogger(UnionPayController.class);
	//private static final String  baseUrl ="https://bh2015.com";
	@RequestMapping("/wx/jsPay")
	public String jsPay(@RequestBody UnionPayVO entity) {
		
		try {
		    System.out.println("md5Key--->"+entity.getMd5Key());
		    entity.setReturnUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionRediret0");
		    entity.setNotifyUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionCallback");
		    return pay(entity);
		} catch (Exception e) {
			e.printStackTrace();
		    return null;
			// TODO: handle exception
		}

	}
	
	@RequestMapping("/wx/ylcjsPay")
	public String ylcjsPay(@RequestBody UnionPayVO entity) {
		
		try {
		    System.out.println("md5Key--->"+entity.getMd5Key());
		    entity.setReturnUrl("https://bhmall.zhiyesoft.cn:2443/bh-order-api/payCallback/unionRediret0");
		    entity.setNotifyUrl("https://bhmall.zhiyesoft.cn:2443/bh-order-api/payCallback/unionCallback");
		    return pay(entity);
		} catch (Exception e) {
			e.printStackTrace();
		    return null;
			// TODO: handle exception
		}

	}
	
	@RequestMapping("/wx/wxSmallApp")
	public String wxSmallApp(@RequestBody UnionPayVO entity) {
		try {
		  //  entity.setReturnUrl("https://bh2015.com/bh-order-api/payCallback/unionRediret?isFlag=0");
		    entity.setNotifyUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionCallback");
		    entity.setTradeType("MINI");
		    return pay(entity);
		} catch (Exception e) {
			e.printStackTrace();
		    return null;
			// TODO: handle exception
		}

	}
	//充值 支付
	@RequestMapping("/wx/recharge")
	public String recharge(@RequestBody UnionPayVO entity) {
		
		try {
			//&openid="+entity.get
		    entity.setNotifyUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionCallback");
		    return pay(entity);
		} catch (Exception e) {
			e.printStackTrace();
		    return null;
			// TODO: handle exception
		}

	}
	
	//手机充值
	@RequestMapping("/wx/mRecharge")
	public String mRecharge(@RequestBody UnionPayVO entity) {
			
			try {
			//	/binhuiApp/#/rechargeSuccess
			    entity.setReturnUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionRediret11");
			//    entity.setReturnUrl(Contants.baseUrl+"/binhuiApp/#/rechargeSuccess");
			    entity.setNotifyUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionCallback");
			    return pay(entity);
			} catch (Exception e) {
				e.printStackTrace();
			    return null;
				// TODO: handle exception
			}

	 }
		
	//种子支付
	@RequestMapping("/wx/seedPay")
	public String seedPay(@RequestBody UnionPayVO entity) {
		
		try {
		    entity.setReturnUrl(Contants.baseUrl+"/sign/iframe.html");
		    entity.setNotifyUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionCallback");
		    return pay(entity);
		} catch (Exception e) {
			e.printStackTrace();
		    return null;
			// TODO: handle exception
		}

	}
	
	@RequestMapping("/wx/depositPay")
	public String depositPay(@RequestBody UnionPayVO entity) {
		try {
			String  openid="oj9MyxE6qOyXvyJts5nd6UrPS2q4";
		    entity.setReturnUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionRediret2");
		    entity.setNotifyUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionCallback");
		    logger.debug(entity.getOpenid()+"----"+openid);
		    return pay(entity);
		} catch (Exception e) {
			e.printStackTrace();
		    return null;
			// TODO: handle exception
		}

	}
	
	@RequestMapping("/wx/promiseMoney")
	public String promiseMoney(@RequestBody UnionPayVO entity) {
		try {
		    entity.setReturnUrl(Contants.baseUrl+"/binhuiApp");
		    entity.setNotifyUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionCallback");
		    return pay(entity);
		} catch (Exception e) {
			e.printStackTrace();
		    return null;
			// TODO: handle exception
		}

	}
	@RequestMapping("/wx/mPromiseMoney")
	public String mPromiseMoney(@RequestBody UnionPayVO entity) {
		try {
			entity.setReturnUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionRediret10");
		    entity.setNotifyUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionCallback");
		    return pay(entity);
		} catch (Exception e) {
			e.printStackTrace();
		    return null;
			// TODO: handle exception
		}
	}
	
	
	@RequestMapping("/wx/messagePay")
	public String messagePay(@RequestBody UnionPayVO entity) {
		try {
		    entity.setReturnUrl(Contants.baseUrl+"/binhuiApp");
		    entity.setNotifyUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionCallback");
		    return pay(entity);
		} catch (Exception e) {
			e.printStackTrace();
		    return null;
			// TODO: handle exception
		}
	}
	
	
	@RequestMapping("/wx/refund")
	public String refund(@RequestBody UnionPayRefundVO entity) {
		try {
/*		entity.setMerOrderId("319400020180131180812116340");
		entity.setMd5Key("fcAmtnx7MwismjWNhNKdHC44mNXtnEQeJkRrhKJwyrW2ysRR");
		entity.setRefundAmount("1");*/
		
		entity.setRequestTimestamp(new Date());
		entity.setMsgType("refund");
		UnionPayRefundVO req = new UnionPayRefundVO();
		entity.setInstMid(req.getInstMid());
		entity.setMsgSrc(req.getMsgSrc());
		entity.setMid(req.getMid());
		entity.setTid(req.getTid());
		
		JSONObject json = (JSONObject) JSONObject.toJSON(entity);
		String sign = SignUtil.makeSign(json.toJSONString(), entity.getMd5Key());
		json.put("sign",sign);
		System.out.println(json.toJSONString());
		boolean checkRet1 = SignUtil.checkSign(json.toJSONString(), entity.getMd5Key());
		System.out.println("验签方式一验签结果: " + checkRet1);
		if(checkRet1){
			Map<String,String> mapObj = JSONObject.parseObject(json.toJSONString(), Map.class);
			Map<String,String> retMap = PayUtil.buildSignMap(mapObj);
			retMap.put("sign", sign);
			String result = HttpService.doPostJson(UnionConfig.refundApiUrl, retMap);
			System.out.println("refund result----->"+result);
			Map<String,String> resultObject = JSON.parseObject(result, Map.class);
			if(resultObject!=null){
				return resultObject.get("refundStatus");//退款成功  SUCCESS
			}
		}else{
			System.out.println("######验签失败#######");
		
		}
		return "FAIL";

	} catch (Exception e) {
		e.printStackTrace();
		return "FAIL";
		// TODO: handle exception
	}
}
	
	@RequestMapping("/wx/appPay")
	public String appPay(@RequestBody UnionPayAppVO entity) {
		System.out.println("###############appPay ok#############");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			entity.setRequestTimestamp(sdf.format(new Date()));
		    return appOrderPay(entity);
		} catch (Exception e) {
			e.printStackTrace();
		    return null;
			// TODO: handle exception
		}

	}
	public static void testRefund(){
		try {
			UnionPayRefundVO entity = new UnionPayRefundVO();
			entity.setMerOrderId("37691804031043230062");
			entity.setMd5Key("2b8JBaFTEn6i26AASw3iswBySjw46pJWiW2dEh8wMsXiNPX");
			entity.setRefundAmount("1");
			entity.setRequestTimestamp(new Date());
			entity.setMsgType("refund");
		    System.out.println("md5Key--->"+entity.getMd5Key());
			JSONObject json = (JSONObject) JSONObject.toJSON(entity);
			System.out.println(json.toJSONString());
			String sign = SignUtil.makeSign(json.toJSONString(), entity.getMd5Key());
			
			System.out.println(sign);
			json.put("sign",sign);
			System.out.println(json.toJSONString());
			boolean checkRet1 = SignUtil.checkSign(json.toJSONString(), entity.getMd5Key());
			System.out.println("验签方式一验签结果: " + checkRet1);
			if(checkRet1){
				Map<String,String> mapObj = JSONObject.parseObject(json.toJSONString(), Map.class);
				Map<String,String> retMap = PayUtil.buildSignMap(mapObj);
				retMap.put("sign", sign);
			//	String refundUrl = UnionConfig.refundApiUrl+"?"+PayUtil.buildSignString(mapObj)+"&sign="+sign;
			//	System.out.println("refundUrl-->"+refundUrl);
				String result = HttpService.doPostJson(UnionConfig.refundApiUrl, retMap);
				Map<String,String> resultObject = JSON.parseObject(result, Map.class);
				System.out.println(resultObject.get("refundStatus"));
				System.out.println("result-->"+result);
			
			}else{
				System.out.println("######验签失败#######");
			
			}

		} catch (Exception e) {
			e.printStackTrace();
		 
			// TODO: handle exception
		}

	}
	private static void testPay(){
		UnionPayVO entity = new UnionPayVO();
		entity.setAttachedData("null,2");
		entity.setReturnUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionRediret0");
		entity.setNotifyUrl(Contants.baseUrl+"/bh-order-api/payCallback/unionCallback");
		entity.setOriginalAmount("1");
		entity.setTotalAmount("1");
		entity.setOpenid("oj9MyxE6qOyXvyJts5nd6UrPS2q4");
	//	vo.setMerOrderId("3194000"+System.currentTimeMillis());
		entity.setMd5Key("2b8JBaFTEn6i26AASw3iswBySjw46pJWiW2dEh8wMsXiNPXK");
		entity.setMerOrderId("3769"+System.currentTimeMillis());
		String ret = pay(entity);
		System.out.println("ret-->"+ret);
	
	}
	private static void testAppPay(){
		UnionPayAppVO entity = new UnionPayAppVO();
		entity.setOriginalAmount("1");
		entity.setTotalAmount("1");
	//	vo.setMerOrderId("3194000"+System.currentTimeMillis());
		entity.setMd5Key("2b8JBaFTEn6i26AASw3iswBySjw46pJWiW2dEh8wMsXiNPXK");
		entity.setMerOrderId("3769"+System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		entity.setRequestTimestamp(sdf.format(new Date()));
		//requestTimestamp
		String ret = appOrderPay(entity);
		System.out.println("ret-->"+ret);
	
	}
	private static String appOrderPay(UnionPayAppVO entity){
		JSONObject json = (JSONObject) JSONObject.toJSON(entity);
		System.out.println(json.toJSONString());
		String sign = SignUtil.makeSign(json.toJSONString(), entity.getMd5Key());
		System.out.println(sign);
		json.put("sign",sign);
		System.out.println(json.toJSONString());
		boolean checkRet1 = SignUtil.checkSign(json.toJSONString(), entity.getMd5Key());
		System.out.println("验签方式一验签结果: " + checkRet1);
		if(checkRet1){
			Map mapObj = JSONObject.parseObject(json.toJSONString(), Map.class);
			System.out.println("map---->"+PayUtil.buildSignString(mapObj));
			Map<String,String> retMap = PayUtil.buildSignMap(mapObj);
			retMap.put("sign", sign);
			String result = HttpService.doPostJson(UnionConfig.payApiUrlApp, retMap);
			
			System.out.println("result--->"+result);
		//	String result = HttpService.doGet(payurl);
			return result;
		}else{
			System.out.println("######验签失败#######");
			return null;
		}
	}
	private static String pay(UnionPayVO entity){
		UnionPayVO req = new UnionPayVO();
		if(entity.getTradeType()!=null&&"MINI".equals(entity.getTradeType())){
			 entity.setInstMid("MINIDEFAULT");
		}else{
			entity.setInstMid(req.getInstMid());
		}
		entity.setMsgSrc(req.getMsgSrc());
		entity.setMid(req.getMid());
		entity.setTid(req.getTid());
		JSONObject json = (JSONObject) JSONObject.toJSON(entity);
		System.out.println(json.toJSONString());
		String sign = SignUtil.makeSign(json.toJSONString(), entity.getMd5Key());
		System.out.println(sign);
		json.put("sign",sign);
		System.out.println(json.toJSONString());
		boolean checkRet1 = SignUtil.checkSign(json.toJSONString(), entity.getMd5Key());
		System.out.println("验签方式一验签结果: " + checkRet1);
		if(checkRet1){
			Map mapObj = JSONObject.parseObject(json.toJSONString(), Map.class);
			System.out.println(PayUtil.buildSignString(mapObj));
			String payurl = UnionConfig.payApiUrl+"?"+PayUtil.buildSignString(mapObj)+"&sign="+sign;
			System.out.println("payurl--->"+payurl);
		//	String result = HttpService.doGet(payurl);
			return payurl;
		}else{
			System.out.println("######验签失败#######");
			return null;
		}
	}
	public static void main(String[] args) {
	  testPay();
	  //testAppPay();
	  //testRefund();
	}


}
