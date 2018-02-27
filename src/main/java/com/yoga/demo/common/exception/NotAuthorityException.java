package com.yoga.demo.common.exception;

import com.yoga.demo.common.CodeConstants;

public class NotAuthorityException extends BusinessException {

	private static final long serialVersionUID = -5708008307239178947L;

	public NotAuthorityException(String errorKey, String errorMessageKey) {
		super(errorKey, errorMessageKey);
	}
	
	@Override
	public byte buildErrorCode() {
		return CodeConstants.ERROR_CODE_NOT_AUTHORITY;
	}

}
