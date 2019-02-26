package com.yoga.demo.service.test;

import com.yoga.demo.BaseTestNG;
import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.service.UserInfoService;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserServiceTest extends BaseTestNG {

	@Autowired
	private UserInfoService userInfoService;

	private ExecutorService executors = Executors.newCachedThreadPool();

	@Test
	public void findByUsernameTest(){
		UserInfo yu = userInfoService.findByUsername("yu");
//		Assert.notNull(yu, "查询的用户不存在!");
		System.err.println("findByUsernameTest --> " + yu);
	}

	@Test
//	@Rollback(false) //添加此行，不然不会做commit
	public void testDelete() throws Exception {
		userInfoService.delete(20);
		System.err.println("testDelete --> delete userId :" + 20);

	}


}
