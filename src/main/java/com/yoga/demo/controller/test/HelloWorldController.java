package com.yoga.demo.controller.test;

import com.yoga.demo.domain.dto.UserInfoSearchDTO;
import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@RestController
@Api("示例")
public class HelloWorldController {
	
	
//	@Autowired
//	private HelloSender helloSender;
//	@Autowired
//	private HelloReceiver helloReceiver;
	
	@Autowired
	Environment environment;

    @Autowired
    private UserInfoService userInfoService;

	
	@Value("${com.yoga.description}")
	private String description;
	
    @RequestMapping("/hello")
    public String index() {
    	//获取自定义资源文件的两种方式
        return "Hello World  " + environment.getProperty("com.yoga.title") + ": " + description;
    }
    
    @PostMapping("/getUser")
    @ApiOperation(value = "根据用户名查询人员")
    public UserInfo getUser(@RequestBody UserInfoSearchDTO dto) {
        return userInfoService.findByUsername(dto.getUsername());
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
