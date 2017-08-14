package com.strongit.oa.noticeconference;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.strongit.oa.bo.TOmConAttach;
import com.strongit.oa.bo.TOmConPerson;
import com.strongit.oa.bo.TOmConType;
import com.strongit.oa.bo.TOmConference;
import com.strongit.oa.bo.TOmConferenceSend;
import com.strongit.oa.bo.TOmDeptreport;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.noticeconference.util.BaseDataExportInfo;
import com.strongit.oa.noticeconference.util.Constants;
import com.strongit.oa.noticeconference.util.ProcessXSL;
import com.strongit.oa.noticeconference.util.StringUtil;
import com.strongit.oa.util.TempPo;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.util.TimeKit;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/*******************************************************************************
 * 
 * @Description: NoticeConferenceAction.java
 * @Date:Mar 26, 2013
 * @Author 万俊龙
 * @Version: V1.0
 * @Copyright: Jiang Xi Strong Co. Ltd. All right reserved.
 */
public class NoticeConferenceAction extends BaseActionSupport<TOmConference> {
	@Autowired
	private INoticeConferenceManager noticeConferenceManager;
	private Page<TOmConference> page = new Page<TOmConference>(15, true);// 会议通知PAGE

	@Autowired
	private IConNoticeAttachManager conticeAttachManager;

	@Autowired
	private IConAcceptManager conferenceSendManager;

	@Autowired
	private IUserService userService;

	private List orgList;

	private File[] attachs; // 附件
	private String[] attachsFileName;// 附件名称
	private String depIds;// 下发通知单位Ids
	private String deptNames;// 下发通知单位名称

	private String conTypeId; // 会议类型id

	private String consendId;// 下发通知单位主键

	private String conNoticeId;// 会议通知编号ID;
	private String conAttachId;// 会议通知附件编号ID；

	private TOmConference model = new TOmConference();

	private Date searchStime; // 搜索开始时间
	private Date searchEtime; // 搜索结束时间
	private Date searchRegendtime;

	private String searchtitle;
	private String searchconferenceAddr;

	private String paramName; // 用户名参数（会员上报）

	private String state;// 会议通知状态

	private String actorSettings;// 展现节点树
	private String topTreeNodeId;// 树形头部节点ID；

	private String attachHtml;// 展现附件浮层
	private String dbobj;// 影藏记录待删除附件的ID号

	private String initDatas;// 初始化下发单位

	private File wordDoc; // 预审单文档
	private String isExitWord;

	private String contence;// 会议内容

	private List<TOmConAttach> atts;
	
	
	
	
	public List<TOmConAttach> getAtts() {
		return atts;
	}

	public void setAtts(List<TOmConAttach> atts) {
		this.atts = atts;
	}

	public String getInitDatas() {
		return initDatas;
	}

	public void setInitDatas(String initDatas) {
		this.initDatas = initDatas;
	}

	public String getConAttachId() {
		return conAttachId;
	}

	public void setConAttachId(String conAttachId) {
		this.conAttachId = conAttachId;
	}

	public String getAttachHtml() {
		return attachHtml;
	}

	public void setAttachHtml(String attachHtml) {
		this.attachHtml = attachHtml;
	}

	public String getDbobj() {
		return dbobj;
	}

	public void setDbobj(String dbobj) {
		this.dbobj = dbobj;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：初始化会议类型
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @throws Exception
	 * @version 1.0
	 * @see
	 */
	protected void initTypeMap() throws Exception {
		List<TOmConType> list = noticeConferenceManager.findAll();
		LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
		typeMap.put("", "");
		for (TOmConType type : list) {
			typeMap.put(type.getContypeId(), type.getContypeName());
		}
		getRequest().setAttribute("typeMap", typeMap);
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：选择下发单位
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String assignDeptListTree() {
		String root = getRequest().getContextPath();
		this.actorSettings = "["
				+ "{name:'单位选择',alias:'org',url:'"
				+ root
				+ "/noticeconference/noticeConference!chooseTree.action',prefix:'o'}"
				+ "]";
		// 初始化已选下派单位
		initDatas = "";
		List<TOmConferenceSend> list = this.noticeConferenceManager
				.findConferenceSendListByConId(this.conNoticeId);
		if (StringUtil.isNotEmpty(list)) {
			for (TOmConferenceSend accept : list) {
				initDatas += "o" + accept.getDeptCode() + ","
						+ accept.getDeptName() + "@";
			}
		}
		return "assignDeptTree";
	}

	public String chooseTree() throws Exception {

		// orgList = addressPerManager.getAllOrgUserList();

		orgList = this.noticeConferenceManager.getAllOrgUserList();
		// --------------------End---------------------
		Set<String> orgList2 = new HashSet<String>(0);
		Set<String> orgList3 = new HashSet<String>(0);
		Object[] temp;
		for (int i = 0; i < orgList.size(); i++) {
			temp = (Object[]) orgList.get(i);
			orgList2.add(String.valueOf(temp[1]));
			orgList3.add(String.valueOf(temp[0]));
		}
		String orgId1 = null, orgId2 = null;
		boolean containsFlag;
		for (int i = 0; i < orgList2.size(); i++) {
			orgId1 = orgList2.iterator().next();
			containsFlag = false;
			Iterator<String> it = orgList3.iterator();
			while (it.hasNext()) {
				orgId2 = it.next();
				if (orgId1.equals(orgId2)) {
					containsFlag = true;
					break;
				}
			}

			if (containsFlag) {
				orgList2.remove(orgId1);
				orgList3.remove(orgId2);
				i = -1;
			} else {
				topTreeNodeId = orgId1;
				break;
			}
		}
		orgList2 = null;
		orgList3 = null;
		return "dep";
	}

	/**
	 * 
	 * 方法简要描述:保存下派单位
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 9, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String saveForConsend() {

		this.noticeConferenceManager.saveForConsend(this.conNoticeId,
				this.depIds);
		this.renderText("true");
		return null;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:展现组织机构树
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @return
	 * @throws Exception
	 * @version 1.0
	 * @see
	 */
	@SuppressWarnings("unchecked")
	public String orgTree() throws Exception {
		User userInfo = userService.getCurrentUser();
		Assert.notNull(userInfo);

		String userId = userInfo.getUserId();
		if (userService.isSystemDataManager(userId)) {
			orgList = userService.getAllOrgInfos();
		} else {
			orgList = userService.getAllOrgInfosByHa(userInfo.getOrgId());
		}
		/*
		 * 去除删除状态的机构信息
		 */
		if (StringUtil.isNotEmpty(orgList)) {
			for (int i = 0; i < orgList.size(); i++) {
				TUumsBaseOrg org = (TUumsBaseOrg) orgList.get(i);
				if ("1".equals(org.getOrgIsdel())) {
					orgList.remove(i);
					i--;
				}
			}

			List<TempPo> list = new LinkedList<TempPo>();
			for (Object obean : orgList) {
				TUumsBaseOrg o = (TUumsBaseOrg) obean;
				String parentId = o.getOrgParentId();
				if (o.getOrgName()!= null && "公文传输单位".equals(o.getOrgName())) {
					parentId = "0";
				}
				String id = o.getOrgId();
				String name = o.getOrgName();
				TempPo po = new TempPo();
				po.setId(id);
				po.setParentId(parentId);
				po.setName(name);
				po.setType("true");// 设置可选
				list.add(po);
			}
			orgList.clear();
			orgList = new ArrayList(list);
			list.clear();
		}
		return "orgTree";
	}

	//测试数据，用于选择输入框提示信息
	public String getMemberInfos() {
		StringBuffer str = new StringBuffer();
		str
				.append("\'万军 <input type=\"hidden\" name=\"hval\" id=\"hval\" value=\"万军#职务#性别#民族#tel#number\"/>\',");
		str
				.append("\'婉君 <input type=\"hidden\" name=\"hval\" id=\"hval\" value=\"婉君#职务#性别#民族#tel#number\"/>\',");
		str
				.append("\'万钧 <input type=\"hidden\" name=\"hval\" id=\"hval\" value=\"万钧#职务#性别#民族#tel#number\"/>\',");

		return this.renderText(str.toString());
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：会议通知上报
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 26, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String toSendMembers() {
		return "send";
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		if (StringUtil.isNotEmpty(conNoticeId)) {
			String[] ids = conNoticeId.split(",");
			for (String conId : ids) {
				if (StringUtil.isNotEmpty(conId)) {
					this.noticeConferenceManager.deleteConNotice(conId);
				}
			}
			this.renderText("ok");
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		initTypeMap();
		return "add";
	}

	/**
	 * 会议相关信息的编辑
	 * 
	 * @description: 1、判断会议内容是否为空，isExitWord=true or isExitWord=false 2、
	 *               获取下发单位组织机构ids，用于初始化下发单位选择树功能 3、获取会议通知附件信息
	 * @author huhl
	 * @return
	 * @throws Exception
	 */
	public String edit() throws Exception {
		if (StringUtil.isEmpty(conNoticeId)) {
			return input();
		}
		initTypeMap();
		model = this.noticeConferenceManager.getConNoticeByConId(conNoticeId);

		/**
		 * 1、判断会议内容是否为空，isExitWord=true or isExitWord=false
		 */
		if (model.getNoticecontent() != null) {
			this.isExitWord = "true";
		} else {
			this.isExitWord = "flase";
		}
		if (StringUtil.isEmpty(model)) {
			model = new TOmConference();
		}

		if (StringUtil.isNotEmpty(model.getNoticecontent())) {

			this.contence = new String(model.getNoticecontent(), "gbk");
		}

		/**
		 * 2、获取下发单位组织机构ids，用于初始化下发单位选择树功能
		 */
		StringBuffer orgNames = new StringBuffer();
		this.depIds = this.noticeConferenceManager.getAcceptOrgIdsByConid(
				conNoticeId, orgNames);
		this.deptNames = orgNames.toString();

		/**
		 * 3、获取会议通知附件信息
		 */
		//attachHtml = this.noticeConferenceManager.getConAttachHtmlByConId(this
		//		.getRequest(), conNoticeId);
		
		atts = conticeAttachManager.getAttachsByAttachConId(model.getConferenceId(), true);
		System.out.println(attachHtml);
		if(model.getTsConfersort()!=null){
		conTypeId = model.getTsConfersort().getContypeId();
		}
		return "edit";
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：下载会议通知附件功能
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @return
	 * @throws Exception
	 * @version 1.0
	 * @see
	 */
	public String downloadConAttach() throws Exception {

		HttpServletResponse response = super.getResponse();
		if (StringUtil.isNotEmpty(conAttachId)) {
			TOmConAttach newConAttch = this.noticeConferenceManager
					.getConAttachByAttachId(conAttachId);
			if (StringUtil.isNotEmpty(newConAttch)) {
				response.reset();
				response.setContentType("application/x-download"); // windows
				OutputStream output = null;
				response.addHeader("Content-Disposition",
						"attachment;filename="
								+ new String(newConAttch.getAttachFileName()
										.getBytes("gb2312"), "iso8859-1"));
			 
				output = response.getOutputStream();
				byte[] bytes = newConAttch.getAttachContent();
				output.write(bytes);
				output.flush();

				newConAttch = null;
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					output = null;
				}
			} else {
				OutputStream output = response.getOutputStream();
				output.write(new Byte("未能找到下载文件"));
				output.close();
				output = null;
			}
		} else {
			OutputStream output = response.getOutputStream();
			output.write(new Byte("未能找到下载文件"));
			output.close();
			output = null;
		}
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		User user = userService.getCurrentUser();
		Assert.notNull(user);
		if (!"1".equals(user.getUserIsSupManager())) {
			model.setConferenceRest1(user.getOrgId());
		}
		
		
		if (StringUtil.isNotEmpty(this.searchtitle)) {
			searchtitle = new String(searchtitle.getBytes("iso-8859-1"), "UTF-8");  
			String title = searchtitle.replace("%", "\'\'%");
			model.setConferenceTitle(title);
		}
		if (StringUtil.isNotEmpty(this.searchconferenceAddr)) {
			searchconferenceAddr = new String(searchconferenceAddr.getBytes("iso-8859-1"), "UTF-8");
			String addr = searchconferenceAddr.replace("%", "\'\'%");
			model.setConferenceAddr(addr);
		}
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat pim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(searchStime!=null){
			String btime = sim.format(searchStime);
			searchStime = pim.parse(btime+" 00:00:00");
		}
		if(searchEtime!=null){
			String etime = sim.format(searchEtime);
			searchEtime = pim.parse(etime+" 23:59:59");
		}
	 
		if ("1".equals(state)) {
			page = noticeConferenceManager.queryIssiedList(page, model,
					searchStime, searchEtime, searchRegendtime);
			return "sendlist";// 返回已发会议通知页面
		} else {
			page = noticeConferenceManager.queryDraftList(page, model,
					searchStime, searchEtime, searchRegendtime);
			return SUCCESS;
		}

	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
	}

	// 保存下发
	public String saveAndSend() {
		// TODO Auto-generated method stub
		if (StringUtil.isNotEmpty(conTypeId)) {
			TOmConType conferSort = this.noticeConferenceManager
					.getConferSortById(conTypeId);
		
			if (StringUtil.isNotEmpty(conferSort)) {
				try {
					/*
					 * if (wordDoc != null) { FileInputStream fs = new
					 * FileInputStream(wordDoc); byte[] fileBuffer = new
					 * byte[(int) wordDoc.length()]; fs.read(fileBuffer);
					 * model.setNoticecontent(fileBuffer); }
					 */
					if (StringUtil.isNotEmpty(this.contence)) {
						model.setNoticecontent(this.contence.getBytes());
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				//设置发起会议的用户ID
				User user = userService.getCurrentUser();
				model.setConferenceRest1(user.getOrgId());
				model.setTsConfersort(conferSort);
				model.setConferenceState(Constants.CONFERENCE_STATE_ISSIED);
				this.noticeConferenceManager.saveConferInfo(model, depIds,
						attachsFileName, attachs);
			}
		}
		else{
			if (StringUtil.isNotEmpty(this.contence)) {
				model.setNoticecontent(this.contence.getBytes());
			}
			User user = userService.getCurrentUser();
			model.setConferenceRest1(user.getOrgId());
			model.setConferenceState(Constants.CONFERENCE_STATE_ISSIED);
			this.noticeConferenceManager.saveConferInfo(model, depIds,
					attachsFileName, attachs);
		}
		this.renderText("ok");
		return null;

	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		if (StringUtil.isNotEmpty(conTypeId)) {
			TOmConType conferSort = this.noticeConferenceManager
					.getConferSortById(conTypeId);
			if (StringUtil.isNotEmpty(conferSort)) {
				try {
					/*
					 * if (wordDoc != null) { FileInputStream fs = new
					 * FileInputStream(wordDoc); byte[] fileBuffer = new
					 * byte[(int) wordDoc.length()]; fs.read(fileBuffer);
					 * model.setNoticecontent(fileBuffer); }
					 */
					if (StringUtil.isNotEmpty(this.contence)) {
						model.setNoticecontent(this.contence.getBytes());
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				User user = userService.getCurrentUser();
				model.setConferenceRest1(user.getOrgId());
				model.setTsConfersort(conferSort);
				model.setConferenceState(Constants.CONFERENCE_STATE_DRAFT);
				this.noticeConferenceManager.saveConferInfo(model, depIds,
						attachsFileName, attachs);
			}
		}
		else{
			if (StringUtil.isNotEmpty(this.contence)) {
				model.setNoticecontent(this.contence.getBytes());
			}
			User user = userService.getCurrentUser();
			model.setConferenceRest1(user.getOrgId());
			model.setConferenceState(Constants.CONFERENCE_STATE_DRAFT);
			this.noticeConferenceManager.saveConferInfo(model, depIds,
					attachsFileName, attachs);
		}
		this.renderText("ok");
		return null;
	}

	/**
	 * @Title: openWorkflowWord
	 * @Description: 从数据库中打开存在的大中型会议通知单(word格式)
	 * @detailed description：千航控件调用的打开保存在会议表中的会议通知单;返回的是一个流的形式
	 * @param：
	 * @return：void
	 * @author：胡海亮
	 * @time：2013-4-10
	 * @throws
	 */
	public void openWorkflowWord() {

		conNoticeId = getRequest().getParameter("conNoticeId");
		System.out.println("conNoticeId::" + conNoticeId);
		model = noticeConferenceManager.getConNoticeByConId(conNoticeId);
		byte[] buf = model.getNoticecontent();
		if (buf != null) {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.reset();
			response.setContentType("application/octet-stream");
			OutputStream output = null;
			try {
				output = response.getOutputStream();
				output.write(buf);
			} catch (Exception e) {
				logger.error("写入到HttpResponse中发生异常。", e);
			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						logger.error("关闭输出流时发生异常", e);
					}
				}
			}
		}

	}

	/***************************************************************************
	 * 
	 * 方法简要描述:判断是否含有通知下发对象
	 * 
	 * @return 1 表示还有下发对象 0 表示没有下发对象 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String hasConferenceSend() {

		StringBuffer ids = new StringBuffer();
		if (StringUtil.isNotEmpty(conNoticeId)) {
			String[] conIds = conNoticeId.split(",");
			for (String conId : conIds) {
				// flag 为true 会议通知含有下发单位
				boolean flag = this.noticeConferenceManager
						.hasConferenceSend(conId);
				if (!flag) {
					TOmConference conference = this.noticeConferenceManager
							.getConNoticeByConId(conId);
					if (StringUtil.isNotEmpty(conference)) {
						ids.append(conference.getConferenceTitle()).append(",");
					}
				}
			}

		}
		if (StringUtil.isEmpty(ids.toString())) {
			this.renderText("1");
		} else {
			ids = ids.deleteCharAt(ids.lastIndexOf(","));
			this.renderText(ids.toString());
		}
		return null;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：发送下发单位
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String conferenceSend() {
		if (StringUtil.isNotEmpty(conNoticeId)) {
			if (StringUtil.isNotEmpty(conNoticeId)) {
				String[] conIds = conNoticeId.split(",");
				for (String conId : conIds) {
					this.noticeConferenceManager.sendConference(conId);
				}
			}
		}
		this.renderText("1");
		return null;
	}

	// 查看会议下派单位的上报情况
	// 下派总数 已上报单位个数 未签收单位 已签收单位
	public String viewReport() {
		int reportSend = 0; // 记录已上报的单位个数
		int waitSend = 0; // 未签收单位
		int total = 0; // 上报总人数
		StringBuffer html = new StringBuffer();
		html
				.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" class=\"table1\" id=\"table\"  style=\" OVERFLOW: scroll\">");

		html.append("<tr>");
		html
				.append("<th width=\"40px\" height=\"22\" align=\"left\" class=\"biao_bg3\">&nbsp;&nbsp;</th>");
		html
				.append("<th width=\"39%\" height=\"22\" class=\"biao_bg3\" showsize=\"1000\" title=\"单位名称\" colspan=\"2\">单位名称</th>");
		html
				.append("<th width=\"39%\" height=\"22\" class=\"biao_bg3\" showsize=\"1000\" title=\"办理进度\" colspan=\"2\">办理进度</th>");
		html
				.append("<th width=\"39%\" height=\"22\" class=\"biao_bg3\" showsize=\"1000\" title=\"操作\" colspan=\"2\">操作<input id='chkall' type='checkbox' onclick='check()'></th>");
		html.append("</tr>");

		List<TOmConferenceSend> list = this.conferenceSendManager
				.findByconNoticeId(this.conNoticeId);
		if (StringUtil.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				int num = i % 2 == 0 ? 1 : 2;
				TOmConferenceSend entity = (TOmConferenceSend) list.get(i);

				Set<TOmDeptreport> reportPersonLst = entity.getTOmDeptreports();

				if (Constants.CONFERENCE_SEND_RECVSTATE_DEFAULT.equals(entity
						.getRecvState())) {
					waitSend += 1;
				} else if (Constants.CONFERENCE_SEND_RECVSTATE_REPORTED
						.equals(entity.getRecvState())
						|| Constants.CONFERENCE_SEND_RECVSTATE_ACCEPTED
								.equals(entity.getRecvState())) {
					reportSend += 1;
				}
				html.append("<tr class='td" + num
						+ "'  style='height:30px;line-height:2.0'>");
				html.append("<td  onclick='view(this," + i
						+ ");'><b>＋</b></td>");
				html
						.append("<td align='left' colspan=\"2\"><b>"
								+ entity.getDeptName()
								+ ((StringUtil.isNotEmpty(reportPersonLst) && reportPersonLst
										.size() > 0) ? "("
										+ reportPersonLst.size() + "人)" : "")
								+ "</b></td>");
				html.append("<td align='center' colspan=\"2\"><b>"
						+ Constants.CONFERENCE_SEND_STATE_MAP.get(entity
								.getRecvState()) + "</b></td>");
				// 已上报 含有 返回重办和办理确认 操作
				if (Constants.CONFERENCE_SEND_RECVSTATE_REPORTED.equals(entity
						.getRecvState())) {
					html
							.append("<td align='center' colspan=\"2\"><a href='#' onclick='reback(this,\""
									+ entity.getSendconId()
									+ "\")'>返回重办 </a>&nbsp;|&nbsp;<input type='checkbox' name='chkItem' value=\""+entity.getSendconId()+"\" id=\""+entity.getSendconId()+"\" onclick='getValue(\""+entity.getSendconId()+"\")' >是否办理</td>");

				} else {
					html.append("<td align='center' colspan=\"2\"></td>");

				}

				/*
				 * html .append("<td align='center' colspan=\"2\"><b>" +
				 * (StringUtil.isNotEmpty(reportPersonLst) ? reportPersonLst
				 * .size() : 0) + "</b></td>");
				 */
				html.append("</tr>");
				StringBuffer main = new StringBuffer();
				main.append("<tbody id='dv" + i + "' style='display:none'>");
				if (StringUtil.isNotEmpty(reportPersonLst)) {
					if (reportPersonLst.size() > 0) {
						total += reportPersonLst.size();
						// 构造上报人员信息查看表
						main.append("<tr>");
						main.append("<th>&nbsp;</th>");
						main.append("<th>名称</th>");
						main.append("<th>职务</th>");
						main.append("<th>手机号</th>");
						main.append("<th>办公电话</th>");
						main.append("<th>&nbsp;</th>");
						main.append("<th>&nbsp;</th>");
						main.append("</tr>");

						for (TOmDeptreport report : reportPersonLst) {
							main.append("<tr  bgcolor='#F5F5DC'>");
							TOmConPerson person = report.getConferee();// this.conferenceSendManager.getPersonById(report.getConferee().getPersonId());
							if (StringUtil.isNotEmpty(person)) {
								main.append("<td >&nbsp;</td>");
								main.append("<td align=\"center\">"
										+ person.getPersonName() + "</td>");
								main.append("<td align=\"center\">"
										+ person.getPersonPost() + "</td>");
								main.append("<td align=\"center\">"
										+ person.getPersonMobile() + "</td>");
								main.append("<td align=\"center\">"
										+ person.getPersonPhone() + "</td>");
								main.append("<th>&nbsp;</th>");
								main.append("<th>&nbsp;</th>");
							}
							main.append("</tr>");
						}
					}
				}
				main.append("</tbody>");

				this.getRequest().setAttribute("reportSend", reportSend);
				this.getRequest().setAttribute("waitSend", waitSend);
				this.getRequest().setAttribute("total", total);
				html.append(main.toString());
			}
		}

		html.append("</table>");
		this.getRequest().setAttribute("contentHtml", html.toString());
		return "viewreport";
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：返回重办
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 16, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String reBack() {
		TOmConferenceSend entity = this.conferenceSendManager
				.getModel(this.consendId);
		if (StringUtil.isNotEmpty(entity)) {
			entity.setRecvState(Constants.CONFERENCE_SEND_RECVSTATE_REBACK); // 返回重办
			Set<TOmDeptreport> reportLst = entity.getTOmDeptreports();
			for (TOmDeptreport report : reportLst) {
				report.setState(Constants.DEPT_REPORT_WAIT_STATE);
			}
			try {
				this.conferenceSendManager.update(entity);
				this.renderText(Constants.CONFERENCE_SEND_STATE_MAP.get(
						Constants.CONFERENCE_SEND_RECVSTATE_REBACK).toString());
			} catch (Exception e) {
				this.renderText("ex");
			}
		} else {
			// 异步操作，该单位已被废除
			this.renderText("-1");
		}
		return null;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：办理确认
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 16, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String sureToConsend() {

		String conAll = getRequest().getParameter("conAll");
		String[] con = conAll.split(",");
		if(con.length>0){
		for(int i=0;i<con.length;i++){
		TOmConferenceSend entity = this.conferenceSendManager
				.getModel(con[i]);
		if (StringUtil.isNotEmpty(entity)) {
			entity.setRecvState(Constants.CONFERENCE_SEND_RECVSTATE_ACCEPTED); // 返回重办
			entity.getTOmConference().setConferenceState(
					Constants.CONFERENCE_STATE_END);
			Set<TOmDeptreport> reportLst = entity.getTOmDeptreports();
			for (TOmDeptreport report : reportLst) {
				report.setState(Constants.DEPT_REPORT_ISSIED_STATE);
			}
			try {
				this.conferenceSendManager.update(entity);
				this.renderText(Constants.CONFERENCE_SEND_STATE_MAP.get(
						Constants.CONFERENCE_SEND_RECVSTATE_ACCEPTED)
						.toString());
			} catch (Exception e) {
				this.renderText("ex");
			}
		} else {
			// 异步操作，该单位已被废除
			this.renderText("-1");
		}
		}
		}
		return null;

	}

	public TOmConference getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	public String getConNoticeId() {
		return conNoticeId;
	}

	public void setConNoticeId(String conNoticeId) {
		this.conNoticeId = conNoticeId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:会议通知入口，跳转到tab分页
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 29, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String container() {
		if (StringUtil.isEmpty(state)) {
			state = "0";
		}
		return "container";
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：展现已办理会议列表
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 18, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @throws ParseException 
	 * @see
	 */
	public String handleConference() throws ParseException {
		if (StringUtil.isNotEmpty(this.searchtitle)) {
			String title = searchtitle.replace("%", "\'\'%");
			model.setConferenceTitle(title);
		}

		if (StringUtil.isNotEmpty(this.searchconferenceAddr)) {
			String addr = searchconferenceAddr.replace("%", "\'\'%");
			model.setConferenceAddr(addr);
		}
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat pim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(searchStime!=null){
			String btime = sim.format(searchStime);
			searchStime = pim.parse(btime+" 00:00:00");
		}
		if(searchEtime!=null){
			String etime = sim.format(searchEtime);
			searchEtime = pim.parse(etime+" 23:59:59");
		}
	 
		page = noticeConferenceManager.queryHandleList(page, model,
				searchStime, searchEtime, searchRegendtime);
		return "handle";
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：查看导出名册
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 18, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String viewHandleReport() {
		int reportSend = 0; // 记录已上报的单位个数
		int waitSend = 0; // 未签收单位
		int total = 0; // 上报总人数
		StringBuffer html = new StringBuffer();
		html
				.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" class=\"table1\" id=\"table\"  style=\" OVERFLOW: scroll\">");

		html.append("<tr>");
		html
				.append("<th width=\"40px\" height=\"22\" align=\"left\" class=\"biao_bg3\">&nbsp;&nbsp;</th>");
		html
				.append("<th width=\"39%\" height=\"22\" class=\"biao_bg3\" showsize=\"1000\" title=\"单位名称\" colspan=\"2\">单位名称</th>");
		html
				.append("<th width=\"39%\" height=\"22\" class=\"biao_bg3\" showsize=\"1000\" title=\"办理进度\" colspan=\"2\">办理进度</th>");
		html
				.append("<th width=\"39%\" height=\"22\" class=\"biao_bg3\" showsize=\"1000\" title=\"人数\" colspan=\"2\">人数</th>");
		html.append("</tr>");

		List<TOmConferenceSend> list = this.conferenceSendManager
				.findByconNoticeId(this.conNoticeId);
		if (StringUtil.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				int num = i % 2 == 0 ? 1 : 2;
				TOmConferenceSend entity = (TOmConferenceSend) list.get(i);

				Set<TOmDeptreport> reportPersonLst = entity.getTOmDeptreports();

				if (Constants.CONFERENCE_SEND_RECVSTATE_DEFAULT.equals(entity
						.getRecvState())) {
					waitSend += 1;
				} else if (Constants.CONFERENCE_SEND_RECVSTATE_REPORTED
						.equals(entity.getRecvState())
						|| Constants.CONFERENCE_SEND_RECVSTATE_ACCEPTED
								.equals(entity.getRecvState())) {
					reportSend += 1;
				}
				html.append("<tr class='td" + num
						+ "'  style='height:30px;line-height:2.0'>");
				html.append("<td  onclick='view(this," + i
						+ ");'><b>＋</b></td>");
				html.append("<td align='left' colspan=\"2\"><b>"
						+ entity.getDeptName() + "</b></td>");
				html.append("<td align='center' colspan=\"2\"><b>"
						+ Constants.CONFERENCE_SEND_STATE_MAP.get(entity
								.getRecvState()) + "</b></td>");
				html
						.append("<td align='center' colspan=\"2\">"
								+ ((StringUtil.isNotEmpty(reportPersonLst) && reportPersonLst
										.size() > 0) ? reportPersonLst.size()
										: 0) + "</td>");

				html.append("</tr>");
				StringBuffer main = new StringBuffer();
				main.append("<tbody id='dv" + i + "' style='display:none'>");
				if (StringUtil.isNotEmpty(reportPersonLst)) {
					if (reportPersonLst.size() > 0) {
						total += reportPersonLst.size();
						// 构造上报人员信息查看表
						main.append("<tr>");
						main.append("<th>&nbsp;</th>");
						main.append("<th>名称</th>");
						main.append("<th>职务</th>");
						main.append("<th>手机号</th>");
						//main.append("<th>性别</th>");
						//main.append("<th>民族</th>");
						main.append("<th>办公电话</th>");
						main.append("<th>&nbsp;</th>");
						main.append("<th>&nbsp;</th>");
						main.append("</tr>");

						for (TOmDeptreport report : reportPersonLst) {
							
							main.append("<tr  bgcolor='#F5F5DC'>");
							TOmConPerson person = report.getConferee();// this.conferenceSendManager.getPersonById(report.getConferee().getPersonId());
							if(person.getPersonPhone()==null){
								person.setPersonPhone("");
							}
							if (StringUtil.isNotEmpty(person)) {
								main.append("<td >&nbsp;</td>");
								main.append("<td align=\"center\">"
										+ person.getPersonName() + "</td>");
								
								main.append("<td align=\"center\">"
										+ person.getPersonPost() + "</td>");
								//main.append("<td align=\"center\">"
								//		+ person.getPersonSax() + "</td>");
								//main.append("<td align=\"center\">"
								//		+ person.getPersonNation() + "</td>");
								main.append("<td align=\"center\">"
										+ person.getPersonMobile() + "</td>");
								main.append("<td align=\"center\">"
										+ person.getPersonPhone() + "</td>");
								main.append("<th>&nbsp;</th>");
								main.append("<th>&nbsp;</th>");
								//main.append("<th>&nbsp;</th>");
							}
							main.append("</tr>");
						}
					}
				}
				main.append("</tbody>");

				this.getRequest().setAttribute("reportSend", reportSend);
				this.getRequest().setAttribute("waitSend", waitSend);
				this.getRequest().setAttribute("total", total);
				html.append(main.toString());
			}
		}

		html.append("</table>");
		this.getRequest().setAttribute("contentHtml", html.toString());

		return "export";
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:导出已处理单位的会议上报名册
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 18, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @throws Exception
	 * @see
	 */
	public String exportMember() throws Exception {
		List<TOmConferenceSend> list = this.conferenceSendManager
				.findByconNoticeId(this.conNoticeId);
		List<TOmDeptreport> reportLst = new ArrayList();
		if (StringUtil.isNotEmpty(list)) {
			for (TOmConferenceSend send : list) {
				// 判断单位上报是否处理
				if (Constants.CONFERENCE_SEND_RECVSTATE_ACCEPTED.equals(send
						.getRecvState())) {
					reportLst.addAll(send.getTOmDeptreports());
				}
			}
		}

		TOmConference conference = this.noticeConferenceManager
				.getConNoticeByConId(conNoticeId);
		try {
			exportfils(conference.getConferenceTitle()+"参会人员表", conference
					.getConferenceTitle(), conference.getConferenceTitle(),
					reportLst);
			this.renderText("1");
		} catch (Exception e) {
			this.renderText(e.getMessage());
		}

		return null;
	}
	
	/***************************************************************************
	 * 
	 * 方法简要描述:导出签到表
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 18, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @throws Exception
	 * @see
	 */
	public String exportCheck() throws Exception {
		List<TOmConferenceSend> list = this.conferenceSendManager
				.findByconNoticeId(this.conNoticeId);
		List<TOmDeptreport> reportLst = new ArrayList();
		if (StringUtil.isNotEmpty(list)) {
			for (TOmConferenceSend send : list) {
				// 判断单位上报是否处理
				if (Constants.CONFERENCE_SEND_RECVSTATE_ACCEPTED.equals(send
						.getRecvState())) {
					reportLst.addAll(send.getTOmDeptreports());
				}
			}
		}

		TOmConference conference = this.noticeConferenceManager
				.getConNoticeByConId(conNoticeId);
		try {
			exportCheckfils(conference.getConferenceTitle()+"签到表", conference
					.getConferenceTitle(), conference.getConferenceTitle(),
					reportLst);
			this.renderText("1");
		} catch (Exception e) {
			this.renderText(e.getMessage());
		}

		return null;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：导出报名名册 excel
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 18, 2013
	 * @Author 万俊龙
	 * @param fileName
	 * @param sheetTitle
	 * @param sheetName
	 * @param reportLst
	 * @version 1.0
	 * @throws Exception
	 * @see
	 */
	private void exportfils(String fileName, String sheetTitle,
			String sheetName, List<TOmDeptreport> reportLst) throws Exception {
		try {

			HttpServletResponse response = getResponse();

			// 创建EXCEL对象
			BaseDataExportInfo export = new BaseDataExportInfo();
			String str = StringUtil.toUtf8String(fileName);
			export.setWorkbookFileName(str);
			export.setSheetTitle(sheetTitle);
			export.setSheetName(sheetName);
			// 描述行信息
			List<String> tableHead = new ArrayList<String>();
			tableHead.add("所属部门");
			tableHead.add("姓名");
			tableHead.add("职务");
			//tableHead.add("性别");
			//tableHead.add("民族");
			tableHead.add("手机号");
			tableHead.add(" ");
			//tableHead.add("办公电话");
			tableHead.add("所属部门");
			tableHead.add("姓名");
			tableHead.add("职务");
			tableHead.add("手机号");
			export.setTableHead(tableHead);

			// 获取导出信息
			List rowList = new ArrayList();
			Map rowhigh = new HashMap();
			int rownum = 0;
			String nowdate = TimeKit.formatDate(new Date(),
					"yyyy-MM-dd HH:mm:ss");
			if (StringUtil.isNotEmpty(reportLst)) {
				for (int i=0;i<reportLst.size(); i=i+2) {
					//for (TOmDeptreport entity : reportLst) {
						TOmDeptreport entity = reportLst.get(i);
						TOmDeptreport entity1 = new TOmDeptreport();
						if(reportLst.size()>=2&&i<=reportLst.size()-2){
							entity1 = reportLst.get(i+1);
						}
					Vector cols = new Vector();
					if(entity.getTOmConferenceSend().getDeptName()!=null){
						cols.add(entity.getTOmConferenceSend().getDeptName()
								.toString());
					}
					if(entity.getConferee().getPersonName()!=null){
						cols.add(entity.getConferee().getPersonName().toString());
					}
					if(entity.getConferee().getPersonPost()!=null){
						cols.add(entity.getConferee().getPersonPost().toString());
					}
					/*if(entity.getConferee().getPersonSax()!=null){
						cols.add(entity.getConferee().getPersonSax().toString());
					}
					if(entity.getConferee().getPersonNation()!=null){
						cols.add(entity.getConferee().getPersonNation().toString());
					}*/
					if(entity.getConferee().getPersonMobile()!=null){
						cols.add(entity.getConferee().getPersonMobile().toString());
					}
					cols.add(" ");
					if(entity1.getReportingId()!=null){
						if(entity.getTOmConferenceSend().getDeptName()!=null){
							cols.add(entity.getTOmConferenceSend().getDeptName()
									.toString());
						}
						if(entity.getConferee().getPersonName()!=null){
							cols.add(entity.getConferee().getPersonName().toString());
						}
						if(entity.getConferee().getPersonPost()!=null){
							cols.add(entity.getConferee().getPersonPost().toString());
						}
						/*if(entity.getConferee().getPersonSax()!=null){
							cols.add(entity.getConferee().getPersonSax().toString());
						}
						if(entity.getConferee().getPersonNation()!=null){
							cols.add(entity.getConferee().getPersonNation().toString());
						}*/
						if(entity.getConferee().getPersonMobile()!=null){
							cols.add(entity.getConferee().getPersonMobile().toString());
						}
					}
					/*if(entity.getConferee().getPersonPhone()!=null){
						cols.add(entity.getConferee().getPersonPhone().toString());
					}*/
					rowList.add(cols);
				}
			}

			export.setRowList(rowList);
			export.setRowHigh(rowhigh);
			ProcessXSL xsl = new ProcessXSL();
			xsl.createWorkBookSheet1(export);
			xsl.writeWorkBook(response);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	
	/***************************************************************************
	 * 
	 * 方法简要描述：导出签到表excel
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 18, 2013
	 * @Author 杨智超
	 * @param fileName
	 * @param sheetTitle
	 * @param sheetName
	 * @param reportLst
	 * @version 1.0
	 * @throws Exception
	 * @see
	 */
	private void exportCheckfils(String fileName, String sheetTitle,
			String sheetName, List<TOmDeptreport> reportLst) throws Exception {
		try {

			HttpServletResponse response = getResponse();

			// 创建EXCEL对象
			BaseDataExportInfo export = new BaseDataExportInfo();
			String str = StringUtil.toUtf8String(fileName);
			export.setWorkbookFileName(str);
			export.setSheetTitle(sheetTitle);
			export.setSheetName(sheetName);
			// 描述行信息
			List<String> tableHead = new ArrayList<String>();
			tableHead.add("所属部门");
			tableHead.add("姓名");
			tableHead.add("职务");
			tableHead.add("签到");
			tableHead.add(" ");
			tableHead.add("所属部门");
			tableHead.add("姓名");
			tableHead.add("职务");
			tableHead.add("签到");
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if(reportLst.get(0).getTOmConferenceSend().getTOmConference().getConferenceStime()!=null){
				tableHead.add("会议时间："+sim.format(reportLst.get(0).getTOmConferenceSend().getTOmConference().getConferenceStime()));
			}
			else{
				tableHead.add("会议时间："+"");
			}
			if(reportLst.get(0).getTOmConferenceSend().getTOmConference().getConferenceAddr()!=null){
				tableHead.add("会议地点："+reportLst.get(0).getTOmConferenceSend().getTOmConference().getConferenceAddr());
			}
			else{
				tableHead.add("会议地点："+"");
			}
			if(reportLst.get(0).getTOmConferenceSend().getTOmConference().getConferenceUndertaker()!=null){
				tableHead.add("承办单位："+reportLst.get(0).getTOmConferenceSend().getTOmConference().getConferenceUndertaker());
			}
			else{
				tableHead.add("承办单位："+"");
			}
			export.setTableHead(tableHead);

			// 获取导出信息
			List rowList = new ArrayList();
			Map rowhigh = new HashMap();
			int rownum = 0;
			String nowdate = TimeKit.formatDate(new Date(),
					"yyyy-MM-dd HH:mm:ss");
			if (StringUtil.isNotEmpty(reportLst)) {
				for (int i=0;i<reportLst.size(); i=i+2) {
				//for (TOmDeptreport entity : reportLst) {
					TOmDeptreport entity = reportLst.get(i);
					TOmDeptreport entity1 = new TOmDeptreport();
					if(reportLst.size()>=2&&i<=reportLst.size()-2){
						entity1 = reportLst.get(i+1);
					}
					
					Vector cols = new Vector();
					if(entity.getTOmConferenceSend().getDeptName()!=null){
						cols.add(entity.getTOmConferenceSend().getDeptName()
								.toString());
					}
					if(entity.getConferee().getPersonName()!=null){
						cols.add(entity.getConferee().getPersonName().toString());
					}
					if(entity.getConferee().getPersonPost()!=null){
						cols.add(entity.getConferee().getPersonPost().toString());
					}
					
					//cols.add(entity.getConferee().getPersonPhone().toString());
					//cols.add(entity.getTOmConferenceSend().getTOmConference().getConferenceAddr());
					cols.add(" ");
					cols.add(" ");
					//cols.add(" ");
					if(entity1.getReportingId()!=null){
					if(entity1.getTOmConferenceSend().getDeptName()!=null){
						cols.add(entity1.getTOmConferenceSend().getDeptName()
								.toString());
					}
					if(entity1.getConferee().getPersonName()!=null){
						cols.add(entity1.getConferee().getPersonName().toString());
					}
					if(entity1.getConferee().getPersonPost()!=null){
						cols.add(entity1.getConferee().getPersonPost().toString());
					}
					
					//cols.add(entity.getConferee().getPersonPhone().toString());
					//cols.add(entity.getTOmConferenceSend().getTOmConference().getConferenceAddr());
					cols.add(" ");
					}
					rowList.add(cols);
				}
			}
			/*Vector cols1 = new Vector();
			if(reportLst.get(0).getTOmConferenceSend().getTOmConference().getConferenceUsertel()!=null){
			cols1.add("会议电话："+reportLst.get(0).getTOmConferenceSend().getTOmConference().getConferenceUsertel());
			}
			else{
				cols1.add("会议电话："+"");
			}
			if(reportLst.get(0).getTOmConferenceSend().getTOmConference().getConferenceAddr()!=null){
				cols1.add("会议地点："+reportLst.get(0).getTOmConferenceSend().getTOmConference().getConferenceAddr());
			}
			else{
				cols1.add("会议地点："+"");
			}
			rowList.add(cols1);*/
			export.setRowList(rowList);
			export.setRowHigh(rowhigh);
			ProcessXSL xsl = new ProcessXSL();
			xsl.createWorkBookSheet1(export);
			xsl.writeWorkBook(response);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public File[] getAttachs() {
		return attachs;
	}

	public void setAttachs(File[] attachs) {
		this.attachs = attachs;
	}

	public Page<TOmConference> getPage() {
		return page;
	}

	public void setPage(Page<TOmConference> page) {
		this.page = page;
	}

	public Date getSearchRegendtime() {
		return searchRegendtime;
	}

	public void setSearchRegendtime(Date searchRegendtime) {
		this.searchRegendtime = searchRegendtime;
	}

	public void setModel(TOmConference model) {
		this.model = model;
	}

	public String[] getAttachsFileName() {
		return attachsFileName;
	}

	public void setAttachsFileName(String[] attachsFileName) {
		this.attachsFileName = attachsFileName;
	}

	public String getDepIds() {
		return depIds;
	}

	public void setDepIds(String depIds) {
		this.depIds = depIds;
	}

	public String getConTypeId() {
		return conTypeId;
	}

	public void setConTypeId(String conTypeId) {
		this.conTypeId = conTypeId;
	}

	public List getOrgList() {
		return orgList;
	}

	public void setOrgList(List orgList) {
		this.orgList = orgList;
	}

	public String getActorSettings() {
		return actorSettings;
	}

	public void setActorSettings(String actorSettings) {
		this.actorSettings = actorSettings;
	}

	public String getTopTreeNodeId() {
		return topTreeNodeId;
	}

	public void setTopTreeNodeId(String topTreeNodeId) {
		this.topTreeNodeId = topTreeNodeId;
	}

	public String getDeptNames() {
		return deptNames;
	}

	public void setDeptNames(String deptNames) {
		this.deptNames = deptNames;
	}

	public String getSearchtitle() {
		return searchtitle;
	}

	public void setSearchtitle(String searchtitle) {
		this.searchtitle = searchtitle;
	}

	public String getSearchconferenceAddr() {
		return searchconferenceAddr;
	}

	public void setSearchconferenceAddr(String searchconferenceAddr) {
		this.searchconferenceAddr = searchconferenceAddr;
	}

	public Date getSearchStime() {
		return searchStime;
	}

	public void setSearchStime(Date searchStime) {
		this.searchStime = searchStime;
	}

	public Date getSearchEtime() {
		return searchEtime;
	}

	public void setSearchEtime(Date searchEtime) {
		this.searchEtime = searchEtime;
	}

	public File getWordDoc() {
		return wordDoc;
	}

	public void setWordDoc(File wordDoc) {
		this.wordDoc = wordDoc;
	}

	public String getIsExitWord() {
		return isExitWord;
	}

	public void setIsExitWord(String isExitWord) {
		this.isExitWord = isExitWord;
	}

	public String getConsendId() {
		return consendId;
	}

	public void setConsendId(String consendId) {
		this.consendId = consendId;
	}

	public String getContence() {
		return contence;
	}

	public void setContence(String contence) {
		this.contence = contence;
	}

}
