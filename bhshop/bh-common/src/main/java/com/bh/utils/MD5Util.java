package com.bh.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	// 获得MD5摘要算法的 MessageDigest 对象
		private static MessageDigest _mdInst = null;
		private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		private static MessageDigest getMdInst() {
			if (_mdInst == null) {
				try {
					_mdInst = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {
					LoggerUtil.error(e);
				}
			}
			return _mdInst;
		}

		public final static String encode(String s) {
			try {
				byte[] btInput = s.getBytes();
				// 使用指定的字节更新摘要
				getMdInst().update(btInput);
				// 获得密文
				byte[] md = getMdInst().digest();
				// 把密文转换成十六进制的字符串形式
				int j = md.length;
				char str[] = new char[j * 2];
				int k = 0;
				for (int i = 0; i < j; i++) {
					byte byte0 = md[i];
					str[k++] = hexDigits[byte0 >>> 4 & 0xf];
					str[k++] = hexDigits[byte0 & 0xf];
				}
				return new String(str);
			} catch (Exception e) {
				LoggerUtil.error(e);
				return null;
			}
		}

		public static void main(String[] arg){
			System.out.print(MD5Util.encode("abc这是一个测试123"));
		}
		
		 /** 
	     * MD5 加密 
	     */  
	 public static String getMD5Str(String str) {  
	        MessageDigest messageDigest = null;  

	        try {  
	            messageDigest = MessageDigest.getInstance("MD5");  

	            messageDigest.reset();  

	            messageDigest.update(str.getBytes("UTF-8"));  
	        } catch (NoSuchAlgorithmException e) {  
	            System.out.println("NoSuchAlgorithmException caught!");  
	            System.exit(-1);  
	        } catch (UnsupportedEncodingException e) {  
	            e.printStackTrace();  
	        }  

	        byte[] byteArray = messageDigest.digest();  

	        StringBuffer md5StrBuff = new StringBuffer();  

	        for (int i = 0; i < byteArray.length; i++) {              
	            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
	                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
	            else  
	                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
	        }  

	        return md5StrBuff.toString();  
	    }  
}
