package com.strongit.xxbs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.xxbs.entity.TInfoBaseColumn;
import com.strongit.xxbs.service.IColumnService;
import com.strongit.xxbs.service.IPublishService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.util.SpringContextUtil;

@Service
@Transactional(readOnly = true)
public class ColumnService implements IColumnService
{
	private GenericDAOHibernate<TInfoBaseColumn, String> colDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		colDao = new GenericDAOHibernate<TInfoBaseColumn, String>(
				sessionFactory, TInfoBaseColumn.class);
	}

	public TInfoBaseColumn getColumn(String colId) throws ServiceException,
			SystemException, DAOException
	{
		return colDao.get(colId);
	}

	public List<TInfoBaseColumn> getColumns() throws ServiceException,
			SystemException, DAOException
	{
		return colDao.findAll();
	}

	public Page<TInfoBaseColumn> getColumns(Page<TInfoBaseColumn> page)
			throws ServiceException, SystemException, DAOException
	{
		return colDao.findAll(page);
	}

	public Page<TInfoBaseColumn> getColumns(Page<TInfoBaseColumn> page,
			String jourId) throws ServiceException, SystemException,
			DAOException
	{
		Criterion cr = Restrictions.eq("TInfoBaseJournal.jourId", jourId);		
		return colDao.findByCriteria(page, cr);
	}

	@Transactional(readOnly = false)
	public void saveColumn(TInfoBaseColumn column) throws ServiceException,
			SystemException, DAOException
	{
		colDao.save(column);
	}

	@Transactional(readOnly = false)
	public String deleteColumn(String colId) throws ServiceException,
			SystemException, DAOException
	{
		String flag = null;
		String a[] = colId.split(",");
		int c = 0;
		ApplicationContext ctx = SpringContextUtil.getApplicationContext();
		IPublishService publishService = (IPublishService) ctx.getBean("publishService");
		for(int i=0;i<a.length;i++){
			if(publishService.isExistByColId(a[i]))
			{
				flag = "noDelete";
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
			colDao.delete(a[i]);
			}
		}
		return flag;
	}

	public Page<TInfoBaseColumn> findColumnsByName(Page<TInfoBaseColumn> page,
			String colName) throws ServiceException, SystemException,
			DAOException
	{
		Criterion criterion = Restrictions.like("colName", "%"+colName+"%");
		return colDao.findByCriteria(page, criterion);
	}
	
	//根据栏目名称和期刊ID找到栏目
	public Page<TInfoBaseColumn> findColumnsByNameAndtoId(Page<TInfoBaseColumn> page,
			String colName , String toId) throws ServiceException, SystemException,
			DAOException
	{
		List<Criterion> crs = new ArrayList<Criterion>();
		Criterion criterion1 = Restrictions.like("colName", "%"+colName+"%");
		crs.add(criterion1);
		if(!toId.equals("all")){
		Criterion criterion2 = Restrictions.eq("TInfoBaseJournal.jourId", toId);
		crs.add(criterion2);
		}
		return colDao.findByCriteria(page, crs.toArray(new Criterion[crs.size()]));
	}

	public Boolean isExistByJourId(String jourId) throws ServiceException,
			SystemException, DAOException
	{
		Boolean is = false;
		Criterion cr2 = Restrictions.eq("TInfoBaseJournal.jourId", jourId);
		List<TInfoBaseColumn> rets = colDao.find(cr2);
		if(rets.size() > 0)
		{
			is = true;
		}
		return is;
	}
	
	public String getColumnSort()
	throws ServiceException,SystemException, DAOException
	{
		
		String hql = "select max(colSort) as sort from TInfoBaseColumn";
		Query query = colDao.getSession().createQuery(hql);
		List dayLists = new ArrayList();
		dayLists = query.list();
		if(dayLists.get(0)!=null){
		String sort = (String)dayLists.get(0);	
		int sort1 = Integer.parseInt(sort);
		if(sort.equals("")||sort.equals(null)){
			sort1=1;
		}
		else{
			sort1 = sort1+1;
		}
		return sort1+"";
		}
		else{
			return "1";
		}
		
	}
	
	public Boolean isExistSore(String sore) throws ServiceException,
	SystemException, DAOException
		{
		Boolean isExist = false;
		Criterion cr = Restrictions.eq("colSort", sore);
		List<TInfoBaseColumn> rets = colDao.findByCriteria(cr);
		if(rets != null && rets.size() > 0)
		{
			isExist = true;
		}
		return isExist;
		}

}
