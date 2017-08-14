package com.strongit.oa.relat;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaWorkForm;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "workForm.action", type = ServletActionRedirectResult.class) })
public class WorkFormAction extends BaseActionSupport {

	private WorkFormManager manager;
	private ToaWorkForm model = new ToaWorkForm();

	private String formId;
	private String formName;
	private String workflowId;
	private String workflowName;

	/**
	 * 标示流程是挂接查询表单还是展现表单
	 * <p>{query:查询表单;view:展现表单}</p>
	 */
	private String type;

	@Autowired
	public void setManager(WorkFormManager manager) {
		this.manager = manager;
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
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

		this.model = this.manager.getWorkForm(this.workflowId);
		if (null != this.model) {
			if ("query".equals(this.type)) {
				this.model.setQformId(Long.parseLong(this.formId));
				this.model.setQformName(this.formName);
			}
			if ("view".equals(this.type)) {
				this.model.setVformId(Long.parseLong(this.formId));
				this.model.setVformName(this.formName);
			}

		} else {

			this.model = new ToaWorkForm();
			if ("query".equals(this.type)) {
				this.model.setQformId(Long.parseLong(this.formId));
				this.model.setQformName(this.formName);
				this.model.setVformName("");
			}
			if ("view".equals(this.type)) {
				this.model.setVformId(Long.parseLong(this.formId));
				this.model.setVformName(this.formName);
				this.model.setQformName("");
			}
			this.model.setPfId(Long.parseLong(this.workflowId));
			this.model.setPfName(this.workflowName);
		}
		try {
			this.manager.addWorkForm(this.model);
			return renderText("ok");
		} catch (Exception e) {
			e.printStackTrace();
			return renderText("error");
		}

	}

	public ToaWorkForm getModel() {
		return null;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		try {
			formName = URLDecoder.decode(formName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.formName = formName;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		try {
			workflowName = URLDecoder.decode(workflowName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.workflowName = workflowName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
