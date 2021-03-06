package com.bh.auc.rocketMQ;

import org.apache.log4j.Logger;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class AuctionRecorderListener implements MessageListenerConcurrently {

    private Logger logger = Logger.getLogger(getClass());

    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        if (list != null) {
            for (MessageExt ext : list) {
                try {
                    logger.info("监听到消息 : " + new String(ext.getBody(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error("consumeMessage error=" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}