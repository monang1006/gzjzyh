package com.strongit.xxbs.service;

import java.util.List;
import java.util.Map;

import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.entity.TInfoBaseScore;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IScoreItemService
{
	public TInfoBaseScore getScoreItem(String scId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseScore> getScoreItems()
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseScore> getScoreItems(String[] scIds)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseScore> getScoreItems(Page<TInfoBaseScore> page)
			throws ServiceException,SystemException, DAOException;
	
	public void saveScoreItem(TInfoBaseScore item)
			throws ServiceException,SystemException, DAOException;
	
	public String deleteScoreItem(String scId)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseScore> findScoreItem(Page<TInfoBaseScore> page,
			String scoreName)
	throws ServiceException,SystemException, DAOException;

}
