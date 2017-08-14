package com.strongit.oa.attendance.arrange;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.bo.ToaAttendArrange;
import com.strongit.oa.bo.ToaAttendSchedGroup;
import com.strongit.oa.bo.ToaAttendSchedule;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.bo.ToaPersonPrivil;
import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Nov 6, 2009 10:01:11 AM
 * @version  2.0.4
 * @comment
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/attendance/arrange/scheGroup.action", type = ServletActionRedirectResult.class) })
public class ScheGroupAction extends BaseActionSupport {
	
	private static final long serialVersionUID = 5931092668488897263L;
	private Page<ToaAttendSchedGroup> page = new Page<ToaAttendSchedGroup>(FlexTableTag.MIDDLE_ROWS, true);
	private ToaAttendSchedGroup model = new ToaAttendSchedGroup();			//班组bo
	private Map<String,String> groupTypeMap=new HashMap<String,String>();	//存放班组类型的Map
	private Map<String,String> scheduleMap=new HashMap<String,String>();	//存放班次的map
	private ScheGroupManager manager;			//班组manager
	private RegManager regManager;				//轮班规则manager
	@Autowired private PersonManager personManger;
	private String groupId;						//班组ID
	private String orgId;						//机构ID
	private String orgName;						//机构名称
	private String fld_str;						//存放已选人员的人员id字符串
	private String fld_str_pre;					//可选的人员
	private String forward;
	private String perOps;						//对应班组下已排班的人员，人员id|人员姓名，人员id|人员姓名
	private static final String FLAG="0"; 		//班组下存在班次
	private List orgList;						//机构
	private String startSchedules;				//起始班次
	private String userId;
	
	public ScheGroupAction() {
		groupTypeMap.put("0", "行政班");
		groupTypeMap.put("1", "倒班");
	}
	
	public String list() throws Exception {
		page=manager.getScheGroup(page, model);
		return SUCCESS;
	}
	
	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		String flag="";
		if("".equals(model.getGroupId())){
			flag="add";
		   	model.setGroupId(null);
		}
		StringBuffer returnhtml=new StringBuffer("");
		String msg=manager.saveScheGroup(model);		//保存对象
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
		returnhtml.append("window.dialogArguments.submitForm();window.close();");
		if("add".equals(flag)){
			returnhtml.append("window.dialogArguments.parent.schedules.location='")
			.append(getRequest().getContextPath())
			.append("/fileNameRedirectAction.action?toPage=/attendance/arrange/temp.jsp'");
		}
		returnhtml.append("</script>");
	    return renderHtml(returnhtml.toString());
	}
	
	public String delete() throws Exception {
		StringBuffer returnhtml=new StringBuffer("");
		String msg=manager.deleteScheGroup(groupId);	//删除班组
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
		.append("/attendance/arrange/scheGroup.action';window.parent.schedules.location='")
		.append(getRequest().getContextPath())
		.append("/fileNameRedirectAction.action?toPage=/attendance/arrange/temp.jsp'")
		.append("</script>");
	    return renderHtml(returnhtml.toString());
	}
	
	public String input() throws Exception {
		  prepareModel();
		  return INPUT;
	}
	
	protected void prepareModel() throws Exception {
		if(groupId!=null&&!"".equals(groupId)&&!"null".equals(groupId)){//添加
	   		model=manager.getScheGroupById(groupId);		//根据id获取对象
	   	}else{
	   		model=new ToaAttendSchedGroup();		
	   	}  
	}
	
	/*
	 * Description:查看班组信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 10, 2009 12:04:03 PM
	 */
	public String view()throws Exception{
		model=manager.getScheGroupById(groupId);//根据班组ID获取班组对象
		return "view";
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 14, 2009 11:29:21 AM
	 * Desc:	班组下是否存在班次
	 * param:
	 */
	public String isHasSchedules()throws Exception {
		model=manager.getScheGroupById(groupId);		//根据id获取对象
		List<ToaAttendArrange> list=manager.getArrangeListByGroupOrUser(groupId, null, null);
		if(model.getToaAttendSchedules()!=null&&model.getToaAttendSchedules().size()>0){//班组下有班次
			renderText(FLAG);
			return null;
		}
		if(list!=null&&list.size()>0){
			renderText(FLAG+FLAG);
			return null;
		}
		return null;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 14, 2009 11:29:04 AM
	 * Desc:	获取已排班和未排班的人员（人员排班第一套界面方案）
	 * param:
	 */
	public String arrange()throws Exception {
		model=manager.getScheGroupById(groupId);		//根据id获取对象
		HttpServletRequest request=this.getRequest();
		manager.getArrangePerson(request, orgId, groupId);
		return "arrange";
	}
	
	/*
	 * Description:保存人员排班
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 13, 2009 10:40:53 AM
	 */
    public String saveArrange()throws Exception{
    	manager.saveArrange(fld_str, fld_str_pre, groupId);
    	return null;
    }
    
    /*
     * 
     * @author: 彭小青
     * @date: 	Nov 14, 2009 11:27:18 AM
     * Desc:	获取该班组下的已排班的人员（人员排班第二套界面方案）
     * param:
     */
    public String arrange1()throws Exception {
		model=manager.getScheGroupById(groupId);		//根据id获取对象
		Set scheSet=model.getToaAttendSchedules();
		//获取班组下的班次，并存放在map里
		ToaAttendSchedule sche;
		Iterator it = scheSet.iterator(); 
	    while (it.hasNext()) { 
	    	sche= (ToaAttendSchedule) it.next(); 
	    	scheduleMap.put(sche.getSchedulesId(), sche.getSchedulesName());
	    } 
		perOps=manager.getArrangePerByGroup(groupId,startSchedules);	//获取该班组下的已排班的人员
		return "selectperson";
	}
    
    /*
     * @author: 彭小青
     * @date: 	Nov 14, 2009 11:26:41 AM
     * Desc:	保存人员排班(第二套方案)
     * param:
     */
    public String saveArrange1()throws Exception{
    	manager.saveArrange1(perOps, groupId,startSchedules);
    	return null;
    }
	
    /*
     * @author: 彭小青
     * @date: 	Nov 14, 2009 11:27:00 AM
     * Desc:	具体树形展现页面分派
     * param:
     */
	public String chooseTree() throws Exception{
	   if("per".equals(forward)){
	    	orgList = manager.getAllOrgUserList(orgId);
	    	return "per";
	    }else{
	    	orgList=manager.getAllOrgList();
	    	return "org";
	    }
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 20, 2009 11:17:13 AM
	 * Desc:	判断是否可以排班
	 * param:
	 */
	public String isCanArrange()throws Exception{
		String flag=regManager.isHasSetAllRegOfGroup(groupId);
		renderText(flag);
		return null;
	}
	
   /*
    * 
    * Description:获取用户能查看到的人员列表
    * param: 
    * @author 	    彭小青
    * @date 	    Apr 14, 2010 5:30:24 PM
    */
    public String getPersonPrivil()throws Exception {
    	ToaPersonPrivil privil=manager.getPersonPrivil(userId);		//根据id获取对象
    	ToaBasePerson person;
    	if(privil!=null&&privil.getId()!=null){
    		byte[] personIds = privil.getPersonIds();
    		if(personIds!=null&&personIds.length>0){
    			String tempContent = new String(personIds,0,personIds.length);
    			tempContent=HtmlUtils.htmlEscape(tempContent);
    			List<ToaBasePerson> list=personManger.getPersonsByIds(tempContent);
    			if(list!=null&&list.size()>0){
    				perOps="";
    				for(int i=0;i<list.size();i++){
    					person=list.get(i);
    					perOps+=","+person.getPersonid()+"|"+person.getPersonName();
    				}
    				perOps=perOps.substring(1);
    			}
    		}
    	}
    	
		return "selectperson";
	}
    
    
    /*
     * 
     * Description:保存设置
     * param: 
     * @author 	    彭小青
     * @date 	    Apr 14, 2010 5:39:18 PM
     */
    public String savePersonPrivil()throws Exception {
    	ToaPersonPrivil privil=manager.getPersonPrivil(userId);		//根据id获取对象
    	if(privil==null||privil.getId()==null){
    		privil=new ToaPersonPrivil();
    		privil.setUserId(userId);
    	}
    	if(perOps!=null){
    		privil.setPersonIds(perOps.getBytes());
    	}
    	this.renderText(manager.savePersonPrivil(privil));
    	return null;
    }
    
	
	public ToaAttendSchedGroup getModel() {
		return model;
	}

	public void setModel(ToaAttendSchedGroup model) {
		this.model = model;
	}

	@Autowired
	public void setManager(ScheGroupManager manager) {
		this.manager = manager;
	}

	public Page<ToaAttendSchedGroup> getPage() {
		return page;
	}

	public Map<String, String> getGroupTypeMap() {
		return groupTypeMap;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setFld_str(String fld_str) {
		this.fld_str = fld_str;
	}

	public void setFld_str_pre(String fld_str_pre) {
		this.fld_str_pre = fld_str_pre;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public List getOrgList() {
		return orgList;
	}

	public String getPerOps() {
		return perOps;
	}

	public void setPerOps(String perOps) {
		this.perOps = perOps;
	}

	public String getStartSchedules() {
		return startSchedules;
	}

	public void setStartSchedules(String startSchedules) {
		this.startSchedules = startSchedules;
	}

	public Map<String, String> getScheduleMap() {
		return scheduleMap;
	}

	@Autowired
	public void setRegManger(RegManager regManager) {
		this.regManager = regManager;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
