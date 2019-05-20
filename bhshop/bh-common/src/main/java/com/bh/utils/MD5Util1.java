package com.bh.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MD5Util1 {

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
		
		public final static String encodesecond(String s) {
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
		
		
		public static String genCodes(String string){
	         			
			String source = string;
			int sourceLength = source.length();
			int randomLength = 6;			
			Random rand = new Random();
			StringBuilder result = new StringBuilder();
			for(int i=0;i<randomLength;){
				int randomNum = rand.nextInt(randomLength);
				if(randomNum >= sourceLength){
					continue;
				}
				i++;
				result.append(source.charAt(randomNum));
			}			
			String r = result.toString();
			return r;
		}
	                     	               
	   

		public static void main(String[] arg){
			System.out.println(MD5Util1.encode("123456"));			
			String str = MD5Util1.encode("1234566");			
			String sub = genCodes(str);
			str += str.endsWith(sub);
			System.out.println(MD5Util1.encode(str));
			
			System.out.println("随机截取6位后的:" + sub);
		}
}
