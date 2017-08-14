/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理日程action跳转类
 */
package com.strongit.oa.calendar;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.jsoup.helper.HttpConnection.Request;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.address.AddressGroupManager;
import com.strongit.oa.bo.ToaAddressGroup;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaCalendar;
import com.strongit.oa.bo.ToaCalendarActivity;
import com.strongit.oa.bo.ToaCalendarAttach;
import com.strongit.oa.bo.ToaCalendarRemind;
import com.strongit.oa.bo.ToaCalendarShare;
import com.strongit.oa.bo.ToaCalendarAssign;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.smscontrol.IsmscontrolService;
import com.strongit.oa.util.BaseDataExportInfo;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.ProcessXSL;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.webservice.iphone.server.pushNotify.PushNotifyManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 处理日程action跳转类
 * 
 * @author luosy
 * @version 1.0
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "calendar.action", type = ServletActionRedirectResult.class) })
public class CalendarAction extends BaseActionSupport<ToaCalendar> {

	private Page<ToaCalendar> page = new Page<ToaCalendar>(FlexTableTag.MAX_ROWS, true);
	private Page<ToaCalendarAssign> assignPage=new Page<ToaCalendarAssign>(FlexTableTag.MAX_ROWS,true);
	private List<ToaCalendarActivity> activitys;

	private String calendarId;
	private String activityId;
	// 添加日程时 开始时间 和 结束时间 格式为("yyyy-MM-dd HH:mm")
	private String termStart;
	private String termEnd;
	
//	 添加日程时 每次循环的 开始时间 和 结束时间 格式为("yyyy-MM-dd HH:mm:ss.0")
	private String repStart;
	private String repEnd;
	
	// 指定查看日期
	private String setDate;
	
	// 初始化json数组
	private String jsonArr;
	//	 提醒脚本
	private String remindData;
	private ToaCalendar model = new ToaCalendar();
	private TUumsBaseUser usermodel =new TUumsBaseUser();
	private CalendarManager manager;
	private AddressGroupManager addrmanager;
	private CalendarActivityManager activityManager;
	
	//附件
	private File[] file;
	private String[] fileFileName;
	private String attachId;//添加附件ID
	private String attachFiles ;//附件脚本

	private Long defAttSize ;	//默认附件大小限制
	//删除记录ids
	private String delAttachIds ;
	private String delRemindIds ;
	//共享的用户
	private String shareToUserIds;
	private String shareToUserNames;
	
	//视图类型  -/个人日程/共享日程/领导日程
	private String inputType;
	private String ifleader;
	
	private String treeUserId;//机构下的用户ID
	private String assignUserId;
	private List assignNameList;//被授权用户姓名
	private String ShowUserList;//选中的被授权用户姓名
	private String checkname;//点击查看后选择的用户ID
	
	//是否提醒
	private String ifRemind;
	private String ifShowUser;
	
	//用户Id
	private String userId;
	
	//短信发送权限
	private boolean hasSmsight;
	
	private DesktopSectionManager sectionManager;
	
	private IsmscontrolService smscontrolService;
	
	private IUserService user;
	//推送给ios的manager类
	@Autowired
	private PushNotifyManager pushManager;
	
	private HashMap<String, String> isleader = new HashMap<String, String>();
	private HashMap<String, String> isshare = new HashMap<String, String>();
	
	public CalendarAction() {
		isleader.put("1", "是");
		isleader.put("0", "否");
		isleader.put(null, "否");
		
		isshare.put("1", "是");
		isshare.put("0", "否");
	}
	
	public HashMap<String, String> getIsleader() {
		return isleader;
	}

	public HashMap<String, String> getIsshare() {
		return isshare;
	}
	public Page getPage() {
		return page;
	}
	
	public String getTreeUserId(){
		return this.treeUserId;
	}
	
	public void setTreeUserId(String treeUserId){
		this.treeUserId=treeUserId;
	}
	
	public String getShowUserList(){
		return this.ShowUserList;
	}
	
	public void setShowUserList(String ShowUserList){
		this.ShowUserList=ShowUserList;
	}
	
	public String getIfShowUser(){
		return this.ifShowUser;
	}
	
	public void setIfShowUser(String ifShowUser){
		this.ifShowUser=ifShowUser;
	}
	
	public String getCheckname(){
		return this.checkname;
	}
	
	public void setCheckname(String checkname){
		this.checkname=checkname;
	}
	

	
	public String getAssignUserId(){
		return this.assignUserId;
	}
	
	public void setAssignUserId(String assignUserId){
		this.assignUserId=assignUserId;
	}
	
	public List getAssignNameList(){
		return this.assignNameList;
	}
	
	public void setAssignNameList(List assignNameList){
		this.assignNameList=assignNameList;
	}
	
	public Page getAssignPage(){
		return assignPage;
	}

	public List<ToaCalendarActivity> getActivitys() {
		return activitys;
	}

	public void setCalendarId(String aCalendarId) {
		calendarId = aCalendarId;
	}
	
	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaCalendar getModel() {
		return model;
	}
	
	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(CalendarManager aManager) {
		manager = aManager;
	}
	
	@Autowired
	public void setSectionManager(DesktopSectionManager sectionManager) {
		this.sectionManager = sectionManager;
	}
	
	@Autowired
	public void setAddrManager(AddressGroupManager addrmanager) {
		this.addrmanager = addrmanager;
	}
	
	@Autowired
	public void setActivityManager(CalendarActivityManager activityManager) {
		this.activityManager = activityManager;
	}
	
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}
	
	@Autowired
	public void setSmscontrolService(IsmscontrolService smscontrolService) {
		this.smscontrolService = smscontrolService;
	}

	public void setTerm(String termStart) {
		this.termStart = termStart;
	}

	public void setTermEnd(String termEnd) {
		this.termEnd = termEnd;
	}

	public void setTermStart(String termStart) {
		this.termStart = termStart;
	}

	public String getRepEnd() {
		return repEnd;
	}

	public String getRepStart() {
		return repStart;
	}

	public void setRepEnd(String repEnd) {
		this.repEnd = repEnd;
	}

	public void setRepStart(String repStart) {
		this.repStart = repStart;
	}

	
	public String getJsonArr() {
		return jsonArr;
	}

	public String getSetDate() {
		return setDate;
	}

	public void setSetDate(String setDate) {
		this.setDate = setDate;
	}
	
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	public String getActivityId() {
		return activityId;
	}

	public String getRemindData() {
		return remindData;
	}
	public void setRemindData(String remindData) {
		this.remindData = remindData;
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

	public String getAttachFiles() {
		return attachFiles;
	}
	
	public void setAttachFiles(String attachFiles) {
		this.attachFiles = attachFiles;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}
	
	public void setDelAttachIds(String delAttachIds) {
		this.delAttachIds = delAttachIds;
	}

	public void setDelRemindIds(String delRemindIds) {
		this.delRemindIds = delRemindIds;
	}
	
	public void setShareToUserIds(String shareToUserIds) {
		this.shareToUserIds = shareToUserIds;
	}

	public String getShareToUserIds() {
		return shareToUserIds;
	}
	
	public String getShareToUserNames() {
		return shareToUserNames;
	}
	
	public void setShareToUserNames(String shareToUserNames) {
		this.shareToUserNames = shareToUserNames;
	}
	
	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getIfleader() {
		return ifleader;
	}

	public void setIfleader(String ifleader) {
		this.ifleader = ifleader;
	}

	public void setIfRemind(String ifRemind) {
		this.ifRemind = ifRemind;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public boolean isHasSmsight() {
		return hasSmsight;
	}

	public void setHasSmsight(boolean hasSmsight) {
		String userId = user.getCurrentUser().getUserId();
		this.hasSmsight = smscontrolService.hasSendRight(userId);
	}
	

	public Long getDefAttSize() {
		return defAttSize;
	}

	public void setDefAttSize(Long defAttSize) {
		this.defAttSize = defAttSize;
	}
	
	///////////////////////////////////////以下为action跳转方法

	/**
	 * author:luosy
	 * description: 跳转到主页面 父窗口
	 * modifyer:
	 * description:
	 * @return
	 */
	public String list() throws Exception {
		activitys = manager.getAllActivityByUserID(userId);
		if("1".equals(user.getCurrentUser().getRest1())){
			ifleader="1";
			System.out.println("ifleader:"+ifleader);
		}
		return "list";
	}
	
	/**
	 * author:hecj
	 * description:添加他人日程列表
	 */
	public String showlist()  throws Exception{
		activitys = manager.getAllActivityByUserID(userId);
		if("1".equals(user.getCurrentUser().getRest1())){
			ifleader="1";
			System.out.println("ifleader:"+ifleader);
		}
		return "showlist";
	}
	
	/**
	 * author:luosy
	 * description: 跳转到日程搜索列表页面
	 * modifyer:
	 * description:
	 * @return
	 */
	public String searchlist() throws Exception {
		try {
			if(model.getCalTitle()!=null&&!"".equals(model.getCalTitle())){
				model.setCalTitle(URLDecoder.decode(model.getCalTitle(), "utf-8"));
			}
			if(model.getCalUserName()!=null&&!"".equals(model.getCalUserName())){
				model.setCalUserName(URLDecoder.decode(model.getCalUserName(), "utf-8"));
			}
			if(null!=model.getToaCalendarActivity()){
				if(model.getToaCalendarActivity().getActivityName()!=null&&!"".equals(model.getToaCalendarActivity().getActivityName())){
					model.getToaCalendarActivity().setActivityName(URLDecoder.decode(model.getToaCalendarActivity().getActivityName(), "utf-8"));
				}
			}
			if(model.getCalPlace()!=null&&!"".equals(model.getCalPlace())){
				model.setCalPlace(URLDecoder.decode(model.getCalPlace(), "utf-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		activitys = manager.getAllActivityByUserID(userId);
		System.out.println(model.getToaCalendarActivity());
		page = manager.searchByCal(page,model,inputType);
		if("share".equals(inputType)|"leader".equals(inputType)){
			return "publiclist";
		}
		return "privatelist";
		
	}
	/**
	 * author:hecj
	 * description:选定机构下的用户，然后将用户的权限授予其他人，这里展现的是授权用户列表
	 */
	public String assignUserList() throws Exception{
		assignPage=manager.searchAssignUser(assignPage, treeUserId);
		//getUserNameByUserId
		return "assignUserList";
	}
	
	/**
	 * author:hecj
	 * description:删除指定的授权用户
	 */
	public String deleteUser() throws Exception{
		manager.deleteUser(assignUserId,treeUserId);
		renderText("ok");
		return null;
	}
	
	/**
	 * author:hecj
	 * description:跳转到他人的日程列表页面
	 * @return
	 */
	public String otherlist() throws Exception{
//		try {
//			if(model.getCalTitle()!=null&&!"".equals(model.getCalTitle())){
//				model.setCalTitle(URLDecoder.decode(model.getCalTitle(), "utf-8"));
//			}
//			if(model.getCalUserName()!=null&&!"".equals(model.getCalUserName())){
//				model.setCalUserName(URLDecoder.decode(model.getCalUserName(), "utf-8"));
//			}
//			if(null!=model.getToaCalendarActivity()){
//				if(model.getToaCalendarActivity().getActivityName()!=null&&!"".equals(model.getToaCalendarActivity().getActivityName())){
//					model.getToaCalendarActivity().setActivityName(URLDecoder.decode(model.getToaCalendarActivity().getActivityName(), "utf-8"));
//				}
//			}
//			if(model.getCalPlace()!=null&&!"".equals(model.getCalPlace())){
//				model.setCalPlace(URLDecoder.decode(model.getCalPlace(), "utf-8"));
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		//activitys = manager.getAllActivityByUserID(userId);
		page = manager.searchByOtherPage(page,model,inputType);
		return "otherlist";
	}
	
	/**
	 * author:luosy
	 * description: 删除 日程
	 * modifyer:
	 * description:
	 * @return
	 */
	public String delete() throws Exception {
		try {
			manager.deleteCalendar(calendarId);
			addActionMessage("删除成功");
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		if("list".equals(inputType)){
			return null;
		}
		return "view";
	}

	/**
	 * author:luosy
	 * description: 判定 新建 或 修改 日程
	 * modifyer:
	 * description:
	 * @return
	 */
	protected void prepareModel() throws Exception {
		if(calendarId==null|"".equals(calendarId)){
			if("".equals(termStart)|null==termStart){
				Calendar now = Calendar.getInstance();
				model.setCalStartTime(now.getTime());
				now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), now.get(Calendar.HOUR_OF_DAY)+2, now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
				model.setCalEndTime(now.getTime());
			}else{
				try {
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Calendar cals = Calendar.getInstance();
						cals.setTime(format.parse(termStart));
					model.setCalStartTime(cals.getTime());// 开始时间
	
					Calendar cale = Calendar.getInstance();
					cale.setTime(format.parse(termEnd));
					model.setCalEndTime(cale.getTime());// 结束时间
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}else{
			model = manager.getCalById(calendarId);
		}
	}
	
	/**
	 * author:hecj
	 * description:跳转到添加他人日程的页面
	 */
	public String assigninput()throws Exception {
		prepareModel();
		//model = manager.getCalById(calendarId);
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		activitys = manager.getAllActivityByUserID(userId);
		defAttSize = manager.getDefAttSize();
		if(model.getCalTitle()!=null&&!"".equals(model.getCalTitle())){
			model.setCalTitle(URLDecoder.decode(model.getCalTitle(), "utf-8"));
		} 
		assignNameList=manager.searchAssignByUserId(userId);
		
		java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
		//	获取附件
		Set att = null;
		if(!"".equals(model.getCalendarId())&&null!=model.getCalendarId()&&!"".equals(calendarId)&&null!=calendarId){
			att = manager.getCalById(calendarId).getToaCalendarAttaches();
			if(null!=manager.getCalById(calendarId).getCalRepeatStime()&&null!=manager.getCalById(calendarId).getCalRepeatEtime()){
				repStart = format1.format(manager.getCalById(calendarId).getCalRepeatStime());
				repEnd = format1.format(manager.getCalById(calendarId).getCalRepeatEtime());
			}
			checkname=model.getUserId();
		}else{
			att = model.getToaCalendarAttaches();
			checkname="";
		}
		if(att!=null){
			Iterator it=att.iterator();
			attachFiles="";
			while (it.hasNext()) {
				ToaCalendarAttach objs = (ToaCalendarAttach) it.next();
				ToaAttachment attachment = manager.getToaAttachmentById(objs.getAttachId());
				attachFiles+="<div id="+attachment.getAttachId()+" style=\"display: \"><a  href=\"javascript:delAttach('"+attachment.getAttachId()+"')\"><font color=\"blue\">[删除]</font><a>" +
						"<a href=\"javascript:download('"+attachment.getAttachId()+"')\">"+attachment.getAttachName()+"</a>&nbsp;</div>";
			}
		}
		//	获取提醒
		Set reminds = model.getToaCalendarReminds();
		if(reminds!=null){
			Iterator i = reminds.iterator();
			remindData="";
			while (i.hasNext()) {
				ToaCalendarRemind objs = (ToaCalendarRemind) i.next();
				remindData+="id"+objs.getRemindId()+","+objs.getRemindTime()+","+objs.getRemindMethod()+","+objs.getRemindCon()+","+objs.getRemindShare()+";";
			}
		}
		
		// 获取日程共享的人员
		Set shares = model.getToaCalendarShares();
		if (shares != null) {
			Iterator i = shares.iterator();
			shareToUserIds = "";
			shareToUserNames = "";
			while (i.hasNext()) {
				ToaCalendarShare objs = (ToaCalendarShare) i.next();
				shareToUserIds += objs.getUserId() + ",";
				shareToUserNames += manager.getUserNameByUserId(objs
						.getUserId())
						+ ",";
			}
			if (shareToUserIds.length() > 1) {
				shareToUserIds = shareToUserIds.substring(0, shareToUserIds
						.length() - 1);
			}
			if (shareToUserNames.length() > 1) {
				shareToUserNames = shareToUserNames.substring(0,
						shareToUserNames.length() - 1);
			}
		}
		
		return "assignadd";
	}
	/**
	 * author:luosy
	 * description:跳转到单个事务的新建或编辑页面
	 * modifyer:
	 * description:
	 * @return
	 */
	public String input() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		activitys = manager.getAllActivityByUserID(userId);
		defAttSize = manager.getDefAttSize();
		
		java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
		//	获取附件
		Set att = null;
		if(!"".equals(model.getCalendarId())&&null!=model.getCalendarId()&&!"".equals(calendarId)&&null!=calendarId){
			att = manager.getCalById(calendarId).getToaCalendarAttaches();
			if(null!=manager.getCalById(calendarId).getCalRepeatStime()&&null!=manager.getCalById(calendarId).getCalRepeatEtime()){
				repStart = format1.format(manager.getCalById(calendarId).getCalRepeatStime());
				repEnd = format1.format(manager.getCalById(calendarId).getCalRepeatEtime());
			}
		}else{
			att = model.getToaCalendarAttaches();
		}
		if(att!=null){
			Iterator it=att.iterator();
			attachFiles="";
			while (it.hasNext()) {
				ToaCalendarAttach objs = (ToaCalendarAttach) it.next();
				ToaAttachment attachment = manager.getToaAttachmentById(objs.getAttachId());
				attachFiles+="<div id="+attachment.getAttachId()+" style=\"display: \"><a href=\"javascript:delAttach('"+attachment.getAttachId()+"')\"><font color=\"blue\">[删除]</font><a>" +
						"<a href=\"javascript:download('"+attachment.getAttachId()+"')\">"+attachment.getAttachName()+"</a>&nbsp;</div>";
			}
		}
		//	获取提醒
		Set reminds = model.getToaCalendarReminds();
		if(reminds!=null){
			Iterator i = reminds.iterator();
			remindData="";
			while (i.hasNext()) {
				ToaCalendarRemind objs = (ToaCalendarRemind) i.next();
				remindData+="id"+objs.getRemindId()+","+objs.getRemindTime()+","+objs.getRemindMethod()+","+objs.getRemindCon()+","+objs.getRemindShare()+";";
			}
		}
		
		// 获取日程共享的人员
		Set shares = model.getToaCalendarShares();
		if (shares != null) {
			Iterator i = shares.iterator();
			shareToUserIds = "";
			shareToUserNames = "";
			while (i.hasNext()) {
				ToaCalendarShare objs = (ToaCalendarShare) i.next();
				shareToUserIds += objs.getUserId() + ",";
				shareToUserNames += manager.getUserNameByUserId(objs
						.getUserId())
						+ ",";
			}
			if (shareToUserIds.length() > 1) {
				shareToUserIds = shareToUserIds.substring(0, shareToUserIds
						.length() - 1);
			}
			if (shareToUserNames.length() > 1) {
				shareToUserNames = shareToUserNames.substring(0,
						shareToUserNames.length() - 1);
			}
		}
		
		return "add";
	}
	
	/**
	 * author:luosy
	 * description:跳转到单个事务的新建或编辑页面
	 * modifyer:
	 * description:
	 * @return
	 */
	public String readonly() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		prepareModel();
		activitys = manager.getAllActivityByUserID(model.getUserId());
		//	获取附件
		Set att = model.getToaCalendarAttaches();
		if(att!=null){
			Iterator it=att.iterator();
			attachFiles="";
			while (it.hasNext()) {
				ToaCalendarAttach objs = (ToaCalendarAttach) it.next();
				ToaAttachment attachment = manager.getToaAttachmentById(objs.getAttachId());
				attachFiles+="<div id="+attachment.getAttachId()+" style=\"display: \"><a href=\"javascript:delAttach('"+attachment.getAttachId()+"')\"><a>" +
						"<a href=\"javascript:download('"+attachment.getAttachId()+"')\"><font color='blue'>"+attachment.getAttachName()+"</font></a>&nbsp;</div>";
			}
		}
		//	获取提醒
		Set reminds = model.getToaCalendarReminds();
		if(reminds!=null){
			Iterator i = reminds.iterator();
			remindData="";
			while (i.hasNext()) {
				ToaCalendarRemind objs = (ToaCalendarRemind) i.next();
				remindData+="id"+objs.getRemindId()+","+objs.getRemindTime()+","+objs.getRemindMethod()+","+objs.getRemindCon()+";";
			}
		}

		//		获取日程共享的人员
		Set shares = model.getToaCalendarShares();
		if(shares!=null){
			Iterator i = shares.iterator();
			shareToUserIds = "";
			shareToUserNames = "";
			while (i.hasNext()) {
				ToaCalendarShare objs = (ToaCalendarShare) i.next();
				shareToUserIds += objs.getUserId()+",";
				shareToUserNames += manager.getUserNameByUserId(objs.getUserId())+",";
			}
			if(shareToUserIds.length()>1){
				shareToUserIds = shareToUserIds.substring(0, shareToUserIds.length()-1);
			}
			if(shareToUserNames.length()>1){
				shareToUserNames = shareToUserNames.substring(0, shareToUserNames.length()-1);
			}
		}
		
		return "readonly";
	}
	
	/**
	 * 获取机构以及部门列表，还有部门成员列表
	 * @author:hecj
	 */
	public String newtree() throws Exception{
		List<TempPo> nodeList = new ArrayList<TempPo>();
		/*TempPo personalRoot = new TempPo();//构造个人通讯录根节点
		personalRoot.setId(GlobalBaseData.ADDRESS_PERSONAL_ROOT_NAME);
		personalRoot.setName(getText(GlobalBaseData.ADDRESS_PERSONAL_ROOT_NAME));//节点名,从资源文件中读取
		personalRoot.setParentId("0");
		personalRoot.setType("docAddress");
		nodeList.add(personalRoot);*/
		
		TempPo publicRoot = new TempPo();//构造系统通讯录根节点
		publicRoot.setId(GlobalBaseData.ADDRESS_PUBLIC_ROOT_NAME);
		publicRoot.setParentId("0");
		publicRoot.setName(getText(GlobalBaseData.ADDRESS_PUBLIC_ROOT_NAME));//节点名,从资源文件中读取
		publicRoot.setType("orgAddress");
		if(publicRoot.getName() == null || "".equals(publicRoot.getName()) || GlobalBaseData.ADDRESS_PUBLIC_ROOT_NAME.equals(publicRoot.getName())){
			publicRoot.setId("0");
		}else{
			nodeList.add(publicRoot);			
		}
		
		//个人通讯录节点挂接设置
		/*List<ToaAddressGroup> groupList = addrmanager.getGroupList();
		for(ToaAddressGroup group : groupList){
			TempPo groupPo = new TempPo();
			groupPo.setId(group.getAddrGroupId());
			groupPo.setName(group.getAddrGroupName());
			groupPo.setParentId(personalRoot.getId());
			groupPo.setType("docAddress");//个人通讯录节点
			nodeList.add(groupPo);
		}*/
		
		List<Organization> orgList = user.getAllDeparments();
		//找到机构根节点,编码长度最短的是根节点
		List<Organization> newList = new ArrayList<Organization>(orgList);
		Collections.sort(newList, new Comparator<Organization>(){

			public int compare(Organization o1, Organization o2) {
				String code1 = o1.getOrgSyscode();
				String code2 = o2.getOrgSyscode();
				if(code1 != null && code2 != null) {
					return code1.length() - code2.length();
				}
				return 0;
			}
			
		});
		String root = null;
		if(!newList.isEmpty()) {
			root = newList.get(0).getOrgSyscode();
		}
		String config_orgCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_ORG);//读取codeRule.properties文件中组织机构编码规则
		for(int i=0;i<orgList.size();i++){
			Organization organization = orgList.get(i);
			TempPo orgPo = new TempPo();
			orgPo.setCodeId(organization.getOrgSyscode());
			orgPo.setId(organization.getOrgSyscode());
			orgPo.setName(organization.getOrgName());
			String parentId = user.findFatherCode(organization.getOrgSyscode(), config_orgCode, null);
			/*if(i == 0){//机构列表根据编码排序，第一个一定是根节点
				if(!"0".equals(parentId)){
					parentId = publicRoot.getId();
				}
			}*/
			if(organization.getOrgSyscode().equals(root)) {
				if(!"0".equals(parentId)){
					parentId = publicRoot.getId();
				}
			}
			List<TUumsBaseUser> userlist= user.getUserListByOrgId(organization.getOrgId());
			for (int j=0;j<userlist.size();j++){
				usermodel=(TUumsBaseUser)userlist.get(j);
				TempPo userPo=new TempPo();
				userPo.setId(usermodel.getUserId());
				userPo.setCodeId(usermodel.getUserId());
				userPo.setName(usermodel.getUserName());
				userPo.setParentId(organization.getOrgSyscode());
				userPo.setType("userAddress");
				if(usermodel.getOrgId().equals(organization.getOrgId())){//如果是默认机构则显示该人员
					nodeList.add(userPo);
				}
			}
			/*if(organization.getOrgParentId() == null || organization.getOrgId().equals(organization.getOrgParentId())){
				orgPo.setParentId(publicRoot.getId());
			}else{
				orgPo.setParentId(organization.getOrgParentId());
			}*/
			orgPo.setParentId(parentId);
			orgPo.setType("orgAddress");//系统通讯录节点
			nodeList.add(orgPo);
		}
		getRequest().setAttribute("groupLst", nodeList);
		return "newtree";
	}
	
	/**
	 * author:hecj
	 * description:保存添加他人日程页面
	 */
	public String saveother()throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		
		long temp = 0;
		defAttSize = manager.getDefAttSize();
		if(file!=null){
			for(int i=0;i<file.length;i++){
				if(file[i].length()==0){
					addActionMessage("error不能上传空文件，请检查要上传的文件！");
					return assigninput();
				}
				temp += file[i].length();
			}
			if(temp>defAttSize){
				addActionMessage("error不能上传附件，附件大小超出最大范围（"+defAttSize/1024/1024+"M）！");
				return assigninput();
			}
		}
		
		//  执行编辑过程中删除的附件
		if( delAttachIds!=null&&!"".equals(delAttachIds)) {
			String id[] = delAttachIds.split(",");
			for(int i=0;i<id.length;i++){
				manager.delAttach(id[i]);
			}
			ToaCalendar cal = manager.getCalById(model.getCalendarId());
			Set s = cal.getToaCalendarAttaches();
			model.setToaCalendarAttaches(s);
			cal=null;
		}
		
		//	执行编辑过程中删除的提醒
		if("0".equals(ifRemind)){//选中不提醒 
			remindData="noRemind";
		}else{//选中提醒 
			if( delRemindIds!=null&&!"".equals(delRemindIds)) {
				String id[] = delRemindIds.split(",");
				for(int i=0;i<id.length;i++){
					id[i] = id[i].replace("id", "");
					manager.delRemind(id[i]);
				}
				ToaCalendar cal = manager.getCalById(model.getCalendarId());
				Set s = cal.getToaCalendarReminds();
				model.setToaCalendarReminds(s);
				cal=null;
			}
		}
		
		//选择添加他人日程的人员
		if("0".equals(ifShowUser)){
			ShowUserList=null;
			model.setAssignUserId(user.getCurrentUser().getUserId());
			//model.setUserId(checkname);
			//model.setCalUserName(user.getUserNameByUserId(checkname));
			manager.saveInpage(activityId, model, shareToUserIds, remindData,file,fileFileName);
		}else{
			if (!"".equals(checkname)){
				
				model.setAssignUserId(user.getCurrentUser().getUserId());
				model.setUserId(checkname);
				model.setCalUserName(user.getUserNameByUserId(checkname));
				model.setToaCalendarActivity(manager.getCalById(calendarId).getToaCalendarActivity());
				manager.saveInpage(activityId,model, shareToUserIds, remindData,file,fileFileName);
				
			}else{
				String cUserId = user.getCurrentUser().getUserId();
				String[] st = ShowUserList.split(",");
				for(int i=0;i<st.length;i++){
					ToaCalendar obj = new ToaCalendar();
					obj.setCalStartTime(model.getCalStartTime());
					obj.setCalEndTime(model.getCalEndTime());
					obj.setRepeatType(model.getRepeatType());
					obj.setCalRepeatStime(model.getCalRepeatStime());
					obj.setCalRepeatEtime(model.getCalRepeatEtime());
					obj.setCalTitle(model.getCalTitle());
					obj.setToaCalendarActivity(model.getToaCalendarActivity());
					obj.setUserId(userId);
				//	obj.setRepeatType(ToaCalendar.REPEAT_NONE);
					obj.setCalHasRemind(ToaCalendar.HAS_NO_REMIND);
					obj.setIsLeader(model.getIsLeader());
					obj.setAssignUserId(cUserId);
					obj.setUserId(st[i]);
					obj.setCalUserName(user.getUserNameByUserId(st[i]));
					ToaCalendarActivity act=null;
					if("".equals(activityId)){
						act=activityManager.getActivity(ToaCalendarActivity.DEFINE_ACTIVITY);
					}else
					{
						act=activityManager.getActivityById(activityId);
					}
					String actName= act.getActivityName();
					String uid=act.getUserId();
					String aid=activityManager.isActivityExist(actName,st[i]);
					if(aid==null){
						ToaCalendarActivity newact=new ToaCalendarActivity();
						//newact.setActivityId(activityId);
						newact.setActivityName(actName);
						newact.setActivityIsDel(act.getActivityIsDel());
						newact.setUserId(st[i]);
						newact.setActivityRemark(act.getActivityRemark());
						activityManager.saveActivity(newact);
						manager.saveInpage(newact.getActivityId(), obj, shareToUserIds, remindData,file,fileFileName);

						activityId=newact.getActivityId();
					}else{
						//activityId=aid;
						manager.saveInpage(aid, obj, shareToUserIds, remindData,file,fileFileName);
					}
					
				}
			}
		}
		addActionMessage("操作成功!");
		return assigninput();
		//return null;
		//return input();
	}
	/**
	 * author:luosy
	 * description:弹出页面 保存日程事务
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		
		long temp = 0;
		defAttSize = manager.getDefAttSize();
		if(file!=null){
			for(int i=0;i<file.length;i++){
				if(file[i].length()==0){
					addActionMessage("error不能上传空文件，请检查要上传的文件！");
					return input();
				}
				temp += file[i].length();
			}
			if(temp>defAttSize){
				addActionMessage("error不能上传附件，附件大小超出最大范围（"+defAttSize/1024/1024+"M）！");
				return input();
			}
		}
		
		//  执行编辑过程中删除的附件
		if( delAttachIds!=null&&!"".equals(delAttachIds)) {
			String id[] = delAttachIds.split(",");
			for(int i=0;i<id.length;i++){
				manager.delAttach(id[i]);
			}
			ToaCalendar cal = manager.getCalById(model.getCalendarId());
			Set s = cal.getToaCalendarAttaches();
			model.setToaCalendarAttaches(s);
			cal=null;
		}
		
		//	执行编辑过程中删除的提醒
		if("0".equals(ifRemind)||"".equals(ifRemind)||""==ifRemind){//选中不提醒 
			remindData="noRemind";
		}else{//选中提醒 
			if( delRemindIds!=null&&!"".equals(delRemindIds)) {
				String id[] = delRemindIds.split(",");
				for(int i=0;i<id.length;i++){
					id[i] = id[i].replace("id", "");
					manager.delRemind(id[i]);
				}
				ToaCalendar cal = manager.getCalById(model.getCalendarId());
				Set s = cal.getToaCalendarReminds();
				model.setToaCalendarReminds(s);
				cal=null;
			}
		}
		if(model==null||model.getCalendarId()==null||"".equals(model.getCalendarId())){
			/**
			 * 新增操作时往推送设置里面新增记录或者修改推送数
			 */
			if(pushManager.getPushState(user.getCurrentUser().getUserId(),PushNotifyManager.PUSH_MODULE_NO_CALENDAR)){
				pushManager.saveNotity(user.getCurrentUser().getUserId(),PushNotifyManager.PUSH_MODULE_NO_CALENDAR, 1);
			}
		}
		manager.saveInpage(activityId, model, shareToUserIds, remindData,file,fileFileName);
		addActionMessage("操作成功!");
		return input();
	}
	
	/**
	 * author:hecj
	 * description:保存指定授权人员
	 */
	public String saveperson() throws Exception
	{
		manager.saveAssignPerson(treeUserId,shareToUserIds);
		return null;
	}
	
	/**
	 * author:luosy
	 * description:日周月视图中 保存日程事务
	 * modifyer:
	 * description:
	 * @return
	 */
	public String add() throws Exception {
		// 时间范围
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar cals = Calendar.getInstance();
			cals.setTime(format.parse(termStart));
			model.setCalStartTime(cals.getTime());// 开始时间

			Calendar cale = Calendar.getInstance();
			cale.setTime(format.parse(termEnd));
			model.setCalEndTime(cale.getTime());// 结束时间
		} catch (ParseException e) {
			e.printStackTrace();
			addActionMessage("不能添加！");
		}
		if (calendarId == null | "".equals(calendarId)) {
			// 标题
			try {
				model.setCalTitle(java.net.URLDecoder.decode(model
						.getCalTitle(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			ToaCalendar cal=manager.getCalById(calendarId);
			model.setAssignUserId(cal.getAssignUserId());
		}
		String calId = manager.saveCalendar(activityId,model,"view");

		JSONObject obj = new JSONObject();
		obj.put("id", calId);
		obj.put("msg", "操作成功！");

		try {
			ServletActionContext.getResponse().getWriter().print(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		addActionMessage(obj.toString());
		return "view";
	}
	
	/**
	 * author:hecj
	 * description:点击修改视图中的日程时触发
	 * modifyer:
	 * description:
	 * @return
	 */
	public String addassign() throws Exception {
		// 时间范围
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar cals = Calendar.getInstance();
			cals.setTime(format.parse(termStart));
			model.setCalStartTime(cals.getTime());// 开始时间

			Calendar cale = Calendar.getInstance();
			cale.setTime(format.parse(termEnd));
			model.setCalEndTime(cale.getTime());// 结束时间
		} catch (ParseException e) {
			e.printStackTrace();
			addActionMessage("不能添加！");
		}
		if (calendarId == null | "".equals(calendarId)) {
			// 标题
			try {
				model.setCalTitle(java.net.URLDecoder.decode(model
						.getCalTitle(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		String calId = manager.saveCalendar(activityId,model,"view");

		JSONObject obj = new JSONObject();
		obj.put("id", calId);
		obj.put("msg", "操作成功！");

		try {
			ServletActionContext.getResponse().getWriter().print(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		addActionMessage(obj.toString());
		return "viewassign";
	}
	
	
	/**
	 * author:luosy
	 * description: ajax添加新活动分类后 刷新 新建或编辑页面
	 * modifyer:
	 * description:
	 * @return
	 */
	public String refreshinput() throws Exception {
		activitys = manager.getAllActivityByUserID(userId);

		defAttSize = manager.getDefAttSize();
		try {
			model.setCalTitle(java.net.URLDecoder.decode(model
					.getCalTitle(), "utf-8"));
			model.setCalCon(java.net.URLDecoder.decode(model
					.getCalCon(), "utf-8"));
			model.setCalPlace(java.net.URLDecoder.decode(model
					.getCalPlace(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedEncodingException();
		}
		return "add";
	}
	
	/**
	 * author:luosy
	 * description:获取有共享日程的领导信息
	 * modifyer:
	 * description:
	 * @return 查看领导日程的 人员树页面
	 * @throws Exception
	 */
	public String leaderList() throws Exception {
		List leaderList = manager.getLeaderCal();
		getRequest().setAttribute("leaderList", leaderList);
		return "leadertree";
	}
	
	
	/**
	 * author:luosy
	 * description:是否具有短信权限
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String hasSmsRight()throws Exception {
		boolean right= smscontrolService.hasSendRight(user.getCurrentUser().getUserId());
		if(right){
			renderText("true");
		}else{
			renderText("false");
		}
		return null;
	}
	
	/**
	 * author:luosy
	 * description:  下载 公共附件下的文件
	 * modifyer:
	 * description:
	 * @return
	 */
	public String down() throws Exception {
		HttpServletResponse response = super.getResponse();
		ToaAttachment file = manager.getToaAttachmentById(attachId);
		response.reset();
		response.setContentType("application/x-download");         //windows
		OutputStream output = null;
		try{
			response.addHeader("Content-Disposition", "attachment;filename=" +
			         new String(file.getAttachName().getBytes("gb2312"),"iso8859-1"));
		    output = response.getOutputStream();
		    output.write(file.getAttachCon());
		    output.flush();
		} catch(Exception e) {
			if(logger.isErrorEnabled()){
				logger.error(e.getMessage(),e);
			}
		} finally {		 	    
		    if(output != null){
		      try {
				output.close();
			} catch (IOException e) {
				if(logger.isErrorEnabled()){
					logger.error(e.getMessage(),e);
				}
			}
		      output = null;
		    }
		}
		return null;
	}
	
	/**
	 * author:luosy
	 * description: 查看有日程共享给当前用户的人员列表
	 * modifyer:
	 * description:
	 * @return 查看共享日程的 人员树页面
	 * @throws Exception
	 */
	public String shareList() throws Exception {
		List<Object[]> shareList = manager.getAllShare();
		getRequest().setAttribute("shareList", shareList);
		return "sharetree";
	}
	
	/**
	 * author:hecj
	 * description:将自己的日程添加权限分配给指定人
	 */
	public String assignList() throws Exception{
		// 获取权限分配指定人员id以及姓名
		List<ToaCalendarAssign> assignList=manager.getAssignList(treeUserId);
		if (assignList != null) {
			Iterator i = assignList.iterator();
			shareToUserIds = "";
			shareToUserNames = "";
			while (i.hasNext()) {
				ToaCalendarAssign objs = (ToaCalendarAssign) i.next();
				String name=manager.getUserNameByUserId(objs.getAssignToUserId());
				if(name==null  ||  "null".equals(name)){
				}else{
					shareToUserIds += objs.getAssignToUserId() + ",";
					shareToUserNames += name+ ",";
				}
			}
			
			if (shareToUserIds.length() > 1) {
				shareToUserIds = shareToUserIds.substring(0, shareToUserIds
						.length() - 1);
			}
			if (shareToUserNames.length() > 1) {
				shareToUserNames = shareToUserNames.substring(0,
						shareToUserNames.length() - 1);
			}
		}
		return "assignList";
	}
	
	/**
	 * author:luosy
	 * description:  个人日程/领导日程
	 * modifyer:
	 * description:
	 * @return
	 */
	public String viewpage() throws Exception {
		jsonArr = new JSONArray().toString();
		if("".equals(inputType)|null==inputType){
			inputType="day";
		}
		if("".equals(setDate)|null==setDate){
			Calendar now = Calendar.getInstance();
			setDate=String.valueOf(now.get(Calendar.YEAR))+"-"+String.valueOf(now.get(Calendar.MONTH)+1)+"-"+String.valueOf(now.get(Calendar.DAY_OF_MONTH));
		}
		
		return "view";
	}
	
	
	/**
	 * author:hecj
	 * description:查看他人日程
	 * 
	 */
	
	public String viewassign() throws Exception {
		jsonArr = new JSONArray().toString();
		if("".equals(inputType)|null==inputType){
			inputType="day";
		}
		if("".equals(setDate)|null==setDate){
			Calendar now = Calendar.getInstance();
			setDate=String.valueOf(now.get(Calendar.YEAR))+"-"+String.valueOf(now.get(Calendar.MONTH)+1)+"-"+String.valueOf(now.get(Calendar.DAY_OF_MONTH));
		}
		
		return "viewassign";
	}
	
	/**
	 * author:hecj
	 * description:查看添加他人日程
	 */
	
	public String listassign() throws Exception {
		jsonArr = new JSONArray().toString();
		if("".equals(inputType)|null==inputType){
			inputType="day";
		}
		if("".equals(setDate)|null==setDate){
			Calendar now = Calendar.getInstance();
			setDate=String.valueOf(now.get(Calendar.YEAR))+"-"+String.valueOf(now.get(Calendar.MONTH)+1)+"-"+String.valueOf(now.get(Calendar.DAY_OF_MONTH));
		}
		
		return "listassign";
	}
	
	/**
	 * author:luosy
	 * description:查看领导日程
	 * modifyer:
	 * description:
	 * @return
	 */
	public String viewleader() throws Exception {
		jsonArr = new JSONArray().toString();
		return "leaderContent";
		
	}
	/**
	 * author:luosy
	 * description: 查看共享日程
	 * modifyer:
	 * description:
	 * @return
	 */
	public String viewshare() throws Exception {
		jsonArr = new JSONArray().toString();
		return "shareContent";
	}
	
	/**
	 * author:luosy
	 * description:	获取桌面显示需要的日程信息
	 * modifyer:
	 * description:
	 * @return	跳转到桌面显示日程的IFRAME
	 * @throws Exception
	 */
	public String desktop() throws Exception {
		Calendar startCal = Calendar.getInstance();
		if(!"".equals(termStart)&&null!=termStart){
			String[] toDate = termStart.split("-"); 
			startCal.set(new Integer(toDate[0]), new Integer(toDate[1]), new Integer(toDate[2]), 00, 00);
		}else{
			startCal.set(Calendar.DAY_OF_MONTH, 1);
			startCal.set(Calendar.HOUR_OF_DAY, 0);
			startCal.set(Calendar.MINUTE, 0);
		}
		
		Calendar endCal = Calendar.getInstance();
		if(!"".equals(termEnd)&&null!=termEnd){
			String[] toDate = termEnd.split("-"); 
			endCal.set(new Integer(toDate[0]), new Integer(toDate[1]), new Integer(toDate[2]), 23, 59);
		}else{
			endCal.set(Calendar.DAY_OF_MONTH, 31);
			endCal.set(Calendar.HOUR_OF_DAY, 23);
			endCal.set(Calendar.MINUTE, 59);
		}
		List calList = manager.getListByTime(startCal, endCal,activityId);
		JSONArray array = manager.makeCalListToJSONArray(calList,true);
		
		setDate = startCal.get(Calendar.YEAR)+"-"+(startCal.get(Calendar.MONTH)+1)+"-"+startCal.get(Calendar.DAY_OF_MONTH);
		jsonArr = array.toString();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(termStart == null || "".equals(termStart.trim()) || "null".equals(termStart.trim())){
			getRequest().setAttribute("today",df.format(new Date()));//获取当前系统时间
		}else{
			String[] toDate = termStart.split("-"); 
			int month = Integer.parseInt(toDate[1]);
			int year = Integer.parseInt(toDate[0]);
			Calendar c = Calendar.getInstance();
			int curMonth = c.get(Calendar.MONTH)+1;
			int curyear = c.get(Calendar.YEAR);
			
			if(month ==  curMonth && curyear == year){
				getRequest().setAttribute("today",df.format(c.getTime()));//获取当前系统时间
			}else{
				getRequest().setAttribute("today",termStart);//设置上一步或者下一步传递的时间参数
			}
		}
		if("small".equals(inputType)){
			return "small";
		}
		if("red".equals(inputType)){
			return "red";
		}
		return "desktop";
	}
	
	/**
	 * author:luosy
	 * description:桌面共享日程
	 * modifyer:
	 * description:
	 * @return
	 */
	public String shareCalForDesktop()throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		
//		获取公告列表list
		List<ToaCalendarShare> list = manager.getShareCalForTable();
		
		String blockid = getRequest().getParameter("blockid");//获取blockid
		Map<String,String> map = sectionManager.getParam(blockid);//通过blockid获取映射对象
		String showNum = map.get("showNum");//显示条数
		String subLength = map.get("subLength");//主题长度
		String showCreator = map.get("showCreator");//是否显示作者
		String showDate = map.get("showDate");//是否显示日期
		String sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "12";
		}
		//跳转连接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
		.append("/fileNameRedirectAction.action?toPage=calendar/calendar-share.jsp")
		.append("', '查看共享日程'")
		.append(");");
		
		if(null!=list){
			for(int i=0;i<num&&i<list.size();i++){//获取在条数范围内的列表
				ToaCalendarShare cal = list.get(i);
				
				//标题连接
				String url = getRequest().getContextPath()+"/calendar/calendar!readonly.action?calendarId="+cal.getToaCalendar().getCalendarId();
				String titleLink = "var a = window.showModalDialog('"+url+"',window,'dialogWidth:700px;dialogHeight:580px;help:no;status:no;scroll:no');";
				
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				innerHtml.append("<table class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td width=\"60%\">");
				innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				
				String calplace=cal.getToaCalendar().getCalPlace();
				String title ="";
				if(calplace!=null){
					calplace=" (地点:" + calplace+ ")";
					length=length+5+calplace.length();
					title = cal.getToaCalendar().getCalTitle()+calplace;
				}else{
					calplace="";
					title = cal.getToaCalendar().getCalTitle();
				}
				
				if(title.length()>length)//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<span title=\"").append(cal.getToaCalendar().getCalTitle()).append("\">	<a href=\"#\" onclick=\"javascript:").append(titleLink).append("\"> ").append(title).append("</a>");
				
				if(cal.getToaCalendar().getToaCalendarAttaches().size()>0)//如果为有附件 
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/mymail/yes.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				else
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/mymail/no.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				
				innerHtml.append("</span>");
				innerHtml.append("</td>");
				innerHtml.append("<td width=\"24%\">");
				if("1".equals(showCreator))//如果设置为显示作者，则显示作者信息
					innerHtml.append("	<span class =\"linkgray\">").append(cal.getToaCalendar().getCalUserName()).append("</span>");
				innerHtml.append("</td>");
				innerHtml.append("<td width=\"16%\">");
				if("1".equals(showDate))//如果设置为显示日期，则显示日期信息
					innerHtml.append("	<span class =\"linkgray10\">(").append(st.format(cal.getToaCalendar().getCalStartTime())).append(")到(").append(st.format(cal.getToaCalendar().getCalEndTime())).append(")</span>");
				innerHtml.append("	</td>");
				innerHtml.append("	</tr>");
				innerHtml.append("	</table>");
			}
		}
		innerHtml.append("<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"").append(link.toString()).append("\"> ")
				 .append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}
	/**
	 * author:luosy
	 * description: 桌面显示  领导日程/日程安排
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String showDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		
		String blockid = getRequest().getParameter("blockid");//获取blockid
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum"); 
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if(blockid != null){				
			Map<String,String> map = sectionManager.getParam(blockid);//通过blockid获取映射对象
			showNum = map.get("showNum");//显示条数
			subLength = map.get("subLength");//主题长度
			showCreator = map.get("showCreator");//是否显示作者
			showDate = map.get("showDate");//是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}
		
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}

		//跳转连接
		StringBuffer link = new StringBuffer();
		if("1".equals(ifleader)){
			link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/fileNameRedirectAction.action?toPage=calendar/calendar-leader.jsp")
			.append("', '查看领导日程'")
			.append(");");
		}else{
			//领导的日程 url = "/calendar/calendar.action?ifLeader=1";
			link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/calendar/calendar.action")
			.append("', '日程安排'")
			.append(");");
		}
		
//		获取公告列表list
		String userId=user.getCurrentUser().getUserId();
		List<ToaCalendar> list ;
		page.setPageSize(num);
		page = manager.getListForTable(userId,ifleader,page);
		list = page.getResult();
		
		if(null!=list){
			for(int i=0;i<num&&i<list.size();i++){//获取在条数范围内的列表
				ToaCalendar cal = list.get(i);
				
				//标题连接
				String url = "";
				if("1".equals(ifleader)){
					url = getRequest().getContextPath()+"/calendar/calendar!readonly.action?calendarId="+cal.getCalendarId();
				}else{
					url = getRequest().getContextPath()+"/calendar/calendar!input.action?ifleader="+ifleader+"&calendarId="+cal.getCalendarId();
				}
				String titleLink = "var a = window.showModalDialog('"+url+"',window,'dialogWidth:700px;dialogHeight:580px;help:no;status:no;scroll:no');";
				
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				
				String calplace=cal.getCalPlace();
				String title ="";
				if(calplace!=null){
					calplace=" (地点:" + calplace+ ")";
					length=length+5+calplace.length();
					title = cal.getCalTitle()+calplace;
				}else{
					calplace="";
					title = cal.getCalTitle();
				}
				
				if(title.length()>length)//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<span style=\"font-size:"+sectionFontSize+"px\" title=\"").append(cal.getCalTitle()).append("\">	<a href=\"#\" style=\"font-size:"+sectionFontSize+"px\" onclick=\"javascript:").append(titleLink).append("\"> ").append(title).append("</a>");
				
				if(cal.getToaCalendarAttaches().size()>0)//如果为有附件 
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/mymail/yes.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				else
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/mymail/no.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				
				innerHtml.append("</span>");
				innerHtml.append("</td>");
				
				if("1".equals(showCreator)){//如果设置为显示作者，则显示作者信息
					innerHtml.append("<td width=\"115px\">");
					innerHtml.append("	<span class =\"linkgray\">").append(cal.getCalUserName()).append("</span>");
					innerHtml.append("</td>");
				}
				
				if("1".equals(showDate)){//如果设置为显示日期，则显示日期信息
					innerHtml.append("<td width=\"100px\">");
					innerHtml.append("	<span class =\"linkgray10\">(").append(st.format(cal.getCalStartTime())).append(")到(").append(st.format(cal.getCalEndTime())).append(")</span>");
					innerHtml.append("	</td>");
				}
				innerHtml.append("	</tr>");
				innerHtml.append("	</table>");
			}
		}
		innerHtml.append("<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"")
				 .append(link.toString()).append("\"> ")
				 .append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\"BORDER=\"0\" ALT=\"\" /></a></div>");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示	 WIDTH=\"15\" HEIGHT=\"10\" 
	}
	
	

	/**
	* @description 个人日程桌面列表显示
	* @method showTableDesktop
	* @author 申仪玲(shenyl)
	* @created 2012-5-31 下午03:49:09
	* @return
	* @throws Exception
	* @return String
	* @throws Exception
	*/
	public String showTableDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		
		String blockid = getRequest().getParameter("blockid");//获取blockid
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum"); 
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if(blockid != null){				
			Map<String,String> map = sectionManager.getParam(blockid);//通过blockid获取映射对象
			showNum = map.get("showNum");//显示条数
			subLength = map.get("subLength");//主题长度
			showCreator = map.get("showCreator");//是否显示作者
			showDate = map.get("showDate");//是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}
		
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}
		
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		if (sectionFontSize == null || "".equals(sectionFontSize)
				|| "null".equals(sectionFontSize)) {
			sectionFontSize = "14";
		}

		//跳转连接
		StringBuffer link = new StringBuffer();
		if("1".equals(ifleader)){
			link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/fileNameRedirectAction.action?toPage=calendar/calendar-leader.jsp")
			.append("', '查看领导日程'")
			.append(");");
		}else{
			//领导的日程 url = "/calendar/calendar.action?ifLeader=1";
			link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/calendar/calendar.action")
			.append("', '日程安排'")
			.append(");");
		}
		
//		获取公告列表list
		String userId=user.getCurrentUser().getUserId();
		List<ToaCalendar> list ;
		page.setPageSize(num);
		page = manager.getListForTable(userId,ifleader,page);
		list = page.getResult();
		
		if(null!=list){
			for(int i=0;i<num&&i<list.size();i++){//获取在条数范围内的列表
				ToaCalendar cal = list.get(i);
				
				//标题连接
				String url = "";
				if("1".equals(ifleader)){
					url = getRequest().getContextPath()+"/calendar/calendar!readonly.action?calendarId="+cal.getCalendarId();
				}else{
					url = getRequest().getContextPath()+"/calendar/calendar!input.action?ifleader="+ifleader+"&calendarId="+cal.getCalendarId();
				}
				String titleLink = "var a = window.showModalDialog('"+url+"',window,'dialogWidth:700px;dialogHeight:580px;help:no;status:no;scroll:no');";
				
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				
				String calplace=cal.getCalPlace();
				String title ="";
				if(calplace!=null){
					calplace=" (地点:" + calplace+ ")";
					length=length+5+calplace.length();
					title = cal.getCalTitle()+calplace;
				}else{
					calplace="";
					title = cal.getCalTitle();
				}
				
				if(title.length()>length)//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<span style=\"font-size:"+sectionFontSize+"px\" title=\"").append(cal.getCalTitle()).append("\">	<a href=\"#\" style=\"font-size:"+sectionFontSize+"px\" onclick=\"javascript:").append(titleLink).append("\"> ").append(title).append("</a>");
				
				if(cal.getToaCalendarAttaches().size()>0)//如果为有附件 
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/mymail/yes.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				else
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/mymail/no.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				
				innerHtml.append("</span>");
				innerHtml.append("</td>");
				
				if("1".equals(showCreator)){//如果设置为显示作者，则显示作者信息
					innerHtml.append("<td width=\"115px\">");
					innerHtml.append("	<span class =\"linkgray\">").append(cal.getCalUserName()).append("</span>");
					innerHtml.append("</td>");
				}
				
				if("1".equals(showDate)){//如果设置为显示日期，则显示日期信息
					innerHtml.append("<td width=\"100px\">");
					innerHtml.append("	<span class =\"linkgray10\">(").append(st.format(cal.getCalStartTime())).append(")到(").append(st.format(cal.getCalEndTime())).append(")</span>");
					innerHtml.append("	</td>");
				}
				innerHtml.append("	</tr>");
				innerHtml.append("	</table>");
			}
		}
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示	 WIDTH=\"15\" HEIGHT=\"10\" 
	}
	
	/**
	 * author:luosy
	 * description:桌面万年历 悬停显示详情
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String showDesktopDetail()throws Exception {
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(model.getCalStartTime());
		List list = manager.getListForTable(cal,ifleader);
		getRequest().setAttribute("list", list);
		return "desktop-detail";
	}

	/**
	 * author:luosy
	 * description: 桌面万年历
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String calForDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		
		String blockid = getRequest().getParameter("blockid");//获取blockid
		Map<String,String> map = sectionManager.getParam(blockid);//通过blockid获取映射对象
		String showNum = map.get("showNum");//显示条数
		String subLength = map.get("subLength");//主题长度
		String showCreator = map.get("showCreator");//是否显示作者
		String showDate = map.get("showDate");//是否显示日期
		
		int num = 0,length = 0;
		StringBuffer link = new StringBuffer();//跳转连接
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		innerHtml.append("<div id=\"calframe\" align=\"center\" style=\"width:100%;\">")
				 .append("<input type=\"hidden\" id=\"calDeskTopID\" value=\"").append(blockid).append("\">")
				 .append("<iframe id=\"daycontent\" src='").append(getRequest().getContextPath()).append("/calendar/calendar!desktop.action'")  
				 .append("frameborder=\"0\" scrolling=\"no\" height=\"285px\" width=\"100%\" align=\"top\" ></iframe></div>");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}

	
	/**
	 * author:luosy
	 * description:点击下个月的图标 载入下个月的日程事务
	 * modifyer:
	 * description:
	 * @return
	 */
	public String changeCalendar(){
		Calendar startCal = Calendar.getInstance();
		if(!"".equals(termStart)&&null!=termStart){
			String[] toDate = termStart.split("-"); 
			startCal.set(new Integer(toDate[0]), new Integer(toDate[1]), new Integer(toDate[2]), 00, 00);
		}
		Calendar endCal = Calendar.getInstance();
		if(!"".equals(termEnd)&&null!=termEnd){
			String[] toDate = termEnd.split("-"); 
			endCal.set(new Integer(toDate[0]), new Integer(toDate[1]), new Integer(toDate[2]), 23, 59);
		}
		List calList = null;
		boolean canEdit = true;
		if("leader".equals(inputType)){
			calList = manager.getleaderCalList(startCal, endCal,userId, inputType);
			canEdit = false;
		}else if("share".equals(inputType)){
			calList = manager.getshareCalList(startCal, endCal,userId, inputType);
			canEdit = false;
		}else{
			calList = manager.getListByTime(startCal, endCal,activityId);
		}
		JSONArray array = manager.makeCalListToJSONArray(calList, canEdit);
		return renderText(array.toString());
	}

	/**
	 * author:hecj
	 * description:日历上显示日程
	 */
	public String changeAssignCalendar(){
		Calendar startCal = Calendar.getInstance();
		if(!"".equals(termStart)&&null!=termStart){
			String[] toDate = termStart.split("-"); 
			startCal.set(new Integer(toDate[0]), new Integer(toDate[1]), new Integer(toDate[2]), 00, 00);
		}
		Calendar endCal = Calendar.getInstance();
		if(!"".equals(termEnd)&&null!=termEnd){
			String[] toDate = termEnd.split("-"); 
			endCal.set(new Integer(toDate[0]), new Integer(toDate[1]), new Integer(toDate[2]), 23, 59);
		}
		List calList = null;
		boolean canEdit = true;
		if("leader".equals(inputType)){
			calList = manager.getleaderCalList(startCal, endCal,userId, inputType);
			canEdit = false;
		}else if("share".equals(inputType)){
			calList = manager.getshareCalList(startCal, endCal,userId, inputType);
			canEdit = false;
		}else{
			calList = manager.getAssignListByTime(startCal, endCal,activityId);
		}
		JSONArray array = manager.makeCalListToJSONArray(calList, canEdit);
		return renderText(array.toString());
	}
	
	
	/**
	 * author:hecj
	 * description:设置授权用户列表list
	 */
	/*public List<Object[]> getAssignList(){
		return this.assignList;
	}
	
	public void setAssignList(List<Object[]> assignList){
		this.assignList=assignList;
	}*/
	
	/**
	 * 导出excel形式的领导日程
	 */
	public String exportExcel(){
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();
		String initDate = request.getParameter("initDate");
		Calendar now = Calendar.getInstance();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf1.parse(initDate);
			now.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");	
		BaseDataExportInfo export = new BaseDataExportInfo();
		now.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		String sheetTitle = "厅领导一周活动安排("+now.get(Calendar.YEAR)+"年"+sdf.format(now.getTime())+"-";
		export.setSheetName("Sheet1");
		List<String> tableHead = new ArrayList<String>();
		tableHead.add("");
		tableHead.add(sdf.format(now.getTime())+"(星期日)");
		String startTime = sdf1.format(now.getTime());
		now.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		tableHead.add(sdf.format(now.getTime())+"(星期一)");
		now.set(Calendar.DAY_OF_WEEK,Calendar.TUESDAY);
		tableHead.add(sdf.format(now.getTime())+"(星期二)");
		now.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
		tableHead.add(sdf.format(now.getTime())+"(星期三)");
		now.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
		tableHead.add(sdf.format(now.getTime())+"(星期四)");
		now.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
		tableHead.add(sdf.format(now.getTime())+"(星期五)");
		now.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		tableHead.add(sdf.format(now.getTime())+"(星期六)");
		String endTime = sdf1.format(now.getTime());
		sheetTitle = sheetTitle + sdf.format(now.getTime()) +")";
		export.setWorkbookFileName(sheetTitle);
		export.setSheetTitle(sheetTitle);
		export.setSheetTitleHigh(800);
		export.setTableHead(tableHead);
		//查询开始结束时间
		Calendar startCal = Calendar.getInstance();
		if(!"".equals(startTime)&&null!=startTime){
			String[] toDate = startTime.split("-");
			startCal.set(new Integer(toDate[0]), new Integer(toDate[1])-1, new Integer(toDate[2]), 00, 00);
		}
		Calendar endCal = Calendar.getInstance();
		if(!"".equals(endTime)&&null!=endTime){
			String[] toDate = endTime.split("-"); 
			endCal.set(new Integer(toDate[0]), new Integer(toDate[1])-1, new Integer(toDate[2]), 23, 59);
		}
		List calList = manager.getAssignListByTime(startCal, endCal,activityId);
		Vector<String> colsAM = new Vector<String>();//“上午”行
		Vector<String> colsPM = new Vector<String>();//“下午”行
		colsAM.add("上午");
		colsPM.add("下午");
		
		List<String> datas = new ArrayList<String>();//用于保存本周日程数据
		for(int i = 0; i < 14; i++){
			datas.add("");
		}

		if(calList!=null&&calList.size()!=0){
			for(int i = 0; i < calList.size(); i++){
				Object[] objs = (Object[])calList.get(i);
				SimpleDateFormat sdf2 = new SimpleDateFormat("E-HH:mm");
				String time = sdf2.format(objs[2]);
				String eTime = sdf2.format(objs[3]);
				
				Date timeInWeek;
				Date timeInWeek2;
				Integer startPosition = new Integer(0);
				Integer endPosition = new Integer(13);
				try {//判断日程是否跨周
					timeInWeek = sdf1.parse(objs[2].toString());
					if(timeInWeek.getTime()>startCal.getTime().getTime()){				
						Integer startAmOrPm = new Integer(time.substring(4, 6));
						if(time.indexOf("星期日")>-1){
							time = time.replace("星期日", "0");
						}else if(time.indexOf("星期一")>-1){
							time = time.replace("星期一", "2");
						}else if(time.indexOf("星期二")>-1){
							time = time.replace("星期二", "4");
						}else if(time.indexOf("星期三")>-1){
							time = time.replace("星期三", "6");
						}else if(time.indexOf("星期四")>-1){
							time = time.replace("星期四", "8");
						}else if(time.indexOf("星期五")>-1){
							time = time.replace("星期五", "10");
						}else if(time.indexOf("星期六")>-1){
							time = time.replace("星期六", "12");
						}
						if(startAmOrPm<13){
							time = time.replace(time.substring(time.length()-5), "0");
						}else{
							time = time.replace(time.substring(time.length()-5), "1");
						}
						String[] sts = time.split("-");
						startPosition = new Integer(sts[0]) + new Integer(sts[1]);
					}
					
					timeInWeek2 = sdf1.parse(objs[3].toString());
					if(timeInWeek2.getTime()<endCal.getTime().getTime()){
						Integer endAmOrPm = new Integer(eTime.substring(4, 6));
						if(eTime.indexOf("星期日")>-1){
							eTime = eTime.replace("星期日", "0");
						}else if(eTime.indexOf("星期一")>-1){
							eTime = eTime.replace("星期一", "2");
						}else if(eTime.indexOf("星期二")>-1){
							eTime = eTime.replace("星期二", "4");
						}else if(eTime.indexOf("星期三")>-1){
							eTime = eTime.replace("星期三", "6");
						}else if(eTime.indexOf("星期四")>-1){
							eTime = eTime.replace("星期四", "8");
						}else if(eTime.indexOf("星期五")>-1){
							eTime = eTime.replace("星期五", "10");
						}else if(eTime.indexOf("星期六")>-1){
							eTime = eTime.replace("星期六", "12");
						}
						if(endAmOrPm<13){
							eTime = eTime.replace(eTime.substring(eTime.length()-5), "0");
						}else{
							eTime = eTime.replace(eTime.substring(eTime.length()-5), "1");
						}
						String[] ets = eTime.split("-");
						endPosition = new Integer(ets[0]) + new Integer(ets[1]);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				//把本条数据保存进datas
				for(int m = Integer.parseInt(startPosition + ""); m < Integer.parseInt(endPosition + "")+1; m++){
					datas.set(m, datas.get(m)+"\r\n"+objs[8].toString()+objs[0].toString());
				}
			}
		}
		
		for(int n = 0; n<datas.size(); n++){
			if(n%2 == 0){
				colsAM.add(datas.get(n));
			}else{
				colsPM.add(datas.get(n));
			}
		}

		List<Vector<String>> rowList = new ArrayList<Vector<String>>();
		rowList.add(colsAM);
		rowList.add(colsPM);
		export.setRowList(rowList);
		export.setColWidth(5500);
		Map rowhigh = new HashMap();
		rowhigh.put("0", "3000");
		rowhigh.put("1", "3000");
		export.setRowHigh(rowhigh);
		ProcessXSL xsl = new ProcessXSL();
		xsl.createWorkBookSheet(export);
		xsl.writeWorkBook(response);
		return "list";
	}
}
