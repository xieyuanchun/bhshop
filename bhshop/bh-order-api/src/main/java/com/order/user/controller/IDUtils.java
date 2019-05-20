package com.order.user.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.bh.utils.StringUtil;

public class IDUtils {
	private static String orderNoPre="3134";
//	private static String orderNoPre="";
	public static String getOrderNo(){
		DateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS");
		int rand = (int)(Math.random()*100);
		String randStr="";
		if(rand<10){
			randStr = "0"+rand;
		}else{
			randStr = rand+"";
		}
		String oid = orderNoPre+df.format(new Date())+randStr;
		return oid;
	}
	public static String getOrderNo(String inOrderNoPre){
		if(StringUtils.isEmpty(inOrderNoPre)){
			inOrderNoPre = orderNoPre;
		}
		DateFormat df = new SimpleDateFormat("yyMMddHHmmssSSS");
		int rand = (int)(Math.random()*10);
		String randStr=""+rand;
		String oid = inOrderNoPre+df.format(new Date())+randStr;
		return oid;
	}
	
	/*public static String sjs() {
		Random rand = new Random();
		String str = "0123456789";
		String result = "";
		for (int i = 0; i < 2; i++) {
			String randStr = Integer.valueOf(rand.nextInt(str.length())).toString();
			result += randStr;
		}
		return result;
	}*/

	
	public static void main(String[] args) {
		//for(int i=0;i< 100;i++)
		//System.out.println(genItemId());
		System.out.println(getOrderNo());
		//System.out.println(genImageName());
	}
}

