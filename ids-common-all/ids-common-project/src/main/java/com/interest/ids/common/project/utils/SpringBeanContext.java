package com.interest.ids.common.project.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 
 * @author lhq
 *
 */
public class SpringBeanContext implements ApplicationContextAware {
	
	private static ApplicationContext context;

	
	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		
		context = ctx;
	}

	
	public static ApplicationContext getContext() {
		return context;
	}

	
	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	/*@SuppressWarnings("unchecked")
	public static Map<String, Object> getBeanByType(Class b) {
		return context.getBeansOfType(b);
	}*/
}

