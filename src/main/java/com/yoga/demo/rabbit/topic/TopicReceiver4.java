package com.yoga.demo.rabbit.topic;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接收者
 * 
 * @author yoga
 */
//@Component
//@RabbitListener(queues = "topic.xxx")
public class TopicReceiver4 {

//    @RabbitHandler
//    public void process(String message) {
//        System.err.println("Topic Receiver xxx  : " + message);
//    }

}
