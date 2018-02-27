package com.yoga.demo.rabbit.hello;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 发送者
 * 
 * @author yoga
 */
//@Component
public class HelloSender {
//	@Autowired
//    private AmqpTemplate rabbitTemplate;
// 
//    public void send() {
//        String context = "hello " + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        System.out.println("Sender : " + context);
//        
//        //具体代码发送的时候还是一样，第一个参数表示交换机，第二个参数表示routing key，第三个参数即消息
//        //这里只用到了 路由键 与 内容
//        this.rabbitTemplate.convertAndSend("hello", context);
//    }
}
