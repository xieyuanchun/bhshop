package com.bh.admin.servlet;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bh.admin.mapper.user.SysCacheConfMapper;
import com.bh.admin.pojo.user.SysCacheConf;
import com.bh.utils.cache.CacheConfig;

/**
 * Servlet implementation class GuavaCaheServlet
 */
@WebServlet("/GuavaCaheServlet")
public class GuavaCaheServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(GuavaCaheServlet.class);
	private static final long serialVersionUID = 1L;
	private SysCacheConfMapper sysCacheConfMapper;
	private WebApplicationContext webApplicationContext;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GuavaCaheServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init() throws ServletException {
        logger.info("开始加载缓存！");
        webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        sysCacheConfMapper = (SysCacheConfMapper) webApplicationContext.getBean("sysCacheConfMapper");
        
        SysCacheConf entity = new SysCacheConf();
		List<SysCacheConf> list = sysCacheConfMapper.listPage(entity);
		logger.info("要加载的数据已准备好！");
		if(list.size()>0){
			for(SysCacheConf conf : list){
				CacheConfig.put(conf.getConfigKey(), conf.getConfigVal());
				logger.info("key为："+conf.getConfigKey()+"的值已加载到缓存");
			}
		}
    }

}
