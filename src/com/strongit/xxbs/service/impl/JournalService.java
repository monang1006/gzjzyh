package com.strongit.xxbs.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.entity.TInfoBaseIssue;
import com.strongit.xxbs.entity.TInfoBaseJournal;
import com.strongit.xxbs.service.IColumnService;
import com.strongit.xxbs.service.IIssueService;
import com.strongit.xxbs.service.IJournalService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.util.SpringContextUtil;

@Service
@Transactional(readOnly = true)
public class JournalService implements IJournalService
{
	private GenericDAOHibernate<TInfoBaseJournal, String> journalDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		journalDao = new GenericDAOHibernate<TInfoBaseJournal, String>(
				sessionFactory, TInfoBaseJournal.class);
	}

	public TInfoBaseJournal getJoural(String jourId) throws ServiceException,
			SystemException, DAOException
	{
		return journalDao.get(jourId);
	}

	public List<TInfoBaseJournal> getJourals() throws ServiceException,
			SystemException, DAOException
	{
		return journalDao.findAll();
	}

	public List<TInfoBaseJournal> getJouralsByNoPublished()
			throws ServiceException, SystemException, DAOException
	{
		List<TInfoBaseJournal> rets = journalDao.findAll();
		List<TInfoBaseJournal> rmJournals = new ArrayList<TInfoBaseJournal>();
		for(TInfoBaseJournal one : rets)
		{
			Set<TInfoBaseIssue> issues = one.getTInfoBaseIssues();
			Set<TInfoBaseIssue> rmIssues = new HashSet<TInfoBaseIssue>();
			for(TInfoBaseIssue row : issues)
			{
				if(Publish.PUBLISHED.equals(row.getIssIsPublish()))
				{
					rmIssues.add(row);
				}
			}
			issues.removeAll(rmIssues);
			one.setTInfoBaseIssues(issues);
			
			if(one.getTInfoBaseIssues().size() <= 0)
			{
				rmJournals.add(one);
			}
		}
		rets.removeAll(rmJournals);

		return rets;
	}

	public Page<TInfoBaseJournal> getJourals(Page<TInfoBaseJournal> page)
			throws ServiceException, SystemException, DAOException
	{
		return journalDao.findAll(page);
	}

	@Transactional(readOnly = false)
	public void saveJoural(TInfoBaseJournal joural) throws ServiceException,
			SystemException, DAOException
	{
		journalDao.save(joural);
	}

	@Transactional(readOnly = false)
	public String deleteJoural(String jourId) throws ServiceException,
			SystemException, DAOException
	{
		String flag = "";
		ApplicationContext ctx = SpringContextUtil.getApplicationContext();
		IIssueService issueService = (IIssueService) ctx.getBean("issueService");
		IColumnService columnService = (IColumnService) ctx.getBean("columnService");
		if(issueService.isExistByJourId(jourId) || columnService.isExistByJourId(jourId))
		{
			flag = "noDelete";
		}
		else
		{
			journalDao.delete(jourId);
			flag = "success";
		}
		return flag;
	}

	public Page<TInfoBaseJournal> findJourals(Page<TInfoBaseJournal> page,
			Map<String, String> search) throws ServiceException,
			SystemException, DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		
		Criterion cr = Restrictions.like("jourName", "%"+search.get("jourName")+"%");
		crs.add(cr);
		
		return journalDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
	}

	@Transactional(readOnly = false)
	public void setDefault(String jourId) throws ServiceException,
			SystemException, DAOException
	{
		List<TInfoBaseJournal> journals = 
				journalDao.findByProperty("jourIsDefault", Publish.DEFAULT);
		for(TInfoBaseJournal one: journals)
		{
			one.setJourIsDefault(Publish.NO_DEFAULT);
		}
		journalDao.save(journals);
		//journal.
	}
	
	public Map<String, String> getJourNames() throws DAOException,
	SystemException, ServiceException
		{
		Map<String, String> ret = new HashMap<String, String>();
		List<TInfoBaseJournal> lists = journalDao.findAll();
		if(lists != null)
		{
			for(TInfoBaseJournal jour : lists)
			{
				ret.put(jour.getJourId(), jour.getJourName());
			}
		}
		return ret;
		}

	public Map<String, String> getJourIdNames() throws DAOException,
			SystemException, ServiceException
	{
		Map<String, String> ret = new HashMap<String, String>();
		List<TInfoBaseJournal> lists = journalDao.findAll();
		if(lists != null)
		{
			for(TInfoBaseJournal jour : lists)
			{
				ret.put(jour.getJourId(), jour.getJourName());
			}
		}
		return ret;
	}

	public Boolean isExistByWtId(String wtId) throws ServiceException,
			SystemException, DAOException
	{
		Boolean is = false;
		Criterion cr2 = Restrictions.eq("TInfoBaseWordTemplate.wtId", wtId);
		List<TInfoBaseJournal> rets = journalDao.find(cr2);
		if(rets.size() > 0)
		{
			is = true;
		}
		return is;
	}

	public List<TInfoBaseJournal> getJouralsByWtId(String wtId) throws ServiceException,
			SystemException, DAOException {
		Criterion cr2 = Restrictions.eq("TInfoBaseWordTemplate.wtId", wtId);
		List<TInfoBaseJournal> rets = journalDao.find(cr2);
		return rets;
	}

}
