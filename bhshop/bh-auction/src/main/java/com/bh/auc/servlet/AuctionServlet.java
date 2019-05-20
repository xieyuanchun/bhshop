package com.bh.auc.servlet;

import com.bh.auc.obj.AuctionHouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class AuctionServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AuctionServlet.class);
    private static final long serialVersionUID = 1L;

    public AuctionServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        logger.info("拍卖场开拍了！");
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        AuctionHouse auctionHouse = new AuctionHouse(webApplicationContext);
        auctionHouse.start();
    }


    public static void main(String[] args) throws Exception {
        logger.info("拍卖场开拍了！");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{
                "classpath:/spring/applicationContext-service.xml", "classpath:/spring/applicationContext-dao.xml",
                "classpath:/spring/applicationContext-rocketMQ-consumer.xml", "classpath:/spring/applicationContext-rocketMQ-producer.xml"});
        AuctionHouse auctionHouse = new AuctionHouse(applicationContext);
        auctionHouse.start();
    }

}
