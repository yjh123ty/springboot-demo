package com.yoga.demo.test;

import static org.junit.Assert.*;

import com.yoga.demo.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yoga.demo.mapper.order.OrderItemMapper;

public class mapperTest extends BaseTest {
	
	@Autowired
	OrderItemMapper itemMapper;
	
	@Test
	public void testName() throws Exception {
		System.err.println(itemMapper.selectByPrimaryKey(1));
	}
}
