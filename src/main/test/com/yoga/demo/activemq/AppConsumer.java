package com.yoga.demo.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;


/**
 * 消息的消费者
 * 
 * 1、注意，若多个消费者同时开启，那么他们是平均去消费队列中的消息，并且，队列中是先进先出，即是说有消息才会被消费者消费
 * 2、而在主题模式下，消费者必须事先订阅了主题，发布的主题消息，才会被消费者接受，并且，消费者接收的主题消息是不会被其他消费者消费的
 * 
 * @author yoga
 */
public class AppConsumer {
	private static final String url = "failover:(tcp://192.168.4.9:61616,tcp://192.168.4.9:61617,tcp://192.168.4.9:61618,tcp://192.168.4.9:61619,tcp://192.168.4.9:61620)?randomize=true";
	private static final String queueName = "queue-1";
	private static final String topicName = "topic-test";
	
	
	public static void main(String[] args) throws JMSException {
		//1.创建工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		
		//2.创建连接
		Connection connection = connectionFactory.createConnection();
		
		//3.启动连接
		connection.start();
		
		//4.创建会话：
		//参数1：是否使用事务，参数2：应答模式
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//5.创建一个目标
		Destination destination = session.createQueue(queueName);
		Destination destinationTopic = session.createTopic(topicName);
		
		//6.创建一个消费者
		MessageConsumer consumer = session.createConsumer(destination);
//		MessageConsumer consumer = session.createConsumer(destinationTopic);
		
		//7.创建一个监听器
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				TextMessage messgae = (TextMessage) message;
				try {
					System.err.println("接收消息" + messgae.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		
		//关闭连接,连接是异步的，因此需要在程序运行完毕才关闭，否则报错
		connection.close();
	}
}
