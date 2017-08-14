package com.strongit.oa.attendance.autoset;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaAttendPlan;
import com.strongmvc.webapp.action.BaseActionSupport;
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/attendance/autoset/plan.action", type = ServletActionRedirectResult.class) })
public class PlanAction extends BaseActionSupport {

	private ToaAttendPlan model=new ToaAttendPlan();
    @Autowired private PlanManager manager;
    private String planId;
    
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
		model=manager.getPlan();
	}

	@Override
	public String save() throws Exception {
		if("".equals(model.getPlanId())){
			model.setPlanId(null);
		}
		if(model.getPlanLtime()==null){
			model.setPlanLtime(model.getPlanStime());
		}
		manager.save(model);
		return INPUT;
	}

	public ToaAttendPlan getModel() {
		return model;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

}
