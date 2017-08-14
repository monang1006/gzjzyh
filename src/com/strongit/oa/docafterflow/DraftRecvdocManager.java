package com.strongit.oa.docafterflow;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDraftRecvdoc;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * <p>Title: DraftRecvdocManager.java</p>
 * <p>Description: </p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @des		 收文草稿箱操作类
 * @date 	 2009-11-13 09:30:20
 * @version  1.0
 */
@Service
@Transactional
public class DraftRecvdocManager {
	
	GenericDAOHibernate<ToaDraftRecvdoc, String> recvdocDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		recvdocDao = new GenericDAOHibernate<ToaDraftRecvdoc, String>(sessionFactory,ToaDraftRecvdoc.class);
	}
	
	/**
	 * @author  于宏洲
	 * @date    2009-11-13 09:30:01
	 * @des 	对象存储    
	 * @return  void
	 */
	public void saveObj(ToaDraftRecvdoc doc){
		recvdocDao.save(doc);
	}

}
