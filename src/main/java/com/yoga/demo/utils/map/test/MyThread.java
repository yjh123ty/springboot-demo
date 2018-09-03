package com.yoga.demo.utils.map.test;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyThread implements Runnable{

    private TestService service;

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("-------------------------------------");
            System.out.println(" 当前线程 :" + Thread.currentThread().getName() + "执行add操作 ... i = " + i);
            service.add("test");
            System.out.println(" 当前线程 :" + Thread.currentThread().getName() + "执行i " + i + "次后");
            service.printMap("test");
            System.out.println("-------------------------------------");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(" 当前线程 :" + Thread.currentThread().getName() + " 执行完毕");
    }
}
