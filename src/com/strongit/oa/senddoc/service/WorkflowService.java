package com.strongit.oa.senddoc.service;

import java.util.List;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * 处理工作流代办事宜接口
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Nov 11, 2011
 * @classpath	com.strongit.oa.senddoc.service.WorkflowService
 * @version  	3.0.2
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
public interface WorkflowService {

	/**
	 * 得到待签收公文列表 （办公厅需求）
	 * @param page
	 * @param workflowType
	 * @return
	 */
	public Page findNotSignTodoForPage(Page page,String workflowType) throws ServiceException,SystemException;
	
	/**
	 * 得到代办公文列表 （不含待签收公文）
	 * @param page
	 * @param workflowType
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public Page findSignTodoForPage(Page page,String workflowType) throws ServiceException,SystemException;
	
	/**
	 * 得到待签收公文列表 （办公厅需求）
	 * @param page
	 * @param workflowType
	 * @return
	 */
	public List findNotSignTodoForList(String workflowType) throws ServiceException,SystemException;
	
	/**
	 * 得到代办公文列表 （不含待签收公文）
	 * @param page
	 * @param workflowType
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List findSignTodoForList(String workflowType) throws ServiceException,SystemException;
	
	/**
	 * 得到待签收公文列表 （办公厅需求）
	 * @param page
	 * @param workflowType
	 * @return
	 */
	public List findNotSignTodoForList(List<Object[]> list) throws ServiceException,SystemException;
	
	/**
	 * 得到代办公文列表 （不含待签收公文）
	 * @param page
	 * @param workflowType
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List findSignTodoForList(List<Object[]> list) throws ServiceException,SystemException;
}
