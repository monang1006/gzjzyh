package com.strongit.oa.infoManager;

import java.util.List;

import com.strongit.oa.bo.ToaSysmanagePmanager;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IInfoManagerService {

	/**
	 * 保存， 记录信息项使用记录()
	 * @author zhengzb
	 * @desc 
	 * 2010-11-8 下午07:55:48 
	 * @param infoManagerModel
	 * @param loginfos
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveInfoManager(ToaSysmanagePmanager infoManagerModel,OALogInfo ... loginfos)throws SystemException,ServiceException;
	
	/**
	 * 根据条件查询信息项使用记录列表
	 * @author zhengzb
	 * @desc 
	 * 2010-11-8 下午08:01:50 
	 * @param infoItemId           信息项ID
	 * @param infoBusinessId       信息项业务数据（注：模糊查询） ：表单ID+“，”+信息项值+“，”+信息集值  
	 * @param loginfos
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaSysmanagePmanager> getListByQuery(String infoItemId,String infoBusinessId,OALogInfo ... loginfos)throws SystemException,ServiceException;
	
	/**
	 * 根据infoItemId查询信息集或信息项，是否使用
	 * @author zhengzb
	 * @desc 
	 * 2010-11-30 上午10:05:12 
	 * @param infoItemId    “表名”或“表名+$+字段名"
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int isHasInfoFormUsed(String infoItemId) throws SystemException,ServiceException;
	
	
	/**
	 * 根据信息管理编码ID删除信息管理
	 * @author zhengzb
	 * @desc 
	 * 2010-11-8 下午08:04:00 
	 * @param infoManagerCode  信息管理编码ID
	 * @param loginfos
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByInfoManagerCode(String infoManagerCode,OALogInfo ... loginfos)throws SystemException,ServiceException;
	
	/***
	 * 删除List<ToaSysmanagePmanager> pmModeList
	 * @author zhengzb
	 * @desc 
	 * 2010-11-9 下午01:51:30 
	 * @param pmModeList				
	 * @param logInfos
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByList(List<ToaSysmanagePmanager> pmModeList,OALogInfo ...logInfos)throws SystemException,ServiceException;
	
	/**
	 * 根据信息管理编码 获取信息管理对象
	 * @author zhengzb
	 * @desc 
	 * 2010-11-8 下午08:05:58 
	 * @param infoManagerCode 信息管理编码ID
	 * @param loginfos
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaSysmanagePmanager getPmanagerModel(String infoManagerCode,OALogInfo ... loginfos)throws SystemException,ServiceException;
}
