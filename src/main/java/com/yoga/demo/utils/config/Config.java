package com.yoga.demo.utils.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "springdemo")
@Configuration
@Data
public class Config {

    private static Cfg cfg;

    @Data
    private static class Cfg{
        private String var1;
        private String var2;
        private String var3;
    }

    public static void main(String[] args) {

        System.out.println(Config.cfg.getVar1());
    }

}
