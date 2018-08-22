package com.yoga.demo.web.aop.anno;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SignRequired {
    String value() default "testSign";
}
