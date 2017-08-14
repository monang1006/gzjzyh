package com.strongit.oa.traceDoc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONArray;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.jbpm.graph.exe.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.component.formtemplate.util.FormGridDataHelper;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;


/**
 * 
 * @company      Strongit Ltd. (C) copyright
 * @date         May 9, 2012 9:22:39 AM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.traceDoc.TraceDocAction
 */
@ParentPackage("default")
@Results( { 
			@Result(name = BaseActionSupport.RELOAD, value = "traceDoc.action", type = ServletActionRedirectResult.class)
		  })
public class TraceDocAction extends AbstractBaseWorkflowAction {
	
	private static final long serialVersionUID = 4103263551309775221L;

	@Autowired TraceDocManager tracmanager;
	private Page<Object[]> page = new Page<Object[]>(FlexTableTag.MAX_ROWS,
			true);
	
	private String processId;
	
	private String userId;  //用户ID /traceDoc/traceDoc* /traceDoc/traceDoc* 重要文件跟踪 个人桌面使用
	
	private String proName;
	
@Autowired @Qualifier("baseManager") BaseManager basemanager; 
   @Autowired  IUserService userService ;//统一用户服务
	
	@Autowired
	protected IWorkflowService workflow; // 工作流服务
	//查询条件
	private String processName;   //流程名
	private String startUserName; //发起人
	private Date searchDate; 	  //启动时间
	private String searchDateString;
	private String timeout;		//流程是否超期
	private String processDefinitionNames="";//流程名称
	private Map<String,String> processDefinitionNameMapIds;
	private List annalList;		//流程处理意见
	private String day;			//任务停留天数
	
	private String isFind = "0"; 	//"1":表示正在执行查询操作,"0"或其他:表示当前不是进行查询操作


	///////////////////////////////
	private String traceProcessTitle;
	

	public String getTraceProcessTitle() {
		return traceProcessTitle;
	}

	public void setTraceProcessTitle(String traceProcessTitle) {
		this.traceProcessTitle = traceProcessTitle;
	}

	///////////////////////////////////////
	public String getIsFind() {
		return isFind;
	}

	public void setIsFind(String isFind) {
		this.isFind = isFind;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	
	
	@SuppressWarnings("unchecked")
	public String viewProcessed() throws Exception{
		try {
			if(taskId != null){
				String[] strBusinessId = getManager().getFormIdAndBussinessIdByTaskId(taskId);
				formId = strBusinessId[1];
				if(!"0".equals(formId)){
					String[] strBussinessId = strBusinessId[0].split(";");
					tableName = strBussinessId[0];
					pkFieldName = strBussinessId[1];
					pkFieldValue = strBussinessId[2];			
				}
				instanceId = adapterBaseWorkflowManager.getWorkflowService().getProcessInstanceIdByTiId(taskId);
				bussinessId = tableName+";"+pkFieldName+";"+pkFieldValue;
			}else{
				if(instanceId != null){
					ProcessInstance processinstance = adapterBaseWorkflowManager.getWorkflowService().getProcessInstanceById(instanceId);
					bussinessId = processinstance.getBusinessId();
					String[] strBussinessId = bussinessId.split(";");
					tableName = strBussinessId[0];
					pkFieldName = strBussinessId[1];
					pkFieldValue = strBussinessId[2];
					formId = processinstance.getMainFormId();
				}
			}
//			ContextInstance cxt = adapterBaseWorkflowManager.getWorkflowService().getContextInstance(instanceId);
			JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
			if(jsonArray != null && !jsonArray.isEmpty()){
				personDemo = jsonArray.toString();
				parentInstanceId = "exists";
			}else{
				ProcessInstance processinstance = adapterBaseWorkflowManager.getWorkflowService().getProcessInstanceById(instanceId);
				String demo = (String) processinstance.getContextInstance().getVariable("@{personDemo}");
				String parentinstanceId="";
				if(!"".equals(demo)&&null!=demo){
					String[]  demos = demo.split(";");
					if(demos.length>4){
						parentinstanceId = demos[4];
						parentinstanceId = parentinstanceId.split("@")[0];
						
						jsonArray = getParentInstanceIdLevelInfo(parentinstanceId);
						personDemo = jsonArray.toString();
						parentInstanceId = "exists";
					}					
				}
			}
			Object end = adapterBaseWorkflowManager.getWorkflowService().getProcessInstanceById(instanceId).getEnd();
			if(end == null){
				state = "0";
			}else{
				state = "1";
			}
//			Object tempObject = cxt.getVariable(WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);//获取父流程实例id
//			if(tempObject != null && !tempObject.toString().equals("")){
//				parentInstanceId = tempObject.toString();	
//				ContextInstance parentCxt = adapterBaseWorkflowManager.getWorkflowService().getContextInstance(parentInstanceId);//获取父流程上下文
//				personDemo = (String)parentCxt.getVariable("@{personDemo}");
//			}
		} catch (Exception e) {
			logger.error("查看主办流程时发生错误", e);
		}
		return "processedview";
	}
	
	
	@Override
	public String delete() throws Exception {
		try {
			String ids = getRequest().getParameter("ids");
			String[] insid=ids.split(",");
			for(int i=0;i<insid.length;i++){ 
				if(insid[i].indexOf("$")>-1){
					int num = insid[i].indexOf("$");
					String result = insid[i].substring(0,num);
					tracmanager.deleteToaTraceDocs(result,userService.getCurrentUser().getUserId());
				}else{
					tracmanager.deleteToaTraceDocs(insid[i],userService.getCurrentUser().getUserId());
				}
				
			}
		
			//addActionMessage("信息删除成功");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			addActionMessage(e.getMessage());
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return list();
	}
	
	@SuppressWarnings("unchecked")
	public String showDesktopDoing() throws Exception {
		//获取blockId  showDesktopDoing
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum"); 
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate"); 
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if(blockId != null) {
			Map<String, String> map = basemanager.getDesktopParam(blockId);
			showNum = map.get("showNum");//显示条数
			subLength = map.get("subLength");//主题长度
			showCreator = map.get("showCreator");//是否显示作者
			showDate = map.get("showDate");//是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}
		
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}

		//获取在办跟踪公文
		Page pageTodo = new Page(num, false);
	//	pageTodo=tracmanager.getTodoTracesByUserName(pageTodo);
	     List dolist=tracmanager.getDoingTracesByUserName(pageTodo).getResult();
//	     if(dolist!=null){
//				for(int i=0;i<dolist.size();i++){
//					for(int j=i+1;j<dolist.size();j++){
//						Object[] obj=(Object[]) dolist.get(i);
//						Object[] objs=(Object[]) dolist.get(j);
//						if(obj[4].equals(objs[4])){ 
//							dolist.remove(j); 
//							j--;
//						} 
//					}
//				}
//			ListUtils.splitList2Page(pageTodo, dolist);
//			}
		//	return lst;
	   //  pageTodo.setResult(dolist);
	     
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		if(dolist!=null){
			innerHtml.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for(Iterator iterator = dolist.iterator(); iterator.hasNext();){
				Object[] object = (Object[])iterator.next();
				StringBuilder strUserName = new StringBuilder("");//人员姓名	
				
				/**
				 * 根据流程实例id得到所有子流程实例id列表
				 * @param instanceId 当前流程实例id
				 */
				List inslist=adapterBaseWorkflowManager.getWorkflowService().getMonitorChildrenInstanceIds(new Long(object[4].toString()));
				if(inslist!=null && inslist.size()>0){
				   for(Iterator iter = inslist.iterator(); iter.hasNext();){
					Object[] obje = (Object[])iter.next();
					List xob=tracmanager.getDoingTracesById(obje[0].toString());
					
			        Object[] rbjs = adapterBaseWorkflowManager.getWorkflowService().getProcessStatusByPiId(obje[0].toString());//得到此流程实例下的运行情况
			        Collection cols = (Collection)rbjs[6];//处理任务信息
			        if(cols != null && !cols.isEmpty()) {
						for(Iterator it = cols.iterator();it.hasNext();) {
							Object[] itbjs = (Object[])it.next();
							if(xob!=null && xob.size()>0){
						       for(Iterator ite =xob.iterator(); ite.hasNext();){
							      Object[] ob = (Object[])ite.next();
							      if(itbjs[1].toString().equals(ob[3].toString())){
										String useId = (String)itbjs[3];
										String userN = new String();
										String orgN = new String();
										if(useId != null && !"".equals(useId)) {
											String[] useIds = useId.split(",");
											for(String id : useIds) {
												userN=userService.getUserNameByUserId(id);
												orgN=(userService.getUserDepartmentByUserId(id).getOrgName());
												if(userN!=null && orgN!=null)
												strUserName.append(userN).append("(").append(orgN.toString()).append(")");
//												String isr=String.valueOf(ob[8]);
												String isread=new String();
											//	if(isr.equals("null") || isr.equals("0")){
													isread="待办";
											//	}else if(isr.equals("1")){
											//		isread="已阅";
											//	}
												strUserName.append("&nbsp;&nbsp;").append(isread);
												strUserName.append(",");
											}
											if(strUserName.length()>0){
											  strUserName.deleteCharAt(strUserName.length() - 1);
											}
										}
							      }
							 
						     }
							}
						     else{
						    	  String useId = (String)itbjs[3];
									String userN = new String();
									String orgN = new String();
									if(useId != null && !"".equals(useId)) {
										String[] useIds = useId.split(",");
										for(String id : useIds) {
											userN=userService.getUserNameByUserId(id);
											orgN=(userService.getUserDepartmentByUserId(id).getOrgName());
											if(userN!=null && orgN!=null)
											strUserName.append(userN).append("(").append(orgN.toString()).append(")");
//											String isr=String.valueOf(object[8]);
											String isread=new String();
											//if(isr.equals("null") || isr.equals("0")){
												isread="待办";
											//}else if(isr.equals("1")){
											//	isread="已阅";
											//}
											strUserName.append("&nbsp;").append(isread);
											strUserName.append(",");
										}
										if(strUserName.length()>0){
										  strUserName.deleteCharAt(strUserName.length() - 1);
										}
									}
						      }
				
						}
						}
				}
				   
				}else{
				Object[] returnObjs = adapterBaseWorkflowManager.getWorkflowService().getProcessStatusByPiId(object[4].toString());//得到此流程实例下的运行情况
				Collection col = (Collection)returnObjs[6];//处理任务信息
			
				if(col != null && !col.isEmpty()) {
					for(Iterator it = col.iterator();it.hasNext();) {
						Object[] itObjs = (Object[])it.next();
						String userId = (String)itObjs[3];
		
						String userN = new String();
						String orgN = new String();
						if(userId != null && !"".equals(userId)) {
							String[] userIds = userId.split(",");
							for(String id : userIds) {
								userN=userService.getUserNameByUserId(id);
								orgN=(userService.getUserDepartmentByUserId(id).getOrgName());
								if(userN!=null && orgN!=null)
								strUserName.append(userN).append("(").append(orgN.toString()).append(")");
//								String isr=String.valueOf(object[8]);
								String isread=new String();
								//if(isr.equals("null") || isr.equals("0")){
									isread="待办";
								//}else if(isr.equals("1")){
								//	isread="已阅";
								//}
								strUserName.append("&nbsp;&nbsp;").append(isread);
								strUserName.append(",");
							}
							if(strUserName.length()>0){
							  strUserName.deleteCharAt(strUserName.length() - 1);
							}
						}
						
					}
				}
				
				}		
				
				innerHtml.append("<table width=\"100%\" style=\"font-size:"+sectionFontSize+"px\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				//图标
				innerHtml.append("<img src=\"").append(rootPath).
				append("/oa/image/desktop/littlegif/news_bullet.gif").append("\"/> ");
				
				//如果显示的内容长度大于设置的主题长度，则过滤该长度
				String title = object[5]==null?"":object[5].toString();
				if(title.length()>length && length > 0) {
					title = title.substring(0,length)+"...";
				}
				
				
				StringBuilder clickTitle = new StringBuilder();
				
				clickTitle.append("var Width=screen.availWidth-10;var Height=screen.availHeight-30;");
				clickTitle
				.append("var a = window.open('")
				.append(getRequest().getContextPath())
				.append("/traceDoc/traceDoc!viewProcessed.action?taskId=")
				.append(object[0])
				.append("&instanceId=")
				.append(object[4])
				.append(
						"','processed','height='+Height+', width='+Width+', top=0, left=0, toolbar=no,menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');")

		
				.append("if(a && a == 'OK'){window.location.reload();}");
		
				innerHtml.append("<span title=\"").append(object[5]).append("("+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(object[1])+")\">")			
				.append("<a href=\"#\" onclick=\"").append(clickTitle.toString()).append("\">").append(title).append("</a></span>");
				
				//innerHtml.append("<span style='position: relative;left:50px;'>&nbsp;&nbsp;&nbsp;").append("</span>");
			
				innerHtml.append("&nbsp;&nbsp;<span class=\"linkgray\" style='font-size:"+sectionFontSize+"px;position: relative;color: red'>").append(strUserName).append("</span>");
				innerHtml.append("</td>");
				//显示发起人信息5 //2011-11-7时间16:12分何城君为了配合省政府办公厅 将width 250分别修改成40%,45%,,left=15%修改为5%,
				if("1".equals(showCreator)) {
					innerHtml.append("<td width=\"80px\">");
					if(object[7]==null){
						object[7]= basemanager.getFormIdAndBussinessIdByTaskId(object[0].toString())[0];
					}
					bussinessId = String.valueOf(object[7]);
					if(!"0".equals(bussinessId)) {
						String departmentName=null;
						String[] args = bussinessId.split(";");
						
						tableName =String.valueOf(args[0]);
						pkFieldName = String.valueOf(args[1]);
						pkFieldValue = String.valueOf(args[2]);	
						Map map = basemanager.getSystemField(pkFieldName, pkFieldValue, tableName);
						userId = (String)map.get(BaseWorkflowManager.WORKFLOW_AUTHOR);
						if(userId != null) {
							userName = userService.getUserNameByUserId(userId);
							Organization department = userService.getUserDepartmentByUserId(userId);
							departmentName = department.getOrgName();
							//包含部门
							String showName = userName+"["+departmentName+"]";
							//不包含部门
							String showTitle = userName;
							String realShowName = showName ;
							if(showName.length()>8) {
								showName = showName.substring(0,8)+"...";
							}
							if(showTitle.length()>3) {
								showTitle = showTitle.substring(0,3)+"...";
							}
							innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px\" title =\""+realShowName+"\" class =\"linkgray\">")
							.append(showTitle).append("</span>");
							
						}
					}
					innerHtml.append("</td>");
				}
				//显示日期信息
				if("1".equals(showDate)) {
					innerHtml.append("<td width=\"100px\">");
					SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
					innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px\" title =\""+new SimpleDateFormat("yyyy-MM-dd hh:mm").format(object[1])+"\" class =\"linkgray10\">")
					.append(st.format(object[1])).append("</span>");
					innerHtml.append("	</td>");
				}
				
				innerHtml.append("	</tr>");
				innerHtml.append("	</table>");
			}
			
		}else {
			//innerHtml.append("<div class=\"linkdiv\" color=\"red\">没有在办文件！</div>");
		}
		
//		【更多】跳转连接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath());
		link.append("/traceDoc/traceDoc!doingDocList.action")
		.append("', '在办文件跟踪'")
		.append(");");		
		innerHtml.append("<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"").append(link.toString()).append("\"> ")
				 .append("<IMG SRC=\"").append(rootPath).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
	
		
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示	
	
		}	
	@SuppressWarnings("unchecked")
	public String showDesktop() throws Exception {
		//获取blockId  showDesktopDoing
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum"); 
		String showCreator = getRequest().getParameter("showCreator"); 
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if(blockId != null) {
			Map<String, String> map = basemanager.getDesktopParam(blockId);
			showNum = map.get("showNum");//显示条数
			subLength = map.get("subLength");//主题长度
			showCreator = map.get("showCreator");//是否显示作者
			showDate = map.get("showDate");//是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}
		
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		//获取重要跟踪公文
		Page<Object[]> pageTo = new Page<Object[]>(num, false);
		pageTo=tracmanager.getTodoTraces(pageTo);
//		Page pageTodo = new Page(num, false);
//		List dolist=pageTo.getResult();
//		if(dolist!=null){
//			for(int i=0;i<dolist.size();i++){
//				for(int j=i+1;j<dolist.size();j++){
//					Object[] obj=(Object[]) dolist.get(i);
//					Object[] objs=(Object[]) dolist.get(j);
//					if(obj[4].equals(objs[4])){ 
//						dolist.remove(j); 
//						j--;
//					} 
//				}
//			}
//		 ListUtils.splitList2Page(pageTodo, dolist);
//		}
		
		
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		List dolist=pageTo.getResult();
		if(dolist != null && dolist.size() != 0){
			innerHtml.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for(Iterator iterator = dolist.iterator(); iterator.hasNext();){
				Object[] object = (Object[])iterator.next();			
				innerHtml.append("<div title=\"\">");
				innerHtml.append("<table width=\"100%\" style=\"font-size:"+sectionFontSize+"px\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				//图标
				innerHtml.append("<img src=\"").append(rootPath).
				append("/oa/image/desktop/littlegif/news_bullet.gif").append("\"/> ");
				
				//如果显示的内容长度大于设置的主题长度，则过滤该长度
				String title = object[3]==null?"":object[3].toString();
				if(title.length()>length && length > 0) {
					title = title.substring(0,length)+"...";
				}
				
				if(object[2] != null){
					object[2] = object[2].toString().split("\\$")[0];
				}
				StringBuilder clickTitle = new StringBuilder();
				
				clickTitle.append("var Width=screen.availWidth-10;var Height=screen.availHeight-30;");
				
				clickTitle
				.append("var a = window.open('")
				.append(getRequest().getContextPath())
				.append("/traceDoc/traceDoc!viewProcessed.action?")
				.append("&instanceId=")
				.append(object[2])
				.append(
						"','processed','height='+Height+', width='+Width+', top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');")

		
				.append("if(a && a == 'OK'){window.location.reload();}");
		
				innerHtml.append("<span title=\"").append(object[3]).append("("+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(object[7])+")\">")			
				.append("<a style=\"font-size:"+sectionFontSize+"px\" href=\"#\" onclick=\"").append(clickTitle.toString()).append("\">").append(title).append("</a></span>");
		
				
				/**
				clickTitle.append("var a = window.open('").append(getRequest().getContextPath())
					.append("/senddoc/sendDoc!CASign.action?taskId=").append(object[0]).append("&instanceId=").append(object[4])
					.append("&workflowName=").append(URLEncoder.encode(URLEncoder.encode(object[3].toString(), "utf-8"),"utf-8"))
					.append("','senddoc','height='+Height+', width='+Width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');");
				
				innerHtml.append("<span title=\"").append(object[5]).append("("+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(object[1])+")\">")			
					.append("<a href=\"#\" onclick=\"").append(clickTitle.toString()).append("\">").append(title).append("</a></span>");
			**/
				innerHtml.append("</td>");
				
				//显示发起人信息
				if("1".equals(showCreator)) {
					innerHtml.append("<td width=\"80px\">");
					if(object[5]==null){
						bussinessId = "0";
//						object[7]= basemanager.getFormIdAndBussinessIdByTaskId(object[0].toString())[0];
					}
					bussinessId = String.valueOf(object[5]);
					if(!"0".equals(bussinessId)) {
						String departmentName=null;
						String[] args = bussinessId.split(";");
						
						tableName =String.valueOf(args[0]);
						pkFieldName = String.valueOf(args[1]);
						pkFieldValue = String.valueOf(args[2]);	
						Map map = basemanager.getSystemField(pkFieldName, pkFieldValue, tableName);
						userId = (String)map.get(BaseWorkflowManager.WORKFLOW_AUTHOR);
						if(userId != null) {
							userName = userService.getUserNameByUserId(userId);
							Organization department = userService.getUserDepartmentByUserId(userId);
							departmentName = department.getOrgName();
							//包含部门
							String showName = userName+"("+departmentName+")";
							String realShowName = showName ;
							//不包含部门
							String showTitle = userName;
							if(showName.length()>8) {
								showName = showName.substring(0,8)+"...";
							}
							
							if(showTitle.length()>3) {
								showTitle = showTitle.substring(0,3)+"...";
							}
							
							innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px\" title =\""+realShowName+"\" class =\"linkgray\">")
							.append(showTitle).append("</span>");
							
						}
					}
					innerHtml.append("</td>");
				}
				
				
				//显示日期信息
				if("1".equals(showDate)) {
					innerHtml.append("<td width=\"100px\">");
					SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
					innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px\" title =\""+new SimpleDateFormat("yyyy-MM-dd hh:mm").format(object[7])+"\" class =\"linkgray10\">")
					.append(st.format(object[7])).append("</span>");
					innerHtml.append("	</td>");
				}
				
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
			
		}else {
			//innerHtml.append("<div class=\"linkdiv\" color=\"red\">没有重要文件！</div>");
		}
		
//		【更多】跳转连接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath());
		link.append("/traceDoc/traceDoc.action")
		.append("', '重要文件'")
		.append(");");		
		innerHtml.append("<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"").append(link.toString()).append("\"> ")
				 .append("<IMG SRC=\"").append(rootPath).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
	
		
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}
	
	@SuppressWarnings("unchecked")
	public String showTableDesktop() throws Exception {
		//获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum"); 
		String showCreator = getRequest().getParameter("showCreator"); 
//		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if(blockId != null) {
			Map<String, String> map = basemanager.getDesktopParam(blockId);
			showNum = map.get("showNum");//显示条数
			subLength = map.get("subLength");//主题长度
			showCreator = map.get("showCreator");//是否显示作者
//			showDate = map.get("showDate");//是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}
		
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		//获取重要跟踪公文
		Page<Object[]> pageTo = new Page<Object[]>(num, false);
		pageTo=tracmanager.getTodoTraces(pageTo);
		//Page pageTodo = new Page(num, false);
		//List dolist=pageTo.getResult();
		/**
		if(dolist!=null){
			for(int i=0;i<dolist.size();i++){
				for(int j=i+1;j<dolist.size();j++){
					Object[] obj=(Object[]) dolist.get(i);
					Object[] objs=(Object[]) dolist.get(j);
					if(obj[4].equals(objs[4])){ 
						dolist.remove(j); 
						j--;
					} 
				}
			}
		 ListUtils.splitList2Page(pageTodo, dolist);
		}
		*/
		//ListUtils.splitList2Page(pageTodo, dolist);		
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		List dolist = pageTo.getResult();
		if(dolist!=null && dolist.size() != 0){
			innerHtml.append("<table width=\"100%\" style=\"font-size:"+sectionFontSize+"px\" cellpadding=\"0\" cellspacing=\"0\">");
			innerHtml.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for(Iterator iterator =dolist.iterator(); iterator.hasNext();){
				Object[] object = (Object[])iterator.next();			
				innerHtml.append(" <tr height=\"25px\">");
				innerHtml.append("<td width=\"65%\">");
				//图标
				innerHtml.append("<img src=\"").append(rootPath).
				append("/oa/image/desktop/littlegif/news_bullet.gif").append("\"/> ");
				
				//如果显示的内容长度大于设置的主题长度，则过滤该长度
				String title = object[3]==null?"":object[3].toString();
				if(title.length()>length && length > 0) {
					title = title.substring(0,length)+"...";
				}
				if ("".equals(title)) {
					title = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				if(object[2] != null){
					object[2] = object[2].toString().split("\\$")[0];
				}
				StringBuilder clickTitle = new StringBuilder();
				
				clickTitle.append("var Width=screen.availWidth-10;var Height=screen.availHeight-30;");
				
				clickTitle
				.append("var a = window.open('")
				.append(getRequest().getContextPath())
				.append("/traceDoc/traceDoc!viewProcessed.action?")
				.append("&instanceId=")
				.append(object[2])
				.append("','processed','height='+Height+', width='+Width+', top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');")
				.append("if(a && a == 'OK'){window.location.reload();}");

				
				innerHtml.append("<span>");
				//通过流程名称获取 流程别名
				String pfNameRest2 = tracmanager.getProcessfileFileByPfName((String)object[1]);
				StringBuilder clickProcessType = new StringBuilder();
				clickProcessType.append(tracmanager
					.genProcessedWorkflowNameLink((String)object[3],(String)object[9], (String)object[8],rootPath));
				//存在流程别名，显示流程别名 ，不存在则显示流程类型名称
				if(pfNameRest2!=null && !"".equals(pfNameRest2)){
					innerHtml.append("<a href=\"#\" onclick=\"")
					.append(clickProcessType.toString())
					.append("\">[")
					.append(pfNameRest2).append(
							"]</a>");
				}else{
					innerHtml.append("<a href=\"#\" onclick=\"")
					.append(clickProcessType.toString())
					.append("\">[")
					.append((String)object[1]).append(
							"]</a>");
				}
				
				innerHtml.append("<a href=\"#\" onclick=\"").append(clickTitle.toString()).append("\" title=\"").append(object[3]==null?"":object[3]).append("\n").append("开始时间：").append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(object[7])+"\">").append(title).append("</a></span>");

				innerHtml.append("</td>");
				
				Properties properties = FormGridDataHelper.getColorSetProperties();

				
				String urgePicPath = properties.getProperty("urge");
				String viewPicPath = properties.getProperty("view");
				
				innerHtml.append("<td width=\"20%\">");
				innerHtml.append("<span>");
				//催办按钮
				innerHtml.append("<a href=\"#\" onclick=\"").append("urge('").append(object[2]).append("')\">");
				innerHtml
				.append("<img src=")
				.append(getRequest().getContextPath())
				.append(urgePicPath)
				.append(" title="
									+ "催办流程"
									+ " style=vertical-align:middle;/>");
				innerHtml.append("</a>&nbsp;");
				//查看办理记录按钮
				innerHtml.append("<a href=\"#\" onclick=\"").append("view('").append(object[2] + "','" + "").append("')\">");
				innerHtml
				.append("<img src=")
				.append(getRequest().getContextPath())
				.append(viewPicPath)
				.append(" title="
									+ "查看办理记录"
									+ " style=vertical-align:middle;/>");
				innerHtml.append("</a>");
				
				innerHtml.append("</span>");
				
				innerHtml.append("</td>");
				
				//显示当前处理人信息
				String CurTaskActorInfo = "";
				String TaskActor = "";
				StringBuffer tip = null;
				if("1".equals(showCreator)) {
					innerHtml.append("<td align=\"left\">");
					String processInstanceId = object[2]==null?"":object[2].toString();
					if(processInstanceId != null && !"".equals(processInstanceId)){
						String[] insIds = processInstanceId.split("\\$");
						CurTaskActorInfo = workflow.getCurrentTaskHandle(insIds[0]).getActor();
						String[] CurTaskActorInfos = CurTaskActorInfo.split(",");
						tip = new StringBuffer();
						Map<String, String> temp = null;
						if (CurTaskActorInfos.length > 0) {
							temp = new LinkedHashMap<String, String>();
							for (String info : CurTaskActorInfos) {
								if("".equals(info)){
									continue;
								}
								String infoKey = info.substring(info.indexOf("("), info
										.length());// 部门信息
								String infoValue = info.substring(0, info.indexOf("("));
								if (!temp.containsKey(infoKey)) {
									temp.put(infoKey, infoValue);
								} else {
									temp.put(infoKey, temp.get(infoKey) + "," + infoValue);
								}
							}
							CurTaskActorInfo = "";
							for (String infoKey : new ArrayList<String>(temp.keySet())) {
								if("".equals(infoKey)){
									continue;
								}
								CurTaskActorInfo += temp.get(infoKey)
										+ infoKey + ",";
								TaskActor += temp.get(infoKey)
										+ ",";

							}
							if(!"".equals(CurTaskActorInfo)){
								CurTaskActorInfo = CurTaskActorInfo.substring(0,
										CurTaskActorInfo.length() - 1);
								TaskActor = TaskActor.substring(0,
										TaskActor.length() - 1);
							}

						}
						tip.append(temp == null ? CurTaskActorInfo : temp
								.containsKey(CurTaskActorInfo) ? temp.get(CurTaskActorInfo)
								: CurTaskActorInfo);
					}
					if(TaskActor.length()>4){
						
						TaskActor = TaskActor.subSequence(0, 4)+"...";
					}
					innerHtml.append(
							"<span class =\"linkgray\" title=\"" + tip + "\" >")
							.append(TaskActor).append("</span>");
					
					innerHtml.append("</td>");

				}

				
				/**
				//显示日期信息
				if("1".equals(showDate)) {
					innerHtml.append("<td width=\"100px\">");
					SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					innerHtml.append("<span class =\"linkgray10\">").append(" ("+st.format(object[1])+")").append("</span>");
					innerHtml.append("	</td>");
				}
				*/
				
				innerHtml.append("</tr>");				
			}
			innerHtml.append("</table>");
			
		}else {
			//innerHtml.append("<div class=\"linkdiv\" color=\"red\">没有重要文件！</div>");
		}
		
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String list() throws Exception {
		//标题,发起人，开始时间,流程名称
		if((traceProcessTitle!=null&&!"".equals(traceProcessTitle)) || (startUserName!=null&&!"".equals(startUserName)) || searchDate!=null){      
			page=tracmanager.getTodoTraces(page,traceProcessTitle,startUserName,searchDate);
			if(page.getTotalCount() < 0){
			    page.setTotalCount(0);
			}
		}else{
			page=tracmanager.getTodoTraces(page);
			if(page.getTotalCount() < 0){
			    page.setTotalCount(0);
			}
		}
		
     
		return SUCCESS;
	}
	
	
	/**
	 *  根据流程类型id 获取相对应的重要文件跟踪列表
	 * 
	 * @description
	 * @author 
	 * @createTime 2014年2月15日14:24:06
	 * @return StringBuilder
	 */
	public String workfolwTypelist() throws Exception {
		   getRequest().setAttribute("workflowType", workflowType);
		   
			page=tracmanager.getTodoTracesBy(page,traceProcessTitle,startUserName,searchDate,workflowType);
			if(page.getTotalCount() < 0){
			    page.setTotalCount(0);
			}
			
		return "workflowlist";
	}
	
	

	
	public String doingDocList() throws Exception {
		
//		获取在办跟踪公文
	
	     List dolist=tracmanager.getDoingTracesByUserName(page).getResult();
	     if(dolist!=null){
//				for(int i=0;i<dolist.size();i++){
//					for(int j=i+1;j<dolist.size();j++){
//						Object[] obj=(Object[]) dolist.get(i);
//						Object[] objs=(Object[]) dolist.get(j);
//						if(obj[4].equals(objs[4])){ 
//							dolist.remove(j); 
//							j--;
//						} 
//					}
//				}
			 
			
			
	     for(Iterator iterator =dolist.iterator(); iterator.hasNext();){
				Object[] object = (Object[])iterator.next();
				StringBuilder strUserName = new StringBuilder("");//人员姓名	
				
				/**
				 * 根据流程实例id得到所有子流程实例id列表
				 * @param instanceId 当前流程实例id
				 */
				List inslist=adapterBaseWorkflowManager.getWorkflowService().getMonitorChildrenInstanceIds(new Long(object[0].toString()));
				if(inslist!=null && inslist.size()>0){
				   for(Iterator iter = inslist.iterator(); iter.hasNext();){
					Object[] obje = (Object[])iter.next();
					List xob=tracmanager.getDoingTracesById(obje[0].toString());
					
			        Object[] rbjs = adapterBaseWorkflowManager.getWorkflowService().getProcessStatusByPiId(obje[0].toString());//得到此流程实例下的运行情况
			        Collection cols = (Collection)rbjs[6];//处理任务信息
			        if(cols != null && !cols.isEmpty()) {
						for(Iterator it = cols.iterator();it.hasNext();) {
							Object[] itbjs = (Object[])it.next();
							if(xob!=null && xob.size()>0){
						       for(Iterator ite =xob.iterator(); ite.hasNext();){
							      Object[] ob = (Object[])ite.next();
							      if(itbjs[1].toString().equals(ob[3].toString())){
										String useId = (String)itbjs[3];
										String userN = new String();
										String orgN = new String();
										if(useId != null && !"".equals(useId)) {
											String[] useIds = useId.split(",");
											for(String id : useIds) {
												userN=userService.getUserNameByUserId(id);
												orgN=(userService.getUserDepartmentByUserId(id).getOrgName());
												if(userN!=null && orgN!=null)
												strUserName.append(userN).append("(").append(orgN.toString()).append(")");
												
												strUserName.append(",");
											}
											if(strUserName.length()>0){
											  strUserName.deleteCharAt(strUserName.length() - 1);
											}
										}
							      }
							 
						     }
							}
						     else{
						    	  String useId = (String)itbjs[3];
									String userN = new String();
									String orgN = new String();
									if(useId != null && !"".equals(useId)) {
										String[] useIds = useId.split(",");
										for(String id : useIds) {
											userN=userService.getUserNameByUserId(id);
											orgN=(userService.getUserDepartmentByUserId(id).getOrgName());
											if(userN!=null && orgN!=null)
											strUserName.append(userN).append("(").append(orgN.toString()).append(")");
											
											strUserName.append(",");
										}
										if(strUserName.length()>0){
										  strUserName.deleteCharAt(strUserName.length() - 1);
										}
									}
						      }
				
						}
						}
				}
				   
				}else{
				Object[] returnObjs = adapterBaseWorkflowManager.getWorkflowService().getProcessStatusByPiId(object[0].toString());//得到此流程实例下的运行情况
				Collection col = (Collection)returnObjs[6];//处理任务信息
			
				if(col != null && !col.isEmpty()) {
					for(Iterator it = col.iterator();it.hasNext();) {
						Object[] itObjs = (Object[])it.next();
						String userId = (String)itObjs[3];
		
						String userN = new String();
						String orgN = new String();
						if(userId != null && !"".equals(userId)) {
							String[] userIds = userId.split(",");
							for(String id : userIds) {
								userN=userService.getUserNameByUserId(id);
								orgN=(userService.getUserDepartmentByUserId(id).getOrgName());
								if(userN!=null && orgN!=null)
								strUserName.append(userN).append("(").append(orgN.toString()).append(")");
								
								strUserName.append(",");
							}
							if(strUserName.length()>0){
							  strUserName.deleteCharAt(strUserName.length() - 1);
							}
						}
						
					}
				}
				
				}
			object[2] = strUserName.toString();	
	     }	
				
			}
	
		//	return lst;
	   //  pageTodo.setResult(dolist);
//	     ListUtils.splitList2Page(page, dolist);
	
		return "doingDocList";
	}
	
	public List getAnnalList() {
		return annalList;
	}

	public void setAnnalList(List annalList) {
		this.annalList = annalList;
	}

	public Page getPage() {
		return page;
	}

	public Map<String, String> getProcessDefinitionNameMapIds() {
		return processDefinitionNameMapIds;
	}

	public void setProcessDefinitionNameMapIds(
			Map<String, String> processDefinitionNameMapIds) {
		this.processDefinitionNameMapIds = processDefinitionNameMapIds;
	}

	public String getProcessDefinitionNames() {
		return processDefinitionNames;
	}

	public void setProcessDefinitionNames(String processDefinitionNames) {
		this.processDefinitionNames = processDefinitionNames;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Date getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}

	public String getSearchDateString() {
		return searchDateString;
	}

	public void setSearchDateString(String searchDateString) {
		this.searchDateString = searchDateString;
	}

	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public IWorkflowService getWorkflow() {
		return workflow;
	}

	public void setWorkflow(IWorkflowService workflow) {
		this.workflow = workflow;
	}

	@Override
	protected String getDictType() {
		// TODO Auto-generated method stub  /traceDoc/traceDoc.action*
		return null;
	}

	@Override
	protected BaseManager getManager() {
		// TODO Auto-generated method stub
		return this.basemanager;
	}

	@Override
	protected String getModuleType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWorkflowType() {
		// TODO Auto-generated method stub
		return null;
	}
	

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public void setPage(Page<Object[]> page) {
		this.page = page;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
