package com.strongit.oa.meetingmanage.meetingaudit;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaMeetingTopic;
import com.strongit.oa.bo.ToaMeetingTopicsort;
import com.strongit.oa.bo.ToaSysmanageDict;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;

import com.strongit.oa.meetingmanage.meetingtopic.MeetingtopicManager;
import com.strongit.oa.meetingmanage.meetingtopicsort.MeetingtopicsortManager;
import com.strongit.oa.util.OALogInfo;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "meetingaudit.action", type = ServletActionRedirectResult.class),
		@Result(name = "displayview", value = "/WEB-INF/jsp/meetingmanage/meetingaudit/meetingaudit-displayview.jsp", type = ServletDispatcherResult.class) })
public class MeetingauditAction extends AbstractBaseWorkflowAction {

	// private Page<ToaMeetingTopic> page = new Page<ToaMeetingTopic>(10, true);
	private MeetingtopicManager topicManager;

	private MeetingtopicsortManager meetingtopicsortManager;// 议题分类业务类

	private List<ToaMeetingTopicsort> sortList;// 议题分类LIST

	private List<ToaMeetingAttach> attachList;// 附件LIST

	// 附件
	private String attachFiles;

	private String topId;// 议题ID

	private ToaMeetingTopic model = null;// 已送审议题BO

	private String topicName; // 议题标题

	private MeetingtopicAuditManager auditmanager;// 工作流处理manager

	public MeetingtopicManager getTopicManager() {
		return topicManager;
	}

	@Autowired
	public void setTopicManager(MeetingtopicManager topicManager) {
		this.topicManager = topicManager;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO 自动生成方法存根
		return null;
	}
	
	public MeetingauditAction(){
		this.formId = "0";
	}
	
	/**
	 * 查看议题信息
	 * 
	 * @author 蒋国斌
	 * @date 2009-5-13 下午02:36:02
	 * @return
	 * @throws Exception
	 */
	public String displayview() throws Exception {
		String[] info = auditmanager.getFormIdAndBussinessIdByTaskId(taskId);
		topId = info[0];
		if (topId != null) {
			model = topicManager.getoneTopic(topId);
			attachList = topicManager.getTopicAttaches(topId);
			if (attachList != null) {
				Iterator it = attachList.iterator();
				attachFiles = "";
				while (it.hasNext()) {
					ToaMeetingAttach att = (ToaMeetingAttach) it.next();

					attachFiles += "<div id=" + att.getAttachId()
							+ " style=\"display: \">"
							+ "<a href=\"javascript:download('"
							+ att.getAttachId() + "')\">" + att.getAttachName()
							+ "</a>&nbsp;</div>";
				}
			}
		}
		sortList = meetingtopicsortManager.getTopicsorts();
		return "displayview";
	}
	
	/**
	 * 转交下一步处理
	 * @return
	 */
	/**
	public String nextstep() throws Exception {
		try {
			User user=userService.getCurrentUser();
			ideaLst=approvalSuggestionService.getAppSuggestionListByUserId(user.getUserId(), new OALogInfo("获取当前用户审批意见列表"));
		
			model = topicManager.getoneTopic(topId);
			workflowName = model.getTopicsort().getProcessName();
			bussinessId = model.getTopicId();
			businessName = model.getTopicSubject();
			if(businessName!=null){
				businessName=businessName.replace("\\r\\n", " ");
			}
			if(businessName!=null){
				businessName=businessName.replace("\\n", " ");
			}
			userName=user.getUserName();
		} catch (Exception e) {
			logger.error("转到下一步时发生异常", e);
		} 
		
		return "tonextstep";
	}
	**/
	public String back() throws Exception {
		String ret = "0";
		try{
			if(StringUtils.hasLength(taskId)){
				//JSONObject jsonObj = JSONObject.fromObject(suggestion);
				//String content = jsonObj.getString("suggestion");
				//remindType = jsonObj.getString("remindType");
//				将提醒(方式|内容)存储在session中
				String content="";
				ActionContext cxt = ActionContext.getContext();
				cxt.getSession().put("remindType", remindType);
				cxt.getSession().put("handlerMes", content);
				cxt.getSession().put("moduleType", getModuleType());//调用消息模块的类型
				String returnNodeId = getRequest().getParameter("returnNodeId");
				String curUserId = adapterBaseWorkflowManager.getUserService().getCurrentUser().getUserId();
				getManager().backSpace(taskId, returnNodeId, formId, content,curUserId, formData);
			}else{
				ret = "-1";
			}
		}catch(SystemException e){
			logger.error(e.getMessage());
			ret = "-3";
		}catch(Exception ex){
			logger.error("退回任务时出现异常,异常信息：" + ex.getMessage());
			ret = "-2";
		}
		return this.renderText(ret);
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

	public ToaMeetingTopic getModel() {
		return this.model;
	}


	public String getTopId() {
		return topId;
	}

	public void setTopId(String topId) {
		this.topId = topId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}


	public List<ToaMeetingAttach> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<ToaMeetingAttach> attachList) {
		this.attachList = attachList;
	}

	public List<ToaMeetingTopicsort> getSortList() {
		return sortList;
	}

	public void setSortList(List<ToaMeetingTopicsort> sortList) {
		this.sortList = sortList;
	}

	public String getAttachFiles() {
		return attachFiles;
	}

	public void setAttachFiles(String attachFiles) {
		this.attachFiles = attachFiles;
	}

	@Autowired
	public void setMeetingtopicsortManager(
			MeetingtopicsortManager meetingtopicsortManager) {
		this.meetingtopicsortManager = meetingtopicsortManager;
	}

	public void setModel(ToaMeetingTopic model) {
		this.model = model;
	}

	public MeetingtopicAuditManager getAuditmanager() {
		return auditmanager;
	}

	@Autowired
	public void setAuditmanager(MeetingtopicAuditManager auditmanager) {
		this.auditmanager = auditmanager;
	}

	@Override
	protected String getDictType() {
		// TODO Auto-generated method stub
		return WorkFlowTypeConst.MEETINGIDEA;
	}

	public void setFormId(String formId){
		super.formId = "0";
	}

	@Override
	protected MeetingtopicAuditManager getManager() {
		// TODO Auto-generated method stub
		return  auditmanager;
	}

	@Override
	protected String getModuleType() {
		// TODO Auto-generated method stub
		return String.valueOf(WorkFlowTypeConst.MEETING);
	}

	@Override
	protected String getWorkflowType() {
		// TODO Auto-generated method stub
		return String.valueOf(WorkFlowTypeConst.MEETING);
	}

}
