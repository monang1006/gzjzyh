package com.strongit.oa.common.custom.user.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.util.StringUtil;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 自定义接口
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date May 8, 2012 1:21:21 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.custom.user.service.CustomUserService
 */
@Service
public class CustomUserService implements ICustomUserService {
	private GenericDAOHibernate<Object, String> serviceDAO;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		serviceDAO = new GenericDAOHibernate<Object, String>(sessionFactory,
				Object.class);
	}

	public GenericDAOHibernate getServiceDAO() {
		return serviceDAO;
	}

	/**
	 * 
	 * 获取分管处室下的用户id
	 * 
	 * @author 严建
	 * @param userId
	 * @return List
	 * @createTime Feb 14, 2012 9:24:55 PM
	 */
	public List getManageUserIds(String userId) {
		StringBuilder hql = new StringBuilder();
		hql
				.append("select user.userId from TUumsBaseOrg org,TUumsBaseUser user where org.orgId = user.orgId and org.rest2 like ?");
		List list = getServiceDAO().find(hql.toString(),
				new Object[] { "%" + userId + "%" });
		return list;
	}

	/**
	 * 获取分管的处室信息
	 * 
	 * @author 严建
	 * @param userId
	 * @return List<Object[]> object[]结构{机构id,机构名称}
	 * @createTime Feb 17, 2012 7:03:49 PM
	 */
	public List getManageOrgInfo(String userId) {
		StringBuilder hql = new StringBuilder();
		hql
				.append("select org.orgId,org.orgName from TUumsBaseOrg org where  org.rest1='0' and org.rest2 like ? order by org.orgSequence desc");
		List list = getServiceDAO().find(hql.toString(),
				new Object[] { "%" + userId + "%" });
		return list;
	}

	/**
	 * 获取分管处室下的用户信息
	 * 
	 * @author 严建
	 * @param userId
	 * @return List<Object[]> object[]结构{用户id,用户名称,机构id,机构名称,机构排序号}
	 * @createTime Feb 17, 2012 7:03:49 PM
	 */
	public List getManagerUserInfo(String userId) {
		StringBuilder hql = new StringBuilder();
		hql
				.append("select user.userId,user.userName,org.orgId,org.orgName,org.orgSequence  from TUumsBaseOrg org,TUumsBaseUser user where org.rest1='0' and  org.orgId = user.orgId and org.rest2 like ? ");
		List list = getServiceDAO().find(hql.toString(),
				new Object[] { "%" + userId + "%" });
		return list;
	}

	/**
	 * 获取分管处室下的用户信息(Map形式)
	 * 
	 * @author 严建
	 * @param userId
	 * @return 用户id：【用户id,用户名称,机构id,机构名称,机构排序号】
	 * @createTime Apr 6, 2012 11:17:48 AM
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object[]> getManagerUserInfoForMap(String userId) {
		Map<String, Object[]> map = null;
		List<Object[]> list = getManagerUserInfo(userId);
		for (Object[] objs : list) {
			if (map == null) {
				map = new HashMap<String, Object[]>();
			}
			map.put(StringUtil.castString(objs[0]), objs);
		}
		return map;
	}

	/**
	 * 获取所有的用户信息
	 * 
	 * @description
	 * @author 严建
	 * @return List<Object[]> Object[]结构：用户id，用户名称，所在部门id，所在部门名称
	 * @createTime May 8, 2012 1:07:40 PM
	 */
	public List getAllUserInfo() {
		return serviceDAO
				.find("select u.userId,u.userName,o.orgId,o.orgName from TUumsBaseUser u,TUumsBaseOrg o where u.orgId=o.orgId");
	}

	/**
	 * 获取指定范围的用户信息
	 * 
	 * @description
	 * @author 严建
	 * @param userIds
	 * @return List<Object[]> Object[]结构：用户id，用户名称，所在部门id，所在部门名称,用户类型
	 * @createTime May 8, 2012 1:16:37 PM
	 */
	public List getUsersInfo(List<String> userIds) {
		List result = null;
		if (userIds != null && !userIds.isEmpty()) {
			StringBuilder param = new StringBuilder();
			for (int i = 0; i < userIds.size(); i++) {
				param.append("?,");
			}
			String Sql = "select u.userId,u.userName,o.orgId,o.orgName,u.rest1,o.orgSequence,u.userSequence "
					+ "from TUumsBaseUser u,TUumsBaseOrg o "
					+ "where u.orgId=o.orgId and u.userId in ("
					+ param.substring(0, param.length() - 1) + ") order by u.userSequence";
			result = serviceDAO.find(Sql, userIds.toArray());
		}
		return result;
	}

	
	/**
	 * 获取指定范围的用户信息
	 * 
	 * @author yanjian
	 * @param userIds
	 * @return	List<Object[]> Object[]结构：用户id，用户名称，用户排序号，所在部门id，所在部门名称，部门排序号
	 * Nov 13, 2012 4:34:36 PM
	 */
	public List getUsersInfoForList(Set<String> userIds) {
		List result = null;
		if (userIds != null && !userIds.isEmpty()) {
			StringBuilder param = new StringBuilder();
			for (int i = 0; i < userIds.size(); i++) {
				param.append("?,");
			}
			String Sql = "select u.userId,u.userName,u.userSequence,o.orgId,o.orgName,o.orgSequence "
					+ "from TUumsBaseUser u,TUumsBaseOrg o "
					+ "where u.orgId=o.orgId and u.userId in ("
					+ param.substring(0, param.length() - 1) + ")";
			result = serviceDAO.find(Sql, userIds.toArray());
		}
		return result;
	}
	
	/**
	 * 获取指定范围的用户信息
	 * 
	 * @author yanjian
	 * @param userIds
	 * @return	Map<String, Object[]> String:用户id; Object[]结构：用户id，用户名称，用户排序号，所在部门id，所在部门名称，部门排序号
	 * Nov 13, 2012 4:34:36 PM
	 */
	public Map<String, Object[]>  getUsersInfoForMap(Set<String> userIds) {
		Map<String, Object[]> result = null;
		List list = getUsersInfoForList(userIds);
		if (list != null && !list.isEmpty()) {
			for (Object obj : list) {
				if (result == null) {
					result = new HashMap<String, Object[]>();
				}
				Object[] objs = (Object[]) obj;
				result.put(StringUtil.castString(objs[0]), objs);
			}
		}
		return result;
	}
	/**
	 * 获取指定范围的用户信息
	 * 
	 * @description
	 * @author 严建
	 * @param userIds
	 * @return Map<String,Object[]> String:用户id;Object[]结构：用户id，用户名称，所在部门id，所在部门名称
	 * @createTime May 8, 2012 1:16:37 PM
	 */
	public Map<String, Object[]> getUserInfoMap(List<String> userIds) {
		LinkedHashMap<String, Object[]> result = null;
		List list = getUsersInfo(userIds);
		if (list != null && !list.isEmpty()) {
			for (Object obj : list) {
				if (result == null) {
					result = new LinkedHashMap<String, Object[]>();
				}
				Object[] objs = (Object[]) obj;
				result.put(StringUtil.castString(objs[0]), objs);
			}
		}
		return result;
	}
	
	/**
	 * 根据机构id获取所在机构下所有用户的id
	 * 
	 * @author yanjian
	 * @param OrgId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Sep 25, 2012 3:26:43 PM
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllUserIdByOrgId(String orgId)
			throws ServiceException, DAOException, SystemException {
		try {
			if(orgId == null || "".equals(orgId)){
				throw new SystemException("orgId is not null");
			}else{
				return serviceDAO.find("select u.userId from TUumsBaseUser u where u.userIsdel = '0' and u.userIsactive='1' and  u.orgId= ? ",orgId);
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	/**
	 * 获取指定机构id下的用户信息
	 * 
	 * @author yanjian
	 * @param orgIds
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Sep 25, 2012 3:34:51 PM
	 */
	public List<String> getAllUserIdByOrgIdArray(String[] orgIds)
			throws ServiceException, DAOException, SystemException {
		try {
			if (orgIds == null || orgIds.length == 0) {
				throw new SystemException("orgIds is not null");
			} else {
				StringBuilder sql = new StringBuilder(
						"select u.userId from TUumsBaseUser u where 1=1 ");
				sql.append(" and u.userIsdel = '0' and u.userIsactive='1' and u.orgId in ( ");
				int orgIdsSize = orgIds.length;
				for (int i = 0; i < orgIdsSize; i++) {
					sql.append(" ? ,");
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" ) ");
				return serviceDAO.find(sql.toString(), orgIds);
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	/**
	 * 获取分管用户id
	 * 
	 * @author yanjian
	 * @param userId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Sep 25, 2012 3:58:42 PM
	 */
	public List<String> getManagerUserIds(String userId)
			throws ServiceException, DAOException, SystemException {
		try {
			StringBuilder hql = new StringBuilder();
			hql
					.append("select user.userId  from TUumsBaseOrg org,TUumsBaseUser user where org.rest1='0'  and user.userIsdel = '0' and user.userIsactive='1'  and  org.orgId = user.orgId and org.rest2 like ? ");
			return getServiceDAO().find(hql.toString(),
					new Object[] { "%" + userId + "%" });
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
}
