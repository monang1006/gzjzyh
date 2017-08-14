/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理新闻公告 附件manager类
 */
package com.strongit.oa.notifyattach;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAffiche;
import com.strongit.oa.bo.ToaAfficheAttach;
import com.strongit.oa.bo.ToaCalendar;
import com.strongit.oa.bo.ToaCalendarAttach;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * NotifyAttachManager公告附件
 * 
 * @author luosy
 * @version 1.0
 */
@Service
@Transactional
public class NotifyAttachManager {

	private GenericDAOHibernate<ToaAfficheAttach, java.lang.String> notifyAttachDao;

	@Autowired
	public void setSessionFactory(SessionFactory session) {
		notifyAttachDao = new GenericDAOHibernate<ToaAfficheAttach, java.lang.String>(
				session, ToaAfficheAttach.class);
	}
	
	/**
	 * author:luosy
	 * description:  保存附件
	 * modifyer:
	 * description:
	 * @param remind
	 */
	@Transactional
	public String saveAttach(String attachId,ToaAffiche notify) throws SystemException,ServiceException{
		try{
			ToaAfficheAttach attach = new ToaAfficheAttach();
			attach.setAttachId(attachId);
			attach.setToaAffiche(notify);
			notifyAttachDao.save(attach);
			return attach.getAfficheAttachId();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"新闻公告附件"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 删除公告附件
	 * modifyer:
	 * description:
	 * @param attachId 公共附件表对象的ID
	 */
	public void delAttach(String attachId) throws SystemException,ServiceException{
		try{
			List l= notifyAttachDao.findByProperty("attachId", attachId);
			if(null!=l){
				if(l.size()>0){
					ToaAfficheAttach attach = (ToaAfficheAttach)l.get(0);
					notifyAttachDao.delete(attach);
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"新闻公告附件"});
		}
	}
}
