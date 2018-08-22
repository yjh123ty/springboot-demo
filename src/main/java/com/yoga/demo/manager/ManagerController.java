package com.yoga.demo.manager;

import com.yoga.demo.utils.config.Config;
import com.yoga.demo.web.aop.anno.SignRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("manager/test")
public class ManagerController {

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
        return "var1: " + config.cfg.getVar1();
    }


}
