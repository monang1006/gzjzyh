package com.strongit.oa.workinspect.worksend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.SystemException;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.TOsManagementSummary;
import com.strongit.oa.bo.TOsTaskAttach;
import com.strongit.oa.bo.TOsWorkReviews;
import com.strongit.oa.bo.TOsWorkType;
import com.strongit.oa.bo.TOsWorktask;
import com.strongit.oa.bo.TOsWorktaskSend;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.PathUtil;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class WorkSendService implements IWorkSendService {

	private GenericDAOHibernate<TOsWorktask, java.lang.String> taskDao;
	private GenericDAOHibernate<TOsWorkType, java.lang.String> workTypeDao;
	private GenericDAOHibernate<TOsTaskAttach, java.lang.String> taskAttachDao;
	private GenericDAOHibernate<TOsWorktaskSend, java.lang.String> sendDao;
	private GenericDAOHibernate<TOsWorkReviews, java.lang.String> reviewDao;
	private GenericDAOHibernate<TOsManagementSummary, java.lang.String> summaryDao;

	@Autowired
	private IUserService userService;
	
//	公共附件接口
	@Autowired
	private IAttachmentService attachmentService;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		taskDao = new GenericDAOHibernate<TOsWorktask, java.lang.String>(
				sessionFactory, TOsWorktask.class);
		workTypeDao = new GenericDAOHibernate<TOsWorkType, java.lang.String>(
				sessionFactory, TOsWorkType.class);
		taskAttachDao = new GenericDAOHibernate<TOsTaskAttach, java.lang.String>(
				sessionFactory, TOsTaskAttach.class);
		sendDao = new GenericDAOHibernate<TOsWorktaskSend, java.lang.String>(
				sessionFactory, TOsWorktaskSend.class);
		reviewDao = new GenericDAOHibernate<TOsWorkReviews, java.lang.String>(
				sessionFactory, TOsWorkReviews.class);
		summaryDao = new GenericDAOHibernate<TOsManagementSummary, java.lang.String>(
				sessionFactory, TOsManagementSummary.class);
	}

	/**
	 * 保存工作任务
	 * 
	 * @param task
	 * @param recvIds
	 * @param file
	 * @param fileFileName
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@Transactional(readOnly = false)
	public void save(TOsWorktask task, String recvIds, File[] file,
			String[] fileFileName) throws ServiceException, SystemException {
		if (task.getWorktaskId() != null && !"".equals(task.getWorktaskId())) {
			// 修改
			TOsWorktask oldTask = this.taskDao.get(task.getWorktaskId());
			oldTask.setWorktaskTitle(task.getWorktaskTitle());
			oldTask.setWorktaskStime(task.getWorktaskStime());
			oldTask.setWorktaskEtime(task.getWorktaskEtime());
			oldTask.setWorktaskEmerlevel(task.getWorktaskEmerlevel());
			oldTask.setWorktaskType(task.getWorktaskType());
			oldTask.setWorktaskContent(task.getWorktaskContent());

			 //防止出现重复待办记录
			Set<TOsWorktaskSend> oldWorktaskSends = oldTask.getTOsWorktaskSends();
			if(oldWorktaskSends != null && !oldWorktaskSends.isEmpty()){
				
				for (TOsWorktaskSend taskSend : oldWorktaskSends) {
					taskSend.setTOsWorktask(null);
				}
				oldTask.setTOsWorktaskSends(null);
			}
			if (recvIds != null && !"".equals(recvIds)) {
				String[] ids = recvIds.split(",");// 要新增的承办者
				List<TOsWorktaskSend> senderList = new ArrayList<TOsWorktaskSend>();
				for (int i = 0; i < ids.length; i++) {
					String[] kv = ids[i].split("\\|");
					String taskType = "1".equals(kv[0]) ? "0" : "1";// 办理类型（页面上传过来的参数：个人=4，单位=1）
					String taskRecvId = kv[1];// 接收单位id/个人id

					TOsWorktaskSend ns = new TOsWorktaskSend();
					ns.setTOsWorktask(oldTask);// 工作任务
					ns.setTaskSendTime(new Date());// 发送时间
					ns.setTaskState("0");// 状态(未签收=0，待办=1，办结=2)
					ns.setTaskRecvId(taskRecvId);
					ns.setTaskType(taskType);
					if (taskType.equals("0")) {
						// 单位
						ns.setTaskRecvOrgid(taskRecvId);// 接收单位id/接收人所在单位id
					} else { // 根据人员id找所属部门
						// 个人
						Organization org = userService
								.getUserDepartmentByUserId(taskRecvId);
						ns.setTaskRecvOrgid(org.getOrgId());// 接收单位id/接收人所在单位id
					}
					ns.setRest1(null); //  区别于草稿
					senderList.add(ns);
				}
				oldTask.setTOsWorktaskSends(new HashSet<TOsWorktaskSend>(
						senderList));
			}
			oldTask.setRest1(null); //  区别于草稿
			taskDao.update(oldTask);
			this.saveAttachment(oldTask, fileFileName, file, Boolean.FALSE);
		} else {
			// 新建
			if (recvIds != null && !"".equals(recvIds)) {
				String[] ids = recvIds.split(",");
				List<TOsWorktaskSend> sendList = new ArrayList<TOsWorktaskSend>();
				for (int i = 0; i < ids.length; i++) {
					TOsWorktaskSend taskSend = new TOsWorktaskSend();
					taskSend.setTOsWorktask(task);// 工作任务
					taskSend.setTaskSendTime(new Date());// 发送时间
					taskSend.setTaskState("0");// 状态(未签收=0，待办=1，办结=2)

					String[] kv = ids[i].split("\\|");
					String taskType = "1".equals(kv[0]) ? "0" : "1";// 办理类型（页面上传过来的参数：个人=4，单位=1）
					String taskRecvId = kv[1];// 接收单位id/个人id

					taskSend.setTaskRecvId(taskRecvId);
					taskSend.setTaskType(taskType);
					if (taskType.equals("0")) {
						// 单位
						taskSend.setTaskRecvOrgid(taskRecvId);// 接收单位id/接收人所在单位id
					} else { // 根据人员id找所属部门
						// 个人
						Organization org = userService
								.getUserDepartmentByUserId(taskRecvId);
						taskSend.setTaskRecvOrgid(org.getOrgId());// 接收单位id/接收人所在单位id
					}
					taskSend.setRest1(null); //  区别于草稿
					sendList.add(taskSend);
				}
				task
						.setTOsWorktaskSends(new HashSet<TOsWorktaskSend>(
								sendList));
			}
			User user = this.userService.getCurrentUser();
			task.setWorktaskUser(user.getUserId());
			task.setWorktaskUnit(user.getOrgId());

			task.setWorktaskEntryTime(new Date());// 录入时间
			task.setManagetProgress(0);
		//	task.setRest1(null);  //  区别于草稿
			taskDao.save(task);
			this.saveAttachment(task, fileFileName, file, Boolean.FALSE);
		}
	}
	//
	@Transactional(readOnly = false)
	public void saveDraft(TOsWorktask task, String recvIds, File[] file,
			String[] fileFileName) throws ServiceException, SystemException {
			
			Date sendTime = new Date();
			if (task.getWorktaskId() != null && !"".equals(task.getWorktaskId())) {
				// 修改
				TOsWorktask oldTask = this.taskDao.get(task.getWorktaskId());
				oldTask.setWorktaskTitle(task.getWorktaskTitle());
				oldTask.setWorktaskStime(task.getWorktaskStime());
				oldTask.setWorktaskEtime(task.getWorktaskEtime());
				oldTask.setWorktaskEmerlevel(task.getWorktaskEmerlevel());
				oldTask.setWorktaskType(task.getWorktaskType());
				oldTask.setWorktaskContent(task.getWorktaskContent());
				oldTask.setWorktaskEmerlevel(task.getWorktaskEmerlevel());
				oldTask.setRest1(task.getRest1());
				 //防止出现重复待办记录
				Set<TOsWorktaskSend> oldWorktaskSends = oldTask.getTOsWorktaskSends();
				if(oldWorktaskSends != null && !oldWorktaskSends.isEmpty()){
					
					for (TOsWorktaskSend taskSend : oldWorktaskSends) {
						taskSend.setTOsWorktask(null);
					}
					oldTask.setTOsWorktaskSends(null);
				}
				
				if (recvIds != null && !"".equals(recvIds)) {
					String[] ids = recvIds.split(",");// 要新增的承办者
					List<TOsWorktaskSend> senderList = new ArrayList<TOsWorktaskSend>();
					for (int i = 0; i < ids.length; i++) {
						String[] kv = ids[i].split("\\|");
						String taskType = "1".equals(kv[0]) ? "0" : "1";// 办理类型（页面上传过来的参数：个人=4，单位=1）
						String taskRecvId = kv[1];// 接收单位id/个人id

						TOsWorktaskSend ns = new TOsWorktaskSend();
						ns.setTOsWorktask(oldTask);// 工作任务
						sendTime.setTime(System.currentTimeMillis()+1000);
						ns.setTaskSendTime(sendTime);// 发送时间
						ns.setTaskState("0");// 状态(未签收=0，待办=1，办结=2)
						ns.setTaskRecvId(taskRecvId);
						ns.setTaskType(taskType);
						if (taskType.equals("0")) {
							// 单位
							ns.setTaskRecvOrgid(taskRecvId);// 接收单位id/接收人所在单位id
						} else { // 根据人员id找所属部门
							// 个人
							Organization org = userService
									.getUserDepartmentByUserId(taskRecvId);
							ns.setTaskRecvOrgid(org.getOrgId());// 接收单位id/接收人所在单位id
						}
						ns.setRest1("0"); //草稿标识
						senderList.add(ns);
					}
					oldTask.setTOsWorktaskSends(new HashSet<TOsWorktaskSend>(
							senderList));
				}
				oldTask.setRest1("0");  //草稿标识
				taskDao.update(oldTask);
				this.saveAttachment(oldTask, fileFileName, file, Boolean.FALSE);
			} else {
				// 新建
				if (recvIds != null && !"".equals(recvIds)) {
					String[] ids = recvIds.split(",");
					List<TOsWorktaskSend> sendList = new ArrayList<TOsWorktaskSend>();
					for (int i = 0; i < ids.length; i++) {
						TOsWorktaskSend taskSend = new TOsWorktaskSend();
						taskSend.setTOsWorktask(task);// 工作任务
						sendTime.setTime(System.currentTimeMillis()+1000);
						taskSend.setTaskSendTime(sendTime);// 发送时间
						taskSend.setTaskState("0");// 状态(未签收=0，待办=1，办结=2)

						String[] kv = ids[i].split("\\|");
						String taskType = "1".equals(kv[0]) ? "0" : "1";// 办理类型（页面上传过来的参数：个人=4，单位=1）
						String taskRecvId = kv[1];// 接收单位id/个人id

						taskSend.setTaskRecvId(taskRecvId);
						taskSend.setTaskType(taskType);
						if (taskType.equals("0")) {
							// 单位
							taskSend.setTaskRecvOrgid(taskRecvId);// 接收单位id/接收人所在单位id
						} else { // 根据人员id找所属部门
							// 个人
							Organization org = userService
									.getUserDepartmentByUserId(taskRecvId);
							taskSend.setTaskRecvOrgid(org.getOrgId());// 接收单位id/接收人所在单位id
						}
						taskSend.setRest1("0"); //草稿标识
						sendList.add(taskSend);
					}
					task
							.setTOsWorktaskSends(new HashSet<TOsWorktaskSend>(
									sendList));
				}
				User user = this.userService.getCurrentUser();
				task.setWorktaskUser(user.getUserId());
				task.setWorktaskUnit(user.getOrgId());

				task.setWorktaskEntryTime(sendTime);// 录入时间
				task.setManagetProgress(0);
				task.setRest1("0");  //  区别于草稿
				taskDao.save(task);
				this.saveAttachment(task, fileFileName, file, Boolean.FALSE);
			}
	}
	
	/**
	 * 删除附件
	 * 
	 * @param attId
	 * @param selectedData
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void delAttach(String attId)
			throws ServiceException, SystemException{
		TOsTaskAttach attach = taskAttachDao.get(attId);
		String rest2 = attach.getAttachRest2();//删除附件的同时删除公告附件表的附件
		attachmentService.deleteAttachment(rest2);
		taskAttachDao.delete(attId);
	}
	
	/**
	 * 增加承办者
	 * 
	 * @param taskId
	 * @param recvIds
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@Transactional(readOnly = false)
	public void addRecver(String taskId, String selectedData)
			throws ServiceException, SystemException {
		TOsWorktask oldTask = this.taskDao.get(taskId);
		if (selectedData != null && !"".equals(selectedData)) {
			String[] ids = selectedData.split(",");
			List<TOsWorktaskSend> senderList = new ArrayList<TOsWorktaskSend>();

			for (int i = 0; i < ids.length; i++) {
				String[] kv = ids[i].split("\\|");
				String taskType = "1".equals(kv[0]) ? "0" : "1";// 办理类型（页面上传过来的参数：个人=4，单位=1）
				String taskRecvId = kv[1];// 接收单位id/个人id

				// 要新加的承办者
				TOsWorktaskSend ns = new TOsWorktaskSend();
				ns.setTOsWorktask(oldTask);// 工作任务
				ns.setTaskSendTime(new Date());// 发送时间
				ns.setTaskState("0");// 状态(未签收=0，待办=1，办结=2)
				ns.setTaskRecvId(taskRecvId);
				ns.setTaskType(taskType);
				if (taskType.equals("0")) {
					// 单位
					ns.setTaskRecvOrgid(taskRecvId);// 接收单位id/接收人所在单位id
				} else { // 根据人员id找所属部门
					// 个人
					Organization org = userService
							.getUserDepartmentByUserId(taskRecvId);
					ns.setTaskRecvOrgid(org.getOrgId());// 接收单位id/接收人所在单位id
				}
				senderList.add(ns);
			}
			if (senderList.size() > 0) {
				this.sendDao.save(senderList);
			}

		}
	}

	/**
	 * 删除承办者
	 * 
	 * @param recvIds
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@Transactional(readOnly = false)
	public void deleteRecver(String recvIds) throws ServiceException,
			SystemException {
		String[] ids = recvIds.split(",");
		for (String id : ids) {
			TOsWorktaskSend send = this.sendDao.get(id);
			if ("0".equals(send.getTaskState())) {
				// 只能删除未接收的承办者
				TOsWorktask TOsWorktask = send.getTOsWorktask();
				TOsWorktask.getTOsWorktaskSends().remove(send);
				this.sendDao.delete(send);
			}
		}
	}

	/**
	 * 删除承办者
	 * 
	 * @param recvIds
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@Transactional(readOnly = false)
	public void deleteWorkSend(String taskId) throws ServiceException,
	SystemException{
		String[] ids = taskId.split(",");
		for (String id : ids) {
			this.taskDao.delete(id);
		}
	}
	
	/**
	 * 删除已发工作
	 * 
	 * @param recvIds
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void deleteWorkSendAtt(List<TOsTaskAttach> list) throws ServiceException,
			SystemException{
		this.taskAttachDao.delete(list);
	}
	/**
	 * 保存工作任务的办理纪要
	 * 
	 * @param task
	 * @param file
	 * @param fileFileName
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@Transactional(readOnly = false)
	public void saveTaskSummary(TOsWorktask task, File[] file,
			String[] fileFileName) throws ServiceException, SystemException {
		this.taskDao.update(task);
		this.saveAttachment(task, fileFileName, file, Boolean.TRUE);
	}

	/**
	 * 保存工作任务的评语
	 * 
	 * @param task
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@Transactional(readOnly = false)
	public void saveTaskReview(TOsWorktask task) throws ServiceException,
			SystemException {
		TOsWorktask workTask = this.taskDao.get(task.getWorktaskId());
		workTask.setCommpleteLevel(task.getCommpleteLevel());
		workTask.setReviewsDemo(task.getReviewsDemo());
		this.taskDao.update(workTask);
	}

	/**
	 * 保存工作下发的评语
	 * 
	 * @param review
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@Transactional(readOnly = false)
	public void saveSendReview(TOsWorkReviews review) throws ServiceException,
			SystemException {
		this.reviewDao.save(review);
	}

	/**
	 * 获取工作任务
	 * 
	 * @param id
	 *            工作任务ID
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public TOsWorktask getTaskById(String id) throws ServiceException,
			SystemException {
		return taskDao.get(id);
	}

	/**
	 * 获取工作任务下发
	 * 
	 * @param id
	 *            工作任务下发ID
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public TOsWorktaskSend getSendTaskById(String id) throws ServiceException,
			SystemException {
		return this.sendDao.get(id);
	}

	/**
	 * 获取工作任务的评语
	 * 
	 * @param id
	 *            评语ID
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public TOsWorkReviews getReviewById(String id) throws ServiceException,
			SystemException {
		return this.reviewDao.get(id);
	}

	/**
	 * 获取工作任务的评语
	 * 
	 * @param id
	 *            工作任务下发ID
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public TOsWorkReviews getReviewBySendId(String id) throws ServiceException,
			SystemException {
		List<TOsWorkReviews> reviews = this.reviewDao.findByProperty(
				"sendtaskId", id);
		if (reviews.size() > 0) {
			return reviews.get(0);// 工作下发：评语=1：1
		}
		return null;
	}

	/**
	 * 获取工作任务列表
	 * 
	 * @param page
	 * @param model
	 * @param taskType
	 *            '1':'按个人查','0':'按部门查'
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public Page<TOsWorktask> getTaskList(Page<TOsWorktask> page,
			TOsWorktask model, String taskType) throws ServiceException,
			SystemException {

		List<Object> parmList = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from TOsWorktask t where 1 = 1");
		hql.append(" and t.rest1 is null "); // 草稿控制标识
		if (model.getWorktaskTitle() != null
				&& !"".equals(model.getWorktaskTitle())) {
			hql.append(" and t.worktaskTitle like ?");
			parmList.add("%" + model.getWorktaskTitle() + "%");
		}
		if (model.getWorktaskNo() != null && !"".equals(model.getWorktaskNo())) {
			hql.append(" and t.worktaskNo like ?");
			parmList.add("%" + model.getWorktaskNo() + "%");
		}
		if ("0".equals(taskType)) {
			// 查看部门工作(当前登录用户所属部门任何人所发工作)
			hql.append(" and t.worktaskUnit = ?");
			parmList.add(this.userService.getCurrentUser().getOrgId());
		} else {
			// 查看个人工作(当前登录用户所发工作)
			hql.append(" and t.worktaskUser = ?");
			parmList.add(this.userService.getCurrentUser().getUserId());
		}

		hql.append(" order by t.worktaskEtime  ");// 录入时间排序
		page = this.taskDao.find(page, hql.toString(), parmList.toArray());
		if (page.getResult() != null) {
			List<TOsWorktask> result = page.getResult();
			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					TOsWorktask work = result.get(i);
					User u = userService.getUserInfoByUserId(work
							.getWorktaskUser());
					work.setWorktaskUserName(u.getUserName());
					StringBuffer recvName = new StringBuffer("");
					Set<TOsWorktaskSend> sends = work.getTOsWorktaskSends();
					int n = 0;
					for (TOsWorktaskSend sender : sends) {
//						if (++n > 1) {
//							recvName
//									.append("<span style='padding-left:10px;'>");
//						} else {
//							recvName.append("<span>");
//						}
						String recvId = sender.getTaskRecvId();
						String recvState = sender.getTaskState();
						String recvStateF = "";
						if ("0".equals(recvState)) {
							//recvStateF = "(<font color='red'>待签收</font>)";
							recvStateF = "(待签收)";
						} else if ("1".equals(recvState)) {
							recvStateF = "(办理中)";
						} else if ("2".equals(recvState)) {
							recvStateF = "(已办结)";
						}
						String recvType = sender.getTaskType();
						if ("1".equals(recvType)) {
							// 个人承办
							User recvU = userService
									.getUserInfoByUserId(recvId);
							recvName.append(
									getRecvOrgNamePath(sender
											.getTaskRecvOrgid())).append(
									recvU.getUserName()).append("/").append(
									recvStateF);
							//recvStateF).append("</span><br/>");
						} else {
							// 单位承办
							recvName.append(
									getRecvOrgNamePath(sender
											.getTaskRecvOrgid())).append(
									recvStateF);
							//recvStateF).append("</span><br/>");
						}
					}
					work.setWorktaskSender(recvName.toString());
					result.set(i, work);
				}
			}
		}
		return page;
	}
	/**
	 * 根据工作ID找到相应的发送记录
	 */
	@SuppressWarnings("unchecked")
	public List<TOsWorktaskSend> getTaskReceiver(TOsWorktask model)throws ServiceException,
			SystemException {
		StringBuffer hql = new StringBuffer(
		"select s from TOsWorktaskSend s where s.taskState <> 2 and s.TOsWorktask.worktaskId = ?  ");
		return this.sendDao.find(hql.toString(), model.getWorktaskId());
	}
	public Page<TOsWorktask> getTaskListDraft(Page<TOsWorktask> page,
			TOsWorktask model, String taskType) throws ServiceException,
			SystemException {

		List<Object> parmList = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("from TOsWorktask t where 1 = 1");
		hql.append(" and t.rest1 = '0' "); // 草稿控制标识
		if (model.getWorktaskTitle() != null
				&& !"".equals(model.getWorktaskTitle())) {
			hql.append(" and t.worktaskTitle like ?");
			parmList.add("%" + model.getWorktaskTitle() + "%");
		}
		if (model.getWorktaskNo() != null && !"".equals(model.getWorktaskNo())) {
			hql.append(" and t.worktaskNo like ?");
			parmList.add("%" + model.getWorktaskNo() + "%");
		}
		if ("0".equals(taskType)) {
			// 查看部门工作(当前登录用户所属部门任何人所发工作)
			hql.append(" and t.worktaskUnit = ?");
			parmList.add(this.userService.getCurrentUser().getOrgId());
		} else {
			// 查看个人工作(当前登录用户所发工作)
			hql.append(" and t.worktaskUser = ?");
			parmList.add(this.userService.getCurrentUser().getUserId());
		}

		hql.append(" order by t.worktaskEntryTime desc ");// 录入时间排序
		page = this.taskDao.find(page, hql.toString(), parmList.toArray());
		if (page.getResult() != null) {
			List<TOsWorktask> result = page.getResult();
			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					TOsWorktask work = result.get(i);
					User u = userService.getUserInfoByUserId(work
							.getWorktaskUser());
					work.setWorktaskUserName(u.getUserName());
					StringBuffer recvName = new StringBuffer("");
					Set<TOsWorktaskSend> sends = work.getTOsWorktaskSends();
					int n = 0;
					for (TOsWorktaskSend sender : sends) {
//						if (++n > 1) {
//							recvName
//									.append("<span style='padding-left:10px;'>");
//						} else {
//							recvName.append("<span>");
//						}
						String recvId = sender.getTaskRecvId();
						String recvState = sender.getTaskState();
						String recvStateF = "";
						if ("0".equals(recvState)) {
							//recvStateF = "(<font color='red'>待签收</font>)";
							recvStateF = "(待签收)";
						} else if ("1".equals(recvState)) {
							recvStateF = "(办理中)";
						} else if ("2".equals(recvState)) {
							recvStateF = "(已办结)";
						}
						String recvType = sender.getTaskType();
						if ("1".equals(recvType)) {
							// 个人承办
							User recvU = userService
									.getUserInfoByUserId(recvId);
							recvName.append(
									getRecvOrgNamePath(sender
											.getTaskRecvOrgid())).append(
									recvU.getUserName()).append("/").append(
									recvStateF)
//									.append("</span><br/>")
									;
						} else {
							// 单位承办
							recvName.append(
									getRecvOrgNamePath(sender
											.getTaskRecvOrgid())).append(
									recvStateF)
//									.append("</span><br/>")
									;
						}
					}
					work.setWorktaskSender(recvName.toString());
					result.set(i, work);
				}
			}
		}
		return page;
	}

	/**
	 * 获取当前登录用户所在单位下人员所发的工作对应的办理情况
	 * 
	 * @param page
	 * @param send
	 * @param taskState
	 *            (未签收=0，待办=1，办结=2)
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public Page<TOsWorktaskSend> getSendList(Page<TOsWorktaskSend> page,
			TOsWorktaskSend send, String taskState) throws ServiceException,
			SystemException {
		List<Object> parmList = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(
				"select s from TOsWorktaskSend s,TUumsBaseOrg o where s.taskRecvOrgid=o.orgId ");
		//草稿标识
		hql.append(" and s.rest1 is null ");
		// 查看当前登录用户所属部门任何人所发工作
		hql.append(" and s.TOsWorktask.worktaskUnit = ?");
		parmList.add(this.userService.getCurrentUser().getOrgId());
		// 工作标题
		if (send.getTOsWorktask() != null
				&& send.getTOsWorktask().getWorktaskTitle() != null
				&& !"".equals(send.getTOsWorktask().getWorktaskTitle())) {
			hql.append(" and s.TOsWorktask.worktaskTitle like ?");
			parmList.add("%" + send.getTOsWorktask().getWorktaskTitle() + "%");
		}
		// 承办单位
		if (send.getTaskRecvOrgName() != null
				&& !"".equals(send.getTaskRecvOrgName())) {
			hql.append(" and o.orgName like ?");
			parmList.add("%" + send.getTaskRecvOrgName() + "%");
		}

		// 开始时间
		if (send.getTaskSendTime() != null
				&& !"".equals(send.getTaskSendTime())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String beginDate = sdf.format(send.getTaskSendTime());
			hql.append(" and to_char(s.taskSendTime,'yyyy-MM-dd')>=?");
			parmList.add(beginDate);
		}
		// 结束时间
		if (send.getTaskRecvTime() != null
				&& !"".equals(send.getTaskRecvTime())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String endDate = sdf.format(send.getTaskRecvTime());
			hql.append(" and to_char(s.taskSendTime,'yyyy-MM-dd')<=? ");
			parmList.add(endDate);
		}

		if (taskState != null && !"".equals(taskState)) {
			// 查看个人工作(当前登录用户所发工作)
			hql.append(" and s.taskState = ?");
			parmList.add(taskState);
		}

		hql.append(" order by s.TOsWorktask.worktaskEtime  ");// 发送时间排序
		page = this.sendDao.find(page, hql.toString(), parmList.toArray());
		try {
			if (page.getResult() != null) {
				List<TOsWorktaskSend> result = page.getResult();

				HttpServletRequest request = ServletActionContext.getRequest();
				String frameroot = (String) request.getSession().getAttribute(
						"frameroot");
				if (frameroot == null || frameroot.equals("")
						|| frameroot.equals("null"))
					frameroot = "/frame/theme_gray";
				frameroot = request.getContextPath() + frameroot;

				String imgState1 = "<img src='" + frameroot
						+ "/images/red.gif'>";
				String imgState2 = "<img src='" + frameroot
						+ "/images/blue.gif'>";
				String imgState3 = "<img src='" + frameroot
						+ "/images/green.gif'>";
				String imgState4 = "<img src='" + frameroot
						+ "/images/dgray.gif'>";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String now = sdf.format(new Date());
				Date nowDate = sdf.parse(now);
				if (result != null) {
					String mobileImg = "<img src='" + frameroot
							+ "/images/mobile.gif'>";
					int num = (page.getPageNo() - 1) * page.getPageSize();
					for (int i = 0; i < result.size(); i++) {
						TOsWorktaskSend s = result.get(i);
						s.setRestMobileImg(mobileImg);
						s.setRestNum(String.valueOf(++num));
						User u = userService.getUserInfoByUserId(s
								.getTOsWorktask().getWorktaskUser());
						s.getTOsWorktask().setWorktaskUserName(u.getUserName());
						String sendState = s.getTaskState();
						String sendStateF = "";
						String imgState = "";
						Date endTime = s.getTOsWorktask().getWorktaskEtime();
						String endTimeStr = sdf.format(endTime);
						endTime = sdf.parse(endTimeStr);

						if ("0".equals(sendState)) {
							sendStateF = "(待签收)";
//							sendStateF = "(<font color='red'>待签收</font>)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("1".equals(sendState)) {
							sendStateF = "(办理中)";
							if (endTime.compareTo(nowDate) < 0) {
								imgState = imgState1;
							} else if (endTime.compareTo(nowDate) == 0) {
								imgState = imgState2;
							} else if (endTime.compareTo(nowDate) > 0) {
								imgState = imgState3;
							}
						} else if ("2".equals(sendState)) {
							sendStateF = "(已办结)";
							imgState = imgState4;
						}
						s.setRestImg(imgState);
						TUumsBaseOrg org = this.userService.getOrgInfoByOrgId(s
								.getTaskRecvOrgid());
						if ("1".equals(s.getTaskType())) {
							// 承办者为个人
							User ru = this.userService.getUserInfoByUserId(s
									.getTaskRecvId());
							s
									.setTaskRecvName(getRecvOrgNamePath(org
											.getOrgId())
											+ ru.getUserName()
											+ "/"
											+ sendStateF);
						} else {
							// 承办者为单位
							s
									.setTaskRecvName(getRecvOrgNamePath(org
											.getOrgId())
											+ sendStateF);
						}

					}
				}
			}
		} catch (com.strongmvc.exception.SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}
	

	/**
	 * 获取工作下发的所有办理纪要信息
	 * 
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public List<TOsManagementSummary> getSummaryList(String taskId)
			throws ServiceException, SystemException {
		// 获取工作下发的纪要
		// List<TOsManagementSummary> summaryList = this.summaryDao
		// .findByProperty("TOsWorktaskSend.TOsWorktask.worktaskId",
		// taskId);

		StringBuffer hql = new StringBuffer(
				"select m from TOsManagementSummary m,TOsWorktask t where m.TOsWorktaskSend.TOsWorktask.worktaskId=t.worktaskId and t.worktaskId=?");
		List<TOsManagementSummary> summaryList = this.sendDao.find(hql
				.toString(), taskId);
		for (TOsManagementSummary summary : summaryList) {
			List<TOsTaskAttach> attachList = this.taskAttachDao.findByProperty(
					"attachSummaryId", summary.getSummaryId());
			summary.setAttachList(attachList);
		}
		return summaryList;
	}

	/**
	 * 获取工作分类列表
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TOsWorkType> getWorkTypeList() throws ServiceException,
			SystemException {
		String hql="from TOsWorkType order by worktypeSequence";
		
		return this.workTypeDao.find(hql, null);
	}

	/**
	 * 保存附件
	 * 
	 * @param task
	 * @param attachName
	 * @param attachCon
	 * @param isTaskSummary
	 *            true:是工作纪要的附件
	 * @return
	 */
	@Transactional(readOnly = false)
	public void saveAttachment(TOsWorktask task, String[] fileNames,
			File[] files, Boolean isTaskSummary) {
		if (files != null) {
			int arr = files.length;
			for (int i = 0; i < arr; i++) {

				String attachName = fileNames[i];
				TOsTaskAttach attach = new TOsTaskAttach();
				String attachType = attachName.substring(attachName
						.lastIndexOf(".") + 1, attachName.length());

				attach.setAttachTaskId(task.getWorktaskId());// 工作任务id
				attach.setAttachFileName(attachName);// 文件名
				attach.setAttachFileType(attachType);// 文件类型
				attach.setAttachFileDate(new Date());// 上传时间

				if (isTaskSummary) {
					attach.setAttachRest1("1");// 保存为工作纪要的附件
				}

				// attach.setAttachSummaryId(null);// 办理纪要id

				String rootPath = PathUtil.getRootPath();// 得到工程根路径
				String dir = rootPath + "uploadfile";
				attach.setFileServer(ServletActionContext.getRequest()
						.getServerName());// 文件服务器
				attach.setAttachFilePath(dir);// 文件路径

				// attach.setAttachCon(attachCon);
				byte[] buff = new byte[(int)files[i].length()];
				String ext = fileNames[i].substring(fileNames[i].lastIndexOf(".") + 1,
						fileNames[i].length());
				//添加公共附件表数据
				Calendar cals = Calendar.getInstance();
				String attachId = attachmentService.saveAttachment(fileNames[i], buff, cals.getTime(), ext, "1", "注:任务工单附件", userService.getCurrentUser().getUserId());
				attach.setAttachRest2(attachId);
				taskAttachDao.save(attach);

				OutputStream os = null;
				InputStream is = null;
				try {
					FileInputStream fis = null;
					fis = new FileInputStream(files[i]);
					byte[] buf = new byte[(int) files[i].length()];
					fis.read(buf);
					File file = new File(dir);
					if (!file.exists()) {
						file.mkdir();
					}
					os = new FileOutputStream(dir + File.separator
							+ attach.getAttachId() + "."
							+ attach.getAttachFileType());
					os.write(buf, 0, buf.length);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
						}
					}
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
						}
					}
				}

			}
		}
	}

	/**
	 * 根据工作获取对应的附件信息
	 * 
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TOsTaskAttach> getAttachListByTaskId(String taskId)
			throws ServiceException, SystemException {
		return this.taskAttachDao.find(
				"from TOsTaskAttach a where a.attachRest1=null and a.attachTaskId=?",
				new Object[] { taskId });
	}

	/**
	 * 根据工作获取对应的工作纪要附件
	 * 
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public List<TOsTaskAttach> getTaskSummaryAttachListByTaskId(String taskId)
			throws ServiceException, SystemException {
		return this.taskAttachDao
				.find(
						"from TOsTaskAttach a where a.attachRest1=1 and a.attachTaskId=?",
						new Object[] { taskId });
	}

	/**
	 * 根据附件id获取附件信息
	 * 
	 * @param attachId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public TOsTaskAttach getAttachById(String attachId)
			throws ServiceException, SystemException {
		return this.taskAttachDao.get(attachId);
	}

	/**
	 * 获取工作的承办者字符串
	 * 
	 * @param taskId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getRecvDataByTaskId(String taskId) throws SystemException,
			ServiceException {
		StringBuffer recvData = new StringBuffer("");
		if (taskId != null && !"".equals(taskId)) {
			TOsWorktask task = this.taskDao.get(taskId);
			Set<TOsWorktaskSend> sends = task.getTOsWorktaskSends();
			Iterator<TOsWorktaskSend> it = sends.iterator();
			while (it.hasNext()) {
				TOsWorktaskSend send = it.next();
				if ("1".equals(send.getTaskType())) {
					// 承办者为个人
					User u = this.userService.getUserInfoByUserId(send
							.getTaskRecvId());
					recvData.append(",").append("4|").append(u.getUserId())
							.append("|").append(u.getUserName());
				} else {
					// 承办者为单位
					TUumsBaseOrg org = this.userService.getOrgInfoByOrgId(send
							.getTaskRecvId());
					recvData.append(",").append("1|").append(org.getOrgId())
							.append("|").append(org.getOrgName());
				}
			}
		}
		return recvData.toString();
	}

	/**
	 * 获取接收单位名称路径
	 * 
	 * @param orgId
	 * @return
	 */
	public String getRecvOrgNamePath(String orgId) {
		String result = "";
		TUumsBaseOrg org = this.userService.getOrgInfoByOrgId(orgId);
		if (org != null) {
			if (org.getOrgParentId() != null) {
				result += getRecvOrgNamePath(org.getOrgParentId())
						+ org.getOrgName() + "/";
			} else {
				result += org.getOrgName() + "/";
			}
		}
		return result;
	}
}
