package com.bh.jd.api;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.goods.GiftsResult;
import com.bh.jd.bean.goods.SkuByPage;
import com.bh.jd.bean.order.CheckDlokOrder;
import com.bh.jd.bean.order.NewStock;
import com.bh.jd.bean.order.OrderStock;
import com.bh.jd.bean.order.StockParams;
import com.bh.jd.bean.order.Track;
import com.bh.jd.enums.GoodsEnum;
import com.bh.jd.util.JDUtil;
import com.bh.utils.HttpUtils;
import com.bh.utils.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONArray;

public class JDStockApi {
	
    public static void main(String[] arg){
   /* 	String str = "[{skuId:'569172',num:'99999999'},{skuId:'1421728',num:'100'}]" ; 
    	String area ="1_0_0";
    	JSONArray ja3 = JSONArray.fromObject(str);
    	JdResult<String>  aa = getNewStockById(ja3.toString(),area);
    	System.out.println(aa.getResultCode());*/
    	//System.out.println(getNewStockById(str, area));
    	 String thirdOrder = "20171228182624353552";//必须    第三方的订单单号
    	 String sku = "[{skuId:'569172',num:'100'},{skuId:'1421728',num:'100','bNeedAnnex':true, 'bNeedGift':true,'price':188.00}]";//[{"skuId":商品编号, "num":商品数量,"bNeedAnnex":true, "bNeedGift":true,"price":100,"yanbao":[{"skuId":商品编号}]}]    (最高支持50种商品)
    /*	 JSONArray ja3 = JSONArray.fromObject(str);
    	 String sku = ja3.toString();*/
    	 //bNeedAnnex表示是否需要附件，默认每个订单都给附件，默认值为：true，如果客户实在不需要附件bNeedAnnex可以给false，该参数配置为false时请谨慎，真的不会给客户发附件的;
		//bNeedGift表示是否需要增品，默认不给增品，默认值为：false，如果需要增品bNeedGift请给true,建议该参数都给true,但如果实在不需要增品可以给false;
		//price 表示透传价格，需要合同权限，接受价格权限，否则不允许传该值；
		 String name = "程凤云";//必须	收货人
		 String province = "1";// 必须	一级地址
		 String city = "72";//	int	必须	二级地址
		 String county = "2799";//int 必须	三级地址
		 String town = "0";//	int	必须	四级地址  (如果该地区有四级地址，则必须传递四级地址，没有四级地址则传0)
		 String address ="纵横广场东塔1402室";//Stirng	必须	详细地址
		 String zip = "546300";//Stirng	非必须  邮编
		 String mobile= "18820079141";//Stirng	必须	手机号 
		 String email = "872820897@qq.com";//	Stirng	必须	邮箱
		 String invoiceState = "2";//int	必须	开票方式(1为随货开票，0为订单预借，2为集中开票 )
		 String invoiceType = "1";//int	必须	1普通发票2增值税发票
		 String selectedInvoiceTitle = "5";//int	必须	发票类型：4个人，5单位
		 String companyName = "aa";//String	必须	发票抬头  (如果selectedInvoiceTitle=5则此字段必须)
		 String invoiceContent = "22";//	int	必须	1:明细，3：电脑配件，19:耗材，22：办公用品  (备注:若增值发票则只能选1 明细)
		 String paymentType = "4";//	int	必须	支付方式 (1：货到付款，2：邮局付款，4：在线支付，5：公司转账，6：银行转账，7：网银钱包，101：金采支付)
		 String isUseBalance = "1";//	int	必须	使用余额paymentType=4时，此值固定是1   ，  其他支付方式0
		 String submitState = "1";//Int	必须	是否预占库存，0是预占库存（需要调用确认订单接口），1是不预占库存     金融支付必须预占库存传0;
    	
		//JdResult jdResult = submitOrder(thirdOrder, sku, name, province, city, county, town, address, zip, mobile, email, invoiceState, invoiceType, selectedInvoiceTitle, companyName, invoiceContent, paymentType, isUseBalance, submitState);
		 String jdResult = SelectJdOrder("70889979275");
	//	 JdResult orderTrack = orderTrack("70889979275");
	//	 System.out.println("orderTrack-->" + orderTrack);
		 String cancel = Cancel("70889979275");
		 
		/* String orderTrack = orderTrack("70792075657");
		 //查询物流
		 System.out.println( "物流：orderTrack-->" +orderTrack);
		 //查询订单
		 String selectJdOrder = SelectJdOrder("70792075657");
		 System.out.println("SelectJdOrder-->" + selectJdOrder);
		 //订单反查
		 String selectJdOrderIdByThirdOrder = SelectJdOrderIdByThirdOrder("20171228182624353552");
		 System.out.println("selectJdOrderIdByThirdOrder-->" + selectJdOrderIdByThirdOrder);
		 */
		 //支付
	/*	 String dopay = DoPay("70792075657");
		 System.out.println("支付-->" +dopay);*/
		 
		 //占用库存
	/*	 String ConfirmOrder = ConfirmOrder("70792075657");
		 System.out.println("ConfirmOrder -->" + ConfirmOrder);*/
		 
		/* String Cancel = Cancel("70792075657");
		 System.out.println("取消-->" + Cancel);*/
		 
	}	
	
    //6.2 批量获取库存接口（建议订单详情页、下单使用）
	public static JdResult<String> getNewStockById(String skuNums,String area){
		//String str = "[{skuId:'569172',num:'100'},{skuId:'1421728',num:'100'}]" ; 
    	//String area ="1_0_0";
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("skuNums", skuNums);
		jsonObject.put("area", area);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.GET_NEW_STOCK_BY_ID.getMethod(), jsonObject);
		
		Gson gson = new Gson();
		JdResult<String> jdResult = gson.fromJson(retJsonStr, new TypeToken<JdResult<String>>(){}.getType());
	
		return jdResult;
	}
	
	//7.1 统一下单接口
	public static JdResult submitOrder(String thirdOrder,String sku,String name,String province,String city,String county,String town,String address,String zip,String mobile,String email,
			String invoiceState,String invoiceType,String selectedInvoiceTitle,String companyName,String invoiceContent,String paymentType,String isUseBalance,String submitState
			){
		/**
			 * 
		private String thirdOrder;//必须    第三方的订单单号
		private String sku;//[{"skuId":商品编号, "num":商品数量,"bNeedAnnex":true, "bNeedGift":true,"price":100,"yanbao":[{"skuId":商品编号}]}]    (最高支持50种商品)
		bNeedAnnex表示是否需要附件，默认每个订单都给附件，默认值为：true，如果客户实在不需要附件bNeedAnnex可以给false，该参数配置为false时请谨慎，真的不会给客户发附件的;
		bNeedGift表示是否需要增品，默认不给增品，默认值为：false，如果需要增品bNeedGift请给true,建议该参数都给true,但如果实在不需要增品可以给false;
		price 表示透传价格，需要合同权限，接受价格权限，否则不允许传该值；
		private String name;//必须	收货人
		private Integer province;// 必须	一级地址
		private Integer city;//	int	必须	二级地址
		private Integer county;//int 必须	三级地址
		private Integer town;//	int	必须	四级地址  (如果该地区有四级地址，则必须传递四级地址，没有四级地址则传0)
		private String address;//Stirng	必须	详细地址
		private String zip;//Stirng	非必须  邮编
		private String mobile;//Stirng	必须	手机号 
		private String email;//	Stirng	必须	邮箱
		private Integer invoiceState;//	int	必须	开票方式(1为随货开票，0为订单预借，2为集中开票 )
		private Integer invoiceType;//int	必须	1普通发票2增值税发票
		private Integer selectedInvoiceTitle;//int	必须	发票类型：4个人，5单位
		private String companyName;//	String	必须	发票抬头  (如果selectedInvoiceTitle=5则此字段必须)
		private Integer invoiceContent;//	int	必须	1:明细，3：电脑配件，19:耗材，22：办公用品  (备注:若增值发票则只能选1 明细)
		private Integer paymentType;//	int	必须	支付方式 (1：货到付款，2：邮局付款，4：在线支付，5：公司转账，6：银行转账，7：网银钱包，101：金采支付)
		private Integer isUseBalance;//	int	必须	使用余额paymentType=4时，此值固定是1   ，  其他支付方式0
		private Integer submitState;//	Int	必须	是否预占库存，0是预占库存（需要调用确认订单接口），1是不预占库存     金融支付必须预占库存传0;
		***/
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("thirdOrder", thirdOrder);
		jsonObject.put("sku", sku);
		jsonObject.put("name", name);
		jsonObject.put("province", province);
		jsonObject.put("city", city);
		jsonObject.put("county", county);
		jsonObject.put("town", town);
		jsonObject.put("address", address);
		jsonObject.put("zip", zip);
		jsonObject.put("mobile", mobile);
		jsonObject.put("email", email);
		jsonObject.put("invoiceState", invoiceState);
		jsonObject.put("invoiceType", invoiceType);
		jsonObject.put("selectedInvoiceTitle", selectedInvoiceTitle);
		jsonObject.put("companyName", companyName);
		jsonObject.put("invoiceContent", invoiceContent);
		jsonObject.put("paymentType", paymentType);
		jsonObject.put("isUseBalance", isUseBalance);
		jsonObject.put("submitState", submitState);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.SUBMIT_ORDER.getMethod(), jsonObject);
		System.out.println("jdsubmitresult  submitOrder-->" + retJsonStr);
		Gson gson = new Gson();
		JdResult ret = JsonUtils.jsonToPojo(retJsonStr, JdResult.class);
		
		return ret;
	}
	
	//7.2  确认预占库存订单接口
	public static String ConfirmOrder(String jdOrderId){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("jdOrderId", jdOrderId);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.CONFIRM_ORDER.getMethod(), jsonObject);
		return retJsonStr;
		
	}
	
	//7.3 取消未确认订单接口
	public static String Cancel(String jdOrderId){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("jdOrderId", jdOrderId);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.CANCEL.getMethod(), jsonObject);
		return retJsonStr;
		
	}
	
	
	//7.4 发起支付接口
	public static String DoPay(String jdOrderId){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("jdOrderId", jdOrderId);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.DOPAY.getMethod(), jsonObject);
		return retJsonStr;
		
	}
	
	//7.5 获取京东预约日历
	public static String PromiseCalendar(String province,String city,String county,String town,String paymentType,String sku){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("province", province);
		jsonObject.put("city", city);
		jsonObject.put("county", county);
		jsonObject.put("town", town);
		jsonObject.put("paymentType", paymentType);
		jsonObject.put("sku", sku);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.PROMISE_CALENDAR.getMethod(), jsonObject);
		return retJsonStr;
		
	}
	
	//7.6 订单反查接口
	public static String SelectJdOrderIdByThirdOrder(String jdOrderId){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("jdOrderId", jdOrderId);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.SELECT_JD_ORDERID_THIRD_ORDER.getMethod(), jsonObject);
		return retJsonStr;
		
	}
	
	//7.7 查询京东订单信息接口
	public static String SelectJdOrder(String jdOrderId){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("jdOrderId", jdOrderId);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.SELECT_JD_ORDER.getMethod(), jsonObject);
		return retJsonStr;
	}
	
	//7.8 查询配送信息接口
	public static JdResult<OrderStock> orderTrack(String jdOrderId){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("jdOrderId", jdOrderId);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.ORDER_TRACK.getMethod(), jsonObject);
		Gson gson = new Gson();
		JdResult<OrderStock> ret = gson.fromJson(retJsonStr, new TypeToken<JdResult<OrderStock>>(){}.getType());
		return ret;
	}
	
	//7.9 统一余额查询接口
	public static String GetBalance(String payType){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("payType", payType);
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.GET_BALANCE.getMethod(), jsonObject);
		return retJsonStr;
		
	}
		
	//7.10 查询用户金彩余额接口（仅供金彩支付客户使用）
	public static String selectJincaiCredit(){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.SELECT_JINCAI_CREDIT.getMethod(), jsonObject);
		return retJsonStr;
		
	}
	
	// 8.2 获取妥投订单接口
	public static JdResult<CheckDlokOrder> checkDlokOrder(String addTime){
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("date", addTime); //订单下单的时间
		jsonObject.put("page", "1");
		String	retJsonStr = HttpUtils.doPost(GoodsEnum.CheckDlokOrder.getMethod(), jsonObject);
//        String  retJsonStr2 = null;
//		try {
//			retJsonStr2 = new String(retJsonStr.getBytes("GBK"),"UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Gson gson = new Gson();
		JdResult<CheckDlokOrder> ret = gson.fromJson(retJsonStr, new TypeToken<JdResult<CheckDlokOrder>>(){}.getType());
	
		return ret;
	}
	
}
