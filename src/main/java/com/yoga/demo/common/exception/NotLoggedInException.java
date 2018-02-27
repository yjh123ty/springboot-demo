package com.yoga.demo.common.exception;

import com.yoga.demo.common.CodeConstants;

public class NotLoggedInException extends BusinessException {

	private static final long serialVersionUID = 1404297308561502414L;
	
	public NotLoggedInException(String errorKey, String errorMessageKey) {
		super(errorKey, errorMessageKey);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public byte buildErrorCode() {
		return CodeConstants.ERROR_CODE_NOT_LOGGED_IN;
	}

}
