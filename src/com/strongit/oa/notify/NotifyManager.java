/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理新闻公告manager类
 */
package com.strongit.oa.notify;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.ToaAffiche;
import com.strongit.oa.bo.ToaAfficheAttach;
import com.strongit.oa.bo.ToaAfficheReceiver;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.notifyattach.NotifyAttachManager;
import com.strongit.oa.paramconfig.ConfigModule;
import com.strongit.oa.paramconfig.ParamConfigService;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.oa.webservice.client.ipp.NotifyWebService;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 新闻公告manager
 * 
 * @author luosy
 * @version 1.0
 */
@Service
@Transactional
@OALogger
public class NotifyManager {
	private GenericDAOHibernate<ToaAffiche, java.lang.String> notifyDao;
	private GenericDAOHibernate<ToaAfficheReceiver, java.lang.String> receiverDao;
	
	
	

//	新闻公告附件
	private NotifyAttachManager attachManager;
	
//	公共附件接口
	private IAttachmentService attachmentService;
	
//	统一用户接口
	private IUserService userService;

	private ParamConfigService pcService;
	
	public NotifyManager() {

	}


	@Autowired
	public void setPcService(ParamConfigService pcService) {
		this.pcService = pcService;
	}
	@Autowired
	public void setAttachmentService(IAttachmentService iattachmentService) {
		this.attachmentService = iattachmentService;
	}
	
	@Autowired
	public void setAttachManager(NotifyAttachManager attachManager) {
		this.attachManager = attachManager;
	}
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		notifyDao = new GenericDAOHibernate<ToaAffiche, java.lang.String>(
				session, ToaAffiche.class);
		receiverDao = new GenericDAOHibernate<ToaAfficheReceiver, java.lang.String>(
				session, ToaAfficheReceiver.class);
	}
	public boolean deleteReceivers(Set set){
		if(set==null){
			return true;
		}
		List list = new ArrayList(set);
		try{
			receiverDao.delete(list);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean deleteReceivers(String id){
		try{
			receiverDao.delete(id);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * author:luosy
	 * description:搜索公告
	 * modifyer:
	 * description:
	 * @param page		分页对象
	 * @param toaAffiche 公告对象
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<ToaAffiche> search(Page<ToaAffiche> page, ToaAffiche toaAffiche) throws SystemException,ServiceException{
		try{
			// 获取当前用户
			String userId = userService.getCurrentUser().getUserId();
			StringBuffer hql = new StringBuffer();
			
			hql.append("select t.toaAffiche from ToaAfficheReceiver t where (t.afficheReceiverId=?  or t.afficheReceiverId='alluser')");
			
			
			Object[] obj = new Object[3];
			
			int i = 1;
			obj[0] = userId;
			
			String title = toaAffiche.getAfficheTitle();
			if (!"".equals(title) && title != null) {
				hql.append(" and t.toaAffiche.afficheTitle like '%"+title+"%' ");
			}
			
			String afficheState = toaAffiche.getAfficheState();
			if (!"".equals(afficheState) && afficheState != null) {
				hql.append(" and t.toaAffiche.afficheState = "+afficheState +" ");
			}else {
				hql.append(" and (t.toaAffiche.afficheState ='1' or t.toaAffiche.afficheState ='2')");
				}
			
			
			String afficheDesc = toaAffiche.getAfficheDesc();
			if (!"".equals(afficheDesc) && afficheDesc != null) {
				hql.append(" and t.toaAffiche.afficheDesc like '%" + afficheDesc + "%' ");
			}
			
			String afficheAuthor = toaAffiche.getAfficheAuthor();
			if (!"".equals(afficheAuthor) && afficheAuthor != null) {
				hql.append(" and t.toaAffiche.afficheAuthor like '%" + afficheAuthor + "%' ");
			}
			
			String afficheGov = toaAffiche.getAfficheGov();
			if (!"".equals(afficheGov) && afficheGov != null) {
				hql.append(" and t.toaAffiche.afficheGov like '%" + afficheGov + "%' ");
			}
			
			Date date = toaAffiche.getAfficheTime();
			if (date != null && !"".equals(date)) {
				Calendar cal=Calendar.getInstance();
				cal.setTime(date);//结束时间
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				Date edate=cal.getTime();
				hql.append(" and t.toaAffiche.afficheTime >= ? and t.toaAffiche.afficheTime <= ?");
				obj[i] = date;
				i++;
				obj[i] = edate;
				i++;
			} else {
				hql.append(" and 1= ? and 1= ? ");
				obj[i] = 1;
				i++;
				obj[i] = 1;
			}
			System.out.println("search:\n"+hql.toString()+" order by t.toaAffiche.afficheTime desc , t.toaAffiche.afficheId desc ");
			page = notifyDao.find(page, hql.toString()+" order by t.toaAffiche.afficheTime desc , t.toaAffiche.afficheId desc ", obj);
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"新闻公告"});
		}
	}

	/**
	 * author:luosy
	 * description: 获取当前用户发布的公告
	 * modifyer:
	 * description:
	 * @param page		分页对象
	 * @param toaAffiche 公告对象 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<ToaAffiche> getMyList(Page<ToaAffiche> page, ToaAffiche toaAffiche) throws SystemException,ServiceException{
		try{
			// 获取当前用户
			String userId = userService.getCurrentUser().getUserId();
			StringBuffer hql = new StringBuffer();
			hql.append("from ToaAffiche t where t.afficheUserid=?");
			Object[] obj = new Object[3];
			
			int i = 1;
			obj[0] = userId;
			
			String title = toaAffiche.getAfficheTitle();
			if (!"".equals(title) && title != null) {				
				if(title.indexOf("%")>-1){									//处理%号
					title = title.replace("%", "/%");
					hql.append(" and t.afficheTitle like '%"+title+"%' ");
					hql.append(" ESCAPE '/'");
				}else{
					hql.append(" and t.afficheTitle like '%"+title+"%' ");
				}
			}
			
			String afficheState = toaAffiche.getAfficheState();
			if (!"".equals(afficheState) && afficheState != null) {
				hql.append(" and t.afficheState = "+afficheState +" ");
			}
			
			String afficheDesc = toaAffiche.getAfficheDesc();
			if (!"".equals(afficheDesc) && afficheDesc != null) {
				hql.append(" and t.afficheDesc like '%" + afficheDesc + "%' ");
			}
			
			String afficheAuthor = toaAffiche.getAfficheAuthor();
			if (!"".equals(afficheAuthor) && afficheAuthor != null) {
				hql.append(" and t.afficheAuthor like '%" + afficheAuthor + "%' ");
			}
			
			String afficheGov = toaAffiche.getAfficheGov();
			if (!"".equals(afficheGov) && afficheGov != null) {
				hql.append(" and t.afficheGov like '%" + afficheGov + "%' ");
			}
			
			Date date = toaAffiche.getAfficheTime();
			if (date != null && !"".equals(date)) {
				Calendar cal=Calendar.getInstance();
				cal.setTime(date);//结束时间
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				Date edate=cal.getTime();
				hql.append(" and   t.afficheTime >=?  and   t.afficheTime <=?");
				obj[i] = date;
				i++;
				obj[i] = edate;
				i++;
			} else {
				hql.append(" and 1= ? and 1= ? ");
				obj[i] = 1;
				i++;
				obj[i] = 1;
			}
			page = notifyDao.find(page, hql.toString()+" order by t.afficheTime desc , t.afficheId desc ", obj);
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"新闻公告"});
		}
	}

	/**
	 * author:漆宝华
	 * description: 获取当前用户的公告
	 * modifyer:
	 * description:
	 * @param page		分页对象
	 * @param toaAffiche 公告对象 
	 * @return
	 */
	public Page<ToaAffiche> getList(Page<ToaAffiche> page, ToaAffiche toaAffiche) throws SystemException,ServiceException{
		try{
			// 获取当前用户
			String userId = userService.getCurrentUser().getUserId();
			StringBuffer hql = new StringBuffer();
			
			hql.append("select t.toaAffiche from ToaAfficheReceiver t where (t.afficheReceiverId=?  or t.afficheReceiverId='alluser')");
			
			
			Object[] obj = new Object[3];
			
			int i = 1;
			obj[0] = userId;
			
			String title = toaAffiche.getAfficheTitle();
			if (!"".equals(title) && title != null) {
				hql.append(" and t.toaAffiche.afficheTitle like '%"+title+"%' ");
			}
			
			String afficheState = toaAffiche.getAfficheState();
			if (!"".equals(afficheState) && afficheState != null) {
				hql.append(" and t.toaAffiche.afficheState = "+afficheState +" ");
			}else {
				hql.append(" and (t.toaAffiche.afficheState ='1' or t.toaAffiche.afficheState ='2')");
				}
			
			String afficheDesc = toaAffiche.getAfficheDesc();
			if (!"".equals(afficheDesc) && afficheDesc != null) {
				hql.append(" and t.toaAffiche.afficheDesc like '%" + afficheDesc + "%' ");
			}
			
			String afficheAuthor = toaAffiche.getAfficheAuthor();
			if (!"".equals(afficheAuthor) && afficheAuthor != null) {
				hql.append(" and t.toaAffiche.afficheAuthor like '%" + afficheAuthor + "%' ");
			}
			
			String afficheGov = toaAffiche.getAfficheGov();
			if (!"".equals(afficheGov) && afficheGov != null) {
				hql.append(" and t.toaAffiche.afficheGov like '%" + afficheGov + "%' ");
			}
			
			Date date = toaAffiche.getAfficheUsefulLife();
			if (date != null && !"".equals(date)) {
				date.setHours(12);
				hql.append(" and ( ( t.toaAffiche.afficheUsefulLife > ? and t.toaAffiche.afficheTime < ?) or t.toaAffiche.afficheUsefulLife is null )");
				obj[i] = date;
				i++;
				obj[i] = date;
				i++;
			} else {
				hql.append(" and 1= ? and 1=?");
				obj[i] = 1;
				i++;
				obj[i] = 1;
			}

			System.out.println("getList:\n"+hql.toString()+" order by t.toaAffiche.afficheTime desc , t.toaAffiche.afficheId desc ");
			page = notifyDao.find(page, hql.toString()+" order by t.toaAffiche.afficheTime desc , t.toaAffiche.afficheId desc ", obj);
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"新闻公告"});
		}
	}

	/**
	 * author:luosy
	 * description: 根据状态查找所有公告
	 * modifyer:
	 * description:
	 * @param page  	分页对象
	 * @param inputType  公告状态
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Page<ToaAffiche> getListByState(Page<ToaAffiche> page,String inputType) throws SystemException,ServiceException{
		try{
			if (null == inputType | "".equals(inputType)) {
				page = notifyDao.find(page, "from ToaAffiche t order by t.afficheTime desc ");
			} else {
				page = notifyDao.find(page,
						"from ToaAffiche t where t.afficheState=? order by t.afficheTime desc ", inputType);
			}
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"新闻公告"});
		}
	}

	/**
	 * author:luosy
	 * description:保存公告
	 * modifyer:
	 * description:
	 * @param toaAffiche  公告对象
	 * @param file 		附件s
	 * @param fileFileName 附件名
	 */
	public void saveNotify(ToaAffiche toaAffiche,File[] file,String[] fileFileName, OALogInfo oaLogInfo) throws SystemException,ServiceException{
		try{
			if(null!=toaAffiche){
				if ("".equals(toaAffiche.getAfficheId())) {
					toaAffiche.setAfficheId(null);
				}
				// 如果未指定有效期起始时间 则默认为当前时间
				Calendar rightNow = Calendar.getInstance();
				if ("".equals(toaAffiche.getAfficheTime())
						| null == toaAffiche.getAfficheTime()) {
					toaAffiche.setAfficheTime(rightNow.getTime());
				}
				toaAffiche.setAfficheDesc(HtmlUtils.htmlEscape(toaAffiche
						.getAfficheDesc()));
				
				// 自动获取当前用户姓名及部门
				String userId = userService.getCurrentUser().getUserId();
				String orgName = userService.getUserDepartmentByUserId(userId).getOrgName();
				User user = userService.getUserInfoByUserId(userId);
				toaAffiche.setAfficheGov(orgName);
				toaAffiche.setAfficheAuthor(user.getUserName());
				toaAffiche.setAfficheUserid(user.getUserId());
				
				//分级信息
				TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userId);
				toaAffiche.setOrgCode(org.getSupOrgCode());
				toaAffiche.setOrgId(org.getOrgId());
				
				
				//是否过期
				if(toaAffiche.getAfficheUsefulLife()!=null){
					if(toaAffiche.getAfficheUsefulLife().getTime()<rightNow.getTime().getTime()){
						toaAffiche.setAfficheState(ToaAffiche.NOTIFY_OVERDUE);
					}
				}
				/*if(null!=toaAfficheReceiver&&!"".equals(toaAfficheReceiver)){
					if("".equals(toaAfficheReceiver.getAfficheReceiver())){
						toaAfficheReceiver.setAfficheReceiver(null);
					}
					receiverDao.save(toaAfficheReceiver);
				}*/
				notifyDao.save(toaAffiche);
				
				
				//保存附件
				this.saveNotifyAtts(toaAffiche, file, fileFileName);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"新闻公告"});
		}
	}
	
	public void saveAfficheRece(ToaAffiche toaAffiche,ToaAfficheReceiver toaAfficheReceiver)throws SystemException,ServiceException{
		try{
	//    保存发布范围
			if(null !=toaAfficheReceiver&&!"".equals(toaAfficheReceiver)){
				if("".equals(toaAfficheReceiver.getAfficheReceiver())){
					toaAfficheReceiver.setAfficheReceiver(null);
				}
				receiverDao.save(toaAfficheReceiver);
				
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"新闻公告"});
		}
	}
	
	public void saveNotifyAtts(ToaAffiche toaAffiche,File[] file,String[] fileFileName)throws SystemException,ServiceException{
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
						String attachId = attachmentService.saveAttachment(fileFileName[i], buf, cals.getTime(), ext, "1", "注:公告附件", userService.getCurrentUser().getUserId());
						//添加公告附件表数据
						attachManager.saveAttach(attachId, toaAffiche);
					}catch (Exception e) {
						e.printStackTrace();
						throw new ServiceException(MessagesConst.save_error,               
								new Object[] {"附件上传失败"});
					}finally{
						try {
							fils.close();
						} catch (IOException e) {
							throw new ServiceException(MessagesConst.save_error,               
									new Object[] {"附件保存失败"});
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"新闻公告"});
		}
	}
	
	/**
	 * author:luosy
	 * description:发布公告
	 * modifyer:
	 * description:
	 * @param notifyIds 公告ID集合"id,id,id……"
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws AjaxException
	 */
	public void publicNotify(String notifyIds, OALogInfo ... loginfos)throws SystemException,ServiceException,AjaxException{
		try{
			if ("".equals(notifyIds) | null == notifyIds) {
			} else {
				String[] ids = notifyIds.split(",");
				for (int i = 0; i < ids.length; i++) {
					ToaAffiche notify = getNotifyById(ids[i]);
					notify.setAfficheState(ToaAffiche.NOTIFY_SENDED);
					notifyDao.save(notify);
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"新闻公告"});
		}catch(AjaxException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"新闻公告"});
		}
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
					new Object[] {"新闻公告取附件"});
		}
	}

	/**
	 * author:luosy
	 * description:删除公告 
	 * modifyer:
	 * description:
	 * @param notifyCode 公告ID
	 */
	public void deleteNotify(String notifyCode, OALogInfo ... loginfos) throws SystemException,ServiceException,AjaxException{
		try{
			if ("".equals(notifyCode) | null == notifyCode) {
			} else {
				String[] ids = notifyCode.split(",");
				for (int i = 0; i < ids.length; i++) {
					notifyDao.delete(ids[i]);
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"新闻公告"});
		}catch(AjaxException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"新闻公告"});
		}
	}

	/**
	 * author:luosy
	 * description:根据ID获取公告
	 * modifyer:
	 * description:
	 * @param notifyId 公告ID
	 * @return
	 */
	public ToaAffiche getNotifyById(String notifyId) throws SystemException,ServiceException{
		try{
			return (ToaAffiche) notifyDao.findById(notifyId, true);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"新闻公告"});
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * author:漆宝华
	 * description:根据ID获取公告收件人
	 * modifyer:
	 * description:
	 * @param notifyId 公告ID
	 * @return
	 */
	public ToaAfficheReceiver getReceiverIdById(String notifyId) throws SystemException,ServiceException{
		try{
			return (ToaAfficheReceiver) receiverDao.findById(notifyId, true);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"新闻公告"});
		}catch(Exception e){
			return null;
		}
	}
	/**
	 * author:luosy
	 * description:删除相关附件
	 * modifyer:
	 * description:
	 * @param attachId 公共附件表主键
	 */
	public void delAttach(String attachId)throws SystemException,ServiceException{
		try{
			if(!"".equals(attachId)&&null!=attachId){
				//删除公共附件表
				attachmentService.deleteAttachment(attachId);
				//删除公告附件表
				attachManager.delAttach(attachId);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"新闻公告附件"});
		}
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
			StringBuffer formatedID = new StringBuffer();
			if("alluser".equals(userIds)){
				formatedID.append("所有人"+"<id:"+userIds+">,");
			}else{
				String[] userid = userIds.split(",");
				for(int i=0; i<userid.length;i++){
				if("".equals(userid[i])){
					continue;   //如果分割之后是空白的字符则跳过
				}
					formatedID.append(userService.getUserNameByUserId(userid[i])+"<id:"+userid[i]+">,");
				}
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
	 * description:提供个人桌面调用接口
	 * modifyer:
	 * description:
	 * @param userId  当前用户id
	 * @return
	 */
	@Transactional(readOnly=true)
	public Page<ToaAffiche> getListForTable(String userId,Page page) throws SystemException,ServiceException{
		try{
			
			StringBuilder hql = new StringBuilder();

			hql.append("from ToaAffiche t where t.afficheState=1 ");
			//分级信息
			TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userId);
			if(userService.isViewChildOrganizationEnabeld()){			//是否允许看到下级机构
				if(org!=null){
					hql.append(" and t.orgCode like '").append(org.getSupOrgCode()).append("%'");
				}
			}else {
				if(org!=null){
					hql.append(" and t.orgId = '").append(org.getOrgId()).append("'");
				}
			}
			hql.append(" order by t.afficheTime desc ");
			return notifyDao.find(page,hql.toString());
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"新闻公告桌面"});
		}
	}

	/**
	 * author:漆宝华
	 * description:提供个人桌面调用接口
	 * modifyer:
	 * description:
	 * @param userId  当前用户id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ToaAfficheReceiver> getListToaAffiche(String userId,Page page)
			throws DAOException, SystemException, ServiceException {

		final String hql = "from ToaAfficheReceiver as t where t.afficheReceiver = alluser or t.afficheReceiver = userId";
		return (List<ToaAfficheReceiver>) receiverDao.find(page,hql.toString());
	}
	
	/**
	 * author:漆宝华
	 * description:获取发送范围
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws Exception
	 */
	public List<ToaAfficheReceiver> getAfficheReceiver(String afficheId)
	throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaAfficheReceiver as t where t.toaAffiche.afficheId = ?";
		List<ToaAfficheReceiver> list = receiverDao.find(hql, afficheId);
		return list;
	}
	
	/**
	 * author:漆宝华
	 * description:获取当前用户的公告
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws Exception
	 */
	public List<ToaAfficheReceiver> getAfficheId(String affReceiverId)
	throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaAfficheReceiver as t where t.afficheReceiverId = ?";
		List<ToaAfficheReceiver> list1 = receiverDao.find(hql, affReceiverId);
		return list1;
	}
	
	/**
	 * author:luosy
	 * description:获取IPP栏目信息
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws Exception
	 */
	public List getLanmuList()throws SystemException,ServiceException,Exception,AjaxException{
		try{
			NotifyWebService nws = new NotifyWebService();
			return nws.getLanmuInfo();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"发布新闻公告_栏目信息"});
		}catch(AjaxException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"发布新闻公告_栏目信息"});
		}catch(Exception e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"发布新闻公告_栏目信息"});
		}
	}
	
	/**
	 * author:luosy
	 * description:通过IPP发布到外网
	 * modifyer:
	 * description:
	 * @param afficheId 新闻公告ID
	 * @param lanmuId 栏目ID
	 * @return
	 * @throws Exception
	 * @throws AjaxException
	 */
	public String sendTOIpp(String afficheId, String lanmuId, OALogInfo ... loginfos)throws Exception,AjaxException{
		try{
			NotifyWebService nws = new NotifyWebService();
			ToaAffiche notify = this.getNotifyById(afficheId);
			String content = HtmlUtils.htmlUnescape(notify.getAfficheDesc());
			
//			//附件
//			DataHandler[] datahandles = new DataHandler[20] ;
//			DataHandler datahandle = null;
//			
//			if(file!=null){
//				for(int i=0;i<file.length;i++){
//					FileDataSource fileSource = new FileDataSource(file[i]);
//					datahandle = new DataHandler(fileSource);
//					datahandles[i]=datahandle;
//					
//					
////					DataOutputStream dos = new DataOutputStream(new OutputStream(file[i])); 
//				}
//			}

			Set<ToaAfficheAttach> afs = notify.getToaAfficheAttachs();

			int length = afs.size();
			DataHandler[] datahandles = new DataHandler[length] ;
			String[] fileName = new String[length] ;
			DataHandler datahandle = null;
			int falg = 0;
			for(ToaAfficheAttach af: afs){
					ToaAttachment att = this.getToaAttachmentById(af.getAttachId());
					//转换数据类型，供webservice使用，将附件数据取出放入DataHandler
					ByteArrayDataSource bda= new ByteArrayDataSource(att.getAttachCon(),"application/x-attach");
					datahandle = new DataHandler(bda);
					datahandles[falg]=datahandle;
					fileName[falg] = att.getAttachName();
					falg++;
			}
			
			return nws.sendToIpp(lanmuId,notify.getAfficheAuthor(),notify.getAfficheTitle(),content,notify.getAfficheId(),datahandles,fileName);
		}catch(AjaxException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"发布新闻公告_发布信息"});
		}catch (Exception e) {
			e.printStackTrace();
			throw new AjaxException();
		}
	}

	
	/**
	 * @author:luosy
	 * @description:获取新闻公告默认附件大小限制
	 * @date : 2011-1-20
	 * @modifyer:
	 * @description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Long getDefAttSize()throws SystemException,ServiceException{
		try{
			String baseMsgSize=pcService.getConfigSize(ConfigModule.NOTIFY);
			if(baseMsgSize!=null && !baseMsgSize.equals("")){
			    return (new Long(baseMsgSize).longValue())*1024*1024;
			}else
				return null;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取新闻公告附件默认大小限制"});
		}
	}
	/**
	 * 获取新闻公告的数量
	 * author  taoji
	 * @return
	 * @date 2014-1-19 下午04:29:34
	 */
	public String getAfficheNum( String userId){
		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from ToaAfficheReceiver t where (t.afficheReceiverId=?  or t.afficheReceiverId='alluser')");
		Object[] obj = {userId};
		List<Integer> t = notifyDao.find(hql.toString(),obj);
		if(t!=null&&t.size()>0){
			return String.valueOf(t.get(0));
		}
		return null;	
	}
}

