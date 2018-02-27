package com.yoga.demo.rabbit.topic;

import org.springframework.stereotype.Component;

@Component
public class TopicSender {
	
//	@Autowired
//	private AmqpTemplate rabbitTemplate;
//
//	public void send() {
//		String context = "hi, i am message all";
//		System.out.println("Sender : " + context);
//		this.rabbitTemplate.convertAndSend("topicExchange", "topic.1", context);
//	}
//
//	public void send1() {
//		String context = "hi, i am message 1";
//		System.out.println("Sender : " + context);
//		this.rabbitTemplate.convertAndSend("topicExchange", "topic.message", context);
//	}
//
//	public void send2() {
//		String context = "hi, i am messages 2";
//		System.err.println("Sender : " + context);
//		this.rabbitTemplate.convertAndSend("topicExchange", "topic.messages", context);
//	}
//	
//	public void send3() {
//		String context = "hi, i am messages yoga";
//		System.err.println("Sender : " + context);
//		this.rabbitTemplate.convertAndSend("topicExchange", "topic.yoga", context);
//	}
//	
//	public void send4() {
//		String context = "hi, i am messages xxx";
//		System.err.println("Sender : " + context);
//		this.rabbitTemplate.convertAndSend("topicExchange", "topic.233", context);
//	}
 
}
