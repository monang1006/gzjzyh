package com.strongit.xxbs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.entity.TInfoBaseAppointTo;
import com.strongit.xxbs.service.IAppointToService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional(readOnly = true)
public class AppointToService implements IAppointToService
{
	private GenericDAOHibernate<TInfoBaseAppointTo, String> atDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		atDao = new GenericDAOHibernate<TInfoBaseAppointTo, String>(
				sessionFactory, TInfoBaseAppointTo.class);
	}
	/**
	 * 根据约稿信息查询所有约稿用户对象
	 */
	public List<TInfoBaseAppointTo> getATs(String aptId)
			throws ServiceException, SystemException, DAOException
	{
		Criterion cr = Restrictions.eq("aptId", aptId);
		List<TInfoBaseAppointTo> rets = atDao.find(cr);
		return rets;
	}

	@Transactional(readOnly = false)
	public void saveATs(String aptId, List<String> orgIds)
			throws ServiceException, SystemException, DAOException
	{
		deleteATs(aptId);
		//List<TInfoBaseAppointTo> tos = new ArrayList<TInfoBaseAppointTo>();
		for(String orgId: orgIds)
		{
			TInfoBaseAppointTo to = new TInfoBaseAppointTo();
			to.setAptId(aptId);
			to.setOrgId(orgId);
			//tos.add(to);
			atDao.save(to);
		}
		//atDao.save(tos);
	}

	@Transactional(readOnly = false)
	public void deleteATs(String aptId) throws ServiceException,
			SystemException, DAOException
	{
		List<TInfoBaseAppointTo> rets = getATs(aptId);
		if(rets != null)
		{
			atDao.delete(rets);
		}
	}

	public String[] getAptIdsByOrgid(String orgId) throws ServiceException,
			SystemException, DAOException
	{
		Criterion cr1 = Restrictions.eq("orgId", orgId);
		List<TInfoBaseAppointTo> ats = atDao.find(cr1);
		String[] ids = new String[ats.size()];
		if(ats != null)
		{
			int i=0;
			for(TInfoBaseAppointTo one : ats)
			{
				ids[i] = one.getAptId();
				i++;
			}
		}

		return ids;
	}

}
