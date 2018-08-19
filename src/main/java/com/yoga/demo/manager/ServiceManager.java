package com.yoga.demo.manager;

import com.yoga.demo.manager.service.IAnimal;
import com.yoga.demo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class ServiceManager {

    @Autowired
    private Map<String, IAnimal> animals;

    public void testMethod(String key){
        System.out.println("start testMethod()...");
        if (animals.get(key) != null){
            IAnimal animal = animals.get(key);
            animal.eat();
            animal.run();
        }
    }

}
