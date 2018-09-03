package com.yoga.demo.controller;

import com.yoga.demo.utils.config.Config;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Api(value = "主页接口", description = "")
public class MainController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${show.env}")
	private String env;

	@Autowired
	private Config config;
	
	@RequestMapping(value = "/",method = RequestMethod.GET)
    public String main(Model model){
		log.info("当前的系统环境为{}.", env);
		System.out.println("读取配置文件： var1: " + config.cfg.getVar1() + " var2:" + config.cfg.getVar2());
		return "pages/main/index";
    }
	
	@RequestMapping(value ="/main/index",method = RequestMethod.GET)
    public String mainIndex(Model model){
		log.info("当前的系统环境为{}.", env);
        return "pages/main/main_info";
    }

}
