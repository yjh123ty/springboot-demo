package com.yoga.demo.test;

import com.yoga.demo.BaseTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.service.UserInfoService;

public class UserServiceTest extends BaseTest {
	
	@Autowired
	UserInfoService userInfoService;
	
	
	@Test
	@Ignore()
	public void testUser() throws Exception {
		UserInfo user = userInfoService.findByUsername("admin");
		System.err.println(user);
	}
	
	
	
}
