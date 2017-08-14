package com.strongit.xxbs.service;

import java.util.List;

import com.strongit.xxbs.entity.TInfoBaseColumn;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IColumnService
{
	public TInfoBaseColumn getColumn(String colId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseColumn> getColumns()
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseColumn> getColumns(Page<TInfoBaseColumn> page,
			String jourId)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseColumn> getColumns(Page<TInfoBaseColumn> page)
			throws ServiceException,SystemException, DAOException;
	
	public void saveColumn(TInfoBaseColumn column)
			throws ServiceException,SystemException, DAOException;
	
	public String deleteColumn(String colId)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseColumn> findColumnsByName(Page<TInfoBaseColumn> page,
			String colName) throws ServiceException, SystemException,
			DAOException;
	
	public Page<TInfoBaseColumn> findColumnsByNameAndtoId(Page<TInfoBaseColumn> page,
			String colName , String toId) throws ServiceException, SystemException,
			DAOException;
	
	public Boolean isExistByJourId(String jourId)
			throws ServiceException,SystemException, DAOException;
	
	public String getColumnSort()
			throws ServiceException,SystemException, DAOException;
	
	public Boolean isExistSore(String sore) throws ServiceException,
			SystemException, DAOException;
}
