/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-4-25
 * Autour: hull
 * Version: V1.0
 * Description： 文档中心权限manager
 */
package com.strongit.oa.archive.tempfile;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.bo.ToaArchiveTempfilePrivil;
import com.strongit.oa.bo.ToaArchiveTfileAppend;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
@OALogger
public class TempPrivilManager {

	   private GenericDAOHibernate<ToaArchiveTempfilePrivil, java.lang.String> privilDao;
	   
	   /**
	    * @roseuid 493F83E700CB
	    */
	   public TempPrivilManager() 
	   {
	    
	   }
	   
	   @Autowired
	   public void setSessionFactory(SessionFactory sessionFactory) 
	   {
		   privilDao=new GenericDAOHibernate<ToaArchiveTempfilePrivil, java.lang.String>(sessionFactory,
				   ToaArchiveTempfilePrivil.class);
		
	   }
	   /**
	    * 添加
	    * @param privil
	    */
	   public void save(ToaArchiveTempfilePrivil privil,OALogInfo ... loginfos) throws SystemException,ServiceException {
		   try {
			privilDao.save(privil);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,        
					new Object[] {"添加权限"});
		}
	   }
	   /**
	    * 添加多条
	    */
	   public void save(List<String> list,String tempfileId,String desc,OALogInfo ... loginfos) throws SystemException,ServiceException{
		   try {
			   
			   List<ToaArchiveTempfilePrivil> privils=new ArrayList<ToaArchiveTempfilePrivil>();
			   deleteByTempfileId(tempfileId);
			   if(list.size()>0){
				   for(String userid:list){//遍历权限用户ID
					   ToaArchiveTempfilePrivil privil=new ToaArchiveTempfilePrivil();
					   ToaArchiveTempfile temfile=new ToaArchiveTempfile();
					   temfile.setTempfileId(tempfileId);
					   privil.setTempfileprivilUser(userid);
					   privil.setToaArchiveTempfile(temfile);
					   privil.setTempfileprivilDesc(desc);
					   privils.add(privil);
				   }
			   }
			   privilDao.save(privils);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,        
					new Object[] {"添加权限"});
		}
	   }
	   
	   /**
	    * 根据文件ID删除
	    * @param id
	    * @throws SystemException
	    * @throws ServiceException
	    */
	   public void deleteByTempfileId(String id,OALogInfo ... loginfos)throws SystemException,ServiceException{
		  // String sql="delete t_oa_archive_tempfile_privil t where t.tempfile_id='"+id+"'";
		   try {
			String hql="from ToaArchiveTempfilePrivil t where t.toaArchiveTempfile.tempfileId=?";
			   
			  List<ToaArchiveTempfilePrivil> list= privilDao.find(hql, id);
			  privilDao.delete(list);
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,        
					new Object[] {"批量删除权限"});
		}
		
		  
	   }
	   
	   /***
	    * 删除
	    * @param id
	    */
	   public void delete(String id,OALogInfo ... loginfos) throws SystemException,ServiceException{
		   try {
			privilDao.delete(id);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,        
					new Object[] {"删除权限"});
		}
	   }
	   /**
	    * 根据ID查看
	    * @param id
	    * @return
	    */
	   public ToaArchiveTempfilePrivil getPrivilByID(String id,OALogInfo ... loginfos) throws SystemException,ServiceException{
		  try {
			return privilDao.get(id);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,        
					new Object[] {"根据ID查看"});
		}
	   }
	   
	   /***
	    * 根据用户查看
	    * @param userid
	    * @return
	    */
	   public List<ToaArchiveTempfilePrivil> getPrivilByUsre(String userid,OALogInfo ... loginfos) throws SystemException,ServiceException{
		   try {
			String sql="from ToaArchiveTempfilePrivil t where t.tempfileprivilUser=?";
			   return privilDao.find(sql, userid);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,        
					new Object[] {"根据用户查看"});
		}
	   }
	   /**
	    * 查看所有
	    * @return
	    */
	   public List<ToaArchiveTempfilePrivil> getAllPrivil() throws SystemException,ServiceException{
		   try {
			return privilDao.findAll();
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,        
					new Object[] {"查看所有"});
		}
	   }
	   
	   /**
	    * 根据用户和文件查看
	    * @param userid
	    * @param tempid
	    * @return
	    */
	   public  List<ToaArchiveTempfilePrivil> getPrivilByUsreTemp(String userid,String tempid,OALogInfo ... loginfos) throws SystemException,ServiceException{
		   try {
			String sql="from ToaArchiveTempfilePrivil t where t.tempfileprivilUser=? and t.toaArchiveTempfile.tempfileId=?";
			   return privilDao.find(sql, userid,tempid);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,        
					new Object[] {"根据用户和文件查看"});
		}
	   }
	   
	   /**
	    * 根据文件查看
	    * @param tempid
	    * @return
	    */
	   public List<ToaArchiveTempfilePrivil> getPrivilByTempfile(String tempid,OALogInfo ... loginfos) throws SystemException,ServiceException{
		   try {
			String sql="from ToaArchiveTempfilePrivil t where t.toaArchiveTempfile.tempfileId=?";
			   return privilDao.find(sql,tempid);
		} catch (DAOException e) {
			throw new ServiceException(MessagesConst.find_error,        
					new Object[] {"根据文件查看"});
		}
	   }
	   

}
