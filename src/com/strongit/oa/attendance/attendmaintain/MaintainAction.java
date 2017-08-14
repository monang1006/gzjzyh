package com.strongit.oa.attendance.attendmaintain;

import java.util.Date;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaAttendRecord;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Nov 25, 2009 10:20:11 AM
 * @version  2.0.4
 * @comment  考勤维护Action
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/attendance/attendmaintain/maintain.action", type = ServletActionRedirectResult.class) })
public class MaintainAction extends BaseActionSupport {

	private final static String flag="1";
	private Page<ToaAttendRecord> page=new Page<ToaAttendRecord>(FlexTableTag.MAX_ROWS,true);
	private ToaAttendRecord model=new ToaAttendRecord();	//考勤明细BO
	private MaintainManager manager;						//考勤维护manager
	@Autowired private PersonManager personManager;
	private String attendId;			//考勤明细ID
	private String orgId;				//部门ID
	private List orgList;				//机构
	private Date startTime;				//计算开始时间
	private Date endTime;				//计算结束时间
	private String personIds;			//人员ID
	
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
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 25, 2009 4:07:40 PM
	 * Desc:	计算考勤
	 * param:
	 */
	public String cal() throws Exception {
		List<ToaBasePerson> userList=personManager.getPersonsByIds(personIds);
		String msg=manager.calAttend(startTime, endTime, userList,flag);
		addActionMessage(msg);
		StringBuffer returnhtml = new StringBuffer(
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
			returnhtml.append("alert('").append(msg).append("');");
		}
		returnhtml.append("window.close();window.dialogArguments.submitForm();").append("</script>");    
		return renderHtml(returnhtml.toString());
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	public Page<ToaAttendRecord> getPage() {
		return page;
	}
	
	public ToaAttendRecord getModel() {
		return model;
	}

	public String getAttendId() {
		return attendId;
	}

	public void setAttendId(String attendId) {
		this.attendId = attendId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Autowired
	public void setManager(MaintainManager manager) {
		this.manager = manager;
	}
	
	public List getOrgList() {
		return orgList;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setPersonIds(String personIds) {
		this.personIds = personIds;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


}
