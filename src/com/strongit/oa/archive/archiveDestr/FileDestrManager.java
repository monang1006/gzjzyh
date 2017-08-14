/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：销毁案卷管理manager
*/
package com.strongit.oa.archive.archiveDestr;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.oa.archive.archivefolder.ArchiveFolderManager;
import com.strongit.oa.bo.ToaArchiveDestroy;
import com.strongit.oa.bo.ToaArchiveDestroyFile;
import com.strongit.oa.bo.ToaArchiveFile;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
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
public class FileDestrManager 
{
   /** 案卷销毁DAO*/
   private GenericDAOHibernate<ToaArchiveDestroyFile, String> destrDao;
   /** 案卷Manager*/
   private ArchiveFolderManager folderManager;
   
   /**
    * @roseuid 4959D19B0232
    */
   public FileDestrManager() 
   {
    
   }
   
   /**
    * 注册DAO
    * @param sessionFactory
    * @roseuid 4959CFD10109
    */
   @Autowired
   public void setSessionFactory(SessionFactory sessionFactory) 
   {
	   destrDao=new GenericDAOHibernate<ToaArchiveDestroyFile, String>(sessionFactory,ToaArchiveDestroyFile.class);
   }
   
   /**
     * @author：pengxq
     * @time：2009-1-4下午12:28:38
     * @desc: 根据销毁案卷id获取销毁文件列表
     * @param String destroyId 销毁案卷id
     * @return 销毁文件列表
    */
   public List getDestrFile(String destroyId,OALogInfo ... loginfos)throws SystemException,ServiceException 
   {
	   List<ToaArchiveDestroyFile> list=null;
	   try{   
		   list=destrDao.find("from ToaArchiveDestroyFile t where t.toaArchiveDestroy.destroyId=?", destroyId);
	   }catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"销毁文件列表"});
	   }
	   return list;
   }
   
   /**
    * @author：pengxq
    * @time：2008-12-30下午10:02:48
    * @desc：根据案卷id获取该案卷的档案文件列表，将档案文件记录保存到销毁文件表中
    * @param ToaArchiveDestroy destrArchive 销毁案卷对象
    * @param String folderId 案卷id
    * @return void
    */
   public void saveDestrFile(ToaArchiveDestroy destrArchive,String folderId,OALogInfo ... loginfos)throws SystemException,ServiceException{
	   List<ToaArchiveFile> list=null;
	   ToaArchiveDestroyFile destroyFile;
	   try{
		   list=folderManager.getArchiveFileByFolderId(folderId,null);	//获取档案文件列表
		   List<ToaArchiveDestroyFile> destroyFileList=new ArrayList<ToaArchiveDestroyFile>();//存放销毁文件列表
		   if(list.size()>0){
			   for(ToaArchiveFile obj:list){	//循环档案文件列表
				   destroyFile=new ToaArchiveDestroyFile();
				   destroyFile.setDestroyFileNo(obj.getFileNo());
				   destroyFile.setDestroyFileName(obj.getFileTitle());
				   destroyFile.setDestroyFileAuthor(obj.getFileAuthor());
				   destroyFile.setDestroyFileDate(obj.getFileDate());
				   destroyFile.setDestroyFilePage(obj.getFilePage());
				   destroyFile.setDestroyFileDesc(obj.getFileDesc());
				   destroyFile.setToaArchiveDestroy(destrArchive);   
				   destroyFileList.add(destroyFile);
			   }
		   }
		   destrDao.save(destroyFileList);	//保存销毁文件列表
	   }catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"销毁文件"});
	   }
   }
   
   /**
    * @author：pengxq
    * @time：2008-12-30下午10:02:48
    * @desc：注册案卷Manager
    * @param ArchiveFolderManager folderManager 案卷Manager
    * @return void
    */
   @Autowired
   public void setFolderManager(ArchiveFolderManager folderManager) {
	   this.folderManager = folderManager;
   }
}
