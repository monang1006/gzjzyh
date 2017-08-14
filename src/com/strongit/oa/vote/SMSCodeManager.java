package com.strongit.oa.vote;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TOaVoteSMS;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * @author piyu
 * Jun 22, 2010
 * 短信问题使用的编号
 */
@Service
@Transactional
public class SMSCodeManager {
	private GenericDAOHibernate<TOaVoteSMS, String> smscodeDao;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.smscodeDao = new GenericDAOHibernate(sessionFactory,
				TOaVoteSMS.class);
	}
	/**
	 * 保存短信问题的编号对象
	 */
	public void addSmsCode(TOaVoteSMS smscode){
		try {
			smscodeDao.insert(smscode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("find.error", new Object[] { "添加问题编号" });
		}
	}
	/**
	 * 删除短信问题的编号对象
	 */
	public void delSmsCode(String codes){
		if(codes==null){
			return ;
		}
		try {
			smscodeDao.delete(codes.split(","));
		} catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("find.error", new Object[] { "删除问题编号" });
		}
	}
	/**
	 * 获取短信问题的编号对象
	 */
	public TOaVoteSMS getByCode(String code){
		TOaVoteSMS smscode=null;
		try {
			smscode=smscodeDao.get(code);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("find.error", new Object[] { "获取问题编号" });
		}
		return smscode;
	}
	/**
	 * 获取短信问题的编号对象
	 */
	public TOaVoteSMS getByQid(String qid){
		TOaVoteSMS smscode=null;
		String hql="from TOaVoteSMS smscode where smscode.qid=?";
		Object[] params=new Object[]{qid};
		try {
			List result=smscodeDao.createQuery(hql, params).list();
			if(result!=null&&result.size()>0){
				smscode=(TOaVoteSMS)result.get(0);
			}
		}  catch (DAOException e) {
			e.printStackTrace();
			throw new ServiceException("find.error", new Object[] { "获取问题编号" });
		}
		return smscode;
	}
	
}
