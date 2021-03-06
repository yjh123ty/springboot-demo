package com.yoga.demo.web.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring Boot自动添加了OrderedCharacterEncodingFilter和HiddenHttpMethodFilter，并且我们可以自定义Filter。

	两个步骤：
	
	1、实现Filter接口，实现Filter方法
	2、添加@Configuration 注解，将自定义Filter加入过滤链
 * 
 * @author yoga
 */
@Configuration
public class WebMvcConfig  extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}


//	@Bean
//    public RemoteIpFilter remoteIpFilter() {
//        return new RemoteIpFilter();
//    }
//	
//	 @Bean
//     public FilterRegistrationBean testFilterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new AuthFilter());
//        registration.addUrlPatterns("/main/index");
//        //添加不需要忽略的格式信息.
//        registration.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/login,/checkLogin,/static/**");
//        registration.setName("MyFilter");
//        registration.setOrder(1);
//        return registration;
//     }
	
}
