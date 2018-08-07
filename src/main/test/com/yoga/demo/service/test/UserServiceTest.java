package com.yoga.demo.service.test;

import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yoga.demo.BaseTest;
import com.yoga.demo.service.UserInfoService;

public class UserServiceTest extends BaseTest {
	
	@Autowired
	private UserInfoService userInfoService;
	
	private ExecutorService executors = Executors.newCachedThreadPool();

	@Test
	public void testName() throws Exception {
		userInfoService.delete(20);
		
//		Future submit = executors.submit(new TaskHandler(1));
		System.err.println("task over ...");
		
	}
	
	
}
