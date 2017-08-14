package com.strongit.xxbs.service;

import java.util.List;
import java.util.Map;

import com.strongit.xxbs.entity.TInfoBaseJournal;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IJournalService
{
	public TInfoBaseJournal getJoural(String jourId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseJournal> getJourals()
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseJournal> getJouralsByNoPublished()
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseJournal> getJourals(Page<TInfoBaseJournal> page)
			throws ServiceException,SystemException, DAOException;
	
	public void saveJoural(TInfoBaseJournal joural)
			throws ServiceException,SystemException, DAOException;
	
	public String deleteJoural(String jourId)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseJournal> findJourals(
			Page<TInfoBaseJournal> page, Map<String, String> search)
			throws ServiceException,SystemException, DAOException;

	public void setDefault(String jourId)
			throws ServiceException,SystemException, DAOException;
	
	public Map<String, String> getJourNames() throws DAOException,
	SystemException, ServiceException;
	
	public Map<String, String> getJourIdNames() throws DAOException,
		SystemException, ServiceException;
	
	public Boolean isExistByWtId(String wtId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseJournal> getJouralsByWtId(String wtId)
			throws ServiceException,SystemException, DAOException;

}
