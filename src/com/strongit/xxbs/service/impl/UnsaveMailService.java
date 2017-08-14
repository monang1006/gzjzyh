package com.strongit.xxbs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.dto.MailInfo;
import com.strongit.xxbs.entity.TInfoBaseUnsaveMail;
import com.strongit.xxbs.service.IMemberService;
import com.strongit.xxbs.service.IPublishService;
import com.strongit.xxbs.service.IUnsaveMailService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = false)
public class UnsaveMailService implements IUnsaveMailService
{
	private GenericDAOHibernate<TInfoBaseUnsaveMail, String> umDao;
	private IMemberService memberService;
	private IPublishService publishService;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		umDao = new GenericDAOHibernate<TInfoBaseUnsaveMail, String>(
				sessionFactory, TInfoBaseUnsaveMail.class);
	}

	@Autowired
	public void setMemberService(IMemberService memberService)
	{
		this.memberService = memberService;
	}

	@Autowired
	public void setPublishService(IPublishService publishService)
	{
		this.publishService = publishService;
	}

	public TInfoBaseUnsaveMail getUnsaveMail(String id)
			throws ServiceException, SystemException, DAOException
	{
		return umDao.get(id);
	}

	public void save(TInfoBaseUnsaveMail mail) throws ServiceException,
			SystemException, DAOException
	{
		umDao.save(mail);
	}

	public void delete(String id) throws ServiceException, SystemException,
			DAOException
	{
		umDao.delete(id);
	}
	
	private Boolean isNotExistMailUid(String uid)
	{
		Boolean is = true;
		 List<TInfoBaseUnsaveMail> rets = umDao.findByProperty("mailUid", uid);
		 if(rets.size() > 0)
		 {
			 is = false;
		 }
		return is;
	}

	public void saveMailInfo(MailInfo one) throws ServiceException,
			SystemException, DAOException
	{
		if(isNotExistMailUid(one.getMailUid()))
		{
			TInfoBaseUnsaveMail mail = new TInfoBaseUnsaveMail();
			mail.setSentDate(one.getDate());
			mail.setSubject(one.getSubject());	
			mail.setMailUid(one.getMailUid());
			mail.setMailAddress(one.getMailUrl());
			mail.setContent(one.getContent());
			umDao.save(mail);
		}
	}

	public Page<TInfoBaseUnsaveMail> getUnsaveMails(
			Page<TInfoBaseUnsaveMail> page) throws ServiceException,
			SystemException, DAOException
	{
		return umDao.findAll(page);
	}
	
	private List<String> getNotUserMail()
	{
		List<String> mails = new ArrayList<String>();
		String hql = "select mailAddress from TInfoBaseUnsaveMail group by mailAddress";
		List<?> rets = umDao.find(hql, new Object[0]);
		for(Object one : rets)
		{
			mails.add((String) one);
		}
		return mails;
	}
	
	private List<TInfoBaseUnsaveMail> getUnsaveMailByMail(String mailAddress)
	{
		return umDao.findByProperty("mailAddress", mailAddress);
	}
	/**
	 * 保存异常邮件
	 */
	public void saveToPubilsh() throws ServiceException, SystemException,
			DAOException
	{
		List<String> mails = getNotUserMail();
		for(String m : mails)
		{
			String userId = memberService.getUserIdByMail(m);
			if(!"".equals(userId))
			{
				String orgId = memberService.getOrgIdByMail(m);
				for(TInfoBaseUnsaveMail one : getUnsaveMailByMail(m))
				{
					MailInfo info = new MailInfo();
					info.setContent(one.getContent());
					info.setDate(one.getSentDate());
					info.setMailUid(one.getMailUid());
					info.setMailUrl(one.getMailAddress());
					info.setOrgId(orgId);
					info.setSubject(one.getSubject());
					info.setUserId(userId);
					publishService.saveMailInfo(info);
					delete(one.getId());
				}
			}
		}
	}

}
