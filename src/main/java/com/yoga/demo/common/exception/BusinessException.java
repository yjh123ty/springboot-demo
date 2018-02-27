package com.yoga.demo.common.exception;

public class BusinessException extends GeneralException {
	private static final long serialVersionUID = -338182902556309562L;

	private String errorKey;
	private Object errorData;
	
	 public BusinessException(String message) {
	        super(message);
	    }
	
	public BusinessException(String errorMessageKey, Object errorData) {
		super(errorMessageKey);
		this.errorData = errorData;
	}
	
	public BusinessException(String errorKey, String errorMessageKey) {
		super(errorMessageKey);
		this.errorKey = errorKey;
	}

	public BusinessException(String errorKey, String errorMessageKey, Object arg) {
		super(errorMessageKey, arg);
		this.errorKey = errorKey;
	}

	public BusinessException(String errorKey, String errorMessageKey, Object[] argArray) {
		super(errorMessageKey, argArray);
		this.errorKey = errorKey;
	}
	
	@Override
	public byte buildErrorCode() {
		return 3;
	}

	/**
	 * @return the errorKey
	 */
	public String getErrorKey() {
		return errorKey;
	}

	/**
	 * @param errorKey the errorKey to set
	 */
	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	public Object getErrorData() {
		return errorData;
	}

	public void setErrorData(Object errorData) {
		this.errorData = errorData;
	}

	
}
