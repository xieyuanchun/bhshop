package com.bh.auc.rocketMQ;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringConsumerTest {

    private ApplicationContext container;

    @Before
    public void setup() {
        container = new ClassPathXmlApplicationContext("classpath:spring/applicationcontext-rocketMQ-consumer.xml");
    }

    @Test
    public void consume() throws Exception {
        SpringConsumer consumer = container.getBean(SpringConsumer.class);

        Thread.sleep(200 * 1000);

        consumer.destroy();
    }
}
