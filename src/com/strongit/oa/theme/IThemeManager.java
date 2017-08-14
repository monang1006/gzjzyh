/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：界面设置接口
*/
package com.strongit.oa.theme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.security.userdetails.UserDetails;

import com.strongit.oa.bo.ToaSysmanageBase;
import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IThemeManager 
{
   
	/**
	 * 
	 * @author：yuhz
	 * @time： May 11, 20094:13:56 PM
	 * @desc:  根据用户ID获取登录用户的界面配置信息
	 * @param  userId
	 * @return 界面设置对象
	 * @throws SystemException
	 * @throws ServiceException ToaSysmanageBase
	 */
	public ToaSysmanageBase getThemeByUser(String userId)throws SystemException,
	ServiceException;
	
   /**
	* @author：pengxq
	* @time：2009-1-5下午02:17:24
	* @desc: 根据主键id获取界面设置对象
	* @param String themeId 界面设置主键id
	* @return 界面设置对象
   */	   
   public ToaSysmanageBase getTheme(String themeId)throws SystemException,
	ServiceException;
   
   /**
    * @author：pengxq
    * @time：2009-1-5下午02:17:24
    * @desc: 获取界面设置对象
    * @param 
    * @return 界面设置对象
   */
   public ToaSysmanageBase getTheme()throws SystemException,
	ServiceException;
   
  /**
    * @author：pengxq
    * @time：2009-1-5下午02:17:24
    * @desc: 保存界面设置
    * @param toaSysmanageBase 界面设置对象
    * @return void
   */
   
   public String saveTheme(ToaSysmanageBase toaSysmanageBase)throws SystemException,
	ServiceException;
   
   /**
    * @author：pengxq
    * @time：2009-1-5下午02:17:24
    * @desc: 获取用户的快捷菜单列表
    * @param UserDetails UserDetails 用户信息
    * @return 快捷菜单列表
   */
   public List<ToaUumsBaseOperationPrivil> getFastMenuList(UserDetails UserDetails)throws SystemException,
	ServiceException;
   
   /**
    * @author：lihao
    * @time：2013-8-12 20:04:28
    * @desc: 主题重置
    * @param 
    * @return 
   */
   public void reset() throws SystemException,
	ServiceException;
}
