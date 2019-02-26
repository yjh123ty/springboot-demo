package com.yoga.demo.redis;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.yoga.demo.test.HelloWorldControlerTests;

@Ignore
public class RedisTest extends HelloWorldControlerTests{
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Test
	public void testName() throws Exception {
		redisTemplate.opsForValue().set("user", "admin");
	}
	
	@Test
	public void testGet() throws Exception {
		String string = redisTemplate.opsForValue().get("user");
		System.err.println(string);
	}
	
	@Test
	public void testDel() throws Exception {
		redisTemplate.opsForValue().getOperations().delete("user");
	}
}
