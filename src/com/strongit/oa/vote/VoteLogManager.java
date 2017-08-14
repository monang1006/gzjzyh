package com.strongit.oa.vote;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TOaVoteLog;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author piyu
 * Jun 21, 2010
 *
 */
@Service
@Transactional
public class VoteLogManager {
	private GenericDAOHibernate<TOaVoteLog, String> logDao;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.logDao = new GenericDAOHibernate(sessionFactory,
				TOaVoteLog.class);
	}
	/**
	 * 用户或手机号是否已经参与过投票
	 */
	@Transactional(readOnly=true)
	public boolean existLog(String vid,String userid,String mobile){
		StringBuffer hql=new StringBuffer("select count(*) from TOaVoteLog log where log.vote.vid=? ");
		Object[]params=new Object[2];
		params[0]=vid;
		if(userid!=null&&userid.length()>0){
			hql.append(" and log.userid=? ");
			params[1]=userid;
		}else{
			hql.append(" and log.mobile=? ");
			params[1]=mobile;
		}
		
		if(Integer.parseInt(logDao.createQuery(hql.toString(),params).list().get(0).toString())>0){
			return true;
		}else{		
			return false;
		}
	}
	/**
	 * 插入投票记录
	 */
	public void addLog(TOaVoteLog log){
		try {
			logDao.insert(log);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("find.error", new Object[] { "新建记录" });
		}
	}
	/**
	 * 分页查询问卷的参与记录
	 */
	@Transactional(readOnly=true)
	public Page<TOaVoteLog> getVoteLogPage(Page<TOaVoteLog> page,String hql,Object[] params){
		try {
			page=logDao.find(page, hql, params);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("find.error", new Object[] { "问卷参与记录查询" });
		}
		return page;
	}
}
