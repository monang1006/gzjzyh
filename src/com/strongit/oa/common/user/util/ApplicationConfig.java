package com.strongit.oa.common.user.util;

import org.springframework.stereotype.Service;

import com.strongit.oa.common.user.util.Const;

/**
 * 应用全局设置类
 * 
 * @author 喻斌
 * @company Strongit Ltd. (c) copyright
 * @date Apr 7, 2009 3:34:48 PM
 * @version 1.0.0.0
 * @classpath com.strongit.uums.security.ApplicationConfig.java
 */
@Service
public class ApplicationConfig {

	// 初始化登录用户参数
	public ApplicationConfig() {
		Const.LOGIN_SYS_NUM = 0;
	}

	// 是否设置密码MD5加密
	private static boolean md5Enable;

	public static boolean isMd5Enable() {
		return md5Enable;
	}

	public void setMd5Enable(boolean md5Enable) {
		ApplicationConfig.md5Enable = md5Enable;
	}
	
	// 当前应用系统编码
	private static String systemCode;

	public static String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		ApplicationConfig.systemCode = systemCode;
	}
	
	// 单点登录模式下应用系统选择页面跳转URL
	private static String systemSelectUrl;

	public static String getSystemSelectUrl() {
		return systemSelectUrl;
	}

	public void setSystemSelectUrl(String systemSelectUrl) {
		ApplicationConfig.systemSelectUrl = systemSelectUrl;
	}
	
	// 是否采用单点登录模式
	private static boolean ssoModeEnabled;

	public static boolean isSsoModeEnabled() {
		return ssoModeEnabled;
	}

	public void setSsoModeEnabled(boolean ssoModeEnabled) {
		ApplicationConfig.ssoModeEnabled = ssoModeEnabled;
	}
	
	// 单点登录模式下当前在线用户获取URL
	private static String onlineUserUrl;

	public static String getOnlineUserUrl() {
		return onlineUserUrl;
	}

	public void setOnlineUserUrl(String onlineUserUrl) {
		ApplicationConfig.onlineUserUrl = onlineUserUrl;
	}
	
	
}
