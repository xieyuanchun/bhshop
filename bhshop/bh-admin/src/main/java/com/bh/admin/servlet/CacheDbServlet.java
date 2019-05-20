package com.bh.admin.servlet;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bh.admin.mapper.user.SysCacheDbMapper;
import com.bh.admin.pojo.user.SysCacheDb;
import com.bh.utils.cache.CacheConfig;

/**
 * Servlet implementation class GuavaCaheServlet
 */
@WebServlet("/GuavaDbServlet")
public class CacheDbServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(CacheDbServlet.class);
	private static final long serialVersionUID = 1L;
	private SysCacheDbMapper sysCacheDbMapper;
	private WebApplicationContext webApplicationContext;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CacheDbServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init() throws ServletException {
        logger.info("开始加载缓存！");
        webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        sysCacheDbMapper = (SysCacheDbMapper) webApplicationContext.getBean("sysCacheDbMapper");
        
        SysCacheDb entity = new SysCacheDb();
		List<SysCacheDb> list = sysCacheDbMapper.listPage(entity);
		logger.info("要加载的数据已准备好！");
		if(list.size()>0){
			for(SysCacheDb db : list){
				CacheConfig.put(db.getDbKey(), db.getDbVal());
				logger.info("key为："+db.getDbKey()+"的值已加载到缓存");
			}
		}
    }

}
