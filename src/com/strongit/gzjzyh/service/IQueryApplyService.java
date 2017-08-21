package com.strongit.gzjzyh.service;

import java.util.Date;

import com.strongit.gzjzyh.po.TGzjzyhApplication;
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
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public Page<TGzjzyhApplication> findQueryApplyPage(Page<TGzjzyhApplication> page,String accoutType, String appFileno, String appBankuser,
			Date appStartDate, Date appEndDate)
			throws ServiceException,SystemException, DAOException;
}