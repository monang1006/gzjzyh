/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK:1.5.0_14; Struts:2.1.2; Spring:2.5.6; Hibernate:3.3.1.GA
 * Create Date: 2008-12-25
 * Autour: dengwenqiang
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.senddoc.manager;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.doc.bo.TdocSendRet;

import com.strongit.oa.bo.ToaSenddoc;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.annotation.OALogger;

import com.strongit.workflow.workflowDao.WorkflowDao;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 退文单处理
 * 
 * @author xiaolj
 * @version 1.0
 * @company      Strongit Ltd. (C) copyright
 * @date         2013-1-31 15:00:23
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.senddoc.manager.SendDocRetManager
 */
@Service
@Transactional
@OALogger
public class SendDocRetManager extends BaseManager {
	private GenericDAOHibernate<TdocSendRet, java.lang.String> sendDocRetDao;
	
	private WorkflowDao serviceDAO=null;
	

	// 统一用户服务
	private IUserService user;

	/**
	 * 注入sessionFactory
	 * 
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		sendDocRetDao = new GenericDAOHibernate<TdocSendRet, String>(
				sessionFactory, TdocSendRet.class);
	}
	
	/**
	 * 保存退文单信息
	 * author xiaolj
	 * @param TdocSendRet
	 */
	public void saveDocRet(TdocSendRet model) {
		sendDocRetDao.save(model);
	}
	
	/**
	 * 保存退文单信息
	 * author xiaolj
	 * @param TdocSendRet
	 */
	public void updateDocRet(TdocSendRet model) {
		sendDocRetDao.update(model);
		
	}
	
	/**
	 * 根据bussinessid查找记录
	 * author xiaolj
	 * @param TdocSendRet
	 */
	public TdocSendRet getDocRetBybusId(String busId) {
		StringBuffer hql = new StringBuffer();
		hql.append("from TdocSendRet t where t.businessId='" + busId + "'");
		List<TdocSendRet> l = sendDocRetDao.find(hql.toString());
		if(l!=null&&l.size()>0){
			return l.get(0);
		}
		return new TdocSendRet();
	}
	
}
