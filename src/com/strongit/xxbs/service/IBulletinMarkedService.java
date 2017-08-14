package com.strongit.xxbs.service;

import com.strongit.xxbs.entity.TInfoBaseBulletinMarked;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IBulletinMarkedService
{
	public TInfoBaseBulletinMarked getBM(String blId, String userId)
			throws ServiceException,SystemException, DAOException;
	
	public Boolean isRead(String blId, String userId)
			throws ServiceException,SystemException, DAOException;
	
	public void saveBM(String blId, String userId)
			throws ServiceException,SystemException, DAOException;
	
	public void deleteBM(String blId, String userId)
			throws ServiceException,SystemException, DAOException;

}
