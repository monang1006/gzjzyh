package com.strongit.oa.common.workflow.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 标注类的作用
 * 目前用于显示工作流中的自动节点作用,方便自动节点类的管理。
 * OA系统中存在很多用到自动节点的地方.基于这个考虑设置此接口
 * 来管理代理类.
 */
public @interface Use {

	String value() default "";//代理类功能
	
	AgentClassType type() default AgentClassType.AUTONODECLASS;//代理类类型：默认为自动节点类型
}
