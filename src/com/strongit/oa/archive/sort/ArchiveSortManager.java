/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：类目管理manager 
*/

package com.strongit.oa.archive.sort;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.oa.archive.archivefolder.ArchiveFolderManager;
import com.strongit.oa.bo.ToaArchiveFolder;
import com.strongit.oa.bo.ToaArchiveSort;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
@OALogger
public class ArchiveSortManager 
{
   private GenericDAOHibernate<ToaArchiveSort ,java.lang.String> archiveSortDao;
   private ArchiveFolderManager folderManager;	//案卷mananger
   
   /**
    * @roseuid 494F446403B9
    */
   public ArchiveSortManager() 
   {
    
   }
   
   private IUserService userService;								//统一用户接口
   
   /**
    * @param sessionFactory
    * @roseuid 494F34740186
    */
   @Autowired
   public void setSessionFactory(SessionFactory sessionFactory) 
   {
	   archiveSortDao=new GenericDAOHibernate<ToaArchiveSort ,java.lang.String>(sessionFactory,ToaArchiveSort.class);
   }
   
   
   /**
    * 获取当前用户所在机构里所有类目
    * @author 胡丽丽
    * @date 2010-01-16
    * @param sortOrgId
    * @return
    * @throws SystemException
    * @throws ServiceException
    */
   public List getAllArchiveSortByOrg() throws SystemException,ServiceException{
	   try {
		   User user=userService.getCurrentUser();
		   TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(user.getUserId());
		   String hql="from ToaArchiveSort t  where 1=1";
		   boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构   
		   if(isView){
			   if(org!=null){
				   hql=hql+" and t.sortOrgCode like '"+org.getSupOrgCode()+"%' order by t.sortArrayNo";
				   return archiveSortDao.find(hql);
			   }
			   
		   }else {
			   if(org!=null){
				   hql=hql+" and  t.sortOrgId=? order by t.sortArrayNo";
				   return archiveSortDao.find(hql, org.getOrgId());
			   }
		   }
		   
		   hql="from ToaArchiveSort t  order by t.sortArrayNo"; 
		   return archiveSortDao.find(hql);
	} catch (Exception e) {
		e.printStackTrace();
		 throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"类目列表"});
	}
   }
   
   
   /**
     * @author：pengxq
     * @time：2009-1-4上午11:36:08
     * @desc: 获取类目列表
     * @param 
     * @return List 类目列表
    */
   @Transactional(readOnly = true)
   public List getAllArchiveSort()  throws SystemException,ServiceException
   {
	   try{
		   Object[] obj= new Object[1];
		   String hql="from ToaArchiveSort t "; 
		   User user=userService.getCurrentUser();
			TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(user.getUserId());
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构   
		   if(isView){
			   hql=hql+" where t.sortOrgCode like ?";
			   obj[0]=org.getOrgSyscode()+"%";
		   }else{
			   hql=hql+" where t.sortOrgId=?";
			   obj[0]=org.getOrgId();
		   }
		   hql=hql+"  order by t.sortArrayNo";
		   return  archiveSortDao.find(hql,obj);
		   
		   
//		   
//		   String hql="from ToaArchiveSort t order by t.sortArrayNo"; 
//		   return  archiveSortDao.find(hql);
		  
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"类目列表"});
	   } 
   }
   
   /**
     * @author：pengxq
     * @time：2009-1-4上午11:35:06
     * @desc: 获取分页类目列表
     * @param Page page 分页对象
     * @return Page 分页类目列表
    */
   @Transactional(readOnly = true)
   public Page<ToaArchiveSort> getAllArchiveSort(Page page)  throws SystemException,ServiceException
   {
	   try{
		   page=archiveSortDao.findAll(page);	   
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"分页类目列表"});
	   }
	   return page;
   }
   
   /**
     * @author：pengxq
     * @time：2009-1-4上午11:32:28
     * @desc: 根据类目主键id获取类目对象
     * @param String id 类目id
     * @return 类目对象
    */
   @Transactional(readOnly=false)
   public ToaArchiveSort getArchiveSort(String id)  throws SystemException,ServiceException
   {
	   ToaArchiveSort obj=null;
	   try{
		   obj=archiveSortDao.get(id);	   
	   }catch(ServiceException e){
		   
		   throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"类目对象"});
	   }
	   return obj;
   }
   
   /**
     * @author：pengxq
     * @time：2009-1-4上午11:33:12
     * @desc: 保存类目
     * @param ToaArchiveSort obj 类目对象
     * @return void
    */
   public String saveArchiveSort(ToaArchiveSort obj,OALogInfo ... loginfos)  throws SystemException,ServiceException
   {
	   String msg="";
	   try{
		   archiveSortDao.save(obj); 
		   msg="类目保存成功！";
	   }catch(ServiceException e){
		   msg="类目保存失败！";
		   throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"类目对象"});
	   }  
	   return msg;
   }
   
   /**
     * @author：pengxq
     * @time：2009-1-4上午11:33:59
     * @desc: 根据类目主键id删除类目
     * @param String archiveId 类目主键
     * @return String 提示信息
    */
   public String delArchiveSort(String archiveId,OALogInfo ... loginfos)  throws SystemException,ServiceException
   {
	   String msg="";
	   try{ 
		   List<ToaArchiveFolder> list=folderManager.getAllFolder(archiveId);//获取该类目的所有案卷
		   if(list.size()>0){	 //判断该类目下是否存在案卷
			   msg="该类目中存在案卷，请先删除相应的案卷在做此操作";
		   }else if(isZisort(archiveId)){
			   msg="该类目下存在子类目，请先删除子类目";
		   }else{
			   archiveSortDao.delete(archiveId);
			   msg="类目删除成功";
		   }
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"类目对象"});
	   } 
	   return msg;
   }
   
   /**
	  * @author：pengxq
	  * @time：2009-1-8上午09:28:12
	  * @desc: 类目编号是否有重复的
	  * @param String sortNo 类目编号
	  * @return 
	 */
	public String isExist(String sortNo,String sortId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		List list=null;
		List<Object> list1 = new ArrayList<Object>();
		try{
		    String hql=" from ToaArchiveSort t where t.sortNo=? ";
		    list1.add(sortNo);
		    if(sortId!=null&&!"".equals(sortId)){
			 hql=hql+"  and t.sortId<>?";
			 list1.add(sortId);}
			 list=archiveSortDao.find(hql, list1.toArray());	
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.operation_error,               
					new Object[] {"判断类目编号是否重复"});
		}
		if(list!=null&&list.size()==0)
			return "1";
		else 
			return "0";
	}
	/*
	 * 判断类目下是否有子类目
	 * Description:
	 * @author  胡丽丽
	 * @date Apr 27, 2010 11:38:55 AM
	 * param:
	 */
	public boolean isZisort(String srotno){
		String sql="from ToaArchiveSort t where t.sortParentNo=?";
		List<String> list=archiveSortDao.find(sql, srotno);
		if(list!=null&&list.size()>0){
			return true;
		}else{
			return false;
		}
	}
   @Autowired
   public void setFolderManager(ArchiveFolderManager folderManager) {
   	this.folderManager = folderManager;
   }


	public IUserService getUserService() {
		return userService;
	}
	
	
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
}
