package com.strongit.xxbs.service;

import java.util.List;

import com.strongit.xxbs.entity.TInfoBasePS;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IPSService
{
	public List<TInfoBasePS> getIPSs(String pubId)
			throws ServiceException,SystemException, DAOException;
	
	public void saveIPSs(String pubId, List<String> scIds)
			throws ServiceException,SystemException, DAOException;
	
	public void deleteIPSs(String pubId)
			throws ServiceException,SystemException, DAOException;

	public Boolean isExistByScId(String scId)
			throws ServiceException,SystemException, DAOException;
}
