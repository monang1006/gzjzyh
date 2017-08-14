package com.strongit.oa.freedomworkflow.service;

import java.util.List;
import java.util.Map;

import com.strongit.oa.bo.TOaFreedomWorkflow;
import com.strongit.oa.freedomworkflow.define.FreedomWorkflowField;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IFreedomWorkflowService
{
	
	public TOaFreedomWorkflow getFreedomWorkflow(String fwId)
			throws ServiceException,SystemException;
	
	public List<TOaFreedomWorkflow> getFreedomWorkflows(
			Map<FreedomWorkflowField, String> queries, Map<String, String> orders)
			throws ServiceException, SystemException;

	public Page<TOaFreedomWorkflow> getFreedomWorkflows(
			Page<TOaFreedomWorkflow> page, Map<String, String> queries,
			Map<String, String> orders) throws ServiceException,
			SystemException;
	
	public void saveFreedomWorkflow(TOaFreedomWorkflow fw)
			throws ServiceException, SystemException;
	
	public void deleteFreedomWorkflow(String fwId)
			throws ServiceException, SystemException;
	
	public void deleteFreedomWorkflows(String[] fwIds)
			throws ServiceException, SystemException;

}
