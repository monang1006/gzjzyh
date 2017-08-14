/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: zhangli
 * Version: V1.0
 * Description： 档案文件管理MANAGER
 */
package com.strongit.oa.archive.archivefile;

import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

import com.strongit.oa.bo.ToaArchiveFile;
import com.strongit.oa.bo.ToaArchiveFileAppend;
import com.strongit.oa.bo.ToaArchiveFolder;
import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
@OALogger
public class ArchiveFileManager {
	/** 档案文件管理Dao*/
	private GenericDAOHibernate<ToaArchiveFile, java.lang.String> archiveDao;

	/** 档案文件附件管理Manager*/
	private ArchiveFileAppendManager fileappendmanager;

	/**
	 * @roseuid 4958897D03D8
	 */
	public ArchiveFileManager() {

	}

	/**
	 * 注册档案文件附件管理Manager
	 * @param aFileappendmanager 档案文件附件管理Manager
	 */
	@Autowired
	public void setFileappendmanager(ArchiveFileAppendManager aFileappendmanager) {
		fileappendmanager = aFileappendmanager;
	}

	/**
	 * 注册DAO
	 * @param sessionFactory
	 * @roseuid 495880830119
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		archiveDao = new GenericDAOHibernate<ToaArchiveFile, java.lang.String>(
				sessionFactory, ToaArchiveFile.class);
	}

	public String sqlString(){
		String hql="select t.fileId,t.fileNo,t.fileAuthor,t.fileTitle,t.fileDate,t.filePage,t.fileDepartment,t.fileDepartmentName,t.fileDocType," +
				   " t.fileDocId,t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderName,t.filePieceNo,t.fileDesc ";
		return hql;
	}
	public String sqlString1(){
        String hql="select t.fileId,t.fileNo,t.fileAuthor,t.fileTitle,t.fileDate,t.fileDeadlineId,t.fileDeadline,t.filePage,t.fileDepartment,t.fileDepartmentName,t.fileDocType," +
                   " t.fileDocId,t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderName,t.filePieceNo,t.fileDesc ";
        return hql;
    }
	/**结果输出
     * @author xush
     * @date 6/14/2013 11:41 AM
     */
    public InputStream searchExcel( ToaArchiveFile file,String disLogo,String state,String fileFolder )throws DAOException{
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
        cell.setCellValue("保管期限");
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
        cell.setCellValue("卷（盒）题名");
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
        
       
//        sheet.setColumnWidth((short)m,(short)5000);
//        cell = row.createCell((short) m++);
//        cell.setCellValue("附件");
//        cellStyle= wb.createCellStyle();
//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
//        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
//        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//        font = wb.createFont();
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
//        cellStyle.setFont(font);
//        cell.setCellStyle(cellStyle);
        List<ToaArchiveFile> list = exportBysearch(file,fileFolder);//获取满足条件的ToaArchiveFile对象
        for (int i = 0; i < list.size()+0; ++i)
        {
            ToaArchiveFile folder = list.get(i);
            SimpleDateFormat st = new SimpleDateFormat(
            "yyyy-MM-dd");
            row = sheet.createRow(i + 1);

            int j=0;
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getFilePieceNo());//当前对象的件号
            cellStyle= wb.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getFileAuthor());
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getFileNo());
            cellStyle= wb.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
           
            
            
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getFileTitle());
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue(st.format(folder.getFileDate()));
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getFileDeadline());
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getToaArchiveFolder().getFolderName());
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            if(folder.getFilePage()!=null){
             cell.setCellValue(folder.getFilePage());}
            else{
                cell.setCellValue("");  
            }
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            
//            cell = row.createCell((short) j++);
//            cell.setCellValue("null");
//            cellStyle= wb.createCellStyle();
//            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
//            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
//            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
//            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//            cell.setCellStyle(cellStyle);
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
     * @author:xush
     * @time：6/14/2013 11:44 AM
     * @desc：根据查询条件导出列表数据
     * @param 
     * @param ToaArchiveTempfile objs 年内文件对象
     * @param String folderId 案卷id
     * @param String status 文件状态
     * @param String orderBy 排序
     * @return Page 分页列表
     */
    public List<ToaArchiveFile> exportBysearch(ToaArchiveFile model,String fileFolder) throws SystemException,ServiceException{ 
        List<Object> list=new ArrayList<Object>();
        List<ToaArchiveFile> fileList;
        try {
            StringBuffer hql=new StringBuffer(" from ToaArchiveFile t where 1=1 ");
            if(model.getToaArchiveFolder()!=null&&model.getToaArchiveFolder().getFolderId()!=null&&!"".equals(model.getToaArchiveFolder().getFolderId())){
                if("0".equals(model.getToaArchiveFolder().getFolderId())){
                    hql.append(" and t.toaArchiveFolder.folderId is null ");
                }else{
                hql.append(" and t.toaArchiveFolder.folderId=?");
                list.add(model.getToaArchiveFolder().getFolderId());
                }
            }
            /** 如果档案文件所属案卷过滤条件*/
            if(fileFolder!=null&&!"".equals(fileFolder)){
                String[] str = fileFolder.split(",");
                 if("0".equals(fileFolder)){
                        hql.append(" and t.toaArchiveFolder.folderId is null ");
                    }else{
                        if(str.length==1){
                            hql.append(" and t.toaArchiveFolder.folderId=?");
                            list.add(str[0]);
                        }else{
                        for (int i = 0; i < str.length; i++) {
                            if(i==0){
                                hql.append(" and (t.toaArchiveFolder.folderId=?");
                                list.add(str[0]);
                            }else if(i==str.length-1){
                                hql.append(" or t.toaArchiveFolder.folderId=?)");
                                list.add(str[str.length-1]);
                                }else{
                                    hql.append(" or t.toaArchiveFolder.folderId=?");
                                    list.add(str[i]);  
                                }
                                }
                            }
                    }
                }
            /** 如果档案文件所属部门，则设置档案文件部门数过滤条件*/
            if(model.getFileDepartment()!=null&&!"".equals(model.getFileDepartment())){
                if("0".equals(model.getFileDepartment())){
                    hql.append(" and t.fileDepartment is null");
                }else{
                hql.append(" and t.fileDepartment=?");
                list.add(model.getFileDepartment());
                }
            }
            /** 如果档案文件编码不为空，则设置档案文件编码过滤条件*/
            if (model.getFileNo() != null && !model.getFileNo().equals("")) {
                //String temp =model.getFileNo().replaceAll("%","/%");
                //hql.append(" and t.fileNo like ?");
                //list.add("%"+model.getFileNo()+"%");
                hql.append(addPercent(list, "t.fileNo",
                        model.getFileNo().trim())); 
            }
            /** 如果档案文件保管期限不为空，则设置档案文件保管期限过滤条件*/
            if (model.getFileDeadline() != null && !model.getFileDeadline().equals("")) {
                hql.append(" and t.fileDeadline like ?");
                list.add("%"+model.getFileDeadline()+"%");
            }
            /** 如果档案文件标题不为空，则设置档案文件标题过滤条件*/
            if (model.getFileTitle() != null
                    && !model.getFileTitle().equals("")) {
                //hql.append(" and t.fileTitle like ?");
                //list.add("%"+model.getFileTitle()+"%");
                hql.append(addPercent(list, "t.fileTitle",
                        model.getFileTitle().trim())); 
            }
            
            if(model.getFileYear()!=null&&!"".equals(model.getFileYear())){
                hql.append(" and t.fileYear=?");
                list.add(model.getFileYear());
            }
            if(model.getFileMonth()!=null&&!"".equals(model.getFileMonth())){
                hql.append(" and t.fileMonth=?");
                list.add(model.getFileMonth());
            }
            hql.append(" order by t.fileDate desc,t.toaArchiveFolder.folderId desc ");
            
                     
                 fileList=archiveDao.find(hql.toString(), list.toArray());
                
                return fileList;
        } catch(ServiceException e){
            throw new ServiceException(); 
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
	public Page<ToaArchiveFile> search(Page<ToaArchiveFile> page,ToaArchiveFile model,String fileFolder,OALogInfo ...infos){
		List<Object> list=new ArrayList<Object>();
		StringBuffer hql=new StringBuffer(sqlString1()+" from ToaArchiveFile t where 1=1 ");
		if(fileFolder!=null&&!"".equals(fileFolder)){
		String[] str = fileFolder.split(",");
		 if("0".equals(fileFolder)){
				hql.append(" and t.toaArchiveFolder.folderId is null ");
			}else{
			    if(str.length==1){
			        hql.append(" and t.toaArchiveFolder.folderId=?");
		            list.add(str[0]);
			    }else{
			    for (int i = 0; i < str.length; i++) {
			        if(i==0){
			            hql.append(" and (t.toaArchiveFolder.folderId=?");
	                    list.add(str[0]);
			        }else if(i==str.length-1){
			            hql.append(" or t.toaArchiveFolder.folderId=?)");
                        list.add(str[str.length-1]);
			            }else{
			                hql.append(" or t.toaArchiveFolder.folderId=?");
	                        list.add(str[i]);  
			            }
			            }
			        }
			}
		}
		/** 如果档案文件编码不为空，则设置档案文件编码过滤条件*/
		if (model.getFileNo() != null && !model.getFileNo().equals("")) {
		    //String temp =model.getFileNo().replaceAll("%","/%");
			//hql.append(" and t.fileNo like ?");
			//list.add("%"+model.getFileNo()+"%");
		    hql.append(addPercent(list, "t.fileNo",
		            model.getFileNo().trim())); 
		}
		/** 如果档案文件保管期限不为空，则设置档案文件保管期限过滤条件*/
        if (model.getFileDeadline() != null && !model.getFileDeadline().equals("")) {
            hql.append(" and t.fileDeadline like ?");
            list.add("%"+model.getFileDeadline()+"%");
        }
		/** 如果档案文件标题不为空，则设置档案文件标题过滤条件*/
		if (model.getFileTitle() != null
				&& !model.getFileTitle().equals("")) {
			//hql.append(" and t.fileTitle like ?");
			//list.add("%"+model.getFileTitle()+"%");
		    hql.append(addPercent(list, "t.fileTitle",
		            model.getFileTitle().trim())); 
		}
		/** 如果档案文件创建日期不为空，则设置档案文件创建日期过滤条件*/
		if (model.getFileDate() != null && !model.getFileDate().equals("")) {
			hql.append(" and t.fileDate>=?");
			list.add(model.getFileDate());
		}
		/** 如果档案文件作者不为空，则设置档案文件作者过滤条件*/
		if (model.getFileAuthor() != null
				&& !model.getFileAuthor().equals("")) {
			hql.append(" and t.fileAuthor like ?");
			list.add("%"+model.getFileAuthor()+"%");
		}
		/** 如果档案文件页数不为空，则设置档案文件页数过滤条件*/
		if (model.getFilePage() != null && !model.getFilePage().equals("")) {
			hql.append(" and t.filePage=?");
			list.add(model.getFilePage());
		}
		/** 如果档案文件所属部门，则设置档案文件部门数过滤条件*/
		if(model.getFileDepartment()!=null&&!"".equals(model.getFileDepartment())){
			if("0".equals(model.getFileDepartment())){
				hql.append(" and t.fileDepartment is null");
			}else{
			hql.append(" and t.fileDepartment=?");
			list.add(model.getFileDepartment());
			}
		}
		if(model.getFileDocType()!=null&&!"".equals(model.getFileDocType())){
			if("0".equals(model.getFileDocType())){
				hql.append(" and t.fileDocType is null ");
			}else{
			hql.append(" and t.fileDocType=?");
			list.add(model.getFileDocType());
			}
		}
		if(model.getFileYear()!=null&&!"".equals(model.getFileYear())){
			hql.append(" and t.fileYear=?");
			list.add(model.getFileYear());
		}
		if(model.getFileMonth()!=null&&!"".equals(model.getFileMonth())){
			hql.append(" and t.fileMonth=?");
			list.add(model.getFileMonth());
		}
		hql.append(" order by t.fileDate desc,t.toaArchiveFolder.folderId desc ");
		
		if(list.size()>0){
			Object[] obj=new Object[list.size()];
			for(int i=0;i<list.size();i++){
				obj[i]=list.get(i);
			}
			page=archiveDao.find(page, hql.toString(), obj);
			List filelist=page.getResult();
			if(filelist!=null){
				page.setResult(packagingList1(filelist));
			}
			return page;
		}else{
			page=archiveDao.find(page, hql.toString());
			List filelist=page.getResult();
			if(filelist!=null){
				page.setResult(packagingList1(filelist));
			}
			return page;
		}
	}
	/**
	 * 根据条件获取某案卷下所有档案文件列表
	 * 
	 * @param folderId
	 *            案卷编号
	 * @param model
	 *            档案文件对象
	 * @return java.util.List 档案文件列表
	 * @roseuid 495880D70261
	 */
	public List getAllFile(String folderId, ToaArchiveFile model,OALogInfo ... loginfos) {
		List<Object> list=new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(
				sqlString()+" from ToaArchiveFile t where t.toaArchiveFolder.folderId=?");
		list.add(folderId);
		SimpleDateFormat st=new SimpleDateFormat("yyyy-MM-dd");
		if (model != null) {
			/** 如果档案文件编码不为空，则设置档案文件编码过滤条件*/
			if (model.getFileNo() != null && !model.getFileNo().equals("")) {
				hql.append(" and t.fileNo like ?");
				list.add( "%" + model.getFileNo() + "%");
			}
			/** 如果档案文件标题不为空，则设置档案文件标题过滤条件*/
			if (model.getFileTitle() != null
					&& !model.getFileTitle().equals("")) {
				hql.append(" and t.fileTitle like ?");
				list.add("%" + model.getFileTitle() + "%");
			}
			/** 如果档案文件创建日期不为空，则设置档案文件创建日期过滤条件*/
			if (model.getFileDate() != null && !model.getFileDate().equals("")) {
				hql.append(" and to_char(t.fileDate,'yyyy-MM-dd') = ?");
				list.add(st.format(model.getFileDate()));
			}
			/** 如果档案文件作者不为空，则设置档案文件作者过滤条件*/
			if (model.getFileAuthor() != null
					&& !model.getFileAuthor().equals("")) {
				hql.append(" and t.fileAuthor like ?");
				list.add("%" + model.getFileAuthor() + "%");
			}
			/** 如果档案文件页数不为空，则设置档案文件页数过滤条件*/
			if (model.getFilePage() != null && !model.getFilePage().equals("")) {
				hql.append(" and t.filePage=?");
				list.add( model.getFilePage());
			}
			/** 如果档案文件备注不为空，则设置档案文件备注过滤条件*/
			if (model.getFileDesc() != null && !model.getFileDesc().equals("")) {
				hql.append(" and t.fileDesc like ?");
				list.add("%" +  model.getFileDesc() + "%");
			}
		}
		 
		hql.append(" order by fileDate desc,fileDepartment desc");
		 Object[] obj = new Object[list.size()];
         for(int i=0;i<list.size();i++){
         	obj[i]=list.get(i);
         }
			List filelist= archiveDao.find(hql.toString(), obj);
			if(filelist!=null){
				filelist=packagingList(filelist);
			}
			return filelist;
	}

	/**
	 * 根据条件获取某案卷下所有档案文件分页列表
	 * 
	 * @param page
	 *            分页对象
	 * @param folderId
	 *            案卷编号
	 * @param model
	 *            档案文件对象
	 * @return com.strongmvc.orm.hibernate.Page 分页对象
	 * @roseuid 495880E60109 
	 */
	public Page<ToaArchiveFile> getAllFile(Page<ToaArchiveFile> page,
			String folderId, ToaArchiveFile model,OALogInfo ... loginfos) {
		String hql=sqlString()+" from ToaArchiveFile t where t.toaArchiveFolder.folderId=?";
		Object[] obj=new Object[1];
		obj[0]=folderId;
		hql=hql+"  order by fileDate desc,fileDepartment desc";
		page= archiveDao.find(page,hql,obj);
		List list=page.getResult();
		if(list!=null){
			page.setResult(packagingList(list));
		}
		return page;
	}

	/**
	 * 获取档案文件对象
	 * 
	 * @param fileId
	 *            档案文件编号
	 * @return com.strongit.oa.bo.ToaArchiveFile 档案文件对象
	 * @roseuid 495881740119
	 */
	public ToaArchiveFile getFile(String fileId,OALogInfo ... loginfos) {
		return archiveDao.get(fileId);
	}

	/**
	 * 获取对应档案文件下的第一个档案文件附件对象
	 * 
	 * @param fileId
	 *            档案文件编号
	 * @return com.strongit.oa.bo.ToaArchiveFile 档案文件附件对象
	 * @roseuid 495881740119
	 */
	public ToaArchiveFileAppend getFileAppendByFile(String fileId,OALogInfo ... loginfos) {
		return fileappendmanager.getFileAppendByFile(fileId);
	}

	/**
	 * 根据附件ID获取附件
	 * @author 胡丽丽
	 * @date 2009-12-25
	 * @param fileId
	 * @return
	 */
	public ToaArchiveFileAppend getFileAppendById(String id) {
		return fileappendmanager.getFileAppend(id);
	}

	/**
	 * 保存档案文件对象
	 * 
	 * @param model
	 *            档案文件对象
	 * @return java.lang.String 保存结果
	 * @roseuid 4958817E0213
	 */
	public String saveFile(ToaArchiveFile model,OALogInfo ... loginfos) {
		String message = null;
		try {
			archiveDao.save(model);
			message = "保存文件成功！";
		} catch (Exception e) {
			message = "保存文件失败！";
		}
		return message;
	}

	/**
	 * 批量删除档案文件
	 * 
	 * @param fileId
	 *            档案文件编号
	 * @return java.lang.String 删除结果
	 * @roseuid 4958818401C5
	 */
	public String delFile(String fileId,OALogInfo ... loginfos) {
		String message = null;
		try {
			String[] str = fileId.split(",");
			for (String a : str) {
				archiveDao.delete(a);
			}
			message = "删除文件成功！";
		} catch (Exception e) {
			message = "删除文件失败！";
		}
		return message;
	}

	/**
	 * 保存档案文件附件
	 * 
	 * @param fileappend 档案文件附件对象
	 * @return java.lang.String 保存结果
	 * @roseuid 4958892402FD
	 */
	public String saveFileAppend(ToaArchiveFileAppend fileappend,OALogInfo ... loginfos) {
		return fileappendmanager.saveFileAppend(fileappend);
	}

	/**
	 * @author：zhangli
	 * @time：2009-1-7下午16:57:11
	 * @desc: 文档流直接写入HttpServletResponse请求
	 * @param response
	 *            HttpServletResponse请求
	 * @param String
	 *            fileId 档案文件id
	 * @return void
	 */
	public void setContentToHttpResponse(HttpServletResponse response,
			String appendId) {
		try {
			fileappendmanager.setContentToHttpResponse(response, appendId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据分组类型获取类型为空的文件个数
	 * @param type
	 * @return
	 */
	public int getFileCountByType(String type,ToaArchiveTempfile temp){
		
		List<Object> list=new ArrayList<Object>();
		String hql="select count(t.fileId) from ToaArchiveFile t where  1=1 ";
		if("1".equals(type)){
			hql=hql+"  and t.fileDocType is null";
		}else if("2".equals(type)){
			hql=hql+" and t.toaArchiveFolder.folderId is null";
		}
		//文件编号
		if(temp.getTempfileNo()!=null&&!"".equals(temp.getTempfileNo())&&!"null".equals(temp.getTempfileNo())){
			hql+= " and t.fileNo like ?"; 
			list.add("%"+temp.getTempfileNo()+"%");
		}
		//文件名称
		if(temp.getTempfileTitle()!=null&&!"".equals(temp.getTempfileTitle())&&!"null".equals(temp.getTempfileTitle())){
			hql+= " and t.fileTitle like ?"; 
			list.add("%"+temp.getTempfileTitle()+"%");
		}
		//文件创建日期
		if(temp.getTempfileDate()!=null&&!"".equals(temp.getTempfileDate())&&!"null".equals(temp.getTempfileDate())){
			hql+=" and t.fileDate >=?";
			list.add(temp.getTempfileDate());
		}
		//文件作者
		if(temp.getTempfileAuthor()!=null&&!"".equals(temp.getTempfileAuthor())&&!"null".equals(temp.getTempfileAuthor())){
			hql+= " and t.fileAuthor like ?";
			list.add("%"+temp.getTempfileAuthor()+"%");
		}
		//文件页码
		if(temp.getTempfilePage()!=null&&!"".equals(temp.getTempfilePage())&&!"null".equals(temp.getTempfilePage())){
			hql+= " and t.filePage = ?";
			list.add(temp.getTempfilePage());	
		} 
		//文件类型
		if(temp.getTempfileDocType()!=null&&!"".equals(temp.getTempfileDocType())&&!"null".equals(temp.getTempfileDocType())){
			if("0".equals(temp.getTempfileDocType())){
				hql=hql+" and t.fileDocType is null ";
			}else{
			hql=hql+" and t.fileDocType=? ";
			list.add(temp.getTempfileDocType());	
			}
		}
		//所属部门
		if(temp.getTempfileDepartment()!=null&&!"".equals(temp.getTempfileDepartment())){
			if("0".equals(temp.getTempfileDepartment())){
				hql=hql+" and t.fileDepartment is null";
			}else{
			hql=hql+" and t.fileDepartment=? ";
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
			hql=hql+" and t.fileYear=?";
			list.add(temp.getTempfileYear());
		}
		//月份
		if(temp.getTempfileMonth()!=null&&!"".equals(temp.getTempfileMonth())){
			hql=hql+" and t.fileMonth=? ";
			list.add(temp.getTempfileMonth());
		}
		Object obj=null;
		if(list.size()>0){
		Object[] param=new Object[list.size()];
		for(int k=0;k<list.size();k++){
			param[k]=list.get(k);
		}
		 obj=archiveDao.findUnique(hql,param);
		}else{
		 obj=archiveDao.findUnique(hql);
		}
		if(obj!=null){
			return Integer.parseInt(obj.toString());
		}else{
			return 0;
		}
	}
	
	/**
	 * 根据文件案卷分组文件
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<Object> getFileFolder(ToaArchiveTempfile temp)throws SystemException, ServiceException  {
		try {
			
			List<Object> list=new ArrayList<Object>();
			String hql ="select t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderName from ToaArchiveFile t where  t.toaArchiveFolder.folderAuditing in ('1','4','5') ";
			
			//文件编号
			if(temp.getTempfileNo()!=null&&!"".equals(temp.getTempfileNo())&&!"null".equals(temp.getTempfileNo())){
				hql+= " and t.fileNo like ?"; 
				list.add("%"+temp.getTempfileNo()+"%");
			}
			//文件名称
			if(temp.getTempfileTitle()!=null&&!"".equals(temp.getTempfileTitle())&&!"null".equals(temp.getTempfileTitle())){
				hql+= " and t.fileTitle like ?"; 
				list.add("%"+temp.getTempfileTitle()+"%");
			}
			//文件创建日期
			if(temp.getTempfileDate()!=null&&!"".equals(temp.getTempfileDate())&&!"null".equals(temp.getTempfileDate())){
				hql+=" and t.fileDate >=?";
				list.add(temp.getTempfileDate());
			}
			//文件作者
			if(temp.getTempfileAuthor()!=null&&!"".equals(temp.getTempfileAuthor())&&!"null".equals(temp.getTempfileAuthor())){
				hql+= " and t.fileAuthor like ?";
				list.add("%"+temp.getTempfileAuthor()+"%");
			}
			//文件页码
			if(temp.getTempfilePage()!=null&&!"".equals(temp.getTempfilePage())&&!"null".equals(temp.getTempfilePage())){
				hql+= " and t.filePage = ?";
				list.add(temp.getTempfilePage());	
			} 
			//文件类型
			if(temp.getTempfileDocType()!=null&&!"".equals(temp.getTempfileDocType())&&!"null".equals(temp.getTempfileDocType())){
				if("0".equals(temp.getTempfileDocType())){
					hql=hql+" and t.fileDocType is null ";
				}else{
				hql=hql+" and t.fileDocType=? ";
				list.add(temp.getTempfileDocType());	
				}
			}
			//所属部门
			if(temp.getTempfileDepartment()!=null&&!"".equals(temp.getTempfileDepartment())){
				if("0".equals(temp.getTempfileDepartment())){
					hql=hql+" and t.fileDepartment is null";
				}else{
				hql=hql+" and t.fileDepartment=? ";
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
				hql=hql+" and t.fileYear=?";
				list.add(temp.getTempfileYear());
			}
			//月份
			if(temp.getTempfileMonth()!=null&&!"".equals(temp.getTempfileMonth())){
				hql=hql+" and t.fileMonth=? ";
				list.add(temp.getTempfileMonth());
			}
			hql=hql+"   group by t.toaArchiveFolder.folderId,t.toaArchiveFolder.folderName"; 
			Query query=null;
			if(list.size()>0){
			Object[] param=new Object[list.size()];
			for(int k=0;k<list.size();k++){
				param[k]=list.get(k);
			}
			 query=archiveDao.createQuery(hql,param);
			}else{
				query=archiveDao.createQuery(hql);
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
	 * 根据文件类型分组文件
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<String> getFileType(ToaArchiveTempfile temp)throws SystemException, ServiceException  {
		try {
			List<Object> list=new ArrayList<Object>();
			String hql ="select t.fileDocType from ToaArchiveFile t where  1=1 ";
			//文件编号
			if(temp.getTempfileNo()!=null&&!"".equals(temp.getTempfileNo())&&!"null".equals(temp.getTempfileNo())){
				hql+= " and t.fileNo like ?"; 
				list.add("%"+temp.getTempfileNo()+"%");
			}
			//文件名称
			if(temp.getTempfileTitle()!=null&&!"".equals(temp.getTempfileTitle())&&!"null".equals(temp.getTempfileTitle())){
				hql+= " and t.fileTitle like ?"; 
				list.add("%"+temp.getTempfileTitle()+"%");
			}
			//文件创建日期
			if(temp.getTempfileDate()!=null&&!"".equals(temp.getTempfileDate())&&!"null".equals(temp.getTempfileDate())){
				hql+=" and t.fileDate >=?";
				list.add(temp.getTempfileDate());
			}
			//文件作者
			if(temp.getTempfileAuthor()!=null&&!"".equals(temp.getTempfileAuthor())&&!"null".equals(temp.getTempfileAuthor())){
				hql+= " and t.fileAuthor like ?";
				list.add("%"+temp.getTempfileAuthor()+"%");
			}
			//文件页码
			if(temp.getTempfilePage()!=null&&!"".equals(temp.getTempfilePage())&&!"null".equals(temp.getTempfilePage())){
				hql+= " and t.filePage = ?";
				list.add(temp.getTempfilePage());	
			} 
			//文件类型
			if(temp.getTempfileDocType()!=null&&!"".equals(temp.getTempfileDocType())&&!"null".equals(temp.getTempfileDocType())){
				if("0".equals(temp.getTempfileDocType())){
					hql=hql+" and t.fileDocType is null ";
				}else{
				hql=hql+" and t.fileDocType=? ";
				list.add(temp.getTempfileDocType());	
				}
			}
			//所属部门
			if(temp.getTempfileDepartment()!=null&&!"".equals(temp.getTempfileDepartment())){
				if("0".equals(temp.getTempfileDepartment())){
					hql=hql+" and t.fileDepartment is null";
				}else{
				hql=hql+" and t.fileDepartment=? ";
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
				hql=hql+" and t.fileYear=?";
				list.add(temp.getTempfileYear());
			}
			//月份
			if(temp.getTempfileMonth()!=null&&!"".equals(temp.getTempfileMonth())){
				hql=hql+" and t.fileMonth=? ";
				list.add(temp.getTempfileMonth());
			}
			hql=hql+" group by t.fileDocType"; 
			Query query=null;
			if(list.size()>0){
			Object[] param=new Object[list.size()];
			for(int k=0;k<list.size();k++){
				param[k]=list.get(k);
			}
			 query=archiveDao.createQuery(hql,param);
			}else{
				query=archiveDao.createQuery(hql);
			}
			List<String> typelist=query.list();
			return typelist;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据文件类型统计" });
		} 
	}
	
	/**
	 * 根据月份分组查询文件
	 * 
	 * @param year
	 * @return
	 */
	public List<String> getFileMonth(String year,ToaArchiveTempfile temp) throws SystemException, ServiceException {
		try {
			
			List<Object> list=new ArrayList<Object>();
			String hql = "select t.fileMonth from ToaArchiveFile t where t.fileYear='"+year+"'  ";
			//文件编号
			if(temp.getTempfileNo()!=null&&!"".equals(temp.getTempfileNo())&&!"null".equals(temp.getTempfileNo())){
				hql+= " and t.fileNo like ?"; 
				list.add("%"+temp.getTempfileNo()+"%");
			}
			//文件名称
			if(temp.getTempfileTitle()!=null&&!"".equals(temp.getTempfileTitle())&&!"null".equals(temp.getTempfileTitle())){
				hql+= " and t.fileTitle like ?"; 
				list.add("%"+temp.getTempfileTitle()+"%");
			}
			//文件创建日期
			if(temp.getTempfileDate()!=null&&!"".equals(temp.getTempfileDate())&&!"null".equals(temp.getTempfileDate())){
				hql+=" and t.fileDate >=?";
				list.add(temp.getTempfileDate());
			}
			//文件作者
			if(temp.getTempfileAuthor()!=null&&!"".equals(temp.getTempfileAuthor())&&!"null".equals(temp.getTempfileAuthor())){
				hql+= " and t.fileAuthor like ?";
				list.add("%"+temp.getTempfileAuthor()+"%");
			}
			//文件页码
			if(temp.getTempfilePage()!=null&&!"".equals(temp.getTempfilePage())&&!"null".equals(temp.getTempfilePage())){
				hql+= " and t.filePage = ?";
				list.add(temp.getTempfilePage());	
			} 
			//文件类型
			if(temp.getTempfileDocType()!=null&&!"".equals(temp.getTempfileDocType())&&!"null".equals(temp.getTempfileDocType())){
				if("0".equals(temp.getTempfileDocType())){
					hql=hql+" and t.fileDocType is null ";
				}else{
				hql=hql+" and t.fileDocType=? ";
				list.add(temp.getTempfileDocType());	
				}
			}
			//所属部门
			if(temp.getTempfileDepartment()!=null&&!"".equals(temp.getTempfileDepartment())){
				if("0".equals(temp.getTempfileDepartment())){
					hql=hql+" and t.fileDepartment is null";
				}else{
				hql=hql+" and t.fileDepartment=? ";
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
			hql = hql + " group by t.fileMonth";
			Query query=null;
			if(list.size()>0){
			Object[] param=new Object[list.size()];
			for(int k=0;k<list.size();k++){
				param[k]=list.get(k);
			}
			 query=archiveDao.createQuery(hql,param);
			}else{
				query=archiveDao.createQuery(hql);
			}
			List<String> monthlist = query.list();
			return monthlist;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.operation_error,
					new Object[] { "根据月份统计" });
		}
	}
	
	/**
	 * 根据年份分组查询文件
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<String> getFileYear(ToaArchiveTempfile temp)
	throws SystemException, ServiceException {
		try {
			List<Object> list=new ArrayList<Object>();
			String hql = "select t.fileYear from ToaArchiveFile t where  1=1  ";
			//文件编号
			if(temp.getTempfileNo()!=null&&!"".equals(temp.getTempfileNo())&&!"null".equals(temp.getTempfileNo())){
				hql+= " and t.fileNo like ?"; 
				list.add("%"+temp.getTempfileNo()+"%");
			}
			//文件名称
			if(temp.getTempfileTitle()!=null&&!"".equals(temp.getTempfileTitle())&&!"null".equals(temp.getTempfileTitle())){
				hql+= " and t.fileTitle like ?"; 
				list.add("%"+temp.getTempfileTitle()+"%");
			}
			//文件创建日期
			if(temp.getTempfileDate()!=null&&!"".equals(temp.getTempfileDate())&&!"null".equals(temp.getTempfileDate())){
				hql+=" and t.fileDate >=?";
				list.add(temp.getTempfileDate());
			}
			//文件作者
			if(temp.getTempfileAuthor()!=null&&!"".equals(temp.getTempfileAuthor())&&!"null".equals(temp.getTempfileAuthor())){
				hql+= " and t.fileAuthor like ?";
				list.add("%"+temp.getTempfileAuthor()+"%");
			}
			//文件页码
			if(temp.getTempfilePage()!=null&&!"".equals(temp.getTempfilePage())&&!"null".equals(temp.getTempfilePage())){
				hql+= " and t.filePage = ?";
				list.add(temp.getTempfilePage());	
			} 
			//文件类型
			if(temp.getTempfileDocType()!=null&&!"".equals(temp.getTempfileDocType())&&!"null".equals(temp.getTempfileDocType())){
				if("0".equals(temp.getTempfileDocType())){
					hql=hql+" and t.fileDocType is null ";
				}else{
				hql=hql+" and t.fileDocType=? ";
				list.add(temp.getTempfileDocType());	
				}
			}
			//所属部门
			if(temp.getTempfileDepartment()!=null&&!"".equals(temp.getTempfileDepartment())){
				if("0".equals(temp.getTempfileDepartment())){
					hql=hql+" and t.fileDepartment is null";
				}else{
				hql=hql+" and t.fileDepartment=? ";
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
				hql=hql+" and t.fileYear=?";
				list.add(temp.getTempfileYear());
			}
			//月份
			if(temp.getTempfileMonth()!=null&&!"".equals(temp.getTempfileMonth())){
				hql=hql+" and t.fileMonth=? ";
				list.add(temp.getTempfileMonth());
			}
			hql = hql + " group by t.fileYear";
			Query query=null;
			if(list.size()>0){
			Object[] param=new Object[list.size()];
			for(int k=0;k<list.size();k++){
				param[k]=list.get(k);
			}
			 query=archiveDao.createQuery(hql,param);
			}else{
				query=archiveDao.createQuery(hql);
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
     * 组装文件对象--对后来档案搜索时的加保管期限的需求
     * @author xush
     * @date 6/21/2013 9:02 AM
     * @param list
     * @return
     */
    public List<ToaArchiveFile> packagingList1(List list){
        List<ToaArchiveFile> listfile=new ArrayList<ToaArchiveFile>();
        for (Object temp : list) {// 循环所有档案文件
            Object[] obj=(Object[])temp;
            ToaArchiveFile file=new ToaArchiveFile();
            file.setFileId(obj[0].toString());
            if(obj[1]!=null){
                file.setFileNo(obj[1].toString());
            }
            if(obj[2]!=null){
                file.setFileAuthor(obj[2].toString());
            }
            if(obj[3]!=null){
                file.setFileTitle(obj[3].toString());
            }
            if(obj[4]!=null){
                file.setFileDate((Date)obj[4]);
            }
            if(obj[5]!=null){
                file.setFileDeadlineId(obj[5].toString());
            }
            if(obj[6]!=null){
                file.setFileDeadline(obj[6].toString());
            }
            if(obj[7]!=null){
                file.setFilePage(Long.parseLong(obj[7].toString()));
            }
            if(obj[8]!=null){
                file.setFileDepartment(obj[8].toString());
            }
            if(obj[9]!=null){
                file.setFileDepartmentName(obj[9].toString());
            }
            if(obj[10]!=null){
                file.setFileDocType(obj[10].toString());
            }
            if(obj[11]!=null){
                file.setFileDocId(obj[11].toString());
            }
            ToaArchiveFolder folder=new ToaArchiveFolder();//案卷对象
            if(obj[12]!=null){
                folder.setFolderId(obj[12].toString());
            }
            if(obj[13]!=null){
                folder.setFolderName(obj[13].toString());
            }
            if(obj[14]!=null){
                file.setFilePieceNo(obj[14].toString());
            }
            if(obj[15]!=null){
                file.setFileDesc(obj[15].toString());
            }
            file.setToaArchiveFolder(folder);
            listfile.add(file);
        }
        return listfile;
    }

    /**
     * 组装文件对象
     * @author hull
     * @date 2010-01-21
     * @param list
     * @return
     */
    public List<ToaArchiveFile> packagingList(List list){
        List<ToaArchiveFile> listfile=new ArrayList<ToaArchiveFile>();
        for (Object temp : list) {// 循环所有档案文件
            Object[] obj=(Object[])temp;
            ToaArchiveFile file=new ToaArchiveFile();
            file.setFileId(obj[0].toString());
            if(obj[1]!=null){
                file.setFileNo(obj[1].toString());
            }
            if(obj[2]!=null){
                file.setFileAuthor(obj[2].toString());
            }
            if(obj[3]!=null){
                file.setFileTitle(obj[3].toString());
            }
            if(obj[4]!=null){
                file.setFileDate((Date)obj[4]);
            }
            if(obj[5]!=null){
                file.setFilePage(Long.parseLong(obj[5].toString()));
            }
            if(obj[6]!=null){
                file.setFileDepartment(obj[6].toString());
            }
            if(obj[7]!=null){
                file.setFileDepartmentName(obj[7].toString());
            }
            if(obj[8]!=null){
                file.setFileDocType(obj[8].toString());
            }
            if(obj[9]!=null){
                file.setFileDocId(obj[9].toString());
            }
            ToaArchiveFolder folder=new ToaArchiveFolder();//案卷对象
            if(obj[10]!=null){
                folder.setFolderId(obj[10].toString());
            }
            if(obj[11]!=null){
                folder.setFolderName(obj[11].toString());
            }
            if(obj[12]!=null){
                file.setFilePieceNo(obj[12].toString());
            }
            if(obj[13]!=null){
                file.setFileDesc(obj[13].toString());
            }
            file.setToaArchiveFolder(folder);
            listfile.add(file);
        }
        return listfile;
    }
}
