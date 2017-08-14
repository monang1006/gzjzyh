package com.strongit.oa.common.business.jbpmtaskextend;

import java.util.List;

import com.strongit.oa.bo.TJbpmTaskExtend;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 流程任务扩展管理类
 * 
 * @author yanjian
 *
 * Nov 23, 2012 9:33:57 PM
 */
public interface IJbpmTaskExtendService {
	/**
	 * 保存数据
	 * 
	 * @author yanjian
	 * @param model
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 23, 2012 9:39:31 PM
	 */
	public void saveModel(TJbpmTaskExtend model) throws ServiceException,DAOException, SystemException;
	/**
	 * 
	 * 获取数据
	 * 
	 * @author yanjian
	 * @param id
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 23, 2012 9:54:25 PM
	 */
	public TJbpmTaskExtend getModelById(String id) throws ServiceException,DAOException, SystemException;
	/**
	 * 根据一组任务实例id，获取相应的扩展数据
	 * 
	 * 
	 * @author yanjian
	 * @param taskInstanceIds
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 23, 2012 10:01:14 PM
	 */
	public List<TJbpmTaskExtend> getInfoForListByTaskInstanceId(Long[] taskInstanceIds) throws ServiceException,DAOException, SystemException;
}
