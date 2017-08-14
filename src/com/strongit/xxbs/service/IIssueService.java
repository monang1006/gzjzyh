package com.strongit.xxbs.service;

import java.util.List;

import com.strongit.xxbs.entity.TInfoBaseIssue;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IIssueService
{
	public TInfoBaseIssue getIssue(String issId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseIssue> getIssues()
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseIssue> getIssues(Page<TInfoBaseIssue> page)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseIssue> getIssues(Page<TInfoBaseIssue> page,
			String jourId)
			throws ServiceException,SystemException, DAOException;
	
	public void saveIssue(TInfoBaseIssue issue)
			throws ServiceException,SystemException, DAOException;
	
	public String deleteIssue(String issId)
			throws ServiceException,SystemException, DAOException;

	public TInfoBaseIssue lastIssue()
			throws ServiceException,SystemException, DAOException;

	public List<TInfoBaseIssue> lastIssues()
			throws ServiceException,SystemException, DAOException;
	
	public Boolean isExistByJourId(String jourId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseIssue> getNotPublishIssueByJour(String jourId) throws ServiceException,
			SystemException, DAOException;
	
	public List getIssuesMonth() throws ServiceException,
			SystemException, DAOException;
	
	public List getIssuesByjour(String jourId) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 首页每日要情
	 */
	public List<TInfoBaseIssue> lastIssuesByYQ() throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 首页南昌政务
	 */
	public List<TInfoBaseIssue> lastIssuesByZW() throws ServiceException,
	SystemException, DAOException;
}
