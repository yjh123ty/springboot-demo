package com.yoga.demo.kafka;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.classification.InterfaceAudience.Private;
import org.apache.hadoop.classification.InterfaceAudience.Public;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProducerDemo{
	
	private Producer createProducer(){
		Properties properties = new Properties();  
        properties.put("bootstrap.servers", "192.168.0.8:9092,192.168.0.8:9093,192.168.0.8:9094");// 声明kafka broker ，要注意地址一定要正确
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer(properties);
        
	}
	
	public static void main(String[] args) {  
		String topic = "test";
		
		Properties properties = new Properties();  
        properties.put("bootstrap.servers", "192.168.0.8:9092,192.168.0.8:9093,192.168.0.8:9094");// 声明kafka broker ，要注意地址一定要正确
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer producer = null;
        try {
            producer = new KafkaProducer(properties);
            for (int i = 0; i < 100; i++) {
                String msg = "发送消息： " + i;
                producer.send(new ProducerRecord<String, String>(topic, msg));
                System.out.println("Sent:" + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            producer.close();
        }

    }  
}
