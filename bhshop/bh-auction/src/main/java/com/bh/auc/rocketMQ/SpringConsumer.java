package com.bh.auc.rocketMQ;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

public class SpringConsumer {
    private Logger logger = Logger.getLogger(getClass());
    private DefaultMQPushConsumer consumer;
    private String consumerGroupName; //分组名
    private String nameServerAddr; //name服务地址
    private String topicName; //主题名称
    private MessageListenerConcurrently messageListener;

    private ConsumeFromWhere consumeFromWhere;
    /* 1 CONSUME_FROM_LAST_OFFSET：第一次启动从队列最后位置消费，后续再启动接着上次消费的进度开始消费
      2 CONSUME_FROM_FIRST_OFFSET：第一次启动从队列初始位置消费，后续再启动接着上次消费的进度开始消费
      3 CONSUME_FROM_TIMESTAMP：第一次启动从指定时间点位置消费，后续再启动接着上次消费的进度开始消费 */

    private MessageModel messageModel; //广播和集群两种消费模式
    private String subExpression; //标签过滤
    ;;

    public void setConsumerGroupName(String consumerGroupName) {
        this.consumerGroupName = consumerGroupName;
    }

    public void setNameServerAddr(String nameServerAddr) {
        this.nameServerAddr = nameServerAddr;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setMessageListener(MessageListenerConcurrently messageListener) {
        this.messageListener = messageListener;
    }

    public SpringConsumer(String consumerGroupName, String nameServerAddr, String topicName,
                          MessageListenerConcurrently messageListener, int consumeFromWhere, int messageModel,
                          String subExpression) {
        this.consumerGroupName = consumerGroupName;
        this.nameServerAddr = nameServerAddr;
        this.topicName = topicName;
        this.messageListener = messageListener;
        this.consumeFromWhere = ConsumeFromWhere.values()[consumeFromWhere];
        this.messageModel = MessageModel.values()[messageModel];
        this.subExpression = subExpression;
    }

    public void init() throws Exception {
        // 创建一个消息消费者，并设置一个消息消费者组
        consumer = new DefaultMQPushConsumer(consumerGroupName);
        // 指定 NameServer 地址
        consumer.setNamesrvAddr(nameServerAddr);
        // 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
        consumer.setConsumeFromWhere(consumeFromWhere);
        //集群模式 （负载均衡）还是 广播模式 （全都收）
        consumer.setMessageModel(messageModel);
        // 订阅指定 Topic 下的所有消息
        if (StringUtils.isBlank(subExpression)) {
            consumer.subscribe(topicName, "*");
        } else {
            consumer.subscribe(topicName, subExpression);
        }
        // 注册消息监听器
        consumer.registerMessageListener(messageListener);
        // 消费者对象在使用之前必须要调用 start 初始化
        consumer.start();
        logger.debug("启动消费者groupName=" + consumerGroupName + ",topicName=" + topicName);
    }

    public void destroy() {
        consumer.shutdown();
        logger.debug("已关消费者groupName=" + consumerGroupName + ",topicName=" + topicName);
    }

    public DefaultMQPushConsumer getConsumer() {
        return consumer;
    }

}
