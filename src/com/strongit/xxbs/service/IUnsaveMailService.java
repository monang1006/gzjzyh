package com.strongit.xxbs.service;

import com.strongit.xxbs.dto.MailInfo;
import com.strongit.xxbs.entity.TInfoBaseUnsaveMail;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IUnsaveMailService
{
	public TInfoBaseUnsaveMail getUnsaveMail(String id)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseUnsaveMail> getUnsaveMails(Page<TInfoBaseUnsaveMail> page)
			throws ServiceException,SystemException, DAOException;
	
	public void save(TInfoBaseUnsaveMail mail)
			throws ServiceException,SystemException, DAOException;
	
	public void saveMailInfo(MailInfo mail)
			throws ServiceException,SystemException, DAOException;
	
	public void delete(String id)
			throws ServiceException,SystemException, DAOException;
	
	public void saveToPubilsh()
			throws ServiceException,SystemException, DAOException;

}
