package com.strongit.xxbs.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.entity.TInfoBaseInfoReport;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.service.IInfoReportService;
import com.strongit.xxbs.service.IOrgService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional(readOnly = true)
public class InfoReportService implements IInfoReportService
{
	private GenericDAOHibernate<TInfoBaseInfoReport, String> rpDao;
	private GenericDAOHibernate<TInfoBasePublish, String> publishDao;
	private IOrgService orgService;
	
	
	@Autowired
	public void setOrgService(IOrgService orgService)
	{
		this.orgService = orgService;
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		rpDao = new GenericDAOHibernate<TInfoBaseInfoReport, String>(
				sessionFactory, TInfoBaseInfoReport.class);
		publishDao = new GenericDAOHibernate<TInfoBasePublish, String>(
				sessionFactory, TInfoBasePublish.class);
	}

	public TInfoBaseInfoReport getReport(String rpId) throws ServiceException,
			SystemException, DAOException
	{
		return rpDao.get(rpId);
	}

	public List<TInfoBaseInfoReport> getReports() throws ServiceException,
			SystemException, DAOException
	{
		return rpDao.findAll();
	}

	public Page<TInfoBaseInfoReport> getReports(Page<TInfoBaseInfoReport> page)
			throws ServiceException, SystemException, DAOException
	{
		return rpDao.findAll(page);
	}
	
	public Page<TInfoBaseInfoReport> getReportsByTitle(Page<TInfoBaseInfoReport> page,String rpTitle)
	throws ServiceException, SystemException, DAOException
	{
		Criterion criterion = Restrictions.like("rpTitle", "%"+rpTitle+"%");
		return rpDao.findByCriteria(page, criterion);
	}

	@Transactional(readOnly = false)
	public void saveReport(TInfoBaseInfoReport report) throws ServiceException,
			SystemException, DAOException
	{
		rpDao.save(report);
	}

	@Transactional(readOnly = false)
	public String deleteReport(String rpId) throws ServiceException,
			SystemException, DAOException
	{
		rpDao.delete(rpId);
		return null;
	}

	public List<TInfoBaseInfoReport> lastReports() throws ServiceException,
			SystemException, DAOException
	{
		@SuppressWarnings("unchecked")
		List<TInfoBaseInfoReport> ret = rpDao.getSession()
				.createCriteria(TInfoBaseInfoReport.class)
				.addOrder(Order.desc("rpDate"))
				.setMaxResults(8)
				.list();
		return ret;
	}

	/*
	 * 通报的表头各机构的采用数量
	 * 
	 */
	public int[] adoptReports(String year,String month)
	throws ServiceException,SystemException, DAOException
	{
		String b[] = orgService.getOrgIdsByisOrg("0");
		String aid = "";
		for(int i=0;i<b.length;i++){
			aid = aid +"'"+b[i]+"',";
		}
		if(!"".equals(aid)){
			aid = aid.substring(0, aid.length()-1);
			}
		
		String c[] = orgService.getOrgIdsByisOrg("1");
		String aid1 = "";
		for(int i=0;i<c.length;i++){
			aid1 = aid1 +"'"+c[i]+"',";
		}
		if(!"".equals(aid1)){
			aid1 = aid1.substring(0, aid1.length()-1);
			}
		
		String d[] = orgService.getOrgIdsByisOrg("2");
		String aid2 = "";
		for(int i=0;i<d.length;i++){
			aid2 = aid2 +"'"+d[i]+"',";
		}
		if(!"".equals(aid2)){
			aid2 = aid2.substring(0, aid2.length()-1);
			}
		
		String e[] = orgService.getOrgIdsByisOrg("3");
		String aid3 = "";
		for(int i=0;i<e.length;i++){
			aid3 = aid3 +"'"+e[i]+"',";
		}
		if(!"".equals(aid3)){
			aid3 = aid3.substring(0, aid3.length()-1);
			}
		
		String f[] = orgService.getOrgIdsByisOrg("4");
		String aid4 = "";
		for(int i=0;i<f.length;i++){
			aid4 = aid4 +"'"+f[i]+"',";
		}
		if(!"".equals(aid4)){
			aid4 = aid4.substring(0, aid4.length()-1);
			}
		
		String sql = "SELECT COUNT(*) FROM(SELECT TAB1.ORG_ID FROM  T_UUMS_BASE_ORG O JOIN (  " +
				" SELECT P.ORG_ID  FROM   T_INFO_BASE_PUBLISH P ,  T_INFO_BASE_ISSUE I WHERE P.ISS_ID=I.ISS_ID " +
				" AND I.ISS_IS_PUBLISH=1 AND P.PUB_USE_STATUS=1 AND to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"')TAB1 " +
						" ON TAB1.ORG_ID = O.ORG_ID WHERE O.ORG_NATURE!=4)TAB2 LEFT JOIN (SELECT P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
						"  WHERE to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"')TAB3 ON TAB2.ORG_ID=TAB3.ORG_ID";
		
		
		
		String sql0 = "SELECT COUNT(*) FROM(SELECT P.ORG_ID FROM   T_INFO_BASE_PUBLISH P ,  T_INFO_BASE_ISSUE I  " +
		"WHERE P.ISS_ID=I.ISS_ID AND P.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH=1  AND  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"'"; 
		if(!"".equals(aid)){
			sql0 = sql0+" AND P.ORG_ID IN("+aid+")";
		}
		else{
			sql0 = sql0+" AND 1!=1";
		}
		sql0 = sql0 + " )TAB1 LEFT JOIN (SELECT P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
				"  WHERE to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"')TAB2 ON TAB2.ORG_ID=TAB1.ORG_ID";
		
		
		
		
		String sql1 = "SELECT COUNT(*) FROM (SELECT TAB3.ORG_ID FROM(SELECT TAB1.ORG_ID FROM  T_UUMS_BASE_ORG O JOIN (  SELECT P.ORG_ID  " +
					  " FROM   T_INFO_BASE_PUBLISH P ,  T_INFO_BASE_ISSUE I  " +
		" WHERE P.ISS_ID=I.ISS_ID  AND I.ISS_IS_PUBLISH=1 AND P.PUB_USE_STATUS=1  AND to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"')TAB1 " +
		" ON TAB1.ORG_ID = O.ORG_ID WHERE  O.ORG_SYSCODE LIKE '001003%' AND O.ORG_NATURE!=5)TAB2 LEFT JOIN (SELECT P.ORG_ID,P.PARENT_ORG_ID FROM  T_INFO_BASE_PIECE P  " +
		"  WHERE to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"')TAB3 ON TAB2.ORG_ID=TAB3.ORG_ID )TAB4  LEFT JOIN (SELECT P.ORG_ID,P.PARENT_ORG_ID FROM  T_INFO_BASE_PIECE P " +
			"  WHERE to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"')TAB5 ON TAB4.ORG_ID=TAB5.PARENT_ORG_ID ";
		
		
		
		
		
		/*String sql2 = "SELECT COUNT(*) FROM  T_INFO_BASE_PUBLISH P , T_INFO_BASE_ISSUE I " +
		"WHERE P.ISS_ID=I.ISS_ID AND P.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH=1  AND  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"'"; 
		if(!"".equals(aid2)){
			sql2 = sql2+" AND P.ORG_ID IN("+aid2+")";
		}
		else{
			sql2 = sql2+" AND 1!=1";
		}*/
		
		String sql2 = "SELECT COUNT(*) FROM(SELECT P.ORG_ID FROM   T_INFO_BASE_PUBLISH P ,  T_INFO_BASE_ISSUE I  " +
		"WHERE P.ISS_ID=I.ISS_ID AND P.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH=1  AND  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"'"; 
		if(!"".equals(aid3)){
			sql2 = sql2+" AND P.ORG_ID IN("+aid2+")";
		}
		else{
			sql2 = sql2+" AND 1!=1";
		}
		sql2 = sql2 + " )TAB1 LEFT JOIN (SELECT P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
				"  WHERE to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"')TAB2 ON TAB2.ORG_ID=TAB1.ORG_ID";
		
		
		
		String sql3 = "SELECT COUNT(*) FROM(SELECT P.ORG_ID FROM   T_INFO_BASE_PUBLISH P ,  T_INFO_BASE_ISSUE I  " +
		"WHERE P.ISS_ID=I.ISS_ID AND P.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH=1  AND  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"'"; 
		if(!"".equals(aid3)){
			sql3 = sql3+" AND P.ORG_ID IN("+aid3+")";
		}
		else{
			sql3 = sql3+" AND 1!=1";
		}
		sql3 = sql3 + " )TAB1 LEFT JOIN (SELECT P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
				"  WHERE to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"')TAB2 ON TAB2.ORG_ID=TAB1.ORG_ID";
		
		
		
		
		String sql4 = "SELECT COUNT(*) FROM  T_INFO_BASE_PUBLISH P , T_INFO_BASE_ISSUE I " +
		"WHERE P.ISS_ID=I.ISS_ID AND P.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH=1  AND  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"'"; 
		if(!"".equals(aid4)){
			sql4 = sql4+" AND P.ORG_ID IN("+aid4+")";
		}
		else{
			sql4 = sql4+" AND 1!=1";
		}
		
		
		
		String sql5 = "SELECT COUNT(*) FROM (SELECT P.ORG_ID FROM   T_INFO_BASE_PUBLISH P ,  T_INFO_BASE_ISSUE I " +
		"WHERE P.ISS_ID=I.ISS_ID AND P.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH=1 AND P.PUB_IS_INSTRUCTION=1 AND   to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"'"+
		" )TAB1 LEFT JOIN  T_UUMS_BASE_ORG O ON  TAB1.ORG_ID=O.ORG_ID WHERE O.ORG_NATURE!=4";
		
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		
		Query query0 = rpDao.getSession().createSQLQuery(sql0);
		List list0 = query0
		.list();
		
		Query query1 = rpDao.getSession().createSQLQuery(sql1);
		List list1 = query1
		.list();
		
		
		Query query2 = rpDao.getSession().createSQLQuery(sql2);
		List list2 = query2
		.list();
		
		Query query3 = rpDao.getSession().createSQLQuery(sql3);
		List list3 = query3
		.list();
		
		Query query4 = rpDao.getSession().createSQLQuery(sql4);
		List list4 = query4
		.list();
		
		Query query5 = rpDao.getSession().createSQLQuery(sql5);
		List list5 = query5
		.list();
		
		int app[] = new int[7];
		if(list.size()>0)
		//采用信息总量
		app[5] =  Integer.valueOf(list.get(0).toString()) ;
		if(list0.size()>0)
		//省政府各有关部门采用信息总数
		app[0] =   Integer.valueOf(list0.get(0).toString());
		if(list1.size()>0)
		//各设区政府采用信息总数
		app[1] = Integer.valueOf(list1.get(0).toString());
		if(list2.size()>0)
		//各县（市区）政府信息总数
		app[2] =   Integer.valueOf(list2.get(0).toString());
		if(list3.size()>0)
		//驻外办信息总数
		app[3] =  Integer.valueOf(list3.get(0).toString());
		if(list4.size()>0)
		//秘书处信息总数
		app[4] =  Integer.valueOf(list4.get(0).toString());
		if(list5.size()>0)
		//领导批示数
		app[6] =  Integer.valueOf(list5.get(0).toString());
		return app;
	}
	
	
	/*
	 * 统计各地市的的通报数量（flag:机构类型）
	 * 
	 */
	public List getOrgReport(String flag,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = "SELECT TAB1.ORG_ID FROM (SELECT P.ORG_ID ,O.ORG_NAME,O.ORG_SEQUENCE,P.ISS_ID FROM   T_INFO_BASE_PUBLISH P  JOIN T_UUMS_BASE_ORG O ON P.ORG_ID=O.ORG_ID " +
				"WHERE O.ORG_NATURE='"+flag+"' AND P.PUB_USE_STATUS=1 AND O.ORG_ISDEL='0' " +
				" GROUP BY P.ORG_ID,O.ORG_NATURE,P.ISS_ID,O.ORG_SEQUENCE,O.ORG_NAME ORDER BY O.ORG_SEQUENCE)TAB1 JOIN  T_INFO_BASE_ISSUE I ON TAB1.ISS_ID=I.ISS_ID " +
				" WHERE  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"' AND I.ISS_IS_PUBLISH=1   GROUP BY TAB1.ORG_ID ,TAB1.ORG_SEQUENCE ORDER BY TAB1.ORG_SEQUENCE ";
		
		
		Query query = rpDao.getSession().createSQLQuery(sql1);
		List list = query
		.list();
		List list3 = new ArrayList();
		List list4 = new ArrayList();
		if("1".equals(flag)){
			
			 String sql3 = "SELECT P.PARENT_ORG_ID FROM  T_INFO_BASE_PIECE P " +
				" WHERE P.PARENT_ORG_ID IS NOT NULL AND to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
			 Query query3 = rpDao.getSession().createSQLQuery(sql3);
			    list3 = query3
				.list();
			    
			    String sql4 = "SELECT TAB1.ORG_PARENT_ID FROM (SELECT O.ORG_PARENT_ID ,O.ORG_NAME,O.ORG_SEQUENCE,P.ISS_ID FROM   T_INFO_BASE_PUBLISH P  JOIN T_UUMS_BASE_ORG O ON P.ORG_ID=O.ORG_ID " +
				"WHERE O.ORG_NATURE='2' AND P.PUB_USE_STATUS=1 AND O.ORG_ISDEL='0' " +
				" GROUP BY O.ORG_PARENT_ID,O.ORG_NATURE,P.ISS_ID,O.ORG_SEQUENCE,O.ORG_NAME ORDER BY O.ORG_SEQUENCE)TAB1 JOIN  T_INFO_BASE_ISSUE I ON TAB1.ISS_ID=I.ISS_ID " +
				" WHERE to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"' AND I.ISS_IS_PUBLISH=1   GROUP BY TAB1.ORG_PARENT_ID ,TAB1.ORG_SEQUENCE ORDER BY TAB1.ORG_SEQUENCE ";
			    Query query4 = rpDao.getSession().createSQLQuery(sql4);
			    list4 = query4
				.list();
		}
		String sql2 = "SELECT P.ORG_ID FROM  T_INFO_BASE_PIECE P , T_UUMS_BASE_ORG O" +
				" WHERE P.ORG_ID=O.ORG_ID AND O.ORG_NATURE='"+flag+"' AND to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query2 = rpDao.getSession().createSQLQuery(sql2);
		List list2 = query2
		.list();
		for(int i=0;i<list2.size();i++){
			list.add(list2.get(i));
		}
		if(list3.size()>0){
			for(int i=0;i<list3.size();i++){
				list.add(list3.get(i));
			}
		}
		if(list4.size()>0){
			for(int i=0;i<list4.size();i++){
				list.add(list4.get(i));
			}
		}
		Set set = new HashSet();
	      List newList = new ArrayList();
	   for (Iterator iter = list.iterator(); iter.hasNext();) {
	          Object element = iter.next();
	          if (set.add(element))
	             newList.add(element);
	       } 
	      list.clear();
	      list.addAll(newList);
		
		return list;
	}
	
	/*
	 * 统计各地市的的呈批数量
	 * 
	 */
	public List getOrgReport1(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = "SELECT SUM(P.PIECE_CODE),COUNT(*) FROM  T_INFO_BASE_PIECE P WHERE P.PIECE_FLAG='2' AND P.PIECE_OPEN='1' ";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql1= sql1+" AND P.ORG_ID IN('"+orgId+"')";
		}
		else{
			sql1= sql1 = " AND 1!=1";
		}
		sql1 = sql1 + " AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql1);
		List list = query
		.list();
		return list;
	}
	
	/*
	 * 统计市级各地市的的呈批数量
	 * 
	 */
	public List getOrgReport1sj(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = "SELECT SUM(P.PIECE_CODE),COUNT(*) FROM  T_INFO_BASE_PIECE P WHERE P.PIECE_FLAG='2' AND P.PIECE_OPEN='1' ";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql1= sql1+" AND (P.ORG_ID = '"+orgId+"' OR P.PARENT_ORG_ID='"+orgId+"') ";
		}
		else{
			sql1= sql1 = " AND 1!=1";
		}
		sql1 = sql1 + " AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql1);
		List list = query
		.list();
		return list;
	}
	
	
	/*
	 * 统计市级各地市的的当前年的累计得分
	 * 
	 */
	public Integer getOrgReportsjsum(String syscode, String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = "SELECT SUM(P.PIECE_CODE) FROM  T_INFO_BASE_PIECE P WHERE 1=1 ";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql1= sql1+" AND (P.ORG_ID = '"+orgId+"' OR P.PARENT_ORG_ID='"+orgId+"') ";
		}
		else{
			sql1= sql1 = " AND 1!=1 ";
		}
		sql1 = sql1 + " AND  to_char(P.PIECE_TIME,'yyyy-mm')<='"+year+"-"+month+"' AND to_char(P.PIECE_TIME,'yyyy')='"+year+"'";
		
		Query query1 = rpDao.getSession().createSQLQuery(sql1);
		List list1 = query1
		.list();
		String s1 = "";
		String s2 = "";
		int m1= 0;
		int m2= 0;
		if(list1.get(0)!=null){
		  s1 = list1.get(0).toString();
		  m1 = Integer.valueOf(s1);
		}
		String sql2 = 
			"SELECT SUM(TAB1.PUB_CODE) FROM (SELECT P.PUB_CODE,P.ORG_ID FROM   T_INFO_BASE_PUBLISH P JOIN  T_INFO_BASE_ISSUE I ON P.ISS_ID=I.ISS_ID  " +
			" WHERE  P.PUB_USE_STATUS=1  AND to_char(I.ISS_TIME,'yyyy-mm')<='"+year+"-"+month+"' AND to_char(I.ISS_TIME,'yyyy')='"+year+"'  AND I.ISS_IS_PUBLISH=1 "+
			" )TAB1 ,  T_UUMS_BASE_ORG O " +
			" WHERE   TAB1.ORG_ID=O.ORG_ID AND O.ORG_SYSCODE LIKE '"+syscode+"%' AND O.ORG_NATURE!=5 ";
		
		Query query2 = rpDao.getSession().createSQLQuery(sql2);
		List list2 = query2
		.list();
		if(list2.get(0)!=null){
			s2 = list2.get(0).toString();
			m2 = Integer.valueOf(s2);
		}
		
		int sum = m1+m2;
		return sum;
	}
	
	/*
	 * 统计市级各地市的的当前年的当前月得分
	 * 
	 */
	public Integer getOrgReportdqsjsum(String syscode, String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = "SELECT SUM(P.PIECE_CODE) FROM  T_INFO_BASE_PIECE P WHERE 1=1";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql1= sql1+" AND (P.ORG_ID = '"+orgId+"' OR P.PARENT_ORG_ID='"+orgId+"') ";
		}
		else{
			sql1= sql1 = " AND 1!=1";
		}
		sql1 = sql1 + " AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		
		Query query1 = rpDao.getSession().createSQLQuery(sql1);
		List list1 = query1
		.list();
		String s1 = "";
		String s2 = "";
		int m1= 0;
		int m2= 0;
		if(list1.get(0)!=null){
		  s1 = list1.get(0).toString();
		  m1 = Integer.valueOf(s1);
		}
		String sql2 = 
			"SELECT SUM(TAB1.PUB_CODE) FROM (SELECT P.PUB_CODE,P.ORG_ID FROM   T_INFO_BASE_PUBLISH P JOIN  T_INFO_BASE_ISSUE I ON P.ISS_ID=I.ISS_ID  " +
			" WHERE  P.PUB_USE_STATUS=1  AND to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"'  AND I.ISS_IS_PUBLISH=1 "+
			" )TAB1 ,  T_UUMS_BASE_ORG O " +
			" WHERE   TAB1.ORG_ID=O.ORG_ID AND O.ORG_SYSCODE LIKE '"+syscode+"%' AND O.ORG_NATURE!=5 ";
		
		Query query2 = rpDao.getSession().createSQLQuery(sql2);
		List list2 = query2
		.list();
		if(list2.get(0)!=null){
			s2 = list2.get(0).toString();
			m2 = Integer.valueOf(s2);
		}
		
		int sum = m1+m2;
		return sum;
	}
	
	
	
	
	/*
	 * 统计各地市的的期刊采用数量
	 * 
	 */
	public List getOrgReport2(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT SUM(P.PUB_CODE),COUNT(*) FROM  T_INFO_BASE_ISSUE I , T_INFO_BASE_PUBLISH P " +
			"WHERE I.ISS_ID=P.ISS_ID AND P.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH=1 ";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql= sql+" AND P.ORG_ID IN('"+orgId+"')";
		}
		else{
			sql= sql = " AND 1!=1";
		}
		sql = sql + " AND  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
	}
	
	/*
	 * 统计各地市的的期刊当前月的分数
	 * 
	 */
	public Integer getOrgReportdqsum(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = 
			"SELECT SUM(P.PUB_CODE) FROM  T_INFO_BASE_ISSUE I , T_INFO_BASE_PUBLISH P " +
			"WHERE I.ISS_ID=P.ISS_ID AND P.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH=1 ";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql1= sql1+" AND P.ORG_ID IN('"+orgId+"')";
		}
		else{
			sql1= sql1 = " AND 1!=1";
		}
		sql1 = sql1 + " AND  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql1);
		List list1 = query
		.list();
		String s1 = "";
		String s2 = "";
		int m1= 0;
		int m2= 0;
		if(list1.get(0)!=null){
		  s1 = list1.get(0).toString();
		  m1 = Integer.valueOf(s1);
		}
		String sql2 = "SELECT SUM(P.PIECE_CODE) FROM  T_INFO_BASE_PIECE P WHERE 1=1 ";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql2= sql2+" AND P.ORG_ID IN('"+orgId+"')";
		}
		else{
			sql2= sql2 = " AND 1!=1";
		}
		sql2 = sql2 + " AND to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query2 = rpDao.getSession().createSQLQuery(sql2);
		List list2 = query2
		.list();
		if(list2.get(0)!=null){
			s2 = list2.get(0).toString();
			m2 = Integer.valueOf(s2);
		}
		
		int sum = m1+m2;
		return sum;
	}
	
	
	/*
	 * 统计各地市的的期刊累计月的分数
	 * 
	 */
	public Integer getOrgReportsum(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = 
			"SELECT SUM(P.PUB_CODE) FROM  T_INFO_BASE_ISSUE I , T_INFO_BASE_PUBLISH P " +
			"WHERE I.ISS_ID=P.ISS_ID AND P.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH=1 ";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql1= sql1+" AND P.ORG_ID IN('"+orgId+"')";
		}
		else{
			sql1= sql1 = " AND 1!=1";
		}
		sql1 = sql1 + " AND  to_char(I.ISS_TIME,'yyyy-mm')<='"+year+"-"+month+"' AND to_char(I.ISS_TIME,'yyyy')='"+year+"'";
		Query query = rpDao.getSession().createSQLQuery(sql1);
		List list1 = query
		.list();
		String s1 = "";
		String s2 = "";
		int m1= 0;
		int m2= 0;
		if(list1.get(0)!=null){
		  s1 = list1.get(0).toString();
		  m1 = Integer.valueOf(s1);
		}
		String sql2 = "SELECT SUM(P.PIECE_CODE) FROM  T_INFO_BASE_PIECE P WHERE 1=1 ";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql2= sql2+" AND P.ORG_ID IN('"+orgId+"')";
		}
		else{
			sql2= sql2 = " AND 1!=1";
		}
		sql2 = sql2 + " AND to_char(P.PIECE_TIME,'yyyy-mm')<='"+year+"-"+month+"'  AND to_char(P.PIECE_TIME,'yyyy')='"+year+"'";
		Query query2 = rpDao.getSession().createSQLQuery(sql2);
		List list2 = query2
		.list();
		if(list2.get(0)!=null){
			s2 = list2.get(0).toString();
			m2 = Integer.valueOf(s2);
		}
		
		int sum = m1+m2;
		return sum;
	}
	
	/*
	 * 统计设区市的的期刊采用数量
	 * 
	 */
	public List getOrgReport10(String syscode,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT SUM(TAB1.PUB_CODE),COUNT(*) FROM (SELECT P.PUB_CODE,P.ORG_ID FROM   T_INFO_BASE_PUBLISH P JOIN  T_INFO_BASE_ISSUE I ON P.ISS_ID=I.ISS_ID  " +
			" WHERE  P.PUB_USE_STATUS=1  AND  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"' AND I.ISS_IS_PUBLISH=1 "+
			" )TAB1 ,  T_UUMS_BASE_ORG O " +
			" WHERE   TAB1.ORG_ID=O.ORG_ID AND O.ORG_SYSCODE LIKE '"+syscode+"%' AND O.ORG_NATURE!=5 ";
			
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
	}
	
	
	/*
	 * 根据机构ID查出已成刊的文章去除出差出访栏目
	 * 
	 */
	public List getOrgReport3(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		
		String sql = 
			"SELECT TAB1.PUB_TITLE,TAB1.PUB_IS_INSTRUCTION ,J.JOUR_NAME FROM (" +
			"SELECT P.PUB_TITLE ,P.PUB_IS_INSTRUCTION , I.JOUR_ID FROM  T_INFO_BASE_PUBLISH P " +
			"JOIN  T_INFO_BASE_ISSUE I ON P.ISS_ID=I.ISS_ID" +
			" WHERE P.ORG_ID IN ('"+orgId+"') AND P.PUB_USE_STATUS=1 AND P.COL_ID!='402882bb38b1c65c0138b1e8884c0001' " +
			" AND   to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"' AND I.ISS_IS_PUBLISH=1";
		/*String sql = 
			"SELECT P.PUB_TITLE ,P.PUB_IS_INSTRUCTION FROM  T_INFO_BASE_PUBLISH P " +
			"JOIN  T_INFO_BASE_ISSUE I ON P.ISS_ID=I.ISS_ID" +
			" WHERE P.ORG_ID IN ('"+orgId+"') AND P.PUB_USE_STATUS=1 AND P.COL_ID!='402882bb38b1c65c0138b1e8884c0001' " +
			" AND   to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"' AND I.ISS_IS_PUBLISH=1";*/
		
		if((orgId!=null)&&(!"".equals(orgId))){
			sql= sql+" AND P.ORG_ID IN('"+orgId+"')";
		}
		else{
			sql= sql = " AND 1!=1";
		}
		sql = sql +" )TAB1 ,T_INFO_BASE_JOURNAL J WHERE TAB1.JOUR_ID=J.JOUR_ID";
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
	}
	
	/*
	 * 根据机构ID查出已成刊的文章只查询出差出访栏目信息数
	 * 
	 */
	public List getOrgReportTrip(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT  COUNT(*) FROM  T_INFO_BASE_PUBLISH P " +
			"JOIN  T_INFO_BASE_ISSUE I ON P.ISS_ID=I.ISS_ID" +
			" WHERE P.ORG_ID IN ('"+orgId+"') AND P.PUB_USE_STATUS=1 AND P.COL_ID='402882bb38b1c65c0138b1e8884c0001' " +
			" AND  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"' AND I.ISS_IS_PUBLISH=1";
		
		if((orgId!=null)&&(!"".equals(orgId))){
			sql= sql+" AND P.ORG_ID IN('"+orgId+"')";
		}
		else{
			sql= sql = " AND 1!=1";
		}
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
	}
	
	
	/*
	 * 根据机构ID查出市级单位已成刊的文章去除出差出访栏目
	 * 
	 */
	public List getOrgReport11(String syscode,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"  SELECT TAB1.PUB_TITLE,TAB1.PUB_IS_INSTRUCTION,O.ORG_NATURE,O.ORG_NAME FROM  " +
			" (SELECT P.ORG_ID,P.PUB_IS_INSTRUCTION,P.PUB_TITLE FROM  T_INFO_BASE_PUBLISH P, T_INFO_BASE_ISSUE I WHERE P.ISS_ID=I.ISS_ID AND  P.PUB_USE_STATUS=1 " +
			"  AND to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"'  AND I.ISS_IS_PUBLISH=1 AND P.COL_ID!='402882bb38b1c65c0138b1e8884c0001')TAB1 JOIN  T_UUMS_BASE_ORG O ON TAB1.ORG_ID=O.ORG_ID  " +
			"  WHERE O.ORG_SYSCODE LIKE '"+syscode+"%' AND O.ORG_NATURE!=5";
		
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
	}
	
	
	/*
	 * 根据机构ID查出市级单位已成刊的文章只查询出差出访栏目信息数
	 * 
	 */
	public List getOrgReportSJTrip (String syscode,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"  SELECT COUNT(*) FROM  " +
			" (SELECT P.ORG_ID,P.PUB_IS_INSTRUCTION,P.PUB_TITLE FROM  T_INFO_BASE_PUBLISH P, T_INFO_BASE_ISSUE I WHERE P.ISS_ID=I.ISS_ID AND  P.PUB_USE_STATUS=1 " +
			"  AND  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"' AND I.ISS_IS_PUBLISH=1  AND P.COL_ID='402882bb38b1c65c0138b1e8884c0001')TAB1 JOIN  T_UUMS_BASE_ORG O ON TAB1.ORG_ID=O.ORG_ID  " +
			"  WHERE O.ORG_SYSCODE LIKE '"+syscode+"%' AND O.ORG_NATURE!=5";
		
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
	}
	
	/*
	 * 根据机构ID查出呈阅件的文章
	 * 
	 */
	public List getOrgReport4(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT P.PIECE_TITLE,P.PIECE_OPEN,P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
			" WHERE P.ORG_ID IN ('"+orgId+"') AND P.PIECE_FLAG='2' " +
			"AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
		
	}
	
	/*
	 * 根据机构ID查出市级呈阅件的文章
	 * 
	 */
	public List getOrgReport4sj(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT P.PIECE_TITLE,P.PIECE_OPEN,P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
			" WHERE (P.ORG_ID = '"+orgId+"' OR P.PARENT_ORG_ID ='"+orgId+"') AND P.PIECE_FLAG='2' " +
			"AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
		
	}
	
	
	/*
	 * 根据机构ID查出市级呈国办的文章
	 * 
	 */
	public List getOrgReport5sj(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT P.PIECE_TITLE,P.PIECE_OPEN ,P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
			" WHERE (P.ORG_ID = '"+orgId+"' OR P.PARENT_ORG_ID ='"+orgId+"') AND P.PIECE_FLAG='1' " +
			"AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
		
	}
	
	/*
	 * 根据机构ID查出上报国办的文章
	 * 
	 */
	public List getOrgReport4s(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT P.PIECE_TITLE,P.PIECE_OPEN,P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
			" WHERE P.ORG_ID IN ('"+orgId+"') AND P.PIECE_FLAG='1' " +
			"AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
		
	}
	
	/*
	 * 根据机构ID查出省办的文章
	 * 
	 */
	public List getOrgReportSB(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT P.PIECE_TITLE,P.PIECE_OPEN,P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
			" WHERE P.ORG_ID IN ('"+orgId+"') AND P.PIECE_FLAG='3' " +
			"AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
		
	}
	
	
	/*
	 * 根据机构ID查出本月加分的信息
	 * 
	 */
	public List getOrgReportJF(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT P.PIECE_TITLE,P.PIECE_OPEN,P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
			" WHERE P.ORG_ID IN ('"+orgId+"') AND P.PIECE_FLAG='4' " +
			"AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
		
	}
	
	/*
	 * 统计各地市的的呈阅数量
	 * 
	 */
	public List getOrgReport5(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = "SELECT SUM(P.PIECE_CODE),COUNT(*) FROM  T_INFO_BASE_PIECE P WHERE P.PIECE_FLAG='2' AND P.PIECE_OPEN='0'";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql1= sql1+" AND P.ORG_ID IN('"+orgId+"')";
		}
		else{
			sql1= sql1 = " AND 1!=1";
		}
		sql1 = sql1 + " AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql1);
		List list = query
		.list();
		return list;
	}
	
	/*
	 * 统计市级各地市的的呈阅数量
	 * 
	 */
	public List getOrgReport5dsj(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = "SELECT SUM(P.PIECE_CODE),COUNT(*) FROM  T_INFO_BASE_PIECE P WHERE P.PIECE_FLAG='2' AND P.PIECE_OPEN='0'";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql1= sql1+" AND (P.ORG_ID = '"+orgId+"' OR P.PARENT_ORG_ID='"+orgId+"') ";
		}
		else{
			sql1= sql1 = " AND 1!=1";
		}
		sql1 = sql1 + " AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql1);
		List list = query
		.list();
		return list;
	}
	
	/*
	 * 统计市级各地市的的批转数量
	 * 
	 */
	public List getOrgReport6sj(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = "SELECT SUM(P.PIECE_CODE),COUNT(*) FROM  T_INFO_BASE_PIECE P WHERE P.PIECE_FLAG='2' AND P.PIECE_OPEN='2'";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql1= sql1+" AND (P.ORG_ID = '"+orgId+"' OR P.PARENT_ORG_ID='"+orgId+"')";
		}
		else{
			sql1= sql1 = " AND 1!=1";
		}
		sql1 = sql1 + " AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql1);
		List list = query
		.list();
		return list;
	}
	
	/*
	 * 统计各地市的的批转数量
	 * 
	 */
	public List getOrgReport6(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = "SELECT SUM(P.PIECE_CODE),COUNT(*) FROM  T_INFO_BASE_PIECE P WHERE P.PIECE_FLAG='2' AND P.PIECE_OPEN='2'";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql1= sql1+" AND P.ORG_ID IN('"+orgId+"')";
		}
		else{
			sql1= sql1 = " AND 1!=1";
		}
		sql1 = sql1 + " AND to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql1);
		List list = query
		.list();
		return list;
	}
	
	
	/*
	 * 根据机构ID查出呈国办的文章得分
	 * 
	 */
	public List getOrgReport7(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT NVL(SUM(P.PIECE_CODE),0) FROM  T_INFO_BASE_PIECE P " +
			" WHERE P.ORG_ID IN ('"+orgId+"') AND P.PIECE_FLAG='1' " +
			" AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
		
	}
	
	/*
	 * 根据机构ID查出呈阅件的文章得分
	 * 
	 */
	public List getOrgReport8(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT NVL(SUM(P.PIECE_CODE),0) FROM  T_INFO_BASE_PIECE P " +
			" WHERE P.ORG_ID IN ('"+orgId+"') AND P.PIECE_FLAG='2' " +
			" AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
		
	}
	

	/*
	 * 根据机构ID查出市级呈国办的文章得分
	 * 
	 */
	public List getOrgReport7s(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT NVL(SUM(P.PIECE_CODE),0) FROM  T_INFO_BASE_PIECE P " +
			" WHERE (P.ORG_ID = '"+orgId+"' OR P.PARENT_ORG_ID='"+orgId+"') AND P.PIECE_FLAG='1' " +
			" AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
		
	}
	
	/*
	 * 根据机构ID查出市级呈阅件的文章得分
	 * 
	 */
	public List getOrgReport8s(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql = 
			"SELECT NVL(SUM(P.PIECE_CODE),0) FROM  T_INFO_BASE_PIECE P " +
			" WHERE (P.ORG_ID = '"+orgId+"' OR P.PARENT_ORG_ID='"+orgId+"') AND P.PIECE_FLAG='2' " +
			" AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql);
		List list = query
		.list();
		return list;
		
	}
	
	/*
	 * 统计各地市的的上报国办数量
	 * 
	 */
	public List getOrgReport9(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = "SELECT SUM(P.PIECE_CODE),COUNT(*) FROM  T_INFO_BASE_PIECE P WHERE P.PIECE_FLAG='1' ";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql1= sql1+" AND P.ORG_ID IN('"+orgId+"')";
		}
		else{
			sql1= sql1 = " AND 1!=1";
		}
		sql1 = sql1 + " AND   to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql1);
		List list = query
		.list();
		return list;
	}
	/*
	 * 统计各地市的的市级上报国办数量
	 * 
	 */
	public List getOrgReport9sj(String orgId,String year,String month)
	throws ServiceException,SystemException, DAOException{
		String sql1 = "SELECT SUM(P.PIECE_CODE),COUNT(*) FROM  T_INFO_BASE_PIECE P WHERE P.PIECE_FLAG='1' ";
		if((orgId!=null)&&(!"".equals(orgId))){
			sql1= sql1+" AND (P.ORG_ID = '"+orgId+"' OR P.PARENT_ORG_ID='"+orgId+"')";
		}
		else{
			sql1= sql1 = " AND 1!=1";
		}
		sql1 = sql1 + " AND  to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"'";
		Query query = rpDao.getSession().createSQLQuery(sql1);
		List list = query
		.list();
		return list;
	}

}
