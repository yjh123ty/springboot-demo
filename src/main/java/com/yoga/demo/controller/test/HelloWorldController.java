package com.yoga.demo.controller.test;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yoga.demo.domain.shiro.UserInfo;

@RestController
public class HelloWorldController {
	
	
//	@Autowired
//	private HelloSender helloSender;
//	@Autowired
//	private HelloReceiver helloReceiver;
	
	@Autowired
	Environment environment;
	
	@Value("${com.yoga.description}")
	private String description;
	
    @RequestMapping("/hello")
    public String index() {
    	//获取自定义资源文件的两种方式
        return "Hello World  " + environment.getProperty("com.yoga.title") + ": " + description;
    }
    
    @RequestMapping("/getUser")
    public UserInfo getUser() {
    	UserInfo user=new UserInfo();
        user.setName("小明");
        return user;
    }
    
    @RequestMapping("/uid")
    String uid(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);
        return session.getId();
    }
    
//    @RequestMapping("/send")
//    public String send(String string) {
//    	helloSender.send();
//        return "SUCCESS!";
//    }
    
}
