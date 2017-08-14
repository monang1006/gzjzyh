/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息文件夹 业务类
 */
package com.strongit.oa.message;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMessage;
import com.strongit.oa.bo.ToaMessageFolder;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 处理消息文件夹类
 * @Create Date: 2009-12-7
 * @author luosy
 * @version 1.0
 */
@Service
@Transactional
@OALogger
public class MessageFolderManager {
	private GenericDAOHibernate<ToaMessageFolder, java.lang.String> messagFolderdao;

	private IUserService userService;
	
	private MessageManager msgManager;

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setMsgManager(MessageManager msgManager) {
		this.msgManager = msgManager;
	}

	public MessageFolderManager() {

	}

	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		messagFolderdao = new GenericDAOHibernate<ToaMessageFolder, java.lang.String>(
				session, ToaMessageFolder.class);
	}

	/**
	 * author:luosy
	 * description: 根据当前用户自定义获取该用户的自定义文件夹列表信息
	 * modifyer:
	 * description:
	 * @return
	 */
	public List<ToaMessageFolder> getFolderListByUser(String userId) throws SystemException,ServiceException{
		try{
			if(null==userId||"".equals(userId)){
				userId = userService.getCurrentUser().getUserId();
			}
			List<ToaMessageFolder> l = messagFolderdao.find("from ToaMessageFolder t where t.msgFolderType ='"+ToaMessageFolder.USER_FOLDER+"' "
					+" and t.userId like '%"+userId+"%' ");
			return l;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"文件夹列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 根据获取当前用户的所有文件夹列表信息
	 * modifyer:
	 * description:
	 * @return
	 */
	public List<ToaMessageFolder> getAllFolderListByUser(String userId) throws SystemException,ServiceException{
		try{
			if("".equals(userId)|null==userId){
				userId = userService.getCurrentUser().getUserId();
			}
			List<ToaMessageFolder> l = messagFolderdao.find("from ToaMessageFolder t where t.userId like '%"+userId+"%'");
			return l;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"文件夹列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 保存自定义文件夹
	 * modifyer:
	 * description:
	 * @param msgFolder
	 * @return
	 */
	public String saveFolder(ToaMessageFolder msgFolder, OALogInfo ... loginfos)throws SystemException,ServiceException{
		try{
			if("".equals(msgFolder.getMsgFolderId())){
				msgFolder.setMsgFolderId(null);
			}
			msgFolder.setUserId(userService.getCurrentUser().getUserId());
			messagFolderdao.save(msgFolder);
			msgFolder.setMsgFolderType("1");//自定义文件夹
			return msgFolder.getMsgFolderId();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存自定义文件夹"});
		}
	}
	
	/**
	 * author:luosy
	 * description:获取文件夹对象
	 * modifyer:
	 * description:
	 * @param folderId
	 * @return
	 */
	public ToaMessageFolder getMsgFolderById(String folderId)throws SystemException,ServiceException{
		try{ 
			return messagFolderdao.findById(folderId,true);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取文件夹"});
		}
	}
	
	/**
	 * author:luosy
	 * description:是否存在同样名称的文件夹
	 * modifyer:
	 * description:
	 * @param folderName
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Boolean isExistFolder(String folderName)throws SystemException,ServiceException{
		try{ 
			String userId = userService.getCurrentUser().getUserId();
			Object[] obj = {userId,folderName};
			List list = messagFolderdao.find("from ToaMessageFolder t where t.userId like ? and t.msgFolderName like ?",obj);
			if(null!=list){
				if(list.size()>0){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取文件夹"});
		}
		
	}
	
	/**
	 * author:luosy
	 * description:  获取基础文件夹对象  如果没有则新建该文件夹
	 * modifyer:
	 * description:
	 * @param folderName  "收件箱"|"发件箱"|"草稿箱"|"垃圾箱"
	 * @return
	 */
	public ToaMessageFolder getMsgFolderByName(String folderName,String userId) throws SystemException,ServiceException{
		try{ 
			Object[] obj = {userId,folderName};
			List l = messagFolderdao.find("from ToaMessageFolder t where t.userId like ? and t.msgFolderName like ?",obj);
			if(l.size()>0){
				return (ToaMessageFolder) l.get(0);
			}else{
				ToaMessageFolder msgFolder = new ToaMessageFolder();
				msgFolder.setMsgFolderName(folderName);
				msgFolder.setMsgFolderType("0");//基础文件夹
				msgFolder.setUserId(userId.trim());
				messagFolderdao.save(msgFolder);
				return msgFolder;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"基础文件夹"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"基础文件夹"});
		}
		
	}
	
	/**
	 * author:luosy
	 * description:删除文件夹
	 * modifyer:
	 * description:
	 * @param folder
	 */
	public void delFolder(ToaMessageFolder folder, OALogInfo ... loginfos)throws SystemException,ServiceException{
		try{ 
			Set msgs = folder.getToaMessages();
			if(msgs!=null){
				Iterator it=msgs.iterator();
				while (it.hasNext()) {
					ToaMessage msg = (ToaMessage) it.next();
					msgManager.delMessage(msg, loginfos);
				}
			}
			messagFolderdao.delete(folder);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除文件夹"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 计算出某个文件夹下的未读消息数
	 * modifyer:
	 * description:
	 * @param msgFolderId 文件夹id
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws AjaxException
	 */
	public String getUnreadCount(String msgFolderId,String userId)throws SystemException,ServiceException,AjaxException{
		try{ 
			if("".equals(userId)||null==userId){
				userId = userService.getCurrentUser().getUserId();
			}
			if("recv".equals(msgFolderId)){
				msgFolderId = getMsgFolderByName(ToaMessageFolder.FOLDER_RECV, userId).getMsgFolderId();
			}else if("send".equals(msgFolderId)){
				msgFolderId = getMsgFolderByName(ToaMessageFolder.FOLDER_SEND, userId).getMsgFolderId();
			}else if("draft".equals(msgFolderId)){
				msgFolderId = getMsgFolderByName(ToaMessageFolder.FOLDER_DRAFT, userId).getMsgFolderId();
			}else if("rubbish".equals(msgFolderId)){
				msgFolderId = getMsgFolderByName(ToaMessageFolder.FOLDER_RUBBISH, userId).getMsgFolderId();
			}

			List l = messagFolderdao.find("select count(*) from ToaMessageFolder fold ,ToaMessage msg " +
					"where fold.msgFolderId = ? and msg.msgIsdel=?  and fold.msgFolderId = msg.toaMessageFolder.msgFolderId and msg.isRead = 0 "
					, msgFolderId,ToaMessage.MSG_NOTDEL);
			if(null!=l){
				return String.valueOf(l.get(0));
			}else{
				return "0";
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未读记数 "});
		}catch(AjaxException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未读记数 "});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未读记数 "});
		}
	}
	
	/*
	 * 已读短信数
	 */
	public String getReadedCount(String msgFolderId,String userId)throws SystemException,ServiceException,AjaxException{
		try{ 
			if("".equals(userId)||null==userId){
				userId = userService.getCurrentUser().getUserId();
			}
			if("recv".equals(msgFolderId)){
				msgFolderId = getMsgFolderByName(ToaMessageFolder.FOLDER_RECV, userId).getMsgFolderId();
			}else if("send".equals(msgFolderId)){
				msgFolderId = getMsgFolderByName(ToaMessageFolder.FOLDER_SEND, userId).getMsgFolderId();
			}else if("draft".equals(msgFolderId)){
				msgFolderId = getMsgFolderByName(ToaMessageFolder.FOLDER_DRAFT, userId).getMsgFolderId();
			}else if("rubbish".equals(msgFolderId)){
				msgFolderId = getMsgFolderByName(ToaMessageFolder.FOLDER_RUBBISH, userId).getMsgFolderId();
			}

			List l = messagFolderdao.find("select count(*) from ToaMessageFolder fold ,ToaMessage msg " +
					"where fold.msgFolderId = ? and msg.msgIsdel=?  and fold.msgFolderId = msg.toaMessageFolder.msgFolderId and msg.isRead = 1 "
					, msgFolderId,ToaMessage.MSG_NOTDEL);
			if(null!=l){
				return String.valueOf(l.get(0));
			}else{
				return "0";
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找已读记数 "});
		}catch(AjaxException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找已读记数 "});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找已读记数 "});
		}
	}
	
	
	/**
	 * author:luosy
	 * description: 获取工作提示列表(用户登入系统后所有为读的系统提示消息)
	 * modifyer:
	 * description:
	 * @param loginTime 用户登入时间
	 * @return
	 */
	public String getUnreadForMessage(Date loginTime){
		try{ 
			String userId = userService.getCurrentUser().getUserId();
			String msgFolderId = getMsgFolderByName(ToaMessageFolder.FOLDER_RECV, userId).getMsgFolderId();
			List list = messagFolderdao.find("select count(*) from ToaMessageFolder fold ,ToaMessage msg " +
					"where fold.msgFolderId = ? and msg.msgIsdel=?  and fold.msgFolderId = msg.toaMessageFolder.msgFolderId " +
					"and msg.isRead = 0 and msg.msgDate>? "
					, msgFolderId,ToaMessage.MSG_NOTDEL,loginTime);
			if(null!=list){
				return String.valueOf(list.get(0));
			}else{
				return "0";
			}
			
			/*List list = messagFolderdao.find("select msg.msgType from ToaMessageFolder fold ,ToaMessage msg " +
					"where fold.msgFolderId = ? and msg.msgIsdel=?  and fold.msgFolderId = msg.toaMessageFolder.msgFolderId " +
					"and msg.isRead = 0  and msg.msgType is not null "
					, msgFolderId,ToaMessage.MSG_NOTDEL);
			return list;*/
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未读记数 "});
		}catch(AjaxException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未读记数 "});
		}
	}
	
	/**
	 * author:luosy
	 * description: 获取工作提示列表(用户登入系统后所有为读的系统提示消息)
	 * modifyer:
	 * description:
	 * @param loginTime 用户登入时间
	 * @return
	 */
	public List getNewMsgForWork(Date loginTime){
		try{ 
			String userId = userService.getCurrentUser().getUserId();
			String msgFolderId = getMsgFolderByName(ToaMessageFolder.FOLDER_RECV, userId).getMsgFolderId();
			List list = messagFolderdao.find("select msg.msgType from ToaMessageFolder fold ,ToaMessage msg " +
					"where fold.msgFolderId = ? and msg.msgIsdel=?  and fold.msgFolderId = msg.toaMessageFolder.msgFolderId " +
					"and msg.isRead = 0 and msg.msgDate>? and msg.msgType is not null "
					, msgFolderId,ToaMessage.MSG_NOTDEL,loginTime);
			return list;
			
			/*List list = messagFolderdao.find("select msg.msgType from ToaMessageFolder fold ,ToaMessage msg " +
					"where fold.msgFolderId = ? and msg.msgIsdel=?  and fold.msgFolderId = msg.toaMessageFolder.msgFolderId " +
					"and msg.isRead = 0  and msg.msgType is not null "
					, msgFolderId,ToaMessage.MSG_NOTDEL);
			return list;*/
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未读记数 "});
		}catch(AjaxException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未读记数 "});
		}
	}
	
	/**
	 * author:luosy
	 * description:根据消息分类 修改消息已读状态
	 * modifyer:
	 * description:
	 * @param msgType
	 */
	public void setReadStateByType(String msgType,Date loginTime){
		String userId = userService.getCurrentUser().getUserId();
		String msgFolderId = getMsgFolderByName(ToaMessageFolder.FOLDER_RECV, userId).getMsgFolderId();
		
		List list = messagFolderdao.find("select msg from ToaMessageFolder fold ,ToaMessage msg " +
				"where fold.msgFolderId = ? and msg.msgIsdel=?  and fold.msgFolderId = msg.toaMessageFolder.msgFolderId " +
				"and msg.isRead = 0 and msg.msgDate>? and msg.msgType=? "
				, msgFolderId,ToaMessage.MSG_NOTDEL,loginTime,msgType);
		for(Object msg:list){
			ToaMessage message = (ToaMessage) msg;
			message.setIsRead("1");
			msgManager.saveMessage(message);
		}
		
		
	}
	
	public boolean isNullFolder(String msgFolderId)throws SystemException,ServiceException,AjaxException{
		try{ 
			List list = messagFolderdao.find("from ToaMessageFolder fold ,ToaMessage msg " +
					"where fold.msgFolderId = ?  and fold.msgFolderId = msg.toaMessageFolder.msgFolderId", msgFolderId);
			if(null==list){
				return true;
			}else{
				if(list.size()>0){
					return false;
				}else{
					return true;
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未读记数 "});
		}catch(AjaxException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找未读记数 "});
		}
	}

}
