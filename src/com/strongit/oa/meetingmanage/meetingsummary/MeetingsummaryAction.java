package com.strongit.oa.meetingmanage.meetingsummary;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaMeetingAttendance;
import com.strongit.oa.bo.ToaMeetingConference;
import com.strongit.oa.bo.ToaMeetingNotice;
import com.strongit.oa.bo.ToaMeetingSummary;
import com.strongit.oa.bo.ToaMeetingTodo;
import com.strongit.oa.bo.ToaMeetingTopic;
import com.strongit.oa.meetingmanage.meetingconference.MeetingconferenceManager;
import com.strongit.oa.meetingmanage.meetingnotice.MeetingnoticeManager;
import com.strongit.oa.meetingmanage.meetingtopic.MeetingtopicManager;
import com.strongit.oa.meetingmanage.util.MeetingmanageConst;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "meetingsummary.action", type = ServletActionRedirectResult.class),
		@Result(name = "display", value = "/WEB-INF/jsp/meetingmanage/meetingsummary/meetingsummary-display.jsp", type = ServletDispatcherResult.class) })
public class MeetingsummaryAction extends BaseActionSupport {

	private String sumId;// 纪要ID

	private String conId; // 会议ID

	private String notId; // 通知ID

	private String todoparams;// 待办工作所有参数

	private String atenparams;// 考勤情况参数

	private String userName;

	private Page<ToaMeetingConference> page = new Page<ToaMeetingConference>(
			FlexTableTag.MAX_ROWS, true);
	private Page<ToaMeetingNotice> nopage = new Page<ToaMeetingNotice>(FlexTableTag.MAX_ROWS, true);
	private Page<ToaMeetingSummary> sumpage = new Page<ToaMeetingSummary>(FlexTableTag.MAX_ROWS,
			true);

	private List<ToaMeetingTopic> topicList;

	private Map guidangMap = new HashMap();

	private ToaMeetingAttach attach = new ToaMeetingAttach();

	private ToaMeetingSummary model = new ToaMeetingSummary();

	private ToaMeetingNotice notice = new ToaMeetingNotice();

	private ToaMeetingTopic topic = null; // 议题对象

	private ToaMeetingAttendance attendance = new ToaMeetingAttendance();

	private ToaMeetingConference conference = new ToaMeetingConference();

	private MeetingtopicManager topicManager;

	private MeetingconferenceManager conferenceManager;

	private MeetingsummaryManager summaryManager;

	private MeetingnoticeManager noticeManager;

	// 查询会议通知字段
	private String noticeTitle; // 会议主题
	private Date noticeStime; // 会议开始时间
	private String noticeAddr; // 会议地址
	private Date noticeEndTime; // 会议结束时间
	private String noticeIs; // 会议状态

	// 查询纪要字段
	private String summtitle;// 纪要主题
	private String summaddr; // 会议地点
	private Date summtime;// 记录时间
	private String isguidang; // 是否归档

	// 附件
	private String attachFiles;

	private List<ToaMeetingAttach> attachList;

	// 删除记录ids
	private String delAttachIds;

	/**
	 * 上传属性设置
	 */
	private File[] file;
	private String[] fileName;

	public MeetingsummaryAction() {
		guidangMap.put("0", "未归档");
		guidangMap.put("1", "已归档");
	}

	@Override
	public String delete() throws Exception {

		try {
			String[] ids = sumId.split(",");
			for (int i = 0; i < ids.length; i++) {
				model = summaryManager.getConsummary(ids[i]);
				if (model != null) {
					summaryManager.deleteSummaryAttaches(ids[i]);
					summaryManager.deleteSummaryTodos(ids[i]);
					notId = model.getNoticeId();
					noticeManager.deleteAttendance(notId); // 根据会议通知ID删除考勤表下的对应记录
					notice = noticeManager.getConnotice(notId);
					notice.setNoticeIs("1"); // 将通知状态改为已发送
					noticeManager.updateConnotice(notice);
					conId = notice.getConferenceId();
					if (conId != null && !"".equals(conId)) {
						conference = conferenceManager.getoneConference(conId);
						conference.setConferenceStatus("1"); // 更改关联会议通知的会议为进行中
						conferenceManager.updateCon(conference);
						topicList = topicManager.getTopicsByConId(conId); // 查询会议所对应的议题
						if (topicList != null) {
							Iterator<ToaMeetingTopic> it = topicList.iterator();
							while (it.hasNext()) {
								topic = (ToaMeetingTopic) it.next();
								topic.setTopicStatus("3"); // 更改议题状态为占用中
								topicManager.updateTopic(topic);
							}
						}
					}
				}

				summaryManager.deleteSummarys(ids[i]);
			}
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
		}
		return renderHtml("<script> window.location='"
				+ getRequest().getContextPath()
				+ "/meetingmanage/meetingsummary/meetingsummary.action';</script>");
	}

	@Override
	public String input() throws Exception {
		this.prepareModel();
		return INPUT;
	}

	@Override
	public String list() throws Exception {
		sumpage = summaryManager.queryConsummary(sumpage, summtitle, summaddr,
				summtime, isguidang);
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (sumId != null && !"".equals(sumId)) {
			model = summaryManager.getConsummary(sumId);
			notId = model.getNoticeId();
			if (notId != null && !"".equals(notId)) {
				notice = noticeManager.getConnotice(notId);
				String[] userIds = notice.getNoticeUsers().split(",");
				StringBuffer userNameInfo = new StringBuffer("");
				for (int i = 0; i < userIds.length; i++) {
					userNameInfo.append(noticeManager.getUserName(userIds[i]))
							.append(",");
				}
				userName = userNameInfo.deleteCharAt(
						userNameInfo.toString().lastIndexOf(",")).toString();
			}

			List<ToaMeetingAttendance> dancelist = noticeManager
					.getConAttendances(notId);
			if (dancelist.size() != 0 && dancelist != null) {
				StringBuffer xx = new StringBuffer();
				for (Iterator it = dancelist.iterator(); it.hasNext();) {
					ToaMeetingAttendance to = (ToaMeetingAttendance) it.next();
					xx.append(to.getAttendanceUsername());
					xx.append("/" + to.getAttendanceFlag() + ",");
				}
				xx.deleteCharAt(xx.toString().lastIndexOf(","));
				this.atenparams = xx.toString();
			}

			List todoList = summaryManager.getSummaryTodos(sumId);
			if (todoList != null && todoList.size() != 0) {
				StringBuffer ss = new StringBuffer();
				for (Iterator it = todoList.iterator(); it.hasNext();) {
					ToaMeetingTodo to = (ToaMeetingTodo) it.next();
					if (to.getTodoTitle() != null) {
						ss.append(to.getTodoTitle());
					} else {
						ss.append("");
					}
					if (to.getTodoUsername() != null) {
						ss.append("/" + to.getTodoUsername());
					} else {
						ss.append("/" + "");
					}
					ss.append("/" + to.getTodoState() + ",");
				}
				ss.deleteCharAt(ss.toString().lastIndexOf(","));
				this.todoparams = ss.toString();
			}

			attachList = summaryManager.getSummaryAttaches(sumId);
			if (attachList != null) {
				Iterator<ToaMeetingAttach> it = attachList.iterator();
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
			model = new ToaMeetingSummary();
			model.setNoticeId(notId);
			notice = noticeManager.getConnotice(notId);
			String users = notice.getNoticeUsers();
			String[] userIds = users.split(",");
			StringBuffer userNameInfo = new StringBuffer("");
			for (int i = 0; i < userIds.length; i++) {
				userNameInfo.append(noticeManager.getUserName(userIds[i]))
						.append(",");
			}
			userNameInfo.deleteCharAt(userNameInfo.lastIndexOf(","));
			userName = userNameInfo.toString();
		}
	}

	@Override
	public String save() throws Exception {
		if ("".equals(sumId) || sumId == null) {
			model.setSummaryId(null);
		}
		if (delAttachIds != null && !"".equals(delAttachIds)) {
			summaryManager.deleteAttaches(delAttachIds);
		}
		model
				.setSummaryContent(HtmlUtils.htmlEscape(model
						.getSummaryContent()));
		model.setIsGuidang("0");// 初始化未归档
		model.setNoticeId(notId);
		summaryManager.saveConsummary(model);
		notice = noticeManager.getConnotice(notId);
		notice.setNoticeIs("2"); // 将通知状态改为会议结束
		noticeManager.updateConnotice(notice);
		conId = notice.getConferenceId();
		if (conId != null && !"".equals(conId)) {
			conference = conferenceManager.getoneConference(conId);
			conference.setConferenceStatus("2");// 更改关联会议通知的会议为已结束
			conferenceManager.updateCon(conference);
			topicList = topicManager.getTopicsByConId(conId); // 查询会议所对应的议题
			if (topicList != null) {
				Iterator<ToaMeetingTopic> it = topicList.iterator();
				while (it.hasNext()) {
					topic = (ToaMeetingTopic) it.next();
					topic.setTopicStatus("4"); // 更改议题状态为使用完毕
					topicManager.updateTopic(topic);
				}
			}
		}

		summaryManager.deleteSummaryTodos(model.getSummaryId());
		if (!this.todoparams.equals("") && this.todoparams != null) {
			String[] xx = this.todoparams.split(",");
			for (int i = 0; i < xx.length; i++) {
				String[] yy = xx[i].split("/");
				String title = yy[0];
				String sname = yy[1];
				String toState = yy[2];
				if (!"".equals(title) || !"".equals(sname)) {
					if (toState.equals("undefined"))
						toState = "0";
					ToaMeetingTodo todo = new ToaMeetingTodo();
					todo.setMeetingSummary(model);
					todo.setTodoTitle(title);
					todo.setTodoUsername(sname);
					todo.setTodoState(toState);
					summaryManager.saveMeetingTodo(todo);
				}
			}
		}
		noticeManager.deleteSummarytens(notId);
		if (!this.atenparams.equals("") && this.atenparams != null) {
			String[] xx = this.atenparams.split(",");
			for (int i = 0; i < xx.length; i++) {
				String[] yy = xx[i].split("/");
				String sname = yy[0];
				String toState = yy[1];
				if(!sname.equals("null")&& !sname.equals("")){
				if (toState.equals("undefined"))
					toState = "0";
				ToaMeetingAttendance dance = new ToaMeetingAttendance();
				dance.setNoticeId(notId);
				dance.setAttendanceUsername(sname);
				dance.setAttendanceFlag(toState);
				noticeManager.saveMeetingAttendance(dance);
			}
			}
		}
		// 保存附件
		if (file != null) {
			for (int i = 0; i < file.length; i++) {

				ToaMeetingAttach attach = new ToaMeetingAttach();
				attach.setMeetingSummary(model.getSummaryId());
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
				summaryManager.saveSummaryattach(attach);

			}
		}

		String href = "<script>window.dialogArguments.location='"
				+ getRequest().getContextPath()
				+ "/meetingmanage/meetingsummary/meetingsummary.action';window.close();</script>";
		return renderHtml(href);
	}

	/**
	 * 导出WORD文档
	 * 
	 * @author 胡丽丽
	 * @date 2009-12-31
	 * @return
	 * @throws Exception
	 */
	public String word() throws Exception {
		String filePath = this.getRequest().getRealPath("/")
				+ "/WEB-INF/jsp/meetingmanage/meetingsummary/meetingsummary-word.jsp";
		HttpServletResponse response = super.getResponse();
		StringBuffer linestr = new StringBuffer();
		String currentLine = "";
		FileInputStream fileinput;
		try {
			fileinput = new FileInputStream(filePath);// 读取文件

			// 根据连接到的html编码方式，修改编码
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fileinput, "utf-8"));
			while ((currentLine = reader.readLine()) != null) {
				linestr.append(currentLine + "\n");
			}
			// model = summaryManager.getConsummary(sumId);
			prepareModel();
			String[] resumevalue = this.getResumeValue(model);
			String[] tagvalue = this.getTagValue();
			String line = linestr.toString();
			for (int i = 0; i < tagvalue.length; i++) {
				if (line.indexOf(tagvalue[i]) != -1) {
					if ("[model.summaryContent]".equals(tagvalue[i])) {
						String content = HtmlUtils.htmlUnescape(resumevalue[i]);
						if (content != null && !content.equals("")) {
							line = line.replace(tagvalue[i], content);
						}
					} else {
						line = line.replace(tagvalue[i], resumevalue[i]);

					}
				}
			}
			response.reset();
			response.setContentType("application/x-download"); // windows
			OutputStream output = null;
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String((resumevalue[0] + ".doc").getBytes("gb2312"),
							"iso8859-1"));
			output = response.getOutputStream();
			byte[] linebyte = line.getBytes();
			output.write(linebyte);
			output.flush();
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				output = null;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	private String[] getResumeValue(ToaMeetingSummary mary) {
		List<ToaMeetingTodo> todoList = summaryManager.getSummaryTodos(mary
				.getSummaryId());
		List<ToaMeetingAttendance> dancelist = noticeManager
				.getConAttendances(mary.getNoticeId());
		StringBuffer todo = new StringBuffer();// 待办事项
		StringBuffer dance = new StringBuffer();// 考勤情况
		todo
				.append(
						"<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0 ")
				.append(
						"style='border-collapse:collapse;border:none;mso-border-alt:solid windowtext .5pt; mso-yfti-tbllook:480;mso-padding-alt:0cm 5.4pt 0cm 5.4pt;mso-border-insideh:.5pt solid windowtext;mso-border-insidev:.5pt solid windowtext'>")
				.append("<tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes'>")
				.append(
						"<td width=174 valign=top style='width:130.15pt;border:solid windowtext 1.0pt;mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt'>")
				.append(
						"<p class=MsoNormal><span lang=EN-US><o:p>工作名称</o:p></span></p>")
				.append(" </td>")
				.append(
						"<td width=174 valign=top style='width:130.2pt;border:solid windowtext 1.0pt; border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt'>")
				.append(
						"<p class=MsoNormal><span lang=EN-US><o:p>处理人</o:p></span></p>")
				.append(" </td>")
				.append(
						" <td width=174 valign=top style='width:130.2pt;border:solid windowtext 1.0pt;border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt'>")
				.append(
						" <p class=MsoNormal><span lang=EN-US><o:p>处理状态</o:p></span></p>")
				.append("</td>").append(" </tr>");
		for (ToaMeetingTodo to : todoList) {
			todo
					.append(
							"<tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes'>")
					.append(
							"<td width=174 valign=top style='width:130.15pt;border:solid windowtext 1.0pt;mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt'>")
					.append(
							"<p class=MsoNormal><span lang=EN-US><o:p>"
									+ to.getTodoTitle() + "</o:p></span></p>")
					.append(" </td>")
					.append(
							"<td width=174 valign=top style='width:130.2pt;border:solid windowtext 1.0pt; border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt'>")
					.append(
							"<p class=MsoNormal><span lang=EN-US><o:p>"
									+ to.getTodoUsername()
									+ "</o:p></span></p>")
					.append(" </td>")
					.append(
							" <td width=174 valign=top style='width:130.2pt;border:solid windowtext 1.0pt;border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt'>")
					.append(
							" <p class=MsoNormal><span lang=EN-US><o:p>"
									+ getstate("todo", to.getTodoState())
									+ "</o:p></span></p>").append("</td>")
					.append(" </tr>");
		}
		todo.append("</table>");
		dance
				.append(
						"<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0 ")
				.append(
						"style='border-collapse:collapse;border:none;mso-border-alt:solid windowtext .5pt; mso-yfti-tbllook:480;mso-padding-alt:0cm 5.4pt 0cm 5.4pt;mso-border-insideh:.5pt solid windowtext;mso-border-insidev:.5pt solid windowtext'>")
				.append("<tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes'>")
				.append(
						"<td width=260 valign=top style='width:195.25pt;border:solid windowtext 1.0pt; mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt'>")
				.append(
						"<p class=MsoNormal><span lang=EN-US><o:p>姓名</o:p></span></p>")
				.append(" </td>")
				.append(
						"<td width=260 valign=top style='width:195.3pt;border:solid windowtext 1.0pt;border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt'>")
				.append(
						"<p class=MsoNormal><span lang=EN-US><o:p>参加会议情况</o:p></span></p>")
				.append("</td>").append("</tr>");
		for (ToaMeetingAttendance dan : dancelist) {

			dance
					.append(
							"<tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes'>")
					.append(
							"<td width=260 valign=top style='width:195.25pt;border:solid windowtext 1.0pt; mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt'>")
					.append(
							"<p class=MsoNormal><span lang=EN-US><o:p>"
									+ dan.getAttendanceUsername()
									+ "</o:p></span></p>")
					.append(" </td>")
					.append(
							"<td width=260 valign=top style='width:195.3pt;border:solid windowtext 1.0pt;border-left:none;mso-border-left-alt:solid windowtext .5pt;mso-border-alt:solid windowtext .5pt;padding:0cm 5.4pt 0cm 5.4pt'>")
					.append(
							"<p class=MsoNormal><span lang=EN-US><o:p>"
									+ getstate("", dan.getAttendanceFlag())
									+ "</o:p></span></p>").append("</td>")
					.append("</tr>");

		}
		dance.append("</table>");
		SimpleDateFormat ds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] resumestr = new String[] { mary.getSummaryTitle(),
				mary.getSummaryAddr(), mary.getSummaryUsers(),
				ds.format(mary.getSummaryTime()), mary.getSummaryContent(),
				todo.toString(), dance.toString() };
		return resumestr;
	}

	/**
	 * 考勤状态
	 * 
	 * @param state
	 * @return
	 */
	private String getstate(String type, String state) {
		if ("todo".equals(type)) {
			if ("1".equals(state)) {
				return "未处理";
			} else if ("2".equals(state)) {
				return "正在处理";
			} else {
				return "处理完";
			}
		} else {
			if ("1".equals(state)) {
				return "出席";
			} else if ("2".equals(state)) {
				return "缺席";
			} else if ("3".equals(state)) {
				return "请假";
			} else {
				return "迟到";
			}
		}
	}

	private String[] getTagValue() {

		String[] tagstr = new String[] { "[model.summaryTitle]",
				"[model.summaryAddr]", "[model.summaryUsers]",
				"[model.summaryTime]", "[model.summaryContent]",
				"[model.todo]", "[model.dance]" };
		return tagstr;
	}

	public String toHTMLString(String in) {
		StringBuffer out = new StringBuffer();
		for (int i = 0; in != null && i < in.length(); i++) {
			char c = in.charAt(i);
			if (c == '\'') {
				out.append("&#039;");
			} else if (c == '\"') {
				out.append("&#034;");
			} else if (c == '<') {
				out.append("&lt;");
			} else if (c == '>') {
				out.append("&gt;");
			} else if (c == '&') {
				out.append("&amp;");
			} else if (c == ' ') {
				out.append("&nbsp;");
			} else if (c == '\n') {
				out.append("<br/>");
			} else {
				out.append(c);
			}
		}
		return out.toString();
	}

	/**
	 * 查看查看会议纪要详情
	 * 
	 * @author 申仪玲
	 *@date
	 * @return
	 * @throws Exception
	 */
	public String display() throws Exception {

		if (conId != null) {
			List<ToaMeetingNotice> noticeList = noticeManager
					.getConnoticeByConId(conId);
			if (noticeList.size() != 0 && noticeList != null) {
				notice = (ToaMeetingNotice) noticeList.get(0);
				notId = notice.getNoticeId();
				String userId = notice.getNoticeUsers();
				if (notice.getNoticeUsers() != null
						&& !notice.getNoticeUsers().equals("")) {
					String[] userIds = notice.getNoticeUsers().split(",");
					StringBuffer userNameInfo = new StringBuffer("");
					for (int i = 0; i < userIds.length; i++) {
						userNameInfo.append(
								noticeManager.getUserName(userIds[i])).append(
								",");
					}
					userName = userNameInfo.deleteCharAt(
							userNameInfo.toString().lastIndexOf(","))
							.toString();
				}

				List<ToaMeetingAttendance> dancelist = noticeManager
						.getConAttendances(notId);
				if (dancelist.size() != 0 && dancelist != null) {
					StringBuffer xx = new StringBuffer();
					for (Iterator it = dancelist.iterator(); it.hasNext();) {
						ToaMeetingAttendance to = (ToaMeetingAttendance) it
								.next();
						xx.append(to.getAttendanceUsername());
						xx.append("/" + to.getAttendanceFlag() + ",");
					}
					xx.deleteCharAt(xx.toString().lastIndexOf(","));
					this.atenparams = xx.toString();
				}

				List<ToaMeetingSummary> sumList = summaryManager
						.getConsummarys(notId);
				if (sumList.size() != 0 && sumList != null) {
					model = (ToaMeetingSummary) sumList.get(0);
					sumId = model.getSummaryId();
					List todoList = summaryManager.getSummaryTodos(sumId);
					if (todoList != null && todoList.size() != 0) {
						StringBuffer ss = new StringBuffer();
						for (Iterator it = todoList.iterator(); it.hasNext();) {
							ToaMeetingTodo to = (ToaMeetingTodo) it.next();
							ss.append(to.getTodoTitle());
							ss.append("/" + to.getTodoUsername());
							ss.append("/" + to.getTodoState() + ",");
						}
						ss.deleteCharAt(ss.toString().lastIndexOf(","));
						this.todoparams = ss.toString();
					}
					attachList = summaryManager.getSummaryAttaches(sumId);
					if (attachList != null) {
						Iterator it = attachList.iterator();
						attachFiles = "";
						while (it.hasNext()) {
							ToaMeetingAttach att = (ToaMeetingAttach) it.next();
							attachFiles += "<div id=" + att.getAttachId()
									+ " style=\"display: \">"
									+ "<a href=\"javascript:download('"
									+ att.getAttachId() + "')\">"
									+ att.getAttachName() + "</a>&nbsp;</div>";
						}
					}
				}
			}
		}
		return "display";
	}

	public String guiDang() throws Exception {

		try {
			summaryManager.addTemplateFile(sumId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			addActionMessage(e.getMessage());
			renderText("fail");
		}

		renderText("OK");
		return null;

	}

	public void setUpload(File[] file) {
		this.file = file;
	}

	public void setUploadFileName(String[] fileName) {
		this.fileName = fileName;
	}

	public String getSumId() {
		return sumId;
	}

	public void setSumId(String sumId) {
		this.sumId = sumId;
	}

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}

	public String getTodoparams() {
		return todoparams;
	}

	public void setTodoparams(String todoparams) {
		this.todoparams = todoparams;
	}

	public String getAtenparams() {
		return atenparams;
	}

	public void setAtenparams(String atenparams) {
		this.atenparams = atenparams;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Page<ToaMeetingConference> getPage() {
		return page;
	}

	public void setPage(Page<ToaMeetingConference> page) {
		this.page = page;
	}

	public Page<ToaMeetingSummary> getSumpage() {
		return sumpage;
	}

	public void setSumpage(Page<ToaMeetingSummary> sumpage) {
		this.sumpage = sumpage;
	}

	public ToaMeetingAttach getAttach() {
		return attach;
	}

	public void setAttach(ToaMeetingAttach attach) {
		this.attach = attach;
	}

	public ToaMeetingNotice getNotice() {
		return notice;
	}

	public void setNotice(ToaMeetingNotice notice) {
		this.notice = notice;
	}

	public String getSummtitle() {
		return summtitle;
	}

	public void setSummtitle(String summtitle) {
		this.summtitle = summtitle;
	}

	public Date getSummtime() {
		return summtime;
	}

	public void setSummtime(Date summtime) {
		this.summtime = summtime;
	}

	public String getDelAttachIds() {
		return delAttachIds;
	}

	public void setDelAttachIds(String delAttachIds) {
		this.delAttachIds = delAttachIds;
	}

	public ToaMeetingSummary getModel() {
		return model;
	}

	public void setModel(ToaMeetingSummary model) {
		this.model = model;
	}

	public MeetingsummaryManager getSummaryManager() {
		return summaryManager;
	}

	@Autowired
	public void setSummaryManager(MeetingsummaryManager summaryManager) {
		this.summaryManager = summaryManager;
	}

	public MeetingnoticeManager getNoticeManager() {
		return noticeManager;
	}

	@Autowired
	public void setNoticeManager(MeetingnoticeManager noticeManager) {
		this.noticeManager = noticeManager;
	}

	public MeetingconferenceManager getConferenceManager() {
		return conferenceManager;
	}

	@Autowired
	public void setConferenceManager(MeetingconferenceManager conferenceManager) {
		this.conferenceManager = conferenceManager;
	}

	public MeetingtopicManager getTopicManager() {
		return topicManager;
	}

	@Autowired
	public void setTopicManager(MeetingtopicManager topicManager) {
		this.topicManager = topicManager;
	}

	public String getSummaddr() {
		return summaddr;
	}

	public void setSummaddr(String summaddr) {
		this.summaddr = summaddr;
	}

	public String getIsguidang() {
		return isguidang;
	}

	public void setIsguidang(String isguidang) {
		this.isguidang = isguidang;
	}

	public ToaMeetingConference getConference() {
		return conference;
	}

	public void setConference(ToaMeetingConference conference) {
		this.conference = conference;
	}

	public Map getGuidangMap() {
		return guidangMap;
	}

	public void setGuidangMap(Map guidangMap) {
		this.guidangMap = guidangMap;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public Date getNoticeStime() {
		return noticeStime;
	}

	public void setNoticeStime(Date noticeStime) {
		this.noticeStime = noticeStime;
	}

	public String getNoticeAddr() {
		return noticeAddr;
	}

	public void setNoticeAddr(String noticeAddr) {
		this.noticeAddr = noticeAddr;
	}

	public Date getNoticeEndTime() {
		return noticeEndTime;
	}

	public void setNoticeEndTime(Date noticeEndTime) {
		this.noticeEndTime = noticeEndTime;
	}

	public String getNoticeIs() {
		return noticeIs;
	}

	public void setNoticeIs(String noticeIs) {
		this.noticeIs = noticeIs;
	}

	public Page<ToaMeetingNotice> getNopage() {
		return nopage;
	}

	public void setNopage(Page<ToaMeetingNotice> nopage) {
		this.nopage = nopage;
	}

	public String getNotId() {
		return notId;
	}

	public void setNotId(String notId) {
		this.notId = notId;
	}

	public ToaMeetingAttendance getAttendance() {
		return attendance;
	}

	public void setAttendance(ToaMeetingAttendance attendance) {
		this.attendance = attendance;
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

	public String getAttachFiles() {
		return attachFiles;
	}

	public void setAttachFiles(String attachFiles) {
		this.attachFiles = attachFiles;
	}

	public List<ToaMeetingAttach> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<ToaMeetingAttach> attachList) {
		this.attachList = attachList;
	}

}
