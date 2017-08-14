package com.strongit.oa.meetingmanage.meetingconference;

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
import org.springframework.web.util.HtmlUtils;
import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaMeetingConference;
import com.strongit.oa.bo.ToaMeetingTopic;
import com.strongit.oa.meetingmanage.meetingnotice.MeetingnoticeManager;
import com.strongit.oa.meetingmanage.meetingtopic.MeetingtopicManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings({ "unchecked", "serial" })
@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "meetingconference.action", type = ServletActionRedirectResult.class),
		@Result(name = "conList",value = "/WEB-INF/jsp/meetingmanage/meetingnotice/meetingnotice-conList.jsp", type = ServletDispatcherResult.class),
		@Result(name = "display", value = "/WEB-INF/jsp/meetingmanage/meetingconference/meetingconference-display.jsp", type = ServletDispatcherResult.class)})
public class MeetingconferenceAction extends BaseActionSupport {

	// 查询字段
	private String conferenceName; // 会议主题
	private String conferenceDemo; // 会议描述
	private Date conferenceStime; // 会议开始时间
	private String conferenceAddr; // 会议地址
	private Date conferenceEndtime; // 会议结束时间
	private String conferenceStatus; // 会议状态

	private String conId; // 会议ID

	private String issummary;// 是否写了会议纪要
	private String attachId; // 附件ID
	private Map<String, String> meetingMap = new HashMap<String, String>();
	private Page<ToaMeetingConference> page = new Page<ToaMeetingConference>(
			FlexTableTag.MAX_ROWS, true);// 会议PAGE
	private ToaMeetingConference model = null; // 会议对象
	private ToaMeetingAttach attach = new ToaMeetingAttach();// 附件对象
	private MeetingconferenceManager conferenceManager;
	private MeetingtopicManager topicManager;
	private MeetingnoticeManager noticeManager;
	private List<ToaMeetingTopic> topicList;
	private ToaMeetingTopic topic = null; // 议题对象
	private String topId; // 议题ID
	private String topSubject; // 议题主题
	// 附件
	private String attachFiles;
	private List<ToaMeetingAttach> attachList;// 附件LIST
	// 删除记录
	private String delAttachIds;

	/**
	 * 上传属性设置
	 */
	private File[] file;
	private String[] fileName;

	// 注入conferenceManager
	@Resource
	public void setConferenceManager(MeetingconferenceManager conferenceManager) {
		this.conferenceManager = conferenceManager;
	}

	// 注入topicManager
	@Resource
	public void setTopicManager(MeetingtopicManager topicManager) {
		this.topicManager = topicManager;
	}
	
	@Resource
	public void setNoticeManager(MeetingnoticeManager noticeManager) {
		this.noticeManager = noticeManager;
	}

	
	public MeetingconferenceAction() {
		meetingMap.put("0", "未开始");
		meetingMap.put("1", "开会中");
		meetingMap.put("2", "已结束");
	}
	
	public String conList() throws Exception {		
		this.conferenceStatus = "0";
		page = conferenceManager.queryConference(page, conferenceName, conferenceDemo, conferenceStime, conferenceAddr, conferenceEndtime, conferenceStatus);
		return "conList";
	}

	
	public String conname() throws Exception {
		
		String conName = "";
		conName = conferenceManager.getoneConference(conId).getConferenceName();
		
		renderText(conName);

		return null;
	}

	public String topSubject() throws Exception {
		
		String topSubject = "";
		
		List<ToaMeetingTopic> topicpo = noticeManager.getContopicByConId(conId);
		ToaMeetingTopic temppo=null;
		/*for(int i=0;i<topicpo.size();i++){
			temppo=topicpo.get(i);
			String tmp="";
			tmp=temppo.getTopicSubject();
			topSubject = tmp;
		}*/
		if (topicpo != null) {
			Iterator<ToaMeetingTopic> it = topicpo.iterator();
			StringBuffer topSubjects = new StringBuffer();
		while (it.hasNext()) {
			temppo = (ToaMeetingTopic) it.next();
			topSubjects.append(temppo.getTopicSubject());
			topSubjects.append(",");
			topSubject = topSubjects.toString();
		}
		}
		renderText(topSubject);
		return null;
	}
	@Override
	public String delete() throws Exception {
		try {
			conferenceManager.deleteConference(conId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
			renderText("deletefalse");
		}
		return list();
	}

	@Override
	public String input() throws Exception {
		this.prepareModel();
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		page = conferenceManager.queryConference(page, conferenceName,
				conferenceDemo, conferenceStime, conferenceAddr,
				conferenceEndtime, conferenceStatus);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {

		if (conId != null && !"".equals(conId)) {
			model = conferenceManager.getoneConference(conId);
			topicList = topicManager.getTopicsByConId(conId); // 查询会议所对应的议题
			if (topicList != null) {
				Iterator<ToaMeetingTopic> it = topicList.iterator();
				StringBuffer topIds = new StringBuffer();
				StringBuffer topSubjects = new StringBuffer();			
				while (it.hasNext()) {
					topic = (ToaMeetingTopic) it.next();
					topIds.append(topic.getTopicId());
					topIds.append(",");
					this.topId = topIds.toString();
					topSubjects.append(topic.getTopicSubject());
					topSubjects.append(",");
					this.topSubject = topSubjects.toString();
				}
			}
			
			attachList = conferenceManager.getconferenceAttaches(conId); //查询会议所对应的附件
			if(attachList!=null){
				Iterator<ToaMeetingAttach> it=attachList.iterator();
				attachFiles="";
				while (it.hasNext()) {					
					ToaMeetingAttach att = (ToaMeetingAttach) it.next();					
					attachFiles+="<div id="+att.getAttachId()+" style=\"display: \"><a href=\"javascript:delAttach('"+att.getAttachId()+"')\">[删除]<a>" +
							"<a href=\"javascript:download('"+att.getAttachId()+"')\">"+att.getAttachName()+"</a>&nbsp;</div>";
				}
			}
		} else {
			model = new ToaMeetingConference();
		}
	}

	@Override
	public String save() throws Exception {
		model = this.getModel();
		if (delAttachIds != null && !"".equals(delAttachIds)) {
			conferenceManager.deleteAttaches(delAttachIds);
		}

		if ("".equals(model.getConferenceId())) {
			model.setConferenceId(null);
		}

		model.setConferenceStatus("0");// 初始化会议状态为未开始
		model.setConferenceContent(HtmlUtils.htmlEscape(model
				.getConferenceContent()));
		conferenceManager.saveConference(model);
		this.conId = model.getConferenceId();
		topicList = topicManager.getTopicsByConId(conId); // 查询会议所对应的议题
		//删除原来关联的议题
		if (topicList != null){
			Iterator<ToaMeetingTopic> it = topicList.iterator();
			while (it.hasNext()){
				topic = (ToaMeetingTopic) it.next();
				topic.setConferenceId(null);
				topic.setTopicStatus("2");
				topicManager.updateTopic(topic);
			}
		}
		//写入现在关联的议题
		if (topId != null && !"".equals(topId)) {
			String[] topicIds = topId.split(",");
			for (int i = 0; i < topicIds.length; i++) {
				topic = topicManager.getoneTopic(topicIds[i]);
				topic.setConferenceId(conId);
				topic.setTopicStatus("3");
				topicManager.updateTopic(topic);
			}
		}
		//保存附件
		if (file != null) {
			for (int i = 0; i < file.length; i++) {

				ToaMeetingAttach attach = new ToaMeetingAttach();
				attach.setMeetingConference(conId);
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file[i]);
					byte[] buf = new byte[(int) file[i].length()];
					fis.read(buf);
					attach.setAttachCon(buf);
					attach.setAttachName(fileName[i]);
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
				conferenceManager.saveConferenceattach(attach);
			}
		}

		return renderHtml("<script>window.dialogArguments.location='"
				+ getRequest().getContextPath()
				+ "/meetingmanage/meetingconference/meetingconference.action'; window.close();</script>");
	}
	
	/*
	 *下载附件
	 * 
	 */
	public String down() throws Exception {
		HttpServletResponse response = super.getResponse();
		ToaMeetingAttach file = conferenceManager.getconferenceAttache(attachId);
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
	
	/**
	 * @author 申仪玲
	 * 查看会议 
	 */
	
	public String display()throws Exception{
		if(conId != null){
			model = conferenceManager.getoneConference(conId);
			topicList = topicManager.getTopicsByConId(conId); // 查询会议所对应的议题
			if (topicList != null) {
				Iterator<ToaMeetingTopic> it = topicList.iterator();
				StringBuffer topIds = new StringBuffer();
				StringBuffer topSubjects = new StringBuffer();			
				while (it.hasNext()) {
					topic = (ToaMeetingTopic) it.next();
					topIds.append(topic.getTopicId());
					topIds.append(",");
					this.topId = topIds.toString();
					topSubjects.append(topic.getTopicSubject());
					topSubjects.append(",");
					this.topSubject = topSubjects.toString();
				}
			}

			attachList = conferenceManager.getconferenceAttaches(conId);
			if(attachList!=null){
				Iterator it=attachList.iterator();
				attachFiles="";
				while (it.hasNext()) {
					ToaMeetingAttach att = (ToaMeetingAttach) it.next();
					
					attachFiles+="<div id="+att.getAttachId()+" style=\"display: \">" +
					"<a href=\"javascript:download('"+att.getAttachId()+"')\">"+att.getAttachName()+"</a>&nbsp;</div>";
				}
			}
			
		}
		return "display";
		
	}
	

	public ToaMeetingConference getModel() {

		return this.model;
	}

	public void setModel(ToaMeetingConference model) {
		this.model = model;
	}

	public Map<String, String> getMeetingMap() {
		return meetingMap;
	}

	public void setMeetingMap(Map<String, String> meetingMap) {
		this.meetingMap = meetingMap;
	}

	public Page<ToaMeetingConference> getPage() {
		return page;
	}

	public void setPage(Page<ToaMeetingConference> page) {
		this.page = page;
	}

	public String getConferenceName() {
		return conferenceName;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	public Date getConferenceStime() {
		return conferenceStime;
	}

	public void setConferenceStime(Date conferenceStime) {
		this.conferenceStime = conferenceStime;
	}

	public String getConferenceAddr() {
		return conferenceAddr;
	}

	public void setConferenceAddr(String conferenceAddr) {
		this.conferenceAddr = conferenceAddr;
	}

	public Date getConferenceEndtime() {
		return conferenceEndtime;
	}

	public void setConferenceEndtime(Date conferenceEndtime) {
		this.conferenceEndtime = conferenceEndtime;
	}

	public String getConferenceDemo() {
		return conferenceDemo;
	}

	public void setConferenceDemo(String conferenceDemo) {
		this.conferenceDemo = conferenceDemo;
	}

	public String getConferenceStatus() {
		return conferenceStatus;
	}

	public void setConferenceStatus(String conferenceStatus) {
		this.conferenceStatus = conferenceStatus;
	}

	public void setIssummary(String issummary) {
		this.issummary = issummary;
	}

	public String getIssummary() {
		return issummary;
	}

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public ToaMeetingAttach getAttach() {
		return attach;
	}

	public void setAttach(ToaMeetingAttach attach) {
		this.attach = attach;
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

	public List<ToaMeetingTopic> getTopicList() {
		return topicList;
	}

	public void setTopicList(List<ToaMeetingTopic> topicList) {
		this.topicList = topicList;
	}

	public ToaMeetingTopic getTopic() {
		return topic;
	}

	public void setTopic(ToaMeetingTopic topic) {
		this.topic = topic;
	}

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}

	public String getTopId() {
		return topId;
	}

	public void setTopId(String topId) {
		this.topId = topId;
	}

	public String getTopSubject() {
		return topSubject;
	}

	public void setTopSubject(String topSubject) {
		this.topSubject = topSubject;
	}
	
	public void setUpload(File[] file) {
		this.file = file;
	}

	public void setUploadFileName(String[] fileName) {
		this.fileName = fileName;
	}

}
