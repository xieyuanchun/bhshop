package com.bh.webserver.client;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.bh.webserver.common.AuctionUserPool;
import com.bh.webserver.common.Message;
import com.bh.webserver.entity.HolllandAuctionEntity;
import com.bh.webserver.proxy.MessageProxy;
/**
 * 
 * @author xxj
 *
 */
@ServerEndpoint(value="/auction/{mgpIds}",configurator = SpringConfigurator.class)
// @ServerEndpoint("/init")
public class AuctionSocketClient {

    private static Logger LOG = LoggerFactory.getLogger(AuctionSocketClient.class);

    /**
     * @param mgpIds 用户id-商品id-期数   eg:14959-15730-28
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("mgpIds") String mgpIds,Session session) {
    	System.out.println("AuctionSocket onOpen-"+mgpIds);
    	if(mgpIds!=null&&!"".equals(mgpIds.trim())){
    		String[] params = mgpIds.split("-");
    		if(params.length!=3){
    			return;
    		}else{
    			Integer mId = Integer.parseInt(params[0]);
    			String goodsId = params[1];
    			String currentPeriods = params[2];
//    			HolllandAuctionEntity oldUser =  AuctionUserPool.get(mgpIds);
//    	    	if(oldUser!=null){
//    	    		if(oldUser.getSession()!=null){
//    	    			try{
//    	    				oldUser.getSession().close();
//    	    			}catch (Exception e) {
//    	    				System.out.println("#########压迫下线出错#########");
//    	    				e.printStackTrace();
//    						// TODO: handle exception
//    					}
//    	    			
//    	    		}
//    	    	}
//    	    	System.out.println("onOpen mId==>"+mgpIds);
    	    
    	    	HolllandAuctionEntity user = new HolllandAuctionEntity();
    	        user.setmId(mId);
    	        user.setCurrentPeriods(currentPeriods);
    	        user.setGoodsId(goodsId);
    	        user.setSession(session);
    	        //加入用户池
//    	        AuctionUserPool.add(mgpIds,user);	
    	        
    	        
    	    	String key = goodsId+"-"+currentPeriods;
    	    	Map<String, HolllandAuctionEntity> oldUserMap = AuctionUserPool.getAuctionEntityList(key);//获取同期同商品 竞拍用户

    	    	if(oldUserMap!=null){
//    	    		HolllandAuctionEntity oldUser =  oldUserMap.get(mgpIds);
//					if (oldUser != null) {
//						if (oldUser.getSession() != null) {
//							try {
//								oldUser.getSession().close();
//							} catch (Exception e) {
//								System.out.println("#########压迫下线出错#########");
//								e.printStackTrace();
//							}
//
//						}
//					}
    	    		oldUserMap.put(mgpIds, user);

    	    		AuctionUserPool.addAuctionEntityList(key, oldUserMap);
    	    	}else{
    	    		Map<String, HolllandAuctionEntity> userMap = new HashMap<String, HolllandAuctionEntity>();
    	    		userMap.put(mgpIds, user);
    	    		AuctionUserPool.addAuctionEntityList(key, userMap);
    	    	}
    	    	
    		}
    		
	       
    	}
       
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
    	if("...".equals(message)){
    		return;
    	}
        LOG.info("user [" + session.getId() + "] Received: " + message);
        //给所有用户发送消息
        MessageProxy.getInstance().getProxy(Message.class).send(message, session);
    }

    @OnError
    public void onError(Throwable throwable) {
        LOG.error(throwable.getMessage());
    }

    @OnClose
    public void onClose(@PathParam("mgpIds") String mgpIds,Session session) {
        //移除用户池
    	System.out.println("AuctionSocket onClose-"+mgpIds);
    	if(mgpIds!=null&&"".equals(mgpIds.trim())){
    		String[] params = mgpIds.split("-");
    		if(params.length!=3){
    			return;
    		}else{
    			Integer mId = Integer.parseInt(params[0]);
    			String goodsId = params[1];
    			String currentPeriods = params[2];
    			String key = goodsId+"-"+currentPeriods;
    	    	Map<String, HolllandAuctionEntity> oldUserMap = AuctionUserPool.getAuctionEntityList(key);//获取同期同商品 竞拍用户
    	    	
				if (oldUserMap != null) {
					oldUserMap.remove(mgpIds);
					AuctionUserPool.addAuctionEntityList(key, oldUserMap);
				}

    		}
    	}
    	
        
        

    }
}
