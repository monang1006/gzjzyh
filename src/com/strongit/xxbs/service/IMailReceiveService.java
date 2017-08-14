package com.strongit.xxbs.service;

import java.io.IOException;

import com.strongit.xxbs.dto.MailReceiveDto;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IMailReceiveService
{
	public MailReceiveDto get()
			throws ServiceException,SystemException, DAOException;
	
	public String get(String name)
			throws ServiceException,SystemException, DAOException;
	
	public void set(String name, String value)
			throws ServiceException,SystemException, DAOException;
	
	public void save()
			throws ServiceException,SystemException, DAOException, IOException;

	public void save(MailReceiveDto mr)
			throws ServiceException,SystemException, DAOException, IOException;

}
