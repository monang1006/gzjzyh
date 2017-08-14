package com.strongit.oa.meetingmanage.meetingnotice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.strongit.oa.common.remind.Constants;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaMeetingConference;
import com.strongit.oa.bo.ToaMeetingNotice;
import com.strongit.oa.bo.ToaMeetingTopic;
import com.strongit.oa.bo.ToaMobileReplyMessage;

import com.strongit.oa.im.IMManager;
import com.strongit.oa.meetingmanage.meetingconference.MeetingconferenceManager;
import com.strongit.oa.meetingmanage.meetingtopic.MeetingtopicManager;
import com.strongit.oa.meetingmanage.util.MeetingmanageConst;
import com.strongit.oa.message.IMessageService;
import com.strongit.oa.mymail.IMailService;
import com.strongit.oa.sms.IsmsService;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.MessagesConst;
import com.strongit.tag.web.grid.stronger.FlexTableTag;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "meetingnotice.action", type = ServletActionRedirectResult.class),
		@Result(name = "notList", value = "/WEB-INF/jsp/meetingmanage/meetingsummary/meetingsummary-notList.jsp", type = ServletDispatcherResult.class),
		@Result(name = "display", value = "/WEB-INF/jsp/meetingmanage/meetingnotice/meetingnotice-display.jsp", type = ServletDispatcherResult.class) })
public class MeetingnoticeAction extends BaseActionSupport {

	public String notId; // 通知ID

	private String conId; // 会议ID
	private String conName; // 会议主题
	private String topSubject;// 议题主题

	// 附件ID
	private String attachId;

	private Map meetingMap = new HashMap();

	private Map noticeMap = new HashMap();

	private Page<ToaMeetingConference> page = new Page<ToaMeetingConference>(
			FlexTableTag.MAX_ROWS, true);// 会议PAGE

	private Page<ToaMeetingNotice> nopage = new Page<ToaMeetingNotice>(FlexTableTag.MAX_ROWS, true);// 会议通知PAGE

	private ToaMeetingNotice model = new ToaMeetingNotice();
	private ToaMeetingConference conference = new ToaMeetingConference();
	private ToaMeetingTopic topic = new ToaMeetingTopic();
	private ToaMeetingAttach attach = new ToaMeetingAttach();// 附件对象

	private MeetingconferenceManager conferenceManager;
	private MeetingnoticeManager noticeManager;
	private MeetingtopicManager topicManager;

	private List<ToaMeetingAttach> attachList;// 会议通知附件LIST
	// private List<ToaMeetingTopic> topicList; //会议议题列表
	// 查询会议字段
	private String conferenceName; // 会议主题
	private String conferenceDemo; // 会议描述
	private Date conferenceStime; // 会议开始时间
	private String conferenceAddr; // 会议地址
	private Date conferenceEndtime; // 会议结束时间
	private String conferenceStatus; // 会议状态

	// 查询会议通知字段
	private String noticeTitle; // 通知主题
	private String noticeAddr; // 通知地址
	private Date noticeStime; // 通知开始时间
	private Date noticeEndTime; // 通知结束时间
	private String noticeIs; // 通知发送状态

	// 附件
	private String attachFiles;
	// 删除记录ids
	private String delAttachIds;

	private String userName;// 开会人员姓名

	private String userId;

	private String sendtype;// 发送通知方式

	private IMessageService msgService;// 内部消息发送方式

	private IMailService mailService;// 邮件发送方式

	private IsmsService smsService;// 手机短信发送方式

	private IMManager rtxService;// rtx发送方式

	/**
	 * 上传属性设置
	 */
	private File[] file;
	private String[] fileName;

	public MeetingnoticeAction() {
		noticeMap.put("0", "未发送");
		noticeMap.put("1", "已发送");
		noticeMap.put("2", "会议结束");
	}

	public String notitle() throws Exception {

		String notitle = "";
		notitle = noticeManager.getConnotice(notId).getNoticeTitle();
		renderText(notitle);
		return null;
	}

	/**
	 * 得到可以写会议记录的会议通知列表（即已发送状态的会议通知）
	 * 
	 * @author 申仪玲
	 *@date
	 * @return
	 * @throws Exception
	 */
	public String notList() throws Exception {
		this.noticeIs = "1";
		nopage = noticeManager.queryConnotice(nopage, noticeTitle, noticeAddr,
				noticeStime, noticeEndTime, noticeIs);
		return "notList";
	}

	@Override
	public String delete() throws Exception {
		try {
			noticeManager.deleteNotices(notId);
			addActionMessage("删除成功");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}

		return list();

	}

	@Override
	public String input() throws Exception {
		this.prepareModel();
		return INPUT;
	}
	public String addNew() throws Exception {
		this.prepareModel();
		conference = conferenceManager.getoneConference(conId);
		return "addNew";
	}

	@Override
	public String list() throws Exception {
		this.conferenceStatus = "0";
		nopage = noticeManager.queryConnotice(nopage, noticeTitle, noticeAddr,
				noticeStime, noticeEndTime, noticeIs);
		if(nopage.getResult() != null){
			for(int j=0;j<nopage.getResult().size();j++){
				String noticeTitle = nopage.getResult().get(j).getNoticeTitle();
				noticeTitle = noticeTitle.replaceAll("\r", "");//处理审批意见有回车的情况
				noticeTitle = noticeTitle.replaceAll("\n", " ");//处理审批意见有换行的情况
				String[] FF_String = new String[]{"\'","\"","<",">","\\\\"};//特殊字符
				String[] NFF_String = new String[]{"’","”", "＜","＞","＼"};//替换字符
				for(int i=0;i<FF_String.length;i++){
					if(noticeTitle.indexOf(FF_String[i])!=-1){
						noticeTitle=noticeTitle.replaceAll(FF_String[i], NFF_String[i]);
					}
				}
				nopage.getResult().get(j).setNoticeTitle(noticeTitle);
			}
		}
		
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (notId != null && !"".equals(notId)) {
			model = noticeManager.getConnotice(notId);
			conId = model.getConferenceId();

			if (conId != null && !"".equals(conId)) {
				conference = conferenceManager.getoneConference(conId);
				this.conId = conference.getConferenceId();
				this.conName = conference.getConferenceName();
				List<ToaMeetingTopic> topicopp = noticeManager
						.getContopicByConId(conId);
				if (topicopp.size() != 0 && topicopp != null) {
					/*
					 * topic = topicManager.getoneTopic(conId); this.topSubject
					 * = topic.getTopicSubject();
					 */
					Iterator<ToaMeetingTopic> it = topicopp.iterator();
					StringBuffer topSubjects = new StringBuffer();
					while (it.hasNext()) {
						topic = (ToaMeetingTopic) it.next();
						topSubjects.append(topic.getTopicSubject());
						topSubjects.append(",");
						this.topSubject = topSubjects.toString();
					}

				} else {
					this.topSubject = "";
				}

			} else {
				this.conId = "";
				this.conName = "";
			}
			userId = model.getNoticeUsers();
			if (model.getNoticeUsers() != null
					&& !model.getNoticeUsers().equals("")) {
				String[] userIds = model.getNoticeUsers().split(",");
				StringBuffer userNameInfo = new StringBuffer("");
				for (int i = 0; i < userIds.length; i++) {
					userNameInfo.append(noticeManager.getUserName(userIds[i]))
							.append(",");
				}
				userName = userNameInfo.toString();
			}

			String id = model.getNoticeId();
			attachList = noticeManager.getNoticeAttaches(id);
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

			model = new ToaMeetingNotice();

		}
	}
	//查看通知
  public String view()throws Exception{
		if (notId != null && !"".equals(notId)) {
			model = noticeManager.getConnotice(notId);
			conId = model.getConferenceId();
			if (conId != null && !"".equals(conId)) {
				conference = conferenceManager.getoneConference(conId);
				this.conId = conference.getConferenceId();
				this.conName = conference.getConferenceName();
				List<ToaMeetingTopic> topicopp = noticeManager
						.getContopicByConId(conId);
				if (topicopp.size() != 0 && topicopp != null) {
					/*
					 * topic = topicManager.getoneTopic(conId); this.topSubject
					 * = topic.getTopicSubject();
					 */
					Iterator<ToaMeetingTopic> it = topicopp.iterator();
					StringBuffer topSubjects = new StringBuffer();
					while (it.hasNext()) {
						topic = (ToaMeetingTopic) it.next();
						topSubjects.append(topic.getTopicSubject());
						topSubjects.append(",");
						this.topSubject = topSubjects.toString();
					}

				} else {
					this.topSubject = "";
				}

			} else {
				this.conId = "";
				this.conName = "";
			}
			userId = model.getNoticeUsers();
			if (model.getNoticeUsers() != null
					&& !model.getNoticeUsers().equals("")) {
				String[] userIds = model.getNoticeUsers().split(",");
				StringBuffer userNameInfo = new StringBuffer("");
				for (int i = 0; i < userIds.length; i++) {
					userNameInfo.append(noticeManager.getUserName(userIds[i]))
							.append(",");
				}
				userName = userNameInfo.toString();
			}

			String id = model.getNoticeId();
			attachList = noticeManager.getNoticeAttaches(id);
			if (attachList != null) {
				Iterator it = attachList.iterator();
				attachFiles = "";
				while (it.hasNext()) {
					ToaMeetingAttach att = (ToaMeetingAttach) it.next();

					attachFiles += "<div id="
							+ att.getAttachId()
							+ " style=\"display: \">"
							+ "<a href=\"javascript:download('"
							+ att.getAttachId() + "')\">" + att.getAttachName()
							+ "</a>&nbsp;</div>";
				}
			}
		}
		return "view";
  }
	@Override
	public String save() throws Exception {
		// model = this.getModel();
		if (delAttachIds != null && !"".equals(delAttachIds)) {
			noticeManager.deleteAttaches(delAttachIds);
		}

		if ("".equals(model.getNoticeId())) {
			model.setNoticeId(null);
		}

		model.setNoticeIs("0");// 初始化通知状态为未发送
		model.setNoticeUsers(userId);
		noticeManager.saveConnotice(model);
		// 将原先关联的会议状态改为未开始
		if (model.getConferenceId() != null
				&& !"".equals(model.getConferenceId())) {
			conference = conferenceManager.getoneConference(model
					.getConferenceId());
			conference.setConferenceStatus("0");
			conferenceManager.updateCon(conference);
		}
		// 写入现在关联的会议
		if (conId != null && !"".equals(conId)) {
			model.setConferenceId(conId);
			model.setNoticeIs("0");// 初始化通知状态为未发送
			model.setNoticeUsers(userId);
			noticeManager.updateConnotice(model);
			conference = conferenceManager.getoneConference(conId);
			conference.setConferenceStatus("1");
			conferenceManager.updateCon(conference);
		}
		// 保存附件
		if (file != null) {
			for (int i = 0; i < file.length; i++) {

				ToaMeetingAttach attach = new ToaMeetingAttach();
				attach.setMeetingNotice(model.getNoticeId());
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
				noticeManager.saveNoticeattach(attach);
			}
		}

		return renderHtml("<script>window.dialogArguments.location='"
				+ getRequest().getContextPath()
				+ "/meetingmanage/meetingnotice/meetingnotice.action';window.close();</script>");
	}
	
	
	

	/**
	 * 下载附件
	 * 
	 * @author 漆宝华
	 *@date 2011-8-2 上午09:39:55
	 * @return
	 */
	public String down() throws Exception {
		HttpServletResponse response = super.getResponse();
		ToaMeetingAttach file = noticeManager.getNoticeAttache(attachId);
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
	 * 保存并发送会议通知
	 * 
	 * @author 蒋国斌
	 *@date 2009-4-28 上午10:39:55
	 * @return
	 */
	public String send() throws Exception {

		if (notId != null && !"".equals(notId)) {

			if (conId != null && !"".equals(conId)) {
				model = noticeManager.getConnotice(notId);
				model.setNoticeIs("1");// 更改通知状态为已发送
				model.setNoticeUsers(userId);
				model.setConferenceId(conId);
				noticeManager.updateConnotice(model);
				conference = conferenceManager.getoneConference(conId);
				conference.setConferenceStatus("1");
				conferenceManager.updateCon(conference);
			} else {
				model = noticeManager.getConnotice(notId);
				model.setNoticeIs("1");// 更改通知状态为已发送
				model.setNoticeUsers(userId);
				noticeManager.updateConnotice(model);
			}

		} else {
			model = this.getModel();
			if (delAttachIds != null && !"".equals(delAttachIds)) {
				noticeManager.deleteAttaches(delAttachIds);
			}

			if ("".equals(model.getConferenceId())) {
				model.setConferenceId(null);
			}
			if (conId != null && !"".equals(conId)) {
				model.setNoticeIs("1");// 更改通知状态为已发送
				model.setNoticeUsers(userId);
				model.setConferenceId(conId);
				noticeManager.saveConnotice(model);
				conference = conferenceManager.getoneConference(conId);
				conference.setConferenceStatus("1");
				conferenceManager.updateCon(conference);
			} else {
				model.setNoticeIs("1");// 更改通知状态为已发送
				model.setNoticeUsers(userId);
				noticeManager.saveConnotice(model);
			}
		}

		// 保存附件
		if (file != null) {
			for (int i = 0; i < file.length; i++) {

				ToaMeetingAttach attach = new ToaMeetingAttach();
				attach.setMeetingNotice(model.getNoticeId());
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
				noticeManager.saveNoticeattach(attach);
			}
		}

		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String con = "开会时间:" + df.format(model.getNoticeStime()) + "---"
					+ df.format(model.getNoticeEndTime()) + " 开会地点："
					+ model.getNoticeAddr();
			final String title = model.getNoticeTitle();
			String s[] = sendtype.split(",");
			for (int k = 0; k < s.length; k++) {
				String mth = s[k];
				final List<String> users = new ArrayList<String>();
				String myusers = model.getNoticeUsers();
				String[] userids = myusers.split(",");
				for (int i = 0; i < userids.length; i++) {
					users.add(userids[i]);
				}

				if (Constants.MSG.equals(mth)) {
					msgService.sendMsg("system", users, "开会通知：" + title, con,
							GlobalBaseData.MSG_MEETING);
				}
				if (Constants.EMAIL.equals(mth)) {
					mailService.autoSendMail(users, "开会通知：" + title, con,
							"system");
				}
				if (Constants.SMS.equals(mth)) {
					String smsCon = "您收到消息(" + title + "" + con + ")，回复<"
							+ ToaMobileReplyMessage.TYPE_MESSAGE + "1>同意"
							+ ",回复<" + ToaMobileReplyMessage.TYPE_MESSAGE
							+ "0>不同意";
					smsService.sendSms("system", users, smsCon,
							GlobalBaseData.SMSCODE_MEETING);
				}
				if (Constants.RTX.equals(mth)) {
					StringBuffer mess = new StringBuffer();
					mess.append("开会通知：");
					mess.append(title + " \n");
					mess.append(con + "\n");
					String rtxMess = mess.toString();
					rtxService.sendIM(rtxMess, myusers,null);
				}
			}

		} catch (Exception e) {
			System.out.print(e);
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "开会通知提醒" });
		}

		return renderHtml("<script>window.dialogArguments.location='"
				+ getRequest().getContextPath()
				+ "/meetingmanage/meetingnotice/meetingnotice.action';window.close();</script>");
	}

	/**
	 * 显示查看会议通知详情
	 * 
	 * @author 申仪玲
	 *@date
	 * @return
	 * @throws Exception
	 */
	public String display() throws Exception {

		if (conId != null && !"".equals(conId)) {
			
			List<ToaMeetingNotice> mylist=noticeManager.getConnoticeByConId(conId);
			if(mylist!=null && mylist.size()>0){

			notId =mylist.get(0).getConferenceId();

			model =mylist.get(0);

			userId = model.getNoticeUsers();
			if (model.getNoticeUsers() != null
					&& !model.getNoticeUsers().equals("")) {
				String[] userIds = model.getNoticeUsers().split(",");
				StringBuffer userNameInfo = new StringBuffer("");
				for (int i = 0; i < userIds.length; i++) {
					userNameInfo.append(noticeManager.getUserName(userIds[i]))
							.append(",");
				}
				userName = userNameInfo.toString();
			}

			String id = model.getNoticeId();
			attachList = noticeManager.getNoticeAttaches(id);
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

			conference = conferenceManager.getoneConference(conId);
			this.conId = conference.getConferenceId();
			this.conName = conference.getConferenceName();
			List<ToaMeetingTopic> topics = noticeManager
					.getContopicByConId(conId);
			if (topics.size() != 0 && topics != null) {
				/*
				 * topic = topicManager.getoneTopic(conId); this.topSubject =
				 * topic.getTopicSubject();
				 */
				Iterator<ToaMeetingTopic> it = topics.iterator();
				StringBuffer topSubjects = new StringBuffer();
				while (it.hasNext()) {
					topic = (ToaMeetingTopic) it.next();
					topSubjects.append(topic.getTopicSubject());
					topSubjects.append(",");
					this.topSubject = topSubjects.toString();
				}

			} else {
				this.topSubject = "";
			}
			}
		} else {
			model = new ToaMeetingNotice();
		}

		return "display";
	}

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}

	public String getTopSubject() {
		return topSubject;
	}

	public void setTopSubject(String topSubject) {
		this.topSubject = topSubject;
	}

	public Map getMeetingMap() {
		return meetingMap;
	}

	public void setMeetingMap(Map meetingMap) {
		this.meetingMap = meetingMap;
	}

	public Page<ToaMeetingConference> getPage() {
		return page;
	}

	public void setPage(Page<ToaMeetingConference> page) {
		this.page = page;
	}

	public MeetingconferenceManager getConferenceManager() {
		return conferenceManager;
	}

	@Autowired
	public void setConferenceManager(MeetingconferenceManager conferenceManager) {
		this.conferenceManager = conferenceManager;
	}

	public MeetingnoticeManager getNoticeManager() {
		return noticeManager;
	}

	@Autowired
	public void setNoticeManager(MeetingnoticeManager noticeManager) {
		this.noticeManager = noticeManager;
	}

	public List<ToaMeetingAttach> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<ToaMeetingAttach> attachList) {
		this.attachList = attachList;
	}

	/*
	 * public List<ToaMeetingTopic> getTopicList() { return topicList; }
	 * 
	 * public void setTopicList(List<ToaMeetingTopic> topicList) {
	 * this.topicList = topicList; }
	 */
	public ToaMeetingNotice getModel() {
		return model;
	}

	public void setModel(ToaMeetingNotice model) {
		this.model = model;
	}

	public Page<ToaMeetingNotice> getNopage() {
		return nopage;
	}

	public void setNopage(Page<ToaMeetingNotice> nopage) {
		this.nopage = nopage;
	}

	public Map getNoticeMap() {
		return noticeMap;
	}

	public void setNoticeMap(Map noticeMap) {
		this.noticeMap = noticeMap;
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

	public String getSendtype() {
		return sendtype;
	}

	public void setSendtype(String sendtype) {
		this.sendtype = sendtype;
	}

	public IMailService getMailService() {
		return mailService;
	}

	@Autowired
	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	public IMessageService getMsgService() {
		return msgService;
	}

	@Autowired
	public void setMsgService(IMessageService msgService) {
		this.msgService = msgService;
	}

	public IsmsService getSmsService() {
		return smsService;
	}

	@Autowired
	public void setSmsService(IsmsService smsService) {
		this.smsService = smsService;
	}

	public ToaMeetingConference getConference() {
		return conference;
	}

	public void setConference(ToaMeetingConference conference) {
		this.conference = conference;
	}

	public IMManager getRtxService() {
		return rtxService;
	}

	@Autowired
	public void setRtxService(IMManager rtxService) {
		this.rtxService = rtxService;
	}

	public String getConferenceName() {
		return conferenceName;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	public String getConferenceDemo() {
		return conferenceDemo;
	}

	public void setConferenceDemo(String conferenceDemo) {
		this.conferenceDemo = conferenceDemo;
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

	public String getConferenceStatus() {
		return conferenceStatus;
	}

	public void setConferenceStatus(String conferenceStatus) {
		this.conferenceStatus = conferenceStatus;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeAddr() {
		return noticeAddr;
	}

	public void setNoticeAddr(String noticeAddr) {
		this.noticeAddr = noticeAddr;
	}

	public String getNoticeIs() {
		return noticeIs;
	}

	public void setNoticeIs(String noticeIs) {
		this.noticeIs = noticeIs;
	}

	public Date getNoticeStime() {
		return noticeStime;
	}

	public void setNoticeStime(Date noticeStime) {
		this.noticeStime = noticeStime;
	}

	public Date getNoticeEndTime() {
		return noticeEndTime;
	}

	public void setNoticeEndTime(Date noticeEndTime) {
		this.noticeEndTime = noticeEndTime;
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

	public void setUpload(File[] file) {
		this.file = file;
	}

	public void setUploadFileName(String[] fileName) {
		this.fileName = fileName;
	}

	public String getNotId() {
		return notId;
	}

	public void setNotId(String notId) {
		this.notId = notId;
	}

	public String getConName() {
		return conName;
	}

	public void setConName(String conName) {
		this.conName = conName;
	}

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public MeetingtopicManager getTopicManager() {
		return topicManager;
	}

	public void setTopicManager(MeetingtopicManager topicManager) {
		this.topicManager = topicManager;
	}

	public ToaMeetingAttach getAttach() {
		return attach;
	}

	public void setAttach(ToaMeetingAttach attach) {
		this.attach = attach;
	}

}