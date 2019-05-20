package com.bh.filter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class ApiAccessFilter implements Filter {
	 FilterConfig filterConfig = null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	 this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	   HttpServletResponse res = (HttpServletResponse) response;  
    	   boolean isFlag = getRequestApiAccesskey((HttpServletRequest) request);
    	   if(isFlag){
    		   chain.doFilter(request, res);
    	   }else{
    		   return;
    	   }
    	   
    
        
    }

    @Override
    public void destroy() {
    	 this.filterConfig = null;
    }
    
    /**
     * 获取请求的token
     */
    private boolean getRequestApiAccesskey(HttpServletRequest httpRequest){
        //从header中获取token
        String apiAccesskey = httpRequest.getHeader("apiAccesskey");
        System.out.println("#########apiAccesskey########>>>>"+apiAccesskey);
        if("888888zy20180316apiAccesskey".equals(apiAccesskey)){
        	return true;
        }
        return false;
    }

}