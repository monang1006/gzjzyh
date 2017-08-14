/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：备份文件附件manager
 */

package com.strongit.oa.archive.archiveDestr;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongit.oa.bo.ToaArchiveFileAppendBak;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
@OALogger
public class AnnexBackupManager {
	private GenericDAOHibernate<ToaArchiveFileAppendBak, String> AnnexBackupDao;

	/**
	 * @roseuid 4959D19A00AB
	 */
	public AnnexBackupManager() {

	}

	/**
	 * 注册DAO
	 * @param sessionFactory
	 * @roseuid 4959CD60005D
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		AnnexBackupDao = new GenericDAOHibernate<ToaArchiveFileAppendBak, String>(
				sessionFactory, ToaArchiveFileAppendBak.class);
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午12:22:10
	 * @desc: 文件附件备份
	 * @param ToaArchiveFileAppendBak
	 *            obj 文件附件对象
	 * @return void
	 */
	public void backupAnnex(ToaArchiveFileAppendBak obj,OALogInfo ... loginfos)  throws SystemException,ServiceException{
		try {
			AnnexBackupDao.save(obj);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,               
					new Object[] {"文件附件备份"});
		}
	}
}
