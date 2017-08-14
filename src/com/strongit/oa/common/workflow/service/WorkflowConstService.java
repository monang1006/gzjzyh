package com.strongit.oa.common.workflow.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowConstService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.oabo.BackInfoBean;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.businesscustom.CustomWorkflowConst;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.exception.ServiceException;

/**
 * Service 工作流接口实现类，适配工作流常量提供的接口
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 20, 2012 3:21:15 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.workflow.service.WorkflowConstService
 */
@Service
@OALogger
public class WorkflowConstService implements IWorkflowConstService {
	@Autowired
	protected IWorkflowService workflowService;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	protected IUserService userService;// 统一用户服务
	/**
	 * 获取流程在父流程实例中所对应的子流程节点id
	 * 
	 * @author 严建
	 * @param pi
	 * @return
	 * @createTime Mar 20, 2012 3:12:44 PM
	 */
	public Long getSuperProcessNodeId(ProcessInstance pi) {
		return (Long) pi.getContextInstance().getVariable(
				CustomWorkflowConst.WORKFLOW_SUPERPROCESS_NODEID);
	}

	/**
	 * 
	 * 根据流程实例id获取流程在父流程中对应的子流程节点id
	 * 
	 * @author 严建
	 * @param instanceId
	 * @return
	 * @createTime Mar 7, 2012 8:29:11 PM
	 */
	public Long getSuperProcessNodeIdByPid(String instanceId) {
		return getSuperProcessNodeId(workflowService
				.getProcessInstanceById(instanceId));
	}

	/**
	 * 根据任务实例id获取父流程节点id
	 * 
	 * @author 严建
	 * @param taskInstanceId
	 * @return
	 * @createTime Mar 20, 2012 2:55:01 PM
	 */
	public Long getSuperProcessNodeIdByTid(String taskInstanceId) {
		return getSuperProcessNodeId(workflowService.getTaskInstanceById(
				taskInstanceId).getProcessInstance());
	}

	/**
	 * 获取父流程实例id
	 * 
	 * @author 严建
	 * @param pi
	 * @return
	 * @createTime Mar 20, 2012 3:18:10 PM
	 */
	public Long getSuperProcessId(ProcessInstance pi) {
		return (Long) pi.getContextInstance().getVariable(
				WorkflowConst.WORKFLOW_SUB_PARENTPROCESSID);
	}

	/**
	 * 根据任务实例id获取父流程实例的id
	 * 
	 * @author 严建
	 * @param taskInstanceId
	 * @return
	 * @createTime Mar 20, 2012 2:24:56 PM
	 */
	public Long getSuperProcessIdByTid(String taskInstanceId) {
		return getSuperProcessId(workflowService.getTaskInstanceById(
				taskInstanceId).getProcessInstance());
	}

	/**
	 * 
	 * 根据流程实例id获取父流程实例的id
	 * 
	 * @author 严建
	 * @param pid
	 * @return
	 * @createTime Mar 20, 2012 3:34:48 PM
	 */
	public Long getSuperProcessIdByPid(String pid) {
		return getSuperProcessId(workflowService.getProcessInstanceById(pid));
	}

	/**
	 * 退文时，将退回信息保存在流程实例中<br/> 驳回时将退回信息保存在父流程实例中，退回和退回上一步保存在当前流程实例中
	 * 
	 * @author 严建
	 * @param token
	 *            当前任务的令牌
	 * @param backInfo
	 *            退回信息
	 * @param backType
	 *            退文方式
	 * @createTime Mar 22, 2012 11:41:57 AM
	 */
	private void setBackInfo(Token token, String backInfo, String backType) {
		if (backType == null || "".equals(backType)) {
			throw new ServiceException("参数backType不能为null或空字符");
		}
		if (CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_OVERRULE
				.equals(backType)) {// 驳回方式退文
			ProcessInstance pi = token.getProcessInstance();
			Long supi = getSuperProcessId(pi);
			ProcessInstance supinstanc = workflowService
					.getProcessInstanceById(supi.toString());
			supinstanc.getContextInstance().setVariable(
					CustomWorkflowConst.WORKFLOW_PROCESS_BACKINFO_PREFIX
							+ supinstanc.getRootToken().getId(), backInfo);
		} else {
			ProcessInstance pi = token.getProcessInstance();
			pi.getContextInstance().setVariable(
					CustomWorkflowConst.WORKFLOW_PROCESS_BACKINFO_PREFIX
							+ pi.getRootToken().getId(), backInfo);
		}
	}

	/**
	 * 退文时，将退回信息保存在流程实例中<br/> 驳回时将退回信息保存在父流程实例中，退回和退回上一步保存在当前流程实例中
	 * 
	 * @author 严建
	 * @param taskinstance
	 * @param backInfo
	 * @param backType
	 * @createTime Mar 22, 2012 1:26:03 PM
	 */
	private void setBackInfo(TaskInstance taskinstance, String backInfo,
			String backType) {
		setBackInfo(taskinstance.getToken(), backInfo, backType);
	}

	/**
	 * 退文时，将退回信息保存在流程实例中<br/> 驳回时将退回信息保存在父流程实例中，退回和退回上一步保存在当前流程实例中
	 * 
	 * @author 严建
	 * @param taskId
	 * @param backInfo
	 * @param backType
	 * @createTime Mar 22, 2012 1:26:43 PM
	 */
	private void setBackInfo(String taskId, String backInfo, String backType) {
		if (taskId == null || "".equals(taskId)) {
			throw new ServiceException("taskId 不能为null或空字符串");
		}
		setBackInfo(workflowService.getTaskInstanceById(taskId), backInfo,
				backType);
	}

	/**
	 * 退文时，将退回信息保存在流程实例中<br/> 驳回时将退回信息保存在父流程实例中，退回和退回上一步保存在当前流程实例中
	 * 
	 * @author 严建
	 * @param backinfobean
	 * @createTime Mar 22, 2012 1:15:47 PM
	 */
	private void setBackInfo(BackInfoBean backinfobean) {
		if (backinfobean == null) {
			throw new NullPointerException("参数backinfobean不能为null");
		}
		JSONObject jsonObj = null;
		if(backinfobean.getSuggestion() == null){
			jsonObj = new JSONObject();
			jsonObj.put("suggestion", "");
		}else{
			 jsonObj = JSONObject
			.fromObject(backinfobean.getSuggestion());
		}
		if (backinfobean.getUserId() == null
				|| "".equals(backinfobean.getUserId())) {
			throw new ServiceException("BackInfoBean属性userId为null或空字符串");
		} else {
			jsonObj.put("userId", backinfobean.getUserId());
			logger.info("退回人userId：" + backinfobean.getUserId());
		}
		if (backinfobean.getUserName() == null
				|| "".equals(backinfobean.getUserName())) {
			throw new ServiceException("BackInfoBean属性userName为null或空字符串");
		} else {
			jsonObj.put("userName", backinfobean.getUserName());
			logger.info("退回人userName：" + backinfobean.getUserName());
		}
		if (backinfobean.getOrgId() == null
				|| "".equals(backinfobean.getOrgId())) {
			throw new ServiceException("BackInfoBean属性orgId为null或空字符串");
		} else {
			jsonObj.put("orgId", backinfobean.getOrgId());
		}
		if (backinfobean.getOrgName() == null
				|| "".equals(backinfobean.getOrgName())) {
			throw new ServiceException("BackInfoBean属性orgName为null或空字符串");
		} else {
			jsonObj.put("orgName", backinfobean.getOrgName());
		}
		if(backinfobean.getRest1() == null){
			backinfobean.setRest1("0");//普通
		}
		if (backinfobean.getRest1() == null
				|| "".equals(backinfobean.getOrgName())) {
			throw new ServiceException("BackInfoBean属性rest1为null或空字符串");
		} else {
			jsonObj.put("rest1", backinfobean.getRest1());
		}
		if (backinfobean.getBackType() == null
				|| "".equals(backinfobean.getBackType())) {
			throw new ServiceException("BackInfoBean属性backType为null或空字符串");
		} else {
			jsonObj.put("backType", backinfobean.getBackType());
			logger.info("退回方式：" + backinfobean.backType());
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String backTime = sdf.format(new Date());
		jsonObj.put("backTime", backTime);
		logger.info("退回时间：" + backTime);
		if(jsonObj.containsKey("suggestion")){
			logger.info("退回意见：" + jsonObj.getString("suggestion"));
		}
		backinfobean.setBackInfo(jsonObj.toString());
		setBackInfo(backinfobean.getTaskId(), backinfobean.getBackInfo(),
				backinfobean.getBackType());
	}

	/**
	 * 退文时，将退回信息保存在流程实例中<br/> 驳回时将退回信息保存在父流程实例中，退回和退回上一步保存在当前流程实例中
	 * 
	 * @author 严建
	 * @param taskId		当前任务id
	 * @param backType		退回方式
	 * @param suggestion	意见信息
	 * @param backUserId	退回人员id
	 * @createTime Mar 23, 2012 11:24:15 AM
	 */
	public void setBackInfo(String taskId,String backType,String suggestion,String backUserId){
		if(backUserId == null || "".equals(backUserId)){
			backUserId = userService.getCurrentUser().getUserId();
		}
		User backUser = userService.getUserInfoByUserId(backUserId==null?IUserService.SYSTEM_ACCOUNT:backUserId);
		BackInfoBean backinfobean = new BackInfoBean();
		backinfobean.setTaskId(taskId);
		backinfobean.setBackType(backType);
		backinfobean.setSuggestion(suggestion);
		backinfobean.setUserId(backUser.getUserId());
		backinfobean.setUserName(backUser.getUserName());
		backinfobean.setOrgId(backUser.getOrgId());
		backinfobean.setRest1(backUser.getRest1());
		backinfobean.setOrgName(userService.getDepartmentByOrgId(backUser.getOrgId()).getOrgName());
		setBackInfo(backinfobean);
	}
	
	/**
	 * 退回文提交时，删除之前存在流程中的退回信息
	 * 
	 * @author 严建
	 * @param token
	 *            当前任务令牌
	 * @createTime Mar 22, 2012 11:40:49 AM
	 */
	private void deleteBackInfo(Token token) {
		if (token != null) {
			token.getProcessInstance().getContextInstance().deleteVariable(
					CustomWorkflowConst.WORKFLOW_PROCESS_BACKINFO_PREFIX
							+ token.getProcessInstance().getRootToken().getId());
		}
	}

	/**
	 * 退回文提交时，删除之前存在流程中的退回信息
	 * 
	 * @author 严建
	 * @param taskinstance
	 * @createTime Mar 22, 2012 12:58:30 PM
	 */
	private void deleteBackInfo(TaskInstance taskinstance) {
		deleteBackInfo(taskinstance.getToken());
	}

	/**
	 * 退回文提交时，根据任务实例id删除之前存在流程中的退回信息
	 * 
	 * @author 严建
	 * @param taskId
	 * @createTime Mar 22, 2012 12:56:57 PM
	 */
	public void deleteBackInfo(String taskId) {
		if (taskId == null || "".equals(taskId)) {
			throw new ServiceException("taskId 不能为null或空字符串");
		}
		deleteBackInfo(workflowService.getTaskInstanceById(taskId));
	}

	/**
	 * 获取当前退文的上一步退回信息
	 * 
	 * @author 严建
	 * @param token
	 *            当前任务令牌
	 * @createTime Mar 22, 2012 11:45:25 AM
	 */
	private String getBackInfo(Token token) {
		return (String) token.getProcessInstance().getContextInstance()
				.getVariable(
						CustomWorkflowConst.WORKFLOW_PROCESS_BACKINFO_PREFIX
								+ token.getProcessInstance().getRootToken().getId());
	}

	/**
	 * 获取当前退文的上一步退回信息
	 * 
	 * @author 严建
	 * @param taskinstance
	 * @return
	 * @createTime Mar 22, 2012 1:08:52 PM
	 */
	private String getBackInfo(TaskInstance taskinstance) {
		return getBackInfo(taskinstance.getToken());
	}

	/**
	 * 根据任务id获取当前退文的上一步退回信息
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Mar 22, 2012 1:09:39 PM
	 */
	public String getBackInfo(String taskId) {
		if (taskId == null || "".equals(taskId)) {
			throw new ServiceException("taskId 不能为null或空字符串");
		}
		return getBackInfo(workflowService.getTaskInstanceById(taskId));
	}
	/**
	 * 根据任务id获取当前退文的上一步退回信息
	 * 
	 * @description
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Apr 10, 2012 10:28:12 PM
	 */
	public BackInfoBean getBackInfoBean(String taskId) {
		String getBackInfo = getBackInfo(taskId);
		JSONObject jsonObj = JSONObject.fromObject(getBackInfo);
		BackInfoBean backinfobean = null;
		if (jsonObj != null && !jsonObj.toString().equals("null")) {
			backinfobean = new BackInfoBean();
			if (jsonObj.containsKey("userName")) {// 用户名称
				backinfobean.setUserName(jsonObj.getString("userName"));
			}
			if (jsonObj.containsKey("userId")) {// 用户id
				backinfobean.setUserId(jsonObj.getString("userId"));
			}
			if (jsonObj.containsKey("backTime")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				try {
					backinfobean.setBackTime(sdf.parse(jsonObj
							.getString("backTime")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (jsonObj.containsKey("orgName")) {
				backinfobean.setOrgName(jsonObj.getString("orgName"));
			}
			if (jsonObj.containsKey("rest1")) {
				backinfobean.setRest1(jsonObj.getString("rest1"));
			}
			if (jsonObj.containsKey("orgId")) {
				backinfobean.setOrgId(jsonObj.getString("orgId"));
			}
		}
		return backinfobean;
	}
}
