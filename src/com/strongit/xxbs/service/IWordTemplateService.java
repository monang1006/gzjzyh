package com.strongit.xxbs.service;

import java.util.List;

import com.strongit.xxbs.entity.TInfoBaseWordTemplate;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IWordTemplateService
{
	public TInfoBaseWordTemplate getTemplate(String wtId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseWordTemplate> getTemplates()
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseWordTemplate> getTemplates(Page<TInfoBaseWordTemplate> page)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseWordTemplate> findTemplatesByTitle(Page<TInfoBaseWordTemplate> page, String title)
			throws ServiceException,SystemException, DAOException;
	
	public void saveTemplate(TInfoBaseWordTemplate template)
			throws ServiceException,SystemException, DAOException;
	
	public String deleteTemplate(String wtId)
			throws ServiceException,SystemException, DAOException;

}
