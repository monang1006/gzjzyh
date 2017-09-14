package com.strongit.gzjzyh.policeregister;

import java.util.Date;

import com.strongit.gzjzyh.po.TGzjzyhUserExtension;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IPoliceRegisterManager {
	
	/**
	 * 根据用户Id获取警官用户信息
	 * 
	 * @param userId -用户Id
	 * @return 警官用户信息
	 * @throws SystemException
	 */
	public TGzjzyhUserExtension getUserExtensionByUserId(String userId) throws SystemException;
	
	/**
	 * 根据警官Id获取警官用户信息
	 * 
	 * @param ueId -警官Id
	 * @return 警官用户信息
	 * @throws SystemException
	 */
	public TGzjzyhUserExtension getUserExtensionById(String ueId) throws SystemException;
	
	/**
	 * 保存警官用户信息
	 * 
	 * @param userExtension -警官用户信息
	 * @throws SystemException
	 */
	public void save(TGzjzyhUserExtension userExtension) throws SystemException;
	
	/**
	 * 审核警官用户信息
	 * 
	 * @param userExtension -警官用户信息
	 * @throws SystemException
	 */
	public void audit(TGzjzyhUserExtension userExtension) throws SystemException;
	
	/**
	 * 查询警官用户分页信息
	 * 
	 * @param page -分页对象
	 * @param searchLoginName -用户登录名
	 * @param searchName -用户姓名
	 * @param searchStatus -审核状态
	 * @param appStartDate -申请起始日期
	 * @param appEndDate -申请结束日期
	 * @return 警官用户分页信息
	 * @throws SystemException
	 */
	public Page<TGzjzyhUserExtension> queryApplyPage(
			Page page, String searchLoginName, String searchName, String searchStatus,
			Date appStartDate, Date appEndDate) throws SystemException;
	
}
