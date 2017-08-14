package com.strongit.oa.component.formdata;

import org.apache.struts2.config.ParentPackage;

import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
public class FormGridDataAction extends BaseActionSupport<TaskBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2913472260216350062L;

	private String todoType;//任务签收类型：notsign：未签收，sign:签收的任务
	
	TaskBean model = new TaskBean();
	
	private String type;
	
	private String tableName;

	public String draft() {
		this.setType("3");
		return "draft";
	}
	
	public String hostedby() {
		this.setType("2");
		return "hostedby";
	}

	public String processed() {
		this.setType("1");
		return "processed";
	}
	
	public String todo() {
		this.setType("0");
		return "todo";
	}

	public String getTodoType() {
		return todoType;
	}

	public TaskBean getModel() {
		return model;
	}

	public void setTodoType(String todoType) {
		this.todoType = todoType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
