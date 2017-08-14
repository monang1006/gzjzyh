package com.strongit.xxbs.service.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.entity.TInfoBaseIssue;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.service.IIssueService;
import com.strongit.xxbs.service.IPublishService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.util.SpringContextUtil;

@Service
@Transactional(readOnly = true)
public class IssueService implements IIssueService
{
	private GenericDAOHibernate<TInfoBaseIssue, String> issueDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		issueDao = new GenericDAOHibernate<TInfoBaseIssue, String>(
				sessionFactory, TInfoBaseIssue.class);
	}

	public TInfoBaseIssue getIssue(String issId) throws ServiceException,
			SystemException, DAOException
	{
		return issueDao.get(issId);
	}

	public List<TInfoBaseIssue> getIssues() throws ServiceException,
			SystemException, DAOException
	{
		@SuppressWarnings("unchecked")
		List<TInfoBaseIssue> rets = issueDao.getSession()
				.createCriteria(TInfoBaseIssue.class)
				.addOrder(Order.desc("issDate"))
				.list();

		return rets;
	}

	public Page<TInfoBaseIssue> getIssues(Page<TInfoBaseIssue> page,
			String jourId) throws ServiceException, SystemException,
			DAOException
	{
		Criterion cr = Restrictions.eq("TInfoBaseJournal.jourId", jourId);		
		return issueDao.findByCriteria(page, cr);
	}

	public Page<TInfoBaseIssue> getIssues(Page<TInfoBaseIssue> page)
			throws ServiceException, SystemException, DAOException
	{
		return issueDao.findAll(page);
	}

	@Transactional(readOnly = false)
	public void saveIssue(TInfoBaseIssue issue) throws ServiceException,
			SystemException, DAOException
	{
		issueDao.save(issue);
	}

	@Transactional(readOnly = false)
	public String deleteIssue(String issId) throws ServiceException,
			SystemException, DAOException
	{
		String flag = null;
		String a[] = issId.split(",");
		int c = 0;
		ApplicationContext ctx = SpringContextUtil.getApplicationContext();
		IPublishService publishService = (IPublishService) ctx.getBean("publishService");
		for(int i=0;i<a.length;i++){
			List<TInfoBasePublish> rets = publishService.findPublishsByIssId(a[i]);
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
			issueDao.delete(a[i]);
			}
		}
		return flag;
	}

	public TInfoBaseIssue lastIssue() throws ServiceException, SystemException,
			DAOException
	{
		@SuppressWarnings("unchecked")
		List<TInfoBaseIssue> rets = issueDao.getSession()
				.createCriteria(TInfoBaseIssue.class)
				//.add(Restrictions.eq("issIsPublish", Publish.PUBLISHED))
				.addOrder(Order.desc("issDate"))
				.setMaxResults(1)
				.list();
		if(rets.size()>0){
		return rets.get(0);
		}
		return null;
	}

	public List<TInfoBaseIssue> lastIssues() throws ServiceException,
			SystemException, DAOException
	{
		@SuppressWarnings("unchecked")
		List<TInfoBaseIssue> ret = issueDao.getSession()
				.createCriteria(TInfoBaseIssue.class)
				.add(Restrictions.eq("issIsPublish", Publish.PUBLISHED))
				.addOrder(Order.desc("TInfoBaseJournal.jourId"))
				.addOrder(Order.desc("issNumber"))
				.setMaxResults(10)
				.list();
		return ret;
	}
	/**
	 * 首页每日要情
	 */
	public List<TInfoBaseIssue> lastIssuesByYQ() throws ServiceException,
	SystemException, DAOException
	{
		String hql = "from TInfoBaseIssue where issIsPublish='1' " +
				"and TInfoBaseJournal.jourName='每日要情' order by  issNumber desc ";
	/*@SuppressWarnings("unchecked")
	List<TInfoBaseIssue> ret = issueDao.getSession()
			.createCriteria(TInfoBaseIssue.class)
			.add(Restrictions.eq("issIsPublish", Publish.PUBLISHED))
			.add(Restrictions.eq("TInfoBaseJournal.jourName", "每日要情"))
			.addOrder(Order.desc("TInfoBaseJournal.jourId"))
			.addOrder(Order.desc("issNumber"))
			.setMaxResults(10)
			.list();*/
	@SuppressWarnings("unchecked")
	List<TInfoBaseIssue> ret = issueDao.find(hql, new Object[]{});
	return ret;
	}
	
	/**
	 * 首页南昌政务
	 */
	public List<TInfoBaseIssue> lastIssuesByZW() throws ServiceException,
	SystemException, DAOException
	{
		String hql = "from TInfoBaseIssue where issIsPublish='1' " +
		"and TInfoBaseJournal.jourName='南昌政务' order by  issNumber desc ";
		/*@SuppressWarnings("unchecked")
		List<TInfoBaseIssue> ret = issueDao.getSession()
			.createCriteria(TInfoBaseIssue.class)
			.add(Restrictions.eq("issIsPublish", Publish.PUBLISHED))
			.add(Restrictions.eq("TInfoBaseJournal.jourName", "每日要情"))
			.addOrder(Order.desc("TInfoBaseJournal.jourId"))
			.addOrder(Order.desc("issNumber"))
			.setMaxResults(10)
			.list();*/
		@SuppressWarnings("unchecked")
		List<TInfoBaseIssue> ret = issueDao.find(hql, new Object[]{});
		return ret;
	}

	public Boolean isExistByJourId(String jourId) throws ServiceException,
			SystemException, DAOException
	{
		Boolean is = false;
		Criterion cr2 = Restrictions.eq("TInfoBaseJournal.jourId", jourId);
		List<TInfoBaseIssue> rets = issueDao.find(cr2);
		if(rets.size() > 0)
		{
			is = true;
		}
		return is;
	}
	
	public List<TInfoBaseIssue> getNotPublishIssueByJour(String jourId) throws ServiceException,
	SystemException, DAOException
		{
		@SuppressWarnings("unchecked")
		List<TInfoBaseIssue> ret = issueDao.getSession()
				.createCriteria(TInfoBaseIssue.class)
				.add(Restrictions.eq("TInfoBaseJournal.jourId", jourId))
				.add(Restrictions.eq("issIsPublish", "0"))
				.addOrder(Order.desc("issDate"))
				.list();
		return ret;
		}

	public List getIssuesMonth() throws ServiceException,
	SystemException, DAOException
	{
		String sql = "SELECT I.ISS_ID,I.JOUR_ID,SUBSTR(I.ISS_TIME, 0, 7) FROM STRONGINFO.T_INFO_BASE_ISSUE I ";
		Query query = issueDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
	}
	
	public List getIssuesByjour(String jourId) throws ServiceException,
	SystemException, DAOException
	{
		@SuppressWarnings("unchecked")
		List<TInfoBaseIssue> ret = issueDao.getSession()
				.createCriteria(TInfoBaseIssue.class)
				.add(Restrictions.eq("TInfoBaseJournal.jourId", jourId))
				.addOrder(Order.desc("issDate"))
				.list();
		return ret;
	}
}
