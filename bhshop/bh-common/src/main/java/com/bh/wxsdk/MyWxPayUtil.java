package com.bh.wxsdk;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
public class MyWxPayUtil {
   public static void main(String args[]) throws Exception{
	   WXPay wxp = new WXPay(new WXPayConfig() {
			@Override
			IWXPayDomain getWXPayDomain() {
				// TODO Auto-generated method stub
				return new IWXPayDomain(){
					@Override
					public void report(String domain, long elapsedTimeMillis, Exception ex) {
					
					}
					@Override
					public DomainInfo getDomain(WXPayConfig config) {
						// TODO Auto-generated method stub
						return new DomainInfo(WXPayConstants.WXPAY_DOMAIN, false);
					}};
			}
			
			@Override
			String getMchID() {
				// TODO Auto-generated method stub
				return WXPayConstants.MCH_ID;
			}
			
			@Override
			String getKey() {
				// TODO Auto-generated method stub
				return WXPayConstants.KEY;
			}
			
			@Override
			InputStream getCertStream() {
				// TODO Auto-generated method stub
				FileInputStream instream=null;
			     try {
			    	// KeyStore keyStore  = KeyStore.getInstance("PKCS12");
				     instream = new FileInputStream(new File("F:/zy/weixincert/apiclient_cert.p12"));
			      //   keyStore.load(instream, "1456013002".toCharArray());
			     }catch (Exception e) {
					// TODO: handle exception
				} finally {
			           // instream.close();
			    }
				return instream;
			}
			
			@Override
			String getAppID() {
				// TODO Auto-generated method stub
				return WXPayConstants.APP_ID;
			}
		},WXPayConstants.WX_NOTIFY,true,false);
   	Map<String,String> reqData = new HashMap<String,String>();
  	reqData.put("transaction_id", "4200000026201712154442749012");
	reqData.put("out_refund_no", "20171215122718851466");
   	reqData.put("total_fee", "2");
   	reqData.put("refund_fee", "2");
   	
   	Map<String,String> retMap = wxp.refund(reqData);
   	
   	System.out.println("retMap--->"+retMap);
   }
   public static Map<String,String> refund(Map<String,String> reqData) throws Exception{

	   WXPay wxp = new WXPay(new WXPayConfig() {
			@Override
			IWXPayDomain getWXPayDomain() {
				// TODO Auto-generated method stub
				return new IWXPayDomain(){
					@Override
					public void report(String domain, long elapsedTimeMillis, Exception ex) {
					
					}
					@Override
					public DomainInfo getDomain(WXPayConfig config) {
						// TODO Auto-generated method stub
						return new DomainInfo(WXPayConstants.WXPAY_DOMAIN, false);
					}};
			}
			
			@Override
			String getMchID() {
				// TODO Auto-generated method stub
				return WXPayConstants.MCH_ID;
			}
			
			@Override
			String getKey() {
				// TODO Auto-generated method stub
				return WXPayConstants.KEY;
			}
			
			@Override
			InputStream getCertStream() {
				// TODO Auto-generated method stub
				FileInputStream instream=null;
			     try {
			    	// KeyStore keyStore  = KeyStore.getInstance("PKCS12");
				     instream = new FileInputStream(new File(WXPayConstants.WX_CERT_PATH));
			      //   keyStore.load(instream, "1456013002".toCharArray());
			     }catch (Exception e) {
					// TODO: handle exception
				} finally {
			           // instream.close();
			    }
				return instream;
			}
			
			@Override
			String getAppID() {
				// TODO Auto-generated method stub
				return WXPayConstants.APP_ID;
			}
		},WXPayConstants.WX_NOTIFY,true,false);
  /*	reqData.put("transaction_id", "4200000026201712154442749012");
	reqData.put("out_refund_no", "20171215122718851466");
   	reqData.put("total_fee", "2");
   	reqData.put("refund_fee", "2");*/

   	Map<String,String> retMap = wxp.refund(reqData);
   	return retMap;
   
   }
}
