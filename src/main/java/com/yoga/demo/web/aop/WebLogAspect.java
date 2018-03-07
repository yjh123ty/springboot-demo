package com.yoga.demo.web.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yoga.demo.common.annotation.WebLog;
import com.yoga.demo.domain.shiro.UserInfo;
import com.yoga.demo.domain.syslog.SysLog;
import com.yoga.demo.service.SysLogService;
import com.yoga.demo.utils.shiro.ShiroUtils;

import net.sf.json.JSONObject;

/**
 * 记录controller调用的日志
 * 
 * @author yoga
 */
@Aspect
@Component
public class WebLogAspect {
	private static Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
	
	@Autowired
	private SysLogService sysLogService;
	
	@Pointcut("execution(public * com.yoga.demo.controller..*.*(..))")
    public void webLog() {
    }
	
	@Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        
        logger.info("*********【start LOG】**********");
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        logger.info("*********【END LOG】**********");

    }
	
	
	@After("webLog()")
	public  void after(JoinPoint joinPoint) { 
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String targetName = joinPoint.getTarget().getClass().getName();  
        String methodName = joinPoint.getSignature().getName();  
        Object[] arguments = joinPoint.getArgs();  
        Class<?> targetClass = null;
        try {
            targetClass = Class.forName(targetName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }  
        Method[] methods = targetClass.getMethods();
        // 获取注解对象
        for (Method method : methods) {  
            if (method.getName().equals(methodName)) {  
                Class<?>[] clazzs = method.getParameterTypes();  
                String desc = "";
                if (clazzs!=null && clazzs.length == arguments.length && method.getAnnotation(WebLog.class) != null) {  
                	desc = method.getAnnotation(WebLog.class).desc();
                	JSONObject json = new JSONObject();
        			for (Object arg : arguments) {
        				json.put(arg.getClass().getName(), arg.toString());
        			}
        			String paramsStr = json == null ? "" : json.toString();
        			
        			//获取用户
        	        UserInfo userInfo = ShiroUtils.getLoginUser();
        	        // 处理log
        	        if(userInfo != null){
        	        	SysLog log = new SysLog();
        	        	log.setUsername(userInfo.getUsername());
        	        	log.setUri(request.getRequestURI());
        	        	log.setCreateTime(new Date());
        	        	log.setDelete(Boolean.FALSE);
        	        	log.setDescription(desc);
        	        	log.setIp(request.getRemoteAddr());
        	        	log.setAction(targetClass.getName() + "." + methodName);
        	        	log.setRequestBody(paramsStr);
        	        	log.setCreateTime(new Date());
        	        	sysLogService.saveLog(log);
        	        }
                }
            }  
        }
    }
}
