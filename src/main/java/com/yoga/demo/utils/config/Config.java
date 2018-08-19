package com.yoga.demo.utils.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 获取配置文件中指定的 参数
 */
@ConfigurationProperties(prefix = "springdemo")
@Configuration
@Data
public class Config {

    public Cfg cfg;

    @Data
    public static class Cfg{
        private String var1;
        private String var2;
        private String var3;
    }

}
