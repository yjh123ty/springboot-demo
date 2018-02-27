package com.yoga.demo.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.service.UserInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserServiceTest {
	
	@Autowired
	UserInfoService userInfoService;
	
	
	@Test
	public void testUser() throws Exception {
		UserInfo user = userInfoService.findByUsername("admin");
		System.err.println(user);
	}
	
	
	
}
