package com.strongit.oa.common.custom.workflow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaGlobalConfig;
import com.strongit.oa.common.custom.workflow.ICustomWorkflowService;
import com.strongit.oa.globalconfig.GlobalConfigService;

@Service
@Transactional
public class CustomWorkflowService implements ICustomWorkflowService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	GlobalConfigService globalConfigService;

	/**
	 * 是否启动主办权限
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 5, 2012 2:46:23 PM
	 */
	public boolean isEnableMainDoingFunction() {
		ToaGlobalConfig toaGlobalConfig = globalConfigService
				.getToaGlobalConfig("plugins_maindoing");
		String value = "1";// 插件默认值为1，表示默认是启用模式
		if (toaGlobalConfig != null) {
			if (toaGlobalConfig.getGlobalValue() != null) {
				value = toaGlobalConfig.getGlobalValue();
			}
		}
		if ("1".equals(value)) {
			logger.info("#####当前环境已经启用了主办功能");
			return true;
		} else {
			logger.info("#####当前环境没有启用了主办功能");
			return false;
		}
	}
}
