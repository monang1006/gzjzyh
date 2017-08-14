package com.strongit.oa.freedomworkflow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TOaFreedomWorkflow;
import com.strongit.oa.freedomworkflow.define.FreedomWorkflowField;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class FreedomWorkflowService implements IFreedomWorkflowService
{

	private GenericDAOHibernate<TOaFreedomWorkflow, String> fwDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory session)
	{
		 fwDao = new GenericDAOHibernate<TOaFreedomWorkflow, String>(session, TOaFreedomWorkflow.class);
	}
	
	public TOaFreedomWorkflow getFreedomWorkflow(String fwId)
			throws ServiceException, SystemException
	{
		return fwDao.get(fwId);
	}

	public List<TOaFreedomWorkflow> getFreedomWorkflows(
			Map<FreedomWorkflowField, String> queries, Map<String, String> orders)
			throws ServiceException, SystemException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		
		if(queries != null)
		{
			String fwId = queries.get(FreedomWorkflowField.FW_ID);
			if(StringUtils.isNotEmpty(fwId))
			{
				if(fwId.contains(","))
				{
					String[] ids = fwId.split(",");
					crs.add(Restrictions.in(FreedomWorkflowField.FW_ID.val(), ids));
				}
				else
				{
					crs.add(Restrictions.eq(FreedomWorkflowField.FW_ID.val(), fwId));
				}
			}
			
			String fwTitle = queries.get("fwTitle");
			if(StringUtils.isNotEmpty(fwTitle))
			{
				crs.add(Restrictions.like("fwTitle", fwTitle, MatchMode.ANYWHERE));
			}
			
			String fwCreator = queries.get("fwCreator");
			if(StringUtils.isNotEmpty(fwCreator))
			{
				crs.add(Restrictions.eq("fwCreator", fwCreator));
			}
			
			String fwbusinessId = queries.get("fwbusinessId");
			if(StringUtils.isNotEmpty(fwbusinessId))
			{
				crs.add(Restrictions.eq("fwbusinessId", fwbusinessId));
			}
			
			String fwbusinessName = queries.get("fwbusinessName");
			if(StringUtils.isNotEmpty(fwbusinessName))
			{
				crs.add(Restrictions.like("fwbusinessName", fwbusinessName, MatchMode.ANYWHERE));
			}
			
			String fwFormId = queries.get("fwFormId");
			if(StringUtils.isNotEmpty(fwFormId))
			{
				crs.add(Restrictions.eq("fwFormId", fwFormId));
			}
			
			String fwStatus = queries.get("fwStatus");
			if(StringUtils.isNotEmpty(fwStatus))
			{
				crs.add(Restrictions.eq("fwStatus", fwStatus));
			}
		}
		
		return null;
	}

	public Page<TOaFreedomWorkflow> getFreedomWorkflows(
			Page<TOaFreedomWorkflow> page, Map<String, String> queries,
			Map<String, String> orders) throws ServiceException,
			SystemException
	{
		return null;
	}

	@Transactional(readOnly = false)
	public void saveFreedomWorkflow(TOaFreedomWorkflow fw)
			throws ServiceException, SystemException
	{
		fwDao.save(fw);
	}

	@Transactional(readOnly = false)
	public void deleteFreedomWorkflow(String fwId) throws ServiceException,
			SystemException
	{
		fwDao.delete(fwId);
	}

	@Transactional(readOnly = false)
	public void deleteFreedomWorkflows(String[] fwIds) throws ServiceException,
			SystemException
	{
		fwDao.delete(fwIds);
	}

}
