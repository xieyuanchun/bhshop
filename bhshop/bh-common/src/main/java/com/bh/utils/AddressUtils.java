package com.bh.utils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;


import com.bh.bean.IDCardEntity;
import com.bh.config.Contants;
import com.bh.utils.pay.HttpService;
import com.bh.utils.pay.JsonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 *  根据IP地址获取详细的地域信息
 *  @project:personGocheck
 *  @class:AddressUtils.java
 *  @author:heguanhua E-mail:37809893@qq.com
 *  @date：Nov 14, 2012 6:38:25 PM
 */
public class AddressUtils{  
 /**
  * 
  * @param content
  *            请求的参数 格式为：name=xxx&pwd=xxx
  * @param encoding
  *            服务器端请求编码。如GBK,UTF-8等
  * @return
  * @throws UnsupportedEncodingException
  */
	public String getAddresses(String content, String encodingString) throws UnsupportedEncodingException {
		// 这里调用pconline的接口
		String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
		// 从http://whois.pconline.com.cn取得IP所在的省市区信息
		String returnStr = this.getResult(urlStr, content, encodingString);
		if (returnStr != null) {
			// 处理返回的省市区信息
			System.out.println(returnStr);
			String[] temp = returnStr.split(",");
			if (temp.length < 3) {
				return "0";// 无效IP，局域网测试
			}
			// String region = (temp[5].split(":"))[1].replaceAll("\"", "");
			// region = decodeUnicode(region);// 省份

			String country = "";
			String area = "";
			String region = "";
			String city = "";
			String county = "";
			String isp = "";
		    for(int i=0;i<temp.length;i++){
			    switch(i){ 
			    	case 1:country = (temp[i].split(":"))[2].replaceAll("\"", ""); 
			    	   	   country = decodeUnicode(country);//国家
			    	   	   break; 
			    	case 3:area = (temp[i].split(":"))[1].replaceAll("\"", ""); 
			    		   area = decodeUnicode(area);//地区 
			    		   break; 
			        case 5:region = (temp[i].split(":"))[1].replaceAll("\"", ""); 
			        	   region = decodeUnicode(region);//省份
			        	   break; 
			        case 7:city = (temp[i].split(":"))[1].replaceAll("\"", ""); 
			               city = decodeUnicode(city);//市区 
			               break; 
			        case 9:county = (temp[i].split(":"))[1].replaceAll("\"", ""); 
			               county = decodeUnicode(county);//地区 
			               break; 
			        case 11:isp = (temp[i].split(":"))[1].replaceAll("\"", ""); 
			                isp = decodeUnicode(isp);//ISP公司
			                break; 
			    } 
		    }
		   //System.out.println("1----->"+country);
		   //System.out.println("2----->"+area);
		   //System.out.println("3----->"+region);
		   //System.out.println("4----->"+city);
		   //System.out.println("5----->"+county);
		   //System.out.println("6----->"+isp);
		   return region+city+county;
		}
		return null;
	}
 
	 /** 
	  * 获取当前网络ip 
	  * @param request 
	  * @return 
	  */  
	 public String getIpAddr(HttpServletRequest request){  
	     String ipAddress = request.getHeader("x-forwarded-for");  
	         if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	             ipAddress = request.getHeader("Proxy-Client-IP");  
	         }  
	         if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	             ipAddress = request.getHeader("WL-Proxy-Client-IP");  
	         }  
	         if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
	             ipAddress = request.getRemoteAddr();  
	             if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
	                 //根据网卡取本机配置的IP  
	                 InetAddress inet=null;  
	                 try {  
	                     inet = InetAddress.getLocalHost();  
	                 } catch (UnknownHostException e) {  
	                     e.printStackTrace();  
	                 }  
	                 ipAddress= inet.getHostAddress();  
	             }  
	         }  
	         //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
	         if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
	             if(ipAddress.indexOf(",")>0){  
	                 ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
	             }  
	         }  
	         return ipAddress;   
	 }  
 
 /**
  * @param urlStr
  *            请求的地址
  * @param content
  *            请求的参数 格式为：name=xxx&pwd=xxx
  * @param encoding
  *            服务器端请求编码。如GBK,UTF-8等
  * @return
  */
	private static String getResult(String urlStr, String content, String encoding) {

		try {
			String host = "http://int.dpool.sina.com.cn";
			String path = "/iplookup/iplookup.php?format=json&ip=119.129.74.69";

				Map<String, String> headers = new HashMap<String, String>();
				
				headers.put("Content-Type", "application/json; charset=UTF-8");
				Map<String, String> querys = new HashMap<String, String>();
				String bodys = "";
				HttpResponse response = HttpUtils.doPost(host, path, headers, querys, bodys);
				//	System.out.println("resultStr-->"+EntityUtils.toString(response.getEntity()));
				return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
 /**
  * unicode 转换成 中文
  * 
  * @author fanhui 2007-3-15
  * @param theString
  * @return
  */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}
	
	
	public static String returnAddressByIp(String content, String encodingString) throws UnsupportedEncodingException {
		// 这里调用pconline的接口
		String urlStr = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";
		// 从http://whois.pconline.com.cn取得IP所在的省市区信息
		String returnStr = getResult(urlStr, content, encodingString);
		return returnStr;
	}
	
	
	/**
	 * 根据经度纬度获取地址
	 * @param    lat = "39.983424"; // 纬度 
	 * @param  lng = "116.322987";  // 经度
	 * @return
	 */
	 public static void main(String[] args) {  
	       
	      /*  String add = getAdd("116.3039", "39.97646");  
	        JSONObject jsonObject = JSONObject.fromObject(add);  
	        JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString("addrList"));  
	        JSONObject j_2 = JSONObject.fromObject(jsonArray.get(0));  
	        String allAdd = j_2.getString("admName");  
	        String arr[] = allAdd.split(",");  
	        for (String string : arr) {
				System.out.println(string);
			}
	        System.out.println("省："+arr[0]+"\n市："+arr[1]+"\n区："+arr[2]);  */
		 
		    getAddByTengxun("39.984154","116.307490");
	    }  
	      
	    public static String getAdd(String log, String lat ){  
	        //lat 小  log  大  
	        //参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)  
	        String urlString = "http://gc.ditu.aliyun.com/regeocoding?l="+lat+","+log+"&type=010";  
	        JSONObject salerJson=JSONObject.fromObject(getUrl(urlString));
	        JSONObject jsonData=JSONObject.fromObject(salerJson);
	        /* JSONArray fileItemsjson = JSONArray.fromObject(jsonData.get("addrList").toString());
	        System.out.println("地区"+fileItemsjson);
	        JSONObject job = fileItemsjson.getJSONObject(0);*/
	        return jsonData.toString();
	    }  
	    
	    public static String getUrl(String url) {
	        String resData = null;
	        StringBuffer s = new StringBuffer();
	        BufferedReader bReader = null;
	        try {
	            //114.55.248.182
	            URL urlWeb = new URL(url);
	            URLConnection connection = urlWeb.openConnection();
	            bReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
	            while (null != (resData = bReader.readLine())) {
	                s.append(resData);
	            }
	            bReader.close();
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        System.out.println(s);
	        return s.toString();
	    }
	    
	    //腾达地方的经度和纬度的解析
	    /***
	     * location=lat<纬度>,lng<经度>
	     * 小的，大的
	     * 参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)  
	     * 比如：39.984154,116.307490
	     * **/
	    public static Map<String, Object> getAddByTengxun( String lat ,String log){  
	        //location=lat<纬度>,lng<经度>
	    	//小的，大的
	        //参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)  
	        String urlString = "http://apis.map.qq.com/ws/geocoder/v1/?location=LOCATION&key=KEY&coord_type=Coord_type&get_poi=Get_poi"
	        		+ "&oi_options=Poi_options&output=Output&callback=Callback";
	        urlString  = urlString.replace("LOCATION", lat+","+log)
	        .replace("KEY", Contants.tengxunMapKey)
	        .replace("Coord_type", "1")
	        .replace("Get_poi", "1")
	        .replace("Poi_options", "address_format=short ") 
	        .replace("Output", "json")
	        .replace("Callback", "function1");
		      
	        
	       // JSONObject salerJson=JSONObject.fromObject(getUrl(urlString));
	       // JSONObject jsonData=JSONObject.fromObject(salerJson);
	       try {
	    	   String res = HttpService.doGet(urlString);
		        Map<String, Object> resultMap = JsonUtil.fromJson(res, HashMap.class);
		        if (resultMap == null) {
		            return null;
		        }
		        return resultMap;
	       } catch (Exception e) {
			e.printStackTrace();
			return null;
			// TODO: handle exception
	       }
	    }  
	    
	    
	    
	    public static String getUrl1(String url) {
	        String resData = null;
	        StringBuffer s = new StringBuffer();
	        BufferedReader bReader = null;
	        try {
	            //114.55.248.182
	            URL urlWeb = new URL(url);
	            URLConnection connection = urlWeb.openConnection();
	            bReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
	            while (null != (resData = bReader.readLine())) {
	                s.append(resData);
	            }
	            bReader.close();
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        System.out.println(s);
	        return s.toString();
	    }
}