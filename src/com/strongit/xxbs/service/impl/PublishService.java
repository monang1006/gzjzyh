package com.strongit.xxbs.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.utils.DateTimes;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.IUserService;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.dto.MailInfo;
import com.strongit.xxbs.dto.ScoreRankDto;
import com.strongit.xxbs.entity.TInfoBaseComment;
import com.strongit.xxbs.entity.TInfoBaseIssue;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.entity.TInfoBasePublishUser;
import com.strongit.xxbs.entity.TInfoBaseScore;
import com.strongit.xxbs.entity.TInfoBaseTotalScore;
import com.strongit.xxbs.service.IOrgService;
import com.strongit.xxbs.service.IPSService;
import com.strongit.xxbs.service.IPublishService;
import com.strongit.xxbs.service.IScoreItemService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.util.SpringContextUtil;

import edu.emory.mathcs.backport.java.util.Arrays;

@Service
@OALogger
@Transactional(readOnly = true)
public class PublishService implements IPublishService
{
	private GenericDAOHibernate<TInfoBasePublish, String> publishDao;
	private GenericDAOHibernate<TInfoBaseIssue, String> issueDao;
	private GenericDAOHibernate<TInfoBaseTotalScore, String> tsDao;
	private GenericDAOHibernate<TInfoBasePublishUser, String> publishUserDao;
	private GenericDAOHibernate<TInfoBaseComment, String> commentDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		publishDao = new GenericDAOHibernate<TInfoBasePublish, String>(
				sessionFactory, TInfoBasePublish.class);
		issueDao = new GenericDAOHibernate<TInfoBaseIssue, String>(
				sessionFactory, TInfoBaseIssue.class);
		tsDao = new GenericDAOHibernate<TInfoBaseTotalScore, String>(
				sessionFactory, TInfoBaseTotalScore.class);
		publishUserDao = new GenericDAOHibernate<TInfoBasePublishUser, String>(
				sessionFactory, TInfoBasePublishUser.class);
		commentDao = new GenericDAOHibernate<TInfoBaseComment, String>(
				sessionFactory, TInfoBaseComment.class);
	}

	public TInfoBasePublish getPublish(String pubId) throws ServiceException,
			SystemException, DAOException
	{
		return publishDao.get(pubId);
	}

	public List<TInfoBasePublish> getPublishs() throws ServiceException,
			SystemException, DAOException
	{
		return publishDao.findAll();
	}
	
	public Page<TInfoBasePublish> getPublishsByuser(Page<TInfoBasePublish> page,
			String orgId, String submitStatus, String useStatus,String[]userId)
			throws ServiceException,SystemException, DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		Criterion cr1 = Restrictions.ne("pubIsMerge", "0");
		Criterion cr2 = Restrictions.isNull("pubIsMerge");
		Criterion cr3 = Restrictions.or(cr1, cr2);
		crs.add(cr3);
		if(!Publish.ALL.equals(orgId))
		{
			Criterion cr = Restrictions.eq("orgId", orgId);
			crs.add(cr);
		}
		if(!Publish.ALL.equals(submitStatus))
		{
			Criterion cr = Restrictions.eq("pubSubmitStatus", submitStatus);
			crs.add(cr);
		}
		if(!Publish.ALL.equals(useStatus))
		{
			Criterion cr = Restrictions.eq("pubUseStatus", useStatus);
			crs.add(cr);
		}
			Criterion cr = Restrictions.in("pubId", userId);
			crs.add(cr);
		return publishDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
	}
	
	public Page<TInfoBasePublish> findPublishsIsUread(Page<TInfoBasePublish> page , int curpage,int unitpage)
	throws ServiceException,SystemException, DAOException
	{
		String sql1 = "select distinct pub_id from t_info_base_publish_user";
		Query query1 = publishDao.getSession().createSQLQuery(sql1);
		List list1 = query1.list();
		String ids = "";
		int pno = (curpage-1)*15;
		for(int i=0;i<list1.size();i++){
			ids+="'"+list1.get(i)+"'"+",";
		}
		ids = ids.substring(0, ids.length()-1);
		
		String sql2 = "from TInfoBasePublish t where t.pubId not in("+ids+") " +
				"and t.pubSubmitStatus='1' and (t.pubIsMerge!='0' or t.pubIsMerge is null) ";
		Query query2 = publishDao.getSession().createQuery(sql2);
		List<TInfoBasePublish> list2 = query2.setFirstResult(pno).setMaxResults(unitpage).list();
		
		String sql3 = "select count(*) from t_info_base_publish t where t.pub_id not in("+ids+") " +
		"and t.pub_submit_status='1'";
		Query query3 = publishDao.getSession().createSQLQuery(sql3);
		List<TInfoBasePublish> list3 = query3.list();
		int totalCount = list3.size();
		page.setTotalCount(totalCount);
		page.setResult(list2);
		return page;
	}
	

	public Page<TInfoBasePublish> getPublishs(Page<TInfoBasePublish> page,
			String orgId, String submitStatus, String useStatus)
			throws ServiceException, SystemException, DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		
		if(!Publish.ALL.equals(orgId))
		{
			Criterion cr = Restrictions.eq("orgId", orgId);
			crs.add(cr);
		}
		if(!Publish.ALL.equals(submitStatus))
		{
			Criterion cr = Restrictions.eq("pubSubmitStatus", submitStatus);
			crs.add(cr);
		}
		if(!Publish.ALL.equals(useStatus))
		{
			Criterion cr = Restrictions.eq("pubUseStatus", useStatus);
			crs.add(cr);
		}
		return publishDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
	}
	
	public Page<TInfoBasePublish> getPublishs1(Page<TInfoBasePublish> page,
			String orgId, String submitStatus, String useStatus )
			throws ServiceException, SystemException, DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		
		if(!Publish.ALL.equals(orgId))
		{
			Criterion cr = Restrictions.eq("orgId", orgId);
			crs.add(cr);
		}
		if(!Publish.ALL.equals(submitStatus))
		{
			Criterion cr = Restrictions.eq("pubSubmitStatus", submitStatus);
			crs.add(cr);
		}
		if((!Publish.ALL.equals(useStatus))&&(!Publish.COMMENT.equals(useStatus))&&(!Publish.SHARE.equals(useStatus)))
		{
			Criterion cr = Restrictions.eq("pubUseStatus", useStatus);
			crs.add(cr);
		}
		if(Publish.COMMENT.equals(useStatus))
		{
			Criterion cr = Restrictions.eq("pubIsComment", Publish.COMMENTED);
			crs.add(cr);
		}
		if(Publish.SHARE.equals(useStatus))
		{
			Criterion cr = Restrictions.eq("pubIsShare", Publish.SHARED);
			crs.add(cr);
		}
		
			Criterion cr1 = Restrictions.ne("pubIsMerge", "0");
			Criterion cr2 = Restrictions.isNull("pubIsMerge");
			Criterion cr3 = Restrictions.or(cr1, cr2);
			crs.add(cr3);
		try {
			publishDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return publishDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
	}
	
	
	
	
	public Page<TInfoBasePublish> getPublishs2(Page<TInfoBasePublish> page,
			String orgId, String submitStatus, String useStatus )
			throws ServiceException, SystemException, DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		
		if(!Publish.ALL.equals(orgId))
		{
			Criterion cr = Restrictions.eq("orgId", orgId);
			crs.add(cr);
		}
		if(!Publish.ALL.equals(submitStatus)&&(!Publish.COMMENT.equals(submitStatus)))
		{
			Criterion cr = Restrictions.eq("pubSubmitStatus", submitStatus);
			crs.add(cr);
		}
		if(Publish.COMMENT.equals(submitStatus))
		{
			Criterion cr = Restrictions.eq("pubIsComment", Publish.COMMENTED);
			crs.add(cr);
		}
		Criterion cr1 = Restrictions.ne("pubIsMerge", "0");
		Criterion cr2 = Restrictions.isNull("pubIsMerge");
		Criterion cr3 = Restrictions.or(cr1, cr2);
		crs.add(cr3);
		return publishDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
	}
	
	//信息报送搜索
	public Page<TInfoBasePublish> findPublishs(Page<TInfoBasePublish> page,
			 String orgId, Map<String, String> search, Boolean isSubmitted) throws ServiceException,
			SystemException, DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		
		/*if(isSubmitted)
		{
			Criterion cr = Restrictions.eq("pubSubmitStatus", Publish.SUBMITTED);
			crs.add(cr);
		}*/
		
		if(!Publish.ALL.equals(orgId))
		{
			Criterion cr = Restrictions.eq("orgId", orgId);
			crs.add(cr);
		}
		
		Criterion cr2 = Restrictions.like("pubTitle", "%"+search.get("pubTitle")+"%");
		crs.add(cr2);
		
		if((!search.get("submitStatus").equals("all"))&&(!search.get("submitStatus").equals(null))){
		Criterion cr3 = Restrictions.eq("pubSubmitStatus", search.get("submitStatus"));
		crs.add(cr3);
		}
		
		
		String startDate = search.get("startDate");
		String endDate = search.get("endDate");
		if(!"".equals(startDate) && !"".equals(endDate))
		{
			Date sDate = DateTimes.ymd2DateTime(startDate);
			Date eDate = DateTimes.ymd2DateTime(endDate, true);
			Criterion cr = Restrictions.between("pubDate", sDate, eDate);
			crs.add(cr);
		}
		Criterion cr1 = Restrictions.ne("pubIsMerge", "0");
		Criterion cr4 = Restrictions.isNull("pubIsMerge");
		Criterion cr3 = Restrictions.or(cr1, cr4);
		crs.add(cr3);

		return publishDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
	}
	
	//信息处理搜索
	public Page<TInfoBasePublish> findPublishs1(Page<TInfoBasePublish> page,
			 String orgId, Map<String, String> search, Boolean isSubmitted) throws ServiceException,
			SystemException, DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		
		/*if(isSubmitted)
		{
			Criterion cr = Restrictions.eq("pubSubmitStatus", Publish.SUBMITTED);
			crs.add(cr);
		}*/
		
		/*if((!Publish.ALL.equals(orgId))&&(!"".equals(orgId)))
		{
			Criterion cr = Restrictions.eq("orgId", orgId);
			crs.add(cr);
		}*/
		
		Criterion cr2 = Restrictions.like("pubTitle", "%"+search.get("pubTitle")+"%");
		crs.add(cr2);
		
		
		if((!search.get("useStatus").equals("all"))&&(!search.get("useStatus").equals(null))){
			Criterion cr4 = Restrictions.eq("pubUseStatus", search.get("useStatus"));
			crs.add(cr4);
			}
		String startDate = search.get("startDate");
		String endDate = search.get("endDate");
		if(!"".equals(startDate) && !"".equals(endDate))
		{
			Date sDate = DateTimes.ymd2DateTime(startDate);
			Date eDate = DateTimes.ymd2DateTime(endDate, true);
			Criterion cr = Restrictions.between("pubDate", sDate, eDate);
			crs.add(cr);
		}
		System.out.println(search.get("orgName"));
		Criterion cr1 = Restrictions.ne("pubIsMerge", "0");
		Criterion cr4 = Restrictions.isNull("pubIsMerge");
		Criterion cr3 = Restrictions.or(cr1, cr4);
		Criterion cr5 = Restrictions.like("orgName", "%"+search.get("orgName")+"%");
		Criterion cr6 = Restrictions.eq("pubSubmitStatus", Publish.SUBMITTED);
		crs.add(cr6);
		crs.add(cr3);
		crs.add(cr5);
		return publishDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
	}
	

	public List<TInfoBasePublish> findPublishsByIssId(String issId)
	throws ServiceException, SystemException, DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		
		Criterion cr1 = Restrictions.eq("pubUseStatus", Publish.USED);
		crs.add(cr1);
		
		Criterion cr2 = Restrictions.eq("TInfoBaseIssue.issId", issId);
		crs.add(cr2);
		
//		@SuppressWarnings("unchecked")
//		List<TInfoBasePublish> rets = publishDao.getSession()
//				.createCriteria(TInfoBasePublish.class)
//				.add(Restrictions.eq("pubUseStatus", Publish.USED))
//				.add(Restrictions.eq("TInfoBaseIssue.issId", issId))			
//				.addOrder(Order.asc("TInfoBaseColumn.colId"))
//				.addOrder(Order.desc("pubSubmitDate"))
//				.list();
		
		StringBuilder hql = new StringBuilder();
		hql.append("select t1 ");
		hql.append("from TInfoBasePublish t1,TInfoBaseColumn t2 ");
		hql.append("where t1.TInfoBaseColumn.colId=t2.colId and t1.pubUseStatus=:useStatus ");
		hql.append("and t1.TInfoBaseIssue.issId=:issId and (t1.pubIsMerge!=0  or t1.pubIsMerge is null) ");
		hql.append("order by t1.pubSort desc  ");
		
		@SuppressWarnings("unchecked")
		List<TInfoBasePublish> rets = publishDao.getSession()
				.createQuery(hql.toString())
				.setString("useStatus", Publish.USED)
				.setString("issId", issId)
				.list();
		return rets;
	}
	
	public List<TInfoBasePublish> findPublishsByIssId(String issId, String orgId)
		throws ServiceException, SystemException, DAOException
	{
//		@SuppressWarnings("unchecked")
//		List<TInfoBasePublish> rets = publishDao.getSession()
//				.createCriteria(TInfoBasePublish.class)
//				.add(Restrictions.eq("pubUseStatus", Publish.USED))
//				.add(Restrictions.eq("TInfoBaseIssue.issId", issId))			
//				.add(Restrictions.eq("orgId", orgId))			
//				.addOrder(Order.asc("TInfoBaseColumn.colId"))
//				.list();
		StringBuilder hql = new StringBuilder();
		hql.append("select t1 ");
		hql.append("from TInfoBasePublish t1,TInfoBaseColumn t2 ");
		hql.append("where t1.TInfoBaseColumn.colId=t2.colId and t1.pubUseStatus=:useStatus ");
		hql.append("and t1.orgId=:orgId ");
		hql.append("and t1.TInfoBaseIssue.issId=:issId ");
		hql.append("order by t2.colSort ");
		
		@SuppressWarnings("unchecked")
		List<TInfoBasePublish> rets = publishDao.getSession()
				.createQuery(hql.toString())
				.setString("useStatus", Publish.USED)
				.setString("issId", issId)
				.setString("orgId", orgId)
				.list();
		return rets;
	}

	
	public List<TInfoBasePublish> findPublishsByIssIdAnduserId(String issId, String userId)
	throws ServiceException, SystemException, DAOException
	{
	@SuppressWarnings("unchecked")
	List<TInfoBasePublish> rets = publishDao.getSession()
			.createCriteria(TInfoBasePublish.class)
			.add(Restrictions.eq("pubUseStatus", Publish.USED))
			.add(Restrictions.eq("TInfoBaseIssue.issId", issId))			
			.add(Restrictions.eq("pubPublisherId", userId))			
			.addOrder(Order.asc("TInfoBaseColumn.colId"))
			.list();
	return rets;
	}

	public Integer numUsed(String orgId) throws ServiceException,
			SystemException, DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		
		Criterion cr = Restrictions.eq("pubUseStatus", Publish.USED);
		crs.add(cr);
		Criterion cr2= Restrictions.eq("orgId", orgId);
		crs.add(cr2);
		int size = publishDao.findByCriteria(crs.toArray(new Criterion[crs.size()])).size();
		return Integer.valueOf(size);
	}

	public Integer numSubmitted(String orgId) throws ServiceException,
			SystemException, DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		
		Criterion cr = Restrictions.eq("pubSubmitStatus", Publish.SUBMITTED);
		crs.add(cr);
		Criterion cr2 = Restrictions.eq("orgId", orgId);
		crs.add(cr2);
		int size = publishDao.findByCriteria(crs.toArray(new Criterion[crs.size()])).size();
		return Integer.valueOf(size);
	}

	public Boolean isExistTitle(String title) throws ServiceException,
			SystemException, DAOException
	{
		Boolean isExist = false;
		Criterion cr = Restrictions.eq("pubTitle", title);
		List<TInfoBasePublish> rets = publishDao.findByCriteria(cr);
		if(rets != null && rets.size() > 0)
		{
			isExist = true;
		}
		return isExist;
	}

	@Transactional(readOnly = false)
	public void savePublish(TInfoBasePublish publish, OALogInfo... loginfos) throws ServiceException,
			SystemException, DAOException
	{
		publishDao.save(publish);
	}

	@Transactional(readOnly = false)
	public void savePublish(TInfoBasePublish publish, String[] scIds, OALogInfo... loginfos)
			throws ServiceException, SystemException, DAOException
	{
		ApplicationContext ctx = SpringContextUtil.getApplicationContext();
		
		IUserService userService = (IUserService) ctx.getBean("userService");
		TUumsBaseOrg org = userService.getOrgInfoByOrgId(publish.getOrgId());
		publish.setPubOrgType(org.getIsOrg());
		publishDao.save(publish);
		
		int jf = 0;
		if (scIds != null && scIds.length > 0)
		{
			IPSService pSService = (IPSService) ctx.getBean("PSService");
			IScoreItemService scoreItemService = (IScoreItemService) ctx.getBean("scoreItemService");
			@SuppressWarnings("unchecked")
			List<String> ls =  Arrays.asList(scIds);
			pSService.saveIPSs(publish.getPubId(), ls);
			
			List<TInfoBaseScore> sis = scoreItemService.getScoreItems(scIds);
			for(TInfoBaseScore one : sis)
			{
				jf +=one.getScScore().intValue();
			}
		}
		
		int tf = 0;
		int us = 0;
		int is = 0;
		if(Publish.USED.equals(publish.getPubUseStatus())
				&& publish.getPubUseScore() != null)
		{
			us = publish.getPubUseScore().intValue();
		}
		if(Publish.INSTRUCTION.equals(publish.getPubIsInstruction())
				&& publish.getPubRemarkScore() != null)
		{
			is = publish.getPubRemarkScore().intValue();
		}		
		tf = us + is + jf;
		
		TInfoBaseTotalScore ts = new TInfoBaseTotalScore();
		ts.setPubDate(publish.getPubDate());
		ts.setPubId(publish.getPubId());
		ts.setOrgId(publish.getOrgId());
		ts.setScore(BigDecimal.valueOf(tf));
		tsDao.save(ts);
		
	}
	
	@Transactional(readOnly = false)
	public String deleteByBs(String pubId) throws ServiceException,
			SystemException, DAOException
	{
		String flag = null;
		String a[] = pubId.split(",");
		int c = 0;
		for(int i=0;i<a.length;i++){
			TInfoBasePublish publish = getPublish(a[i]);
			if(Publish.SUBMITTED.equals(publish.getPubSubmitStatus()))
			{
				flag = "notDelete";
				return flag;
			}
			else
			{
				c =c+1;
				//publishDao.delete(a[i]);
				flag = "success";
			}
		}
		if(c==a.length){
			for(int i=0;i<a.length;i++){
			publishDao.delete(a[i]);
			}
		}
		/*TInfoBasePublish publish = getPublish(pubId);
		if(Publish.SUBMITTED.equals(publish.getPubSubmitStatus()))
		{
			flag = "notDelete";
		}
		else
		{
			publishDao.delete(pubId);
			flag = "success";
		}*/
		return flag;
	}

	@Transactional(readOnly = false)
	public String deleteByCb(String pubId, OALogInfo... loginfos) throws ServiceException,
			SystemException, DAOException
	{
		String flag = null;
		String a[] = pubId.split(",");
		int c = 0;
		for(int i=0;i<a.length;i++){
		TInfoBasePublish submit = getPublish(a[i]);
		if(submit.getPubUseStatus().equals(Publish.USED))
		{
			flag = "notDelete";
			return flag;
		}
		else
		{
			c =c+1;
			//publishDao.delete(pubId);
			//tsDao.delete(pubId);
			flag = "success";
		}
	}
		if(c==a.length){
			for(int i=0;i<a.length;i++){
			publishDao.delete(a[i]);
			//tsDao.delete(a[i]);
			}
		}
		return flag;
	}
	
	@Transactional(readOnly = false)
	public void setShare(String pubId, String isshare) throws ServiceException,
			SystemException, DAOException
	{
		TInfoBasePublish publish = getPublish(pubId);
		if(Publish.SHARED.equals(isshare))
		{
			publish.setPubIsShare(Publish.SHARED);
		}
		else if(Publish.NO_SHARED.equals(isshare))
		{
			publish.setPubIsShare(Publish.NO_SHARED);
		}
		savePublish(publish);
	}
	
	@Transactional(readOnly = false)
	public void markPreUse(String pubId, Boolean preUse)
			throws ServiceException, SystemException, DAOException
	{
		TInfoBasePublish publish = getPublish(pubId);
		if(preUse)
		{
			publish.setPubUseStatus(Publish.PRE_USED);
		}
		else
		{
			publish.setPubUseStatus(Publish.NO_USED);
		}
		savePublish(publish);
	}

	private int countInfo(Date date, String subject, String orgId)
	{
		Criterion cr1 = Restrictions.eq("pubSubmitStatus", Publish.SUBMITTED);
		//Criterion cr2 = Restrictions.eq("pubDate", date);
		Criterion cr3 = Restrictions.eq("pubTitle", subject);
		Criterion cr4 = Restrictions.eq("orgId", orgId);
		List<TInfoBasePublish> lists = publishDao.findByCriteria(
				new Criterion[]{cr1, cr3, cr4});
		
		return lists.size();
	}
		
	private Boolean isNotExistMailUid(String uid)
	{
		Boolean is = true;
		 List<TInfoBasePublish> rets = publishDao.findByProperty("mailUid", uid);
		 if(rets.size() > 0)
		 {
			 is = false;
		 }
		return is;
	}
	
	@Transactional(readOnly = false)
	public void saveMailInfo(MailInfo one) throws ServiceException,
			SystemException, DAOException
	{
		ApplicationContext ctx = SpringContextUtil.getApplicationContext();	
		IUserService userService = (IUserService) ctx.getBean("userService");
		
		String subject = one.getSubject();
		String orgId = one.getOrgId();
		String userId = one.getUserId();
		if(isNotExistMailUid(one.getMailUid()))
		{
			TInfoBasePublish publish = new TInfoBasePublish();
			publish.setPubInfoType(Publish.INFO_GENERAL);
			publish.setPubIsShare(Publish.NO_SHARED);
			publish.setPubUseStatus(Publish.NO_USED);
			publish.setPubIsText(Publish.TEXT_CONTENT);
			publish.setPubIsMailInfo(Publish.MAIL_INFO);
			publish.setPubSubmitStatus(Publish.SUBMITTED);
			publish.setPubUseStatus(Publish.NO_USED);
			publish.setPubIsComment(Publish.NO_COMMENTED);
			publish.setPubIsInstruction(Publish.NO_INSTRUCTION);
			publish.setPubDate(one.getDate());
			publish.setPubTitle(subject);	
			publish.setPubRawTitle(subject);
			publish.setMailUid(one.getMailUid());
			publish.setOrgId(orgId);
			publish.setPubPublisherId(userId);
			publish.setPubRawContent(one.getContent());
			publish.setPubEditContent(one.getContent());
			publish.setOrgName(userService.getOrgInfoByOrgId(orgId).getOrgName());
			TUumsBaseOrg org = userService.getOrgInfoByOrgId(publish.getOrgId());
			publish.setPubOrgType(org.getIsOrg());
							
			savePublish(publish);
			System.out.println("已保存：" + subject);
		}
	}

	@Transactional(readOnly = false)
	public void saveComment(String pubId, String comment)
			throws ServiceException, SystemException, DAOException
	{
		TInfoBasePublish publish = getPublish(pubId);
		if(!"".equals(comment))
		{
			publish.setPubIsComment(Publish.COMMENTED);
			publish.setPubComment(comment);
		}
		else
		{
			publish.setPubIsComment(Publish.NO_COMMENTED);
		}
		savePublish(publish);
	}

	public List<ScoreRankDto> scoreRank() throws ServiceException,
			SystemException, DAOException
	{
		ApplicationContext ctx = SpringContextUtil.getApplicationContext();
		IOrgService orgService = (IOrgService) ctx.getBean("orgService");

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
		String hql = "select orgId,sum(score) from TInfoBaseTotalScore" +
		 " where pubDate <= :eDate and pubDate >= :sDate "+
				" group by orgId order by sum(score) desc";
		List<?> srs = tsDao.getSession().createQuery(hql)
				.setMaxResults(7)
				.setDate("sDate", sDate)
				.setDate("eDate", eDate)
				.list();
		List<ScoreRankDto> rets = new ArrayList<ScoreRankDto>();
		Map<String, String> orgNames = orgService.getOrgNames();
		int index = 1;
		for(Object row : srs)
		{
			Object[] one = (Object[]) row;
			
			ScoreRankDto sr = new ScoreRankDto();
			sr.setOrgId((String)one[0]);
			sr.setOrgName(orgNames.get(one[0]));
			sr.setIndex(String.valueOf(index++));
			
			BigDecimal totalScore = (BigDecimal) one[1];
			sr.setTotalScore(totalScore.toString());
			
			rets.add(sr);
		}
		return rets;
	}

	public Boolean isExistByColId(String colId) throws ServiceException, SystemException,
			DAOException
	{
		Boolean is = false;
		Criterion cr2 = Restrictions.eq("TInfoBaseColumn.colId", colId);
		List<TInfoBasePublish> rets = publishDao.find(cr2);
		if(rets.size() > 0)
		{
			is = true;
		}
		return is;
	}

	public Boolean isExistByAptId(String aptId) throws ServiceException,
			SystemException, DAOException
	{
		Boolean is = false;
		Criterion cr2 = Restrictions.eq("TInfoBaseAppoint.aptId", aptId);
		List<TInfoBasePublish> rets = publishDao.find(cr2);
		if(rets.size() > 0)
		{
			is = true;
		}
		return is;
	}
	
	public TInfoBasePublishUser getPU(String pubId, String userId)
		throws ServiceException, SystemException, DAOException
		{
		Criterion cr1 = Restrictions.eq("pubId", pubId);
		Criterion cr2 = Restrictions.eq("userId", userId);
		TInfoBasePublishUser ret = null;
		List<TInfoBasePublishUser> rets = 
			publishUserDao.find(new Criterion[]{cr1, cr2});
		if(rets.size() > 0)
		{
			ret = rets.get(0);
		}
		return ret;
	}

	public Boolean isRead(String pubId, String userId) throws ServiceException,
		SystemException, DAOException
		{
		Boolean isRead = false;
		TInfoBasePublishUser pu = getPU(pubId, userId);
		if(pu != null)
		{
			isRead = true;
		}
		return isRead;
		}

	@Transactional(readOnly = false)
	public void savePU(String pubId, String userId) throws ServiceException,
		SystemException, DAOException
	{
		deletePU(pubId, userId);
		TInfoBasePublishUser pu = new TInfoBasePublishUser();
		pu.setPubId(pubId);
		pu.setUserId(userId);
		publishUserDao.save(pu);
	}

	@Transactional(readOnly = false)
	public String deletePU(String puId, String userId) throws ServiceException,
		SystemException, DAOException
	{
		String flag = null;
		String a[] = puId.split(",");
		int c = 0;
		for(int i=0;i<a.length;i++){
		TInfoBasePublish submit = getPublish(a[i]);
		if(submit.getPubUseStatus().equals(Publish.USED))
		{
			flag = "notDelete";
			return flag;
		}
		else
		{
			c =c+1;
			//publishDao.delete(pubId);
			//tsDao.delete(pubId);
			flag = "success";
		}
	}
		if(c==a.length){
			for(int i=0;i<a.length;i++){
				TInfoBasePublishUser ret = getPU(a[i], userId);
				if(ret != null)
				{
				publishUserDao.delete(ret);
				}
			}
		}
		return flag;
	}
	
	public List<TInfoBasePublish> findPublishDate()
	throws ServiceException,SystemException, DAOException{
		/*String hql ="select distinct t.pubSubmitDate from TInfoBasePublish as t where t.pubUseStatus=1 order by t.pubSubmitDate desc";
		List<?> srs = publishUserDao.getSession().createQuery(hql)
		.setMaxResults(7)
		.list();*/
		@SuppressWarnings("unchecked")
		List<TInfoBasePublish> ret = publishDao.getSession()
				.createCriteria(TInfoBasePublish.class)
				.add(Restrictions.eq("pubUseStatus", Publish.USED))
				.addOrder(Order.desc("pubSubmitDate"))
				.setMaxResults(10)
				.list();
		return ret;
		//Criterion criterion = Restrictions.eq("pubUseStatus", Publish.USED);
		//return publishDao.findByCriteria(criterion);
		
	}

	@SuppressWarnings("unchecked")
	public List<TInfoBasePublish> findPublishedByDate(Date startDate,
			Date endDate) throws ServiceException, SystemException,
			DAOException
	{
		List<TInfoBasePublish> pubs = new ArrayList<TInfoBasePublish>();
		
		List<TInfoBaseIssue> iss = issueDao.findByProperty("issIsPublish", Publish.PUBLISHED);		
		String[] issIds = new String[iss.size()];
		int i = 0;
		for(TInfoBaseIssue one : iss)
		{
			issIds[i] = one.getIssId();
			i++;
		}
		
		if(iss.size() > 0)
		{
			Criterion cr1 = Restrictions.in("TInfoBaseIssue.issId", issIds);

			Date sDate = DateTimes.date2DateTime(startDate);
			Date eDate = DateTimes.date2DateTime(endDate, true);
			Criterion cr2 = Restrictions.between("pubSubmitDate", sDate, eDate);
			
			pubs = publishDao.getSession()
					.createCriteria(TInfoBasePublish.class)
					.add(cr1)
					.add(cr2)
					.addOrder(Order.desc("orgId"))
					.addOrder(Order.desc("pubSubmitDate"))
					.list();			
		}
		
		return pubs;
	}

	public Map<String, BigDecimal> getPlusScore(List<TInfoBasePublish> publishs)
			throws ServiceException, SystemException, DAOException
	{
		Map<String, BigDecimal> ps = new HashMap<String, BigDecimal>();
		String[] pubIds = new String[publishs.size()];
		int i = 0;
		for(TInfoBasePublish one : publishs)
		{
			pubIds[i++] = one.getPubId();
		}

		if(publishs.size() > 0)
		{
			Criterion cr1 = Restrictions.in("pubId", pubIds);
			List<TInfoBaseTotalScore> ts = tsDao.find(cr1);
			for(TInfoBaseTotalScore row : ts)
			{
				ps.put(row.getPubId(), row.getScore());
			}
		}

		return ps;
	}

	public Map<String, BigDecimal> getOrgScore(List<TInfoBasePublish> publishs)
			throws ServiceException, SystemException, DAOException
	{
		Map<String, BigDecimal> ps = new HashMap<String, BigDecimal>();
		String[] pubIds = new String[publishs.size()];
		int i = 0;
		for(TInfoBasePublish one : publishs)
		{
			pubIds[i] = one.getPubId();
			i++;
		}

		if(publishs.size() > 0)
		{
			StringBuilder hql = new StringBuilder();
			hql.append("select orgId,sum(score) as total ");
			hql.append("from TInfoBaseTotalScore ");
			hql.append("where pubId in (:pubIds) ");
			hql.append("group by orgId");
			Query query = publishDao.getSession().createQuery(hql.toString());
			List<?> ts = query.setParameterList("pubIds", pubIds).list();

			for(Object row : ts)
			{
				Object[] obj = (Object[]) row;
				ps.put((String) obj[0], (BigDecimal) obj[1]);
			}
		}

		return ps;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TInfoBaseComment> findComment(String pubId) throws ServiceException, SystemException,
			DAOException
	{
		
		Criterion cr = Restrictions.eq("comPublishId", pubId);
		List<TInfoBaseComment> rets = commentDao.getSession()
										.createCriteria(TInfoBaseComment.class)
										.add(cr)
										.addOrder(Order.desc("comDate"))
										.list();
		return rets;
	}
	
	@Transactional(readOnly = false)
	public void saveComment(TInfoBaseComment comment) throws ServiceException, SystemException,
			DAOException
	{
		commentDao.save(comment);
	}
	
	
	@Transactional(readOnly = false)
	public void delectComment(TInfoBaseComment comment) throws ServiceException, SystemException,
			DAOException
	{
		commentDao.delete(comment);
	}
	
	public TInfoBaseComment findComment1(String comId) throws ServiceException, SystemException,
		DAOException
	{
		Criterion cr = Restrictions.eq("comId", comId);
		List<TInfoBaseComment> rets = commentDao.findByCriteria(cr);
		return rets.get(0);
	}
	
	public List<TInfoBasePublishUser> getPUList(String userId)
	throws ServiceException,SystemException, DAOException{
		Criterion cr = Restrictions.eq("userId", userId);
		List<TInfoBasePublishUser> rets = publishUserDao.findByCriteria(cr);
		return rets;
	}
	
	
	@Transactional(readOnly = false)
	public void updatePublish(TInfoBasePublish pub)
			throws ServiceException, SystemException, DAOException
	{
		publishDao.update(pub);
	}
	
	public List<TInfoBasePublish> getPublishBymerge(String flag)
	throws ServiceException,SystemException, DAOException{
		Criterion cr = Restrictions.eq("pubMergeFlag", flag);
		List<TInfoBasePublish> rets = publishDao.findByCriteria(cr);
		return rets;
	}
	
	
	public Page<TInfoBasePublish> getPublishBymerge1(Page page,String flag)
	throws ServiceException,SystemException, DAOException{
		Criterion cr = Restrictions.eq("pubMergeFlag", flag);
		page = publishDao.findByCriteria(page, cr);
		return page;
	}
	
	public TInfoBasePublish getPublishBymergeFlag(String flag)
	throws ServiceException,SystemException, DAOException{
		Criterion cr1 = Restrictions.eq("pubMergeFlag", flag);
		Criterion cr2 = Restrictions.eq("pubIsMerge", "1");
		List<TInfoBasePublish> rets = 
			publishDao.find(new Criterion[]{cr1, cr2});
		return rets.get(0);
	}
	
	/*查询最大排序号的文章列表
	 * (non-Javadoc)
	 * @see com.strongit.xxbs.service.IPublishService#getPublishOrdersort(java.lang.String)
	 */
	public List<?> getPublishOrdersort(String colId,String issId)
	throws ServiceException,SystemException, DAOException{
		String sql = "SELECT MAX(P.PUB_SORT) FROM T_INFO_BASE_PUBLISH P WHERE   P.PUB_SORT IS NOT NULL AND P.COL_ID='"+colId+"' AND P.ISS_ID='"+issId+"' ORDER BY PUB_SORT DESC";
		Query query = publishDao.getSession().createSQLQuery(sql);
		List dayLists = query
		.list();
		return dayLists;
	}
	/*
	 * 获得比当前文章排序号大的所有文章按升序排列
	 * 
	 */
	public List<?> getPublishup(int sort,String colId,String issId)
	throws ServiceException,SystemException, DAOException{
		String sql = "SELECT P.PUB_ID FROM  T_INFO_BASE_PUBLISH P WHERE   P.PUB_SORT IS NOT NULL AND P.COL_ID='"+colId+"' AND P.ISS_ID='"+issId+"' AND PUB_SORT>"+sort+" ORDER BY PUB_SORT ASC";
		Query query = publishDao.getSession().createSQLQuery(sql);
		List dayLists = query
		.list();
		return dayLists;
	}
	
	/*
	 * 获得比当前文章排序号小的所有文章按降序排列
	 * 
	 */
	public List<?> getPublishdown(int sort,String colId,String issId)
	throws ServiceException,SystemException, DAOException{
		String sql = "SELECT P.PUB_ID FROM  T_INFO_BASE_PUBLISH P WHERE   P.PUB_SORT IS NOT NULL AND P.COL_ID='"+colId+"' AND P.ISS_ID='"+issId+"' AND PUB_SORT<"+sort+" ORDER BY PUB_SORT DESC";
		Query query = publishDao.getSession().createSQLQuery(sql);
		List dayLists = query
		.list();
		return dayLists;
	}
	
	/*
	 * 获得前一篇文章
	 * 
	 */
	public String getPrePublish(String date)
	throws ServiceException,SystemException, DAOException{
		String sql = "SELECT PUB_ID FROM  T_INFO_BASE_PUBLISH P  WHERE P.PUB_DATE>TO_TIMESTAMP('"+date+"','yyyy-mm-dd hh24:mi:ss.ff')  AND (P.PUB_IS_MERGE!=0 OR P.PUB_IS_MERGE IS NULL) ORDER BY PUB_DATE ASC";
		Query query = publishDao.getSession().createSQLQuery(sql);
		List dayLists = query
		.list();
		if(dayLists.size()>0){
		return dayLists.get(0).toString();
		}
		else{
			return null;
		}
	}
	
	/*
	 * 获得后一篇文章
	 * 
	 */
	public String getNextPublish(String date)
	throws ServiceException,SystemException, DAOException{
		String sql = "SELECT PUB_ID FROM  T_INFO_BASE_PUBLISH P  WHERE P.PUB_DATE<TO_TIMESTAMP('"+date+"','yyyy-mm-dd hh24:mi:ss.ff')  AND (P.PUB_IS_MERGE!=0 OR P.PUB_IS_MERGE IS NULL) ORDER BY PUB_DATE DESC";
		Query query = publishDao.getSession().createSQLQuery(sql);
		List dayLists = query
		.list();
		if(dayLists.size()>0){
			return dayLists.get(0).toString();
			}
			else{
				return null;
			}
	}
	

	/*
	 * 批处理得到所有文章ID
	 * 
	 */
	public List getPublishId()
	throws ServiceException,SystemException, DAOException{
		String sql = "SELECT PUB_ID FROM  T_INFO_BASE_PUBLISH P WHERE P.ORG_NAME IS NULL ORDER BY PUB_DATE DESC ";
		Query query = publishDao.getSession().createSQLQuery(sql);
		List dayLists = query
		.list();
		
		return dayLists;
	}
	
	/*
	 * 批处理得到所有文章ID
	 * 
	 */
	public List getMerPublishId()
	throws ServiceException,SystemException, DAOException{
		String sql = "SELECT PUB_ID FROM  T_INFO_BASE_PUBLISH P WHERE P.PUB_MERGE_ORG IS NOT NULL AND P.PUB_MERGE_NAME IS NULL";
		Query query = publishDao.getSession().createSQLQuery(sql);
		List dayLists = query
		.list();
		
		return dayLists;
	}
	
}
