package com.strongit.oa.desktop;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaDesktopPortal;
import com.strongit.oa.bo.ToaPortalPrival;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.viewmodel.IModelPrivalService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 门户操作类
 * 
 * @author zouhr
 * 
 */
@Service
@Transactional
public class DesktopPortalManager {

	private GenericDAOHibernate<ToaDesktopPortal, String> portalDao;


	private IModelPrivalService privalmanagers;

	public IModelPrivalService getPrivalmanagers() {
		return privalmanagers;
	}

	@Autowired
	public void setPrivalmanagers(IModelPrivalService privalmanagers) {
		this.privalmanagers = privalmanagers;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.portalDao = new GenericDAOHibernate<ToaDesktopPortal, String>(
				sessionFactory, ToaDesktopPortal.class);	
	}

	/**
	 * 读取门户信息集 可以按门户名查询门户信息集
	 */
	public Page<ToaDesktopPortal> getPortalPages(Page<ToaDesktopPortal> page,
			String portalName) throws SystemException, ServiceException {
		Object[] values = new Object[1];
		StringBuffer hql = new StringBuffer(
				"from ToaDesktopPortal t where 1=1 ");

		// 名称
		if (portalName != null && !"".equals(portalName)
				&& !"null".equals(portalName)) {
			hql.append(" and t.portalName like ?");
			values[0] = "%" + portalName + "%";
		} else {
			hql.append(" and 1=?");
			values[0] = 1;
		}
		hql.append("order by t.secrec ASC ");
		return page = portalDao.find(page, hql.toString(), values);
	}

	public Page<ToaDesktopPortal> getPortalPagesView(
			Page<ToaDesktopPortal> page, String ismoren)
			throws SystemException, ServiceException {
		Object[] values = new Object[1];
		StringBuffer hql = new StringBuffer(
				"from ToaDesktopPortal t where 1=1 ");
		if (ismoren != null && !"".equals(ismoren) && !"null".equals(ismoren)) {
			hql.append(" and t.isMoren like ?");
			values[0] = "%" + ismoren + "%";
		} else {
			hql.append(" and 1=?");
			values[0] = 1;
		}

		hql.append("order by t.secrec ASC ");
		return page = portalDao.find(page, hql.toString(), values);
	}

	public List<ToaDesktopPortal> getDesktopProtaListForMoren(String userId) {
		List<ToaDesktopPortal> list = new ArrayList<ToaDesktopPortal>();
		List<ToaDesktopPortal> list2 = privalmanagers
				.getDesktopProtaList(userId);
		if (list2 != null && list2.size() > 0) {
			for (Iterator<ToaDesktopPortal> it = list2.iterator(); it.hasNext();) {
				ToaDesktopPortal tdp = it.next();
				if (tdp.getIsMoren().equals("1")) {
					list.add(tdp);
				}
			}
		}
		return list;
	}
	
	
	public String getDesktopPortalList(String userId){
		List<ToaDesktopPortal> list=this.getDesktopProtaListForMoren(userId);
		String portalStr="";
		if (list != null && list.size() > 0) {
			for (Iterator<ToaDesktopPortal> it = list.iterator(); it.hasNext();) {
				ToaDesktopPortal tdp = it.next();
				//pt[1]="<%=basePath%>/desktop/desktopWhole.action?defaultType=2&portalId="+tdp.getPortalId()+"";
				portalStr+="|"+tdp.getPortalName()+"#"+tdp.getPortalId();
			}
		}else{
			portalStr+="|"+"默认桌面"+"#";
		}
		if(portalStr.length()>0){
			portalStr=portalStr.substring(1);
		}
		return portalStr;
	}

	public List<ToaDesktopPortal> getDesktopProtaListForAll(String userId) {

		return privalmanagers.getDesktopProtaList(userId);
	}

	/**
	 * 通过ID读取门户对像
	 * 
	 * @param portalId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaDesktopPortal getPortal(String portalId) throws SystemException,
			ServiceException {
		try {
			return ((ToaDesktopPortal) this.portalDao.get(portalId));
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "门户管理" });
		}
	}

	/**
	 * 添加门户
	 * 
	 * @param portal
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void addPortal(ToaDesktopPortal portal) throws SystemException,
			ServiceException {
		try {
			if ("".equals(portal.getPortalId())) {
				portal.setPortalId(null);
			}
			Date nowday = new Date();
			portal.setSetTime(nowday);
			this.portalDao.save(portal);
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "门户管理添加" });
		}
	}

	public void updatePortal(ToaDesktopPortal portal) throws SystemException,
			ServiceException {
		try {

			this.portalDao.update(portal);
		} catch (ServiceException e) {
			throw new ServiceException("find.error", new Object[] { "门户管理添加" });
		}
	}


	public ToaDesktopPortal getOnePortal() throws SystemException,
			ServiceException {
		String hql = "";
		List list = null;

		hql = "from ToaDesktopPortal t";
		list = portalDao.find(hql);

		if (list.size() == 0) {
			return null;
		} else
			return (ToaDesktopPortal) list.get(0);

	}

	/**
	 * 删除门户
	 * 
	 * @param portalId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void delPortal(String portalId) throws SystemException,
			ServiceException {
		try {
			String[] ids = portalId.split(",");

			for (int i = 0; i < ids.length; ++i) {
				this.portalDao.delete(ids[i]);
			}
		} catch (ServiceException e) {
			throw new ServiceException("delete.error",
					new Object[] { "门户管理删除" });
		}
	}
	
	public void deleteByPortalId (String portalId){
		privalmanagers.deleteByPortalId(portalId);
	}

	/*
	 * 
	 * Description: param: @author 彭小青 @date Sep 26, 2009 11:26:59 AM
	 */
	public boolean isExistName(String portalId, String portalName)
			throws SystemException, ServiceException {
		try {
			boolean flag = true;
			String hql = "";
			List list = null;
			if (portalId != null && !"".equals(portalId)) {
				hql = "from ToaDesktopPortal t where t.portalId<>? and t.portalName=?";
				list = portalDao.find(hql, portalId, portalName);
			} else {
				hql = "from ToaDesktopPortal t where t.portalName=?";
				list = portalDao.find(hql, portalName);
			}
			if (list.size() == 0) {
				flag = false;
			}
			return flag;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { " 门户名称是否存在" });
		}
	}
	
	/**
	 * 门户管理权限获取门户
	 * @author zhengzb
	 * @desc 
	 * 2011-1-26 上午10:47:18 
	 * @param portalIds
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaDesktopPortal> getDesktopPortalListByIds(String portalIds) throws SystemException,ServiceException{
		List<ToaDesktopPortal> list=new ArrayList<ToaDesktopPortal>();
		try {
			StringBuffer sql = new StringBuffer("");
			sql.append("from ToaDesktopPortal  t  where 1=1");
			sql.append(" and t.portalId in ("+portalIds+")");
			//sql.append(" and t.isMoren=1 ");//门户是默认门户
			sql.append("order by t.secrec deSC ");
			
			list=portalDao.find(sql.toString());
			
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "门户管理权限获取门户" });
		}
		return list;
	}

}
