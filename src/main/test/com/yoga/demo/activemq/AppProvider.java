package com.yoga.demo.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 向队列发送消息的类
 * 
 * @author yoga
 */
public class AppProvider {
	
	private static final String url = "failover:(tcp://192.168.4.9:61619,tcp://192.168.4.9:61620)?randomize=true";
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
//		Destination destinationTopic = session.createQueue(topicName);
		
		//6.创建一个生产者
		MessageProducer producer = session.createProducer(destination);
//		MessageProducer producer = session.createProducer(destinationTopic);
		
		for (int i = 0; i < 10; i++) {
			//7.创建消息
			TextMessage messgae = session.createTextMessage("text " + i);
			producer.send(messgae);
			
			System.err.println("发送消息" + messgae.getText());
		}
		
		//关闭连接
		connection.close();
	}
}

