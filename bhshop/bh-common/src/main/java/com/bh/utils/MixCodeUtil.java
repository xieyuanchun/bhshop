package com.bh.utils;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;

public class MixCodeUtil {
	public static String orderNoPre="3134";
	public static String GetUserIP(HttpServletRequest request) throws Exception {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}


	// 随机码
	public static String sjs() {
		Random rand = new Random();
		String str = "0123456789";
		String result = "";
		for (int i = 0; i < 4; i++) {
			String randStr = Integer.valueOf(rand.nextInt(str.length())).toString();
			result += randStr;
		}
		return result;
	}
	
	//生成商品编号
	public static String createOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		Random r = new Random();
		int x=r.nextInt(999999-100000+1)+100000;
		key = key + x;
		return key;
	}
	
	//生成专区分类编号
	public static String createOutTradeNo1() {
		SimpleDateFormat format = new SimpleDateFormat("yyMMdd", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		Random r = new Random();
		int x=r.nextInt(9999-1000+1)+1000;
		key = key + x;
		return key;
	}
	
	/*public static String getOrderNo(){
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String oid = df.format(new Date()) + sjs();
		return oid;
	}*/
	
	public static String getOrderNo(){
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		int rand = (int)(Math.random()*10);
		String randStr=""+rand;
		String oid = orderNoPre+df.format(new Date())+randStr;
		return oid;
	}
	
	
    //过滤敏感词 特殊字符过滤
    public static String washStr(String str) throws Exception{
    	 String param1="";
		 Properties prop =  new  Properties();    
	        InputStream in = MixCodeUtil. class .getResourceAsStream( "/resource/keyword.properties" );         
	            prop.load(in);    
	            param1 = prop.getProperty( "keyword" ).trim();    
	            param1 = param1.substring(0, param1.length() - 1);
				String[] strNa = param1.split(",");
				for (int i = 0; i < strNa.length; i++) {
					String strTemp = strNa[i].toString();
					str = str.replace(strTemp, "");
				
	        }  
				
			return str;    
    }
    
    //cheng  获取n位的随机码
 // 随机码
 	public static String getCode(Integer n) {
 		Random rand = new Random();
 		String str = "0123456789";
 		String result = "";
 		for (int i = 0; i < n; i++) {
 			String randStr = Integer.valueOf(rand.nextInt(str.length())).toString();
 			result += randStr;
 		}
 		return result;
 	}
 	
    
    
    
  
}
