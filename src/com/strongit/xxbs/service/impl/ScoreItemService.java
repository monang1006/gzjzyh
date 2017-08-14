package com.strongit.xxbs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.entity.TInfoBaseColumn;
import com.strongit.xxbs.entity.TInfoBaseScore;
import com.strongit.xxbs.service.IPSService;
import com.strongit.xxbs.service.IScoreItemService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.util.SpringContextUtil;

@Service
@Transactional(readOnly = true)
public class ScoreItemService implements IScoreItemService
{
	private GenericDAOHibernate<TInfoBaseScore, String> scoreDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		scoreDao = new GenericDAOHibernate<TInfoBaseScore, String>(
				sessionFactory, TInfoBaseScore.class);
	}

	public TInfoBaseScore getScoreItem(String scId) throws ServiceException,
			SystemException, DAOException
	{
		return scoreDao.get(scId);
	}

	public List<TInfoBaseScore> getScoreItems() throws ServiceException,
			SystemException, DAOException
	{
		return scoreDao.findAll();
	}

	public List<TInfoBaseScore> getScoreItems(String[] scIds)
			throws ServiceException, SystemException, DAOException
	{
		Criterion cr = Restrictions.in("scId", scIds);
		return scoreDao.find(cr);
	}

	public Page<TInfoBaseScore> getScoreItems(Page<TInfoBaseScore> page)
			throws ServiceException, SystemException, DAOException
	{
		return scoreDao.findAll(page);
	}

	@Transactional(readOnly = false)
	public void saveScoreItem(TInfoBaseScore item) throws ServiceException,
			SystemException, DAOException
	{
		scoreDao.save(item);
	}

	@Transactional(readOnly = false)
	public String deleteScoreItem(String scId) throws ServiceException,
			SystemException, DAOException
	{
		String flag = null;
		String a[] = scId.split(",");
		int c = 0;
		ApplicationContext ctx = SpringContextUtil.getApplicationContext();
		IPSService PSService = (IPSService) ctx.getBean("PSService");
		for(int i=0;i<a.length;i++){
		if(PSService.isExistByScId(a[i]))
		{
			flag = "noDelete";
			return flag;
		}
		else
		{
			c =c+1;
			//scoreDao.delete(scId);
			flag = "success";
		}
	}
		if(c==a.length){
			for(int i=0;i<a.length;i++){
			scoreDao.delete(a[i]);
			}
		}
		return flag;
	}
	
	public Page<TInfoBaseScore> findScoreItem(Page<TInfoBaseScore> page,
			String scoreName)
			throws ServiceException,SystemException, DAOException
	{
		Criterion cr1 = Restrictions.like("scName", "%"+scoreName+"%");
		return scoreDao.findByCriteria(page, cr1);
	}

}
