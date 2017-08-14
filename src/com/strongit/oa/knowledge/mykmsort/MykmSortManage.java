/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-4-25
 * Autour: hull
 * Version: V1.0
 * Description： 知识类型manager
 */
package com.strongit.oa.knowledge.mykmsort;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaKnowledgeMykm;
import com.strongit.oa.bo.ToaKnowledgeMykmSort;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class MykmSortManage {

	/** 知识类型DAO * */
	public GenericDAOHibernate<ToaKnowledgeMykmSort, String> mykmSortDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		mykmSortDAO = new GenericDAOHibernate<ToaKnowledgeMykmSort, String>(
				sessionFactory, ToaKnowledgeMykmSort.class);
	}

	/**
	 * 添加知识类型
	 * 
	 * @param mykmSort
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveMykmSort(ToaKnowledgeMykmSort mykmSort)
	throws SystemException, ServiceException {
		try {
			mykmSortDAO.save(mykmSort);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "我的知识类型添加" });
			// e.printStackTrace();

		}
	}

	/**
	 * 删除知识类型
	 * 
	 * @param sortID
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteMykmSort(String sortID) throws SystemException,
	ServiceException {
		try {
			mykmSortDAO.delete(sortID);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "我的知识类型删除" });
		}
	}

	/**
	 * 根据ID查看知识类型
	 * 
	 * @param sortID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaKnowledgeMykmSort getMykmSortByID(String sortID)
	throws SystemException, ServiceException {
		try {
			return mykmSortDAO.get(sortID);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "查看知识类型" });
		}
	}

	/**
	 * 查看所有的知识类型
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaKnowledgeMykmSort> getAllMykmSort(
			Page<ToaKnowledgeMykmSort> page) throws SystemException,
			ServiceException {
		try {
			return mykmSortDAO.findAll(page);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "查看所有的知识类型" });
		}
	}

	/**
	 * 根据属性查询
	 * 
	 * @param page
	 * @param name
	 * @param user
	 * @return
	 */
	public Page<ToaKnowledgeMykmSort> getMykmSortByName(
			Page<ToaKnowledgeMykmSort> page, String name, String user)
			throws SystemException, ServiceException {
		try {
			String sql = "from ToaKnowledgeMykmSort sort where sort.mykmSortName=? and sort.mykmSortUser=?";
			Object[] obj = { name, user };
			return mykmSortDAO.find(page, sql, obj);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "属性查询知识类型" });
		}
	}

	/**
	 * 根据用户查询
	 * 
	 * @param page
	 * @param user
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaKnowledgeMykmSort> getMykmSortByUser(
			Page<ToaKnowledgeMykmSort> page, String user)
			throws SystemException, ServiceException {
		try {

			String sql = "from ToaKnowledgeMykmSort sort where sort.mykmSortUser=?";
			Object[] obj = { user };
			return mykmSortDAO.find(page, sql, obj);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "根据用户查询" });
		}
	}

	/**
	 * 根据用户查询
	 * 
	 * @param user
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaKnowledgeMykmSort> getMykmSortListByUser(String user)
	throws SystemException, ServiceException {
		try {

			String sql = "from ToaKnowledgeMykmSort sort where sort.mykmSortUser=?";
			return mykmSortDAO.find(sql, user);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "根据用户查询" });
		}
	}
}
