package com.strongit.oa.common.business.mainactorconfig;

import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TMainReassignActorConfing;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * Manage 主办人员处理信息
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 9, 2012 7:40:52 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.business.mainactorconfig.MainActorConfigManage
 */
@Service
@Transactional
@OALogger
public class MainReassignActorConfigManage {
	private GenericDAOHibernate<TMainReassignActorConfing, java.lang.String> dao;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IWorkflowService workflowService;

	@Autowired
	IUserService userService; // 注入统一用户接口

	@Autowired
	private MyLogManager logService;

	/**
	 * 注入SessionFactory
	 * 
	 * @param session
	 *            会话对象.
	 */
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		dao = new GenericDAOHibernate<TMainReassignActorConfing, java.lang.String>(
				session, TMainReassignActorConfing.class);
	}

	private void save(TMainReassignActorConfing model, OALogInfo... infos) {
		dao.save(model);
	}

	private void delete(TMainReassignActorConfing model, OALogInfo... infos) {
		dao.delete(model);
	}

	/**
	 * 根据流程实例id获取TMainReassignActorConfing
	 * 
	 * @author 严建
	 * @param processInstanceId
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Mar 9, 2012 7:48:51 PM
	 */
	public TMainReassignActorConfing getModelByProcessInstanceId(
			String processInstanceId) throws DAOException, ServiceException,
			SystemException {
		StringBuilder hql;
		try {
			hql = new StringBuilder(
					"from TMainReassignActorConfing t where t.processInstanceId = ?");
			List list = dao.find(hql.toString(), processInstanceId);
			if (list != null && !list.isEmpty()) {
				return (TMainReassignActorConfing) list.get(0);
			} else {
				return null;
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 获取主办人员指派人员信息
	 * 
	 * @author 严建
	 * @param processInstanceId
	 * @return
	 * @createTime Mar 9, 2012 8:13:23 PM
	 */
	public UserBeanTemp getMainReassignActorInfoByProcessInstanceId(
			String processInstanceId) {
		/*
		 * if(如果主办人员信息为空){ if(流程发起人信息为空){ return 当前用户信息 }else{ return 流程发起人信息; }
		 * }else{ return 主办人员信息; }
		 */
		StringBuilder hql;
		try {
			hql = new StringBuilder(
					"select u.userId,u.userName,o.orgId,o.orgName  from TMainReassignActorConfing t,TUumsBaseUser u,TUumsBaseOrg o  where t.processInstanceId = ? and t.actorId = u.userId and  u.orgId=o.orgId ");
			List list = dao.find(hql.toString(), processInstanceId);
			if (list != null && !list.isEmpty()) {
				Object[] objs = (Object[]) list.get(0);
				return new UserBeanTemp((String) objs[0], (String) objs[1],
						(String) objs[2], (String) objs[3]);
			} else {
				ProcessInstance processinstance = workflowService
						.getProcessInstanceId(processInstanceId);
				if (processinstance.getStartUserId() != null) {
					Organization organization = userService
							.getUserDepartmentByUserId(processinstance
									.getStartUserId());
					return new UserBeanTemp(processinstance.getStartUserId(),
							processinstance.getStartUserName(), organization
									.getOrgId(), organization.getOrgName());
				} else {
					User user = userService.getCurrentUser();
					Organization organization = userService
							.getUserDepartmentByUserId(user.getUserId());
					return new UserBeanTemp(user.getUserId(), user
							.getUserName(), organization.getOrgId(),
							organization.getOrgName());
				}
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 根据任务实例id获取主办人员指派人员信息
	 * 
	 * @author 严建
	 * @param taskInstanceId
	 * @return
	 * @createTime Mar 9, 2012 8:24:11 PM
	 */
	public UserBeanTemp getMainReassignActorInfoByTaskInstanceId(
			String taskInstanceId) {
		try {
			return getMainReassignActorInfoByProcessInstanceId(workflowService
					.getProcessInstanceIdByTiId(taskInstanceId));
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 更新数据信息 前提：model的processInstanceId不能为空
	 * 
	 * @author 严建
	 * @param model
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Mar 9, 2012 7:52:12 PM
	 */
	public void updateModel(TMainReassignActorConfing model, OALogInfo... infos)
			throws DAOException, ServiceException, SystemException {
		try {
			TMainReassignActorConfing newBean = getModelByProcessInstanceId(model
					.getProcessInstanceId());
			if (newBean != null) {
				newBean.setActorId(model.getActorId());
				save(newBean, new OALogInfo("指派时，保存主办权限信息"));
			} else {
				save(model, new OALogInfo("指派时，保存主办权限信息"));
			}
			ToaLog log = new ToaLog();
			String ip = ServletActionContext.getRequest().getRemoteAddr();
			log.setOpeIp(ip); // 操作者IP地址
			log.setOpeUser(userService.getCurrentUser().getUserName()); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo("指派时，保存主办权限信息");// 日志信息
			logger.error("指派时，保存主办权限信息");
			logService.saveObj(log);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	 * 根据流程实例删除
	 * 
	 * @author 严建
	 * @param processInstanceId
	 * @param infos
	 * @createTime Mar 24, 2012 5:17:14 PM
	 */
	public void deleteModel(String processInstanceId, OALogInfo... infos) {
		TMainReassignActorConfing model = getModelByProcessInstanceId(processInstanceId);
		if (model != null) {
			delete(model, new OALogInfo("删除"
					+ userService.getUserNameByUserId(model.getActorId()) + "【"
					+ model.getActorId() + "】指派的主办权限信息"));
			ToaLog log = new ToaLog();
			String ip = ServletActionContext.getRequest().getRemoteAddr();
			log.setOpeIp(ip); // 操作者IP地址
			log.setOpeUser(userService.getCurrentUser().getUserName()); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo("删除"
					+ userService.getUserNameByUserId(model.getActorId()) + "【"
					+ model.getActorId() + "】指派的主办权限信息");// 日志信息
			logger.error("删除"
					+ userService.getUserNameByUserId(model.getActorId()) + "【"
					+ model.getActorId() + "】指派的主办权限信息");
			logService.saveObj(log);
		}
	}
}
