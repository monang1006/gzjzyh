package com.strongit.oa.iphoneset;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.strongit.oa.bo.ToaIphoneSet;
import com.strongit.oa.bo.ToaSystemset;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
public class IphonesetManager {
	private GenericDAOHibernate<ToaIphoneSet, String> iphonesetDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.iphonesetDao = new GenericDAOHibernate<ToaIphoneSet, String>(
				sessionFactory, ToaIphoneSet.class);

	}
	
	/**
	 * @author：shenyl
	 * @time：2012-8-17上午10:30:29 读取IPHONE系统设置
	 * @return ToaIphoneSet
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaIphoneSet getIphoneset() throws SystemException, ServiceException {
		try {
			List list = this.iphonesetDao.findAll();
			if (list.size() != 0) {
				return ((ToaIphoneSet) list.get(0));
			} else
				return null;
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "读取系统配置" });
		}
	}
	
	/**
	 * @author：shenyl
	 * @time：2012-9-19上午9:23:29 保存IPHONE系统设置
	 * @return ToaIphoneSet
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void save(ToaIphoneSet model) throws SystemException,
			ServiceException {
		iphonesetDao.save(model);
	}


}
