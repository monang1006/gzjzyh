package com.strongit.oa.docmonitor.service;

import java.util.Map;

import com.strongit.oa.docmonitor.vo.DocMonitorParamter;
import com.strongit.oa.docmonitor.vo.DocMonitorVo;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IDocMonitorService {
	/**
	 * 个人权限待办数据
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Sep 25, 2012 11:44:20 AM
	 */
	public Page<DocMonitorVo> getPersonalTodo(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;

	/**
	 * 处领导权限待办数据
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Sep 25, 2012 11:44:53 AM
	 */
	public Page<DocMonitorVo> getDLeaderTodo(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;

	/**
	 * 厅领导权限待办数据
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Sep 25, 2012 11:45:54 AM
	 */
	public Page<DocMonitorVo> getTLeaderTodo(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;

	/**
	 * 个人权限已办未办结数据
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Sep 25, 2012 11:44:20 AM
	 */
	public Page<DocMonitorVo> getPersonalProcessing(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;

	/**
	 * 处领导权限已办未办结数据
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Sep 25, 2012 11:44:53 AM
	 */
	public Page<DocMonitorVo> getDLeaderProcessing(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;

	/**
	 * 听领导权限已办未办结数据
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Sep 25, 2012 11:45:54 AM
	 */
	public Page<DocMonitorVo> getTLeaderProcessing(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;

	/**
	 * 个人权限已办办结数据
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Sep 25, 2012 11:44:20 AM
	 */
	public Page<DocMonitorVo> getPersonalProcessed(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;

	/**
	 * 处领导权限已办办结数据
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Sep 25, 2012 11:44:53 AM
	 */
	public Page<DocMonitorVo> getDLeaderProcessed(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;

	/**
	 * 听领导权限已办办结数据
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Sep 25, 2012 11:45:54 AM
	 */
	public Page<DocMonitorVo> getTLeaderProcessed(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;

	/**
	 * 个人权限所有已办数据
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Sep 25, 2012 11:44:20 AM
	 */
	public Page<DocMonitorVo> getPersonalProcessAll(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;

	/**
	 * 处领导权限所有已办数据
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Sep 25, 2012 11:44:53 AM
	 */
	public Page<DocMonitorVo> getDLeaderProcessAll(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;

	/**
	 * 听领导权限所有已办数据
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 *             Sep 25, 2012 11:45:54 AM
	 */
	public Page<DocMonitorVo> getTLeaderProcessAll(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;
	/**
	 * 获取分管领导柱状图显示的待办数据
	 * 
	 * @author yanjian
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Sep 25, 2012 4:22:19 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String[]> getManagerDeptTodoDocs(DocMonitorParamter param)
			throws ServiceException, DAOException, SystemException;
	/**
	 * 查看单个处室的待办文件情况
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Sep 25, 2012 4:48:35 PM
	 */
	@SuppressWarnings("unchecked")
	public Page<DocMonitorVo> getTodoDeptLocaltion(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;
	/**
	 * 查看单个处室的已办文件情况
	 * 
	 * @author yanjian
	 * @param page
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Sep 26, 2012 10:02:28 AM
	 */
	public Page<DocMonitorVo> getProcessDeptLocaltion(Page<DocMonitorVo> page,
			DocMonitorParamter param) throws ServiceException, DAOException,
			SystemException;
	/**
	 * 获取分管领导柱状图显示的已办数据
	 * 
	 * @author yanjian
	 * @param param
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Sep 25, 2012 7:48:27 PM
	 */
	public Map<String,String[]> getManagerDeptProcessDocs(DocMonitorParamter param) throws ServiceException,DAOException, SystemException;
	
}
