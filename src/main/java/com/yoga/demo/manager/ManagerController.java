package com.yoga.demo.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manager")
public class ManagerController {

    @Autowired
    private ServiceManager serviceManager;

    @GetMapping("/test")
    public void test(@RequestParam String animalType){
        serviceManager.testMethod(animalType);
    }


}
