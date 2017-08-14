package com.strongit.xxbs.service;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.strongit.xxbs.dto.StatisticsDto;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.entity.TInfoQueryTree;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface ISubmitService
{
	
	public List<TInfoQueryTree> getQueryTree()
			throws ServiceException, SystemException, DAOException;
	
	public Page<TInfoBasePublish> findSubmittedByQs(
			Page<TInfoBasePublish> page, String qs) throws ServiceException,
			SystemException, DAOException;
	
	public TreeSet<StatisticsDto> getTotalStatistics()
			throws ServiceException,SystemException, DAOException;
	
	public Map<String, StatisticsDto> getStatistics()
			throws ServiceException,SystemException, DAOException;

	public List<TInfoBasePublish> lastSubmitted(String orgId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBasePublish> lastUsed(String orgId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBasePublish> lastShared()
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBasePublish> findUsedByDate(String date, String orgId)
			throws ServiceException,SystemException, DAOException;
	
	public String getPublishNum(String sdate, String orgId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBasePublish> findUsedByDate1(String date, String orgId)
			throws ServiceException, SystemException, DAOException;
	
	public List<?> getStatistics1(String org[],String useStatus,int curpage,int unitpage) throws ServiceException,
			SystemException, DAOException;
	
	public List<?> getStatistics2(String org[],String year) throws ServiceException,
	SystemException, DAOException;

	public List<?> getStatistics3(String org[],String year) throws ServiceException,
	SystemException, DAOException;
	
	public List<?> getStatistics4(String org[],String year) throws ServiceException,
	SystemException, DAOException;
	
	public List<?> getStatistics5(String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	public List<?> getStatistics6(String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询各处室信息采用情况（其他信息呈国办或呈阅件）通报
	 * 
	 */
	public List<?> getStatistics6s(String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	public List<?> getStatistics7(String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 首页厅处室采用情况统计列表
	 * 
	 */
	public List<?> getStatistics8(String org[],String year) throws ServiceException,
	SystemException, DAOException;
	/**
	 * 首页采用排名
	 * 
	 */
	public List<?> getStatistics9(String org[],String year) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 采用排名显示具体分数
	 * 
	 */
	public List<?> getStatistics10(String org,String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询市级各机构每月得分
	 * 
	 */
	public List<?> getStatistics11(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException;
	
	
	/**
	 * 查询市级单位机构名称和总得分报表
	 * 
	 */
	public List<?> getStatistics12(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 采用排名市级显示具体分数
	 * 
	 */
	public List<?> getStatistics10s(String org,String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	public List<TInfoBasePublish> publishlist(String strDate,String endDate) throws ServiceException, SystemException,
	DAOException;
	
	/**
	 * 统计每月上报信息条数
	 * 
	 */
	public List<?> getStatisticsByMonthSubmit(String org[],String year) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 统计每月采用信息条数
	 * 
	 */
	public List<?> getStatisticsByMonthUse(String org[],String year) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 统计县级全年上报信息条数按上报信息排序
	 * 
	 */
	public List<?> getStatisticsByXJYearSubmit(String org[],String year) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 统计县级全年采用信息条数按采用信息排序
	 * 
	 */
	public List<?> getStatisticsByXJYearUseOrder(String org[],String year) throws ServiceException,
	SystemException, DAOException;
	/**
	 * 统计全年上报信息条数根据上报信息排序
	 * 
	 */
	public List<?> getStatisticsByYearSubmit(String org[],String year) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 统计全年上采用信息条数根据采用信息排序
	 * 
	 */
	public List<?> getStatisticsByYearUseOrder(String org[],String year) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 统计全年上报信息条数(按采用信息排序)
	 * 
	 */
	public List<?> getStatisticsByYearSubmitOrder(String org[],String year) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 统计全年采用信息条数(按上报信息排序)
	 * 
	 */
	public List<?> getStatisticsByYearUse(String org[],String year) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 统计市级每月上报信息条数
	 * 
	 */
	public List<?> getStatisticsBySJMonthSubmit(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 统计市级每月采用信息条数
	 * 
	 */
	public List<?> getStatisticsBySJMonthUse(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 统计市级当前年上报信息条数按上报信息排序
	 * 
	 */
	public List<?> getStatisticsBySJYearSubmit(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 统计市级当前年采用信息条数按采用信息排序
	 * 
	 */
	public List<?> getStatisticsBySJYearUseOrder(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 统计市级当前年上报信息条数(按采用信息排序)
	 * 
	 */
	public List<?> getStatisticsBySJYearSubmitOrder(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 统计市级当前年采用信息条数（按上报信息排序）
	 * 
	 */
	public List<?> getStatisticsBySJYearUse(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询市级每日要情数量
	 * 
	 */
	public List<?> getStatisticsByYQ(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询市级南昌政务数量
	 * 
	 */
	public List<?> getStatisticsByZW(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询市级领导批示数量
	 * 
	 */
	public List<?> getStatisticsByPS(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询省级每日要情数量
	 * 
	 */
	public List<?> getStatisticsBySJYQ(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询省级江西政务数量
	 * 
	 */
	public List<?> getStatisticsBySJZW(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询省级数量
	 * 
	 */
	public List<?> getStatisticsBySJ(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询省级领导批示数量
	 * 
	 */
	public List<?> getStatisticsBySJPS(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询国办信息
	 * 
	 */
	public List<?> getStatisticsByGBXX(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询国办信息领导批示
	 * 
	 */
	public List<?> getStatisticsByGBXXPS(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询本月加分
	 * 
	 */
	public List<?> getStatisticsByJF(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询本月得分
	 * 
	 */
	public List<?> getStatisticsByDF(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	

	/**
	 * 查询上月累计
	 * 
	 */
	public List<?> getStatisticsBySYLJ(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	
	/**
	 * 查询累计得分
	 * 
	 */
	public List<?> getStatisticsByLJDF(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException;
	
}
