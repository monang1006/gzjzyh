package com.strongit.xxbs.service;

import java.util.List;

import com.strongit.xxbs.entity.TInfoBaseInfoReport;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IInfoReportService
{
	public TInfoBaseInfoReport getReport(String rpId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseInfoReport> getReports()
			throws ServiceException,SystemException, DAOException;
		
	public Page<TInfoBaseInfoReport> getReports(Page<TInfoBaseInfoReport> page)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBaseInfoReport> getReportsByTitle(Page<TInfoBaseInfoReport> page,String rpTitle)
	throws ServiceException,SystemException, DAOException;
	
	public void saveReport(TInfoBaseInfoReport report)
			throws ServiceException,SystemException, DAOException;
	
	public String deleteReport(String rpId)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBaseInfoReport> lastReports()
			throws ServiceException,SystemException, DAOException;
	
	
	/*
	 * 通报的表头各机构的采用数量
	 * 
	 */
	public int[] adoptReports(String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 统计各地市的的通报数量（flag:机构类型）
	 * 
	 */
	public List getOrgReport(String flag,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 统计各地市的的呈批数量
	 * 
	 */
	public List getOrgReport1(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;

	
	public List getOrgReport10(String syscode,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 统计各地市的的呈阅数量
	 * 
	 */
	public List getOrgReport5(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 统计各地市的的批转数量
	 * 
	 */
	public List getOrgReport6(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 统计各地市的的期刊采用数量
	 * 
	 */
	public List getOrgReport2(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	

	/*
	 * 根据机构ID查出已成刊的文章
	 * 
	 */
	public List getOrgReport3(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 根据机构ID查出市级单位已成刊的文章
	 * 
	 */
	public List getOrgReport11(String syscode,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 根据机构ID查出呈阅件的文章
	 * 
	 */
	public List getOrgReport4(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	/*
	 * 根据机构ID查出呈国办的文章
	 * 
	 */
	public List getOrgReport7(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	/*
	 * 根据机构ID查出上报国办的文章
	 * 
	 */
	public List getOrgReport4s(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 根据机构ID查出省办的文章
	 * 
	 */
	public List getOrgReportSB(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	/*
	 * 根据机构ID查出本月加分的信息
	 * 
	 */
	public List getOrgReportJF(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	/*
	 * 根据机构ID查出呈阅件的文章得分
	 * 
	 */
	public List getOrgReport8(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	/*
	 * 统计各地市的的上报国办数量
	 * 
	 */
	public List getOrgReport9(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 根据机构ID查出市级呈国办的文章得分
	 * 
	 */
	public List getOrgReport7s(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 根据机构ID市级查出呈阅件的文章得分
	 * 
	 */
	public List getOrgReport8s(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	
	/*
	 * 根据机构ID查出市级呈阅件的文章
	 * 
	 */
	public List getOrgReport4sj(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 根据机构ID查出市级呈国办的文章
	 * 
	 */
	public List getOrgReport5sj(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	public List getOrgReport6sj(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	public List getOrgReport5dsj(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	public List getOrgReport9sj(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	public List getOrgReport1sj(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 统计市级各地市的的当前年的累计得分
	 * 
	 */
	public Integer getOrgReportsjsum(String syscode, String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 统计市级各地市的的当前年的当前月得分
	 * 
	 */
	public Integer getOrgReportdqsjsum(String syscode, String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 统计各地市的的期刊当前月的分数
	 * 
	 */
	public Integer getOrgReportdqsum(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 统计各地市的的期刊当前月的分数
	 * 
	 */
	public Integer getOrgReportsum(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 根据机构ID查出市级单位已成刊的文章只查询出差出访栏目信息数
	 * 
	 */
	public List getOrgReportSJTrip (String syscode,String year,String month)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 根据机构ID查出已成刊的文章只查询出差出访栏目信息数
	 * 
	 */
	public List getOrgReportTrip(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException;
}
