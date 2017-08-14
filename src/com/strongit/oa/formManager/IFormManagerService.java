package com.strongit.oa.formManager;

import java.util.List;

import com.strongit.oa.bo.ToaSysmanageFormManager;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IFormManagerService {
	/**
	 * 保存， 记录表单使用记录()
	 * @author zhengzb
	 * @desc 
	 * 2010-11-8 下午07:55:48 
	 * @param infoManagerModel
	 * @param loginfos
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveFormManager(ToaSysmanageFormManager FmModel,OALogInfo ... loginfos)throws SystemException,ServiceException;
	
	/**
	 * 根据条件查询表单使用记录列表
	 * @author zhengzb
	 * @desc 
	 * 2010-11-8 下午08:01:50 
	 * @param formId           表单ID
	 * @param formBusinessId       表单业务ID
	 * @param loginfos
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaSysmanageFormManager> getListByQuery(String formId,String formBusinessId,OALogInfo ... loginfos)throws SystemException,ServiceException;
	
	
	/**
	 * 根据表单管理编码ID删除表单管理
	 * @author zhengzb
	 * @desc 
	 * 2010-11-8 下午08:04:00 
	 * @param formId  表单管理编码ID
	 * @param loginfos
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByFormManagerCode(String formId,OALogInfo ... loginfos)throws SystemException,ServiceException;
	
	/**
	 * 删除List<ToaSysmanageFormManager> 对象
	 * @author zhengzb
	 * @desc 
	 * 2010-11-9 下午03:39:44 
	 * @param fmMoList       
	 * @param loginfos
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByList(List<ToaSysmanageFormManager> fmMoList,OALogInfo ... loginfos)throws SystemException,ServiceException;
	
	/**
	 * 根据表单管理编码 获取表单管理对象
	 * @author zhengzb
	 * @desc 
	 * 2010-11-8 下午08:05:58 
	 * @param formManagerCode 表单管理编码ID
	 * @param loginfos
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaSysmanageFormManager getPmanagerModel(String formManagerCode,OALogInfo ... loginfos)throws SystemException,ServiceException;
	
	/**
	 * 根据表单ID，查询表单中单表，使用记录
	 * @author zhengzb
	 * @desc 
	 * 2010-11-30 上午11:20:44 
	 * @param formId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int isHasEFormUsed(String formId)throws SystemException,ServiceException;

}
