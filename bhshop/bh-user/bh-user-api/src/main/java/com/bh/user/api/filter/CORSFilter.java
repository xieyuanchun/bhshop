package com.bh.user.api.filter;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class CORSFilter extends HttpServlet implements Filter {
	private static final long serialVersionUID = 1L;
	FilterConfig filterConfig = null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	 this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
    	HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
	//	response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setHeader("Access-Control-Allow-Origin", "http://120.77.155.160:80");
		response.setHeader("Access-Control-Allow-Origin", "http://bhmall.zhiyesoft.cn");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers",
				"x-requested-with, content-type, Content-Disposition,Origin,Authorization");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		if (request.getHeader("Access-Control-Request-Headers") != null){
			response.setStatus(200);
		}	
		else{
			filterChain.doFilter(req, res);
		}
    }

    @Override
    public void destroy() {
    	 this.filterConfig = null;
    }
}