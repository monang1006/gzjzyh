package com.strongit.xxbs.service;

import java.util.List;

import com.strongit.xxbs.entity.TInfoBaseAppointTo;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IAppointToService
{
	public List<TInfoBaseAppointTo> getATs(String aptId)
			throws ServiceException,SystemException, DAOException;
	
	public void saveATs(String aptId, List<String> orgIds)
			throws ServiceException,SystemException, DAOException;
	
	public void deleteATs(String aptId)
			throws ServiceException,SystemException, DAOException;
	
	public String[] getAptIdsByOrgid(String orgId)
			throws ServiceException,SystemException, DAOException;

}
