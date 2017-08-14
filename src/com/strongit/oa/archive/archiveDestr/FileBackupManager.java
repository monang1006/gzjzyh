/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description 文件备份manager
 */

package com.strongit.oa.archive.archiveDestr;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongit.oa.bo.ToaArchiveFileBak;
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
public class FileBackupManager {
	private GenericDAOHibernate<ToaArchiveFileBak, java.lang.String> fileBackupDao;

	/**
	 * @roseuid 4959D19B0138
	 */
	public FileBackupManager() {

	}

	/**
	 * 注册DAO
	 * 
	 * @param sesionFactory
	 * @roseuid 4959CC480251
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sesionFactory) {
		fileBackupDao = new GenericDAOHibernate<ToaArchiveFileBak, String>(
				sesionFactory, ToaArchiveFileBak.class);
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午12:22:10
	 * @desc: 文件备份
	 * @param ToaArchiveFileBak
	 *            obj 文件对象
	 * @return void
	 */
	public void backupFile(ToaArchiveFileBak obj,OALogInfo ... loginfos) throws SystemException,ServiceException {
		try {
			if (obj.getFileId() == null) {
				obj.setFileId(null);
			}
			fileBackupDao.save(obj);

		} catch (ServiceException e) {
			   throw new ServiceException(MessagesConst.operation_error,               
						new Object[] {"文件对象备份"});
		}
	}
}
