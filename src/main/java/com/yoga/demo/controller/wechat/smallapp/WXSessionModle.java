package com.yoga.demo.controller.wechat.smallapp;

public class WXSessionModle {
    private String session_key;
    private String openid;

    public String getSession_key() {
        return session_key;
    }
    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }
    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
