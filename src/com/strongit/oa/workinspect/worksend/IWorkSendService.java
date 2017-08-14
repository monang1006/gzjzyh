package com.strongit.oa.workinspect.worksend;

import java.io.File;
import java.util.List;

import javax.transaction.SystemException;

import com.strongit.oa.bo.TOsManagementSummary;
import com.strongit.oa.bo.TOsTaskAttach;
import com.strongit.oa.bo.TOsWorkReviews;
import com.strongit.oa.bo.TOsWorkType;
import com.strongit.oa.bo.TOsWorktask;
import com.strongit.oa.bo.TOsWorktaskSend;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;

public interface IWorkSendService {

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
	public void save(TOsWorktask task, String recvIds, File[] file,
			String[] fileFileName) throws ServiceException, SystemException;
	
	public void saveDraft(TOsWorktask task, String recvIds, File[] file,
			String[] fileFileName) throws ServiceException, SystemException;
	
	/**
	 * 增加承办者
	 * 
	 * @param taskId
	 * @param selectedData
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void addRecver(String taskId, String selectedData)
			throws ServiceException, SystemException;
	
	/**
	 * 删除附件
	 * 
	 * @param attId
	 * @param selectedData
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void delAttach(String attId)
			throws ServiceException, SystemException;

	/**
	 * 删除承办者
	 * 
	 * @param recvIds
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void deleteRecver(String recvIds) throws ServiceException,
			SystemException;
	
	/**
	 * 删除已发工作
	 * 
	 * @param taskId
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void deleteWorkSend(String taskId) throws ServiceException,
			SystemException;
	
	/**
	 * 删除已发工作
	 * 
	 * @param recvIds
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void deleteWorkSendAtt(List<TOsTaskAttach> list) throws ServiceException,
			SystemException;


	/**
	 * 保存工作任务的办理纪要
	 * 
	 * @param task
	 * @param file
	 * @param fileFileName
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void saveTaskSummary(TOsWorktask task, File[] file,
			String[] fileFileName) throws ServiceException, SystemException;

	/**
	 * 保存工作任务的评语
	 * 
	 * @param task
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void saveTaskReview(TOsWorktask task) throws ServiceException,
			SystemException;

	/**
	 * 保存工作下发的评语
	 * 
	 * @param review
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void saveSendReview(TOsWorkReviews review) throws ServiceException,
			SystemException;

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
			SystemException;
	
	/**
	 * 获取已发工作-个人办理页面-接受人员ID信息
	 * @author: qibh
	 *@param model
	 *@throws ServiceException
	 *@throws SystemException
	 * @created: 2013-5-7 上午09:15:58
	 * @version :5.0
	 */
	public List<TOsWorktaskSend> getTaskReceiver(TOsWorktask model)throws ServiceException,
	SystemException;
	
	//草稿列表
	public Page<TOsWorktask> getTaskListDraft(Page<TOsWorktask> page,
			TOsWorktask model, String taskType) throws ServiceException,
			SystemException;
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
			SystemException;

	/**
	 * 获取工作对应的所有办理纪要信息
	 * 
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TOsManagementSummary> getSummaryList(String taskId)
			throws ServiceException, SystemException;

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
			SystemException;

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
			SystemException;

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
			SystemException;

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
			SystemException;

	/**
	 * 获取工作分类列表
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TOsWorkType> getWorkTypeList() throws ServiceException,
			SystemException;

	/**
	 * 根据工作获取对应的附件信息
	 * 
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TOsTaskAttach> getAttachListByTaskId(String taskId)
			throws ServiceException, SystemException;

	/**
	 * 根据工作获取对应的工作纪要附件
	 * 
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TOsTaskAttach> getTaskSummaryAttachListByTaskId(String taskId)
			throws ServiceException, SystemException;

	/**
	 * 根据附件id获取附件信息
	 * 
	 * @param attachId
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public TOsTaskAttach getAttachById(String attachId)
			throws ServiceException, SystemException;

	/**
	 * 获取工作的承办者字符串
	 * 
	 * @param taskId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getRecvDataByTaskId(String taskId) throws SystemException,
			ServiceException;

	/**
	 * 获取接收单位名称路径
	 * 
	 * @param orgId
	 * @return
	 */
	public String getRecvOrgNamePath(String orgId);
}
