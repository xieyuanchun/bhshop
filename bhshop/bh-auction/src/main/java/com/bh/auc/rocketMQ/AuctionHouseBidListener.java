package com.bh.auc.rocketMQ;

import com.bh.auc.obj.Auctioneer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

/**
 * 拍卖行的监听器
 */
public class AuctionHouseBidListener implements MessageListenerConcurrently {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuctionHouseBidListener.class);
    private static HashMap<String, Auctioneer> mapAuctioneer = new HashMap<String, Auctioneer>();

    public HashMap<String, Auctioneer> getMapAuctioneer() {
        return mapAuctioneer;
    }

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        if (list != null) {
            for (MessageExt messageExt : list) {
                try {
                    logger.debug("拍卖师监听到主题" + messageExt.getTopic() + "的消息:messageExt=" + messageExt.toString());
                    if (mapAuctioneer != null) {
                        String tag = messageExt.getTags();
                        Auctioneer auctioneer = mapAuctioneer.get(tag);
                        if (auctioneer != null) {
                            auctioneer.bidQueue.put((Message) messageExt);//直接把从rocketmq收到的消息透传到内部的消息队列中bidQueue
                        }
                    }
                } catch (Exception e) {
                    logger.error("拍卖师接收信息异常:error=" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}