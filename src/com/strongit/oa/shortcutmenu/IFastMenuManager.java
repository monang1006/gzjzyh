/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：菜单快捷组接口
 */
package com.strongit.oa.shortcutmenu;

import java.util.List;

import org.springframework.security.userdetails.UserDetails;

import com.strongit.oa.bo.ToaSystemmanageUserFastmen;
import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.common.user.model.User;
import com.strongit.uums.bo.TUumsBasePrivil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * @author pengxq
 * @version 1.0
 */
public interface IFastMenuManager {

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:38:29
	 * @desc: 根据用户获取快捷菜单列表
	 * @param String
	 *            userid 用户id
	 * @return 快捷菜单列表
	 */
	public List<ToaUumsBaseOperationPrivil> getFastMenu(UserDetails user)
			throws SystemException, ServiceException;

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:39:35
	 * @desc: 根据快捷菜单主键获取对象
	 * @param String
	 *            id 主键
	 * @return 对象
	 */
	public ToaSystemmanageUserFastmen getRecord(String id)
			throws SystemException, ServiceException;

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:41:06
	 * @desc: 获取用户的快捷菜单对象
	 * @param String
	 *            userid 用户id
	 * @return 快捷菜单对象
	 */
	public ToaSystemmanageUserFastmen getRecords(String userid)
			throws SystemException, ServiceException;

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:49:46
	 * @desc: 保存快捷菜单设置
	 * @param obj
	 *            快捷菜单对象
	 * @return void
	 */
	public String saveFastMenu(ToaSystemmanageUserFastmen obj)
			throws SystemException, ServiceException;

	/**
	 * @author：pengxq
	 * @time：2009-1-6下午01:51:40
	 * @desc: 获取当前用户
	 * @param
	 * @return
	 */
	public User getCurrentUser();

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:38:29
	 * @desc: 根据用户获取备选菜单列表
	 * @param String
	 *            userid 用户id
	 * @return 备选菜单列表
	 */
	public List getMenuList(UserDetails user,ToaSystemmanageUserFastmen obj) throws SystemException,
			ServiceException;
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-2-27下午07:11:30
	  * @desc: 展现上导航前调用此方法
	  * @param
	  * @return
	 */
	public List<ToaUumsBaseOperationPrivil> showToolbarMenu(UserDetails user)
			throws SystemException, ServiceException;
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-2-27下午07:08:42
	  * @desc: 获取系统默认快捷菜单设置记录
	  * @param
	  * @return ToaSystemmanageUserFastmen 系统默认快捷菜单对象
	 */
	public ToaSystemmanageUserFastmen getSystemSetting()
			throws SystemException, ServiceException;
	
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-2-27下午07:11:30
	  * @desc: 根据传入的对象获取快捷菜单
	  * @param
	  * @return
	 */
	public List getMenuList(ToaSystemmanageUserFastmen obj);
}
