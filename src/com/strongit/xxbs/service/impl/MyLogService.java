package com.strongit.xxbs.service.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaLog;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongit.xxbs.service.IAppointToService;
import com.strongit.xxbs.service.IBulletinService;
import com.strongit.xxbs.service.IMyLogService;
import com.strongit.xxbs.service.IPublishService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class MyLogService implements IMyLogService
{
	private GenericDAOHibernate<ToaLog, String> logDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		logDao = new GenericDAOHibernate<ToaLog, String>(
				sessionFactory, ToaLog.class);
	}

	public ToaLog getMyLog(String blId) throws ServiceException,
			SystemException, DAOException
	{
		return logDao.get(blId);
	}

	public List<ToaLog> getMyLogs() throws ServiceException,
			SystemException, DAOException
	{
		return logDao.findAll();
	}

	public Page<ToaLog> getMyLogs(Page<ToaLog> page)
			throws ServiceException, SystemException, DAOException
	{
		return logDao.findAll(page);
	}

	@Transactional(readOnly = false)
	public void saveMyLog(ToaLog log)
			throws ServiceException, SystemException, DAOException
	{
		logDao.save(log);
	}

	@Transactional(readOnly = false)
	public String deleteMyLog(String blId) throws ServiceException,
			SystemException, DAOException
	{
		
		String flag = null;
		if(!blId.equals(null)){
		String a[] = blId.split(",");
		for(int i=0;i<a.length;i++){
			logDao.delete(a[i]);
			flag = "success";
		} 
		}
		return flag;
	}

	public Page<ToaLog> findMyLogByTitle(
			Page<ToaLog> page, String blTitle)
			throws ServiceException, SystemException, DAOException
	{
		Criterion criterion = Restrictions.like("logInfo", "%"+blTitle+"%");
		return logDao.findByCriteria(page, criterion);
	}


}
