package com.yoga.demo.utils.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.yoga.demo.common.Constants;
import com.yoga.demo.domain.shiro.UserInfo;


public class ShiroUtils {
	
	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}
	
	public static UserInfo getUser() {
		return (UserInfo)SecurityUtils.getSubject().getPrincipal();
	}

	public static Integer getUserId() {
		return getUser().getUid();
	}
	
	public static void setSessionAttribute(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static Object getSessionAttribute(Object key) {
		return getSession().getAttribute(key);
	}
	
	public static boolean isLogin() {
		return SecurityUtils.getSubject().getPrincipal() != null;
	}

	public static void logout() {
		SecurityUtils.getSubject().logout();
	}

	/**
	 * 获取验证码code
	 * @return
	 */
	public static String getCaptcha() {
		String result = getSessionAttribute(Constants.CAPTCHA_TOKEN).toString();
		getSession().removeAttribute(Constants.CAPTCHA_TOKEN);
		return result;
	}
	
	/**
	 * 获取登录用户
	 * @return
	 */
	public static UserInfo getLoginUser() {
		UserInfo userInfo = (UserInfo) getSessionAttribute(Constants.LOGIN_USER.concat("-").concat(String.valueOf(getUserId())));
		return userInfo;
	}
	
	
}
