package com.strongit.xxbs.service;

import java.util.List;

import com.strongit.oa.bo.ToaLog;
import com.strongit.xxbs.entity.TInfoBaseBulletin;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IMyLogService
{
	public ToaLog getMyLog(String blId)
			throws ServiceException,SystemException, DAOException;
	
	public List<ToaLog> getMyLogs()
			throws ServiceException,SystemException, DAOException;
	
	public Page<ToaLog> getMyLogs(Page<ToaLog> page)
			throws ServiceException,SystemException, DAOException;
	
	public void saveMyLog(ToaLog log)
			throws ServiceException,SystemException, DAOException;
	
	public String deleteMyLog(String blId)
			throws ServiceException,SystemException, DAOException;
	
	public Page<ToaLog> findMyLogByTitle(Page<ToaLog> page,
			String blTitle) throws ServiceException, SystemException,
			DAOException;

}
