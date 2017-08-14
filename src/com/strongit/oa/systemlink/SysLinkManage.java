/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-07-20
 * Autour: pengxq
 * Version: V1.0
 * Description：系统链接实现类
 */

package com.strongit.oa.systemlink;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSystemmanageSystemLink;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * �������Service
 * 
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
public class SysLinkManage{
	private GenericDAOHibernate<ToaSystemmanageSystemLink, java.lang.String> sysLinkDao;

	public SysLinkManage() {

	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		sysLinkDao = new GenericDAOHibernate<ToaSystemmanageSystemLink, java.lang.String>(
				sessionFactory, ToaSystemmanageSystemLink.class);
	}


	@Transactional(readOnly = true)
	public Page<ToaSystemmanageSystemLink> getSysLink(
			Page<ToaSystemmanageSystemLink> page,String systemName ,String linkUrl,
			String systemDesc) throws SystemException,
			ServiceException {
		try {
			Object[] obj = new Object[3];
			int i = 0;
			String hql = "from ToaSystemmanageSystemLink t where 1=1";
			//系统名称
			if (systemName != null && !"".equals(systemName)
					&& !"null".equals(systemName)) {
				hql += " and t.systemName like ?";
				obj[i] = "%" + systemName + "%";
				i++;
			}
			
			//链接地址
			if (linkUrl != null && !"".equals(linkUrl)
					&& !"null".equals(linkUrl)) {
				hql += " and t.linkUrl like ?";
				obj[i] = "%" + linkUrl + "%";
				i++;
			}
			//系统描述
			if (systemDesc != null && !"".equals(systemDesc)
					&& !"null".equals(systemDesc)) {
				hql += " and t.systemDesc like ?";
				obj[i] = "%" + systemDesc + "%";
				i++;
			}

			Object[] param = new Object[i];
			for (int k = 0, t = 0; k < obj.length; k++) {
				if (obj[k] != null) {
					param[t] = obj[k];
					t++;
				}
			}

			hql+=" order by t.linkId desc";
			return sysLinkDao.find(page, hql, param);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "系统链接分页列表" });
		}
	}
	
	public ToaSystemmanageSystemLink getSystemLink(String linkId) throws SystemException,
	ServiceException {
		try {
			ToaSystemmanageSystemLink model=sysLinkDao.get(linkId);	
			return  model;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "系统链接对象" });
		}
	}
	
	public String saveSystemLink(ToaSystemmanageSystemLink model) throws SystemException,
	ServiceException {
		String msg="";
		boolean flag=false;
		if(model.getLinkId()==null){
			flag=true;
		}
		try {
			sysLinkDao.save(model);
		} catch (ServiceException e) {
			if(flag){
				msg="增加外部系统链接失败！";
			}else{
				msg="编辑外部系统链接失败！";
			}
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "系统链接对象" });
		}
		return msg;
	}
	
	
	public String delete(String linkId) throws SystemException, ServiceException {
		String msg="";
		try {
			ToaSystemmanageSystemLink obj = this.getSystemLink(linkId);
			if (obj != null)
				sysLinkDao.delete(obj);
		} catch (ServiceException e) {
			 msg="删除外部系统链接失败!";
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "系统链接对象" });
		}
		return msg;
	}
	
	public List<ToaSystemmanageSystemLink> getAllSysLink() throws SystemException, ServiceException{
		try {
			List<ToaSystemmanageSystemLink> list=sysLinkDao.findAll();
			return list;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "系统链接列表" });
		}
	}

	public Page<ToaSystemmanageSystemLink> getSysLink(
			Page<ToaSystemmanageSystemLink> page1, String systemName,
			String linkUrl) {
		// TODO Auto-generated method stub
		return null;
	}
}
