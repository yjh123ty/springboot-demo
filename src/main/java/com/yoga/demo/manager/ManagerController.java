package com.yoga.demo.manager;

import com.yoga.demo.utils.config.Config;
import com.yoga.demo.web.aop.anno.SignRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 测试 controller
 */
@RestController
@RequestMapping("manager/test")
public class ManagerController {

    @Value("#{config.commonMapInProp}")
    public Map<String, Config.CommonMapInProp> commonMapInProp;

    @Autowired
    private ServiceManager serviceManager;

    @Autowired
    private Config config;

    @GetMapping("/animal")
    @SignRequired
    public void test(@RequestParam String animalType){
        serviceManager.testMethod(animalType);
    }

    @GetMapping("/sign")
    @SignRequired("xxx")
    @ResponseBody
    public String testSign(){
        Config.CommonMapInProp jk = commonMapInProp.get("JK");
        System.out.println(jk.getAppkey());
        System.out.println(jk.getSecret());
        return "var1: " + config.cfg.getVar1();
    }

}
