package com.yoga.demo.common;

public class CodeConstants {
	public final static Integer ERROR_CODE_SUCCESS = 0;//成功，没有错误
	public final static Integer ERROR_CODE_NOT_LOGGED_IN = 1;//没有登录，或没有进行认证
	public final static Integer ERROR_CODE_DATA_CHECK_FAILURE = 2;//数据验证失败
	public final static Integer ERROR_CODE_BUSINESS_EXCEPTION = 3;//业务操作异常
	public final static Integer ERROR_CODE_NOT_AUTHORITY = 4;//没有操作权限
	public final static Integer ERROR_CODE_DATA_OPERATION_EXCEPTION = 5;//数据操作异常
	public final static Integer ERROR_CODE_VIEW_EXCEPTION = 6;//显示层异常
	public final static Integer ERROR_CODE_TOKEN_EXCEPTION = 7;//授权码非法
	public final static Integer ERROE_CODE_CONFIRM_MESSAGE = 8;//需confirm的消息code
	public final static Integer ERROR_CODE_ILLEGAL_CODE = 9;//验证码输入错误
	public final static Integer ERROR_CODE_ILLEGAL_LOGIN = 10;//登录验证错误
	public final static Integer ERROR_CODE_ILLEGAL_REGISTER = 11;//注册验证错误
	public final static Integer ERROR_CODE_FAIL_SEND_EMAIL = 11;//发送邮件错误
	public final static Integer ERROR_CODE_SYSTEM_ERROR = 127;//系统错误
}
