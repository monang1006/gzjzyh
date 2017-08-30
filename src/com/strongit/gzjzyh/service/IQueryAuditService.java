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
public interface IQueryAuditService {

	/**未审核列表
	 * @param page
	 * @param accoutType
	 * @param appFileno
	 * @param appBankuser
	 * @param appStartDate
	 * @param appEndDate
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public Page<TGzjzyhApplication> findQueryAuditPage(
			Page<TGzjzyhApplication> page, String accoutType, String appFileno,
			String appBankuser, Date appStartDate, Date appEndDate, String appOrg)
			throws ServiceException, SystemException, DAOException;

	/**已审核列表
	 * @param page
	 * @param accoutType
	 * @param appFileno
	 * @param appBankuser
	 * @param appStartDate
	 * @param appEndDate
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public Page<TGzjzyhApplication> findQueryCheckedPage(
			Page<TGzjzyhApplication> page, String accoutType, String appFileno,
			String appBankuser, Date appStartDate, Date appEndDate, String appOrg)
			throws ServiceException, SystemException, DAOException;

	/**
	 * @param vo
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void audit(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException;

}