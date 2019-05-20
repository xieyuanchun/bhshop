package com.bh.utils;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FromCookie {

	public static void SetCookie(HttpServletRequest request, HttpServletResponse response,String id,int second) throws Exception {
		
		if (id != null && !"".equals(id)) {
			id = URLEncoder.encode(id, "utf-8");
		}

		
		String cooKie = id ;
		
		
        CookieUtils.setCookie(request, response, "userInfo", cooKie, second);
        
	}
		
}
