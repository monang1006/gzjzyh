/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-02-11
 * Autour: pengxq
 * Version: V1.0
 * Description：系统默认邮箱配置业务实现类
*/

package com.strongit.oa.systemmail;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.oa.bo.ToaSysDefaultmail;
import com.strongit.oa.mymail.util.StringUtil;
import com.strongit.oa.mymail.util.WriteMail;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
public class SystemMailManager 
{
   
   private GenericDAOHibernate<ToaSysDefaultmail, java.lang.String> defaultMailDao;

   
   /**
    * @roseuid 494857FE02EE
    */
   public SystemMailManager() 
   {
	  
   }
   
   /**
    * @param sessionFactory
    * @roseuid 494854950242
    */
   @Autowired
   public void setSessionFactory(SessionFactory sessionFactory) 
   {
	   defaultMailDao=new GenericDAOHibernate<ToaSysDefaultmail, java.lang.String>(sessionFactory,ToaSysDefaultmail.class);
   }
   
  /**
   * 
    * @author：pengxq
    * @time：2009-2-12上午09:55:22
    * @desc: 获取系统默认邮箱配置对象
    * @param 
    * @return defaultMail 系统默认邮箱配置对象
   */
   public ToaSysDefaultmail getDefaultMail()throws SystemException,ServiceException{  
	   try{
		   ToaSysDefaultmail defaultMail=null;
		   List<ToaSysDefaultmail> list=defaultMailDao.find("from ToaSysDefaultmail t");
		   if(list.size()>0){
			   defaultMail=list.get(0);
		   }
		   return defaultMail;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
				new Object[] {"系统默认邮箱配置对象"});
		}
   }
   
   /**
    * 
     * @author：pengxq
     * @time：2009-2-12上午10:16:52
     * @desc: 保存系统默认邮箱配置对象
     * @param ToaSysDefaultmail obj 系统默认邮箱配置对象
     * @return void
    */
   public String saveDefaultMail(ToaSysDefaultmail obj)throws SystemException,ServiceException{  
	   String msg="";
	   try{
		   if(obj != null){
			   defaultMailDao.save(obj); 			   
		   }else{
			   msg="保存系统默认邮箱配置失败！";
		   }
		}catch(ServiceException e){
			msg="保存系统默认邮箱配置失败！";
			throw new ServiceException(MessagesConst.save_error,               
				new Object[] {"系统默认邮箱配置对象"});
		}
		return msg;
   }
   
   /**
    * 
     * @author：pengxq
     * @time：2009-2-12下午03:51:22
     * @desc: 系统邮箱发送邮件
     * @param String receivers 收件人员
     * @param ToaSysDefaultmail obj 系统邮箱配置信息 
     * @return String 提示信息
    */
   public String sendMail(String receivers,ToaSysDefaultmail obj){
	   String msg="";
	   List<String> toList=null;
	   try{
		   if(obj!=null){
	  			String sendServer=obj.getDefaultMailSys();	//获得smtp服务器
	  			String fromEmail=obj.getDefaultMail();
	  			String port=obj.getDefaultMailPort();			//获得smtp服务器端口
	  			String ssl=obj.getDefaultMailSsl();			//是否需要ssl加密
	  			String username=obj.getDefaultMailUser();		//用户名称
	  			String password=obj.getDefaultMailPsw();	
	  			toList=StringUtil.stringToList(receivers, ",");	//收件人员列表	
	  			WriteMail writeMail=new WriteMail(sendServer,fromEmail,username,username,password,toList,null,null,"系统默认邮箱测试","测试成功",ssl,port);
	  			HashMap map;
				map = writeMail.send();
	  			String state=(String) map.get("state");
	  			if(state.equals("success")){
	  				msg="true";
	  			}else{
	  				msg="false";
	  			}
	  		}
	   }catch(Exception e){
		   msg="false";
	   }
	   return msg;
   }
   
   
   /**
	 * author:luosy
	 * description:  判断邮件模块是否已启用
	 * modifyer:
	 * description:
	 * @return true : 启用；false ：关闭
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean isMailUseable()throws SystemException,ServiceException{  
		   try{
			   ToaSysDefaultmail defaultMail=null;
			   List<ToaSysDefaultmail> list=defaultMailDao.find("from ToaSysDefaultmail t");
			   if(list.size()>0){
				   defaultMail=list.get(0);
			   }
			   if(null!=defaultMail){
				   if(ToaSysDefaultmail.USEABLE_TURE.equals(defaultMail.getDefaultMailUseable())){
					   return true;
				   }
			   }
			   return false;
			}catch(ServiceException e){
				throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"系统默认邮箱配置对象"});
			}
		   
	   }
	}
