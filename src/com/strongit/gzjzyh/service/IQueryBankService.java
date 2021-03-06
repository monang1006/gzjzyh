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
public interface IQueryBankService {

	/**
	 * 未签收列表
	 * 
	 * @param page -分页信息
	 * @param accoutType -账号类型
	 * @param appFileno -文书号
	 * @param appOrg -经办单位
	 * @param appStartDate -申请开始时间
	 * @param appEndDate -申请结束时间
	 * @return 未签收列表
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public Page<TGzjzyhApplication> findQueryBankNotSignPage(
			Page<TGzjzyhApplication> page, String accoutType, String appFileno,
			String appOrg, Date appStartDate, Date appEndDate)
			throws ServiceException, SystemException, DAOException;

	/**
	 * 已签收列表
	 * 
	 * @param page -分页信息
	 * @param accoutType -账号类型
	 * @param appFileno -文书号
	 * @param appOrg -经办单位
	 * @param appStartDate -申请开始时间
	 * @param appEndDate -申请结束时间
	 * @return 已签收列表
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public Page<TGzjzyhApplication> findQueryBankSignPage(
			Page<TGzjzyhApplication> page, String accoutType, String appFileno,
			String appOrg, Date appStartDate, Date appEndDate)
			throws ServiceException, SystemException, DAOException;

	/**
	 * 已处理列表
	 * 
	 * @param page -分页信息
	 * @param accoutType -账号类型
	 * @param appFileno -文书号
	 * @param appOrg -经办单位
	 * @param appStartDate -申请开始时间
	 * @param appEndDate -申请结束时间
	 * @return 已处理列表
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public Page<TGzjzyhApplication> findQueryBankProcessedPage(
			Page<TGzjzyhApplication> page, String accoutType, String appFileno,
			String appOrg, Date appStartDate, Date appEndDate)
			throws ServiceException, SystemException, DAOException;

	/**
	 * 签收申请
	 * 
	 * @param vo -申请实体类
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void doSign(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException;

	/**
	 * 处理申请
	 * 
	 * @param vo -申请实体类
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public void process(TGzjzyhApplyVo vo)
			throws ServiceException, SystemException, DAOException;
}