package com.bh.utils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;




import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;


import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
public class HttpClientUtil {
    
    private static CookieStore cookieStore= new BasicCookieStore();
      
    public static String getCookie(String name){
    	    
    	  List<Cookie> cookies =  cookieStore.getCookies();
        
    	  for(Cookie cookie : cookies){
             
    		  if(cookie.getName().equalsIgnoreCase(name)){
               
    			  return cookie.getValue();
              }
          }
    	  

        return null;
        
    }
    
	public static String doGet(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();;
		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();
			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);
			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(),
						"UTF-8");
			}
		} catch (Exception e) {
			LoggerUtil.error(e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				LoggerUtil.error(e);
			}
		}
		return resultString;
	}

	public static String doGet(String url) {
		return doGet(url, null);
	}
	public static String doPost(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();;
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建参数列表
			if (param != null) {
				List paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
						paramList);
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpclient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			LoggerUtil.error(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LoggerUtil.error(e);
			}
		}
		return resultString;
	}
	
	public static String doPostbyFrom(String url, MultipartEntity entity) {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();;
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(entity);

			// 执行http请求
			response = httpclient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			LoggerUtil.error(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LoggerUtil.error(e);
			}
		}
		return resultString;
	}
	
	
	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();;
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json,
					ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
		 // 执行http请求
         response = httpclient.execute(httpPost);
         resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		       
		} catch (Exception e) {
			LoggerUtil.error(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LoggerUtil.error(e);
			}
		}
		return resultString;
	}
	
    public static String doPost(String url) {
		return doPost(url, null);
	}
    
    
    public static String doPostJsonByCookie(String url, String json,List<String> cookies) {
    	
    	CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();;
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json,
					ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
		 // 执行http请求
         response = httpclient.execute(httpPost);
         resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		 if (response!=null && !response.equals("")) {
			 String value = "";	
			 
	         Header[] headers = response.getHeaders("Set-Cookie");   		 
			 
	         for (int i = 0; i < headers.length; i++) {
	        	  
	           
	             cookies.add(headers[i].getValue());
	        
			}
			 
			
						 	 
		 }
		       
		} catch (Exception e) {
			LoggerUtil.error(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LoggerUtil.error(e);
			}
		}
		return resultString;
    	
    	
    }
    
	
} 
