package com.bh.utils;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.bh.bean.Alipay;
import com.bh.config.AlipayConfig;
import com.bh.result.BhResult;
/**
 * 支付宝工具类
 * @author xxj
 *
 */
public class AlipayUtil {

    private static Logger logger = LoggerFactory.getLogger(AlipayUtil.class);
    private static AlipayClient alipayClient;
    private static Object object = new Object();
    public static AlipayClient getAlipayClient(){
        if(alipayClient == null) {
            synchronized (object) {
                if(alipayClient == null) {
                    alipayClient = new DefaultAlipayClient(AlipayConfig.serviceUrl, AlipayConfig.appId, AlipayConfig.appPrivateKey, "json", AlipayConfig.charset, AlipayConfig.alipayPublicKey, AlipayConfig.signType);
                }
            }
        }
        return alipayClient;
    }

    public static BhResult createPageTrade(Alipay alipay){
    	BhResult result = new BhResult();
    	//获得初始化的AlipayClient
    	AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.serviceUrl, AlipayConfig.appId, AlipayConfig.appPrivateKey, "json", AlipayConfig.charset, AlipayConfig.alipayPublicKey, AlipayConfig.signType);
    	
    	//设置请求参数
    	AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
    	alipayRequest.setReturnUrl(AlipayConfig.returnUrl);
    	alipayRequest.setNotifyUrl(AlipayConfig.notifyUrl);
    	 	
    	alipayRequest.setBizContent("{\"out_trade_no\":\""+ alipay.getOutTradeNo() +"\"," 
    			+ "\"total_amount\":\""+ alipay.getTotalAmount() +"\"," 
    			+ "\"subject\":\""+ alipay.getSubject() +"\"," 
    			+ "\"body\":\""+ alipay.getBody() +"\"," 
    			+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
    	//请求
    	try {
    		AlipayTradePagePayResponse response = alipayClient.pageExecute(alipayRequest);
    		 if(response.isSuccess()) {
               
                 result.setStatus(200);
                 result.setMsg(response.getBody());
                 return result;
             } else {
             
             	result.setStatus(-1);
             	result.setMsg(response.getCode() + ":" + response.getMsg());
             	return result;
             
             }
		} catch (AlipayApiException e) {
	         result.setStatus(-1);
	         result.setMsg(e.getErrCode() + ":" + e.getErrMsg());
	         return result;
			// TODO: handle exception
		}
    
    }
    public static BhResult createAppTrade(Alipay alipay){
    	BhResult result = new BhResult();
    	//实例化客户端
    	AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.serviceUrl,AlipayConfig.appId, AlipayConfig.appPrivateKey, "json", AlipayConfig.charset, AlipayConfig.signType, "RSA2");
    	//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
    	AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
    	//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
    	AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
    	model.setBody(alipay.getBody());
    	model.setSubject(alipay.getSubject());
    	model.setOutTradeNo(alipay.getOutTradeNo());
    	model.setTotalAmount(alipay.getTotalAmount());
    	model.setProductCode("QUICK_MSECURITY_PAY");
    	request.setBizModel(model);
    	request.setNotifyUrl(AlipayConfig.notifyUrl);
    	try {
    	        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
    	        System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
    	        result.setStatus(200);
    	        result.setData(response.getBody());
    	        return result;
    	    } catch (AlipayApiException e) {
    	     e.printStackTrace();
    	     result.setStatus(500);
   	         result.setMsg(e.getErrCode() + ":" + e.getErrMsg());
   	         return result;
    	}
    }
    public static void writePageTrade(HttpServletResponse response,Alipay alipay){
    	BhResult tradeResult = createPageTrade(alipay);
    	try {
    		response.setContentType("text/html;charset=" + AlipayConfig.charset);
    		response.getWriter().write(tradeResult.getMsg());// 直接将完整的表单html输出到页面
    		response.getWriter().flush();
    		response.getWriter().close();
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    }
    public static void main(String[] args){
    	String totalAmount="0.1";
    	Alipay alipay = new Alipay();
		alipay.setTotalAmount(totalAmount);
		alipay.setSubject("您将要支付:"+totalAmount);
		alipay.setBody("xxj测试支付宝创建订单描述");
		String orderNo = String.valueOf(System.currentTimeMillis());
		alipay.setOutTradeNo(orderNo);
		//网站支付
		// System.out.println(AlipayUtil.createPageTrade(alipay).getMsg());
		//app支付
		AlipayUtil.createAppTrade(alipay);
    }
   
}