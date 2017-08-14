/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息业务类
 */
package com.strongit.oa.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaMessage;
import com.strongit.oa.bo.ToaMessageAttach;
import com.strongit.oa.bo.ToaMessageFolder;
import com.strongit.oa.bo.ToaMsgStateMean;
import com.strongit.oa.bo.ToaSms;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.mymail.util.StringUtil;
import com.strongit.oa.mymail.util.StrongDate;
import com.strongit.oa.paramconfig.ConfigModule;
import com.strongit.oa.paramconfig.ParamConfigService;
import com.strongit.oa.sms.IsmsService;
import com.strongit.oa.sms.SmsManager;
import com.strongit.oa.smsplatform.ModelStateMeanManager;
import com.strongit.oa.smsplatform.SmsPlatformManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.oa.webservice.iphone.server.pushNotify.PushNotifyManager;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 处理消息类
 * @Create Date: 2009-12-7
 * @author luosy
 * @version 1.0
 */
@Service
@Transactional
@OALogger
public class MessageManager {
	
	private GenericDAOHibernate<ToaMessage, java.lang.String> messageDao;
	
	private IUserService userService;
	
	//消息文件夹manager类
	private MessageFolderManager msgForlderManager;

	//消息附件manager类
	private MessageAttManager attachManager;

	//公共附件接口类
	private IAttachmentService attachmentService;

	//手机短信发送接口类
	private IsmsService smsService;

	//	手机短信manager类
	private SmsManager smsManager;
	
	//手机回复意义manager类
	private ModelStateMeanManager stateMeanManager;
	
	//手机短信平台manager类
	private SmsPlatformManager smsPlatformManager;

	//手机短信回复意义manager类
	private MessageStateMeanManager msgStateMeanManager;
	//推送给ios的manager类
	@Autowired
	private PushNotifyManager pushManager;
	
	private ParamConfigService pcService;

	@Autowired
	public void setPcService(ParamConfigService pcService) {
		this.pcService = pcService;
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setMsgForlderManager(MessageFolderManager msgForlderManager) {
		this.msgForlderManager = msgForlderManager;
	}
	@Autowired
	public void setAttachManager(MessageAttManager attachManager) {
		this.attachManager = attachManager;
	}
	@Autowired
	public void setAttachmentService(IAttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
	
	@Autowired
	public void setSmsService(IsmsService smsService) {
		this.smsService = smsService;
	}
	
	@Autowired
	public void setSmsManager(SmsManager smsManager) {
		this.smsManager = smsManager;
	}

	@Autowired
	public void setStateMeanManager(ModelStateMeanManager stateMeanManager) {
		this.stateMeanManager = stateMeanManager;
	}
	
	@Autowired
	public void setSmsPlatformManager(SmsPlatformManager smsPlatformManager) {
		this.smsPlatformManager = smsPlatformManager;
	}
	
	@Autowired
	public void setMsgStateMeanManager(MessageStateMeanManager msgStateMeanManager) {
		this.msgStateMeanManager = msgStateMeanManager;
	}
	
	public MessageManager() {

	}

	/**
	 * author:luosy
	 * description: 设置messageDao
	 * modifyer:
	 * description:
	 * @param session
	 */
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		messageDao = new GenericDAOHibernate<ToaMessage, java.lang.String>(
				session, ToaMessage.class);
	}
	
	/**
	 * author:luosy
	 * description: 保存消息记录的附件
	 * modifyer:
	 * description:
	 * @param msglist 消息记录的list(发送者的记录+接收者的记录)
	 * @param file		附件
	 * @param fileFileName	附件名
	 * @return 		上传成功(true)
	 */
	public boolean SaveMessageAtt(List<ToaMessage> msglist,File[] file,String[] fileFileName )throws SystemException,ServiceException{
		try{
//		保存附件
			if(file!=null){
				Calendar cals = Calendar.getInstance();
				for(int i=0;i<file.length;i++){
					FileInputStream fils = null;
					try{
						fils = new FileInputStream(file[i]);
						byte[] buf = new byte[(int)file[i].length()];
						fils.read(buf);	
						String ext = fileFileName[i].substring(fileFileName[i].lastIndexOf(".") + 1,
								fileFileName[i].length());
						//添加公共附件表数据
						String attachId = attachmentService.saveAttachment(fileFileName[i], buf, cals.getTime(), ext, "1", "注:消息附件", "");
						//添加消息附件表数据
						for(int k=0;k<msglist.size();k++){
							attachManager.saveAttach(attachId, msglist.get(k));
						}
					}catch (Exception e) {
						throw new ServiceException(MessagesConst.save_error,               
								new Object[] {"消息附件上传失败"});
					}finally{
						try {
							fils.close();
						} catch (IOException e) {
							throw new ServiceException(MessagesConst.save_error,               
									new Object[] {"消息附件上传失败"});
						}
					}
				}
			}
			return true;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"消息记录"});
		}
	}
	
	/**
	 * author:luosy
	 * description:  获取文件夹对象
	 * modifyer:
	 * description:
	 * @param folderId 文件夹ID
	 * @return
	 */
	public ToaMessageFolder getFolderByFolderId(String folderId)throws SystemException,ServiceException{
		try{
			return msgForlderManager.getMsgFolderById(folderId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"文件夹列表"});
		}
	}
	/**
	 * author:luosy
	 * description:获取某个文件夹下的消息列表
	 * modifyer:
	 * description:
	 * @param page
	 * @param FolderId 文件夹的ID
	 * @param beginDate  
	 * @param endDate
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Page<ToaMessage> getMsgByFolderId(Page<ToaMessage> page,String FolderId,Date beginDate,Date endDate,String readstate,String name)throws SystemException,ServiceException{
		try{
			Object[] obj=new Object[6];
			StringBuffer sql = new StringBuffer();
			sql.append(" select msg.msgId,msg.msgTitle,msg.isRead,msg.msgDate,msg.msgSize,msg.msgSender,msg.msgPri,msg.isHasAttr,msg.msgReceiver " +
					"from ToaMessage as msg where msg.msgIsdel=? and msg.toaMessageFolder.msgFolderId=? ");
			
			obj[0]=ToaMessage.MSG_NOTDEL;
			obj[1]=FolderId;
			int i=2;
			if (!"".equals(name) && name != null) {
				if(name.indexOf("%")!=-1){
					sql.append(" and msg.msgTitle like ? ");
					name=name.replaceAll("%", "/%");
					obj[i]="%" + name+ "%";
					sql.append(" ESCAPE '/' ");
					i++;
				}else{
				sql.append(" and msg.msgTitle like ? ");
				obj[i]="%"+name+"%";
				i++;
				}
			}else {
				sql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			if(beginDate!=null){
				sql.append(" and msg.msgDate>=? ");
				obj[i]=beginDate;
				i++;
			}else {
				sql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			if(endDate!=null){
				sql.append(" and msg.msgDate<=? ");
				obj[i]=endDate;
				i++;
			}else {
				sql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			if(!"".equals(readstate) && readstate != null){
				sql.append(" and msg.isRead=? ");
				obj[i]=readstate;
				i++;
			}else {
				sql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
			page = messageDao.find(page, sql.append(" order by msg.msgDate desc ").toString(),obj);
			List list = page.getResult();
			List resList = new ArrayList();
			if(null!=list){
//				for(Object objArr:list){
//					Object[] objs = (Object[])objArr;
//					if(null==objs[5]){
//						objs[5]="";
//					}else{
//						objs[5] = this.getUserNameInFormat(objs[5].toString());
//					}
//					if(null==objs[8]){
//						objs[8]="";
//					}else{
//						objs[8] = this.getUserNameInFormat(objs[8].toString());;
//					}
//					resList.add(objs);
//				}
				for(int n=0;n<list.size();n++){
					Object[] objs = (Object[])list.get(n);
					if(null==objs[5]){
						objs[5]="";
					}else{
						objs[5] = this.getUserNameInFormat(objs[5].toString());
					}
					if(null==objs[8]){
						objs[8]="";
					}else{
						objs[8] = this.getUserNameInFormat(objs[8].toString());;
					}
					resList.add(objs);
					
				}
			}
			page.setResult(resList);
			return page;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"消息列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description:获取默认文件夹下的消息列表 
	 * modifyer:
	 * description:
	 * @param page
	 * @param FolderName  "收件箱"|"发件箱"|"草稿箱"|"垃圾箱"
	 * @param beginDate  搜索的开始时间
	 * @param endDate	搜索的结束时间
	 * @param name		搜索的消息标题
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<ToaMessage> getMsgByFolderName(Page<ToaMessage> page,String FolderName,Date beginDate,Date endDate,String readstate,String name)throws SystemException,ServiceException{
		try{
			String userId = userService.getCurrentUser().getUserId();
			String folderId = msgForlderManager.getMsgFolderByName(FolderName,userId).getMsgFolderId();
			return getMsgByFolderId(page,folderId, beginDate, endDate, readstate,name);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"消息列表"});
		}catch(Exception e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"消息列表"});
		}
	}

	/**
	 * author:luosy
	 * description: 获取所有自定义文件夹
	 * modifyer:
	 * description:
	 * @returns List
	 */
	@Transactional(readOnly=true)
	public List<ToaMessageFolder> getMyFolderList() throws SystemException,ServiceException{
		try{
			String userId = userService.getCurrentUser().getUserId();
			return msgForlderManager.getFolderListByUser(userId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"自定义文件夹列表"});
		}
	}
	
	
	/**
	 * author:luosy
	 * description: 移动消息至某文件夹
	 * modifyer:
	 * description:
	 * @param msgId 消息IDs （id1,id2,id3）
	 * @param folder 文件夹对象
	 * @return
	 */
	public boolean changeFolder(String msgId, ToaMessageFolder folder) throws SystemException,ServiceException{
		try{
			if(msgId.indexOf(",")!=-1){
				String[] id=msgId.split(",");
				try{
					for(int i=0;i<id.length;i++){
						ToaMessage msg=messageDao.get(id[i]);
						msg.setToaMessageFolder(folder);
						msg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
						messageDao.save(msg);
						messageDao.flush();
					}
				}catch(Exception e){
					return false;
				}
			}else{
				ToaMessage msg=messageDao.get(msgId);
				msg.setToaMessageFolder(folder);
				msg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
				messageDao.save(msg);
				messageDao.flush();
			}
			return true;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"移动消息"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 移动消息至垃圾箱
	 * modifyer:
	 * description:
	 * @param msgId 消息IDs （id1,id2,id3）
	 * @param folder 文件夹对象
	 * @return
	 */
	public boolean changeToRubbish(String msgId, OALogInfo ... loginfos)throws SystemException,ServiceException{
		try{
			String[] id=msgId.split(",");
			String userId = userService.getCurrentUser().getUserId();
			ToaMessageFolder folder = msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_RUBBISH,userId);
			for(int i=0;i<id.length;i++){
				ToaMessage msg=messageDao.get(id[i]);
				msg.setToaMessageFolder(folder);
				msg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
				messageDao.save(msg);
				messageDao.flush();
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"移动消息至垃圾箱"});
		}
		return true;
	}
	
	
	/**
	 * author:luosy
	 * description: 保存消息到草稿
	 * modifyer:
	 * description:
	 * @param msg  消息对象
	 * @param file 附件对象
	 * @param fileName 附件名
	 * @return
	 */
	public String saveMessageToDraft(ToaMessage msg,File file[],String[] fileFileName,String[] dbobj, OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			if(msg.getMsgTitle()==null|"".equals(msg.getMsgTitle())){
				msg.setMsgTitle("无标题");
			}
			String SenderId = userService.getCurrentUser().getUserId();
			List<ToaMessage> msglist = new ArrayList<ToaMessage>();
			msg.setMsgDate(new Date());
			msg.setIsRead("0");
			if(dbobj==null&&file==null){
				msg.setIsHasAttr("0");
			}else{
				msg.setIsHasAttr("1");
			}
			//文件夹
			msg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_DRAFT,SenderId));
			
			msg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
			//附件
			if(!"".equals(msg.getMsgId())&&null!=msg.getMsgId()){
				messageDao.delete(msg.getMsgId());
				msg.setMsgId(null);
				messageDao.save(msg);
			}else{
				messageDao.save(msg);
			}
			if(dbobj!=null){
				for(int i=0;i<dbobj.length;i++){
					if(dbobj[i].indexOf(";")<0){
						msg.setIsHasAttr("1");
						attachManager.saveAttach(dbobj[i], msg);
					}
				}
			}
			
			msglist.add(msg);
			if(msglist != null){
				SaveMessageAtt(msglist, file, fileFileName);
			}
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存消息到草稿"});
		}
		return msg.getMsgId();
	}
	
	/**
	 * author:luosy
	 * description: 发送消息
	 * modifyer:
	 * description:
	 * @param msg
	 * @return 发送消息的ID
	 */
	public ToaMessage sendMessage(String msgId,ToaMessage msg,File file[],String[] fileFileName,String[] dbobj, OALogInfo ... loginfos)throws SystemException,ServiceException{
		
		if(msg.getMsgTitle()==null||"".equals(msg.getMsgTitle())){	//标题为空时
			msg.setMsgTitle("无标题");
		}
		
		try{
			List<ToaMessage> msglist = new ArrayList<ToaMessage>();
			//添加 发送者记录 如果是系统发送则不保存发送者记录
			if(!"系统消息<id:system>".equals(msg.getMsgSender())){
				if(dbobj==null&&file==null){
					msg.setIsHasAttr("0");
				}else{
					msg.setIsHasAttr("1");
				}
				msg.setMsgId(null);
				String sender=msg.getMsgSender();
				msg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_SEND,sender.substring(sender.indexOf(":")+1, sender.indexOf(">"))));
				msg.setMsgDate(new Date());
				msg.setIsRead("0");
				msg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
				messageDao.save(msg);
				msglist.add(msg);
			}
	
				
			//添加 接受者记录     user1:6006800BBDF034DFE040007F01001D9E
			String receiver=msg.getMsgReceiver();	
			String temp[]=receiver.split(",");
			for(int i=0;i<temp.length;i++){
				ToaMessage recvMsg = new ToaMessage();
				if(dbobj==null&&file==null){
					recvMsg.setIsHasAttr("0");
				}else{
					recvMsg.setIsHasAttr("1");
				}
				recvMsg.setMsgSender(msg.getMsgSender());
				recvMsg.setMsgReceiver(msg.getMsgReceiver());
				recvMsg.setMsgCc(msg.getMsgCc());
				recvMsg.setParentMsgId( msg.getMsgId());	
				recvMsg.setMsgTitle(msg.getMsgTitle());
				recvMsg.setMsgContent(msg.getMsgContent());
				recvMsg.setMsgDate(msg.getMsgDate());
				recvMsg.setMsgType(msg.getMsgType());
				recvMsg.setIsRead("0");
				recvMsg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
				recvMsg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_RECV,temp[i].substring(temp[i].indexOf(":")+1, temp[i].indexOf(">"))));
				messageDao.save(recvMsg);
				msglist.add(recvMsg);
			}
			
			//保存附件
			if(msglist.size()>0){	//保存转发或则发送时添加的附件
				this.SaveMessageAtt(msglist, file, fileFileName);
			}
			
			if(dbobj!=null&&dbobj.length>0){	//保存转发的信息的附件
				this.saveOtherAttach(msglist,dbobj);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"添加消息记录"});
		}
		return msg;
	}
	
	/**
	 * author:luosy
	 * description:组装用户ID 
	 * modifyer:
	 * description:
	 * @param userIds    (userid1,userid2,……)
	 * @return			(userName1<id:userId1>,userName2<id:userId2>,……)
	 */
	public String formatUserId(String userIds){
		if(!"".equals(userIds)&&null!=userIds){
			String[] userid = userIds.split(",");
			StringBuffer formatedID = new StringBuffer();
			for(int i=0; i<userid.length;i++){
				formatedID.append(userService.getUserNameByUserId(userid[i].trim())+"<id:"+userid[i]+">,");
			}
			
			String reStr = formatedID.toString();
			reStr = reStr.substring(0, reStr.length()-1);
			
			return reStr;
		}else{
			return "";
		}
		
	}
	
	/**
	 * author:luosy
	 * description:格式化用户ID
	 * modifyer:
	 * description:
	 * @param FormatUserId  (userName1<id:userId1>,userName2<id:userId2>,……)
	 * @return				 (userid1,userid2,……)
	 */
	public String getUserIdsInFormat(String FormatUserId){
		if(!"".equals(FormatUserId)&&null!=FormatUserId){
			StringBuffer userIds = new StringBuffer();
			String[] FormatUserIds = FormatUserId.split(",");
			for(int i=0; i<FormatUserIds.length;i++){
				userIds.append(FormatUserIds[i].substring(FormatUserIds[i].indexOf(":")+1, FormatUserIds[i].indexOf(">"))+",");
			}
			String reStr = userIds.toString();
			reStr = reStr.substring(0, userIds.length()-1);
			
			return reStr;
		}else{
			return "";
		}
	}
	
	/**
	 * author:luosy
	 * description:格式化用户ID
	 * modifyer:
	 * description:
	 * @param FormatUserId  (userName1<id:userId1>,userName2<id:userId2>,……)
	 * @return				 (userName1,userName2,……)
	 */
	public String getUserNameInFormat(String FormatUserId){
		if(!"".equals(FormatUserId)&&null!=FormatUserId){
			StringBuffer userNames = new StringBuffer();
			String[] FormatUserIds = FormatUserId.split(",");
			for(int i=0; i<FormatUserIds.length;i++){
				userNames.append(FormatUserIds[i].substring(0, FormatUserIds[i].indexOf("<"))+",");
			}
			String reStr = userNames.toString();
			reStr = reStr.substring(0, userNames.length()-1);
			
			return reStr;
		}else{
			return "";
		}
	}
	
	/**
	 * author:luosy
	 * description:获取所有用户id
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 */
	public String getAllUserId()throws SystemException{
		StringBuffer sb = new StringBuffer();
		List<User> setUser = userService.getAllUserInfo();
		for(User user:setUser){
			sb.append(user.getUserId()).append(",");
		}
		String allUserIds = sb.toString().substring(0, sb.toString().length()-1);
		return allUserIds;
	}
	
	/**
	 * author:luosy
	 * description:发送消息外部接口实现
	 * modifyer:
	 * description:
	 * @param flag 发送标识 系统system/人员person
	 * @param receiver 接收者 List
	 * @param title	 消息标题
	 * @param content 消息内容
	 */
	public void sendMsg(String flag,List receiver,String title,String content,String msgType) throws SystemException,ServiceException{
		try{
			StringBuffer recvUser = new StringBuffer();
			String userId = "";
			
			//组装用户ID
			if(flag==null|"system".equals(flag)){//如果发送者ID为空 则显示为系统发送
				userId = "系统消息<id:system>";
			}else{
				userId = userService.getCurrentUser().getUserId();
				userId = ""+userService.getUserNameByUserId(userId)+"<id:"+userId+">";
			}
			
			for(int i=0;i<receiver.size();i++){
				String userName = userService.getUserNameByUserId(receiver.get(i).toString());
				if(null!=userName&&!"null".equals(userName)){
					recvUser.append(""+userName+"<id:"+receiver.get(i).toString()+">,");
				}
			}
			String strRecv = recvUser.toString();
			if(strRecv.length()<1){
				return;
			}
			strRecv = strRecv.substring(0, strRecv.length()-1);
			
			ToaMessage msg = new ToaMessage();
			msg.setMsgDate(new Date());
			msg.setMsgSender(userId);
			msg.setMsgReceiver(strRecv);
			msg.setMsgTitle(title);
			msg.setMsgContent(content);
			msg.setMsgType(msgType);
			
			this.sendMessage("", msg, null, null, null);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"消息发送接口"});
		}
	}

	/**
	 * author:luosy
	 * description: 删除消息对象
	 * modifyer:
	 * description:
	 * @param msgIds 需要被删除的消息的ID集合
	 * @return
	 */
	public boolean delMessageByMsgId(String msgIds, OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			String[] id=msgIds.split(",");
			for(int i=0;i<id.length;i++){
				ToaMessage msg = messageDao.get(id[i]);
//				msg.setMsgIsdel(ToaMessage.MSG_DEL);
//				messageDao.save(msg);
				this.delMessage(msg);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除消息"});
		}
		return true;
	}
	
	/**
	 * author:luosy
	 * description: 删除消息记录
	 * modifyer:
	 * description:
	 * @param msg
	 * @param loginfos
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void delMessage(ToaMessage msg, OALogInfo ... loginfos)throws SystemException,ServiceException{
		try{
//			msg.setMsgIsdel(ToaMessage.MSG_DEL);
//			messageDao.save(msg);
			Set atts = msg.getToaMessageAttaches();
			if(atts!=null){
				Iterator it=atts.iterator();
				while (it.hasNext()) {
					ToaMessageAttach objs = (ToaMessageAttach) it.next();
					attachManager.delAttach(objs.getAttachId7(), msg.getMsgId());
				}
			}
			messageDao.delete(msg);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除消息"});
		}
	}
	
	public String saveMessage(String msgId,ToaMessage msg, OALogInfo ... loginfos)throws SystemException,ServiceException{
		String returnStatus="0";
		if(msg.getMsgTitle()==null||"".equals(msg.getMsgTitle())){	//标题为空时
			msg.setMsgTitle("无标题");
		}
		
		try{
			
			//添加 发送者记录 如果是系统发送则不保存发送者记录
			if(!"系统消息<id:system>".equals(msg.getMsgSender())){
				//msg.setIsHasAttr("0");
				
				if(msgId!=null && !"".equals(msgId)){
					//不要修改原来的
					//msg.setMsgId(msgId);
				}
				String sender=msg.getMsgSender();
				msg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_SEND,sender.substring(sender.indexOf(":")+1, sender.indexOf(">"))));
				msg.setMsgDate(new Date());
				msg.setIsRead("0");
				msg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
				messageDao.save(msg);
			}
	
				
			//添加 接受者记录     user1:6006800BBDF034DFE040007F01001D9E
			String receiver=msg.getMsgReceiver();	
			String temp[]=receiver.split(",");
			String rcvUserId=null;
			for(int i=0;i<temp.length;i++){
				ToaMessage recvMsg = new ToaMessage();
				recvMsg.setIsHasAttr("0");
				recvMsg.setMsgSender(msg.getMsgSender());
				recvMsg.setMsgReceiver(msg.getMsgReceiver());
				recvMsg.setMsgCc(msg.getMsgCc());
				if(msgId!=null || "".equals(msgId)){
					recvMsg.setParentMsgId(msgId);
				}
				recvMsg.setMsgTitle(msg.getMsgTitle());
				recvMsg.setMsgContent(msg.getMsgContent());
				recvMsg.setMsgDate(msg.getMsgDate());
				recvMsg.setMsgType(msg.getMsgType());
				recvMsg.setIsRead("0");
				recvMsg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
				/**
				 * 新增操作时往推送设置里面新增记录或者修改推送数
				 */
				rcvUserId=temp[i].substring(temp[i].indexOf(":")+1, temp[i].indexOf(">"));
				if(pushManager.getPushState(rcvUserId,PushNotifyManager.PUSH_MODULE_NO_MESSAGE)){
					pushManager.saveNotity(rcvUserId,PushNotifyManager.PUSH_MODULE_NO_MESSAGE, 1);
				}
				recvMsg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_RECV,rcvUserId));
				messageDao.save(recvMsg);
				returnStatus="1";
			}
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"添加消息记录"});
		}
		return returnStatus;
	}
 
	/**
	 * 用于webservice调用
	 * @description
	 *
	 * @author  hecj
	 * @date    Apr 25, 2014 8:57:57 AM
	 * @param   
	 * @return  String
	 * @throws
	 */
	public String saveMessageExt(String msgId,ToaMessage msg,String isReply,OALogInfo ... loginfos)throws SystemException,ServiceException{
		String returnStatus="0";
		if(msg.getMsgTitle()==null||"".equals(msg.getMsgTitle())){	//标题为空时
			msg.setMsgTitle("无标题");
		}
		ToaMessage tmpMsg=null;
		if(!"".equals(msgId)){
			tmpMsg=getMessageById(msgId);
			//回复不显示附件
			if(!"1".equals(isReply)){
				msg.setIsHasAttr(tmpMsg.getIsHasAttr());
			}else{
				//如果是回复则不显示附件
				msg.setIsHasAttr("0");
			}
		}
		try{
			
			//添加 发送者记录 如果是系统发送则不保存发送者记录
			if(!"系统消息<id:system>".equals(msg.getMsgSender())){
				//msg.setIsHasAttr("0");
				
				if(msgId!=null && !"".equals(msgId)){
					//不要修改原来的
					//msg.setMsgId(msgId);
				}
				String sender=msg.getMsgSender();
				msg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_SEND,sender.substring(sender.indexOf(":")+1, sender.indexOf(">"))));
				msg.setMsgDate(new Date());
				msg.setIsRead("0");
				msg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
				messageDao.save(msg);
				/**
				 * 发送者的附件传给接收者
				 * 添加消息附件表数据
				 */
				if(!"1".equals(isReply)){
					if(tmpMsg!=null){
						Set attSet=tmpMsg.getToaMessageAttaches();
						if(attSet!=null&&attSet.size()>0){
							Iterator it=attSet.iterator();
							while(it.hasNext()){
								ToaMessageAttach att=(ToaMessageAttach)it.next();
								attachManager.saveAttach(att.getAttachId7(), msg);
							}
						}
					}
				}
			}
	
				
			//添加 接受者记录     user1:6006800BBDF034DFE040007F01001D9E
			String receiver=msg.getMsgReceiver();	
			String temp[]=receiver.split(",");
			String rcvUserId=null;
			for(int i=0;i<temp.length;i++){
				ToaMessage recvMsg = new ToaMessage();
				//recvMsg.setIsHasAttr("0");
				recvMsg.setMsgSender(msg.getMsgSender());
				recvMsg.setMsgReceiver(msg.getMsgReceiver());
				recvMsg.setMsgCc(msg.getMsgCc());
				if(msgId!=null && !"".equals(msgId)){
					recvMsg.setParentMsgId(msgId);
				}
				//转发时发送人的附件标示转给收件人的附件标示
				if(!"1".equals(isReply)){
					recvMsg.setIsHasAttr(tmpMsg==null?"0":tmpMsg.getIsHasAttr());
				}else{
					recvMsg.setIsHasAttr("0");
				}
				recvMsg.setMsgTitle(msg.getMsgTitle());
				recvMsg.setMsgContent(msg.getMsgContent());
				recvMsg.setMsgDate(msg.getMsgDate());
				recvMsg.setMsgType(msg.getMsgType());
				recvMsg.setIsRead("0");
				recvMsg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
				/**
				 * 新增操作时往推送设置里面新增记录或者修改推送数
				 */
				rcvUserId=temp[i].substring(temp[i].indexOf(":")+1, temp[i].indexOf(">"));
				recvMsg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_RECV,rcvUserId));
				messageDao.save(recvMsg);
				
				/**
				 * 发送者的附件传给接收者
				 * 添加消息附件表数据
				 */
				if(!"1".equals(isReply)){
					if(tmpMsg!=null){
						Set attSet=tmpMsg.getToaMessageAttaches();
						if(attSet!=null&&attSet.size()>0){
							Iterator it=attSet.iterator();
							while(it.hasNext()){
								ToaMessageAttach att=(ToaMessageAttach)it.next();
								attachManager.saveAttach(att.getAttachId7(), recvMsg);
							}
						}
					}
				}
				
				if(pushManager.getPushState(rcvUserId,PushNotifyManager.PUSH_MODULE_NO_MESSAGE)){
					pushManager.saveNotity(rcvUserId,PushNotifyManager.PUSH_MODULE_NO_MESSAGE, 1);
				}
				returnStatus="1";
			}
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"添加消息记录"});
		}
		return returnStatus;
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-14下午09:20:14
	  * @desc: 根据主键获取消息对象
	  * @param String keyid 主键
	  * @return ToaMessage 消息对象
	 */
	public ToaMessage getMessageById(String keyid) throws SystemException,ServiceException{
		ToaMessage message=null;
		try{
			message=messageDao.get(keyid);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取消息记录"});
		}
		return message;
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-14下午09:19:59
	  * @desc: 获取当前用户
	  * @param
	  * @return
	 */
	public User getCurrUser() throws SystemException,ServiceException{
		try{
			return userService.getCurrentUser();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"获取当前用户"});
		}
	}
	
	/**
	  * @author：pengxq
	  * @time：2009-1-14下午09:17:59
	  * @desc: 快速回复
	  * @param ToaMessage receive1 待回复的短消息对象
	  * @param String quicklyre 回复内容
	  * @param String isReturnBack 是否已读回执变量
	  * @return
	 */
	public void quickSend(ToaMessage receive1,String quicklyre,String isReturnBack)throws SystemException,ServiceException{
		try{
			User user=this.getCurrUser();
			if(receive1.getMsgTitle()==null||"".equals(receive1.getMsgTitle())){	//标题为空时
				receive1.setMsgTitle("无标题");
			}
			
			//添加 发送者记录
			ToaMessage send=new ToaMessage();	
			send.setMsgSender(user.getUserName()+"<id:"+user.getUserId()+">");	//发信人
			send.setMsgReceiver(receive1.getMsgSender());	//收信人
			send.setMsgTitle(receive1.getMsgTitle());		//标题		
			send.setMsgContent(quicklyre);	//回复内容
			send.setMsgDate(new Date());	//日期
			send.setIsRead("0");			//设置未读
			send.setIsHasAttr("0");			//无附件
			send.setParentMsgId(receive1.getMsgId());
			//因为接受者和发件者存储形式是：用户姓名<id:用户id>，要取出用户id
			String sender=send.getMsgSender();
			send.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_SEND,sender.substring(sender.indexOf(":")+1,sender.indexOf(">"))));//设置为发件箱的短消息
			send.setMsgIsdel(ToaMessage.MSG_NOTDEL);
			messageDao.save(send);
			
			receive1.setIsReply("1");	//设置被回复的那条信息已回复
			receive1.setMsgIsdel(ToaMessage.MSG_NOTDEL);
			messageDao.save(receive1);
			
			//添加 接收者记录     user1:6006800BBDF034DFE040007F01001D9E
			ToaMessage receive2 = new ToaMessage();
			receive2.setMsgSender(send.getMsgSender());
			receive2.setMsgReceiver(send.getMsgReceiver());	
			receive2.setMsgTitle(send.getMsgTitle());
			receive2.setMsgContent(send.getMsgContent());
			receive2.setMsgDate(send.getMsgDate());
			receive2.setIsRead("0");
			receive2.setIsHasAttr("0");
			receive2.setParentMsgId(send.getMsgId());
			//因为接受者和发件者存储形式是：用户姓名<id:用户id>，要取出用户id
			String receiver=send.getMsgReceiver();
			receive2.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_RECV,receiver.substring(receiver.indexOf(":")+1,receiver.indexOf(">"))));
			receive2.setMsgIsdel(ToaMessage.MSG_NOTDEL);
			messageDao.save(receive2);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"快速回复"});
		}	
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-14下午09:21:29
	  * @desc: 跳转到回复页面
	  * @param
	  * @return
	 */
	public ToaMessage getReplyCon(ToaMessage model)throws SystemException,ServiceException{
		User user=this.getCurrUser();
		try{
			StringBuffer beforeHtml=new StringBuffer();
			beforeHtml.append("<br>")
			.append("<br>")
			.append("<br>")
			.append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>"+StrongDate.getDate(new Date(), StrongDate.heng)+"</FONT></DIV><FONT face=Verdana size=2>\n")
			.append("<HR style=\"WIDTH: 122px; HEIGHT: 2px\" align=left SIZE=2>\n")
			.append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>发&nbsp;件&nbsp;人："+model.getMsgSender()+"</FONT></DIV><FONT face=Verdana size=2>\n")
			.append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>发送时间："+StrongDate.getDate(model.getMsgDate(), StrongDate.heng)+"</FONT></DIV><FONT face=Verdana size=2>\n")
			.append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>收&nbsp;件&nbsp;人："+StringUtil.getRetrunStr(model.getMsgReceiver())+"</FONT></DIV><FONT face=Verdana size=2>\n")
			.append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>主&nbsp;&nbsp;&nbsp;&nbsp;题："+StringUtil.getRetrunStr(model.getMsgTitle())+"</FONT></DIV><FONT face=Verdana size=2>\n")
			.append("<HR style=\"WIDTH: 100%; HEIGHT: 2px\" align=left SIZE=2>\n");
			messageDao.getSession().clear();
			model.setMsgContent(HtmlUtils.htmlEscape(beforeHtml.toString())+(model.getMsgContent()==null?"":model.getMsgContent()));
			model.setMsgTitle("RE:"+(model.getMsgTitle()==null?"":model.getMsgTitle()));
			model.setMsgReceiver(model.getMsgSender());	
			model.setMsgSender(user.getUserName()+"<id:"+user.getUserId()+">");
			return model;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"跳转到回复页面"});
		}
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-15上午09:00:56
	  * @desc: 回复消息
	  * @param
	  * @return
	 */
	public String replyMessage(String msgId,ToaMessage msg,File file[],String[] fileFileName, OALogInfo ... loginfos)throws SystemException,ServiceException{
		String senderId = userService.getCurrentUser().getUserId();
		String sendName=userService.getCurrentUser().getUserName();
		String message="回复失败";
		try {
			ToaMessage oldMsg=this.getMessageById(msgId);
			
			if(msg.getMsgTitle()==null||"".equals(msg.getMsgTitle())){	//标题为空时
				msg.setMsgTitle("无标题");
			}
			
			//添加 发送者记录
			if(msgId!=null&&!"".equals(msgId)&&!"null".equals(msgId)){
				List<ToaMessage> msglist = new ArrayList<ToaMessage>();

				
				if(file==null){
					msg.setIsHasAttr("0");
				}else{
					msg.setIsHasAttr("1");
				}
				msg.setMsgId(null);	
				msg.setMsgDate(new Date());
				msg.setParentMsgId(msgId);
				msg.setIsRead("0");
				msg.setMsgSender(sendName+"<id:"+senderId+">");
				msg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_SEND,senderId));//设置为发件箱的短消息
				msg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
				messageDao.save(msg);
				msglist.add(msg);
				
				oldMsg.setIsReply("1");
				oldMsg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
				messageDao.save(oldMsg);
				
				//添加 收件人记录   
				String receivers=msg.getMsgReceiver();
				String[] recArray=receivers.split(",");
				for(int i=0;i<recArray.length;i++){
					ToaMessage recvMsg = new ToaMessage();
					if(file==null){
						recvMsg.setIsHasAttr("0");
					}else{
						recvMsg.setIsHasAttr("1");
					}
					recvMsg.setMsgSender(msg.getMsgSender());
					recvMsg.setMsgReceiver(msg.getMsgReceiver());		
					recvMsg.setIsRead("0");
					recvMsg.setMsgTitle(msg.getMsgTitle());
					recvMsg.setMsgCc(msg.getMsgCc());
					recvMsg.setMsgContent(msg.getMsgContent());
					recvMsg.setMsgDate(msg.getMsgDate());
					recvMsg.setParentMsgId(msg.getMsgId());
					recvMsg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
					//因为接受者和发件者存储形式是：用户姓名<id:用户id>，要取出用户id
					//String receiver=msg.getMsgReceiver();
					recvMsg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_RECV,recArray[i].substring(recArray[i].indexOf(":")+1,recArray[i].indexOf(">"))));
					messageDao.save(recvMsg);
					msglist.add(recvMsg);
				}
				
				//ToaMessage csMsg=new ToaMessage();
				//判断有没有要抄送的人
//				if(recvMsg.getMsgCc()!=null&&!"".equals(recvMsg.getMsgCc())&&!"null".equals(recvMsg.getMsgCc())){	
//					BeanUtils.copyProperties(csMsg, recvMsg);
//					csMsg.setMsgId(null);
//					csMsg.setMsgReceiver(recvMsg.getMsgCc());
//					messageDao.save(csMsg);
//					msglist.add(csMsg);
//				}	
				
				if(msglist.size()>0){
					this.SaveMessageAtt(msglist, file, fileFileName);
				}		
			}
			message="回复成功！";
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"消息回复"});
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"消息回复"});
		} 			
		return message;
	}

	/**
 	  * 
	  * @author：pengxq
	  * @time：2009-1-15下午02:54:33
	  * @desc: 根据parentMsgId获取属于该信息的发出信息记录
	  * @param  String parentMsgId 属于该信息的发出信息记录id
	  * @return ToaMessage 发出的信息记录
	 */
	public ToaMessage getMessageByParen(String parentMsgId)throws SystemException,ServiceException{
		ToaMessage message=null;
		try{
			String hql="from ToaMessage t where t.msgId=?";
			List list=messageDao.find(hql, parentMsgId);
			if(list.size()>0){
				message=(ToaMessage) list.get(0);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取消息记录"});
		}
		return message;
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-15下午03:14:09
	  * @desc: 保存短消息
	  * @param ToaMessage message 短消息对象
	  * @return void
	 */
	public void saveMessage(ToaMessage message, OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			message.setMsgIsdel(ToaMessage.MSG_NOTDEL);
			messageDao.save(message);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存消息记录"});
		}
		
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-15下午09:04:05
	  * @desc: 跳转到转发页面
	  * @param
	  * @return
	 */
	public ToaMessage getTranCon(ToaMessage model)throws SystemException,ServiceException{
		try{
			User user=this.getCurrUser();
			String userid=user.getUserId();
			StringBuffer beforeHtml=new StringBuffer();
			beforeHtml.append("<br>")
			.append("<br>")
			.append("<br>")
			.append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>"+StrongDate.getDate(new Date(), StrongDate.heng)+"</FONT></DIV><FONT face=Verdana size=2>\n")
			.append("<HR style=\"WIDTH: 122px; HEIGHT: 2px\" align=left SIZE=2>\n")
			.append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>原发件人："+model.getMsgSender()+"</FONT></DIV><FONT face=Verdana size=2>\n")
			.append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>发送时间："+StrongDate.getDate(model.getMsgDate(), StrongDate.heng)+"</FONT></DIV><FONT face=Verdana size=2>\n")
			.append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>收&nbsp;件&nbsp;人："+StringUtil.getRetrunStr(model.getMsgReceiver())+"</FONT></DIV><FONT face=Verdana size=2>\n")
			.append("<DIV align=left><FONT face=Verdana color=#c0c0c0 size=2>主&nbsp;&nbsp;&nbsp;&nbsp;题："+StringUtil.getRetrunStr(model.getMsgTitle())+"</FONT></DIV><FONT face=Verdana color=#0d0d0d size=2>\n")
			.append("<HR style=\"WIDTH: 100%; HEIGHT: 2px\" align=left SIZE=2>\n");
			messageDao.getSession().clear();
			model.setMsgContent(HtmlUtils.htmlEscape(beforeHtml.toString())+(model.getMsgContent()==null?"":model.getMsgContent()));
			model.setMsgTitle("FW:"+(model.getMsgTitle()==null?"":model.getMsgTitle()));
			model.setMsgReceiver(null);	
			model.setMsgSender(userService.getUserNameByUserId(userid)+"<id:"+userid+">");
			return model;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"到转发页面"});
		}
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-16下午09:03:20
	  * @desc: 回执
	  * @param
	  * @return
	 */
	public String receipt(ToaMessage message)throws SystemException,ServiceException{
		
		ToaMessageFolder folder = message.getToaMessageFolder();
		String userId = folder.getUserId();
		User user = userService.getUserInfoByUserId(userId);
		String userName=user.getUserName();
		String msg="回执失败！！";
		try{
			ToaMessage sendMsg=new ToaMessage();
			sendMsg.setIsRead("0");
			sendMsg.setMsgDate(new Date());
			sendMsg.setMsgReceiver(message.getMsgSender());
			sendMsg.setMsgSender(userName+"<id:"+userId+">");
			sendMsg.setMsgTitle("回执消息");
			sendMsg.setMsgContent(("您已经回执了"+sendMsg.getMsgReceiver()+"发给您的消息“"+message.getMsgTitle()+"”"));
			String sender=sendMsg.getMsgSender();
			sendMsg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_SEND,sender.substring(sender.indexOf(":")+1,sender.indexOf(">"))));//设置为发件箱的短消息
			sendMsg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
			messageDao.save(sendMsg);	
			
			ToaMessage recvMsg=new ToaMessage();
			recvMsg.setIsRead("0");
			recvMsg.setMsgDate(new Date());
			recvMsg.setMsgReceiver(sendMsg.getMsgReceiver());
			recvMsg.setMsgSender(sendMsg.getMsgSender());
			recvMsg.setMsgTitle("回执消息");
			recvMsg.setMsgContent((sendMsg.getMsgSender()+"回执了您发送的消息“"+message.getMsgTitle()+"”"));
			String receiver=recvMsg.getMsgReceiver();
			recvMsg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_RECV,receiver.substring(receiver.indexOf(":")+1,receiver.indexOf(">"))));
			recvMsg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
			messageDao.save(recvMsg);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"回执"});
		}
		return msg;
	}
	
	/**
	 * author:luosy
	 * description:得到公共附件
	 * modifyer:
	 * description:
	 * @param attachId 公共附件ID
	 * @return	ToaAttachment 公共附件对象
	 */
	public ToaAttachment getToaAttachmentById(String attachId)throws SystemException,ServiceException{
		try{
			return attachmentService.getAttachmentById(attachId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"消息取公共附件"});
		}
	}
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-18下午04:54:04
	  * @desc: 从附件中间表中获取外键值为msgId的记录
	  * @param String msgId 消息主键值
	  * @return 
	 */
	public List getAttachMent(String msgId)throws SystemException,ServiceException{
		try{
			return attachManager.getAttachMent(msgId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取附件"});
		}
	}
	
	
	/**
	 * author:luosy
	 * description: 删除某个消息对象的附件
	 * modifyer:
	 * description:
	 * @param msgId
	 */
	public void delAttach(String msgId,String attId)throws SystemException,ServiceException{
		try{
			attachManager.delAttach(attId, msgId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取附件"});
		}
	}
	
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-1-19上午10:57:45
	  * @desc: 保存转发的消息中的附件
	  * @param List list  转发时的发送记录、接收记录列表
	  * @param dbobj 附件id数组
	  * @return  String 保存是否成功
	 */
	public String saveOtherAttach(List list,String[] dbobj) throws SystemException,ServiceException{
		String msg="保存失败！！";
		try{
			for(int i=0;i<dbobj.length;i++){	//转发的消息中存在附件
				if(dbobj[i].indexOf(";")==-1){	 
					if(list!=null&&list.size()>0){	//消息数大于0
						for(int k=0;k<list.size();k++){
							attachManager.saveAttach(dbobj[i], (ToaMessage) list.get(k));
						}
					}
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存附件"});
		}
		return msg;
	}
	
	/**
	 * author:luosy
	 * description: 获取桌面显示的消息列表
	 * modifyer:
	 * description:
	 * @param userId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public Page<ToaMessage> getListForTable(String userId,Page<ToaMessage> page)throws SystemException,ServiceException{
		try{
			String folderId = msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_RECV,userId).getMsgFolderId();
//			List<ToaMessage> lst = messageDao.find("from ToaMessage msg where msg.msgIsdel=? and msg.toaMessageFolder.msgFolderId=? order by msg.msgDate desc ",ToaMessage.MSG_NOTDEL,folderId);
			page = messageDao.find(page, "from ToaMessage msg where msg.msgIsdel=? and msg.toaMessageFolder.msgFolderId=? order by msg.msgDate desc ",ToaMessage.MSG_NOTDEL,folderId);
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"桌面消息列表"});
		}
	}
	
	
	/**
	 * author:luosy
	 * description:  发送短信提醒
	 * modifyer:
	 * description:
	 * @param recvUser
	 * @param msgTitle
	 */
	public synchronized String sendSms(ToaMessage msg, String recvUser,String smsCon,String ownerNum
										,List means,Date smsSendDate,String moduleCode)throws SystemException,ServiceException{
		try{
			//获取自增长
			String  increaseCode = smsPlatformManager.getFullCode(moduleCode);
			if("toolong".equals(increaseCode)){
				return "toolong";
			}
			
			//设置消息记录的短信编码
			msg.setMessageCode(increaseCode);
			msg.setModuleCode(moduleCode);
			
			StringBuffer sb = new StringBuffer();
			sb.append("");
			//保存消息对应的短信回复答案
			for(int i=0;i<means.size();i++){
				String mean = means.get(i).toString();
				if(!"".equals(mean)&&null!=mean){
					ToaMsgStateMean msm = new ToaMsgStateMean();
					msm.setMsgState(String.valueOf(i));
					msm.setMsgStateMean(mean);
					msm.setToaMessage(msg);
					msgStateMeanManager.saveMsgStateMean(msm);
					sb.append("").append(moduleCode).append(increaseCode).append(i).append(mean).append("，");
				}
			}
			msg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
			messageDao.save(msg);
			
			//保存短信发送内容
			if(sb.toString().length()>1){
				String addCon = sb.toString().substring(0, sb.toString().length()-1);
				smsCon = smsCon+"\n回复:"+addCon+"";
			}
			ToaSms sms = new ToaSms();
			sms.setSmsCon(smsCon);
			sms.setSmsIsdel(ToaSms.SMS_NOTDEL);
			sms.setSmsSendTime(smsSendDate);
			sms.setSmsSendUser(userService.getCurrentUser().getUserId());
			sms.setModelCode(moduleCode);
			sms.setIncreaseCode(increaseCode);
			
			ToaBussinessModulePara modelCof= smsPlatformManager.getObjByCode(moduleCode);
			if(null!=modelCof){
				sms.setModelName(modelCof.getBussinessModuleName());
			}
			
			smsService.sendSmsByMessage(ownerNum, recvUser, sms);
			
			//自增长编码+1
			smsPlatformManager.updateMsgCode(moduleCode);
			
			return "true";
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"桌面消息列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description:得到对应的消息接收情况
	 * modifyer:
	 * description:
	 * @param msgId 消息ID
	 * @return ArrayList {{userId,isRead},{userId,isRead},……}
	 */
	@Transactional(readOnly=true)
	public List getRecvMessage(String msgId){
		return messageDao.find(" select msg.toaMessageFolder.userId, msg.isRead " +
				"from ToaMessage as msg where msg.parentMsgId=? ",msgId);
	}
	
	/**
	 * author:luosy
	 * description: 获取某个模块的短信默认回复答案
	 * modifyer:
	 * description:
	 * @param modulId 模块ID
	 * @return
	 */
	public List getModulStateMean(String modulId){
		return stateMeanManager.getObjsByPar(modulId);
	}
	
	
	/**
	 * author:luosy
	 * description:根据模块Id 获取该模块的配置信息
	 * modifyer:
	 * description:
	 * @param modulId 模块Id
	 * @return
	 */
	public ToaBussinessModulePara getModuleParaByCode(String modulId){
		return smsPlatformManager.getObjByCode(modulId);
	}
	
	/**
	 * author:luosy
	 * description:获取消息列表
	 * modifyer:
	 * description:
	 * @param moduleCode 模块ID
	 * @param increaseCode自增长编码
	 * @return
	 */
	public ToaMessage getMsgByModuleCode(String moduleCode,String increaseCode){
		List list = messageDao.find("from ToaMessage msg where msg.moduleCode=? and msg.messageCode=? ",moduleCode,increaseCode);
		if(null!=list){
			if(list.size()>0){
				return (ToaMessage)list.get(0);
			}
		}
		return null;
	}
	
	/**
	 * author:luosy
	 * description:找到某条消息记录对应的回复答案
	 * modifyer:
	 * description:
	 * @param msgId 消息ID 
	 * @return
	 */
	@Transactional(readOnly=true)
	public List getMsgMeansbyMsgId(String msgId){
		List list = msgStateMeanManager.getMeanListByMsgId(msgId);
		return list;
	}
	
	/**
	 * author:luosy
	 * description:获取对应模块编码的短信列表
	 * modifyer:
	 * description:
	 * @param modelCode 模块ID
	 * @param increaseCode 自增长编码
	 * @return
	 */
	public List<ToaSms> getSmsListByMoudelCode(String moduleCode,String increaseCode){
		return smsManager.getReceiverList(moduleCode, increaseCode);
	}
	
	/**
	 * author:luosy
	 * description:获取对应模块编码的短信内容
	 * modifyer:
	 * description:
	 * @param moduleCode
	 * @param increaseCode
	 * @return
	 */
	public String getSmsConByMoudelCode(String moduleCode,String increaseCode){
		List<ToaSms> list = getSmsListByMoudelCode(moduleCode,increaseCode);
		String smsCon =""; 
		if(null!=list){
			if(list.size()>0){
				Object obj = list.get(0).getSmsCon();
				if(obj!=null){
					smsCon = obj.toString();
					int i = smsCon.indexOf("\n回复:");
					if(i>1){
						return smsCon.substring(0, i);
					}
				}
			}
		}
		return smsCon;
	}
	
	/**
	 * author:luosy
	 * description:获取对应模块编码的 手录短信号码
	 * modifyer:
	 * description:
	 * @param moduleCode
	 * @param increaseCode
	 * @return
	 */
	public String getOwnerNumByMoudelCode(String moduleCode,String increaseCode){
		List<ToaSms> list = getSmsListByMoudelCode(moduleCode,increaseCode);
		StringBuffer ownerNum = new StringBuffer(); 
		if(null!=list){
			for(int i=0;i<list.size();i++){
				ToaSms sms = (ToaSms)list.get(i);
				String recvnum = sms.getSmsRecvnum();
				if(null!=recvnum&&!"".equals(recvnum)){
					if(recvnum.equals(sms.getSmsRecver())){
						ownerNum.append(sms.getSmsRecvnum()).append(",");
					}
				}
			}
		}
		String allNum = ownerNum.toString();
		if(allNum.length()>0){
			allNum = allNum.substring(0, allNum.length()-1);
		}
		return allNum;
	}
	
	
	public String replyMessage(String userId,String msgId,ToaMessage msg)throws SystemException,ServiceException{
		String sender=msg.getMsgSender();
		String message="0";
		try {
			ToaMessage oldMsg=this.getMessageById(msgId);
			
			if(msg.getMsgTitle()==null||"".equals(msg.getMsgTitle())){	//标题为空时
				msg.setMsgTitle("无标题");
			}
			
			//添加 发送者记录
			if(msgId!=null&&!"".equals(msgId)&&!"null".equals(msgId)){
				
				msg.setIsHasAttr("0");
				msg.setMsgId(null);	
				msg.setMsgDate(new Date());
				msg.setParentMsgId(msgId);
				msg.setIsRead("0");
				msg.setMsgSender(sender);
				msg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_SEND,userId));//设置为发件箱的短消息
				msg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
				messageDao.save(msg);
				
				oldMsg.setIsReply("1");
				oldMsg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
				messageDao.save(oldMsg);
				
				//添加 收件人记录   
				String receivers=msg.getMsgReceiver();
				String[] recArray=receivers.split(",");
				String rcvUserId=null;
				for(int i=0;i<recArray.length;i++){
					ToaMessage recvMsg = new ToaMessage();
					recvMsg.setIsHasAttr("0");
					recvMsg.setMsgSender(sender);
					recvMsg.setMsgReceiver(msg.getMsgReceiver());		
					recvMsg.setIsRead("0");
					recvMsg.setMsgTitle(msg.getMsgTitle());
					recvMsg.setMsgCc(msg.getMsgCc());
					recvMsg.setMsgContent(msg.getMsgContent());
					recvMsg.setMsgDate(msg.getMsgDate());
					recvMsg.setParentMsgId(msgId);
					recvMsg.setMsgIsdel(ToaMessage.MSG_NOTDEL);
					//因为接受者和发件者存储形式是：用户姓名<id:用户id>，要取出用户id
					//String receiver=msg.getMsgReceiver();
					/**
					 * 新增操作时往推送设置里面新增记录或者修改推送数
					 */
					rcvUserId=recArray[i].substring(recArray[i].indexOf(":")+1,recArray[i].indexOf(">"));
					if(pushManager.getPushState(rcvUserId,PushNotifyManager.PUSH_MODULE_NO_MESSAGE)){
						pushManager.saveNotity(rcvUserId,PushNotifyManager.PUSH_MODULE_NO_MESSAGE, 1);
					}
					recvMsg.setToaMessageFolder(msgForlderManager.getMsgFolderByName(ToaMessageFolder.FOLDER_RECV,rcvUserId));
					messageDao.save(recvMsg);
				}
			
			}
			message="1";
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"消息回复"});
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"消息回复"});
		} 			
		return message;
	}

	
	/**
	 * author:luosy
	 * description: 获取某用户的所有附件所占的空间（b）
	 * modifyer:
	 * description:
	 * @param userId 用户ID
	 * @return
	 */
	public long getMsgAttachmentSize(String userId){
		long attSize = 0;
		String sql = " select att.attachSize " +
				" from ToaMessage as msg,ToaMessageAttach as msgatt,ToaAttachment as att " +
				" where msg.toaMessageFolder.userId=? and msgatt.toaMessage.msgId=msg.msgId and att.attachId=msgatt.attachId7";
		List list = messageDao.find(sql,userId);
		if(list!=null){
			long singlesize = 0;
			String size = "";
			for(int i=0;i<list.size();i++){
				size = list.get(i)+"";
				if(!"".equals(size)&&!"null".equals(size)){
					singlesize = new Long(size).longValue();
					attSize += singlesize;
				}else{
					attSize += 0;
				}
			}
		}
		
		return attSize;
	}
	
	/**
	 * author:luosy
	 * description:获取系统管理中对个人的消息空间大小的值
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public long getbaseMsgSize() throws SystemException,ServiceException{
		try{
			String baseMsgSize=pcService.getConfigSize(ConfigModule.MESSAGE);
			return (new Long(baseMsgSize).longValue())*1024*1024;
		}catch (Exception e) {
			return 52428800;
		}
	}
	
}
