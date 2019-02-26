package com.yoga.demo.test;

import com.yoga.demo.BaseTest;
import com.yoga.demo.mapper.order.OrderItemMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class mapperTest extends BaseTest {
	
	@Autowired
	OrderItemMapper itemMapper;
	
	@Test
	@Ignore
	public void testName() throws Exception {
		System.err.println(itemMapper.selectByPrimaryKey(1));
	}
}
