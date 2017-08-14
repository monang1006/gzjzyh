package com.strongit.oa.desktop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.userdetails.UserDetails;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.web.ServletCacheAdministrator;
import com.strongit.oa.bo.ToaAffiche;
import com.strongit.oa.bo.ToaCalendar;
import com.strongit.oa.bo.ToaDesktopPortal;
import com.strongit.oa.bo.ToaDesktopSection;
import com.strongit.oa.bo.ToaDesktopWhole;
import com.strongit.oa.bo.ToaInfopublishArticle;
import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaMeetingroomApply;
import com.strongit.oa.bo.ToaMessage;
import com.strongit.oa.calendar.CalendarManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.Role;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.desktop.util.Mycomparator;
import com.strongit.oa.infopub.articles.ArticlesManager;
import com.strongit.oa.infopub.column.ColumnManager;
import com.strongit.oa.meetingroom.MeetingApplyManager;
import com.strongit.oa.message.MessageFolderManager;
import com.strongit.oa.notify.NotifyManager;
import com.strongit.oa.shortcutmenu.IFastMenuManager;
import com.strongit.oa.common.user.util.Const;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 桌面方案管理Actoin
 * @author yuhz
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/desktopWhole.action") })
public class DesktopWholeAction extends BaseActionSupport{

	private IUserService user;
	
	private DesktopWholeManager wholeManager;
	private DesktopPortalManager manager;
	
	private String wholeId;
	
	private String portalId;//门户ID
	private String infoType;//信息发布-政务要闻类型zwyw，其他为空
	private ArticlesManager articlesManager;//文件manager
	private static final String YES = "1";// 是
	/** 栏目ID* */
	private String columnId;
	/** 栏目Manager* */
	private ColumnManager columnManager;
	private NotifyManager noticeManager;//新闻公告manager
	private CalendarManager calendarManager;//日程安排manager
	private String ifleader;//是否领导
	private MeetingApplyManager meetManager;//会议室安排manager
	private MessageFolderManager msgManager;//消息提醒manager
	private ToaDesktopWhole whole;
	private String defaultType;						//默认类型
	private String operate;                         //set:门户设置,view:门户查看
	
	private String layouttype=null;					//在当前方案下的列数
	private String layout1=null;					//第一列宽度
	private String layout2=null;					//第二列宽度
	private String layout3=null;					//第三列宽度
	private String orderStr=null;					//当前桌面的顺序字符串
	
	private List col1=null;							//存储第一列的List
	private List col2=null;							//存储第二列内容的List
	private List col3=null;							//存储第三列内容的List
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IFastMenuManager fastMenuManager;	//快捷菜单接口
	
	private List userFMList=new ArrayList();	//用户的快捷菜单列表
	
	private String webType;                     //网页版桌面类型

	


	@Autowired
	public void setWholeManager(DesktopWholeManager wholeManager) {
		this.wholeManager = wholeManager;
	}
	@Autowired
	public void setManager(DesktopPortalManager manager) {
		this.manager = manager;
	}
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
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

	/**
	 * @author：yuhz
	 * @time：Feb 17, 20093:30:16 PM
	 * @desc: 对桌面的展现操作
	 * @return
	 */
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		String forword =SUCCESS;
		User userObj=user.getCurrentUser();
		if("0".equals(defaultType)){								 //非领导桌面方案						
			whole=wholeManager.getDefaultDesktop("0");
		}else if("1".equals(defaultType)){							 //领导桌面方案
			whole=wholeManager.getDefaultDesktop("1");
		}else if("2".equals(defaultType)){							 //门户桌面方案
			//	whole=wholeManager.getDesttopWholeById(this.manager.getPortal(portalId).getPortalDesktopId());
			if(wholeManager.getDefaultDesktopWholenoMorById(portalId,userObj.getUserId())==null){
			   whole=wholeManager.getDefaultDesktopWholeisMor(portalId);
			  if(manager.getPortal(portalId).getIsEdit().equals("1")){
			     String testOrder=whole.getDesktopLayout();
				if(testOrder==null||testOrder.equals("")){
					testOrder="";
				}
				ToaDesktopWhole newDesk = new ToaDesktopWhole();
				newDesk.setDesktopId(null);
				
				//newDesk.setDesktopIsdefault("2");
				newDesk.setIsMoren("0");
				newDesk.setDesktopIsdefault("0");
				newDesk.setUserId(userObj.getUserId());
				newDesk.setPortalId(whole.getPortalId());
				
				newDesk.setDesktopCenter(whole.getDesktopCenter());
				newDesk.setDesktopColumn(whole.getDesktopColumn());
				newDesk.setDesktopCtime(whole.getDesktopCtime());
				newDesk.setDesktopLayout(whole.getDesktopLayout());
				newDesk.setDesktopLeft(whole.getDesktopLeft());
				newDesk.setDesktopRight(whole.getDesktopRight());
				newDesk.setDesktopRole(whole.getDesktopRole());
				//saveObjN(newDesk);
				Set sets=whole.getToaDesktopSections();
				Set saveSet=new HashSet();
				Iterator it=sets.iterator();
				while(it.hasNext()){
					ToaDesktopSection section=(ToaDesktopSection)it.next();
					ToaDesktopSection deskSection=new ToaDesktopSection();
					BeanUtils.copyProperties(deskSection, section);
					deskSection.setSectionId(null);
					deskSection.setToaDesktopWhole(newDesk);
					saveSet.add(deskSection);
					
				}
				
				newDesk.setDesktopLayout(testOrder);
				newDesk.setToaDesktopSections(saveSet);
				wholeManager.saveObjN(newDesk);
				List newList=new ArrayList();
				List oldList=new ArrayList();
				newList.addAll(newDesk.getToaDesktopSections());
				oldList.addAll(whole.getToaDesktopSections());
				Mycomparator comparator=new Mycomparator();
				Collections.sort(newList,comparator);
				Collections.sort(oldList,comparator);
				for(int i=0;i<newList.size();i++){
					ToaDesktopSection  newSection=(ToaDesktopSection)newList.get(i);
					ToaDesktopSection  oldSection=(ToaDesktopSection)oldList.get(i);
					testOrder=testOrder.replace(oldSection.getSectionId(), newSection.getSectionId());
				}

				newDesk.setDesktopLayout(testOrder);
				wholeManager.saveObjN(newDesk);
				
			}
			}
			
			whole=wholeManager.getDefaultDesktopWholenoMor(portalId,userObj.getUserId());
			ToaDesktopPortal portal=this.manager.getPortal(portalId);
				if("0".equals(portal.getIsEdit())){
					forword = "view";
				}else{
					forword = "portal";
				}
		}else{
			whole=wholeManager.getDesktopWhole(userObj.getUserId(),userObj.getRest1());
		}
		wholeId = whole.getDesktopId();								 //获得当前的桌面方案
		layouttype = String.valueOf(whole.getDesktopColumn());   	 //当前桌面方案存在多少列
		
		layout1 = String.valueOf(whole.getDesktopLeft());		
		layout2 = String.valueOf(whole.getDesktopCenter());
		layout3 = String.valueOf(whole.getDesktopRight());
		
		orderStr = whole.getDesktopLayout();
		
		if(orderStr==null){
			col1 = null;
			col2 = null;
			col3 = null;
			
		}else{
			String[] temp=wholeManager.createOrder(orderStr);
			if(temp!=null){
				col1 = wholeManager.getColumn(temp[0]);
				if(temp.length>1&&Integer.parseInt(layouttype)>=2){
					col2 = wholeManager.getColumn(temp[1]);
				}else{
					col2 = null;
				}
				if(temp.length>2&&Integer.parseInt(layouttype)>=3){
					col3 = wholeManager.getColumn(temp[2]);
				}else{
					col3 = null;
				}

			}
		}
//		userObj.getUserId();
		
		return forword;
	}
	
	public String edited() throws Exception {
		// TODO Auto-generated method stub
		String forword ="edited";
		User userObj=user.getCurrentUser();
		if("0".equals(defaultType)){								 //非领导桌面方案						
			whole=wholeManager.getDefaultDesktop("0");
		}else if("1".equals(defaultType)){							 //领导桌面方案
			whole=wholeManager.getDefaultDesktop("1");
		}else if("2".equals(defaultType)){							 //门户桌面方案
			
			  whole=wholeManager.getDefaultDesktopWholeisMor(portalId);
			
				if("view".equals(operate)){
					forword = "view";
				}else{
					forword = "edited";
				}
		}else{
			//User userObj=user.getCurrentUser();
			whole=wholeManager.getDesktopWhole(userObj.getUserId(),userObj.getRest1());
		}
		wholeId = whole.getDesktopId();								 //获得当前的桌面方案
		layouttype = String.valueOf(whole.getDesktopColumn());   	 //当前桌面方案存在多少列
		
		layout1 = String.valueOf(whole.getDesktopLeft());		
		layout2 = String.valueOf(whole.getDesktopCenter());
		layout3 = String.valueOf(whole.getDesktopRight());
		
		orderStr = whole.getDesktopLayout();
		
		if(orderStr==null){
			col1 = null;
			col2 = null;
			col3 = null;
			
		}else{
			String[] temp=wholeManager.createOrder(orderStr);
			if(temp!=null){
				col1 = wholeManager.getColumn(temp[0]);
				if(temp.length>1&&Integer.parseInt(layouttype)>=2){
					col2 = wholeManager.getColumn(temp[1]);
				}else{
					col2 = null;
				}
				if(temp.length>2&&Integer.parseInt(layouttype)>=3){
					col3 = wholeManager.getColumn(temp[2]);
				}else{
					col3 = null;
				}

			}
		}
//		userObj.getUserId();
		
		return forword;
	}
	
	/**
	 * @author：yuhz
	 * @time：Feb 17, 20093:30:16 PM
	 * @desc: 对当前桌面方案的查询操作
	 * @return
	 */
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		if(wholeId!=null)
			whole = wholeManager.getDesttopWholeById(wholeId);
		else
			whole = new ToaDesktopWhole();
	}

	/**
	 * @author：yuhz
	 * @time：Feb 17, 20093:30:16 PM
	 * @desc: 对当前桌面方案对应参数的保存
	 * @return
	 */
	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		prepareModel();
		whole.setDesktopLayout(orderStr);
		whole.setDesktopColumn(Integer.parseInt(layouttype));
		whole.setDesktopLeft(Integer.parseInt(layout1));
		whole.setDesktopCenter(Integer.parseInt(layout2));
		whole.setDesktopRight(Integer.parseInt(layout3));
		wholeManager.saveObj(whole);
		flushMemory(this.getRequest(),whole.getDesktopId());
		return null;
	}
	
	public String deleteWhole() throws Exception {
		ToaDesktopWhole temp=wholeManager.getDesttopWholeById(wholeId);
		if(temp.getDesktopIsdefault().equals("1")){
			return renderText("default");
		}else{
			if(wholeManager.deleteDesktop(wholeId)){
				if(portalId!=null){	//如果是门户管理模块的恢复默认设置	
					ToaDesktopPortal portal=manager.getPortal(portalId);//根据门户id查找门户对象
					if(portal!=null){
						ToaDesktopWhole whole = wholeManager.getDesktopWholeByPortal(null);
						whole.setDesktopIsdefault("2");//类型
						portal.setPortalDesktopId(whole.getDesktopId());
						manager.addPortal(portal);
					}	
				}
				return renderText("yes");
			}else{
				return renderText("no");
			}
		}
		
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @author:yuhz
	 * @time: 2009-9-10
	 * @desc: 刷新oscache缓存
	 * @return:
	 * @param request
	 */
	private void flushMemory(HttpServletRequest request,String key){
		ServletCacheAdministrator admin=null;
		admin=ServletCacheAdministrator.getInstance(request.getSession().getServletContext());
		if(admin!=null){
//			System.out.println(PageContext.APPLICATION_SCOPE);
			
			String aukey = admin.generateEntryKey(key, request, PageContext.APPLICATION_SCOPE,null);
			Cache cache = admin.getCache(request, PageContext.APPLICATION_SCOPE);
			cache.flushEntry(aukey);
//			admin.flushAll();
		}
	}
	

	/*
	 * 
	 * Description: param: @author 彭小青 @date Jan 26, 2011 3:08:49 PM
	 */
	public String getDesktopPortalList() throws Exception {
		User user = userService.getCurrentUser();
		this.renderText(manager.getDesktopPortalList(user.getUserId()));
		return null;
	}
	
	
	/**
	 * @author:luosy
	 * @description:  桌面办公门户（bgt）
	 * @date : 2011-12-3
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws Exception
	 */
	public String gotoBgtDesktop() throws Exception {
		//获取当前用户
		User user = userService.getCurrentUser();
		HttpServletRequest request = getRequest(); // 获取session
		request.setAttribute("user", user);
		Organization org = userService.getDepartmentByOrgId(user.getOrgId());
		String isOrg = org.getIsOrg();
		if(isOrg == null || isOrg.equals("")){
			isOrg = "0";
		}
		request.setAttribute("user", user);
		request.setAttribute("isOrg", isOrg);
		request.setAttribute("deptId", org.getOrgId());
		
//		user.getUserName()
		
		if("3".equals(user.getRest1())){
			return "bgtDesktopForLeader";
		}else if("1".equals(user.getRest1())){
			return "bgtDesktopForLeader2";
		}else{
			return "bgtDesktop";
		}
	}
	
	/**
	* @description 网页版界面桌面初始化
	* @method gotoWebDesktop
	* @author 申仪玲(shenyl)
	* @created 2012-5-10 下午08:29:59
	* @return
	* @throws Exception
	* @return String
	* @throws Exception
	*/
	public String gotoWebDesktop()throws Exception {
		//获取当前用户
		User user = userService.getCurrentUser();
		HttpServletRequest request = getRequest(); // 获取session
		request.setAttribute("user", user);
		Organization org = userService.getDepartmentByOrgId(user.getOrgId());
		String isOrg = org.getIsOrg();
		if(isOrg == null || isOrg.equals("")){
			isOrg = "0";
		}
		 UserDetails userDetail = this.getUserDetails();
		 userFMList=fastMenuManager.getFastMenu(userDetail);	//当前用户快捷菜单列表
		
		request.setAttribute("user", user);
		request.setAttribute("isOrg", isOrg);
		request.setAttribute("deptId", org.getOrgId());
		
		List<Role> roleList = userService.getUserRoleInfosByUserId(user.getUserId(), Const.IS_YES);
		if(!roleList.isEmpty()){//领导网页版界面定制
			for (Iterator<Role> it = roleList.iterator(); it.hasNext();) {
				Role role = it.next();
				if(role.getRoleName().equals("厅长") || role.getRoleName().equals("副厅长")){
					return "webFtabforLD";
				}

			}
		}
		
		return webType;
	}
	
	/**
	 * 
	 * @Title:政务要闻
	 * @Description:获取信息发布下，按政务要闻作为类型查询，将结果组装成html格式返回
	 * @Param 
	 * @Author:hecj
	 * @Return 
	 * @Throws
	 */
	public String govNews(){
		/** page对象* */
		Page<ToaInfopublishColumnArticl> page = new Page<ToaInfopublishColumnArticl>(6, true);
		page = articlesManager
				.getColumnArticleList(page,columnId);
		String showNum = getRequest().getParameter("showNum");// 显示条数
		String subLength = getRequest().getParameter("subLength");// 主题长度
		String showco = "";
		
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}
		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		
		List<ToaInfopublishColumnArticl> list=page.getResult();
		if (list!=null&&list.size() > 0)// 判断是否有值
		{
			showco = list.get(0).getToaInfopublishColumn().getClumnName();
		}
		
		
		StringBuffer innerHtml = new StringBuffer();
		if (list!=null&&list.size() > 0) {
			String src = "";
			String links = "";
			String texts = "";
			for (int i = 0; i<num&&i < list.size(); i++) {
				ToaInfopublishColumnArticl columnBo = list.get(i);
				String annexContent = columnBo.getToaInfopublishArticle()
						.getArticlesPic();
				int j = 0;
				if (!"".equals(annexContent) && annexContent != null) {
					src += "|" + getRequest().getContextPath()
							+ annexContent;
					links += "|"
							+ getRequest().getContextPath()
							+ "/infopub/articles/articles!showColumnArticl.action?columnArticleId="
							+ columnBo.getColumnArticleId();
					String testTitle = "";
					if (columnBo.getToaInfopublishArticle()
							.getArticlesTitle().length() > 25)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
						testTitle = columnBo.getToaInfopublishArticle()
								.getArticlesTitle().substring(0, 22)
								+ "...";
					else {
						testTitle = columnBo.getToaInfopublishArticle()
								.getArticlesTitle();
					}
					texts += "|" + testTitle;
					j++;
				}
				if (j == 4) {
					break;
				}
			}
			if (!"".equals(src)) {
				src = src.substring(1);
			}
			if (!"".equals(links)) {
				links = links.substring(1);
			}
			if (!"".equals(texts)) {
				texts = texts.substring(1);
			}
			innerHtml
				.append(
					"<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ><tr><td  width=\"200\">")
				.append(
					"<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\" width=\"200\" height=\"175\"> ")
				.append(
					"<param name=\"allowScriptAccess\" value=\"sameDomain\">")
			    .append("<param name=\"movie\" value=\"")
			    .append(getRequest().getContextPath())
			    .append(
					"/common/flash/focus.swf\"><param name=\"quality\" value=\"high\"> ")
				.append("<param name=\"bgcolor\" value=\"#F0F0F0\"> ")
				.append(
					"<param name=\"menu\" value=\"false\"><param name=wmode value=\"opaque\"> ")
				.append("<param name=\"FlashVars\" value=\"pics=")
				.append(src)
				.append("&links=")
				.append(links)
				.append("&texts=")
				.append(texts)
				.append(
					"&borderwidth=200&borderheight=160&textheight=15\"> ")
				.append(
					"<embed src=\"pixviewer.swf\" wmode=\"opaque\" FlashVars=\"pics=")
				.append(src)
				.append("&links=")
				.append(links)
				.append("&texts=")
				.append(texts)
				.append(
					"&borderwidth=200&borderheight=160&textheight=15\" menu=\"false\" bgcolor=\"#F0F0F0\" quality=\"high\" width=\"200\" height=\"160\" allowScriptAccess=\"sameDomain\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" /> ")
				.append("</object></td><td valign=\"top\">");
			for (int i = 0; i < num && i < list.size(); i++) {// 获取在条数范围内的列表
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				ToaInfopublishColumnArticl columnBo = list.get(i);
				innerHtml
					.append(
						"	<div class=\"linkdiv\" valign=top style=\"\">")
					.append("	<IMG SRC=\"")
					.append(getRequest().getContextPath())
					.append(
						"/oa/image/desktop//littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				if (YES.equals(columnBo.getColumnArticleIsstandtop())) {// 是否为固顶
					innerHtml.append("<font color=\"#0000FF\">[顶]</font>");
				}
				innerHtml.append("	<span title=\"").append(columnBo.getToaInfopublishArticle().getArticlesTitle()).append("\">");
				String title = columnBo.getToaInfopublishArticle().getArticlesTitle();
				if (title.length() > length)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0, length) + "...";
					innerHtml
					.append(
						"	<a href=\"javascript:window.parent.navigate('")
					.append(getRequest().getContextPath())
					.append(
						"/infopub/articles/articles!showColumnArticl.action?columnArticleId=")
					.append(columnBo.getColumnArticleId())
					.append("','").append(
						columnBo.getToaInfopublishArticle()
								.getArticlesTitle()).append(
						"')\"> ");
					String colour = columnBo.getToaInfopublishArticle().getArticlesTitlecolor();
					String font = columnBo.getToaInfopublishArticle().getArticlesTitlefont();
				if (!"0".equals(colour) && !"".equals(colour)) {// 设置了颜色
					if ("1".equals(font)) {// 粗体
							innerHtml.append("<B><font color=\"")
							.append(colour).append("\">").append(title)
							.append("</font></B></a></span>");
					} else if ("2".equals(font)) {// 斜体
							innerHtml.append("<I><font color=\"")
							.append(colour).append("\">").append(title)
							.append("</font></I></a></span>");
					} else if ("3".equals(font)) {// 粗斜体
							innerHtml.append("<B><I><font color=\"").append(
									colour).append("\">").append(title).append(
									"</font></I></B></a></span>");
					} else {
							innerHtml.append("<font color=\"").append(colour)
							.append("\">").append(title).append(
								"</font></a></span>");
							}
				} else {
						if ("1".equals(font)) {// 粗体
							innerHtml.append("<B>").append(title).append(
							"</B></a></span>");
						} else if ("2".equals(font)) {// 斜体
							innerHtml.append("<I>").append(title).append(
							"</I></a></span>");
						} else if ("3".equals(font)) {// 粗斜体
							innerHtml.append("<B><I>").append(title).append(
							"</I></B></a></span>");
						} else {
							innerHtml.append(title).append("</a></span>");
						}

				}
				if (YES.equals(columnBo.getToaInfopublishArticle().getArticlesIshot())) {// 是否为热点
						innerHtml
							.append("<IMG SRC=\"")
							.append(getRequest().getContextPath())
							.append("/oa/image/desktop/hot.gif\" WIDTH=\"23\" HEIGHT=\"7\" BORDER=\"0\" ALT=\"\">");
				}//IF
			}//IF
		}//FOR
		return renderHtml(innerHtml.toString());
		
	}
	
	/**
	 * 
	 * @Title:领导活动,政府文件，信息发布
	 * @Description:获取信息发布下，按领导活动作为类型查询，将结果组装成html格式返回
	 * @Param 
	 * @Author:hecj
	 * @Return 
	 * @Throws
	 */
	public String leaderActive(){
		ToaInfopublishColumn column = null;//信息发布
		column = columnManager.getColumn(columnId);
		/** page对象* */
		Page<ToaInfopublishColumnArticl> page = new Page<ToaInfopublishColumnArticl>(6, true);
		if(column!=null&&column.getProcessName()!=null&&!"".equals(column.getProcessName()))//判断栏目是否走了工作流
		{
			page=articlesManager.getColumnArticlPagesByProcess(page, columnId);
		}else{
			page=articlesManager.getColumnArticlPagesByState(page, columnId);
		}
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		if(page.getResult()!=null){
			innerHtml.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for(Iterator iterator = page.getResult().iterator(); iterator.hasNext();){
				ToaInfopublishColumnArticl col = (ToaInfopublishColumnArticl)iterator.next();
				ToaInfopublishArticle articl =(ToaInfopublishArticle)col.getToaInfopublishArticle();
				
				innerHtml.append("<div class=\"linkdiv\" title=\"\">");
				//图标
				innerHtml.append("<img src=\"").append(rootPath).
				append("/oa/image/desktop/littlegif/news_bullet.gif").append("\"/> ");
				
				
				int length=13;//设定显示的标题长度
				String title =articl.getArticlesTitle() ==null?"":articl.getArticlesTitle().toString();
				
				if(title.length()>length && length > 0) {
					title = title.substring(0,length)+"...";
				}
				if (!"".equals(title)){
					/*SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
					String dt=df.format(articl.getArticlesStandtopstart());
					title=title+"  "+ dt;*/
					if("zwyw".equals(infoType)){
						title=title+"  "+"日期";
					}
				}
				StringBuilder clickTitle = new StringBuilder();
				/*clickTitle.append("var Width=screen.availWidth-300;var Height=screen.availHeight-30;");
				clickTitle.append("var a = window.open('").append(getRequest().getContextPath())
					.append("/infopub/articles/articles!show.action?columnArticleId=").append(col.getColumnArticleId().toString())
					.append("','articl','height='+Height+', width='+Width+',toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');");*/
				clickTitle.append("var a = navigate('").append(getRequest().getContextPath())
					.append("/infopub/articles/articles!show.action?columnArticleId=").append(col.getColumnArticleId().toString())
					.append("','").append(title).append("');");
				innerHtml.append("<span><a href=\"#\" onclick=\"").append(clickTitle.toString()).append("\">").append(title).append("</a></span>");
				innerHtml.append("</div>");
			}
		}
		return renderHtml(innerHtml.toString());
	}
	
	/**
	 * 
	 * @Title:通知公告
	 * @Description:获取新闻公告栏数据
	 * @Param 
	 * @Author:hecj
	 * @Return 
	 * @Throws
	 */
	public String notice(){
		String showNum = getRequest().getParameter("showNum");// 显示条数
		String subLength = getRequest().getParameter("subLength");// 主题长度
		
		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}

		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}
		
		Page<ToaAffiche> page = new Page<ToaAffiche>(num, true);
		ToaAffiche model = new ToaAffiche();
		page = noticeManager.getList(page, model);
		
		
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		if(page.getResult()!=null){
			innerHtml.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for(Iterator iterator = page.getResult().iterator(); iterator.hasNext();){
				ToaAffiche aff = (ToaAffiche)iterator.next();
				
				innerHtml.append("<div class=\"linkdiv\" title=\"\">");
				//图标
				innerHtml.append("<img src=\"").append(rootPath).
				append("/oa/image/desktop/littlegif/news_bullet.gif").append("\"/> ");
				
				
				//设定显示的标题长度
				String title =aff.getAfficheTitle() ==null?"":aff.getAfficheTitle().toString();
				if(title.length()>length && length > 0) {
					title = title.substring(0,length)+"...";
				}
				
				
				StringBuilder clickTitle = new StringBuilder();
				/*clickTitle.append("var Width=screen.availWidth-300;var Height=screen.availHeight-30;");
				clickTitle.append("var a = window.open('").append(getRequest().getContextPath())
					.append("/notify!view.action?afficheId=").append(aff.getAfficheId())
					.append("','articl','height='+Height+', width='+Width+',toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');");*/
				
				clickTitle.append("var a = navigate('").append(getRequest().getContextPath())
					.append("/notify/notify!view.action?afficheId=").append(aff.getAfficheId())
					.append("','").append(title).append("');");
				innerHtml.append("<span><a href=\"#\" onclick=\"").append(clickTitle.toString()).append("\">").append(title).append("</a></span>");
				String afficheGov = aff.getAfficheGov() == null?"":aff.getAfficheGov().toString(); //发布部门
				innerHtml.append("	<span class =\"linkgray\">").append(afficheGov).append("</span>");
				String afficheTime = aff.getAfficheTime().toString();
				afficheTime = afficheTime == null?"":afficheTime.substring(0,afficheTime.indexOf(" ")).toString(); //发布时间
				innerHtml.append("	<span class =\"linkgray\">").append(afficheTime).append("</span>");
				innerHtml.append("</div>");
			}
		}
		return renderHtml(innerHtml.toString());
	}
	
	/**
	 * 
	 * @Title:会议室安排
	 * @Description:获取会议室列表
	 * @Param 
	 * @Author:hecj
	 * @Return 
	 * @Throws
	 */
	public String meetingApply(){
		Page<ToaMeetingroomApply> page = new Page<ToaMeetingroomApply>(6, true);
		ToaMeetingroomApply model = new ToaMeetingroomApply();
		page = meetManager.getApplyByPage(page, model, "");
		
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		if(page.getResult()!=null){
			innerHtml.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for(Iterator iterator = page.getResult().iterator(); iterator.hasNext();){
				ToaMeetingroomApply meeting = (ToaMeetingroomApply)iterator.next();
				
				innerHtml.append("<div class=\"linkdiv\" title=\"\">");
				//图标
				innerHtml.append("<img src=\"").append(rootPath).
				append("/oa/image/desktop/littlegif/news_bullet.gif").append("\"/> ");
				
				
				int length=13;//设定显示的标题长度
				String title =meeting.getMaMeetingdec() ==null?"":meeting.getMaMeetingdec().toString();
				if(title.length()>length && length > 0) {
					title = title.substring(0,length)+"...";
				}
				
				
				StringBuilder clickTitle = new StringBuilder();
				clickTitle.append("var Width=screen.availWidth;var Height=screen.availHeight;");
				clickTitle.append("var a = window.open('").append(getRequest().getContextPath())
					.append("/meetingroom/meetingApply!view.action?maId=").append(meeting.getMaId())
					.append("','articl','height='+Height+', width='+Width+',toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');");
				
				/*clickTitle.append("var a = navigate('").append(getRequest().getContextPath())
					.append("/meetingroom/meetingApply!view.action?maId=").append(meeting.getMaId())
					.append("','").append(title).append("');");*/
				innerHtml.append("<span><a href=\"#\" onclick=\"").append(clickTitle.toString()).append("\">").append(title).append("</a></span>");
				innerHtml.append("</div>");
			}
		}
		return renderHtml(innerHtml.toString());
	}
	
	/**
	 * 
	 * @Title:日程安排
	 * @Description:获取日程安排列表
	 * @Param 
	 * @Author:hecj
	 * @Return 
	 * @Throws
	 */
	public String calendar(){
		Page<ToaCalendar> page = new Page<ToaCalendar>(6, true);
		ToaCalendar model = new ToaCalendar();
		page = calendarManager.searchByCal(page,model,"edit");
		
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		if(page.getResult()!=null){
			innerHtml.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for(Iterator iterator = page.getResult().iterator(); iterator.hasNext();){
				Object[] obj = (Object[])iterator.next();
				
				innerHtml.append("<div class=\"linkdiv\" title=\"\">");
				//图标
				innerHtml.append("<img src=\"").append(rootPath).
				append("/oa/image/desktop/littlegif/news_bullet.gif").append("\"/> ");
				
				
				int length=13;//设定显示的标题长度
				String title =obj[1] ==null?"":obj[1].toString();
				if(title.length()>length && length > 0) {
					title = title.substring(0,length)+"...";
				}
				String showtime=obj[3].toString();
				if(!"".equals(showtime)){
					showtime=showtime.substring(0,showtime.indexOf(" "));
				}
				if(!"".equals(title)){
					title=title+"  "+showtime;
				}
				
				StringBuilder clickTitle = new StringBuilder();
				clickTitle.append("var Width=screen.availWidth;var Height=screen.availHeight;");
				clickTitle.append("var a = window.open('").append(getRequest().getContextPath())
					.append("/calendar/calendar!input.action?ifleader=").append(obj[9].toString()).append("&calendarId=").append(obj[0].toString())
					.append("','articl','height='+Height+', width='+Width+',toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');");
				
				
				
				/*clickTitle.append("var a = navigate('").append(getRequest().getContextPath())
					.append("/calendar/calendar!input.action?ifleader=").append(obj[9].toString()).append("&calendarId=").append(obj[0].toString())
					.append("','").append(title).append("');");*/
				innerHtml.append("<span><a href=\"#\" onclick=\"").append(clickTitle.toString()).append("\">").append(title).append("</a></span>");
				innerHtml.append("</div>");
			}
		}
		return renderHtml(innerHtml.toString());
	}
	/**
	 * 
	 * @Title:消息提醒
	 * @Description:获取消息提醒总数
	 * @Param 
	 * @Author:hecj
	 * @Return 
	 * @Throws
	 */
	public String messageRemind(){
		Page<ToaMessage> page = new Page<ToaMessage>(6, true);
		String count = "";
		count=msgManager.getUnreadCount("recv","");
		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		if(!"".equals(count)){
			innerHtml.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			
			innerHtml.append("<div class=\"linkdiv\" title=\"\">");
			//图标
			innerHtml.append("<img src=\"").append(rootPath).
			append("/oa/image/desktop/littlegif/news_bullet.gif").append("\"/> ");

			StringBuilder clickTitle = new StringBuilder();
			/*clickTitle.append("var Width=screen.availWidth-300;var Height=screen.availHeight-30;");
			clickTitle.append("var a = window.open('").append(getRequest().getContextPath())
				.append("/notify!view.action?afficheId=").append(aff.getAfficheId())
				.append("','articl','height='+Height+', width='+Width+',toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');");*/
			
			clickTitle.append("var a = navigate('").append(getRequest().getContextPath())
				.append("/message/message.action?folderId=recv")
				.append("','").append("消息提醒").append("');");
			innerHtml.append("<span>最近两周您有<a href=\"#\" onclick=\"").append(clickTitle.toString()).append("\">").append(count).append("</a>条未阅读通知,请及时查看</span>");
			innerHtml.append("</div>");
		}
		return renderHtml(innerHtml.toString());
	}
	
	public ToaDesktopWhole getWhole() {
		return whole;
	}

	public String getLayout1() {
		return layout1;
	}

	public void setLayout1(String layout1) {
		this.layout1 = layout1;
	}

	public String getLayout2() {
		return layout2;
	}

	public void setLayout2(String layout2) {
		this.layout2 = layout2;
	}

	public String getLayout3() {
		return layout3;
	}

	public void setLayout3(String layout3) {
		this.layout3 = layout3;
	}

	public String getLayouttype() {
		return layouttype;
	}

	public void setLayouttype(String layouttype) {
		this.layouttype = layouttype;
	}

	public String getOrderStr() {
		return orderStr;
	}

	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}

	public List getCol1() {
		return col1;
	}

	public void setCol1(List col1) {
		this.col1 = col1;
	}

	public List getCol2() {
		return col2;
	}

	public void setCol2(List col2) {
		this.col2 = col2;
	}

	public List getCol3() {
		return col3;
	}

	public void setCol3(List col3) {
		this.col3 = col3;
	}

	public String getWholeId() {
		return wholeId;
	}

	public void setWholeId(String wholeId) {
		this.wholeId = wholeId;
	}

	public String getDefaultType() {
		return defaultType;
	}

	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}

	public String getPortalId() {
		return portalId;
	}

	public void setPortalId(String portalId) {
		this.portalId = portalId;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	
	@Autowired
	public void setArticlesManager(ArticlesManager articlesManager) {
		this.articlesManager = articlesManager;
	}
	@Autowired
	public void setColumnManager(ColumnManager columnManager) {
		this.columnManager = columnManager;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	@Autowired
	public void setNoticeManager(NotifyManager noticeManager) {
		this.noticeManager = noticeManager;
	}
	@Autowired
	public void setMeetManager(MeetingApplyManager meetManager) {
		this.meetManager = meetManager;
	}
	@Autowired
	public void setCalendarManager(CalendarManager calendarManager) {
		this.calendarManager = calendarManager;
	}
	public String getIfleader() {
		return ifleader;
	}
	public void setIfleader(String ifleader) {
		this.ifleader = ifleader;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	@Autowired
	public void setMsgManager(MessageFolderManager msgManager) {
		this.msgManager = msgManager;
	}
	
	public List getUserFMList() {
		return userFMList;
	}
	public void setUserFMList(List userFMList) {
		this.userFMList = userFMList;
	}
	public String getWebType() {
		return webType;
	}
	public void setWebType(String webType) {
		this.webType = webType;
	}
}
