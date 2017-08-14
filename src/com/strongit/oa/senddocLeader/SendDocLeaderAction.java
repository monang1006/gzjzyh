package com.strongit.oa.senddocLeader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.strongit.doc.sends.DocSendManager;
import com.strongit.oa.autoencoder.CodemanageManager;
import com.strongit.oa.bo.Tjbpmbusiness;
import com.strongit.oa.bo.ToaSenddoc;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.common.workflow.comparator.util.SortConst;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.common.workflow.model.TaskBusinessBean;
import com.strongit.oa.common.workflow.plugin.DefinitionPluginService;
import com.strongit.oa.doctemplate.doctempItem.DocTempItemManager;
import com.strongit.oa.doctemplate.doctempType.DocTempTypeManager;
import com.strongit.oa.senddoc.AttachManager;
import com.strongit.oa.senddoc.CAAuth;
import com.strongit.oa.senddoc.SendDocAction;
import com.strongit.oa.senddoc.bo.ParamBean;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.senddoc.manager.SendDocDesktopManager;
import com.strongit.oa.senddoc.manager.SendDocHtmlManager;
import com.strongit.oa.senddoc.manager.SendDocLinkManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.senddoc.manager.service.ISendDocIcoService;
import com.strongit.oa.senddoc.pojo.ProcessedParameter;
import com.strongit.oa.senddoc.util.HandleKindConst;
import com.strongit.oa.systemset.ISystemsetService;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.StringUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
import com.strongit.oa.common.user.IUserService;

@ParentPackage("default") 
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "sendDocLeader.action", type = ServletActionRedirectResult.class)
			,@Result(name = "senddoctodo", value = "/WEB-INF/jsp/senddocLeader/handle/senddoc/sendDocLeader-todo.jsp", type = ServletDispatcherResult.class)
			,@Result(name = "senddocprocessed", value = "/WEB-INF/jsp/senddocLeader/handle/senddoc/sendDocLeader-processed.jsp", type = ServletDispatcherResult.class)
			,@Result(name = "recvdoctodo", value = "/WEB-INF/jsp/senddocLeader/handle/recvdoc/sendDocLeader-todo.jsp", type = ServletDispatcherResult.class)
			,@Result(name = "recvdocprocessed", value = "/WEB-INF/jsp/senddocLeader/handle/recvdoc/sendDocLeader-processed.jsp", type = ServletDispatcherResult.class)
		 })
public class SendDocLeaderAction extends SendDocAction {
	
	@Autowired
	SendDocDesktopManager sendDocDesktopManager;
	@Autowired
	AttachManager attachManager;
	
	@Autowired
	SendDocManager manager;
	
	@Autowired
	SendDocUploadManager sendDocUploadManager;
	
	@Autowired
	ISendDocIcoService sendDocIcoManager;

	@Autowired
	SendDocLinkManager sendDocLinkManager;
	 
	@Autowired
	SendDocLeaderManager sendDocLeaderManager;
	
	@Autowired private IUserService user;
	
    @Autowired
	SendDocHtmlManager sendDocHtmlManager;
    
	@Autowired
	DefinitionPluginService definitionPluginService;// 流程定义插件服务类
	
	@Autowired
	CodemanageManager codeService;// 编号生成器服务类
    
	public String getProcessStatus() {
		return processStatus;
	}


	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	@Autowired
	IProcessDefinitionService processDefinitionService;

	@Autowired
	IProcessInstanceService processInstanceService;


	@Autowired
	private IUserService userService;// 统一用户服务

	private String sortType = ""; // 已办事宜排序方式
	
	@Autowired
	protected AdapterBaseWorkflowManager adapterBaseWorkflowManager;
	
	protected Map<String, List<Object[]>> workflowMap; // 启动流程集合
	
	protected List<Object[]> workflowTypeList; // 流程类型列表
	
	protected String excludeWorkflowTypeName = ""; // 流程类型名称
	
	

	public String getExcludeWorkflowTypeName() {
		return excludeWorkflowTypeName;
	}


	public void setExcludeWorkflowTypeName(String excludeWorkflowTypeName) {
		this.excludeWorkflowTypeName = excludeWorkflowTypeName;
	}


	public String getSortType() {
		return sortType;
	}


	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	/**
	 * 桌面显示 领导联系人查看领导的 已办事宜
	 * 
	 * @author 
	 * @return
	 * @throws Exception
	 * @createTime 
	 */
	public String showLdTableDesktopDoneWork() throws Exception {

		// 获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = getRequest().getParameter("showCreator");;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if (blockId != null) {
			Map<String, String> map = manager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");//字体大小
		}

		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}

		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		Page<TaskBusinessBean> page = sendDocLeaderManager.getLdProcessedWorkflowForDesktop(processStatus, num, sortType,
						filterSign);
		if(page==null){
			
			return null;
		}
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer(
				"	<table width=\"100%\" style=\"font-size:"+sectionFontSize+"px\" cellpadding=\"0\" cellspacing=\"0\">");
		if (page.getResult() != null) {		

			for (Iterator iterator = page.getResult().iterator(); iterator
					.hasNext();) {
				TaskBusinessBean taskbusinessbean = (TaskBusinessBean) iterator
						.next();
				innerHtml.append(" <tr height=\"25px\">");
				// 图标
				innerHtml.append("<td width=\"75%\">");
				sendDocIcoManager.loadProcessedIco(innerHtml, taskbusinessbean, rootPath);
//				if (processStatus.equals("1")) {// 办结文件
//					// object[14] = "";
//					innerHtml.append("<img src=\"").append(rootPath).append(
//							"/oa/image/desktop/littlegif/news_bullet.gif")
//							.append("\"/> ");
//				} else {// 已办文件
//					/* 显示红黄蓝图标 */
//					sendDocIcoManager.loadRedYellowGreenIco(innerHtml,
//							taskbusinessbean.getWorkflowStartDate(), rootPath);
//					
//					/* 显示公文期限图标 */
//					/**
//					sendDocIcoManager.loadTimeOutIco(innerHtml,
//							taskbusinessbean.getWorkflowStartDate(),
//							taskbusinessbean.getEndTime(), rootPath);
//					*/
//
//				}

				// 如果显示的内容长度大于设置的主题长度，则过滤该长度
				String title = taskbusinessbean.getBusinessName() == null ? ""
						: taskbusinessbean.getBusinessName();
				title = title.replace("\\r\\n", " ");
				title = title.replace("\\n", " ");
				String showTitle = title;
				if (title.length() > length && length > 0) {
					showTitle = title.substring(0, length) + "...";
				}

				Object titleShowDate = null;
				String docType = null;
				if (sortType.startsWith("taskenddate")) {
					docType = "办文时间：";
					titleShowDate = taskbusinessbean.getTaskEndDate();
				} else {
					docType = "来文时间：";
					titleShowDate = taskbusinessbean.getWorkflowStartDate();
				}
				StringBuilder clickTitle = new StringBuilder();
				clickTitle
						.append("var width=screen.availWidth-10;var height=screen.availHeight-30;");

				clickTitle
						.append("var a = window.open('")
						.append(getRequest().getContextPath())
						.append("/senddocLeader/sendDocLeader!viewProcessed.action?taskId=")
						.append(taskbusinessbean.getTaskId())
						.append("&instanceId=")
						.append(taskbusinessbean.getInstanceId())
						.append("&state=")
						.append(processStatus)
						.append("&searchType=1")
						.append(
								"','processed','height='+height+', width='+width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');")
						.append("if(a && a == 'OK'){window.location.reload();}");

				StringBuilder clickProcessType = new StringBuilder();
				clickProcessType.append(sendDocLeaderManager
						.genProcessedWorkflowNameLink(taskbusinessbean
								.getWorkflowName(), taskbusinessbean
								.getFormId(), taskbusinessbean
								.getWorkflowType(), processStatus, rootPath,
								filterSign));
				innerHtml.append("<span>").append("").append("<a href=\"#\" onclick=\"")
						.append(clickProcessType.toString())
						.append("\">[")
						.append(taskbusinessbean.getWorkflowAliaName()).append(
								"]</a>").append("").append(
								"<a href=\"#\" onclick=\"").append(
								clickTitle.toString()).append("\"")
								.append("title=\"").append(title).append("\n").append(docType + new SimpleDateFormat("yyyy-MM-dd HH:mm")
								.format(titleShowDate)).append("\">").append(
								showTitle).append("</a></span>");
				/**
				// 显示发起人信息
				
				if ("1".equals(showCreator)) {
					innerHtml.append("&nbsp;&nbsp;<span class =\"linkgray\">")
							.append(taskbusinessbean.getStartUserName())
							.append("</span>");
				}

				// 显示日期信息
				if ("1".equals(showDate)) {
					SimpleDateFormat st = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					innerHtml
							.append("&nbsp;&nbsp;<span class =\"linkgray10\">")
							.append(
									" ("
											+ st.format(taskbusinessbean
													.getWorkflowStartDate())
											+ ")").append("</span>");
				}
				*/
				innerHtml.append("</td>");
				//显示时间
				innerHtml.append("<td width=\"20%\">");
				innerHtml.append(
								new SimpleDateFormat("yyyy-MM-dd HH:mm")
										.format(taskbusinessbean
												.getWorkflowStartDate()));
				innerHtml.append("</td>");
				//显示人名
				innerHtml.append("<td>");
				//sendDocLinkManager.showCurTaskInfo(innerHtml, taskbusinessbean);
				StringBuffer tip = null;
				String CurTaskActorInfo = null;
				//只显示名字，不显示部门     申仪玲 2012.05.23
				String TaskActor = "";
				if (taskbusinessbean.getWorkflowEndDate() == null) {
					CurTaskActorInfo = (taskbusinessbean.getCurTaskActorInfo() == null ? ""
							: taskbusinessbean.getCurTaskActorInfo());
					String[] CurTaskActorInfos = CurTaskActorInfo.split(",");
					tip = new StringBuffer();
					if(CurTaskActorInfos.length > 0){
						for (String info : CurTaskActorInfos) {
							String username = null;
							if(info.indexOf("(") == -1){
								 username = info;
							}else{
								 username = info.substring(0,info.indexOf("("));
							}
							if(TaskActor.length()>0){
								TaskActor += ",";
							}
							TaskActor += username;
						}
						tip.append(CurTaskActorInfo);
					}else{
						tip = new StringBuffer("");
						CurTaskActorInfo = "";
					}

				} else {
					tip = new StringBuffer("");
					CurTaskActorInfo = "";
				}
				if(CurTaskActorInfo.length()>5){
					CurTaskActorInfo = CurTaskActorInfo.subSequence(0, 5)+"...";
				}
				//清除空格
				TaskActor = TaskActor.replaceAll(" ","");
				if(TaskActor.length()>3){
					
					TaskActor = TaskActor.subSequence(0, 3)+"...";
				}
				
				List<User> users = adapterBaseWorkflowManager.getWorkflowService().getCurrentTaskHandle(taskbusinessbean.getInstanceId()).getActors();
				String userNames = "";
				if(users != null && users.size()> 0){
					for(int i=0;i<users.size();i++){
						 User user = adapterBaseWorkflowManager.getUserService().getUserInfoByUserId(users.get(i).getUserId());
						 if("".equals(userNames)){							 
							 userNames = user.getUserName() + "_" + user.getUserLoginname();
						 }else{
							 userNames += "," + user.getUserName() + "_" + user.getUserLoginname();
						 }
					}
				}
				//已办事宜桌面列表不发送即时大蚂蚁消息
				/**
				innerHtml.append(
						"<span class =\"linkgray\" title=\"" + tip + "\" >").append("<a href=\"#\" onclick=\"sendMsg('" + userNames + "');\">")
						.append(TaskActor).append("</a></span>");
				*/
				
				innerHtml.append(
						"<span class =\"linkgray\" title=\"" + tip + "\" >").append(TaskActor).append("</span>");
				
				innerHtml.append("</td>");

				innerHtml.append(" </tr>");
			}

		}
		innerHtml.append("</table>");

		// 跳转连接
		StringBuffer link = new StringBuffer();
		String sortHtml = "";
		link.append("javascript:window.parent.refreshWorkByTitle('").append(
				getRequest().getContextPath());

		// processStatus-->0：未办结流程，1：已办结流程，
		if ("0".equals(processStatus)) {
			link.append("/senddocLeader/sendDocLeader!processedWorkflow.action?state=")
					.append(2).append("', '已办事宜'").append(");");
		} else if ("1".equals(processStatus)) {
			link.append("/senddocLeader/sendDocLeader!processedWorkflow.action?state=")
					.append(1).append("', '办结事宜'").append(");");
		} else {
			link.append("/senddocLeader/sendDocLeaderc!processedWorkflow.action?state=")
					.append(2).append("', '已办事宜'").append(");");
			if ("2".equals(processStatus)) {
				sortHtml = sendDocLinkManager.genSortTypeLink(blockId,
						this.sortType);
			}
		}
		innerHtml
				.append(
						"<div class=\"select\">"
								+ sortHtml
								+ "</div><div align=\"right\" style=\"padding:2px；font-size:12px;float:right;\"><a href=\"#\" onclick=\"")
				.append(link.toString()).append("\"> ")
				.append("<IMG SRC=\"").append(rootPath).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");

		innerHtml.append("<input type=\"hidden\" class=\"listsize\" value=\"")
				.append(page == null ? 0 : (page.getTotalCount())).append(
						"\" />");
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}
	
	
	/**
	 * 查看已办任务
	 * 
	 * @author:邓志城
	 * @date:2009-12-18 下午01:56:13
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String viewProcessed() throws Exception {
		try {
			//String suporgcode = userService.getCurrentUser().getSupOrgCode();
			String suporgcode = userService.getCurrentUserLeader().getSupOrgCode();
			getRequest().setAttribute("suporgcode", suporgcode);
			// 判断流程主办人员是否是当前用户
			String curUserId = adapterBaseWorkflowManager.getUserService()
					.getCurrentUser().getUserId();
			String userid=userService.getLDId(curUserId);//通过当前用户的id获取领导的id
			flag = adapterBaseWorkflowManager.getWorkflowService()
					.hasMainDoing(userid, taskId)
					+ "";

			String[] strBusinessId = getManager()
					.getFormIdAndBussinessIdByTaskId(taskId);
			TwfBaseNodesetting nodesetting = adapterBaseWorkflowManager
					.getWorkflowService().findFirstNodeSetting(taskId,
							workflowName);
			adapterBaseWorkflowManager.getSendDocUploadManager()
					.initProcessedByNodeSetting(nodesetting);
			nodeId = StringUtil.castString(nodesetting.getNsNodeId());
			formId = strBusinessId[1];
			if (!"0".equals(formId)) {
				String[] strBussinessId = strBusinessId[0].split(";");
				tableName = strBussinessId[0];
				pkFieldName = strBussinessId[1];
				pkFieldValue = strBussinessId[2];
			}

			ProcessInstance processinstance = adapterBaseWorkflowManager
					.getWorkflowService().getProcessInstanceById(instanceId);			
			businessName = processinstance.getBusinessName();
			workflowName = processinstance.getName();
			if (processinstance.getEnd() != null) {
				state = "1";
			} else {
				state = "0";
			}
			bussinessId = tableName + ";" + pkFieldName + ";" + pkFieldValue;
			String businessType = adapterBaseWorkflowManager.getJbpmbusinessmanager().findByBusinessId(bussinessId).getBusinessType();
			getRequest().setAttribute("businessType", businessType);
			JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
			if(jsonArray != null && !jsonArray.isEmpty()){
				personDemo = jsonArray.toString();
				parentInstanceId = "exists";
			}else{
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
//			ContextInstance cxt = adapterBaseWorkflowManager
//					.getWorkflowService().getContextInstance(instanceId);
//			Object tempObject = cxt
//					.getVariable(WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);// 获取父流程实例id
//			if (tempObject != null && !tempObject.toString().equals("")) {
//				parentInstanceId = tempObject.toString();
//				ContextInstance parentCxt = adapterBaseWorkflowManager
//						.getWorkflowService().getContextInstance(
//								parentInstanceId);// 获取父流程上下文
//				personDemo = (String) parentCxt.getVariable("@{personDemo}");
//			}

			Object[] toSelectItems = { "taskId" };
			List sItems = Arrays.asList(toSelectItems);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("taskType", "2");// 取非办结任务
			paramsMap.put("processSuspend", "0");// 取非挂起任务
			paramsMap.put("processInstanceId", instanceId);
			paramsMap.put("handlerId",userid);// 当前用户办理任务
			Map orderMap = new HashMap<Object, Object>();
			orderMap.put("taskStartDate", "1");
			List list = adapterBaseWorkflowManager.getWorkflowService()
					.getTaskInfosByConditionForList(sItems, paramsMap,
							orderMap, null, null, null, null);
			if (list != null && !list.isEmpty()) {
				getRequest().setAttribute("isExsitTodo", "1");
			} else {
				getRequest().setAttribute("isExsitTodo", "0");
			}

		} catch (Exception e) {
			logger.error("查看主办流程时发生错误", e);
		}
		return "processedview";
	}
	
	public String listprocessed() throws Exception {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(getWorkflowType());
		parameter.setBusinessName(businessName);
		parameter.setWorkflowType(workflowType);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setExcludeWorkflowTypeName(excludeWorkflowTypeName);
		parameter.setUserName(userName);
		parameter.setStartDate(startDate);
		parameter.setFilterSign(filterSign);
		parameter.setEndDate(endDate);
		parameter.setState(state);
		parameter.setIsSuspended("0");
		pageWorkflow = sendDocLeaderManager.getProcessedWorks(pageWorkflow, parameter,
						new OALogInfo("得到已办办任务列表"));
		return "listprocessed";
	}
	/**
	 * 已办流程 通过参数state控制已办结流程和未办结流程
	 * 
	 * @author:邓志城
	 * @date:2010-11-18 上午09:09:49
	 * @return
	 * @throws Exception
	 */
	public String processedWorkflow() throws Exception {

		//		List workflowInfoList = getManager().getProcessedWorkflow(workflowType,
		//				state, filterSign);
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(workflowType);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setProcessStatus(state);
		parameter.setFilterSign(filterSign);
		List workflowInfoList =sendDocLeaderManager.getProcessedWorkflow(parameter);
		doneWorkflowList(workflowInfoList);
		return "processedworkflow";
	}
	
	/**
	 * 处理流程信息
	 * 
	 * @author:邓志城
	 * @date:2010-11-15 上午11:56:22
	 * @param workflowInfoList
	 */
	private Object[] doneWorkflowList(List workflowInfoList) {
		if (workflowInfoList != null && !workflowInfoList.isEmpty()) {
			workflowMap = new HashMap<String, List<Object[]>>(workflowInfoList
					.size());
			List<Object[]> tempWorkflowTypeList = new LinkedList<Object[]>();
			workflowTypeList = new ArrayList<Object[]>(workflowInfoList.size());
			List<String> workflowTpeIdList = new LinkedList<String>();
			for (int i = 0; i < workflowInfoList.size(); i++) {
				Object[] workflowInfo = (Object[]) workflowInfoList.get(i);
				if (!workflowTpeIdList.contains(workflowInfo[1].toString())) {
					workflowTpeIdList.add(workflowInfo[1].toString());
					tempWorkflowTypeList.add(new Object[] { workflowInfo[1],
							workflowInfo[2] });
				}
				if (!workflowMap.containsKey(workflowInfo[1].toString())) {
					List<Object[]> workflowList = new ArrayList<Object[]>(1);
					workflowList.add(new Object[] { workflowInfo[0],
							workflowInfo[3], workflowInfo[4] });
					workflowMap.put(workflowInfo[1].toString(), workflowList);
				} else {
					workflowMap.get(workflowInfo[1].toString()).add(
							new Object[] { workflowInfo[0], workflowInfo[3],
									workflowInfo[4] });
				}
			}
			for (int i = 0; i < tempWorkflowTypeList.size(); i++) {
				Object[] objType = tempWorkflowTypeList.get(i);
				List<Object[]> workflowList = workflowMap.get(objType[0]
						.toString());
				if (workflowList != null) {
					int total = 0;
					for (int j = 0; j < workflowList.size(); j++) {
						Object[] workflowInfo = (Object[]) workflowList.get(j);
						int count = (Integer) workflowInfo[2];
						total = total + count;
					}
					objType = ObjectUtils.addObjectToArray(objType, total);
					workflowTypeList.add(objType);
				}
			}
			return new Object[] { workflowMap, workflowTypeList };
		}
		return null;
	}
	
	
	/**
	 * 得到已办流程列表 支持自定义查询及自定义显示
	 * 
	 * 郑志斌 20110816 根据流程标题最大长度，动态显示：流程标题、拟稿人、办理日期、任务类型区域太长
	 * 
	 * @modify yanjian 2011-09-28 添加来文办文排序
	 */
	@SuppressWarnings("unchecked")
	public String processed() throws Exception {
		getRequest().setAttribute("privilName", "领导的已办事宜");
		//公文传输中的意见征询  标识
		String sends = getRequest().getParameter("sends");
		if("1".equals(sends)){
			getRequest().setAttribute("sends", sends);
		}
		
		String suporgcode = userService.getCurrentUserLeader().getSupOrgCode();
		getRequest().setAttribute("suporgcode", suporgcode);
		
		//System.out.println("orgcode:" + suporgcode);
		
		int titleLength = 0; // 流程标题显示长度
		int otherLength = 0; // 拟稿人、办理日期、任务类型显示长度
		ParamBean parambean = new ParamBean();
		try {
			ProcessedParameter parameter = new ProcessedParameter();
			parameter.setWorkflowName(workflowName);
			parameter.setFormId(formId);
			parameter.setExcludeWorkflowType(excludeWorkflowType);
			parameter.setWorkflowType(workflowType);
			if(!"1".equals(showSignUserInfo)){//不是已分办 yanijan
				String currworkflowType = workflowType;
				handleKind = "0";
				if("3".equals(currworkflowType)){
					handleKind = "2";
				}else if("2".equals(currworkflowType)){
					handleKind = "1";
				}
			}
			parameter.setUserName(userName);
			parameter.setIsBackSpace(isBackSpace);
			parameter.setStartDate(startDate);
			parameter.setEndDate(endDate);
			parameter.setProcessStatus(state);
			
			parameter.setTaskIdList(null);
			parameter.setShowSignUserInfo(showSignUserInfo);
			if(showSignUserInfo != null && showSignUserInfo.equals("1")){
				if(sortType == null || sortType.equals("")){
					sortType = SortConst.SORT_TYPE_PROCESSSTARTDATE_DESC;
				}
			}
			parameter.setSortType(sortType);
			parameter.setFilterSign(filterSign);
			parameter.setFilterYJZX(filterYJZX);
			parameter.setHandleKind(handleKind);
			parameter.setIsSuspended("0");
			parameter.setNodeName(nodeName);
			Object[] obj = sendDocLeaderManager.getProcessed(page, parameter);
			
			parambean.setFilterYJZX(filterYJZX);
			parambean.setSortType(sortType);
			sendDocLinkManager.processedGridViewSort(parambean);
			if (obj != null) {
				List list = (List) obj[1];
				showColumnList = (List) obj[0];
				queryColumnList = (List<EFormComponent>) obj[2];
				tableName = (String) obj[3];

				int columnSize = showColumnList.size() - 1;
				if (columnSize > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map map = (Map) list.get(i);
						String workFlowTitle = (String) map
								.get("WORKFLOWTITLE");
						if (workFlowTitle != null && !workFlowTitle.equals("")) {
							if (workFlowTitle.length() > titleLength) {
								titleLength = workFlowTitle.length();
							}
							workFlowTitle = workFlowTitle
									.replace("\\r\\n", " ");
							workFlowTitle = workFlowTitle.replace("\\n", " ");
						}
						sendDocIcoManager.processedGridViewIco(map);
					}
					titleLength = (titleLength == 0 ? 15 : titleLength);
					if (titleLength > 0) {
						titleLength = titleLength * 2;
						if (titleLength < (100 - 20 * (columnSize - 1))) { // 流程标题最小显示长度
							titleLength = 100 - 20 * (columnSize - 1);
							otherLength = 20;
						} else if (titleLength > (100 - 12 * (columnSize - 1))) { // 流程标题最大显示长度
							titleLength = 100 - 12 * (columnSize - 1);

							otherLength = 12;
						} else {
							otherLength = (100 - titleLength)
									/ (columnSize - 1) + 1;
						}
					}

				}
			}
		} catch (Exception e) {
			logger.error("获取已办流程时发生异常", e);
		}
		getRequest().setAttribute("pageResultSize",
				(page.getResult() == null ? 0 : page.getResult().size()));
		getRequest().setAttribute("sortHtml", (parambean.getSortHtml() == null ?"":parambean.getSortHtml()).toString());
		getRequest().setAttribute("yjzxHtml", (parambean.getYjzxHtml() == null ? "":parambean.getYjzxHtml()).toString());
		getRequest().setAttribute("titleLength", String.valueOf(titleLength));
		getRequest().setAttribute("otherLength", String.valueOf(otherLength));
		return HandleKindConst.getReturnString(handleKind, "processed");
	}
	
	
	@Override
	protected BaseManager getManager() {
		return this.manager;
	}

	@Override
	protected String getModuleType() {
		return String.valueOf(WorkFlowTypeConst.SENDDOC);
	}

	@Override
	public String getWorkflowType() {
		return this.workflowType;
	}

	@Override
	public List<Object[]> getWorkflowTypeList() {
		return workflowTypeList;
	}
 	/**
	 * @author:桌面显示    领导联系人查看领导的的待办事宜
	 * @description: “待办事宜”中显示，且急件需要“标红、置顶”；
	 * @date : 2011-7-3
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 * @modify “待办事宜”增加红黄牌预警提示功能
	 * @modify  过滤标题中的换行符
	 */
	@SuppressWarnings("unchecked")
	public String getLdTodoByRedType() throws Exception {
		logger.info("\r\n\t\t{@@@@@@} 系统调用：com.strongit.oa.senddoc.SendDocAction.getTodoByRedType() (*^◎^*){@@@@@@}");
		logger.info("\r\n\t\t{@@@@@@} 开始处理桌面待办事宜显示(*^◎^*){@@@@@@}");
		Long start = System.currentTimeMillis();
		// 获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if (blockId != null) {
			Map<String, String> map = manager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");// 是否字体大小
		}

		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}

		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		ParamBean parambean = new ParamBean();
		parambean.setLength(length);
		parambean.setRootPath(getRequest().getContextPath());
		parambean.setNum(num);
		parambean.setType(type);
		parambean.setShowCreator(showCreator);
		parambean.setShowDate(showDate);
		parambean.setSectionFontSize(sectionFontSize);
		parambean.setWorkflowType(workflowType);
		Page<TaskBusinessBean> page = sendDocUploadManager.getLdTodoByRedType(
				workflowType, type, num);
		if(page==null){
			return null;
		}
		StringBuffer innerHtml = new StringBuffer();
		sendDocLeaderManager.loadDesktopTodoHtml(page, innerHtml, parambean);
		logger.info("\r\n\t\t{@@@@@@} 桌面待办事宜显示耗时:"+(System.currentTimeMillis()-start)+"ms{@@@@@@}");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}
	
	
	/**
	 * 得到待办流程列表 支持自定义查询以及自定义显示.
	 * 
	 * 郑志斌 20110816 根据流程标题最大长度，动态显示：流程标题、拟稿人、办理日期、任务类型区域太长
	 */
	@SuppressWarnings("unchecked")
	public String todo() throws Exception {
		getRequest().setAttribute("privilName", "领导待办提醒");
		
		String workflowflag = ServletActionContext.getRequest().getParameter("workflowflag");
		isReceived = ServletActionContext.getRequest().getParameter("isReceived");
		
		if("1".equals(workflowflag)){
			workflowName = "公文转办流程";
		}else if("0".equals(workflowflag)){
			workflowName = "征求意见流程";
		}else{
			//workflowName = "";
			//前台桌面显示问题修改   taoji
			if(workflowName==null){
				workflowName = "";
			}
		}
		
		logger.info("\r\n\t\t{@@@@@@} 系统调用：com.strongit.oa.senddoc.SendDocAction.todo() (*^◎^*){@@@@@@}");
		logger.info("\r\n\t\t{@@@@@@} 开始处理待办流程列表显示(*^◎^*){@@@@@@}");
		Long start = System.currentTimeMillis();
		int titleLength = 0; // 流程标题显示长度
		int otherLength = 0; // 拟稿人、办理日期、任务类型显示长度
		try {
			ProcessedParameter parameter = new ProcessedParameter();
			String currworkflowType = workflowType;
			handleKind = "0";
			if("3".equals(currworkflowType)){
				handleKind = "2";
			}else if("2".equals(currworkflowType)){
				handleKind = "1";
			}
			parameter.setWorkflowName(workflowName);
			parameter.setFormId(formId);
			parameter.setWorkflowType(workflowType);
			parameter.setUserName(userName);
			parameter.setIsBackSpace(isBackSpace);
			String fromLeaderDesktopPage = getRequest().getParameter("fromLeaderDesktopPage");//"1"代表页面来自领导首页，则取本周时间
			if(fromLeaderDesktopPage != null && "1".equals(fromLeaderDesktopPage)){
				Date now = new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(now);
				if(calendar.get(Calendar.DAY_OF_WEEK)-1==0){	//如果是星期日 则向前退一天
					now.setTime(now.getTime()-24*60*60*1000);
					calendar.setTime(now);
				}
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				Date date = calendar.getTime();
				date.setHours(0);
				date.setMinutes(0);
				date.setSeconds(0);
				parameter.setStartDate(date);
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				calendar.set(Calendar.DATE, calendar.get(Calendar.DATE)+1);
				date = calendar.getTime();
				date.setHours(23);
				date.setMinutes(59);
				date.setSeconds(59);
				parameter.setEndDate(date);
			}else{
				parameter.setStartDate(startDate);
				parameter.setEndDate(endDate);
			}
			
			parameter.setType(type);
			parameter.setTaskIdList(null);
			parameter.setHandleKind(handleKind);
			parameter.setOrgId(roomId);//根据处室id取该处室全部(待签收文件)
			Object[] obj = sendDocLeaderManager.getTodo(page, parameter);
			if (obj != null) {
				List list = (List) obj[1];
				showColumnList = (List) obj[0];
				queryColumnList = (List<EFormComponent>) obj[2];
				tableName = (String) obj[3];
				int columnSize = showColumnList.size() - 1;
				if (columnSize > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map map = (Map) list.get(i);
						String workFlowTitle = (String) map
								.get("WORKFLOWTITLE");
						if (workFlowTitle != null && !workFlowTitle.equals("")) {
							if (workFlowTitle.length() > titleLength) {
								titleLength = workFlowTitle.length();
							}
							workFlowTitle = workFlowTitle
									.replace("\\r\\n", " ");
							workFlowTitle = workFlowTitle.replace("\\n", " ");
						}
						//列表加载图标
						sendDocIcoManager.todoGridViewIco(map);
//						StringBuilder picImage = new StringBuilder();
//						String rootPath = getRequest().getContextPath();
//						sendDocIcoManager.gridViewRedYellowGreenIco(picImage,
//								(Date) map.get("processstartdate"), rootPath);
//						// 显示公文期限图标
//						sendDocIcoManager.gridViewTimeOutIco(picImage, map
//								.get("taskStartDate"), map.get("timeOut"),
//								rootPath);
//
//						// 通过标识区分正常办理文、代办文、退文
//						sendDocIcoManager.gridViewBackspaceIco(picImage, (String) map.get("isBackspace"), (String) map.get("taskId"), rootPath);
//						sendDocIcoManager.gridViewAssignTypeIco(picImage, (String) map.get("assignType"), rootPath);
//						if (!map.containsKey("png")) {
//							map.put("png", picImage.toString());
//						}
					}
					titleLength = (titleLength == 0 ? 15 : titleLength);
					if (titleLength > 0) {
						titleLength = titleLength * 2;
						if (titleLength < (100 - 20 * (columnSize - 1))) { // 流程标题最小显示长度
							titleLength = 100 - 20 * (columnSize - 1);
							otherLength = 20;
						} else if (titleLength > (100 - 12 * (columnSize - 1))) { // 流程标题最大显示长度
							titleLength = 100 - 12 * (columnSize - 1);

							otherLength = 12;
						} else {
							otherLength = (100 - titleLength)
									/ (columnSize - 1) + 1;
						}
					}

				}
			}
		} catch (Exception e) {
			logger.error("获取待办事宜时发生异常", e);
		}
		getRequest().setAttribute("titleLength", String.valueOf(titleLength));
		getRequest().setAttribute("otherLength", String.valueOf(otherLength));
		logger.info("\r\n\t\t{@@@@@@} 桌面待办事宜显示耗时:"+(System.currentTimeMillis()-start)+"ms{@@@@@@}");
		String a= HandleKindConst.getReturnString(handleKind, "todo");
		return HandleKindConst.getReturnString(handleKind, "todo");
	}

	/**
	 * 办理任务时进行CA认证,认证成功后跳转到input方法
	 * 
	 * @author:邓志城
	 * @date:2011-4-2 下午03:47:55
	 * @modyfy：严建 2011-11024 11:50 添加系统全局设置是否启用的ca认证的处理
	 * @return
	 */
	public String CASign() throws Exception {

		if (taskId != null && !"".equals(taskId)) {
			Object[] objs = manager.getNodeWorkflowPluginInfoByTaskId(taskId);// 得到插件信息
			pluginInfo = objs[0].toString();

			ToaSystemset systemset = adapterBaseWorkflowManager.getSystemsetManager().getSystemset(); // 获取系统全局配置信息

			if (systemset.getIsUseCASign() == null
					|| systemset.getIsUseCASign().equals("")) {
				systemset.setIsUseCASign("0");
			}
			if (systemset.getIsUseCASign().equals("0")) {// 如果系统全局设置没有启用ca认证
				// ，系统直接调用input方法
				return input();
			}

			TwfBaseNodesetting setting = (TwfBaseNodesetting) objs[1];
			if (setting != null) {
				isNeedSign = setting.getPlugin("plugins_chkNeedSign");// 返回节点上设置的是否需要签名认证设置信息
				if ("1".equals(isNeedSign)) {
					String num = "1234567890abcdefghijklmnopqrstopqrstuvwxyz";
					int size = 6;
					char[] charArray = num.toCharArray();
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < size; i++) {
						sb
								.append(charArray[((int) (Math.random() * 10000) % charArray.length)]);
					}
					getRequest().getSession().setAttribute("original_data",
							sb.toString());
				} else {
					getRequest().getSession().removeAttribute("original_data");
					return input();
				}
			}
		}
		return "checksign";// 页面跳转到CA认证页面
	}
	
	/**
	 * 电子表单处理界面 支持新建流程和办理流程
	 */
	@Override
	public String input() throws Exception {
		HttpSession session = getSession();
		timeout = session.getMaxInactiveInterval();

		ToaSystemset systemset = adapterBaseWorkflowManager.getSystemsetManager().getSystemset(); // 获取系统全局配置信息
		suggestionStyle = systemset.getSuggestionStyle(); // 得到意见输入模式

		if (workflowName == null || workflowName.equals("")) {
			if (pkFieldValue != null && !pkFieldValue.equals("")) {
				Map map = manager.getSystemField(pkFieldValue, tableName);
				workflowName = (String) map
						.get(BaseWorkflowManager.WORKFLOW_NAME);// 得到流程名
			}
		}
		TwfBaseNodesetting nodeSetting = manager.getOperationHtml(taskId,
				workflowName);
		nodeName= nodeSetting.getNsNodeName();
		returnFlag = nodeSetting.getNsFormPrivInfo();
		privilegeInfo = adapterBaseWorkflowManager.getWorkflowService().getDocumentPrivilege(nodeSetting);
		User user = adapterBaseWorkflowManager.getUserService().getCurrentUser();// 得到当前用户
	 	//User user = adapterBaseWorkflowManager.getUserService().getCurrentUserLeader();// 得到当前用户的领导的信息
		//String userId = adapterBaseWorkflowManager.getUserService().getUserId();
		//User userLeader = userService.getCurrentUserLeader();
		String userId = user.getUserId();
		String LdId=userService.getLDId(userId);
		User Leader= userService.getUserInfoByUserId(LdId);// 当前用户领导的信息
		userName=Leader.getUserName();
		Organization organization = adapterBaseWorkflowManager.getUserService().getDepartmentByLoginName(Leader
				.getUserLoginname());// 得到当前用户的领导所属单位
		orgName = organization.getOrgName();// 当前用户的领导的所属单位名称
		if (taskId == null || "".equals(taskId)) {// 草稿
			if (nodeSetting != null) {// 流程启动表单取第一个节点上的表单
				formId = nodeSetting.getNsNodeFormId().toString();
				String strFlag = (String) nodeSetting
						.getPlugin("plugins_businessFlag");// 得到当前节点设置的域信息
				if (strFlag != null) {
					strFlag = URLDecoder.decode(strFlag, "utf-8");
					strFlag = strFlag.replaceAll("#", ",");
					JSONArray jsonArray = JSONArray.fromObject(strFlag);
					JSONObject fbj = jsonArray.getJSONObject(0);
					if (fbj.containsKey("type")) {// 输入意见字段
						jsonArray.remove(fbj);
						firstNodeControlName = fbj.getString("fieldName");
					}
				}
			}
			
			//taoji  新建征求意见不显示意见征询反馈栏
			getRequest().setAttribute("fankui", "1");
			
			
			if (pkFieldValue == null || "".equals(pkFieldValue)) {// 新建
				user = adapterBaseWorkflowManager.getUserService().getUserInfoByUserId(user.getUserId());
				userJob = user.getUserDescription();// 得到用户职务
				if (userJob == null) {
					userJob = "";
				}
				tableName = adapterBaseWorkflowManager.getEformService().getTable(formId);
				String codeId = definitionPluginService
						.getRuleCodeIdByWorkflowName(workflowName);
				if (codeId != null) {
					workflowCode = codeService.getCodeToFlow(codeId);
					logger
							.info("流程[" + workflowName + "]编码规则为："
									+ workflowCode);

				}
				
				
			} else {// 修改
				if (pkFieldName == null || "".equals(pkFieldName)) {
					pkFieldName = manager.getPrimaryKeyName(tableName);// 得到主键名称
				}
			}
		} else {
			// 判断流程主办人员是否是当前用户
			flag = adapterBaseWorkflowManager.getWorkflowService().hasMainDoing(LdId, taskId)+ "";
			String taskFlag = getRequest().getParameter("taskFlag");// 任务是否需要校验
			if (taskFlag != null && "notNeedCheck".equals(taskFlag)) {

			} else {
				if (!sendDocLeaderManager.isTaskInCurrentUser(taskId)) {
					return super.renderJavaScript("该任务已不在您的待办事宜列表中！");
				}
			}
//			// 任务是否允许办理
//			String ret = manager.judgeTaskIsDone(taskId);
//			String[] rets = ret.split("\\|");
//			String retCode = rets[0];
//			if (retCode.equals("f1")) {
//				return super.renderJavaScript("该任务正在被其他人处理或被挂起，请稍后再试或与管理员联系！");
//			} else if (retCode.equals("f2")) {
//				return super.renderJavaScript("该任务已被取消，请查阅处理记录或与管理员联系！");
//			} else if (retCode.equals("f3")) {
//				return super.renderJavaScript("该任务已被其他人处理，请查阅详细处理记录！");
//			}
//			else {
//				isReceived = adapterBaseWorkflowManager.getWorkflowService().getTaskInstanceById(taskId).getIsReceive();	
//				TaskInstance taskInstance = adapterBaseWorkflowManager.getWorkflowService().getTaskInstanceById(taskId);
//				getRequest().setAttribute("isBackspace", taskInstance.getIsBackspace());
//				ProcessInstance processInstance = taskInstance.getProcessInstance();
//				workflowName = processInstance.getName();
//				instanceId = processInstance.getId()+"";
//				businessName = processInstance.getBusinessName();
//				JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
//				if(jsonArray != null && !jsonArray.isEmpty()){
//					personDemo = jsonArray.toString();
//					parentInstanceId = "exists";
//				}
//				String[] info = manager.getFormIdAndBussinessIdByTaskId(taskId);
//				formId = info[1];
//				bussinessId = info[0];
//				if ("".equals(formId) || formId == null || "0".equals(formId)) {// 表单模板不存在,即流程未挂接表单,如栏目流程
//					return this.nextstep();
//				}
//				if(!adapterBaseWorkflowManager.getWorkflowService().isSignNodeTask(null,nodeSetting)){//签收节点查看时不签收任务
//					if("1".equals(taskInstance.getIsReceive())){
//						
//					}else{//肖利建--签收的在这里加if判断
//						if(!"公文转办流程".equals(workflowName) && !"征求意见流程".equals(workflowName)){
//							manager.signForTask(taskId, "0", new OALogInfo("签收任务"));							
//						}
//					}
//				}
//				if (!"0".equals(bussinessId)) {
//					String[] args = bussinessId.split(";");
//					tableName = args[0];
//					pkFieldName = args[1];
//					pkFieldValue = args[2];
//
//				}
//			}
			isReceived = adapterBaseWorkflowManager.getWorkflowService().getTaskInstanceById(taskId).getIsReceive();	
			TaskInstance taskInstance = adapterBaseWorkflowManager.getWorkflowService().getTaskInstanceById(taskId);
			getRequest().setAttribute("isBackspace", taskInstance.getIsBackspace());
			ProcessInstance processInstance = taskInstance.getProcessInstance();
			workflowName = processInstance.getName();
			instanceId = processInstance.getId()+"";
			businessName = processInstance.getBusinessName();
			JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
			if(jsonArray != null && !jsonArray.isEmpty()){
				personDemo = jsonArray.toString();
				parentInstanceId = "exists";
			}else{
				String demo = (String) processInstance.getContextInstance().getVariable("@{personDemo}");
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
			String[] info = manager.getFormIdAndBussinessIdByTaskId(taskId);
			formId = info[1];
			bussinessId = info[0];
			if ("".equals(formId) || formId == null || "0".equals(formId)) {// 表单模板不存在,即流程未挂接表单,如栏目流程
				return this.nextstep();
			}
			if(!adapterBaseWorkflowManager.getWorkflowService().isSignNodeTask(null,nodeSetting)){//签收节点查看时不签收任务
				if("1".equals(taskInstance.getIsReceive())){
					
				}else{//肖利建--签收的在这里加if判断
					if(!"公文转办流程".equals(workflowName) && !"征求意见流程".equals(workflowName)){
						manager.signForTask(taskId, "0", new OALogInfo("签收任务"));							
					}
				}
			}
			if (!"0".equals(bussinessId)) {
				String[] args = bussinessId.split(";");
				tableName = args[0];
				pkFieldName = args[1];
				pkFieldValue = args[2];

			}

		}
		sendDocUploadManager.initViewByNodeSetting(nodeSetting);
		
		/*控制公文传输转来的流程限制  tj
		String t = getRequest().getParameter("gwcs");
		if("gwcs".equals(t)){
			return "gwcs";
		}
		*/
		return INPUT;
	}
	
	
	/**
	 * 待办流程
	 * 
	 * @author:邓志城
	 * @date:2010-11-15 上午11:58:20
	 * @return
	 * @throws Exception
	 */
	public String todoWorkflow() throws Exception {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(workflowType);
		parameter.setType(type);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		List workflowInfoList = sendDocLeaderManager.getTodoWorkflow(parameter);
		doneWorkflowList(workflowInfoList);
		
		//公文传输  辅助参数
		String t = getRequest().getParameter("gwcs");
		getRequest().setAttribute("gwcs", t);
		
		return "todoworkflow";
	}
	
	/**
	 * 待办列表视图列表
	 * @description
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Apr 17, 2012 3:30:17 PM
	 */
	public String listtodo() throws Exception {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowType(getWorkflowType());
		parameter.setBusinessName(businessName);
		parameter.setWorkflowType(workflowType);
		parameter.setExcludeWorkflowType(excludeWorkflowType);
		parameter.setUserName(userName);
		parameter.setStartDate(startDate);
		parameter.setEndDate(endDate);
		parameter.setIsBackSpace(isBackSpace);
		pageWorkflow = sendDocLeaderManager.getTodoWorks(pageWorkflow, parameter,
						new OALogInfo("得到待办任务列表"));
		return "listtodo";
	}
	
	/**
	 * 执行CA认证
	 * 
	 * @author:邓志城
	 * @date:2011-4-2 下午03:57:21
	 * @return
	 */
	public String doCASign() {
		String ret = "0";
		try {
			boolean isSuccess = new CAAuth().auth(getRequest().getSession(), getRequest());
			if (!isSuccess) {
				ret = "1";
			}
		} catch (Exception e) {
			logger.error("CA认证发生错误", e);
			ret = "-1";
		}
		return this.renderText(ret);
	}

}
