package com.yoga.demo.utils.result;

import com.yoga.demo.common.CodeConstants;
import com.yoga.demo.common.JsonMsgBean;

public class JsonMsgBeanUtils {
	
	public static JsonMsgBean defaultSeccess(){
		return new JsonMsgBean(true, null, 0 ,null);
	}
	
	public static JsonMsgBean defaultSeccess(Object data){
		return new JsonMsgBean(true, null, 0 ,data);
	}
	
	public static JsonMsgBean success(String message, Object data){
		return new JsonMsgBean(true, message, 0 ,data);
	}
	
	public static JsonMsgBean fail(String message, Integer errorCode){
		return new JsonMsgBean(false, message, errorCode ,null);
	}
	
	public static JsonMsgBean authFail(String message){
		return new JsonMsgBean(false, message, CodeConstants.ERROR_CODE_ILLEGAL_LOGIN ,null);
	}
}	

