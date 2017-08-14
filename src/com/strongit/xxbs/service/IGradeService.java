package com.strongit.xxbs.service;

import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IGradeService
{
	public String get(String name)
			throws ServiceException,SystemException, DAOException;
	
	public void set(String name, String value)
			throws ServiceException,SystemException, DAOException;
	
	public void save()
			throws ServiceException,SystemException, DAOException;

}
