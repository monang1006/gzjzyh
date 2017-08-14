package com.strongit.oa.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Title: OALogger.java</p>
 * <p>Description: 日志信息注解类</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2009-11-23 18:38:01
 * @version  1.0
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME)
public @interface OALogger {
	public String description() default "The annotation of OALog";
}
