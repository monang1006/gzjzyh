/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：年内文件管理manager
 */

package com.strongit.oa.archive.tempfile;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.archive.archivefile.ArchiveFileAppendManager;
import com.strongit.oa.archive.archivefile.ArchiveFileManager;
import com.strongit.oa.archive.archivefolder.ArchiveFolderManager;
import com.strongit.oa.bo.ToaArchiveFile;
import com.strongit.oa.bo.ToaArchiveFileAppend;
import com.strongit.oa.bo.ToaArchiveFolder;
import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.bo.ToaArchiveTfileAppend;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.FiltrateContent;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.workflow.workflowDao.WorkflowDao;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.sun.tools.xjc.model.Model;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
@Transactional
@OALogger
public class TempFileManager extends BaseManager {
	
	private ArchiveFolderManager fordermanager;	//案卷manager
	private AnnexManager annexMananger;	//附件manager
	/** 用户管理Service接口*/
	private IUserService userservice;
	private ArchiveFileManager filemanager;
	private ArchiveFileAppendManager appendManager;
	@Autowired private TempPrivilManager privilManager;
	private static final String ZE="0";//常量0
	private static final String ONE="1";// 常量1
	private static final String TWO="2";// 常量2
	private static final String THREE="3";
	private static final String FOUR="4";


	private GenericDAOHibernate<ToaArchiveTempfile, java.lang.String> tempFileDao;
	private WorkflowDao serviceDAO=null;

	/**
	 * @roseuid 493F83E700CB
	 */
	public TempFileManager() 
	{

	}
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) 
	{
		tempFileDao = new GenericDAOHibernate<ToaArchiveTempfile, java.lang.String>(sessionFactory,
				ToaArchiveTempfile.class);
		serviceDAO = new WorkflowDao(sessionFactory, Object.class);
	}

	/**
	 * @author：pengxq
	 * @time：2008-12-29下午06:26:42
	 * @desc：根据逐渐获取年内文件对象
	 * @param String id 年内文件主键
	 * @return 年内文件对象
	 */
	public ToaArchiveTempfile getTempFile(String id,OALogInfo ... loginfos) throws SystemException,ServiceException{   
		try{
			ToaArchiveTempfile obj=null;
			obj=tempFileDao.get(id);
			return obj;
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"年内文件对象"});
		} 
	}

	/**
	 * 
	 * @author zhengzb
	 * @desc 搜索档案中心文件，所显示的字段
	 * 2010-4-29 下午14:58:41 
	 * @return 返回所要显示的字段
	 */
	public String sqlString() throws SystemException,ServiceException{
		String  hql="select t.tempfileId,t.tempfileNo,t.tempfileAuthor,t.tempfileTitle,t.tempfileDate,t.tempfilePage,t.tempfileDepartment,t.tempfileDepartmentName,t.tempfileDocType," +
					" t.tempfileDocId,t.toaArchiveFolder.folderId,t.tempfilePieceNo,t.tempfileDesc ";
		return hql;
	}
	
	/**
	 * @author：pengxq
	 * @time：2008-12-29下午06:22:34
	 * @desc：获取分页的分页的年内文件列表
	 * @param Page<ToaArchiveTempfile> page
	 * @param String status 是否入卷标识
	 * @return 分页的年内文件列表
	 */
	@Transactional(readOnly = true)
	public Page<ToaArchiveTempfile> getPageTempFile(Page<ToaArchiveTempfile> page,String status,String treeType,String treeValue,String depLogo,OALogInfo ... loginfos)  throws SystemException,ServiceException
	{
		try{
			String supOrgCode="";
			User user=userservice.getCurrentUser();
//			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			//Organization org=userservice.getUserDepartmentByUserId(user.getUserId());
			//String orgId=org.getOrgId();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			supOrgCode=org.getSupOrgCode();
			String orgIds="";
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(depLogo!=null&&!depLogo.equals("")){
				orgIds=" t.tempfileDepartment like '"+user.getOrgId()+"'";
			}else{
				if(isView){
					if(org!=null){
						orgIds="t.tempfileOrgCode like '"+supOrgCode+"%'";							
					}
				}else {
					if(org!=null){
						//orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
						orgIds="t.tempfileOrgCode like '"+supOrgCode+"'";		
					}
				}
				
			}
			String hql="";
			if(status!=null&&TWO.equals(status)){
				if(org!=null){
					hql=sqlString()+" from ToaArchiveTempfile t where "+orgIds+"  and t.toaArchiveFolder.folderId is not null order by t.tempfileDate desc" ;					
				}else {
					hql=sqlString()+" from ToaArchiveTempfile t where  t.toaArchiveFolder.folderId is not null order by t.tempfileDate desc" ;					
				}
			}else if(status!=null&&ONE.equals(status)){
				if(org!=null){
					hql=sqlString()+" from ToaArchiveTempfile t where  "+orgIds+" and t.toaArchiveFolder.folderId is null order by t.tempfileDate desc";
				}else {
					
					hql=sqlString()+" from ToaArchiveTempfile t where   t.toaArchiveFolder.folderId is null order by t.tempfileDate desc";
				}
			} else{
				String str="";
				if(treeValue!=null&&!"".equals(treeValue)&&!"null".equals(treeValue)){
					if(ONE.equals(treeType)){//来文类型
						if(ZE.equals(treeValue)){
							str=" (t.tempfileDocType is null or t.tempfileDocType='0' ) ";
						}else{
							str=" t.tempfileDocType=? ";
						}
					}else if(TWO.equals(treeType)){//案卷
						if(ZE.equals(treeValue)){
							str=" t.toaArchiveFolder.folderId is null ";
						}else{
							str=" t.toaArchiveFolder.folderId=? ";
						}
					}else if(THREE.equals(treeType)){//发文部门
						if(ZE.equals(treeValue)){
							str=" t.tempfileDepartment is null ";
						}else{
							str=" t.tempfileDepartment=? ";
						}
					}else if("4".equals(treeType)){
						String [] sr=treeValue.split(",");
						if(sr!=null&&sr.length>0){
							str=" t.tempfileYear=? ";
							if(sr.length>1){
								str=str+" and t.tempfileMonth='"+sr[1]+"'";
								treeValue=sr[0];
							}
						}
					}
					hql=sqlString()+" from ToaArchiveTempfile t where  "+str;
					if(org!=null){
						hql=hql+" and "+orgIds;
					}
					hql=hql+" order by t.tempfileDate desc";
					if("0".equals(treeValue)){
						page=tempFileDao.find(page, hql);
					}else{
						page=tempFileDao.find(page, hql, treeValue);
					}
					List templist=page.getResult();
					if(templist!=null&&templist.size()>0){
						page.setResult(packagingList(templist));
					}
					return page;
				}
				hql=sqlString()+" from ToaArchiveTempfile t where  "+orgIds+"  order by t.tempfileDate desc";
			}
			page= tempFileDao.find(page, hql);
			List templist=page.getResult();
			if(templist!=null&&templist.size()>0){
				page.setResult(packagingList(templist));
			}
			return page;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"分页年内文件列表"}); 
		}
	}

	/**
	 * @author：pengxq
	 * @time：2008-12-29下午06:21:44
	 * @desc：保存年内文件
	 * @param ToaArchiveTempfile toaArchiveTempfile 年内文件对象
	 * @return void
	 */
	public String saveTempfile(ToaArchiveTempfile toaArchiveTempfile,OALogInfo ... loginfos) throws SystemException,ServiceException
	{
		String msg="";
		try{
			if(toaArchiveTempfile.getTempfileDocType()==null||"".equals(toaArchiveTempfile.getTempfileDocType())){
				toaArchiveTempfile.setTempfileDocType(TempFileType.TEMPFILE);
			}
			tempFileDao.save(toaArchiveTempfile);
			msg="保存年内文件成功！";
		}catch(Exception e){
			msg="保存年内文件失败！";
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"年内文件对象"}); 
		}
		return msg;
	}

	/**
	 * @author：pengxq
	 * @time：2008-12-29下午06:20:56
	 * @desc：删除年内文件
	 * @param String tempfileId 年内文件id
	 * @return void
	 */
	public void deleteTempfile(String tempfileId,OALogInfo ... loginfos)throws SystemException,ServiceException {
		try{
			annexMananger.deleteAnnex(tempfileId);
			privilManager.deleteByTempfileId(tempfileId, new OALogInfo("删除档案中心文件权限"));
			tempFileDao.delete(tempfileId);		//删除年内文件
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"年内文件对象"}); 
		} 
	}
	/**结果输出
     * @author xush
     * @date 6/8/2013 1:50 PM
     */
    public InputStream searchExcel( ToaArchiveTempfile file,String disLogo,String state,String folderId)throws DAOException{
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFRow row;
        HSSFCell cell;
        HSSFSheet sheet = wb.createSheet("sheet1");
        row = sheet.createRow(0);
        cell = row.createCell((short) 0);  
        int m=0;
        //row = sheet.createRow(0);
        HSSFCellStyle cellStyle= wb.createCellStyle();
        HSSFFont font = wb.createFont();
        sheet.setColumnWidth((short)m,(short)5000);  
        cell = row.createCell((short) m++);      
        cell.setCellValue("件号");
        cellStyle= wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        
        cell = row.createCell((short) m++);
        sheet.setColumnWidth((short)m,(short)5000);
        cell.setCellValue("责任者");
        cellStyle= wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        
        sheet.setColumnWidth((short)m,(short)5000); 
        cell = row.createCell((short) m++);
        cell.setCellValue("文号");
        cellStyle= wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        
        sheet.setColumnWidth((short)m,(short)5000);
        cell = row.createCell((short) m++);
        cell.setCellValue("题名");
        cellStyle= wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        
        sheet.setColumnWidth((short)m,(short)5000);
        cell = row.createCell((short) m++);
        cell.setCellValue("日期");
        cellStyle= wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        
        sheet.setColumnWidth((short)m,(short)5000);
        cell = row.createCell((short) m++);
        cell.setCellValue("页数");
        cellStyle= wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        
        
        List<ToaArchiveTempfile> list = exportBysearch(file,folderId,"","","");//获取满足条件的ToaArchiveTempfile对象
        for (int i = 0; i < list.size()+0; ++i)
        {
            ToaArchiveTempfile folder = list.get(i);
            SimpleDateFormat st = new SimpleDateFormat(
            "yyyy-MM-dd");
            row = sheet.createRow(i + 1);

            int j=0;
//            cell = row.createCell((short) j++);
//            cell.setCellValue(i + 1);
//            cellStyle= wb.createCellStyle();
//            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
//            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getTempfilePieceNo());//当前对象的件号
            cellStyle= wb.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getTempfileAuthor());
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getTempfileNo());
            cellStyle= wb.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
           
            
            
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getTempfileTitle());
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue(st.format(folder.getTempfileDate()));
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            if(folder.getTempfilePage()!=null){
             cell.setCellValue(folder.getTempfilePage());}
            else{
                cell.setCellValue("");  
            }
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            
            
            
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try{
                 wb.write(os);
            }catch (IOException e){
                 e.printStackTrace();
            }
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            return is;
      }
	/**
	 * @author:pengxq
	 * @time：2008-12-29下午06:17:29
	 * @desc：年内文件管理模块，根据查询条件查询年内文件
	 * @param Page<ToaArchiveTempfile> page 分页对象
	 * @param ToaArchiveTempfile objs 年内文件对象
	 * @param String status 文件状态
	 * @return Page 年内文件分页列表
	 */
	@Transactional(readOnly = true)
	public Page<ToaArchiveTempfile> search(Page<ToaArchiveTempfile> page,ToaArchiveTempfile objs,String status,String depLogo,String fileFolder,OALogInfo ... loginfos) throws SystemException,ServiceException
	{
		try{
			page=this.queryByCon(page,objs,fileFolder,status," order by t.tempfileDate desc, t.toaArchiveFolder.folderId desc",depLogo);
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"分页年内文件列表"}); 
		}
	}

	/**
	 * @author：pengxq
	 * @time：2008-12-29下午06:17:29
	 * @desc：对选择的文件入卷
	 * @param String folderId 案卷id
	 * @param String[] fileId  文件id数组
	 * @return void
	 */
	public String portalFile(String folderId,String[] fileId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		String msg="";
		try{
			if(fileId.length>0){
				for(int i=0;i<fileId.length;i++){
					ToaArchiveTempfile tempFile=this.getTempFile(fileId[i]);
					ToaArchiveFolder toaArchiveFolder = fordermanager.getFolder(folderId);
					tempFile.setToaArchiveFolder(toaArchiveFolder);
					this.saveTempfile(tempFile);
				}
			} 
			msg="年内文件入卷成功！";
		}catch(ServiceException e){
			msg="年内文件入卷失败！";
			throw new ServiceException(MessagesConst.operation_error,               
					new Object[] {"年内文件入卷"}); 
		}
		return msg;
	}

	 /**
	    * @author：hull
	    * @time：2010-01-18
	    * @desc：对选择的文件入卷归档
	    * @param String folderId 案卷id
	    * @param String[] fileId  文件id数组
	    * @return void
	    */
	   public String portalFileAuthing(String folderId,String[] fileId,OALogInfo...infos) throws SystemException,ServiceException{
		  String msg="";
		   try{
			   if(fileId.length>0){
				   for(int i=0;i<fileId.length;i++){
					   ToaArchiveTempfile tempFile=this.getTempFile(fileId[i]);
					   ToaArchiveFolder toaArchiveFolder = fordermanager.getFolder(folderId);
					   
					   ToaArchiveFile file = new ToaArchiveFile();/**新建一个档案文件对象*/

						/**将年内文件中的值赋给档案文件中*/
					    file.setToaArchiveFolder(toaArchiveFolder);
						file.setFileAuthor(tempFile.getTempfileAuthor());/**作者*/
						file.setFileDate(tempFile.getTempfileDate());/**创建日期*/
						file.setFileDepartment(tempFile.getTempfileDepartment());/**单位*/
						file.setFileDesc(tempFile.getTempfileDesc());/**描述*/
						file.setFileDocId(tempFile.getTempfileDocId());/**收发文编号*/
						file.setFileDocType(tempFile.getTempfileDocType());/**收发文类型*/
						file.setFileNo(tempFile.getTempfileNo());/**文件编号*/
						file.setFilePage(tempFile.getTempfilePage());/**文件页数*/
						file.setFileTitle(tempFile.getTempfileTitle());/**文件标题*/
						file.setFileYear(tempFile.getTempfileYear());
						file.setFileMonth(tempFile.getTempfileMonth());
						file.setFileFormId(tempFile.getTempfileFormId());
						file.setWorkflow(tempFile.getWorkflow());
					    filemanager.saveFile(file);/**保存案卷文件*/

						ToaArchiveTfileAppend tfileappend=null;/** 根据年内文件ID查找文件的附件*/

		                Set<ToaArchiveTfileAppend> set=tempFile.getToaArchiveTfileAppends();
		                Iterator<ToaArchiveTfileAppend> appiterator=set.iterator();
		                if(appiterator.hasNext()){
		                	while(appiterator.hasNext()){
		                		tfileappend=appiterator.next();
		                		if(tfileappend!=null&&tfileappend.getTempappendId()!=null){
		                			ToaArchiveFileAppend fileappend = new ToaArchiveFileAppend();/**新建一个档案文件附件对象*/

		                			/**将年内文件附件中的值赋给档案文件附件中*/
		                			fileappend.setAppendName(tfileappend.getTempappendName());/**附件名称*/
		                			fileappend.setAppendContent(tfileappend
		                					.getTempappendContent());/**附件内容*/
		                			fileappend.setAppendType(tfileappend.getTempappendType());/**附件类型*/
		                			fileappend.setAppendSize(tfileappend.getTempappendSize());/**附件大小*/
		                			fileappend.setToaArchiveFile(file);/**档案文件对象*/
		                			appendManager.saveFileAppend(fileappend);/**保存案卷文件附件*/
		                		}
		                	}
		                }
		                
		                deleteTempfile(tempFile.getTempfileId());
				   }
			   } 
			  
			   msg="年内文件入卷成功！";
		   }catch(ServiceException e){
			   msg="年内文件入卷失败！";
			   throw new ServiceException(MessagesConst.operation_error,               
						new Object[] {"年内文件入卷"}); 
		   }
		   return msg;
	   }
	/**
	 * @author：pengxq
	 * @time：2008-12-29下午06:15:28
	 * @desc：对选择的文件组卷
	 * @param ToaArchiveFolder toaArchiveFolder 案卷对象
	 * @param String[] fileId 文件id数组
	 * @return void
	 */
	public void gropFile(ToaArchiveFolder toaArchiveFolder,String[] fileId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			if(fileId.length>0){
				for(int i=0;i<fileId.length;i++){
					ToaArchiveTempfile tempFile=this.getTempFile(fileId[i]);
					tempFile.setToaArchiveFolder(toaArchiveFolder);
					this.saveTempfile(tempFile);
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.operation_error,               
					new Object[] {"年内文件组卷"}); 
		}
	}

	/**
	 * @author：pengxq
	 * @time：2008-12-29下午06:13:29
	 * @desc：根据案卷id获取案卷对象
	 * @param String folderId 案卷id
	 * @return ToaArchiveFolder 案卷对象
	 */
	public ToaArchiveFolder getFolderById(String folderId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{ 
			return fordermanager.getFolder(folderId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 案卷对象"}); 
		}   
	} 
	/** 
	 * @author: 郑志斌  2010/04/14 20：57
	 * @desc:根据案卷id获取"ToaArchiveTempfile"文件列表
	 * @param : String folderId 案卷id
	 * @return :文件"ToaArchiveTempfile"列表
	 * */
	public List<ToaArchiveTempfile> getTempfileByForderId(String folderId,OALogInfo ... loginfos)throws SystemException,ServiceException{  
		try{
			String hql =" from ToaArchiveTempfile t where t.toaArchiveFolder.folderId = ? order by t.tempfileDate desc ";
			List<ToaArchiveTempfile> list =tempFileDao.find(hql.toString(), folderId);
			return list;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 文件'ToaArchiveTempfile'列表"}); 
		}
	}

	/**
	 * @author：pengxq
	 * @time：2008-12-29下午06:14:38
	 * @desc：根据案卷id获取文件列表
	 * @param String folderId 案卷id
	 * @return 文件列表
	 */
	public List getFileByFolderId(String folderId,OALogInfo ... loginfos)throws SystemException,ServiceException{  
		try{
			List list=null;
			String hql=" from ToaArchiveTempfile t where t.toaArchiveFolder.folderId = ? order by t.tempfileDate desc";
			list=tempFileDao.find(hql, folderId);
			if(list!=null){
				list=packagingList1(list);
				return	list;
			}
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 文件列表"}); 
		}	  
	}
	
	/**
	 * @author：pengxq
	 * @time：2009-1-4下午06:43:05
	 * @desc: 案卷归档模块中，查看未归档案卷的文件，按查询条件查询
	 * @param String folderId 案卷编号
	 * @param ToaArchiveTempfile objs 年内文件对象
	 * @return List 查询结果
	 */
	public List getFileByFolderId(String folderId,ToaArchiveTempfile objs ,OALogInfo ... loginfos)throws SystemException,ServiceException{	  
		try{
			List list=null;
			list=this.queryByCon1(objs, folderId, "3", "  order by t.tempfileDate desc");
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 文件列表"}); 
		}   
	}

	/**
	 * @author：ganyao
	 * @time：June 28,2013
	 * @desc: 案卷模块中，查看未归档案卷的文件，根据案卷的id查询
	 * @param String folderId 案卷编号
	 * @param ToaArchiveTempfile objs 文件对象
	 * @return List 查询结果
	 */
	public List<ToaArchiveTempfile> getFileByFolderIds(String folderId,ToaArchiveTempfile objs ,OALogInfo ... loginfos)throws SystemException,ServiceException{	  
		try{
			List<ToaArchiveTempfile> list=new ArrayList<ToaArchiveTempfile>();
			list=this.queryByCon2(objs, folderId, "   order by t.tempfileNo,t.toaArchiveFolder.folderId desc");
			return list; 
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 文件列表"}); 
		} 
		
		
		
	}
	/***
     * 
    * 方法简要描述
    *    处理对特殊字符%的处理
    *  <br/>查询条件中包含%，添加转义字符，将%进行转义
    *  
    *  
    *
    * JavaDoc tags,比如
    * @Date:7/26/2013 2:47 PM
    * @Author xush
    * @param paramlist
    * @param keyStr
    * @param value
    * @return
    * @version  1.0
    * @see
     */
    public  String addPercent(final List paramlist,String keyStr,String value){
        StringBuffer sql=new StringBuffer();
        if(!"".equals(value)&&value!=null){
            sql.append(" and "+keyStr+" like ?");
            if(value.contains("%") ||value.contains("_") ){
                sql.append(" ESCAPE '#' ");
                value=value.replace("%", "#%");
                value=value.replace("_", "#_");
                paramlist.add("%"+value+"%"); 
            }else{
                paramlist.add("%"+value+"%");
            }           
        }
        return sql.toString(); 
    }
	/**
     * @author:xush
     * @time：6/13/2013 1:49 PM
     * @desc：根据查询条件导出列表数据
     * @param 
     * @param ToaArchiveTempfile objs 文件对象
     * @param String folderId 案卷id
     * @param String status 文件状态
     * @param String orderBy 排序
     * @return Page 分页列表
     */
	public List<ToaArchiveTempfile> exportBysearch(ToaArchiveTempfile objs,String folderId,String status,String orderBy,String depLogo,OALogInfo ... loginfos)throws SystemException,ServiceException{  
        try{
            String hql="";
            List<Object> list=new ArrayList<Object>();
            User user=userservice.getCurrentUser();
            List orgList=userservice.getOrgAndChildOrgByCurrentUser();
            //Organization org=userservice.getUserDepartmentByUserId(user.getUserId());
            //String orgId=org.getOrgId();
            
            TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
            String orgIds="";
            if(depLogo!=null&&!depLogo.equals("")){
                orgIds=" t.tempfileDepartment like '"+user.getOrgId()+"'";
            }else{
                boolean isView=userService.isViewChildOrganizationEnabeld();            //是否允许看到下级机构
                
                if(isView){
                    if(org!=null){
                        orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";                         
                    }
                }else {
                    if(org!=null){
                        orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";       
                        //orgIds="t.tempfileOrgId = ?";
                        //list.add(org.getOrgId());
                    }
                }
                
            }
            
//          
            if(status!=null&&"2".equals(status))
                if(org!=null){
                    
                    hql=" from ToaArchiveTempfile t  where  "+orgIds+" and t.toaArchiveFolder.folderId is not null";
                }else {
                    hql=" from ToaArchiveTempfile t  where  t.toaArchiveFolder.folderId is not null";
                }
            else if(status!=null&&"1".equals(status))
                if(org!=null){
                    hql=" from ToaArchiveTempfile t where  "+orgIds+" and t.toaArchiveFolder.folderId is null";
                }else {                 
                    hql=" from ToaArchiveTempfile t where  t.toaArchiveFolder.folderId is null";
                }
            else if(status!=null&&"3".equals(status))//案卷归档模块中，查看未归档案卷的文件，按查询条件查询
                if(org!=null){
                    hql=" from ToaArchiveTempfile t where  "+orgIds+" and t.toaArchiveFolder.folderId='"+folderId+"'";
                }else {
                    
                    hql=" from ToaArchiveTempfile t where  t.toaArchiveFolder.folderId='"+folderId+"'";
                }
            else if(objs.getToaArchiveFolder().getFolderNo()!=null&&!"".equals(objs.getToaArchiveFolder().getFolderNo()))//资料中心：卷（盒）号搜索   郑志斌修改
            {
                if(org!=null){
                    hql=" from ToaArchiveTempfile t  where 1=1  and  "+orgIds+" and (t.toaArchiveFolder.folderId is not null and t.toaArchiveFolder.folderNo like '%"+objs.getToaArchiveFolder().getFolderNo()+"%') ";
                }else {
                    
                    hql=" from ToaArchiveTempfile t  where 1=1   and (t.toaArchiveFolder.folderId is not null and t.toaArchiveFolder.folderNo like '%"+objs.getToaArchiveFolder().getFolderNo()+"%') ";
                }
            }
            else {
                if(org!=null){
                    hql=" from ToaArchiveTempfile t  where 1=1  and "+orgIds;       
                }else {
                    hql=" from ToaArchiveTempfile t ";                              
                }
            }
            //文件编号
            if(objs.getTempfileNo()!=null&&!"".equals(objs.getTempfileNo())&&!"null".equals(objs.getTempfileNo())){
                //hql+= " and t.tempfileNo like ?"; 
                //objs.setTempfileNo(objs.getTempfileNo().trim());
                //list.add("%" + objs.getTempfileNo() + "%");
                int t = objs.getTempfileNo().indexOf("%");
                if(t!=-1){
                    String temp =objs.getTempfileNo().replaceAll("%","/%");;
                    hql+=" and t.tempfileNo like '%"
                            + temp + "%'" +" escape '/'";
                }else{
                  hql+= " and t.tempfileNo like ?"; 
                    objs.setTempfileNo(objs.getTempfileNo().trim());
                    list.add("%" + objs.getTempfileNo() + "%");
                }
            }
            //文件名称
            if(objs.getTempfileTitle()!=null&&!"".equals(objs.getTempfileTitle())&&!"null".equals(objs.getTempfileTitle())){
                //hql+= " and t.tempfileTitle like ?"; 
                //objs.setTempfileTitle(objs.getTempfileTitle().trim());
                //list.add("%" + objs.getTempfileTitle() + "%");
                int t = objs.getTempfileTitle().indexOf("%");
                if(t!=-1){
                    String temp =objs.getTempfileTitle().replaceAll("%","/%");;
                    hql+=" and t.tempfileTitle like '%"
                            + temp + "%'" +" escape '/'";
                }else{
                  hql+= " and t.tempfileTitle like ?"; 
                  objs.setTempfileTitle(objs.getTempfileTitle().trim());
                    list.add("%" + objs.getTempfileTitle() + "%");
                }
            }
            //年份
            if(objs.getTempfileYear()!=null&&!"".equals(objs.getTempfileYear())){
                hql=hql+" and t.tempfileYear=?";
                list.add("%" + objs.getTempfileYear() + "%");
            }
            //月份
            if(objs.getTempfileMonth()!=null&&!"".equals(objs.getTempfileMonth())){
                hql=hql+" and t.tempfileMonth=? ";
                list.add(objs.getTempfileMonth());
            }
            //保管期限
            if(objs.getTempfileDeadline()!=null&&!"".equals(objs.getTempfileDeadline())){
                hql=hql+" and t.tempfileDeadline like ? ";
                list.add("%" + objs.getTempfileDeadline() + "%");
            }
            if(folderId!=null&&!"".equals(folderId)){
                String[] str = folderId.split(",");
                 if("0".equals(folderId)){
                     hql=hql+" and t.toaArchiveFolder.folderId is null ";
                    }else{
                        if(str.length==1){
                            hql=hql+" and t.toaArchiveFolder.folderId=?";
                            list.add(str[0]);
                        }else{
                        for (int j = 0; j < str.length; j++) {
                            if(j==0){
                                hql=hql+" and (t.toaArchiveFolder.folderId=?";
                                list.add(str[0]);
                            }else if(j==str.length-1){
                                hql=hql+" or t.toaArchiveFolder.folderId=?)";
                                list.add(str[str.length-1]);
                                }else{
                                    hql=hql+" or t.toaArchiveFolder.folderId=?";
                                    list.add(str[j]);
                                }
                                }
                            }
                    }
                }
            //备注
            if(objs.getTempfileDesc()!=null&&!"".equals(objs.getTempfileDesc())){
                hql=hql+" and t.tempfileDesc=? ";
                objs.setTempfileDesc(objs.getTempfileDesc().trim());
                list.add("%" + objs.getTempfileDesc() + "%");
            }
            hql=hql+" order by t.tempfileDate desc, t.toaArchiveFolder.folderId desc";
            List<ToaArchiveTempfile> tempfileList=tempFileDao.find(hql.toString(), list.toArray());
            return tempfileList;
        }catch(ServiceException e){
            throw new ServiceException(); 
        }  
    }

	/**
	 * @author:pengxq
	 * @time：2008-12-29下午06:17:29
	 * @desc：根据查询条件查询年内文件
	 * @param Page<ToaArchiveTempfile> page 分页列表
	 * @param ToaArchiveTempfile objs 年内文件对象
	 * @param String folderId 案卷id
	 * @param String status 文件状态
	 * @param String orderBy 排序
	 * @return Page 分页列表
	 */
	public Page queryByCon(Page<ToaArchiveTempfile> page,ToaArchiveTempfile objs,String folderId,String status,String orderBy,String depLogo,OALogInfo ... loginfos)throws SystemException,ServiceException{  
		try{
			String hql="";
			Object[] obj=new Object[125];
			int i = 0;
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			//Organization org=userservice.getUserDepartmentByUserId(user.getUserId());
			//String orgId=org.getOrgId();
			SimpleDateFormat st=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			String orgIds="";
			if(depLogo!=null&&!depLogo.equals("")){
				orgIds=" t.tempfileDepartment like '"+user.getOrgId()+"'";
			}else{
				boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
				
				if(isView){
					if(org!=null){
						orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
					}
				}else {
					if(org!=null){
						orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
					}
				}
				
			}
			
//			for(int j=0;j<orgList.size();j++){
//				org=(Organization)orgList.get(j);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
			if(status!=null&&"2".equals(status))
				if(org!=null){
					
					hql=sqlString()+" from ToaArchiveTempfile t  where  "+orgIds+" and t.toaArchiveFolder.folderId is not null";
				}else {
					hql=sqlString()+" from ToaArchiveTempfile t  where  t.toaArchiveFolder.folderId is not null";
				}
			else if(status!=null&&"1".equals(status))
				if(org!=null){
					hql=sqlString()+" from ToaArchiveTempfile t where  "+orgIds+" and t.toaArchiveFolder.folderId is null";
				}else {					
					hql=sqlString()+" from ToaArchiveTempfile t where  t.toaArchiveFolder.folderId is null";
				}
			else if(status!=null&&"3".equals(status))//案卷归档模块中，查看未归档案卷的文件，按查询条件查询
				if(org!=null){
					hql=sqlString()+" from ToaArchiveTempfile t where  "+orgIds+" and t.toaArchiveFolder.folderId='"+folderId+"'";
				}else {
					
					hql=sqlString()+" from ToaArchiveTempfile t where  t.toaArchiveFolder.folderId='"+folderId+"'";
				}
			else if(objs.getToaArchiveFolder().getFolderNo()!=null&&!"".equals(objs.getToaArchiveFolder().getFolderNo()))//资料中心：卷（盒）号搜索   郑志斌修改
			{   String folderNo = objs.getToaArchiveFolder().getFolderNo().replace("//", "//////").replace("%", "//%").replace("'", "''");  
				if(org!=null){
					hql=sqlString()+" from ToaArchiveTempfile t  where 1=1  and  "+orgIds+" and (t.toaArchiveFolder.folderId is not null and t.toaArchiveFolder.folderNo like '%"+folderNo+"%') ";
				}else {
					
					hql=sqlString()+" from ToaArchiveTempfile t  where 1=1   and (t.toaArchiveFolder.folderId is not null and t.toaArchiveFolder.folderNo like '%"+folderNo+"%') ";
				}
			}
			else {
				if(org!=null){
					hql=sqlString()+" from ToaArchiveTempfile t  where 1=1  and "+orgIds;		
				}else {
					hql=sqlString()+" from ToaArchiveTempfile t ";								
				}
			}
			//文件编号
			if(objs.getTempfileNo()!=null&&!"".equals(objs.getTempfileNo())&&!"null".equals(objs.getTempfileNo())){

				//hql+= " and t.tempfileNo like ?"; 
				//objs.setTempfileNo(objs.getTempfileNo().trim());

				String tempFileNo = objs.getTempfileNo().trim();
				if(tempFileNo.indexOf("%")!=-1){
					tempFileNo =tempFileNo.replaceAll("%","/%");
					hql+=" and t.tempfileNo like '%" + tempFileNo + "%' ESCAPE '/' ";
					objs.setTempfileNo(objs.getTempfileNo());
				}
				else{
					hql+= " and t.tempfileNo like ?"; 
					objs.setTempfileNo(tempFileNo);
					obj[i]="%"+objs.getTempfileNo()+"%";
					i++;
				}
			}
				

				//obj[i]="%"+objs.getTempfileNo()+"%";
				//i++;
			    //int t = objs.getTempfileNo().indexOf("%");
                //if(t!=-1){
                   // String temp =objs.getTempfileNo().replaceAll("%","/%");
                    //hql+= " and t.tempfileNo like  '%"
                               // + temp + "%'" +" escape '/'";
                    
                //}else{
                   // hql+= " and t.tempfileNo like ?"; 
                    //objs.setTempfileNo(objs.getTempfileNo().trim());
                    
                    //obj[i]="%"+objs.getTempfileNo()+"%";
                   // i++;
                //}

			//}
			//文件名称
			if(objs.getTempfileTitle()!=null&&!"".equals(objs.getTempfileTitle())&&!"null".equals(objs.getTempfileTitle())){

				///hql+= " and t.tempfileTitle like ?"; 
				//objs.setTempfileTitle(objs.getTempfileTitle().trim());
				//obj[i]="%"+objs.getTempfileTitle()+"%";
			    int t = objs.getTempfileTitle().indexOf("%");
                if(t!=-1){
                    String temp =objs.getTempfileTitle().replaceAll("%","/%");
                    hql+= " and t.tempfileTitle like  '%"
                                + temp + "%'" +" escape '/'";
                    
                }else{
                    hql+= " and t.tempfileTitle like ?"; 
                    objs.setTempfileTitle(objs.getTempfileTitle().trim());
                    obj[i]="%"+objs.getTempfileTitle()+"%";
                    i++;
                }
				

				String tempFileTitle = objs.getTempfileTitle().trim();
				if(tempFileTitle.indexOf("%")!=-1){
					tempFileTitle =tempFileTitle.replaceAll("%","/%");
					hql+=" and t.tempfileTitle like '%" + tempFileTitle + "%' ESCAPE '/'  ";
					objs.setTempfileTitle(objs.getTempfileTitle());
				}
				else{
					hql+= " and t.tempfileTitle like ?"; 
					objs.setTempfileTitle(objs.getTempfileTitle());
					obj[i]="%"+objs.getTempfileTitle()+"%";
					i++;
				}

			}
			//文件创建日期
			if(objs.getTempfileDate()!=null&&!"".equals(objs.getTempfileDate())&&!"null".equals(objs.getTempfileDate())){
				hql+=" and t.tempfileDate >=?";
				obj[i]=objs.getTempfileDate();
				i++;
			}
			//文件创建日期
            if(objs.getTempfileDate()!=null&&!"".equals(objs.getTempfileDate())&&!"null".equals(objs.getTempfileDate())){
                hql+=" and t.tempfileDate  <?";
                Calendar date = Calendar.getInstance();
                date.setTime(objs.getTempfileDate());       
                date.set(Calendar.DATE, date.get(Calendar.DATE) +1);     
                Date endDate = date.getTime();
                
                obj[i]=endDate;
                i++;
            }
			//文件作者
			if(objs.getTempfileAuthor()!=null&&!"".equals(objs.getTempfileAuthor())&&!"null".equals(objs.getTempfileAuthor())){
				String tempFileAuthor = objs.getTempfileAuthor().trim();
				if(tempFileAuthor.indexOf("%")!=-1){
					tempFileAuthor =tempFileAuthor.replaceAll("%","/%");
					hql+=" and t.tempfileAuthor like '%" + tempFileAuthor + "%' ESCAPE '/'  ";
					objs.setTempfileAuthor(objs.getTempfileAuthor());
				}
				else{
					hql+= " and t.tempfileAuthor like ?"; 
					objs.setTempfileAuthor(objs.getTempfileAuthor());
					obj[i]="%"+objs.getTempfileAuthor()+"%";
					i++;
				}
			}
			//文件页码
			if(objs.getTempfilePage()!=null&&!"".equals(objs.getTempfilePage())&&!"null".equals(objs.getTempfilePage())){
				hql+= " and t.tempfilePage = ?";
				obj[i]=objs.getTempfilePage();		
				i++;
			} 
			String tt=objs.getTempfileDocType();
			//文件类型
			if(objs.getTempfileDocType()!=null&&!"".equals(objs.getTempfileDocType())&&!"null".equals(objs.getTempfileDocType())){
				if("0".equals(objs.getTempfileDocType())){
					hql=hql+" and t.tempfileDocType ='0' ";
				}else{
					hql=hql+" and t.tempfileDocType=? ";
					objs.setTempfileDocType(objs.getTempfileDocType().trim());
					obj[i]=objs.getTempfileDocType();
					i++;
				}
			}
			//所属部门
			if(objs.getTempfileDepartment()!=null&&!"".equals(objs.getTempfileDepartment())){
				if("0".equals(objs.getTempfileDepartment())){
					hql=hql+" and t.tempfileDepartment is null";
				}else{
					hql=hql+" and t.tempfileDepartment=? ";
					obj[i]=objs.getTempfileDepartment();
					objs.setTempfileDepartment(objs.getTempfileDepartment().trim());
					i++;
				}
			}
			//部门名称
			if(objs.getTempfileDepartmentName()!=null&&!"".equals(objs.getTempfileDepartmentName())){
				if("0".equals(objs.getTempfileDepartmentName())){
					hql=hql+" and t.tempfileDepartmentName is null";
				}else{
					
					String tempFileDepartmentName = objs.getTempfileDepartmentName().trim();
					if(tempFileDepartmentName.indexOf("%")!=-1){
						tempFileDepartmentName =tempFileDepartmentName.replaceAll("%","/%");
						hql+=" and t.tempfileDepartmentName like '%" + tempFileDepartmentName + "%' ESCAPE '/'  ";
						objs.setTempfileDepartmentName(objs.getTempfileDepartmentName());
					}
					else{
						hql+= " and t.tempfileDepartmentName like ?"; 
						objs.setTempfileDepartmentName(objs.getTempfileDepartmentName());
						obj[i]="%"+objs.getTempfileDepartmentName()+"%";
						i++;
					}
				}
			}
			//所属案卷
			if(objs.getToaArchiveFolder()!=null&&objs.getToaArchiveFolder().getFolderId()!=null&&!"".equals(objs.getToaArchiveFolder().getFolderId())){
				if("0".equals(objs.getToaArchiveFolder().getFolderId())){
					hql=hql+" and  t.toaArchiveFolder.folderId is null ";
				}else{
				hql=hql+" and  t.toaArchiveFolder.folderId=? ";
				obj[i]=objs.getToaArchiveFolder().getFolderId();
				i++;
				}
			} 
			if(folderId!=null&&!"".equals(folderId)){
		        String[] str = folderId.split(",");
		         if("0".equals(folderId)){
		             hql=hql+" and t.toaArchiveFolder.folderId is null ";
		            }else{
		                if(str.length==1){
		                    hql=hql+" and t.toaArchiveFolder.folderId=?";
		                    obj[i]=str[0];
		                    i++;
		                }else{
		                for (int j = 0; j < str.length; j++) {
		                    if(j==0){
		                        hql=hql+" and (t.toaArchiveFolder.folderId=?";
		                        obj[i]=str[0];
	                            i++;
		                    }else if(j==str.length-1){
		                        hql=hql+" or t.toaArchiveFolder.folderId=?)";
		                        obj[i]=str[str.length-1];
                                i++;
		                        }else{
		                            hql=hql+" or t.toaArchiveFolder.folderId=?";
		                            obj[i]=str[j];
	                                i++;
		                        }
		                        }
		                    }
		            }
		        }
			//年份
			if(objs.getTempfileYear()!=null&&!"".equals(objs.getTempfileYear())){
				hql=hql+" and t.tempfileYear=?";
				obj[i]=objs.getTempfileYear();
				i++;
			}
			//月份
			if(objs.getTempfileMonth()!=null&&!"".equals(objs.getTempfileMonth())){
				hql=hql+" and t.tempfileMonth=? ";
				obj[i]=objs.getTempfileMonth();
				i++;
			}
			//保管期限
            if(objs.getTempfileDeadline()!=null&&!"".equals(objs.getTempfileDeadline())){
                hql=hql+" and t.tempfileDeadline like ? ";
                obj[i]="%"+objs.getTempfileDeadline()+"%";
                i++;
            }
			//备注
			if(objs.getTempfileDesc()!=null&&!"".equals(objs.getTempfileDesc())){
				hql=hql+" and t.tempfileDesc=? ";
				objs.setTempfileDesc(objs.getTempfileDesc().trim());
				obj[i]=objs.getTempfileDesc();
				i++;
			}
			//件号
			if(objs.getTempfilePieceNo()!=null&&!"".equals(objs.getTempfilePieceNo())){
				hql=hql+" and t.tempfilePieceNo like ? ";
				objs.setTempfilePieceNo(objs.getTempfilePieceNo().trim());
				obj[i]="%"+objs.getTempfilePieceNo()+"%";
				i++;
			}

			hql=hql+orderBy; 
			Object[] param=new Object[i];
			for(int k=0,t=0;k<obj.length;k++){
				if(obj[k]!=null){
					param[t]=obj[k];
					t++;
				}
			}
			page=tempFileDao.find(page, hql, param);
			List tempfileList = page.getResult();
			if (tempfileList != null) {
				page.setResult(packagingList(tempfileList));
			}
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 文件分页列表"}); 
		}  
	}

	/**
	 * @author:pengxq
	 * @time：2008-12-29下午06:17:29
	 * @desc：根据查询条件查询年内文件
	 * @param ToaArchiveTempfile objs 年内文件对象
	 * @param String folderId 案卷id
	 * @param String status 文件状态
	 * @param String orderBy 排序
	 * @return List 年内文件列表
	 */
	public List queryByCon1(ToaArchiveTempfile objs,String folderId,String status,String orderBy,OALogInfo ... loginfos)throws SystemException,ServiceException{  
		try{
			User user=  userservice.getCurrentUser();
			List list=null;
			String hql="";
			Object[] obj=new Object[30];
			int i = 0;

			if(status!=null&&"2".equals(status))
				hql=sqlString()+" from ToaArchiveTempfile t where t.toaArchiveFolder.folderId is not null";
			else if(status!=null&&"1".equals(status))
				hql=sqlString()+" from ToaArchiveTempfile t where t.toaArchiveFolder.folderId is null";
			else if(status!=null&&"3".equals(status))//案卷归档模块中，查看未归档案卷的文件，按查询条件查询
				hql=sqlString()+" from ToaArchiveTempfile t where t.toaArchiveFolder.folderId='"+folderId+"'";
			else
				hql=sqlString()+" from ToaArchiveTempfile t where 1=1 ";
			
			
			//文件件号
			if(objs.getTempfilePieceNo()!=null&&!"".equals(objs.getTempfilePieceNo())&&!"null".equals(objs.getTempfilePieceNo())){
				hql+= " and t.tempfilePieceNo like ?"; 
				obj[i]="%"+objs.getTempfilePieceNo()+"%";
				i++;
			}
			//文件编号
			if(objs.getTempfileNo()!=null&&!"".equals(objs.getTempfileNo())&&!"null".equals(objs.getTempfileNo())){
				hql+= " and t.tempfileNo like ?"; 
				obj[i]="%"+objs.getTempfileNo()+"%";
				i++;
			}
			//文件名称
			if(objs.getTempfileTitle()!=null&&!"".equals(objs.getTempfileTitle())&&!"null".equals(objs.getTempfileTitle())){
				hql+= " and t.tempfileTitle like ?"; 
				obj[i]="%"+objs.getTempfileTitle()+"%";
				i++;
			}
			//文件创建日期
			if(objs.getTempfileDate()!=null&&!"".equals(objs.getTempfileDate())&&!"null".equals(objs.getTempfileDate())){
				hql+=" and t.tempfileDate >=?";
				obj[i]=objs.getTempfileDate();
				i++;
			}
			
            //文件创建日期
            if(objs.getTempfileDate()!=null&&!"".equals(objs.getTempfileDate())&&!"null".equals(objs.getTempfileDate())){
                hql+=" and t.tempfileDate  <?";
                Calendar date = Calendar.getInstance();
                date.setTime(objs.getTempfileDate());       
                date.set(Calendar.DATE, date.get(Calendar.DATE) +1);     
                Date endDate = date.getTime();
                
                obj[i]=endDate;
                i++;
            }
			//文件作者
			if(objs.getTempfileAuthor()!=null&&!"".equals(objs.getTempfileAuthor())&&!"null".equals(objs.getTempfileAuthor())){
				hql+= " and t.tempfileAuthor like ?";
				obj[i]="%"+objs.getTempfileAuthor()+"%";		
				i++;
			}
			//文件页码
			if(objs.getTempfilePage()!=null&&!"".equals(objs.getTempfilePage())&&!"null".equals(objs.getTempfilePage())){
				hql+= " and t.tempfilePage = ?";
				obj[i]=objs.getTempfilePage();		
				i++;
			} 
			
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){	
					hql=hql+" and t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";
				}
			}else {
				if(org!=null){
					hql=hql+" and t.tempfileOrgId ='"+org.getOrgId()+"'";
				}
			}
			
			
			hql=hql+orderBy; 
			Query query=tempFileDao.createQuery(hql);
			if(i==0){
				query= tempFileDao.createQuery(hql.toString());;
			}	  
			else if(i==1){	
				query = tempFileDao.createQuery(hql.toString(), obj[0]);	   
			}
			else if(i==2){
				query = tempFileDao.createQuery(hql.toString(),obj[0],obj[1]);
			}
			else if(i==3){
				query = tempFileDao.createQuery(hql.toString(),obj[0],obj[1],obj[2]);
			}else if(i==4){  
				query = tempFileDao.createQuery(hql.toString(),obj[0],obj[1],obj[2],obj[3]);
			}else if(i==5){  
				query = tempFileDao.createQuery(hql.toString(),obj[0],obj[1],obj[2],obj[3],obj[4]);
			}
			if(query!=null){
				list = query.list();
			}
			if (list != null) {
				return packagingList(list);
			}
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 文件分页列表"}); 
		}

	}



	/**
	 * @author：pengxq
	 * @time：2009-1-3下午03:32:18
	 * @desc：根据文件id获取文件附件对象
	 * @param String tempFileId 文件id
	 * @return ToaArchiveTfileAppend 附件对象
	 */
	public ToaArchiveTfileAppend getAnnex(String tempFileId,OALogInfo ... loginfos)throws SystemException,ServiceException{  
		try{
			ToaArchiveTfileAppend obj=null;
			obj=annexMananger.getAnnex(tempFileId);
			return obj;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 附件对象"}); 
		}  
	}
	/**
	    * @author：胡丽丽
	    * @time：2009-12-23
	    * @desc：根据文件id获取文件附件对象
	    * @param String tempFileId 文件id
	    * @return ToaArchiveTfileAppend 附件对象
	    */
	   public ToaArchiveTfileAppend getFileAnnex(String id)throws SystemException,ServiceException{  
		   try{
			   ToaArchiveTfileAppend obj=null;
			   obj=annexMananger.getFileAnnex(id);
			   return obj;
		   }catch(ServiceException e){
			   throw new ServiceException(MessagesConst.find_error,               
						new Object[] {" 附件对象"}); 
		   }  
	   }
	/**
	 * @author：pengxq
	 * @time：2009-1-3下午03:45:46
	 * @desc：根据案卷id获取案卷的状态
	 * @param String folderId 案卷id
	 * @return 案卷状态
	 */
	public String getFolderStatus(String folderId,OALogInfo ... loginfos)throws SystemException,ServiceException{  
		try{
			String status="";
			status=fordermanager.getFolderStatus(folderId);
			return status;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 案卷状态"});
		}     
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午01:57:11
	 * @desc: 文档流直接写入HttpServletResponse请求
	 * @param response HttpServletResponse请求
	 * @param String tempFileId 年内文件id
	 * @return void
	 */
	public void setContentToHttpResponse(HttpServletResponse response, String tempFileId, String tfileAppedId,OALogInfo ... loginfos){
		try{
			annexMananger.setContentToHttpResponse(response, tempFileId,tfileAppedId);
		}catch(ServiceException e){
			e.printStackTrace();
		}
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-7下午05:28:12
	 * @desc: 文件编号是否有重复的
	 * @param
	 * @return true 重复 false 没有重复
	 */
	public boolean isExist(String tempfileNo,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			boolean flag=true;
			String hql="from ToaArchiveTempfile t where t.tempfileNo=?";
			List list=tempFileDao.find(hql, tempfileNo);
			if(list.size()==0){
				flag=false;
			}
			return flag;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 文件编号是否存在"});
		}	
	}

	/**
	 * 
	 * @author：pengxq
	 * @time：2009-3-4上午09:55:30
	 * @desc: 根据部门id获取部门名称
	 * @param String orgId 部门id
	 * @return 部门名称
	 */
	public String getOrgNameById(String orgId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			Organization org=userservice.getDepartmentByOrgId(orgId);
			if(org!=null){
				return org.getOrgName();
			}else{
				return null;
			}	
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 文件编号是否存在"});
		}	
	}

	/**
	 * 根据年份分组查询文件
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<String> getTempfileYear()
	throws SystemException, ServiceException {
		try {
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			String orgIds="";
			
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){
					orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
				}
			}else {
				if(org!=null){
					orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
				}
			}
			
			
//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
			String hql = "select t.tempfileYear from ToaArchiveTempfile t where 1=1";
			if(org!=null){
				hql=hql+" and "+orgIds;
			}
			hql = hql + " group by t.tempfileYear";
			Query query = tempFileDao.createQuery(hql);
			List<String> list = query.list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据年份统计" });
		}
	}

	
	/**
	 * 根据年份分组查询部门文件
	 * @author qibaohua 2012-02-13
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<String> getTempfileYearDepartment(String depLogo)
	throws SystemException, ServiceException {
		try {
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			String orgIds="";
			String department = userService.getOrgInfoByOrgId(user.getOrgId()).getOrgName();
			String departments="";
			
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){
					orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
				}
			}else {
				if(org!=null){
					orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
				}
			}
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				departments="t.tempfileDepartmentName = '"+department+"'";
			}
			
//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
			String hql = "select t.tempfileYear from ToaArchiveTempfile t where 1=1";
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				hql=hql+" and "+departments;
			}
			if(org!=null){
				hql=hql+" and "+orgIds;
			}
			hql = hql + " group by t.tempfileYear";
			Query query = tempFileDao.createQuery(hql);
			List<String> list = query.list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据年份统计" });
		}
	}

	/**
	 * 根据年份分组查询文件
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<String> getTempfileYear(ToaArchiveTempfile temp)
	throws SystemException, ServiceException {
		try {
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
//			Organization org=new Organization();
			String orgIds="";
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){
					orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
				}
			}else {
				if(org!=null){
					orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
				}
			}
			
//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
			
			List<Object> list=new ArrayList<Object>();
			String hql = "select t.tempfileYear from ToaArchiveTempfile t where  t.tempfileDepartment in ("+orgIds+")  ";
			//文件编号
			if(temp.getTempfileNo()!=null&&!"".equals(temp.getTempfileNo())&&!"null".equals(temp.getTempfileNo())){
				hql+= " and t.tempfileNo like ?"; 
				list.add("%"+temp.getTempfileNo()+"%");
			}
			//文件名称
			if(temp.getTempfileTitle()!=null&&!"".equals(temp.getTempfileTitle())&&!"null".equals(temp.getTempfileTitle())){
				hql+= " and t.tempfileTitle like ?"; 
				list.add("%"+temp.getTempfileTitle()+"%");
			}
			//文件创建日期
			if(temp.getTempfileDate()!=null&&!"".equals(temp.getTempfileDate())&&!"null".equals(temp.getTempfileDate())){
				hql+=" and t.tempfileDate >=?";
				list.add(temp.getTempfileDate());
			}
			//文件作者
			if(temp.getTempfileAuthor()!=null&&!"".equals(temp.getTempfileAuthor())&&!"null".equals(temp.getTempfileAuthor())){
				hql+= " and t.tempfileAuthor like ?";
				list.add("%"+temp.getTempfileAuthor()+"%");
			}
			//文件页码
			if(temp.getTempfilePage()!=null&&!"".equals(temp.getTempfilePage())&&!"null".equals(temp.getTempfilePage())){
				hql+= " and t.tempfilePage = ?";
				list.add(temp.getTempfilePage());	
			} 
			//文件类型
			if(temp.getTempfileDocType()!=null&&!"".equals(temp.getTempfileDocType())&&!"null".equals(temp.getTempfileDocType())){
				if("0".equals(temp.getTempfileDocType())){
					hql=hql+" and t.tempfileDocType is null ";
				}else{
				hql=hql+" and t.tempfileDocType=? ";
				list.add(temp.getTempfileDocType());	
				}
			}
			//所属部门
			if(temp.getTempfileDepartment()!=null&&!"".equals(temp.getTempfileDepartment())){
				if("0".equals(temp.getTempfileDepartment())){
					hql=hql+" and t.tempfileDepartment is null";
				}else{
				hql=hql+" and t.tempfileDepartment=? ";
				list.add(temp.getTempfileDepartment());
				}
			}
			//所属案卷
			if(temp.getToaArchiveFolder()!=null&&temp.getToaArchiveFolder().getFolderId()!=null&&!"".equals(temp.getToaArchiveFolder().getFolderId())){
				if("0".equals(temp.getToaArchiveFolder().getFolderId())){
					hql=hql+" and  t.toaArchiveFolder.folderId is null ";
				}else{
				hql=hql+" and  t.toaArchiveFolder.folderId=? ";
				list.add(temp.getToaArchiveFolder().getFolderId());
				}
			} 
			//年份
			if(temp.getTempfileYear()!=null&&!"".equals(temp.getTempfileYear())){
				hql=hql+" and t.tempfileYear=?";
				list.add(temp.getTempfileYear());
			}
			//月份
			if(temp.getTempfileMonth()!=null&&!"".equals(temp.getTempfileMonth())){
				hql=hql+" and t.tempfileMonth=? ";
				list.add(temp.getTempfileMonth());
			}
			hql = hql + " group by t.tempfileYear";
			Query query=null;
			if(list.size()>0){
			Object[] param=new Object[list.size()];
			for(int k=0;k<list.size();k++){
				param[k]=list.get(k);
			}
			 query=tempFileDao.createQuery(hql,param);
			}else{
				query=tempFileDao.createQuery(hql);
			}
			List<String> yearlist = query.list();
			return yearlist;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据年份统计" });
		}
	}
	/**
	 * 根据月份分组查询文件
	 * 
	 * @param year
	 * @return
	 */
	public List<String> getTempfileMonth(String year) throws SystemException, ServiceException {
		try {
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			String orgIds="";
			
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){
					orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
				}
			}else {
				if(org!=null){
					orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
				}
			}
			
//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
			String hql = "select t.tempfileMonth from ToaArchiveTempfile t where t.tempfileYear='"+year+"'";
			if(org!=null){
				hql=hql+" and "+orgIds;
			}
			
			hql = hql + " group by t.tempfileMonth";
			Query query = tempFileDao.createQuery(hql);
			List<String> list = query.list();
			return list;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据月份统计" });
		}
	}
	/**
	 * 根据月份分组查询部门文件
	 * 
	 * @param year
	 * @return
	 */
	public List<String> getTempfileMonthDepartment(String year,String depLogo) throws SystemException, ServiceException {
		try {
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			String orgIds="";
			String department = userService.getOrgInfoByOrgId(user.getOrgId()).getOrgName();
			String departments="";
			
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){
					orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
				}
			}else {
				if(org!=null){
					orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
				}
			}
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				departments="t.tempfileDepartmentName = '"+department+"'";
			}
//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
			String hql = "select t.tempfileMonth from ToaArchiveTempfile t where t.tempfileYear='"+year+"'";
			if(org!=null){
				hql=hql+" and "+orgIds;
			}
			if (depLogo != null && !depLogo.equals("")
								&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
							hql=hql+" and "+departments;
						}
			
			hql = hql + " group by t.tempfileMonth";
			Query query = tempFileDao.createQuery(hql);
			List<String> list = query.list();
			return list;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据月份统计" });
		}
	}
	/**
	 * 根据月份分组查询文件
	 * 
	 * @param year
	 * @return
	 */
	public List<String> getTempfileMonth(String year,ToaArchiveTempfile temp) throws SystemException, ServiceException {
		try {
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			String orgIds="";
			
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){
					orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
				}
			}else {
				if(org!=null){
					orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
				}
			}
//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
			
			List<Object> list=new ArrayList<Object>();
			String hql = "select t.tempfileMonth from ToaArchiveTempfile t where t.tempfileYear='"+year+"'";
			if(org!=null){
				hql=hql+ " and "+orgIds;
			}
			//文件编号
			if(temp.getTempfileNo()!=null&&!"".equals(temp.getTempfileNo())&&!"null".equals(temp.getTempfileNo())){
				hql+= " and t.tempfileNo like ?"; 
				list.add("%"+temp.getTempfileNo()+"%");
			}
			//文件名称
			if(temp.getTempfileTitle()!=null&&!"".equals(temp.getTempfileTitle())&&!"null".equals(temp.getTempfileTitle())){
				hql+= " and t.tempfileTitle like ?"; 
				list.add("%"+temp.getTempfileTitle()+"%");
			}
			//文件创建日期
			if(temp.getTempfileDate()!=null&&!"".equals(temp.getTempfileDate())&&!"null".equals(temp.getTempfileDate())){
				hql+=" and t.tempfileDate >=?";
				list.add(temp.getTempfileDate());
			}
			//文件作者
			if(temp.getTempfileAuthor()!=null&&!"".equals(temp.getTempfileAuthor())&&!"null".equals(temp.getTempfileAuthor())){
				hql+= " and t.tempfileAuthor like ?";
				list.add("%"+temp.getTempfileAuthor()+"%");
			}
			//文件页码
			if(temp.getTempfilePage()!=null&&!"".equals(temp.getTempfilePage())&&!"null".equals(temp.getTempfilePage())){
				hql+= " and t.tempfilePage = ?";
				list.add(temp.getTempfilePage());	
			} 
			//文件类型
			if(temp.getTempfileDocType()!=null&&!"".equals(temp.getTempfileDocType())&&!"null".equals(temp.getTempfileDocType())){
				if("0".equals(temp.getTempfileDocType())){
					hql=hql+" and t.tempfileDocType is null ";
				}else{
				hql=hql+" and t.tempfileDocType=? ";
				list.add(temp.getTempfileDocType());	
				}
			}
			//所属部门
			if(temp.getTempfileDepartment()!=null&&!"".equals(temp.getTempfileDepartment())){
				if("0".equals(temp.getTempfileDepartment())){
					hql=hql+" and t.tempfileDepartment is null";
				}else{
				hql=hql+" and t.tempfileDepartment=? ";
				list.add(temp.getTempfileDepartment());
				}
			}
			//所属案卷
			if(temp.getToaArchiveFolder()!=null&&temp.getToaArchiveFolder().getFolderId()!=null&&!"".equals(temp.getToaArchiveFolder().getFolderId())){
				if("0".equals(temp.getToaArchiveFolder().getFolderId())){
					hql=hql+" and  t.toaArchiveFolder.folderId is null ";
				}else{
				hql=hql+" and  t.toaArchiveFolder.folderId=? ";
				list.add(temp.getToaArchiveFolder().getFolderId());
				}
			} 
			hql = hql + " group by t.tempfileMonth";
			Query query=null;
			if(list.size()>0){
			Object[] param=new Object[list.size()];
			for(int k=0;k<list.size();k++){
				param[k]=list.get(k);
			}
			 query=tempFileDao.createQuery(hql,param);
			}else{
				query=tempFileDao.createQuery(hql);
			}
			List<String> monthlist = query.list();
			return monthlist;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据月份统计" });
		}
	}
	/**
	 * 根据文件类型分组文件
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<String> getTempfileType()throws SystemException, ServiceException  {
		try {
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			String orgIds="";
			
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){
					orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
				}
			}else {
				if(org!=null){
					orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
				}
			}
			
			
//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
			String hql ="select t.tempfileDocType from ToaArchiveTempfile t where  1=1";
			if(org!=null){
				hql=hql+" and "+orgIds+"   group by t.tempfileDocType";
			}else {
				hql=hql+"  group by t.tempfileDocType";
			}
			Query query=tempFileDao.createQuery(hql);
			List<String> list=query.list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据文件类型统计" });
		} 
	}
	/**
	 * 根据文件类型获得部门分组文件
	 * @author qibaohua  2012-02-13
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<String> getTempfileTypeDepartment(String depLogo)throws SystemException, ServiceException  {
		try {
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			String orgIds="";
			String department = userService.getOrgInfoByOrgId(user.getOrgId()).getOrgName();
			String departments="";
			
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){
					orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
				}
			}else {
				if(org!=null){
					orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
				}
			}
			
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				departments="t.tempfileDepartmentName = '"+department+"'";
			}
//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
			String hql ="select t.tempfileDocType from ToaArchiveTempfile t where  1=1";
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				hql=hql+" and "+departments;
			}
			if(org!=null){
				hql=hql+" and "+orgIds+"   group by t.tempfileDocType";
			}else {
				hql=hql+"  group by t.tempfileDocType";
			}
			Query query=tempFileDao.createQuery(hql);
			List<String> list=query.list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据文件类型统计" });
		} 
	}
	/**
	 * 根据文件类型分组文件
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<String> getTempfileType(ToaArchiveTempfile temp)throws SystemException, ServiceException  {
		try {
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			List<Object> list=new ArrayList<Object>();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			String orgIds="";
			
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){
					orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
				}
			}else {
				if(org!=null){
					orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
				}
			}
			
			
//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
			String hql ="select t.tempfileDocType from ToaArchiveTempfile t where 1=1";
			if(org!=null){
				hql=hql+" and "+orgIds;
			}
			//文件编号
			if(temp.getTempfileNo()!=null&&!"".equals(temp.getTempfileNo())&&!"null".equals(temp.getTempfileNo())){
				hql+= " and t.tempfileNo like ?"; 
				list.add("%"+temp.getTempfileNo()+"%");
			}
			//文件名称
			if(temp.getTempfileTitle()!=null&&!"".equals(temp.getTempfileTitle())&&!"null".equals(temp.getTempfileTitle())){
				hql+= " and t.tempfileTitle like ?"; 
				list.add("%"+temp.getTempfileTitle()+"%");
			}
			//文件创建日期
			if(temp.getTempfileDate()!=null&&!"".equals(temp.getTempfileDate())&&!"null".equals(temp.getTempfileDate())){
				hql+=" and t.tempfileDate >=?";
				list.add(temp.getTempfileDate());
			}
			//文件作者
			if(temp.getTempfileAuthor()!=null&&!"".equals(temp.getTempfileAuthor())&&!"null".equals(temp.getTempfileAuthor())){
				hql+= " and t.tempfileAuthor like ?";
				list.add("%"+temp.getTempfileAuthor()+"%");
			}
			//文件页码
			if(temp.getTempfilePage()!=null&&!"".equals(temp.getTempfilePage())&&!"null".equals(temp.getTempfilePage())){
				hql+= " and t.tempfilePage = ?";
				list.add(temp.getTempfilePage());	
			} 
			//文件类型
			if(temp.getTempfileDocType()!=null&&!"".equals(temp.getTempfileDocType())&&!"null".equals(temp.getTempfileDocType())){
				if("0".equals(temp.getTempfileDocType())){
					hql=hql+" and t.tempfileDocType is null ";
				}else{
				hql=hql+" and t.tempfileDocType=? ";
				list.add(temp.getTempfileDocType());	
				}
			}
			//所属部门
			if(temp.getTempfileDepartment()!=null&&!"".equals(temp.getTempfileDepartment())){
				if("0".equals(temp.getTempfileDepartment())){
					hql=hql+" and t.tempfileDepartment is null";
				}else{
				hql=hql+" and t.tempfileDepartment=? ";
				list.add(temp.getTempfileDepartment());
				}
			}
			//所属案卷
			if(temp.getToaArchiveFolder()!=null&&temp.getToaArchiveFolder().getFolderId()!=null&&!"".equals(temp.getToaArchiveFolder().getFolderId())){
				if("0".equals(temp.getToaArchiveFolder().getFolderId())){
					hql=hql+" and  t.toaArchiveFolder.folderId is null ";
				}else{
				hql=hql+" and  t.toaArchiveFolder.folderId=? ";
				list.add(temp.getToaArchiveFolder().getFolderId());
				}
			} 
			//年份
			if(temp.getTempfileYear()!=null&&!"".equals(temp.getTempfileYear())){
				hql=hql+" and t.tempfileYear=?";
				list.add(temp.getTempfileYear());
			}
			//月份
			if(temp.getTempfileMonth()!=null&&!"".equals(temp.getTempfileMonth())){
				hql=hql+" and t.tempfileMonth=? ";
				list.add(temp.getTempfileMonth());
			}
			hql=hql+" group by t.tempfileDocType"; 
			Query query=null;
			if(list.size()>0){
			Object[] param=new Object[list.size()];
			for(int k=0;k<list.size();k++){
				param[k]=list.get(k);
			}
			 query=tempFileDao.createQuery(hql,param);
			}else{
				query=tempFileDao.createQuery(hql);
			}
			List<String> typelist=query.list();
			return typelist;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据文件类型统计" });
		} 
	}
	
	/**
	 * 根据文件案卷分组文件
	 * @return
	 */
	/**
	 * @author zhengzb  2010/05/04 11:57 修改
	 * @desc 根据需求把搜索显示的字段”卷（盒）名“修改为”卷（盒）号“
	 */
	public List<Object> getTempfileFolder()throws SystemException, ServiceException  {
		try {
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			String orgIds="";
			
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){
					orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
				}
			}else {
				if(org!=null){
					orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
				}
			}
			
//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
//			String hql ="select t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderName from ToaArchiveTempfile t where  t.tempfileDepartment in ("+orgIds+")   group by t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderName";
			//zhengzb修改
			String hql ="select t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderNo from ToaArchiveTempfile t where 1=1";
			if(org!=null){
				hql=hql+" and "+orgIds+"   group by t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderNo";	
			}else {
				hql=hql+"  group by t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderNo";	
			}		 
			Query query=tempFileDao.createQuery(hql);
			List<Object> list=query.list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据文件类型统计" });
		} 
	}
	/**
	 * 根据文件案卷部门分组文件
	 * @return
	 * @author qibaohua 2012-02-13
	 * @desc 根据需求把搜索显示的字段”卷（盒）名“修改为”卷（盒）号“
	 */
	public List<Object> getTempfileFolderDepartment(String depLogo)throws SystemException, ServiceException  {
		try {
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			String orgIds="";
			String department = userService.getOrgInfoByOrgId(user.getOrgId()).getOrgName();
			String departments="";
			
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){
					orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
				}
			}else {
				if(org!=null){
					orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
				}
			}
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				departments="t.tempfileDepartmentName = '"+department+"'";
			}

//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);
//			String hql ="select t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderName from ToaArchiveTempfile t where  t.tempfileDepartment in ("+orgIds+")   group by t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderName";
			//zhengzb修改
			String hql ="select t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderNo from ToaArchiveTempfile t where 1=1";
			if (depLogo != null && !depLogo.equals("")
					&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
				hql=hql+" and "+departments;
			}
			if(org!=null){
				hql=hql+" and "+orgIds+"   group by t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderNo";	
			}else {
				hql=hql+"  group by t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderNo";	
			}		 
			Query query=tempFileDao.createQuery(hql);
			List<Object> list=query.list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据文件类型统计" });
		} 
	}
	/**
	 * 根据文件案卷分组文件
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<Object> getTempfileFolder(ToaArchiveTempfile temp)throws SystemException, ServiceException  {
		try {
			User user=userservice.getCurrentUser();
			List orgList=userservice.getOrgAndChildOrgByCurrentUser();
			TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
			String orgIds="";
			
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			if(isView){
				if(org!=null){
					orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
				}
			}else {
				if(org!=null){
					orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
				}
			}
			
//			for(int i=0;i<orgList.size();i++){
//				org=(Organization)orgList.get(i);
//				orgIds+=",'"+org.getOrgId()+"'";
//			}
//			orgIds=orgIds.substring(1);

			List<Object> list=new ArrayList<Object>();
			String hql ="";
			if(org!=null){
				hql ="select t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderName from ToaArchiveTempfile t where "+orgIds;
			}else {
				
				hql ="select t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderName from ToaArchiveTempfile t where 1=1";
			}
			
			//文件编号
			if(temp.getTempfileNo()!=null&&!"".equals(temp.getTempfileNo())&&!"null".equals(temp.getTempfileNo())){
				hql+= " and t.tempfileNo like ?"; 
				list.add("%"+temp.getTempfileNo()+"%");
			}
			//文件名称
			if(temp.getTempfileTitle()!=null&&!"".equals(temp.getTempfileTitle())&&!"null".equals(temp.getTempfileTitle())){
				hql+= " and t.tempfileTitle like ?"; 
				list.add("%"+temp.getTempfileTitle()+"%");
			}
			//文件创建日期
			if(temp.getTempfileDate()!=null&&!"".equals(temp.getTempfileDate())&&!"null".equals(temp.getTempfileDate())){
				hql+=" and t.tempfileDate >=?";
				list.add(temp.getTempfileDate());
			}
			//文件作者
			if(temp.getTempfileAuthor()!=null&&!"".equals(temp.getTempfileAuthor())&&!"null".equals(temp.getTempfileAuthor())){
				hql+= " and t.tempfileAuthor like ?";
				list.add("%"+temp.getTempfileAuthor()+"%");
			}
			//文件页码
			if(temp.getTempfilePage()!=null&&!"".equals(temp.getTempfilePage())&&!"null".equals(temp.getTempfilePage())){
				hql+= " and t.tempfilePage = ?";
				list.add(temp.getTempfilePage());	
			} 
			//文件类型
			if(temp.getTempfileDocType()!=null&&!"".equals(temp.getTempfileDocType())&&!"null".equals(temp.getTempfileDocType())){
				if("0".equals(temp.getTempfileDocType())){
					hql=hql+" and t.tempfileDocType is null ";
				}else{
				hql=hql+" and t.tempfileDocType=? ";
				list.add(temp.getTempfileDocType());	
				}
			}
			//所属部门
			if(temp.getTempfileDepartment()!=null&&!"".equals(temp.getTempfileDepartment())){
				if("0".equals(temp.getTempfileDepartment())){
					hql=hql+" and t.tempfileDepartment is null";
				}else{
				hql=hql+" and t.tempfileDepartment=? ";
				list.add(temp.getTempfileDepartment());
				}
			}
			//所属案卷
			if(temp.getToaArchiveFolder()!=null&&temp.getToaArchiveFolder().getFolderId()!=null&&!"".equals(temp.getToaArchiveFolder().getFolderId())){
				if("0".equals(temp.getToaArchiveFolder().getFolderId())){
					hql=hql+" and  t.toaArchiveFolder.folderId is null ";
				}else{
				hql=hql+" and  t.toaArchiveFolder.folderId=? ";
				list.add(temp.getToaArchiveFolder().getFolderId());
				}
			} 
			//年份
			if(temp.getTempfileYear()!=null&&!"".equals(temp.getTempfileYear())){
				hql=hql+" and t.tempfileYear=?";
				list.add(temp.getTempfileYear());
			}
			//月份
			if(temp.getTempfileMonth()!=null&&!"".equals(temp.getTempfileMonth())){
				hql=hql+" and t.tempfileMonth=? ";
				list.add(temp.getTempfileMonth());
			}
			hql=hql+"   group by t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderName"; 
			Query query=null;
			if(list.size()>0){
			Object[] param=new Object[list.size()];
			for(int k=0;k<list.size();k++){
				param[k]=list.get(k);
			}
			 query=tempFileDao.createQuery(hql,param);
			}else{
				query=tempFileDao.createQuery(hql);
			}
			List<Object> folderlist=query.list();
			return folderlist;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据文件类型统计" });
		} 
	}
	
	/**
	 * 根据分组类型获取类型为空的文件个数
	 * @param type
	 * @return
	 */
	public int gettempfileCountByType(String type){
		User user=userservice.getCurrentUser();
		List orgList=userservice.getOrgAndChildOrgByCurrentUser();
		TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
		String orgIds="";
		
		boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
		if(isView){
			if(org!=null){
				orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
			}
		}else {
			if(org!=null){
				orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
			}
		}
		
		
//		for(int i=0;i<orgList.size();i++){
//			org=(Organization)orgList.get(i);
//			orgIds+=",'"+org.getOrgId()+"'";
//		}
//		orgIds=orgIds.substring(1);
		String hql="";
		if(org!=null){
			hql="select count(t.tempfileId) from ToaArchiveTempfile t where  "+orgIds;
		}else {
			hql="select count(t.tempfileId) from ToaArchiveTempfile t where 1=1 ";
		}
			
		if("1".equals(type)){
			hql=hql+"  and t.tempfileDocType is null";
		}else if("2".equals(type)){
			hql=hql+" and t.toaArchiveFolder.folderId is null";
		}
		Object obj=tempFileDao.findUnique(hql);
		if(obj!=null){
			return Integer.parseInt(obj.toString());
		}else{
			return 0;
		}
	}
	/**
	 * 根据分组类型获取类型为空的文件个数
	 * @author qibaohua 2012-02-13
	 * @param type
	 * @return
	 */
	public int gettempfileCountByTypeDepartment(String type,String depLogo){
		User user=userservice.getCurrentUser();
		List orgList=userservice.getOrgAndChildOrgByCurrentUser();
		TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
		String orgIds="";
		String department = userService.getOrgInfoByOrgId(user.getOrgId()).getOrgName();
		String departments="";
		
		boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
		if(isView){
			if(org!=null){
				orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
			}
		}else {
			if(org!=null){
				orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
			}
		}
		if (depLogo != null && !depLogo.equals("")
				&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
			departments="t.tempfileDepartmentName = '"+department+"'";
		}

		
//		for(int i=0;i<orgList.size();i++){
//			org=(Organization)orgList.get(i);
//			orgIds+=",'"+org.getOrgId()+"'";
//		}
//		orgIds=orgIds.substring(1);
		String hql="";
		if(org!=null){
			hql="select count(t.tempfileId) from ToaArchiveTempfile t where  "+orgIds;
		}else {
			hql="select count(t.tempfileId) from ToaArchiveTempfile t where 1=1 ";
		}
		if (depLogo != null && !depLogo.equals("")
				&& !depLogo.equals("undefined")&& !depLogo.equals("null")) {
			hql=hql+" and "+departments;
		}	
		if("1".equals(type)){
			hql=hql+"  and t.tempfileDocType is null";
		}else if("2".equals(type)){
			hql=hql+" and t.toaArchiveFolder.folderId is null";
		}
		Object obj=tempFileDao.findUnique(hql);
		if(obj!=null){
			return Integer.parseInt(obj.toString());
		}else{
			return 0;
		}
	}
	/**
	 * 根据分组类型获取类型为空的文件个数
	 * @param type
	 * @return
	 */
	public int gettempfileCountByType(String type,ToaArchiveTempfile temp){
		User user=userservice.getCurrentUser();
		List orgList=userservice.getOrgAndChildOrgByCurrentUser();
		List<Object> list=new ArrayList<Object>();
		TUumsBaseOrg  org=userService.getSupOrgByUserIdByHa(user.getUserId());
		String orgIds="";
		
		boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
		if(isView){
			if(org!=null){
				orgIds="t.tempfileOrgCode like '"+org.getSupOrgCode()+"%'";							
			}
		}else {
			if(org!=null){
				orgIds="t.tempfileOrgId = '"+org.getOrgId()+"'";
			}
		}
		
//		for(int i=0;i<orgList.size();i++){
//			org=(Organization)orgList.get(i);
//			orgIds+=",'"+org.getOrgId()+"'";
//		}
//		orgIds=orgIds.substring(1);
		String hql="";
		if(org!=null){
			hql="select count(t.tempfileId) from ToaArchiveTempfile t where "+orgIds;			
		}else {
			hql="select count(t.tempfileId) from ToaArchiveTempfile t where  1=1 ";	
		}
		if("1".equals(type)){
			hql=hql+"  and t.tempfileDocType is null";
		}else if("2".equals(type)){
			hql=hql+" and t.toaArchiveFolder.folderId is null";
		}
		//文件编号
		if(temp.getTempfileNo()!=null&&!"".equals(temp.getTempfileNo())&&!"null".equals(temp.getTempfileNo())){
			hql+= " and t.tempfileNo like ?"; 
			list.add("%"+temp.getTempfileNo()+"%");
		}
		//文件名称
		if(temp.getTempfileTitle()!=null&&!"".equals(temp.getTempfileTitle())&&!"null".equals(temp.getTempfileTitle())){
			hql+= " and t.tempfileTitle like ?"; 
			list.add("%"+temp.getTempfileTitle()+"%");
		}
		//文件创建日期
		if(temp.getTempfileDate()!=null&&!"".equals(temp.getTempfileDate())&&!"null".equals(temp.getTempfileDate())){
			hql+=" and t.tempfileDate >=?";
			list.add(temp.getTempfileDate());
		}
		//文件作者
		if(temp.getTempfileAuthor()!=null&&!"".equals(temp.getTempfileAuthor())&&!"null".equals(temp.getTempfileAuthor())){
			hql+= " and t.tempfileAuthor like ?";
			list.add("%"+temp.getTempfileAuthor()+"%");
		}
		//文件页码
		if(temp.getTempfilePage()!=null&&!"".equals(temp.getTempfilePage())&&!"null".equals(temp.getTempfilePage())){
			hql+= " and t.tempfilePage = ?";
			list.add(temp.getTempfilePage());	
		} 
		//文件类型
		if(temp.getTempfileDocType()!=null&&!"".equals(temp.getTempfileDocType())&&!"null".equals(temp.getTempfileDocType())){
			if("0".equals(temp.getTempfileDocType())){
				hql=hql+" and t.tempfileDocType is null ";
			}else{
			hql=hql+" and t.tempfileDocType=? ";
			list.add(temp.getTempfileDocType());	
			}
		}
		//所属部门
		if(temp.getTempfileDepartment()!=null&&!"".equals(temp.getTempfileDepartment())){
			if("0".equals(temp.getTempfileDepartment())){
				hql=hql+" and t.tempfileDepartment is null";
			}else{
			hql=hql+" and t.tempfileDepartment=? ";
			list.add(temp.getTempfileDepartment());
			}
		}
		//所属案卷
		if(temp.getToaArchiveFolder()!=null&&temp.getToaArchiveFolder().getFolderId()!=null&&!"".equals(temp.getToaArchiveFolder().getFolderId())){
			if("0".equals(temp.getToaArchiveFolder().getFolderId())){
				hql=hql+" and  t.toaArchiveFolder.folderId is null ";
			}else{
			hql=hql+" and  t.toaArchiveFolder.folderId=? ";
			list.add(temp.getToaArchiveFolder().getFolderId());
			}
		} 
		//年份
		if(temp.getTempfileYear()!=null&&!"".equals(temp.getTempfileYear())){
			hql=hql+" and t.tempfileYear=?";
			list.add(temp.getTempfileYear());
		}
		//月份
		if(temp.getTempfileMonth()!=null&&!"".equals(temp.getTempfileMonth())){
			hql=hql+" and t.tempfileMonth=? ";
			list.add(temp.getTempfileMonth());
		}
		Object obj=null;
		if(list.size()>0){
		Object[] param=new Object[list.size()];
		for(int k=0;k<list.size();k++){
			param[k]=list.get(k);
		}
		 obj=tempFileDao.findUnique(hql,param);
		}else{
		 obj=tempFileDao.findUnique(hql);
		}
		if(obj!=null){
			return Integer.parseInt(obj.toString());
		}else{
			return 0;
		}
	}
	/**
	 * 组装文件对象
	 * @author hull
	 * @date 2010-01-21
	 * @param list
	 * @return
	 */
	public List<ToaArchiveTempfile> packagingList(List list){
		List<ToaArchiveTempfile> listfile=new ArrayList<ToaArchiveTempfile>();
		for (Object temp : list) {// 循环所有档案文件
			Object[] obj=(Object[])temp;
			ToaArchiveTempfile file=new ToaArchiveTempfile();
			file.setTempfileId(obj[0].toString());
			if(obj[1]!=null){
				file.setTempfileNo(obj[1].toString());
			}
			if(obj[2]!=null){
				file.setTempfileAuthor(obj[2].toString());
			}
			if(obj[3]!=null){
				file.setTempfileTitle(obj[3].toString());
			}
			if(obj[4]!=null){
				file.setTempfileDate((Date)obj[4]);
			}
			if(obj[5]!=null){
				file.setTempfilePage(Long.parseLong(obj[5].toString()));
			}
			if(obj[6]!=null){
				file.setTempfileDepartment(obj[6].toString());
			}
			if(obj[7]!=null){
				file.setTempfileDepartmentName(obj[7].toString());
			}
			if(obj[8]!=null){
				file.setTempfileDocType(obj[8].toString());
			}
			if(obj[9]!=null){
				file.setTempfileDocId(obj[9].toString());
			}
			
			Map<String, ToaArchiveFolder> map=getMapFolder();
			if(obj[10]!=null){
				if(map.get(obj[10].toString())!=null){
					file.setToaArchiveFolder(map.get(obj[10].toString()));
				}
			}
			if(obj[11]!=null){
				file.setTempfilePieceNo(obj[11].toString());
			}
			if(obj[12]!=null){
				file.setTempfileDesc(obj[12].toString());
			}
			listfile.add(file);
		}
		return listfile;
	}
	/**
     * 对归档出现的错误进行修改
     * 
     * @author xush
     * @date 7/9/2013 5:12 PM
     * @param list
     * @return
     */
    public List<ToaArchiveTempfile> packagingList1(List list){
        List<ToaArchiveTempfile> listfile=new ArrayList<ToaArchiveTempfile>();
        for (Object temp : list) {// 循环所有档案文件
            ToaArchiveTempfile obj=(ToaArchiveTempfile)temp;
            ToaArchiveTempfile file=new ToaArchiveTempfile();
            listfile.add(obj);
        }
        return listfile;
    }
	/**
	 * 获取案卷
	 * @return
	 */
	public Map<String, ToaArchiveFolder> getMapFolder(){
		ToaArchiveFolder model=new ToaArchiveFolder();
		//model.setFolderAuditing("0");
		List<ToaArchiveFolder> list=fordermanager.getAllFolerBySelect(model);
		Map<String, ToaArchiveFolder> map=new HashMap<String, ToaArchiveFolder>();
		if(list!=null){
			for(ToaArchiveFolder folder:list){
				map.put(folder.getFolderId(), folder);
			}
		}
		return map;
	}
	
	/**
	 * 根据资料中心文件Id查询资料中心文件是否存在
	 * @author zhengzb
	 * @desc 
	 * 2010-6-24 下午06:02:00 
	 * @param tempfileId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int  getIsTempfileBytempfileId(String tempfileId) throws SystemException,ServiceException {
		try {
			String hql="select count(*) from ToaArchiveTempfile t  ";
			hql=hql+" where t.tempfileId like ? ";					
		return Integer.parseInt(tempFileDao.findUnique(hql,tempfileId).toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[]{"根据资料中心文件Id查询资料中心文件是否存在"});	
		}
	}
	
	@Autowired
	public void setFordermanager(ArchiveFolderManager fordermanager) {
		this.fordermanager = fordermanager;
	}

	@Autowired
	public void setAnnexMananger(AnnexManager annexMananger) {
		this.annexMananger = annexMananger;
	}

	/**
	 * author:zhangli
	 * description:注册用户Service接口
	 * @param archivefilemanager 用户Service接口
	 */
	@Autowired
	public void setUserservice(IUserService userservice) {
		this.userservice = userservice;
	}
	@Autowired
	public void setFilemanager(ArchiveFileManager filemanager) {
		this.filemanager = filemanager;
	}
	@Autowired
	public void setAppendManager(ArchiveFileAppendManager appendManager) {
		this.appendManager = appendManager;
	}

	/**
	 * 通过流程类型ID得到流程类型名称
	 * @param ptid
	 *              流程名称
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getPtNameByPtId(String ptid){
		 StringBuffer sql = new StringBuffer("select pt.ptName")
		  .append(" from TwfInfoProcessType pt")
	      .append(" where pt.ptId = ?");
	    return getServiceDAO().find(sql.toString(),Long.valueOf(ptid));
	}
	
	public WorkflowDao getServiceDAO() {
	    return this.serviceDAO;
	  }
	
	
	/**
	 * @author:
	 * @time：6/28/2013 11:03 AM
	 * @desc：根据案卷id查找文件
	 * @param ToaArchiveTempfile objs 文件对象
	 * @param String folderId 案卷id
	 * @param String orderBy 排序
	 * @return List 文件列表
	 */
	public List<ToaArchiveTempfile> queryByCon2(ToaArchiveTempfile objs,String folderId,String orderBy,OALogInfo ... loginfos)throws SystemException,ServiceException{  
		try{
			String[] str = folderId.split(",");
			List<ToaArchiveTempfile> list=null;
			List<ToaArchiveTempfile> list1=null;
			List<ToaArchiveTempfile> tmpfileList=new ArrayList<ToaArchiveTempfile>();
			String hql="";
			for(String i:str){
				hql=sqlString()+" from ToaArchiveTempfile t where t.toaArchiveFolder.folderId='"+i+"'";		
				hql=hql+orderBy; 
				Query query=tempFileDao.createQuery(hql);		
					if(query!=null){
						list = query.list();
								if(list!=null&&list.size()>0){
									 list1=packagingList(list);
										for(ToaArchiveTempfile t:list1){
											tmpfileList.add(t);
										}
								}
						}
			}
			return tmpfileList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 文件分页列表"}); 
		}

	}

	
	public JSONArray getHistoryDataInfoAndFiles(String tfId)throws SystemException,ServiceException{  
		try{
			JSONArray array = new JSONArray();
			JSONObject jsonObj = new JSONObject();
			
			String flowsn = "219005701";
			
			//业务表字段信息
			String sql = "select * from gz_gt_old.dispatch_doc t where t.dispatchid='"+flowsn+"'";
			List datainfoList = jdbcTemplate.queryForList(sql);
			if(null!=datainfoList && datainfoList.size()>0){
				Map dataObj = (Map) datainfoList.get(0);
				array.add(JSONObject.fromObject(dataObj));
			}
			//处室核稿意见
			sql = "select * from gz_gt_old.CHECKHANDLE t where t.objectid='"+flowsn+"'";
			List ngdatainfoList = jdbcTemplate.queryForList(sql);
			JSONArray hgarray = new JSONArray();
			if(null!=ngdatainfoList && ngdatainfoList.size()>0){
				for(Object dataObj : ngdatainfoList){
					Map datamap = (Map) dataObj;
					hgarray.add(JSONObject.fromObject(datamap));
				}
				jsonObj.clear();
				jsonObj.put("CHECKHANDLE", hgarray);
				array.add(jsonObj);
			}
			
			//办公室拟稿意见
			sql = "select * from gz_gt_old.DISJHHANDLE t where t.objectid='"+flowsn+"'";
			List bgshgdatainfoList = jdbcTemplate.queryForList(sql);
			if(null!=bgshgdatainfoList && bgshgdatainfoList.size()>0){
				Map dataObj = (Map) bgshgdatainfoList.get(0);
				array.add(JSONObject.fromObject(dataObj));
			}
			//会文意见
			sql = "select * from gz_gt_old.ISSUED_HANDLE_DS t where t.dispatchid='"+flowsn+"'";
			List hwdatainfoList = jdbcTemplate.queryForList(sql);
			if(null!=hwdatainfoList && hwdatainfoList.size()>0){
				Map dataObj = (Map) hwdatainfoList.get(0);
				array.add(JSONObject.fromObject(dataObj));
			}
			//分管局领导拟稿意见
			sql = "select * from gz_gt_old.ISSUED_HANDLE t where t.dispatchid='"+flowsn+"'";
			List fgjlddatainfoList = jdbcTemplate.queryForList(sql);
			if(null!=fgjlddatainfoList && fgjlddatainfoList.size()>0){
				Map dataObj = (Map) fgjlddatainfoList.get(0);
				array.add(JSONObject.fromObject(dataObj));
			}
			
			
			//附件信息
			sql = "select * from gz_gt_old.attach t where t.flowsn='"+flowsn+"' and t.catalog='fwzwfj'";
			List fileinfoList = jdbcTemplate.queryForList(sql);
			JSONArray fjarray = new JSONArray();
			if(null!=fileinfoList && fileinfoList.size()>0){
				for(Object dataObj : fileinfoList){
					Map datamap = (Map) dataObj;
					fjarray.add(JSONObject.fromObject(datamap));
				}
				jsonObj.clear();
				jsonObj.put("attach", fjarray);
				array.add(jsonObj);
			}
			
			//流程信息
			sql = "select * from gz_gt_old.FLOWCTRL t where t.flowsn='"+flowsn+"'";
			List flowinfoList = jdbcTemplate.queryForList(sql);
			if(null!=flowinfoList && flowinfoList.size()>0){
				Map flowObj = (Map) flowinfoList.get(0);
				array.add(JSONObject.fromObject(flowObj));
			}
			return array;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {" 文件分页列表"}); 
		}
	}
	
	
	
}
