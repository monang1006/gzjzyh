package com.strongit.gzjzyh.service;

import java.util.Date;

import com.strongit.gzjzyh.po.TGzjzyhApplication;
import com.strongit.gzjzyh.po.TGzjzyhCase;
import com.strongit.gzjzyh.vo.TGzjzyhApplyVo;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * @description: 
 * @company: Thinvent Digital Technology co., Ltd. (c) copyright
 * @author: 
 * @CreateDate: 2017年8月17日
 * @version: V2.0
 */
public interface IQueryApplyService {

	/**
	 * 查询申请分页列表
	 * 
	 * @param page -分页对象
	 * @param accoutType -账户类型
	 * @param appFileno -文书号
	 * @param appBankuser -查询银行
	 * @param appStartDate -申请开始时间
	 * @param appEndDate -申请结束时间
	 * @param caseCode -案件编码
	 * @return 申请分页列表
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public Page<TGzjzyhApplication> findQueryApplyPage(
			Page<TGzjzyhApplication> page, String searchRequiredType,
			String searchAppFileNo, String searchAppBankuser, Date searchAppStartDate,
			Date searchAppEndDate, String searchCaseId, String searchAppStatus)
			throws ServiceException, SystemException, DAOException;
	
	/**
	 * 查询个人桌面首页申请信息
	 * 
	 * @param page -分页对象
	 * @return 申请信息
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public Page<TGzjzyhApplication> findDesktopQueryApplyPage(
			Page<TGzjzyhApplication> page) throws ServiceException, SystemException, DAOException;

	/**
	 * 查询案件分页列表
	 * 
	 * @param page -分页信息
	 * @param caseCode -案件编号
	 * @param caseName -案件名称
	 * @return 案件分页列表
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public Page<TGzjzyhCase> findCasePage(Page<TGzjzyhCase> page,
			String caseCode, String caseName)
			throws ServiceException, SystemException, DAOException;

	/**
	 * 保存申请
	 * 
	 * @param vo -申请实体
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void save(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException;

	/**
	 * 删除申请
	 * 
	 * @param ids -申请Id，多个以，分隔
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void delete(String ids)
			throws ServiceException, SystemException, DAOException;

	/**
	 * 根据申请Id得到申请关联信息
	 * 
	 * @param appId -申请Id
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public TGzjzyhApplyVo getApplyById(String appId)
			throws ServiceException, SystemException, DAOException;
	
	/**
	 * 根据申请Id得到申请信息
	 * 
	 * @param appId -申请Id
	 * @return 申请信息
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public TGzjzyhApplication getApplicationById(String appId)
			throws ServiceException, SystemException, DAOException;
	
	/**
	 * 根据案件Id得到案件信息
	 * 
	 * @param caseId -案件Id
	 * @return 案件信息
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public TGzjzyhCase getCaseById(String caseId)
			throws ServiceException, SystemException, DAOException;

	/**
	 * 根据用户Id得到申请关联信息
	 * 
	 * @param userId -用户Id
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public TGzjzyhApplyVo getExtensionByUserId(String userId)
			throws ServiceException, SystemException, DAOException;

	/**
	 * 批量提交申请
	 * 
	 * @param ids -申请Id，多个以，分隔
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void goCommits(String ids)
			throws ServiceException, SystemException, DAOException;

	/**
	 * 申请撤消
	 * 
	 * @param ids -申请Id，多个以，分隔
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void goBack(String ids)
			throws ServiceException, SystemException, DAOException;

	/**
	 * 保存并提交申请
	 * 
	 * @param vo -申请实体
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void saveOrCommit(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException;

	/**
	 * 根据申请Id得到申请查看关联信息
	 * 
	 * @param appId -申请Id
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public TGzjzyhApplyVo getViewById(String appId)
			throws ServiceException, SystemException, DAOException;

}