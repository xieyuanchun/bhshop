package com.bh.utils;


import java.util.Random;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;

public class StringUtil {
	// 生成UUID
	public static String genUuId() {
		String uuid = UUID.randomUUID().toString().toUpperCase().replace("-", "");
		return uuid;
	}

	/**
	 * @param 解码
	 * @return
	 */
	public static byte[] decodeBase64(String str) {
		return Base64.decodeBase64(str);
	}

	/**
	 * 二进制数据编码为BASE64字符串
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String encodeBase64(byte[] bytes) {
		return new String(Base64.encodeBase64(bytes));
	}

	public static String byte2hex(byte[] b) // 二进制转字符串
	{
		StringBuffer sb = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1) {
				sb.append("0" + stmp);
			} else {
				sb.append(stmp);
			}

		}
		return sb.toString();
	}

	public static byte[] hex2byte(String str) { // 字符串转二进制
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 == 1)
			return null;
		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[i / 2] = (byte) Integer.decode("0X" + str.substring(i, i + 2)).intValue();
			}
			return b;
		} catch (Exception e) {
			return null;
		}
	}
	
	/*public static int getMaxVal(int max){
		Random rand = new Random();
		int h = 0;
		if(max>=2){
			if(max>=1000){
				h = max/100;
			}else if(max<1000&&max>=650){
				h=max/60;
			}else if(max<650&&max>=375){
				h=max/40;
			}else if(max<375&&max>=250){
				h=max/30;
			}else{
				h=max/20;
			}
		}
		int val = 1;
		if(h>0){
		  val = rand.nextInt(h);
		  if(val==0&&max>1){
			val = 1;
		  }
		}
		return val;
	}*/
	
	public static int getMaxVal(int totalVal,int totalRecord){
		int val=0;
		if(totalVal<=0){//剩余评分数《=0  测该商品的销量不增加
			return 0;
		}
		if(totalRecord==1){//如果是最后一参与平分销售的商品 ，那么剩余的平分数全部赋值给它
			return totalVal;
		}
		Random rand = new Random();
		int avg=totalVal/totalRecord;
		if(avg>=1000){//平均值《100的时候越细分出现销量不增加的商品的概率越低（不可能避免出现0的情况）
			val = rand.nextInt(100)+avg;
		}else if(avg<1000&&avg>=650){ 
			val = rand.nextInt(75)+avg;
		}else if(avg<650&&avg>=375){
			val = rand.nextInt(50)+avg;
		}else if(avg<375&&avg>=250){
			val = rand.nextInt(30)+avg;
		}else if(avg<250&&avg>=100){
			val = rand.nextInt(25)+avg;
		}else if(avg<100&&avg>=60){
			val = rand.nextInt(20)+avg;
		}else if(avg<60&&avg>=30){
			val = rand.nextInt(15)+avg;
		}else if(avg<30&&avg>=15){
			val = rand.nextInt(10)+avg;
		}else if(avg<15&&avg>=5){
			val = rand.nextInt(5)+avg;
		}else{
			val=rand.nextInt(2)+1;
		}
		
		if(val>totalVal){
			val=totalVal;
		}
		return val;
	}

	public static void main(String[] args) {
		int a=1000;
		int b=300;
		int count=0;
		for(int i=0;i<300;i++){
			int ret = getMaxVal(a, b);
			if(ret<0){
				System.out.println("负数:"+ret);
				return;
			}
			System.out.println(ret);
			a = a - ret;
			b = b - 1;
			count += ret;
		}
		System.out.println("参与平分的销量："+count);
	}



}
