/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：访问ip控制实现类
 */
package com.strongit.oa.ipaccess.setipscope;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.oa.bo.ToaSysmanageLogin;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
public class IpScopeManager {
	private GenericDAOHibernate<ToaSysmanageLogin, java.lang.String> IpScopeDao;

	/**
	 * @param sessionFactory
	 * @roseuid 4939D5A803C8
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		IpScopeDao = new GenericDAOHibernate<ToaSysmanageLogin, java.lang.String>(
				sessionFactory, ToaSysmanageLogin.class);
	}

	/**
	 * @roseuid 4939DB8F0109
	 */
	public IpScopeManager() {

	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午03:11:28
	 * @desc: 获取分页列表
	 * @param Page
	 *            page 分页对象
	 * @return Page分页列表
	 */
	@Transactional(readOnly = true)
	public Page<ToaSysmanageLogin> getAllIpScope(Page<ToaSysmanageLogin> page)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaSysmanageLogin t where 1=1";
			page = IpScopeDao.find(page,hql);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "访问ip控制分页列表" });
		}
		return page;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午03:06:10
	 * @desc: 根据主键获取访问ip控制对象
	 * @param String
	 *            ipScopeId 主键
	 * @return 访问ip控制对象
	 */
	public ToaSysmanageLogin getIpScope(String ipScopeId)
			throws SystemException, ServiceException {
		ToaSysmanageLogin obj = null;
		try {
			obj = IpScopeDao.get(ipScopeId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "访问ip控制对象" });
		}
		return obj;
	}

	/**
	 * 
	 * @author：pengxq
	 * @time：2009-1-5下午03:07:54
	 * @desc: 保存对象
	 * @param ToaSysmanageLogin
	 *            ipScope访问ip控制对象
	 * @return void
	 */
	public String saveIpScope(ToaSysmanageLogin ipScope) throws SystemException,
			ServiceException {
		String msg="";
		try {
			IpScopeDao.save(ipScope);
		} catch (ServiceException e) {
			msg="保存访问ip段失败！";
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "访问ip控制对象" });
		}
		return msg;
	}

	/**
	 * 
	 * @author：pengxq
	 * @time：2009-1-5下午03:07:54
	 * @desc: 保存对象
	 * @param ToaSysmanageLogin
	 *            ipScope访问ip控制对象
	 * @return void
	 */
	public void deleteIpScope(String ipScopeId) throws SystemException,
			ServiceException {
		try {
			ToaSysmanageLogin obj = IpScopeDao.get(ipScopeId);
			if (obj != null)
				IpScopeDao.delete(obj);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "访问ip控制对象" });
		}
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午03:11:28
	 * @desc: 根据查询条件查询
	 * @param Page
	 *            page 分页对象
	 * @param String
	 *            startIp 起始ip地址
	 * @param String
	 *            endIp 结束ip地址
	 * @param String
	 *            detail 备注
	 * @return Page分页列表
	 */
	@Transactional(readOnly = true)
	public Page<ToaSysmanageLogin> searchIpScope(Page page, String startIp,
			String endIp, String detail) throws SystemException,
			ServiceException {
		try{
			Object[] obj = new Object[3];
			int i = 0;
			String hql = "from ToaSysmanageLogin t where 1=1";
			// 起始ip
			if (startIp != null && !"".equals(startIp) && !"null".equals(startIp)) {
				hql += " and t.loginBeginIp >= ?";
				obj[i] = startIp;
				i++;
			}
			// 结束ip
			if (endIp != null && !"".equals(endIp) && !"null".equals(endIp)) {
				hql += " and t.loginEndIp =< ?";
				obj[i] = endIp;
				i++;
			}
			// 备注
			if (detail != null && !"".equals(detail) && !"null".equals(detail)) {
				hql += " and t.loginDesc like ?";
				obj[i] = "%" + detail + "%";
				i++;
			}
			// if(i==0){
			// page.setAutoCount(true);
			// page = IpScopeDao.findAll(page);
			// }
			// else if(i==1){
			// page.setAutoCount(false);
			// page = IpScopeDao.find(page,hql.toString(),obj[0]);
			// }
			// else if(i==2){
			// page.setAutoCount(false);
			// page = IpScopeDao.find(page,hql.toString(),obj[0],obj[1]);
			// }
			// else if(i==3){
			// page.setAutoCount(false);
			// page = IpScopeDao.find(page,hql.toString(),obj[0],obj[1],obj[2]);
			// }
			// return page;
			Object[] param = new Object[i];
			for (int k = 0, t = 0; k < obj.length; k++) {
				if (obj[k] != null) {
					param[t] = obj[k];
					t++;
				}
			}
			return IpScopeDao.find(page, hql, param);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "访问ip控制对象" });
		}	
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午03:25:44
	 * @desc: 根据ip地址和用户判断该用户是否有登入该系统的权限
	 * @param String
	 *            ip ip地址
	 * @param String
	 *            userid 用户id
	 * @return
	 */
	public boolean isRestrOrNot(String ip, String userid)
			throws SystemException, ServiceException {
		// TODO Auto-generated method stub
		try{
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "访问ip控制对象" });
		}
		return false;
	}

}
