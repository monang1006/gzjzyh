/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：菜单快捷组实现类
 */
package com.strongit.oa.shortcutmenu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.bo.Channel;
import com.strongit.oa.bo.ToaSystemmanageUserFastmen;
import com.strongit.oa.bo.ToaUumsBaseOperationPrivil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.optprivilmanage.BaseOptPrivilManager;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
public class FastMenuManager implements IFastMenuManager {
	private GenericDAOHibernate<ToaSystemmanageUserFastmen, java.lang.String> fastMenuDao;// 快捷菜单dao

	private IUserService userService; // 用户接口

	private BaseOptPrivilManager optprivilmanager;

	@Autowired
	public void setOptprivilmanager(BaseOptPrivilManager optprivilmanager) {
		this.optprivilmanager = optprivilmanager;
	}

	/**
	 * @roseuid 494AFF6803A9
	 */
	public FastMenuManager() {

	}

	/**
	 * @param sessionFactory
	 * @roseuid 494AFD5A0261
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		fastMenuDao = new GenericDAOHibernate<ToaSystemmanageUserFastmen, String>(
				sessionFactory, ToaSystemmanageUserFastmen.class);
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:38:29
	 * @desc: 根据用户获取快捷菜单列表
	 * @param UserDetails
	 *            user 用户信息
	 * @return 快捷菜单列表
	 */
	public List<ToaUumsBaseOperationPrivil> getFastMenu(UserDetails user)
			throws SystemException, ServiceException {
		try {
			User currUser = userService.getCurrentUser();
			String userid = currUser.getUserId();
			List<ToaUumsBaseOperationPrivil> list = new ArrayList<ToaUumsBaseOperationPrivil>();// 获取该用户的快捷菜单列表
			ToaSystemmanageUserFastmen obj = this.getRecords(userid);	
			list=getMenuList(obj);
			return list;
		}catch (ServiceException e) {
			 throw new ServiceException(MessagesConst.find_error,
						new Object[] { "快捷菜单列表" });
		}
	}

	/**
	 * 
	  * @author：pengxq
	  * @time：2009-2-27下午07:11:30
	  * @desc: 根据传入的对象获取快捷菜单
	  * @param
	  * @return
	 */
	public List<ToaUumsBaseOperationPrivil> getMenuList(ToaSystemmanageUserFastmen obj){
		List<ToaUumsBaseOperationPrivil> list = new ArrayList<ToaUumsBaseOperationPrivil>();// 获取该用户的快捷菜单列表
		try {
			if (obj != null) {
				String modleId = obj.getModerIds();
				if (modleId != null && !"".equals(modleId)
						&& !"null".equals(modleId)) {
					String[] modleIds = modleId.split(",");
					// list=this.getList(modleId,"yes");
					//TUumsBasePrivil privil = new TUumsBasePrivil();
					for (String modleId1 : modleIds) {// 获取模块对象，并保存到list中
						/*privil = PrivilHelper.getPrivil(this
								.getValueFromPro("sysSysCode")
								+ "-" + modleId1);*/
						String sysCode = this
						.getValueFromPro("sysSysCode")
						+ "-" + modleId1;
						if(optprivilmanager.checkPrivilBySysCode(sysCode))
							list.add(optprivilmanager.getPrivilInfoByPrivilSyscode(modleId1));
					}
				}
			}
		} catch (ServiceException e) {
			 throw new ServiceException(MessagesConst.find_error,
						new Object[] { "快捷菜单列表" });
		}
		return list;
	}
	
	/**
	 * 
	  * @author：pengxq
	  * @time：2009-2-27下午07:11:30
	  * @desc: 展现上导航前调用此方法
	  * @param
	  * @return
	 */
	public List<ToaUumsBaseOperationPrivil> showToolbarMenu(UserDetails user)
			throws SystemException, ServiceException {
		try {
			User currUser = userService.getCurrentUser();
			String userid = currUser.getUserId();
			List<ToaUumsBaseOperationPrivil> list = new ArrayList<ToaUumsBaseOperationPrivil>();// 获取该用户的快捷菜单列表
			ToaSystemmanageUserFastmen obj = this.getRecords(userid);	
			if(obj==null){
				obj=this.getSystemSetting();
			}
			list=getMenuList(obj);
			return list;
		}catch (ServiceException e) {
			 throw new ServiceException(MessagesConst.find_error,
						new Object[] { "快捷菜单列表" });
		}
	}
	
	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:38:29
	 * @desc: 根据用户获取备选菜单列表
	 * @param UserDetails
	 *            user 用户信息
	 * @return 备选菜单列表
	 */
	public List getMenuList(UserDetails user,ToaSystemmanageUserFastmen obj) throws SystemException,
	ServiceException {
		List<ToaUumsBaseOperationPrivil> privilList = new ArrayList<ToaUumsBaseOperationPrivil>(); // 获取该用户的快捷菜单列表
		List<ToaUumsBaseOperationPrivil> AllList=new ArrayList<ToaUumsBaseOperationPrivil>();
		String modleId = ""; // 模块编码串
		String[] modleIds = null;
		try {
			AllList = optprivilmanager.getCurrentUserPrivilLstWithLink();// 获取当前用户权限以及带链接的权限列表
			User currUser = userService.getCurrentUser(); // 当前用户
			String userid = currUser.getUserId(); // 用户id
			ToaUumsBaseOperationPrivil privilobj;
			if (obj != null) {
				modleId = obj.getModerIds();
				if (modleId != null)
					modleIds = modleId.split(",");
			}
			String code = ""; // 权限CODE
			for(int i=0;i<AllList.size();i++){
				privilobj=AllList.get(i);
				code = privilobj.getPrivilSyscode(); 
				String dec = privilobj.getPrivilDescription();
				if((dec != null && dec.startsWith("个人桌面使用")) || "个人桌面".equals(privilobj.getPrivilName())) { 
					continue;
				}
				if (modleIds != null && modleIds.length > 0) {
					int k = 0;
					for (int j = 0; j < modleIds.length; j++) {
						if (code != null && code.equals(modleIds[j])) {
							k++;
						}
					}
					if (k == 0) {
						privilList.add(privilobj);
					}
				}	    						
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "备选菜单列表" });
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (obj != null && modleId != null && !"".equals(modleId)) {
			return privilList;
		} else {
			return AllList;
		}
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:49:46
	 * @desc: 保存快捷菜单设置
	 * @param obj
	 *            快捷菜单对象
	 * @return void
	 */
	public String saveFastMenu(ToaSystemmanageUserFastmen obj)
			throws SystemException, ServiceException {
		String msg="快捷菜单设置成功,重新登录后生效！";
		try {
			fastMenuDao.save(obj);
		} catch (ServiceException e) {
			msg="保存快捷菜单设置失败！";
			throw new ServiceException(MessagesConst.save_error,
						new Object[] { " 快捷菜单对象" });
		}
		return msg;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:39:35
	 * @desc: 根据快捷菜单主键获取对象
	 * @param String
	 *            id 主键
	 * @return 快捷菜单记录
	 */
	public ToaSystemmanageUserFastmen getRecord(String id)
			throws SystemException, ServiceException {
		ToaSystemmanageUserFastmen obj = null;
		try {
			obj = fastMenuDao.get(id);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { " 快捷菜单记录" });
		}
		return obj;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:41:06
	 * @desc: 获取用户的快捷菜单对象
	 * @param String
	 *            userid 用户id
	 * @return 快捷菜单对象
	 */
	public ToaSystemmanageUserFastmen getRecords(String userid)
	throws SystemException, ServiceException {
		ToaSystemmanageUserFastmen systemFastMenu=this.getSystemSetting();	//获取该用户快捷菜单对象
		ToaSystemmanageUserFastmen obj = null;
		try {
			List<ToaSystemmanageUserFastmen> list = fastMenuDao.find(
					"from ToaSystemmanageUserFastmen t where t.userid=?",
					userid);
			if (list.size() > 0){	//如果该人员已设置了自己的快捷菜单
				obj = list.get(0);
				if(obj.getModerIds()==null||"".equals(obj.getModerIds())){//个人没有快捷菜单，则将系统的快捷菜单赋给该人员
					obj.setModerIds(systemFastMenu.getModerIds());
				}
			}else{	//如果该人员没有设置了自己的快捷菜单			
				obj=this.savePersonFastMenu(systemFastMenu, userid);
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { " 快捷菜单记录" });
		}
		return obj;
	}

	/**
	 * 
	  * @author：pengxq
	  * @time：2009-2-27下午07:08:42
	  * @desc: 获取系统默认快捷菜单设置记录
	  * @param
	  * @return ToaSystemmanageUserFastmen 系统默认快捷菜单对象
	 */
	public ToaSystemmanageUserFastmen getSystemSetting()
			throws SystemException, ServiceException {
		ToaSystemmanageUserFastmen obj = null;
		try {
			List<ToaSystemmanageUserFastmen> list = fastMenuDao.find(
					"from ToaSystemmanageUserFastmen t where t.systemLogo=1");
			if (list.size() > 0)
				obj = list.get(0);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { " 快捷菜单记录" });
		}
		return obj;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-6下午01:51:40
	 * @desc: 获取当前用户
	 * @param
	 * @return
	 */
	public User getCurrentUser() {
		return userService.getCurrentUser();
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午02:38:29
	 * @desc: 静态数据
	 * @param
	 * @return
	 */
	public List<Channel> getList(String modleId, String flag){
		List<Channel> list = new ArrayList<Channel>();
		Channel test1 = new Channel();

		if (flag != null && flag.equals("yes")) {
			if (modleId.indexOf("1") != -1) {
				test1.setBlockid("1");
				test1.setBlocktitle("个人桌面");
				test1.setBlockimg("desk.gif");
				test1.setBlocktpl("url");
				list.add(test1);
			}
			if (modleId.indexOf("2") != -1) {
				Channel test2 = new Channel();
				test2.setBlockid("2");
				test2.setBlocktitle("我的邮件");
				test2.setBlockimg("mail.gif");
				test2.setBlocktpl("url");
				list.add(test2);
			}
			if (modleId.indexOf("3") != -1) {
				Channel test3 = new Channel();
				test3.setBlockid("3");
				test3.setBlocktitle("我的信息");
				test3.setBlockimg("xx.gif");
				test3.setBlocktpl("url");
				list.add(test3);
			}
			if (modleId.indexOf("4") != -1) {
				Channel test4 = new Channel();
				test4.setBlockid("4");
				test4.setBlocktitle("个人邮件");
				test4.setBlockimg("set.gif");
				test4.setBlocktpl("url");
				list.add(test4);
			}
			if (modleId.indexOf("5") != -1) {
				Channel test5 = new Channel();
				test5.setBlockid("5");
				test5.setBlocktitle("我的收藏");
				test5.setBlockimg("sc.gif");
				test5.setBlocktpl("url");
				list.add(test5);
			}
			for (int i = 0; i < 5; i++) {
				if (modleId.indexOf(String.valueOf(i + 6)) != -1) {
					Channel test = new Channel();
					test.setBlockid(String.valueOf(i + 6));
					test.setBlocktitle("个人设置");
					test.setBlockimg("sc.gif");
					test.setBlocktpl("url");
					list.add(test);
				}
			}
		} else if (flag != null && flag.equals("no")) {
			if (modleId.indexOf("1") == -1) {
				test1.setBlockid("1");
				test1.setBlocktitle("个人桌面");
				test1.setBlockimg("desk.gif");
				test1.setBlocktpl("url");
				list.add(test1);
			}
			if (modleId.indexOf("2") == -1) {
				Channel test2 = new Channel();
				test2.setBlockid("2");
				test2.setBlocktitle("我的邮件");
				test2.setBlockimg("mail.gif");
				test2.setBlocktpl("url");
				list.add(test2);
			}
			if (modleId.indexOf("3") == -1) {
				Channel test3 = new Channel();
				test3.setBlockid("3");
				test3.setBlocktitle("我的信息");
				test3.setBlockimg("xx.gif");
				test3.setBlocktpl("url");
				list.add(test3);
			}
			if (modleId.indexOf("4") == -1) {
				Channel test4 = new Channel();
				test4.setBlockid("4");
				test4.setBlocktitle("个人邮件");
				test4.setBlockimg("set.gif");
				test4.setBlocktpl("url");
				list.add(test4);
			}
			if (modleId.indexOf("5") == -1) {
				Channel test5 = new Channel();
				test5.setBlockid("5");
				test5.setBlocktitle("我的收藏");
				test5.setBlockimg("sc.gif");
				test5.setBlocktpl("url");
				list.add(test5);
			}
			for (int i = 0; i < 5; i++) {
				if (modleId.indexOf(String.valueOf(i + 6)) == -1) {
					Channel test = new Channel();
					test.setBlockid(String.valueOf(i + 6));
					test.setBlocktitle("个人设置");
					test.setBlockimg("sc.gif");
					test.setBlocktpl("url");
					list.add(test);
				}
			}
		} else {
			test1.setBlockid("1");
			test1.setBlocktitle("个人桌面");
			test1.setBlockimg("desk.gif");
			test1.setBlocktpl("url");
			list.add(test1);
			Channel test2 = new Channel();
			test2.setBlockid("2");
			test2.setBlocktitle("我的邮件");
			test2.setBlockimg("mail.gif");
			test2.setBlocktpl("url");
			list.add(test2);
			Channel test3 = new Channel();
			test3.setBlockid("3");
			test3.setBlocktitle("我的信息");
			test3.setBlockimg("xx.gif");
			test3.setBlocktpl("url");
			list.add(test3);
			Channel test4 = new Channel();
			test4.setBlockid("4");
			test4.setBlocktitle("个人邮件");
			test4.setBlockimg("set.gif");
			test4.setBlocktpl("url");
			list.add(test4);
			Channel test5 = new Channel();
			test5.setBlockid("5");
			test5.setBlocktitle("我的收藏");
			test5.setBlockimg("sc.gif");
			test5.setBlocktpl("url");
			list.add(test5);
			for (int i = 0; i < 5; i++) {
				Channel test = new Channel();
				test.setBlockid(String.valueOf(i + 6));
				test.setBlocktitle("个人设置");
				test.setBlockimg("sc.gif");
				test.setBlocktpl("url");
				list.add(test);
			}
		}
		return list;
	}

	/**
	 * @author：pengxq
	 * @time：2009-2-3下午02:53:54
	 * @desc: 从资源文件中读取相应变量的值
	 * @param String
	 *            param 需获取值的变量
	 * @return String 变量的值
	 */
	public String getValueFromPro(String param) {
		Properties properties = new Properties();
		URL in = this.getClass().getClassLoader().getResource(
				"appconfig.properties");
		try {
			properties.load(new FileInputStream(in.getFile()));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return properties.getProperty(param);
	}
	
	
	/*
	 * 
	 * Description:将系统快捷菜单拷贝给个人
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 26, 2010 9:31:05 AM
	 */
	public ToaSystemmanageUserFastmen savePersonFastMenu(ToaSystemmanageUserFastmen model,String userid) throws SystemException,ServiceException{
		try{
			ToaSystemmanageUserFastmen fastmenu=new ToaSystemmanageUserFastmen();
			fastmenu.setModerIds(model.getModerIds());
			fastmenu.setUserid(userid);
			fastmenu.setSystemLogo("0");
			fastmenu.setCreateTime(new Date());
			fastMenuDao.save(fastmenu);
			return fastmenu;
		}catch(ServiceException e){
			   throw new ServiceException(MessagesConst.find_error,new Object[] { "将系统快捷菜单拷贝给个人" });
		}
	}


	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
}
