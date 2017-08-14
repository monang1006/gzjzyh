package com.strongit.xxbs.service;

import java.util.List;
import java.util.Map;

import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IOrgService
{
	Map<String, String> getOrgNames()
			throws DAOException, SystemException, ServiceException;

	Map<String, TUumsBaseOrg> getOrgs(String[] orgIds)
			throws DAOException, SystemException, ServiceException;

	Page<TUumsBaseOrg> getOrgs(Page<TUumsBaseOrg> page, String[] orgIds)
			throws DAOException, SystemException, ServiceException;
	
	public String[] getOrgIds()
			throws DAOException, SystemException, ServiceException;
	
	public String getOrgGroup(String userId)
			throws DAOException, SystemException, ServiceException;

	public String getOrgIdByMail(String mail)
			throws DAOException, SystemException, ServiceException;

	public Page<TUumsBaseOrg> findAllOrg(Page<TUumsBaseOrg> page)
	throws DAOException, SystemException, ServiceException;
	
	public List<TUumsBaseOrg> getOrgsById(String[] orgIds)
	throws DAOException, SystemException, ServiceException;
	
	public void updateOrg(TUumsBaseOrg org)
	throws ServiceException,SystemException, DAOException;
	
	public Page<TUumsBaseOrg> findOrgByTitle(Page<TUumsBaseOrg> page, String orgName)
	throws DAOException, SystemException, ServiceException;
	
	public String[] getOrgIdsByisOrg(String isOrg)
	throws DAOException, SystemException, ServiceException;
	
	public List<TUumsBaseOrg> getOrgNames1()
	throws DAOException, SystemException, ServiceException;
	
	public List<TUumsBaseOrg> getOrgIdsByisOrg1(String isOrg)
	throws DAOException, SystemException, ServiceException;
	
	public void saveOrgAndChildren(TUumsBaseOrg boOrg, String iscode)
	throws DAOException, SystemException, ServiceException;
	
	public List<?> getOrgtree(String flag)
	throws DAOException, SystemException, ServiceException;
	
	public List<?> getOrgtree1(String flag)
	throws DAOException, SystemException, ServiceException;
	/*
	 * 获得比当前用户排序号大的所选机构所有用户按升序排列
	 * 
	 */
	public List<?> getMaxUserup(Long seq,String orgId)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 获得比当前用户排序号小的所选机构所有用户按降序排列
	 * 
	 */
	public List<?> getMaxUserdown(Long seq,String orgId)
	throws ServiceException,SystemException, DAOException;
	
	public void updateUser(TUumsBaseUser user)
	throws ServiceException,SystemException, DAOException;
	
	public TUumsBaseOrg getOrgById(String orgId)
	throws DAOException, SystemException, ServiceException;
	//得到省直部门和驻外办的机构ID
	public String[] getDeptOrgIds()
	throws DAOException, SystemException, ServiceException;
	
	public String[] getOrgIdsBySubmit()
	throws DAOException, SystemException, ServiceException;
}
