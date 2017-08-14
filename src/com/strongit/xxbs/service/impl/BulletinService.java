package com.strongit.xxbs.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongit.xxbs.service.IAppointToService;
import com.strongit.xxbs.service.IBulletinService;
import com.strongit.xxbs.service.IPublishService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class BulletinService implements IBulletinService
{
	private GenericDAOHibernate<TInfoBaseBulletin, String> bulDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		bulDao = new GenericDAOHibernate<TInfoBaseBulletin, String>(
				sessionFactory, TInfoBaseBulletin.class);
	}

	public TInfoBaseBulletin getBulletin(String blId) throws ServiceException,
			SystemException, DAOException
	{
		return bulDao.get(blId);
	}

	public List<TInfoBaseBulletin> getBulletins() throws ServiceException,
			SystemException, DAOException
	{
		return bulDao.findAll();
	}

	public Page<TInfoBaseBulletin> getBulletins(Page<TInfoBaseBulletin> page)
			throws ServiceException, SystemException, DAOException
	{
		return bulDao.findAll(page);
	}

	@Transactional(readOnly = false)
	public void saveBulletin(TInfoBaseBulletin bulletin)
			throws ServiceException, SystemException, DAOException
	{
		bulDao.save(bulletin);
	}

	@Transactional(readOnly = false)
	public String deleteBulletin(String blId) throws ServiceException,
			SystemException, DAOException
	{
		
		String flag = null;
		if(!blId.equals(null)){
		String a[] = blId.split(",");
		for(int i=0;i<a.length;i++){
			bulDao.delete(a[i]);
			flag = "success";
		} 
		}
		return flag;
	}

	/**
	 * 查询首页通知公告
	 */
	public Page<TInfoBaseBulletin> findBulletinsByTitle(
			Page<TInfoBaseBulletin> page, String blTitle)
			throws ServiceException, SystemException, DAOException
	{
		Criterion criterion = Restrictions.like("blTitle", "%"+blTitle+"%");
		return bulDao.findByCriteria(page, criterion);
	}

	public List<TInfoBaseBulletin> lastBulletins() throws ServiceException,
			SystemException, DAOException
	{ 
		@SuppressWarnings("unchecked")
		List<TInfoBaseBulletin> ret = bulDao.getSession()
				.createCriteria(TInfoBaseBulletin.class)
				.addOrder(Order.desc("blDate"))
				.setMaxResults(5)
				.list();
		return ret;
	}

}
