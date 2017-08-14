package com.strongit.xxbs.service;

import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IMemberService
{
	public String getOrgIdByMail(String mail)
			throws ServiceException,SystemException, DAOException;

	public String getUserIdByMail(String mail)
			throws ServiceException,SystemException, DAOException;

	public void save(TUumsBaseUser user)
			throws ServiceException,SystemException, DAOException;
	
	public void update(TUumsBaseUser user)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TUumsBaseUser> getNotMailUser(Page<TUumsBaseUser> page)
			throws ServiceException,SystemException, DAOException;
	
	public void saveMailToUser(String userId, String mail)
			throws ServiceException,SystemException, DAOException;
}
