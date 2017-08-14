/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK:1.5.0_14; Struts:2.1.2; Spring:2.5.6; Hibernate:3.3.1.GA
 * Create Date: 2008-12-25
 * Autour: dengwenqiang
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.common.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.Position;
import com.strongit.oa.common.user.model.Privilege;
import com.strongit.oa.common.user.model.Role;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.model.UserGroup;
import com.strongit.uums.bo.TUumsBaseAbrole;
import com.strongit.uums.bo.TUumsBaseAreacode;
import com.strongit.uums.bo.TUumsBaseGroup;
import com.strongit.uums.bo.TUumsBaseGroupUser;
import com.strongit.uums.bo.TUumsBaseLog;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBasePost;
import com.strongit.uums.bo.TUumsBasePostOrg;
import com.strongit.uums.bo.TUumsBasePostUser;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongit.uums.bo.TUumsBasePrivilType;
import com.strongit.uums.bo.TUumsBaseRole;
import com.strongit.uums.bo.TUumsBaseRoleUser;
import com.strongit.uums.bo.TUumsBaseSysmanager;
import com.strongit.uums.bo.TUumsBaseSystem;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.uums.security.UserInfo;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * 统一用户接口
 * 
 * @author 邓志城
 * @version 3.0
 */
public interface IUserService {

	public static String GradeAuthorizationUser = "4";//不受分级授权的用户身份标识

	public static String SYSTEMADMINISTRATOR = "1";//系统管理员

	public static String SYSTEMMANAGER = "2";//管理员
	
	public static String SYSTEM_ACCOUNT = "402882282262726001226289c8cb0001";//系统用户id,用户线程轮询时的业务操作。

	public void setUserId(String userId);

	//取回任务时,无法得到当前用户信息
	public String getUserId();

	/**
	 * 验证指定的用户是否是系统管理员
	 * @author:邓志城
	 * @date:2010-12-31 上午11:14:09
	 * @param userId		用户ID
	 * @return
	 * 		true：系统管理员；false：非系统管理员
	 */
	public boolean isSystemAdministrator(String userId);

	/**
	 * 验证指定的用户是否是管理员
	 * @author:邓志城
	 * @date:2010-12-31 上午11:14:09
	 * @param userId		用户ID
	 * @return
	 * 		true：管理员；false：非管理员
	 */
	public boolean isSystemManager(String userId);

	/**
	 * 验证指定的用户是否是分级授权管理员（即不受分级授权控制,可以查看所有数据）
	 * @author:邓志城
	 * @date:2010-12-31 下午02:12:40
	 * @param userId		用户ID
	 * @return
	 * 	true：管理员；false：非管理员
	 */
	public boolean isSystemDataManager(String userId);

	/**
	 * 修改分级授权开关
	 * @author:邓志城
	 * @date:2010-12-29 上午10:06:17
	 * @param bViewChildOrganizationEnabeld
	 * 		true：允许看到下级机构；false：不允许看到下级机构。
	 */
	public void setViewChildOrganizationEnabled(
			boolean bViewChildOrganizationEnabeld);

	/**
	 * 是否开启分级授权开关
	 * @author:邓志城
	 * @date:2010-12-31 上午11:05:46
	 * @return
	 * 	true:开启；false：不开启
	 */
	public boolean isViewSetOpen();

	/**
	 * 是否允许看到下级机构
	 * @author:邓志城
	 * @date:2010-12-29 上午10:03:29
	 * @return
	 * 		true：允许看到下级机构；false：不允许看到下级机构。
	 */
	public boolean isViewChildOrganizationEnabeld();

	/**
	 * 根据用户Id获取到该用户所在分级授权机构对象
	 * @author:邓志城
	 * @date:2010-12-29 上午10:10:14
	 * @param userId		用户ID
	 * @return
	 * 		用户所在分级授权机构
	 * 例如：
	 * 	张三属于办公室人员,办公室属于机构A下部门。那么用户所在分级授权机构就是A。
	 * 	李四属于机构A下人员，那么那么用户所在分级授权机构就是A。
	 */
	public TUumsBaseOrg getSupOrgByUserIdByHa(String userId);

	/**
	 * 根据组织机构Id获取到上级分级授权机构对象
	 * @author:邓志城
	 * @date:2010-12-29 上午10:42:30
	 * @param orgId		机构id
	 * @return
	 * 		上级分级授权机构对象
	 * 	例如：
	 * 		orgId对应部门办公室，则返回机构A
	 * 		orgId对应A，则返回A
	 */
	public TUumsBaseOrg getSupOrgByOrgIdByHa(String orgId);

	/**
	 * 得到所有机构列表
	 * @author:邓志城
	 * @date:2010-12-31 下午03:36:24
	 * @return
	 */
	public List<TUumsBaseOrg> getAllOrgInfo();

	/**
	 * 得到机构下的用户列表
	 * @author:邓志城
	 * @date:2010-12-29 上午10:50:17
	 * @param orgId		机构或部门id
	 * @return
	 * 		用户列表
	 */
	public List<User> getAllUserInfoByHa(String orgId);

	/**
	 * 得到当前用户所在机构下的用户列表
	 * @author:邓志城
	 * @date:2010-12-29 上午10:50:17
	 * @return
	 * 		用户列表
	 */
	public List<User> getAllUserInfoByHa();

	/**
	 * 得到分级授权模式下用户组列表
	 * @author:邓志城
	 * @date:2010-1-26 上午11:06:47
	 * @param haOrgId 分级授权机构id
	 * @return
	 */
	public java.util.List<com.strongit.uums.bo.TUumsBaseGroup> getAllGroupInfoByHa(
			java.lang.String haOrgId);

	/**
	 * 得到当前用户所属机构和对应机构下的部门列表
	 * @author:邓志城
	 * @date:2010-1-12 下午03:01:36
	 * @return
	 */
	public java.util.List<Organization> getCurrentUserOrgAndDept();

	/**
	 * 得到分级授权组织机构树 
	 * @author:邓志城
	 * @date:2009-11-15 下午06:01:32
	 * @param orgIsdel - -是否删除过滤条件;“0”：未删除；“1”：已删除；其他：全部
	 * @param haOrgId 用于分级授权的组织机构Id 
	 * @return List 组织机构信息集
	 */
	public java.util.List<com.strongit.uums.bo.TUumsBaseOrg> getAllOrgInfoByHa(
			java.lang.String orgIsdel, java.lang.String haOrgId);

	/**
	 * 得到分级授权组织机构列表
	 * @author:邓志城
	 * @date:2010-12-31 上午11:50:55
	 * @param haOrgId		用于分级授权的组织机构Id 
	 * @return
	 * 		未删除的组织机构列表
	 */
	public java.util.List<com.strongit.uums.bo.TUumsBaseOrg> getAllOrgInfoByHa(
			java.lang.String haOrgId);

	/**
	 * 得到当前用户分级授权组织机构树
	 * @author:邓志城
	 * @date:2009-11-15 下午06:04:57
	 * @return
	 */
	public java.util.List<Organization> getCurrentUserOrgInfoByHa();

	/**
	 * 得到所有有效的分级授权组织机构下的岗位信息
	 * @author:邓志城
	 * @date:2010-12-31 下午01:35:34
	 * @param haOrgId  用于分级授权的组织机构Id 
	 * @return
	 * 	List 所有有效组织机构下的岗位信息
	 Object[]{(0)岗位Id,(1)岗位名称,(2)岗位所在组织机构Id,(3)岗位排序号} 
	 */
	public java.util.List<java.lang.Object[]> getAllOrgPostInfoByHa(
			java.lang.String haOrgId);

	/**
	 * 得到分级授权的角色信息列表。 
	 * @author:邓志城
	 * @date:2010-12-31 下午01:47:50
	 * @param haOrgId  用于分级授权的组织机构Id 
	 * @return  角色信息列表 
	 */
	public java.util.List<com.strongit.uums.bo.TUumsBaseRole> getAllRoleInfosByHa(
			java.lang.String haOrgId);

	/**
	 * 得到过滤后的分级授权的角色信息列表。 
	 * @author:邓志城
	 * @date:2010-12-31 下午01:50:37
	 * @param isactive  是否过滤未启用的角色；“0”：未启用;“1”：已启用;其他：全部
	 * @param haOrgId  用于分级授权的组织机构Id 
	 * @return
	 * 	角色信息集 
	 */
	public java.util.List<com.strongit.uums.bo.TUumsBaseRole> queryRolesByHa(
			java.lang.String isactive, java.lang.String haOrgId);

	/**
	 * 得到角色分页列表
	 * @author:邓志城
	 * @date:2010-12-31 下午01:54:19
	 * @param page				分页对象
	 * @param roleCode			角色编码查询条件
	 * @param selectrolename	角色名称查询条件
	 * @param roleisact			角色是否启用查询条件;“0”：未启用;“1”：已启用;其他：全部
	 * @param roledesc			角色描述查询条件
	 * @param haOrgId			用于分级授权的组织机构Id 
	 * @return					角色分页信息 
	 */
	public com.strongmvc.orm.hibernate.Page<com.strongit.uums.bo.TUumsBaseRole> queryRolesByHa(
			com.strongmvc.orm.hibernate.Page<com.strongit.uums.bo.TUumsBaseRole> page,
			java.lang.String roleCode, java.lang.String selectrolename,
			java.lang.String roleisact, java.lang.String roledesc,
			java.lang.String haOrgId);

	/**
	 * 根据查询条件得到分级授权角色信息列表
	 * @author:邓志城
	 * @date:2010-12-31 下午01:57:32
	 * @param roleCode				角色编码查询条件
	 * @param selectrolename		角色名称查询条件
	 * @param roleisact				角色是否启用查询条件;“0”：未启用;“1”：已启用;其他：全部
	 * @param roledesc				角色描述查询条件
	 * @param haOrgId				用于分级授权的组织机构Id 
	 * @return						角色列表信息 
	 */
	public java.util.List<com.strongit.uums.bo.TUumsBaseRole> queryRolesByHa(
			java.lang.String roleCode, java.lang.String selectrolename,
			java.lang.String roleisact, java.lang.String roledesc,
			java.lang.String haOrgId);

	/**
	 * 保存角色信息(分级授权机制) 
	 * @author:邓志城
	 * @date:2010-12-31 下午02:01:08
	 * @param boRole		角色信息BO
	 * @param haOrgId		用于分级授权的组织机构Id 
	 */
	public void saveRoleByHa(com.strongit.uums.bo.TUumsBaseRole boRole,
			java.lang.String haOrgId);

	/**
	 * 根据用户Id查询符合条件的该用户所属分级授权角色信息 
	 * @author:邓志城
	 * @date:2010-12-31 下午02:02:38
	 * @param userId				用户Id
	 * @param isRoleactive			角色是否启用；“0”：未启用；“1”：启用；其他：全部
	 * @param haOrgId				用于分级授权的组织机构Id 
	 * @return						角色信息集 
	 */
	public java.util.List<com.strongit.uums.bo.TUumsBaseRole> getUserRoleInfosByUserIdByHa(
			java.lang.String userId, java.lang.String isRoleactive,
			java.lang.String haOrgId);

	/**
	 * 获取当前用户信息
	 * @return com.strongit.oa.common.user.User 内部用户对象
	 */
	public User getCurrentUser() throws SystemException;

	/**
	 * 获取当前用户的领导信息
	 * @return com.strongit.oa.common.user.User 内部用户对象
	 */
	public User getCurrentUserLeader() throws SystemException;
	/**
	 * 根据登录名获取用户信息
	 * @param loginName 用户登陆名
	 * @return com.strongit.oa.common.user.User 内部用户对象
	 */
	public User getUserInfoByLoginName(String loginName) throws SystemException;

	/**
	 * 根据用户ID获取用户信息
	 * @param userId 用户ID
	 * @return com.strongit.oa.common.user.User 内部用户对象
	 */
	public User getUserInfoByUserId(String userId) throws SystemException;

	/**
	 * 根据用户ID获取用户信息
	 * @param userId 用户ID
	 * @return com.strongit.oa.common.user.User 内部用户对象
	 */
	public TUumsBaseUser getUumsUserInfoByUserId(String userId) throws SystemException;
	
	/**
	 * 获取指定用户的用户名
	 * @param userId 用户ID
	 * @return String 用户名
	 */
	public String getUserNameByUserId(String userId) throws SystemException;

	/**
	 * 获取指定用户的岗位
	 * @param userId 用户ID
	 * @return List 岗位信息集合
	 */
	public List<Position> getUserPositionsByUserId(String userId)
			throws SystemException;

	/**
	 * 获取指定用户的组织机构信息
	 * @param userId 用户ID
	 * @return 
	 */
	public Organization getUserDepartmentByUserId(String userId)
			throws SystemException;

	/**
	 * 根据部门ID获取部门信息
	 * @param orgId 部门ID
	 * @return
	 * @throws SystemException
	 */
	public Organization getDepartmentByOrgId(String orgId)
			throws SystemException;

	/**
	 * 根据岗位ID获取用户列表
	 * @param positionId
	 * @return List<User>
	 */
	public List<User> getUsersByPositionId(String positionId)
			throws SystemException;

	/**
	 * 获取指定部门的负责人ID
	 * @param orgId 部门ID
	 * @return 
	 */
	public User getDepartmentManagerByOrgId(String orgId)
			throws SystemException;

	/**
	 * 获取指定部门的上级部门ID
	 * @param orgId 部门ID
	 * @return
	 */
	public Organization getParentDepartmentByOrgId(String orgId)
			throws SystemException;

	/**
	 * 获取所有部门以及部门下的部门
	 * @return List
	 */
	public List<Organization> getAllDeparments() throws SystemException;

	/**
	 * 取出当前用户所属组织机构和下设组织机构
	 * @return List
	 */
	public List<Organization> getOrgAndChildOrgByCurrentUser()
			throws SystemException;

	/**
	 * 取出给定Id用户所属组织机构和下设组织机构
	 * @return List
	 */
	public List<Organization> getOrgAndChildOrgByUserId(String userId)
			throws SystemException;

	/**
	 * 根据部门ID获取用户列表
	 * @param orgId
	 * @return
	 */
	public List<User> getUsersByOrgID(String orgId) throws SystemException;

	/**
	 * 根据用户ID获取权限
	 * @param userId
	 * @return
	 */
	public List<Privilege> getPrivilegeByUserId(String userId)
			throws SystemException;

	/**
	 * 根据用户登录名获取权限
	 * @param loginName
	 * @return
	 */
	public List<Privilege> getPrivilegeByLoginName(String loginName)
			throws SystemException;

	/**
	 * 根据用户登录名获取岗位信息
	 * @param loginName
	 * @return List<Position>
	 */
	public List<Position> getPositionsByLoginName(String loginName)
			throws SystemException;

	/**
	 * 根据组织机构编码获取上级部门信息
	 * @param orgSyscode
	 * @return
	 */
	public Organization getParentDepartmentBySyscode(String orgSyscode)
			throws SystemException;

	/**
	 * 根据组织机构编码获取部门负责人
	 * @param orgSyscode
	 * @return User
	 */
	public User getDeparmentManagerBySyscode(String orgSyscode)
			throws SystemException;

	/**
	 * 根据用户登录名获取用户所在部门
	 * @param loginName
	 * @return
	 */
	public Organization getDepartmentByLoginName(String loginName)
			throws SystemException;

	/**
	 * author:dengzc
	 * description:
	 * modifyer:
	 * description:
	 * @param orgId
	 * @return
	 * @throws SystemException
	 */
	public TUumsBaseOrg getOrgInfoByOrgId(String orgId) throws SystemException;

	/**
	 * author:dengzc
	 * description:
	 * modifyer:
	 * description:
	 * @param orgId
	 * @return
	 * @throws SystemException
	 */
	public TUumsBaseOrg getParentOrgByOrgId(String orgId)
			throws SystemException;

	/**
	 * 获取指定组织机构的所有父组织机构
	 * @author:邓志城
	 * @date:2009-8-4 下午12:30:24
	 * @param lstOrg
	 * @param org
	 * @return
	 * @throws SystemException
	 */
	public List<Organization> getFartherOrgListByOrgId(
			List<Organization> lstOrg, Organization org) throws SystemException;

	/**
	 * author:dengzc
	 * description:
	 * modifyer:
	 * description:
	 * @param orgId
	 * @return
	 * @throws SystemException
	 */
	public List<TUumsBaseUser> getUserListByOrgId(String orgId)
			throws SystemException;

	/**
	 * 获取所有用户组
	 * @author:邓志城
	 * @date:2009-5-25 上午11:58:24
	 * @return
	 * @throws SystemException
	 */
	public List<UserGroup> getAllGroupInfo() throws SystemException;

	/**
	 * 获取某用户组下未删除的人员
	 * @author:邓志城
	 * @date:2009-5-25 下午01:58:48
	 * @param groupId
	 * @return
	 * @throws SystemException
	 */
	public List<User> getUsersInfoByGroupId(String groupId)
			throws SystemException;

	/**
	 * 获取组织机构下的岗位列表
	 * @author:邓志城
	 * @date:2009-8-3 下午02:30:09
	 * @param orgId
	 * @return List<String[]{postId,postName}>
	 * @throws SystemException
	 */
	public List<String[]> getPostListByOrgId(String orgId)
			throws SystemException;

	/**
	 * 获取岗位列表【岗位挂接在机构下】
	 * @author:邓志城
	 * @date:2009-8-3 下午03:27:36
	 * @return List<String[]{岗位ID,岗位名称}>
	 * @throws SystemException
	 */
	public List<String[]> getPostListWithOrg() throws SystemException;

	/**
	 * 获取所有激活状态并且未删除的用户列表
	 * @author:邓志城
	 * @date:2009-8-3 下午07:12:14
	 * @return
	 * @throws SystemException
	 */
	public List<User> getAllUserInfo() throws SystemException;

	/**
	 * 得到所有机构用户
	 * @return
	 */
	public List<TUumsBaseUser> getAllUserInfos();

	/**
	 * 获取组织机构列表【删除或者未删除状态的】
	 * @author:邓志城
	 * @date:2009-8-3 下午07:03:13
	 * @param orgIsdel
	 * @return
	 * @throws SystemException
	 */
	public List<Organization> getOrgs(String orgIsdel) throws SystemException;

	/**
	 * 保存组织机构信息
	 * @author:邓志城
	 * @date:2009-9-8 上午09:49:04
	 * @param orgInfo 机构信息
	 * @return true:成功，false:失败
	 */
	public boolean saveOrgInfo(TUumsBaseOrg orgInfo);

	/**
	 * 得到下一个组织机构排序号
	 * @author:邓志城
	 * @date:2009-9-8 上午09:51:04
	 * @return 下一个组织机构排序号
	 */
	public Long getNextOrgSequence();

	/**
	 * 得到下一个人员排序号
	 * @author:邓志城
	 * @date:2009-9-8 上午09:52:18
	 * @return 下一个人员排序号
	 */
	public Long getNextUserSequence();

	/**
	 * 保存人员信息
	 * @author:邓志城
	 * @date:2009-9-8 上午09:53:14
	 * @param userInfo
	 * @return boolean true：成功；false：失败
	 */
	public boolean saveUserInfo(TUumsBaseUser userInfo);

	/**
	 * 根据备用字段(rest1保存的是财政那边的机构ID)获得一个组织机构对象(OA专用)
	 * @author:邓志城
	 * @date:2009-9-8 上午09:56:24
	 * @param rest1 财政机构ID
	 * @param isOrgdel 组织机构是否删除<br>
	 * 		<p>“0”：否；“1”：是；其他：全部</p>
	 * @return 组织机构信息
	 */
	public TUumsBaseOrg getOrgInfoByRest1(String rest1, String isOrgdel);

	/**
	 * 删除指定的组织结构信息。<br>
	 * 	<p>增加删除组织结构时同时删除子机构及机构下人员</p>
	 * @author:邓志城
	 * @date:2009-9-8 上午09:58:47
	 * @param orgIds 组织结构信息Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 */
	public void deleteOrg(String orgIds);

	/**
	 * 删除指定的用户信息
	 * @author:邓志城
	 * @date:2009-9-8 上午10:05:00
	 * @param userIds 全用户信息Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 */
	public void deleteUser(String userIds);

	/**
	 * 根据组织机构Id得到组织机构下符合条件的人员信息
	 * @author:邓志城
	 * @date:2009-9-8 上午10:06:07
	 * @param orgId 组织机构Id
	 * @param userIsactive 人员是否启用<br>
	 *  		<p>“0”：未启用；“1”：已启用；其他：全部</p>
	 * @param userIsdel 人员是否删除<br>
	 * 		<p>“0”：未删除；“1”：已删除；其他：全部</p>
	 * @return 人员信息集
	 */
	public List<TUumsBaseUser> getUserInfoByOrgId(String orgId,
			String userIsactive, String userIsdel);

	/**
	 * 根据传入的机构编码获得其下面的所有子机构（包含本级）
	 * @author:邓志城
	 * @date:2009-9-8 上午10:08:21
	 * @param orgSyscode 组织机构编码
	 * @param orgIsdel 组织机构是否删除<br>
	 * 		<p>“0”：未删除；“1”：已删除；其他：全部</p>
	 * @return 组织机构信息集
	 */
	public List<TUumsBaseOrg> getSubOrgInfoByOrgsyscode(String orgSyscode,
			String orgIsdel);

	/**
	 * 当用户登录时先将已开始生效的委托规则置为取消
	 * @author:邓志城
	 * @date:2009-9-8 上午10:10:12
	 * @param userId 人员Id
	 * @param currentDate 当前时间
	 */
	public void cancelAbroleBeforLogin(String userId, Date currentDate);

	/**
	 * 当用户登录时先暂停其还未启用的委托规则，以防止多个用户同时拥有委托资源权限
	 * @author:邓志城
	 * @date:2009-9-8 上午10:11:22
	 * @param userId 人员Id
	 * @param currentDate 当前时间
	 */
	public void pauseAbroleBeforLogin(String userId, Date currentDate);

	/**
	 * 得到已经同步过的财政机构列表【将国斌需求】
	 * @author:邓志城
	 * @date:2009-9-9 下午05:10:56
	 * @return
	 */
	public List getOrgsbyCode();

	/**
	 * 得到用户MAP：KEY：机构ID，VALUE：机构下的人员列表
	 * @author:邓志城
	 * @date:2009-9-10 下午02:23:30
	 * @return
	 */
	public Map<String, List<User>> getUserMap();

	/**
	 * 得到岗位MAP
	 * KEY:机构ID，VALUE:机构下的岗位列表
	 * @author:邓志城
	 * @date:2009-9-10 下午02:43:26
	 * @return
	 */
	public Map<String, List<String[]>> getPostMap();

	/*
	 * 
	 * Description:根据岗位ID获取岗位信息列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Apr 7, 2010 2:21:40 PM
	 */
	public Position getPostInfoByPostId(String postId);

	/**
	 * 读取岗位列表.
	 * @author:邓志城
	 * @date:2010-7-15 下午03:36:31
	 * @return	岗位列表.
	 */
	public List<Position> getPostList();

	/**
	 * @author:luosy
	 * @description: 获取所有已启用的角色信息
	 * @date : 2010-12-24
	 * @modifyer:
	 * @description:
	 * @param isActive “0”：未启用；“1”：启用；其他：全部
	 * @return
	 */
	public List<Role> getAllUsedRoleInfos(String isActive);

	/**
	 * @author:luosy
	 * @description: 获取用户的角色信息
	 * @date : 2010-12-24
	 * @modifyer:
	 * @description:
	 * @param userId
	 * @param isRoleactive  “0”：未启用；“1”：启用；其他：全部
	 * @return
	 */
	public List<Role> getUserRoleInfosByUserId(String userId,
			String isRoleactive);

	/**
	 * 根据编码规则得到父级机构
	 * @author:邓志城
	 * @date:2010-1-12 下午02:52:53
	 * @param code
	 * @param ruleCode
	 * @param defaultParent
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String findFatherCode(String code, String ruleCode,
			String defaultParent) throws SystemException, ServiceException;

	/**
	 * 根据机构编码得到机构对象
	 * @author:邓志城
	 * @date:2011-2-16 上午11:05:50
	 * @param orgSyscode	机构编码
	 * @return
	 * 	机构对象
	 */
	public TUumsBaseOrg getOrgInfoBySyscode(String orgSyscode);
	
	/**
	 * 获取组织机构列表【删除或者未删除状态的而是否为机构的】
	 * @author:肖利建
	 * @date:2012-12-24 上午15:53:55
	 * @param orgIsdel 机构是否删除   isOrg 是否为机构 
	 * @return
	 * 	机构对象
	 */
	public List<Organization> getOrgAgency(String orgIsdel, String isOrg) throws SystemException;
	
	/**
	 * 通过领导联系人的id获取领导的id
	 * @return
	 * @throws SQLException
	 */
	public String getLDId(String id) throws SQLException;
	
	/**
	 * 通过用户id获取领导的 用户名
	 * @return
	 * @throws SQLException
	 */
	public String getLeaderByuserId(String id) throws SQLException;
	/**
	 * 通过领导的id获取领导联系人的id
	 * @return
	 * @throws SQLException
	 */
	public String getLDLxrId(String id) throws SQLException;
	/**
	 * 根据syscode获取其下组织机构列表 asc 排序
	 */
	public List<TUumsBaseOrg> getOrg(String syscode);
	/**
	 * 根据syscode删除数据  物理删除
	 */
	public void delOrgByCode(String syscode);

	/**
	 * author:luosy 2014-1-6
	 * description:
	 * @param Page 人员分页信息
	 * @param orgSyscode - -组织机构编码
	 * @param isUseractive - -人员是否启用；“1”：启用；“0”：停用；其他：所有
	 * @param isUserdel - -人员是否删除；“1”：删除；“0”：未删除；其他：所有
	 * @param haOrgId - -用于分级授权的组织机构Id
	 * @return
	 */
	public Page<TUumsBaseUser> getUsersOfOrgAndChildByOrgcodeForPageByHa(
			Page<TUumsBaseUser> page, String sysCode, String isUseractive,
			String  isUserdel, String orgId) throws DAOException,SystemException,ServiceException;
	
	/**
	 * 得到所有的组织结构信息。
	 * 
	 * @return List<TUumsBaseOrg> 组织结构信息列表
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseOrg> getAllOrgInfos() throws DAOException,
			SystemException, ServiceException;
	
	/**
	 * 通过组织机构Id得到分级授权的符合条件的组织结构信息
	 * @param orgIsdel -组织机构是否删除，“0”：未逻辑删除;“1”：已逻辑删除;其他：全部
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return List<TUumsBaseOrg> - 组织结构信息列表
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseOrg> queryOrgsByHa(String orgIsdel, String haOrgId)
			throws DAOException, SystemException, ServiceException;
	
	/**
	 * 得到所有符合条件的组织结构信息
	 * 
	 * @param orgIsdel -
	 *            组织机构是否删除<br>
	 *            <p>
	 *            “0”：未逻辑删除;“1”：已逻辑删除;其他：全部
	 *            </p>
	 * @return List<TUumsBaseOrg> - 组织结构信息列表
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseOrg> queryOrgs(String orgIsdel) throws DAOException,
			SystemException, ServiceException;
	
	/**
	 * 根据用户id获取岗位信息。
	 * 
	 * @param userId -
	 *            用户id
	 * @return List<TUumsBasePost> 岗位信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePost> getPostInfoByUserId(String userId)
			throws DAOException, SystemException, ServiceException;
	/**
	 * 根据用户id集合获取岗位信息。
	 * 
	 * @param userIdLst -
	 *            用户id集合
	 * @return  Map<String, List<TUumsBasePost>> 用户id:岗位信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Map<String, List<TUumsBasePost>> getPostUsersByUserIds(
			List<String> userIdLst) throws DAOException, SystemException,
			ServiceException;
	
	/**
	 * 根据组织机构编码得到下级指定机构类型的组织机构列表信息
	 * @param orgSyscode -组织机构编码,为null或“”则查询所有编码的
	 * @param isOrg -机构类型查询条件；“0”：部门类型，“1”：机构类型，其他：全部
	 * @param orgIsdel -组织机构是否删除查询条件；“0”：未删除，“1”：已删除，其他：全部
	 * @return List<TUumsBaseOrg> 下级组织机构信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseOrg> getSelfAndChildOrgsByOrgsyscodeAndType(
			String orgSyscode, String isOrg, String orgIsdel)
			throws DAOException, SystemException, ServiceException;
	
	/**
	 * 根据编码类型和当前编码得到上级编码
	 * @param codeRule -编码类型
	 * @param code -当前编码
	 * @return String 上级编码，若当前编码为顶级则返回""
	 * @throws SystemException
	 * @throws DAOException
	 * @throws ServiceException
	 */
	public String getParentCode(String codeRule, String code)
			throws SystemException, DAOException, ServiceException;

	/**
	 * 得到组织机构树的根节点的编码长度
	 * @author 喻斌
	 * @date Feb 21, 2010 3:20:19 PM
	 * @return int 组织机构树的根节点的编码长度<br>
	 * 		<p>如果启用了分级授权，则返回返回当前用户所属机构编码长度；如果未启用分级授权，则返回最顶级机构编码长度</p>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int findTopOrgCodeLength() throws DAOException, SystemException,
			ServiceException;

	/**
	 * 根据人员Id得到人员单独关联的资源权限信息
	 * @author 喻斌
	 * @date Jun 18, 2009 2:22:19 PM
	 * @param userId -人员Id
	 * @param isPrivilactive -资源权限是否启用<br>
	 * 		<p>“0”：未启用；“1”：已启用；其他：全部</p>
	 * @return List<TUumsBasePrivil> 资源权限信息集
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TUumsBasePrivil> getUserPrivilsByUserId(String userId,
			String isPrivilactive) throws DAOException, ServiceException, SystemException;
	
	/**
	 * 根据用户登录名得到指定用户权限信息列表
	 * 
	 * @param username -
	 *            用户登录名
	 * @param isPrivilactive
	 *            -权限启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isGroupactive
	 *            -用户组启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isRoleactive
	 *            -角色启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @return List - 用户权限信息列表
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePrivil> getPrivilInfosByUserLoginName(String username,
			String isPrivilactive, String isGroupactive,
			String isRoleactive) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 根据过滤条件得到当前用户拥有的系统信息列表。
	 * 
	 * @author 喻斌
	 * @date Mar 13, 2009 9:43:07 AM
	 * @param systemIsactive
	 *            -系统是否启用<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @return List<TUumsBaseSystem> 系统信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseSystem> getCurrentUserSystems(String systemIsactive)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 根据权限系统编码得到权限信息
	 * 
	 * @author 喻斌
	 * @date Apr 6, 2009 1:00:52 PM
	 * @param privilSyscode
	 *            -权限系统编码
	 * @return TUumsBasePrivil 权限BO信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TUumsBasePrivil getPrivilInfoByPrivilSyscode(String privilSyscode)
			throws DAOException, SystemException, ServiceException;
	
	/**
	 * 根据用户Id更新用户密码信息<br>
	 * <p>
	 * 需要考虑加密
	 * </p>
	 * <p>
	 * 增加MD5加密功能
	 * </p>
	 * <p>
	 * 取消MD5加密，传入参数在前台加密
	 * </p>
	 * 
	 * @author 喻斌
	 * @date Feb 10, 2009 3:17:32 PM
	 * @param userId
	 *            -用户Id
	 * @param newPassword
	 *            -更新密码信息<br>
	 *            <p>
	 *            需要考虑加密
	 *            </p>
	 */
	public void updateUserPassword(String userId, String newPassword);
	
	/**
	 * 通过组织机构Id得到分级授权的所有组织机构信息。
	 * @author 喻斌
	 * @date Nov 7, 2009 9:21:50 AM
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return List - 组织结构信息列表
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseOrg> getAllOrgInfosByHa(String haOrgId)
			throws DAOException, SystemException, ServiceException;
	

	/**
	 * 根据组织机构系统编码获取自己及下属未删除的分级授权组织机构信息
	 * @author 喻斌
	 * @date Nov 7, 2009 9:43:11 AM
	 * @param orgSyscode -组织机构系统编码
	 * @param orgIsdel -是否删除过滤条件；“0”：未删除；“1”：已删除；其他：全部
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return List<TUumsBaseOrg> 组织机构信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseOrg> getSelfAndChildOrgsByOrgSyscodeByHa(
			String orgSyscode, String orgIsdel, String haOrgId)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 查询指定类型编码的下级编码长度，若指定编码为空则返回第一级编码长度
	 * @author 喻斌
	 * @date Jul 14, 2009 9:21:12 AM
	 * @param ruleType -编码类型
	 * @param code -指定编码
	 * @return int 下级编码长度，若指定编码已是最大长度，则返回其本身长度
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int findChildCodeLength(String ruleType, String code)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 根据组织机构系统编码获取上级组织机构信息
	 * 
	 * @param orgSyscode -
	 *            组织机构系统编码
	 * @return TUumsBaseOrg - 上级组织机构信息BO,若没有上级则返回本级,若不存在该组织机构则返回null
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TUumsBaseOrg getParentOrgByOrgSyscode(String orgSyscode)
			throws DAOException, SystemException, ServiceException;
	
	/**
	 * 根据用户id获取组织机构信息。
	 * 
	 * @param userId -
	 *            用户id
	 * @return TUumsBaseOrg 组织机构信息BO,没有则返回null
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TUumsBaseOrg getOrgInfoByUserId(String userId) throws DAOException,
			SystemException, ServiceException;
	

	/**
	 * 根据岗位Id和组织机构Id和其他过滤条件获得用户信息列表。
	 *
	 * @author 喻斌
	 * @date Mar 13, 2009 9:04:03 AM
	 * @param postId
	 *            -岗位Id
	 * @param orgId
	 *            -组织机构Id
	 * @param userIsactive
	 *            -用户是否启用<br>
	 *            <p>
	 *            “0”：未启用
	 *            </p>
	 *            <p>
	 *            “1”：已启用
	 *            </p>
	 *            <p>
	 *            其他：全部
	 *            </p>
	 * @param userIsdel
	 *            -用户是否删除<br>
	 *            <p>
	 *            “0”：未删除
	 *            </p>
	 *            <p>
	 *            “1”：已删除
	 *            </p>
	 *            <p>
	 *            其他：全部
	 *            </p>
	 * @return List<TUumsBaseUser> 用户信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseUser> getUserInfosByPostIdAndOrgId(String postId,
			String orgId, String userIsactive, String userIsdel)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 根据组织机构编码和是否删除条件得到相应的组织机构信息
	 * @author 喻斌
	 * @date Jul 19, 2009 7:32:44 PM
	 * @param orgSyscode -组织机构编码
	 * @param orgIsdel -组织机构是否删除<br>
	 * 		<p>“0”：否；“1”：是；其他：全部</p>
	 * @return List<TUumsBaseOrg> 组织机构信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseOrg> getOrgsByOrgSyscode(String orgSyscode,
			String orgIsdel) throws DAOException, SystemException,
			ServiceException;
	/**
	 * 得到当前登录用户。
	 *
	 * @return User - 当前登录用户VO
	 */
	public UserInfo getCurrentUserInfo();

	/**
	 * 得到指定组织机构下的符合查询条件的分级授权用户分页信息
	 * @author 喻斌
	 * @date Nov 9, 2009 8:53:22 AM
	 * @param page
	 *            -分页对象
	 * @param orgId
	 *            -组织机构Id
	 * @param selectname
	 *            -用户名称查询条件
	 * @param selectloginname
	 *            -用户登录名查询条件
	 * @param isdel
	 *            -用户是否删除状态；“0”：未删除，“1”：已删除，其他：全部
	 * @param isActive
	 *            -用户是否启用状态；“0”：未删除，“1”：已删除，其他：全部
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return Page<TUumsBaseUser> 用户信息分页类
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseUser> queryOrgUsersByHa(Page<TUumsBaseUser> page,
			String orgId, String selectname, String selectloginname,
			String isdel, String isActive, String haOrgId) throws DAOException,
			SystemException, ServiceException;
	
	/**
	 * 得到指定组织机构下的分级授权用户分页信息
	 * @author 喻斌
	 * @date Nov 9, 2009 9:06:50 AM
	 * @param page -
	 *            分页信息
	 * @param orgId -
	 *            组织机构ID
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return Page -用户分页信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseUser> queryOrgUsersByHa(Page<TUumsBaseUser> page,
			String orgId, String haOrgId) throws DAOException, SystemException,
			ServiceException;

	/**
	 * 根据过滤条件得到所有分级授权用户列表
	 * @author 喻斌
	 * @date Nov 9, 2009 8:46:11 AM
	 * @param userIsdel -用户是否删除;“0”：未删除,“1”：已删除,其他：全部
	 * @param userIsactive -用户是否启用;“0”：未启用,“1”：已启用,其他：全部
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return List<TUumsBaseUser> 用户信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseUser> queryUsersByHa(String userIsdel,
			String userIsactive, String haOrgId) throws DAOException,
			SystemException, ServiceException;
	
	/**
	 * 根据过滤条件得到分级授权用户分页信息
	 * @author 喻斌
	 * @date Nov 9, 2009 9:04:43 AM
	 * @param page
	 *            -分页信息
	 * @param userIsdel
	 *            -用户是否删除；“0”：未删除，“1”：已删除，其他：全部
	 * @param userIsactive
	 *            -用户是否启用；“0”：未启用，“1”：已启用，其他：全部
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return Page<TUumsBaseUser> 用户分页信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseUser> queryUsersByHa(Page<TUumsBaseUser> page,
			String userIsdel, String userIsactive, String haOrgId)
			throws DAOException, SystemException, ServiceException;
	
	/**
	 * 根据过滤条件得到一页用户信息
	 *
	 * @author 喻斌
	 * @date Mar 13, 2009 2:52:02 PM
	 * @param page
	 *            -分页信息
	 * @param userIsdel
	 *            -用户是否删除;“0”：未删除,“1”：已删除,其他：全部
	 * @param userIsactive
	 *            -用户是否启用;“0”：未启用,“1”：已启用,其他：全部
	 * @return Page<TUumsBaseUser> 用户分页信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseUser> queryUsers(Page<TUumsBaseUser> page,
			String userIsdel, String userIsactive) throws DAOException,
			SystemException, ServiceException;
	

	/**
	 * 保存用户角色。
	 *
	 * @param userId -
	 *            用户Id
	 * @param roleIds -
	 *            角色Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveUserRoles(String userId, String roleIds)
			throws DAOException, SystemException, ServiceException;
	

	/**
	 * 根据用户ID获取用户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return com.strongit.oa.common.user.User 内部用户对象
	 */
	public TUumsBaseUser getBaseUserByUserId(String userId) throws SystemException;
	

	
	//action解耦代码接口重写
	
	/**
	 * 查询符合查询条件的委派信息
	 * 
	 * @author 喻斌
	 * @date Jul 19, 2009 4:48:42 PM
	 * @param page
	 *            -分页对象
	 * @param userUuid
	 *            -委托人Id
	 * @param srcName
	 *            -委托人姓名查询条件
	 * @param targetName
	 *            -被委托人姓名查询条件
	 * @param startDate
	 *            -开始时间查询条件
	 * @param endDate
	 *            -结束时间查询条件
	 * @param status
	 *            -规则是否生效查询条件
	 * @return Page<TUumsBaseAbrole> 分页对象
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseAbrole> queryHistoryRecords(
			Page<TUumsBaseAbrole> page, String userUuid, String srcName,
			String targetName, Date startDate, Date endDate, String status)
			throws DAOException, SystemException, ServiceException;
	
	/**
	 * 查询符合查询条件的委派信息
	 * 
	 * @author 喻斌
	 * @date Jul 19, 2009 4:48:42 PM
	 * @param userUuid
	 *            -委托人Id
	 * @param srcName
	 *            -委托人姓名查询条件
	 * @param targetName
	 *            -被委托人姓名查询条件
	 * @param startDate
	 *            -开始时间查询条件
	 * @param endDate
	 *            -结束时间查询条件
	 * @param status
	 *            -规则是否生效查询条件
	 * @return Page<TUumsBaseAbrole> 分页对象
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseAbrole> queryHistoryRecords(
			String userUuid, String srcName,
			String targetName, Date startDate, Date endDate, String status)
			throws DAOException, SystemException, ServiceException;


	/**
	 * 添加委派信息
	 * 
	 * @param aUser
	 *            A用户，委派人
	 * @param aUsername
	 *            A用户姓名
	 * @param bUser
	 *            B用户，被委派人
	 * @param bUsername
	 *            B用户姓名
	 * @param startDate
	 *            委派生效时间
	 * @param endDate
	 *            委派结束时间
	 * @param privilIds
	 *            A委派给B的资源权限,格式为资源Id,资源Id,...
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean doAppoint(String aUser, String aUsername, String bUser,
			String bUsername, Date startDate, Date endDate, String privilIds,String orgId)
			throws DAOException, SystemException, ServiceException;
	
	/**
	 * 根据人员Id得到被委托的资源权限信息
	 * @author 喻斌
	 * @date Jul 23, 2009 11:20:54 AM
	 * @param userId -人员Id
	 * @param isPrivilactive -资源权限是否启用<br>
	 * 		<p>“1”：启用；“0”：未启用；其他：全部</p>
	 * @return List<TUumsBasePrivil> 委托的资源权限信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePrivil> getAbrolePrivilByUserId(String userId, String isPrivilactive)
												throws DAOException, SystemException, ServiceException;
	
	/**
	 * 根据人员登录账号得到被委托的资源权限信息
	 * @author 喻斌
	 * @date Jul 23, 2009 11:20:54 AM
	 * @param userLoginname -人员登录账号
	 * @param isPrivilactive -资源权限是否启用<br>
	 * 		<p>“1”：启用；“0”：未启用；其他：全部</p>
	 * @return List<TUumsBasePrivil> 委托的资源权限信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePrivil> getAbrolePrivilByUserLoginname(String userLoginname, String isPrivilactive)
												throws DAOException, SystemException, ServiceException;
	
	/**
	 * 当用户登出时将登录时置为暂停的委托规则重新置为生效
	 * @author 喻斌
	 * @date Jul 23, 2009 2:24:01 PM
	 * @param userId -人员Id
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void resumeAbroleAfterLogout(String userId)
						throws DAOException, SystemException, ServiceException;

	/**
	 * 更新委派规则。
	 * 
	 * @param role
	 *            -bo对象
	 * @param privilCodes
	 *            -A委派给B的资源权限,格式为资源编码,资源编码,...
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean updateABRole(TUumsBaseAbrole role, String privilCodes)
			throws DAOException, SystemException, ServiceException;
	
	/**
	 * 取消委托规则
	 * @author 喻斌
	 * @date Jul 23, 2009 11:04:05 AM
	 * @param abroleIds -委托规则Id，格式为Id1,Id2,...
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void cancelAbrole(String abroleIds)
			throws DAOException, SystemException, ServiceException;
	
	/**
	 * 删除委托规则
	 * @author 喻斌
	 * @date Jul 23, 2009 11:04:05 AM
	 * @param abroleIds -委托规则Id，格式为Id1,Id2,...
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteAbrole(String abroleIds)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 根据委托规则Id得到资源权限委托信息
	 * 
	 * @author 喻斌
	 * @date Jul 8, 2009 2:48:08 PM
	 * @param abroleId
	 *            -委托规则Id
	 * @return TUumsBaseAbrole 委托信息，没有则返回null
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TUumsBaseAbrole getAbroleById(String abroleId) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 根据用户Id得到用户所拥有的应用系统
	 * 
	 * @author 喻斌
	 * @date Mar 13, 2009 3:46:46 PM
	 * @param userId
	 *            -人员Id
	 * @param isPrivilactive
	 *            -权限启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isGroupactive
	 *            -用户组启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isRoleactive
	 *            -角色启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @return List<TUumsBasePrivil> 权限信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseSystem> getPrivilSystems(String userId,
			String isPrivilactive, String isGroupactive, String isRoleactive)
			throws DAOException, SystemException, ServiceException;
	
	/**
	 * 根据人员登录账号得到被委托的分级授权资源权限信息
	 * @author 胡志文 huzw
	 * @param userLoginname -人员登录账号
	 * @param haOrgId -机构ID
	 * @param isPrivilactive -资源权限是否启用<br>
	 * 		<p>“1”：启用；“0”：未启用；其他：全部</p>
	 * @return List<TUumsBasePrivil> 委托的资源权限信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePrivil> getAbrolePrivilByUserLoginnameByHa(String userLoginname, String isPrivilactive,String haOrgId)
												throws DAOException, SystemException, ServiceException;
	

	/**
	 * 根据资源权限编码匹配所有的资源权限信息
	 * @author 喻斌
	 * @date Jul 11, 2009 10:33:40 AM
	 * @param privilCode -资源权限编码
	 * @param isPrivilactive -资源权限是否启用<br>
	 * 		<p>“1”：已启用；“0”：未启用；其它：全部情况</p>
	 * @return List<TUumsBasePrivil> 匹配的资源权限信息
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TUumsBasePrivil> getPrivilInfosByPrivilCode(String privilCode, String isPrivilactive)
			throws DAOException, ServiceException, SystemException;
	
	
	/**
	 * 根据过滤条件得到指定用户初始化委托规则权限信息列表。
	 * 
	 * @param userId -
	 *            用户id
	 * @param isPrivilactive
	 *            -权限启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isGroupactive
	 *            -用户组启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isRoleactive
	 *            -角色启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param privilSyscode -资源权限编码查询条件
	 * @param systemId -应用系统id查询条件
	 * @param abroleId -指定的委托规则Id
	 * @return List<TUumsBasePrivil> 权限信息集
	 */
	public List<TUumsBasePrivil> getInitDelegationPrivilInfoByUserId(String userId,
			String isPrivilactive, String isGroupactive,
			String isRoleactive, String privilSyscode, String systemId, String abroleId) throws DAOException,
			SystemException, ServiceException;

	public void saveBaseLog(TUumsBaseLog baseLog);
	
	public TUumsBaseLog getLogInfoById(String logId);
	
	public Page<TUumsBaseLog> queryBaseLog(Page<TUumsBaseLog> page,
			String username, String desc,String result);
	
	

	/**
	 * 删除指定的外挂系统信息。
	 * 
	 * @param sysIds -
	 *            外挂系统信息Ids
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteSystems(String sysIds) throws DAOException,
			SystemException, ServiceException;


	/**
	 * 根据查询条件得到系统信息分页对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-19 下午03:44:19
	 * @param page -分页对象
	 * @param selectsystemCode -系统编码查询条件
	 * @param selectsystemname -系统名称查询条件
	 * @param systemisact -系统是否启用查询条件<br>
	 * 		<p>“1”：启用； “0”：未启用； 其他：全部</p>
	 * @param startDate -开始时间查询条件
	 * @param endDate -结束时间查询条件
	 * @return Page<TUumsBaseSystem> 应用系统分页信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseSystem> querySystems(Page<TUumsBaseSystem> page,
			String selectsystemCode, String selectsystemname, String systemisact,
			Date startDate, Date endDate) throws DAOException, SystemException,
			ServiceException;

	/**
	 * 保存外挂系统信息。
	 * 
	 * @param bo -
	 *            外挂系统信息BO
	 * @param childTogether
	 *            -当外挂系统被启用时是否将系统下权限设置成启用状态<br>
	 *            <P>
	 *            “1”：是；“0”：否
	 *            </p>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveSystem(TUumsBaseSystem bo, String childTogether)
			throws DAOException, SystemException, ServiceException;


	/**
	 * 根据应用系统Id得到应用系统信息
	 * 
	 * @param sysId -
	 *            应用系统Id
	 * @return TUumsBaseSystem - 应用系统BO
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TUumsBaseSystem getSystemBySystemId(String sysId) throws DAOException,
			SystemException, ServiceException;
	

	/**
	 * 得到下一个应用系统排序号
	 * @author 喻斌
	 * @date Jun 26, 2009 3:18:35 PM
	 * @return Long 下一个应用系统排序号
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Long getNextSystemSequenceCode() throws DAOException, SystemException, ServiceException;
	

	/**
	 * 得到下一个排序号
	 * @author 喻斌
	 * @date Jun 26, 2009 3:18:35 PM
	 * @return Long 下一个应用系统排序号
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Long getNextOrgSequenceCode() throws DAOException, SystemException, ServiceException;

	/**
	 * 得到下一个Post排序号
	 * @author 喻斌
	 * @date Jun 26, 2009 3:18:35 PM
	 * @return Long 下一个应用系统排序号
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Long getNextPostSequenceCode() throws DAOException, SystemException, ServiceException;
	/**
	 * 得到下一个Privil排序号
	 * @author 喻斌
	 * @date Jun 26, 2009 3:18:35 PM
	 * @return Long 下一个应用系统排序号
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Long getNextPrivilSequenceCode() throws DAOException, SystemException, ServiceException;
	/**
	 * 得到下一个Group排序号
	 * @author 喻斌
	 * @date Jun 26, 2009 3:18:35 PM
	 * @return Long 下一个应用系统排序号
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Long getNextGroupSequenceCode() throws DAOException, SystemException, ServiceException;
	/**
	 * 得到下一个user排序号
	 * @author 喻斌
	 * @date Jun 26, 2009 3:18:35 PM
	 * @return Long 下一个应用系统排序号
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Long getNextUserSequenceCode() throws DAOException, SystemException, ServiceException;

	/**
	 * 检测org编码是否唯一
	 * @author 喻斌
	 * @date Jun 26, 2009 3:18:35 PM
	 * @param code -应用系统编码
	 * @return boolean
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean compareOrgCode(String code) throws DAOException, SystemException,
			ServiceException;
	/**
	 * 检测应用系统编码是否唯一
	 * @author 喻斌
	 * @date Jun 26, 2009 3:18:35 PM
	 * @param code -应用系统编码
	 * @return boolean
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean compareSystemCode(String code) throws DAOException, SystemException,
			ServiceException;
	/**
	 * 检测privil编码是否唯一
	 * @author 喻斌
	 * @date Jun 26, 2009 3:18:35 PM
	 * @param code -应用系统编码
	 * @return boolean
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean comparePrivilCode(String code) throws DAOException, SystemException,
			ServiceException;
	/**
	 * 检测role编码是否唯一
	 * @author 喻斌
	 * @date Jun 26, 2009 3:18:35 PM
	 * @param code -应用系统编码
	 * @return boolean
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean compareRoleCode(String code) throws DAOException, SystemException,
			ServiceException;
	/**
	 * 检测group编码是否唯一
	 * @author 喻斌
	 * @date Jun 26, 2009 3:18:35 PM
	 * @param code -应用系统编码
	 * @return boolean
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean compareGroupCode(String code) throws DAOException, SystemException,
			ServiceException;
	/**
	 * 检测user编码是否唯一
	 * @author 喻斌
	 * @date Jun 26, 2009 3:18:35 PM
	 * @param code -应用系统编码
	 * @return boolean
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean compareUserCode(String code) throws DAOException, SystemException,
			ServiceException;
	

	/**
	 * 根据查询条件得到分页岗位信息
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-19 下午03:45:01
	 * @param page
	 *            -分页信息
	 * @param postname
	 *            -岗位名称查询条件
	 * @param postdesc
	 *            -岗位描述查询条件
	 * @return Page<TUumsBasePost> 岗位信息分页类
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBasePost> queryPosts(Page<TUumsBasePost> page,
			String postname, String postdesc) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 保存岗位信息
	 * 
	 * @param boPost -
	 *            岗位信息BO
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void savePost(TUumsBasePost boPost) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 删除指定的岗位信息。
	 * 
	 * @param postIds -
	 *            岗位信息Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deletePosts(String postIds) throws DAOException,
			SystemException, ServiceException;
	

	/**
	 * 得到下一个组织结构代码。
	 * 
	 * @param code -
	 *            当前组织结构代码
	 * @return String - 下一个组织结构代码
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getNextOrgCode(String code) throws DAOException,
			SystemException, ServiceException;

    /**
     * 保存组织结构信息。
     * <br>2013-08-12 wencp：添加(注:仅新增)组织机构后，该组织机构会和所有全局岗位进行关联
     * @param boOrg -
     *            组织结构信息BO
     * @throws DAOException
     * @throws SystemException
     * @throws ServiceException
     */
	public void saveOrg(TUumsBaseOrg boOrg) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 删除指定的组织结构信息,同时删除组织机构下的人员和下级的组织机构</p>
	 * @param orgIds -
	 *            组织结构信息Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteOrgs(String orgIds) throws DAOException, SystemException,
			ServiceException;

	/**
	 * 根据过滤条件得到分页组织结构信息集
	 * 
	 * @author 喻斌
	 * @date Mar 12, 2009 5:15:36 PM
	 * @param page
	 *            -分页对象
	 * @param orgIsdel
	 *            -组织机构是否逻辑删除<br>
	 *            <p>
	 *            “0”：未逻辑删除;“1”：已逻辑删除;其他：全部
	 *            </p>
	 * @return Page<TUumsBaseOrg> 组织机构信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseOrg> queryOrgs(Page<TUumsBaseOrg> page, String orgIsdel)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 修改组织机构编码时同时对该组织机构下的子机构编码进行修改
	 * @author 喻斌
	 * @date Sep 29, 2009 9:43:01 AM
	 * @param boOrg -组织机构bo
	 * @param iscode -组织机构修改前的编码
	 * @param isOrg -1:机构变部门 0:部门变机构
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveOrgAndChildren(TUumsBaseOrg boOrg, String iscode,
			String isOrg) throws DAOException, SystemException,
			ServiceException;
	

	/**
	 * 根据区域代码得到区域信息
	 * 
	 * @author 喻斌
	 * @date Jun 19, 2009 5:19:58 PM
	 * @param areaCode
	 *            -区域代码
	 * @return TUumsBaseAreacode 区域编码信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TUumsBaseAreacode getAreaInfoByAreaCode(String areaCode)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 得到全部区划代码信息。
	 * 
	 * @return List - 区划代码信息列表
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseAreacode> getAllAreaCodes() throws DAOException,
			SystemException, ServiceException;
	
	/**
	 * 得到给定区划代码的子级代码
	 * @author 喻斌
	 * @date Jul 10, 2009 1:13:06 PM
	 * @param areacode -区划代码
	 * @return List<TUumsBaseAreacode> 子级区划代码信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseAreacode> getChildAreaCodes(String areacode)
			throws DAOException, SystemException, ServiceException;
	
	/**
	 * 根据组织机构Id得到组织机构下的岗位信息
	 * 
	 * @author 喻斌
	 * @date Jun 18, 2009 2:14:00 PM
	 * @param orgId
	 *            -组织机构Id
	 * @return List<TUumsBasePost> 岗位信息集
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TUumsBasePost> getPostInfoByOrgId(String orgId)
			throws DAOException, ServiceException, SystemException;

	/**
	 * 设置组织机构下的岗位信息
	 * @author 喻斌
	 * @date Jun 8, 2009 10:17:31 AM
	 * @param orgId -组织机构Id
	 * @param postWpIds -岗位Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveOrgPost(String orgId, String postWpIds)
			throws DAOException, SystemException, ServiceException;
	
	
	/**
	 * 根据查询条件得到岗位信息列表
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-19 下午03:45:01
	 * @param postname
	 *            -岗位名称查询条件
	 * @param postdesc
	 *            -岗位描述查询条件
	 * @return List<TUumsBasePost> 岗位信息分页类
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePost> queryPosts(String postname, String postdesc) throws DAOException,
			SystemException, ServiceException;
	

	/**
	 * 得到分页岗位信息。
	 * 
	 * @param page -
	 *            分页信息
	 * @return Page - 岗位信息分页类
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBasePost> queryPosts(Page<TUumsBasePost> page)
			throws DAOException, SystemException, ServiceException;
	

	/**
	 * 得到所有的岗位信息。
	 * 
	 * @return List<TUumsBasePost> 岗位信息列表
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePost> getAllPostInfos() throws DAOException,
			SystemException, ServiceException;

	/**
	 * 根据给定的岗位Id组得到岗位信息
	 * 
	 * @param postIds -
	 *            岗位信息Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @return List<TUumsBasePost> 岗位信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePost> getPostInfosByPostIds(String postIds)
			throws DAOException, SystemException, ServiceException;
	

	/**
	 * 根据组织机构编码格式和是否删除条件得到相应的分级授权组织机构信息
	 * @author 喻斌
	 * @date Nov 7, 2009 9:50:16 AM
	 * @param orgSyscode -组织机构编码，方法内不自动增加‘%’
	 * @param orgIsdel -组织机构是否删除；“0”：否；“1”：是；其他：全部
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return List<TUumsBaseOrg> 组织机构信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseOrg> getOrgsByOrgSyscodeByHa(String orgSyscode,
			String orgIsdel, String haOrgId) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 上下移动分级授权组织结构树中的节点位置
	 * @author 喻斌
	 * @date Nov 7, 2009 9:39:33 AM
	 * @param currentOrg -当前组织机构节点信息
	 * @param moveDownOrUp -上下移动标识<br>
	 *            <p>
	 *            down:下移（see com.strongit.uums.util.MOVE_DOWN_IN_TREE）
	 *            up:上移（see com.strongit.uums.util.MOVE_UP_IN_TREE）
	 *            </p>
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return String；移动成功：返回“”；移动不成功：返回不成功信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String moveOrgPositionInTreeByHa(TUumsBaseOrg currentOrg,
			String moveDownOrUp, final String haOrgId) throws DAOException,
			SystemException, ServiceException;
	
	/**
	 * 校验代码编码规则是否符合规范
	 * 
	 * @author 蒋国斌
	 * @date 2009-2-27 上午11:39:19
	 * @param code -组织机构编码
	 * @return
	 * @throws SystemException
	 * @throws DAOException
	 * @throws ServiceException
	 */
	public boolean checkCode(String code) throws DAOException, SystemException,
			ServiceException;
	
	/**校验用户组编码编码规则是否符合规范
	 * author:luosy 2014-1-18
	 * description:
	 * modifyer:
	 * description:
	 * @param code	用户组编码
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean checkGroupCode(String code) throws DAOException, SystemException,
			ServiceException,IOException;
	
	/**校验权限编码规则是否符合规范
	 * author:luosy 2014-1-18
	 * description:
	 * modifyer:
	 * description:
	 * @param code	权限编码
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws IOException 
	 */
	public boolean checkPrivilCode(String code) throws DAOException, SystemException,
			ServiceException, IOException;
	
	/**
	 * 判断组织机构系统编码的父级是否已存在
	 * @author 喻斌
	 * @date Apr 16, 2009 1:43:39 PM
	 * @param code -组织机构系统编码
	 * @param orgId -编辑时的组织机构Id
	 * @return boolean 父级元素是否已存在,最顶级元素的父级默认存在
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws IOException
	 */
	public boolean checkOrgParentIsExist(String code, String orgId)
			throws DAOException, SystemException, ServiceException;


	/**
	 * 判断组织机构系统编码的父级是否已存在
	 * @author 喻斌
	 * @date Apr 16, 2009 1:43:39 PM
	 * @param code -组织机构系统编码
	 * @param orgId -编辑时的组织机构Id
	 * @return boolean 父级元素是否已存在,最顶级元素的父级默认存在
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws IOException
	 */
	public boolean checkPrivilParentIsExist(String code, String orgId)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 判断组织机构系统编码的父级是否已存在
	 * @author 喻斌
	 * @date Apr 16, 2009 1:43:39 PM
	 * @param code -组织机构系统编码
	 * @param orgId -编辑时的组织机构Id
	 * @return boolean 父级元素是否已存在,最顶级元素的父级默认存在
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws IOException
	 */
	public boolean checkGroupParentIsExist(String code, String orgId)
			throws DAOException, SystemException, ServiceException;
	
	
	/**
	 * 还原已删除的组织机构
	 * 
	 * @author 喻斌
	 * @date Mar 13, 2009 1:57:39 PM
	 * @param orgIds
	 *            -组织机构Ids,格式为：机构Id,机构Id...
	 * @param userTogether -组织机构用户是否同时被还原<br>
	 * 		<p>“1”：是；“0”：否</p>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void reductOrg(String orgIds, String userTogether)
			throws DAOException, SystemException, ServiceException;
	

	/**
	 * 删除指定的权限信息。
	 * 
	 * @param privilIds -
	 *            权限信息Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deletePrivils(String privilIds) throws DAOException,
			SystemException, ServiceException;
	/**
	 * 判断用户是否具有管理员身份
	 *
	 * @author 喻斌
	 * @date Feb 19, 2009 11:36:03 AM
	 * @param userId
	 *            -用户Id
	 * @return boolean 是否具有管理员身份
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean checkUserIsManager(String userId) throws DAOException,
			SystemException, ServiceException;
	/**
	 * 得到下一个权限代码。
	 * 
	 * @param code -
	 *            当前权限代码
	 * @return String - 下一个权限代码
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getNextPrivilCode(String code) throws DAOException,
			SystemException, ServiceException;
	/**
	 * 根据是否启用条件得到外挂系统信息。
	 * 
	 * @author 喻斌
	 * @date Mar 13, 2009 9:50:31 AM
	 * @param systemIsactive
	 *            -系统是否启用<br>
	 *            <p>
	 *            “0”：未启用;“1”：已启用;其他：全部
	 *            </p>
	 * @return List<TUumsBaseSystem> 系统信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseSystem> querySystems(String systemIsactive)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 得到过滤后的一页权限信息。
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-13 上午11:13:46
	 * @param page
	 * @param isactive
	 *            过滤未启用的权限<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBasePrivil> queryPrivils(Page<TUumsBasePrivil> page,
			String isactive) throws DAOException,
			SystemException, ServiceException;
	

	/**
	 * 根据资源权限Id得到资源权限信息
	 * 
	 * @param privilId -
	 *            权限ID
	 * @return TUumsBasePrivil - 权限BO
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TUumsBasePrivil getPrivilInfoByPrivilId(String privilId) throws DAOException,
			SystemException, ServiceException;
	
	/**
	 * 根据资源类型Id得到资源类型信息
	 * @author 喻斌
	 * @date Jun 23, 2009 3:10:55 PM
	 * @param typeId -资源类型Id
	 * @return TUumsBasePrivilType 资源类型信息
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public TUumsBasePrivilType getPrivilTypeById(String typeId)
			throws DAOException, ServiceException, SystemException;

	/**
	 * 保存权限。
	 * 
	 * @param boPrivil -
	 *            权限BO
	 * @param childTogether
	 *            -是否将下级资源权限也设置成启用状态<br>
	 *            <P>
	 *            “1”：是; “0”：否
	 *            </p>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void savePrivil(TUumsBasePrivil boPrivil, String childTogether)
			throws DAOException, SystemException, ServiceException;
	

	/**
	 * 上下移动权限树中的节点位置
	 * 
	 * @author 喻斌
	 * @date Feb 11, 2009 9:44:23 AM
	 * @param currentPrivil
	 *            -当前权限节点信息
	 * @param moveDownOrUp
	 *            -上下移动标识<br>
	 *            <p>
	 *            down:下移 see com.strongit.uums.util.MOVE_DOWN_IN_TREE
	 *            </p>
	 *            <P>
	 *            up:上移 see com.strongit.uums.util.MOVE_UP_IN_TREE
	 *            </P>
	 * @return String <br>
	 *         <p>
	 *         移动成功：返回“”; 移动不成功：返回不成功信息
	 *         </p>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws IOException
	 */
	public String movePrivilPositionInTree(TUumsBasePrivil currentPrivil,
			String moveDownOrUp) throws DAOException, SystemException,
			ServiceException;
	
	/**
	 * 上下移动权限树中的节点位置
	 * 
	 * @author 喻斌
	 * @date Feb 11, 2009 9:44:23 AM
	 * @param currentPrivil
	 *            -当前权限节点信息
	 * @param moveDownOrUp
	 *            -上下移动标识<br>
	 *            <p>
	 *            down:下移 see com.strongit.uums.util.MOVE_DOWN_IN_TREE
	 *            </p>
	 *            <P>
	 *            up:上移 see com.strongit.uums.util.MOVE_UP_IN_TREE
	 *            </P>
	 * @return String <br>
	 *         <p>
	 *         移动成功：返回“”; 移动不成功：返回不成功信息
	 *         </p>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws IOException
	 */
	public String movePrivilPositionInPrivilTypeTree(TUumsBasePrivil currentPrivil,
			String moveDownOrUp) throws DAOException, SystemException,
			ServiceException;
	

	/**
	 * 根据资源权限编码得到父级权限信息
	 * 
	 * @author 喻斌
	 * @date Apr 6, 2009 10:31:56 AM
	 * @param privilSyscode
	 *            -权限系统编码
	 * @return TUumsBasePrivil 父级权限信息，若为最顶级则返回null
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TUumsBasePrivil getParentPrivilByPrivilSyscode(String privilSyscode)
			throws DAOException, SystemException, ServiceException;
	

	/**
	 * 得到资源权限类型的下一个排序号
	 * @author 喻斌
	 * @date Jun 23, 2009 5:50:33 PM
	 * @return Long 下一个排序号
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public Long getNextPrivilTypeSequence()
			throws DAOException, ServiceException, SystemException;

	/**
	 * 保存资源权限类型信息
	 * @author 喻斌
	 * @date Jun 23, 2009 6:02:34 PM
	 * @param privilType -资源权限类型实体类
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void savePrivilType(TUumsBasePrivilType privilType)
			throws DAOException, ServiceException, SystemException;

	/**
	 * 删除资源类型信息
	 * @author 喻斌
	 * @date Jun 24, 2009 9:40:33 AM
	 * @param ids -资源类型Ids, 形式为Id1,Id2,...,Idn
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void delPrivilTypes(String ids)
			throws DAOException, ServiceException, SystemException;
	

	/**
	 * 删除指定的角色信息。
	 * 
	 * @param roleIds -
	 *            角色信息Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteRoles(String roleIds) throws DAOException,
			SystemException, ServiceException;
	

	/**
	 * 根据查询条件得到角色信息
	 * @author 蒋国斌
	 * @date 2009-3-19 下午03:23:08 
	 * @param page -分页对象
	 * @param roleCode -角色编码查询条件
	 * @param selectrolename -角色名称查询条件
	 * @param roleisact -角色是否启用查询条件<br>
	 * 		<p>“0”：未启用;“1”：已启用;其他：全部</p>
	 * @param roledesc -角色描述查询条件
	 * @return Page<TUumsBaseRole> 角色分页信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseRole> queryRoles(Page<TUumsBaseRole> page,String roleCode,
			String selectrolename,String roleisact,String roledesc)
				throws DAOException, SystemException, ServiceException;

	/**
	 * 根据角色Id得到角色所对应的资源权限信息
	 * 
	 * @param roleId -
	 *            角色ID
	 * @param isPrivilactive-是否过滤未启用的权限<br>
	 * 		<p>“0”：未启用;“1”：已启用;其他：全部</p>
	 * @return List<TUumsBasePrivil> - 角色权限列表
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePrivil> getRolePrivilInfosByRoleId(String roleId, String isPrivilactive)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 获取匹配给定资源权限编码和应用系统的资源权限信息
	 * @author 喻斌
	 * @date Jul 11, 2009 10:42:13 AM
	 * @param privilCode -资源权限编号
	 * @param systemCode -应用系统编号
	 * @param isPrivilactive -资源权限是否启用<br>
	 * 		<p>“1”：已启用；“0”：未启用；其它：全部情况</p>
	 * @return List<TUumsBasePrivil> 匹配的资源权限信息
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TUumsBasePrivil> getPrivilInfosByPrivilCodeAndSystem(String privilCode, String systemCode, String isPrivilactive)
			throws DAOException, ServiceException, SystemException;

	/**
	 * 保存角色权限。
	 * 
	 * @param roleId -
	 *            角色Id
	 * @param privilCodes -
	 *            权限Codes，其中每个Code用逗号(,)做分割符，例如：code1,code2,code3,code4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveRolePrivilsByPrivilCodes(String roleId, String privilCodes)
			throws DAOException, SystemException, ServiceException;
	

	/**
	 * 复制角色权限。
	 * 
	 * @param sourceRoleIds -
	 *            源角色Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @param destRoleIds -
	 *            目的角色Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void copyRolePrivils(String sourceRoleIds, String destRoleIds)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 根据角色Id得到角色用户列表。
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-12 上午11:20:47
	 * @param roleId--角色ID
	 * @param isactive--是否过滤未启用的用户
	 * 		<p>“0”：未启用；“1”：已启用；其他：全部</p>
	 * @param isdel--是否过滤已删除的用户
	 * 		<p>“0”：未删除；“1”：已删除；其他：全部</p>
	 * @return List<TUumsBaseUser> 用户信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseUser> getRoleUsersByRoleId(String roleId,
		String isactive, String isdel) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 根据组织机构编码得到该级和所有分级授权子级组织机构的符合条件的分级授权人员列表信息
	 * @author 喻斌
	 * @date Nov 9, 2009 9:31:31 AM
	 * @param orgSyscode
	 *            -组织机构编码
	 * @param isUseractive
	 *            -人员是否启用；“1”：启用；“0”：停用；其他：所有
	 * @param isUserdel
	 *            -人员是否删除；“1”：删除；“0”：未删除；其他：所有
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return List<TUumsBaseUser> 人员Id
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseUser> getUsersOfOrgAndChildByOrgcodeByHa(
			String orgSyscode, String isUseractive, String isUserdel,
			String haOrgId) throws DAOException, SystemException,
			ServiceException;

	/**
	 * 保存角色用户。
	 * 
	 * @param roleId -
	 *            角色Id
	 * @param userIds -
	 *            用户Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveRoleUsers(String roleId, String userIds)
			throws DAOException, SystemException, ServiceException;
	/**
	 * 得到下一个角色排序号
	 * @author 喻斌
	 * @date Jun 23, 2009 9:25:02 AM
	 * @return Long 角色排序号
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Long getNextRoleSequence() throws DAOException, SystemException,
					ServiceException;

	/**
	 * 得到指定的角色。
	 * 
	 * @param roleId -
	 *            角色ID
	 * @return - 角色信息BO
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TUumsBaseRole getRoleInfoByRoleId(String roleId) throws DAOException,
			SystemException, ServiceException;
	

	/**
	 * 保存用户信息。
	 *
	 * @param boUser -
	 *            用户信息BO
	 */
	public void saveUser(TUumsBaseUser boUser) throws DAOException,
			SystemException, ServiceException;
	
	/**
	 * 统一用户最新调用的保存用户接口
	* 函数功能说明 
	* Jianggb  2014-3-5 
	* 修改者名字 修改日期 
	* 修改内容 
	* @param @param boUser
	* @param @param oldOrgIds
	* @param @throws DAOException
	* @param @throws SystemException
	* @param @throws ServiceException     
	* @return void    
	* @throws
	 */
	public void saveUserByOldOrgIds(TUumsBaseUser boUser, String oldOrgIds)
	               throws DAOException, SystemException, ServiceException;
	

	/**
	 * 保存用户信息。
	 *
	 * @param boUser -
	 *            用户信息BO
	 */
	public void saveUserForDel(TUumsBaseUser boUser) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 还原已删除的用户信息
	 *
	 * @author 喻斌
	 * @date Mar 13, 2009 3:14:51 PM
	 * @param userId
	 *            -用户Id,格式为Id,...,Id
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String reductUser(String userId) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 得到指定组织机构下的符合查询条件的用户信息
	 *
	 * @author 喻斌
	 * @date Jun 9, 2009 4:43:24 PM
	 * @param page
	 *            -分页对象
	 * @param orgId
	 *            -组织机构Id
	 * @param selectname
	 *            -用户名称查询条件
	 * @param selectloginname
	 *            -用户登录名查询条件
	 * @param isdel
	 *            -用户是否删除状态;“0”：未删除,“1”：已删除,其他：全部
	 * @param isActive
	 *            -用户是否启用状态;“0”：未删除,“1”：已删除,其他：全部
	 * @return Page<TUumsBaseUser> 用户信息分页类
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseUser> queryOrgUsers(Page<TUumsBaseUser> page,
			String orgId, String selectname, String selectloginname,
			String isdel, String isActive) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 根据查询条件得到一页用户信息
	 *
	 * @author 蒋国斌
	 * @date 2009-3-19 下午02:44:03
	 * @param page
	 *            -分页对象
	 * @param selectname
	 *            -用户名称查询条件
	 * @param selectloginname
	 *            -用户登录名查询条件
	 * @param selectorg
	 *            -用户所属组织机构名称
	 * @param isdel
	 *            -用户是否处于删除状态;“0”：未删除,“1”：已删除,其他：全部
	 * @param isActive
	 *            -用户是否处于启用状态;“0”：未删除,“1”：已删除,其他：全部
	 * @return Page<TUumsBaseUser> 用户分页信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseUser> queryUsers(Page<TUumsBaseUser> page,
			String selectname, String selectloginname, String selectorg,
			String isdel, String isActive) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 根据查询条件得到分级授权用户分页信息
	 * @author 喻斌
	 * @date Nov 9, 2009 8:49:15 AM
	 * @param page
	 *            -分页对象
	 * @param selectname
	 *            -用户名称查询条件
	 * @param selectloginname
	 *            -用户登录名查询条件
	 * @param selectorg
	 *            -用户所属组织机构名称
	 * @param isdel
	 *            -用户是否处于删除状态；“0”：未删除，“1”：已删除，其他：全部
	 * @param isActive
	 *            -用户是否处于启用状态；“0”：未删除，“1”：已删除，其他：全部
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return Page<TUumsBaseUser> 用户分页信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseUser> queryUsersByHa(Page<TUumsBaseUser> page,
			String selectname, String selectloginname, String selectorg,
			String isdel, String isActive, String haOrgId) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 保存应用系统管理员信息。
	 * 	<p>授权规则为：</p>
	 * <p>1. 若当前登录用户在应用系统中的权限等级比被授权的用户在该应用系统中的权限等级要低，则不能变更被授权用户的原有权限等级；</p>
	 * <p>2. 若当前登录用户在应用系统中的权限等级与被授权的用户相同或更高，则变更被授权用户的权限等级为设置的等级；</p>
	 * @param userId -用户Id
	 * @param authorizationInfo -授权信息，格式为：应用Id,权限等级,应用Id,权限等级,...,应用Id,权限等级
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return String 完成信息，若为null或“”则表示授权成功，否则为异常提示信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String saveSystemAdminInfo(String userId, String authorizationInfo, String haOrgId) throws DAOException, SystemException, ServiceException;
	

	/**
	 * 根据用户Id查询符合条件的该用户所属角色信息
	 * @author 喻斌
	 * @date Jun 18, 2009 11:34:01 AM
	 * @param userId -用户Id
	 * @param isRoleactive -角色是否启用<br>
	 * 		<p>“0”：未启用；“1”：启用；其他：全部</p>
	 * @return List<TUumsBaseRole> 角色信息集
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TUumsBaseRole> getUumsBaseRoleInfosByUserId(String userId, String isRoleactive) throws
		DAOException, ServiceException, SystemException;
	

	/**
	 * 根据用户Id查询符合条件的该用户所属用户组信息
	 * @author 喻斌
	 * @date Jun 18, 2009 11:34:01 AM
	 * @param userId -用户Id
	 * @param isGroupactive -用户组是否启用<br>
	 * 		<p>“0”：未启用；“1”：启用；其他：全部</p>
	 * @return List<TUumsBaseGroup> 用户组信息集
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TUumsBaseGroup> getGroupInfosByUserId(String userId, String isGroupactive) throws
		DAOException, ServiceException, SystemException;
	
	/**
	 * 删除指定的用户信息。
	 *
	 * @param userIds -
	 *            全用户信息Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 */
	public void deleteUsers(String userIds) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 得到过滤后的分级授权用户组列表信息
	 * @author 喻斌
	 * @date Nov 7, 2009 11:21:48 AM
	 * @param isactive -用户组是否启用
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return List<TUumsBaseGroup> 用户组列表信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseGroup> queryGroupsByHa(String isactive, String haOrgId)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 保存用户岗位。
	 * 2012-04-16 yubin：在岗位人员关联中增加组织机构id，以支持人员对应多组织机构的功能
	 *
	 * @param userId -
	 *            用户Id
	 * @param postIds -
	 *            岗位Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....；其中id1有两种方式：
	 *            <P>id1为岗位id，则表示该岗位id在用户默认组织机构下</P>
	 *            <P>id1为岗位id|组织机构id格式，则 表示岗位在给定的组织机构下</P>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveUserPosts(String userId, String postIds)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 保存用户用户组。
	 *
	 * @param userId -
	 *            用户Id
	 * @param groupIds -
	 *            用户组Ids，其中每个Code用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveUserGroups(String userId, String groupIds)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 得到指定Id用户权限信息列表，仅返回超级管理员、系统管理员和管理员的权限(适用于管理员为其他人员授权)
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-12 下午05:06:37
	 * @param userId -用户Id
	 * @param isPrivilactive
	 *            -权限启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isGroupactive
	 *            -用户组启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isRoleactive
	 *            -角色启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePrivil> getAdminPrivilInfosByUserId(String userId, String isPrivilactive,
			String isGroupactive, String isRoleactive) throws DAOException, SystemException,
			ServiceException;

	/**
	 * 保存用户权限。
	 *
	 * @param userId -
	 *            用户Id
	 * @param privilIds -
	 *            权限codes，其中每个Code用逗号(,)做分割符，例如：code1,code2,code3,code4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveUserPrivilsByPrivilCodes(String userId, String privilIds)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 复制用户权限。
	 *
	 * @param sourceUserIds -
	 *            源用户Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @param destUserIds -
	 *            目的用户Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void copyUserPrivils(String sourceUserIds, String destUserIds)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 得到全部外挂系统信息。
	 * 
	 * @return List - 外挂系统信息列表
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseSystem> getAllSystems() throws DAOException,
			SystemException, ServiceException;

	/**
	 * 检测用户登陆名唯一性
	 *
	 * @author 蒋国斌
	 * @date 2009-3-4 上午09:15:38
	 * @param loginName
	 * @return
	 */
	public boolean compareUserName(String loginName) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 根据用户Id得到用户的应用系统管理员信息
	 *
	 * @author 喻斌
	 * @date Jun 26, 2009 11:53:59 AM
	 * @param userId
	 *            -用户Id
	 * @return List<TUumsBaseSysmanager> 用户的应用系统管理员信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseSysmanager> getSystemAdminInfoByUserId(String userId)
			throws DAOException, SystemException, ServiceException;
	
	/**
	 * 得到指定Id用户在给定应用系统下的指定权限编码的权限信息列表，仅返回超级管理员、系统管理员和管理员的权限(适用于管理员为其他人员授权)
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-12 下午05:06:37
	 * @param userId -用户Id
	 * @param privilCode -资源权限编码
	 * @param systemCode -应用系统编码
	 * @param isPrivilactive
	 *            -权限启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isGroupactive
	 *            -用户组启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isRoleactive
	 *            -角色启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePrivil> getAdminPrivilInfosByUserAndPrivilAndSystem(String userId,
			String privilCode, String systemCode, String isPrivilactive,
			String isGroupactive, String isRoleactive) throws DAOException, SystemException,
			ServiceException;

	/**
	 * 得到指定Id用户的指定权限编码的权限信息列表，仅返回超级管理员、系统管理员和管理员的权限(适用于管理员为其他人员授权)
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-12 下午05:06:37
	 * @param userId -用户Id
	 * @param privilCode -资源权限编码
	 * @param isPrivilactive
	 *            -权限启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isGroupactive
	 *            -用户组启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isRoleactive
	 *            -角色启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePrivil> getAdminPrivilInfosByUserAndPrivil(String userId,
			String privilCode, String isPrivilactive,
			String isGroupactive, String isRoleactive) throws DAOException, SystemException,
			ServiceException;

	/**
	 * 删除指定的用户组信息。
	 * 
	 * @param groupIds -
	 *            用户组信息Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 */
	public void deleteGroups(String groupIds) throws DAOException,
			SystemException, ServiceException;
	/**
	 * 得到下一个用户组代码。
	 * 
	 * @param code -
	 *            当前用户组代码
	 * @return String - 下一个用户组代码
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getNextGroupCode(String code) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 得到全部的用户组信息。
	 * 
	 * @return List - 用户组信息列表
	 */
	public List<TUumsBaseGroup> getAllGroupInfos() throws DAOException,
			SystemException, ServiceException;

	/**
	 * 得到全部的分级授权用户组信息。
	 * @author 喻斌
	 * @date Nov 7, 2009 11:20:19 AM
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return List<TUumsBaseGroup> 用户组列表
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseGroup> getAllGroupInfosByHa(String haOrgId) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 得到指定用户组下的过滤后用户信息列表。
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-11 下午04:54:43
	 * @param groupId
	 *            用户组Id
	 * @param isactive
	 *            过滤未启用的用户
	 * @param isdel
	 *            过滤已删除的用户
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseUser> getGroupUsers(String groupId,
			String isactive, String isdel) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 保存组用户信息。
	 * 
	 * @param groupId -
	 *            组Id
	 * @param userIds -
	 *            用户Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveGroupUsers(String groupId, String userIds)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 得到指定用户组下过滤后的用户组权限信息。
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-12 上午09:13:35
	 * @param groupId
	 * @param isactive
	 *            过滤未启用的权限
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePrivil> getGroupPrivilInfos(String groupId,
			String isactive) throws DAOException, SystemException,
			ServiceException;

	/**
	 * 保存组权限。
	 * 
	 * @param groupId -
	 *            组Id
	 * @param privilIds -
	 *            权限编码s，其中每个Code用逗号(,)做分割符，例如：code1,code2,code3,code4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveGroupPrivilsByPrivilCodes(String groupId, String privilIds)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 复制用户组权限。
	 * 
	 * @param sourceGroupIds -
	 *            源用户组Ids，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @param destGroupIds -
	 *            目的用户组Codes，其中每个Id用逗号(,)做分割符，例如：id1,id2,id3,id4....
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void copyGroupPrivils(String sourceGroupIds, String destGroupIds)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 得到所有分级授权用户组下过滤后的用户分页信息
	 * @author 喻斌
	 * @date Nov 7, 2009 11:16:45 AM
	 * @param page -用户分页信息
	 * @param isactive -过滤未启用的用户
	 * @param isdel -过滤已删除的用户
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseUser> getAllGroupUsersByHa(Page<TUumsBaseUser> page,
			String isactive, String isdel, String haOrgId) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 得到指定用户组下过滤后的一页用户信息。
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-11 下午05:12:12
	 * @param page
	 *            用户分页信息
	 * @param groupId
	 *            用户组Id
	 * @param isactive
	 *            过滤未启用的用户
	 * @param isdel
	 *            过滤已删除的用户
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<TUumsBaseUser> getGroupUsers(Page<TUumsBaseUser> page,
			String groupId, String isactive, String isdel) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 得到指定的用户组信息。
	 * 
	 * @param id -
	 *            用户组信息Id
	 * @return TUumsBaseGroup - 用户组信息
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TUumsBaseGroup getGroupInfoByGroupId(String id) throws DAOException,
			SystemException, ServiceException;

	/**
	 * 保存用户组信息(分级授权机制)
	 * @author 喻斌
	 * @date Nov 9, 2009 3:40:25 PM
	 * @param boGroup -
	 *            用户组信息BO
	 * @param childTogether
	 *            -是否将下级用户组也设置成启用状态；“1”：是; “0”：否
	 * @param haOrgId -用于分级授权的组织机构Id
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveGroupByHa(TUumsBaseGroup boGroup, String childTogether, String haOrgId)
			throws DAOException, SystemException, ServiceException;

	/**
	 * 上下移动用户组树中的节点位置
	 * 
	 * @author 蒋国斌
	 * @date Feb 11, 2009 10:44:23 AM
	 * @param currentGroup
	 *            -当前用户组节点信息
	 * @param moveDownOrUp
	 *            -上下移动标识<br>
	 *            <p>
	 *            down:下移 see com.strongit.uums.util.MOVE_DOWN_IN_TREE
	 *            </p>
	 *            <P>
	 *            up:上移 see com.strongit.uums.util.MOVE_UP_IN_TREE
	 *            </P>
	 * @return String <br>
	 *         <p>
	 *         移动成功：返回“”; 移动不成功：返回不成功信息
	 *         </p>
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws IOException
	 */
	public String moveGroupInTree(TUumsBaseGroup currentGroup,
			String moveDownOrUp) throws DAOException, SystemException,
			ServiceException;

	/**
	 * 根据系统编码得到父级用户组信息
	 * 
	 * @author 喻斌
	 * @date Apr 6, 2009 10:31:56 AM
	 * @param groupSyscode
	 *            -用户组系统编码
	 * @return TUumsBaseGroup 父级用户组信息,最顶级则返回null
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public TUumsBaseGroup getParentGroupByGroupSyscode(String groupSyscode)
			throws DAOException, SystemException, ServiceException;


	/**
	 * 根据用户ID获取用户所属的所有组织机构信息
	 * <br>
	 * creator: 胡志文 <br>
	 * create date: Apr 17, 2012
	 * @param userId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBaseOrg> getOrgInfosByUserId(String userId)
			throws DAOException, SystemException, ServiceException;

	public List<TUumsBaseOrg> getOrgInfosByUserIdByHa(String userId,
			String orgId);

	/**
	 * 根据用户登录名以及机构ID得到指定用户分级授权权限信息列表
	 * @author 胡志文 huzw
	 * @param username -
	 *            用户登录名
	 * @param isPrivilactive
	 *            -权限启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isGroupactive
	 *            -用户组启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @param isRoleactive
	 *            -角色启用过滤<br>
	 *            <p>
	 *            “0”：未启用; “1”：已启用; 其他：全部
	 *            </p>
	 * @return List - 用户权限信息列表
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePrivil> getPrivilInfosByUserLoginNameByHa(
			String userLoginname, String isYes, String isYes2, String isYes3,
			String currentOrgId);

	/**
	 * 修改当前用户的权限
	 *
	 * @param newPrivils
	 *            格式为privil.getBasePrivilType().getBaseSystem().getSysSyscode()+
	 *            "-" + privil.getPrivilSyscode() 需要替换的权限
	 */
	public void modifyCurrentUserPrivil(List<String> authsList);
	

	/**
	 * 根据用户id获取分级授权下岗位信息。
	 * @author 喻斌
	 * @date 2012-4-18 上午02:13:57
	 * @param userId -用户Id
	 * @param haOrgId -分级授权组织机构Id
	 * @return List<TUumsBasePost> 岗位人员关联信息集
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<TUumsBasePostUser> getPostInfoByUserIdByHa(String userId, String haOrgId)
				throws DAOException, SystemException, ServiceException;
	

	/**
	 * 根据多个组织机构Id得到组织机构下的岗位信息
	 * @author 喻斌
	 * @date 2012-4-18 上午06:24:17
	 * @param orgIds
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<TUumsBasePostOrg> getPostInfoByOrgIds(List<String> orgIds)
				throws DAOException, ServiceException, SystemException;
	
	/**
	 * 根据用户ID获得该用户拥有的用户角色关联信息
	* 函数功能说明 
	* Jianggb  2014-3-6 
	* 修改者名字 修改日期 
	* 修改内容 
	* @param @param userId
	* @param @param haOrgId
	* @param @return
	* @param @throws DAOException
	* @param @throws SystemException
	* @param @throws ServiceException     
	* @return List<TUumsBaseRoleUser>    
	* @throws
	 */
	public List<TUumsBaseRoleUser> getUserToRoleInfosByUserIdByHa(String userId,String haOrgId)
               throws DAOException, SystemException, ServiceException;
	/**
	 * 根据用户Id查询在分级授权模式下符合条件的用户用户组关联信息
	* 函数功能说明 
	* Jianggb  2014-3-7 
	* 修改者名字 修改日期 
	* 修改内容 
	* @param @param userId
	* @param @param haOrgId
	* @param @return
	* @param @throws DAOException
	* @param @throws ServiceException
	* @param @throws SystemException     
	* @return List<TUumsBaseGroupUser>    
	* @throws
	 */
	public List<TUumsBaseGroupUser> getUserToGroupInfosByUserId(String userId, String haOrgId) throws
	DAOException, ServiceException, SystemException;
	
	/**
	 * 验证身份证是否唯一
	* 函数功能说明 
	* Jianggb  2014-3-7 
	* 修改者名字 修改日期 
	* 修改内容 
	* @param @param userId
	* @param @param haOrgId
	* @param @return
	* @param @throws DAOException
	* @param @throws ServiceException
	* @param @throws SystemException     
	* @return List<TUumsBaseGroupUser>    
	* @throws
	 */
	public abstract boolean compareRest4(String paramString1, String paramString2)
	    throws DAOException, SystemException, ServiceException;
}


