package com.strongit.oa.mymail;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaMail;
import com.strongit.oa.bo.ToaMailAttach;
import com.strongit.oa.bo.ToaMailBox;
import com.strongit.oa.bo.ToaMailFolder;
import com.strongit.oa.bo.ToaPersonalConfig;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.myinfo.MyInfoManager;
import com.strongit.oa.mymail.util.AttachmentUtil;
import com.strongit.oa.mymail.util.FileHelper;
import com.strongit.oa.mymail.util.StringUtil;
import com.strongit.oa.mymail.util.StrongDate;
import com.strongit.oa.mymail.util.WriteMail;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
/**
 * 处理邮件模块Action
 * @author yuhz
 * @version 1.0
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "/mail.action") })
public class MailAction extends BaseActionSupport{
	
	private int nowProcess;					//目前收取完毕的邮件数目
	
	private int total;						//总共的邮件数目
	
	private ToaMail myMail;
	
	private MailManager mailManager;
	
	private MailFolderManager mailFolderManager;
	
	private MailBoxManager mailBoxManager;
	
	private IAttachmentService attachmentService;
	
	private MailAttManager mailAttManager;
	
	private List<ToaMailFolder> folderList;
	
	private String sendid;
	
	private String folderId;			 //文件夹ID
	
	private String boxId;				 //邮箱ID
	
	private String boxName;				 //邮箱名称
	
	private String folderName;			 //文件夹名称
	
	private String type;				 //防止出现问题该属性只许赋值
	
	private String returnType;		     //功能和上面的变量一直，只能前台读取不能赋值
	
	private Page<ToaMail> page=new Page<ToaMail>(FlexTableTag.MAX_ROWS,true);
	
	private List<ToaMailBox> list;
	
	private String sender;					//发送ID
	
	private String csinput;					//抄送
	
	private String msinput;					//密送
	
	private String subject;					//邮件主题
	
	private String ydhz;					//已读回执
	
	private String desc;					//文章正文
		
	private File[] file;					//附件列表
	
	private String[] fileName;				//附件名称
	
	private String mailaddress;				//收件人地址
	
	private String showReceiver;			//显示邮件的时候用到的显示
	
	private String quicklyre;				//快速回复正文
	
	private String msgId;					//根据其来判断是否是已经存在的邮件
	
	private List<ToaAttachment> attList;	//附件列表
	
	private String[] dbobj;					//已经存储在数据库中的附件转发中的
	
	private String searchName;				//邮件标题名称
	
	private Date beginDate;					//开始时间
	
	private Date endDate;					//结束时间
	
	private String firstDate;			
	
	private String otherDate;
	
	private String blockid;					//个人桌面传入
	
	private DesktopSectionManager dsmanager;
	
	private IUserService user;
	
	private String myDefaultMail;
	
	private MyInfoManager myInfoManager;
	
	private String receiver;
	
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setQuicklyre(String quicklyre) {
		this.quicklyre = quicklyre;
	}
	
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}

	@Autowired
	public void setMailFolderManager(MailFolderManager mailFolderManager) {
		this.mailFolderManager = mailFolderManager;
	}

	@Autowired
	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}

	@Autowired
	public void setMailBoxManager(MailBoxManager mailBoxManager) {
		this.mailBoxManager = mailBoxManager;
	}
	
	@Autowired
	public void setAttachmentService(IAttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	@Autowired
	public void setMailAttManager(MailAttManager mailAttManager) {
		this.mailAttManager = mailAttManager;
	}

	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20091:51:34 PM
	 * @desc: 收取邮件调用入口
	 * @throws Exception void
	 */
	public void getMail()throws Exception{
		//System.out.println("开始收取邮件");
		HttpSession session=getSession();
		session.setAttribute("getstatus", "1");
		session.setAttribute("totalmail", "10000");
		ToaMailBox mailBox=mailBoxManager.getObjById(sendid);
		ToaMailFolder mailFolder=mailFolderManager.getFolderByParent(sendid);
		if(mailManager.recSaveMail(session, mailBox,mailFolder)){
			session.setAttribute("totalmail", "-2");			//邮箱成功标识
		}else{
			session.setAttribute("totalmail", "-1");			//邮件出现错误，发送错误标识回前台进行处理
		}
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20091:49:48 PM
	 * @desc: 显示收取邮件的进度
	 * @return
	 * @throws Exception String
	 */
	public String showStatus() throws Exception{
		HttpSession session=getSession();
		//System.out.println(session.getAttribute("getstatus"));
		HttpServletResponse response=this.getResponse();
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		String total="<total>"+session.getAttribute("totalmail")+"</total>";
		String status="<status>"+session.getAttribute("getstatus")+"</status>";
		out.println("<response>");
		out.println(total);
		out.println(status);
		out.println("</response>");
		out.close();
		return null;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20091:52:06 PM
	 * @desc: 快捷回复调用类
	 * @return
	 * @throws Exception String
	 */
	public String quickSend() throws Exception{
		HttpServletResponse response=this.getResponse();
		List<String> toList=null;
		List<String> ccList=null;
		List<String> bccList=null;
		PrintWriter out = response.getWriter();
		ToaMailBox sendBox=mailBoxManager.getObjById(boxId);
		ToaMail viewMail=mailManager.getObjById(sendid);
		
		String sendServer=sendBox.getSmtpServer();
		String fromEmail=sendBox.getMailAddress();
		String port=sendBox.getSmtpPort();
		String ssl=sendBox.getSmtpSsl();
		String user=sendBox.getPop3Account();
		String password=sendBox.getPop3Pwd();
		toList=StringUtil.stringToList(viewMail.getMailSender(), ",");
		ccList=StringUtil.stringToList("", ",");								//抄送人员列表
		bccList=StringUtil.stringToList("", ",");
		WriteMail writeMail=new WriteMail(sendServer,fromEmail,user,user,password,toList,ccList,bccList,"Re:"+viewMail.getMailTitle(),quicklyre,ssl,port);
		HashMap map=writeMail.send();
		if("success".equals(map.get("state"))){
			ToaMailFolder folder=mailFolderManager.getFolderByParent(boxId, "2");
			ToaMail saveObj=new ToaMail();
			saveObj.setMailIsHasAtt("0");										//附件
			saveObj.setMailIsReply("0");										//已读回执
			saveObj.setMailIsRead("0");											//是否已读
			saveObj.setMailPri("0");
			saveObj.setMailCon(quicklyre);										//正文
			saveObj.setMailReceiver(viewMail.getMailSender());					//收件人
			saveObj.setMailSendDate(new Date());								//发件日期
			saveObj.setMailSender(fromEmail);									//发件人
			saveObj.setMailTitle("Re:"+viewMail.getMailTitle());				//邮件主题
			saveObj.setToaMailFolder(folder);									//文件夹
			if(mailManager.saveObj(saveObj)){
				out.println("<script>parent.callback('true')</script>");		//回掉所要执行的javascript语句
			}else{
				out.println("<script>parent.callback('savefalse')</script>");
			}
		}else{
			out.println("<script>parent.callback('false')</script>");
		}
		return null;
	}
	
	public String otherModelSend() throws Exception{
		HttpServletResponse response=this.getResponse();
		List<String> ccList=null;
		List<String> bccList=null;
		List<String> toList=null;
		PrintWriter out = response.getWriter();
		User nowUser=user.getCurrentUser();
		ToaPersonalConfig config=myInfoManager.getConfigByUserid(nowUser.getUserId());
		if(config==null){
			
		}else{
			Vector<FileHelper> attach=new Vector<FileHelper>();
			if(file!=null){
				if(!AttachmentUtil.chargeFileEmpty(file)){								//判断邮件的附件列表是否都存在
					out.println("<script>parent.callback('empty')</script>");
					return null;
				}else{

					//进行附件的存储工作
					long temp = 0;
					for(int i=0;i<file.length;i++){
						temp += file[i].length();
						FileHelper po=new FileHelper();
						po.setFile(file[i]);
						po.setFileName(fileName[i]);
						attach.add(po);
					}
					if(temp>GlobalBaseData.ATTACH_SIZE){
						out.println("<script>parent.callback('lagre')</script>");
						return null;
					}
				}
			}
			String sendServer=config.getDefaultMailSys();								//获得smtp服务器
			String fromEmail=config.getDefaultMail();
			String port=config.getDefaultMailPort();									//获得smtp服务器端口
			String ssl=config.getDefaultMailSsl();										//是否需要ssl加密
			String user=config.getDefaultMailUser();									//用户名称
			String password=config.getDefaultMailPsw();									//用户密码
			toList=StringUtil.stringToList(mailaddress, ",");							//收件人员列表
			ccList=StringUtil.stringToList(csinput, ",");								//抄送人员列表
			bccList=StringUtil.stringToList(msinput, ",");								//密送人员列表
			WriteMail writeMail=new WriteMail(sendServer,fromEmail,user,user,password,toList,ccList,bccList,subject,desc,ssl,port);
			writeMail.setFile(attach);
			if("1".equals(ydhz)){														//判断所发邮件是否要进行已读回执
				writeMail.setReadBack(true);
			}
			HashMap map=writeMail.send();
			if("success".equals(map.get("state"))){
				out.println("<script>parent.callback('true')</script>");
				return null;
			}else{
				out.println("<script>parent.callback('false')</script>");
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20091:52:39 PM
	 * @desc: 邮件发送方法（发送邮件页面）
	 * @return
	 * @throws Exception String
	 */
	public String sendMail() throws Exception{
		HttpServletResponse response=this.getResponse();
		List<String> ccList=null;
		List<String> bccList=null;
		List<String> toList=null;
		PrintWriter out = response.getWriter();
		ToaMailBox mailBox=mailBoxManager.getObjById(sender);
		Vector<FileHelper> attach=new Vector<FileHelper>();
		if(file!=null){
			if(!AttachmentUtil.chargeFileEmpty(file)){								//判断邮件的附件列表是否都存在
				out.println("<script>parent.callback('empty')</script>");
				return null;
			}else{
				//进行附件的存储工作
				long temp = 0;
				for(int i=0;i<file.length;i++){
					temp += file[i].length();
					FileHelper po=new FileHelper();
					po.setFile(file[i]);
					po.setFileName(fileName[i]);
					attach.add(po);
				}
				if(temp>GlobalBaseData.ATTACH_SIZE){
					out.println("<script>parent.callback('lagre')</script>");
					return null;
				}
			}
		}
		if(dbobj!=null){															//判断是否在数据库中存在邮件（例如草稿箱中有邮件列表重发送的时候）
			for(int k=0;k<dbobj.length;k++){
				if(dbobj[k].indexOf(";")!=-1){
					
				}else{
					ToaAttachment attachment=attachmentService.getAttachmentById(dbobj[k]);
					byte[] fileBytes=attachment.getAttachCon();
					try{
//						File fold=new File("c://mailtemp");							//将以前就存在的邮件从数据库中取出存放在临时文件夹中
						File fold=new File(System.getProperty("java.io.tmpdir")+"\\oatemp");
						if(fold.exists()){
							
						}else{
							fold.mkdir();
						}
//						System.getProperty("java.io.tmpdir");
						File tempFile=this.getFileFromBytes(fileBytes, System.getProperty("java.io.tmpdir")+"\\oatemp\\"+attachment.getAttachId());	//临时文件夹路径选取
						//System.out.println("----------------------------"+System.getProperty("user.dir")+attachment.getAttachId());
						FileHelper po=new FileHelper();
						po.setFile(tempFile);
						po.setFileName(attachment.getAttachName());
						attach.add(po);
					}catch(Exception e){
						e.printStackTrace();
					}

				}	
					
			}
		}
		String sendServer=mailBox.getSmtpServer();									//获得smtp服务器
		String fromEmail=mailBox.getMailAddress();
		String port=mailBox.getSmtpPort();											//获得smtp服务器端口
		String ssl=mailBox.getSmtpSsl();											//是否需要ssl加密
		String user=mailBox.getPop3Account();										//用户名称
		String password=mailBox.getPop3Pwd();										//用户密码
		toList=StringUtil.stringToList(mailaddress, ",");							//收件人员列表
		ccList=StringUtil.stringToList(csinput, ",");								//抄送人员列表
		bccList=StringUtil.stringToList(msinput, ",");								//密送人员列表
		
		WriteMail writeMail=new WriteMail(sendServer,fromEmail,user,user,password,toList,ccList,bccList,subject,desc,ssl,port);
		writeMail.setFile(attach);
		if("1".equals(ydhz)){														//判断所发邮件是否要进行已读回执
			writeMail.setReadBack(true);
		}
		HashMap map=writeMail.send();
		if("success".equals(map.get("state"))){										//根据发送邮件后返回的Map来测试是否发送成功
			ToaMailFolder folder=mailFolderManager.getFolderByParent(sender, "2");
			ToaMail saveObj=new ToaMail();
			saveObj.setMailCon(desc);
			if(file==null){
				saveObj.setMailIsHasAtt("0");
				if(dbobj==null){
					
				}else{
					for(int f=0;f<dbobj.length;f++){
						if(dbobj[f].indexOf(";")==-1){
							saveObj.setMailIsHasAtt("1");
							break;
						}
					}
				}
			}else{
				saveObj.setMailIsHasAtt("1");
			}
			if("1".equals(ydhz)){
				saveObj.setMailIsReply("1");
			}else{
				saveObj.setMailIsReply("0");
			}
			saveObj.setMailIsRead("0");
			
			saveObj.setMailPri("0");
			saveObj.setMailReceiver(mailaddress);
			saveObj.setMailSendDate(new Date());
			saveObj.setMailSender(fromEmail);
			saveObj.setMailTitle(subject);
			saveObj.setToaMailFolder(folder);
			if(mailManager.saveObj(saveObj)){														//进行数据库保存操作
				if(file!=null||dbobj!=null){
					Calendar cals = Calendar.getInstance();
					int k=0;
					if(file!=null){
						for(k=0;k<file.length;k++){
							FileInputStream fils = null;
							fils = new FileInputStream(file[k]);
							byte[] buf = new byte[(int)file[k].length()];
							fils.read(buf);	
							String attId = attachmentService.saveAttachment(fileName[k], buf, cals.getTime(), "", "1", "注:邮件附件", "userId");
							if(mailAttManager.saveObj(attId, saveObj)){
								
							}else{
								out.println("<script>parent.callback('false')</script>");
								return null;
							}
						}
					}
					int other=0;
					if(dbobj!=null){
						
						for(other=0;other<dbobj.length;other++){
							if(dbobj[other].indexOf(";")!=-1){
								
							}else{
								ToaAttachment attachment=attachmentService.getAttachmentById(dbobj[other]);
								String oAttId=attachmentService.saveAttachment(attachment.getAttachName(), attachment.getAttachCon(), cals.getTime(), "", "1", "注:邮件附件", "userId");
								if(mailAttManager.saveObj(oAttId, saveObj)){
									
								}else{
									out.println("<script>parent.callback('false')</script>");
									return null;
								}
							}
						}
					}
					
					out.println("<script>parent.callback('true')</script>");

					
				}else{
					out.println("<script>parent.callback('true')</script>");			//回掉所要执行的javascript语句
				}
			}else{
				out.println("<script>parent.callback('savefalse')</script>");
			}
		}else{
			out.println("<script>parent.callback('false')</script>");
		}
		return null;
	}
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20091:57:36 PM
	 * @desc: 进行转发操作
	 * @return
	 * @throws Exception String
	 */
	public String tran() throws Exception{
		if(boxId!=null&&!"".equals(boxId)){
			List<ToaMailBox> temp=mailBoxManager.getAllList();
			list=new ArrayList<ToaMailBox>();
			for(int i=0;i<temp.size();i++){
				if(boxId.equals((temp.get(i)).getMailboxId())){
					list.add(temp.get(i));
					temp.remove(i);
					break;
				}
			}
			for(int i=0;i<temp.size();i++){
				list.add(temp.get(i));
			}
		}
		myMail=mailManager.getObjById(sendid);
		StringBuffer beforeHtml=new StringBuffer();
		beforeHtml.append("<br>")
				  .append("<br>")
				  .append("<br>")
				  .append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>"+StrongDate.getDate(new Date(), StrongDate.heng)+"</FONT></DIV>\n")
				  .append("<HR style=\"WIDTH: 122px; HEIGHT: 2px\" align=\"left\" SIZE=\"2\">\n")
				  .append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=\"2\">原发件人："+myMail.getMailSender()+"</FONT></DIV>\n")
				  .append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=\"2\">发送时间："+StrongDate.getDate(myMail.getMailSendDate(), StrongDate.heng)+"</FONT></DIV>\n")
				  .append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=\"2\">收&nbsp;件&nbsp;人："+StringUtil.getRetrunStr(myMail.getMailReceiver())+"</FONT></DIV>\n")
				  .append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=\"2\">主&nbsp;&nbsp;&nbsp;&nbsp;题："+StringUtil.getRetrunStr(myMail.getMailTitle())+"</FONT></DIV>\n")
				  .append("<HR style=\"WIDTH: 100%; HEIGHT: 2px\" align=left SIZE=\"2\">\n");
		

		Set attSet=myMail.getToaMailAttaches();
		if(attSet==null||attSet.size()==0){

		}else{
			List<ToaMailAttach> list=new ArrayList<ToaMailAttach>(attSet);
			attList=new ArrayList<ToaAttachment>();
			for(int i=0;i<list.size();i++){
				ToaMailAttach temp=list.get(i);
				ToaAttachment obj=attachmentService.getAttachmentById(temp.getAttachId6());
				attList.add(obj);
			}
		}
		mailManager.flashSession();
		myMail.setMailCon(beforeHtml.toString()+(myMail.getMailCon()==null?"":myMail.getMailCon()));
		myMail.setMailTitle("FW:"+(myMail.getMailTitle()==null?"":myMail.getMailTitle()));
		return "tran";
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20091:57:54 PM
	 * @desc: 草稿箱进行编辑操作
	 * @return
	 * @throws Exception String
	 */
	public String editDrafts() throws Exception{
		if(boxId!=null&&!"".equals(boxId)){
			List<ToaMailBox> temp=mailBoxManager.getAllList();
			list=new ArrayList<ToaMailBox>();
			for(int i=0;i<temp.size();i++){
				if(boxId.equals((temp.get(i)).getMailboxId())){
					list.add(temp.get(i));
					temp.remove(i);
					break;
				}
			}
			for(int i=0;i<temp.size();i++){
				list.add(temp.get(i));
			}
		}
		myMail=mailManager.getObjById(sendid);
		Set attSet=myMail.getToaMailAttaches();
		if(attSet==null||attSet.size()==0){

		}else{
			List<ToaMailAttach> list=new ArrayList<ToaMailAttach>(attSet);
			attList=new ArrayList<ToaAttachment>();
			for(int i=0;i<list.size();i++){
				ToaMailAttach temp=list.get(i);
				ToaAttachment obj=attachmentService.getAttachmentById(temp.getAttachId6());
				attList.add(obj);
			}
		}
		return "editDrafts";
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20091:59:01 PM
	 * @desc: 回复操作
	 * @return
	 * @throws Exception String
	 */
	public String reply()throws Exception{
		if(boxId!=null&&!"".equals(boxId)){
			List<ToaMailBox> temp=mailBoxManager.getAllList();
			list=new ArrayList<ToaMailBox>();
			for(int i=0;i<temp.size();i++){
				if(boxId.equals((temp.get(i)).getMailboxId())){
					list.add(temp.get(i));
					temp.remove(i);
					break;
				}
			}
			for(int i=0;i<temp.size();i++){
				list.add(temp.get(i));
			}
		}
		myMail=mailManager.getObjById(sendid);
//		System.out.println(myMail.getToaMailAttaches().size());
		StringBuffer beforeHtml=new StringBuffer();
		beforeHtml.append("<br>")
				  .append("<br>")
				  .append("<br>")
				  .append("<br>")
				  .append("<br>")
				  .append("<br>")
				  .append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>"+StrongDate.getDate(new Date(), StrongDate.heng)+"</FONT></DIV>\n")
				  .append("<HR style=\"WIDTH: 122px; HEIGHT: 2px\" align=left SIZE=2>\n")
				  .append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>发&nbsp;件&nbsp;人："+myMail.getMailSender()+"</FONT></DIV>\n")
				  .append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>发送时间："+StrongDate.getDate(myMail.getMailSendDate(), StrongDate.heng)+"</FONT></DIV>\n")
				  .append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>收&nbsp;件&nbsp;人："+StringUtil.getRetrunStr(myMail.getMailReceiver())+"</FONT></DIV>\n")
				  .append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>主&nbsp;&nbsp;&nbsp;&nbsp;题："+StringUtil.getRetrunStr(myMail.getMailTitle())+"</FONT></DIV>\n")
				  .append("<HR style=\"WIDTH: 100%; HEIGHT: 2px\" align=left SIZE=2>\n");
		mailManager.flashSession();								//防止进行set操作后就进行数据保存
		myMail.setMailCon(beforeHtml.toString()+myMail.getMailCon());
		myMail.setMailTitle("RE:"+myMail.getMailTitle());
		return "reply";
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20091:59:26 PM
	 * @desc: 对草稿箱的草稿进行保存操作
	 * @return
	 * @throws Exception String
	 */
	public String saveDraft()throws Exception{
		HttpServletResponse response=this.getResponse();
		PrintWriter out = response.getWriter();
		ToaMailBox mailBox=mailBoxManager.getObjById(sender);
		ToaMailFolder folder=mailFolderManager.getFolderByParent(sender, "3");
		ToaMail saveObj=new ToaMail();
		if(msgId!=null){
			saveObj.setMailId(msgId);
		}
		saveObj.setMailCon(desc);
		if(file==null){
			saveObj.setMailIsHasAtt("0");
			if(dbobj==null){
				
			}else{
				for(int f=0;f<dbobj.length;f++){
					if(dbobj[f].indexOf(";")==-1){
						saveObj.setMailIsHasAtt("1");
						break;
					}
				}
			}
		}else{
			saveObj.setMailIsHasAtt("1");
		}
		saveObj.setMailIsRead("0");
		if("1".equals(ydhz)){
			saveObj.setMailIsReply("1");
		}else{
			saveObj.setMailIsReply("0");
		}
		saveObj.setMailPri("0");
		saveObj.setMailReceiver(mailaddress);
		saveObj.setMailSendDate(new Date());
		saveObj.setMailSender(mailBox.getMailAddress());
		saveObj.setMailTitle(subject);
		saveObj.setToaMailFolder(folder);
		if(mailManager.saveObj(saveObj)){
			if(file!=null||dbobj!=null){
				Calendar cals = Calendar.getInstance();
				int k;
				if(file!=null){
					for(k=0;k<file.length;k++){
						FileInputStream fils = null;
						fils = new FileInputStream(file[k]);
						byte[] buf = new byte[(int)file[k].length()];
						fils.read(buf);	
						String attId = attachmentService.saveAttachment(fileName[k], buf, cals.getTime(), "", "1", "注:邮件附件", "userId");
						if(mailAttManager.saveObj(attId, saveObj)){
							
						}else{
							out.println("<script>parent.aftersave('false')</script>");
							return null;
						}
					}
				}
				int other=0;
				if(dbobj!=null){
					
					for(other=0;other<dbobj.length;other++){
						if(dbobj[other].indexOf(";")!=-1){						//存在这样的数据就删除，证明附件就会删除
							String id=dbobj[other].substring(0, dbobj[other].indexOf(";"));
							attachmentService.deleteAttachment(id);
							ToaMailAttach mailAtt=mailAttManager.findByAttId(id);
							if(mailAtt!=null){
								mailAttManager.deleteObj(mailAtt);
							}
						}else{
							
						}
					}
				}
				
				out.println("<script>parent.aftersave('true')</script>");

				
			}else{
				out.println("<script>parent.aftersave('true')</script>");		//回掉所要执行的javascript语句
			}
		}else{
			out.println("<script>parent.aftersave('false')</script>");
		}
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		if("real".equals(type)){
			if(mailManager.delObjById(sendid)){
				return renderText("true");
			}else{
				return renderText("false");
			}
		}else if("notreal".equals(type)){
			if(mailManager.changeBox(sendid)){
				return renderText("true");
			}else{
				return renderText("false");
			}
		}
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Autowired
	public void setDsmanager(DesktopSectionManager dsmanager) {
		this.dsmanager = dsmanager;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:00:32 PM
	 * @desc: 与个人桌面的接口
	 * @return
	 * @throws Exception String
	 */
	public String showDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		User userObj=user.getCurrentUser();
		//List<ToaMail> list = mailManager.getNewestMailByUser(userObj.getUserId());
		//String blockid = getRequest().getParameter("blockid");		//获取blockid
		Map<String,String> map = dsmanager.getParam(blockid);		//通过blockid获取映射对象
		String showNum = map.get("showNum");						//显示条数
		String subLength = map.get("subLength");					//主题长度
		String showCreator = map.get("showCreator");				//是否显示作者
		String showDate = map.get("showDate");						//是否显示日期
		String sectionFontSize = map.get("sectionFontSize");		//是否显示字体大小
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
		page.setPageSize(num);
		List<ToaMail> list=mailManager.getNewestMailByUser(userObj.getUserId(),page);
		if(list!=null){
			for(int i=0;i<num&&i<list.size();i++){						//获取在条数范围内的列表
				ToaMail mail = list.get(i);
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				if("1".equals(mail.getMailIsRead()))					//如果为已读，则显示已读图片，否则显示未读图片
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/mymail/read.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				else
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/mymail/unread.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				String title = mail.getMailTitle();
				title=title==null?"无标题":title;
				if(title.length()>length)								//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				if("1".equals(mail.getMailIsRead())){
					innerHtml.append("	<span title=\"").append(mail.getMailTitle()).append("\">")
					.append("	<a href=\"#\" style=\"font-size:"+sectionFontSize+"px\" onclick=\"toViewMail('"+mail.getMailId()+"')\"> ").append(title).append("</a></span>");
				}else{
					innerHtml.append("	<span title=\"").append(mail.getMailTitle()).append("\">")
					.append("	<a href=\"#\" style=\"font-size:"+sectionFontSize+"px\" onclick=\"toViewMail('"+mail.getMailId()+"')\"> ").append("<font color=black face='黑体'>"+title+"</font>").append("</a></span>");
				}
				innerHtml.append("</td>");
				if("1".equals(showCreator))	{							//如果设置为显示作者，则显示作者信息
					innerHtml.append("<td width=\"115px\">");
					innerHtml.append("<span class =\"linkgray\">").append(mail.getMailSender()).append("</span>");
					innerHtml.append("</td>");
				}
				
				if("1".equals(showDate)){								//如果设置为显示日期，则显示日期信息
					innerHtml.append("<td width=\"100px\">");
					String dateStr="";
					if(mail.getMailSendDate()==null){
						
					}else{
						dateStr=st.format(mail.getMailSendDate());
					}
					innerHtml.append("	<span class =\"linkgray10\">").append(dateStr).append("</span>");
					innerHtml.append("	</td>");
				}
				innerHtml.append("	</tr>");
				innerHtml.append("	</table>");
			}
		}
		if(list==null){
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
		if(list!=null&&list.size()<num){
			for (int i = 0; i < num-list.size() ; i++) { // 获取在条数范围内的列表
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
		return renderHtml(innerHtml.toString());					//用renderHtml将设置好的html代码返回桌面显示
	}
	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub
		if("afterrec".equals(type)){					//收件完成之后进行跳转操作
			ToaMailFolder folder=mailFolderManager.getFolderByParent(sendid, "1");
			if(folder==null){
				return "context";
			}
			boxId=(folder.getToaMailBox()).getMailboxId();
			//生成转移文件夹数据
			List<ToaMailFolder> tempList=mailFolderManager.getFolderByPId(boxId);
			folderList=new ArrayList<ToaMailFolder>();
			for(int i=0;i<tempList.size();i++){
				if(folder.getMailfolderId().equals(tempList.get(i).getMailfolderId())){
					continue;
				}else{
					folderList.add(tempList.get(i));
				}
			}
			boxName=(folder.getToaMailBox()).getMailboxUserName();
			folderName=folder.getMailfolderName();
			returnType=type;
			page=mailManager.getBoxMail(folder, page,this.searchName,this.beginDate,this.endDate);
			this.firstDate=StrongDate.getDate(this.beginDate, StrongDate.heng);
			this.otherDate=StrongDate.getDate(this.endDate, StrongDate.heng);
			return SUCCESS;
		}else if("view".equals(type)){					//进行查看操作
			ToaMailFolder folder=mailFolderManager.getObjById(sendid);
			if(folder==null){
				return "context";
			}
			boxId=(folder.getToaMailBox()).getMailboxId();
			//生成转移文件夹数据
			List<ToaMailFolder> tempList=mailFolderManager.getFolderByPId(boxId);
			folderList=new ArrayList<ToaMailFolder>();
			for(int i=0;i<tempList.size();i++){
				if(folder.getMailfolderId().equals(tempList.get(i).getMailfolderId())){
					continue;
				}else{
					folderList.add(tempList.get(i));
				}
			}
			boxName=(folder.getToaMailBox()).getMailboxUserName();
			folderName=folder.getMailfolderName();
			returnType=type;
			page=mailManager.getBoxMailById(sendid, page,this.searchName,this.beginDate,this.endDate);
			this.firstDate=StrongDate.getDate(this.beginDate, StrongDate.heng);
			this.otherDate=StrongDate.getDate(this.endDate, StrongDate.heng);
			return SUCCESS;
		}else if("send".equals(type)){						//发送完成后的操作
			ToaMailFolder folder=mailFolderManager.getObjById(sendid);
			if(folder==null){
				return "context";
			}
			boxId=(folder.getToaMailBox()).getMailboxId();
			//生成转移文件夹数据
			List<ToaMailFolder> tempList=mailFolderManager.getFolderByPId(boxId);
			folderList=new ArrayList<ToaMailFolder>();
			for(int i=0;i<tempList.size();i++){
				if(folder.getMailfolderId().equals(tempList.get(i).getMailfolderId())){
					continue;
				}else{
					folderList.add(tempList.get(i));
				}
			}
			boxName=(folder.getToaMailBox()).getMailboxUserName();
			folderName=folder.getMailfolderName();
			returnType=type;
			page=mailManager.getBoxMailById(sendid, page,this.searchName,this.beginDate,this.endDate);
			this.firstDate=StrongDate.getDate(this.beginDate, StrongDate.heng);
			this.otherDate=StrongDate.getDate(this.endDate, StrongDate.heng);
			return "sendList";
		}else if("rubbish".equals(type)){					//垃圾箱操作
			ToaMailFolder folder=mailFolderManager.getObjById(sendid);
			if(folder==null){
				return "context";
			}
			boxId=(folder.getToaMailBox()).getMailboxId();
			//生成转移文件夹数据
			List<ToaMailFolder> tempList=mailFolderManager.getFolderByPId(boxId);
			folderList=new ArrayList<ToaMailFolder>();
			for(int i=0;i<tempList.size();i++){
				if(folder.getMailfolderId().equals(tempList.get(i).getMailfolderId())){
					continue;
				}else{
					folderList.add(tempList.get(i));
				}
			}
			boxName=(folder.getToaMailBox()).getMailboxUserName();
			folderName=folder.getMailfolderName();
			returnType=type;
			page=mailManager.getBoxMailById(sendid, page,this.searchName,this.beginDate,this.endDate);
			this.firstDate=StrongDate.getDate(this.beginDate, StrongDate.heng);
			this.otherDate=StrongDate.getDate(this.endDate, StrongDate.heng);
			return "rubbishList";
		}else if("other".equals(type)){						//对手东建立的文件夹的操作
			ToaMailFolder folder=mailFolderManager.getObjById(sendid);
			if(folder==null){
				return "context";
			}
			boxId=(folder.getToaMailBox()).getMailboxId();
			//生成转移文件夹数据
			List<ToaMailFolder> tempList=mailFolderManager.getFolderByPId(boxId);
			folderList=new ArrayList<ToaMailFolder>();
			for(int i=0;i<tempList.size();i++){
				if(folder.getMailfolderId().equals(tempList.get(i).getMailfolderId())){
					continue;
				}else{
					folderList.add(tempList.get(i));
				}
			}
			boxName=(folder.getToaMailBox()).getMailboxUserName();
			folderName=folder.getMailfolderName();
			returnType=type;
			page=mailManager.getBoxMailById(sendid, page,this.searchName,this.beginDate,this.endDate);
			this.firstDate=StrongDate.getDate(this.beginDate, StrongDate.heng);
			this.otherDate=StrongDate.getDate(this.endDate, StrongDate.heng);
			return "otherList";
		}else if("drafts".equals(type)){					//跳转至草稿箱的操作
			ToaMailFolder folder=mailFolderManager.getObjById(sendid);
			if(folder==null){
				return "context";
			}
			boxId=(folder.getToaMailBox()).getMailboxId();
			//生成转移文件夹数据
			List<ToaMailFolder> tempList=mailFolderManager.getFolderByPId(boxId);
			folderList=new ArrayList<ToaMailFolder>();
			for(int i=0;i<tempList.size();i++){
				if(folder.getMailfolderId().equals(tempList.get(i).getMailfolderId())){
					continue;
				}else{
					folderList.add(tempList.get(i));
				}
			}
			boxName=(folder.getToaMailBox()).getMailboxUserName();
			folderName=folder.getMailfolderName();
			returnType=type;
			page=mailManager.getBoxMailById(sendid, page,this.searchName,this.beginDate,this.endDate);
			return "draftsList";	
		}
		return null;
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:03:14 PM
	 * @desc: 进行文件夹转换的操作
	 * @return
	 * @throws Exception String
	 */
	public String changeFolder() throws Exception{
		ToaMailFolder folder=mailFolderManager.getObjById(folderId);
		if(folder==null){
			return renderText("nofolder");
		}else{
			if(sendid!=null&&!"".equals(sendid)){
				if(mailManager.changeFolder(sendid, folder)){
					return renderText("true");
				}else{
					return renderText("false");
				}
			}else{
				return renderText("exception");
			}

		}
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:03:35 PM
	 * @desc: 进行快速阅读
	 * @return
	 * @throws Exception String
	 */
	public String quicklyview()throws Exception{
		
		if(sendid!=null&&!"".equals(sendid)){
			myMail=mailManager.getObjById(sendid);
			Set attSet=myMail.getToaMailAttaches();
			if(attSet==null||attSet.size()==0){

			}else{
				List<ToaMailAttach> list=new ArrayList<ToaMailAttach>(attSet);
				attList=new ArrayList<ToaAttachment>();
				for(int i=0;i<list.size();i++){
					ToaMailAttach temp=list.get(i);
					ToaAttachment obj=attachmentService.getAttachmentById(temp.getAttachId6());
					attList.add(obj);
				}
			}
		}
		return "quicklyview";
	}
	
	/**
	 * 
	 * author:yuhz
	 * description:为其他调用用户配置的默认邮件发送邮件提供.
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String otherModel() throws Exception{
		User nowUser=user.getCurrentUser();
		ToaPersonalConfig config=myInfoManager.getConfigByUserid(nowUser.getUserId());
		receiver=URLDecoder.decode(receiver,"utf-8");
		if(config==null){
			
		}else{
			myDefaultMail=config.getDefaultMail();
		}
		return "otherModel";		
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:03:51 PM
	 * @desc: 进行查看邮件的操作
	 * @return
	 * @throws Exception String
	 */
	public String view() throws Exception{
		if(sendid!=null&&!"".equals(sendid)){
			myMail=mailManager.getObjById(sendid);
			Set attSet=myMail.getToaMailAttaches();
			if(attSet==null||attSet.size()==0){

			}else{
				List<ToaMailAttach> list=new ArrayList<ToaMailAttach>(attSet);
				attList=new ArrayList<ToaAttachment>();
				for(int i=0;i<list.size();i++){
					ToaMailAttach temp=list.get(i);
					ToaAttachment obj=attachmentService.getAttachmentById(temp.getAttachId6());
					attList.add(obj);
				}
			}
			boxId=(myMail.getToaMailFolder()).getToaMailBox().getMailboxId();
			this.showReceiver=myMail.getMailReceiver();
			if(showReceiver==null||"null".equals(showReceiver)){
				showReceiver="";
				myMail.setMailReceiver("");
			}
			if(showReceiver.indexOf(",")==-1){
				
			}else{
				String[] temp=showReceiver.split(",");
				if(temp.length>3){
					showReceiver=temp[0]+","+temp[1]+","+temp[2]+".....";
				}else{
					showReceiver="";
					for(int i=0;i<temp.length;i++){
						if(i!=temp.length-1){
							showReceiver=showReceiver+temp[i]+",";
						}else{
							showReceiver=showReceiver+temp[i];
						}
					}
				}
			}
			mailManager.changeState(sendid);
		}
		return "view";
	}
	public String desktopview() throws Exception{
		if(sendid!=null&&!"".equals(sendid)){
			myMail=mailManager.getObjById(sendid);
			Set attSet=myMail.getToaMailAttaches();
			if(attSet==null||attSet.size()==0){

			}else{
				List<ToaMailAttach> list=new ArrayList<ToaMailAttach>(attSet);
				attList=new ArrayList<ToaAttachment>();
				for(int i=0;i<list.size();i++){
					ToaMailAttach temp=list.get(i);
					ToaAttachment obj=attachmentService.getAttachmentById(temp.getAttachId6());
					attList.add(obj);
				}
			}
			boxId=(myMail.getToaMailFolder()).getToaMailBox().getMailboxId();
			this.showReceiver=myMail.getMailReceiver();
			if(showReceiver==null||"null".equals(showReceiver)){
				showReceiver="";
				myMail.setMailReceiver("");
			}
			if(showReceiver.indexOf(",")==-1){
				
			}else{
				String[] temp=showReceiver.split(",");
				if(temp.length>3){
					showReceiver=temp[0]+","+temp[1]+","+temp[2]+".....";
				}else{
					showReceiver="";
					for(int i=0;i<temp.length;i++){
						if(i!=temp.length-1){
							showReceiver=showReceiver+temp[i]+",";
						}else{
							showReceiver=showReceiver+temp[i];
						}
					}
				}
			}
			mailManager.changeState(sendid);
		}
		return "desktopview";
	}
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:07:06 PM
	 * @desc: 显示邮件信息
	 * @return
	 * @throws Exception String
	 */
	public String showInfo() throws Exception{
		if(sendid!=null&&!"".equals(sendid)){
			myMail=mailManager.getObjById(sendid);
		}
		return "showInfo";
	}
	
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:04:13 PM
	 * @desc: 跳转至发送邮件页面所利用方法
	 * @return
	 * @throws Exception String
	 */
	public String write() throws Exception{
		if(boxId!=null&&!"".equals(boxId)){
			//System.out.println(boxId);
			List<ToaMailBox> temp=mailBoxManager.getAllList();
			list=new ArrayList<ToaMailBox>();
			for(int i=0;i<temp.size();i++){
				if(boxId.equals((temp.get(i)).getMailboxId())){
					list.add(temp.get(i));
					temp.remove(i);
					break;
				}
			}
			for(int i=0;i<temp.size();i++){
				list.add(temp.get(i));
			}
		}
		return "write";
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:07:40 PM
	 * @desc: 将而二进制字节转化为对象
	 * @param objBytes
	 * @return
	 * @throws Exception Object
	 */
	public Object getObjectFromBytes(byte[] objBytes) throws Exception{
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        return oi.readObject();
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 17, 20092:08:02 PM
	 * @desc: 
	 * @param b
	 * @param outputFile
	 * @return File
	 */
	public File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }


	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSendid(String sendid) {
		this.sendid = sendid;
	}

	public Page<ToaMail> getPage() {
		return page;
	}

	public String getSendid() {
		return sendid;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBoxId() {
		return boxId;
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	public String getBoxName() {
		return boxName;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getReturnType() {
		return returnType;
	}

	public ToaMail getMyMail() {
		return myMail;
	}

	public List<ToaMailBox> getList() {
		return list;
	}

	public void setCsinput(String csinput) {
		this.csinput = csinput;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setMsinput(String msinput) {
		this.msinput = msinput;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setYdhz(String ydhz) {
		this.ydhz = ydhz;
	}

	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}

	public void setFileFileName(String[] fileName) {
		this.fileName = fileName;
	}

	public String getShowReceiver() {
		return showReceiver;
	}

	public List<ToaMailFolder> getFolderList() {
		return folderList;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public List<ToaAttachment> getAttList() {
		return attList;
	}

	public void setDbobj(String[] dbobj) {
		this.dbobj = dbobj;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public String getFirstDate() {
		return firstDate;
	}

	public String getOtherDate() {
		return otherDate;
	}

	public void setEndDate(Date endDate) {
		if(endDate==null){
			
		}else{
			endDate.setHours(23);
			endDate.setMinutes(59);
			endDate.setSeconds(59);
		}
		this.endDate = endDate;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public void setBlockid(String blockid) {
		this.blockid = blockid;
	}
	@Autowired
	public void setMyInfoManager(MyInfoManager myInfoManager) {
		this.myInfoManager = myInfoManager;
	}

	public String getMyDefaultMail() {
		return myDefaultMail;
	}

	public void setMyDefaultMail(String myDefaultMail) {
		this.myDefaultMail = myDefaultMail;
	}
}
