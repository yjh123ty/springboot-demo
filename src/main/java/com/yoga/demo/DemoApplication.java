package com.yoga.demo;

import com.yoga.demo.utils.wechat.SingleAccessToken;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 指定session失效时间 http://stackoverflow.com/questions/32501541/what-is-the-default-session-timeout-and-how-to-configure-it-when-using-the-sprin
 * @EnableRedisHttpSession 这个注解声明了一个bean,是一个过滤器 springSessionRepositoryFilter,实现 HttpSession超时后的替换功能
 * 
 * 
 */
@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1200) //2分钟失效 60 * 2  springSessionRepositoryFilte
@MapperScan("com.yoga.demo.mapper")	//扫描指定包下的注解
//@EnableScheduling				//在启动类上面加上@EnableScheduling即可开启定时。
@EnableTransactionManagement
@ImportResource(locations = {"classpath:spring/spring-context.xml"})
@ComponentScan(basePackages = {"com.yoga.demo"})
public class DemoApplication{

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		System.err.println(SingleAccessToken.getInstance().getAccessToken());
	}
}

