package com.strongit.oa.freedomworkflow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.TOaFreedomWorkflow;
import com.strongit.oa.bo.TOaFreedomWorkflowTask;
import com.strongit.oa.bo.ToaMobileReplyMessage;
import com.strongit.oa.common.remind.Constants;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.freedomworkflow.define.FreedomWorkflowStatus;
import com.strongit.oa.im.IMManager;
import com.strongit.oa.message.IMessageService;
import com.strongit.oa.mymail.IMailService;
import com.strongit.oa.sms.IsmsService;
import com.strongit.oa.util.GlobalBaseData;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class FreedomWorkflowTaskService implements IFreedomWorkflowTaskService
{

	private GenericDAOHibernate<TOaFreedomWorkflowTask, String> ftDao;
	
	@Autowired
	private IFreedomWorkflowService fwSrv;
	
	@Autowired
	private IUserService userSrv;
	
	@Autowired
	private IMessageService msgService;// 内部消息发送方式

	@Autowired
	private IMailService mailService;// 邮件发送方式

	@Autowired
	private IsmsService smsService;// 手机短信发送方式

	@Autowired
	private IMManager rtxService;// rtx发送方式

	
	@Autowired
	public void setSessionFactory(SessionFactory session)
	{
		 ftDao = new GenericDAOHibernate<TOaFreedomWorkflowTask, String>(session, TOaFreedomWorkflowTask.class);
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#getFreedomWorkflowTask(java.lang.String)
	 */
	public TOaFreedomWorkflowTask getFreedomWorkflowTask(String ftId)
			throws ServiceException, SystemException
	{
		Assert.hasLength(ftId, "任务Id不能为空");
		return ftDao.get(ftId);
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#getFreedomWorkflowTasks(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<TOaFreedomWorkflowTask> getFreedomWorkflowTasks(
			String handler, String ftStatus)
			throws ServiceException, SystemException
	{
		return ftDao.getSession()
				.createCriteria(TOaFreedomWorkflowTask.class)
				.add(Restrictions.eq("ftStatus", FreedomWorkflowStatus.FT_PENDING.val()))
				.add(Restrictions.eq("ftHandler", handler))
				.addOrder(Order.asc("ftSequence"))
				.list();
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#getNextTask(java.lang.String)
	 */
	public TOaFreedomWorkflowTask getNextTask(String curFtId)
			throws ServiceException, SystemException
	{
		Assert.hasLength(curFtId, "当前任务Id不能为空");
		
		TOaFreedomWorkflowTask task = getFreedomWorkflowTask(curFtId);
		TOaFreedomWorkflowTask next = null;
		
		if(task != null)
		{
			String fwId= task.getTOaFreedomWorkflow().getFwId();
			Long nextSequence = task.getFtSequence() + 1;
			next = (TOaFreedomWorkflowTask) ftDao.getSession()
				.createCriteria(TOaFreedomWorkflowTask.class)
				.add(Restrictions.eq("TOaFreedomWorkflow.fwId", fwId))
				.add(Restrictions.eq("ftSequence", nextSequence))
				.setMaxResults(1)
				.uniqueResult();
		}
		return next;
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#saveFreedomWorkflowTasks(com.strongit.oa.bo.TOaFreedomWorkflow, java.lang.String, java.lang.String)
	 */
	@Transactional(readOnly = false)
	public synchronized void saveFreedomWorkflowTasks(TOaFreedomWorkflow fw,
			String firstTaskMemo, String jsonHandles) throws ServiceException,
			SystemException	{
		Assert.notNull(fw, "流程信息不能为空");
		Assert.hasLength(jsonHandles, "jsonHandles任务信息不能为空");
		
		fwSrv.saveFreedomWorkflow(fw);
		Date date = new Date();
		User user = userSrv.getCurrentUser();
		
		List<TOaFreedomWorkflowTask> tasks = new ArrayList<TOaFreedomWorkflowTask>();
		TOaFreedomWorkflowTask firstTask = new TOaFreedomWorkflowTask();
		firstTask.setFtTitle(fw.getFwTitle());
		firstTask.setFtHandler(user.getUserId());
		firstTask.setFtStartTime(date);
		firstTask.setFtEndTime(date);
		firstTask.setFtSequence(1L);
		firstTask.setFtMemo(firstTaskMemo);
		firstTask.setFtStatus(FreedomWorkflowStatus.FT_DONE.val());
		firstTask.setTOaFreedomWorkflow(fw);
		tasks.add(firstTask);
		
		JSONArray handles = JSONArray.fromObject(jsonHandles);
		for(int i=0,len=handles.size();i<len;i++)
		{
			Long sequence = Long.valueOf(i + 2);		
			
			JSONObject obj = handles.getJSONObject(i);
			TOaFreedomWorkflowTask task = (TOaFreedomWorkflowTask) JSONObject
					.toBean(obj, TOaFreedomWorkflowTask.class);
			task.setTOaFreedomWorkflow(fw);
			task.setFtSequence(sequence);
			task.setFtStatus(FreedomWorkflowStatus.FT_NOT_START.val());
			
			if(i== 0)
			{
				task.setFtStatus(FreedomWorkflowStatus.FT_PENDING.val());
				task.setFtStartTime(date);
			}

			tasks.add(task);
		}
		ftDao.save(tasks);
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#editFreedomWorkflowTasks(java.lang.String, java.lang.String)
	 */
	@Transactional(readOnly = false)
	public synchronized void editFreedomWorkflowTasks(String fwId, String jsonHandles)
			throws ServiceException, SystemException
	{
		Assert.hasLength(fwId, "流程id不能为空");
		Assert.hasLength(jsonHandles, "jsonHandles任务信息不能为空");
		
		deleteFreedomWorkflowNotStartTasks(fwId);
		TOaFreedomWorkflow fw = new TOaFreedomWorkflow();
		fw.setFwId(fwId);

		int donePendingNum = donePendingTaskNum(fwId);
		
		List<TOaFreedomWorkflowTask> tasks = new ArrayList<TOaFreedomWorkflowTask>();
		JSONArray handles = JSONArray.fromObject(jsonHandles);
		for(int i=0,len=handles.size();i<len;i++)
		{
			Long sequence = Long.valueOf(i + donePendingNum + 1);
//			if(donePendingNum > 0){
//				sequence = sequence + 1;
//			}			
			
			JSONObject obj = handles.getJSONObject(i);
			TOaFreedomWorkflowTask task = (TOaFreedomWorkflowTask) JSONObject
					.toBean(obj, TOaFreedomWorkflowTask.class);
			task.setTOaFreedomWorkflow(fw);
			task.setFtSequence(sequence);
			task.setFtStatus(FreedomWorkflowStatus.FT_NOT_START.val());
			
			if(i== 0 && !hasPendingTask(fwId))
			{
				task.setFtStatus(FreedomWorkflowStatus.FT_PENDING.val());
				task.setFtStartTime(new Date());
			}

			tasks.add(task);
		}
		ftDao.save(tasks);
	}
	
	/**
	 * 是否有处理中的任务
	 * 
	 * @param fwId 流程id
	 * @return true是存在处理中的任务
	 */
	private boolean hasPendingTask(String fwId)
	{
		boolean has = true;
		TOaFreedomWorkflowTask task = (TOaFreedomWorkflowTask) ftDao.getSession()
			.createCriteria(TOaFreedomWorkflowTask.class)
			.add(Restrictions.eq("TOaFreedomWorkflow.fwId", fwId))
			.add(Restrictions.eq("ftStatus", FreedomWorkflowStatus.FT_PENDING.val()))
			.uniqueResult();
		if(task == null)
		{
			has = false;
		}
		return has;
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#deleteFreedomWorkflowTasks(java.lang.String)
	 */
	@Transactional(readOnly = false)
	public void deleteFreedomWorkflowTasks(String fwId)
			throws ServiceException, SystemException
	{
		Assert.hasLength(fwId, "流程id不能为空");
		
		List<TOaFreedomWorkflowTask> tasks = getFreedomWorkflowTasks(fwId);
		ftDao.delete(tasks);
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#getFreedomWorkflowTasks(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<TOaFreedomWorkflowTask> getFreedomWorkflowTasks(String fwId)
			throws ServiceException, SystemException
	{

		return ftDao.getSession()
				.createCriteria(TOaFreedomWorkflowTask.class)
				.add(Restrictions.eq("TOaFreedomWorkflow.fwId", fwId))
				.addOrder(Order.asc("ftSequence"))
				.list();
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#deleteFreedomWorkflowNotStartTasks(java.lang.String)
	 */
	@Transactional(readOnly = false)
	public void deleteFreedomWorkflowNotStartTasks(String fwId)
			throws ServiceException, SystemException
	{
		Assert.hasLength(fwId, "流程id不能为空");

		List<TOaFreedomWorkflowTask> tasks = getFreedomWorkflowNotStartTasks(fwId);
		ftDao.delete(tasks);
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#deleteFreedomWorkflowDoneTasks(java.lang.String)
	 */
	@Transactional(readOnly = false)
	public void deleteFreedomWorkflowDoneTasks(String fwId)
			throws ServiceException, SystemException
	{
		Assert.hasLength(fwId, "流程id不能为空");

		List<TOaFreedomWorkflowTask> tasks = getFreedomWorkflowDoneTasks(fwId);
		ftDao.delete(tasks);
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#getFreedomWorkflowNotDoneTasks(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<TOaFreedomWorkflowTask> getFreedomWorkflowNotDoneTasks(
			String fwId) throws ServiceException, SystemException
	{
		Assert.hasLength(fwId, "流程id不能为空");

		return ftDao.getSession()
			.createCriteria(TOaFreedomWorkflowTask.class)
			.add(Restrictions.eq("TOaFreedomWorkflow.fwId", fwId))
				.add(Restrictions.or(Restrictions.eq("ftStatus",
						FreedomWorkflowStatus.FT_PENDING.val()), Restrictions
						.eq("ftStatus", FreedomWorkflowStatus.FT_NOT_START.val())))
			.addOrder(Order.asc("ftSequence"))
			.list();
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#getFreedomWorkflowNotStartTasks(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<TOaFreedomWorkflowTask> getFreedomWorkflowNotStartTasks(
			String fwId) throws ServiceException, SystemException
	{
		Assert.hasLength(fwId, "流程id不能为空");

		return ftDao.getSession()
			.createCriteria(TOaFreedomWorkflowTask.class)
			.add(Restrictions.eq("TOaFreedomWorkflow.fwId", fwId))
			.add(Restrictions.eq("ftStatus", FreedomWorkflowStatus.FT_NOT_START.val()))
			.addOrder(Order.asc("ftSequence"))
			.list();
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#getFreedomWorkflowDoneTasks(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<TOaFreedomWorkflowTask> getFreedomWorkflowDoneTasks(String fwId)
			throws ServiceException, SystemException
	{
		Assert.hasLength(fwId, "流程id不能为空");

		return ftDao.getSession()
				.createCriteria(TOaFreedomWorkflowTask.class)
				.add(Restrictions.eq("TOaFreedomWorkflow.fwId", fwId))
				.add(Restrictions.eq("ftStatus", FreedomWorkflowStatus.FT_DONE.val()))
				.addOrder(Order.asc("ftSequence"))
				.list();
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#doneTaskNum(java.lang.String)
	 */
	public int donePendingTaskNum(String fwId) throws ServiceException,
			SystemException
	{
		Assert.hasLength(fwId, "流程id不能为空");

		return (Integer) ftDao.getSession()
				.createCriteria(TOaFreedomWorkflowTask.class)
				.add(Restrictions.eq("TOaFreedomWorkflow.fwId", fwId))
				.add(Restrictions.or(Restrictions.eq("ftStatus",
						FreedomWorkflowStatus.FT_PENDING.val()), Restrictions
						.eq("ftStatus", FreedomWorkflowStatus.FT_DONE.val())))
				.setProjection(Projections.rowCount())
				.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#setFirstTaskMemo(java.lang.String, java.lang.String)
	 */
	@Transactional(readOnly = false)
	public boolean setFirstTaskMemo(String fwId, String ftMemo) throws ServiceException,
			SystemException
	{
		Assert.hasLength(fwId, "流程id不能为空");

		boolean isOk = false;
		
		TOaFreedomWorkflowTask task = (TOaFreedomWorkflowTask) ftDao.getSession()
				.createCriteria(TOaFreedomWorkflowTask.class)
				.add(Restrictions.eq("TOaFreedomWorkflow.fwId", fwId))
				.addOrder(Order.asc("ftSequence"))
				.setMaxResults(1)
				.uniqueResult();
		if(task != null)
		{
			task.setFtMemo(ftMemo);
			ftDao.save(task);
			isOk = true;
		}
		
		return isOk;
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#getMyNotDoneTasks(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<TOaFreedomWorkflowTask> getMyNotDoneTasks(String handler)
			throws ServiceException, SystemException
	{
		Assert.hasLength(handler, "处理人不能为空");
		
		return ftDao.getSession()
				.createCriteria(TOaFreedomWorkflowTask.class)
				.add(Restrictions.eq("ftHandler", handler))
				.add(Restrictions.or(Restrictions.eq("ftStatus",
						FreedomWorkflowStatus.FT_PENDING.val()), Restrictions
						.eq("ftStatus", FreedomWorkflowStatus.FT_NOT_START.val())))
				.addOrder(Order.desc("ftStartTime"))
				.list();
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#getMyPendingTasks(com.strongmvc.orm.hibernate.Page, java.lang.String, java.lang.String)
	 */
	public Page<TOaFreedomWorkflowTask> getMyPendingTasks(
			Page<TOaFreedomWorkflowTask> page, String handler, String ftTitle)
			throws ServiceException, SystemException
	{		
		Assert.hasLength(handler, "处理人不能为空");

		Junction jc = Restrictions.conjunction()
				.add(Restrictions.eq("ftHandler", handler))
				.add(Restrictions.eq("ftStatus", FreedomWorkflowStatus.FT_PENDING.val()));
		
		if(StringUtils.isNotEmpty(ftTitle))
		{
			jc.add(Restrictions.like("ftTitle", ftTitle, MatchMode.ANYWHERE));
		}
		
		Criteria ca = ftDao.getSession().createCriteria(TOaFreedomWorkflowTask.class);
		int totalNum = (Integer) ca.add(jc)
				.setProjection(Projections.rowCount()).uniqueResult();
		
		Criteria cr = ftDao.getSession().createCriteria(TOaFreedomWorkflowTask.class);
		cr.add(jc);
		@SuppressWarnings("unchecked")
		List<TOaFreedomWorkflowTask> tasks = cr.addOrder(Order.desc("ftStartTime"))
				.setFirstResult((page.getPageNo() - 1) * page.getPageSize())
				.setMaxResults(page.getPageSize())
				.list();
		
		page.setTotalCount(totalNum);
		page.setResult(tasks);
		return page;		
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#getMyDoneTasks(com.strongmvc.orm.hibernate.Page, java.lang.String)
	 */
	public Page<TOaFreedomWorkflowTask> getMyDoneTasks(
			Page<TOaFreedomWorkflowTask> page, String handler, String ftTitle)
			throws ServiceException, SystemException
	{		
		Assert.hasLength(handler, "处理人不能为空");

//		Junction jc = Restrictions.conjunction()
//				.add(Restrictions.eq("ftHandler", handler))
//				.add(Restrictions.eq("ftStatus", FreedomWorkflowStatus.FT_DONE.val()));
//		
//		if(StringUtils.isNotEmpty(ftTitle))
//		{
//			jc.add(Restrictions.like("ftTitle", ftTitle, MatchMode.ANYWHERE));
//		}
//		
//		Criteria ca = ftDao.getSession().createCriteria(TOaFreedomWorkflowTask.class);
//		int totalNum = (Integer) ca.add(jc)
//				.setProjection(Projections.rowCount()).uniqueResult();
//		
//		Criteria cr = ftDao.getSession().createCriteria(TOaFreedomWorkflowTask.class);
//		cr.add(jc);
//		@SuppressWarnings("unchecked")
//		List<TOaFreedomWorkflowTask> tasks = cr.addOrder(Order.desc("ftStartTime"))
//				.setFirstResult((page.getPageNo() - 1) * page.getPageSize())
//				.setMaxResults(page.getPageSize())
//				.list();
//		
//		page.setTotalCount(totalNum);
//		page.setResult(tasks);
//		return page;
		
		String sf = "ftHandler='%s' and ftStatus='%s' ";
		String criterion = String.format(sf, new Object[]{handler, FreedomWorkflowStatus.FT_DONE.val()});
		if(StringUtils.isNotEmpty(ftTitle))
		{
			criterion = criterion + "and ftTitle like '%" + ftTitle + "%'";
		}
		
		StringBuilder hql = new StringBuilder();
		hql.append("from TOaFreedomWorkflowTask t1 where ");
		hql.append(criterion);
		hql.append("  and ftSequence=(select max(ftSequence) from TOaFreedomWorkflowTask t2 where t1.TOaFreedomWorkflow.fwId=t2.TOaFreedomWorkflow.fwId and ");
		hql.append(criterion);
		hql.append(") order by ftEndTime desc");
		
		return ftDao.find(page, hql.toString(), new Object[]{});
	}

	public List<TOaFreedomWorkflowTask> getMyNotDoneTasks(String handler, String fwId)
			throws ServiceException, SystemException
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see com.strongit.oa.freedomworkflow.service.IFreedomWorkflowTaskService#doneTask(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional(readOnly = false)
	public synchronized void doneTask(String handler, String ftId, String ftMemo) throws ServiceException,
			SystemException
	{
		Assert.hasLength(handler, "处理人不能为空");
		Assert.hasLength(ftId, "任务id不能为空");
		
		TOaFreedomWorkflowTask task = getFreedomWorkflowTask(ftId);
		if(task != null && !FreedomWorkflowStatus.FT_DONE.val().equals(task.getFtStatus()))
		{
			task.setFtStatus(FreedomWorkflowStatus.FT_DONE.val());
			task.setFtEndTime(new Date());
			if(StringUtils.isNotEmpty(ftMemo))
			{
				task.setFtMemo(ftMemo);
			}
			ftDao.save(task);
			if(task != null)
			{
				TOaFreedomWorkflowTask next = getNextTask(ftId);
				if(next != null)
				{
					next.setFtStatus(FreedomWorkflowStatus.FT_PENDING.val());
					next.setFtStartTime(new Date());
					ftDao.save(next);
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public void sendMsg(String remindTypes, List<String> revUsers, String msg)
			throws ServiceException, SystemException
	{
		Assert.hasLength(remindTypes, "发送方式不能为空");
		Assert.notEmpty(revUsers, "接收者不能为空");
		Assert.hasLength(msg, "消息内容不能为空");
		
		String title = "";
		String[] types = remindTypes.split(",");
		if(msg.length() > 20)
		{
			title = title + msg.substring(0, 20) + "...";
		}
		else
		{
			title = msg;
		}
		
		for(String type : types)
		{
			if (Constants.MSG.equals(type)) {
				msgService.sendMsg("system", revUsers, title, msg,
						GlobalBaseData.MSG_GZCL);
			}
			if (Constants.EMAIL.equals(type)) {
				mailService.autoSendMail(revUsers, title, msg,
						"system");
			}
			if (Constants.SMS.equals(type)) {
				smsService.sendSms("system", revUsers, msg,
						GlobalBaseData.SMSCODE_WORKFLOW);
			}
			if (Constants.RTX.equals(type)) {
				try
				{
					rtxService.sendIMMessage(msg, revUsers,null);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

	}

}
