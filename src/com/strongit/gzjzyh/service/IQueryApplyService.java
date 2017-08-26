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
	 * @param page
	 * @param accoutType
	 * @param appFileno
	 * @param appBankuser
	 * @param appStartDate
	 * @param appEndDate
	 * @param caseCode
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public Page<TGzjzyhApplication> findQueryApplyPage(
			Page<TGzjzyhApplication> page, String accoutType, String appFileno,
			String appBankuser, Date appStartDate, Date appEndDate,
			String caseCode)
			throws ServiceException, SystemException, DAOException;

	/**
	 * @param page
	 * @param caseCode
	 * @param caseName
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public Page<TGzjzyhCase> findCasePage(Page<TGzjzyhCase> page,
			String caseCode, String caseName)
			throws ServiceException, SystemException, DAOException;

	/**
	 * @param vo
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void save(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException;

	/**
	 * @param vo
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void update(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException;

	/**
	 * @param ids
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void delete(String ids)
			throws ServiceException, SystemException, DAOException;

	/**
	 * @param appId
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public TGzjzyhApplyVo getApplyById(String appId)
			throws ServiceException, SystemException, DAOException;

	/**
	 * @param userId
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public TGzjzyhApplyVo getExtensionByUserId(String userId)
			throws ServiceException, SystemException, DAOException;

	/**批量提交
	 * @param ids
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void goCommits(String ids)
			throws ServiceException, SystemException, DAOException;

	/**撤消
	 * @param ids
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void goBack(String ids)
			throws ServiceException, SystemException, DAOException;

	/**保存并提交
	 * @param ids
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void saveOrCommit(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException;

	/**修改并提交
	 * @param ids
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void updateOrCommit(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException;

	/**
	 * @param appId
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public TGzjzyhApplyVo getViewById(String appId)
			throws ServiceException, SystemException, DAOException;

}