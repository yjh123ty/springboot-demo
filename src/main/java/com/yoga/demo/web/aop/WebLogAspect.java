package com.yoga.demo.web.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yoga.demo.common.Constants;
import com.yoga.demo.common.annotation.WebLog;
import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.domain.syslog.SysLog;
import com.yoga.demo.utils.shiro.ShiroUtils;

/**
 * 记录controller调用的日志
 * 
 * @author yoga
 */
@Aspect
@Component
public class WebLogAspect {
	private static Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
	
	@Pointcut("execution(public * com.yoga.demo.controller.*.*(..))")
    public void webLog() {
    }
	
	@Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        System.out.println( "进入doBefore切面");
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }
	
	@Around("webLog()")    
	public Object introcepter(ProceedingJoinPoint pjp) throws Throwable{    
	    System.err.println("拦截到了" + pjp.getSignature().getName() +"方法...");
	    MethodSignature methodSignature = (MethodSignature)pjp.getSignature();    
	    Method targetMethod = methodSignature.getMethod();    
	    Class<? extends Method> clazz = targetMethod.getClass();
	    
	    if(targetMethod.isAnnotationPresent(WebLog.class)){
	    	WebLog anno = targetMethod.getAnnotation(WebLog.class);
	    	// 接收到请求，记录请求内容
	        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	        HttpServletRequest request = attributes.getRequest();
	        //获取用户
	        UserInfo userInfo = ShiroUtils.getLoginUser();
	        if(userInfo != null){
	        	SysLog log = new SysLog();
	        	log.setUsername(userInfo.getUsername());
	        	log.setUri(request.getRequestURI());
	        	log.setCreateTime(new Date());
	        	log.setDelete(Boolean.FALSE);
	        	log.setDescription(anno.desc());
	        	log.setIp(request.getRemoteAddr());
	        	log.setAction(methodSignature.getDeclaringTypeName() + "." + methodSignature.getName());
	        	
	        	System.err.println(log.toString());
	        }
	    }
	    pjp.proceed();
		return pjp;    
	}    
}
