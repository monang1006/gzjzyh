package com.strongit.doc.receives;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.informix.util.stringUtil;
import com.strongit.doc.bo.TdocSend;
import com.strongit.doc.bo.TtransDoc;
import com.strongit.doc.bo.TtransDocAttach;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.IGenericDAO;

/**
 * 公文服务类.
 * 
 * @author qt
 * @company Strongit Ltd. (C) copyright
 * @date
 * @version 2.0.2.3
 * @classpath
 * @comment
 * @email
 */
@Service
@Transactional
public class RecvTDocManager {

	IGenericDAO<TtransDoc, java.lang.String> docDao = null; // 定义DAO操作类.
	IGenericDAO<TdocSend, java.lang.String> docSendDao = null; // 定义DAO操作类.
	private GenericDAOHibernate<TtransDocAttach, java.lang.String> attachDao;

	/**
	 * 注入SESSION工厂
	 * 
	 * @author:
	 * @date:2010-7-7 下午04:54:30
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		docDao = new GenericDAOHibernate<TtransDoc, java.lang.String>(
				sessionFactory, TtransDoc.class);
		attachDao = new GenericDAOHibernate<TtransDocAttach, java.lang.String>(sessionFactory,
				TtransDocAttach.class);
		docSendDao = new GenericDAOHibernate<TdocSend, java.lang.String>(sessionFactory,
				TdocSend.class);
	}

	/**
	 * 根据公文id得到公文信息
	 * @author:qt
	 * @date:2010-8-11 上午10:16:59
	 * @param id				公文主键id
	 * @return					公文信息
	 * @throws ServiceException
	 */
	TtransDoc getDocById(String id) throws ServiceException {
		return docDao.get(id);
	}

	public List<TdocSend> getAllInfoList (String id) throws ServiceException {

		String hql = "select t.deptName from TdocSend t where t.docId = ?";
		return docSendDao.find(hql, id);
	}
	
	
	public TtransDocAttach getDocAttachById(String attachId) throws SystemException,ServiceException{
		try {
			TtransDocAttach archiveAttach= attachDao.get(attachId);
			return archiveAttach;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"公文附件Id查找附件"});
		}
	}
	 /*
	  * 删除公文中选择的附件
	  * */
	public String delDocAttachById(String attachId) throws SystemException,ServiceException{
		try {
	     	attachDao.delete(attachId);
		} catch (Exception e) {
			e.printStackTrace();
		}
     	return "success";
	}
	 
}
