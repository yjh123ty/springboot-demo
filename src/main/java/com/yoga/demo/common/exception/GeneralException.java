package com.yoga.demo.common.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;

import com.yoga.demo.utils.BeanHelper;


public abstract class GeneralException extends RuntimeException {
	private static final long serialVersionUID = -8264507012525974460L;


	private String errorMessageKey;
	private List<Object> messageArgs;
		
	public abstract Integer buildErrorCode();
	
	public GeneralException(String errorMessageKey, Throwable e) {
		super(errorMessageKey, e);
	}
	
	public GeneralException(String errorMessageKey,Object arg, Throwable e) {
		super(errorMessageKey, e);
		this.errorMessageKey = errorMessageKey;
		this.messageArgs = new ArrayList<Object>();
		this.messageArgs.add(arg);
	}
	
	public GeneralException(String errorMessageKey,Object[] argArray, Throwable e) {
		super(errorMessageKey, e);
		this.errorMessageKey = errorMessageKey;
		this.messageArgs =  new ArrayList<Object>();
		for(Object arg : argArray){
			this.messageArgs.add(arg);
		}
	}

	public GeneralException(String errorMessageKey) {
		super(errorMessageKey);
		this.errorMessageKey = errorMessageKey;
	}
		
	public GeneralException(String errorMessageKey,Object arg){
		super(errorMessageKey);
		this.errorMessageKey = errorMessageKey;
		this.messageArgs = new ArrayList<Object>();
		this.messageArgs.add(arg);
	}
	
	public GeneralException(String errorMessageKey,Object[] argArray){
		super(errorMessageKey);
		this.errorMessageKey = errorMessageKey;
		this.messageArgs =  new ArrayList<Object>();
		for(Object arg : argArray){
			this.messageArgs.add(arg);
		}
	}
	
	public String getErrorMessageKey() {
		return errorMessageKey;
	}

	public List<Object> getMessageArgs() {
		return messageArgs;
	}

	public void setErrorMessageKey(String errorMessageKey) {
		this.errorMessageKey = errorMessageKey;
	}

	public void setMessageArgs(List<Object> messageArgs) {
		this.messageArgs = messageArgs;
	}
	
	@Override
	public String getLocalizedMessage() {
		try {
			ResourceBundleMessageSource resource = BeanHelper.getSpringBean(ResourceBundleMessageSource.class);
			return resource.getMessage(this.getErrorMessageKey(), this.getMessageArgs() == null ? null : this.getMessageArgs().toArray(), Locale.CHINESE);
		} catch (Exception e) {
			e.printStackTrace();
			return super.getLocalizedMessage();
		}
    }
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName());
		builder.append(" [errorMessageKey=");
		builder.append(errorMessageKey);
		builder.append(", messageArgs=");
		builder.append(messageArgs);
		builder.append(", errorCode=");
		builder.append(buildErrorCode());
		builder.append("]");
		return builder.toString();
	}


}
