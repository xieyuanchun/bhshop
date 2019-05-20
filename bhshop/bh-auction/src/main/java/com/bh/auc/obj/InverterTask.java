package com.bh.auc.obj;

import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * Created by zhouyongyi on 2018/7/3
 * 用户倒计时结束后，把消息标签发送到消息队列，触发动作
 */
public class InverterTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(InverterTask.class);
    private BlockingQueue<Message> bidQueue = null;

    public InverterTask(BlockingQueue<Message> bidQueue) {
        this.bidQueue = bidQueue;
    }

    @Override
    public void run() {
        Message message = new Message("", "DOWN_PRICE", "".getBytes());
        try {
            bidQueue.put(message);
        } catch (Exception e) {
            logger.error("InverterTask error=" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
