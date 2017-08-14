package com.strongit.oa.globalconfig;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaGlobalConfig;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 处理全局配置项Action.
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Feb 15, 2012
 * @classpath	com.strongit.oa.config.GlobalConfigAction
 * @version  	5.0
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
@ParentPackage("default")
public class GlobalConfigAction extends BaseActionSupport<ToaGlobalConfig> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1087046632501710815L;

	private String pluginData;						//前台传递过来的配置项,Json格式。
	
	private List<ToaGlobalConfig> plugins;
	
	@Autowired
	private GlobalConfigService manager;
	
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		plugins = manager.getPlugins();
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 保存全局配置项.
	 */
	@Override
	public String save() throws Exception {
		String ret = "0";
		try {
			manager.save(pluginData);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ret = "-1";
		}
		return this.renderText(ret);
	}

	public ToaGlobalConfig getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPluginData(String pluginData) {
		this.pluginData = pluginData;
	}

	public List<ToaGlobalConfig> getPlugins() {
		return plugins;
	}

}
