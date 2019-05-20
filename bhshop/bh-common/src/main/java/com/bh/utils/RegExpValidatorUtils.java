package com.bh.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.utils.pay.WXPayConstants;
import com.control.file.upload;
import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;

public final class RegExpValidatorUtils {

	/**
	 * 验证邮箱
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isEmail(String str) {
		String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		return match(regex, str);
	}

	/**
	 * 验证IP地址
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isIP(String str) {
		String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
		String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";
		return match(regex, str);
	}

	/**
	 * 验证网址Url
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsUrl(String str) {
		String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
		return match(regex, str);
	}

	/**
	 * 验证电话号码
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsTelephone(String str) {
		String regex = "^(\\d{3,4}-)?\\d{6,8}$";
		return match(regex, str);
	}

	/**
	 * 验证输入密码条件(字符与数据同时出现)
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPassword(String str) {
		String regex = "[A-Za-z]+[0-9]";
		return match(regex, str);
	}

	/**
	 * 验证输入密码长度 (6-18位)
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPasswLength(String str) {
		String regex = "^\\d{6,18}$";
		return match(regex, str);
	}

	/**
	 * 验证输入邮政编号
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPostalcode(String str) {
		String regex = "^\\d{6}$";
		return match(regex, str);
	}

	/**
	 * 验证输入手机号码
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsHandset(String str) {
		String regex = "^[1]+[3,5]+\\d{9}$";
		return match(regex, str);
	}

	/**
	 * 验证手机号-scj
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkMobile(String str) {
		String regex = "^1(3|4|5|7|8)\\d{9}$";
		return match(regex, str);
	}

	/**
	 * 验证输入身份证号
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsIDcard(String str) {
		String regex = "(^\\d{18}$)|(^\\d{15}$)";
		return match(regex, str);
	}

	/**
	 * 验证输入两位小数
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsDecimal(String str) {
		String regex = "^[0-9]+(.[0-9]{2})?$";
		return match(regex, str);
	}

	/**
	 * 验证输入一年的12个月
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsMonth(String str) {
		String regex = "^(0?[[1-9]|1[0-2])$";
		return match(regex, str);
	}

	/**
	 * 验证输入一个月的31天
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsDay(String str) {
		String regex = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
		return match(regex, str);
	}

	/**
	 * 验证日期时间
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合网址格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean isDate(String str) {
		// 严格验证时间格式的(匹配[2002-01-31], [1997-04-30],
		// [2004-01-01])不匹配([2002-01-32], [2003-02-29], [04-01-01])
		// String regex =
		// "^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))-((((0[1-9])|(1[0-2]))-((0[1-9])|(1\\d)|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((01,3-9])|(1[0-2]))-(29|30)))))$";
		// 没加时间验证的YYYY-MM-DD
		// String regex =
		// "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$";
		// 加了时间验证的YYYY-MM-DD 00:00:00
		String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
		return match(regex, str);
	}

	/**
	 * 验证数字输入
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsNumber(String str) {
		String regex = "^[0-9]*$";
		return match(regex, str);
	}

	/**
	 * 验证非零的正整数
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsIntNumber(String str) {
		String regex = "^\\+?[1-9][0-9]*$";
		return match(regex, str);
	}

	/**
	 * 验证大写字母
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsUpChar(String str) {
		String regex = "^[A-Z]+$";
		return match(regex, str);
	}

	/**
	 * 验证小写字母
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsLowChar(String str) {
		String regex = "^[a-z]+$";
		return match(regex, str);
	}

	/**
	 * 验证验证输入字母
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsLetter(String str) {
		String regex = "^[A-Za-z]+$";
		return match(regex, str);
	}

	/**
	 * 验证验证输入汉字
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsChinese(String str) {
		String regex = "^[\u4e00-\u9fa5],{0,}$";
		return match(regex, str);
	}

	/**
	 * 验证验证输入字符串
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsLength(String str) {
		String regex = "^.{8,}$";
		return match(regex, str);
	}

	/**
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 对字符串处理:将指定位置到指定位置的字符以星号代替
	 * 
	 * @param content
	 *            传入的字符串
	 * @param begin
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return
	 */
	public static String getStarString(String content, int begin, int end) {

		if (begin >= content.length() || begin < 0) {
			return content;
		}
		if (end >= content.length() || end < 0) {
			return content;
		}
		if (begin >= end) {
			return content;
		}
		String starStr = "";
		for (int i = begin; i < end; i++) {
			starStr = starStr + "*";
		}
		return content.substring(0, begin) + starStr + content.substring(end, content.length());

	}

	public static String toAllString(String string) {
		int n = string.length();
		String t1 = "";
		for (int i = 0; i < n; i++) {
			t1 += "*";
		}
		return t1;
	}

	/**
	 * 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替
	 * 
	 * @param content
	 *            传入的字符串
	 * @param frontNum
	 *            保留前面字符的位数
	 * @param endNum
	 *            保留后面字符的位数
	 * @return 带星号的字符串
	 */

	public static String getStarString2(String content, int frontNum, int endNum) {

		if (frontNum >= content.length() || frontNum < 0) {
			return content;
		}
		if (endNum >= content.length() || endNum < 0) {
			return content;
		}
		if (frontNum + endNum >= content.length()) {
			return content;
		}
		String starStr = "";
		for (int i = 0; i < (content.length() - frontNum - endNum); i++) {
			starStr = starStr + "*";
		}
		return content.substring(0, frontNum) + starStr
				+ content.substring(content.length() - endNum, content.length());

	}

	public static String replaceCard(String src) {
		StringBuilder resultBuilder = new StringBuilder();
		String part1 = src.substring(0, 3);

		resultBuilder.append(part1);
		resultBuilder.append(" **** ");

		String part2 = src.substring(src.length() - 10, src.length());

		resultBuilder.append(part2);
		return resultBuilder.toString();
	}

	public static double formatdouble(double data) {
		DecimalFormat df = new DecimalFormat(".##");
		double d = data;
		d = Double.valueOf(df.format(d));
		return d;
	}

	// byte数组到图片到硬盘上
	public static void byte2image(byte[] data, String path) {
		if (data.length < 3 || path.equals(""))
			return;// 判断输入的byte是否为空
		try {
			FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));// 打开输入流
			imageOutput.write(data, 0, data.length);// 将byte写入硬盘
			imageOutput.close();
			System.out.println("Make Picture success,Please find image in " + path);
		} catch (Exception ex) {
			System.out.println("Exception: " + ex);
			ex.printStackTrace();
		}
	}

	public static String getFileExtendName(byte[] photoByte) {
		String strFileExtendName = ".jpg";
		if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70) && (photoByte[3] == 56)
				&& ((photoByte[4] == 55) || (photoByte[4] == 57)) && (photoByte[5] == 97)) {
			strFileExtendName = ".gif";
		} else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73) && (photoByte[9] == 70)) {
			strFileExtendName = ".jpg";
		} else if ((photoByte[0] == 66) && (photoByte[1] == 77)) {
			strFileExtendName = ".bmp";
		} else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71)) {
			strFileExtendName = ".png";
		}
		return strFileExtendName;
	}

	/**
	 *  * 获取媒体文件  *   * @param 存放路径  *      字节数组  * @param media_id  *     主键识别
	 *  *
	 */
	public static String downloadMedia(String path, byte[] filebyte, String key1) {

		try {

			String targetFileName = StringUtil.genUuId() + getFileExtendName(filebyte);
			File targetFile = new File(path, targetFileName);
			byte2image(filebyte, path + targetFileName);

			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			StringBuffer key = new StringBuffer();
			key.append(key1);
			key.append(targetFileName);
			upload myupload = new upload();
			String localFilePath = path + targetFileName;
			boolean bl = myupload.singleupload("aliyun", localFilePath, key.toString());
			StringBuffer realPath = new StringBuffer(Contants.bucketHttps);
			realPath.append(key1);
			realPath.append(targetFileName);

			if (bl) {
				return realPath.toString();
			} else {
				return "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}

	}

	// 过滤特殊字符和敏感字符
	public static String StringFilter(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx ="[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	// 活动倒计时
	public static String getEndTime(Date beginTime, Date endTime) {
		String waitTime = null;
		long a = new Date().getTime();
		long b = endTime.getTime();
		if (b > a) {
			int c = (int) ((b - a) / 1000);
			String hours = c / 3600 + "";
			if (Integer.parseInt(hours) < 10) {
				hours = "0" + hours;
			}
			String min = c % 3600 / 60 + "";
			if (Integer.parseInt(min) < 10) {
				min = "0" + min;
			}
			String sencond = c % 3600 % 60 + "";
			if (Integer.parseInt(sencond) < 10) {
				sencond = "0" + sencond;
			}
			waitTime = hours + ":" + min + ":" + sencond;
		}
		return waitTime;
	}

	// 活动倒计时
	public static String getEndTime1(Date endTime) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = endTime;
			Calendar cl = Calendar.getInstance();
			cl.setTime(date);

			cl.add(Calendar.DATE, Contants.CONFIGGOODS);
			String temp = "";
			temp = sdf.format(cl.getTime());

			String waitTime = null;
			long a = new Date().getTime();
			long b = sdf.parse(temp).getTime();
			if (b > a) {
				int c = (int) ((b - a) / 1000);
				String hours = c / 3600 + "";
				if (Integer.parseInt(hours) < 10) {
					hours = "0" + hours;
				}
				String min = c % 3600 / 60 + "";
				if (Integer.parseInt(min) < 10) {
					min = "0" + min;
				}
				String sencond = c % 3600 % 60 + "";
				if (Integer.parseInt(sencond) < 10) {
					sencond = "0" + sencond;
				}
				waitTime = hours ;
			}
			return waitTime;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	// smdate是输入时间，bdate是现在时间
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 根据日期获得所在周的日期
	 * 
	 * @param mdate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<String> dateToWeek(Date mdate) {
		// 定义输出日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");

		int b = mdate.getDay();
		Date fdate;
		List<String> list = new ArrayList<String>();
		Long fTime = mdate.getTime() - b * 24 * 3600000;
		for (int a = 1; a <= 7; a++) {

			fdate = new Date();
			fdate.setTime(fTime + (a * 24 * 3600000)); // 一周从周日开始算，则使用此方式
			String date = sdf.format(fdate);

			String[] strs = date.split("-");
			// StringBuffer sb = new StringBuffer();
			// sb.append(strs[1].toString()).append(".").append(strs[2].toString());
			list.add(a - 1, StringUtils.join(strs, "-"));
		}
		return list;
	}

	/** 获取两个时间的时间查 如1天2小时30分钟 */
	public static String getDatePoor(Date endDate, Date nowDate) {

		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天" + hour + "小时" + min + "分钟";
	}

	/**
	 * 计算两个日期之间相差的天、时、分、秒
	 * 
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static void daysBetween1(String first, String second) throws ParseException {
		SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		daysBetween2(sformat.parse(first), sformat.parse(second));
	}

	/**
	 * 计算两个日期之间相差的天、时、分、秒
	 * 
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static void daysBetween2(Date first, Date second) throws ParseException {
		SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		first = sformat.parse(sformat.format(first));
		second = sformat.parse(sformat.format(second));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(first);
		long firstMills = calendar.getTimeInMillis();
		calendar.setTime(second);
		long secondMills = calendar.getTimeInMillis();
		long rateD = 1000 * 60 * 60 * 24;
		long rateH = 1000 * 60 * 60;
		long rateM = 1000 * 60;
		long rateS = 1000;
		long mills = secondMills - firstMills;
		long days = mills / rateD;
		long hours = (mills % rateD) / rateH;
		long minutes = (mills % rateD % rateH) / rateM;
		long seconds = (mills % rateD % rateH % rateM) / rateS;
		System.out.println("时间1:" + sformat.format(first));
		System.out.println("时间2:" + sformat.format(second));
		System.out.println("两者相差:" + days + "天" + hours + "时" + minutes + "分" + seconds + "秒");
	}

	public static String plusDay(int num, Date newDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("现在的日期是：" + newDate);
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
		newDate = ca.getTime();
		String enddate = sdf.format(newDate);
		System.out.println("增加天数以后的日期：" + enddate);
		return enddate;
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 定义输出日期格式

		// Date currentDate = new Date();
		// 比如今天是2015-02-03
		// List<String> days = dateToWeek(currentDate);
		// System.out.println("今天的日期: " + sdf.format(currentDate));
		// for(String date : days) {
		// System.out.println(date);
		// }

		int day = 10;
		String createDate = "2018-01-17 17:27:18";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println("aaa：" +getEndTime1(sdf.parse(createDate)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	/** 
	 * 判断时间是否在时间段内 
	 *  
	 * @param date 
	 *            当前时间 yyyy-MM-dd HH:mm:ss 
	 * @param strDateBegin 
	 *            开始时间 00:00:00 
	 * @param strDateEnd 
	 *            结束时间 00:05:00 
	 * @return 
	 */  
	public static boolean isInDate(Date date, String strDateBegin,  
	        String strDateEnd) {  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    String strDate = sdf.format(date);  
	    // 截取当前时间时分秒  
	    int strDateH = Integer.parseInt(strDate.substring(11, 13));  
	    int strDateM = Integer.parseInt(strDate.substring(14, 16));  
	    int strDateS = Integer.parseInt(strDate.substring(17, 19));  
	    // 截取开始时间时分秒  
	    int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));  
	    int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));  
	    int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));  
	    // 截取结束时间时分秒  
	    int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));  
	    int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));  
	    int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));  
	    if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {  
	        // 当前时间小时数在开始时间和结束时间小时数之间  
	        if (strDateH > strDateBeginH && strDateH < strDateEndH) {  
	            return true;  
	            // 当前时间小时数等于开始时间小时数，分钟数在开始和结束之间  
	        } else if (strDateH == strDateBeginH && strDateM >= strDateBeginM  
	                && strDateM <= strDateEndM) {  
	            return true;  
	            // 当前时间小时数等于开始时间小时数，分钟数等于开始时间分钟数，秒数在开始和结束之间  
	        } else if (strDateH == strDateBeginH && strDateM == strDateBeginM  
	                && strDateS >= strDateBeginS && strDateS <= strDateEndS) {  
	            return true;  
	        }  
	        // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数  
	        else if (strDateH >= strDateBeginH && strDateH == strDateEndH  
	                && strDateM <= strDateEndM) {  
	            return true;  
	            // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数等于结束时间分钟数，秒数小等于结束时间秒数  
	        } else if (strDateH >= strDateBeginH && strDateH == strDateEndH  
	                && strDateM == strDateEndM && strDateS <= strDateEndS) {  
	            return true;  
	        } else {  
	            return false;  
	        }  
	    } else {  
	        return false;  
	    }  
	} 
	
	
	public static int hourBetween(Date newDate,Date oldDate){
		try {
			SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//如2016-08-10 20:40  
			String fromDate =simpleFormat.format(oldDate);   
			String toDate = simpleFormat.format(newDate);  
			long from = simpleFormat.parse(fromDate).getTime();  
			long to = simpleFormat.parse(toDate).getTime();  
			int hours = (int) ((to - from)/(1000 * 60 * 60));  
			//System.out.println("相差小时" + hours+"");
			return hours;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return 0;
		}
	}
	
	
	public static String returnNewString(String oldString){
		try {
			 org.json.JSONObject jsonObj = new org.json.JSONObject(oldString);
		     org.json.JSONArray personList = jsonObj.getJSONArray("url");
		    
			return (String) personList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return "";
		}
	}

}
