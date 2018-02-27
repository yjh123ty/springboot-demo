/**  
 * youmu Service Inc
 * All Rights Reserved @2017
 *
 */
package com.yoga.demo.utils.token;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;


/**
 * 
 * @author yujiahao
 * @version $Id: TokenUtil.java, v 0.1 2017年4月10日 下午5:17:07 yujiahao Exp $
 */
public class TokenUtil {
	
	private static Logger logger = LoggerFactory.getLogger(TokenUtil.class);
    
    private static Map<String,Long> tokenMap = new HashMap<>();
    
    //cookie过期时间为1小时
    private final static int EXPIRE_TIME = 1000 * 60 * 60 * 1;
    
    /**  token过期时间  7 day */
    public static int TOKEN_EXPIRE = 7 * 24 * 60 * 60 * 1000;
    
    /** session expired half hour */
    public static int SESSION_EXPIRE = 30 * 60 * 1000;
    
    /** 将认证信息放在cookie中的名称 */
    public static final String TOKEN_PREFIX = "Bearer ";
    
    public static void setCookie(HttpServletResponse response,String name,String value,int expire){
        Cookie cookie = new Cookie(name, value);
        if(expire != 0){
            cookie.setMaxAge(expire);
        }else{
            cookie.setMaxAge(EXPIRE_TIME);
        }
        //设置在同一应用服务器内共享方法,单web项目用不上
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 设置cookie
     * @param response
     * @param string
     * @param token
     */
    public static void setTokenCookie(HttpServletResponse response, String tokenname, String token) {
        setCookie(response, tokenname, token, EXPIRE_TIME);
    }

    /**
     * 获取cookie
     * @param request
     * @return
     */
    public static String getTokenInCookie(HttpServletRequest request,String name) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null || name == null || name.length() == 0)
                return null;
        Cookie cookie = null;
        for (int i = 0;i<cookies.length;i++) {
            if(!cookies[i].getName().equals(name))
                continue;
            cookie = cookies[i];
            if (request.getServerName().equals(cookie.getDomain()))
                break;
        }
        if(cookie == null){
            return null;
        }
        return cookie.getValue();
    }
    
    
    public static void removeCookie(HttpServletResponse response, String name)
    {
        if (name != null)
        {
            Cookie cookie = new Cookie(name, "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
    
    public static void setUserInMap(String token,Long uid){
        tokenMap.put(token,uid);
    }
    
    public static Long getUserInMap(String token){
        return tokenMap.get(token);
    }
    
    public static void removeTokenInMap(String token){
        tokenMap.remove(token);
    }

    /**
     * 
     * @param response
     * @param string
     */
    public static void removeTokenInCookie(HttpServletResponse response, String name) {
        if (name != null)
        {
            Cookie cookie = new Cookie(name, "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
    
    public static void printMap(){
        for(Map.Entry<String, Long> entry : tokenMap.entrySet()){
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
        }
    }

    /**
     * 
     * @param token
     * @return
     */
    public static String getUidInToken(String token) {
        return JwtUtils.validateToken(token);
    }
    
    /**
     * 从请求头中获取token信息.
     * @param request
     * @return
     */
    public static String getTokenInHeader(HttpServletRequest request) {
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(StringUtils.isNotBlank(header)) {
                return header.substring(TOKEN_PREFIX.length()).trim();
            }
        } catch (Exception e) {
            logger.error("从请求头中获取token错误", e);
        }
        return null;
    }
    
}
