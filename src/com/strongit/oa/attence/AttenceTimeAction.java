package com.strongit.oa.attence;

import java.util.Date;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaAttendanceTime;
import com.strongmvc.webapp.action.BaseActionSupport;



/**
 * @author       汤世风
 * @company      Strongit Ltd. (C) copyright
 * @date         2012年9月17日
 * @comment      考勤时间设定Action
 */

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "attenceTime.action", type = ServletActionRedirectResult.class) })
public class AttenceTimeAction extends BaseActionSupport {

	private ToaAttendanceTime model = new ToaAttendanceTime();
	private AttenceTimeManager attenceTimeManager;

	public AttenceTimeAction(){

	}

	public ToaAttendanceTime getModel() {
		return model;
	}

	public void setModel(ToaAttendanceTime model) {
		this.model = model;
	}

	public AttenceTimeManager getAttenceTimeManager() {
		return attenceTimeManager;
	}

	@Autowired
	public void setAttenceTimeManager(AttenceTimeManager attenceTimeManager) {
		this.attenceTimeManager = attenceTimeManager;
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
		model=attenceTimeManager.getLastRecord();
		return "set";
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		model.setTime(new Date());
		attenceTimeManager.save(model);
		return renderHtml("<script> window.location='"
				+ getRequest().getContextPath()
				+ "/attence/attenceTime.action';</script>");
	}
	
	

}
