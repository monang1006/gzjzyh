package com.strongit.oa.docafterflow;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.Tdocattach;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * <p>Title: DocAttachManager.java</p>
 * <p>Description: </p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @des		 收文草稿相关联的草稿操作
 * @date 	 2009-11-13 10:27:43
 * @version  1.0
 */
@Service
@Transactional
public class DocAttachManager {

	GenericDAOHibernate<Tdocattach, String> attachDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory){
		attachDao = new GenericDAOHibernate<Tdocattach, String>(sessionFactory,Tdocattach.class);
	}
	
	public void saveObj(Tdocattach attach){
		attachDao.merge(attach);
	}
	
}
