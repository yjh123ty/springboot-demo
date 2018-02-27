package com.yoga.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.yoga.demo.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService{
	
//	@Autowired
//	private JmsTemplate jmsQueueTemplate;
//
//	@Override
//	public void send() {
//		jmsQueueTemplate.convertAndSend("queue-test", "我是自动转换的消息");
//	}
//	
//	@Override
//	public void sendTopic() {
//		jmsQueueTemplate.convertAndSend("queue-topic", "我是自动转换的订阅消息");
//	}
//	
//	/**
//     * 消息队列监听器
//     * destination 队列地址
//     * containerFactory 监听器容器工厂, 若存在2个以上的监听容器工厂,需进行指定
//     * 
//     * p.s:监听容器设置了线程池的缘故，在实际消费过程中，监听器消费的顺序会有所差异
//     */
//    @JmsListener(destination = "queue-test", containerFactory = "jmsTopicListenerCF")
//	public void receive(String msg){
//		System.err.println("点对点模式1: " + msg);
//	} 
//    
//    /**
//     * 订阅
//     * @param msg
//     */
//    @JmsListener(destination = "queue-topic", containerFactory = "jmsTopicListenerCF")
//    public void receiveTopic(String msg){
//        System.out.println("订阅者 - " + msg);
//    }
//    
//    @JmsListener(destination = "queue-topic", containerFactory = "jmsTopicListenerCF")
//    public void receiveTopic2(String msg){
//        System.out.println("订阅者2 - " + msg);
//    }
    
    
	

}
