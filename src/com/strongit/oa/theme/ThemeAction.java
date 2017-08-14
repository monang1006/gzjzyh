/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：界面设置action
 */
package com.strongit.oa.theme;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.ui.rememberme.AbstractRememberMeServices;
import org.springframework.security.userdetails.UserDetails;

import com.strongit.oa.bo.ToaDesktopPortal;
import com.strongit.oa.bo.ToaForamula;
import com.strongit.oa.bo.ToaPagemodel;
import com.strongit.oa.bo.ToaSysmanageBase;
import com.strongit.oa.bo.ToaSystemmanageSystemLink;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.desktop.DesktopPortalManager;
import com.strongit.oa.systemlink.SysLinkManage;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.oa.util.PathUtil;
import com.strongit.oa.util.SyncBbsMailUserpsw;
import com.strongit.oa.viewmodel.ViewModelManager;
import com.strongit.oa.viewmodel.ViewModelPageManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.license.LicenseException;
import com.strongit.uums.license.LicenseManager;
import com.strongit.uums.license.MacUtil;
import com.strongit.uums.optprivilmanage.BaseOptPrivilManager;
import com.strongit.uums.synchroni.SynchroniManager;
import com.strongit.oa.common.user.util.Const;
import com.strongit.oa.common.user.util.PropertiesUtil;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * ��������ACTION
 * 
 * @author pengxq
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "theme!input.action", type = ServletActionRedirectResult.class),
	@Result(name = "left", value = "/WEB-INF/jsp/theme/perspective_content.jsp", type = ServletDispatcherResult.class),
	@Result(name = "menu", value = "/WEB-INF/jsp/theme/menu.jsp", type = ServletDispatcherResult.class),
	@Result(name = "login", value = "/index.shtml", type = ServletDispatcherResult.class),
	@Result(name = "login_Portal", value = "/index_Portal.shtml", type = ServletDispatcherResult.class),
	@Result(name = "login_Simple", value = "/index_Simple.shtml", type = ServletDispatcherResult.class),
	@Result(name = "login_News", value = "/index_News.shtml", type = ServletDispatcherResult.class),
	@Result(name = "perspective", value = "/WEB-INF/jsp/theme/perspective_toolbar.jsp", type = ServletDispatcherResult.class),
	@Result(name = "sessionError", value = "/common/error/406.jsp", type = ServletDispatcherResult.class)})
public class ThemeAction extends BaseActionSupport<ToaSysmanageBase> {

	private Page page = new Page<ToaSysmanageBase>(FlexTableTag.MAX_ROWS, true);
	private List<ToaUumsBaseOperationPrivil> list = new ArrayList<ToaUumsBaseOperationPrivil>(); // 当前用户拥有的权限列表
	private List<ToaDesktopPortal> listPortal = new ArrayList<ToaDesktopPortal>(); // 门户列表
	private List<ToaSystemmanageSystemLink> linkList = new ArrayList<ToaSystemmanageSystemLink>();
	private List<ToaUumsBaseOperationPrivil> privilList; // 系统模块权限集
	private List<HashMap> defaultMenuList = new ArrayList<HashMap>();
	private HashMap<String, String> defaultMenuMap = new HashMap<String, String>();
	private IThemeManager manager; // 界面设置接口
	private IUserService userService; // 用户接口
	private DesktopPortalManager portalManager; // 门户
	private SysLinkManage sysmanager; // 获取联接到外部系统的连接列表
	private SynchroniManager synchManager; // webservice调用service
	private SystemsetManager systemsetManager;
	private BaseOptPrivilManager optprivilmanager;
	private ToaSysmanageBase modle = new ToaSysmanageBase();// 界面设置bo
	private ToaSystemset systemset = new ToaSystemset();
	private TUumsBaseOrg org;
	private ToaSysmanageBase modle1;
	private String baseId; // 界面设置主键id
	private String priviparent; // 当前选择的导航对应的权限id
	private String url; // 财政的单点登陆地址
	private String privilRuleCode; // 权限编码规则
	private String sysSysCode; // 当前外挂系统的系统编码
	private String baseMenuIcon;
	private String sysset;
	private String j_username; // 用户登录名 用于Cookie记录功能
	private final static int MENUNUM = 5;
	private final static int NOMENU = 0;
	private String pageModelId;
	private String modelId;
	private File[] file;
	private Integer refeshTime; // 桌面自动刷新时间间隔，如果为0则不刷新

	
	//////////////////////////////////////////
	private File files;
	private File topPic;
	private File topPicRed;
	private File topPicBlue;
	private File topPicGray;
	private File topPicGreen;
	////////////////////////////////
	// 当前登录用户选择的机构ID huzw
	private String currentOrgId = "";
	public String getCurrentOrgId() {
		return currentOrgId;
	}

	public void setCurrentOrgId(String currentOrgId) {
		this.currentOrgId = currentOrgId;
	}

	private List<TUumsBaseOrg> orgList = new ArrayList<TUumsBaseOrg>();

	public List<TUumsBaseOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<TUumsBaseOrg> orgList) {
		this.orgList = orgList;
	}

	@Autowired
	private ViewModelManager viewModelManager;

	@Autowired
	private ViewModelPageManager viewModelPageManager;
	
	@Autowired
	protected AdapterBaseWorkflowManager adapterBaseWorkflowManager;

	public String input() throws Exception {
		getRequest().setAttribute("backlocation",
				getRequest().getContextPath() + "/theme/theme!input.action");
		prepareModel();
		List list = optprivilmanager.getCurrentUserFirstMenuPrivil();
		ToaUumsBaseOperationPrivil privil = null;
		for (int i = 0; list != null && i < list.size(); i++) {
			privil = (ToaUumsBaseOperationPrivil) list.get(i);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("code", "nav_" + privil.getPrivilSyscode());
			map.put("name", privil.getPrivilName());
			defaultMenuList.add(defaultMenuMap);
			defaultMenuMap.put("nav_" + privil.getPrivilSyscode(), privil
					.getPrivilName());
		}
		return INPUT;
	}

	/*
	 * 
	 * @author: 彭小青
	 * 
	 * @date: Dec 14, 2009 10:53:17 AM Desc: 获取个人界面设置的信息 param:
	 */
	public String myInput() throws Exception {
		getRequest().setAttribute("backlocation",
				getRequest().getContextPath() + "/theme/theme!myInput.action");
		modle = manager.getThemeByUser(userService.getCurrentUser().getUserId());
		List list = optprivilmanager.getCurrentUserFirstMenuPrivil();
		ToaUumsBaseOperationPrivil privil = null;
		for (int i = 0; list != null && i < list.size(); i++) {
			privil = (ToaUumsBaseOperationPrivil) list.get(i);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("code", "nav_" + privil.getPrivilSyscode());
			map.put("name", privil.getPrivilName());
			defaultMenuList.add(defaultMenuMap);
			defaultMenuMap.put("nav_" + privil.getPrivilSyscode(), privil
					.getPrivilName());
		}
		return "myself";
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:17:45
	 * @desc: 上导航action
	 * @param
	 * @return
	 */
	public String RefreshToolbar() throws Exception {
		int k = MENUNUM;
		int size = 0;
		
		String limitType = getRequest().getParameter("limitType");
		
		
		getRequest().setAttribute(
				"backlocation",
				getRequest().getContextPath()
						+ "/theme/theme!RefreshToolbar.action");
		prepareModel();
		
		modle1 = manager.getThemeByUser(userService.getCurrentUser().getUserId());
		UserDetails userDetail = this.getUserDetails();
		list = manager.getFastMenuList(userDetail);
		org = userService.getUserDepartmentByUserId(userService.getCurrentUser()
				.getUserId());
		
		
		orgList = userService.getOrgInfosByUserId(userService.getCurrentUser()
				.getUserId());
		if(orgList==null){
			orgList = new ArrayList<TUumsBaseOrg>();
		}
		currentOrgId = userService.getCurrentUser().getOrgId();
		
		
		// listPortal = portalManager.getPortalPages(page, null).getResult();
		listPortal = portalManager.getDesktopProtaListForAll(userService
				.getCurrentUser().getUserId());

		List<ToaUumsBaseOperationPrivil> testList = new ArrayList<ToaUumsBaseOperationPrivil>(); // 用户快捷菜单list
		List<ToaUumsBaseOperationPrivil> testList2 = new ArrayList<ToaUumsBaseOperationPrivil>();
		List<ToaDesktopPortal> listPortal1 = new ArrayList<ToaDesktopPortal>(); // 多门户列表
		List<ToaDesktopPortal> listPortal2 = new ArrayList<ToaDesktopPortal>();
		if (listPortal != null && listPortal.size() > 0) {
			for (int i = 0; i < listPortal.size(); i++) {
				if (i < k) { // 前5个门户
					listPortal1.add(listPortal.get(i));
				} else {
					listPortal2.add(listPortal.get(i));
				}
			}
		}
		if (listPortal != null) {
			size = listPortal.size();
		}
		if (size > k - 1) {
			k = NOMENU;
		} else {
			k = MENUNUM - size;
		}
		if (list.size() > 0) {
			//			modify by luosy 办公厅样式快捷菜单
			if(limitType!=null&&!"".equals(limitType)){
				k=0;	//所有的快捷菜单都放在下拉菜单中
			}
			//end			modify by luosy 办公厅样式快捷菜单
			
			for (int i = 0; i < list.size(); i++) {
				if (i < k) { // 前5个快捷菜单
					testList.add(list.get(i));
				} else {
					testList2.add(list.get(i));
				}
			}
		}
		linkList = sysmanager.getAllSysLink();
		HttpServletRequest request = getRequest(); // 获取session
		request.setAttribute("testList", testList); // 保存到session范围内
		request.setAttribute("testList2", testList2);
		request.setAttribute("listPortal1", listPortal1);
		request.setAttribute("listPortal2", listPortal2);
		User user = userService.getCurrentUser();
		request.setAttribute("user", user);
		systemset = systemsetManager.getSystemset(); // 获取系统全局配置信息
		privilList = optprivilmanager.getCurrentUserFirstMenuPrivil();// 小山
																		// 一级菜单列表
		List<ToaUumsBaseOperationPrivil> priviparentlist = optprivilmanager
				.getCurrentUserFirstMenuPrivil();
		List<ToaUumsBaseOperationPrivil> list2 = new ArrayList<ToaUumsBaseOperationPrivil>(
				0);
		for (int i = 0; i < priviparentlist.size(); i++) {
			ToaUumsBaseOperationPrivil m = new ToaUumsBaseOperationPrivil();
			m = priviparentlist.get(i);
			List<ToaUumsBaseOperationPrivil> list = new ArrayList<ToaUumsBaseOperationPrivil>(0);
			
			//modify by luosy 办公厅样式只显示到二级菜单
			if(limitType!=null&&!"".equals(limitType)){//是否控制菜单显示级数
				list = optprivilmanager.getCurrentUserAllSecondMenuPrivil(m.getPrivilSyscode());
			}else{
				list = optprivilmanager.getCurrentUserSecondMenuPrivil(m.getPrivilSyscode());
			}
			//end			modify by luosy 办公厅样式只显示到二级菜单

			list2.addAll(list);
		}
		
		//列出用户拥有资源权限的三级菜单，非Tab网页版界面使用     申仪玲   20120611
		List<ToaUumsBaseOperationPrivil> list3 = new ArrayList<ToaUumsBaseOperationPrivil>(0);
		if(list2 != null){
			for (int i = 0; i < list2.size(); i++) {
				ToaUumsBaseOperationPrivil m = new ToaUumsBaseOperationPrivil();
				List<ToaUumsBaseOperationPrivil> list = new ArrayList<ToaUumsBaseOperationPrivil>(0);
				m = list2.get(i);
				if(null!=m.getPrivilSyscode()){
					list = optprivilmanager.getCurrentUserThirdMenuPrivil(m.getPrivilSyscode());
					list3.addAll(list);
				}
			}			
		}
		
		
		String userId = user.getUserLoginname();
		String userPassword = (String) getRequest().getSession().getAttribute("ExpresslyPassword");
		request.setAttribute("userName", userId);
		request.setAttribute("password", userPassword);
		
		SyncBbsMailUserpsw syncBbsMailUserpsw = new SyncBbsMailUserpsw();
		String bangongluntan = syncBbsMailUserpsw.getloginURL("bangongluntan");
		String gerenyouxiang = syncBbsMailUserpsw.getloginURL("gerenyouxiang");
		
		getRequest().setAttribute("bangongluntan", bangongluntan);
		getRequest().setAttribute("gerenyouxiang", gerenyouxiang);
		
		getSession().setAttribute("menus2", list2);
		getSession().setAttribute("menus3", list3);
		getSession().setAttribute("menus", privilList);
		getSession().setAttribute("orgName", org.getOrgName());
		System.out.println(pageModelId);
		ToaPagemodel pageModel = viewModelPageManager.getObjById(pageModelId);
		
		//获取服务器当前时间
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyy年MM月dd日 E");   
        String serverTime=dateformat.format(new Date());
        getRequest().setAttribute("serverTime",serverTime);
       
		
		
		if (pageModel != null) {
			String pageModelName = pageModel.getPagemodelName();
			return pageModelName;
		} else {
			return "toolbar";
		}

	}
	
	
	
	/**
	* @description 非Tab网页版界面动态导航栏展现(对应系统三级菜单)
	* @method showToolBar
	* @author 申仪玲(shenyl)
	* @created 2012-6-11 下午07:18:45
	* @return
	* @throws Exception
	* @return String
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	public String showToolBar() throws Exception {				
		//用户所拥有的系统一级资源权限菜单列表
		List<ToaUumsBaseOperationPrivil> list1 = optprivilmanager.getCurrentUserFirstMenuPrivil();
		//用户所拥有的系统二级资源权限菜单列表
		List<ToaUumsBaseOperationPrivil> list2 = new ArrayList<ToaUumsBaseOperationPrivil>(0);
		//用户所拥有的系统三级资源权限菜单列表
		List<ToaUumsBaseOperationPrivil> list3 = new ArrayList<ToaUumsBaseOperationPrivil>(0);
		//用户有启动权限的流程
		List workflowInfoList = adapterBaseWorkflowManager.getWorkflowService().getStartProcess();
		
		if(list1 != null){
			for (int i = 0; i < list1.size(); i++) {
				ToaUumsBaseOperationPrivil m = new ToaUumsBaseOperationPrivil();
				m = list1.get(i);
				List<ToaUumsBaseOperationPrivil> list = new ArrayList<ToaUumsBaseOperationPrivil>(0);			
				list = optprivilmanager.getCurrentUserAllSecondMenuPrivil(m.getPrivilSyscode());		
				list2.addAll(list);
			}
		}
		
		if(list2 != null){
			for (int i = 0; i < list2.size(); i++) {
				ToaUumsBaseOperationPrivil m = new ToaUumsBaseOperationPrivil();
				List<ToaUumsBaseOperationPrivil> list = new ArrayList<ToaUumsBaseOperationPrivil>(0);
				m = list2.get(i);
				list = optprivilmanager.getCurrentUserThirdMenuPrivil(m.getPrivilSyscode());
				list3.addAll(list);
			}			
		}

		StringBuffer innerHtml = new StringBuffer();
		//首页		
		StringBuffer row1 = new StringBuffer();
		//发文处理
		StringBuffer row2 = new StringBuffer();
		//收文处理
		StringBuffer row3 = new StringBuffer();
		//我的流程
		StringBuffer row4 = new StringBuffer();
		//日程管理
		StringBuffer row5 = new StringBuffer();
		//内部邮件
		StringBuffer row6 = new StringBuffer();
		//信息发布
		StringBuffer row7 = new StringBuffer();
		//文件柜
		StringBuffer row8 = new StringBuffer();
		//工作日志
		StringBuffer row9 = new StringBuffer();
		//会议管理
		StringBuffer row10 = new StringBuffer();
		
		//新建流程   shenyiling 20120703
		StringBuffer row11 = new StringBuffer();
		
		String title = "办公门户";
		
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);//获取年份
	    int month=ca.get(Calendar.MONTH) + 1;//获取月份
	    int day=ca.get(Calendar.DATE);//获取日
	    int WeekOfYear = ca.get(Calendar.DAY_OF_WEEK)-1;	    
	    String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };  
	    
	    String showDate = year + "年" + month + "月" + day + "日&nbsp" + weeks[WeekOfYear];
	    
	    modle = manager.getTheme();	 
	    //取得浏览器窗口标题，该属性在系统管理的界面设置中设置
		row1.append("<div>").append("欢迎来到" + modle.getBaseWindowsTitle() + "！今天是" + showDate);
		row2.append("<div>");
		row3.append("<div>");
		row4.append("<div>");
		row5.append("<div>");
		row6.append("<div>");
		row7.append("<div>");
		row8.append("<div>");
		row9.append("<div>");
		row10.append("<div>");
		row11.append("<div>");
		
		if(list3 != null){																
			for (int i = 0; i < list3.size(); i++) {
				ToaUumsBaseOperationPrivil m = new ToaUumsBaseOperationPrivil();
				m = list3.get(i);
				if(m.getPrivilSyscode().startsWith("00020001")){
					/**
					if(m.getPrivilSyscode().equals("000200010006")){
						//公文草拟直接弹出新建发文流程  shenyiling 20120703
						String workflowName =  URLEncoder.encode(URLEncoder.encode("发文流程", "utf-8"), "utf-8");
						row2.append("<a href=\"#\" onclick=\"javascript:creatProcess('" +workflowName + "','82','2')\">" );
						row2.append("&nbsp").append( m.getPrivilName()).append("&nbsp").append("</a>").append("|");
					}else{
						
						row2.append("<a href=\"#\" onclick=\"javascript:refreshWorkByTitle('" +getRequest().getContextPath()
								+ m.getPrivilAttribute() + "','" + title + "')\">" );
						row2.append("&nbsp").append( m.getPrivilName()).append("&nbsp").append("</a>").append("|");
					}
					*/
					row2.append("<a href=\"#\" onclick=\"javascript:refreshWorkByTitle('" +getRequest().getContextPath()
							+ m.getPrivilAttribute() + "','" + title + "')\">" );
					row2.append("&nbsp").append( m.getPrivilName()).append("&nbsp").append("</a>").append("|");
					
				}else if(m.getPrivilSyscode().startsWith("00020002")){
					row3.append("<a href=\"#\" onclick=\"javascript:refreshWorkByTitle('" +getRequest().getContextPath()
							+ m.getPrivilAttribute() + "','" + title + "')\">" );
					row3.append("&nbsp").append( m.getPrivilName()).append("&nbsp").append("</a>").append("|");
					
				}else if(m.getPrivilSyscode().startsWith("00010001")){
					row4.append("<a href=\"#\" onclick=\"javascript:refreshWorkByTitle('" +getRequest().getContextPath()
							+ m.getPrivilAttribute() + "','" + title + "')\">" );
					row4.append("&nbsp").append( m.getPrivilName()).append("&nbsp").append("</a>").append("|");
					
				}else if(m.getPrivilSyscode().startsWith("00010002")){
					row5.append("<a href=\"#\" onclick=\"javascript:refreshWorkByTitle('" +getRequest().getContextPath()
							+ m.getPrivilAttribute() + "','" + title + "')\">" );
					row5.append("&nbsp").append( m.getPrivilName()).append("&nbsp").append("</a>").append("|");
					
				}else if(m.getPrivilSyscode().startsWith("00010003")){
					row6.append("<a href=\"#\" onclick=\"javascript:refreshWorkByTitle('" +getRequest().getContextPath()
							+ m.getPrivilAttribute() + "','" + title + "')\">" );
					row6.append("&nbsp").append( m.getPrivilName()).append("&nbsp").append("</a>").append("|");
					
				}else if(m.getPrivilSyscode().startsWith("00040002")){
					row7.append("<a href=\"#\" onclick=\"javascript:refreshWorkByTitle('" +getRequest().getContextPath()
							+ m.getPrivilAttribute() + "','" + title + "')\">" );
					row7.append("&nbsp").append( m.getPrivilName()).append("&nbsp").append("</a>").append("|");
					
				}else if(m.getPrivilSyscode().startsWith("00010007")){
					row8.append("<a href=\"#\" onclick=\"javascript:refreshWorkByTitle('" +getRequest().getContextPath()
							+ m.getPrivilAttribute() + "','" + title + "')\">" );
					row8.append("&nbsp").append( m.getPrivilName()).append("&nbsp").append("</a>").append("|");
					
				}else if(m.getPrivilSyscode().startsWith("00010010")){
					row9.append("<a href=\"#\" onclick=\"javascript:refreshWorkByTitle('" +getRequest().getContextPath()
							+ m.getPrivilAttribute() + "','" + title + "')\">" );
					row9.append("&nbsp").append( m.getPrivilName()).append("&nbsp").append("</a>").append("|");
					
				}else if(m.getPrivilSyscode().startsWith("00020008")){
					row10.append("<a href=\"#\" onclick=\"javascript:refreshWorkByTitle('" +getRequest().getContextPath()
							+ m.getPrivilAttribute() + "','" + title + "')\">" );
					row10.append("&nbsp").append( m.getPrivilName()).append("&nbsp").append("</a>").append("|");
					
				}				
			}
			
		}
		//按流程类型进行排序
		Collections.sort(workflowInfoList, new Comparator<Object[]>() {
			public int compare(Object[] arg0, Object[] arg1) {
				Long userSequence1 = new Long(arg0[1].toString());
				Long userSequence2 = new Long(arg1[1].toString());
				Long key1;
				if (userSequence1 != null) {
					key1 = userSequence1;
				} else {
					key1 = Long.MAX_VALUE;
				}
				Long key2;
				if (userSequence2 != null) {
					key2 = userSequence2;
				} else {
					key2 = Long.MAX_VALUE;
				}
				return key1.compareTo(key2);
			}
		});
		//定制新建流程下的流程
		if(workflowInfoList != null){
			for (int i = 0; i < workflowInfoList.size(); i++) {
				Object[] workflowInfo = (Object[]) workflowInfoList.get(i);
				String workflowName =  workflowInfo[0].toString();
//				row11.append("<a href=\"#\" onclick=\"javascript:window.open('" +getRequest().getContextPath()
//						+"/senddoc/sendDoc!input.action?workflowName="+workflowName+"&formId=" + workflowInfo[3] + "&workflowType=" + workflowInfo[1]+
//						"')\">" );
				row11.append("<a href=\"#\" onclick=\"javascript:creatProcess('" +workflowName + "','" + workflowInfo[3]+ "','" + workflowInfo[1] + "')\">" );
				row11.append("&nbsp").append( workflowInfo[4]).append("&nbsp").append("</a>").append("|");
			}
		}
		
		row1.append("</div>");
		row2.append("</div>");
		row3.append("</div>");
		row4.append("</div>");
		row5.append("</div>");
		row6.append("</div>");
		row7.append("</div>");
		row8.append("</div>");
		row9.append("</div>");
		row10.append("</div>");
		row11.append("</div>");
		
		List<String> listpri = optprivilmanager.getAllOptPrivilSysCode();
		
		innerHtml.append(row1.toString());
		
		if(listpri.contains("001-00020001")){
			innerHtml.append(row2.toString());
		}
		if(listpri.contains("001-00020002")){
			innerHtml.append(row3.toString());
		}
		if(listpri.contains("001-00010001")){
			innerHtml.append(row4.toString());
		}
		if(listpri.contains("001-00010002")){
			innerHtml.append(row5.toString());
		}
		if(listpri.contains("001-00010003")){
			innerHtml.append(row6.toString());
		}
		if(listpri.contains("001-00040002")){
			innerHtml.append(row7.toString());
		}
		if(listpri.contains("001-00010007")){
			innerHtml.append(row8.toString());
		}
		if(listpri.contains("001-00010010")){
			innerHtml.append(row9.toString());
		}
		if(listpri.contains("001-00020008")){
			innerHtml.append(row10.toString());
		}
		
		if(listpri.contains("001-00010001")){
			innerHtml.append(row11.toString());
		}
		
		logger.info(innerHtml.toString());
		System.out.println(innerHtml.toString());
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}

	
	/**
	* @description 导航栏动态获取用户有展现权限的流程（页面显示的是流程别名）
	* @method showProcess
	* @author 申仪玲(shenyl)
	* @created 2012-7-15 上午11:05:39
	* @return
	* @throws Exception
	* @return String
	* @throws Exception
	*/
	@SuppressWarnings("unchecked")
	public String showProcess() throws Exception {	
		//用户有启动权限的流程
		List<Object[]> workflowInfoList = adapterBaseWorkflowManager.getWorkflowService().getStartProcess();
		StringBuffer innerHtml = new StringBuffer();
		//按流程类型进行排序
		Collections.sort(workflowInfoList, new Comparator<Object[]>() {
			public int compare(Object[] arg0, Object[] arg1) {
				Long userSequence1 = new Long(arg0[1].toString());
				Long userSequence2 = new Long(arg1[1].toString());
				Long key1;
				if (userSequence1 != null) {
					key1 = userSequence1;
				} else {
					key1 = Long.MAX_VALUE;
				}
				Long key2;
				if (userSequence2 != null) {
					key2 = userSequence2;
				} else {
					key2 = Long.MAX_VALUE;
				}
				return key1.compareTo(key2);
			}
		});
		//定制新建流程下的流程
		if(workflowInfoList != null){
			for (int i = 0; i < workflowInfoList.size(); i++) {
				Object[] workflowInfo = (Object[]) workflowInfoList.get(i);
				String workflowName =  workflowInfo[0].toString();
				innerHtml.append("<a href=\"#\" onclick=\"javascript:creatProcess('" +workflowName + "','" + workflowInfo[3]+ "','" + workflowInfo[1] + "')\">" );
				innerHtml.append("&nbsp").append( workflowInfo[4]).append("&nbsp").append("</a>");
			}
		}
		logger.info(innerHtml.toString());
		System.out.println(innerHtml.toString());
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}
	
	/**
	 * 跳转到其他系统（财政系统）
	 * 
	 * @author 蒋国斌
	 *@date 2009-7-22 下午02:55:56
	 * @return
	 * @throws Exception
	 */
	public String jump() throws Exception {

		User user = userService.getCurrentUser();
		String userId = user.getUserLoginname();
		try {
			// HttpSession req=this.getSession();
			// String sessionId=req.getId();
			// String userPassword=synchManager.parseUserPasswordXml(sessionId,
			// userId);
			String userPassword = (String) getRequest().getSession()
					.getAttribute("ExpresslyPassword");
			HttpServletRequest request = getRequest(); // 获取session
			request.setAttribute("userName", userId);
			request.setAttribute("password", userPassword);
			request.setAttribute("url", url);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "fail";
		}
		return "jump";
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:17:59
	 * @desc: 左边导航action
	 * @param
	 * @return
	 */
	public String RefreshLeftside() throws Exception {
		getRequest().setAttribute(
				"backlocation",
				getRequest().getContextPath()
						+ "/theme/theme!RefreshLeftside.action");
		prepareModel();
		modle1 = manager.getThemeByUser(userService.getCurrentUser().getUserId());
		if ((priviparent == null || "".equals(priviparent) || "null"
				.equals(priviparent))
				&& modle1.getBaseDefaultStartMenu() != null) {
			priviparent = modle1.getBaseDefaultStartMenu().substring(4,
					modle1.getBaseDefaultStartMenu().length());
		}
		privilRuleCode = PropertiesUtil.getCodeRule(Const.RULE_CODE_PRIVIL);
		privilList = optprivilmanager.getCurrentUserFirstMenuPrivil();
		if (!optprivilmanager.checkPrivilBySysCode(sysSysCode + "-"
				+ priviparent)) {
			if (privilList != null && privilList.size() > 0) {
				ToaUumsBaseOperationPrivil privil = privilList.get(0);
				priviparent = privil.getPrivilSyscode();
			} else {
				priviparent = null;
			}
		}
		systemset = systemsetManager.getSystemset(); // 获取系统全局配置信息
		ToaPagemodel pageModel = viewModelPageManager.getObjById(pageModelId);
		if (pageModel != null) {
			String pageModelName = pageModel.getPagemodelName();
			return pageModelName;
		} else {
			return "leftside";
		}

	}
	
	
	public String getFirstLevelPrivil() throws Exception {

		privilList = optprivilmanager.getCurrentUserFirstMenuPrivil();
	
	java.util.Collections.sort(privilList,
				new Comparator<ToaUumsBaseOperationPrivil>() {
					public int compare(ToaUumsBaseOperationPrivil arg0,
							ToaUumsBaseOperationPrivil arg1) {
						Long key1, key2;
						if (arg0.getPrivilSequence() != null) {
							key1 = Long.valueOf(arg0
									.getPrivilSequence());
						} else {
							key1 = Long.MAX_VALUE;
						}
						if (arg1.getPrivilSequence() != null) {
							key2 = Long.valueOf(arg1
									.getPrivilSequence());
						} else {
							key2 = Long.MAX_VALUE;
						}
						return key2.compareTo(key1);
					}
				});
		return "left";
	}
	
	public String getSubPrivilMenu2() throws Exception {
		try {
			if(sysSysCode == null || "".equals(sysSysCode)){
				throw new Exception("请设置要展现模块信息的系统编码!");
			}
			
	ToaUumsBaseOperationPrivil privils = optprivilmanager
			.getPrivilInfoByPrivilSyscode(priviparent);
	privilList = optprivilmanager.getCurrentUserFirstMenuPrivil();

	String priviparentname = privils.getPrivilName();
	privilList = optprivilmanager
			.getCurrentUserSecondMenuPrivil(priviparent);
	privilList.add(privils);
			
			/**
			 * 修改日期：2011.3.27 修改内容：增加对权限结果集的排序
			 */
			java.util.Collections.sort(privilList,
					new Comparator<ToaUumsBaseOperationPrivil>() {
						public int compare(ToaUumsBaseOperationPrivil arg0,
								ToaUumsBaseOperationPrivil arg1) {
							Long key1, key2;
							if (arg0.getPrivilSequence() != null) {
								key1 = Long.valueOf(arg0
										.getPrivilSequence());
							} else {
								key1 = Long.MAX_VALUE;
							}
							if (arg1.getPrivilSequence() != null) {
								key2 = Long.valueOf(arg1
										.getPrivilSequence());
							} else {
								key2 = Long.MAX_VALUE;
							}
							return key1.compareTo(key2);
						}
					});
			int nextPrivilLength = userService.findChildCodeLength(Const.RULE_CODE_PRIVIL, this.priviparent);//指定以及权限的下级权限编码长度
			boolean hasThreePrivil = false;
			
			StringBuffer treeNodes = new StringBuffer("");
			
			if(privilList != null && !privilList.isEmpty()){
				for(ToaUumsBaseOperationPrivil privil: privilList){
					if(privil.getPrivilSyscode().length() >= nextPrivilLength){
						hasThreePrivil = true;//拥有三层次的资源权限	
					}
					treeNodes.append(",{")
							 .append("\"id\":").append("\"").append(privil.getPrivilSyscode()).append("\"").append(",");
					if("1".equals(privil.getRest4())){
						treeNodes.append("\"name\":").append("\"<font color='red'>").append(privil.getPrivilName()).append("(委托)</font>\"").append(",");
					}else{
						if(this.priviparent.equals(privil.getPrivilSyscode())){
							treeNodes.append("\"name\":").append("\"").append(privil.getPrivilName()).append("\"").append(",");
						}else if(this.priviparent.equals(privil.getPrivilSyscode()) || privil.getPrivilSyscode().length()==nextPrivilLength){
							if(url.indexOf("theme_red")==-1){
							 treeNodes.append("\"name\":").append("\"<img  src='").append(this.getRequest().getContextPath()).append("/theme/theme_default/image/menu/288.gif' border=0 /> ").append(privil.getPrivilName()).append("\"").append(",");
						}else{
							 treeNodes.append("\"name\":").append("\"<img  src='").append(this.getRequest().getContextPath()).append("/theme/theme_red/image/menu/288.gif' border=0 /> ").append(privil.getPrivilName()).append("\"").append(",");
							}
						}
					}
					
					if (!this.priviparent.equals(privil.getPrivilSyscode()) && privil.getPrivilSyscode().length()!=nextPrivilLength) {
						if(url.indexOf("theme_red")==-1){
						  treeNodes.append("\"name\":").append("\"<img  src='").append(this.getRequest().getContextPath()).append("/theme/theme_default/image/menu/289.gif' border=0 /> ").append(privil.getPrivilName()).append("\"").append(",");
						}else{
							  treeNodes.append("\"name\":").append("\"<img  src='").append(this.getRequest().getContextPath()).append("/theme/theme_red/image/menu/289.gif' border=0 /> ").append(privil.getPrivilName()).append("\"").append(",");
						}
						treeNodes.append("\"url\":").append("\"").append(
								"javascript:redirect('").append(
								this.getRequest().getContextPath()).append(
								privil.getPrivilAttribute()).append("','")
								.append(privil.getPrivilName()).append("','")
								.append(privil.getPrivilSyscode())
								.append("');").append("\"").append(",");
					}
					
					if(this.priviparent.equals(privil.getPrivilSyscode())){
						treeNodes.append("\"pid\":").append("\"-1\"");
					}else if(hasThreePrivil && privil.getPrivilSyscode().length() == nextPrivilLength){
//					}else if(privil.getPrivilSyscode().length() == nextPrivilLength){
						treeNodes.append("\"pid\":").append("\"-1\"");
					}else if(!hasThreePrivil && privil.getPrivilSyscode().length() == nextPrivilLength){
						treeNodes.append("\"pid\":").append("\"").append(this.priviparent).append("\"");
						
					}else{
						treeNodes.append("\"pid\":").append("\"").append(privil.getPrivilSyscode().substring(0, nextPrivilLength)).append("\"");
					}
					treeNodes.append("}");
				}
			}
			System.out.println(treeNodes);
			this.getResponse().setContentType("application/json");
			this.getResponse().getWriter().write("".equals(treeNodes.toString())?"[]":("[" + treeNodes.toString().substring(1) + "]"));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}
	
	
	/**
	 * 列出一级菜单对应的二级菜单(只列出二级菜单) add by yangwg
	 * 
	 * @author strong_yangwg
	 * @return
	 * @throws Exception
	 */
	public String RefreshPriviMenu() throws Exception {
		getRequest().setAttribute(
				"backlocation",
				getRequest().getContextPath()
						+ "/theme/theme!RefreshPriviMenu.action");
		prepareModel();
		modle1 = manager.getThemeByUser(userService.getCurrentUser().getUserId());
		if ((priviparent == null || priviparent.equals("") || priviparent
				.equals("null"))
				&& modle1.getBaseDefaultStartMenu() != null) {
			priviparent = modle1.getBaseDefaultStartMenu().substring(4,
					modle1.getBaseDefaultStartMenu().length());
		}
		this.logger.info("----------------------->priviparent:" + priviparent);
		// 当前用户第一级菜单权限
		List<ToaUumsBaseOperationPrivil> priviparentlist = optprivilmanager
				.getCurrentUserFirstMenuPrivil();
		// 系统模块菜单
		ToaUumsBaseOperationPrivil privil = optprivilmanager
				.getPrivilInfoByPrivilSyscode(priviparent);
		privilList = optprivilmanager.getCurrentUserFirstMenuPrivil();
		if (!optprivilmanager.checkPrivilBySysCode(sysSysCode + "-"
				+ priviparent)) {
			if (privilList != null && privilList.size() > 0) {
				privil = priviparentlist.get(0);
				priviparent = privil.getPrivilSyscode();
			} else
				priviparent = null;
		}

		String priviparentname = privil.getPrivilName();
		// 只列出二级菜单（不包括子菜单）
		privilList = optprivilmanager
				.getCurrentUserAllSecondMenuPrivil(priviparent);
		getRequest().setAttribute("priviparentname", priviparentname);
		systemset = systemsetManager.getSystemset(); // 获取系统全局配置信息
		ToaPagemodel pageModel = viewModelPageManager.getObjById(pageModelId);
		if (pageModel != null) {
			String pageModelName = pageModel.getPagemodelName();
			System.out.println("--------------------------->pageModelName:"
					+ pageModelName);
			return pageModelName;
		} else {
			return "module";
		}
	}

	/**
	 * 查询二级模块所有的子模块 add by yangwg
	 * 
	 * @return
	 * @throws Exception
	 */
	public String RefreshPriviModule() throws Exception {
		// 查询该模块下所有的子模块,priviparent为父级模块编码
		privilList = optprivilmanager
				.getCurrentUserSecondMenuPrivil(priviparent);
		return "modulePage";
	}

	/*
	 * 
	 * @author: 彭小青
	 * 
	 * @date: Dec 14, 2009 10:47:42 AM Desc: 获取菜单树节点 param:
	 */
	public String RefreshPrivilist() throws Exception {
		getRequest().setAttribute(
				"backlocation",
				getRequest().getContextPath()
						+ "/theme/theme!RefreshPrivilist.action");
		prepareModel();
		User user1=new User();
		try{
			 user1 = userService.getCurrentUser();
		}catch (Exception e) {
			/** 如果session过期，抛出异常时，进行处理。跳转到session超期界面。niwy*/
			if(e.getMessage().toString().indexOf("很抱歉，会话过程已结束，请您重新登录。")!=-1){
				return "sessionError";
			}

		}
		modle1 = manager.getThemeByUser(user1.getUserId());
		if ((priviparent == null || priviparent.equals("") || priviparent
				.equals("null"))
				&& modle1.getBaseDefaultStartMenu() != null) {
			priviparent = modle1.getBaseDefaultStartMenu().substring(4,
					modle1.getBaseDefaultStartMenu().length());
		}
		List<ToaUumsBaseOperationPrivil> priviparentlist = optprivilmanager
				.getCurrentUserFirstMenuPrivil();
		ToaUumsBaseOperationPrivil privil = optprivilmanager
				.getPrivilInfoByPrivilSyscode(priviparent);
		privilList = optprivilmanager.getCurrentUserFirstMenuPrivil();
		if (!optprivilmanager.checkPrivilBySysCode(sysSysCode + "-"
				+ priviparent)) {
			if (privilList != null && privilList.size() > 0) {
				privil = priviparentlist.get(0);
				priviparent = privil.getPrivilSyscode();
			} else
				priviparent = null;
		}

		String priviparentname = privil.getPrivilName();
		privilList = optprivilmanager
				.getCurrentUserSecondMenuPrivil(priviparent);
		getRequest().setAttribute("priviparentname", priviparentname);
		systemset = systemsetManager.getSystemset(); // 获取系统全局配置信息
		ToaPagemodel pageModel = viewModelPageManager.getObjById(pageModelId);
		if (pageModel != null) {
			String pageModelName = pageModel.getPagemodelName();
			return pageModelName;
		} else {
			return "module";
		}

	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:17:24
	 * @desc: 顶层框架action
	 * @param
	 * @return
	 */
	public String RefreshTop() throws Exception {
		// 2011年7月18日添加软件保护模块调用
		try {
			LicenseManager.checkAuthorized("UUMS", "2.0");
		} catch (LicenseException e) {
			HttpServletRequest req = getRequest();
			req.setAttribute("error", e.getMessage());
			req.setAttribute("code", LicenseManager.getCode());
			return "reg";
		}
		getRequest().setAttribute(
				"backlocation",
				getRequest().getContextPath()
						+ "/theme/theme!RefreshTop.action");
		prepareModel();
		systemset = systemsetManager.getSystemset(); // 获取系统全局配置信息
		String interfaceTheme = modle.getBaseInterfaceThemes(); // 获取界面主题设置
		User user1=new User();
		try{
			 user1 = userService.getCurrentUser();
		}catch (Exception e) {
			/** 如果session过期，抛出异常时，进行处理。跳转到session超期界面。niwy*/
			if(e.getMessage().toString().indexOf("很抱歉，会话过程已结束，请您重新登录。")!=-1){
				return "sessionError";
			}

		}
		ToaSysmanageBase myTheme = manager.getThemeByUser(user1.getUserId());
		if (myTheme == null) {
			List<ToaUumsBaseOperationPrivil> list = optprivilmanager
					.getCurrentUserFirstMenuPrivil();
			myTheme = new ToaSysmanageBase();
			myTheme.setBaseInterfaceThemes(modle.getBaseInterfaceThemes()); // 界面主题
			myTheme.setBaseMenuIcon(modle.getBaseMenuIcon()); // 快捷菜单图标
			myTheme.setUserId(userService.getCurrentUser().getUserId());
			if (!optprivilmanager.checkPrivilBySysCode("001-"+modle
					.getBaseDefaultStartMenu().substring(4,
							modle.getBaseDefaultStartMenu().length()))) { // 默认选项是否有权限
				myTheme.setBaseDefaultStartMenu("nav_0002");
				if (list != null && list.size() != 0) {
					myTheme.setBaseDefaultStartMenu("nav_"
							+ list.get(0).getPrivilAttribute());
				}
			} else {
				myTheme
						.setBaseDefaultStartMenu(modle
								.getBaseDefaultStartMenu()); // 默认展开菜单
			}
			manager.saveTheme(myTheme);
		} else {
			logger.info("该用户存在主题");
		}
		HttpSession session = getSession(); // 获取界面主题设置
		interfaceTheme = myTheme.getBaseInterfaceThemes(); // 根据自己的主题来赋值
		if (interfaceTheme == null || interfaceTheme.equals("")
				|| interfaceTheme.equals("null"))
			interfaceTheme = "/frame/theme_gray"; // 默认界面主题为灰色方案
		session.setAttribute("eWebEditor_User", "OK");
		User user = userService.getCurrentUser();
		session.setAttribute("userName", user.getUserName());
		session.setAttribute("frameroot", interfaceTheme); // 将界面主题保存在session范围内	
		Integer refreshtime = systemset.getRefreshTime();
		if(refreshtime == null || "".equals(refreshtime) ){
			refreshtime = 0;
		}
		session.setAttribute("refreshTime", refreshtime);		
		session.setAttribute("sysTheme", modle); // 新增
		linkList = sysmanager.getAllSysLink(); // 新增
		session.setAttribute("linkList", linkList); // 新增

		String pageName = "";
		if (modelId != null && !"".equals(modelId) && !"null".equals(modelId)) {
			ToaForamula previewObj = viewModelManager.getObjById(modelId);
			List<ToaPagemodel> sonList = viewModelPageManager
					.getModelRootPage(previewObj.getForamulaId());
			if (sonList.size() == 0) {
				return "top";
			} else {
				return sonList.get(0).getPagemodelName();
			}
		} else {
			pageName = viewModelManager.getDefaultModel(user.getUserId());
		}
		if (pageName == null) {
			return "top";
		} else {
			return pageName;
		}

	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:17:24
	 * @desc: 顶层框架action
	 * @param
	 * @return
	 */
	public String login() throws Exception {
		// 2011年7月18日添加软件保护模块调用
		try {
			LicenseManager.checkAuthorized("UUMS", "2.0");
		} catch (LicenseException e) {
			HttpServletRequest req = getRequest();
			req.setAttribute("error", e.getMessage());
			req.setAttribute("code", LicenseManager.getCode());
			return "reg";
		}
		getRequest().setAttribute("backlocation",
				getRequest().getContextPath() + "/theme/theme!login.action");
		prepareModel();
		String interfaceTheme = modle.getBaseInterfaceThemes(); // 获取界面主题设置
		String loginTheme = modle.getBaseLoginThemes();			// 获取登陆界面风格设置
		HttpSession session = getSession(); // 获取界面主题设置
		if (interfaceTheme == null || interfaceTheme.equals("")
				|| interfaceTheme.equals("null"))
			interfaceTheme = "/frame/theme_gray"; // 默认界面主题为灰色方案
		session.setAttribute("frameroot", interfaceTheme); // 将界面主题保存在session范围内
		Properties properties = new Properties();
		URL in = this.getClass().getClassLoader().getResource(
				"appconfig.properties");
		try {
			properties.load(new FileInputStream(in.getFile()));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		systemset = systemsetManager.getSystemset(); // 获取系统全局配置信息
		if (systemset != null)
			sysset = systemset.getUsbkey(); // 获取是否启用USB登陆方式
		session.setAttribute("version", properties.getProperty("version")); // 将界面主题保存在session范围内
		session.setAttribute("contextPath", getRequest().getContextPath()); // 将界面主题保存在session范围内

		// 增加Cooike中读取用户名功能 邓志城 2010年9月6日10:03:50
		Cookie[] cookies = getRequest().getCookies();
		if ((cookies != null) && (cookies.length != 0)) {
			for (int i = 0; i < cookies.length; i++) {
				if (AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY
						.equals(cookies[i].getName())) {
					j_username = cookies[i].getValue();
					j_username = URLDecoder.decode(j_username, "utf-8");
					logger.info("从cookie中读取用户名为：" + j_username);
					break;
				}
			}
		}
		// ca认证原文
		String randNum = generateRandomNum();
		session.setAttribute("original_data", randNum);
		session.setAttribute("picpath", modle.getPicPath());
		// 登陆界面风格 BY 刘皙
		if(("portal").equals(loginTheme)){
			return "login_Portal";
		}else if(("simple").equals(loginTheme)){
			return "login_Simple";
		}else if(("news").equals(loginTheme)){
			return "login_News";
		}else {
			return "login";
		}
	}

	/**
	 * 产生认证原文
	 */
	private String generateRandomNum() {
		/**************************
		 * 第二步 服务端产生认证原文 *
		 **************************/
		String num = "1234567890abcdefghijklmnopqrstopqrstuvwxyz";
		int size = 6;
		char[] charArray = num.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb
					.append(charArray[((int) (Math.random() * 10000) % charArray.length)]);
		}
		return sb.toString();
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	/*
	 * 
	 * @author: 彭小青
	 * 
	 * @date: Dec 14, 2009 10:46:36 AM Desc: 保存界面设置 param:
	 */
	public String mySave() throws Exception {
		getRequest().setAttribute("backlocation",getRequest().getContextPath() + "/theme/theme!myInput.action");
		String msg = manager.saveTheme(modle); // 保存界面设置内容
		StringBuffer returnhtml = new StringBuffer(
				"<script> var scriptroot = '").append(
				getRequest().getContextPath()).append("'</script>").append(
				"<SCRIPT src='").append(getRequest().getContextPath()).append(
				"/common/js/commontab/workservice.js'>").append("</SCRIPT>")
				.append("<SCRIPT src='").append(getRequest().getContextPath())
				.append("/common/js/commontab/service.js'>")
				.append("</SCRIPT>").append("<script>");
		if (msg != null && !"".equals(msg)) {
			returnhtml.append("alert('").append(getActionMessages().toString())
					.append("');");
		}
		returnhtml.append(" location='").append(getRequest().getContextPath())
				.append("/theme/theme!myInput.action").append("';</script>");
		return renderHtml(returnhtml.toString());
	}

	@Override
	public String save() throws Exception {
		getRequest().setAttribute("backlocation",
				getRequest().getContextPath() + "/theme/theme!input.action");
		if ("".equals(modle.getBaseId()))
			modle.setBaseId(null);
		
		///////////////////////////////
		
			if(files != null) {//登陆界面背景图片	
				String Path = "oa" +File.separator + "image" + File.separator + "topPic" + File.separator +"test.jpg";
    			System.out.println(Path);//获取web项目的路径
    			String picPath = PathUtil.getRootPath()+ Path;
    			System.out.println(picPath);
    			File f = new File(picPath);
    			if(!f.exists()) 
    			f.createNewFile();
    			BufferedImage im = ImageIO.read(files);
    			ImageIO.write(im, "jpg", f);
    			//modle.setPicPath(Path.replace("\\", "/")); //数据库存储图片路径
			}	      
	        //主界面顶部图片
	        if(topPicGray != null){//灰色主题
	        	modle.setFastMenuFontSize(null);
	        	System.out.println(modle.getFastMenuFontSize());
    			String Path = "oa" +File.separator + "image" + File.separator + "topPic" + File.separator +"testGray.jpg";
    			System.out.println(Path);//获取web项目的路径
    			String picPath = PathUtil.getRootPath()+ Path;
    			System.out.println(picPath);
    			File f = new File(picPath);
    			if(!f.exists()) 
    			f.createNewFile();
    			BufferedImage im = ImageIO.read(topPicGray);
    			ImageIO.write(im, "jpg", f);
    			//modle.setLeftbg(Path.replace("\\", "/")); //数据库存储图片路径
	        }
	        if(topPicBlue != null){//蓝色主题
	        	modle.setFastMenuFontSize(null);
	        	System.out.println(modle.getFastMenuFontSize());
	        	String Path = "oa" +File.separator + "image" + File.separator + "topPic" + File.separator +"testBlue.jpg";
    			System.out.println(Path);//获取web项目的路径
    			String picPath = PathUtil.getRootPath()+ Path;
    			System.out.println(picPath);
    			File f = new File(picPath);
    			if(!f.exists()) 
    			f.createNewFile();
    			BufferedImage im = ImageIO.read(topPicBlue);
    			ImageIO.write(im, "jpg", f);
    			//modle.setLeftbg(Path.replace("\\", "/")); //数据库存储图片路径
	        }
	        if(topPicGreen != null){//绿色主题
	        	 modle.setFastMenuFontSize(null);
	        	System.out.println(modle.getFastMenuFontSize());
	        	String Path = "oa" +File.separator + "image" + File.separator + "topPic" + File.separator +"testGreen.jpg";
    			System.out.println(Path);//获取web项目的路径
    			String picPath = PathUtil.getRootPath()+ Path;
    			System.out.println(picPath);
    			File f = new File(picPath);
    			if(!f.exists()) 
    			f.createNewFile();
    			BufferedImage im = ImageIO.read(topPicGreen);
    			ImageIO.write(im, "jpg", f);
    			//modle.setLeftbg(Path.replace("\\", "/")); //数据库存储图片路径
	        }
			if(topPicRed != null){//红色主题
				modle.setFastMenuFontSize(null);
				System.out.println(modle.getFastMenuFontSize());
				String Path = "oa" +File.separator + "image" + File.separator + "topPic" + File.separator +"testRed.jpg";
				System.out.println(Path);//获取web项目的路径
				String picPath = PathUtil.getRootPath()+ Path;
				System.out.println(picPath);
				File f = new File(picPath);
				if(!f.exists()) 
				f.createNewFile();
				BufferedImage im = ImageIO.read(topPicRed);
				ImageIO.write(im, "jpg", f);
				//modle.setLeftbg(Path.replace("\\", "/")); //数据库存储图片路径
			 }
		manager.reset();   
		/////////////////////////////////////////
		String msg = manager.saveTheme(modle); // 保存界面设置内容
		addActionMessage(msg);
		StringBuffer returnhtml = new StringBuffer(
				"<script> var scriptroot = '").append(
				getRequest().getContextPath()).append("'</script>").append(
				"<SCRIPT src='").append(getRequest().getContextPath()).append(
				"/common/js/commontab/workservice.js'>").append("</SCRIPT>")
				.append("<SCRIPT src='").append(getRequest().getContextPath())
				.append("/common/js/commontab/service.js'>")
				.append("</SCRIPT>").append("<script>");
		if (msg != null && !"".equals(msg)) {
			returnhtml.append("alert('").append(getActionMessages().toString())
					.append("');");
		}
		returnhtml.append(" location='").append(getRequest().getContextPath())
				.append("/theme/theme!input.action").append("';</script>");
		return renderHtml(returnhtml.toString());
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4948589E036B
	 */
	@Override
	public String delete() throws Exception {
		return null;
	}

	/**
	 * @roseuid 4948589E038A
	 */
	@Override
	protected void prepareModel() {
		modle = manager.getTheme();
		if (modle == null)
			modle = new ToaSysmanageBase();

	}

	/**
	 * @author：pengxq
	 * @time：2009-2-3下午02:53:54
	 * @desc: 从资源文件中读取相应变量的值
	 * @param String
	 *            param 需获取值的变量
	 * @return String 变量的值
	 */
	public String getValueFromPro(String param) {
		Properties properties = new Properties();
		URL in = this.getClass().getClassLoader().getResource(
				"appconfig.properties");
		try {
			properties.load(new FileInputStream(in.getFile()));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return properties.getProperty(param);
	}

	public String uploadLicense() throws Exception {
		File license = file[0];
		String uploadPath = "";
		String os = MacUtil.getOsName();
		if (os.startsWith("Windows")) {
			uploadPath = "C:/StrongSoft/G3/.metadata/.keys/lincense/";
		} else {
			uploadPath = "/home/StrongSoft/G3/.metadata/.keys/lincense/";
		}
		if (license == null) {
			return this
					.renderHtml("<script>window.parent.myAlert('empty');</script>");
		} else {
			File path = new File(uploadPath);
			File serLicense = new File(uploadPath + "StrongSoft.license");
			try {
				if (path.isDirectory()) {

				} else {
					path.mkdirs();
					if (os.startsWith("Windows")) {
						Runtime.getRuntime().exec("attrib C:/StrongSoft +H");
					} else {

					}
				}
				serLicense.createNewFile();
				copyFile(license, serLicense);
				LicenseManager.refresh();
				return this
						.renderHtml("<script>window.parent.myAlert('success');</script>");
			} catch (Exception e) {
				System.out.println(e);
				return this
						.renderHtml("<script>window.parent.myAlert('uperror');</script>");
			}
		}
	}

	private void copyFile(File f1, File f2) throws Exception {
		int length = 2097152;
		FileInputStream in;
		try {
			in = new FileInputStream(f1);
			FileOutputStream out = new FileOutputStream(f2);
			byte[] buffer = new byte[length];
			while (true) {
				int ins = in.read(buffer);
				if (ins == -1) {
					in.close();
					out.flush();
					out.close();
					break;
				} else
					out.write(buffer, 0, ins);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * @author:luosy
	 * @description:  资源权限三级菜单在桌面页面显示（bgt）
	 * @date : 2011-12-3
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	public String gotoBgtMenupage() throws Exception {
		//获取当前用户
		String privCode = getRequest().getParameter("privCode");// 资源权限CODE
		
		return "bgtMenupage";
	}
	
	public String gotoBgtMenutree()throws Exception {
		
		getRequest().setAttribute( "backlocation", getRequest().getContextPath()
						+ "/theme/theme!RefreshPrivilist.action");
		prepareModel();
		priviparent = getRequest().getParameter("privCode");// 资源权限CODE
		
		List<ToaUumsBaseOperationPrivil> priviparentlist = optprivilmanager.getCurrentUserFirstMenuPrivil();
		ToaUumsBaseOperationPrivil privil = optprivilmanager
				.getPrivilInfoByPrivilSyscode(priviparent);
		privilList = optprivilmanager.getCurrentUserFirstMenuPrivil();
		if (!optprivilmanager.checkPrivilBySysCode(sysSysCode + "-" + priviparent)) {
			if (privilList != null && privilList.size() > 0) {
				privil = priviparentlist.get(0);
				priviparent = privil.getPrivilSyscode();
			} else
				priviparent = null;
		}

		String priviparentname = privil.getPrivilName();
		privilList = optprivilmanager.getCurrentUserSecondMenuPrivil(priviparent);
		
		getRequest().setAttribute("priviparentname", priviparentname);
		systemset = systemsetManager.getSystemset(); // 获取系统全局配置信息
		return "bgtmodule";
	}
	
	/**
	* @method checkLogin
	* @author 申仪玲
	* @created 2012-3-12 下午04:12:57
	* @description 检查用户是否已经登录
	* @return String 返回类型
	*/
	public String checkLogin()throws Exception{
		
		HttpSession session = getSession(); 
		String loginUser = (String) session.getAttribute("userName");
		if(loginUser != null && !"".equals(loginUser)){
			return renderHtml("true");
		}else{
			return renderHtml("false");
		}		
	}
	/**
	 * @method 主题重置
	 * @author 李浩
	 * @created 2013-8-12 19:09:38
	 * @description 
	 * @return String 返回类型
	 */
	public String reset()throws Exception{
		manager.reset();
		return "input";
	}
	
	
	
	public SystemsetManager getSystemsetManager() {
		return systemsetManager;
	}

	@Autowired
	public void setSystemsetManager(SystemsetManager systemsetManager) {
		this.systemsetManager = systemsetManager;
	}

	public String getSysSysCode() {
		return sysSysCode;
	}

	public void setSysSysCode(String sysSysCode) {
		this.sysSysCode = sysSysCode;
	}

	public List<ToaUumsBaseOperationPrivil> getPrivilList() {
		return privilList;
	}

	public void setPrivilList(List<ToaUumsBaseOperationPrivil> privilList) {
		this.privilList = privilList;
	}

	public String getPrivilRuleCode() {
		return privilRuleCode;
	}

	public void setPrivilRuleCode(String privilRuleCode) {
		this.privilRuleCode = privilRuleCode;
	}

	public ThemeAction() {
		sysSysCode = this.getValueFromPro("sysSysCode");
	}

	public Page getPage() {
		return page;
	}

	public void setBaseId(java.lang.String aBaseId) {
		baseId = aBaseId;
	}

	public ToaSysmanageBase getModle() {
		return modle;
	}

	@Autowired
	public void setManager(IThemeManager aManager) {
		manager = aManager;
	}

	public ToaSysmanageBase getModel() {
		return modle;
	}

	public List getList() {
		return list;
	}

	@Autowired
	public void setCurrentUser(IUserService managers) {
		this.userService = managers;
	}

	public String getPriviparent() {
		return priviparent;
	}

	public void setPriviparent(String priviparent) {
		this.priviparent = priviparent;
	}

	@Autowired
	public void setOptprivilmanager(BaseOptPrivilManager optprivilmanager) {
		this.optprivilmanager = optprivilmanager;
	}

	public String getBaseMenuIcon() {
		return baseMenuIcon;
	}

	public void setBaseMenuIcon(String baseMenuIcon) {
		this.baseMenuIcon = baseMenuIcon;
	}

	public ToaSysmanageBase getModle1() {
		return modle1;
	}

	public void setModle1(ToaSysmanageBase modle1) {
		this.modle1 = modle1;
	}

	public List<HashMap> getDefaultMenuList() {
		return defaultMenuList;
	}

	public void setDefaultMenuList(List<HashMap> defaultMenuList) {
		this.defaultMenuList = defaultMenuList;
	}

	public HashMap<String, String> getDefaultMenuMap() {
		return defaultMenuMap;
	}

	public void setDefaultMenuMap(HashMap<String, String> defaultMenuMap) {
		this.defaultMenuMap = defaultMenuMap;
	}

	@Autowired
	public void setPortalManager(DesktopPortalManager portalManager) {
		this.portalManager = portalManager;
	}

	public SysLinkManage getSysmanager() {
		return sysmanager;
	}

	@Autowired
	public void setSysmanager(SysLinkManage sysmanager) {
		this.sysmanager = sysmanager;
	}

	public List<ToaSystemmanageSystemLink> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<ToaSystemmanageSystemLink> linkList) {
		this.linkList = linkList;
	}

	public SynchroniManager getSynchManager() {
		return synchManager;
	}

	@Autowired
	public void setSynchManager(SynchroniManager synchManager) {
		this.synchManager = synchManager;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSysset() {
		return sysset;
	}

	public void setSysset(String sysset) {
		this.sysset = sysset;
	}

	public ToaSystemset getSystemset() {
		return systemset;
	}

	public void setSystemset(ToaSystemset systemset) {
		this.systemset = systemset;
	}

	public TUumsBaseOrg getOrg() {
		return org;
	}

	public String getJ_username() {
		return j_username;
	}

	public void setJ_username(String j_username) {
		this.j_username = j_username;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getPageModelId() {
		return pageModelId;
	}

	public void setPageModelId(String pageModelId) {
		this.pageModelId = pageModelId;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public Integer getRefeshTime() {
		return refeshTime;
	}

	public void setRefeshTime(Integer refeshTime) {
		this.refeshTime = refeshTime;
	}
	
	
	public void setTopPic(File topPic) {
		this.topPic = topPic;
	}
	

	public File getTopPic() {
		return topPic;
	}
	public File getFiles() {
		return files;
	}

	public void setFiles(File files) {
		this.files = files;
	}

	public File getTopPicRed() {
		return topPicRed;
	}

	public void setTopPicRed(File topPicRed) {
		this.topPicRed = topPicRed;
	}

	public File getTopPicBlue() {
		return topPicBlue;
	}

	public void setTopPicBlue(File topPicBlue) {
		this.topPicBlue = topPicBlue;
	}

	public File getTopPicGray() {
		return topPicGray;
	}

	public void setTopPicGray(File topPicGray) {
		this.topPicGray = topPicGray;
	}

	public File getTopPicGreen() {
		return topPicGreen;
	}

	public void setTopPicGreen(File topPicGreen) {
		this.topPicGreen = topPicGreen;
	}
	
}