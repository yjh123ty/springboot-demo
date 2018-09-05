package com.yoga.demo.utils.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 获取配置文件中指定的 参数
 */
@ConfigurationProperties(prefix = "springdemo")
@Configuration
@Data
public class Config {

    public Cfg cfg;

    public Map<String, CommonMapInProp> commonMapInProp;

    @Data
    public static class Cfg{
        private String var1;
        private String var2;
        private String var3;
    }

    @Data
    public static class CommonMapInProp {
        private String appkey;
        private String secret;
    }

//    public void print(){
//        for (Map.Entry<String, CommonMapInProp> e : configInProp.entrySet()) {
//            System.out.println("CommonMapInProp -- >　key : " + e.getKey());
//            System.out.println("CommonMapInProp -- >　value : " + e.getValue());
//            System.out.println("-------");
//        }
//    }



}
