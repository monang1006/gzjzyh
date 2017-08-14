package com.strongit.oa.formconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TefTemplateConfig;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.user.IUserService;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Jan 18, 2010 11:55:37 AM
 * @version  2.0.4
 * @comment  收文、发文表单配置Action
 */
@ParentPackage("default")
public class ConfigAction extends BaseActionSupport {

	private TefTemplateConfig model;
	
	private ConfigManager manager;
	
	private String processTypeId;
	
	private String sequeceStr;
	
	
	
	List<EForm> formList=new ArrayList<EForm>();
	
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		formList=manager.getRelativeFormByProcessType(processTypeId);
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
	}

	@Override
	public String save() throws Exception {
		return null;
	}
	
	public String config() throws Exception {
		renderText(manager.configForm(sequeceStr, processTypeId));
		return null;
	}
	

	public TefTemplateConfig getModel() {
		return model;
	}

	public void setModel(TefTemplateConfig model) {
		this.model = model;
	}

	@Autowired
	public void setManager(ConfigManager manager) {
		this.manager = manager;
	}

	public List<EForm> getFormList() {
		return formList;
	}

	public void setProcessTypeId(String processTypeId) {
		this.processTypeId = processTypeId;
	}

	public String getProcessTypeId() {
		return processTypeId;
	}

	public void setSequeceStr(String sequeceStr) {
		this.sequeceStr = sequeceStr;
	}
}
