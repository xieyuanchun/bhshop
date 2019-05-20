package com.bh.utils;

import javax.servlet.http.HttpServletRequest;

public class DeviceUtil {
	public static boolean isMobileDevice(HttpServletRequest request) {
		/**
		 * android : 所有android设备 mac os : iphone ipad windows
		 * phone:Nokia等windows系统的手机
		 */
		String requestHeader = request.getHeader("user-agent");
		String[] deviceArray = new String[] { "android", "mac os", "windows phone" };
		if (requestHeader == null)
			return false;
		requestHeader = requestHeader.toLowerCase();
		for (int i = 0; i < deviceArray.length; i++) {
			if (requestHeader.indexOf(deviceArray[i]) > 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean isWxDevice(HttpServletRequest request){
		String userAgent = request.getHeader("user-agent").toLowerCase();
		if(userAgent.indexOf("micromessenger")>-1){//微信客户端
			return true;
		}
		return false;
	}
}
