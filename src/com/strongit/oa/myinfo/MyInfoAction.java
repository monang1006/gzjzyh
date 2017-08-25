/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-02-09
 * Autour: pengxq
 * Version: V1.0
 * Description：个人信息action
*/
package com.strongit.oa.myinfo;

import java.awt.Image;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.userdetails.UserDetails;

import com.strongit.gzjzyh.GzjzyhConstants;
import com.strongit.gzjzyh.tosync.IToSyncManager;
import com.strongit.oa.attachment.AttachmentHelper;
import com.strongit.oa.bo.ToaPersonalConfig;
import com.strongit.oa.bo.ToaPersonalInfo;
import com.strongit.oa.bo.ToaSyssingleload;
import com.strongit.oa.bo.ToaSystemmanageSystemLink;
import com.strongit.oa.bo.ToaSystemset;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.doctemplate.doctempItem.DocTempItemManager;
import com.strongit.oa.im.cache.Cache;
import com.strongit.oa.mymail.util.StringUtil;
import com.strongit.oa.mymail.util.WriteMail;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.systemlink.SysLinkManage;
import com.strongit.oa.systemset.SystemsetManager;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * ��������ACTION
 * 
 * @author pengxq
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "myInfo!input.action", type = ServletActionRedirectResult.class) })
public class MyInfoAction extends BaseActionSupport<ToaPersonalInfo> 
{
	private ToaPersonalInfo model = new ToaPersonalInfo();	//个人信息bo
	private ToaPersonalConfig configModel=new ToaPersonalConfig();	//个人配置bo
	private ToaSyssingleload singleloadmodel=new ToaSyssingleload();	//第三方系统bo
	private ToaSystemmanageSystemLink toaSystemmanageSystemLink=new ToaSystemmanageSystemLink();	//第三方系统链接bo
	private List<ToaSyssingleload> list1 = new ArrayList<ToaSyssingleload>();  //个人信息列表
	private MyInfoManager infoManager;						//个人信息manager
	private IUserService userManagers;						//用户接口 
	@Autowired 
	private InfoConfigManager configManager;				//个人配置manager
	@Autowired
	private SystemsetManager systemsetManager; //系统全局设置
	@Autowired
	private IToSyncManager syncManager;
	private DesktopSectionManager sectionManager;
	private String prsnId;									//个人信息主键
	private List<ToaPersonalConfig> list=new ArrayList<ToaPersonalConfig>(); //个人配置列表
	private String oldPassword;					//旧密码
	private String newPassword;					//新密码
	private String rePassword;
	private String md5pass;
	
	private String userRest1;                	//用户类型
	
	private String delPics;                     //删除签名图片

	private File upload;
	private String SYS_ID;
	private String initdatas;
	
	private Page<ToaSystemmanageSystemLink> page1 = new Page<ToaSystemmanageSystemLink>(FlexTableTag.MAX_ROWS, true);
	private List<ToaSystemmanageSystemLink> list2 = new ArrayList<ToaSystemmanageSystemLink>();
	private SysLinkManage sysLinkManage;
	private String linkUrl;
	private String systemName;
	private String systemDesc;

	/**
   	 * @roseuid 4948589E038A
   	 */
   	@Override
   	protected void prepareModel() 
   	{
   		
   	}
   
   	/**
   	 * @return java.lang.String
   	 * @roseuid 494852C302EE
   	 */
   	public String input() throws Exception{	
   	  getRequest().setAttribute("backlocation", 
				getRequest().getContextPath()+"/myinfo/myInfo!input.action");
   		User user=userManagers.getCurrentUser();
   		String userName=user.getUserName();
   		UserDetails userDetail=this.getUserDetails();
   		String userid=user.getUserId();					//用户id
   		String userPassword=userDetail.getPassword();	//用户密码
   		userRest1 = user.getRest1();
   		model=infoManager.getInfoByUserid(userid);		//个人信息bo对象
   		if(model==null||model.getPrsnId()==null||model.getPrsnId().equals("")||model.getPrsnId().equals("null")){
   			model=new ToaPersonalInfo();
   			model.setPrsnName(userName);
   			configModel=new ToaPersonalConfig();
   		}else{
   			list=configManager.getConfigByInfoid(model.getPrsnId());
   			if(list!=null&&list.size()>0){
   				//configModel=list.get(0);
   				configModel=infoManager.getConfigByUserid(userid);	//根据用户id获取个人配置信息
   			} 			
   		}
   		oldPassword=userPassword;
   		
   		byte[] buf = model.getSign();
   		if(buf != null) {
   			InputStream is = FileUtil.ByteArray2InputStream(buf);
   			String path = AttachmentHelper.saveFile(getRequest().getContextPath(), is, "temp.jpg");
   			logger.info("生成附件：" + path);
   			model.setTempFilePath(path);   			
   		}
   		return INPUT;
   	}

   	
   	
	/**
	 * 获取个人账号单点登陆信息
	 * 
	 * @description
	 * @author 高岱
	 * @return
	 * @createTime 2013年4月7日16:19:19
	 */
   	
   	public String singleLoadon(){
   	 getRequest().setAttribute("backlocation", 
				getRequest().getContextPath()+"/systemlink/sysLink.action");		
	   page1=sysLinkManage.getSysLink(page1, systemName ,linkUrl,systemDesc);
	   list2 =page1.getResult();
	   getRequest().setAttribute("list", list2);
   		String userId=userManagers.getCurrentUser().getUserId();
   		StringBuffer htmlStr = new StringBuffer();
   		List<ToaSyssingleload> list3 = new ArrayList<ToaSyssingleload>(); 
   		list3=infoManager.getInfoByUSER_ID(userId);
   		list1 = new ArrayList();
   		for(ToaSyssingleload t : list3){
   			ToaSyssingleload m = new ToaSyssingleload();
   			m.setLinkId(t.getLinkId());
   			m.setSYS_ID(t.getSYS_ID());
   			m.setSYS_NAME(t.getSYS_NAME());
   			m.setSYS_NUM(t.getSYS_NUM());
   			m.setUSER_ID(t.getUSER_ID());
   		    String temp = t.getSYS_PASSWORD();
   		    temp =  temp.replaceAll("\"", "&quot");
   		    m.setSYS_PASSWORD(temp);
   			list1.add(m);
   		}
   		if(list1.isEmpty() || list1.size()==0){
   			list1=null;
   		}
   		return "singleLoadon";
   		
   		}
   	
	
   
   	
	public List<ToaSystemmanageSystemLink> getList2() {
		return list2;
	}

	public void setList2(List<ToaSystemmanageSystemLink> list2) {
		this.list2 = list2;
	}

	/**
	 * 获取工作委派信息
	 * 
	 * @description
	 * @author 刘皙
	 * @return
	 * @createTime 2012年4月7日16:19:19
	 */
	public String inputWorkEntrust() throws Exception {
		User user = userManagers.getCurrentUser();
		String userid = user.getUserId(); // 用户id
		model = infoManager.getInfoByUserid(userid); // 个人信息bo对象
		return "workEntrust";
	}

	/**
	 * 保存工作委派信息
	 * 
	 * @description
	 * @author 刘皙
	 * @return
	 * @createTime 2012年4月7日16:19:19
	 */
	public String saveWorkEntrust() throws Exception {
		String mailnum = model.getMailnum();
		User user = userManagers.getCurrentUser();
		String userid = user.getUserId(); // 用户id
		model = infoManager.getInfoByUserid(userid); // 个人信息bo对象
		model.setMailnum(mailnum);
		infoManager.savePrsnInfo(model); // 保存个人信息
		return "workEntrust";
	}
   
   	/**
   	 * @return java.lang.String
   	 * @roseuid 4948589E031C
   	 */
   	 @Override
     public String list() throws Exception
     {
  	   getRequest().setAttribute("backlocation", 
  				getRequest().getContextPath()+"/systemlink/sysLink.action");		
  	   page1=sysLinkManage.getSysLink(page1, systemName ,linkUrl,systemDesc);
  	   getRequest().setAttribute("list", page1.getResult());
  	   return SUCCESS;
     }
   		
   	/**
   	 * @return java.lang.String
   	 * @roseuid 4948589E033C
   	 */
   	
   	@Override
   	public String save() throws Exception 
   	{	
   		if("".equals(model.getPrsnId())){
   			model.setPrsnId(null);
   		}
   		User user=userManagers.getCurrentUser();
   		model.setUserId(user.getUserId());
   		String msg=infoManager.savePrsnInfo(model);	//保存个人信息
   		//赣州经侦银行查询系统使用，同步数据到警局端
   		this.syncManager.createToSyncMsg(model, GzjzyhConstants.OPERATION_TYPE_EDIT_BANKACCOUNT_PERSONAL);
   		if(upload != null) {//签名
   			FileInputStream fis = null;
   			try {
   				//判断图片分辨率是否符合
   				Image srcImage = null;
   				srcImage = ImageIO.read(upload); 
   				int srcWidth = srcImage.getWidth(null);		//原图片宽度   
   				int srcHeight = srcImage.getHeight(null);	//原图片高度 
   				if(srcWidth!=120||srcHeight!=90){
   					StringBuffer ttt = new StringBuffer("<script>");
   					ttt.append("alert(\"图片尺寸不符合，请重新选择图片上传。\"); window.location='")
   					.append(getRequest().getContextPath())
   					.append("/myinfo/myInfo!input.action';</script>");
   					return renderHtml(ttt.toString());
   				}
   				
   				
   				File file = File.createTempFile("test", ".jpg");// 创建临时文件
   				String tempFile = file.getPath();
   				logger.info(tempFile);
   				DocTempItemManager.makeSmallImage(upload, tempFile);
				fis = new FileInputStream(file);
				byte[] buf = FileUtil.inputstream2ByteArray(fis);
				model.setSign(buf);
				infoManager.savePrsnInfo(model);
				//file.delete();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally {
				if(fis != null) {
					fis.close();
				}
			}
   		}else if(delPics!=null&&!"".equals(delPics)){
   			//model.setSign(null);
   			infoManager.getInfoByUSERID1(user.getUserId());
   		}
   		updatePassword();		//修改密码
   		if("".equals(configModel.getPrsnConfId())){
   			configModel.setPrsnConfId(null);
   		}
   		if(model.getPrsnId()!=null&&!"".equals(model)&&!"null".equals(model))
   			//configModel.setToaPersonalInfo(model);
   			//configManager.saveConfig(configModel);	//保存个人配置信息
   		
   		addActionMessage(msg);
   		
   		StringBuffer returnhtml = new StringBuffer(
		"<script> var scriptroot = '")
		.append(getRequest().getContextPath())
		.append("'</script>")
		.append("<SCRIPT src='")
		.append(getRequest().getContextPath())
		.append(
				"/common/frame/perspective_content/actions_container/js/workservice.js'>")
		.append("</SCRIPT>")
		.append("<SCRIPT src='")
		.append(getRequest().getContextPath())
		.append(
				"/common/frame/perspective_content/actions_container/js/service.js'>")
		.append("</SCRIPT>")
		.append("<script>");
   		if(msg!=null&&!"".equals(msg)){
			returnhtml.append("alert('")
			.append(getActionMessages().toString())
			.append("');");
   		}
   		returnhtml.append("alert(' 保存成功。');");
		returnhtml.append(" window.location='")
		.append(getRequest().getContextPath())
		.append(
				"/myinfo/myInfo!input.action';</script>");
   		return renderHtml(returnhtml.toString());
   	}
   	
   	
   	
   	public String delPic(){
   		User user=userManagers.getCurrentUser();
   		infoManager.getInfoByUSERID1(user.getUserId());
   		renderText("true");
		return null;
	}
   	
   	
   	/**
   	 * 保存系统信息
   	 * @date 2014年1月10日09:06:49
   	 */
   	
 	public String saveSystem() throws Exception 
   	{	
 	//	System.out.println(list1)
 		User user=userManagers.getCurrentUser();
 		if(list1!=null){        //list1，从jsp页面传过来的实体的数组
 			for(int i=0;i<list1.size();i++){
 				ToaSyssingleload singleload = list1.get(i);
 				//
 				if(singleload.getSYS_ID() == null || "".equals(singleload.getSYS_ID())){
 					singleload.setSYS_ID(null);//如果是""，需要设置为null,才能保存到数据库中
 				}
 					singleload.setUSER_ID(user.getUserId());
 					infoManager.saveSystem(singleload);
 			}
 		}
   		return renderHtml("ok");
   	}
   	
   	
 	
	/**
	 * author:高岱
	 * description: 第三方系统桌面显示
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
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if(blockid != null){
			Map<String,String> map = sectionManager.getParam(blockid);//通过blockid获取映射对象
			showNum = map.get("showNum");//显示条数
			subLength = map.get("subLength");//主题长度
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}
		if(sectionFontSize == null || "".equals("sectionFontSize") || "null".equals("sectionFontSize") ){
			sectionFontSize = "12";
		}
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		//		链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/systemlink/sysLink.action")
			.append("', '系统列表栏'")
			.append(");");
		
//		获取系统列表list
		page1=sysLinkManage.getSysLink(page1, "" ,"","");
		List<ToaSystemmanageSystemLink> result = page1.getResult();
		if(result != null && !result.isEmpty()) {
			
			for(int i=0;i<num&&i<result.size();i++){//获取在条数范围内的列表
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
                innerHtml.append("<tr>");
				innerHtml.append("<td>");
				
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				
				
				ToaSystemmanageSystemLink notify = result.get(i);
				String title = notify.getSystemName();
				if(title==null){
					title="";
				}
				StringBuilder titleLink = new StringBuilder();
				titleLink.append("javascript:window.open('").append(notify.getLinkUrl()).append("','第三方');");
				StringBuilder tip = new StringBuilder();
				tip.append(notify.getSystemName())
					.append("\n").append("系统名称：" + notify.getSystemName() )
					.append("\n").append("链接地址：" + notify.getLinkUrl() );
				
				
				if(title.length()>length)//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<a href=\"#\" onclick=\"").append(titleLink.toString()).append("\">")
				.append("<span style=\"font-size: "+sectionFontSize+"px;\" title=\"").append(tip).append("\">").append(title).append("</span></a>");
				innerHtml.append("</td>");
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
			}
		}
		
		if(result==null){
			for (int i = 0; i < num; i++) { // 获取在条数范围内的列表
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				innerHtml
				.append("	&nbsp;");
				innerHtml.append("</td>");
				innerHtml.append("</tr>");
				innerHtml.append("	</table>");
				}
		}
		if(result!=null&&result.size()<num){
			for (int i = 0; i < num-result.size() ; i++) { // 获取在条数范围内的列表
			innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
			innerHtml.append("<tr>");
			innerHtml.append("<td>");
			innerHtml
			.append("	&nbsp;");
			innerHtml.append("</td>");
			innerHtml.append("</tr>");
			innerHtml.append("	</table>");
			}
		}
		/*innerHtml.append("<div align=\"right\" style=\"padding:2px;font-size:12px;\"><a href=\"#\" onclick=\"").append(link.toString()).append("\"> ")
				 .append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");*/
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}
 	
   	
   	
   	
   	
 	
 	
   
   	/**
   	 * 
   	  * @author：pengxq
   	  * @time：2009-2-10下午08:05:19
   	  * @desc: 更改用户密码
   	  * @param 
   	  * @return void
   	 */
   	private void updatePassword(){
   		User user=userManagers.getCurrentUser();
   		try{
   			if(newPassword!=null&&!"".equals(newPassword)&&!"null".equals(newPassword)&&!oldPassword.equals(newPassword)){
   				userManagers.updateUserPassword(user.getUserId(), newPassword);
   				
   				//更新即使通讯软件中的密码等信息
   				User userModel = new User();
   				userModel.setUserId(user.getUserId());
   				userModel.setUserPassword(newPassword);
   				Cache.getService().updateUserInfo(userModel);
   				// ---------- End ---------------
   				
   				//更新邮件、论坛中的密码等信息
   				//SyncBbsMailUserpsw syncUser = new SyncBbsMailUserpsw();
   				//syncUser.setUserPswByUrl(user.getUserLoginname(), rePassword, SyncBbsMailUserpsw.BBSURL);
   				//syncUser.setUserPswByUrl(user.getUserLoginname(), rePassword, SyncBbsMailUserpsw.MAILURL);
   				// ---------- End ---------------
   				
   				//将新密码写进session
   				getRequest().getSession().setAttribute("ExpresslyPassword", rePassword);
   				// ---------- End ---------------
   			}
   		}catch(Exception e){
   			e.printStackTrace();
   		}
   	}
   	
   	public String changePassword(){
   		try{
   			updatePassword();
   			return renderText("true");
   		}catch(Exception e){
   			e.printStackTrace();
   			return renderText("false");
   		}
   	}
   	
   	public String chargePass(){
   		//读取系统全局设置
   		ToaSystemset systemset = systemsetManager.getSystemset();
   		//密码是否必须同时包含字母和数字
   		String passSet = systemset.getPassSet();
   		if(passSet != null && "true".equals(passSet)){
   			//是否含有数字
   			boolean includeDigit = false;
   			//是否含有字母
   			boolean includeLetter = false;
   			if(newPassword != null && !"".equals(newPassword)){
   				for(int i=0; i<newPassword.length(); i++){
   					char ch = newPassword.charAt(i);
   					if(Character.isDigit(ch)){
   						includeDigit = true;
   					}
   					if(Character.isLetter(ch)){
   						includeLetter = true;
   					}
   				}
   				if(!includeDigit || !includeLetter){
   					return renderText("noLetterOrDigit");
   				}
   			}
   		}
   		User user=userManagers.getCurrentUser();
   		User nowUser=userManagers.getUserInfoByUserId(user.getUserId());
   		if(md5pass.equals(nowUser.getUserPassword())){
   			return renderText("true");
   		}else{
   			return renderText("false");
   		}
   	}
   	
   	/**
   	  * @author：pengxq
   	  * @time：2009-2-12下午02:10:09
   	  * @desc: 个人默认邮箱发送邮件测试
   	  * @param
   	  * @return
   	 */
	public String sendMail() throws Exception{
		String msg="false";
   		if(configModel!=null){
   			String sendServer=configModel.getDefaultMailSys();		//获得smtp服务器
   			String fromEmail=configModel.getDefaultMail();
   			String port=configModel.getDefaultMailPort();			//获得smtp服务器端口
   			String ssl=configModel.getDefaultMailSsl();				//是否需要ssl加密
   			String username=configModel.getDefaultMailUser();		//用户名称
   			String password=configModel.getDefaultMailPsw();	
   			List<String> toList=StringUtil.stringToList(fromEmail, ",");			//收件人员列表	
   			WriteMail writeMail=new WriteMail(sendServer,fromEmail,username,username,password,toList,null,null,"个人默认邮箱测试","测试成功",ssl,port);
   			HashMap map=writeMail.send();	
   			String state=(String) map.get("state");
  			if(state.equals("success")){
  				msg="true";
  			}else{
  				msg="false";
  			}
   		}
   		return renderText(msg);
	}
   		
   	/**
   	 * @return java.lang.String
   	 * @roseuid 4948589E036B
   	 */
   	@Override
   	public String delete() 
   	{
   		return null;
   	}
   	
   	
   	
   	/**
	 * author:luosy
	 * description:删除一个或多个系统信息
	 * modifyer:
	 * description:
	 * @return
	 */
	public String deleteSys() throws Exception {
		try {
				if ("".equals(SYS_ID) | null == SYS_ID) {
				} else {
					infoManager.deleteSys(SYS_ID);
				}
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
			return renderText("ok");
	}

   	
 
	public ToaPersonalInfo getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	@Autowired
	public void setInfoManager(MyInfoManager infoManager) {
		this.infoManager = infoManager;
	}

	@Autowired
	public void setUserManagers(IUserService userManagers) {
		this.userManagers = userManagers;
	}

	public String getPrsnId() {
		return prsnId;
	}

	public void setPrsnId(String prsnId) {
		this.prsnId = prsnId;
	}

	public ToaPersonalConfig getConfigModel() {
		return configModel;
	}

	public void setConfigModel(ToaPersonalConfig configModel) {
		this.configModel = configModel;
	}

	

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getMd5pass() {
		return md5pass;
	}

	public void setMd5pass(String md5pass) {
		this.md5pass = md5pass;
	}

	public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}
	
	public String getDelPics() {
		return delPics;
	}

	public void setDelPics(String delPics) {
		this.delPics = delPics;
	}

	public String getUserRest1() {
		return userRest1;
	}

	public void setUserRest1(String userRest1) {
		this.userRest1 = userRest1;
	}

	public ToaSyssingleload getSingleloadmodel() {
		return singleloadmodel;
	}

	public void setSingleloadmodel(ToaSyssingleload singleloadmodel) {
		this.singleloadmodel = singleloadmodel;
	}
	public List<ToaSyssingleload> getList1() {
		return list1;
	}

	public void setList1(List<ToaSyssingleload> list1) {
		this.list1 = list1;
	}

	public String getInitdatas() {
		return initdatas;
	}

	public void setInitdatas(String initdatas) {
		this.initdatas = initdatas;
	}
	
	public String getSYS_ID() {
		return SYS_ID;
	}

	public void setSYS_ID(String sYS_ID) {
		SYS_ID = sYS_ID;
	}
	
	public ToaSystemmanageSystemLink gettoaSystemmanageSystemLink() {
		// TODO Auto-generated method stub
		return toaSystemmanageSystemLink;
	}
	
	public void settoaSystemmanageSystemLink(ToaSystemmanageSystemLink toaSystemmanageSystemLink) {
		this.toaSystemmanageSystemLink = toaSystemmanageSystemLink;
	}
	
	@Autowired
	public void setsysLinkManage(SysLinkManage sysLinkManage) {
		this.sysLinkManage = sysLinkManage;
	}

	public SysLinkManage getsysLinkManage() {
		return sysLinkManage;
	}
	
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemDesc() {
		return systemDesc;
	}

	public void setSystemDesc(String systemDesc) {
		this.systemDesc = systemDesc;
	}
	
	
	@Autowired
	public void setSectionManager(DesktopSectionManager sectionManager) {
		this.sectionManager = sectionManager;
	}

	public ToaSystemmanageSystemLink getToaSystemmanageSystemLink(){
		return toaSystemmanageSystemLink;
	}

	public void setToaSystemmanageSystemLink(
			ToaSystemmanageSystemLink toaSystemmanageSystemLink) {
		this.toaSystemmanageSystemLink = toaSystemmanageSystemLink;
	}

	public List<ToaPersonalConfig> getList() {
		return list;
	}

	public void setList(List<ToaPersonalConfig> list) {
		this.list = list;
	}

	public Page<ToaSystemmanageSystemLink> getPage1() {
		return page1;
	}

	public void setPage1(Page<ToaSystemmanageSystemLink> page1) {
		this.page1 = page1;
	}

	public SysLinkManage getSysLinkManage() {
		return sysLinkManage;
	}

	public void setSysLinkManage(SysLinkManage sysLinkManage) {
		this.sysLinkManage = sysLinkManage;
	}
	
}
