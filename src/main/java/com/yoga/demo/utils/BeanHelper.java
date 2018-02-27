package com.yoga.demo.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

public class BeanHelper implements ApplicationContextAware {
	private static ApplicationContext appContext;
	private static String contextRealPath;
	
	/**
	 * @return the appContext
	 */
	public static final ApplicationContext getAppContext() {
		return appContext;
	}

	/**
	 * 
	 * @param beanName
	 * @return Spring Bean
	 */
	public static final Object getSpringBean(String beanName) {
		return appContext.getBean(beanName);
	}
	
	public static final <T> T getSpringBean(Class<T> beanType) {
		return appContext.getBean(beanType);
	}

	public static String getContextRealPath() {
		return contextRealPath;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
		if (appContext instanceof WebApplicationContext) {
			contextRealPath = ((WebApplicationContext)appContext).getServletContext().getRealPath("");
		}
	}
	
}
