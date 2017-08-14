package com.strongit.oa.workinspect.worktodo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOsManagementSummary;
import com.strongit.oa.bo.TOsTaskAttach;
import com.strongit.oa.bo.TOsWorktask;
import com.strongit.oa.bo.TOsWorktaskSend;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
public class WorkTodoAction extends BaseActionSupport<TOsWorktaskSend> {

	@Autowired
	private IWorkTodoService iWorkTodoService;
	private Page<TOsWorktaskSend> page = new Page<TOsWorktaskSend>(15, true);// 分页
	private TOsWorktaskSend model = new TOsWorktaskSend();// 数据字典对象
	private TOsManagementSummary summary = new TOsManagementSummary();// 办理纪要对象
	private Map typeMap;
	private String sendtaskId;// 数据字典ID
	private String selectTaskTitle;// 查询字段标题
	private List<TOsManagementSummary> summaryList = new ArrayList<TOsManagementSummary>();// 办理纪要
	private List<TOsTaskAttach> attachList = new ArrayList<TOsTaskAttach>();// 附件
	private File[] file; // 附件
	private String[] fileFileName; // 附件名称
	
	private String delAttIds;//删除附件ID
	private String taskId;
	// 查询条件
	private Date selectTaskSendTime;
	private Date selectTaskSendTime2;
	private String selectTaskUser;
	private String selectTaskState;
	@Autowired
	private IUserService userService;// 用户

	private DesktopSectionManager sectionManager;// 个人桌面添加
	private String toMydesk; // 回到个人桌面
	private String yangyong;
	private String attachId;
	private String listodo;

	public String getListodo() {
		return listodo;
	}

	public void setListodo(String listodo) {
		this.listodo = listodo;
	}

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public String getYangyong() {
		return yangyong;
	}

	public void setYangyong(String yangyong) {
		this.yangyong = yangyong;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<TOsTaskAttach> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<TOsTaskAttach> attachList) {
		this.attachList = attachList;
	}

	public String getToMydesk() {
		return toMydesk;
	}

	public void setToMydesk(String toMydesk) {
		this.toMydesk = toMydesk;
	}

	private String flage;// 个人桌面使用

	public String getFlage() {
		return flage;
	}

	public void setFlage(String flage) {
		this.flage = flage;
	}

	public DesktopSectionManager getSectionManager() {
		return sectionManager;
	}

	@Autowired
	public void setSectionManager(DesktopSectionManager sectionManager) {
		this.sectionManager = sectionManager;
	}

	public Date getSelectTaskSendTime() {
		return selectTaskSendTime;
	}

	public void setSelectTaskSendTime(Date selectTaskSendTime) {
		this.selectTaskSendTime = selectTaskSendTime;
	}

	public Date getSelectTaskSendTime2() {
		return selectTaskSendTime2;
	}

	public void setSelectTaskSendTime2(Date selectTaskSendTime2) {
		this.selectTaskSendTime2 = selectTaskSendTime2;
	}

	public String getSelectTaskUser() {
		return selectTaskUser;
	}

	public void setSelectTaskUser(String selectTaskUser) {
		this.selectTaskUser = selectTaskUser;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public String[] getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getSelectTaskTitle() {
		return selectTaskTitle;
	}

	public void setSelectTaskTitle(String selectTaskTitle) {
		this.selectTaskTitle = selectTaskTitle;
	}

	public String getSendtaskId() {
		return sendtaskId;
	}

	public void setSendtaskId(String sendtaskId) {
		this.sendtaskId = sendtaskId;
	}

	public Map getTypeMap() {
		return typeMap;
	}

	public void setTypeMap(Map typeMap) {
		this.typeMap = typeMap;
	}

	public Page<TOsWorktaskSend> getPage() {
		return page;
	}

	public void setPage(Page<TOsWorktaskSend> page) {
		this.page = page;
	}

	@Autowired
	public void setiWorkTodoService(IWorkTodoService iWorkTodoService) {
		this.iWorkTodoService = iWorkTodoService;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		prepareModel();
		return null;
	}
	
	public String showTable()throws Exception{
		
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		int num = Integer.parseInt(showNum);
		int sub = Integer.parseInt(subLength);
		page = new Page<TOsWorktaskSend>(10, true);
		page.setPageNo(1);
		page.setPageSize(num);
		
		TOsWorktask task = new TOsWorktask();
		task.setWorktaskTitle(this.selectTaskTitle);
		task.setWorktaskUserName(this.selectTaskUser);
		model.setTOsWorktask(task);
		model.setTaskRecvTime(this.selectTaskSendTime);// 开始时间
		model.setTaskSendTime(this.selectTaskSendTime2);// 结束时间
		model.setTaskState(this.selectTaskState);
		page = this.iWorkTodoService.getAllTOsWork(page, model);// 得到所有待办工作
		
		List<TOsWorktaskSend> resList = new ArrayList<TOsWorktaskSend>();
		
		resList = page.getResult();
		
		StringBuffer innerHtml = new StringBuffer();
		SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd");
		
		if (resList != null) {			
			innerHtml.append("<table width=\"100%\" style=\"font-size:"+sectionFontSize+"px\" cellpadding=\"0\" cellspacing=\"0\">");

			for (int i=0;i<resList.size()&&i<num;i++) {

				String sendtaskId = resList.get(i).getSendtaskId();
				String worktaskTitle = resList.get(i).getTOsWorktask().getWorktaskTitle();
				String title = worktaskTitle;
				String workstate = resList.get(i).getTaskState();
				if("0".equals(workstate)){
					workstate = "待签收";
				}else if("0".equals(workstate)){
					workstate = "办理中";
				}
				String conSendTime = sdfDate.format(resList.get(i).getTOsWorktask().getWorktaskStime());
				
				if(worktaskTitle.length()>sub){
					worktaskTitle=worktaskTitle.substring(0, sub)+"...";
				}

				innerHtml.append("<tr height=\"25px\">");
				innerHtml.append("<td width=\"75%\">");
				//图标
				innerHtml.append("<img src=\"").append(getRequest().getContextPath()).
				append("/oa/image/desktop/littlegif/news_bullet.gif").append("\"/> ");
				
				// 标题链接
				StringBuffer titleLink = new StringBuffer();
				innerHtml
				.append("<a href=\"#\"  onclick=\"").append("var width=screen.availWidth-10;var height=screen.availHeight-30;" +
						"var a = window.open(").append("'"+getRequest().getContextPath()).append("/workinspect/worktodo/workTodo!view.action?sendtaskId=").append(sendtaskId).append("','processed','height='+height+', width='+width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');" +
						"if(a && a == 'OK')" +
						"{window.location.reload();}\"")
				
				.append(" title=\"").append(title).append("\">")
				.append(worktaskTitle)
				.append("</a>").append("");
				innerHtml.append("</td>");
		
				innerHtml.append("<td width=\"15%\">");
				innerHtml
				.append(workstate);
				innerHtml.append("</td>");
				
				innerHtml.append("<td>");
				innerHtml
				.append(conSendTime);
				innerHtml.append("</td>");	
				innerHtml.append("</tr>");
			}
			innerHtml.append("</table>");
		}
			return renderHtml(innerHtml.toString());	// 用renderHtml将设置好的html代码返回桌面显示
	}

	@Override
	public String list() throws Exception {
		// tOsWorktaskSend.setWorktaskTitle(selectTaskTitle);
		TOsWorktask task = new TOsWorktask();
		task.setWorktaskTitle(this.selectTaskTitle);
		task.setWorktaskUserName(this.selectTaskUser);
		model.setTOsWorktask(task);
		model.setTaskRecvTime(this.selectTaskSendTime);// 开始时间
		model.setTaskSendTime(this.selectTaskSendTime2);// 结束时间
		model.setTaskState(this.selectTaskState);
		page = this.iWorkTodoService.getAllTOsWorktaskSend(page, model);// 得到个人所有待办工作
		if (typeMap == null) {
			typeMap = new HashMap();
		}
		typeMap.put("0", "待签收");
		typeMap.put("1", "办理中");
		typeMap.put("2", "已办结");

		return "personal";
	}

	// 得到个人所有待签收工作
	public String list2() throws Exception {
		TOsWorktask task = new TOsWorktask();
		task.setWorktaskTitle(this.selectTaskTitle);
		task.setWorktaskUserName(this.selectTaskUser);
		model.setTOsWorktask(task);
		model.setTaskRecvTime(this.selectTaskSendTime);// 开始时间
		model.setTaskSendTime(this.selectTaskSendTime2);// 结束时间
		model.setTaskState(this.selectTaskState);
		page = this.iWorkTodoService.getAllnotsign(page, model);// 得到个人所有待办工作
		if (typeMap == null) {
			typeMap = new HashMap();
		}
		typeMap.put("0", "待签收");
		typeMap.put("1", "办理中");
		typeMap.put("2", "已办结");

		return "personal";
	}

	// 得到个人所有待办工作
	public String list3() throws Exception {
		TOsWorktask task = new TOsWorktask();
		task.setWorktaskTitle(this.selectTaskTitle);
		task.setWorktaskUserName(this.selectTaskUser);
		model.setTOsWorktask(task);
		model.setTaskRecvTime(this.selectTaskSendTime);// 开始时间
		model.setTaskSendTime(this.selectTaskSendTime2);// 结束时间
		model.setTaskState(this.selectTaskState);
		page = this.iWorkTodoService.getAllnotTodo(page, model);// 得到个人所有待办工作
		if (typeMap == null) {
			typeMap = new HashMap();
		}
		typeMap.put("0", "待签收");
		typeMap.put("1", "办理中");
		typeMap.put("2", "已办结");

		return "personal";
	}

	public String returnList() throws Exception {
		TOsWorktask task = new TOsWorktask();
		task.setWorktaskTitle(this.selectTaskTitle);
		task.setWorktaskUserName(this.selectTaskUser);
		model.setTOsWorktask(task);
		model.setTaskRecvTime(this.selectTaskSendTime);// 开始时间
		model.setTaskSendTime(this.selectTaskSendTime2);// 结束时间
		model.setTaskState(this.selectTaskState);
		page = this.iWorkTodoService.getUnitWork(page, model);// 得到部门待办工作
		if (typeMap == null) {
			typeMap = new HashMap();
		}
		typeMap.put("0", "待签收");
		typeMap.put("1", "办理中");
		typeMap.put("2", "已办结");

		return "org";
	}

	public String returnList1() throws Exception {
		TOsWorktask task = new TOsWorktask();
		task.setWorktaskTitle(this.selectTaskTitle);
		task.setWorktaskUserName(this.selectTaskUser);
		model.setTOsWorktask(task);
		model.setTaskRecvTime(this.selectTaskSendTime);// 开始时间
		model.setTaskSendTime(this.selectTaskSendTime2);// 结束时间
		model.setTaskState(this.selectTaskState);
		page = this.iWorkTodoService.getWorkTobe(page, model);// 得到部门待签收工作
		if (typeMap == null) {
			typeMap = new HashMap();
		}
		typeMap.put("2", "已办结");
		typeMap.put("1", "办理中");
		typeMap.put("0", "待签收");

		return "org";
	}

	public String returnList2() throws Exception {
		TOsWorktask task = new TOsWorktask();
		task.setWorktaskTitle(this.selectTaskTitle);
		task.setWorktaskUserName(this.selectTaskUser);
		model.setTOsWorktask(task);
		model.setTaskRecvTime(this.selectTaskSendTime);// 开始时间
		model.setTaskSendTime(this.selectTaskSendTime2);// 结束时间
		model.setTaskState(this.selectTaskState);
		page = this.iWorkTodoService.getWorkTodo(page, model);// 得到部门办理中工作
		if (typeMap == null) {
			typeMap = new HashMap();
		}
		typeMap.put("0", "待签收");
		typeMap.put("1", "办理中");
		typeMap.put("2", "已办结");

		return "org";
	}
	public String getsend() throws Exception {
		return "send";
	}
	public String gettodo() throws Exception {
	return "todo";
    }
	public String getdeal() throws Exception {
		return "deal";
	}
	@Override
	protected void prepareModel() throws Exception {
		if (sendtaskId == null || "".equals(sendtaskId)) {
			model = new TOsWorktaskSend();
		} else {
			model = this.iWorkTodoService.getById(sendtaskId);
		}
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public TOsWorktaskSend getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	/**
	 * 查看工作评语
	 * 
	 * @return
	 * @throws Exception
	 */
	public String viewDev() throws Exception {
		prepareModel();

		return "view";
	}

	/**
	 * 查看办理纪要
	 * 
	 * @return
	 * @throws Exception
	 */
	public String view() throws Exception {
		prepareModel();
		TOsWorktask task = model.getTOsWorktask();
		TUumsBaseUser u = userService.getUserInfoByUserId(task
				.getWorktaskUser());
		task.setWorktaskUserName(u.getUserName());
		TUumsBaseOrg o = userService.getOrgInfoByOrgId(task
				.getWorktaskUnit());
		task.setWorktaskUnitName(o.getOrgName());
		if (model.getOperateUserid() != null
				&& !"".equals(model.getOperateUserid())) {
			TUumsBaseUser u2 = userService.getUserInfoByUserId(model
					.getOperateUserid());
			model.setOperateUserName(u2.getUserName());
		}
		// 是否有附件
		attachList = this.iWorkTodoService
				.getTaskSummaryAttachListByTaskId(task.getWorktaskId());
		Set<TOsManagementSummary> summarys = model.getTOsManagementSummaries();
		summaryList = new ArrayList<TOsManagementSummary>(summarys);
		if (summaryList.size() > 0) {
			summary = summaryList.get(0);
			List<TOsTaskAttach> attachList = this.iWorkTodoService
					.getSummaryListBySummaryId(summary.getSummaryId());
			if (attachList.size() > 0) {
				summary.setAttachList(attachList);
			}

		}
		return "summary";
	}

	/**
	 * 填写办理纪要
	 * 
	 * @return
	 * @throws Exception
	 */
	public String SaveHandle() throws Exception {
		if(null!=delAttIds&&!"".equals(delAttIds)){
			String[] ids = delAttIds.split(",");
			for(int i=0;i<ids.length;i++){
				iWorkTodoService.delAttach(ids[i]);
			}
		}
		iWorkTodoService.save(summary, file, fileFileName);
		if ("1".equals(yangyong)) {
			return renderHtml("<script>window.returnValue='ok'; window.close();</script>");
		} else {
			// return renderHtml("<script>window.close();</script>");
			return renderHtml("<script>window.returnValue = \"OK\";window.close();window.opener.location='"
					+ getRequest().getContextPath()
					+ "/workinspect/worktodo/workTodo!list.action';</script>");
		}
	}

	/**
	 * 弹出办理纪要页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String AndHandle() throws Exception {
		prepareModel();
		summary = new TOsManagementSummary();
		summary.setTOsWorktaskSend(model);
		return "handle";
	}

	/**
	 * 签收工作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String siGn() throws Exception {
		TOsWorktaskSend send = this.iWorkTodoService.getById(sendtaskId);
		if (send != null) {
			if (!"0".equals(send.getTaskState())) {
				String html = "<script>alert('该任务已签收，不需要再次签收！');window.location='"
						+ getRequest().getContextPath()
						+ "/workinspect/worktodo/workTodo!list.action';</script>";
				return renderHtml(html);
			} else {
				iWorkTodoService.siGn(send);
			}

		}
		String html = "<script>window.location='"
				+ getRequest().getContextPath()
				+ "/workinspect/worktodo/workTodo!list.action';</script>";
		return this.renderHtml(html);
	}

	/**
	 * 部门签收工作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String orgSign() throws Exception {
		TOsWorktaskSend send = this.iWorkTodoService.getById(sendtaskId);
		if (send != null) {
			if (!"0".equals(send.getTaskState())) {
				String html = "<script>alert('该任务已签收，不需要再次签收！');window.location='"
						+ getRequest().getContextPath()
						+ "/workinspect/worktodo/workTodo!returnList.action';</script>";
				return renderHtml(html);
			} else {
				iWorkTodoService.siGn(send);
			}

		}
		String html = "<script>window.location='"
				+ getRequest().getContextPath()
				+ "/workinspect/worktodo/workTodo!returnList.action';</script>";
		return this.renderHtml(html);
	}

	/**
	 * 签收
	 * @return
	 * @throws Exception
	 */
	public String siGn1() throws Exception {
		TOsWorktaskSend send = this.iWorkTodoService.getById(sendtaskId);
		if (send != null) {
			if (!"0".equals(send.getTaskState())) {
				String html = "<script>alert('该任务已签收，不需要再次签收！');window.location='"
						+ getRequest().getContextPath()
						+ "/workinspect/worktodo/workTodo!list.action';</script>";
				return renderHtml(html);
			} else {
				iWorkTodoService.siGn(send);
			}

		}
		prepareModel();
		TOsWorktask task = model.getTOsWorktask();
		TUumsBaseUser u = userService.getUserInfoByUserId(task
				.getWorktaskUser());
		task.setWorktaskUserName(u.getUserName());
		TUumsBaseOrg o = userService.getOrgInfoByOrgId(task
				.getWorktaskUnit());
		task.setWorktaskUnitName(o.getOrgName());
		if (model.getOperateUserid() != null
				&& !"".equals(model.getOperateUserid())) {
			TUumsBaseUser u2 = userService.getUserInfoByUserId(model
					.getOperateUserid());
			model.setOperateUserName(u2.getUserName());
		}
		return "summary";
	}
	/**
	 * 个人签收
	 * @return
	 * @throws Exception
	 */
	public String siGn2() throws Exception {
		TOsWorktaskSend send = this.iWorkTodoService.getById(sendtaskId);
		if (send != null) {
			if (!"0".equals(send.getTaskState())) {
				String html = "<script>alert('该任务已签收，不需要再次签收！');window.location='"
						+ getRequest().getContextPath()
						+ "/workinspect/worktodo/workTodo!list.action';</script>";
				return renderHtml("0");
			} else {
				iWorkTodoService.siGn(send);
			}

		}
		return this.renderHtml("1");
	}

	/**
	 * 我的办理件查询
	 * 
	 * @return
	 * @throws Exception
	 */

	public String getSelect() {
		System.out.println("1111111111");
		if (typeMap == null) {
			typeMap = new HashMap();
		}
		typeMap.put("0", "待签收");
		typeMap.put("1", "办理中");
		typeMap.put("2", "已办结");
		return "select";
	}

	/**
	 * 我的发送件查询
	 * 
	 * @return
	 * @throws Exception
	 */

	public String getSelectSend() {
		return "selectSend";
	}

	/**
	 * author:yy
	 * description: 桌面显示(个人待收工作)
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String personalSend() {
		StringBuffer innerHtml = new StringBuffer();
		String blockid = getRequest().getParameter("blockid");// 获取blockid
		Map<String, String> map = sectionManager.getParam(blockid);// 通过blockid获取映射对象
		String showNum = map.get("showNum");// 显示条数
		String subLength = map.get("subLength");// 主题长度
		String showCreator = map.get("showCreator");// 是否显示作者
		String showDate = map.get("showDate");// 是否显示日期
		String sectionFontSize = getRequest().getParameter("sectionFontSize");// 字体大小

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

		// “更多”链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('");
		link.append(getRequest().getContextPath());
		link
				.append("/workinspect/worktodo/workTodo!list2.action?selectTaskState=0',");
		link.append("'个人待接收');");
		// 获取个人待收list
		List<TOsWorktaskSend> list = null;
		page.setPageSize(num);
		page = this.iWorkTodoService.getSend(page, model);// 得到个人待收工作
		list = page.getResult();

		if (list != null) {
			for (int i = 0; i < num && i < list.size(); i++) {// 获取在条数范围内的列表
				TOsWorktaskSend tos = list.get(i);
				String sendtaskId = tos.getSendtaskId();
				String flag = this.iWorkTodoService.getById(sendtaskId).getTaskState();//得到是否需要显示按钮的标志
				if(!"0".equals(flag)){
				}else{
					
				}

				// 标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript:if(confirm('您确定查看该任务吗?')){");
				titleLink
						.append("javascript:var ret = window.showModalDialog('");
				titleLink.append(getRequest().getContextPath());
//				titleLink
//						.append("/workinspect/worktodo/workTodo!siGn1.action?yangyong=1&sendtaskId=");
				titleLink
				.append("/workinspect/worktodo/workTodo!view.action?yangyong=1&sendtaskId=");  //点击查看任务内容
				titleLink.append(sendtaskId);
				
				titleLink
						.append("',window,'help:no;status:no;scroll:no;dialogWidth:760px; dialogHeight:600px');"
								+ "window.location.reload();");
				titleLink.append("}");
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");

				innerHtml
						.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td >");
				String title = tos.getTOsWorktask().getWorktaskTitle();
				if (title == null) {
					title = "";
				}
				if (title.length() > length)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0, length) + "...";

				innerHtml.append("	<span ").append(" title=\"").append(
						tos.getTOsWorktask().getWorktaskTitle()).append("\">")
						.append("	<a style=\"font-size:"+sectionFontSize+"px\" href=\"#\" onclick=\"").append(
								titleLink.toString()).append("\"> ").append(
								title).append("</a></span>");
				innerHtml.append("</td>");

				if ("1".equals(showCreator)) {// 如果设置为显示作者，则显示发送人的信息
					innerHtml.append("<td width=\"80px\">");
					if (tos.getTaskRecvId() == null) {
						innerHtml.append("<span class =\"linkgray\">").append(
								"").append("</span>");
					} else {
						TUumsBaseUser u = userService
								.getUserInfoByUserId(tos.getTOsWorktask()
										.getWorktaskUser());
						innerHtml.append("<span class =\"linkgray\">").append(
								u.getUserName()).append("</span>");
					}
					innerHtml.append("</td>");
				}
				if ("1".equals(showDate)) {// 如果设置为显示日期， 则显示发送时间
					innerHtml.append("<td width=\"100px\">");
					innerHtml.append("<span class =\"linkgray\">").append(
							st.format(tos.getTaskSendTime())).append("</span>");
					innerHtml.append("</td>");
				}

				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
		}
		innerHtml
				.append(
						"<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"")
				.append(link.toString()).append("\"> ").append("<img src='../oa/image/more.gif' border='0' complete='complete'/></a></div>");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * author:yy
	 * description: 桌面显示(个人待办工作)
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String personalTodo() {
		StringBuffer innerHtml = new StringBuffer();
		String blockid = getRequest().getParameter("blockid");// 获取blockid
		Map<String, String> map = sectionManager.getParam(blockid);// 通过blockid获取映射对象
		String showNum = map.get("showNum");// 显示条数
		String subLength = map.get("subLength");// 主题长度
		String showCreator = map.get("showCreator");// 是否显示作者
		String showDate = map.get("showDate");// 是否显示日期
		String sectionFontSize = getRequest().getParameter("sectionFontSize");// 字体大小
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

		// “更多”链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('");
		link.append(getRequest().getContextPath());
		link
				.append("/workinspect/worktodo/workTodo!list3.action?selectTaskState=1',");
		link.append("'个人待办');");
		// 获取个人待收list
		List<TOsWorktaskSend> list = null;
		page.setPageSize(num);
		page = this.iWorkTodoService.getAllTOsWorktaskTodo(page, model);// 得到个人待办工作
		list = page.getResult();

		if (list != null) {
			for (int i = 0; i < num && i < list.size(); i++) {// 获取在条数范围内的列表
				TOsWorktaskSend tos = list.get(i);
				String sendtaskId = tos.getSendtaskId();

				// 标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript:window.showModalDialog('");
				titleLink.append(getRequest().getContextPath());
				titleLink
						.append("/workinspect/worktodo/workTodo!view.action?yangyong=1&sendtaskId=");
				titleLink.append(sendtaskId);
				titleLink
						.append("',window,'help:no;status:no;scroll:no;dialogWidth:760px; dialogHeight:600px');"
								+ "window.location.reload();");

				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");

				innerHtml
						.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				String title = tos.getTOsWorktask().getWorktaskTitle();
				if (title == null) {
					title = "";
				}
				if (title.length() > length)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0, length) + "...";
				innerHtml.append("	<span ").append(" title=\"").append(
						tos.getTOsWorktask().getWorktaskTitle()).append("\">")
						.append("	<a style=\"font-size:"+sectionFontSize+"px\" href=\"#\" onclick=\"").append(
								titleLink.toString()).append("\"> ").append(
								title).append("</a></span>");

				innerHtml.append("</td>");
				if ("1".equals(showCreator)) {// 如果设置为显示作者，则显示作者信息
					innerHtml.append("<td width=\"80px\">");
					if (tos.getTaskRecvId() == null) {
						innerHtml.append("<span class =\"linkgray\">").append(
								"").append("</span>");
					} else {
						TUumsBaseUser u = userService
								.getUserInfoByUserId(tos.getTOsWorktask()
										.getWorktaskUser());
						innerHtml.append("<span class =\"linkgray\">").append(
								u.getUserName()).append("</span>");
					}
					innerHtml.append("</td>");
				}
				if ("1".equals(showDate)) {// 如果设置为显示日期，则显示工作的接收时间
					innerHtml.append("<td width=\"100px\">");
					innerHtml.append("<span class =\"linkgray\">").append(
							st.format(tos.getTaskSendTime())).append("</span>");
					innerHtml.append("</td>");
				}
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
		}
		innerHtml
				.append(
						"<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"")
				.append(link.toString()).append("\"> ").append("<img src='../oa/image/more.gif' border='0' complete='complete'/></a></div>");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * author:yy
	 * description: 桌面显示(部门待收工作)
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String departTobe() {
		StringBuffer innerHtml = new StringBuffer();
		String blockid = getRequest().getParameter("blockid");// 获取blockid
		Map<String, String> map = sectionManager.getParam(blockid);// 通过blockid获取映射对象
		String showNum = map.get("showNum");// 显示条数
		String subLength = map.get("subLength");// 主题长度
		String showCreator = map.get("showCreator");// 是否显示作者
		String showDate = map.get("showDate");// 是否显示日期
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
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

		// “更多”链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('");
		link.append(getRequest().getContextPath());
		link
				.append("/workinspect/worktodo/workTodo!returnList1.action?selectTaskState=0',");
		link.append("'部门待签收');");
		// 获取个人待收list
		List<TOsWorktaskSend> list = null;
		page.setPageSize(num);
		page = this.iWorkTodoService.getWorkTobe(page, model);// 得到部门待签收工作
		list = page.getResult();

		if (list != null) {
			for (int i = 0; i < num && i < list.size(); i++) {// 获取在条数范围内的列表
				TOsWorktaskSend tos = list.get(i);
				String sendtaskId = tos.getSendtaskId();

				// 标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript:if(confirm('您确定接收并办理该任务吗?')){");
				titleLink.append("javascript:window.showModalDialog('");
				titleLink.append(getRequest().getContextPath());
				titleLink
						.append("/workinspect/worktodo/workTodo!siGn1.action?yangyong=1&sendtaskId=");
				titleLink.append(sendtaskId);
				titleLink
						.append("',window,'help:no;status:no;scroll:no;dialogWidth:760px; dialogHeight:600px');"
								+ "window.location.reload();");
				titleLink.append("}");

				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml
						.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				String title = tos.getTOsWorktask().getWorktaskTitle();
				if (title == null) {
					title = "";
				}
				if (title.length() > length)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0, length) + "...";
				innerHtml.append("	<span ").append(" title=\"").append(
						tos.getTOsWorktask().getWorktaskTitle()).append("\">")
						.append("	<a style=\"font-size:"+sectionFontSize+"px\" href=\"#\" onclick=\"").append(
								titleLink.toString()).append("\"> ").append(
								title).append("</a></span>");
				innerHtml.append("</td>");

				if ("1".equals(showCreator)) {// 如果设置为显示作者，则显示作者信息
					innerHtml.append("<td width=\"80px\">");
					if (tos.getTaskRecvId() == null) {
						innerHtml.append("<span class =\"linkgray\">").append(
								"").append("</span>");
					} else {
						TUumsBaseUser u = userService
								.getUserInfoByUserId(tos.getTOsWorktask()
										.getWorktaskUser());
						innerHtml.append("<span class =\"linkgray\">").append

						(u.getUserName()).append("</span>");
					}
					innerHtml.append("</td>");
				}
				if ("1".equals(showDate)) {// 如果设置为显示日期，则显示公文生效日期信息
					innerHtml.append("<td width=\"100px\">");
					innerHtml.append("<span class =\"linkgray\">").append(
							st.format(tos.getTaskSendTime())).append("</span>");
					innerHtml.append("</td>");
				}
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
		}
		innerHtml
				.append(
						"<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"")
				.append(link.toString()).append("\"> ").append("<img src='../oa/image/more.gif' border='0' complete='complete'/></a></div>");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	/**
	 * author:yy
	 * description: 桌面显示(部门待办理工作)
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String mensTodo() {
		StringBuffer innerHtml = new StringBuffer();
		String blockid = getRequest().getParameter("blockid");// 获取blockid
		Map<String, String> map = sectionManager.getParam(blockid);// 通过blockid获取映射对象
		String showNum = map.get("showNum");// 显示条数
		String subLength = map.get("subLength");// 主题长度
		String showCreator = map.get("showCreator");// 是否显示作者
		String showDate = map.get("showDate");// 是否显示日期
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
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

		// “更多”链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('");
		link.append(getRequest().getContextPath());
		link
				.append("/workinspect/worktodo/workTodo!returnList2.action?selectTaskState=1',");
		link.append("'部门待办工作');");
		// 获取个人待收list
		List<TOsWorktaskSend> list = null;
		page.setPageSize(num);
		page = this.iWorkTodoService.getWorkTodo(page, model);// 得到部门待办理工作
		list = page.getResult();

		if (list != null) {
			for (int i = 0; i < num && i < list.size(); i++) {// 获取在条数范围内的列表
				TOsWorktaskSend tos = list.get(i);
				String sendtaskId = tos.getSendtaskId();

				// 标题连接
				StringBuffer titleLink = new StringBuffer();
				titleLink.append("javascript:window.showModalDialog('");
				titleLink.append(getRequest().getContextPath());
				titleLink
						.append("/workinspect/worktodo/workTodo!view.action?yangyong=1&sendtaskId=");
				titleLink.append(sendtaskId);
				titleLink
						.append("',window,'help:no;status:no;scroll:no;dialogWidth:760px; dialogHeight:600px');"
								+ "window.location.reload();");

				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml
						.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				String title = tos.getTOsWorktask().getWorktaskTitle();
				if (title == null) {
					title = "";
				}
				if (title.length() > length)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0, length) + "...";
				innerHtml.append("	<span ").append(" title=\"").append(
						tos.getTOsWorktask().getWorktaskTitle()).append("\">")
						.append("	<a style=\"font-size:"+sectionFontSize+"px\" href=\"#\" onclick=\"").append(
								titleLink.toString()).append("\"> ").append(
								title).append("</a></span>");
				innerHtml.append("</td>");

				if ("1".equals(showCreator)) {// 如果设置为显示作者，则显示作者信息
					innerHtml.append("<td width=\"80px\">");
					if (tos.getTaskRecvId() == null) {
						innerHtml.append("<span class =\"linkgray\">").append(
								"").append("</span>");
					} else {
						TUumsBaseUser u = userService
								.getUserInfoByUserId(tos.getTOsWorktask()
										.getWorktaskUser());
						innerHtml.append("<span class =\"linkgray\">").append

						(u.getUserName()).append("</span>");
					}
					innerHtml.append("</td>");
				}
				if ("1".equals(showDate)) {// 如果设置为显示日期，则显示公文生效日期信息
					innerHtml.append("<td width=\"100px\">");
					innerHtml.append("<span class =\"linkgray\">").append(
							st.format(tos.getTaskSendTime())).append("</span>");
					innerHtml.append("</td>");
				}
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
		}
		innerHtml
				.append(
						"<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"")
				.append(link.toString()).append("\"> ").append("<img src='../oa/image/more.gif' border='0' complete='complete'/></a></div>");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	// 下载附件
	public void download() throws Exception {
		HttpServletResponse response = super.getResponse();
		TOsTaskAttach file = iWorkTodoService.getAttachById(attachId);
		response.reset();
		response.setContentType("application/x-download"); // windows
		OutputStream output = null;
		int bytesum = 0;
		int byteread = 0;
		try {
			String url = file.getAttachFilePath();
			File fileObj = new File(url + File.separator + file.getAttachId()
					+ "." + file.getAttachFileType());
			FileInputStream inStream = new FileInputStream(fileObj);
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(file.getAttachFileName().getBytes("gb2312"),
							"iso8859-1"));
			output = response.getOutputStream();
			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				output.write(buffer, 0, byteread);
			}
			inStream.close();
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				output = null;
			}
		}
	}

	// 我的办理件查询
	public String getSelectSendTodo() {
		System.out.println("11111111111111");
		return "personal";
	}

	public void setSelectTaskState(String selectTaskState) {
		this.selectTaskState = selectTaskState;
	}

	public String getSelectTaskState() {
		return selectTaskState;
	}

	public TOsManagementSummary getSummary() {
		return summary;
	}

	public void setSummary(TOsManagementSummary summary) {
		this.summary = summary;
	}

	public List<TOsManagementSummary> getSummaryList() {
		return summaryList;
	}

	public void setSummaryList(List<TOsManagementSummary> summaryList) {
		this.summaryList = summaryList;
	}

	public String getDelAttIds() {
		return delAttIds;
	}

	public void setDelAttIds(String delAttIds) {
		this.delAttIds = delAttIds;
	}

}
