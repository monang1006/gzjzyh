package com.strongit.oa.wap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.ui.rememberme.AbstractRememberMeServices;
import org.springframework.util.StringUtils;

import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaMessage;
import com.strongit.oa.bo.ToaMessageFolder;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.infopub.articles.ArticlesManager;
import com.strongit.oa.message.MessageFolderManager;
import com.strongit.oa.message.MessageManager;
import com.strongit.oa.wap.service.WorkflowForWapService;
import com.strongit.oa.work.WorkManager;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @company  Strongit wap开发
 * @author 	 彭小青
 * @date 	 Jan 18, 2010 11:55:37 AM
 * @version  2.0.4
 * @comment  登录验证
 */
@ParentPackage("default")
public class LoginAction extends BaseActionSupport {
	
	private String j_username;
	
	private String j_password;
	
	private String msg;
	
	private List<String[]> list=new ArrayList<String[]>();
	
	@Autowired WorkManager manager;
	
	@Autowired
	private WorkflowForWapService workflowService;
	
	@Autowired ArticlesManager articleManager;
	
	@Autowired MessageManager messageManager;
	
	private MessageFolderManager msgForlderManager;
	
	@Autowired
	private IUserService userService;//注入统一用户服务
	
	protected static final String defaultIsBackSpace = "4";
	
	protected String isBackSpace = defaultIsBackSpace;//是否是回退的任务“0”：非回退任务；“1”：回退任务；“4”：全部
	
	private String aliasName;
	
	/*
	 * 
	 * Description:验证登录
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 5, 2010 3:21:25 PM
	 */
	public String validateLogin()throws Exception{
		String forward="error";
		try{
			if(j_username==null||"".equals(j_username)){
				msg="请输入用户名！";
			}else if(j_password==null||"".equals(j_password)){
				msg="请输入密码！";
			}else{
				//getRequest().getRequestDispatcher("j_spring_security_check").forward(getRequest(), getResponse());
				//增加Cooike记录用户名功能
				Cookie cookie = new Cookie(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, j_username);
				String maxAge = getText(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
				if(maxAge != null && !AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY.equals(maxAge)){
					Integer IntMaxAge = Integer.parseInt(maxAge);
					cookie.setMaxAge(IntMaxAge);
					logger.info("有效期为:" + IntMaxAge + "秒.");
				}else{
					cookie.setMaxAge(7 * 24 * 60 * 60);
				}
				cookie.setPath(StringUtils.hasLength(getRequest().getContextPath()) ? getRequest().getContextPath() : "/");
				getResponse().addCookie(cookie);
				logger.info("Added remember-me cookie for user '" + j_username + "', expiry: '" + new Date(7 * 24 * 60 * 60) + "'");
				getResponse().sendRedirect(getRequest().getContextPath()+"/j_spring_security_check?j_username="+j_username+"&j_password="+j_password);
				forward=null;
			}
		}catch(Exception e){
			e.printStackTrace();
			Cookie cookie = new Cookie(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, null);
			cookie.setMaxAge(0);
			cookie.setPath(StringUtils.hasLength(getRequest().getContextPath()) ? getRequest().getContextPath() : "/");
			getResponse().addCookie(cookie);
		}
		return forward;
	}
	
	/**
	 * 
	 * @Description:WapOA主界面,获取每个功能项的名称，图片，链接
	 * @Param 
	 * @Author:hecj
	 * @Return List<String[]>
	 * @Date 2012-2-17 下午02:50:46 
	 * @Throws
	 */
	public String getMainInfo() throws Exception{
		HttpServletRequest request=this.getRequest();
		String[] gn=new String[3];	//中文描述、链接、图标
		User user = userService.getCurrentUser();
		aliasName="";
		if(user!=null){
			aliasName=user.getUserName();
		}
		
		Page<ToaInfopublishColumnArticl> articlpage=new Page<ToaInfopublishColumnArticl>(10,true);
		articlpage=articleManager.getColumnArticleList(articlpage,null);
		int count=articlpage==null?0:articlpage.getTotalCount();
		gn=new String[3];
		gn[0]="通知公告["+count+"]";
		gn[1]=request.getContextPath()+"/wap/articles!tree.action?treeType=wap";
		gn[2]="news.png";
		list.add(gn);
		
		gn=new String[3];
		gn[0]="通讯录";
		//gn[1]=request.getContextPath()+"/fileNameRedirectAction.action?toPage=/wap/addressOrg-tree.jsp?isFirst=yes";
		gn[1]=request.getContextPath()+"/wap/addressOrg!getOrguserlist.action";
		gn[2]="contact.png";
		list.add(gn);
		
		Page<ToaMessage> pagecount = new Page<ToaMessage>();
		pagecount = messageManager.getMsgByFolderName(pagecount, ToaMessageFolder.FOLDER_RECV,
				null, null,null, null);
		gn=new String[3];
		
		String mailCount= "0";
		mailCount=msgForlderManager.getUnreadCount("recv", user.getUserId());
		gn[0]="内部邮件["+mailCount+"]";
		gn[1]=request.getContextPath()+"/wap/message!waprecvlist.action";
		gn[2]="email.png";
		list.add(gn);
		
		int workcount = workflowService.getTodoCount(user.getUserId(), "2,3");//发文
		
		int todocount = workflowService.getTodoCount(user.getUserId(), null);//发文
		int othercount=0;
		othercount=todocount-workcount;
		gn=new String[3];
		gn[0]="待办事宜["+othercount+"]";
		gn[1]=request.getContextPath()+"/wap/work.action?listMode=10&worktype=0";
		gn[2]="work.png";
		list.add(gn);
		
		gn=new String[3];
		gn[0]="公文管理["+workcount+"]";
		gn[1]=request.getContextPath()+"/wap/work.action?listMode=10&worktype=1";
		gn[2]="workmana.png";
		list.add(gn);
		
		/*gn=new String[3];
		gn[0]="智能更新";
		gn[1]="#";
		gn[2]="icon.png";
		list.add(gn);*/

		return "mainPage";
	}
	
	@Override
	public String delete() throws Exception {
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public String list() throws Exception {
		return null;
	}

	@Override
	protected void prepareModel() throws Exception {
	}

	@Override
	public String save() throws Exception {
		return null;
	}

	public Object getModel() {
		return null;
	}

	public String getJ_password() {
		return j_password;
	}

	public void setJ_password(String j_password) {
		this.j_password = j_password;
	}

	public String getJ_username() {
		return j_username;
	}

	public void setJ_username(String j_username) {
		this.j_username = j_username;
	}

	public String getMsg() {
		return msg;
	}

	public List<String[]> getList() {
		return list;
	}

	public String getAliasName() {
		return aliasName;
	}
	@Autowired
	public void setMsgForlderManager(MessageFolderManager msgForlderManager) {
		this.msgForlderManager = msgForlderManager;
	}
}
