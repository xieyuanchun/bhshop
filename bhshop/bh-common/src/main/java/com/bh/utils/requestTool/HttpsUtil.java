package com.bh.utils.requestTool;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解决httpClient对https请求报不支持SSLv3问题.
 * JDK_HOME/jrebcurity/java.security 文件中注释掉：
 * jdk.certpath.disabledAlgorithms=MD2
 * jdk.tls.disabledAlgorithms=DSA(或jdk.tls.disabledAlgorithms=SSLv3)
 */
public class HttpsUtil {
    static String host = "localhost";
    static Integer port = 28081;
    static boolean userPoxy = false;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public static boolean isUserPoxy() {
        return userPoxy;
    }

    public static void setUserPoxy(boolean isUserPoxy) {
        userPoxy = isUserPoxy;
    }

    public static CloseableHttpClient createClient() throws Exception {
        TrustStrategy trustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] xc, String msg)
                    throws CertificateException {
                return true;
            }
        };
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(trustStrategy);
        HostnameVerifier hostnameVerifierAllowAll = new HostnameVerifier() {
            @Override
            public boolean verify(String name, SSLSession session) {
                return true;
            }
        };
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1",
                "TLSv1.1", "TLSv1.2"}, null, hostnameVerifierAllowAll);

        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(
                    IOException exception,
                    int executionCount,
                    HttpContext context) {
                //重试设置
                if (executionCount >= 5) {
                    // Do not retry if over max retry count
                    return false;
                }
                if (exception instanceof InterruptedIOException) {
                    // Timeout
                    return false;
                }
                if (exception instanceof UnknownHostException) {
                    // Unknown host
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {
                    // Connection refused
                    return false;
                }
                if (exception instanceof SSLException) {
                    // SSL handshake exception
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    return true;
                }
                return false;
            }
        };
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(120000)
                .setSocketTimeout(120000)//超时设置
                .build();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setRetryHandler(myRetryHandler)//重试设置
                .setDefaultRequestConfig(requestConfig)
                .build();
        return httpclient;
    }

    public static String get(String url) throws Exception {
        return get(url, null, null);
    }

    public static String get(String url, Map<String, String> header, Map<String, String> outCookies) throws Exception {
        String body = "";
        String Encoding = "utf-8";
        CloseableHttpClient client = createClient();
        try {
            CookieStore cookieStore = new BasicCookieStore();
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setCookieStore(cookieStore);
            // 创建get方式请求对象
            HttpGet httpGet = new HttpGet(url);
            if (header != null) {
                if (header.get("Host") != null) httpGet.setHeader("Host", header.get("Host"));
                if (header.get("Accept") != null) httpGet.setHeader("Accept", header.get("Accept"));
                if (header.get("Origin") != null) httpGet.setHeader("Origin", header.get("Origin"));
                if (header.get("Accept-Encoding") != null)
                    httpGet.setHeader("Accept-Encoding", header.get("Accept-Encoding"));
                if (header.get("Accept-Language") != null)
                    httpGet.setHeader("Accept-Language", header.get("Accept-Language"));
                if (header.get("User-Agent") != null) httpGet.setHeader("User-Agent", header.get("User-Agent"));
                if (header.get("x-requested-with") != null)
                    httpGet.setHeader("x-requested-with", header.get("x-requested-with"));
                if (header.get("Encoding") != null) Encoding = header.get("Encoding");
                if (header.get("Cookie") != null) httpGet.setHeader("Cookie", header.get("Cookie"));
            }
            System.out.println("请求地址：" + url);
            // 执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpGet, localContext);
            // 获取结果实体
            try {
                // 如果需要输出cookie
                if (outCookies != null) {
                    List<Cookie> cookies = cookieStore.getCookies();
                    for (int i = 0; i < cookies.size(); i++) {
                        outCookies.put(cookies.get(i).getName(), cookies.get(i).getValue());
                    }
                }
                HttpEntity entity = response.getEntity();
                System.out.println("返回：" + response.getStatusLine());
                if (entity != null) {
                    // 按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity, Encoding);
                    // System.out.println("返回："+body);
                }
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return body;
    }

    public static String httpPost(String url, Map<String, String> params, Map<String, String> header, Map<String, String> outCookies, String charset)
            throws Exception {
        String body = "";
        String encoding = charset;
        String contentType = header.get("Content-Type");
        CloseableHttpClient client = createClient();
        CookieStore cookieStore = new BasicCookieStore();
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setCookieStore(cookieStore);
        try {
            // 创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);
            if (header != null) {
                String key;
                String value;
                Iterator iter = header.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    key = (String) entry.getKey();
                    value = (String) entry.getValue();
                    if (!key.equalsIgnoreCase("Content-Length") && !key.equalsIgnoreCase("Accept") && !key.equalsIgnoreCase("Accept-Encoding")) {
                        httpPost.setHeader(key, value);
                    }
                }
            }
            // 装填参数
            if (contentType.equalsIgnoreCase("text/html")) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                if (params != null) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    }
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
            } else if (contentType.equalsIgnoreCase("application/json")) {  //JOSN格式参数
                StringEntity myEntity = new StringEntity(params.get("data"),
                        ContentType.create("application/json", "GBK"));
                httpPost.setEntity(myEntity);
            } else {
                //设置参数
                String data = new String(params.get("data").getBytes("UTF-8"), "UTF-8");
                StringEntity stringEntity = new StringEntity(data);
                stringEntity.setContentType(contentType);
                httpPost.setEntity(stringEntity);
            }
            System.out.println("请求地址：" + url);
            // 执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpPost, localContext);
            // 获取结果实体
            try {
                // 如果需要输出cookie
                if (outCookies != null) {
                    List<Cookie> cookies = cookieStore.getCookies();
                    for (int i = 0; i < cookies.size(); i++) {
                        outCookies.put(cookies.get(i).getName(), cookies.get(i).getValue());
                    }
                }
                HttpEntity entity = response.getEntity();
                System.out.println("返回：" + response.getStatusLine());
                if (entity != null) {
                    // 按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity, encoding);
                    // System.out.println("返回："+body);
                }
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
        return body;
    }


    public static void main(String[] args) throws Exception {
        String body = get("https://www.baidu.com/");
        System.out.println(body);
    }

    public static String httpUrlConn(String strUrl, String method, Map<String, String> params, Map<String, String> header, Map<String, String> outCookies,
                                     Map<String, List<String>> mapListHeadsFields, String charset, boolean blnRedirect)
            throws Exception {
        String strResult = "";
        String contentType = header.get("Content-Type");
        //打开连接
        URL url = new URL(strUrl);
        HttpURLConnection httpUrlConn = null;
        if (strUrl.toLowerCase().startsWith("https")) {
            if (userPoxy) {
                String strProxy = "localhost";
                String[] proxyArray = StringUtils.split(strProxy, ":");
                if (proxyArray.length >= 2) {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyArray[0], Integer.valueOf(proxyArray[1])));
                    httpUrlConn = (HttpsURLConnection) url.openConnection(proxy);
                } else {
                    httpUrlConn = (HttpsURLConnection) url.openConnection();
                }
            } else {
                httpUrlConn = (HttpsURLConnection) url.openConnection();
            }
        } else {
            if (userPoxy) {
                String strProxy = "localhost";
                String[] proxyArray = StringUtils.split(strProxy, ":");
                if (proxyArray.length >= 2) {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyArray[0], Integer.valueOf(proxyArray[1])));
                    httpUrlConn = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    httpUrlConn = (HttpURLConnection) url.openConnection();
                }
            } else {
                httpUrlConn = (HttpURLConnection) url.openConnection();
            }
        }
        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);
        httpUrlConn.setInstanceFollowRedirects(blnRedirect);  // 必须设置false，否则会自动redirect到Location的地址
        String pragma = header.get("Pragma");
        if (pragma != null && pragma.equalsIgnoreCase("no-cache")) {
            httpUrlConn.setUseCaches(false);
        }
        httpUrlConn.setConnectTimeout(50000);//设置连接超时,如果在建立连接之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
        httpUrlConn.setReadTimeout(50000);//设置读取超时,如果在数据可读取之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
        httpUrlConn.setRequestMethod(method);  // 设置请求方式（GET/POST）

        try {
            String data = params.get("data");
            //设置头
            if (header != null) {
                String key;
                String value;
                Iterator iter = header.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    key = (String) entry.getKey();
                    value = (String) entry.getValue();
                    if (!key.equalsIgnoreCase("Content-Length") && !key.equalsIgnoreCase("Accept-Encoding")) {
                        httpUrlConn.setRequestProperty(key, value);
                    }
                }
                if (StringUtils.isNotBlank(data)) {
                    httpUrlConn.setRequestProperty("Content-Length", String.valueOf(data.length()));
                }

            }
            // 装填参数
            if (StringUtils.isNoneBlank(contentType)) {
                if (contentType.equalsIgnoreCase("text/html")) {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    if (params != null) {
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                        }
                    }
                } else if (contentType.equalsIgnoreCase("application/json")) {  //JOSN格式参数
                    StringEntity myEntity = new StringEntity(data,
                            ContentType.create("application/json", charset));
                } else {
                    //设置参数
                    if (StringUtils.isNoneEmpty(data)) {
                        OutputStreamWriter osw = new OutputStreamWriter(httpUrlConn.getOutputStream(), charset);
                        osw.write(data);
                        osw.flush();
                        osw.close();
                    }
                }
            }
            int retCode = httpUrlConn.getResponseCode();
            header.put("redirectUrl", httpUrlConn.getURL().toString());  //读取跳转后的地址
            mapListHeadsFields.putAll(httpUrlConn.getHeaderFields());
            if (!(retCode == HttpURLConnection.HTTP_OK || retCode == HttpURLConnection.HTTP_MOVED_TEMP || retCode == HttpURLConnection.HTTP_BAD_REQUEST || retCode == HttpURLConnection.HTTP_SEE_OTHER)) {
                System.out.println("http请求出错，返回retCode=" + retCode);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream(), charset));
            StringBuffer stringBuffer = new StringBuffer();
            String strTemp = "";
            while ((strTemp = br.readLine()) != null) {
                stringBuffer.append(strTemp);
            }
            strResult = stringBuffer.toString();
            br.close();
            setCookies(httpUrlConn.getHeaderFields(), outCookies); //处理cookie

        } finally {
            if (httpUrlConn != null) httpUrlConn.disconnect();
        }
        return strResult;
    }

    /**
     * 读取cookies
     */

    private static void setCookies(Map<String, List<String>> mapListHeadsFields, Map<String, String> outCookies) {
        if (mapListHeadsFields != null && mapListHeadsFields.size() > 0) {
            List<String> listCookies = mapListHeadsFields.get("Set-Cookie");
            if (listCookies != null) {
                int size = listCookies.size();
                for (int i = 0; i < size; i++) {
                    String[] arraySession = listCookies.get(i).split(";");
                    if (arraySession.length > 0) { //只需要第一列
                        int post = arraySession[0].indexOf("=");
                        if (post > 0) {
                            String key = arraySession[0].substring(0, post);
                            String value = arraySession[0].substring(post + 1, arraySession[0].length());
                            outCookies.put(key, value);
                        }
                    }
                }
            }
        }

    }


    /**
     * http下载文件
     *
     * @param strUrl
     * @param method
     * @param params
     * @param header
     * @param outCookies
     * @param charset
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String httpUrlDownFile(String strUrl, String
            method, Map<String, String> params, Map<String, String> header, Map<String, String> outCookies, Map<String, List<String>> mapListHeadsFields, String
                                                 charset, String fileName)
            throws Exception {
        String strResult = "";
        String contentType = header.get("Content-Type");
        //打开连接
        URL url = new URL(strUrl);
        HttpURLConnection httpUrlConn = null;
        if (strUrl.toLowerCase().startsWith("https")) {
//            trustAllHttpsCertificates();
//            HostnameVerifier hv = new HostnameVerifier() {
//                public boolean verify(String urlHostName, SSLSession session) {
//                    System.out.println("Warning: URL Host: " + urlHostName + " vs. "
//                            + session.getPeerHost());
//                    return true;
//                }
//            };
            //  HttpsURLConnection.setDefaultHostnameVerifier(hv);
            httpUrlConn = (HttpsURLConnection) url.openConnection();
        } else {
            httpUrlConn = (HttpURLConnection) url.openConnection();
        }
        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);
        httpUrlConn.setInstanceFollowRedirects(false);  // 必须设置false，否则会自动redirect到Location的地址-- 文件下载不一定
        String pragma = header.get("Pragma");
        if (pragma != null && pragma.equalsIgnoreCase("no-cache")) {
            httpUrlConn.setUseCaches(false);
        }
        httpUrlConn.setConnectTimeout(50000);//设置连接超时,如果在建立连接之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
        httpUrlConn.setReadTimeout(50000);//设置读取超时,如果在数据可读取之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
        httpUrlConn.setRequestMethod(method);  // 设置请求方式（GET/POST）

        try {
            String data = params.get("data");
            //设置头
            if (header != null) {
                String key;
                String value;
                Iterator iter = header.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    key = (String) entry.getKey();
                    value = (String) entry.getValue();
                    if (!key.equalsIgnoreCase("Content-Length") && !key.equalsIgnoreCase("Accept") && !key.equalsIgnoreCase("Accept-Encoding")) {
                        httpUrlConn.setRequestProperty(key, value);
                    }
                }
                if (StringUtils.isNotBlank(data)) {
                    httpUrlConn.setRequestProperty("Content-Length", String.valueOf(data.length()));
                }
            }
            // 装填参数
            if (StringUtils.isNoneBlank(contentType)) {
                if (contentType.equalsIgnoreCase("text/html")) {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    if (params != null) {
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                        }
                    }
                } else if (contentType.equalsIgnoreCase("application/json")) {  //JOSN格式参数
                    StringEntity myEntity = new StringEntity(data,
                            ContentType.create("application/json", charset));
                } else {
                    //设置参数
                    OutputStreamWriter osw = new OutputStreamWriter(httpUrlConn.getOutputStream(), charset);
                    osw.write(data);
                    osw.flush();
                    osw.close();
                }
            }
            int retCode = httpUrlConn.getResponseCode();
            mapListHeadsFields.putAll(httpUrlConn.getHeaderFields());
            if (retCode == HttpURLConnection.HTTP_OK || retCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                //文件保存位置
                File file = new File(fileName);
                //创建路径
                File path = file.getAbsoluteFile();
                if (!path.exists()) { //目录不存在创建目录
                    path.mkdir();
                }
                if (file.exists()) { //删除文件
                    file.delete();
                }
                //修正为连续下载大文件
                RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
                int bytesRead;
                BufferedInputStream bis = new BufferedInputStream(httpUrlConn.getInputStream());
                byte[] buff = new byte[1024];
                long offset = 0;
                while ((bytesRead = bis.read(buff, 0, buff.length)) != -1) {
                    raf.seek(offset);
                    raf.write(buff, 0, bytesRead);
                    offset = offset + bytesRead;
                }
                raf.close();
                buff = null;
                strResult = "ok";
                setCookies(httpUrlConn.getHeaderFields(), outCookies); //处理cookie
            }
        } finally {
            if (httpUrlConn != null) httpUrlConn.disconnect();
        }
        return strResult;
    }

    /**
     * http上传文件
     *
     * @param strUrl
     * @param method
     * @param params
     * @param header
     * @param outCookies
     * @param charset
     * @param file       要上传的文件
     * @return
     * @throws Exception
     */
    public static String httpUrlUploadFile(String strUrl, String
            method, Map<String, String> params, Map<String, String> header, Map<String, String> outCookies, Map<String, List<String>> mapListHeadsFields, String
                                                   charset, String fileFormName, File file)
            throws Exception {
        String strResult = "";
        String contentType = header.get("Content-Type");
        //打开连接
        URL url = new URL(strUrl);
        HttpURLConnection httpUrlConn = null;
        if (strUrl.toLowerCase().startsWith("https")) {
            httpUrlConn = (HttpsURLConnection) url.openConnection();
        } else {
            httpUrlConn = (HttpURLConnection) url.openConnection();
        }
        httpUrlConn.setDoOutput(true);
        httpUrlConn.setDoInput(true);

        String pragma = header.get("Pragma");
        if (pragma != null && pragma.equalsIgnoreCase("no-cache")) {
            httpUrlConn.setUseCaches(false);
        }
        httpUrlConn.setConnectTimeout(50000);//设置连接超时,如果在建立连接之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
        httpUrlConn.setReadTimeout(50000);//设置读取超时,如果在数据可读取之前超时期满，则会引发一个 java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
        httpUrlConn.setRequestMethod(method);  // 设置请求方式（GET/POST）

        try {
            //设置头
            if (header != null) {
                String key;
                String value;
                Iterator iter = header.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    key = (String) entry.getKey();
                    value = (String) entry.getValue();
                    if (!key.equalsIgnoreCase("Content-Length") && !key.equalsIgnoreCase("Accept") && !key.equalsIgnoreCase("Accept-Encoding")) {
                        httpUrlConn.setRequestProperty(key, value);
                    }
                }
                if (file.exists()) {
                    httpUrlConn.setRequestProperty("Content-Length", String.valueOf(file.length()));
                }
            }
            // 装填参数
            if (StringUtils.isNoneBlank(contentType)) {
                if (contentType.equalsIgnoreCase("text/html")) {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    if (params != null) {
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                        }
                    }
                } else {
                    if (file.exists()) {
                        String boundary =null;
                        Pattern pattern = Pattern.compile("boundary=----(\\S+)");
                        Matcher matcher = pattern.matcher(contentType);
                        while (matcher.find()) {
                            boundary = matcher.group(1);
                        }
                        writeToServer(httpUrlConn, boundary, fileFormName, charset, file);
                    }
                }
            }
            int retCode = httpUrlConn.getResponseCode();
            mapListHeadsFields.putAll(httpUrlConn.getHeaderFields());
            if (retCode == HttpURLConnection.HTTP_OK || retCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream(), charset));
                StringBuffer stringBuffer = new StringBuffer();
                String strTemp = "";
                while ((strTemp = br.readLine()) != null) {
                    stringBuffer.append(strTemp);
                }
                strResult = stringBuffer.toString();
                br.close();
                setCookies(httpUrlConn.getHeaderFields(), outCookies); //处理cookie
            }
        } finally {
            if (httpUrlConn != null) httpUrlConn.disconnect();
        }
        return strResult;
    }

    private static void writeToServer(HttpURLConnection httpUrlConn, String boundary, String fileFormName, String charset, File file) throws IOException {
        OutputStream out = new DataOutputStream(httpUrlConn.getOutputStream());
        // 传输内容
        StringBuffer contentBody = new StringBuffer("\r\n------" + boundary);
        String boundaryMessage1 = "\r\n------" + boundary;
        out.write(boundaryMessage1.getBytes(charset));
        contentBody.setLength(0); //清空
        contentBody.append("\r\n").append("Content-Disposition: form-data; name=\"").append(fileFormName).append("\"; filename=\"")
                .append(file.getName() + "\"").append("\r\nContent-Type: application/octet-stream").append("\r\n\r\n");

        String boundaryMessage2 = contentBody.toString();
        out.write(boundaryMessage2.getBytes(charset));
        // 开始真正向服务器写文件
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        byte[] bufferOut = new byte[(int) file.length()];
        int bytes = dis.read(bufferOut);
        out.write(bufferOut, 0, bytes);
        fis.close();
        dis.close();
        contentBody.setLength(0);//清空
        contentBody.append("\r\n------" + boundary + "--\r\n");
        String boundaryMessage3 = contentBody.toString();
        out.write(boundaryMessage3.getBytes(charset));
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[512];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
            buffer = null;
        }
        bos.close();
        return bos.toByteArray();
    }

    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
}