package com.strongit.oa.attendance.arrange;
import java.util.Date;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.strongit.oa.bo.ToaAttendSchedGroup;
import com.strongit.oa.bo.ToaAttendSchedule;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Nov 7, 2009 10:07:43 AM
 * @version  2.0.4
 * @comment
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/attendance/arrange/schedules.action", type = ServletActionRedirectResult.class) })
public class SchedulesAction extends BaseActionSupport {

	private static final long serialVersionUID = 5931092668488897263L;
	private Page<ToaAttendSchedule> page = new Page<ToaAttendSchedule>(FlexTableTag.MAX_ROWS, true);
	private ToaAttendSchedule model = new ToaAttendSchedule();			//班次bo
	private ToaAttendSchedGroup scheGroup = new ToaAttendSchedGroup();	//班组Obo
	private SchedulesManager manager;		//班次manager
	private ScheGroupManager groupManager;	//班组manager
	private String schedulesId;				//班次ID
	private String groupId;					//班组ID
	private final static String YES="0";	//已生效

	public SchedulesAction() {
	}
	
	public String delete() throws Exception {
		StringBuffer returnhtml=new StringBuffer("");
		String msg=manager.deleteSchedules(schedulesId);
		addActionMessage(msg);
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
		returnhtml.append("window.location='")
		.append(getRequest().getContextPath())
		.append("/attendance/arrange/schedules.action?groupId=")
		.append(groupId)
		.append("';</script>");
	    return renderHtml(returnhtml.toString());
	}
	
	public String input() throws Exception {
		  prepareModel();
		  return INPUT;
	}
	
	public String list() throws Exception {
		scheGroup=groupManager.getScheGroupById(groupId);//获取班组对象
		page=manager.getSchedules(page, model,groupId);
		return SUCCESS;
	}
	
	
	protected void prepareModel() throws Exception {
		scheGroup=groupManager.getScheGroupById(groupId);//根据班组ID获取班组对象
		if(schedulesId!=null&&!"".equals(schedulesId)&&!"null".equals(schedulesId)){
	   		model=manager.getSchedulesById(schedulesId);		//根据id获取对象
	   	}else{
	   		model=new ToaAttendSchedule();	
	   		model.setEarlyTime(0);
	   		model.setLaterTime(0);
	   		model.setSkipTime(0);
	   	}  
	}

	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		if("".equals(model.getSchedulesId()))
		   	model.setSchedulesId(null);
		scheGroup=groupManager.getScheGroupById(groupId);//根据班组ID获取班组对象
		model.setToaAttendSchedGroup(scheGroup);	
		String msg=manager.saveSchedules(model);		//保存对象
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
	
	/*
	 * 
	 * Description:查看班次信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 10, 2009 11:29:17 AM
	 */
	public String view()throws Exception{
		scheGroup=groupManager.getScheGroupById(groupId);//根据班组ID获取班组对象
		model=manager.getSchedulesById(schedulesId);//根据id获取对象
		return "view";
	}
	
	/*
	 * 
	 * Description:班次所在班组是否生效
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 10, 2009 11:36:42 AM
	 */
	public String isEffective()throws Exception{
		scheGroup=groupManager.getScheGroupById(groupId);//获取班组对象
		Date now=new Date();
		if(now.after(scheGroup.getGroupStime())){
			renderText(YES);
		}
		return null;
	}

	public String getSchedulesId() {
		return schedulesId;
	}

	public void setSchedulesId(String schedulesId) {
		this.schedulesId = schedulesId;
	}

	public Page<ToaAttendSchedule> getPage() {
		return page;
	}

	@Autowired
	public void setManager(SchedulesManager manager) {
		this.manager = manager;
	}

	public void setModel(ToaAttendSchedule model) {
		this.model = model;
	}

	public ToaAttendSchedule getModel() {
		return model;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Autowired
	public void setGroupManager(ScheGroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public ToaAttendSchedGroup getScheGroup() {
		return scheGroup;
	}

	public void setScheGroup(ToaAttendSchedGroup scheGroup) {
		this.scheGroup = scheGroup;
	}
	
}
