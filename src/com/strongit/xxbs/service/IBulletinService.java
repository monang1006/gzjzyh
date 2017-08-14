package com.strongit.xxbs.service;

import java.util.List;

import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IBulletinService
{
	public TInfoBaseBulletin getBulletin(String blId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseBulletin> getBulletins()
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseBulletin> getBulletins(Page<TInfoBaseBulletin> page)
			throws ServiceException,SystemException, DAOException;
	
	public void saveBulletin(TInfoBaseBulletin bulletin)
			throws ServiceException,SystemException, DAOException;
	
	public String deleteBulletin(String blId)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseBulletin> findBulletinsByTitle(Page<TInfoBaseBulletin> page,
			String blTitle) throws ServiceException, SystemException,
			DAOException;

	public List<TInfoBaseBulletin> lastBulletins()
			throws ServiceException,SystemException, DAOException;
}
