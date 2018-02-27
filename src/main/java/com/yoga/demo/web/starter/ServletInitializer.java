package com.yoga.demo.web.starter;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.yoga.demo.DemoApplication;

/**
 * 覆盖configure()，把启动类Application注册进去。外部web应用服务器构建Web Application Context的时候，会把启动类添加进去。
 * 
 * @author yoga
 */
public class ServletInitializer extends SpringBootServletInitializer{
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }
}
