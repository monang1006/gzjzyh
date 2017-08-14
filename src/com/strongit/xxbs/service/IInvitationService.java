package com.strongit.xxbs.service;

import java.util.List;
import java.util.Map;

import com.strongit.xxbs.entity.TInfoBaseAppoint;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IInvitationService
{
	public TInfoBaseAppoint getAppoint(String aptId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseAppoint> getAppoints()
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseAppoint> getAppoints(Page<TInfoBaseAppoint> page)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseAppoint> getAppoints(Page<TInfoBaseAppoint> page,
			String orgId, Boolean isSinceToday)
			throws ServiceException,SystemException, DAOException;
		
	public void saveAppoint(TInfoBaseAppoint appoint, String[] orgIds)
			throws ServiceException,SystemException, DAOException;
	
	public void saveAppoint(TInfoBaseAppoint appoint)
			throws ServiceException,SystemException, DAOException;
	
	public String deleteAppoint(String aptId)
			throws ServiceException,SystemException, DAOException;
	
	/**
	 * 搜索
	 * @param seearch 约稿标题、发布时间、截止时间组成的Map
	 * @return
	 */
	public Page<TInfoBaseAppoint> findAppoints(Page<TInfoBaseAppoint> page,
			Map<String, String> search) throws ServiceException,
			SystemException, DAOException;
	
public List<TInfoBaseAppoint> lastAppoints()
		throws ServiceException,SystemException, DAOException;

public List<TInfoBaseAppoint> lastAppoints(String orgId)
	throws ServiceException,SystemException, DAOException;
}
