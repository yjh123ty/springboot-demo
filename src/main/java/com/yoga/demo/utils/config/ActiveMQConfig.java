package com.yoga.demo.utils.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

//@Configuration
//@EnableJms		//注解@EnableJms设置在@Configuration类上，用来声明对 JMS 注解的支持。
public class ActiveMQConfig {
	//失效转移机制 ：若有服务宕机，则可从另外一个地址获取服务。randomize表示随机从uri中获取一个地址
//	private static final String P_ACTIVEMQ_URL = "failover:(tcp://192.168.4.9:61619,tcp://192.168.4.9:61620)?randomize=true";
//	private static final String C_ACTIVEMQ_URL = "failover:(tcp://192.168.4.9:61616,tcp://192.168.4.9:61617,tcp://192.168.4.9:61618)?randomize=true";
//	private static final String queueName = "queue-test";
//	private static final String topicName = "topic-test";
//	
//	/**
//	 * 配置工厂
//	 * @return
//	 */
//	@Bean
//	public ActiveMQConnectionFactory connectionFactory(String url){
//		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
//		connectionFactory.setBrokerURL(url);
//		connectionFactory.setUserName("admim");
//		connectionFactory.setPassword("admim");
//		return connectionFactory;
//	}
//	
//	/**
//	 * JMS工具模板
//	 * @return
//	 */
//	@Bean
//	public JmsTemplate jmsQueueTemplate(){
//		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory(P_ACTIVEMQ_URL));
//		//支持发布订阅功能
//	    jmsTemplate.setPubSubDomain(true);
//	    return jmsTemplate;
//	}
//	
//	/**
//     * JMS 队列的监听容器工厂
//     */
//	@Bean(name = "jmsTopicListenerCF")
//	public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory() {
//	    DefaultJmsListenerContainerFactory factory =
//	            new DefaultJmsListenerContainerFactory();
//	    factory.setConnectionFactory(connectionFactory(C_ACTIVEMQ_URL));
//	    //设置连接数
//	    factory.setConcurrency("1");
//	    //重连间隔时间
//	    factory.setRecoveryInterval(1000L);
//	    //支持发布订阅功能
//	    factory.setPubSubDomain(true);
//	    return factory;
//	}
}
