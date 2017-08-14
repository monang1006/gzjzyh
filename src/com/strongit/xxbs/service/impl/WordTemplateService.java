package com.strongit.xxbs.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.entity.TInfoBaseJournal;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.entity.TInfoBaseWordTemplate;
import com.strongit.xxbs.service.IJournalService;
import com.strongit.xxbs.service.IPublishService;
import com.strongit.xxbs.service.IWordTemplateService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.util.SpringContextUtil;

@Service
@Transactional(readOnly = true)
public class WordTemplateService implements IWordTemplateService
{
	private GenericDAOHibernate<TInfoBaseWordTemplate, String> temDao;
	@Autowired
	private IJournalService journalService;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		temDao = new GenericDAOHibernate<TInfoBaseWordTemplate, String>(
				sessionFactory, TInfoBaseWordTemplate.class);
	}

	public TInfoBaseWordTemplate getTemplate(String wtId)
			throws ServiceException, SystemException, DAOException
	{
		return temDao.get(wtId);
	}

	public List<TInfoBaseWordTemplate> getTemplates() throws ServiceException,
			SystemException, DAOException
	{
		return temDao.findAll();
	}

	public Page<TInfoBaseWordTemplate> getTemplates(
			Page<TInfoBaseWordTemplate> page) throws ServiceException,
			SystemException, DAOException
	{
		return temDao.findAll(page);
	}

	@Transactional(readOnly = false)
	public void saveTemplate(TInfoBaseWordTemplate template)
			throws ServiceException, SystemException, DAOException
	{
		temDao.save(template);
	}

	@Transactional(readOnly = false)
	public String deleteTemplate(String wtId) throws ServiceException,
			SystemException, DAOException
	{
		String flag = null;
		String a[] = wtId.split(",");
		int c = 0;
		for(int i=0;i<a.length;i++){
			List<TInfoBaseJournal> rets = journalService.getJouralsByWtId(a[i]);
			if(rets.size() > 0)
			{
				flag = "noDelete";
				return flag;
			}
			else
			{
				c =c+1;
				//publishDao.delete(a[i]);
				flag = "success";
			}
		}
		if(c==a.length){
			for(int i=0;i<a.length;i++){
				temDao.delete(a[i]);
			}
		}
		return flag;
	}

	public Page<TInfoBaseWordTemplate> findTemplatesByTitle(
			Page<TInfoBaseWordTemplate> page, String title)
			throws ServiceException, SystemException, DAOException
	{
		Criterion criterion = Restrictions.like("wtTitle", "%"+title+"%");
		return temDao.findByCriteria(page, criterion);
	}

}
