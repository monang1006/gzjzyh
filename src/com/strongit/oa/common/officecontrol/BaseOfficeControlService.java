package com.strongit.oa.common.officecontrol;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.util.PropertiesUtil;

/**
 * OFFICE#OCX控件服务基类
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-12-8 上午11:03:45
 * @version  3.0
 * @classpath com.strongit.oa.common.officecontrol.BaseOfficeControlService
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class BaseOfficeControlService {
	
	protected Properties prop = new Properties();

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public BaseOfficeControlService() {
		try {
			prop = PropertiesUtil.getPropertiesWithFileName("appconfig.properties");
			logger.info("load file appconfig.properties success.--->" + prop);
		} catch (Exception e) {
			logger.error("加载文件appconfig.properties时发生错误", e);
		}
	}
	
	/**
	 * 得到上下文路径
	 * @author:邓志城
	 * @date:2010-12-8 上午11:03:21
	 * @return	工程上下文路径
	 */
	protected String getContext() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String path = request.getContextPath();
		if(path.endsWith("/")) {
			path="";
		}
		return path;
	}
}
