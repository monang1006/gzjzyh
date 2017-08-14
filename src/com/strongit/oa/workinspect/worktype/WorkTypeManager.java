package com.strongit.oa.workinspect.worktype;

import javax.jcr.Session;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TOsWorkType;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author       qibh
 * @company      Strongit Ltd. (C) copyright
 * @date         2013年5月2日20:01:03
 * @version      1.0.0.0
 * @comment      工作分类Manager
 */
@Service
@Transactional
public class WorkTypeManager{
	
private GenericDAOHibernate<TOsWorkType, String> typeDao;
	
	/**
	 * @param sessionFactory
	 * @roseuid 495040F7029F
	 */
	
	@Autowired
	public void setSessioinFactory(SessionFactory sessionFactory) {
		typeDao = new GenericDAOHibernate<TOsWorkType, String> (sessionFactory,TOsWorkType.class);
	}
	
	/**
	 * 根据查询条件获取所有分类
	 * @author: qibh
	 *@return
	 * @created: 2013-6-3 上午03:04:25
	 * @version :5.0
	 */
	public Page<TOsWorkType> getAllType(Page<TOsWorkType> page,TOsWorkType model){
		try {
			StringBuffer  queryStr = new StringBuffer("from TOsWorkType as t where 1=1  ");
			if(model.getWorktypeValue()!=null&&!"".equals(model.getWorktypeValue())){
				queryStr.append(" and t.worktypeValue like  '%"+model.getWorktypeValue()+"%' ");
			}
			if(model.getWorktypeName()!=null&&!"".equals(model.getWorktypeName())){
				queryStr.append(" and t.worktypeName like  '%"+model.getWorktypeName()+"%' ");
			}
			if(model.getWorktypeDemo()!=null&&!"".equals(model.getWorktypeDemo())){
				queryStr.append(" and t.worktypeDemo like  '%"+model.getWorktypeDemo()+"%' ");
			}
			return typeDao.find(page, queryStr.toString()+" order by t.worktypeSequence ");
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"工作分类"});
		}
	}
	
	/**
	 * 保存工作分类
	 * @author: qibh
	 *@return
	 * @created: 2013-6-3 上午03:04:25
	 * @version :5.0
	 */
	public void saveType(TOsWorkType model){
		model.setWorktypeValue(model.getWorktypeName());
		typeDao.save(model);
	}
	public void updateType(TOsWorkType model){
//		String hql="update from TOsWorkType set t where WORKTYPE_ID=?";
//		Query query=typeDao.createQuery(hql, worktypeId);
//		query.executeUpdate();
		typeDao.update(model);
	}

	/**
	 * 根据分类ID获取相应的分类
	 * @author: qibh
	 *@return
	 * @created: 2013-6-3 上午03:04:25
	 * @version :5.0
	 */
	public TOsWorkType fingById(String worktypeId){
		TOsWorkType  model=new TOsWorkType();
		StringBuffer  queryStr = new StringBuffer("from TOsWorkType as t where WORKTYPE_ID="+worktypeId);
		model=typeDao.findById(worktypeId,false);
		return model;
	}
	/**
	 * 删除工作分类
	 * @author: qibh
	 *@return
	 * @created: 2013-6-3 上午03:04:25
	 * @version :5.0
	 */
	public boolean delete(String worktypeId){
		String hql="delete from TOsWorkType t where WORKTYPE_ID=?";
		Query query=typeDao.createQuery(hql, worktypeId);
		query.executeUpdate();
		return true;
	}
}