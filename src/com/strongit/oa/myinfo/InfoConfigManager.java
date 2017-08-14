/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-02-09
 * Autour: pengxq
 * Version: V1.0
 * Description：个人配置业务实现类
*/

package com.strongit.oa.myinfo;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.oa.bo.ToaPersonalConfig;
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
public class InfoConfigManager 
{
   
   private GenericDAOHibernate<ToaPersonalConfig, java.lang.String> infoConfigDao;
   
   /**
    * @roseuid 494857FE02EE
    */
   public InfoConfigManager() 
   {
	  
   }
   
   /**
    * @param sessionFactory
    * @roseuid 494854950242
    */
   @Autowired
   public void setSessionFactory(SessionFactory sessionFactory) 
   {
	   infoConfigDao=new GenericDAOHibernate<ToaPersonalConfig, java.lang.String>(sessionFactory,ToaPersonalConfig.class);
   }
  
   /**
    * 
     * @author：pengxq
     * @time：2009-2-10下午02:34:07
     * @desc: 根据个人信息主键值获取个人配置信息
     * @param String personInfoId 个人信息主键
     * @return List 个人配置列表
    */
   public List getConfigByInfoid(String personInfoId)throws SystemException,ServiceException{
	   List list=null;
	   try{
		   String sql="from ToaPersonalConfig t where t.toaPersonalInfo.prsnId=?";
		   list=infoConfigDao.find(sql, personInfoId);
	   }catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"个人配置列表"});
	   }
	   return list;
   } 
   
   /**
    * 
     * @author：pengxq
     * @time：2009-2-11上午09:05:13
     * @desc: 保存个人配置信息
     * @param ToaPersonalConfig obj 个人配置对象
     * @return void
    */
   public void saveConfig(ToaPersonalConfig obj)throws SystemException,ServiceException{
	   try{
		   infoConfigDao.save(obj);
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"个人配置对象"});
	   }
   }
   
}
