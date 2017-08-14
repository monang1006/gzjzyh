package com.strongit.oa.workinspect.worktodo;

import java.io.File;
import java.util.List;

import com.strongit.oa.bo.TOsManagementSummary;
import com.strongit.oa.bo.TOsTaskAttach;
import com.strongit.oa.bo.TOsWorktaskSend;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IWorkTodoService {
	/**
	 * Description：得到个人待办工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<GunsReq>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getAllTOsWorktaskSend(
			Page<TOsWorktaskSend> page, TOsWorktaskSend tOsWorktaskSend)
			throws DAOException, SystemException, ServiceException;

	/**
	 * Description：得到个人带办工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<GunsReq>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getAllTOsWorktaskTodo(
			Page<TOsWorktaskSend> page, TOsWorktaskSend tOsWorktaskSend)
			throws DAOException, SystemException, ServiceException;

	/**
	 * Description：得到部门待办工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<GunsReq>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getUnitWork(Page<TOsWorktaskSend> page,
			TOsWorktaskSend tOsWorktaskSend) throws DAOException,
			SystemException, ServiceException;

	public TOsWorktaskSend getById(String id);

	/**
	 * 保存任务下发表
	 * 
	 * @param towsend
	 * @param sendtaskId
	 * @param file
	 * @param fileFileName
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void save(TOsWorktaskSend tOsWorktaskSend, File[] file,
			String[] fileFileName) throws ServiceException, SystemException;

	/**
	 * 保存办理纪要
	 * @param summary
	 * @param file
	 * @param fileFileName
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void save(TOsManagementSummary summary, File[] file,
			String[] fileFileName) throws ServiceException, SystemException;

	/**
	 * 删除办理纪要附件
	 * 
	 * @param attId
	 * @param selectedData
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void delAttach(String attId)
			throws ServiceException, SystemException;
	
	/**
	 * 签收工作
	 * @param summary
	 * @param file
	 * @param fileFileName
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void siGn(TOsWorktaskSend summary) throws ServiceException,
			SystemException;

	/**
	 * Description：得到部门待签收工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<GunsReq>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getWorkTobe(Page<TOsWorktaskSend> page,
			TOsWorktaskSend tOsWorktaskSend) throws DAOException,
			SystemException, ServiceException;

	/**
	 * Description：得到部门待办理工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<GunsReq>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getWorkTodo(Page<TOsWorktaskSend> page,
			TOsWorktaskSend tOsWorktaskSend) throws DAOException,
			SystemException, ServiceException;

	/**
	 * Description：得到个人未签收工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<TOsWorktaskSend>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getSend(Page<TOsWorktaskSend> page,
			TOsWorktaskSend tOsWorktaskSend) throws DAOException,
			SystemException, ServiceException;

	/**
	 * Description：得到个人未签收工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<TOsWorktaskSend>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getAllnotsign(Page<TOsWorktaskSend> page,
			TOsWorktaskSend tOsWorktaskSend) throws DAOException,
			SystemException, ServiceException;

	/**
	 * Description：个人桌面得到个人未签收工作
	 * 
	 * @param page
	 *            分页对象
	 * @param TOsWorktaskSend
	 *            个人待办对象
	 * @return Page<TOsWorktaskSend>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TOsWorktaskSend> getAllnotTodo(Page<TOsWorktaskSend> page,
			TOsWorktaskSend tOsWorktaskSend) throws DAOException,
			SystemException, ServiceException;

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
	 * author:luosy
	 * description:得到公共附件
	 * modifyer:
	 * description:
	 * @param attachId 公共附件ID
	 * @return	ToaAttachment 公共附件对象
	 */
	public ToaAttachment getToaAttachmentById(String attachId,String rest2)throws SystemException,ServiceException;
		
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

	public List<TOsTaskAttach> getSummaryListBySummaryId(String summaryId)
			throws ServiceException, SystemException;

	public Page<TOsWorktaskSend> getAllTOsWork(Page<TOsWorktaskSend> page,
			TOsWorktaskSend model);
}
