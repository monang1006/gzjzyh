package com.strongit.oa.officeocx;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;

import com.strongmvc.webapp.action.BaseActionSupport;
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "officeOCX.action", type = ServletActionRedirectResult.class) })
public class OfficeOCXAction extends BaseActionSupport<OfficeOCXBean>{

	private static final long serialVersionUID = 6805908507138777914L;
	private OfficeOCXBean model=new OfficeOCXBean();
	
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	//新建office文档
	public String input() throws Exception {
		prepareModel();
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		model = new OfficeOCXBean();
		model.setOpentype("new");
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	
	public void setModel(OfficeOCXBean model) {
		this.model = model;
	}

	public OfficeOCXBean getModel() {
		return model;
	}
		
	
}
