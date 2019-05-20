
package com.bh.utils.requestTool;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/25.
 */
public class RequestTool {
    private final String RAW_FILE_NAME = "C:\\Program Files (x86)\\Fiddler2\\RawFile.htm";
    private final String DEFAULT_CHARSET = "UTF-8";
    private String method; //post or GET
    private String url;
    private Map<String, String> header = new HashMap<String, String>();
    private Map<String, String> params = new HashMap<String, String>();
    private Map<String, List<String>> mapListHeadsFields = new HashMap<String, List<String>>();
    private Map<String, String> outCookies = new HashMap<String, String>();
    private boolean blnRedirect = false; //是否重定向
    private String fileFormName; //上传文件参数名
    private boolean blnUseProxy = false; //是否使用代理
    private String redirectUrl = "";  //跳转后的地址

    public RequestTool() {
    }

    public RequestTool(String url) {
        this.url = url;
    }

    public String getRAW_FILE_NAME() {
        return RAW_FILE_NAME;
    }

    public String getDEFAULT_CHARSET() {
        return DEFAULT_CHARSET;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getOutCookies() {
        return outCookies;
    }

    public void setOutCookies(Map<String, String> outCookies) {
        this.outCookies = outCookies;
    }

    public Map<String, List<String>> getMapListHeadsFields() {
        return mapListHeadsFields;
    }

    public void setMapListHeadsFields(Map<String, List<String>> mapListHeadsFields) {
        this.mapListHeadsFields = mapListHeadsFields;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isBlnRedirect() {
        return blnRedirect;
    }

    public void setBlnRedirect(boolean blnRedirect) {
        this.blnRedirect = blnRedirect;
    }

    public boolean isBlnUseProxy() {
        return blnUseProxy;
    }

    public void setBlnUseProxy(boolean blnUseProxy) {
        HttpsUtil.setUserPoxy(blnUseProxy);
    }

    public String getRedirectUrl() {
        return header.get("redirectUrl");
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    //凑返回的cookie
    public String getStrOutCookie() {
        String ret = "";
        String key;
        String value;
        for (Map.Entry<String, String> entry : outCookies.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            ret = ret + key + "=" + value + "; ";
        }
        return ret;
    }

    /**
     * 读取请求裸文件的http请求到内存
     *
     * @param rawFileName
     */
    public void readRequestRaw(String rawFileName) {
        BufferedReader reader = null;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(rawFileName), "GBK");
            reader = new BufferedReader(isr);
            String line;
            boolean isLastLine = false;
            int lineNum = 0;
            String data = "";
            while ((line = reader.readLine()) != null) {
                if (lineNum == 0) { //第一行
                    String[] strArray = StringUtils.split(line, " ");
                    if (strArray.length == 3) {
                        url = strArray[1];
                        method = strArray[0];
                    }
                } else {
                    if (isLastLine) {//最后一行了
                        if (StringUtils.isNotEmpty(line)) {
                            if (data.isEmpty()) {
                                data = line;
                            } else {
                                data = data + "\r\n" + line;
                                if (line.contains("Content-Disposition")) {
                                    Pattern pattern = Pattern.compile("name=\"(\\S*)\"; ");
                                    Matcher matcher = pattern.matcher(line);
                                    while (matcher.find()) {
                                        fileFormName = matcher.group(1);
                                    }
                                }
                            }
                            params.put("data", data);
                        }
                    } else if (StringUtils.isBlank(line)) { //倒数第二行.下一行是最后一行了
                        isLastLine = true;
                    } else {
                        String[] strArray = StringUtils.split(line, ": ");
                        header.put(strArray[0], line.replaceFirst(strArray[0] + ": ", ""));
                    }
                }
                lineNum++; //函数加1
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //关闭Reader出现的异常一般不需要处理。
                }
            }
        }
    }


    /**
     * 模拟一个请求
     *
     * @param charset 字符集
     * @param method  提交方式：传入"POST"或 "GET"
     * @return
     */
    public String simulate(String charset, String method) {
        this.method = method;
        String result = null;
        try {
            result = HttpsUtil.httpUrlConn(url, method, params, header, outCookies, mapListHeadsFields, charset, blnRedirect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 模拟一个请求
     *
     * @param charset 字符集
     * @return
     */
    public String simulate(String charset) {
        return simulate(charset, method); //method为读取配置RawFile的读取值
    }

    /**
     * 模拟一个请求
     *
     * @return
     */
    public String simulate() {
        return simulate(DEFAULT_CHARSET);
    }


    /**
     * 按默认字符集值模拟一个post请求
     *
     * @return
     */
    public String postSimulate() {
        return simulate(DEFAULT_CHARSET, "POST");
    }

    /**
     * 按指定字符集模拟一个post请求
     *
     * @param charset
     * @return
     */
    public String postSimulate(String charset) {
        return simulate(charset, "POST");
    }

    /**
     * 按默认字符集值模拟一个get请求
     *
     * @return
     */
    public String getSimulate() {
        return simulate(DEFAULT_CHARSET, "GET");
    }


    /**
     * 按指定字符集模拟一个GET请求
     *
     * @param charset
     * @return
     */
    public String getSimulate(String charset) {
        return simulate(charset, "GET");
    }


    /**
     * 下载远程地址到指定文件保存
     *
     * @return
     */
    public String downloadFile(String fileName) {
        String result = null;
        try {
            result = HttpsUtil.httpUrlDownFile(url, method, params, header, outCookies, mapListHeadsFields, DEFAULT_CHARSET, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 上传指定文件
     *
     * @return
     */
    public String uploadFile(File file) {
        String result = null;
        try {
            result = HttpsUtil.httpUrlUploadFile(url, method, params, header, outCookies, mapListHeadsFields, DEFAULT_CHARSET, fileFormName, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}