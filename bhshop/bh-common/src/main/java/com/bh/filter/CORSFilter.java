package com.bh.filter;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter implements Filter {
	 FilterConfig filterConfig = null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	 this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    
    	HttpServletResponse res = (HttpServletResponse) response;  
    	   res.setHeader("Access-Control-Allow-Origin", "*");  
    	   res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
    	   res.setHeader("Access-Control-Allow-Headers","Access-Control");
    	//   res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");  
    	   res.setHeader("Access-Control-Allow-Credentials", "true");  
    //	   res.setHeader("XDomainRequestAllowed","1");  
    	   chain.doFilter(request, res);
    
        
    }

    @Override
    public void destroy() {
    	 this.filterConfig = null;
    }
}