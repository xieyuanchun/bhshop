package com.bh.webserver.client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import com.bh.webserver.common.Message;
import com.bh.webserver.common.UserPool;
import com.bh.webserver.entity.UserEntity;
import com.bh.webserver.proxy.MessageProxy;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
/**
 * 
 * @author xxj
 *
 */
@ServerEndpoint(value="/init/{mId}",configurator = SpringConfigurator.class)
// @ServerEndpoint("/init")
public class WebSocketClient {

    private static Logger LOG = LoggerFactory.getLogger(WebSocketClient.class);

    @OnOpen
    public void onOpen(@PathParam("mId") Integer mId,Session session) {
    	if(mId!=null&&mId!=0){
	    	UserEntity oldUser =  UserPool.get(mId);
	    	if(oldUser!=null){
	    		if(oldUser.getSession()!=null){
	    			try{
	    				oldUser.getSession().close();
	    			}catch (Exception e) {
	    				System.out.println("#########压迫下线出错#########");
	    				e.printStackTrace();
						// TODO: handle exception
					}
	    			
	    		}
	    	}
	    	System.out.println("onOpen mId==>"+mId);
	        UserEntity user = new UserEntity();
	        user.setmId(mId);
	        user.setSession(session);
	       

	        //加入用户池
	        UserPool.add(mId,user);	
	       
    	}
       
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        LOG.info("user [" + session.getId() + "] Received: " + message);
        //给所有用户发送消息
        MessageProxy.getInstance().getProxy(Message.class).send(message, session);
    }

    @OnError
    public void onError(Throwable throwable) {
        LOG.error(throwable.getMessage());
    }

    @OnClose
    public void onClose(@PathParam("mId") Integer mId,Session session) {
        //移除用户池
    	System.out.println("onClose mId==>"+mId);
    	if(mId!=null&&mId!=0){
    		UserPool.remove(mId);
    	}
    	
        
        

    }
}
