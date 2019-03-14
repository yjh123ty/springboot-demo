package com.yoga.demo.controller.wechat.smallapp;

import com.yoga.demo.utils.http.HttpUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class WXLoginController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/wxLogin")
    public WXJsonResult wxLogin(String code) {

        System.out.println("wxlogin - code: " + code);

//		https://api.weixin.qq.com/sns/jscode2session?
//				appid=APPID&
//				secret=SECRET&
//				js_code=JSCODE&
//				grant_type=authorization_code

        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=APPID" +
                "&secret=SECRET" +
                "&js_code=JS_CODE" +
                "&grant_type=authorization_code";

        url = url.replace("APPID", "wxa06422026c79a539");
        url = url.replace("SECRET", "5492786c3acc6dedb282c2bc8f49762d");
        url = url.replace("JS_CODE", code);

        String wxResult = HttpUtils.get(url);

        System.out.println(wxResult);

        WXSessionModle result = (WXSessionModle)JSONObject.toBean(JSONObject.fromObject(wxResult), WXSessionModle.class);

        // 存入session到redis
        redisTemplate.opsForValue().set("wx-user-session:" + result.getOpenid()
        ,result.getSession_key(), 30, TimeUnit.MINUTES);

        return WXJsonResult.ok();
    }

}

