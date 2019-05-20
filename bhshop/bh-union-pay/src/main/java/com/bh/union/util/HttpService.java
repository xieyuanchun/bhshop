package com.bh.union.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * HttpService 初始化需要在Configure指定密钥的为止，以及密码
 * Created by hupeng on 2015/7/28.
 */
public class HttpService {
    private static Log logger = LogFactory.getLog(HttpService.class);

   // private static CloseableHttpClient httpClient = buildHttpClient();

    private static int socketTimeout = 5000;

    private static int connectTimeout = 5000;

    private static int requestTimeout = 5000;

 /*   public static CloseableHttpClient buildHttpClient() {

        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File(Configure.getCertLocalPath()));
            try {
                keyStore.load(instream, Configure.getCertPassword().toCharArray());
            } finally {
                instream.close();
            }


            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, Configure.getCertPassword().toCharArray())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(requestTimeout)
                    .setSocketTimeout(socketTimeout).build();

            httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setSSLSocketFactory(sslsf)
                    .build();

            return httpClient;
        } catch (Exception e) {
            throw new RuntimeException("error create httpclient......", e);
        }
    }*/
    
    
    public static CloseableHttpClient buildHttpClient() {
    
    	BasicHttpClientConnectionManager  connManager = new BasicHttpClientConnectionManager(
                      RegistryBuilder.<ConnectionSocketFactory>create()
                              .register("http", PlainConnectionSocketFactory.getSocketFactory())
                              .register("https", SSLConnectionSocketFactory.getSocketFactory())
                              .build(),
                      null,
                      null,
                      null
              );
   	 ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {
  	      @Override
  	      public long getKeepAliveDuration(org.apache.http.HttpResponse httpResponse,
  	          HttpContext httpContext) {
  	        return 20 * 1000; // 20 seconds,because tomcat default keep-alive timeout is 20s
  	      }
  	    };
    	CloseableHttpClient httpClient = HttpClientBuilder.create()
                  .setConnectionManager(connManager)
                  .setKeepAliveStrategy(connectionKeepAliveStrategy)
                  .build();
           return httpClient;
        
    }


    public static String doGet(String requestUrl) throws Exception {
        HttpGet httpget = new HttpGet(requestUrl);
        try {


            logger.debug("Executing request " + httpget.getRequestLine());
            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            CloseableHttpClient httpClient = buildHttpClient();
            return httpClient.execute(httpget, responseHandler);
        } finally {
            httpget.releaseConnection();
        }
    }
    
    public static String doSslGet(String requestUrl) throws Exception {
    	CloseableHttpClient httpClient = buildHttpClient();
        if (requestUrl.startsWith("https://")) {
			sslClient(httpClient);
		}
        HttpGet httpget = new HttpGet(requestUrl);
        try {

            
            logger.debug("Executing request " + httpget.getRequestLine());
            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };

            return httpClient.execute(httpget, responseHandler);
        } finally {
            httpget.releaseConnection();
        }
    }
    
    public static byte[] doGetBytes(String requestUrl) throws Exception {
    	RequestConfig requestConfig = RequestConfig.custom()    
                .setSocketTimeout(15000)    
                .setConnectTimeout(15000)    
                .setConnectionRequestTimeout(15000)    
                .build();        
    	HttpPost httpPost = new HttpPost(requestUrl);// 创建httpPost      
    	CloseableHttpClient httpClient = null;    
        CloseableHttpResponse response = null;    
        HttpEntity entity = null;    
        byte[] responseContent = null;    
        try {    
            // 创建默认的httpClient实例.    
            httpClient = HttpClients.createDefault();    
            httpPost.setConfig(requestConfig);    
            // 执行请求    
            response = httpClient.execute(httpPost);    
            entity = response.getEntity();   
            responseContent = EntityUtils.toByteArray(entity);  
        } catch (Exception e) {    
            e.printStackTrace();    
        } finally {    
            try {    
                // 关闭连接,释放资源    
                if (response != null) {    
                    response.close();    
                }    
                if (httpClient != null) {    
                    httpClient.close();    
                }    
            } catch (IOException e) {    
                e.printStackTrace();    
            }    
        }    
        return responseContent;    
    }

/*    public static String doPost(String url, Object object2Xml) {

        String result = null;

        HttpPost httpPost = new HttpPost(url);

        String postDataXML = XMLParser.toXML(object2Xml);

        logger.info("API POST DATA:");
        logger.info(postDataXML);

        StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);

        logger.info("executing request" + httpPost.getRequestLine());

        try {
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            result = EntityUtils.toString(entity, "UTF-8");

        } catch (ConnectionPoolTimeoutException e) {
            logger.error("http get throw ConnectionPoolTimeoutException(wait time out)", e);

        } catch (ConnectTimeoutException e) {
            logger.error("http get throw ConnectTimeoutException", e);

        } catch (SocketTimeoutException e) {
            logger.error("http get throw SocketTimeoutException", e);

        } catch (Exception e) {
            logger.error("http get throw Exception", e);

        } finally {
            httpPost.releaseConnection();
        }

        return result;
    }*/
	private static void sslClient(HttpClient httpClient) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] xcs, String str) {

				}

				public void checkServerTrusted(X509Certificate[] xcs, String str) {

				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry registry = ccm.getSchemeRegistry();
			registry.register(new Scheme("https", 443, ssf));
		} catch (KeyManagementException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}
	 public static String doPostJson(String url, Object object2Json) {

	        String result = null;

	        HttpPost httpPost = new HttpPost(url);
	        String json = JSON.toJSONString(object2Json);
	        System.out.println("request-->"+json);
	    //    String postDataXML = XMLParser.toXML(object2Xml);

	        logger.info("API POST DATA:");
	        logger.info(json);

	        StringEntity postEntity = new StringEntity(json, "UTF-8");
	        httpPost.addHeader("Content-Type", "text/json");
	        httpPost.setEntity(postEntity);

	        logger.info("executing request" + httpPost.getRequestLine());

	        try {
	        	CloseableHttpClient httpClient = buildHttpClient();
	            HttpResponse response = httpClient.execute(httpPost);

	            HttpEntity entity = response.getEntity();

	            result = EntityUtils.toString(entity, "UTF-8");

	        } catch (ConnectionPoolTimeoutException e) {
	            logger.error("http get throw ConnectionPoolTimeoutException(wait time out)", e);

	        } catch (ConnectTimeoutException e) {
	            logger.error("http get throw ConnectTimeoutException", e);

	        } catch (SocketTimeoutException e) {
	            logger.error("http get throw SocketTimeoutException", e);

	        } catch (Exception e) {
	            logger.error("http get throw Exception", e);

	        } finally {
	            httpPost.releaseConnection();
	        }

	        return result;
	    }
    public static void main(String[] args) {
    	int i = 1;
    	double b = (double)i/100;
    	System.out.println(b);
		
	}
}
