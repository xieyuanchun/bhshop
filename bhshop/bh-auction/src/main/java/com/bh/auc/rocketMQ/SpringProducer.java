package com.bh.auc.rocketMQ;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringProducer {
    private static final Logger logger = LoggerFactory.getLogger(SpringProducer.class);

    private String producerGroupName;

    private String nameServerAddr;

    private DefaultMQProducer producer;

    public SpringProducer(String producerGroupName, String nameServerAddr) {
        this.producerGroupName = producerGroupName;
        this.nameServerAddr = nameServerAddr;
    }

    public void init() throws Exception {
        //创建一个消息生产者，并设置一个消息生产者组
        producer = new DefaultMQProducer(producerGroupName);
        //指定 NameServer 地址
        producer.setNamesrvAddr(nameServerAddr);
        //初始化 SpringProducer，整个应用生命周期内只需要初始化一次
        producer.start();
        logger.debug("成功启动生产者groupName=" + producerGroupName);
    }

    public void destroy() {
        producer.shutdown();
        logger.debug("已关闭生产者groupName=" + producerGroupName);
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }
}
