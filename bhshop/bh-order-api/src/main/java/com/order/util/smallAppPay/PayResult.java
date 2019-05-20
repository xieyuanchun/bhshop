package com.order.util.smallAppPay;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.bh.config.Contants;
import com.bh.utils.IPUtils;
import net.sf.json.JSONObject;
public class PayResult {
   public JSONObject getPayResult(PayVo payVo)throws Exception{
      String body = new String(payVo.getBody().getBytes("UTF-8"),"ISO-8859-1");    
      String appid = Contants.sAppId;//小程序ID   
      String mch_id =Contants.s_mch_id;//商户号   
      System.out.println("mch_id--->"+mch_id);
      String nonce_str = String.valueOf(System.currentTimeMillis());//UUIDHexGenerator.generate();//随机字符串   
      String today = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());   
      
      String notify_url = Contants.s_notify_url;//通知地址   
      String trade_type = "JSAPI";//交易类型    
      /**/   
      PaymentPo paymentPo = new PaymentPo();   
      paymentPo.setAppid(appid);   
      paymentPo.setMch_id(mch_id);   
      paymentPo.setNonce_str(nonce_str);   
      String newbody=new String(body.getBytes("ISO-8859-1"),"UTF-8");//以utf-8编码放入paymentPo，微信支付要求字符编码统一采用UTF-8字符编码   
      paymentPo.setBody(newbody);   
      paymentPo.setOut_trade_no(payVo.getOut_trade_no());
      paymentPo.setTotal_fee(payVo.getTotal_fee());   
      paymentPo.setSpbill_create_ip(payVo.getSpbill_create_ip());   
      paymentPo.setNotify_url(notify_url);   
      paymentPo.setTrade_type(trade_type);   
      paymentPo.setOpenid(payVo.getOpenId());
      paymentPo.setAttach(payVo.getAttach());
      // 把请求参数打包成数组   
      Map<String,String> sParaTemp = new HashMap<String,String>();   
      sParaTemp.put("appid", paymentPo.getAppid());   
      sParaTemp.put("mch_id", paymentPo.getMch_id());   
      sParaTemp.put("nonce_str", paymentPo.getNonce_str());   
      sParaTemp.put("body",  paymentPo.getBody());   
      sParaTemp.put("out_trade_no", paymentPo.getOut_trade_no());   
      sParaTemp.put("total_fee",paymentPo.getTotal_fee());   
      sParaTemp.put("spbill_create_ip", paymentPo.getSpbill_create_ip());   
      sParaTemp.put("notify_url",paymentPo.getNotify_url());   
      sParaTemp.put("trade_type", paymentPo.getTrade_type());   
      sParaTemp.put("openid", paymentPo.getOpenid());
      sParaTemp.put("attach", paymentPo.getAttach());
      // 除去数组中的空值和签名参数   
      Map<String,String> sPara = PayUtil.paraFilter(sParaTemp);   
      String prestr = PayUtil.createLinkString(sPara); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串   
      String key = "&key="+Contants.sKey; // 商户支付密钥   
      System.out.println("small app prestr--->"+prestr); 
      System.out.println("small app key1--->"+key); 
      //MD5运算生成签名   
      String mysign = PayUtil.sign(prestr, key, "utf-8").toUpperCase();  
      System.out.println("mysign---->"+mysign);
      paymentPo.setSign(mysign);   
      //打包要发送的xml   
      String respXml = MessageUtil.messageToXML(paymentPo);   
      // 打印respXml发现，得到的xml中有“__”不对，应该替换成“_”   
      respXml = respXml.replace("__", "_");   
      String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单API接口链接   
      String param = respXml; 
      System.out.println("param---->"+param);
      //String result = SendRequestForUrl.sendRequest(url, param);//发起请求   
      String result =PayUtil.httpRequest(url, "POST", param);  
      System.out.println("result--->"+result);
      // 将解析结果存储在HashMap中   
      Map<String,String> map = new HashMap<String,String>();   
      InputStream in=new ByteArrayInputStream(result.getBytes());    
      // 读取输入流   
      SAXReader reader = new SAXReader();   
      Document document = reader.read(in);   
      // 得到xml根元素   
      Element root = document.getRootElement();   
      // 得到根元素的所有子节点   
      @SuppressWarnings("unchecked")   
      List<Element> elementList = root.elements();   
      for (Element element : elementList) {   
          map.put(element.getName(), element.getText());   
      }   
      // 返回信息   
      String return_code = map.get("return_code");//返回状态码   
      String return_msg = map.get("return_msg");//返回信息   
      System.out.println("small app return_msg--->"+return_msg); 
      System.out.println("small app return_code--->"+return_code);   
      JSONObject JsonObject=new JSONObject() ;   
      if(return_code=="SUCCESS"||return_code.equals(return_code)){   
          // 业务结果   
          String prepay_id = map.get("prepay_id");//返回的预付单信息   
          System.out.println("small app prepay_id--->"+prepay_id); 
          String nonceStr=String.valueOf(System.currentTimeMillis());   
          System.out.println("small app nonceStr--->"+nonceStr); 
          JsonObject.put("nonceStr", nonceStr);   
          JsonObject.put("package", "prepay_id="+prepay_id);   
          Long timeStamp= System.currentTimeMillis()/1000;
          System.out.println("small app timeStamp--->"+timeStamp); 
          JsonObject.put("timeStamp", timeStamp+"");   
          String stringSignTemp = "appId="+appid+"&nonceStr=" + nonceStr + "&package=prepay_id=" + prepay_id+ "&signType=MD5&timeStamp=" + timeStamp; 
          System.out.println("small app stringSignTemp--->"+stringSignTemp); 
          //再次签名   
          String paySign=PayUtil.sign(stringSignTemp, "&key="+Contants.sKey, "utf-8").toUpperCase();
          System.out.println("small app paySign--->"+paySign); 
          JsonObject.put("paySign", paySign);
      }
      return JsonObject;
   }
}
