package com.strongit.oa.senddoc;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.Node;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.approveinfo.ApproveinfoManager;
import com.strongit.oa.bo.ToaApprovalSuggestion;
import com.strongit.oa.bo.ToaApproveinfo;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.senddoc.manager.SendDocHtmlManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.suggestion.IApprovalSuggestionService;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.exception.SystemException;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = "form", value = "/senddoc/sendDoc!input.action", type = ServletDispatcherResult.class) })
public class SendDocWorkflowAction extends BaseActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 414804380436597047L;

	private static String RETCODE_SUC = "0";
	
	private static String RETCODE_FAIL = "-1";
	
	@Autowired
	private IWorkflowService workflowService;


	@Autowired
	private MyInfoManager myInfoManager;
	

	@Autowired
	private SendDocHtmlManager sendDocHtmlManager;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private SendDocManager manager;
	
	@Autowired
	private ApproveinfoManager approveInfoManager;
	
	private String suggestion;
	
	@Autowired
	private IApprovalSuggestionService approvalSuggestionService;				//审批意见接口
	
	@Autowired
	private SystemsetManager systemsetManager;
	
	private List ideaLst;//保存在字典中的意见
	
	private String instanceId;
	
	private String taskId;
	
	//节点名称
	private String nodeName;
	
	private String formId;
	
	private String personDemo;
	
	private String bussinessId;
	
	private String aid;

	private String tableName;
	
	private String pkFieldName;
	
	private String pkFieldValue;
	
	private String returnFlag;
	
	private String privilegeInfo;
	
	private String userId;
	
	private String userName;
	
	private String orgName;
	
	private String users;//委托人信息
	
	private String suggestionStyle;    //意见输入模式
	
	private String sDate;

	private String businessName;
	private String parentInstanceId;


	/**
	 * 通过bussinessId得到草稿中保存在意见表中(临时意见表和流程意见表)的办理意见
	 * @return
	 */
	public String findSuggestionInDraftByBid() {
		JSONObject result = new JSONObject();
		try {
			//临时意见表中记录
			List<ToaApproveinfo> list = approveInfoManager.findApproveInfoByBid(bussinessId);
			if(list.isEmpty()) {
				//流程意见表中记录
				if(bussinessId.length() == 32 ){//说明是拟稿环节
					result.put("status", "1");
				}else{//说明已进入流程
					TwfInfoApproveinfo tinfo = workflowService.getApproveinfoByTaskId(bussinessId);
					if(tinfo == null){
						//不存在记录
						result.put("status", "1");
					}else{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						String aiDate = "";
						String atDate = "";
						
						if(tinfo.getAiDate() != null){
							aiDate = tinfo.getAiDate().toString();					
							Date date = sdf.parse(aiDate);
							aiDate = sdf.format(date);
						}
						
						if(tinfo.getAiFpersonname() != null){					
							atDate = tinfo.getAiFpersonname().toString();
							Date rdate = sdf.parse(atDate);
							atDate = sdf.format(rdate);
						}
						
						result.put("status", "0");
						result.put("aiContent", tinfo.getAiContent());
						result.put("aiId", tinfo.getAiId());
						result.put("aiDate",aiDate);
						result.put("atoPersonId", tinfo.getAiOpersonid());
						result.put("atDate",atDate);
						result.put("taskId",tinfo.getAiTaskId() );
					}
				}

			} else {
				ToaApproveinfo info = list.get(0);				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String aiDate = "";
				String atDate = "";
				
				if(info.getAiDate() != null){
					aiDate = info.getAiDate().toString();					
					Date date = sdf.parse(aiDate);
					aiDate = sdf.format(date);
				}
				
				if(info.getAtDate() != null){					
					atDate = info.getAtDate().toString();
					Date rdate = sdf.parse(atDate);
					atDate = sdf.format(rdate);
				}
				
				result.put("status", "0");
				result.put("aiContent", info.getAiContent());
				result.put("aiId", info.getAiId());
				result.put("aiDate",aiDate);
				result.put("atoPersonId", info.getAtoPersonId());
				result.put("atDate",atDate);
				result.put("taskId",info.getAiBusinessId());
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			result.put("status", "-1");
		}
		return this.renderText(result.toString());
	}
	
	
	/**
	 * 通过Aid得到流程中保存在意见表中(临时意见表和流程意见表)的办理意见
	 * @return
	 */
	public String findSuggestionInDraftByAid() {
		JSONObject result = new JSONObject();
		try {
			//临时意见表中记录
			List<ToaApproveinfo> list = approveInfoManager.findApproveInfoByAid(aid);
			if(list.isEmpty()) {
				//流程意见表中记录
				TwfInfoApproveinfo tinfo = workflowService.getApproveInfoById(aid);
				if(tinfo == null){
					//不存在记录
					result.put("status", "1");
				}else{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String aiDate = "";
					String atDate = "";
					
					if(tinfo.getAiDate() != null){
						aiDate = tinfo.getAiDate().toString();					
						Date date = sdf.parse(aiDate);
						aiDate = sdf.format(date);
					}
					
					if(tinfo.getAiFpersonname() != null){					
						atDate = tinfo.getAiFpersonname().toString();
						Date rdate = sdf.parse(atDate);
						atDate = sdf.format(rdate);
					}
					
					result.put("status", "0");
					result.put("aiContent", tinfo.getAiContent());
					result.put("aiId", tinfo.getAiId());
					result.put("aiDate",aiDate);
					result.put("atoPersonId", tinfo.getAiOpersonid());
					result.put("atDate",atDate);
					result.put("taskId",tinfo.getAiTaskId() );
				}
			} else {
				ToaApproveinfo info = list.get(0);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String aiDate = "";
				String atDate = "";
				
				if(info.getAiDate() != null){
					aiDate = info.getAiDate().toString();					
					Date date = sdf.parse(aiDate);
					aiDate = sdf.format(date);
				}
				
				if(info.getAtDate() != null){					
					atDate = info.getAtDate().toString();
					Date rdate = sdf.parse(atDate);
					atDate = sdf.format(rdate);
				}
				
				result.put("status", "0");
				result.put("aiContent", info.getAiContent());
				result.put("aiId", info.getAiId());
				result.put("aiDate",aiDate);
				result.put("atoPersonId", info.getAtoPersonId());
				result.put("atDate",atDate);
				result.put("taskId",info.getAiBusinessId());
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			result.put("status", "-1");
		}
		return this.renderText(result.toString());
	}
	

	/**
	 * 得到指定任务上的办理意见
	 * @return
	 */
	public String findSuggestionInTask() {
		JSONObject result = new JSONObject();
		try {
			TwfInfoApproveinfo info = workflowService.getApproveinfoByTaskId(taskId);
			if(info == null) {
				result.put("status", "1");
			} else {
				result.put("status", "0");
				result.put("aiContent", info.getAiContent());
				result.put("aiId", info.getAiId());
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			result.put("status", "-1");
		}
		return this.renderText(result.toString());
	}
	
	/**
	 * 清空流程办理记录
	 * 保留任务信息
	 * 操作成功返回0，失败返回-1
	 */
	public void clearProcessInstance() {
		String ret = "0";
		try {
			workflowService.deleteProcessInstanceRelateInfo(instanceId,taskId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ret = "-1";
		}
		this.renderText(ret);
	}
	
	/**
	 * 校验任务是否允许主办人员办理。
	 * 若当前办理人有一个人设置了主办人员代办，则允许主办人员办理。否则不能办理。
	 * status:  “0”：不存在主办人员代办；“1”：存在主办人员代办；“2”：发起人和当前用户不一致；“3”：流程已结束，不能办理。“-1”：发生异常
	 */
	@SuppressWarnings("unchecked")
	public String getCurrentTaskHandler() throws Exception {
		JSONObject result = new JSONObject();
		try {
			User user = userService.getCurrentUser();
			JSONArray info = new JSONArray();
			boolean isExistHostby = false;
			Object[] objs = workflowService.getProcessStatusByPiId(instanceId);
			Collection<Object[]> currentTaskInfo = (Collection<Object[]>)objs[6];
			ProcessInstance processInstance = workflowService.getProcessInstanceById(instanceId);//得到流程实例对象
			ProcessDefinition processDefinition = processInstance.getProcessDefinition();
			String startUserId = processInstance.getStartUserId();//得到发起人用户id
			result.put("businessName", processInstance.getBusinessName());//业务数据标题
			result.put("workflowName", processDefinition.getName());
			if(!workflowService.hasMainDoing(user.getUserId(),taskId)) {
				result.put("status", "2");//主办人员和当前用户不同
			} else if(processInstance.getEnd() != null){
				result.put("status", "3");//流程已结束,不允许办理
			} else  {
				if(currentTaskInfo != null && !currentTaskInfo.isEmpty()) {//得到当前任务处理情况
					for(Object[] taskInfos : currentTaskInfo) {
						String taskFlag = (String)taskInfos[0];//得到任务处理标志 “task”：表示未完成的任务；“subProcess”：表示正在执行的子流程
						if("task".equals(taskFlag)) {//普通任务
							//String currentUserId = (String)taskInfos[3];
							String nodeName = (String)taskInfos[1];//得到节点名称
							Node node = processDefinition.getNode(nodeName);
							List<Object[]> list = workflowService.getCurrentHandleByNode(instanceId, String.valueOf(node.getId()));
							if(list != null && list.size() > 0) {
								for(Object[] currentInfo : list) {
									String userId = currentInfo[4].toString();
									String taskId = currentInfo[5].toString();			//任务实例id
									if(!result.containsKey("taskId")) {
										result.put("taskId", taskId);//任务实例id
										String isBackTask = "0";//当前任务的上一任务是否是退回的任务
										JbpmContext jbpmContext = workflowService.getJbpmContext();
										try{
											TaskInstance taskInstance = jbpmContext.getTaskInstance(new Long(taskId));
											if(taskInstance != null) {
												if("1".equals(taskInstance.getIsBackspace())) {//找到了退回任务
													isBackTask = "1";
												}
											}
										}finally{
											jbpmContext.close();
										}
										result.put("isBackspace", isBackTask);
									}
									ToaPersonalInfo entry = myInfoManager.getInfoByUserid(userId);
									if(entry != null) {
										String flag = entry.getMailnum();//得到委托设置
										if("0".equals(flag)) {//存在主办人员代办获取办理人就是当前用户
											if(!userId.equals(user.getUserId())){//主办人员和当前用户不是同一个人，"代办意见"按钮才允许出现												
												isExistHostby = true;
											}
											JSONObject json = new JSONObject();
											json.put("userId", userId);						//用户id
											json.put("userName", entry.getPrsnName());		//用户名称
											json.put("taskId", taskId);						//保存当前任务实例id
											info.add(json);
										}
									}	
								}
								
							}
						}
					}
				}
				if(isExistHostby) {
					result.put("status", "1");//存在主办人员代办且主办人员与当前用户不是同一人
				} else {
					result.put("status", "0");
				}
			}
			result.put("user", info.toString());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			result.put("status", "-1");
		}
		logger.info(result.toString());
		return this.renderText(result.toString());
	}

	public String toSuggestion() throws Exception {
		User user = userService.getCurrentUser();
		ideaLst = approvalSuggestionService.getAppSuggestionListByUserId(user.getUserId());
		//yanjian 2011-11-23 15:58 处理原有数据库中数据
		for(int j=0;j<ideaLst.size();j++){
			ToaApprovalSuggestion suggestionBean =(ToaApprovalSuggestion) ideaLst.get(j);
			String suggestion = suggestionBean.getSuggestionContent();
			suggestion = suggestion.replaceAll("\r", "");//处理审批意见有回车的情况
			suggestion = suggestion.replaceAll("\n", " ");//处理审批意见有换行的情况
			String[] FF_String = new String[]{"\'","\"","<",">","\\\\"};//特殊字符
			String[] NFF_String = new String[]{"’","”", "＜","＞","＼"};//替换字符
			boolean isFlag = false;
			for(int i=0;i<FF_String.length;i++){
				if(suggestion.indexOf(FF_String[i])!=-1){
					suggestion=suggestion.replaceAll(FF_String[i], NFF_String[i]);
					isFlag = true;
				}
			}
			if(isFlag) {
				suggestionBean.setSuggestionContent(suggestion);				
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		if(!"".equals(sDate) && sDate != null){
			date = sdf.parse(sDate);
		}
		getRequest().setAttribute("date", date);
		return "tosuggestion";
	}

	public void saveSuggestion() throws Exception {
		String ret = "0";
		try {
			String[] info = manager.getFormIdAndBussinessIdByTaskId(taskId);
			bussinessId = info[0];
			String isNewForm = "0";
			if ("0".equals(bussinessId)) {
				isNewForm = "1";
			}
			JSONObject json = new JSONObject();
			json.put("suggestion", suggestion);
			json.put("CAInfo", "");
			String recordId = workflowService.handleProcess(taskId, isNewForm, formId, bussinessId, 
								json.toString(), userService.getCurrentUser().getUserId());
			ret = recordId;
			
			if(userId != null && userId.length() > 0) {
				TwfInfoApproveinfo approveInfo = workflowService.getApproveInfoById(recordId);
				//approveInfo.setAiActorId(userId);
				approveInfo.setAiOpersonid(userId);
				approveInfo.setAiAssigntype("0");//委托类型
				approveInfo.setAiOpersonname(userName);
				workflowService.saveApproveInfo(approveInfo);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			ret = "-1";
		}
		this.renderText(ret);
	}
	
	public void updateSuggestion() {
		String ret = "0";
		try {
			TwfInfoApproveinfo info = workflowService.getApproveInfoById(bussinessId);
			if(info == null	) {
				throw new SystemException("意见记录未找到。");
			}
			info.setAiContent(suggestion);
			info.setAiDate(new Date());
			workflowService.saveApproveInfo(info);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			ret = "-1";
		}
		this.renderText(ret);
	}
	
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		
		ToaSystemset systemset = systemsetManager.getSystemset(); // 获取系统全局配置信息
		suggestionStyle = systemset.getSuggestionStyle() ;        //得到意见输入模式
		
		if(instanceId == null || instanceId.equals("")){
			instanceId = workflowService.getProcessInstanceIdByTiId(taskId);
		}
		if (instanceId != null && !instanceId.equals("")) {
			ContextInstance cxt =workflowService.getContextInstance(instanceId);
			businessName = cxt.getProcessInstance().getBusinessName();
			JSONArray jsonArray = getParentInstanceIdLevelInfo(instanceId);
			if(jsonArray != null && !jsonArray.isEmpty()){
				personDemo = jsonArray.toString();
				parentInstanceId = "exists";
			}
//			Object tempObject = cxt
//					.getVariable(WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);// 获取父流程实例id
//			if (tempObject != null && !tempObject.toString().equals("")) {
//				parentInstanceId = tempObject.toString();
//				ContextInstance parentCxt = adapterBaseWorkflowManager.getWorkflowService()
//						.getContextInstance(parentInstanceId);// 获取父流程上下文
//				personDemo = (String) parentCxt
//						.getVariable("@{personDemo}");
//			}
		}
		TwfBaseNodesetting nodeSetting = manager.getOperationHtml(taskId,null);
		returnFlag = nodeSetting.getNsFormPrivInfo();
		privilegeInfo = workflowService.getDocumentPrivilege(nodeSetting);
		User user = userService.getCurrentUser();// 得到当前用户
		userId = user.getUserId();
		userName = user.getUserName();// 当前用户姓名
		Organization organization = userService
				.getDepartmentByLoginName(user.getUserLoginname());// 得到当前用户所属单位
		orgName = organization.getOrgName();// 当前用户所属单位名称
		String strNodeInfo = workflowService.getNodeInfo(taskId);
		String[] arrNodeInfo = strNodeInfo.split(",");
		String[] info = new String[2];
		if ("form".equals(arrNodeInfo[0])) {
			info[0] = arrNodeInfo[2];
			info[1] = arrNodeInfo[3];
		} else {
			// 异常情况，抛出异常
		}
		formId = info[1];
		bussinessId = info[0];
		if (!"0".equals(bussinessId)) {
			String[] args = bussinessId.split(";");
			tableName = args[0];
			pkFieldName = args[1];
			pkFieldValue = args[2];
			
		}
		return INPUT;
	}
	/**
	 * 根据流程实例id获取父流程数据信息，适用于查看原表单的操作
	 * 
	 * @author 严建
	 * @param instanceId	流程实例id
	 * @return
	 * @createTime Jul 20, 2012 2:16:23 PM
	 */
	protected JSONArray getParentInstanceIdLevelInfo(String instanceId) {
		JSONArray jsonArray = new JSONArray();
		List parentInstanceList = workflowService.getMonitorParentInstanceIds(
						new Long(instanceId));
		if (parentInstanceList != null && !parentInstanceList.isEmpty()) {
			int parentInstanceListSize = parentInstanceList.size();
			for (int i = 0; i < parentInstanceListSize; i++) {
				String tempPID = ((Object[]) parentInstanceList.get(i))[0]
						.toString();
				String tempParentPID = "-1";// 父流程实例id
				if (i < parentInstanceListSize - 1) {
					tempParentPID = ((Object[]) parentInstanceList.get(i + 1))[0]
							.toString();
				}
				ProcessInstance temppi = workflowService.getProcessInstanceId(tempPID);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("instanceId", temppi.getId()); // 流程实例id
				jsonObject.put("parentInstanceId", tempParentPID); // 父流程实例id，“-1”表示不存在
				jsonObject.put("businessId", temppi.getBusinessId()); // 流程业务id
				jsonObject.put("mainFormId", temppi.getMainFormId()); // 流程实例业务表单id
				jsonArray.add(jsonObject);
			}
		}
		return jsonArray;
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

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 得到指定的任务节点的之前办理过的任务节点列表（退回）
	 * @return
	 * @throws Exception
	 */
	public String preNodeList() throws Exception {
		getRequest().setAttribute("preNodeListHtml", sendDocHtmlManager.preNodeMapHtml(taskId));
		return "prenodelist";
	}
	/**
	 * 流程图 --判断指定的任务节点是否是不可退回的任务节点（退回）
	 * @return
	 * @throws Exception
	 */
	public String preNodeIsplugins_notbackspace() throws Exception {
	String flag="";	
	flag=sendDocHtmlManager.NodeMapHtml(taskId,nodeName);
	if(flag=="success"){
		//如果该节点是不可退回节点，返回0
		return renderHtml("0");
	}
	else {
		return renderHtml("1");
	}
		
	}
	
	/**
	 * 流程图 --判断指定的任务节点是否是不可驳回的任务节点（驳回）
	 * @return
	 * @throws Exception
	 */
	public String preNodeIsplugins_notoverrule() throws Exception {
	String flag="";	
	flag=sendDocHtmlManager.NodeMapHtml2(taskId,nodeName);
	if(flag=="success"){
		//如果该节点是不可退回节点，返回0
		return renderHtml("0");
	}
	else {
		return renderHtml("1");
	}
		
	}
	
	
	/**得到指定的任务节点的之前办理过的任务节点列表(驳回)
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Mar 27, 2012 10:10:14 AM
	 */
	public String preSuperNodeList() throws Exception{
		getRequest().setAttribute("preSuperNodeListpHtml", sendDocHtmlManager.preSuperNodeMapHtml(taskId));
		return "presupernodelist";
	}
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getPersonDemo() {
		return personDemo;
	}

	public void setPersonDemo(String personDemo) {
		this.personDemo = personDemo;
	}

	public String getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}
	
	public String getAid() {
		return aid;
	}


	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPkFieldName() {
		return pkFieldName;
	}

	public void setPkFieldName(String pkFieldName) {
		this.pkFieldName = pkFieldName;
	}

	public String getPkFieldValue() {
		return pkFieldValue;
	}

	public void setPkFieldValue(String pkFieldValue) {
		this.pkFieldValue = pkFieldValue;
	}

	public String getReturnFlag() {
		return returnFlag;
	}

	public void setReturnFlag(String returnFlag) {
		this.returnFlag = returnFlag;
	}

	public String getPrivilegeInfo() {
		return privilegeInfo;
	}

	public void setPrivilegeInfo(String privilegeInfo) {
		this.privilegeInfo = privilegeInfo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public List getIdeaLst() {
		return ideaLst;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	
	public String getSuggestionStyle() {
		return suggestionStyle;
	}

	public void setSuggestionStyle(String suggestionStyle) {
		this.suggestionStyle = suggestionStyle;
	}
	
	public String getsDate() {
		return sDate;
	}


	public void setsDate(String sDate) {
		this.sDate = sDate;
	}


	public String getBusinessName() {
		return businessName;
	}


	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}


	public String getParentInstanceId() {
		return parentInstanceId;
	}


	public void setParentInstanceId(String parentInstanceId) {
		this.parentInstanceId = parentInstanceId;
	}


	public String getNodeName() {
		return nodeName;
	}


	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
   
}
