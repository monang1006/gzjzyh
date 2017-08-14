package com.strongit.xxbs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.entity.TInfoBasePS;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.service.IPSService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional(readOnly = true)
public class PSService implements IPSService
{
	private GenericDAOHibernate<TInfoBasePS, String> psDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		psDao = new GenericDAOHibernate<TInfoBasePS, String>(
				sessionFactory, TInfoBasePS.class);
	}

	public List<TInfoBasePS> getIPSs(String pubId) throws ServiceException,
			SystemException, DAOException
	{
		Criterion cr = Restrictions.eq("TInfoBasePublish.pubId", pubId);
		return psDao.find(cr);
	}

	@Transactional(readOnly = false)
	public void saveIPSs(String pubId, List<String> scIds)
			throws ServiceException, SystemException, DAOException
	{
		deleteIPSs(pubId);
		TInfoBasePublish pub = new TInfoBasePublish();
		pub.setPubId(pubId);
		List<TInfoBasePS> pss = new ArrayList<TInfoBasePS>();
		for(String scId: scIds)
		{
			TInfoBasePS ps = new TInfoBasePS();
			ps.setScId(scId);
			ps.setTInfoBasePublish(pub);
			pss.add(ps);
		}
		psDao.save(pss);
	}

	@Transactional(readOnly = false)
	public void deleteIPSs(String pubId) throws ServiceException,
			SystemException, DAOException
	{
		List<TInfoBasePS> pss = getIPSs(pubId);
		psDao.delete(pss);
	}

	public Boolean isExistByScId(String scId) throws ServiceException,
			SystemException, DAOException
	{
		Boolean is = false;
		Criterion cr2 = Restrictions.eq("scId", scId);
		List<TInfoBasePS> rets = psDao.find(cr2);
		if(rets.size() > 0)
		{
			is = true;
		}
		return is;
	}

}
