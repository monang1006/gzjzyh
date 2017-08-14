package com.strongit.xxbs.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.entity.TInfoBaseBulletinMarked;
import com.strongit.xxbs.service.IBulletinMarkedService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional(readOnly = true)
public class BulletinMarkedService implements IBulletinMarkedService
{
	private GenericDAOHibernate<TInfoBaseBulletinMarked, String> bmDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		bmDao = new GenericDAOHibernate<TInfoBaseBulletinMarked, String>(
				sessionFactory, TInfoBaseBulletinMarked.class);
	}

	public TInfoBaseBulletinMarked getBM(String blId, String userId)
			throws ServiceException, SystemException, DAOException
	{
		Criterion cr1 = Restrictions.eq("blId", blId);
		Criterion cr2 = Restrictions.eq("userId", userId);
		TInfoBaseBulletinMarked ret = null;
		List<TInfoBaseBulletinMarked> rets = 
				bmDao.find(new Criterion[]{cr1, cr2});
		if(rets.size() > 0)
		{
			ret = rets.get(0);
		}
		return ret;
	}

	public Boolean isRead(String blId, String userId) throws ServiceException,
			SystemException, DAOException
	{
		Boolean isRead = false;
		TInfoBaseBulletinMarked bm = getBM(blId, userId);
		if(bm != null)
		{
			isRead = true;
		}
		return isRead;
	}

	@Transactional(readOnly = false)
	public void saveBM(String blId, String userId) throws ServiceException,
			SystemException, DAOException
	{
		deleteBM(blId, userId);
		TInfoBaseBulletinMarked bm = new TInfoBaseBulletinMarked();
		bm.setBlId(blId);
		bm.setUserId(userId);
		bmDao.save(bm);
	}

	@Transactional(readOnly = false)
	public void deleteBM(String blId, String userId) throws ServiceException,
			SystemException, DAOException
	{
		TInfoBaseBulletinMarked ret = getBM(blId, userId);
		if(ret != null)
		{
			bmDao.delete(ret);
		}
	}

}
