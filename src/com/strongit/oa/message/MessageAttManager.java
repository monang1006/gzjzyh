/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息附件 manager类
 */
package com.strongit.oa.message;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaMessage;
import com.strongit.oa.bo.ToaMessageAttach;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 处理消息附件类
 * @Create Date: 2009-12-7
 * @author luosy
 * @version 1.0
 */
@Service
@Transactional
public class MessageAttManager {
	
	private GenericDAOHibernate<ToaMessageAttach,String> msgAttDao;

	private IAttachmentService attachmentService;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		msgAttDao = new GenericDAOHibernate<ToaMessageAttach,String>(sessionFactory,ToaMessageAttach.class);
	}
	@Autowired
	public void setAttachmentService(IAttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}
	/**
	 * author:luosy
	 * description:保存附件
	 * modifyer:
	 * description:
	 * @param attachId  公共附件表对应ID
	 * @param msg  对应的消息对象
	 * @return 消息附件对象的ID
	 */
	@Transactional
	public String saveAttach(String attachId,ToaMessage msg) throws SystemException,ServiceException{
		try{
			ToaMessageAttach attach = new ToaMessageAttach();
			attach.setAttachId7(attachId);
			attach.setToaMessage(msg);
			msgAttDao.save(attach);
			return attach.getMessageAttachId();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"保存消息附件"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 删除附件
	 * modifyer:
	 * description:
	 * @param msgAtt 需要被删除的附件对象
	 */
	public void delAttach(ToaMessageAttach msgAtt)throws SystemException,ServiceException{
		try{
			List l= msgAttDao.findByProperty("attachId7", msgAtt.getAttachId7());
			if(l.size()>1){
				msgAttDao.delete(msgAtt);
			}else if(l.size()>0){
				attachmentService.deleteAttachment(msgAtt.getAttachId7());
				msgAttDao.delete(msgAtt);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除消息附件"});
		}
	}
	/**
	 * author:luosy
	 * description:删除附件
	 * modifyer:
	 * description:
	 * @param msgAttId需要被删除的附件对象的公共附件Id
	 * @param msgId
	 */
	public void delAttach(String attId,String msgId) throws SystemException,ServiceException{
		try{
			List all= msgAttDao.findByProperty("attachId7", attId);
			List<ToaMessageAttach> l= msgAttDao.find("from ToaMessageAttach t where t.attachId7 like '%"+attId+"%' and t.toaMessage.msgId like '%"+msgId+"%'");
			if(all.size()>1){
				msgAttDao.delete(l.get(0));
			}else if(all.size()>0){
				attachmentService.deleteAttachment(attId);
				msgAttDao.delete(l.get(0));
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"删除公共附件"});
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
	@Transactional(readOnly=true)
	public List getMessageAttach(String msgId) throws SystemException,ServiceException{
		try{
			List list = msgAttDao.find("from ToaMessageAttach t where t.toaMessage.msgId=?", msgId);
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找消息附件"});
		}
	}
	
	/**
	 * author: pengxq
	 * description: 得到某条记录的公共附件 列表对象
	 * modifyer:
	 * description:
	 * @param msgId
	 * @return
	 */
	@Transactional(readOnly=true)
	public List getAttachMent(String msgId) throws SystemException,ServiceException{
		try{
			List list=null;
			List attchMentList=new ArrayList();
			list=this.getMessageAttach(msgId);//该消息记录的所有附件
			ToaMessageAttach messageAttach=new ToaMessageAttach();
			if(list==null){
				return null;
			}
			for(int i=0;i<list.size();i++){
				messageAttach=(ToaMessageAttach) list.get(i);
				ToaAttachment toaAttachMent=attachmentService.getAttachmentById(messageAttach.getAttachId7());
				attchMentList.add(toaAttachMent);
			}
			return attchMentList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找公共附件"});
		}
	}
	
}
