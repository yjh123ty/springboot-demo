package com.yoga.demo.utils.map.test;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TestService {
    private ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

    //这里同时进行了读写操作，因此虽然使用的是线程安全的容器，但是在操作层面，并不是原子操作（即又在读又在写）
    public  void add(String key){
        Integer value = map.get(key);
        if (value == null){
            map.put(key, 1);
        }else{
            map.put(key, 1 + value);
        }
    }

    public void printMap(String key){
        System.out.println(map.get(key));
    }
}
