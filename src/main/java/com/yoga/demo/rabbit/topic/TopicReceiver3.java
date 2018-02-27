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
//@RabbitListener(queues = "topic.yoga")
public class TopicReceiver3 {

//    @RabbitHandler
//    public void process(String message) {
//        System.out.println("Topic Receiver Yoga  : " + message);
//    }

}
