package com.yoga.demo.utils.map.test;

public class TestDemo {
    public static void main(String[] args) throws InterruptedException {
        TestService service = new TestService();

        Thread a = new Thread(new MyThread(service));
        Thread b = new Thread(new MyThread(service));

        a.start();
        Thread.sleep(500);

        b.start();

        Thread.sleep(2000);
        service.printMap("test");
    }
}
