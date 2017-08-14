/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：附件管理manager 
*/

package com.strongit.oa.archive.tempfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.bo.ToaArchiveTfileAppend;
import com.strongit.oa.prsnfldr.util.Round;
import com.strongit.oa.search.SearchManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

@Service
@Transactional
@OALogger
public class AnnexManager 
{
	
   private GenericDAOHibernate<ToaArchiveTfileAppend, java.lang.String> annexDao;
   
	private SearchManager searchManager;//全文检索
	
	@Autowired
	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}
   
   /**
    * @roseuid 493F83E700CB
    */
   public AnnexManager() 
   {
    
   }
   
   @Autowired
   public void setSessionFactory(SessionFactory sessionFactory) 
   {
	   annexDao = new GenericDAOHibernate<ToaArchiveTfileAppend, java.lang.String>(sessionFactory,
			   ToaArchiveTfileAppend.class);
   }
   
   /**
    * @author：pengxq
    * @time：2008-12-29下午04:22:20
    * @desc: 保存文件附件
    * @param
    * @return
    */
   public void saveAnnex(File[] file,ToaArchiveTempfile toaArchiveTempfile,String fileName,OALogInfo...infos)throws SystemException,ServiceException{
	   String tempFileId=toaArchiveTempfile.getTempfileId();
	   ToaArchiveTfileAppend annex=null;
	   String[] names=fileName.split(",");

		try {
			for(int i=0;i<file.length;i++){
				annex =new ToaArchiveTfileAppend();
				 FileInputStream fis = null;
				 FileInputStream indexfis = null;//全文检索用到
			//String ext =fileName.substring(fileName.lastIndexOf(".")+1);
			String size;
			try {
				fis = new FileInputStream(file[i]);
				indexfis=new FileInputStream(file[i]); //全文检索用
				byte[] buf = new byte[(int)file[i].length()];
				fis.read(buf);
				annex.setTempappendContent(buf);//文件内容
				size = "";	
				long length_byte = file[i].length();
				if(length_byte >= 1024){
					double length_k   = ((double)length_byte)/1024;
					if(length_k >= 1024){
						size = Round.round(length_k/1024, 2)+"MB"; 
					}else{
						size = length_byte/1024+"k";
					}
				}else{
					size = length_byte+"字节";
				}
				fis.close();
			} catch (FileNotFoundException ex) {
				annex.setTempappendContent(" ".getBytes());//文件内容
				size="1k";
			}
			annex.setTempappendName(names[i]);//文件名称
			//annex.setTempappendType(ext);	//文件后缀
			annex.setTempappendSize(size);	//文件大小
			annex.setToaArchiveTempfile(toaArchiveTempfile);
			
			annexDao.save(annex);	
//			saveSearchAppend(annex,indexfis,new OALogInfo("年内文件所属附件添加索引搜索"));//年内文件所属附件添加索引搜索
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"年内文件附件"}); 
		} catch (IOException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			throw new SystemException();
		}		
	}
   /**
    * 
    * @author zhengzb
    * @desc 对档案中心文件所属附件，添加索引搜索操作
    * 2010-5-10 下午03:54:21 
    * @param annex     附件对象
    * @param indexfis	附件文件流
    * @param loginfos	
    * @throws SystemException
    * @throws ServiceException
    */
   public void saveSearchAppend(ToaArchiveTfileAppend annex,FileInputStream indexfis,OALogInfo ... loginfos)throws SystemException,ServiceException{
	   try {
		   //String tempFileId=annex.getToaArchiveTempfile().getTempfileId();
		   String [] tempFile=new String[7];//定义档案中心文件数组
		   tempFile[0]=annex.getToaArchiveTempfile().getTempfileTitle();//题名
		   tempFile[1]=annex.getToaArchiveTempfile().getTempfileDeadline();//保管权限
		   tempFile[2]=annex.getToaArchiveTempfile().getTempfileDepartmentName();//所属部门
		   tempFile[3]=annex.getToaArchiveTempfile().getTempfileDesc();//文件备注
		   tempFile[4]=annex.getToaArchiveTempfile().getTempfileAuthor();//责任者
		   tempFile[5]=getDateTime(annex.getToaArchiveTempfile().getTempfileDate());//日期
		   tempFile[6]=annex.getToaArchiveTempfile().getTempfileNo();//文号
		   searchManager.saveIndex(annex, tempFile, indexfis);//添加索引搜索
		   		
	} catch (Exception e) {
		// TODO: handle exception
		throw new ServiceException(MessagesConst.save_error,               
				new Object[] {"档案中心附件添加索引搜索"}); 
	}
   }
   
   /**
    * 
    * @author zhengzb
    * @desc 修改档案中心文件时，对所属附件更新索引搜索操作
    * 2010-5-10 下午03:54:21 
    * @param annex     附件对象
    * @param loginfos	
    * @throws SystemException
    * @throws ServiceException
    */
   public void saveSearchAppend(ToaArchiveTfileAppend annex,OALogInfo ... loginfos)throws SystemException,ServiceException{
	   try {
		   String [] tempFile=new String[7];//定义档案中心文件数组
		   tempFile[0]=annex.getToaArchiveTempfile().getTempfileTitle();//题名
		   tempFile[1]=annex.getToaArchiveTempfile().getTempfileDeadline();//保管权限
		   tempFile[2]=annex.getToaArchiveTempfile().getTempfileDepartmentName();//所属部门
		   tempFile[3]=annex.getToaArchiveTempfile().getTempfileDesc();//文件备注
		   tempFile[4]=annex.getToaArchiveTempfile().getTempfileAuthor();//责任者
		   tempFile[5]=getDateTime(annex.getToaArchiveTempfile().getTempfileDate());//日期
		   tempFile[6]=annex.getToaArchiveTempfile().getTempfileNo();//文号
		   searchManager.updetIndex(annex, tempFile);//更新档案中心文件下的所有附件的全文搜索索引
		   
		
	} catch (Exception e) {
		// TODO: handle exception
		throw new ServiceException(MessagesConst.save_error,               
				new Object[] {"档案中心附件添加索引搜索"}); 
	}
   }
   
   /**
	 * 格式化时间
	 */
	public String getDateTime(Date time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(time);
		return date;
	}
   
  /** 
   * @author：pengxq
   * @time：2008-12-29下午04:20:21
   * @desc：根据附件所属文件的文件ID获取附件对象
   * @param String tempFileId 文件ID
   * @return 
   */ 
   public ToaArchiveTfileAppend getAnnex(String tempFileId,OALogInfo ... loginfos)throws SystemException,ServiceException{  
	   try{
		   ToaArchiveTfileAppend obj=new ToaArchiveTfileAppend();
		   String hql="from ToaArchiveTfileAppend t where t.toaArchiveTempfile.tempfileId=?";
		   List<ToaArchiveTfileAppend> list= annexDao.find(hql, tempFileId);
		   if(list.size()>0){
			   obj= list.get(0);
		   }
		   return obj;
	   }catch(ServiceException e){ 
		   throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"年内文件附件"}); 
	   }   
   }
   /** 
    * @author：pengxq
    * @time：2008-12-29下午04:20:21
    * @desc：根据附件所属文件的文件ID获取附件对象
    * @param String tempFileId 文件ID
    * @return 
    */ 
    public ToaArchiveTfileAppend getFileAnnex(String attendId,OALogInfo ... loginfos)throws SystemException,ServiceException{  
 	   try{
 		   ToaArchiveTfileAppend obj=new ToaArchiveTfileAppend();
 		    obj=  annexDao.get(attendId);
 		     return obj;
 	   }catch(ServiceException e){ 
 		   throw new ServiceException(MessagesConst.find_error,               
 					new Object[] {"根据附件所属文件的文件ID获取附件对象"}); 
 	   }   
    }
   /**
    * @author：pengxq
    * @time：2008-12-29下午04:22:59
    * @desc：读取附件内容
    * @param
    * @return
    */
   public String readAnnex(String tempFileId,OALogInfo ... loginfos)throws SystemException,ServiceException{
	   String content="";
	   ToaArchiveTfileAppend annex=this.getAnnex(tempFileId);
		try {
			byte[] annexContent = annex.getTempappendContent();
			String tempContent = new String(annexContent,0,annexContent.length);
			content = HtmlUtils.htmlEscape(tempContent);
		} catch (ServiceException e) {
			   throw new ServiceException(MessagesConst.operation_error,               
						new Object[] {"读取附件"}); 
		}
		return content;
   }
   
	/**
	  * @author：pengxq
	  * @time：2009-1-5下午01:57:11
	  * @desc: 文档流直接写入HttpServletResponse请求
	  * @param response HttpServletResponse请求
	  * @param String tempFileId 年内文件id
	  * @return void
	 */
	public void setContentToHttpResponse(HttpServletResponse response, String tempFileId, String tfileAppedId,OALogInfo ... loginfos) {
//		ToaArchiveTfileAppend obj = getAnnex(tempFileId);
		ToaArchiveTfileAppend obj=null; //获取附件对象
		if(tfileAppedId!=null&&!"".equals(tfileAppedId)){
			obj=getFileAnnex(tfileAppedId);
		}
		else{
			obj = getAnnex(tempFileId);
		}
		response.reset();
		response.setContentType("application/octet-stream");
		try {
		response.setHeader("Content-Disposition", "attachment; filename="
				+ new String(obj.getTempappendName().getBytes("gb2312"),
				"iso8859-1"));
		OutputStream output = null;
			output = response.getOutputStream();
			output.write(obj.getTempappendContent());
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
   
	/**
	 * @author zhengzb
	 * @desc 归档成功后，删除档案中心文件全文检索索引
	 * @time 2010-05-12 15:57
	 */
   /**
    * @author：pengxq
    * @time：2008-12-29下午07:01:50
    * @desc：删除年内文件附件
    * @param String tempFileId 年内文件id
    * @return void
    */
   public void deleteAnnex(String tempFileId,OALogInfo ... loginfos)throws SystemException,ServiceException{
	   try{
		   String hql="from ToaArchiveTfileAppend t where t.toaArchiveTempfile.tempfileId=?";
		   List<ToaArchiveTfileAppend> list= annexDao.find(hql, tempFileId);
		   if(list.size()>0){
			   annexDao.delete(list) ;//删除档案中心文件
			   String [] ids=new String[list.size()];
			   for(int i=0;i<list.size();i++){
				   ids[i]=list.get(i).getTempappendId();
			   }
			   searchManager.delIndex(ids);//归档后，删除全文检索索引
		   }
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"年内文件附件"}); 
	   }
   }
   
   /**
    * @author zhengzb
    * @Desc 删除档案中心文件所属附件的全检索索引
    * @time: 2010-05-12 16:03
    */
   /**
    * @author：pengxq
    * @time：2008-12-29下午07:01:50
    * @desc：删除年内文件附件
    * @param String tempFileId 年内文件id
    * @return void
    */
   public void deleteAnnexByID(String appendid,OALogInfo ... loginfos)throws SystemException,ServiceException{
	   try{
		  String[] ids=appendid.split(",");
		  for(int i=0;i<ids.length;i++){
			  if(ids[i]!=null&&!"".equals(ids[i]))
			  annexDao.delete(ids[i]);
//			  searchManager.delIndex(ids[i]);//删除搜索索引
			  
		  }
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"年内文件附件"}); 
	   }
   }
   
   /**
    * 
     * @author：pengxq
     * @time：2009-6-11上午09:28:13
     * @desc: 
     * @param
     * @return
    */
   public void saveAppend(ToaArchiveTfileAppend annex,OALogInfo ... loginfos){
	   try{
		   annexDao.save(annex);
	   }catch(ServiceException e){
		   throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"年内文件附件"}); 
	   }
	   
   }
   
   /**
	 * 获取每个文件的附件数量
	 * @author 胡丽丽
	 * @date 2010-01-20
	 * @return
	 */
	public Map<String,Integer> getCount(){
		String sql = "select t.tempappendId, t.toaArchiveTempfile.tempfileId from ToaArchiveTfileAppend t";
		List<Object[]> objsLst = annexDao.find(sql);
		Map<String,Integer> map = new HashMap<String,Integer>();
		if(objsLst!=null && !objsLst.isEmpty()){
			for(Object[] objs : objsLst){
				if(objs[1]!=null){
					if(!map.containsKey(objs[1])){
						map.put(objs[1].toString(), 1);
					}else{
						map.put(objs[1].toString(), map.get(objs[1])+1);
					}
				}
			}
		}
		
		return map;
	}
   
	 /** 
	    * @author：胡丽丽
	    * @time：2010-02-03
	    * @desc：根据附件所属文件的文件ID获取附件对象
	    * @param String tempFileId 文件ID
	    * @return 
	    */ 
	    public List<ToaArchiveTfileAppend> getListAnnex(String tempFileId)throws SystemException,ServiceException{  
	 	   try{
	 		   ToaArchiveTfileAppend obj=new ToaArchiveTfileAppend();
	 		   String hql="from ToaArchiveTfileAppend t where t.toaArchiveTempfile.tempfileId=?";
	 		   List<ToaArchiveTfileAppend> list= annexDao.find(hql, tempFileId);
	 		   
	 		   return list;
	 	   }catch(ServiceException e){ 
	 		   throw new ServiceException(MessagesConst.find_error,               
	 					new Object[] {"年内文件附件"}); 
	 	   }   
	    }
}
   
