package com.strongit.gzjzyh.statistic;

import java.util.List;

import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * @description:
 * @company: Thinvent Digital Technology co., Ltd. (c) copyright
 * @author:
 * @CreateDate: 2017年8月17日
 * @version: V2.0
 */
public interface IStatisticService {

	/**
	 * 时效统计
	 * 
	 * @param isOrder
	 *            -是否进行排序
	 * @param orderColumn
	 *            -排序字段
	 * @param orderType
	 *            -排序类型
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public List<Object[]> efficiencyStatistic(boolean isOrder,
			String orderColumn, String orderType) throws ServiceException,
			SystemException, DAOException;

	/**
	 * 时间统计
	 * 
	 * @param isOrder
	 *            -是否进行排序
	 * @param orderColumn
	 *            -排序字段
	 * @param orderType
	 *            -排序类型
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public List<Object[]> timeStatistic(boolean isOrder, String orderColumn,
			String orderType) throws ServiceException, SystemException,
			DAOException;

	/**
	 * 地区统计
	 * 
	 * @param isOrder
	 *            -是否进行排序
	 * @param orderColumn
	 *            -排序字段
	 * @param orderType
	 *            -排序类型
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public List<Object[]> areaStatistic(boolean isOrder, String orderColumn,
			String orderType) throws ServiceException, SystemException,
			DAOException;

	/**
	 * 银行统计
	 * 
	 * @param isOrder
	 *            -是否进行排序
	 * @param orderColumn
	 *            -排序字段
	 * @param orderType
	 *            -排序类型
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public List<Object[]> bankStatistic(boolean isOrder, String orderColumn,
			String orderType) throws ServiceException, SystemException,
			DAOException;

	/**
	 * 得到所有一级组织机构名称
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public List[] getAllOrgNames() throws ServiceException,
			SystemException, DAOException;

	/**
	 * 得到所有银行名称
	 * 
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 * @throws DAOException
	 */
	public List<String> getAllBankNames() throws ServiceException,
			SystemException, DAOException;

}