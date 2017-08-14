/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息 action跳转类
 */
package com.strongit.oa.message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaMessage;
import com.strongit.oa.bo.ToaMessageFolder;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.smscontrol.IsmscontrolService;
import com.strongit.oa.updatebadwords.phrasefilter.IPhraseFilterManage;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.PathUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "message.action", type = ServletActionRedirectResult.class),
	@Result(name = "info", value = "/WEB-INF/jsp/message/info.jsp", type = ServletDispatcherResult.class)})
public class MessageAction extends BaseActionSupport {

	private Page<ToaMessage> page = new Page<ToaMessage>(FlexTableTag.MIDDLE_ROWS, true);

	private String msgId;

	private ToaMessage model = new ToaMessage();

	private MessageManager manager;
	
	private MessageFolderManager msgForlderManager;
	
	private String folderId;	//文件夹
	private String folderName;	//文件夹名称
	
	private String disLogo;		//查看信息时，区分发件箱、收件箱、草稿箱、垃圾箱、其他
	
	private String forward;		//控制跳转
	
	private String showReceiver;//显示收件人姓名
	
	private String msgReceiverIds;//接受人id
	
	private String msgReceiverNames;//接受人姓名
	
	private String sendid;		//查看信息时的信息id
	
	private String quicklyre;	//快速回复正文
	
	private String csinput;		//抄送
	
	private String sfxyhz;		//回复信息时的是否需要回执
	
	private String dxhf;		//发送短信时是否需要短信回复
	
	private String sfhz;		//阅读信息时的是否回执
		
	private File[] file;		//附件
	
	private String[] fileFileName;	//附件名称
	
	private List<ToaMessageFolder> folderList;	

	//搜索时间范围
	private String searchTitle;		//标题
	
	private Date beginDate;			//开始时间

	private Date starttime;			//开始时间,用于时间查询
	
	private Date endDate;			//结束时间
	
	private Date endtime;			//结束时间,用于时间查询
	
	private String readstate;       //阅读状态，用于状态查询
	
	private List attachMentList;	//附件列表
	
	private String[] dbobj;					//已经存储在数据库中的附件转发中的
	
	private String isclose;
	
	private boolean hasSendRight;
	
	private DesktopSectionManager sectionManager;
	
	private IPhraseFilterManage filterManager;
	
	private IUserService user;
	
	private IsmscontrolService smscontrolService;
	
	private String moduleCode;//模块ID
	
	private ToaBussinessModulePara modulePara;//对应模块配置
	
	private List means;	//短信回复信息{同意，不同意……}
	
	private String ownerNum;	//自定义手机号码
	
	private String smsCon;	//自定义短信内容
	
	private Date smsSendDate;	//短信发送时间

	private long BaseMsgSize;

	private long usedMsgSize;
	
	/**词语过滤模块--消息模块对应的ID*/
	private static final String FILTER_PHRASE_ID = "402882271e1f6980011e1f6ce51a0001";
	
	@Autowired
	public void setManager(MessageManager manager) {
		this.manager = manager;
	}

	@Autowired
	public void setMsgForlderManager(MessageFolderManager msgForlderManager) {
		this.msgForlderManager = msgForlderManager;
	}
	
	@Autowired
	public void setSectionManager(DesktopSectionManager sectionManager) {
		this.sectionManager = sectionManager;
	}
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}
	
	@Autowired
	public void setSmscontrolService(IsmscontrolService smscontrolService) {
		this.smscontrolService = smscontrolService;
	}
	
	public void setModel(ToaMessage model) {
		this.model = model;
	}

	public ToaMessage getModel() {
		return model;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page<ToaMessage> aPage) {
		page = aPage;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String aMsgId) {
		msgId = aMsgId;
	}

	public MessageAction() {

	}
	
	////////////////////////////// 以下为action跳转方法
	/**
	 * author:luosy
	 * description:	 编辑或再次发送  
	 * 				 初始化数据
	 * modifyer:
	 * description:
	 * @return
	 */
	public String input() throws Exception {
		model = manager.getMessageById(msgId);
		msgReceiverNames = model.getMsgReceiver();
		if(!"".equals(msgReceiverNames)&&null!=msgReceiverNames){
			msgReceiverIds = manager.getUserIdsInFormat(msgReceiverNames);
			msgReceiverNames = msgReceiverNames.replaceAll("<id:\\w{32}>", "");
		}
		attachMentList=manager.getAttachMent(model.getMsgId());
		
		//如果发送了短信
//smsCon,ownerNum,means,moduleCode);
		String mCode = model.getModuleCode();
		String iCode = model.getMessageCode();
		if(!"".equals(iCode)&&null!=iCode
				&&!"".equals(mCode)&&null!=mCode){
			dxhf = "1";
			means = manager.getMsgMeansbyMsgId(msgId);
			smsCon = manager.getSmsConByMoudelCode(mCode, iCode);
			ownerNum = manager.getOwnerNumByMoudelCode(mCode, iCode);
		}else{
			//		取出默认短信回复答案
			means = manager.getModulStateMean(moduleCode);
			//短信模块配置
			modulePara = manager.getModuleParaByCode(moduleCode);
		}
		
		//短信最大字数
		modulePara = manager.getModuleParaByCode(moduleCode);
		
		//获取发送权限
		String userId = user.getCurrentUser().getUserId();
		this.hasSendRight = smscontrolService.hasSendRight(userId);
		
		BaseMsgSize = manager.getbaseMsgSize();
		usedMsgSize = manager.getMsgAttachmentSize(userId);
		return "write";
	}

	/**
	 * author:luosy
	 * description:	消息列表
	 * modifyer:
	 * description:
	 * @return
	 */
	public String list() throws Exception {
		folderList = manager.getMyFolderList();
		try {
			if(searchTitle!=null&&!"".equals(searchTitle)){
				searchTitle = URLDecoder.decode(searchTitle, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String forward="otherlist";
		if("recv".equals(folderId)){
			page = manager.getMsgByFolderName(page,ToaMessageFolder.FOLDER_RECV,this.starttime,this.endtime,this.readstate,searchTitle);
			getRequest().setAttribute("starttime",beginDate);
			getRequest().setAttribute("endtime", endDate);
			forward="recvlist";
		}else if("send".equals(folderId)){
			page = manager.getMsgByFolderName(page,ToaMessageFolder.FOLDER_SEND,this.starttime,this.endtime,this.readstate,searchTitle);
			getRequest().setAttribute("starttime",beginDate);
			getRequest().setAttribute("endtime", endDate);
			forward="sendlist";
		}else if("draft".equals(folderId)){
			page = manager.getMsgByFolderName(page,ToaMessageFolder.FOLDER_DRAFT,this.starttime,this.endtime,this.readstate,searchTitle);
			getRequest().setAttribute("starttime",beginDate);
			getRequest().setAttribute("endtime", endDate);
			forward="draftlist";
		}else if("rubbish".equals(folderId)){
			page = manager.getMsgByFolderName(page,ToaMessageFolder.FOLDER_RUBBISH,this.starttime,this.endtime,this.readstate,searchTitle);
			getRequest().setAttribute("starttime",beginDate);
			getRequest().setAttribute("endtime", endDate);
			forward="rubbishlist";
		}else{
			folderName = manager.getFolderByFolderId(folderId).getMsgFolderName();
			page = manager.getMsgByFolderId(page,folderId,this.starttime,this.endtime,this.readstate,searchTitle);
			getRequest().setAttribute("starttime",beginDate);
			getRequest().setAttribute("endtime", endDate);
			forward="otherlist";
		}
		return forward;
	}

	/**
	 * author:luosy
	 * description:	发送消息
	 * modifyer:
	 * description:
	 * @return
	 */
	public String sendMessage() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		String forward=null;
		String callback = "true";//返回页面后的操作
		
		
		if(file!=null){
			long temp = 0;
			long usedSize = manager.getMsgAttachmentSize(user.getCurrentUser().getUserId());
			long baseSize = manager.getbaseMsgSize();
			for(int i=0;i<file.length;i++){
				if(file[i].length()==0){
					callback = "empty";
					HttpServletResponse response=this.getResponse();
					PrintWriter out;
					try {
						out = response.getWriter();
						out.println("<script>parent.callback('"+callback+"')</script>");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return forward;
				}
				temp += file[i].length();
			}
			if((temp+usedSize)>baseSize){
				callback = "attach";
				
				HttpServletResponse response=this.getResponse();
				PrintWriter out;
				try {
					out = response.getWriter();
					out.println("<script>parent.callback('"+callback+"')</script>");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return forward;
			}
		}
		
		if("alluser".equals(msgReceiverIds)){
			msgReceiverIds = manager.getAllUserId();
		}
		//我的消息对应的过滤模块主键：402882271e1f6980011e1f6ce51a0001
		//手机短信modleId：40280c981ec653cc011ec65b6db1000d
		//个人邮件modleId：40280c981ec653cc011ec65718fd0004
		
		model.setIsSendBack(sfxyhz);				//设置是否需要回执
		String content;
		String title;
		if(disLogo!=null&&disLogo.equals("reply")){	//回复短消息	
			content=filterManager.filterPhrase(quicklyre, FILTER_PHRASE_ID);
			title = filterManager.filterPhrase(model.getMsgTitle(), FILTER_PHRASE_ID);
			model.setMsgTitle(title);
			model.setMsgContent(HtmlUtils.htmlEscape(content));
//			处理用户id格式
			String sender = manager.formatUserId(user.getCurrentUser().getUserId());
			String recvUser = manager.formatUserId(msgReceiverIds);
			
			model.setMsgSender(sender);
			model.setMsgReceiver(recvUser);
			manager.replyMessage(msgId,model,file,fileFileName);
		}else if(disLogo!=null&&disLogo.equals("resend")){	//转发	
			content=filterManager.filterPhrase(quicklyre, FILTER_PHRASE_ID);
			title = filterManager.filterPhrase(model.getMsgTitle(), FILTER_PHRASE_ID);
			model.setMsgTitle(title);
			model.setMsgContent(HtmlUtils.htmlEscape(content));
			
			//处理用户id格式
			String sender = manager.formatUserId(user.getCurrentUser().getUserId());
			String recvUser = manager.formatUserId(msgReceiverIds);
			
			model.setMsgSender(sender);
			model.setMsgReceiver(recvUser);
			
			ToaMessage msg = manager.sendMessage(msgId,model,file,fileFileName,dbobj, new OALogInfo("我的消息-消息记录-『转发』："+title));
			//发送手机短信 （ dxhf=1：需要发送短信）
			if("1".equals(dxhf)){
				if(moduleCode!=null&&!moduleCode.equals("")){
					callback = manager.sendSms(msg,msgReceiverIds, smsCon,ownerNum,means,smsSendDate,moduleCode);
				}else{
					callback = manager.sendSms(msg,msgReceiverIds, smsCon,ownerNum,means,smsSendDate,"0");
				}
			}
		}else{ 											//新建发送
			content=filterManager.filterPhrase(model.getMsgContent(),FILTER_PHRASE_ID);
			title = filterManager.filterPhrase(model.getMsgTitle(), FILTER_PHRASE_ID);
			model.setMsgTitle(title);
			model.setMsgContent(HtmlUtils.htmlEscape(content));
			//处理用户id格式
			String sender = manager.formatUserId(user.getCurrentUser().getUserId());
			String recvUser = manager.formatUserId(msgReceiverIds);
			
			model.setMsgSender(sender);
			model.setMsgReceiver(recvUser);
			ToaMessage msg = manager.sendMessage(null,model,file,fileFileName,dbobj, new OALogInfo("我的消息-消息记录-『发送』："+title));
			
			//发送手机短信 （ dxhf=1：需要发送短信）
			if("1".equals(dxhf)){
				callback = manager.sendSms(msg,msgReceiverIds, smsCon,ownerNum,means,smsSendDate,moduleCode);
			}
			if(folderId.equals("draft")){//从草稿箱中发送邮件，则删除草稿箱中这条记录
				ToaMessage msgdraft = manager.getMessageById(msgId);
				manager.delMessage(msgdraft, new OALogInfo("我的消息-消息记录-『彻底删除』："+msgdraft.getMsgTitle()));
			}
		}
		HttpServletResponse response=this.getResponse();
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println("<script>parent.callback('"+callback+"')</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return forward;
	}
	
	/**
	  * @author：pengxq
	  * @time：2009-1-14下午08:40:35
	  * @desc: 快速查看邮件
	  * @param 
	  * @return
	 */
	public String quicklyview()throws Exception{
		if(msgId!=null&&!"".equals(msgId)){
			model=manager.getMessageById(msgId);
			if(model!=null&&!"".equals(model)){
				disLogo=model.getToaMessageFolder().getMsgFolderName();
				attachMentList=manager.getAttachMent(msgId);
				if(model.getIsRead()==null||"".equals(model.getIsRead())||"null".equals(model.getIsRead())||"0".equals(model.getIsRead())){
					model.setIsRead("1");
					manager.saveMessage(model);
				}
				return forward;
			}
		}
		return "info";
	}
	
	/**
	  * @author：pengxq
	  * @time：2009-1-14下午08:41:05
	  * @desc: 查看信息详情
	  * @param
	  * @return 跳转到"write"/"view"
	 */
	public String view() throws Exception{
		getRequest().setAttribute("backlocation", "javascript:cancel()");
 		if(msgId!=null&&!"".equals(msgId)){//"view"查看
			model=manager.getMessageById(msgId);
			if(model.getIsRead()==null||"".equals(model.getIsRead())||"null".equals(model.getIsRead())||"0".equals(model.getIsRead())){
				model.setIsRead("1");
				manager.saveMessage(model);
			}	
			showReceiver=model.getMsgReceiver();	//获取收件人
			if(showReceiver==null){
				showReceiver="";
			}
			
			String[] recs = showReceiver.split(",");
			StringBuilder sb = new StringBuilder("");
			if(recs.length>54){
				for (int i=0;i<55;i++){
					sb.append(recs[i]).append(",");
				}
				showReceiver = sb.substring(0, sb.length()-1)+"   ...";
			}
			
			//只有收信需要回执
			if(model.getToaMessageFolder().getMsgFolderName()!=null&&model.getToaMessageFolder().getMsgFolderName().equals("收件箱")){
				ToaMessage sendMessage=manager.getMessageByParen(model.getParentMsgId());
				if(sendMessage!=null&&null==model.getIsReturnBack()){
					sfxyhz=sendMessage.getIsSendBack();		
				}else{
					sfxyhz="0";
				}
			}
			attachMentList=manager.getAttachMent(msgId);
			
		}else{	//"write"写信
			model=new ToaMessage();
			String userId = user.getCurrentUser().getUserId();
			this.hasSendRight = smscontrolService.hasSendRight(userId);
			//通讯录下选用户发消息
			if(!"".equals(msgReceiverIds)&&null!=msgReceiverIds){
				
				msgReceiverNames = "";
				String[] recvIds = msgReceiverIds.split(",");
				for(int i = 0;i<recvIds.length;i++){
					msgReceiverNames += user.getUserNameByUserId(recvIds[i])+",";
				}
				if(!"".equals(msgReceiverNames)&&null!=msgReceiverNames){
					msgReceiverNames = msgReceiverNames.substring(0, msgReceiverNames.length()-1);
				}
			}
			
			//取出默认短信回复答案
			means = manager.getModulStateMean(moduleCode);
			//短信模块配置
			modulePara = manager.getModuleParaByCode(moduleCode);
			
			BaseMsgSize = manager.getbaseMsgSize();
			usedMsgSize = manager.getMsgAttachmentSize(userId);
		}
		return forward;
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-14下午08:41:28
	  * @desc: 快速回复
	  * @param
	  * @return
	 */
	public String quickSend() throws Exception{
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		if(msgId!=null){
			model=manager.getMessageById(msgId);
		}
		String recvId = manager.getUserIdsInFormat(model.getMsgSender());
		if("system".equals(recvId)){	//系统消息不能回复
			return renderHtml("<script>alert(\"不能回复系统信息\");</script>");
		}else{
			manager.quickSend(model,quicklyre,sfxyhz);		
			return "temp";
		}
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-14下午08:41:46
	  * @desc: 
	  * @param
	  * @return
	 */
	public void parameter(){
		
		if(msgId!=null&&!"".equals(msgId)&&!"null".equals(msgId)){
			model=manager.getMessageById(msgId);//在我的发件箱中有，在收信人的收件箱中有
		}else{
			model=new ToaMessage();
		}
	}
	
	/**
	 * @author:hecj
	 * @description:根据msgId获得字符串
	 *
	 */
	public String getModelById(){
		try{
			String ret="";
			if(msgId!=null&&!"".equals(msgId)&&!"null".equals(msgId)){
				model=manager.getMessageById(msgId);//在我的发件箱中有，在收信人的收件箱中有
				
				ToaMessage sendMessage=manager.getMessageByParen(model.getParentMsgId());
				if(sendMessage!=null&&null==model.getIsReturnBack()){
					sfxyhz=sendMessage.getIsSendBack();		
				}else{
					sfxyhz="0";
				}
				ret = sfxyhz+","+(model.getIsReturnBack()==null?"":model.getIsReturnBack());
			}
			return renderHtml(ret);
		}catch( Exception e){
			return renderHtml("");
		}
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-14下午08:41:57
	  * @desc: 跳转至回复信息页面 
	  * @param
	  * @return
	 */
	public String reply()throws Exception{
		
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		parameter();
		model=manager.getReplyCon(model);	//获取回复短消息页面展现的信息
		msgReceiverIds = manager.getUserIdsInFormat(model.getMsgReceiver());
		msgReceiverNames = manager.getUserNameInFormat(model.getMsgReceiver());

		BaseMsgSize = manager.getbaseMsgSize();
		usedMsgSize = manager.getMsgAttachmentSize(user.getCurrentUser().getUserId());
		
		if("system".equals(msgReceiverIds)){	//系统消息不能回复
			return renderHtml("<script>alert(\"不能回复系统信息\");window.close();</script>");
		}else{
			return "reply";
		}
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-15下午02:10:05
	  * @desc: 已读回执
	  * @param
	  * @return
	 */
	public String receipt()throws Exception{
		parameter();
		if(model.getMsgId()!=null){
			model.setIsReturnBack(sfhz);
			manager.saveMessage(model);
			if("1".equals(sfhz)){
				manager.receipt(model);	
			}
		}
		return null;
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-15下午08:26:47
	  * @desc: 跳转至转发信息页面
	  * @param
	  * @return
	 */
	public String tran()throws Exception{
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		parameter();
		model=manager.getTranCon(model);	//获取回复短消息页面展现的信息
		attachMentList=manager.getAttachMent(model.getMsgId());
		if(model.getMsgReceiver()!=null&&!"".equals(model.getMsgReceiver())&&!"null".equals(model.getMsgReceiver())){
			msgReceiverIds = manager.getUserIdsInFormat(model.getMsgReceiver());
			msgReceiverNames = manager.getUserNameInFormat(model.getMsgReceiver());
		}	
		BaseMsgSize = manager.getbaseMsgSize();
		usedMsgSize = manager.getMsgAttachmentSize(user.getCurrentUser().getUserId());
		this.hasSendRight = smscontrolService.hasSendRight(user.getCurrentUser().getUserId());
		return "tran";
	}
	
	/**
	 * author:luosy
	 * description:  移动消息对象到某个文件夹下 （ajax）
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String changeFolder() throws Exception{
		ToaMessageFolder folder=manager.getFolderByFolderId(folderId);
		if(folder==null){
			return renderText("nofolder");
		}else{
			if(msgId!=null&&!"".equals(msgId)){
				if(manager.changeFolder(msgId, folder)){
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
	 * author:luosy
	 * description: 保存为草稿
	 * modifyer:
	 * description:
	 * @return
	 */
	public String save() throws Exception{
		try {
			HttpServletResponse response=this.getResponse();
			PrintWriter out = response.getWriter();
			if("alluser".equals(msgReceiverIds)){
				msgReceiverIds = manager.getAllUserId();
			}
			String callback="true";
			String userId=user.getCurrentUser().getUserId();
//			检查附件
			if(file!=null){
				long temp = 0;
				long usedSize = manager.getMsgAttachmentSize(userId);
				long baseSize = manager.getbaseMsgSize();
				for(int i=0;i<file.length;i++){
					if(file[i].length()==0){
						callback = "empty";
						out.println("<script>parent.aftersave('"+callback+"')</script>");
						return null;
					}
					temp += file[i].length();
				}
				if((temp+usedSize)>baseSize){
					callback = "attach";
					out.println("<script>parent.aftersave('"+callback+"')</script>");
					return null;
				}
			}
			String sender = manager.formatUserId(userId);
			String recvUser = manager.formatUserId(msgReceiverIds);
			model.setMsgSender(sender);
			model.setMsgReceiver(recvUser);
			
			model.setMsgContent(HtmlUtils.htmlEscape(model.getMsgContent()));
			String newMsgId = manager.saveMessageToDraft(model, file, fileFileName,dbobj);
			
			if("0".equals(isclose)){
				callback = "close";
			}else{
				callback = "true";
			}
			out.println("<script>parent.aftersaveWithMsgId('"+callback+"','"+newMsgId+"')</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * author:luosy
	 * description: 删除消息 real:彻底删除|notreal:移动到垃圾箱
	 * modifyer:
	 * description:
	 * @return
	 */
	public String delete() throws Exception{
		if("real".equals(forward)){
			try{
				if(msgId!=null&&!"".equals(msgId)){
					String[] id=msgId.split(",");
					for(int i=0;i<id.length;i++){
						ToaMessage msg = manager.getMessageById(id[i]);
						manager.delMessage(msg, new OALogInfo("我的消息-消息记录-『彻底删除』："+msg.getMsgTitle()));
					}
				}
				return renderText("true");
			}catch(Exception e){
				return renderText("false");
			}
		}else if("notreal".equals(forward)){
			try{
				String[] id=msgId.split(",");
				String userId = user.getCurrentUser().getUserId();
				ToaMessageFolder folder = msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_RUBBISH,userId);
				for(int i=0;i<id.length;i++){
					ToaMessage msg = manager.getMessageById(id[i]);
					msg.setToaMessageFolder(folder);
					manager.saveMessage(msg, new OALogInfo("我的消息-消息记录-『删除』："+msg.getMsgTitle()));
				}
				return renderText("true");
			}catch(Exception e){
				e.printStackTrace();
				return renderText("false");
			}
		}
		return null;
	}

	protected void prepareModel() {

	}
	
	/**
	 * author:luosy
	 * description: 桌面显示
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public String showDesktop() throws Exception {
		StringBuffer innerHtml = new StringBuffer();
		
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum"); 
		String showCreator = getRequest().getParameter("showCreator"); 
		String showDate = getRequest().getParameter("showDate");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		if(blockId != null) {
			Map<String, String> map = sectionManager.getParam(blockId);
			showNum = map.get("showNum");//显示条数
			subLength = map.get("subLength");//主题长度
			showCreator = map.get("showCreator");//是否显示作者
			showDate = map.get("showDate");//是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
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
			sectionFontSize = "12";
		}
		//链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/fileNameRedirectAction.action?toPage=message/message.jsp")
			.append("', '内部邮件'")
			.append(");");
		
		
//		获取公告列表list
		String userId=user.getCurrentUser().getUserId();
		page.setPageSize(num);
		page = manager.getListForTable(userId,page);
		
		List<ToaMessage> list = page.getResult();
		if(null!=list){
			for(int i=0;i<num&&i<list.size();i++){//获取在条数范围内的列表
				ToaMessage msg = list.get(i);
				
//				标题连接
				StringBuffer clickTitle = new StringBuffer();
				clickTitle.append("var a = window.showModalDialog('").append(getRequest().getContextPath())
					.append("/message/message!view.action?forward=view&msgId=").append(msg.getMsgId())
					.append("',window,'dialogWidth:700pt;dialogHeight:400pt;help:no;status:no;scroll:no');")
					.append("if(a == 'reply'){var reply = window.showModalDialog('").append(getRequest().getContextPath())
					.append("/message/message!reply.action?msgId=").append(msg.getMsgId())
					.append("',window,'dialogWidth:700pt;dialogHeight:400pt;help:no;status:no;scroll:no');window.location.reload();}")
					.append("else if(a == 'del'){window.location.reload();} else if(a == 'resend'){var resend = window.showModalDialog('")
					.append(getRequest().getContextPath()).append("/message/message!tran.action?msgId=").append(msg.getMsgId())
					.append("',window,'dialogWidth:700pt;dialogHeight:400pt;help:no;status:no;scroll:no');window.location.reload();}else {window.location.reload();}");
				
				
				SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
				innerHtml.append("<table width=\"100%\" class=\"linkdiv\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				if("1".equals(msg.getIsRead()))//如果为有已读，则显示已读图片，否则显示未读图片
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/mymail/read.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				else
					innerHtml.append("	<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/mymail/unread.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
				String title = msg.getMsgTitle();
				if(title==null){
					title = "";
				}
				if(title.length()>length)//如果显示的内容长度大于设置的主题长度，则过滤该长度
					title = title.substring(0,length)+"...";
				innerHtml.append("	<span title=\"").append(msg.getMsgTitle()).append("\">")
				.append("	<a style=\"font-size:"+sectionFontSize+"px\" href=\"#\" onclick=\"").append(clickTitle.toString()).append("\"> ").append(title).append("</a></span>");
				innerHtml.append("</td>");
				if("1".equals(showCreator)){//如果设置为显示作者，则显示作者信息
					innerHtml.append("<td width=\"80px\">");
					innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px\" class =\"linkgray\">").append(msg.getMsgSender()).append("</span>");
					innerHtml.append("</td>");
				}
				
				if("1".equals(showDate)){//如果设置为显示日期，则显示日期信息
					innerHtml.append("<td width=\"100px\">");
					innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px\" title =\""+new SimpleDateFormat("yyyy-MM-dd hh:mm").format(msg.getMsgDate())+"\" class =\"linkgray10\">").append(st.format(msg.getMsgDate())).append("</span>");
					innerHtml.append("</td>");
				}
				innerHtml.append("	</tr>");
				innerHtml.append("	</table>");
			}
		}if(list==null){
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
		
		innerHtml.append("<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"").append(link.toString()).append("\"> ")
				 .append("<IMG SRC=\"").append(getRequest().getContextPath()).append("/oa/image/more.gif\" BORDER=\"0\" /></a></div>");
		return renderHtml(innerHtml.toString());//用renderHtml将设置好的html代码返回桌面显示
	}
	
	
	/**
	 * author:luosy
	 * description: 快捷方式（发布消息）
	 * 				在location打开写消息页面
	 * modifyer:
	 * description:
	 * @return
	 */
	public String writeMessage(){
		//"write"写信
		model=new ToaMessage();
		String userId = user.getCurrentUser().getUserId();
		this.hasSendRight = smscontrolService.hasSendRight(userId);
		//通讯录下选用户发消息
		if(!"".equals(msgReceiverIds)&&null!=msgReceiverIds){
			
			msgReceiverNames = "";
			String[] recvIds = msgReceiverIds.split(",");
			for(int i = 0;i<recvIds.length;i++){
				msgReceiverNames += user.getUserNameByUserId(recvIds[i])+",";
			}
			if(!"".equals(msgReceiverNames)&&null!=msgReceiverNames){
				msgReceiverNames = msgReceiverNames.substring(0, msgReceiverNames.length()-1);
			}
		}
		
		//取出默认短信回复答案
		means = manager.getModulStateMean(moduleCode);
		//短信模块配置
		modulePara = manager.getModuleParaByCode(moduleCode);
		
		return "justwrite";
	}
	
	/**
	 * @author 张磊
	 * @description:  批量下载邮件到本机
	 * @description:  方法一、获取邮件有压缩格式，包括附件
	 * @return
	 * @throws Exception
	 */
	private ZipOutputStream zos ;
	@SuppressWarnings("unchecked")
	public String exportMail() throws Exception{
		String str = getRequest().getParameter("mailId");
		String realPath = PathUtil.getRootPath().concat("uploadfile");
		File parentFile = new File(realPath , user.getCurrentUser().getUserId()+"/");//可处理用户并发操作
		parentFile.mkdir();//创建父目录
		String mailIdArr[] = str.split(",");
		String mailId = "";
		File f = new File("");
		int h = 0;//用于重复标题时区分
		for(int i=0;i<mailIdArr.length;i++){
			mailId = mailIdArr[i];
			model = manager.getMessageById(mailId);//获取邮件对象
			List attList = manager.getAttachMent(mailId);//获取附件
			String msgTitle = model.getMsgTitle();
			msgTitle = msgTitle.replaceAll("\\:", "：");
			msgTitle = msgTitle.replaceAll("\\*", "×");
			msgTitle = msgTitle.replaceAll("\\?", "？");
			msgTitle = msgTitle.replaceAll("\\\"", "“");
			msgTitle = msgTitle.replaceAll("\\/", "、");
			msgTitle = msgTitle.replaceAll("\\\\", "＼");
			msgTitle = msgTitle.replaceAll("\\|", "｜");
			msgTitle = URLDecoder.decode(msgTitle, "utf-8");
			String mailName = msgTitle.concat(".htm");//获取邮件标题+扩展名
			String msgCon = model.getMsgContent();
			if(mailName.indexOf("自由流工作提醒：")!=-1){
				msgCon = msgCon.replaceAll("\\:", "：");
				msgCon = msgCon.replaceAll("\\*", "×");
				msgCon = msgCon.replaceAll("\\?", "？");
				msgCon = msgCon.replaceAll("\\\"", "“");
				msgCon = msgCon.replaceAll("\\/", "、");
				msgCon = msgCon.replaceAll("\\\\", "＼");
				msgCon = msgCon.replaceAll("\\|", "｜");
				msgCon = URLDecoder.decode(msgCon, "utf-8");
				mailName = model.getMsgContent().concat(".htm");//获取邮件标题+扩展名;
			}
			File[] ff = parentFile.listFiles();
			if (ff.length==0) {
				if(mailName.indexOf("自由流工作提醒：")!=-1){
					f = new File(parentFile, msgCon);
				}else{
					f = new File(parentFile, msgTitle);
				}
			} else {
				List listName = new ArrayList();
				for (int c = 0; c < ff.length; c++) {
					String ffName = ff[c].getName();
					listName.add(ffName);
				}
				if(mailName.indexOf("自由流工作提醒：")!=-1){
					if (listName.contains(msgCon)) {//如果匹配相同文件名，在后+1
						h++;
						f = new File(parentFile, msgCon.concat("(" + h + ")"));
					} else {
						f = new File(parentFile, msgCon);
					}

				}else{
					if (listName.contains(msgTitle)) {//如果匹配相同文件名，在后+1
						h++;
						f = new File(parentFile, msgTitle.concat("(" + h + ")"));
					} else {
						f = new File(parentFile, msgTitle);
					}
				}
			}
			f.mkdirs();
			File f1 = new File(f , mailName);//创建文件
			File f2 = null;
			f1.createNewFile();
			String from = manager.getUserNameInFormat(model.getMsgSender());
			String to = manager.getUserNameInFormat(model.getMsgReceiver());
			OutputStream os1 = new FileOutputStream(f1);
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
			sb.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"><HTML>");
			sb.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\" /></head><BODY>");
			
			sb.append("日期："+sdf.format(model.getMsgDate())+"<br />");
			sb.append("发件人："+from+"<br />");
			sb.append("收件人："+to+"<br />");
			sb.append("标题："+model.getMsgTitle()+"<br />");
			sb.append("内容："+HtmlUtils.htmlUnescape(model.getMsgContent()));

			sb.append(" </BODY></HTML>");
			
			os1.write(sb.toString().getBytes());
			
			ToaAttachment attModel = new ToaAttachment();
			if(!attList.equals("")){			//当存在附件时 执行以下操作
				for(int j=0;j<attList.size();j++){
					attModel = (ToaAttachment) attList.get(j);
					f2 = new File(f , attModel.getAttachName());
					f2.createNewFile();
					OutputStream os2 = new FileOutputStream(f2);
					try{
						os2.write(attModel.getAttachCon()); // 将附件写入流
					}catch(Exception e){
						e.printStackTrace();
						if(e.toString().indexOf("NullPointerException")!=-1){
							StringBuffer returnhtml = new StringBuffer(
							"<script>location ='").append(
							//getRequest().getContextPath()).append("/fileNameRedirectAction.action?toPage=message/info.jsp").append("';</SCRIPT>");
							getRequest().getContextPath()).append("/message/message!quicklyview.action?forward=quicklyview&msgId=")
							.append(str).append("';alert(\'导出失败，附件未找到，请检查附件是否在此服务器上，或联系管理员！\');</SCRIPT>");
							return renderHtml(returnhtml.toString());
						}else{
							StringBuffer returnhtml = new StringBuffer(
							"<script>location ='").append(
							getRequest().getContextPath()).append("/message/message!quicklyview.action?forward=quicklyview&msgId=")
							.append(str).append("';alert(\'导出失败，请联系管理员！\');</SCRIPT>");
							return renderHtml(returnhtml.toString());
						}
					}
					os2.flush();
					os2.close();
				}
			}
			os1.flush();
			os1.close();
		}
		//处理文件名
		String fileName = "信件导出".replaceAll("\\[.+\\]", "");
        fileName = java.net.URLEncoder.encode(fileName, "utf-8");
        fileName = fileName.replace('+', ' ').concat(".zip");

		HttpServletResponse response = super.getResponse();
		OutputStream res = getResponse().getOutputStream();
		response.reset(); //清空输出流 
		response.setHeader("Content-disposition","attachment; filename="+fileName);//设定输出文件头 
		response.setContentType("application/x-download");  
        zos = new ZipOutputStream(res);
        zos.setEncoding("gbk");
		downloadZip(parentFile);	//执行压缩文件操作
		zos.flush();
		zos.close();
		res.flush();
		res.close();
        
		deleteFile(parentFile);		//删除服务器记录
        return null;
	}
	private void downloadZip(File inputFile) throws Exception {
		zip(zos, inputFile, "");
	}
	//循环遍历压缩文件
	private void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}
	
	private void deleteFile(File delFile) {
		logger.debug("删除服务器过期文件执行开始...");
		if (delFile.exists()) { // 判断文件是否存在
			logger.debug("文件存在");
			if (delFile.isFile()) { // 判断是否是文件
				delFile.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (delFile.isDirectory()) { // 否则如果它是一个目录
				File files[] = delFile.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					delFile.delete();
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			delFile.delete();
		} else {
			System.out.println("所删除的文件不存在！" + '\n');
		}
	}
	
	/**
	 * @author 张磊
	 * @description:  批量下载邮件到本机
	 * @description:  方法二、直接获取eml格式文件
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String exportMails() throws Exception{
		String str = getRequest().getParameter("mailId");
		String realPath = PathUtil.getRootPath().concat("uploadfile");
		File parentFile = new File(realPath , user.getCurrentUser().getUserId()+"/");
		parentFile.mkdirs();//创建父目录
		String mailIdArr[] = str.split(",");
		String mailId = "";
		File f = new File("");
		int h = 0;//用于重复标题时区分
		for(int i=0;i<mailIdArr.length;i++){
			mailId = mailIdArr[i];
			model = manager.getMessageById(mailId);//获取邮件对象
			List attList = manager.getAttachMent(mailId);//获取附件
			String msgTitle = model.getMsgTitle();
			msgTitle = msgTitle.replaceAll("\\:", "：");
			msgTitle = msgTitle.replaceAll("\\*", "×");
			msgTitle = msgTitle.replaceAll("\\?", "？");
			msgTitle = msgTitle.replaceAll("\\\"", "“");
			msgTitle = msgTitle.replaceAll("\\/", "、");
			msgTitle = msgTitle.replaceAll("\\\\", "＼");
			msgTitle = msgTitle.replaceAll("\\|", "｜");
			msgTitle = msgTitle.replaceAll("%", "%25");
			msgTitle = URLDecoder.decode(msgTitle, "utf-8");
			String mailName = msgTitle.concat(".eml");//获取邮件标题+扩展名，用于验证
			File[] ff = parentFile.listFiles();
			if (ff.length==0) {
				f = new File(parentFile, mailName);
			} else {
				List listName = new ArrayList();
				for (int c = 0; c < ff.length; c++) {
					String ffName = ff[c].getName();
					listName.add(ffName);
				}
				if (listName.contains(mailName)) {//如果匹配相同文件名，在后+1
					h++;
					f = new File(parentFile, msgTitle.concat("(" + h + ").eml"));
				} else {
					f = new File(parentFile, mailName);
				}
			}
			f.createNewFile();
			OutputStream os = new FileOutputStream(f);
			String from = manager.getUserNameInFormat(model.getMsgSender());
			String to = manager.getUserNameInFormat(model.getMsgReceiver());
			Session session = Session.getDefaultInstance(new Properties());
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(MimeUtility.encodeWord(from)));//设置发件人
			//设置收件人 当发送至多人时
			String[] toName = to.split(",");
			List strList = new ArrayList();// 不能使用string类型的类型，这样只能发送一个收件人
			for (int n = 0; n < toName.length; n++) {
				if(null!=toName[n] && !"".equals(toName[n])){
					strList.add(new InternetAddress(MimeUtility.encodeWord(toName[n])));
				}
			}
			InternetAddress[] recAddress = (InternetAddress[]) strList
					.toArray(new InternetAddress[strList.size()]);
			msg.setRecipients(Message.RecipientType.TO, recAddress);
			msg.setSubject(model.getMsgTitle());//设置标题
			msg.setSentDate(model.getMsgDate());//设置时间
			Multipart mm=new MimeMultipart();//新建一个MimeMultipart对象用来存放多个BodyPart对象
			BodyPart mdp=new MimeBodyPart();//新建一个存放附件的BodyPart
			boolean temp = false;
			if(model.getMsgContent()!=null){
				mdp.setContent(HtmlUtils.htmlUnescape(model.getMsgContent()),"text/html;charset=utf-8");//设置内容
				mm.addBodyPart(mdp);
				temp = true;
			}
			
			ToaAttachment attModel = new ToaAttachment();
			if(!attList.equals("")){			//当存在附件时 执行以下操作
				for(int j=0;j<attList.size();j++){
					temp = true;
					attModel = (ToaAttachment) attList.get(j);
					String attFile = realPath+"/"+attModel.getAttachId()+"."+attModel.getAttachType();
					FileDataSource fds = new FileDataSource(attFile);
					DataHandler dh=new DataHandler(fds);//存放的附件
					mdp=new MimeBodyPart();
					mdp.setFileName(MimeUtility.encodeWord(attModel.getAttachName()));
					mdp.setDataHandler(dh);
					mm.addBodyPart(mdp);
				}
			}
			if(temp){
				msg.setContent(mm);
			}else{
				msg.setText("");
			}
			msg.saveChanges();
			try{
				msg.writeTo(os);//写入流
			}catch(Exception e){
				e.printStackTrace();
				if(e.toString().indexOf("系统找不到指定的文件")!=-1){
					StringBuffer returnhtml = new StringBuffer(
					"<script>location ='").append(
					getRequest().getContextPath()).append("/message/message!quicklyview.action?forward=quicklyview&msgId=")
					.append(str).append("';alert(\'导出失败，附件未找到，请检查附件是否在此服务器上，或联系管理员！\');</SCRIPT>");
					return renderHtml(returnhtml.toString());
				}else{
					StringBuffer returnhtml = new StringBuffer(
					"<script>location ='").append(
					getRequest().getContextPath()).append("/message/message!quicklyview.action?forward=quicklyview&msgId=")
					.append(str).append("';alert(\'导出失败，请联系管理员！\');</SCRIPT>");
					return renderHtml(returnhtml.toString());
				}
			}
			os.flush();
			os.close();
		}
		UUID randomUUID = UUID.randomUUID();
		String UUIDFilePath = realPath+File.separator+randomUUID+".zip";
		File randomUUIDFile = new File(UUIDFilePath);
		FileOutputStream outFileStream = new FileOutputStream(randomUUIDFile);
		try {
			ZipOutputStream zos = new ZipOutputStream(outFileStream);
	        zos.setEncoding("gbk");
	        zip(zos, parentFile, "");
	        zos.close();
	        outFileStream.close();
			
	        
	        //处理文件名
	        String fileName = "信件导出".replaceAll("\\[.+\\]", "");
	        fileName = java.net.URLEncoder.encode(fileName, "utf-8");
	        fileName = fileName.replace('+', ' ').concat(".zip");
			
			HttpServletResponse response = getResponse();
			response.reset();
			response.setContentType("application/x-download"); // windows
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			OutputStream output = null;
			FileInputStream fin = new FileInputStream(randomUUIDFile);
			try {
				 output = response.getOutputStream();
				 byte[] b = new byte[512];
					int read = 0;
					while ((read = fin.read(b)) != -1) {
						output.write(b, 0, read);
					}
				 output.flush();
			} catch (Exception e) {
				//有些浏览器会中断下载操作，提示用户是否下载，用户确认下载之后，再发送一遍请求，来下载文件
				e.printStackTrace();
			}
			if (output != null) {
				try {
					fin.close();
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					fin.close();
					output.close();
				}
				output = null;
			}
	        
	    
//	        
//	        HttpServletResponse response = getResponse();
//	        FileInputStream fin = new FileInputStream(randomUUIDFile);
//	        OutputStream output = response.getOutputStream();
//	        response.reset(); //清空输出流 
//			response.setHeader("Content-disposition","attachment; filename="+fileName);//设定输出文件头 
//			response.setContentType("application/x-download"); 
//	        
//			output.write(FileUtil.inputstream2ByteArray(fin));
//	        
//		    // 将写入到客户端的内存的数据,刷新到磁盘
//		   // output.flush();
//		    output.close();
//		    fin.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			 deleteFile(parentFile);		//删除服务器记录
			 deleteFile(randomUUIDFile);//删除临时文件
		}
        return null;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String msgTitle="%";
//		String msgTitle="FW：~!@#$%^&×()_-+=｜＼{}[]：;";
//		String msgTitle="FW：~!@#$%^&×()_-+=｜＼{}[]：;";
		msgTitle= URLDecoder.decode(msgTitle, "utf-8");
	}
	public String getDisLogo() {
		return disLogo;
	}

	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getShowReceiver() {
		return showReceiver;
	}

	public void setSendid(String sendid) {
		this.sendid = sendid;
	}

	public void setQuicklyre(String quicklyre) {
		this.quicklyre = quicklyre;
	}

	public void setCsinput(String csinput) {
		this.csinput = csinput;
	}

	public void setSfxyhz(String sfxyhz) {
		this.sfxyhz = sfxyhz;
	}

	public void setSfhz(String sfhz) {
		this.sfhz = sfhz;
	}

	public String getFolderId() {
		return folderId;
	}

	public List<ToaMessageFolder> getFolderList() {
		return folderList;
	}

	public String getFolderName() {
		return folderName;
	}

	public String getSfxyhz() {
		return sfxyhz;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
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

	public String getSearchTitle() {
		return searchTitle;
	}

	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}

	public List getAttachMentList() {
		return attachMentList;
	}

	public void setAttachMentList(List attachMentList) {
		this.attachMentList = attachMentList;
	}

	public String[] getDbobj() {
		return dbobj;
	}

	public void setDbobj(String[] dbobj) {
		this.dbobj = dbobj;
	}
	
	@Autowired
	public void setManage(IPhraseFilterManage filterManager) {
		this.filterManager = filterManager;
	}

	public String getMsgReceiverIds() {
		return msgReceiverIds;
	}

	public void setMsgReceiverIds(String msgReceiverIds) {
		this.msgReceiverIds = msgReceiverIds;
	}

	public String getMsgReceiverNames() {
		return msgReceiverNames;
	}

	public void setMsgReceiverNames(String msgReceiverNames) {
		this.msgReceiverNames = msgReceiverNames;
	}

	public void setDxhf(String dxhf) {
		this.dxhf = dxhf;
	}

	public void setIsclose(String isclose) {
		this.isclose = isclose;
	}

	public String getIsclose() {
		return isclose;
	}

	public boolean isHasSendRight() {
		return hasSendRight;
	}

	public void setHasSendRight(boolean hasSendRight) {
		String userId = user.getCurrentUser().getUserId();
		this.hasSendRight = smscontrolService.hasSendRight(userId);
	}

	public List getMeans() {
		return means;
	}

	public void setMeans(List means) {
		this.means = means;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public ToaBussinessModulePara getModulePara() {
		return modulePara;
	}

	public void setModulePara(ToaBussinessModulePara modulePara) {
		this.modulePara = modulePara;
	}

	public String getOwnerNum() {
		return ownerNum;
	}

	public void setOwnerNum(String ownerNum) {
		this.ownerNum = ownerNum;
	}

	public String getSmsCon() {
		return smsCon;
	}

	public void setSmsCon(String smsCon) {
		this.smsCon = smsCon;
	}

	public Date getSmsSendDate() {
		return smsSendDate;
	}

	public void setSmsSendDate(Date smsSendDate) {
		this.smsSendDate = smsSendDate;
	}

	public String getDxhf() {
		return dxhf;
	}

	public long getBaseMsgSize() {
		return BaseMsgSize;
	}

	public long getUsedMsgSize() {
		return usedMsgSize;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	public String getReadstate() {
		return readstate;
	}

	public void setReadstate(String readstate) {
		this.readstate = readstate;
	}

}
