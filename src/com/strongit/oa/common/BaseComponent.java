package com.strongit.oa.common;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 提供基本的容器支持.
 * 集成Spring容器相关功能.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2009-12-28 下午03:46:42
 * @version  2.0.2.3
 * @classpath com.strongit.oa.common.BaseComponent
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class BaseComponent implements ApplicationContextAware{

	private ApplicationContext cxt ;//定义Spring上下文容器
	
	/**
	 * 注入Spring容器上下文,在容器启动时由Spring容器自动执行.
	 * @param applicationContext Spring容器上下文.
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.cxt = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return this.cxt;
	}
	
}
