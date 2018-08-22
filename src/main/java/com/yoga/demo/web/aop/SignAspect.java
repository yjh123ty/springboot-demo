package com.yoga.demo.web.aop;

import com.yoga.demo.web.aop.anno.SignRequired;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SignAspect {
    private static Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut(value = "com.yoga.demo.web.aop.anno.SignRequired")
    public void sign() {
    }

    @Around("@annotation(signRequired)")
    public void doSign(ProceedingJoinPoint joinPoint, SignRequired signRequired) throws Throwable {
        System.out.println("进入切面 doSign...");

        int i = 1;
        for (Object o : joinPoint.getArgs()) {
            System.out.println(i + " args : " + o.toString());
            i++;
        }
        System.out.println("标签上的value  : " + signRequired.value());

        joinPoint.proceed();
    }
}
