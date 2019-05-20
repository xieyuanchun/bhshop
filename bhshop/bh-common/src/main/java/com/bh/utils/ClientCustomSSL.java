package com.bh.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
public class ClientCustomSSL {
	 public final static void main(String[] args) throws Exception {
		 post();
	    }
	 
	 public static void post() throws Exception{
		 CloseableHttpResponse response = null;
	      KeyStore keyStore  = KeyStore.getInstance("PKCS12");
	        FileInputStream instream = new FileInputStream(new File("F:/zy/weixincert/apiclient_cert.p12"));
	        try {
	            keyStore.load(instream, "1456013002".toCharArray());
	        } finally {
	            instream.close();
	        }

	        // Trust own CA and all self-signed certs
	        SSLContext sslcontext = SSLContexts.custom()
	                .loadKeyMaterial(keyStore, "1456013002".toCharArray())
	                .build();
	        // Allow TLSv1 protocol only
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	                sslcontext,
	                new String[] { "TLSv1" },
	                null,
	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        CloseableHttpClient httpclient = HttpClients.custom()
	                .setSSLSocketFactory(sslsf)
	                .build();
	        try {

	       //     HttpPost httpget = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");

	         // 创建Http Post请求
				HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
				// 创建参数列表
				/*if (param != null) {
					List paramList = new ArrayList<>();
					for (String key : param.keySet()) {
						paramList.add(new BasicNameValuePair(key, param.get(key)));
					}
					// 模拟表单
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
							paramList);
					httpPost.setEntity(entity);
				}*/
				// 执行http请求
				response = httpclient.execute(httpPost);
				String resultString = EntityUtils.toString(response.getEntity(), "utf-8");
				System.out.println("resultString--->"+resultString);

	           
	        } finally {
	            httpclient.close();
	        }
	 }
	 
	 
	/* public static void post() throws Exception{
	      KeyStore keyStore  = KeyStore.getInstance("PKCS12");
	        FileInputStream instream = new FileInputStream(new File("F:/zy/weixincert/apiclient_cert.p12"));
	        try {
	            keyStore.load(instream, "1456013002".toCharArray());
	        } finally {
	            instream.close();
	        }

	        // Trust own CA and all self-signed certs
	        SSLContext sslcontext = SSLContexts.custom()
	                .loadKeyMaterial(keyStore, "1456013002".toCharArray())
	                .build();
	        // Allow TLSv1 protocol only
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	                sslcontext,
	                new String[] { "TLSv1" },
	                null,
	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        CloseableHttpClient httpclient = HttpClients.custom()
	                .setSSLSocketFactory(sslsf)
	                .build();
	        try {

	            HttpPost httpget = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");

	            System.out.println("executing request" + httpget.getRequestLine());

	            CloseableHttpResponse response = httpclient.execute(httpget);
	            try {
	                HttpEntity entity = response.getEntity();

	                System.out.println("----------------------------------------");
	                System.out.println(response.getStatusLine());
	                if (entity != null) {
	                    System.out.println("Response content length: " + entity.getContentLength());
	                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
	                    String text;
	                    while ((text = bufferedReader.readLine()) != null) {
	                        System.out.println(text);
	                    }
	                   
	                }
	                EntityUtils.consume(entity);
	            } finally {
	                response.close();
	            }
	        } finally {
	            httpclient.close();
	        }
	 }*/
}
