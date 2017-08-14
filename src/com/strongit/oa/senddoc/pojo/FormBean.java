package com.strongit.oa.senddoc.pojo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.strongmvc.orm.hibernate.Page;

public class FormBean implements Serializable{

	private static final long serialVersionUID = -8130753353005273456L;

	private String strTaskActors;  //字符格式的处理人信息("人员ID|节点ID,人员ID|节点ID...")
	private String isSelectOtherActors;  //是否可以选择其他处理人
	private String maxTaskActors;  //任务接点可选择最大处理人数目
	private String docId;  //公文ID
	private String docTitle;  //公文标题
	private String formId;  //表单ID
	private String formData;  //表单数据
	private String taskId;  //任务ID
	private String userId;  //用户ID
	private String workflowName;  //流程名称
	private String nodeId;  //任务节点ID
	private String nodeName;  //任务节点名称
	private String transitionId;  //流程流向ID
	private String transitionName;  //流程流向名称
	private String concurrentTrans;  //并发流向("流向ID1,流向ID2,...")
	private String isStartWorkflow;  //是否是启动流程
	private String suggestion;  //审批意见
	private String listMode;  //列表模式，使用四个常量标识
	private String workflowType;  //工作流类型
	private String tableName;  //表单对应主表名称
	private String pkFieldName;  //表单对应主键名称
	public static final int DRAFT_LIST = 0;  //草稿列表
	public static final int TODO_LIST = 1;  //待办列表
	public static final int HOSTED_BY_LIST = 2;  //主办列表
	public static final int PROCESSED_LIST = 3;  //已办列表
	private String multiIds;  //多个ID
	private File wordDoc;  //Word文档
	private String wordDocFileName;
	private String wordDocContentType;
	private String searchTitle;  //搜索标题
	private String searchDraftDate;  //搜索时间
	private String bussinessId;  //业务ID,(表名,主键字段名,主键值)
	private String infoSet;  //信息表名称
		
	public FormBean() {
		
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getListMode() {
		return listMode;
	}

	public void setListMode(String listMode) {
		this.listMode = listMode;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}
	
	public String getSearchTitle() {
		return searchTitle;
	}

	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}

	public String getSearchDraftDate() {
		return searchDraftDate;
	}

	public void setSearchDraftDate(String searchDraftDate) {
		this.searchDraftDate = searchDraftDate;
	}
	
	public String getMultiIds() {
		return multiIds;
	}

	public void setMultiIds(String multiIds) {
		this.multiIds = multiIds;
	}

	public File getWordDoc() {
		return wordDoc;
	}

	public void setWordDoc(File wordDoc) {
		this.wordDoc = wordDoc;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	public String getIsSelectOtherActors() {
		return isSelectOtherActors;
	}

	public void setIsSelectOtherActors(String isSelectOtherActors) {
		this.isSelectOtherActors = isSelectOtherActors;
	}

	public String getMaxTaskActors() {
		return maxTaskActors;
	}

	public void setMaxTaskActors(String maxTaskActors) {
		this.maxTaskActors = maxTaskActors;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getTransitionId() {
		return transitionId;
	}

	public void setTransitionId(String transitionId) {
		this.transitionId = transitionId;
	}

	public String getConcurrentTrans() {
		return concurrentTrans;
	}

	public void setConcurrentTrans(String concurrentTrans) {
		this.concurrentTrans = concurrentTrans;
	}
	
	public String getStrTaskActors() {
		return strTaskActors;
	}

	public void setStrTaskActors(String strTaskActors) {
		this.strTaskActors = strTaskActors;
	}
	
	public String getTransitionName() {
		return transitionName;
	}

	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}

	public String getIsStartWorkflow() {
		return isStartWorkflow;
	}

	public void setIsStartWorkflow(String isStartWorkflow) {
		this.isStartWorkflow = isStartWorkflow;
	}
	
	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}
	
	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
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

	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}

	public String getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}

	public String getInfoSet() {
		return infoSet;
	}

	public void setInfoSet(String infoSet) {
		this.infoSet = infoSet;
	}

	public String getWordDocFileName() {
		return wordDocFileName;
	}

	public void setWordDocFileName(String wordDocFileName) {
		this.wordDocFileName = wordDocFileName;
	}

	public String getWordDocContentType() {
		return wordDocContentType;
	}

	public void setWordDocContentType(String wordDocContentType) {
		this.wordDocContentType = wordDocContentType;
	}
}
