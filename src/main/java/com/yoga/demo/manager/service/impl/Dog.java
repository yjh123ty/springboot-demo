package com.yoga.demo.manager.service.impl;

import com.yoga.demo.manager.service.IAnimal;
import org.springframework.stereotype.Service;

@Service("dog")
public class Dog implements IAnimal {
    @Override
    public void eat() {
        System.out.println("dog is eating...");
    }

    @Override
    public void run() {
        System.out.println("dog is running...");
    }
}
