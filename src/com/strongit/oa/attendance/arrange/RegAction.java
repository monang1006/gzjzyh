package com.strongit.oa.attendance.arrange;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaAttendRegulation;
import com.strongit.oa.bo.ToaAttendSchedGroup;
import com.strongit.oa.bo.ToaAttendSchedule;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Nov 10, 2009 3:55:08 PM
 * @version  2.0.4
 * @comment  轮班规则Action
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/attendance/arrange/regulation.action", type = ServletActionRedirectResult.class) })
public class RegAction extends BaseActionSupport {

	private static final long serialVersionUID = 5931092668488897263L;
	private Page<ToaAttendRegulation> page = new Page<ToaAttendRegulation>(FlexTableTag.MAX_ROWS, true);
	private ToaAttendRegulation model = new ToaAttendRegulation();			//轮班规则bo
	private Map<String,String> cycleMap=new HashMap<String,String>();		//轮班周期Map
	private RegManager manager;				//轮班规则manager
	private SchedulesManager scheManager;	//班次Manager
	private String regId;					//轮班规则ID
	private String scheId;					//班次ID
	private String scheduleName;			//班次名称
	private ToaAttendSchedGroup scheGroup;	//班组BO 
	public RegAction() {
	}
	
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		ToaAttendSchedule schedule=scheManager.getSchedulesById(scheId);
		scheGroup=schedule.getToaAttendSchedGroup();
		scheduleName=schedule.getSchedulesName();
		model=manager.getRegulationByScheId(scheId);
		if(model!=null){
			regId=model.getRegId();
	   	}else{
	   		model=new ToaAttendRegulation();	
	   	}  
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
		String msg=manager.saveRegulation(model,scheId);
		addActionMessage(msg);
		StringBuffer returnhtml=new StringBuffer("");
		returnhtml.append(
		"<script> var scriptroot = '")
		.append(getRequest().getContextPath())
		.append("'</script>")
		.append("<SCRIPT src='")
		.append(getRequest().getContextPath())
		.append(
				"/common/js/commontab/workservice.js'>")
		.append("</SCRIPT>")
		.append("<SCRIPT src='")
		.append(getRequest().getContextPath())
		.append(
				"/common/js/commontab/service.js'>")
		.append("</SCRIPT>")
		.append("<script>");
		if(msg!=null&&!"".equals(msg)){
		    returnhtml.append("alert('")
			.append(getActionMessages().toString())
			.append("');");
		}
		returnhtml.append("window.dialogArguments.submitForm();window.close();")
		.append("</script>");
	    return renderHtml(returnhtml.toString());
	}
	
	public ToaAttendRegulation getModel() {
		return model;
	}

	public void setModel(ToaAttendRegulation model) {
		this.model = model;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public Page<ToaAttendRegulation> getPage() {
		return page;
	}
    
	@Autowired
	public void setManager(RegManager manager) {
		this.manager = manager;
	}

	public String getScheId() {
		return scheId;
	}

	public void setScheId(String scheId) {
		this.scheId = scheId;
	}

	public Map<String, String> getCycleMap() {
		return cycleMap;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	@Autowired
	public void setScheManager(SchedulesManager scheManager) {
		this.scheManager = scheManager;
	}

	public ToaAttendSchedGroup getScheGroup() {
		return scheGroup;
	}
	
}
