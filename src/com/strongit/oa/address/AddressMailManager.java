package com.strongit.oa.address;

import java.util.Iterator;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAddressMail;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      个人通讯录邮件管理Manager
 */
@Service
@Transactional
public class AddressMailManager {

	GenericDAOHibernate<ToaAddressMail, String> mailDao;
	
	/**
	 * @param sessionFactory
	 * @roseuid 49503EE9002E
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		mailDao = new GenericDAOHibernate<ToaAddressMail, String>(sessionFactory,ToaAddressMail.class);
	}
	
	/**
	 * 批量删除邮件信息
	 */
	public void deleteMail(Set<ToaAddressMail> mail)throws SystemException,ServiceException{
		try {
			for(Iterator<ToaAddressMail> it = mail.iterator();it.hasNext();){
				mailDao.delete(it.next());
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,new Object[] {"联系人邮件"});
		}
	}
	/**
	 * 保存邮件信息
	 */
	public void addMail(ToaAddressMail mail)throws SystemException,ServiceException{
		try {
			mailDao.save(mail);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"联系人邮件"});
		}
	}
}
