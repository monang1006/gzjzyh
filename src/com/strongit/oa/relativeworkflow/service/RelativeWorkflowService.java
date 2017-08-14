package com.strongit.oa.relativeworkflow.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.TOaRelativeWorkflow;
import com.strongit.oa.relativeworkflow.ProcessInstanceDto;
import com.strongit.uums.security.UserInfo;
import com.strongit.workflow.workflowInterface.IProcessInstanceService;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * @author 钟伟
 * @version 1.0
 * @date 2013-12-13
 */
@Service
@Transactional(readOnly = true)
public class RelativeWorkflowService implements IRelativeWorkflowService
{
	private GenericDAOHibernate<TOaRelativeWorkflow, String> rwDao;
	
	@Autowired
	private IProcessInstanceService piSrv;
	
	@Autowired
	private ITaskService taskSrv;

	
	@Autowired
	public void setSessionFactory(SessionFactory session)
	{
		rwDao = new GenericDAOHibernate<TOaRelativeWorkflow, String>(session,
				TOaRelativeWorkflow.class);
	}
	
	/* (non-Javadoc)
	 * @see com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService#getRelativeWorkflow(java.lang.String)
	 */
	public TOaRelativeWorkflow getRelativeWorkflow(String rwId)
			throws ServiceException, SystemException
	{
		Assert.hasLength(rwId, "关联流程信息ID不能为空");
		
		return rwDao.get(rwId);
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService#getRelativeWorkflows(java.lang.String)
	 */
	public List<TOaRelativeWorkflow> getRelativeWorkflows(Long piId)
			throws ServiceException, SystemException
	{
		Assert.notNull(piId, "流程实例ID不能为空");
		
		Criterion cr1 = Restrictions.eq("piId", piId);
		Criterion cr2 = Restrictions.eq("piRefId", piId);
		Disjunction dj = Restrictions.disjunction();
		dj.add(cr1);
		dj.add(cr2);
		return rwDao.find(dj);
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService#getRelativeWorkflows(com.strongmvc.orm.hibernate.Page, java.lang.String)
	 */
	public Page<TOaRelativeWorkflow> getRelativeWorkflows(
			Page<TOaRelativeWorkflow> page, Long piId)
			throws ServiceException, SystemException
	{
		Assert.notNull(page);
		Assert.notNull(piId, "流程实例ID不能为空");
		
		Criterion cr1 = Restrictions.eq("piId", piId);
		Criterion cr2 = Restrictions.eq("piRefId", piId);
		Disjunction dj = Restrictions.disjunction();
		dj.add(cr1);
		dj.add(cr2);
		return rwDao.findByCriteria(page, dj);
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService#getRelativeWorkflowsBySql(java.lang.Long)
	 */
	public List<ProcessInstanceDto> getRelativeWorkflowsBySql(Long piId)
			throws ServiceException, SystemException	{
		Assert.notNull(piId, "流程实例ID不能为空");
		
		String sql = "select j.id_,j.start_,j.name_,j.business_name_,j.start_user_id_,j.start_user_name_ from T_OA_RELATIVE_WORKFLOW t join JBPM_PROCESSINSTANCE j on (t.pi_ref_id=j.id_ and t.pi_id=?) or (t.pi_id=j.id_ and t.pi_ref_id=?) order by j.start_ desc";
		List<?> rets = rwDao.getSession()
			.createSQLQuery(sql)
			.setLong(0, piId)
			.setLong(1, piId)
			.list();
		List<ProcessInstanceDto> pis = new ArrayList<ProcessInstanceDto>();

		for(Object obj : rets)
		{
			ProcessInstanceDto pi = new ProcessInstanceDto();
			Object[] row = (Object[]) obj;
			BigDecimal pid = (BigDecimal) row[0];
			pi.setProcessInstanceId(pid.longValue());
			
			Object startDate = row[1];
			Date sDate = null;
			if(startDate instanceof String)
			{
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					sDate = df.parse(String.valueOf(startDate));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			else
			{
				sDate = (Date) startDate;
			}
			if(sDate != null)
			{
				pi.setProcessStartDate(sDate);
			}
			
			pi.setProcessName((String) row[2]);
			pi.setBusinessName((String) row[3]);
			pi.setStartUserId((String) row[4]);
			pi.setStartUserName((String) row[5]);
			pis.add(pi);
		}
		return pis;
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService#saveRelativeWorkflow(com.strongit.oa.bo.TOaRelativeWorkflow)
	 */
	@Transactional(readOnly = false)
	public void saveRelativeWorkflow(TOaRelativeWorkflow rw)
			throws ServiceException, SystemException
	{
		Assert.notNull(rw);
		
		if(!rw.getPiId().equals(rw.getPiRefId()))
		{
			rwDao.save(rw);
		}
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService#deleteRelativeWorkflow(java.lang.String)
	 */
	@Transactional(readOnly = false)
	public void deleteRelativeWorkflow(String rwId) throws ServiceException,
			SystemException
	{
		Assert.hasLength(rwId, "关联流程信息ID不能为空");
		
		rwDao.delete(rwId);
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService#deleteRelativeWorkflows(java.lang.String[])
	 */
	@Transactional(readOnly = false)
	public void deleteRelativeWorkflows(String[] rwIds) throws ServiceException,
			SystemException
	{
		Assert.notEmpty(rwIds, "关联流程信息ID数组不能为空");
		
		rwDao.delete(rwIds);
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService#deleteRelativeWorkflowByPiId(java.lang.String)
	 */
	@Transactional(readOnly = false)
	public void deleteRelativeWorkflowByPiId(Long piId)
			throws ServiceException, SystemException
	{
		Assert.notNull(piId, "流程实例ID不能为空");
		
		List<TOaRelativeWorkflow> rws = getRelativeWorkflows(piId);
		rwDao.delete(rws);
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService#getProcessInstances(int, int, java.lang.String, java.lang.Long)
	 */
	public Page<ProcessInstanceDto> getProcessInstances(
			int curPage, int unitPage, String userId, Long curPiId) throws ServiceException,
			SystemException
	{
		List<String> toSelectItems = new ArrayList<String>();
		toSelectItems.add("processInstanceId");
		toSelectItems.add("processName");
		toSelectItems.add("businessName");
		toSelectItems.add("processTypeName");
		toSelectItems.add("startUserId");
		toSelectItems.add("startUserName");
		toSelectItems.add("processStartDate");
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("hasHandlerId", userId);
		paramsMap.put("processSuspend", "0");
		
		Map<String, String> orderMap = new HashMap<String, String>();
		orderMap.put("processStartDate", "1");
			
		String customQuery = " @processInstanceId not in (" + curPiId + ") ";

		
		Page<Object[]> tmpPage = new Page<Object[]>(unitPage, true);
		tmpPage.setPageNo(curPage);
		tmpPage = piSrv.getProcessInstanceByConditionForPage(tmpPage, toSelectItems, paramsMap, orderMap, null, null, customQuery, null);
		
		Page<ProcessInstanceDto> page = new Page<ProcessInstanceDto>(unitPage, true);
		List<ProcessInstanceDto> rets = new ArrayList<ProcessInstanceDto>();
		
		try
		{
			BeanUtils.copyProperties(page, tmpPage);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		for(Object[] one : tmpPage.getResult())
		{
			ProcessInstanceDto pi = new ProcessInstanceDto();
			pi.setProcessInstanceId((Long) one[0]);
			pi.setProcessName((String) one[1]);
			pi.setBusinessName((String) one[2]);
			pi.setProcessTypeName((String) one[3]);
			pi.setStartUserId((String) one[4]);
			pi.setStartUserName((String) one[5]);
			pi.setProcessStartDate((Date) one[6]);
			rets.add(pi);
		}
		page.setResult(rets);
		return page;
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService#saveRelativeWorkflow(java.lang.Long, java.lang.Long[])
	 */
	@Transactional(readOnly = false)
	public void saveRelativeWorkflow(Long piId, String[] piRefIds)
			throws ServiceException, SystemException
	{
		deleteRelativeWorkflowByPiId(piId);
		
		Date now = new Date();
		for(int i=0,len=piRefIds.length;i<len;i++)
		{
			TOaRelativeWorkflow rw = new TOaRelativeWorkflow();
			rw.setPiId(piId);
			rw.setPiRefId(Long.valueOf(piRefIds[i]));
			rw.setRwDate(now);
			saveRelativeWorkflow(rw);
		}
	}

	public Long getTaskId(String userId, Long piId) throws ServiceException,
			SystemException
	{
		List<String> toSelectItems = new ArrayList<String>();
		toSelectItems.add("taskId");
		toSelectItems.add("taskName");
		toSelectItems.add("taskStartDate");
		toSelectItems.add("taskEndDate");
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processInstanceId", piId);
		paramsMap.put("handlerId", userId);
		
		Map<String, String> orderMap = new HashMap<String, String>();
		orderMap.put("taskStartDate", "0");

		List<Object[]> objs = taskSrv.getTaskInfosByConditionForList(toSelectItems, paramsMap, orderMap, null, null, null, null);

		Long taskId = 0L;
		if(objs.size() > 0)
		{
			Object[] obj = objs.get(objs.size() - 1);
			if(obj[3] == null && objs.size() > 1)
			{
				taskId = (Long) objs.get(objs.size() - 2)[0];
			}
			else
			{
				taskId = (Long) objs.get(objs.size() - 1)[0];
			}
		}

		return taskId;
	}

	public Map<Long, ProcessInstanceDto> getProcessInstances(List<Long> piIds)
			throws ServiceException, SystemException
	{
		List<String> toSelectItems = new ArrayList<String>();
		toSelectItems.add("processInstanceId");
		toSelectItems.add("processName");
		toSelectItems.add("businessName");
		toSelectItems.add("processTypeName");
		toSelectItems.add("startUserId");
		toSelectItems.add("startUserName");
		toSelectItems.add("processStartDate");
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processInstanceId", piIds);
		
		Map<String, String> orderMap = new HashMap<String, String>();
		orderMap.put("processStartDate", "1");
		
		List<Object[]> tmpList = new ArrayList<Object[]>();
		tmpList = piSrv.getProcessInstanceByConditionForList(toSelectItems, paramsMap, orderMap, null, null, null, null);
		
		List<ProcessInstanceDto> rets = new ArrayList<ProcessInstanceDto>();
		
		for(Object[] one : tmpList)
		{
			ProcessInstanceDto pi = new ProcessInstanceDto();
			pi.setProcessInstanceId((Long) one[0]);
			pi.setProcessName((String) one[1]);
			pi.setBusinessName((String) one[2]);
			pi.setProcessTypeName((String) one[3]);
			pi.setStartUserId((String) one[4]);
			pi.setStartUserName((String) one[5]);
			pi.setProcessStartDate((Date) one[6]);
			rets.add(pi);
		}
		
		Map<Long, ProcessInstanceDto> map = new HashMap<Long, ProcessInstanceDto>();
		for(ProcessInstanceDto one : rets)
		{
			map.put(Long.valueOf(one.getProcessInstanceId()), one);
		}
		return map;
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.relativeworkflow.service.IRelativeWorkflowService#ProcessInstanceIsSuspend(java.lang.Long)
	 */
	public boolean ProcessInstanceIsSuspend(Long piId) throws ServiceException,
			SystemException 
	{
		boolean isSuspend = false;
		
		List<String> toSelectItems = new ArrayList<String>();
		toSelectItems.add("processSuspend");
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processInstanceId", piId);

		List<Object[]> objs = taskSrv.getTaskInfosByConditionForList(toSelectItems, paramsMap, null, null, null, null, null);
		if(objs.size() >= 1)
		{
			Object obj = objs.get(0);
			isSuspend = (Boolean) obj;
		}
		
		return isSuspend;
	}

}
