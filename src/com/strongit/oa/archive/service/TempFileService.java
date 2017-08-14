/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：年内文件实现类 
*/

package com.strongit.oa.archive.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.archive.ITempFileService;
import com.strongit.oa.archive.tempfile.AnnexManager;
import com.strongit.oa.archive.tempfile.TempPrivilManager;
import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.bo.ToaArchiveTfileAppend;
import com.strongit.oa.bo.ToaBaseOrg;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.search.SearchManager;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;



/**
 * 
 * @author pengxq
 * 年内文件接口实现类
 *
 */
@Service
@Transactional
@OALogger
public class TempFileService implements ITempFileService
{
	
		private GenericDAOHibernate<ToaArchiveTempfile, java.lang.String> tempFileDao;
		
		private IUserService userService;//用户接口
		
		private TempPrivilManager privilManager;//文件权限manager
		
		private AnnexManager manager;
		
		private SearchManager searchManager;//全文检索
		
		@Autowired
		public void setSearchManager(SearchManager searchManager) {
			this.searchManager = searchManager;
		}
		
		@Autowired
		public void setManager(AnnexManager manager) {
			this.manager = manager;
		}

		@Autowired
		public void setPrivilManager(TempPrivilManager privilManager) {
			this.privilManager = privilManager;
		}

		@Autowired
		 public void setUserService(IUserService userService) {
			this.userService = userService;
		}
		
		@Autowired
		SessionFactory sessionFactory; // 提供session

		/**
		    * @roseuid 493F83E700CB
		 */
		 public TempFileService() 
		 {
		    
		 }
		 
		 @Autowired
		 public void setSessionFactory(SessionFactory sessionFactory) 
		 {
			 	tempFileDao = new GenericDAOHibernate<ToaArchiveTempfile, java.lang.String>(sessionFactory,
					  ToaArchiveTempfile.class);
		 }
		   		
		public String gettime(Date date,String type){
			SimpleDateFormat formar=new SimpleDateFormat(type);
			String time=formar.format(date);
			return time;
		}
		
		/**
	    * @author：pengxq
	    * @time：2008-12-29下午06:21:44
	    * @desc：保存年内文件
	    * @param ToaArchiveTempfile toaArchiveTempfile 年内文件对象
	    * @return void
	    */
	   public void saveTempfile(ToaArchiveTempfile toaArchiveTempfile,OALogInfo ... loginfos)throws SystemException,ServiceException{
		   try{
			   if(toaArchiveTempfile.getTempfileDate()!=null){
				   toaArchiveTempfile.setTempfileYear(gettime(toaArchiveTempfile.getTempfileDate(),"yyyy"));
				   toaArchiveTempfile.setTempfileMonth(gettime(toaArchiveTempfile.getTempfileDate(),"MM"));
			   }
			   if(toaArchiveTempfile.getTempfileDepartmentName()==null){//如果部门名为空时，
				   if(toaArchiveTempfile.getTempfileDepartment()!=null&&!toaArchiveTempfile.getTempfileDepartment().equals("")){
					   Organization org=userService.getDepartmentByOrgId(toaArchiveTempfile.getTempfileDepartment());//通过部门ID，获取部门信息
					   if(org!=null){
						   toaArchiveTempfile.setTempfileDepartmentName(org.getOrgName());//对归档的文件，设置部门名
					   }
				   }
			   }
			   
			   if(toaArchiveTempfile.getTempfileOrgCode()==null){															//机构CODE是否为空
					User user=userService.getCurrentUser();
				    TUumsBaseOrg org= userService.getSupOrgByUserIdByHa(user.getUserId());
				    if(org!=null){
				    	toaArchiveTempfile.setTempfileOrgCode(org.getSupOrgCode());											//对归档的文档添加机构CODE和ID
				    	toaArchiveTempfile.setTempfileOrgId(org.getOrgId());
				    }
				}
			   
			   tempFileDao.save(toaArchiveTempfile);
			   String [] archiveFile=new String[1];//定义档案文件数组,做全文检索所检索文件的字段用
				archiveFile[0]=toaArchiveTempfile.getTempfileTitle();//档案文件题名
//				archiveFile[1]=toaArchiveTempfile.getTempfileDeadline();//档案文件保管权限
//				archiveFile[2]=toaArchiveTempfile.getTempfileDepartmentName();//档案文件所属部门
//				archiveFile[3]=toaArchiveTempfile.getTempfileDesc();//档案文件备注
//				archiveFile[4]=toaArchiveTempfile.getTempfileAuthor();//责任者
//				archiveFile[5]= getDateTime(toaArchiveTempfile.getTempfileDate());//档案文件时间
//				archiveFile[6]=toaArchiveTempfile.getTempfileNo();//档案文件文号
			   String businessId = toaArchiveTempfile.getTempfileDocId();
			   String[] args = businessId.split(";");
			   String tableName = args[0];
			   String pkFieldName = args[1];
			   String pkFieldValue = args[2];
			   String ptId = toaArchiveTempfile.getTempfileDocType();
			   StringBuilder sql = new StringBuilder("select * from ");
			   sql.append(tableName).append(" where ").append(pkFieldName);
			   sql.append(" = '").append(pkFieldValue).append("'");
			   ResultSet rs = executeJdbcQuery(sql.toString());
			   ByteArrayOutputStream baos = new ByteArrayOutputStream();
			   InputStream is = null;
			   byte[] content =null;
			   try {
				while(rs.next()){
					 	Object  obj = new Object();
					   if(ptId.equals(""+WorkFlowTypeConst.RECEDOC)){			//收文归档
						   obj  =  rs.getObject("CONTENT");
					   }else if(ptId.equals(""+WorkFlowTypeConst.SENDDOC)){      //发文归档
						   obj  =  rs.getObject("SENDDOC_CONTENT");
					   }
					   if (obj instanceof Blob) {
							Blob bObj = (Blob) obj;
							is = bObj.getBinaryStream();
							int input = 0;
							byte[] buf = new byte[8192];
							try {
								while ((input = is.read(buf)) != -1) {
									baos.write(buf, 0, input);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							content = baos.toByteArray();
							ToaArchiveTfileAppend tappend = new ToaArchiveTfileAppend();
							Set<ToaArchiveTfileAppend> tempFileAppend = new HashSet<ToaArchiveTfileAppend>();
							tappend.setTempappendContent(content);
							tappend.setTempappendName(toaArchiveTempfile.getTempfileTitle()); 
							tempFileAppend.add(tappend);
							toaArchiveTempfile.setToaArchiveTfileAppends(tempFileAppend);
						} else {
						}

					   }
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			   Set toaArchiveTfileAppends=toaArchiveTempfile.getToaArchiveTfileAppends();		   
			   if(toaArchiveTfileAppends!=null&&toaArchiveTfileAppends.size()>0){
				   for(Iterator<ToaArchiveTfileAppend> iterator=toaArchiveTfileAppends.iterator();iterator.hasNext();){
					   ToaArchiveTfileAppend annex=iterator.next();
					   annex.setToaArchiveTempfile(toaArchiveTempfile);
					  if(toaArchiveTempfile.getTempfileDocType()!=null){
						  if(annex.getTempappendType()!=null&&"2".equals(annex.getTempappendType())){
							  annex.setTempappendName(annex.getTempappendName()+".pdf");
						  }else{
							  annex.setTempappendName(annex.getTempappendName()+".doc");
						  }
					   }
					   manager.saveAppend(annex);
					   try {
//							File file2 = FileUtil.byteArray2File(fileappend.getAppendContent());//把附件二进制数组转化为文件。
							File file2=byteArray2File(annex.getTempappendContent());
							FileInputStream indexfis = new FileInputStream(file2);
							searchManager.saveIndex(annex, archiveFile, indexfis);//保存档案文件搜索索引
//							file2.deleteOnExit();
							file2.delete();
						} catch (FileNotFoundException e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						} catch (Exception e) {
							// TODO 自动生成 catch 块
							e.printStackTrace();
						}
				   }
				   
			   }else {
				   searchManager.saveIndex(toaArchiveTempfile);//如果档案不存在附件，对当前档案文件添加索引
			   }
			   if(toaArchiveTempfile.getTempfileDepartment()!=null&&!"".equals(toaArchiveTempfile.getTempfileDepartment())){
				   List<User> userList=userService.getUsersByOrgID(toaArchiveTempfile.getTempfileDepartment());
				   List<String> useridList=new ArrayList<String>();
				   for(User user:userList){
					   useridList.add(user.getUserId());
				   }
				   privilManager.save(useridList, toaArchiveTempfile.getTempfileId(),null);
			   }
		   }catch(ServiceException e){
			   throw new ServiceException(MessagesConst.save_error,               
						new Object[] {"年内文件对象"});
		   }
	   }

	   /**
		 * 将一个字节数组对象转换成一个文件对象
		 * @author:邓志城
		 * @date:2009-7-17 下午05:45:38
		 * @param input
		 * @return
		 * @throws Exception
		 */
		public  File byteArray2File(byte[] input)throws Exception{
			File file = null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			try{
				//C:\DOCUME~1\ADMINI~1\LOCALS~1\Temp\test52933.temp
				file = File.createTempFile("test", ".tmp");//创建临时文件
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				bos.write(input);
//				file.deleteOnExit();
			}catch(Exception e){
				throw new SystemException("字节数组转成文件异常：" + e);
			}finally{
				if(bos!=null){
					bos.close();
				}
				if(fos!=null){
					fos.close();
				}
			}
			return file;
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
		 * 执行JDBC查询
		 * 
		 * @author:邓志城
		 * @date:2009-12-11 下午05:25:33
		 * @param sql
		 * @return
		 * @throws DAOException
		 */
		protected ResultSet executeJdbcQuery(String sql) throws DAOException {
			try {
				return getConnection().prepareStatement(sql).executeQuery();
			} catch (SQLException e) {
				throw new DAOException(e);
			}
		}
		
		/**
		 * 得到数据库连接
		 * 
		 * @author:邓志城
		 * @date:2009-12-11 下午05:22:34
		 * @return
		 */
		@SuppressWarnings("deprecation")
		protected Connection getConnection() {
			return getSession().connection();
		}
  
		
		/**
		 * 为子类提供session,方便jdbc相关操作
		 * 
		 * @author:邓志城
		 * @date:2009-12-11 下午04:22:43
		 * @return Hibernate session.
		 */
		protected Session getSession() {
			return sessionFactory.getCurrentSession();
		}
}
