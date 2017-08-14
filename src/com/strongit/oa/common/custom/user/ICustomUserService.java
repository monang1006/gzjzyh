package com.strongit.oa.common.custom.user;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 自定义接口
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         May 8, 2012 1:20:33 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.common.custom.user.ICustomUserService
 */
public interface ICustomUserService {
	/**
	 * 
	 * 获取分管处室下的用户id
	 * 
	 * @author 严建
	 * @param userId
	 * @return
	 * @createTime Feb 14, 2012 9:24:55 PM
	 */
	public List getManageUserIds(String userId);

	/**
	 * 获取分管的处室信息
	 * 
	 * @author 严建
	 * @param userId
	 * @return List<Object[]> object[]结构{机构id,机构名称}
	 * @createTime Feb 17, 2012 7:03:49 PM
	 */
	public List getManageOrgInfo(String userId);

	/**
	 * 获取分管处室下的用户信息
	 * 
	 * @author 严建
	 * @param userId
	 * @return List<Object[]> object[]结构{用户id,用户名称,机构id,机构名称,机构排序号}
	 * @createTime Feb 17, 2012 7:03:49 PM
	 */
	public List getManagerUserInfo(String userId);

	/**
	 * 获取分管处室下的用户信息(Map形式)
	 * 
	 * @author 严建
	 * @param userId
	 * @return 用户id：【用户id,用户名称,机构id,机构名称,机构排序号】
	 * @createTime Apr 6, 2012 11:17:48 AM
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object[]> getManagerUserInfoForMap(String userId);
	/**
         * 获取所有的用户信息
         * 
         * @description
         * @author 严建
         * @return List<Object[]> Object[]结构：用户id，用户名称，所在部门id，所在部门名称
         * @createTime May 8, 2012 1:07:40 PM
         */
        public List getAllUserInfo();
        /**
         * 获取指定范围的用户信息
         * 
         * @description
         * @author 严建
         * @param userIds
         * @return List<Object[]> Object[]结构：用户id，用户名称，所在部门id，所在部门名称
         * @createTime May 8, 2012 1:16:37 PM
         */
        public List getUsersInfo(List<String> userIds);
        /**
    	 * 获取指定范围的用户信息
    	 * 
    	 * @description
    	 * @author 严建
    	 * @param userIds
    	 * @return Map<String,Object[]> String:用户id;Object[]结构：用户id，用户名称，所在部门id，所在部门名称
    	 * @createTime May 8, 2012 1:16:37 PM
    	 */
    	public Map<String, Object[]> getUserInfoMap(List<String> userIds);
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
    			throws ServiceException, DAOException, SystemException;
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
    			throws ServiceException, DAOException, SystemException ;
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
    			throws ServiceException, DAOException, SystemException ;
    	/**
    	 * 获取指定范围的用户信息
    	 * 
    	 * @author yanjian
    	 * @param userIds
    	 * @return	List<Object[]> Object[]结构：用户id，用户名称，用户排序号，所在部门id，所在部门名称，部门排序号
    	 * Nov 13, 2012 4:34:36 PM
    	 */
    	public List getUsersInfoForList(Set<String> userIds) ;
    	/**
    	 * 获取指定范围的用户信息
    	 * 
    	 * @author yanjian
    	 * @param userIds
    	 * @return	Map<String, Object[]> String:用户id; Object[]结构：用户id，用户名称，用户排序号，所在部门id，所在部门名称，部门排序号
    	 * Nov 13, 2012 4:34:36 PM
    	 */
    	public Map<String, Object[]>  getUsersInfoForMap(Set<String> userIds) ;
}
