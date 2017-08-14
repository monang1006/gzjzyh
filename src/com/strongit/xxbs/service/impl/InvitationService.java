package com.strongit.xxbs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.utils.DateTimes;
import com.strongit.xxbs.common.contant.Info;
import com.strongit.xxbs.common.contant.Publish;
import com.strongit.xxbs.entity.TInfoBaseAppoint;
import com.strongit.xxbs.service.IAppointToService;
import com.strongit.xxbs.service.IInvitationService;
import com.strongit.xxbs.service.IPublishService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.util.SpringContextUtil;

import edu.emory.mathcs.backport.java.util.Arrays;

@Service
@Transactional(readOnly = true)
public class InvitationService implements IInvitationService
{
	private 	ApplicationContext ctx = SpringContextUtil.getApplicationContext();

	private GenericDAOHibernate<TInfoBaseAppoint, String> appointDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		appointDao = new GenericDAOHibernate<TInfoBaseAppoint, String>(
				sessionFactory, TInfoBaseAppoint.class);
	}
	
	public TInfoBaseAppoint getAppoint(String aptId) throws ServiceException,
			SystemException, DAOException
	{
		return appointDao.get(aptId);
	}

	public List<TInfoBaseAppoint> getAppoints() throws ServiceException,
			SystemException, DAOException
	{
		return appointDao.findAll();
	}

	public Page<TInfoBaseAppoint> getAppoints(Page<TInfoBaseAppoint> page)
			throws ServiceException, SystemException, DAOException
	{
		return appointDao.findAll(page);
	}

	public Page<TInfoBaseAppoint> getAppoints(Page<TInfoBaseAppoint> page,
			String orgId, Boolean isSinceToday) throws ServiceException, SystemException,
			DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		Date sdate = new Date();
		Criterion cr1 = Restrictions.ge("aptDuedate", sdate);
		crs.add(cr1);
		Criterion cr4 = Restrictions.le("aptDate", sdate);
		crs.add(cr4);
		
		Criterion cr2 = Restrictions.eq("aptIsAllOrg", Publish.ALL_ORG);
		
		IAppointToService appointToService = (IAppointToService) ctx.getBean("appointToService");
		String[] ids = appointToService.getAptIdsByOrgid(orgId);
		
		Criterion cr = null;
		if(ids.length == 0)
		{
			 cr = cr2;
		}
		else
		{
			Criterion cr3 = Restrictions.in("aptId", ids);
			 cr = Restrictions.or(cr2, cr3);
		}
			
		
		
		
		crs.add(cr);
		
		return appointDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public void saveAppoint(TInfoBaseAppoint appoint, String[] orgIds)
			throws ServiceException, SystemException, DAOException
	{
		saveAppoint(appoint);
		if(orgIds != null && orgIds.length > 0)
		{
			IAppointToService appointToService = (IAppointToService) ctx.getBean("appointToService");
			appointToService.saveATs(appoint.getAptId(), Arrays.asList(orgIds));
		}
	}

	@Transactional(readOnly = false)
	public void saveAppoint(TInfoBaseAppoint appoint) throws ServiceException,
			SystemException, DAOException
	{
		appointDao.save(appoint);
		IAppointToService appointToService = (IAppointToService) ctx.getBean("appointToService");
		appointToService.deleteATs(appoint.getAptId());
	}

	@Transactional(readOnly = false)
	public String deleteAppoint(String aptId) throws ServiceException,
			SystemException, DAOException
	{
		String flag = null;
		String a[] = aptId.split(",");
		int c = 0;
		IAppointToService appointToService = (IAppointToService) ctx.getBean("appointToService");
		IPublishService publishService = (IPublishService) ctx.getBean("publishService");
		for(int i=0;i<a.length;i++){
		if(publishService.isExistByAptId(a[i]))
		{
			flag = "notDelete";
			return flag;
		}
		else
		{
			c =c+1;
			//appointDao.delete(aptId);
			//appointToService.deleteATs(aptId);
			flag = Info.SUCCESS;
		}
	}
		if(c==a.length){
			for(int i=0;i<a.length;i++){
				appointDao.delete(a[i]);
				appointToService.deleteATs(a[i]);
			}
		}
		return flag;
	}

	public Page<TInfoBaseAppoint> findAppoints(Page<TInfoBaseAppoint> page,
			Map<String, String> search) throws ServiceException,
			SystemException, DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
				
		Criterion cr = Restrictions.like("aptTitle", "%"+search.get("aptTitle")+"%");
		crs.add(cr);
		
		String startDate = search.get("aptDate");
		String endDate = search.get("aptDuedate");
		if(!"".equals(startDate) && !"".equals(endDate))
		{
			Date sDate = DateTimes.ymd2Date(startDate);
			Date eDate = DateTimes.ymd2Date(endDate);
			Criterion cr2 = Restrictions.ge("aptDate", sDate);
			Criterion cr3 = Restrictions.le("aptDuedate", eDate);
			crs.add(cr2);
			crs.add(cr3);
		}
		return appointDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
	}

	public List<TInfoBaseAppoint> lastAppoints() throws ServiceException,
			SystemException, DAOException
	{
		@SuppressWarnings("unchecked")
		List<TInfoBaseAppoint> ret = appointDao.getSession()
				.createCriteria(TInfoBaseAppoint.class)
				.addOrder(Order.desc("aptDate"))
				.setMaxResults(8)
				.list();
		
		return ret;
	}

	public List<TInfoBaseAppoint> lastAppoints(String orgId)
			throws ServiceException, SystemException, DAOException
	{
		Page<TInfoBaseAppoint> page = new Page<TInfoBaseAppoint>(1, true);
		page.setPageNo(1);
		page.setPageSize(8);
		page.setOrder("desc");
		page.setOrderBy("aptDate");
		page = getAppoints(page, orgId, true);
		return page.getResult();
	}
	
}
