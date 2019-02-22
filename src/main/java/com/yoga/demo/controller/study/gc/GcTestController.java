package com.yoga.demo.controller.study.gc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("study")
@RestController
@Slf4j
public class GcTestController {

    @GetMapping("gc/test")
    public void testGC(){
        byte[] bts = new byte[1024 * 1024 * 10];// 代码段1
        long startTime = System.currentTimeMillis(); // deal
         try {
            // 模拟业务花费时间
             Thread.sleep(500);
         } catch (InterruptedException e) {
         }
         // 理论上这里输出500附近
         long costTime = (System.currentTimeMillis() - startTime);
         if (costTime > 600)
         {
            log.info("cost time:" + costTime);
         }
    }

}
