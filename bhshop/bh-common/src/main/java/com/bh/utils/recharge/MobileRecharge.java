package com.bh.utils.recharge;
/**
 * 话费充值
 */
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;
 
public class MobileRecharge {
 
    public static final String APPKEY = "11e462bfb968f2c4";// 你的appkey
    public static final String URL = "http://api.jisuapi.com/mobilerecharge/recharge";
//    public static final String mobile = "15158825888";// 手机号
//    public static final String amount = "30";// 充值金额
//    public static final String outorderno = "66522311111";// 外部订单号
    public static final String appsecret = "IviLJKuGbwksL0yqc97uP7OopRRaCYir";
    public static Map<String,String> queryarr;
 
    public static String recharge(String amount2,String mobile2,String outorderno2) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        queryarr = new HashMap<String,String>();
        queryarr.put("amount", amount2);
        queryarr.put("mobile", mobile2);
        queryarr.put("outorderno", outorderno2);
        String sign = makeSign(queryarr, appsecret);// 签名 mobile amount
                                                    // outorderno为签名字段，参考下面的签名函数
        String result = null;
        String url = URL + "?appkey=" + APPKEY + "&mobile=" + mobile2 + "&amount=" + amount2 + "&outorderno=" + outorderno2
                + "&sign=" + sign;
        String row = "0";
        try {
            result = HttpUtil.sendGet(url, "utf-8");
            JSONObject json = JSONObject.fromObject(result);
            
            if (json.getInt("status") != 0) {
                System.out.println(json.getString("msg"));                
            } else {
            	String r =  json.getJSONObject("result").getString("rechargestatus");
            	row = r;
            	
                JSONObject resultarr = (JSONObject) json.opt("result");
                String mobile = resultarr.getString("mobile");
                String totalfee = resultarr.getString("totalfee");
                String amount = resultarr.getString("amount");
                String outorderno = resultarr.getString("outorderno");
                String orderno = resultarr.getString("orderno");
                String rechargestatus = resultarr.getString("rechargestatus");
                System.out.println(mobile + " " + amount + " " + outorderno + " " + orderno + " " + totalfee + " "
                        + rechargestatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return row;
    }
 
    public static String makeSign(Map<String, String> queryarr, String appsecret)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        TreeMap map = new TreeMap(queryarr);
        Iterator ir = map.keySet().iterator();
        String str = new String();
        while (ir.hasNext()) {
            Object key = ir.next();
            str += map.get(key);
        }
        str += appsecret;
        return getMd5(str.getBytes());
    }
 
    public static String getMd5(byte[] buffer) throws NoSuchAlgorithmException {
        String s = null;
        char hexDigist[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(buffer);
        byte[] datas = md.digest(); // 16个字节的长整数
        char[] str = new char[2 * 16];
        int k = 0;
        for (int i = 0; i < 16; i++) {
            byte b = datas[i];
            str[k++] = hexDigist[b >>> 4 & 0xf];// 高4位
            str[k++] = hexDigist[b & 0xf];// 低4位
        }
        s = new String(str);
        return s;
    }
//    public static void main(String[] args)throws Exception{
//    	recharge("0.1", "15918402266", "15499");
//	}
}