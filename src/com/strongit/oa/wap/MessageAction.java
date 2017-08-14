/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息 action跳转类
 */
package com.strongit.oa.wap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.attachment.AttachmentHelper;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaMessage;
import com.strongit.oa.bo.ToaMessageFolder;
import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.desktop.DesktopSectionManager;
import com.strongit.oa.infopub.articles.ArticlesManager;
import com.strongit.oa.message.MessageFolderManager;
import com.strongit.oa.message.MessageManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.wap.util.Row;
import com.strongit.oa.wap.util.Status;
import com.strongit.oa.wap.util.Data;
import com.strongit.oa.wap.util.Form;
import com.strongit.oa.wap.util.Item;
import com.strongit.oa.smscontrol.IsmscontrolService;
import com.strongit.oa.updatebadwords.phrasefilter.IPhraseFilterManage;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.wap.manager.WorkForWapManager;
import com.strongit.oa.wap.service.WorkflowForWapService;
import com.strongit.oa.work.WorkManager;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "message.action", type = ServletActionRedirectResult.class) })
public class MessageAction extends BaseActionSupport {

	private Page<ToaMessage> page = new Page<ToaMessage>(FlexTableTag.MAX_ROWS, true);
	private Page<ToaInfopublishColumnArticl> pageToaInfo = new Page<ToaInfopublishColumnArticl>(
			FlexTableTag.MAX_ROWS, true);
	private int currentPage;
	private String waporgId;
	private String isAllUser;	//wap开发添加
	
	private String message;		//wap开发添加
	
	private String orgId;		//wap开发添加
	
	private String userName;	//wap开发添加
	
	private String msgId;
	@Autowired ArticlesManager articleManager;
	private ToaMessage model = new ToaMessage();
	private MessageManager manager;
	@Autowired WorkManager workmanager;  //工作流
	@Autowired
	private WorkflowForWapService workflowService;
	@Autowired
	private WorkForWapManager workForWapManager;
	private ArticlesManager articlesManager; //信息发布
	private String attachHtml;//返回给wapoa的附件列表
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
	
	private Date endDate;			//结束时间
	
	private List attachMentList;	//附件列表
	
	private String[] dbobj;					//已经存储在数据库中的附件转发中的
	
	private String isclose;
	
	private boolean hasSendRight;
	
	private Page<EForm> workpage = new Page<EForm>(FlexTableTag.MAX_ROWS, false);//分页对象,每页20条,支持自动获取总记录数
	private Page<ToaMessage> androidpage = new Page<ToaMessage>(7, true);
	
	private DesktopSectionManager sectionManager;
	
	private IPhraseFilterManage filterManager;
	
	@Autowired
	private IUserService userService;//注入统一用户服务
	
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
	
	public String androidindexpage(){
		HttpServletRequest request = getRequest();
		try{
			Page<ToaMessage> pagecount = new Page<ToaMessage>();
			pagecount = manager.getMsgByFolderName(pagecount, ToaMessageFolder.FOLDER_RECV,
					this.beginDate, this.endDate,null, searchTitle);
			
			String mailCount= "0";
			mailCount=msgForlderManager.getUnreadCount("recv", userService.getUserId());
			
			int messageCount=0;
			//消息总记录
			if(pagecount.getResult()!=null)
			    messageCount=pagecount.getResult().size();
			
			//信息发布的总记录
			//pageToaInfo=articlesManager.getAndroidColumnArticlPagesByProcess(pageToaInfo, null);
			
			int info=0;
			pageToaInfo=articleManager.getColumnArticleList(pageToaInfo,null);
			info=pageToaInfo==null?0:pageToaInfo.getTotalCount();
			/*if(pageToaInfo.getResult()!=null)
				info=pageToaInfo.getResult().size();*/
			
			//工作流代办总记录
			
			int workcount = workflowService.getTodoCount(userService.getCurrentUser().getUserId(), "2,3");//收发文类型,公文管理
			
			int todocount = workflowService.getTodoCount(userService.getCurrentUser().getUserId(), null);//所有在办待办
			int other=0;
			other=todocount-workcount;
			
			/*String s="<data>"+messageCount+","+info+","+other+","+workcount+"</data>";
			getRequest().setAttribute("recvlist", s);*/
			
			Data data = new Data();
			Form form=new Form();
			List<Item> lst=new ArrayList<Item>();
			Item item=new Item();
			item.setType("message");
			item.setValue(mailCount);
			lst.add(item);
			
			item=new Item();
			item.setType("info");
			item.setValue(Integer.toString(info));
			lst.add(item);
			
			item=new Item();
			item.setType("todo");
			item.setValue(Integer.toString(other));
			lst.add(item);
			
			item=new Item();
			item.setType("work");
			item.setValue(Integer.toString(workcount));
			lst.add(item);
			
			form.setItems(lst);
			data.setForm(form);
			Status statu = new Status("0","success");
			data.setStatus(statu);
			request.setAttribute("result", Data.GenerateJsonFormData(data));
		}catch(Exception ex){
			String msg = ex.getMessage();
		    Status status = new Status("1",msg);
		    Data data = new Data();
		    data.setStatus(status);
		    request.setAttribute("result", Data.GenerateJsonFormData(data));
		}
		return "androidrecvlist";
	}
	
	public String androidrecvlist() {
		Data data=new Data();
		HttpServletRequest request = getRequest();
		try{
			currentPage=currentPage==0?1:currentPage;
			androidpage.setPageNo(currentPage);
			androidpage= manager.getMsgByFolderName(androidpage, ToaMessageFolder.FOLDER_RECV,
					this.beginDate, this.endDate,null, searchTitle);
			if(androidpage==null||androidpage.getResult().size()==0){
				currentPage=androidpage.getTotalPages();
				androidpage.setPageNo(currentPage);
				androidpage= manager.getMsgByFolderName(androidpage, ToaMessageFolder.FOLDER_RECV,
						this.beginDate, this.endDate,null, searchTitle);
			}
			Object[] objs= androidpage.getResult().toArray();
			List<Row> rows=new ArrayList<Row>();
			//msg.msgId,msg.msgTitle,msg.isRead,msg.msgDate,msg.msgSize,msg.msgSender,msg.msgPri,msg.isHasAttr,msg.msgReceiver " 
			for(int i=0;i<objs.length;i++){
				//Object[] obj2 = (Object[]) obj[i];
				//html.append("<data>"+obj2[0]+","+obj2[1]+","+obj2[2]+","+obj2[3]+","+obj2[4]+","+obj2[5]+"</data>");
				//Object[] obj=objs[i];
				Object[] obj=(Object[])objs[i];
				Row row=new Row();
				List<Item> items = new ArrayList<Item>();
				Item item=new Item();
				item.setType("msgId");
				item.setValue(obj[0].toString());
				items.add(item);
				
				item=new Item();
				item.setType("msgTitle");
				item.setValue(obj[1].toString());
				items.add(item);
				
				item=new Item();
				item.setType("msgDate");
				item.setValue(obj[3].toString());
				items.add(item);
				
				item=new Item();
				item.setType("msgSender");
				item.setValue(obj[5].toString());
				items.add(item);
				
				item=new Item();
				item.setType("isRead");
				item.setValue(obj[2].toString());
				items.add(item);
				
				row.setItems(items);
				rows.add(row);
			}
			data.setRows(rows);
			Status status=new Status("0","success");
			com.strongit.oa.wap.util.Page page=new com.strongit.oa.wap.util.Page();
			page.setTotalPages(androidpage.getTotalPages());
			page.setTotalCount(androidpage.getTotalCount());
			data.setStatus(status);
			data.setPage(page);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
			/*html.append("<pages>"+androidpage.getTotalPages()+"</pages>");System.out.println(html.toString());
			getRequest().setAttribute("recvlist", html.toString());*/
		}catch(Exception e){
			String err=e.getMessage();
			Status status=new Status("1",err);
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
		}
		return "androidrecvlist";
	}
	
	/**
	 * 
	 * 安卓获取内部消息附件列表
	 * 
	 * @author  hecj
	 * @date    2012-8-1 下午01:39:43
	 * @param
	 * @return
	 * @throws
	 */
	public String androidAttachList(){
		HttpServletRequest request = getRequest();
		HttpServletResponse response=getResponse();
		OutputStream outputStream=null;
		BufferedInputStream br=null;
		InputStream is =null;
		String rtn="";
		Data data=new Data();
		try {
			String root = request.getContextPath();
			
			List<Row> rows=new ArrayList<Row>();
			List<ToaAttachment> attList=manager.getAttachMent(msgId);
			if(attList != null && !attList.isEmpty()&&attList.size()>0) {
				for(ToaAttachment attach : attList) {
					if(attach.getAttachCon()!=null){
						is = FileUtil.ByteArray2InputStream(attach.getAttachCon());
						String fileName=attach.getAttachName();;
						String fileType=fileName.substring(fileName.indexOf(".")+1,fileName.length());
						String path = AttachmentHelper.saveFile(root, is, fileName);
						
						Row row=new Row();
						List<Item> items = new ArrayList<Item>();
						Item item=new Item();
						item.setType("attachId");
						item.setValue(attach.getAttachId());
						items.add(item);
						
						item=new Item();
						item.setType("filename");
						item.setValue(fileName);
						items.add(item);
						
						item=new Item();
						item.setType("filepath");
						item.setValue(path);
						items.add(item);
						
						row.setItems(items);
						rows.add(row);
						//rtn=rtn+attach.getDocattachid()+","+fileName+","+path+";";
					}
				}
			}
			data.setRows(rows);
			Status status=new Status("0","success");
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
			
		}catch(Exception e){
			logger.error("读取附件数据发生异常", e);
			String msg = e.getMessage();
			String err=e.getMessage();
			Status status=new Status("1",err);
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonRowData(data));
		}finally{
			try {
				if(outputStream!=null){
					outputStream.close();
				}
				if(br!=null){
					br.close();
				}
				if(is!=null){
					is.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "androidrecvlist";
	}
	
	public String androidview() throws Exception{
		HttpServletRequest request = getRequest();
		Data data = new Data();
		try{
			model = manager.getMessageById(msgId);
			showReceiver = model.getMsgReceiver(); // 获取收件人
			msgReceiverIds = manager.getUserIdsInFormat(model.getMsgReceiver());
			msgReceiverNames = manager.getUserNameInFormat(model.getMsgReceiver());
			if (model.getIsRead() == null || "".equals(model.getIsRead())
					|| "null".equals(model.getIsRead())
					|| "0".equals(model.getIsRead())) {
				model.setIsRead("1");
				manager.saveMessage(model);
			}
			String msgSenderIds=manager.getUserIdsInFormat(model.getMsgSender());
			String msgSenderNames=manager.getUserNameInFormat(model.getMsgSender());
			String str="<data>"+model.getMsgTitle()+"~"+model.getMsgDate()+"~"+msgSenderIds+"~"+msgSenderNames+"~"+msgReceiverIds+"~"+msgReceiverNames+"~"+model.getMsgContent()+"</data>";
			getRequest().setAttribute("recvlist", str);


			//只有收信需要回执
			if(model.getToaMessageFolder().getMsgFolderName()!=null&&model.getToaMessageFolder().getMsgFolderName().equals("收件箱")){
				ToaMessage sendMessage=manager.getMessageByParen(model.getParentMsgId());
				if(sendMessage!=null&&null==model.getIsReturnBack()){
					if(model.getMsgId()!=null){
						String needRe= sendMessage.getIsSendBack();//isSendBack :1是需要回复
						if("1".equals(needRe)&&!"1".equals(model.getIsReturnBack())){//isReturnBack:1是已回复了
							model.setIsReturnBack("1");
							//manager.saveMessage(model);
							manager.receipt(model);	
						}
					}
				}
			}
			
			
			
			Form form=new Form();
			List<Item> lst=new ArrayList<Item>();
			Item item=new Item();
			item.setType("msgtitle");
			item.setValue(model.getMsgTitle());
			lst.add(item);
			
			item=new Item();
			item.setType("msgdate");
			item.setValue(model.getMsgDate().toString());
			lst.add(item);
			
			item=new Item();
			item.setType("sendid");
			item.setValue(msgSenderIds);
			lst.add(item);
			
			item=new Item();
			item.setType("sender");
			item.setValue(msgSenderNames);
			lst.add(item);
			
			item=new Item();
			item.setType("receiveids");
			item.setValue(msgReceiverIds);
			lst.add(item);
			
			item=new Item();
			item.setType("receiver");
			item.setValue(msgReceiverNames);
			lst.add(item);
			
			String con=model.getMsgContent();
			if(con!=null&&!"".equals(con)){
				con=con.replace("&lt;p&gt;", "");
				con=con.replace("&lt;/p&gt;","");
			}
			item=new Item();
			item.setType("msgcontent");
			item.setValue(con);
			lst.add(item);
			
			form.setItems(lst);
			data.setForm(form);
			Status statu = new Status("0","success");
			data.setStatus(statu);
			request.setAttribute("result", Data.GenerateJsonFormData(data));
		}catch(Exception e){
			String err=e.getMessage();
			Status status=new Status("1",err);
			data.setStatus(status);
			request.setAttribute("result", Data.GenerateJsonFormData(data));
		}
		return "androidrecvlist";
	}
	
	public String androidSendMessage() throws Exception {
		try {
			/*String content = filterManager.filterPhrase(model.getMsgContent(),
					FILTER_PHRASE_ID);
			String title = filterManager.filterPhrase(model.getMsgTitle(),
					FILTER_PHRASE_ID);
			model.setMsgTitle(title);
			// model.setMsgContent(content);
			model.setMsgContent(HtmlUtils.htmlEscape(content));
			// 处理用户id格式
			String sender = manager.formatUserId(user.getCurrentUser()
					.getUserId());
			String recvUser = manager.formatUserId(msgReceiverIds);

			model.setMsgSender(sender);
			model.setMsgReceiver(recvUser);
			ToaMessage msg = manager.sendMessage(null, model, null, null, null);*/
			
			String content;
			String title;
			if (disLogo != null && disLogo.equals("reply")) { // 回复短消息
				content = filterManager.filterPhrase(model.getMsgContent(), FILTER_PHRASE_ID);
				title = filterManager.filterPhrase(model.getMsgTitle(),
						FILTER_PHRASE_ID);
				model.setMsgTitle(title);
				model.setMsgContent(HtmlUtils.htmlEscape(content));
				// 处理用户id格式
				String sender = manager.formatUserId(userService.getCurrentUser()
						.getUserId());
				String recvUser = manager.formatUserId(msgReceiverIds);

				model.setMsgSender(sender);
				model.setMsgReceiver(recvUser);
				manager.replyMessage(msgId, model, null, null);
			} else if (disLogo != null && disLogo.equals("forward")) { // 转发
				content = filterManager.filterPhrase(model.getMsgContent(), FILTER_PHRASE_ID);
				title = filterManager.filterPhrase(model.getMsgTitle(),
						FILTER_PHRASE_ID);
				model.setMsgTitle(title);
				model.setMsgContent(HtmlUtils.htmlEscape(content));

				// 处理用户id格式
				String sender = manager.formatUserId(userService.getCurrentUser()
						.getUserId());
				String recvUser = manager.formatUserId(msgReceiverIds);

				model.setMsgSender(sender);
				model.setMsgReceiver(recvUser);

				manager.sendMessage(msgId, model, null, null, null,null);
			} else { // 新建发送
				content = filterManager.filterPhrase(model.getMsgContent(),
						FILTER_PHRASE_ID);
				title = filterManager.filterPhrase(model.getMsgTitle(),
						FILTER_PHRASE_ID);
				model.setMsgTitle(title);
				model.setMsgContent(HtmlUtils.htmlEscape(content));
				// 处理用户id格式
				String sender = manager.formatUserId(userService.getCurrentUser()
						.getUserId());
				String recvUser = manager.formatUserId(msgReceiverIds);

				model.setMsgSender(sender);
				model.setMsgReceiver(recvUser);
				manager.sendMessage(null, model, null,
						null, null, null);

			
			}
		} catch (Exception e) {
		}
		return "androidsendmessage";
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
		String userId = userService.getCurrentUser().getUserId();
		this.hasSendRight = smscontrolService.hasSendRight(userId);
		
		BaseMsgSize = manager.getbaseMsgSize();
		usedMsgSize = manager.getMsgAttachmentSize(userId);
		return "write";
	}

	public String waprecvlist() {
		//folderList = manager.getMyFolderList();
		
		currentPage=currentPage==0?1:currentPage;
		androidpage.setPageNo(currentPage);
		if(searchTitle!=null&&!"".equals(searchTitle)){
			try {
				searchTitle=URLDecoder.decode(searchTitle,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		androidpage= manager.getMsgByFolderName(androidpage, ToaMessageFolder.FOLDER_RECV,
				this.beginDate, this.endDate,null, searchTitle);
		if(androidpage==null||androidpage.getResult().size()==0){
			currentPage=androidpage.getTotalPages();
			androidpage.setPageNo(currentPage);
			androidpage= manager.getMsgByFolderName(androidpage, ToaMessageFolder.FOLDER_RECV,
					this.beginDate, this.endDate,null, searchTitle);
		}
		
		return "waprecvlist";
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
			page = manager.getMsgByFolderName(page,ToaMessageFolder.FOLDER_RECV,this.beginDate,this.endDate,null,searchTitle);
			forward="recvlist";
		}else if("send".equals(folderId)){
			page = manager.getMsgByFolderName(page,ToaMessageFolder.FOLDER_SEND,this.beginDate,this.endDate,null,searchTitle);
			forward="sendlist";
		}else if("draft".equals(folderId)){
			page = manager.getMsgByFolderName(page,ToaMessageFolder.FOLDER_DRAFT,this.beginDate,this.endDate,null,searchTitle);
			forward="draftlist";
		}else if("rubbish".equals(folderId)){
			page = manager.getMsgByFolderName(page,ToaMessageFolder.FOLDER_RUBBISH,this.beginDate,this.endDate,null,searchTitle);
			forward="rubbishlist";
		}else{
			folderName = manager.getFolderByFolderId(folderId).getMsgFolderName();
			page = manager.getMsgByFolderId(page,folderId,this.beginDate,this.endDate,null,searchTitle);
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
		String callback = "true";// 返回页面后的操作
		//String forward = null; wap开发修改
		if(msgReceiverIds==null||"".equals(msgReceiverIds)){//wap开发添加
			message="对不起，请您选择收信人!";
			return forward;
		}
		if(model!=null){
			if("".equals(model.getMsgTitle())||model.getMsgTitle()==null){
				message="请输入标题！";
				return forward;
			}
			if("".equals(model.getMsgContent())||model.getMsgContent()==null){
				message="请输入内容！";
				return forward;
			}
		}
		message="发送信息失败！";		//wap开发添加
		if (file != null) {
			long temp = 0;
			long usedSize = manager.getMsgAttachmentSize(userService.getCurrentUser()
					.getUserId());
			long baseSize = manager.getbaseMsgSize();
			for (int i = 0; i < file.length; i++) {
				if (file[i].length() == 0) {
					callback = "empty";
					HttpServletResponse response = this.getResponse();
					PrintWriter out;
					try {
						out = response.getWriter();
						out.println("<script>parent.callback('" + callback
								+ "')</script>");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return forward;
				}
				temp += file[i].length();
			}
			if ((temp + usedSize) > baseSize) {
				callback = "attach";

				HttpServletResponse response = this.getResponse();
				PrintWriter out;
				try {
					out = response.getWriter();
					out.println("<script>parent.callback('" + callback
							+ "')</script>");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return forward;
			}
		}

		if ("alluser".equals(msgReceiverIds)||"1".equals(isAllUser)) {		//wap开发添加所有人的判断
			msgReceiverIds = manager.getAllUserId();
		}
		// 我的消息对应的过滤模块主键：402882271e1f6980011e1f6ce51a0001
		// 手机短信modleId：40280c981ec653cc011ec65b6db1000d
		// 个人邮件modleId：40280c981ec653cc011ec65718fd0004

		model.setIsSendBack(sfxyhz); // 设置是否需要回执
		String content;
		String title;
		if (disLogo != null && disLogo.equals("reply")) { // 回复短消息
			content = filterManager.filterPhrase(quicklyre, FILTER_PHRASE_ID);
			title = filterManager.filterPhrase(model.getMsgTitle(),
					FILTER_PHRASE_ID);
			model.setMsgTitle(title);
			model.setMsgContent(HtmlUtils.htmlEscape(content));
			// 处理用户id格式
			String sender = manager.formatUserId(userService.getCurrentUser()
					.getUserId());
			String recvUser = manager.formatUserId(msgReceiverIds);

			model.setMsgSender(sender);
			model.setMsgReceiver(recvUser);
			manager.replyMessage(msgId, model, file, fileFileName);
		} else if (disLogo != null && disLogo.equals("resend")) { // 转发
			content = filterManager.filterPhrase(quicklyre, FILTER_PHRASE_ID);
			title = filterManager.filterPhrase(model.getMsgTitle(),
					FILTER_PHRASE_ID);
			model.setMsgTitle(title);
			model.setMsgContent(HtmlUtils.htmlEscape(content));

			// 处理用户id格式
			String sender = manager.formatUserId(userService.getCurrentUser()
					.getUserId());
			String recvUser = manager.formatUserId(msgReceiverIds);

			model.setMsgSender(sender);
			model.setMsgReceiver(recvUser);

			manager.sendMessage(msgId, model, file, fileFileName, dbobj,
					new OALogInfo("我的消息-消息记录-『转发』：" + title));
		} else { // 新建发送
			content = filterManager.filterPhrase(model.getMsgContent(),
					FILTER_PHRASE_ID);
			title = filterManager.filterPhrase(model.getMsgTitle(),
					FILTER_PHRASE_ID);
			model.setMsgTitle(title);
			model.setMsgContent(HtmlUtils.htmlEscape(content));
			// 处理用户id格式
			String sender = manager.formatUserId(userService.getCurrentUser()
					.getUserId());
			String recvUser = manager.formatUserId(msgReceiverIds);

			model.setMsgSender(sender);
			model.setMsgReceiver(recvUser);
			ToaMessage msg = manager.sendMessage(null, model, file,
					fileFileName, dbobj, new OALogInfo("我的消息-消息记录-『发送』："
							+ title));

			// 发送手机短信 （ dxhf=1：需要发送短信）
			if ("1".equals(dxhf)) {
				callback = manager.sendSms(msg, msgReceiverIds, smsCon,
						ownerNum, means, smsSendDate, moduleCode);
			}
		}
		HttpServletResponse response = this.getResponse();
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println("<script>parent.callback('" + callback + "')</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		message="发送信息成功！";		//wap开发添加
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
			disLogo=model.getToaMessageFolder().getMsgFolderName();
			attachMentList=manager.getAttachMent(msgId);
			if(model.getIsRead()==null||"".equals(model.getIsRead())||"null".equals(model.getIsRead())||"0".equals(model.getIsRead())){
				model.setIsRead("1");
				manager.saveMessage(model);
			}
		}
		return forward;
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
			String userId = userService.getCurrentUser().getUserId();
			this.hasSendRight = smscontrolService.hasSendRight(userId);
			//通讯录下选用户发消息
			if(!"".equals(msgReceiverIds)&&null!=msgReceiverIds){
				
				msgReceiverNames = "";
				String[] recvIds = msgReceiverIds.split(",");
				for(int i = 0;i<recvIds.length;i++){
					msgReceiverNames += userService.getUserNameByUserId(recvIds[i])+",";
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
	  * @author：pengxq
	  * @time：2009-1-14下午08:41:05
	  * @desc: 查看信息详情
	  * @param
	  * @return 跳转到"write"/"view"
	 */
	public String wapview() {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		if(msgId!=null&&!"".equals(msgId)){//"view"查看
			model=manager.getMessageById(msgId);
			
			//只有收信需要回执
			if(model.getToaMessageFolder().getMsgFolderName()!=null&&model.getToaMessageFolder().getMsgFolderName().equals("收件箱")){
				ToaMessage sendMessage=manager.getMessageByParen(model.getParentMsgId());
				if(sendMessage!=null&&null==model.getIsReturnBack()){
					sfxyhz=sendMessage.getIsSendBack();		
				}else{
					sfxyhz="0";
				}
			}
			if("1".equals(sfxyhz)&&!"".equals(model.getMsgSender())&&model.getMsgSender()!=null&&"0".equals(model.getIsRead()) ){
				ToaMessage msg=new ToaMessage();
				msg.setMsgTitle("消息回执");
				msg.setMsgContent(HtmlUtils.htmlEscape("对方已查看了您发送的邮件！"));
				// 处理用户id格式
				String sender = manager.formatUserId(userService.getCurrentUser()
						.getUserId());
				String recvUser = model.getMsgSender();
				msg.setIsReturnBack("0");
				msg.setMsgSender(sender);
				msg.setMsgReceiver(recvUser);
				folderName="回复短信成功!";//提示内容
				
				manager.sendMessage(null, msg, null,
						null, null, null);
			}
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
			attachMentList=manager.getAttachMent(msgId);
			if(attachMentList != null && !attachMentList.isEmpty()&&attachMentList.size()>0) {
				HttpServletRequest request = getRequest();
				InputStream is=null;
				try{
					String root=request.getContextPath();
					attachHtml="";
					attachHtml=attachHtml+"<div><table>";
					for(int i=0;i<attachMentList.size();i++) {
						ToaAttachment attach=(ToaAttachment)attachMentList.get(i);
						attachHtml=attachHtml+"<tr><td style=\"height:45px; line-height:45px;border-bottom:0px\">";
						if(attach.getAttachCon()!=null){
							is = FileUtil.ByteArray2InputStream(attach.getAttachCon());
							String fileName=attach.getAttachName();;
							String fileType=fileName.substring(fileName.indexOf(".")+1,fileName.length());
							String path = AttachmentHelper.saveFile(root, is, fileName);
							attachHtml=attachHtml+"<a href=\""+ path +"\">"+fileName+"</a></br>";
							
							//rtn=rtn+attach.getDocattachid()+","+fileName+","+path+";";
						}
						attachHtml=attachHtml+"</td></tr>";
					}
					attachHtml=attachHtml+"</table></div>";
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try{
						if(is!=null){
							is.close();
						}
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}else{	//"write"写信
			model=new ToaMessage();
			String userId = userService.getCurrentUser().getUserId();
			this.hasSendRight = smscontrolService.hasSendRight(userId);
			//通讯录下选用户发消息
			if(!"".equals(msgReceiverIds)&&null!=msgReceiverIds){
				
				msgReceiverNames = "";
				
				String[] recvIds = msgReceiverIds.split(",");
				Set<String> newSet = new HashSet<String>();
				
				for(int i = 0;i<recvIds.length;i++){
					newSet.add(recvIds[i].trim());
				}
				recvIds=(String[])newSet.toArray(new String[0]);
				msgReceiverIds="";
				for(int i = 0;i<recvIds.length;i++){
					msgReceiverIds=msgReceiverIds+ recvIds[i].trim() + ",";
					msgReceiverNames += userService.getUserNameByUserId(recvIds[i].trim())+",";
				}
				if(!"".equals(msgReceiverNames)&&null!=msgReceiverNames){
					msgReceiverNames = msgReceiverNames.substring(0, msgReceiverNames.length()-1);
				}
				if(!"".equals(msgReceiverIds)&&null!=msgReceiverIds&&",".equals(msgReceiverIds.substring(msgReceiverIds.length()-1,msgReceiverIds.length()))){
					msgReceiverIds = msgReceiverIds.substring(0, msgReceiverIds.length()-1);
				}
			}
			
			//取出默认短信回复答案
			means = manager.getModulStateMean(moduleCode);
			//短信模块配置
			modulePara = manager.getModuleParaByCode(moduleCode);
			
			BaseMsgSize = manager.getbaseMsgSize();
			usedMsgSize = manager.getMsgAttachmentSize(userId);
		}
		if(model!=null&&model.getMsgContent()!=null&&!"".equals(model.getMsgContent())){
			String con= model.getMsgContent();
			model.setMsgContent(workForWapManager.changeSpecialWords(con));
		}
		return forward;
	}
	
	
	public String wapSelectPerson(){
//		通讯录下选用户发消息
		String temp="";
		if(!"".equals(msgReceiverIds)&&null!=msgReceiverIds){
			//receiverNames = "";
			msgReceiverIds=msgReceiverIds.replace(",,", ",");
			String[] recvIds = msgReceiverIds.split(",");
			
			if(msgReceiverNames==null){
				msgReceiverNames="";
			}
			for(int i = 0;i<recvIds.length;i++){
				temp+=","+recvIds[i].trim();
				msgReceiverNames += userService.getUserNameByUserId(recvIds[i].trim())+",";
			}
			if(!"".equals(msgReceiverNames)&&null!=msgReceiverNames){
				msgReceiverNames = msgReceiverNames.substring(0, msgReceiverNames.length()-1);
			}
			msgReceiverIds=temp.substring(1);
			if(orgId!=null&&!"".equals(orgId)){ //重用参数
				model = manager.getMessageById(orgId);
			}else{
				if(waporgId!=null&&!"".equals(waporgId)){
					model = manager.getMessageById(waporgId);
				}
			}
		}else{
			if(waporgId!=null&&!"".equals(waporgId)){
				model = manager.getMessageById(waporgId);
			}
		}
		if(model!=null&&model.getMsgContent()!=null&&!"".equals(model.getMsgContent())){
			String con= model.getMsgContent();
			model.setMsgContent(workForWapManager.changeSpecialWords(con));
		}
		return "wapnew";
	}
	
	public String wapSendMessage() throws Exception {
		String me="wapnew";
		try {
			String content;
			String title;
			if (disLogo != null && disLogo.equals("reply")) { // 回复短消息
				content = filterManager.filterPhrase(model.getMsgContent(), FILTER_PHRASE_ID);
				content=content.replace("\t","");
				content=content.replace("\r","");
				content=content.replace("\n","");
				content=content.trim();
				title = filterManager.filterPhrase(model.getMsgTitle(),
						FILTER_PHRASE_ID);
				
				if(!"".equals(msgReceiverIds)&&!"".equals(title)&&!"".equals(content)){
				model.setMsgTitle(title);
				model.setMsgContent(HtmlUtils.htmlEscape(content));
				// 处理用户id格式
				String sender = manager.formatUserId(userService.getCurrentUser()
						.getUserId());
				String recvUser = manager.formatUserId(msgReceiverIds);

				model.setMsgSender(sender);
				model.setMsgReceiver(recvUser);
				folderName="回复短信成功!";//提示内容
				me="wapalert";
				manager.replyMessage(msgId, model, null, null);
				}else{
					if(folderName==null||"null".equals(folderName)){
						folderName="";
					}
					if(msgReceiverIds ==null || "".equals(msgReceiverIds)){
						folderName+="<请选择人员!>";
					}else if(title==null|| "".equals(title)){
						folderName+="<请填写标题!>";
					}else if(content==null|| "".equals(content)){
						
						folderName+="<请填写消息内容!>";
					}
					me="wapwrongalert";
				}
			} else if (disLogo != null && disLogo.equals("forward")) { // 转发
				content = filterManager.filterPhrase(model.getMsgContent(), FILTER_PHRASE_ID);
				content=content.replace("\t","");
				content=content.replace("\r","");
				content=content.replace("\n","");
				content=content.trim();
				title = filterManager.filterPhrase(model.getMsgTitle(),
						FILTER_PHRASE_ID);
				
				if(!"".equals(msgReceiverIds)&&!"".equals(title)&&!"".equals(content)){
				model.setMsgTitle(title);
				model.setMsgContent(HtmlUtils.htmlEscape(content));

				// 处理用户id格式
				String sender = manager.formatUserId(userService.getCurrentUser()
						.getUserId());
				String recvUser = manager.formatUserId(msgReceiverIds);

				model.setMsgSender(sender);
				model.setMsgReceiver(recvUser);
				folderName="转发短信成功!";//提示内容
				me="wapalert";
				manager.sendMessage(msgId, model, null, null, null,null);
				}else{
					if(folderName==null||"null".equals(folderName)){
						folderName="";
					}
					if(msgReceiverIds ==null || "".equals(msgReceiverIds))
						folderName+="<请选择人员!>";
					else if(title==null|| "".equals(title))
						folderName+="<请填写标题!>";
					else if(content==null|| "".equals(content))
						folderName+="<请填写消息内容!>";
					me="wapwrongalert";
				}
			} else { // 新建发送
				content = filterManager.filterPhrase(model.getMsgContent(),
						FILTER_PHRASE_ID).trim();
				title = filterManager.filterPhrase(model.getMsgTitle(),
						FILTER_PHRASE_ID);
				if(content==null|| "".equals(content))
					getRequest().setAttribute("info1", "内容不能为空");
				if(title==null|| "".equals(title))	
					getRequest().setAttribute("info2", "标题不能为空");
				if(msgReceiverIds ==null || "".equals(msgReceiverIds))
					getRequest().setAttribute("info3", "请选择人员");
				if(!"".equals(msgReceiverIds)&&!"".equals(title)&&!"".equals(content)){
				model.setMsgTitle(title);
				model.setMsgContent(HtmlUtils.htmlEscape(content));
				// 处理用户id格式
				String sender = manager.formatUserId(userService.getCurrentUser()
						.getUserId());
				String recvUser = manager.formatUserId(msgReceiverIds);

				model.setMsgSender(sender);
				model.setMsgReceiver(recvUser);
				folderName="发送短信成功!";//提示内容
				me="wapalert";
				manager.sendMessage(null, model, null,
						null, null, null);

				}
			}
		} catch (Exception e) {
		}
		if(model.getMsgContent()!=null){
			String tmpCon=model.getMsgContent();
			tmpCon=tmpCon.trim();
		}
		return me;
	}
	
	public String wapnew(){
		if("reply".equals(forward) ){
			model = manager.getMessageById(msgId);
			msgReceiverIds = manager.getUserIdsInFormat(model.getMsgSender());
			msgReceiverNames = manager.getUserNameInFormat(model.getMsgSender());
		
		}else if("forward".equals(forward))
		{
			model = manager.getMessageById(msgId);
		}
		if(model.getMsgContent()!=null){
			String con=model.getMsgContent();
			int ifind=con.indexOf("<id:");
			if(ifind!=-1){
				String strarrow=con.substring(ifind+36,ifind+37);
				if(">".equals(strarrow)){
					con=con.substring(0,ifind)+con.substring(ifind+37,con.length());
					model.setMsgContent(con);
				}
			}
		}
		return "wapnew";
	}
	
	
	public String wapdelete()throws Exception{
		try{
		String[] id = msgId.split(",");
		String userId = userService.getCurrentUser().getUserId();
		ToaMessageFolder folder = msgForlderManager.getMsgFolderByName(
				ToaMessageFolder.FOLDER_RUBBISH, userId);
		for (int i = 0; i < id.length; i++) {
			ToaMessage msg = manager.getMessageById(id[i]);
			msg.setToaMessageFolder(folder);
			manager.saveMessage(msg, new OALogInfo("我的消息-消息记录-『删除』："
					+ msg.getMsgTitle()));
					folderName="消息转入垃圾箱成功";
		}
		}catch(Exception ex){
			folderName="消息转入垃圾箱失败";
		}
		return "wapalert";
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
		usedMsgSize = manager.getMsgAttachmentSize(userService.getCurrentUser().getUserId());
		
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
		usedMsgSize = manager.getMsgAttachmentSize(userService.getCurrentUser().getUserId());
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
//			检查附件
			if(file!=null){
				long temp = 0;
				long usedSize = manager.getMsgAttachmentSize(userService.getCurrentUser().getUserId());
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
			String sender = manager.formatUserId(userService.getCurrentUser().getUserId());
			String recvUser = manager.formatUserId(msgReceiverIds);
			model.setMsgSender(sender);
			model.setMsgReceiver(recvUser);
			
			model.setMsgContent(HtmlUtils.htmlEscape(model.getMsgContent()));
			manager.saveMessageToDraft(model, file, fileFileName,dbobj);
			
			if("0".equals(isclose)){
				callback = "close";
			}else{
				callback = "true";
			}
			out.println("<script>parent.aftersave('"+callback+"')</script>");
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
				String userId = userService.getCurrentUser().getUserId();
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
		
		
		String blockid = getRequest().getParameter("blockid");//获取blockid
		Map<String,String> map = sectionManager.getParam(blockid);//通过blockid获取映射对象
		String showNum = map.get("showNum");//显示条数
		String subLength = map.get("subLength");//主题长度
		String showCreator = map.get("showCreator");//是否显示作者
		String showDate = map.get("showDate");//是否显示日期
		
		int num = 0,length = 0;
		if(showNum!=null&&!"".equals(showNum)&&!"null".equals(showNum)){
			num = Integer.parseInt(showNum);
		}
		if(subLength!=null&&!"".equals(subLength)&&!"null".equals(subLength)){
			length = Integer.parseInt(subLength);
		}
		//链接
		StringBuffer link = new StringBuffer();
		link.append("javascript:window.parent.refreshWorkByTitle('").append(getRequest().getContextPath())
			.append("/fileNameRedirectAction.action?toPage=message/message.jsp")
			.append("', '内部邮件'")
			.append(");");
		
		
//		获取公告列表list
		String userId=userService.getCurrentUser().getUserId();
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
				innerHtml.append("<div class=\"linkdiv\" title=\"\">");
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
				.append("	<a href=\"#\" onclick=\"").append(clickTitle.toString()).append("\"> ").append(title).append("</a></span>");
				if("1".equals(showCreator))//如果设置为显示作者，则显示作者信息
					innerHtml.append("	<span class =\"linkgray\">").append(msg.getMsgSender()).append("</span>");
				if("1".equals(showDate))//如果设置为显示日期，则显示日期信息
					innerHtml.append("	<span class =\"linkgray10\">").append(st.format(msg.getMsgDate())).append("</span>");
				innerHtml.append("	</div>");
			}
		}
		
		innerHtml.append("<div align=\"right\" style=\"padding:2px；font-size:12px;\"><a href=\"#\" onclick=\"").append(link.toString()).append("\"> ").append("更多</a></div>");
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
		String userId = userService.getCurrentUser().getUserId();
		this.hasSendRight = smscontrolService.hasSendRight(userId);
		//通讯录下选用户发消息
		if(!"".equals(msgReceiverIds)&&null!=msgReceiverIds){
			
			msgReceiverNames = "";
			String[] recvIds = msgReceiverIds.split(",");
			for(int i = 0;i<recvIds.length;i++){
				msgReceiverNames += userService.getUserNameByUserId(recvIds[i])+",";
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
		String userId = userService.getCurrentUser().getUserId();
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

	public Page<ToaMessage> getAndroidpage() {
		return androidpage;
	}

	public void setAndroidpage(Page<ToaMessage> androidpage) {
		this.androidpage = androidpage;
	}

	public WorkManager getWorkmanager() {
		return workmanager;
	}

	public void setWorkmanager(WorkManager workmanager) {
		this.workmanager = workmanager;
	}

	public ArticlesManager getArticlesManager() {
		return articlesManager;
	}

	public void setArticlesManager(ArticlesManager articlesManager) {
		this.articlesManager = articlesManager;
	}

	public String getMessage() {
		return message;
	}

	public String getWaporgId() {
		return waporgId;
	}

	public void setWaporgId(String waporgId) {
		this.waporgId = waporgId;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getIsAllUser() {
		return isAllUser;
	}

	public void setIsAllUser(String isAllUser) {
		this.isAllUser = isAllUser;
	}

	public String getAttachHtml() {
		return attachHtml;
	}

	public void setAttachHtml(String attachHtml) {
		this.attachHtml = attachHtml;
	}
	

}
