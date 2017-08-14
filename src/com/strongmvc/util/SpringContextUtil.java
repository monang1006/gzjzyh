/**
 * SpringContext工具类，为应用系统提供spring上下文
 * @author 喻斌
 * @company Strongit Ltd. (c) copyright
 * @date Aug 9, 2010 9:45:05 AM
 * @version 1.0
 * @classpath com.strongmvc.util.SpringContextUtil
 */
package com.strongmvc.util;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.applicationContext = context;

	}
	
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	/**
	 * 获得SpringContext上下文中的国际化消息
	 * @author 喻斌
	 * @date Aug 10, 2010 2:58:15 AM
	 * @param messageConst -消息名称常量
	 * @param params -参数数组
	 * @param locale -国际化信息，为null则取系统默认语种
	 * @return String 国际化消息
	 */
	public static String getMessage(String messageConst, Object[] params, Locale locale){
		return applicationContext.getMessage(messageConst, params, locale);
	}
}
