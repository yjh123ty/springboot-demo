package com.yoga.demo.controller.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yoga.demo.service.MessageService;

/**
 * 测试MQ生产者与消费者
 * 
 * @author yoga
 */
@RestController
@RequestMapping(value = "/mq")
public class MQController {
//	@Autowired
//	private MessageService messageService;
//	
//	/**
//	 * 发送消息
//	 * @return
//	 */
//	@RequestMapping(value = "/send", method = RequestMethod.GET)
//    public String Send(){
//		messageService.send();
//        return "success";
//    }
//	
//	/**
//	 * 发送消息
//	 * @return
//	 */
//	@RequestMapping(value = "/sendTopic")
//    public String convertAndSend(){
//		messageService.sendTopic();
//        return "success";
//    }
	
}
