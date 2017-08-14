package com.strongit.oa.attendance.register;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.ToaAttendRecord;
import com.strongit.oa.bo.ToaAttendTemp;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.personnel.baseperson.PersonManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 Nov 16, 2009 11:10:46 AM
 * @version  2.0.4
 * @comment  考勤登记Action
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/attendance/register/register.action", type = ServletActionRedirectResult.class) })
public class RegisterAction extends BaseActionSupport {

	private static final long serialVersionUID = 5931092668488897263L;
	private Page<Object[]> page = new Page<Object[]>(FlexTableTag.MAX_ROWS, true);
	private ToaAttendRecord model = new ToaAttendRecord();			//轮班规则bo
	private RegisterManager manager;				//轮班规则manager
	private IUserService userservice;				//用户接口
	@Autowired private PersonManager perManager;		//人员manager	
	private String attendId;						//轮班规则ID
	private String scheduleId;						//班次ID
	private String personId;						//人员ID
	private String attendType;					    //登记类型（上班或下班）
	private String registerTime;					//客户端登记时间
	private List<ToaAttendTemp> registelist;		
	private String attendDesc;//情况说明
	private Date startDate;
	private Date endDate;
	
	public String getAttendDesc() {
		return attendDesc;
	}

	public void setAttendDesc(String attendDesc) {
		this.attendDesc = attendDesc;
	}

	public RegisterAction() {
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
		User user=userservice.getCurrentUser();
		HttpServletRequest request=this.getRequest();
		registelist=manager.buildRegistList(request,user);
		return SUCCESS;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		
	}

	@Override
	public String save() throws Exception {
		return null;
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 18, 2009 10:03:21 AM
	 * Desc:	登记考勤
	 * param:
	 */
	public String attendRegister() throws Exception{
		HttpServletRequest request=this.getRequest();
		manager.register(attendId,attendType,registerTime);
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
		returnhtml.append("window.location='")
		.append(request.getContextPath())
		.append("/attendance/register/register.action")
		.append("'</script>");
	    return renderHtml(returnhtml.toString());
	}
	
	/**
	 * 情况说明初始化页面
	 * @author 胡丽丽
	 * @date:2009-11-18
	 * @return
	 * @throws Exception
	 */
	public String descRegister()throws Exception{
		model=manager.getAttendRecordById(attendId);
		if(model!=null){
			attendDesc=model.getAttendDesc();
		}
		return "desc";
	}
	
	/**
	 * 保存情况说明
	 * @author 胡丽丽
	 * @date:2009-11-18
	 * @return
	 * @throws Exception
	 */
	public String saveDesc()throws Exception{
		try {
			manager.saveDescByID(attendId,attendDesc);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return renderHtml("<script>window.dialogArguments.submitForm();window.close();</script>");
	}
	
	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 24, 2009 2:24:12 PM
	 * Desc:	上下班记录列表
	 * param:
	 */
	public String viewRegisterRecord()throws Exception{
		try{
			Date date=new Date();
			String dateStr="";
			if(endDate==null){		//如果结束时间为空，设置为当天
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				dateStr=sdf.format(date);
				endDate=sdf.parse(dateStr);
			}
			
			if(startDate==null){	//如果开始时间为空，设置为当月的第一天
				SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
				dateStr=sdf2.format(date);
				String dateStr2=dateStr.substring(0,dateStr.lastIndexOf("-")+1)+"01";
				startDate=sdf2.parse(dateStr2);
			}	
			
			HttpServletRequest request=this.getRequest();
			User user=userservice.getCurrentUser();
			List<Object[]> list=manager.getRegisterRecordList(request,user,startDate,endDate);//组装上下班记录信息列表
			List pagelist=new ArrayList();
			int pagesize=page.getPageSize();
			int pageNo=page.getPageNo();
			Object[] obj;
			if(list!=null&&list.size()>0){	//如果找到上下班记录
				for(int i=(pageNo-1)*pagesize;i<pageNo*pagesize&&i<list.size();i++){
					obj=list.get(i);
					pagelist.add(obj);
				}
				page.setTotalCount(list.size());
			}else{							//没找到上下班记录
				page.setTotalCount(0);
			}
			page.setResult(pagelist);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "records";
	}
	
	/*
	 * 
	 * Description:根据出勤时间获取当天出勤的第一条考勤明细记录
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 23, 2010 11:43:48 AM
	 */
	public String getAttandenceByTime()throws Exception{
		User user=userservice.getCurrentUser();
		ToaBasePerson person=perManager.getPersonByUumsPerId(user.getUserId());//根据统一用户ID获取对应人事的人员ID
		if(person!=null){	
			List<ToaAttendRecord> list=manager.getRegisterRecord(person.getPersonid(), startDate, startDate);
			if(list!=null&&list.size()>0){
				model=list.get(0);
				attendDesc="";
				for(ToaAttendRecord record:list){
					if(record.getAttendDesc()!=null&&!"".equals(record.getAttendDesc()))
						attendDesc+=record.getAttendDesc()+",";
				}
			}
		}
		return "desc";
	}
	
	@Autowired
	public void setManager(RegisterManager manager) {
		this.manager = manager;
	}

	public ToaAttendRecord getModel() {
		return model;
	}

	public void setModel(ToaAttendRecord model) {
		this.model = model;
	}

	public String getAttendId() {
		return attendId;
	}

	public void setAttendId(String attendId) {
		this.attendId = attendId;
	}

	@Autowired
	public void setUserservice(IUserService userservice) {
		this.userservice = userservice;
	}

	public List<ToaAttendTemp> getRegistelist() {
		return registelist;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public void setAttendType(String attendType) {
		this.attendType = attendType;
	}

	public String getPersonId() {
		return personId;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public Page<Object[]> getPage() {
		return page;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
}
