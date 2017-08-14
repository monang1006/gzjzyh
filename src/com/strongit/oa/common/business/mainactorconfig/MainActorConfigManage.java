package com.strongit.oa.common.business.mainactorconfig;

import java.util.List;

import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TMainActorConfing;
import com.strongit.oa.common.custom.workflow.ICustomWorkflowService;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
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
public class MainActorConfigManage {
	private GenericDAOHibernate<TMainActorConfing, java.lang.String> dao;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ICustomWorkflowService customWorkflowService;
	@Autowired
	private IWorkflowService workflowService;

	@Autowired
	private IUserService userService; // 注入统一用户接口

	@Autowired
	private MainReassignActorConfigManage mainReassignActorConfigManage; // 注入统一用户接口
	/**
	 * 注入SessionFactory
	 * 
	 * @param session
	 *            会话对象.
	 */
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		dao = new GenericDAOHibernate<TMainActorConfing, java.lang.String>(
				session, TMainActorConfing.class);
	}

	private void save(TMainActorConfing model, OALogInfo... infos) {
		dao.save(model);
	}

	/**
	 * 根据流程实例id获取TMainActorConfing
	 * 
	 * @author 严建
	 * @param processInstanceId
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Mar 9, 2012 7:48:51 PM
	 */
	public TMainActorConfing getModelByProcessInstanceId(
			String processInstanceId) throws DAOException, ServiceException,
			SystemException {
		StringBuilder hql;
		try {
			hql = new StringBuilder(
					"from TMainActorConfing t where t.processInstanceId = ?");
			List list = dao.find(hql.toString(), processInstanceId);
			if (list != null && !list.isEmpty()) {
				return (TMainActorConfing) list.get(0);
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
	 * 获取主办人员信息
	 * 
	 * @author 严建
	 * @param processInstanceId
	 * @return
	 * @createTime Mar 9, 2012 8:13:23 PM
	 */
	public UserBeanTemp getMainActorInfoByProcessInstanceId(
			String processInstanceId) {
		/*
		 * if(如果主办人员信息为空){ if(流程发起人信息为空){ return 当前用户信息 }else{ return 流程发起人信息; }
		 * }else{ return 主办人员信息; }
		 */
		StringBuilder hql;
		try {
			hql = new StringBuilder(
					"select u.userId,u.userName,o.orgId,o.orgName  from TMainActorConfing t,TUumsBaseUser u,TUumsBaseOrg o  where t.processInstanceId = ? and t.mainActorId = u.userId and  u.orgId=o.orgId ");
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
	 * 根据任务实例id获取主办人员信息
	 * 
	 * @author 严建
	 * @param taskInstanceId
	 * @return
	 * @createTime Mar 9, 2012 8:24:11 PM
	 */
	public UserBeanTemp getMainActorInfoByTaskInstanceId(String taskInstanceId) {
		try {
			return getMainActorInfoByProcessInstanceId(workflowService
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
	public void updateModel(TMainActorConfing model, OALogInfo... infos)
			throws DAOException, ServiceException, SystemException {
		try {
			if(customWorkflowService.isEnableMainDoingFunction()) {//主办权限已经启动时)
				TMainActorConfing newBean = getModelByProcessInstanceId(model
						.getProcessInstanceId());
				if (newBean != null) {
					newBean.setMainActorId(model.getMainActorId());
					save(newBean);
				} else {
					save(model);
				}
				mainReassignActorConfigManage.deleteModel(model
						.getProcessInstanceId());
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
}
