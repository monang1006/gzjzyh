package com.strongit.xxbs.service;

import java.util.List;
import java.util.Map;

import com.strongit.xxbs.entity.TInfoBaseAdoption;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IAdoptionService
{
	public TInfoBaseAdoption getAdoption(String adoId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseAdoption> getAdoptions()
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseAdoption> getAdoptions(Page<TInfoBaseAdoption> page)
			throws ServiceException,SystemException, DAOException;
	
	public TInfoBaseAdoption findAdoptionByPubId(String pubId)
			throws ServiceException,SystemException, DAOException;
	
	public String[] findAdoptionByJvId(String jvId)
			throws ServiceException,SystemException, DAOException;
	
	public void saveAdoption(TInfoBaseAdoption adoption)
			throws ServiceException,SystemException, DAOException;
	
	public void deleteAdoption(String adoId)
			throws ServiceException,SystemException, DAOException;
	
	Map<String, TInfoBaseAdoption> getAdos()
			throws DAOException, SystemException, ServiceException;

}
