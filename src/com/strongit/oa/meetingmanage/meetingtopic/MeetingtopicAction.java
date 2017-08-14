package com.strongit.oa.meetingmanage.meetingtopic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaMeetingTopic;
import com.strongit.oa.bo.ToaMeetingTopicsort;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.meetingmanage.meetingtopicsort.MeetingtopicsortManager;
import com.strongit.oa.meetingmanage.util.MeetingmanageConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "meetingtopic.action", type = ServletActionRedirectResult.class),
		@Result(name = "display", value = "/WEB-INF/jsp/meetingmanage/meetingtopic/meetingtopic-display.jsp", type = ServletDispatcherResult.class),
		@Result(name = "topicList", value = "/WEB-INF/jsp/meetingmanage/meetingconference/meetingconference-topicList.jsp", type = ServletDispatcherResult.class),
		@Result(name = "displayview", value = "/WEB-INF/jsp/meetingmanage/meetingtopic/meetingtopic-displayview.jsp", type = ServletDispatcherResult.class) })
public class MeetingtopicAction extends AbstractBaseWorkflowAction {

	private String topId;// 议题ID

	// 附件ID
	private String attachId;
	private Map<String, String> meetingMap = new HashMap();

	private GenericDAOHibernate<ToaMeetingTopic, String> topicDao = null;
	private Page<ToaMeetingTopic> page = new Page<ToaMeetingTopic>(FlexTableTag.MAX_ROWS, true);// 议题PAGE

	private ToaMeetingTopic model = new ToaMeetingTopic();// 议题对象

	private ToaMeetingAttach attach = new ToaMeetingAttach();// 附件对象

	private MeetingtopicManager topicManager;

	private MeetingtopicsortManager meetingtopicsortManager;

	private List<ToaMeetingTopicsort> sortList;// 议题分类LIST

	private List<ToaMeetingAttach> attachList;// 附件LIST
	// 查询字段
	private String tCode; // 议题流水号
	private String tSubject; // 议题主题
	private String tSorts; // 议题分类名称
	private Date tEstime; // 议题创建时间
	private String tStatus; // 议题状态

	/**
	 * 上传属性设置
	 */
	private File[] file;
	private String[] fileName;

	private String topicName; // 议题标题
	private String taskId; // 任务ID
	private List workflowTransitionslist; // 流程流向集

	private String issave;// 是否已经保存过


	// 附件
	private String attachFiles;
	// 删除记录ids
	private String delAttachIds;



	@Autowired
	public void setTopicManager(MeetingtopicManager topicManager) {
		this.topicManager = topicManager;
	}

	public MeetingtopicAction() {

		meetingMap.put("0", "未送审");
		meetingMap.put("1", "审核中");
		meetingMap.put("2", "已审核");
		meetingMap.put("3","占用中");
		meetingMap.put("4","占用完毕");
	}

	public String topicList() throws Exception {
		
		page = topicManager.queryTopicForCon(page, tCode, tSubject, tSorts, tEstime,
				tStatus);
		return "topicList";

	}

	public String topsubject() throws Exception {
		String[] ids = topId.split(",");
		String st="";
		for (int i = 0; i < ids.length; i++) {
			StringBuffer topSubjects = new StringBuffer();
			model = topicManager.getoneTopic((ids[i]));
			//model.setTopicStatus(MeetingmanageConst.USING);
			//topicDao.save(model);
			topSubjects.append(model.getTopicSubject());
			topSubjects.append(",");
			renderText(topSubjects.toString());
			
		}
		return null;

	}

	/**
	 * 判断该议题分类下有没有挂流程
	 * 
	 * @author 蒋国斌
	 *@date 2009-4-20 上午09:20:20
	 * @return
	 * @throws Exception
	 */
	public String getProcessName() throws Exception {
		String processName = topicManager.getoneTopic(topId).getTopicsort()
				.getProcessName();
		model = topicManager.getoneTopic(topId);
		String state = model.getTopicStatus(); // 议题状态
		if (processName != null && !"".equals(processName)
				&& MeetingmanageConst.UN_AUDIT.equals(state)) { // 议题分类已挂流程并且议题未送审
			StringBuffer str = new StringBuffer(100);
			str.append(topId).append(",")

			.append(state);
			renderText(str.toString());
		} else {
			renderText("flagfalse");
		}
		return null;
	}

	/**
	 * 打开提交流程向导页面,传给页面可选流向
	 * 
	 * @author 蒋国斌
	 *@date 2009-4-20 上午09:34:16
	 * @return
	 * @throws Exception
	 */
	/**
	public String wizard() throws Exception {
		workflowName = topicManager.getoneTopic(topId).getTopicsort()
				.getProcessName();
		model = topicManager.getoneTopic(topId);
		bussinessId = model.getTopicId();
		businessName = model.getTopicSubject();
		// instanceId=model.getProcessInstanceId();
		// 获取审批意见列表
		ideaLst = topicManager.getCurrentUserDictItem(getDictType());
		// 得到发文字典类主键
		ToaSysmanageDict dict = topicManager.getDict(getDictType());
		if (dict != null) {
			dictId = dict.getDictCode();
		}
		return "wizard2";
	}

	
	/**
	 * 转交下一步处理
	 * @return
	 * 郑志斌修改  2010-11-18 获取当前用户的审批意见列表
	 */

	public String nextstep() throws Exception {
		try {
			User user=adapterBaseWorkflowManager.getUserService().getCurrentUser();
			ideaLst=adapterBaseWorkflowManager.getApprovalSuggestionService().getAppSuggestionListByUserId(user.getUserId(), new OALogInfo("获取当前用户审批意见列表"));
		
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
	
	
	/**
	 * 议题直接审核通过，针对那些没有挂流程的议题分类情况
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pubTopic() throws Exception {
		String[] id = topId.split(",");
		for (int i = 0; i < id.length; i++) {
			model = topicManager.getoneTopic(id[i]);
			model.setTopicStatus(MeetingmanageConst.ISAUDIT);// 改变议题 状态为已审核
			topicManager.updateTopic(model);
		}
		String me = this.issave;
		if (me == null) {
			// return
			// renderHtml("<script> alert('成功通过'); window.location='"+getRequest().getContextPath()+"/meetingmanage/meetingtopic/meetingtopic.action'; </script>");
			renderText("success");
		} else {
			// return
			// renderHtml("<script> alert('成功通过!');window.dialogArguments.location='"+getRequest().getContextPath()+"/meetingmanage/meetingtopic/meetingtopic.action';window.close();</script>");
			renderText("successfull");
		}
		return null;
	}

	/**
	 * 用户查看议题时显示
	 * 
	 * @return
	 */
	public String showTopic() throws Exception {
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
		return "showTopic";
	}

	@Override
	public String delete() throws Exception {
		try {
			topicManager.deleteTopics(topId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
			renderText("deletefalse");
			// return
			// renderHtml("<script> alert('删除失败，请先删除与之关联的通知。'); window.location='"
			// + getRequest().getContextPath()
			// + "/meetingmanage/meetingtopic/meetingtopic.action';</script>");
		}
		// return renderHtml("<script> window.location='"
		// + getRequest().getContextPath()
		// + "/meetingmanage/meetingtopic/meetingtopic.action';</script>");
		renderText("deletetrue");
		return null;
	}
	@Override
	public String input() throws Exception {
		this.prepareModel();

		return INPUT;
	}

	public String display() throws Exception {
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
		return "display";
	}

	public String displayview() throws Exception {
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

	@Override
	public String list() throws Exception {
		page = topicManager.queryTopic(page, tCode, tSubject, tSorts, tEstime,
				tStatus);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (topId != null && !"".equals(topId)) {
			this.issave = "2";
			model = topicManager.getoneTopic(topId);
			attachList = topicManager.getTopicAttaches(topId);
			if (attachList != null) {
				Iterator it = attachList.iterator();
				attachFiles = "";
				while (it.hasNext()) {
					ToaMeetingAttach att = (ToaMeetingAttach) it.next();

					attachFiles += "<div id="
							+ att.getAttachId()
							+ " style=\"display: \"><a href=\"javascript:delAttach('"
							+ att.getAttachId() + "')\">[删除]<a>"
							+ "<a href=\"javascript:download('"
							+ att.getAttachId() + "')\">" + att.getAttachName()
							+ "</a>&nbsp;</div>";
				}
			}
		} else {
			model = new ToaMeetingTopic();
		}
		sortList = meetingtopicsortManager.getTopicsorts();

	}

	@Override
	public String save() throws Exception {
		if (delAttachIds != null && !"".equals(delAttachIds)) {
			topicManager.deleteAttaches(delAttachIds);
		}

		if ("".equals(model.getTopicId())) {
			model.setTopicId(null);
		}
		if (issave.equals("") || issave == null || issave.equals("2")) {
			model.setTopicStatus(MeetingmanageConst.UN_AUDIT);// 初始化议题状态为未送审
			model
					.setTopicContent(HtmlUtils.htmlEscape(model
							.getTopicContent()));
			topicManager.saveTopic(model);
			this.topId = model.getTopicId();
			if (file != null) {
				for (int i = 0; i < file.length; i++) {

					ToaMeetingAttach attach = new ToaMeetingAttach();
					attach.setMeetingTopic(model.getTopicId());

					FileInputStream fis = null;
					try {
						fis = new FileInputStream(file[i]);
						byte[] buf = new byte[(int) file[i].length()];
						fis.read(buf);
						attach.setAttachCon(buf);
						attach.setAttachName(fileName[i]);
						// String ext =
						// fileName[i].substring(fileName[i].lastIndexOf(".")+1);

					} catch (Exception e) {
						if (logger.isErrorEnabled()) {
							logger.error("上传失败！" + e);
						}
						throw e;
					} finally {
						try {
							fis.close();
						} catch (IOException e) {
							if (logger.isErrorEnabled()) {
								logger.error("文件关闭失败！" + e);
							}
							throw e;
						}
					}
					topicManager.saveTopicattach(attach);
					this.issave = "1";
				}

			}
		}
		getRequest().setAttribute("topId", topId);
		return input();
	}

	public String down() throws Exception {
		HttpServletResponse response = super.getResponse();
		ToaMeetingAttach file = topicManager.getTopicAttache(attachId);
		response.reset();
		response.setContentType("application/x-download"); // windows
		OutputStream output = null;
		try {
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(file.getAttachName().getBytes("gb2312"),
							"iso8859-1"));
			output = response.getOutputStream();
			output.write(file.getAttachCon());
			output.flush();
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage(), e);
			}
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
				}
				output = null;
			}
		}
		return null;
	}

	public ToaMeetingTopic getModel() {
		return this.model;
	}

	public Page<ToaMeetingTopic> getPage() {
		return page;
	}

	public void setPage(Page<ToaMeetingTopic> page) {
		this.page = page;
	}

	public void setModel(ToaMeetingTopic model) {
		this.model = model;
	}

	public Map getMeetingMap() {
		return meetingMap;
	}

	public void setMeetingMap(Map meetingMap) {
		this.meetingMap = meetingMap;
	}

	public String getTopId() {
		return topId;
	}

	public void setTopId(String topId) {
		this.topId = topId;
	}

	public ToaMeetingAttach getAttach() {
		return attach;
	}

	public void setAttach(ToaMeetingAttach attach) {
		this.attach = attach;
	}

	public List<ToaMeetingTopicsort> getSortList() {
		return sortList;
	}

	public void setSortList(List<ToaMeetingTopicsort> sortList) {
		this.sortList = sortList;
	}

	public void setUpload(File[] file) {
		this.file = file;
	}

	public void setUploadFileName(String[] fileName) {
		this.fileName = fileName;
	}

	public List<ToaMeetingAttach> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<ToaMeetingAttach> attachList) {
		this.attachList = attachList;
	}

	public String getAttachFiles() {
		return attachFiles;
	}

	public void setAttachFiles(String attachFiles) {
		this.attachFiles = attachFiles;
	}

	public String getDelAttachIds() {
		return delAttachIds;
	}

	public void setDelAttachIds(String delAttachIds) {
		this.delAttachIds = delAttachIds;
	}

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public List getWorkflowTransitionslist() {
		return workflowTransitionslist;
	}

	public void setWorkflowTransitionslist(List workflowTransitionslist) {
		this.workflowTransitionslist = workflowTransitionslist;
	}

	@Resource
	public void setMeetingtopicsortManager(
			MeetingtopicsortManager meetingtopicsortManager) {
		this.meetingtopicsortManager = meetingtopicsortManager;
	}

	@Override
	protected String getDictType() {
		// TODO Auto-generated method stub
		return WorkFlowTypeConst.MEETINGIDEA;
	}

	@Override
	protected BaseManager getManager() {
		// TODO Auto-generated method stub
		return topicManager;
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

	public String gettCode() {
		return tCode;
	}

	public void settCode(String tCode) {
		this.tCode = tCode;
	}

	public String gettSubject() {
		return tSubject;
	}

	public void settSubject(String tSubject) {
		this.tSubject = tSubject;
	}

	public String gettSorts() {
		return tSorts;
	}

	public void settSorts(String tSorts) {
		this.tSorts = tSorts;
	}

	public Date gettEstime() {
		return tEstime;
	}

	public void settEstime(Date tEstime) {
		this.tEstime = tEstime;
	}

	public String gettStatus() {
		return tStatus;
	}

	public void settStatus(String tStatus) {
		this.tStatus = tStatus;
	}

	public String getIssave() {
		return issave;
	}

	public void setIssave(String issave) {
		this.issave = issave;
	}

	public MeetingtopicManager getTopicManager() {
		return topicManager;
	}

}
