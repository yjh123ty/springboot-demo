package com.yoga.demo.manager.service.impl;

import com.yoga.demo.manager.service.IAnimal;
import org.springframework.stereotype.Service;

@Service("cat")
public class Cat implements IAnimal {
    @Override
    public void eat() {
        System.out.println("cat is eating...");
    }

    @Override
    public void run() {
        System.out.println("cat is running...");
    }
}
