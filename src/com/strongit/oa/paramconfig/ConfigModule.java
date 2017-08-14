package com.strongit.oa.paramconfig;

/**
 * 定义系统设置模块类别.采用枚举方式
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-8-3 下午02:08:19
 * @version  2.0.2.3
 * @classpath com.strongit.oa.paramconfig.ConfigModule
 * @comment
 * @email dengzc@strongit.com.cn
 */
public enum ConfigModule {

	PRSNFLDR("0"),						//个人文件柜
	
	MESSAGE("1"),						//消息模块

	CALENDAR("2"),						//日程模块
	
	NOTIFY("3");						//新闻公告模块

	String value ;
	
	ConfigModule(String value){
		this.value = value ;
	}
	
	public String getValue() {
		return this.value;
	}
}
