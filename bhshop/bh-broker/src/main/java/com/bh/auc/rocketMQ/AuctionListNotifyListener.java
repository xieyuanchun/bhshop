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
 * 拍卖列表的ws服务收到拍价通知后就进行广播
 */
public class AuctionListNotifyListener implements MessageListenerConcurrently {
    private Logger logger = Logger.getLogger(getClass());
    private static Map<String, Session> mapSession = new HashMap<String, Session>(); // 用户记录连接session的地址本，

    public Map<String, Session> getMapSession() {
        return mapSession;
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
                    logger.debug("拍卖列表的ws服务监听到消息:periodTag=" + messageExt.getTags() + ",body=" + msgBody);
                    for (String sessionId : mapSession.keySet()) {
                        Session session = mapSession.get(sessionId);
                        session.getBasicRemote().sendText(msgBody);//发送信息给用户
                    }
                } catch (Exception e) {
                    logger.error("拍卖列表的ws服务失败:error=" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}