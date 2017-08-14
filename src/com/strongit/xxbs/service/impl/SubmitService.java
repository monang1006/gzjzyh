package com.strongit.xxbs.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.utils.DateTimes;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.xxbs.common.StatisticsComparator;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.PointDto;
import com.strongit.xxbs.dto.StatisticsDto;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.entity.TInfoQueryTree;
import com.strongit.xxbs.service.ISubmitService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

import edu.emory.mathcs.backport.java.util.Collections;

@Service
@Transactional(readOnly = true)
public class SubmitService implements ISubmitService
{
	private GenericDAOHibernate<TInfoBasePublish, String> submitDao;
	private GenericDAOHibernate<TInfoQueryTree, String> treeDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		submitDao = new GenericDAOHibernate<TInfoBasePublish, String>(
				sessionFactory, TInfoBasePublish.class);
		treeDao = new GenericDAOHibernate<TInfoQueryTree, String>(
				sessionFactory, TInfoQueryTree.class);
	}

	public List<TInfoQueryTree> getQueryTree()
			throws ServiceException, SystemException, DAOException
	{
		return treeDao.findAll();
	}

	public Page<TInfoBasePublish> findSubmittedByQs(Page<TInfoBasePublish> page, String qs)
			throws ServiceException, SystemException, DAOException
	{
		Criterion[] criterion = new Criterion[3];
		if("orgT".equals(qs))
			criterion[0] = Restrictions.eq("pubOrgType", Publish.ORG_T);
		else if("orgJ".equals(qs))
			criterion[0] = Restrictions.eq("pubOrgType", Publish.ORG_J);
		else if("orgZ".equals(qs))
			criterion[0] = Restrictions.eq("pubOrgType", Publish.ORG_Z);
		else if("orgW".equals(qs))
			criterion[0] = Restrictions.eq("pubOrgType", Publish.ORG_W);
		else if("infoP".equals(qs))
			criterion[0] = Restrictions.eq("pubInfoType", Publish.INFO_GENERAL);
		else if("infoM".equals(qs))
			criterion[0] = Restrictions.eq("pubInfoType", Publish.INFO_SECRET);
		else if("today".equals(qs))
		{
			Calendar now = Calendar.getInstance();
			Date date = new Date();
			now.setTime(date);
			Date sDate = DateTimes.date2DateTime(date);
			now.add(Calendar.DATE, 1);
			Date eDate = DateTimes.date2DateTime(now.getTime());

			criterion[0] = Restrictions.between("pubDate", sDate, eDate);
		}
		else if("yesterday".equals(qs))
		{
			Calendar now = Calendar.getInstance();
			Date date = new Date();
			Date eDate = DateTimes.date2DateTime(date);
			now.setTime(date);
			now.add(Calendar.DATE, -1);
			Date sDate = DateTimes.date2DateTime(now.getTime());
			criterion[0] = Restrictions.between("pubDate", sDate, eDate);
		}
		else if("week".equals(qs))
		{
			Calendar now = Calendar.getInstance();
			Date date = new Date();
			//星期一
			now.setTime(date);
			now.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			Date sDate = now.getTime();
			//星期日
			now.setTime(date);
			now.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			now.add(Calendar.WEEK_OF_MONTH, 1);
			now.set(Calendar.HOUR_OF_DAY, 23);
			now.set(Calendar.MINUTE, 59);
			now.set(Calendar.SECOND, 59);
			Date eDate = now.getTime();
			System.out.println(sDate.toString()+eDate.toString());
			criterion[0] = Restrictions.between("pubDate", sDate, eDate);
		}
		else if("month".equals(qs))
		{
			Calendar now = Calendar.getInstance();
			Date date = new Date();
			//本月1日
			now.setTime(date);
			now.set(Calendar.DATE, 1);
			Date sDate = now.getTime();
			//本月末日
			now.setTime(date);
			now.add(Calendar.MONTH, 1);
			now.set(Calendar.DATE, 1);
			now.add(Calendar.DATE, -1);
			Date eDate = now.getTime();
			criterion[0] = Restrictions.between("pubDate", sDate, eDate);
		}
		else if("year".equals(qs))
		{
			Calendar now = Calendar.getInstance();
			Date date = new Date();
			//本年1月1日
			now.setTime(date);
			now.set(Calendar.MONTH, 1);
			now.set(Calendar.DATE, 1);
			Date sDate = now.getTime();
			//本年12月末日
			now.setTime(date);
			now.set(Calendar.MONTH, 12);
			now.add(Calendar.MONTH, 1);
			now.set(Calendar.DATE, 1);
			now.add(Calendar.DATE, -1);
			Date eDate = now.getTime();
			criterion[0] = Restrictions.between("pubDate", sDate, eDate);
		}
		else if("readU".equals(qs))
			criterion[0] = Restrictions.eq("pubIsRead", Publish.NO_READ);
		else if("readR".equals(qs))
			criterion[0] = Restrictions.eq("pubIsRead", Publish.READ);
		else if("useU".equals(qs))
			criterion[0] = Restrictions.eq("pubUseStatus", Publish.NO_USED);
		else if("useR".equals(qs))
			criterion[0] = Restrictions.eq("pubUseStatus", Publish.USED);
		else if("shareU".equals(qs))
			criterion[0] = Restrictions.eq("pubIsShare", Publish.NO_SHARED);
		else if("shareR".equals(qs))
			criterion[0] = Restrictions.eq("pubIsShare", Publish.SHARED);
		else if("appointU".equals(qs))
			criterion[0] = Restrictions.isNull("TInfoBaseAppoint.aptId");
		else if("appointR".equals(qs))
			criterion[0] = Restrictions.isNotNull("TInfoBaseAppoint.aptId");
		else
		{
			criterion[0] = Restrictions.eq("pubSubmittatus", Publish.SUBMITTED);
		}
		
		criterion[1] = Restrictions.eq("pubSubmitStatus", Publish.SUBMITTED);
		Criterion cr1 = Restrictions.ne("pubIsMerge", "0");
		Criterion cr2 = Restrictions.isNull("pubIsMerge");
		criterion[3] = Restrictions.or(cr1, cr2);
		return submitDao.findByCriteria(page, criterion);
	}	
	
	public TreeSet<StatisticsDto> getTotalStatistics() throws ServiceException,
	SystemException, DAOException
	{
		TreeSet<StatisticsDto> stats = 
				new TreeSet<StatisticsDto>(new StatisticsComparator());
		StringBuilder sTotal = new StringBuilder();
		sTotal.append("select orgId,count(*) as total ");
		sTotal.append("from TInfoBasePublish ");
		sTotal.append("where pubUseStatus = '1' ");
		sTotal.append("group by orgId ");
		List<?> totalLists = submitDao.find(sTotal.toString(), new Object[0]);
		for(Object one : totalLists)
		{
			StatisticsDto stat = new StatisticsDto();
			Object[] row = (Object[]) one;
			String orgId = (String) row[0];
			long total = (Long) row[1];
			stat.setOrgId(orgId);
			stat.setTotal(total);
			stats.add(stat);
		}
		return stats;
	}
	
	public Map<String, StatisticsDto> getStatistics() throws ServiceException,
			SystemException, DAOException
	{
		
		Map<String, StatisticsDto> mapStats =
				new HashMap<String, StatisticsDto>();
		
		StringBuilder sTotal = new StringBuilder();
		sTotal.append("select orgId,count(*) as total ");
		sTotal.append("from TInfoBasePublish ");
		sTotal.append("where pubUseStatus = '1' ");
		sTotal.append("group by orgId ");
		List<?> totalLists = submitDao.find(sTotal.toString(), new Object[0]);
		for(Object one : totalLists)
		{
			StatisticsDto stat = new StatisticsDto();
			Object[] row = (Object[]) one;
			String key = (String) row[0];
			long total = (Long) row[1];
			stat.setTotal(total);
			mapStats.put(key, stat);
		}
		
		Calendar now = Calendar.getInstance();
		Date date = new Date();
		
		//本年1月1日
		now.setTime(date);
		now.set(Calendar.MONTH, 1);
		now.set(Calendar.DATE, 1);
		Date sDate = now.getTime();
		//本年12月末日
		now.setTime(date);
		now.set(Calendar.MONTH, 12);
		now.add(Calendar.MONTH, 1);
		now.set(Calendar.DATE, 1);
		now.add(Calendar.DATE, -1);
		Date eDate = now.getTime();
		
		StringBuilder sYear = new StringBuilder();
		sYear.append("select orgId,count(*) as total ");
		sYear.append("from TInfoBasePublish ");
		sYear.append("where pubUseStatus='1' and pubDate <= :eDate and pubDate >= :sDate ");
		sYear.append("group by orgId");
		
		Query query = submitDao.getSession().createQuery(sYear.toString());
		List<?> yearLists = 
				query.setDate("sDate", sDate)
				.setDate("eDate", eDate)
				.list();
		
		//List<?> yearLists = submitDao.find(sYear.toString(), new Object[0]);
		for(Object one : yearLists)
		{
			Object[] row = (Object[]) one;
			String key = (String) row[0];
			long total = (Long) row[1];
			StatisticsDto stat = mapStats.get(key);
			stat.setByYear(total);
			mapStats.put(key, stat);
		}
		
		now = Calendar.getInstance();
		date = new Date();
		//本月1日
		now.setTime(date);
		now.set(Calendar.DATE, 1);
		Date smDate = now.getTime();
		//本月末日
		now.setTime(date);
		now.add(Calendar.MONTH, 1);
		now.set(Calendar.DATE, 1);
		now.add(Calendar.DATE, -1);
		Date emDate = now.getTime();
		StringBuilder sMonth = new StringBuilder();
		sMonth.append("select orgId,count(*) as total ");
		sMonth.append("from TInfoBasePublish ");
		sMonth.append("where pubUseStatus='1' and pubDate <= :emDate and pubDate >= :smDate ");
		sMonth.append("group by orgId");
		
		Query query2 = submitDao.getSession().createQuery(sMonth.toString());
		List<?> monthLists = 
				query2.setDate("smDate", smDate)
				.setDate("emDate", emDate)
				.list();
		for(Object one : monthLists)
		{
			Object[] row = (Object[]) one;
			String key = (String) row[0];
			long total = (Long) row[1];
			StatisticsDto stat = mapStats.get(key);
			stat.setByMonth(total);
			mapStats.put(key, stat);
		}

		StringBuilder sDay = new StringBuilder();
		sDay.append("select orgId,count(*) as total ");
		sDay.append("from TInfoBasePublish ");
		sDay.append("where pubUseStatus='1' and pubDate <= :enDate and pubDate >= :snDate ");
		sDay.append("group by orgId");
		
		date = new Date();
		Date snDate = DateTimes.date2DateTime(date);
		now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DATE, 1);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		Date enDate =now.getTime();
		Query query3 = submitDao.getSession().createQuery(sDay.toString());
		List<?> dayLists = 
				query3.setDate("snDate", snDate)
				.setDate("enDate", enDate)
				.list();
		for(Object one : dayLists)
		{
			Object[] row = (Object[]) one;
			String key = (String) row[0];
			long total = (Long) row[1];
			StatisticsDto stat = mapStats.get(key);
			stat.setByDay(total);
			mapStats.put(key, stat);
		}

		return mapStats;
	}
	public List<?> getStatistics1(String org[],String useStatus,int curpage,int unitpage) throws ServiceException,
	SystemException, DAOException
		{
		
			Calendar now = Calendar.getInstance();
			Date date = new Date();
			
			//本年1月1日
			now.setTime(date);
			now.set(Calendar.MONTH, 1);
			now.set(Calendar.DATE, 1);
			Date sDate = now.getTime();
			//本年12月末日
			now.setTime(date);
			now.set(Calendar.MONTH, 12);
			now.add(Calendar.MONTH, 1);
			now.set(Calendar.DATE, 1);
			now.add(Calendar.DATE, -1);
			Date eDate = now.getTime();
		
		
			
			now = Calendar.getInstance();
			date = new Date();
			//本月1日
			now.setTime(date);
			now.set(Calendar.DATE, 1);
			Date smDate = now.getTime();
			//本月末日
			now.setTime(date);
			now.add(Calendar.MONTH, 1);
			now.set(Calendar.DATE, 1);
			now.add(Calendar.DATE, -1);
			Date emDate = now.getTime();
			
			now = Calendar.getInstance();
			Date date1 = new Date();
			now.setTime(date);
			Date stDate = DateTimes.date2DateTime(date1);
			now.add(Calendar.DATE, 1);
			Date etDate = DateTimes.date2DateTime(now.getTime());
			/*String ids="";
			for(int i=0;i<org.length;i++){
				ids = ids +"," +"'"+org[i]+"'";
			}
			ids=ids.substring(1);*/
			/*String hql = "select tab5.org_name,tab5.num1,tab5.num2,tab5.num3,tab5.num4 from T_UUMS_BASE_ORG o left join"+
						  " (select tab11.org_id,tab12.num2,tab14.num4,tab13.num3,tab11.num1,tab11.org_name from"+
						  " (select o.org_id,tab1. num1,o.org_name from T_UUMS_BASE_ORG o left join (select t.org_id,count(*)num1  from t_info_base_publish t where t.org_id in ("+ids+") group by  t.org_id) tab1  on tab1.org_id=o.org_id)tab11,"+
						  " (select o.org_id,tab2. num2,o.org_name from T_UUMS_BASE_ORG o left join (select t.org_id,count(*)num2  from t_info_base_publish t where t.org_id in ("+ids+") and t.pub_submit_date>=:stDate and t.pub_submit_date<=:etDate group by  t.org_id) tab2  on tab2.org_id=o.org_id)tab12,"+
						  " (select o.org_id,tab3. num3,o.org_name from T_UUMS_BASE_ORG o left join (select t.org_id,count(*)num3  from t_info_base_publish t where t.org_id in ("+ids+") and t.pub_submit_date>=:sDate and t.pub_submit_date<=:eDate group by  t.org_id) tab3  on tab3.org_id=o.org_id)tab13,"+
						  " (select o.org_id,tab4. num4,o.org_name from T_UUMS_BASE_ORG o left join (select t.org_id,count(*)num4  from t_info_base_publish t where t.org_id in ("+ids+") and t.pub_submit_date>=:smDate and t.pub_submit_date<=:emDate group by  t.org_id) tab4  on tab4.org_id=o.org_id)tab14"+
						  " where tab11.org_id=tab12.org_id and  tab12.org_id =tab13.org_id and tab13.org_id=tab14.org_id )tab5"+
						  " on o.org_id = tab5.org_id ";
			String sqlwhere1 = " order by tab5.num1 desc nulls last";//全部信息;
			String sqlwhere2 = " order by tab5.num2 desc nulls last";//当日
			String sqlwhere3 = " order by tab5.num3 desc nulls last";//本年
			String sqlwhere4 = " order by tab5.num4 desc nulls last";//本月
			if("1".equals(useStatus)){
				hql = hql+sqlwhere1;
			}
			if("2".equals(useStatus)){
				hql = hql+sqlwhere2;
			}
			if("3".equals(useStatus)){
				hql = hql+sqlwhere3;
			}
			if("4".equals(useStatus)){
				hql = hql+sqlwhere4;
			}*/
			int pno = (curpage-1)*15;
			List list = new ArrayList();
			if(useStatus.equals("4")){
			String sql1 = "select t.org_id ,count(*) as num1 from t_info_base_publish t " +
						" where  t.pub_submit_date>=:stDate " +
						" and t.pub_submit_date<=:etDate" +
						" group by t.org_id";//当日
			String sql2 ="select t.org_id ,count(*) as num1 from t_info_base_publish t " +
						" where  t.pub_submit_date>=:sDate " +
						" and t.pub_submit_date<=:eDate" +
						" group by t.org_id";//当年
			
			String sql3 ="select t.org_id ,count(*) as num1 from t_info_base_publish t " +
			" where  t.pub_submit_date is not null"+
			" group by t.org_id";//总数
			
			Query query1 = submitDao.getSession().createSQLQuery(sql1);
			List dayLists1 = 
				query1.setDate("stDate", stDate)
				.setDate("etDate", etDate)
				.list();
			Map day = new HashMap();
			for(int i=0;i<dayLists1.size();i++){
				Object[] obj1 = (Object[])dayLists1.get(i);
				day.put(obj1[0], obj1[1]);
			}
			
			Query query2 = submitDao.getSession().createSQLQuery(sql2);
			List dayLists2 = 
				query2.setDate("sDate", sDate)
				.setDate("eDate", eDate)
				.list();
			Map year = new HashMap();
			for(int i=0;i<dayLists2.size();i++){
				Object[] obj2 = (Object[])dayLists2.get(i);
				year.put(obj2[0], obj2[1]);
			}
			
			Query query3 = submitDao.getSession().createSQLQuery(sql3);
			List dayLists3 = query3.list();
			Map count = new HashMap();
			for(int i=0;i<dayLists3.size();i++){
				Object[] obj3 = (Object[])dayLists3.get(i);
				count.put(obj3[0], obj3[1]);
			}
			String aid = "";
			for(int i=0;i<org.length;i++){
				aid = aid +"'"+org[i]+"',";
			}
			aid = aid.substring(0, aid.length()-1);
			
			String sql =  "select o.org_id,o.org_name,NVL(tab1.num1,0) from t_uums_base_org o"+ 
				" left join (select t.org_id ,count(*) as num1 "+
				" from t_info_base_publish t "+
				" where  t.pub_submit_date>=:smDate " +
				" and t.pub_submit_date<=:emDate " +
				" group by t.org_id)tab1 on "+
			    "   o.org_id=tab1.org_id where o.org_id in("+aid+") order by NVL(tab1.num1,0) desc ";
			
			
			
				Query query = submitDao.getSession().createSQLQuery(sql);
				List dayLists =new ArrayList();
			try {
				 dayLists = 
					query.setDate("smDate", smDate)
					.setDate("emDate", emDate).setFirstResult(pno).setMaxResults(unitpage)
					.list();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			for(int i=0;i<dayLists.size();i++){
				Object[] obj4 = (Object[])dayLists.get(i);
				list.add(new Object[]{obj4[1],count.get(obj4[0]),day.get(obj4[0]),year.get(obj4[0]),obj4[2]});
			}
			}
			
			
			
			
			if(useStatus.equals("2")){
				String sql1 = "select t.org_id ,count(*) as num1 from t_info_base_publish t " +
							  " where  t.pub_submit_date>=:smDate " +
							  " and t.pub_submit_date<=:emDate" +
							  " group by t.org_id";//当月
				String sql2 ="select t.org_id ,count(*) as num1 from t_info_base_publish t " +
							" where  t.pub_submit_date>=:sDate " +
							" and t.pub_submit_date<=:eDate" +
							" group by t.org_id";//当年
	
					String sql3 ="select t.org_id ,count(*) as num1 from t_info_base_publish t " +
					" where  t.pub_submit_date is not null"+
					" group by t.org_id";//总数
					
					Query query1 = submitDao.getSession().createSQLQuery(sql1);
					List dayLists1 = 
						query1.setDate("smDate", smDate)
						.setDate("emDate", emDate)
						.list();
					Map day = new HashMap();
					for(int i=0;i<dayLists1.size();i++){
						Object[] obj1 = (Object[])dayLists1.get(i);
						day.put(obj1[0], obj1[1]);
					}
					
					Query query2 = submitDao.getSession().createSQLQuery(sql2);
					List dayLists2 = 
						query2.setDate("sDate", sDate)
						.setDate("eDate", eDate)
						.list();
					Map year = new HashMap();
					for(int i=0;i<dayLists2.size();i++){
						Object[] obj2 = (Object[])dayLists2.get(i);
						year.put(obj2[0], obj2[1]);
					}
					
					Query query3 = submitDao.getSession().createSQLQuery(sql3);
					List dayLists3 = query3.list();
					Map count = new HashMap();
					for(int i=0;i<dayLists3.size();i++){
						Object[] obj3 = (Object[])dayLists3.get(i);
						count.put(obj3[0], obj3[1]);
					}
					
					
					String aid = "";
					for(int i=0;i<org.length;i++){
						aid = aid +"'"+org[i]+"',";
					}
					aid = aid.substring(0, aid.length()-1);
					
					String sql =  "select o.org_id,o.org_name,NVL(tab1.num1,0) from t_uums_base_org o"+ 
						" left join (select t.org_id ,count(*) as num1 "+
						" from t_info_base_publish t "+
						" where  t.pub_submit_date>=:stDate " +
						" and t.pub_submit_date<=:etDate " +
						" group by t.org_id)tab1 on "+
					    "   o.org_id=tab1.org_id where o.org_id in("+aid+") order by NVL(tab1.num1,0) desc ";
					
					
					
						Query query = submitDao.getSession().createSQLQuery(sql);
					
					
					List dayLists = 
						query.setDate("stDate", stDate)
						.setDate("etDate", etDate).setFirstResult(pno).setMaxResults(unitpage)
						.list();
					for(int i=0;i<dayLists.size();i++){
						Object[] obj4 = (Object[])dayLists.get(i);
						list.add(new Object[]{obj4[1],count.get(obj4[0]),obj4[2],year.get(obj4[0]),day.get(obj4[0])});
					}
			}
			
			
			
			
			
			if(useStatus.equals("3")){
				String sql1 = "select t.org_id ,count(*) as num1 from t_info_base_publish t " +
							  " where  t.pub_submit_date>=:stDate " +
							  " and t.pub_submit_date<=:etDate" +
							  " group by t.org_id";//当日
				String sql2 ="select t.org_id ,count(*) as num1 from t_info_base_publish t " +
							" where  t.pub_submit_date>=:smDate " +
							" and t.pub_submit_date<=:emDate" +
							" group by t.org_id";//当月
	
					String sql3 ="select t.org_id ,count(*) as num1 from t_info_base_publish t " +
					" where  t.pub_submit_date is not null"+
					" group by t.org_id";//总数
					
					Query query1 = submitDao.getSession().createSQLQuery(sql1);
					List dayLists1 = 
						query1.setDate("stDate", stDate)
						.setDate("etDate", etDate)
						.list();
					Map day = new HashMap();
					for(int i=0;i<dayLists1.size();i++){
						Object[] obj1 = (Object[])dayLists1.get(i);
						day.put(obj1[0], obj1[1]);
					}
					
					Query query2 = submitDao.getSession().createSQLQuery(sql2);
					List dayLists2 = 
						query2.setDate("smDate", smDate)
						.setDate("emDate", emDate)
						.list();
					Map year = new HashMap();
					for(int i=0;i<dayLists2.size();i++){
						Object[] obj2 = (Object[])dayLists2.get(i);
						year.put(obj2[0], obj2[1]);
					}
					
					Query query3 = submitDao.getSession().createSQLQuery(sql3);
					List dayLists3 = query3.list();
					Map count = new HashMap();
					for(int i=0;i<dayLists3.size();i++){
						Object[] obj3 = (Object[])dayLists3.get(i);
						count.put(obj3[0], obj3[1]);
					}
					
					String aid = "";
					for(int i=0;i<org.length;i++){
						aid = aid +"'"+org[i]+"',";
					}
					aid = aid.substring(0, aid.length()-1);
					
					
					String sql =  "select o.org_id,o.org_name,NVL(tab1.num1,0) from t_uums_base_org o"+ 
						" left join (select t.org_id ,count(*) as num1 "+
						" from t_info_base_publish t "+
						" where  t.pub_submit_date>=:sDate " +
						" and t.pub_submit_date<=:eDate " +
						" group by t.org_id)tab1 on "+
					    "   o.org_id=tab1.org_id where o.org_id in("+aid+") order by NVL(tab1.num1,0) desc ";
					
					
					
						Query query = submitDao.getSession().createSQLQuery(sql);
					
					
					List dayLists = 
						query.setDate("sDate", sDate)
						.setDate("eDate", eDate).setFirstResult(pno).setMaxResults(unitpage)
						.list();
					for(int i=0;i<dayLists.size();i++){
						Object[] obj4 = (Object[])dayLists.get(i);
						list.add(new Object[]{obj4[1],count.get(obj4[0]),day.get(obj4[0]),obj4[2],year.get(obj4[0])});
					}
			}
			
			

			if(useStatus.equals("1")){
				String sql1 = "select t.org_id ,count(*) as num1 from t_info_base_publish t " +
							  " where  t.pub_submit_date>=:stDate " +
							  " and t.pub_submit_date<=:etDate" +
							  " group by t.org_id";//当日
				String sql2 ="select t.org_id ,count(*) as num1 from t_info_base_publish t " +
							" where  t.pub_submit_date>=:smDate " +
							" and t.pub_submit_date<=:emDate" +
							" group by t.org_id";//当月
	
					String sql3 ="select t.org_id ,count(*) as num1 from t_info_base_publish t " +
					" where  t.pub_submit_date>=:sDate " +
					" and t.pub_submit_date<=:eDate" +
					" group by t.org_id";//当年
					
					Query query1 = submitDao.getSession().createSQLQuery(sql1);
					List dayLists1 = 
						query1.setDate("stDate", stDate)
						.setDate("etDate", etDate)
						.list();
					Map day = new HashMap();
					for(int i=0;i<dayLists1.size();i++){
						Object[] obj1 = (Object[])dayLists1.get(i);
						day.put(obj1[0], obj1[1]);
					}
					
					Query query2 = submitDao.getSession().createSQLQuery(sql2);
					List dayLists2 = 
						query2.setDate("smDate", smDate)
						.setDate("emDate", emDate)
						.list();
					Map year = new HashMap();
					for(int i=0;i<dayLists2.size();i++){
						Object[] obj2 = (Object[])dayLists2.get(i);
						year.put(obj2[0], obj2[1]);
					}
					
					Query query3 = submitDao.getSession().createSQLQuery(sql3);
					List dayLists3 = 
						query3.setDate("sDate", sDate)
							.setDate("eDate", eDate)
							.list();
					Map count = new HashMap();
					for(int i=0;i<dayLists3.size();i++){
						Object[] obj3 = (Object[])dayLists3.get(i);
						count.put(obj3[0], obj3[1]);
					}
					
					
					
					String aid = "";
					for(int i=0;i<org.length;i++){
						aid = aid +"'"+org[i]+"',";
					}
					aid = aid.substring(0, aid.length()-1);
					
					
					String sql =  "select o.org_id,o.org_name,NVL(tab1.num1,0) from t_uums_base_org o"+ 
						" left join (select t.org_id ,count(*) as num1 "+
						" from t_info_base_publish t "+
						" where  t.pub_submit_date is not null"+
						" group by t.org_id)tab1 on "+
					    "   o.org_id=tab1.org_id where o.org_id in("+aid+") order by NVL(tab1.num1,0) desc ";
					
					
					
						Query query = submitDao.getSession().createSQLQuery(sql);
					
					
					List dayLists = 
						query
						.setFirstResult(pno).setMaxResults(unitpage)
						.list();
					for(int i=0;i<dayLists.size();i++){
						Object[] obj4 = (Object[])dayLists.get(i);
						list.add(new Object[]{obj4[1],obj4[2],day.get(obj4[0]),count.get(obj4[0]),year.get(obj4[0])});
					}
			}
			
			return list;
		}
	
	/**
	 * 查询各机构每月得分
	 * 
	 */
	public List<?> getStatistics2(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
			
			String aid = "";
			for(int i=0;i<org.length;i++){
				aid = aid +"'"+org[i]+"',";
			}
			if(!"".equals(aid)){
			aid = aid.substring(0, aid.length()-1);
			}
			else{
				aid = null;
			}
					String sql1 = "SELECT T.ORG_ID AS ORGID,  to_char(I.ISS_TIME, 'yyyy-mm') AS SUBDATE, SUM(T.PUB_CODE) AS SCORE " +
								 " FROM   T_INFO_BASE_PUBLISH T JOIN  T_INFO_BASE_ISSUE I ON T.ISS_ID=I.ISS_ID " +
								 " WHERE I.ISS_TIME>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND I.ISS_TIME<=to_date('"+year+"-12-30 23:59:59' ,'yyyy-mm-dd hh24:mi:ss')" +
								 " AND T.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH='1' " +
								 " AND  T.ORG_ID IN("+aid+") GROUP BY T.ORG_ID,to_char(I.ISS_TIME, 'yyyy-mm')";
					
					Query query1 = submitDao.getSession().createSQLQuery(sql1).setResultTransformer(Transformers.aliasToBean(PointDto.class));
					List<PointDto> list1 = query1
					.list();
					Set<PointDto> point = new HashSet<PointDto>();
					for(int i=0;i<org.length;i++){
						for(Integer n=1;n<=12;n++){
							PointDto po1 = new PointDto();
							po1.setOrgid(org[i]);
							if(n.toString().length()==1){
								po1.setSubdate(year+"-0"+n);
							}
							else{
								po1.setSubdate(year+"-"+n);
							}
								po1.setScore(BigDecimal.valueOf(0));
								point.add(po1);
						}
					}
					for(PointDto po : list1){
						if(point.contains(po)){
							point.remove(po);
						}
					}
					point.addAll(list1);
					String sql2 = " SELECT P.ORG_ID AS ORGID,to_char(P.PIECE_TIME, 'yyyy-mm') AS SUBDATE ,SUM(P.PIECE_CODE)AS SCORE  FROM  T_INFO_BASE_PIECE P  " +
					"WHERE  P.PIECE_TIME >=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PIECE_TIME <= to_date('"+year+"-12-30 23:59:59' ,'yyyy-mm-dd hh24:mi:ss') GROUP BY P.ORG_ID,to_char(P.PIECE_TIME, 'yyyy-mm')" ;
					Query query2 = submitDao.getSession().createSQLQuery(sql2);
					List list2 = query2
					.list();
					
					Set<PointDto> po2 = new HashSet<PointDto>();
					for(PointDto po : point){
						for(Integer n=1;n<=12;n++){
							PointDto po1 = new PointDto();
							po1.setOrgid(po.getOrgid());
							if(n.toString().length()==1){
								po1.setSubdate(year+"-0"+n);
							}
							else{
								po1.setSubdate(year+"-"+n);
							}
							if(!point.contains(po1)){
								po1.setScore(BigDecimal.valueOf(0));
								po2.add(po1);
							}
						}
					}
					
					point.addAll(po2);
					
					for(PointDto po : point){
						for(int m=0;m<list2.size();m++){
							if(po.getOrgid().equals(((Object[])list2.get(m))[0].toString())){
								if(po.getSubdate().equals(((Object[])list2.get(m))[1].toString())){
									String ss = ((Object[])list2.get(m))[2].toString();
									int a = po.getScore().intValue()+Integer.valueOf(ss);
									po.setScore(BigDecimal.valueOf(a));
							}
						}
					}
				}
					List<PointDto> list = new ArrayList();//生成List
					Iterator<PointDto> it = point.iterator();
					while(it.hasNext()){
						list.add(it.next());			//Set转化List
					}
					List<Object[]> l = new ArrayList<Object[]>();
					for(int i=0;i<list.size();i++){
						Object[]obj = new Object[3];
						obj[0]= list.get(i).getOrgid();
						obj[1]= list.get(i).getSubdate();
						if(list.get(i).getScore().toString().equals("0")){
							obj[2]= "";
						}
						else{
						obj[2]= list.get(i).getScore();
						}
						l.add(obj);
					}
			return l;
		}
	
	/**
	 * 查询机构名称和总得分报表
	 * 
	 */
	public List<?> getStatistics3(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
		
			
			String aid = "";
			for(int i=0;i<org.length;i++){
				aid = aid +"'"+org[i]+"',";
			}
			
			
			if(!"".equals(aid)){
				aid = aid.substring(0, aid.length()-1);
				}
			else{
				aid = null;
			}
			
					String sql = "SELECT TAB3.ORG_ID,TAB3.ORG_NAME,NVL((NVL(TAB4.SUM1, 0)+TAB3.SCORE),0) AS SUM1 " +
							"FROM (SELECT O.ORG_ID, O.ORG_NAME, NVL(TAB2.SCORE,0) AS SCORE   FROM  T_UUMS_BASE_ORG O" +
							" LEFT JOIN (SELECT F.ORG_ID, SUM(F.PUB_CODE)AS SCORE FROM   T_INFO_BASE_PUBLISH F JOIN  T_INFO_BASE_ISSUE I ON F.ISS_ID=I.ISS_ID "+
							"WHERE I.ISS_TIME>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND I.ISS_TIME<=to_date('"+year+"-12-30 23:59:59' ,'yyyy-mm-dd hh24:mi:ss') "+
							" AND F.PUB_USE_STATUS=1 AND  I.ISS_IS_PUBLISH='1'"+
							" GROUP BY F.ORG_ID)TAB2 ON O.ORG_ID =  TAB2.ORG_ID WHERE O.ORG_ISDEL='0' ";
					
					if(!"".equals(aid)){
						sql = sql+" AND O.ORG_ID IN("+aid+")";
					}
					sql = sql +" ORDER BY NVL(TAB2.SCORE, 0) DESC) TAB3 LEFT JOIN " +
							"(SELECT SUM(P.PIECE_CODE)AS SUM1 ,P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
							"	WHERE P.PIECE_TIME >= to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PIECE_TIME <= to_date('"+year+"-12-30 23:59:59' ,'yyyy-mm-dd hh24:mi:ss')  GROUP BY P.ORG_ID)TAB4 ON TAB3.ORG_ID=TAB4.ORG_ID" +
									" ORDER BY SUM1 DESC";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			
			return dayLists;
		}
	
	/**
	 * 查询市级各机构每月得分
	 * 
	 */
	public List<?> getStatistics11(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException
		{
			
		String aid = "";
		for(int i=0;i<org.length;i++){
			aid = aid +"'"+org[i]+"',";
		}
		
		if(!"".equals(aid)){
			aid = aid.substring(0, aid.length()-1);
			}
		else{
			aid = null;
		}
		
		String bid = "";
		for(int i=0;i<porg.length;i++){
			bid = bid +"'"+porg[i]+"',";
		}
		
		if(!"".equals(bid)){
			bid = bid.substring(0, bid.length()-1);
			}
		else{
			bid = null;
		}
					String sql1 = 
							" SELECT T.ORG_ID AS ORGID,  to_char(I.ISS_TIME, 'yyyy-mm') AS SUBDATE, NVL(SUM(T.PUB_CODE),0) AS SCORE FROM    T_INFO_BASE_PUBLISH T JOIN  T_INFO_BASE_ISSUE I ON T.ISS_ID=I.ISS_ID" +
								 " WHERE I.ISS_TIME>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND I.ISS_TIME<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') " +
								 " AND T.PUB_USE_STATUS=1  AND I.ISS_IS_PUBLISH='1'  " +
								 " AND  T.ORG_ID IN("+aid+") GROUP BY T.ORG_ID,to_char(I.ISS_TIME, 'yyyy-mm')";
					
					Query query1 = submitDao.getSession().createSQLQuery(sql1).setResultTransformer(Transformers.aliasToBean(PointDto.class));
					List<PointDto> list1 = query1
					.list();
					Set<PointDto> point = new HashSet<PointDto>();
					point.addAll(list1);
					
					String sql2 = "SELECT O.ORG_PARENT_ID, O.ORG_NAME,TAB2.NUM, NVL(SUM(TAB2.SCORE),0) AS SUM1 " +
							" FROM   T_UUMS_BASE_ORG O  LEFT JOIN (SELECT F.ORG_ID,to_char(I.ISS_TIME, 'yyyy-mm') AS NUM , SUM(F.PUB_CODE)AS SCORE " +
							" FROM   T_INFO_BASE_PUBLISH F JOIN  T_INFO_BASE_ISSUE I ON F.ISS_ID=I.ISS_ID "+
							 " WHERE I.ISS_TIME>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND I.ISS_TIME<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss')  AND F.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH='1'  " +
							 " GROUP BY F.ORG_ID,to_char(I.ISS_TIME, 'yyyy-mm') )TAB2 ON O.ORG_ID =  TAB2.ORG_ID WHERE O.ORG_ISDEL='0'   " +
							 " AND  TAB2.ORG_ID IN("+bid+") GROUP BY  O.ORG_PARENT_ID, O.ORG_NAME,TAB2.NUM "; 
					Query query2 = submitDao.getSession().createSQLQuery(sql2);
					List list2 = query2
					.list();
					
					String sql3 = " SELECT P.ORG_ID,to_char(P.PIECE_TIME, 'yyyy-mm') AS SUBDATE ,NVL(SUM(P.PIECE_CODE),0) AS SUM1  FROM  T_INFO_BASE_PIECE P  " +
							"WHERE  P.PIECE_TIME >= to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PIECE_TIME <= to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss')  GROUP BY P.ORG_ID,to_char(P.PIECE_TIME, 'yyyy-mm')" ;
					Query query3 = submitDao.getSession().createSQLQuery(sql3);
					List list3 = query3
					.list();
					
					String sql4 = " SELECT P.PARENT_ORG_ID,to_char(P.PIECE_TIME, 'yyyy-mm') AS SUBDATE ,NVL(SUM(P.PIECE_CODE),0) AS SUM1  FROM  T_INFO_BASE_PIECE P  " +
					"WHERE  P.PIECE_TIME >= to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PIECE_TIME <= to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') AND P.PARENT_ORG_ID IS NOT NULL  GROUP BY P.PARENT_ORG_ID,to_char(P.PIECE_TIME, 'yyyy-mm')" ;
					Query query4 = submitDao.getSession().createSQLQuery(sql4);
					List list4 = query4
					.list();
					Set<PointDto> po2 = new HashSet<PointDto>();
					for(int i=0;i<org.length;i++){
					//for(PointDto po : point){
						for(Integer n=1;n<=12;n++){
							PointDto po1 = new PointDto();
							po1.setOrgid(org[i]);
							if(n.toString().length()==1){
								po1.setSubdate(year+"-0"+n);
							}
							else{
								po1.setSubdate(year+"-"+n);
							}
							if(!point.contains(po1)){
								po1.setScore(BigDecimal.valueOf(0));
								po2.add(po1);
							}
						}
					//}
					}
					point.addAll(po2);
					
					for(PointDto po : point){
						for(int j=0;j<list2.size();j++){
							if(po.getOrgid().equals(((Object[])list2.get(j))[0].toString())){
									if(po.getSubdate().equals(((Object[])list2.get(j))[2].toString())){
										String ss = ((Object[])list2.get(j))[3].toString();
										int a = po.getScore().intValue()+Integer.valueOf(ss);
										po.setScore(BigDecimal.valueOf(a));
									}
								}
							}
					for(int m=0;m<list3.size();m++){
						if(po.getOrgid().equals(((Object[])list3.get(m))[0].toString())){
							if(po.getSubdate().equals(((Object[])list3.get(m))[1].toString())){
								String ss = ((Object[])list3.get(m))[2].toString();
								int a = po.getScore().intValue()+Integer.valueOf(ss);
								po.setScore(BigDecimal.valueOf(a));
							}
						}
					}
					
					for(int n=0;n<list4.size();n++){
						if(po.getOrgid().equals(((Object[])list4.get(n))[0].toString())){
							if(po.getSubdate().equals(((Object[])list4.get(n))[1].toString())){
								String ss = ((Object[])list4.get(n))[2].toString();
								int a = po.getScore().intValue()+Integer.valueOf(ss);
								po.setScore(BigDecimal.valueOf(a));
							}
						}
					}
				}
				List<PointDto> list = new ArrayList();//生成List
				Iterator<PointDto> it = point.iterator();
				while(it.hasNext()){
					list.add(it.next());			//Set转化List
				}
				List<Object[]> l = new ArrayList<Object[]>();
				for(int i=0;i<list.size();i++){
					Object[]obj = new Object[3];
					obj[0]= list.get(i).getOrgid();
					obj[1]= list.get(i).getSubdate();
					if(list.get(i).getScore().toString().equals("0")){
						obj[2]= "";
					}
					else{
					obj[2]= list.get(i).getScore();
					}
					l.add(obj);
				}
			return l;
		}
	
	/**
	 * 查询市级单位机构名称和总得分报表
	 * 
	 */
	public List<?> getStatistics12(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException
		{
		
			
			String aid = "";
			for(int i=0;i<org.length;i++){
				aid = aid +"'"+org[i]+"',";
			}
			
			if(!"".equals(aid)){
				aid = aid.substring(0, aid.length()-1);
				}
			else{
				aid = null;
			}
			
			String bid = "";
			for(int i=0;i<porg.length;i++){
				bid = bid +"'"+porg[i]+"',";
			}
			
			if(!"".equals(bid)){
				bid = bid.substring(0, bid.length()-1);
				}
			else{
				bid = null;
			}
							String sql = "SELECT TAB9.ORG_ID,TAB9.ORG_NAME,(NVL(TAB9.SUM1, 0)+NVL(TAB8.SUM1,0)) AS SUM2 FROM  " +
									"(SELECT TAB6.ORG_ID,TAB6.ORG_NAME,NVL((NVL(TAB7.SUM1, 0)+TAB6.SUM1),0) AS SUM1 FROM  " +
							" (SELECT TAB3.ORG_ID,TAB3.ORG_NAME,NVL((NVL(TAB5.SUM2, 0)+NVL(TAB3.SCORE,0)),0) AS SUM1 FROM " +
							" (SELECT O.ORG_ID, O.ORG_NAME, TAB2.SCORE  FROM   T_UUMS_BASE_ORG O" +
							" LEFT JOIN (SELECT F.ORG_ID, SUM(F.PUB_CODE)AS SCORE FROM   T_INFO_BASE_PUBLISH F , T_INFO_BASE_ISSUE I "+
							" WHERE I.ISS_ID=F.ISS_ID AND I.ISS_TIME>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND I.ISS_TIME<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') "+
							" AND F.PUB_USE_STATUS=1  AND I.ISS_IS_PUBLISH='1'  "+
							" GROUP BY F.ORG_ID)TAB2 ON O.ORG_ID =  TAB2.ORG_ID WHERE O.ORG_ISDEL='0' ";
					
					if(!"".equals(aid)){
						sql = sql+" AND O.ORG_ID IN("+aid+")";
					}
					sql = sql +" ORDER BY NVL(TAB2.SCORE, 0) DESC) TAB3 LEFT JOIN " +
							" (SELECT TAB4.ORG_PARENT_ID ,SUM(TAB4.SUM1)AS SUM2 FROM (SELECT O.ORG_PARENT_ID, O.ORG_NAME, NVL(SUM(TAB2.SCORE),0) " +
							" AS SUM1  FROM   T_UUMS_BASE_ORG O LEFT JOIN (SELECT F.ORG_ID, SUM(F.PUB_CODE)AS SCORE FROM   T_INFO_BASE_PUBLISH F " +
							" , T_INFO_BASE_ISSUE I WHERE I.ISS_ID=F.ISS_ID AND I.ISS_TIME>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND I.ISS_TIME<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') " +
							" AND F.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH='1'  " +
							" GROUP BY F.ORG_ID)TAB2 ON O.ORG_ID =  TAB2.ORG_ID WHERE O.ORG_ISDEL='0'  ";
							if(!"".equals(bid)){
								sql = sql+" AND O.ORG_ID IN("+bid+")";
							}
					sql = sql +" GROUP BY  O.ORG_PARENT_ID, O.ORG_NAME ) TAB4 GROUP BY TAB4.ORG_PARENT_ID )TAB5 " +
							" ON TAB3.ORG_ID=TAB5.ORG_PARENT_ID)TAB6 LEFT JOIN ";
					sql = sql +	"(SELECT SUM(P.PIECE_CODE)AS SUM1 ,P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
							"	WHERE P.PIECE_TIME >= to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PIECE_TIME <= to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') GROUP BY P.ORG_ID)TAB7 ON TAB6.ORG_ID=TAB7.ORG_ID " +
									" ORDER BY SUM1 DESC)TAB9 LEFT JOIN " +
									" (SELECT P.PARENT_ORG_ID ,NVL(SUM(P.PIECE_CODE),0) AS SUM1  FROM  T_INFO_BASE_PIECE P  " +
					" WHERE  P.PIECE_TIME >= '"+year+"-01-01 0:0:1' AND P.PIECE_TIME <= to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') AND P.PARENT_ORG_ID IS NOT NULL GROUP BY P.PARENT_ORG_ID)TAB8 " +
							" ON TAB9.ORG_ID=TAB8.PARENT_ORG_ID ORDER BY SUM2 DESC ";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
				
				return dayLists;
		}
	/**
	 * 查询各县（市区）在设区市信息采用情况
	 * 
	 */
	public List<?> getStatistics4(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
		
			
			String aid = "";
			for(int i=0;i<org.length;i++){
				aid = aid +"'"+org[i]+"',";
			}
			
			
			if(!"".equals(aid)){
				aid = aid.substring(0, aid.length()-1);
				}
			else{
				aid = null;
			}
					
					String sql = "SELECT TAB3.ORG_ID,TAB3.ORG_NAME,NVL(TAB4.SUM1, 0)+NVL(TAB3.SUM1,0) AS SUM1,TAB3.CODE FROM(SELECT O.ORG_ID,O.ORG_NAME,TAB1.SUM1,SUBSTR(O.ORG_SYSCODE, 0, 9)  AS CODE "+
					 " FROM  T_UUMS_BASE_ORG O   LEFT JOIN (SELECT F.ORG_ID, SUM(F.PUB_CODE)AS SUM1 FROM    T_INFO_BASE_PUBLISH F JOIN   T_INFO_BASE_ISSUE I ON F.ISS_ID=I.ISS_ID "+
					 " WHERE I.ISS_TIME >=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') "+
					 "AND I.ISS_TIME<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') "+ 
					 " AND F.PUB_USE_STATUS=1  AND I.ISS_IS_PUBLISH='1' GROUP BY F.ORG_ID) TAB1 ON TAB1.ORG_ID = O.ORG_ID WHERE O.ORG_ISDEL='0'   ";
		
					if(!"".equals(aid)){
						sql = sql+" AND O.ORG_ID IN("+aid+")";
					}
					sql = sql +" ORDER BY O.ORG_SYSCODE, NVL(TAB1.SUM1, 0) DESC)TAB3  LEFT JOIN" +
					"(SELECT SUM(P.PIECE_CODE)AS SUM1 ,P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
					"	WHERE P.PIECE_TIME >= to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PIECE_TIME <= to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') GROUP BY P.ORG_ID)TAB4 ON TAB3.ORG_ID=TAB4.ORG_ID" +
							" ORDER BY TAB3.CODE,SUM1 DESC";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
					
			return dayLists;
		}
	
	
	/**
	 * 查询各处室信息采用情况（领导批示活动及发文）通报
	 * 
	 */
	public List<?> getStatistics5(String year,String month) throws ServiceException,
	SystemException, DAOException
		{
		
					String sql = "SELECT TAB1.ORG_ID,SUM(TAB1.NUM) FROM (SELECT T.ORG_ID,T.COL_ID,COUNT(*)AS NUM FROM  T_INFO_BASE_PUBLISH T JOIN  T_INFO_BASE_ISSUE I ON T.ISS_ID=I.ISS_ID" +
							" WHERE  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"' AND I.ISS_IS_PUBLISH='1'   AND T.PUB_USE_STATUS=1 GROUP BY T.ORG_ID,T.COL_ID)TAB1 JOIN  T_INFO_BASE_COLUMN C ON TAB1.COL_ID=C.COL_ID  "+
							"WHERE (C.COL_NAME='领导批示' OR C.COL_NAME='领导活动' OR C.COL_NAME='重要发文')  GROUP BY TAB1.ORG_ID ";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
					
			return dayLists;
		}
	
	/**
	 * 查询各处室信息采用情况（其他信息）通报
	 * 
	 */
	public List<?> getStatistics6(String year,String month) throws ServiceException,
	SystemException, DAOException
		{
		
					String sql = "SELECT TAB1.ORG_ID,TAB1.COL_ID, C.COL_NAME,TAB1.NUM FROM (SELECT T.ORG_ID,T.COL_ID,COUNT(*)AS NUM FROM  T_INFO_BASE_PUBLISH T JOIN  T_INFO_BASE_ISSUE I ON T.ISS_ID=I.ISS_ID" +
							" WHERE to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"' AND I.ISS_IS_PUBLISH='1'  AND T.PUB_USE_STATUS=1 GROUP BY T.ORG_ID,T.COL_ID)TAB1 JOIN  T_INFO_BASE_COLUMN C ON TAB1.COL_ID=C.COL_ID     " +
							" WHERE (C.COL_NAME!='领导批示' AND C.COL_NAME!='领导活动' AND C.COL_NAME!='重要发文') ";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
					
			return dayLists;
		}
	
	/**
	 * 查询各处室信息采用情况（其他信息呈国办或呈阅件）通报
	 * 
	 */
	public List<?> getStatistics6s(String year,String month) throws ServiceException,
	SystemException, DAOException
		{
		
					String sql = "SELECT P.ORG_ID, P.PIECE_FLAG,COUNT(*) FROM  T_INFO_BASE_PIECE P" +
							" WHERE to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"' GROUP BY P.PIECE_FLAG,P.ORG_ID ";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
					
			return dayLists;
		}
	
	
	/**
	 * 查询各处室信息采用情况（得分）通报
	 * 
	 */
	public List<?> getStatistics7(String year,String month) throws ServiceException,
	SystemException, DAOException
		{
		
					String sql = "SELECT TAB5.ORG_ID ,NVL((NVL(TAB4.SUM1, 0)+TAB5.SUM1),0) AS SUM1 FROM " +
								" (SELECT O.ORG_ID,NVL(TAB3.SUM1,0) AS SUM1 FROM  T_UUMS_BASE_ORG O LEFT JOIN(SELECT T.ORG_ID,SUM(T.PUB_ADOPT_CODE)AS " +
								" SUM1 FROM   T_INFO_BASE_PUBLISH T JOIN  T_INFO_BASE_ISSUE I ON T.ISS_ID=I.ISS_ID "+
								 " WHERE to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+
								 "' AND T.PUB_USE_STATUS=1  AND I.ISS_IS_PUBLISH='1' " +
								 " GROUP BY T.ORG_ID)TAB3  ON O.ORG_ID=TAB3.ORG_ID GROUP BY O.ORG_ID,TAB3.SUM1)TAB5 LEFT JOIN" +
								 "(SELECT NVL(SUM(P.PIECE_CODE),0)AS SUM1 ,P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
								 " WHERE to_char(P.PIECE_TIME,'yyyy-mm')='"+year+"-"+month+"' GROUP BY P.ORG_ID)TAB4 ON TAB5.ORG_ID=TAB4.ORG_ID ";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
					
			return dayLists;
		}
	
	
	/**
	 * 首页厅处室采用情况统计列表
	 * 
	 */
	public List<?> getStatistics8(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
		
			
			String aid = "";
			for(int i=0;i<org.length;i++){
				aid = aid +"'"+org[i]+"',";
			}
			
			
			if(!"".equals(aid)){
				aid = aid.substring(0, aid.length()-1);
				}
			else{
				aid = null;
			}
			
					String sql = "SELECT TAB6.ORG_ID,TAB6.ORG_NAME,TAB6.SUM1 FROM (SELECT TAB5.ORG_ID,TAB5.ORG_NAME,TAB5.SUM1 , rownum as rw FROM ( "+
						"SELECT TAB3.ORG_ID,TAB3.ORG_NAME,NVL((NVL(TAB4.SUM1, 0)+TAB3.SCORE),0) AS SUM1 " +
							"FROM (SELECT O.ORG_ID, O.ORG_NAME, TAB2.SCORE  FROM  T_UUMS_BASE_ORG O" +
							" LEFT JOIN (SELECT F.ORG_ID, SUM(F.PUB_ADOPT_CODE)AS SCORE FROM  T_INFO_BASE_PUBLISH F JOIN  T_INFO_BASE_ISSUE I ON F.ISS_ID=I.ISS_ID "+
							"WHERE I.ISS_TIME>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND I.ISS_TIME<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') "+
							" AND F.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH='1' "+
							" GROUP BY F.ORG_ID)TAB2 ON O.ORG_ID =  TAB2.ORG_ID   ";
					
					if(!"".equals(aid)){
						sql = sql+" WHERE O.ORG_ID IN("+aid+")";
					}
					sql = sql +" ORDER BY NVL(TAB2.SCORE, 0) DESC) TAB3 LEFT JOIN " +
							"(SELECT SUM(P.PIECE_CODE)AS SUM1 ,P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
							"	WHERE  P.PIECE_TIME >= to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PIECE_TIME <= to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') GROUP BY P.ORG_ID)TAB4 ON TAB3.ORG_ID=TAB4.ORG_ID" +
									" ORDER BY SUM1 DESC )TAB5 where rownum <=5 )TAB6 where rw >0  ";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			
			return dayLists;
		}
	
	
	/**
	 * 首页采用排名
	 * 
	 */
	public List<?> getStatistics9(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
		
			
			String aid = "";
			for(int i=0;i<org.length;i++){
				aid = aid +"'"+org[i]+"',";
			}
			
			
			if(!"".equals(aid)){
				aid = aid.substring(0, aid.length()-1);
				}
			else{
				aid = null;
			}
			
					String sql =  "SELECT TAB6.ORG_ID,TAB6.ORG_NAME,TAB6.SUM1 FROM (SELECT TAB5.ORG_ID,TAB5.ORG_NAME,TAB5.SUM1 , rownum as rw FROM ( "+
						"SELECT TAB3.ORG_ID,TAB3.ORG_NAME,NVL((NVL(TAB4.SUM1, 0)+TAB3.SCORE),0) AS SUM1 ,NVL(TAB4.SUM1, 0) AS SUM2,TAB3.SCORE AS SUM3 " +
							"FROM (SELECT O.ORG_ID, O.ORG_NAME, TAB2.SCORE  FROM  T_UUMS_BASE_ORG O" +
							" LEFT JOIN (SELECT F.ORG_ID, SUM(F.PUB_CODE)AS SCORE FROM  T_INFO_BASE_PUBLISH F JOIN  T_INFO_BASE_ISSUE I ON F.ISS_ID=I.ISS_ID "+
							"WHERE I.ISS_TIME>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND I.ISS_TIME<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') "+
							" AND F.PUB_USE_STATUS=1  AND I.ISS_IS_PUBLISH='1' "+
							" GROUP BY F.ORG_ID)TAB2 ON O.ORG_ID =  TAB2.ORG_ID WHERE O.ORG_ISDEL='0' ";
					/*String sql = "select o.org_id,o.org_name ,tab1.num1 from t_uums_base_org o left join "+

					"(select t.org_id,sum(t.pub_use_score) as num1 from t_info_base_publish t "+

					"where t.pub_submit_date>='"+year+"-01-01 0:0:1' and t.pub_submit_date<='"+year+"-12-30 23:59:59' " +
					" group by t.org_id)tab1 "+

					"on o.org_id=tab1.org_id ";*/
					
					if(!"".equals(aid)){
						sql = sql+" AND O.ORG_ID IN("+aid+")";
					}
					sql = sql +" ORDER BY NVL(TAB2.SCORE, 0) DESC) TAB3 LEFT JOIN " +
							"(SELECT SUM(P.PIECE_CODE)AS SUM1 ,P.ORG_ID FROM  T_INFO_BASE_PIECE P " +
							"	WHERE P.PIECE_TIME >= to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PIECE_TIME <=  to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') GROUP BY P.ORG_ID)TAB4 ON TAB3.ORG_ID=TAB4.ORG_ID" +
									" ORDER BY SUM1 DESC )TAB5 where rownum <=5 )TAB6 where rw >0  ";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			
			return dayLists;
		}
	
	
	/**
	 * 采用排名显示具体分数
	 * 
	 */
	public List<?> getStatistics10(String org,String year,String month) throws ServiceException,
	SystemException, DAOException
		{
		
					String sql = "SELECT NVL(SUM(F.PUB_CODE),0)AS SCORE  FROM  T_INFO_BASE_PUBLISH F JOIN  T_INFO_BASE_ISSUE I ON F.ISS_ID=I.ISS_ID "+
							"WHERE  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+
							"' AND F.PUB_USE_STATUS=1  AND I.ISS_IS_PUBLISH='1' AND F.ORG_ID='"+org+"'";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			return dayLists;
		}
	
	/**
	 * 采用排名市级显示具体分数
	 * 
	 */
	public List<?> getStatistics10s(String org,String year,String month) throws ServiceException,
	SystemException, DAOException
		{
		
					String sql = "SELECT NVL(SUM(F.PUB_CODE),0)AS SCORE  FROM  T_INFO_BASE_PUBLISH F JOIN  T_INFO_BASE_ISSUE I ON F.ISS_ID=I.ISS_ID "+
							"WHERE  to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+
							" AND F.PUB_USE_STATUS=1  AND I.ISS_IS_PUBLISH='1' ";
					sql = sql + " AND (F.ORG_ID='"+org+"' OR F.ORG_ID IN(SELECT O.ORG_ID FROM  T_UUMS_BASE_ORG O WHERE O.ORG_PARENT_ID='"+org+"'))  ";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			return dayLists;
		}
	
	/**
	 * 统计每月上报信息条数
	 * 
	 */
	public List<?> getStatisticsByMonthSubmit(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
					String aid = "";
					for(int i=0;i<org.length;i++){
						aid = aid +"'"+org[i]+"',";
					}
					
					
					if(!"".equals(aid)){
						aid = aid.substring(0, aid.length()-1);
						}
					else{
						aid = null;
					}
		
					String sql = "SELECT ORG_ID,to_char(PUB_DATE, 'yyyy-mm'),COUNT(*) FROM T_INFO_BASE_PUBLISH WHERE ORG_ID IN ("+aid+")  AND PUB_DATE IS NOT NULL " +
							" AND  PUB_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND PUB_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') GROUP BY to_char(PUB_DATE, 'yyyy-mm'), ORG_ID";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			return dayLists;
		}
	
	/**
	 * 统计每月采用信息条数
	 * 
	 */
	public List<?> getStatisticsByMonthUse(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
					String aid = "";
					for(int i=0;i<org.length;i++){
						aid = aid +"'"+org[i]+"',";
					}
					
					
					if(!"".equals(aid)){
						aid = aid.substring(0, aid.length()-1);
						}
					else{
						aid = null;
					}
		
					String sql = "SELECT ORG_ID,to_char(PUB_SUBMIT_DATE, 'yyyy-mm'),COUNT(*) FROM T_INFO_BASE_PUBLISH WHERE ORG_ID IN ("+aid+")  AND PUB_SUBMIT_DATE IS NOT NULL " +
							" AND  PUB_SUBMIT_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND PUB_SUBMIT_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') GROUP BY to_char(PUB_SUBMIT_DATE, 'yyyy-mm'), ORG_ID";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			return dayLists;
		}
	
	/**
	 * 统计县级全年上报信息条数按上报信息排序
	 * 
	 */
	public List<?> getStatisticsByXJYearSubmit(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
					String aid = "";
					for(int i=0;i<org.length;i++){
						aid = aid +"'"+org[i]+"',";
					}
					
					
					if(!"".equals(aid)){
						aid = aid.substring(0, aid.length()-1);
						}
					else{
						aid = null;
					}
		
					String sql = " SELECT  O.ORG_ID,O.ORG_NAME,NVL(TAB1.SUM1,0),SUBSTR(O.ORG_SYSCODE, 0, 9)  AS CODE FROM T_UUMS_BASE_ORG O LEFT JOIN (SELECT P.ORG_ID ,COUNT(*) AS SUM1 FROM T_INFO_BASE_PUBLISH P " +
							" WHERE  P.PUB_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PUB_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss')  GROUP BY P.ORG_ID )TAB1" +
							" ON O.ORG_ID=TAB1.ORG_ID " +
							"  WHERE O.ORG_ISDEL='0' AND O.ORG_ID IN ("+aid+") " +
							"  ORDER BY CODE, NVL(TAB1.SUM1,0) DESC";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			return dayLists;
		}
	
	/**
	 * 统计县级全年采用信息条数按采用信息排序
	 * 
	 */
	public List<?> getStatisticsByXJYearUseOrder(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
					String aid = "";
					for(int i=0;i<org.length;i++){
						aid = aid +"'"+org[i]+"',";
					}
					
					
					if(!"".equals(aid)){
						aid = aid.substring(0, aid.length()-1);
						}
					else{
						aid = null;
					}
		
					String sql = " SELECT  O.ORG_ID,O.ORG_NAME,NVL(TAB1.SUM1,0),SUBSTR(O.ORG_SYSCODE, 0, 9)  AS CODE FROM T_UUMS_BASE_ORG O LEFT JOIN (SELECT P.ORG_ID ,COUNT(*) AS SUM1 FROM T_INFO_BASE_PUBLISH P " +
							" WHERE  P.PUB_SUBMIT_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PUB_SUBMIT_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss')  GROUP BY P.ORG_ID )TAB1" +
							" ON O.ORG_ID=TAB1.ORG_ID " +
							"  WHERE O.ORG_ISDEL='0' AND O.ORG_ID IN ("+aid+") " +
							"  ORDER BY CODE, NVL(TAB1.SUM1,0) DESC";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			return dayLists;
		}
	
	/**
	 * 统计全年上报信息条数根据上报信息排序
	 * 
	 */
	public List<?> getStatisticsByYearSubmit(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
					String aid = "";
					for(int i=0;i<org.length;i++){
						aid = aid +"'"+org[i]+"',";
					}
					
					
					if(!"".equals(aid)){
						aid = aid.substring(0, aid.length()-1);
						}
					else{
						aid = null;
					}
		
					String sql = " SELECT  O.ORG_ID,O.ORG_NAME,NVL(TAB1.SUM1,0) FROM T_UUMS_BASE_ORG O LEFT JOIN (SELECT P.ORG_ID ,COUNT(*) AS SUM1 FROM T_INFO_BASE_PUBLISH P " +
							" WHERE  P.PUB_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PUB_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss')  GROUP BY P.ORG_ID )TAB1" +
							" ON O.ORG_ID=TAB1.ORG_ID " +
							"  WHERE O.ORG_ISDEL='0' AND O.ORG_ID IN ("+aid+") " +
							"  ORDER BY NVL(TAB1.SUM1,0) DESC";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			return dayLists;
		}
	
	/**
	 * 统计全年上采用信息条数根据采用信息排序
	 * 
	 */
	public List<?> getStatisticsByYearUseOrder(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
					String aid = "";
					for(int i=0;i<org.length;i++){
						aid = aid +"'"+org[i]+"',";
					}
					
					
					if(!"".equals(aid)){
						aid = aid.substring(0, aid.length()-1);
						}
					else{
						aid = null;
					}
		
					String sql = " SELECT  O.ORG_ID,O.ORG_NAME,NVL(TAB1.SUM1,0) FROM T_UUMS_BASE_ORG O LEFT JOIN (SELECT P.ORG_ID ,COUNT(*) AS SUM1 FROM T_INFO_BASE_PUBLISH P " +
							" WHERE  P.PUB_SUBMIT_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PUB_SUBMIT_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss')  GROUP BY P.ORG_ID )TAB1" +
							" ON O.ORG_ID=TAB1.ORG_ID " +
							"  WHERE O.ORG_ISDEL='0' AND O.ORG_ID IN ("+aid+") " +
							"  ORDER BY NVL(TAB1.SUM1,0) DESC";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			return dayLists;
		}
	
	/**
	 * 统计全年上报信息条数(按采用信息排序)
	 * 
	 */
	public List<?> getStatisticsByYearSubmitOrder(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
					String aid = "";
					for(int i=0;i<org.length;i++){
						aid = aid +"'"+org[i]+"',";
					}
					
					
					if(!"".equals(aid)){
						aid = aid.substring(0, aid.length()-1);
						}
					else{
						aid = null;
					}
		
					String sql = "SELECT  ORG_ID,COUNT(*) FROM T_INFO_BASE_PUBLISH WHERE ORG_ID IN ("+aid+") AND PUB_DATE IS NOT NULL "  +
							" AND PUB_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND PUB_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') GROUP BY ORG_ID";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			return dayLists;
		}
	
	/**
	 * 统计全年采用信息条数(按上报信息排序)
	 * 
	 */
	public List<?> getStatisticsByYearUse(String org[],String year) throws ServiceException,
	SystemException, DAOException
		{
					String aid = "";
					for(int i=0;i<org.length;i++){
						aid = aid +"'"+org[i]+"',";
					}
					
					
					if(!"".equals(aid)){
						aid = aid.substring(0, aid.length()-1);
						}
					else{
						aid = null;
					}
		
					String sql = "SELECT  ORG_ID,COUNT(*) FROM T_INFO_BASE_PUBLISH WHERE ORG_ID IN ("+aid+") AND PUB_SUBMIT_DATE IS NOT NULL "  +
							" AND PUB_SUBMIT_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND PUB_SUBMIT_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') GROUP BY ORG_ID";
					Query query = submitDao.getSession().createSQLQuery(sql);
					List dayLists = query
					.list();
						
			return dayLists;
		}
	
	/**
	 * 统计市级每月上报信息条数
	 * 
	 */
	public List<?> getStatisticsBySJMonthSubmit(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						for(int i=0;i<porg.length;i++){
							bid = bid +"'"+porg[i]+"',";
						}
						
						if(!"".equals(bid)){
							bid = bid.substring(0, bid.length()-1);
							}
						else{
							bid = null;
						}
						String sql = "SELECT ORG_ID AS ORGID,to_char(PUB_DATE, 'yyyy-mm') AS SUBDATE,COUNT(*) AS COUNT FROM T_INFO_BASE_PUBLISH WHERE ORG_ID IN ("+aid+") AND PUB_DATE IS NOT NULL " +
								" AND  PUB_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND PUB_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') GROUP BY to_char(PUB_DATE, 'yyyy-mm'), ORG_ID";
						Query query1 = submitDao.getSession().createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(PointDto.class));
						List<PointDto> list1 = query1
						.list();
						Set<PointDto> point = new HashSet<PointDto>();
						point.addAll(list1);
						
						String sql2 = "SELECT O.ORG_PARENT_ID,to_char(PUB_DATE, 'yyyy-mm'),COUNT(*) " +
								" FROM T_INFO_BASE_PUBLISH P,T_UUMS_BASE_ORG O WHERE O.ORG_ID IN ("+bid+") AND P.ORG_ID=O.ORG_ID AND PUB_DATE IS NOT NULL AND O.ORG_ISDEL='0'" +
								" GROUP BY O.ORG_PARENT_ID,to_char(PUB_DATE, 'yyyy-mm')";
						Query query2 = submitDao.getSession().createSQLQuery(sql2);
						Set<PointDto> po2 = new HashSet<PointDto>();
						List list2 = query2
						.list();
						for(int i=0;i<org.length;i++){
							//for(PointDto po : point){
								for(Integer n=1;n<=12;n++){
									PointDto po1 = new PointDto();
									po1.setOrgid(org[i]);
									if(n.toString().length()==1){
										po1.setSubdate(year+"-0"+n);
									}
									else{
										po1.setSubdate(year+"-"+n);
									}
									if(!point.contains(po1)){
										po1.setCOUNT(0);
										po2.add(po1);
									}
								}
							//}
							}
							point.addAll(po2);
							
							for(PointDto po : point){
								for(int j=0;j<list2.size();j++){
									if(po.getOrgid().equals(((Object[])list2.get(j))[0].toString())){
											if(po.getSubdate().equals(((Object[])list2.get(j))[1].toString())){
												String ss = ((Object[])list2.get(j))[2].toString();
												int a = po.getCOUNT().intValue()+Integer.valueOf(ss);
												po.setCOUNT(a);
											}
										}
									}
							}
						List<PointDto> list = new ArrayList();//生成List
						Iterator<PointDto> it = point.iterator();
						while(it.hasNext()){
							list.add(it.next());			//Set转化List
						}
						List<Object[]> l = new ArrayList<Object[]>();
						for(int i=0;i<list.size();i++){
							Object[]obj = new Object[3];
							obj[0]= list.get(i).getOrgid();
							obj[1]= list.get(i).getSubdate();
							if(list.get(i).getCOUNT().toString().equals("0")){
								obj[2]= "";
							}
							else{
							obj[2]= list.get(i).getCOUNT();
							}
							l.add(obj);
						}
				return l;
		}
	
	/**
	 * 统计市级每月采用信息条数
	 * 
	 */
	public List<?> getStatisticsBySJMonthUse(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						for(int i=0;i<porg.length;i++){
							bid = bid +"'"+porg[i]+"',";
						}
						
						if(!"".equals(bid)){
							bid = bid.substring(0, bid.length()-1);
							}
						else{
							bid = null;
						}
						String sql = "SELECT ORG_ID AS ORGID,to_char(PUB_SUBMIT_DATE, 'yyyy-mm') AS SUBDATE,COUNT(*) AS COUNT FROM T_INFO_BASE_PUBLISH WHERE ORG_ID IN ("+aid+") AND PUB_SUBMIT_DATE IS NOT NULL " +
								" AND  PUB_SUBMIT_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND PUB_SUBMIT_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') GROUP BY to_char(PUB_SUBMIT_DATE, 'yyyy-mm'), ORG_ID";
						Query query1 = submitDao.getSession().createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(PointDto.class));
						List<PointDto> list1 = query1
						.list();
						Set<PointDto> point = new HashSet<PointDto>();
						point.addAll(list1);
						
						String sql2 = "SELECT O.ORG_PARENT_ID,to_char(PUB_SUBMIT_DATE, 'yyyy-mm'),COUNT(*) " +
								" FROM T_INFO_BASE_PUBLISH P,T_UUMS_BASE_ORG O WHERE O.ORG_ID IN ("+bid+") AND P.ORG_ID=O.ORG_ID AND P.PUB_SUBMIT_DATE IS NOT NULL AND O.ORG_ISDEL='0'" +
								" GROUP BY O.ORG_PARENT_ID,to_char(PUB_SUBMIT_DATE, 'yyyy-mm')";
						Query query2 = submitDao.getSession().createSQLQuery(sql2);
						Set<PointDto> po2 = new HashSet<PointDto>();
						List list2 = query2
						.list();
						for(int i=0;i<org.length;i++){
							//for(PointDto po : point){
								for(Integer n=1;n<=12;n++){
									PointDto po1 = new PointDto();
									po1.setOrgid(org[i]);
									if(n.toString().length()==1){
										po1.setSubdate(year+"-0"+n);
									}
									else{
										po1.setSubdate(year+"-"+n);
									}
									if(!point.contains(po1)){
										po1.setCOUNT(0);
										po2.add(po1);
									}
								}
							//}
							}
							point.addAll(po2);
							
							for(PointDto po : point){
								for(int j=0;j<list2.size();j++){
									if(po.getOrgid().equals(((Object[])list2.get(j))[0].toString())){
											if(po.getSubdate().equals(((Object[])list2.get(j))[1].toString())){
												String ss = ((Object[])list2.get(j))[2].toString();
												int a = po.getCOUNT().intValue()+Integer.valueOf(ss);
												po.setCOUNT(a);
											}
										}
									}
							}
						List<PointDto> list = new ArrayList();//生成List
						Iterator<PointDto> it = point.iterator();
						while(it.hasNext()){
							list.add(it.next());			//Set转化List
						}
						List<Object[]> l = new ArrayList<Object[]>();
						for(int i=0;i<list.size();i++){
							Object[]obj = new Object[3];
							obj[0]= list.get(i).getOrgid();
							obj[1]= list.get(i).getSubdate();
							if(list.get(i).getCOUNT().toString().equals("0")){
								obj[2]= "";
							}
							else{
							obj[2]= list.get(i).getCOUNT();
							}
							l.add(obj);
						}
				return l;
		}
	
	
	/**
	 * 统计市级当前年上报信息条数按上报信息排序
	 * 
	 */
	public List<?> getStatisticsBySJYearSubmit(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						for(int i=0;i<porg.length;i++){
							bid = bid +"'"+porg[i]+"',";
						}
						
						if(!"".equals(bid)){
							bid = bid.substring(0, bid.length()-1);
							}
						else{
							bid = null;
						}
						String sql = "SELECT TAB1.ORG_ID ,TAB1.ORG_NAME,NVL(TAB1.SUM1,0)+NVL(TAB2.SUM2,0) AS SUM3 " +
								" FROM(SELECT O.ORG_ID ,O.ORG_NAME, COUNT(*) AS SUM1 FROM T_UUMS_BASE_ORG O" +
								"  LEFT JOIN T_INFO_BASE_PUBLISH P ON O.ORG_ID=P.ORG_ID  " +
								" WHERE PUB_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND PUB_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') " +
								"  AND PUB_DATE IS NOT NULL AND O.ORG_ID IN ("+aid+") AND O.ORG_ISDEL='0' GROUP BY O.ORG_ID,O.ORG_NAME)TAB1 LEFT JOIN " +
								" (SELECT O.ORG_PARENT_ID,COUNT(*) AS SUM2 FROM T_INFO_BASE_PUBLISH P,T_UUMS_BASE_ORG O " +
								" WHERE P.PUB_DATE>='"+year+"-01-01 0:0:1' AND P.PUB_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') AND O.ORG_ISDEL='0' " +
								" AND P.ORG_ID=O.ORG_ID AND P.PUB_DATE IS NOT NULL AND O.ORG_ID IN ("+bid+")  GROUP BY O.ORG_PARENT_ID)" +
								" TAB2 ON  TAB1.ORG_ID=TAB2.ORG_PARENT_ID ORDER BY SUM3 DESC";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 统计市级当前年采用信息条数按采用信息排序
	 * 
	 */
	public List<?> getStatisticsBySJYearUseOrder(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						for(int i=0;i<porg.length;i++){
							bid = bid +"'"+porg[i]+"',";
						}
						
						if(!"".equals(bid)){
							bid = bid.substring(0, bid.length()-1);
							}
						else{
							bid = null;
						}
						String sql = "SELECT TAB1.ORG_ID ,TAB1.ORG_NAME,NVL(TAB1.SUM1,0)+NVL(TAB2.SUM2,0) AS SUM3 " +
								" FROM(SELECT O.ORG_ID ,O.ORG_NAME, COUNT(*) AS SUM1 FROM T_UUMS_BASE_ORG O" +
								"  LEFT JOIN T_INFO_BASE_PUBLISH P ON O.ORG_ID=P.ORG_ID  " +
								" WHERE PUB_SUBMIT_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND PUB_SUBMIT_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') " +
								"  AND PUB_SUBMIT_DATE IS NOT NULL AND O.ORG_ID IN ("+aid+") AND O.ORG_ISDEL='0' GROUP BY O.ORG_ID,O.ORG_NAME)TAB1 LEFT JOIN " +
								" (SELECT O.ORG_PARENT_ID,COUNT(*) AS SUM2 FROM T_INFO_BASE_PUBLISH P,T_UUMS_BASE_ORG O " +
								" WHERE P.PUB_SUBMIT_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND P.PUB_SUBMIT_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') AND O.ORG_ISDEL='0' " +
								" AND P.ORG_ID=O.ORG_ID AND P.PUB_SUBMIT_DATE IS NOT NULL AND O.ORG_ID IN ("+bid+")  GROUP BY O.ORG_PARENT_ID)" +
								" TAB2 ON  TAB1.ORG_ID=TAB2.ORG_PARENT_ID ORDER BY SUM3 DESC";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 统计市级当前年上报信息条数(按采用信息排序)
	 * 
	 */
	public List<?> getStatisticsBySJYearSubmitOrder(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						for(int i=0;i<porg.length;i++){
							bid = bid +"'"+porg[i]+"',";
						}
						
						if(!"".equals(bid)){
							bid = bid.substring(0, bid.length()-1);
							}
						else{
							bid = null;
						}
						String sql = "SELECT TAB1.ORG_ID ,NVL(TAB1.SUM1,0)+NVL(TAB2.SUM2,0) " +
								" FROM(SELECT ORG_ID ,COUNT(*) AS SUM1 FROM T_INFO_BASE_PUBLISH" +
								" WHERE PUB_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND PUB_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') " +
								" AND PUB_DATE IS NOT NULL AND ORG_ID IN ("+aid+") GROUP BY ORG_ID)TAB1 LEFT JOIN " +
								" (SELECT O.ORG_PARENT_ID,COUNT(*) AS SUM2 FROM T_INFO_BASE_PUBLISH P,T_UUMS_BASE_ORG O " +
								" WHERE PUB_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND PUB_DATE<=to_date('"+year+"-12-30 23:59:59' ,'yyyy-mm-dd hh24:mi:ss') AND O.ORG_ISDEL='0' " +
								" AND P.ORG_ID=O.ORG_ID AND P.PUB_DATE IS NOT NULL AND O.ORG_ID IN ("+bid+")  GROUP BY O.ORG_PARENT_ID)" +
								" TAB2 ON TAB1.ORG_ID=TAB2.ORG_PARENT_ID ";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 统计市级当前年采用信息条数（按上报信息排序）
	 * 
	 */
	public List<?> getStatisticsBySJYearUse(String org[],String year,String porg[]) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						for(int i=0;i<porg.length;i++){
							bid = bid +"'"+porg[i]+"',";
						}
						
						if(!"".equals(bid)){
							bid = bid.substring(0, bid.length()-1);
							}
						else{
							bid = null;
						}
						String sql = "SELECT TAB1.ORG_ID ,NVL(TAB1.SUM1,0)+NVL(TAB2.SUM2,0) " +
								" FROM(SELECT ORG_ID ,COUNT(*) AS SUM1 FROM T_INFO_BASE_PUBLISH" +
								" WHERE PUB_SUBMIT_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND PUB_SUBMIT_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') " +
								" AND PUB_SUBMIT_DATE IS NOT NULL AND ORG_ID IN ("+aid+") GROUP BY ORG_ID)TAB1 LEFT JOIN " +
								" (SELECT O.ORG_PARENT_ID,COUNT(*) AS SUM2 FROM T_INFO_BASE_PUBLISH P,T_UUMS_BASE_ORG O " +
								" WHERE PUB_SUBMIT_DATE>=to_date('"+year+"-01-01 0:0:1','yyyy-mm-dd hh24:mi:ss') AND PUB_SUBMIT_DATE<=to_date('"+year+"-12-30 23:59:59','yyyy-mm-dd hh24:mi:ss') AND O.ORG_ISDEL='0' " +
								" AND P.ORG_ID=O.ORG_ID AND P.PUB_SUBMIT_DATE IS NOT NULL AND O.ORG_ID IN ("+bid+")  GROUP BY O.ORG_PARENT_ID)" +
								" TAB2 ON TAB1.ORG_ID=TAB2.ORG_PARENT_ID ";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 查询市级每日要情数量
	 * 
	 */
	public List<?> getStatisticsByYQ(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "select p.org_id,count(*) from t_info_base_publish p " +
									 "where p.iss_id in(select i.iss_id from t_info_base_issue i " +
									 "where i.jour_id in(select j.jour_id from t_info_base_journal j "+
									 "where j.jour_name='每日要情')   and to_char(i.iss_time,'yyyy-mm')='"+year+"-"+month+"' and i.iss_is_publish='1') "+
									 "and p.org_id in("+aid+") and  p.pub_use_status=1  group by p.org_id ";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 查询市级南昌政务数量
	 * 
	 */
	public List<?> getStatisticsByZW(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "select p.org_id,count(*) from t_info_base_publish p " +
									 "where p.iss_id in(select i.iss_id from t_info_base_issue i " +
									 "where i.jour_id in(select j.jour_id from t_info_base_journal j "+
									 "where j.jour_name='南昌政务')   and to_char(i.iss_time,'yyyy-mm')='"+year+"-"+month+"' and i.iss_is_publish='1') "+
									 "and p.org_id in("+aid+") and p.pub_use_status=1  group by p.org_id ";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}

	
	/**
	 * 查询市级领导批示数量
	 * 
	 */
	public List<?> getStatisticsByPS(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "SELECT TAB1.ORG_ID,COUNT(*) FROM (SELECT P.ORG_ID FROM   T_INFO_BASE_PUBLISH P ,  T_INFO_BASE_ISSUE I " +
						"WHERE P.ISS_ID=I.ISS_ID AND P.PUB_USE_STATUS=1 AND I.ISS_IS_PUBLISH=1 AND P.PUB_IS_INSTRUCTION=1 AND   to_char(I.ISS_TIME,'yyyy-mm')='"+year+"-"+month+"'"+
						" AND P.ORG_ID IN("+aid+") )TAB1 LEFT JOIN  T_UUMS_BASE_ORG O ON  TAB1.ORG_ID=O.ORG_ID GROUP BY TAB1.ORG_ID ";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	/**
	 * 查询省级每日要情数量
	 * 
	 */
	public List<?> getStatisticsBySJYQ(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "select p.org_id,count(*) from t_info_base_piece p where p.piece_flag='3' "+
									 "and p.piece_open='0' and p.org_id in("+aid+")  and to_char(p.piece_time,'yyyy-mm')='"+year+"-"+month+"' group by p.org_id ";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 查询省级江西政务数量
	 * 
	 */
	public List<?> getStatisticsBySJZW(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "select p.org_id,count(*) from t_info_base_piece p where p.piece_flag='3' "+
									 "and p.piece_open='1' and p.org_id in("+aid+")  and to_char(p.piece_time,'yyyy-mm')='"+year+"-"+month+"' group by p.org_id ";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 查询省级数量
	 * 
	 */
	public List<?> getStatisticsBySJ(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "select p.org_id,count(*) from t_info_base_piece p where p.piece_flag='3' "+
									 "and p.org_id in("+aid+")  and to_char(p.piece_time,'yyyy-mm')='"+year+"-"+month+"' group by p.org_id ";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 查询省级领导批示数量
	 * 
	 */
	public List<?> getStatisticsBySJPS(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "select p.org_id,count(*) from t_info_base_piece p where p.piece_flag='3' "+
									 "and p.piece_is_instruction='1' and p.org_id in("+aid+")  and to_char(p.piece_time,'yyyy-mm')='"+year+"-"+month+"' group by p.org_id ";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 查询国办信息
	 * 
	 */
	public List<?> getStatisticsByGBXX(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "select p.org_id,count(*) from t_info_base_piece p where p.piece_flag='1' "+
									 " and p.org_id in("+aid+")  and to_char(p.piece_time,'yyyy-mm')='"+year+"-"+month+"' group by p.org_id ";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 查询国办信息领导批示
	 * 
	 */
	public List<?> getStatisticsByGBXXPS(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "select p.org_id,count(*) from t_info_base_piece p where p.piece_flag='1' "+
									 "and p.piece_is_instruction='1' and p.org_id in("+aid+")  and to_char(p.piece_time,'yyyy-mm')='"+year+"-"+month+"' group by p.org_id ";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 查询本月加分
	 * 
	 */
	public List<?> getStatisticsByJF(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "select p.org_id,sum(p.piece_code) from t_info_base_piece p where p.piece_flag='4' "+
									 "and p.org_id in("+aid+")  and to_char(p.piece_time,'yyyy-mm')='"+year+"-"+month+"' group by p.org_id  ";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 查询本月得分
	 * 
	 */
	public List<?> getStatisticsByDF(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "select tab2.org_id ,nvl(nvl(tab2.code,0) + nvl(tab3.code,0),0) from (select o.org_id,tab1.code from t_uums_base_org o "+
						"left join (select p.org_id,sum(p.pub_code) as code from t_info_base_publish p,t_info_base_issue i "+
						"where  to_char(i.iss_time,'yyyy-mm')='"+year+"-"+month+"'  and p.iss_id=i.iss_id "+
						"and p.pub_use_status=1  and i.iss_is_publish='1' group by p.org_id )tab1 on o.org_id=tab1.org_id where o.org_id in("+aid+"))tab2 left join "+
						"(select p.org_id,sum(p.piece_code) as code from t_info_base_piece p "+
						"where  to_char(p.piece_time,'yyyy-mm')='"+year+"-"+month+"'" +
						" and p.org_id in("+aid+") group by p.org_id)tab3 on tab2.org_id=tab3.org_id";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 查询上月累计
	 * 
	 */
	public List<?> getStatisticsBySYLJ(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "select tab2.org_id ,nvl(nvl(tab2.code,0) + nvl(tab3.code,0),0) from (select o.org_id,tab1.code from t_uums_base_org o "+
									"left join (select p.org_id,sum(p.pub_code) as code from t_info_base_publish p,t_info_base_issue i "+
									"where  to_char(i.iss_time,'yyyy-mm')<'"+year+"-"+month+"' and p.iss_id=i.iss_id and to_char(i.iss_time,'yyyy')='"+year+"' "+
									"and p.pub_use_status=1  and i.iss_is_publish='1' group by p.org_id )tab1 on o.org_id=tab1.org_id where o.org_id in("+aid+"))tab2 left join "+
									"(select p.org_id,sum(p.piece_code) as code from t_info_base_piece p "+
									"where  to_char(p.piece_time,'yyyy-mm')<'"+year+"-"+month+"' and to_char(p.piece_time,'yyyy')='"+year+"' " +
									" and p.org_id in("+aid+") group by p.org_id)tab3 on tab2.org_id=tab3.org_id";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}
	
	/**
	 * 查询累计得分
	 * 
	 */
	public List<?> getStatisticsByLJDF(String org[],String year,String month) throws ServiceException,
	SystemException, DAOException
		{
						String aid = "";
						for(int i=0;i<org.length;i++){
							aid = aid +"'"+org[i]+"',";
						}
						
						
						if(!"".equals(aid)){
							aid = aid.substring(0, aid.length()-1);
							}
						else{
							aid = null;
						}
						String bid = "";
						String sql = "select tab2.org_id  ,tab2.org_name,nvl(nvl(tab2.code,0) + nvl(tab3.code,0), 0) from (select o.org_id,o.org_name,tab1.code from t_uums_base_org o "+
									"left join (select p.org_id,sum(p.pub_code) as code from t_info_base_publish p,t_info_base_issue i "+
									"where  to_char(i.iss_time,'yyyy-mm')<='"+year+"-"+month+"'   and p.iss_id=i.iss_id and to_char(i.iss_time,'yyyy')='"+year+"' "+
									"and p.pub_use_status=1  and i.iss_is_publish='1'  group by p.org_id )tab1 on o.org_id=tab1.org_id where o.org_id in ("+aid+"))tab2 left join "+
									"(select p.org_id,sum(p.piece_code) as code from t_info_base_piece p "+
									"where  to_char(p.piece_time,'yyyy-mm')<='"+year+"-"+month+"' and to_char(p.piece_time,'yyyy')='"+year+"' " +
									" and p.org_id in("+aid+") group by p.org_id)tab3 on tab2.org_id=tab3.org_id  order by nvl(nvl(tab2.code,0) + nvl(tab3.code,0), 0) desc";
						Query query = submitDao.getSession().createSQLQuery(sql);
						List dayLists = query
						.list();
						return dayLists;
		}

	public Page<TInfoBasePublish> findSubmitted(Page<TInfoBasePublish> page,
			Map<String, String> search) throws ServiceException,
			SystemException, DAOException
	{
		Criterion[] criterion = null;
		
		Criterion cr = Restrictions.eq("pubSubmitStatus", Publish.SUBMITTED);
		
		Criterion cr2 = Restrictions.like("pubTitle", "%"+search.get("pubTitle")+"%");
		
		String orgId = search.get("orgId");
		Criterion cr4 = null;
		if("0".equals(orgId))
		{
			cr4 = Restrictions.like("orgId", "%");
		}
		else
		{
			cr4 = Restrictions.eq("orgId", orgId);
		}
		
		String pubDate = search.get("pubDate");
		if(!"".equals(pubDate))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			try
			{
				date = sdf.parse(search.get("pubDate"));
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			Criterion cr3 = Restrictions.eq("pubDate", date);
			criterion = new Criterion[]{cr, cr2, cr3, cr4};
		}
		else
		{
			criterion = new Criterion[]{cr, cr2, cr4};
		}
		
		/*
		@SuppressWarnings("unchecked")
		List<TInfoBasePublish> lists = submitDao.getSession().createCriteria(TInfoBasePublish.class)
			//.add(Restrictions.like("TUumsBaseOrg.orgName", "%"))
			.createAlias("TUumsBaseOrg", "org")
			.add(Restrictions.like("org.orgName", "%江%"))
			.list();
		page.setResult(lists);
		return page;*/
		return submitDao.findByCriteria(page, criterion);
	}

	public Page<TInfoBasePublish> findNotSubmitted(Page<TInfoBasePublish> page,
			Map<String, String> search) throws ServiceException,
			SystemException, DAOException
	{
		Criterion[] criterion = null;	
		Criterion cr = Restrictions.eq("pubSubmitStatus", Publish.NO_SUBMITTED);		
		Criterion cr2 = Restrictions.like("pubTitle", "%"+search.get("pubTitle")+"%");
		Criterion cr4 = Restrictions.eq("orgId", search.get("orgId"));
		
		String pubDate = search.get("pubDate");
		if(!"".equals(pubDate))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			try
			{
				date = sdf.parse(search.get("pubDate"));
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			Criterion cr3 = Restrictions.eq("pubDate", date);
			criterion = new Criterion[]{cr, cr2, cr3, cr4};
		}
		else
		{
			criterion = new Criterion[]{cr, cr2, cr4};
		}		
		
		return submitDao.findByCriteria(page, criterion);
	}

	

	public List<?> fullSubmitted(String jvId) throws ServiceException,
			SystemException, DAOException
	{
		StringBuilder hql = new StringBuilder();
		hql.append("select a.pubId,a.pubTitle,a.orgId," +
				"b.adoUseScore,b.adoDate,b.TInfoBaseColumn.colName ");
		hql.append("from TInfoBasePublish as a,TInfoBaseAdoption as b ");
		hql.append("where a.pubId=b.pubId ");
		hql.append("order by b.adoDate desc ");

		return submitDao.find(hql.toString());
	}

	public List<TInfoBasePublish> lastSubmitted(String orgId) throws ServiceException,
			SystemException, DAOException
	{
		Criterion cr = null;
		Criterion cr1 = Restrictions.eq("pubSubmitStatus", Publish.SUBMITTED);
		if(Publish.ALL.equals(orgId))
		{
			cr = cr1;
		}
		else
		{
			Criterion cr2 = Restrictions.eq("orgId", orgId);
			cr = Restrictions.and(cr1, cr2);
		}
		Criterion cr3 = Restrictions.ne("pubIsMerge", "0");
		Criterion cr4 = Restrictions.isNull("pubIsMerge");
		@SuppressWarnings("unchecked")
		List<TInfoBasePublish> ret = submitDao.getSession()
				.createCriteria(TInfoBasePublish.class)
				.add(cr)
				.add(Restrictions.or(cr3, cr4))
				.add(Restrictions.ne("pubUseStatus", Publish.USED))
				.addOrder(Order.desc("pubDate"))
				.setMaxResults(8)
				.list();
		return ret;
	}

	public List<TInfoBasePublish> lastUsed(String orgId) throws ServiceException,
			SystemException, DAOException
	{
		Criterion cr = null;
		Criterion cr1 = Restrictions.eq("pubUseStatus", Publish.USED);
		if(Publish.ALL.equals(orgId))
		{
			cr = cr1;
		}
		else
		{
			Criterion cr2 = Restrictions.eq("orgId", orgId);
			cr = Restrictions.and(cr1, cr2);
		}
		Criterion cr3 = Restrictions.ne("pubIsMerge", "0");
		Criterion cr4 = Restrictions.isNull("pubIsMerge");
		@SuppressWarnings("unchecked")
		List<TInfoBasePublish> ret = submitDao.getSession()
				.createCriteria(TInfoBasePublish.class)
				.add(cr)
				.add(Restrictions.or(cr3, cr4))
				.addOrder(Order.desc("pubDate"))
				.setMaxResults(7)
				.list();
		return ret;
	}

	public List<TInfoBasePublish> lastShared() throws ServiceException,
			SystemException, DAOException
	{
		@SuppressWarnings("unchecked")
		List<TInfoBasePublish> ret = submitDao.getSession()
				.createCriteria(TInfoBasePublish.class)
				.add(Restrictions.eq("pubIsShare", Publish.SHARED))
				.addOrder(Order.desc("pubDate"))
				.setMaxResults(7)
				.list();
		return ret;
	}

	public List<TInfoBasePublish> findUsedByDate(String date, String orgId)
			throws ServiceException, SystemException, DAOException
	{
		Date sDate = DateTimes.ymd2DateTime(date);
		Date eDate = DateTimes.ymd2DateTime(date, true);
		
		@SuppressWarnings("unchecked")
		List<TInfoBasePublish> rets = submitDao.getSession()
				.createCriteria(TInfoBasePublish.class)
				.add(Restrictions.eq("pubUseStatus", Publish.USED))
				.add(Restrictions.eq("pubSubmitStatus", Publish.SUBMITTED))
				.add(Restrictions.between("pubDate", sDate, eDate))
				.addOrder(Order.desc("orgId"))
				.addOrder(Order.desc("pubDate"))
				.list();
		
		return rets;
	}
	
	public List<TInfoBasePublish> findUsedByDate1(String date, String orgId)
			throws ServiceException, SystemException, DAOException
	{
		Date sDate = DateTimes.ymd2DateTime(date);
		Date eDate = DateTimes.ymd2DateTime(date, true);
		
		@SuppressWarnings("unchecked")
		List<TInfoBasePublish> rets = submitDao.getSession()
				.createCriteria(TInfoBasePublish.class)
				.add(Restrictions.eq("pubUseStatus", Publish.USED))
				.add(Restrictions.eq("pubSubmitStatus", Publish.SUBMITTED))
				.add(Restrictions.between("pubSubmitDate", sDate, eDate))
				.addOrder(Order.desc("orgId"))
				.addOrder(Order.desc("pubDate"))
				.list();
		
		return rets;
	}

	public String getPublishNum(String sdate, String orgId) throws ServiceException, SystemException,
			DAOException
	{
		Criterion cr1 = Restrictions.eq("pubSubmitStatus", Publish.SUBMITTED);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try
		{
			date = sdf.parse(sdate);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		Calendar now = Calendar.getInstance();
		//本月1日
		now.setTime(date);
		now.set(Calendar.DATE, 1);
		Date smDate = now.getTime();
		//本月末日
		now.setTime(date);
		now.add(Calendar.MONTH, 1);
		now.set(Calendar.DATE, 1);
		now.add(Calendar.DATE, -1);
		Date emDate = now.getTime();

		Criterion cr2 = Restrictions.between("pubDate", smDate, emDate);
		Criterion cr3 = Restrictions.eq("orgId", orgId);
		List<TInfoBasePublish> rets = submitDao.findByCriteria(
				new Criterion[]{cr1, cr2, cr3});
		return String.valueOf(rets.size());
	}
	
	public List<TInfoBasePublish> publishlist(String strDate,String endDate) throws ServiceException, SystemException,
	DAOException
	{
		Date sDate = DateTimes.ymd2DateTime(strDate);
		Date eDate = DateTimes.ymd2DateTime(endDate, true);
		Criterion cr1 = Restrictions.between("pubSubmitDate", sDate, eDate);
		Criterion cr2 = Restrictions.eq("isOA", "1");
		List<TInfoBasePublish> pub =  submitDao.findByCriteria(
				new Criterion[]{cr1, cr2});
		return pub;
	}

}
