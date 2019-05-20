package com.bh.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


/**
 * @Description: 营销活动------工具类
 * @author xieyc
 * @date 2018年1月23日 下午5:02:17 
 */
public class TopicUtils {
	/**
	 * @Description: 
	 * @author xieyc
	 * @date 2018年1月18日 下午3:55:07 
	 */
	public static String getEndTime(Date endTime){
		String waitTime = null;
		long a = new Date().getTime();
		long b = endTime.getTime();
		if(b>a){
			int c = (int)((b-a) / 1000);
			System.out.println(c);
			String hours = c / 3600+"";
			if(Integer.parseInt(hours)<10){
				hours = "0"+ hours;
			}
			String min = c % 3600 /60+"";
			if(Integer.parseInt(min)<10){
				min = "0"+ min;
			}
			String sencond = c % 3600 % 60+"";
			if(Integer.parseInt(sencond)<10){
				sencond = "0"+ sencond;
			}
		   waitTime = hours+":"+min+":"+sencond;
		}
		return waitTime;
	}
	/**
	 * @Description: 生成指定规则长度的字符穿
	 * @author xieyc
	 * @return 
	 * @date 2018年1月18日 下午1:06:28 
	 */
	public static String getRandomString(int length){
    	String  str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";//规则
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
          int number=random.nextInt(62);
          sb.append(str.charAt(number));
        }
        return sb.toString();
    }	
	
    /**
     * @Description: 生成中奖号码
     * @author xieyc
     * @date 2018年1月18日 下午4:20:32 
     */
    public static String generatePrizeNum(){
    	DateFormat df = new SimpleDateFormat("yyyyMMdd");
    	String prizeNum = df.format(new Date()) + sjs(4);
    	return prizeNum;
		
	}
    
    /**
     * @Description: 获取日期的时和分:--->18:23
     * @author xieyc
     * @date 2018年1月18日 下午4:20:32 
     */
    public static String getDayAndHour(Date time){
    	 StringBuffer sb=new StringBuffer();
    	 Calendar now = Calendar.getInstance(); 
    	 now.setTime(time);         
         int minute=now.get(Calendar.MINUTE);  
         int hour=now.get(Calendar.HOUR_OF_DAY);
         if(hour<10){
        	 sb.append(0); 
         }
         sb.append(hour);
		 sb.append(":");
		 if(minute<10){
        	 sb.append(0); 
         }
		 sb.append(minute);
		 return sb.toString();
	}
    
	
	/**
	 * @Description: 生成订单号或团购号
	 * @author xieyc
	 * @date 2018年1月23日 下午5:00:50 
	 */
	public static String getOrderNo(){
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String oid = df.format(new Date()) + sjs(5);
		return oid;
	}
	
	public static String sjs(Integer size) {
		Random rand = new Random();
		String str = "0123456789";
		String result = "";
		for (int i = 0; i <= size; i++) {
			String randStr = Integer.valueOf(rand.nextInt(str.length())).toString();
			result += randStr;
		}
		return result;
	}
	/**
	 * 获得指定日期的后一天
	 * @author xieyc
	 * @param specifiedDay
	 * @return 
	 * @return
	 */
	public static  Date getDayAfter(Integer dayTime) {
		Date dNow = new Date();   //当前时间
		Date dBefore=new Date();
		try {
			Calendar calendar = Calendar.getInstance();  //得到日历
			calendar.setTime(dNow);//把当前时间赋给日历
			calendar.add(Calendar.DAY_OF_MONTH, dayTime);
			dBefore = calendar.getTime();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
			String defaultStartDate = sdf.format(dBefore);    //格式化
			dBefore=sdf.parse(defaultStartDate);
			System.out.println(dBefore);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dBefore;
	}
	
	public static void main(String[] args) {
		System.out.println(getDayAndHour(new Date()));
		//System.out.println(getOrderNo());
	}
}

