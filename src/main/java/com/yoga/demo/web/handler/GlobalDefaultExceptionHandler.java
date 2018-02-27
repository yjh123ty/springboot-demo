package com.yoga.demo.web.handler;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yoga.demo.common.CodeConstants;
import com.yoga.demo.common.JsonMsgBean;
import com.yoga.demo.common.exception.BusinessException;
import com.yoga.demo.utils.result.JsonMsgBeanUtils;

/**
 * 全局异常捕获
 * 
 * @author yoga
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
		@ExceptionHandler(value = Exception.class)
		@ResponseBody
		public JsonMsgBean defaultErrorHandler(HttpServletRequest req, Exception e)  {
			JsonMsgBean result = new JsonMsgBean(false ,e.getMessage() ,CodeConstants.ERROR_CODE_SYSTEM_ERROR);
	      	//打印异常信息：
	       e.printStackTrace();
	       if(e instanceof BusinessException){
	    	   result = new JsonMsgBean(false ,e.getMessage() ,CodeConstants.ERROR_CODE_BUSINESS_EXCEPTION);
	       }else if (e instanceof UnknownAccountException) {
	    	   result = JsonMsgBeanUtils.authFail(e.getMessage());
	       }else if (e instanceof IncorrectCredentialsException) {
	    	   result = JsonMsgBeanUtils.authFail(e.getMessage());
	       }else if (e instanceof UnknownAccountException) {
	    	   result = JsonMsgBeanUtils.authFail(e.getMessage());
	       }
	       return result;
		}
}
