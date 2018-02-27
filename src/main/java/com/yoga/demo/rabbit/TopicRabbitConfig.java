package com.yoga.demo.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 特别注意：
 * 	使用 Binding类 中的queue参数
 * 
 * @author yoga
 */
//@Configuration
public class TopicRabbitConfig {

//	final static String message = "topic.message";
//    final static String messages = "topic.messages";
//    final static String yoga = "topic.yoga";
//    final static String xxx = "topic.xxx";
//
//    @Bean
//    public Queue queueMessage() {
//        return new Queue(TopicRabbitConfig.message);
//    }
//
//    @Bean
//    public Queue queueMessages() {
//        return new Queue(TopicRabbitConfig.messages);
//    }
//    
//    @Bean
//    public Queue queueMessagesYoga() {
//        return new Queue(TopicRabbitConfig.yoga);
//    }
//    
//    @Bean
//    public Queue queueMessagesX() {
//        return new Queue(TopicRabbitConfig.xxx);
//    }
//
//    /*
//     * 声明交换器
//     */
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange("topicExchange");
//    }
//
//    @Bean
//    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
//        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
//    }
//    
//    @Bean
//    Binding bindingExchangeMessageY(Queue queueMessagesYoga, TopicExchange exchange) {
//        return BindingBuilder.bind(queueMessagesYoga).to(exchange).with("topic.yoga");
//    }
//
//    @Bean
//    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
//        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
//    }
//    
//    @Bean
//    Binding bindingExchangeMessageX(Queue queueMessagesX, TopicExchange exchange) {
//        return BindingBuilder.bind(queueMessagesX).to(exchange).with("topic.#");
//    }
}
