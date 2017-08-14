package com.strongit.oa.common.workflow.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.strongit.oa.common.user.model.User;
import com.strongit.oa.senddoc.bo.UserBeanTemp;

/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         May 15, 2012 12:27:11 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.common.workflow.model.TaskBean
 */
public class TaskBean {

	private Date taskStartDateStart; // 任务开始时间上限

	private Date taskStartDateEnd; // 任务开始时间下限

	private Date taskEndDateStart; // 任务结束时间上限

	private Date taskEndDateEnd; // 任务结束时间下限

	private Date taskStartDate; // 任务开始时间

	private Date taskEndDate; // 任务结束时间

	private Date workflowStartDate; // 流程启动时间

	private Date workflowEndDate; // 流程结束时间

	private String workflowIsTimeOut; // 流程是否超期

	private String taskId; // 任务实例id

	private String formId; // 表单模板id

	private String instanceId; // 流程实例id

	private String workflowName; // 流程名称

	private String workflowStatus; // 流程状态

	private String workflowType; // 流程类型

	private String excludeWorkflowType; // 排除的流程类型

	private String workflowTypeName; // 流程类型名称

	private String workflowAliaName; // 流程名称别名

	private String bsinessId; // 业务数据id

	private String businessName; // 业务数据标题

	private String startUserId; // 发起人id

	private String startUserName; // 发起人名称

	private String startUserDeptName; // 发起人部门名称

	private List<User> actors; // 当前任务处理人信息

	private List<User> prevActors; // 上一任务处理人信息

	private List<Object[]> currentHandle;

	private String nodeId;

	private String nodeName;

	private String actorName;

	private String actorId;

	private String actorOrgName;

	private String workflowStatusDesc; // 流程办理状态描述

	private String isBackspace; // “isBackspace”：任务是否是回退任务(“0”：否；“1”：是)

	private String assignType; // “assignType”：委托类型(“0”：委托；“1”：指派)

	private String processSuspend; // “processSuspend”：流程是否被挂起(false：否；true：是)

	private String sortType; // 排序方式

	private String deptName;

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public String getActorName() {
		return actorName;
	}

	/**
	 * 得到当前办理节点上的任务实例列表(含子流程)
	 * 
	 * @return
	 */
	public List<String> getCurrentTaskIds() {
		List<String> tasks = new ArrayList<String>();
		if (currentHandle != null && !currentHandle.isEmpty()) {
			for (Object[] objs : currentHandle) {
				tasks.add(objs[5].toString());
			}
		}
		return tasks;
	}

	public String getActor() {
		StringBuilder userInfo = new StringBuilder();
		if (actors != null && !actors.isEmpty()) {
			/*
			 * 根据用户排序号排序 yanjian 2012-02-29 10:25
			 */
			/*Collections.sort(actors, new Comparator<User>() {
				public int compare(User arg0, User arg1) {
					Long key0 = arg0.getUserSequence() == null ? Long.MAX_VALUE
							: arg0.getUserSequence();
					Long key1 = arg1.getUserSequence() == null ? Long.MAX_VALUE
							: arg1.getUserSequence();
					return key0.compareTo(key1);
				}
			});*/
			/**
			 * 先根据机构排序，再根据用户序号排序 jianggb 2014-03-02
			 */
				Collections.sort(actors, new Comparator<User>(){       
				    public int compare(User f1, User f2){    
				        if(f1.getOrgSequence()-f2.getOrgSequence()>0){    
				            return 1;    
				        }else if (f1.getOrgSequence()-f2.getOrgSequence()<0){    
				            return -1;    
				        }else{//Foo.a equal, then check Foo.b    
				            return (int) (f1.getUserSequence()-f2.getUserSequence());    
				        }    
				}});  
			
			for (User user : actors) {
				if("1".equals(user.getRest1())){	//厅领导不显示部门
					userInfo.append(user.getUserName()).append(",");
				}else{
					userInfo.append(user.getUserName()).append("(").append(
							user.getOrgName()).append(")").append(",");
				}
			}
			if (userInfo.length() > 0) {
				userInfo.deleteCharAt(userInfo.length() - 1);
			}
		}
		return userInfo.toString();
	}

	public String getPrevActor() {
		StringBuilder userInfo = new StringBuilder();
		if (prevActors != null && !prevActors.isEmpty()) {
			for (User user : prevActors) {
				userInfo.append(user.getUserName()).append("(").append(
						user.getOrgName()).append(")").append(",");
			}
			if (userInfo.length() > 0) {
				userInfo.deleteCharAt(userInfo.length() - 1);
			}
		}
		return userInfo.toString();
	}

	public List<User> getActors() {
		return actors;
	}

	public void setActors(List<User> actors) {
		this.actors = actors;
	}

	public String getBsinessId() {
		return bsinessId;
	}

	public void setBsinessId(String bsinessId) {
		this.bsinessId = bsinessId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getStartUserId() {
		return startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}

	public Date getTaskStartDate() {
		return taskStartDate;
	}

	public void setTaskStartDate(Date taskStartDate) {
		this.taskStartDate = taskStartDate;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public List<Object[]> getCurrentHandle() {
		return currentHandle;
	}

	public void setCurrentHandle(List<Object[]> currentHandle) {
		this.currentHandle = currentHandle;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public Date getTaskEndDate() {
		return taskEndDate;
	}

	public void setTaskEndDate(Date taskEndDate) {
		this.taskEndDate = taskEndDate;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getWorkflowEndDate() {
		return workflowEndDate;
	}

	public void setWorkflowEndDate(Date workflowEndDate) {
		this.workflowEndDate = workflowEndDate;
	}

	public Date getWorkflowStartDate() {
		return workflowStartDate;
	}

	public void setWorkflowStartDate(Date workflowStartDate) {
		this.workflowStartDate = workflowStartDate;
	}

	public String getWorkflowAliaName() {
		return workflowAliaName;
	}

	public void setWorkflowAliaName(String workflowAliaName) {
		this.workflowAliaName = workflowAliaName;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public void setPrevActors(List<User> prevActors) {
		this.prevActors = prevActors;
	}

	public String getAssignType() {
		return assignType;
	}

	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}

	public String getIsBackspace() {
		return isBackspace;
	}

	public void setIsBackspace(String isBackspace) {
		this.isBackspace = isBackspace;
	}

	public String getProcessSuspend() {
		return processSuspend;
	}

	public void setProcessSuspend(String processSuspend) {
		this.processSuspend = processSuspend;
	}

	public String getWorkflowTypeName() {
		return workflowTypeName;
	}

	public void setWorkflowTypeName(String workflowTypeName) {
		this.workflowTypeName = workflowTypeName;
	}

	// 兼容以前的页面使用的变量
	public void setState(String state) {
		this.workflowStatus = state;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getStartUserDeptName() {
		return startUserDeptName;
	}

	public void setStartUserDeptName(String startUserDeptName) {
		this.startUserDeptName = startUserDeptName;
	}

	public String getWorkflowIsTimeOut() {
		return workflowIsTimeOut;
	}

	public void setWorkflowIsTimeOut(String workflowIsTimeOut) {
		this.workflowIsTimeOut = workflowIsTimeOut;
	}

	public String getWorkflowStatusDesc() {
		return workflowStatusDesc;
	}

	public void setWorkflowStatusDesc(String workflowStatusDesc) {
		this.workflowStatusDesc = workflowStatusDesc;
	}

	public Date getTaskEndDateEnd() {
		return taskEndDateEnd;
	}

	public void setTaskEndDateEnd(Date taskEndDateEnd) {
		this.taskEndDateEnd = taskEndDateEnd;
	}

	public Date getTaskEndDateStart() {
		return taskEndDateStart;
	}

	public void setTaskEndDateStart(Date taskEndDateStart) {
		this.taskEndDateStart = taskEndDateStart;
	}

	public Date getTaskStartDateEnd() {
		return taskStartDateEnd;
	}

	public void setTaskStartDateEnd(Date taskStartDateEnd) {
		this.taskStartDateEnd = taskStartDateEnd;
	}

	public Date getTaskStartDateStart() {
		return taskStartDateStart;
	}

	public void setTaskStartDateStart(Date taskStartDateStart) {
		this.taskStartDateStart = taskStartDateStart;
	}

	public String getExcludeWorkflowType() {
		return excludeWorkflowType;
	}

	public void setExcludeWorkflowType(String excludeWorkflowType) {
		this.excludeWorkflowType = excludeWorkflowType;
	}

	public String getActorOrgName() {
		return actorOrgName;
	}

	public void setActorOrgName(String actorOrgName) {
		this.actorOrgName = actorOrgName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}
}
