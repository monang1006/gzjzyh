package com.strongit.oa.meetingmanage.meetingtopic;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaMeetingAttendance;
import com.strongit.oa.bo.ToaMeetingTopic;
import com.strongit.oa.bo.ToaSenddoc;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.meetingmanage.util.MeetingmanageConst;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class MeetingtopicManager extends BaseManager {
	private GenericDAOHibernate<ToaMeetingTopic, String> topicDao = null;

	private GenericDAOHibernate<ToaMeetingAttach, String> attachDao = null;

	// 统一用户服务
	private IUserService user;

	public IUserService getUser() {
		return user;
	}

	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}

	public MeetingtopicManager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		topicDao = new GenericDAOHibernate<ToaMeetingTopic, String>(
				sessionFactory, ToaMeetingTopic.class);
		attachDao = new GenericDAOHibernate<ToaMeetingAttach, String>(
				sessionFactory, ToaMeetingAttach.class);

	}

	/**
	 * 得到一页会议议题列表
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-30 上午11:58:55
	 * @param page
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceExceptionprivate
	 *             String topicCode;
	 */
	public Page<ToaMeetingTopic> queryTopic(Page<ToaMeetingTopic> page,
			String topicCode, String topicSubject, String topicSorts,
			Date topicEstime, String topicStatus) throws DAOException,
			SystemException, ServiceException {

		Calendar cal = Calendar.getInstance();

		Object[] values = new Object[1];
		StringBuffer queryStr = new StringBuffer(
				"from ToaMeetingTopic t where 1=1");

		if (topicStatus != null && !"".equals(topicStatus)) {
			queryStr.append(" and t.topicStatus ='" + topicStatus + "'");

		}
		// else{
		// queryStr.append(" and t.topicStatus ='0'");
		// }
		if (topicCode != null && !"".equals(topicCode)) {
			queryStr.append(" and t.topicCode like '%" + topicCode + "%'");
		}
		if (topicSubject != null && !"".equals(topicSubject)) {
			queryStr
					.append(" and t.topicSubject like '%" + topicSubject + "%'");
		}
		if (topicSorts != null && !"".equals(topicSorts)) {
			queryStr.append(" and t.topicsort.topicsortName like '%"
					+ topicSorts + "%'");

		}
		if (topicEstime != null && !"".equals(topicEstime)) {
			queryStr.append(" and t.topicEstime>=?");
			values[0] = topicEstime;
		} else {
			queryStr.append(" and 1=?");
			values[0] = 1;
		}
		queryStr.append(" order by t.topicEstime desc");

		return topicDao.find(page, queryStr.toString(), values);
	}

	/**
	 * 得到议题LIST对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午09:24:55
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaMeetingTopic> getTopics() throws DAOException,
			SystemException, ServiceException {
		final String hql = "from ToaMeetingTopic";
		return topicDao.find(hql);
	}

	/**
	 * 通过会议ID得到议题LIST对象
	 * 
	 * @author 申仪玲
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaMeetingTopic> getTopicsByConId(String conId)
			throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaMeetingTopic t where t.conferenceId =?";
		return topicDao.find(hql, conId);
	}

	/**
	 * 根据会议议题ID得到指定会议对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-30 下午01:47:57
	 * @param sortId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaMeetingTopic getoneTopic(String topicId) throws DAOException,
			SystemException, ServiceException {
		return topicDao.get(topicId);
	}

	/**
	 * 更新议题状态为审核中
	 * 
	 * @author Administrator蒋国斌 StrongOA2.0_DEV 2010-3-19 上午09:02:10
	 * @param topicId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void updateTopicState(String topicId) throws SystemException,
			ServiceException {
		try {
			ToaMeetingTopic topic = this.getoneTopic(topicId);
			topic.setTopicStatus("1");
			this.updateTopic(topic);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "更新议题状态" });
		}
	}

	/**
	 * 保存会议议题
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午09:29:12
	 * @param toSort
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveTopic(ToaMeetingTopic toSort) throws DAOException,
			SystemException, ServiceException {
		TUumsBaseOrg org = user.getSupOrgByUserIdByHa(user.getCurrentUser()
				.getUserId());
		toSort.setTopOrgcode(org.getOrgSyscode());
		toSort.setDepartmentId(org.getOrgId());
		topicDao.save(toSort);
	}

	@Transactional(readOnly = false)
	public void updateTopic(ToaMeetingTopic topic) throws DAOException,
			SystemException, ServiceException {
		topicDao.update(topic);
	}

	/**
	 * 支持批量删除会议议题以及议题下的附件
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午09:29:41
	 * @param sortIds
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteTopics(String topicIds) throws DAOException,
			SystemException, ServiceException {
		String[] ids = topicIds.split(",");
		for (int i = 0; i < ids.length; i++) {
			List<ToaMeetingAttach> list = this.getTopicAttaches(ids[i]);
			if (list.size() != 0 || list != null) {
				attachDao.delete(list);
			}
		}
		topicDao.delete(ids);
	}

	/**
	 * 保存会议附件
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:03:59
	 * @param attach
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveTopicattach(ToaMeetingAttach attach) throws DAOException,
			SystemException, ServiceException {
		attachDao.save(attach);
	}

	/**
	 * 删除会议附件
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:04:22
	 * @param sortIds
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteAttaches(String sortIds) throws DAOException,
			SystemException, ServiceException {
		String[] ids = sortIds.split(",");
		attachDao.delete(ids);
	}

	/**
	 * 根据指定会议ID删除会议附件
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:13:56
	 * @param topicId
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteTopicAttaches(String topicId) throws DAOException,
			SystemException, ServiceException {
		List<ToaMeetingAttach> list = this.getTopicAttaches(topicId);
		attachDao.delete(list);
	}

	/**
	 * 根据会议ID得到其下面的附件列表
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午10:04:34
	 * @param topicId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaMeetingAttach> getTopicAttaches(String topicId)
			throws DAOException, SystemException, ServiceException {
		final String hql = "from ToaMeetingAttach as t where t.meetingTopic =?";
		return attachDao.find(hql, topicId);
	}

	/**
	 * 根据附件ID得到附件
	 * 
	 * @author 蒋国斌
	 * @date 2009-4-1 下午04:32:59
	 * @param id
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaMeetingAttach getTopicAttache(String id) throws DAOException,
			SystemException, ServiceException {

		return attachDao.get(id);
	}

	/**
	 * 提交工作流处理 这里覆盖父类的方法，将保存电子表单数据和提交工作流处理一并处理,处理完成之后修改会议状态
	 * 
	 * @param formId
	 *            表单ID
	 * @param workflowName
	 *            流程名称
	 * @param businessId
	 *            业务数据id
	 * @param businessName
	 *            标题
	 * @param taskActors
	 *            下一步处理人([人员ID|节点ID，人员ID|节点ID……])
	 * @param transitionName
	 *            迁移线名称
	 */
	public String handleWorkflow(String formId, String workflowName,
			String businessId, String businessName, String[] taskActors,
			String tansitionName, String concurrentTrans, String sugguestion,
			String formData, OALogInfo... infos) throws SystemException,
			ServiceException {
		String processInstance = "";
		try {
			User curUser = userService.getCurrentUser();
			processInstance = workflow.startWorkflow("0", workflowName, curUser
					.getUserId(), businessId, businessName, taskActors,
					tansitionName, concurrentTrans, sugguestion);
			ToaMeetingTopic mod = this.getoneTopic(businessId);
			mod.setProcessInstanceId(processInstance);
			mod.setTopicStatus(MeetingmanageConst.AUDITING);// 改变会议状态为审核中
			this.updateTopic(mod);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "工作流处理" });
		}
		return processInstance;
	}

	/**
	 * 为会议选择议题查询状态为已审核和占用中的一页议题
	 * 
	 * @author ceo
	 * @param page
	 * @param tCode
	 * @param tSubject
	 * @param tSorts
	 * @param tEstime
	 * @param tStatus
	 * @return
	 */
	public Page<ToaMeetingTopic> queryTopicForCon(Page<ToaMeetingTopic> page,
			String topicCode, String topicSubject, String topicSorts,
			Date topicEstime, String topicStatus) throws DAOException,
			SystemException, ServiceException {

		Calendar cal = Calendar.getInstance();

		Object[] values = new Object[1];
		StringBuffer queryStr = new StringBuffer(
				"from ToaMeetingTopic t where 1=1");

		if (topicStatus != null && !"".equals(topicStatus)) {
			queryStr.append(" and t.topicStatus ='" + topicStatus + "'");

		} else {
			queryStr.append(" and (t.topicStatus ='2' or t.topicStatus ='3')");
		}
		if (topicCode != null && !"".equals(topicCode)) {
			queryStr.append(" and t.topicCode like '%" + topicCode + "%'");
		}
		if (topicSubject != null && !"".equals(topicSubject)) {
			queryStr
					.append(" and t.topicSubject like '%" + topicSubject + "%'");
		}
		if (topicSorts != null && !"".equals(topicSorts)) {
			queryStr.append(" and t.topicsort.topicsortName like '%"
					+ topicSorts + "%'");

		}
		if (topicEstime != null && !"".equals(topicEstime)) {
			queryStr.append(" and t.topicEstime>=?");
			values[0] = topicEstime;
		} else {
			queryStr.append(" and 1=?");
			values[0] = 1;
		}
		queryStr.append(" order by t.topicEstime desc");

		return topicDao.find(page, queryStr.toString(), values);

	}

}
