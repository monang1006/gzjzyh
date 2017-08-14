/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：界面设置实现类
*/

package com.strongit.oa.theme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.strongit.oa.bigant.SQlServerHelper;
import com.strongit.oa.bo.ToaSysmanageBase;
import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.bo.ToaView;
import com.strongit.oa.shortcutmenu.IFastMenuManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
@OALogger
public class ThemeManager implements IThemeManager 
{
   
   private IFastMenuManager manager;	//快捷菜单manager
   
   private Connection con; 
   
	@Autowired
	SessionFactory sessionFactory; // 提供session
	
   private GenericDAOHibernate<ToaSysmanageBase, java.lang.String> themeDao;
   
   /**
    * @roseuid 494857FE02EE
    */
   public ThemeManager() 
   {
	  
   }
   
   /**
    * @param sessionFactory
    * @roseuid 494854950242
    */
   @Autowired
   public void setSessionFactory(SessionFactory sessionFactory) 
   {
	   themeDao=new GenericDAOHibernate<ToaSysmanageBase, java.lang.String>(sessionFactory,ToaSysmanageBase.class);
   }
   
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
   public ToaSysmanageBase getThemeByUser(String userId)throws SystemException,ServiceException{
	   ToaSysmanageBase obj=null;
	   try{
		   List<ToaSysmanageBase> list=themeDao.find("from ToaSysmanageBase as obj where obj.userId=?", userId);
		   if(list.size()>=1){
			   obj=list.get(0);
		   }
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.find_error,new Object[] { "界面设置对象" });
	   }
	   return obj;
   }
   
   /**
    * @author：pengxq
    * @time：2009-1-5下午02:17:24
    * @desc: 根据主键id获取界面设置对象
    * @param String themeId 界面设置主键id
    * @return 界面设置对象
   */
   public ToaSysmanageBase getTheme(String themeId)throws SystemException,
	ServiceException 
   {
	   ToaSysmanageBase obj=null;
	   try{
		   obj=themeDao.get(themeId);	
		  }catch(ServiceException e){
			  throw new ServiceException(MessagesConst.find_error,
						new Object[] { "界面设置对象" });
		  }   
	   return obj;
   }

   /**
    * @author：pengxq
    * @time：2009-1-5下午02:17:24
    * @desc: 获取界面设置对象
    * @param 
    * @return 界面设置对象
   */
   public ToaSysmanageBase getTheme() throws SystemException,
	ServiceException
   {
	   ToaSysmanageBase obj=null;
	   try{
		   List<ToaSysmanageBase> list=themeDao.find("from ToaSysmanageBase as obj where obj.userId is null",null);	
		   if(list.size()>0){
			   obj=list.get(0);
		   }
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.find_error,
					new Object[] { "界面设置对象" });
	   }   
	   return obj;
   }

   /**
    * @author：pengxq
    * @time：2009-1-5下午02:17:24
    * @desc: 保存界面设置
    * @param toaSysmanageBase 界面设置对象
    * @return void
   */
   public String saveTheme(ToaSysmanageBase toaSysmanageBase) throws SystemException,
	ServiceException
   {
	  String msg="";
	  try{
		  themeDao.save(toaSysmanageBase);	
	  }catch(ServiceException e){
		  msg="保存界面设置失败！";
		  throw new ServiceException(MessagesConst.save_error,
					new Object[] { "界面设置对象" });
	  }   
	  return msg;
   }
   
   /**
    * @author：pengxq
    * @time：2009-1-5下午02:17:24
    * @desc: 获取用户的快捷菜单列表
    * @param UserDetails UserDetails 用户信息
    * @return 快捷菜单列表
   */
   public List<ToaUumsBaseOperationPrivil> getFastMenuList(UserDetails UserDetails) throws SystemException,
	ServiceException{
	   List<ToaUumsBaseOperationPrivil> list=null;
	   try{
		   	list= manager.showToolbarMenu(UserDetails);	
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.find_error,
					new Object[] { "快捷菜单列表" });
	   }   
	   return list;
   }

   /**
    * @author：lihao
    * @time：2013-8-12 20:04:28
    * @desc: 主题重置
    * @param 
    * @return 
   */
   public void reset() throws SystemException,
	ServiceException
   { 
		PreparedStatement ps = null;
		con = this.getConnection();
		if(con != null){
			try 
			{
				ps = con.prepareStatement("DELETE FROM T_OA_SYSMANAGE_BASE WHERE USERID IS NOT NULL");
				ps.executeQuery();
				if(ps!=null) ps.close();
				if(con!=null) con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	
}
	/**
	 * 为子类提供session,方便jdbc相关操作
	 * 
	 * @author:lihao
	 * @date:2013年8月14日20:24:47
	 * @return Hibernate session.
	 */
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * 得到数据库连接
	 * 
	 * @author:lihao
	 * @date:2013年8月14日20:24:47
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected Connection getConnection() {
		return getSession().connection();
	}
@Autowired
   public void setManager(IFastMenuManager manager) {
	   this.manager = manager;
   }
}
