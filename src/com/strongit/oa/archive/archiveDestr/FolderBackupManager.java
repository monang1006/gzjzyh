/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：备份案卷manager
 */

package com.strongit.oa.archive.archiveDestr;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongit.oa.bo.ToaArchiveFolderBak;
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
public class FolderBackupManager {
	/** 备份案卷Dao */
	private GenericDAOHibernate<ToaArchiveFolderBak, String> folderBackupDao;

	/**
	 * @roseuid 4959D19B029F
	 */
	public FolderBackupManager() {

	}

	/**
	 * 注册DAO
	 * 
	 * @param sessionFactory
	 * @roseuid 4959CA5D02BF
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		folderBackupDao = new GenericDAOHibernate<ToaArchiveFolderBak, String>(
				sessionFactory, ToaArchiveFolderBak.class);
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午12:22:10
	 * @desc: 案卷备份
	 * @param ToaArchiveFolderBak
	 *            obj 案卷对象
	 * @return void
	 */
	public void backupFolder(ToaArchiveFolderBak obj,OALogInfo ... loginfos)  throws SystemException,ServiceException{
		try {
			folderBackupDao.save(obj);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,               
					new Object[] {"案卷备份"});
		}
	}
}
