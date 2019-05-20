package com.bh.auc.rocketMQ;

import org.apache.log4j.Logger;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拍卖经纪人的监听器
 */
public class BokerNotifyListener implements MessageListenerConcurrently {
    private Logger logger = Logger.getLogger(getClass());
    private static Map<String, Map<String, Session>> mapTagSession=new HashMap<String, Map<String, Session>>(); // 用户记录连接session的hashmap，

    public Map<String, Map<String, Session>> getMapTagSession() {
        return mapTagSession;
    }

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        if (list != null) {
        	  /**	 
        	   * 	messageExt==>
        	   *		String tag: bhshop-15794-1
        	   */
            for (MessageExt messageExt : list) {
                try {
                    String msgBody = new String(messageExt.getBody(), "UTF-8");
                    logger.debug("拍卖经纪人监听到消息:periodTag=" + messageExt.getTags()  + ",msgBody=" + msgBody);
                    Map<String, Session> sessionGroup = mapTagSession.get(messageExt.getTags());
                    if (sessionGroup != null) {
						for ( String sessionId : sessionGroup.keySet()) {
							Session session=sessionGroup.get(sessionId);
							session.getBasicRemote().sendText(msgBody);//发送信息给用户
						}
                   }
                } catch (Exception e) {
                    logger.error("拍卖经纪人监听失败:error=" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}