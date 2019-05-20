package com.bh.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.bh.jd.util.JDUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetLatitude {
	public static void main(String[] args) {
		// lat 39.97646
		// log 116.3039
		
		//lng (经度) 115.50889266701079 
		//lat (纬度) 25.93677166072324
		//String add = getAdd("116.3039", "39.97646");
		String add = getAdd("116.67789952964417", "23.700043577114195");
		
		JSONObject jsonObject = JSONObject.fromObject(add);
		JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString("addrList"));
		JSONObject j_2 = JSONObject.fromObject(jsonArray.get(0));
		String allAdd = j_2.getString("admName");
		String arr[] = allAdd.split(",");
		System.out.println("省：" + arr[0] + "\n市：" + arr[1] + "\n区：" + arr[2]);
		
		GetLatitude GetLatitude=new GetLatitude();
		
		
		Map<String, String> map=GetLatitude.getGeocoderLatitude("广东省潮州市湘桥区城区");
		
		for (String in : map.keySet()) {
			System.out.println(in+" "+map.get(in));
		}
	}
	/**
	 * 
	 * @Description: 根据经纬度获取地址
	 * @author xieyc
	 * @date 2018年8月7日 下午5:10:11 
	 */
	public static String getAdd(String log, String lat) {
		// lat 小 log 大
		// 参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
		String urlString = "http://gc.ditu.aliyun.com/regeocoding?l=" + lat + "," + log + "&type=010";
		String res = "";
		try {
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			java.io.BufferedReader in = new java.io.BufferedReader(
					new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res += line;
			}
			in.close();
		} catch (Exception e) {
			System.out.println("error in wapaction,and e is " + e.getMessage());
		}
		System.out.println(res);
		return res;
	}
	  

	
	
	
	
    /**
     * 对Map内所有value作utf8编码，拼接返回结果
     * @param data 参数的封装
     * @return 拼接的访问字符串的一部分如：address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakyoursk
     * @throws UnsupportedEncodingException
     */
    public static String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        for (Entry<?, ?> pair : data.entrySet()) {
            queryString.append(pair.getKey() + "=");
            queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8") + "&");
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    /**
     * 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
     * @param md5
     * @return
     */
    private static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    /**
     * 计算sn的值
     * @param paramsMap
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String result(Map paramsMap) throws UnsupportedEncodingException {
        /**
         * 计算sn跟参数对出现顺序有关，get请求请使用LinkedHashMap保存
         * <key,value>，该方法根据key的插入顺序排序；post请使用TreeMap保存
         * <key,value>，该方法会自动将key按照字母a-z顺序排序。所以get请求可自定义参数顺序（sn参数必须在最后）发送请求，
         * 但是post请求必须按照字母a-z顺序填充body（sn参数必须在最后）。以get请求为例：http://api.map.baidu.
         * com/geocoder/v2/?address=百度大厦&output=json&ak=yourak，
         * paramsMap中先放入address，再放output，然后放ak，放入顺序必须跟get请求中对应参数的出现顺序保持一致。
         */
        // 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，拼接返回结果address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourak
        String paramsStr = toQueryString(paramsMap);

        // 对paramsStr前面拼接上/geocoder/v2/?，后面直接拼接yoursk得到/geocoder/v2/?address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakyoursk
        String wholeStr = new String("/geocoder/v2/?" + paramsStr + "8447Bo4MKs0N2wBvw8FHTlmxTTtjVS60");

        // 对上面wholeStr再作utf8编码
        String tempStr = URLEncoder.encode(wholeStr, "UTF-8");

        // 调用下面的MD5方法得到最后的sn签名7de5a22212ffaa9e326444c75a58f9a0
        // System.out.println(MD5(tempStr));
        return MD5(tempStr);
    }
    
    /**
     * 返回输入地址的经纬度坐标 key lng(经度),lat(纬度)
     */
    public static Map<String, String> getGeocoderLatitude(String address) {
        BufferedReader in = null;
        try {
            Map paramsMap = new LinkedHashMap<String, String>();
            paramsMap.put("address", address);
            paramsMap.put("output", "json");
            paramsMap.put("ak", "gxrt9PQoLZugsrfCwoAsm8XV7Nqv38Y0");
            String quest = toQueryString(paramsMap);
            URL tirc = new URL(
                    "http://api.map.baidu.com/geocoder/v2/?" + quest + "&sn=" + GetLatitude.result(paramsMap));

            in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            String str = sb.toString();
            Map<String, String> map = null;
            if (str!=null) {
                int lngStart = str.indexOf("lng\":");
                int lngEnd = str.indexOf(",\"lat");
                int latEnd = str.indexOf("},\"precise");
                if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
                    String lng = str.substring(lngStart + 5, lngEnd);
                    String lat = str.substring(lngEnd + 7, latEnd);
                    map = new HashMap<String, String>();
                    map.put("lng", lng);
                    map.put("lat", lat);
                    return map;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static Map<String,String> getlnglat(String paht){
        Map<String, String> json = getGeocoderLatitude(paht);
        return json;
    }
}
