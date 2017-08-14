/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-23
 * Autour: zhangli
 * Version: V1.0
 * Description： 案卷管理MANAGER
 */
package com.strongit.oa.archive.archivefolder;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.archive.archivefile.ArchiveFileManager;
import com.strongit.oa.archive.tempfile.AnnexManager;
import com.strongit.oa.archive.tempfile.TempFileManager;
import com.strongit.oa.bo.ToaArchiveFile;
import com.strongit.oa.bo.ToaArchiveFileAppend;
import com.strongit.oa.bo.ToaArchiveFolder;
import com.strongit.oa.bo.ToaArchiveSort;
import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.bo.ToaArchiveTfileAppend;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.search.SearchManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.TempPo;
import com.strongit.oa.util.UUIDGenerator;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class ArchiveFolderManager {
	/** 档案案卷Dao*/
	private GenericDAOHibernate<ToaArchiveFolder, java.lang.String> archiveDao;
	/** 档案文件所属附件DAO*/
	private GenericDAOHibernate<ToaArchiveFileAppend ,java.lang.String> archiveAppendDao;
	private GenericDAOHibernate<ToaArchiveTempfile, java.lang.String> tempFileDao;


	/** 未归档*/
	private static final String NO = "0";

	/** 已归档*/
	private static final String YES = "1";

	/** 审核中*/
	private static final String AUDITING = "2";

	/** 驳回*/
	private static final String BACK = "3";
	
	/** 销毁审核*/
	private static final String DESTR_AUDIT = "4";

	/** 用户管理Service接口*/
	private IUserService userservice;

	/** 年内文件Manager*/
	private TempFileManager fileManager;
	
	private SearchManager searchManager;//全文检索
	
	@Autowired protected IUserService userService;//统一用户服务
	
	@Autowired
	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}
   

	/** 档案文件Manager*/
	private ArchiveFileManager archivefilemanager;
	
	@Autowired AnnexManager annexManager;
	/**
	 * author:zhangli
	 * description:注册档案文件Manager
	 * @param archivefilemanager 档案文件Manager
	 */
	@Autowired
	public void setArchivefilemanager(ArchiveFileManager archivefilemanager) {
		this.archivefilemanager = archivefilemanager;
	}

	/**
	 * @roseuid 49506F4A0167
	 */
	public ArchiveFolderManager() {

	}

	/**
	 * 注册DAO
	 * @param sessionFactory
	 * @roseuid 494F59020177
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		archiveDao = new GenericDAOHibernate<ToaArchiveFolder, java.lang.String>(
				sessionFactory, ToaArchiveFolder.class);
		archiveAppendDao = new GenericDAOHibernate<ToaArchiveFileAppend, java.lang.String>(
				sessionFactory, ToaArchiveFileAppend.class);
		tempFileDao = new GenericDAOHibernate<ToaArchiveTempfile, java.lang.String>(sessionFactory,
				ToaArchiveTempfile.class);
	}
	


	/**
	 * 获取所有案卷
	 * @author xush
	 * @return java.util.List 案卷列表
	 * @roseuid 494F59360157
	 */
	public List getAllFolder() throws SystemException,ServiceException{
		try{
			return archiveDao.findAll();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"案卷列表"});
		}
	}
	/**
     * 把去年的案卷重新插入数据库一份
     * 
     * @return String
     * @roseuid 494F59360157
     * date 6/28/2013 1:40 PM
     */
	public String insertFolder(List<ToaArchiveSort> list,String msg) throws Exception{
	    SimpleDateFormat t=new SimpleDateFormat("yyyy");
	    if(list!=null&&list.size()>0){
	        //遍历类目所得对象
	        for(ToaArchiveSort sort:list){
	            //通过类目id获取其下的案卷对象
	           List<ToaArchiveFolder> folderList=getFolderBySortId(sort.getSortId());
	           //遍历所得的案卷对象
	           if(folderList!=null&&folderList.size()>0){
	               for(ToaArchiveFolder fo:folderList){
	                   //若有年度为今年的则为已导入过的，返回“2”
	                   if(!"".equals(fo.getFolderYear())&&fo.getFolderYear()!=null){
	                   if(fo.getFolderYear().equals(t.format(new Date()))){
	                       return "2";
	                   }
	                   }
	                   }
	               for(ToaArchiveFolder f:folderList){
	                 //若有年度为去年的才执行插入
	                   if(Integer.parseInt(f.getFolderYear())==Integer.parseInt(t.format(new Date()))-1){
	                   ToaArchiveFolder folder=new  ToaArchiveFolder();
	                   folder.setFolderArchiveNo(f.getFolderArchiveNo());
	                   folder.setFolderArrayNo(f.getFolderArrayNo());
	                   folder.setFolderAuditing("0");
	                   folder.setFolderBoxNo(f.getFolderBoxNo());
	                   folder.setFolderCreaterId(f.getFolderCreaterId());
	                   folder.setFolderCreaterName(f.getFolderCreaterName());
	                   folder.setFolderDate(new Date());
	                   folder.setFolderDepartment(f.getFolderDepartment());
	                   folder.setFolderDesc(f.getFolderDesc());
	                   folder.setFolderDepartmentName(f.getFolderDepartmentName());
	                   folder.setFolderDictNo(f.getFolderDictNo());
	                  folder.setFolderFormName(f.getFolderFormName());
	                  folder.setFolderFromDate(f.getFolderFromDate());
	                  folder.setFolderLimitName(f.getFolderLimitName());
	                  folder.setFolderLimitId(f.getFolderLimitId());
	                  folder.setFolderName(f.getFolderName());
	                  folder.setFolderNo(f.getFolderNo());
	                  folder.setFolderOrgcode(f.getFolderOrgcode());
	                  folder.setFolderOrgId(f.getFolderOrgId());
	                  folder.setFolderOrgName(f.getFolderOrgName());
	                  folder.setFolderPage(f.getFolderPage());
	                  folder.setFolderToDate(f.getFolderToDate());
	                  
	                  folder.setFolderYear(t.format(new Date()));
	                  folder.setOrgId(f.getOrgId());
	                  folder.setToaArchiveSort(sort);
	                  //保存
	                  archiveDao.save(folder);}
	               }
	           }
	        }
	    }
	    return "1";
	}
	/**
     * 通过类目id获取案卷
     * @author xush
     * @return List<ToaArchiveFolder>
     * @roseuid 494F59360157
     */
 public List<ToaArchiveFolder> getFolderBySortId(String sortId){
     try {
        List<Object> list = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("from ToaArchiveFolder t where 1=1");
        //类目id为过滤条件
        if (sortId != null) {
           hql.append(" and t.toaArchiveSort.sortId = ?");
            list.add(sortId);
        }
        List<ToaArchiveFolder> folderList = new ArrayList<ToaArchiveFolder>();
        //通过hql得到folderList
        folderList = archiveDao.find(hql.toString(), list.toArray());
        return folderList;
    } catch (Exception e) {
        throw new ServiceException();
    }
     
 }
	/**
	 * 
	 * @author zhengzb
	 * @desc 为方法添加了OALogInfo ... loginfos 记录日志
	 * 2010-4-30 上午08:38:35 
	 * @param model
	 * @param loginfos
	 * @return
	 */
	public List<ToaArchiveFolder> getAllFolerBySelect(ToaArchiveFolder model ,OALogInfo ... loginfos)throws SystemException,ServiceException{
		try {			
			List<Object> list=new ArrayList<Object>();
			User user=  userservice.getCurrentUser();
			StringBuffer hql = new StringBuffer(
			"from ToaArchiveFolder t where 1=1");
			
			/** 如果案卷名称不为空，则设置案卷名称过滤条件*/
			if (model.getFolderName() != null && !model.getFolderName().equals("")) {
				hql.append(" and t.folderName like ?");
				list.add("%" + model.getFolderName() + "%");
			}
			/** 如果案卷编码不为空，则设置案卷编码过滤条件*/
			if (model.getFolderArrayNo() != null
					&& !model.getFolderArrayNo().equals("")) {
				hql.append(" and t.folderArrayNo like ?");
				list.add("%" + model.getFolderArrayNo() + "%");
			}
			/** 如果案卷创建日期不为空，则设置案卷创建日期过滤条件*/
			if (model.getFolderDate() != null && !model.getFolderDate().equals("")) {
				hql.append(" and t.folderDate>=?");
				list.add( model.getFolderDate());
			}
			/** 如果案卷状态不为空，则设置案卷状态过滤条件*/
			if (model.getFolderAuditing() != null
					&& !model.getFolderAuditing().equals("")) {
				hql.append(" and t.folderAuditing=?");
				list.add( model.getFolderAuditing());
			}
			/** 如果案卷保存期限不为空，则设置案卷保存期限过滤条件*/
			if (model.getFolderLimitId() != null
					&& !model.getFolderLimitId().equals("")) {
				hql.append(" and t.folderLimitId=?");
				list.add( model.getFolderLimitId());
			}
			/** 如果案卷创建者不为空，则设置案卷创建者过滤条件*/
			if (model.getFolderCreaterId() != null
					&& !model.getFolderCreaterId().equals("")) {
				hql.append(" and t.folderCreaterId=?");
				list.add( model.getFolderCreaterId());
			}
			
//			User user=userService.getCurrentUser();
			TUumsBaseOrg org= userService.getSupOrgByUserIdByHa(user.getUserId());
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			String orgids="";
			if(!isView){
				if(org!=null){
					hql.append(" and t.orgId=?");
					list.add(org.getOrgId());				
				}
			}else{
				if(org!=null){
					hql.append(" and  t.folderOrgcode like '"+org.getSupOrgCode()+"%'");					
				}
			}
			
			hql.append("  order by t.folderDate desc,t.folderDepartment desc");
			//是否有过滤条件
			List<ToaArchiveFolder> folderlist=new ArrayList<ToaArchiveFolder>();
			if(list.size()>0){
				Object[] obj=new Object[list.size()];
				for(int i=0;i<list.size();i++){
					obj[i]=list.get(i);
				}
				folderlist= archiveDao.find(hql.toString(), obj);
			}else{
				folderlist= archiveDao.find(hql.toString());
			}
			return folderlist;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查询中的案卷树展现"});
		}
	}

	/**
	 * 获取对应类目下的所有案卷
	 * @param sortId 类目编号
	 * @return java.util.List
	 * @roseuid 494F59360157
	 */
	public List getAllFolder(String sortId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			return archiveDao.find(
				"from ToaArchiveFolder t where t.toaArchiveSort.sortId=? order by t.folderDate",
				sortId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"案卷列表"});
		}
	}
	/**结果输出
     * @author xush
     * @date 6/8/2013 1:50 PM
     */
    public InputStream searchExcel(ToaArchiveFolder model,String archiveSortId,Map statemap)throws DAOException{
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
        cell.setCellValue("卷（盒）号");
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
        cell.setCellValue("年度");
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
        cell.setCellValue("机构");
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
        cell.setCellValue("卷（盒）状态");
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
        List<ToaArchiveFolder> list = search(model,archiveSortId);//获取满足条件的ToaArchiveFolder对象
        for (int i = 0; i < list.size()+0; ++i)
        {
            ToaArchiveFolder folder = list.get(i);
            SimpleDateFormat st = new SimpleDateFormat(
            "yyyy");
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
            cell.setCellValue(folder.getFolderNo());
            cellStyle= wb.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getFolderName());
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue(st.format(folder.getFolderDate()));
            cellStyle= wb.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平对齐
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
           
            
            
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getFolderDepartmentName());
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue(folder.getFolderLimitName());
            cellStyle= wb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cell.setCellStyle(cellStyle);
            
            cell = row.createCell((short) j++);
            cell.setCellValue((String)statemap.get(folder.getFolderAuditing()));
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
    public List<ToaArchiveFolder> search(ToaArchiveFolder model,String archiveSortId)throws DAOException {
        try{
//          Object[] obj = new Object[7];
            List<Object> list=new ArrayList<Object>();
            List<ToaArchiveFolder> list1=new ArrayList<ToaArchiveFolder>();
            StringBuffer hql = new StringBuffer("from ToaArchiveFolder t where t.toaArchiveSort.sortId=?");
            list.add(archiveSortId);
            
        
            /** 如果案卷名称不为空，则设置案卷名称过滤条件*/
            if (model.getFolderName() != null && !model.getFolderName().equals("")) {
                hql.append(" and t.folderName like ?");
                model.setFolderName(model.getFolderName().trim());
                list.add("%" + model.getFolderName() + "%");

            }
            
            
            //** 如果案卷创建日期不为空，则设置案卷创建日期过滤条件*//*
            if (model.getFolderDate() != null && !model.getFolderDate().equals("")) {
                hql.append(" and t.folderDate>=?");
                list.add( model.getFolderDate());
            }
            
            /** 如果案卷编码不为空，则设置案卷编码过滤条件*/
            if (model.getFolderNo() != null
                    && !model.getFolderNo().equals("")) {
                hql.append(" and t.folderNo like ?");
                model.setFolderNo(model.getFolderNo().trim());
                list.add( "%"+model.getFolderNo() + "%");

            }
            /** 如果案卷创建日期不为空，则设置案卷创建日期过滤条件*/
            if (model.getFolderDate() != null && !model.getFolderDate().equals("")) {
                Calendar scal = Calendar.getInstance();//设置查询年度的第一天0时0分0秒
                scal.setTime(model.getFolderDate());
                scal.set(Calendar.MONTH, Calendar.JANUARY);
                scal.set(Calendar.DAY_OF_MONTH, 1);
                scal.set(Calendar.HOUR, 0);
                scal.set(Calendar.MINUTE, 0);
                scal.set(Calendar.SECOND, 0);
//              scal.set(model.getFolderDate().getYear(),1,1,0,0,0);
                hql.append(" and (t.folderDate>=?");
                list.add(scal.getTime());
//              obj[i] = scal.getTime();
//              i++;
                Calendar ecal = Calendar.getInstance();//设置查询年度的最后一天23时59分59秒
                ecal.setTime(scal.getTime());
                ecal.set(Calendar.MONTH, Calendar.DECEMBER);
                ecal.set(Calendar.DAY_OF_MONTH, 31);
                ecal.set(Calendar.HOUR, 23);
                ecal.set(Calendar.MINUTE, 59);
                ecal.set(Calendar.SECOND, 59);
//              ecal.set(model.getFolderDate().getYear(),12,31,23,59,59);
                hql.append(" and t.folderDate<=? )");
                list.add(ecal.getTime());
//              obj[i] = ecal.getTime();
//              i++;
            }
            /** 如果案卷状态不为空，则设置案卷状态过滤条件*/
            if (model.getFolderAuditing() != null
                    && !model.getFolderAuditing().equals("")) {
                hql.append(" and t.folderAuditing=?");
                model.setFolderAuditing(model.getFolderAuditing().trim());
                
                list.add(model.getFolderAuditing());

            }
            /** 如果案卷保存期限不为空，则设置案卷保存期限过滤条件*/
            if (model.getFolderLimitId() != null
                    && !model.getFolderLimitId().equals("")) {
                hql.append(" and t.folderLimitId=?");
                model.setFolderLimitId(model.getFolderLimitId().trim());
                list.add(model.getFolderLimitId());

            }
            /** 如果案卷创建者不为空，则设置案卷创建者过滤条件*/
            if (model.getFolderCreaterId() != null
                    && !model.getFolderCreaterId().equals("")) {
                hql.append(" and t.folderCreaterId=?");
                model.setFolderCreaterId(model.getFolderCreaterId().trim());
                list.add(model.getFolderCreaterId());

            }
            /** 如果案卷机构不为空，则设置案卷机构过滤条件 */
            if (model.getFolderDepartmentName()!=null&&!model.getFolderDepartmentName().equals("")) {
                hql.append(" and t.folderDepartmentName like ? ");
                model.setFolderDepartmentName(model.getFolderDepartmentName().trim());
                list.add("%"+model.getFolderDepartmentName()+"%");

            }
        
            
            hql.append("  order by t.folderDate desc,t.folderDepartment desc ");
            list1=archiveDao.find(hql.toString(), list.toArray());
            return list1;
        }catch(ServiceException e){
            throw new ServiceException(MessagesConst.find_error,               
                    new Object[] {"案卷分页列表"});
        }
    }

        //return list1;
        
  
    
	/**
	 * 根绝条件查找对应类目下的案卷分页列表
	 * 
	 * @param page
	 *            分页对象
	 * @param sortId
	 *            类目编号
	 * @param model
	 *            案卷对象
	 * @return com.strongmvc.orm.hibernate.Page 分页对象
	 * @roseuid 494F597003A9
	 */
	public Page<ToaArchiveFolder> getAllFolder(Page<ToaArchiveFolder> page,
			String sortId, ToaArchiveFolder model,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			Object[] obj = new Object[6];
			StringBuffer hql = new StringBuffer(
					"from ToaArchiveFolder t where t.toaArchiveSort.sortId=?");
			int i = 0;
			/** 如果案卷名称不为空，则设置案卷名称过滤条件*/
			if (model.getFolderName() != null && !model.getFolderName().equals("")) {
				hql.append(" and t.folderName like ?");
				obj[i] = "%" + model.getFolderName() + "%";
				i++;
			}
			/** 如果案卷编码不为空，则设置案卷编码过滤条件*/
			if (model.getFolderArrayNo() != null
					&& !model.getFolderArrayNo().equals("")) {
				hql.append(" and t.folderArrayNo like ?");
				obj[i] = "%" + model.getFolderArrayNo() + "%";
				i++;
			}
			/** 如果案卷创建日期不为空，则设置案卷创建日期过滤条件*/
			if (model.getFolderDate() != null && !model.getFolderDate().equals("")) {
				hql.append(" and t.folderDate>=?");
				obj[i] = model.getFolderDate();
				i++;
			}
			/** 如果案卷状态不为空，则设置案卷状态过滤条件*/
			if (model.getFolderAuditing() != null
					&& !model.getFolderAuditing().equals("")) {
				hql.append(" and t.folderAuditing=?");
				obj[i] = model.getFolderAuditing();
				i++;
			}
			/** 如果案卷保存期限不为空，则设置案卷保存期限过滤条件*/
			if (model.getFolderLimitId() != null
					&& !model.getFolderLimitId().equals("")) {
				hql.append(" and t.folderLimitId=?");
				obj[i] = model.getFolderLimitId();
				i++;
			}
			/** 如果案卷创建者不为空，则设置案卷创建者过滤条件*/
			if (model.getFolderCreaterId() != null
					&& !model.getFolderCreaterId().equals("")) {
				hql.append(" and t.folderCreaterId=?");
				obj[i] = model.getFolderCreaterId();
				i++;
			}

			if (i == 0) {
				page = archiveDao.find(page, hql.toString(), sortId);
			} else if (i == 1) {
				page = archiveDao.find(page, hql.toString(), sortId, obj[0]);
			} else if (i == 2) {
				page = archiveDao
						.find(page, hql.toString(), sortId, obj[0], obj[1]);
			} else if (i == 3) {
				page = archiveDao.find(page, hql.toString(), sortId, obj[0],
						obj[1], obj[2]);
			} else if (i == 4) {
				page = archiveDao.find(page, hql.toString(), sortId, obj[0],
						obj[1], obj[2], obj[3]);
			} else if (i == 5) {
				page = archiveDao.find(page, hql.toString(), sortId, obj[0],
						obj[1], obj[2], obj[3], obj[4]);
			} else if (i == 6) {
				page = archiveDao.find(page, hql.toString(), sortId, obj[0],
						obj[1], obj[2], obj[3], obj[4], obj[5]);
			}
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"案卷分页列表"});
		}
	}

	/**
	 * 根绝条件查找对应类目下的案卷分页列表
	 * 
	 * @param page
	 *            分页对象
	 * @param sortId
	 *            类目编号
	 * @param model
	 *            案卷对象
	 * @return com.strongmvc.orm.hibernate.Page 分页对象
	 * @roseuid 494F597003A9
	 */
	public Page<ToaArchiveFolder> getAllFolderfile(Page<ToaArchiveFolder> page,
			String sortId, String folderDate,ToaArchiveFolder model,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
//			Object[] obj = new Object[7];
			List<Object>list=new ArrayList<Object>();
			
			StringBuffer hql = new StringBuffer("from ToaArchiveFolder t where t.toaArchiveSort.sortId=?");
			list.add(sortId);
			
//			int i = 0;
			/** 如果案卷名称不为空，则设置案卷名称过滤条件*/
			if (model.getFolderName() != null && !model.getFolderName().equals("")) {
				hql.append(" and t.folderName like ?");
				model.setFolderName(model.getFolderName().trim());
				list.add("%" + model.getFolderName() + "%");
//				obj[i] = "%" + model.getFolderName() + "%";
//				i++;
			}
			
			
			//** 如果案卷创建日期不为空，则设置案卷创建日期过滤条件*//*
			
			if (folderDate!= null && !folderDate.equals("")) {
				//hql.append(" and t.folderDate>=?");
				//list.add( model.getFolderDate());
				SimpleDateFormat df = new SimpleDateFormat("yyyy");
                hql.append(" and to_char(t.folderDate,'yyyy') =?");
                list.add(folderDate);
			}
			if (folderDate== null && "".equals(folderDate)){
			if (model.getFolderDate() != null && !model.getFolderDate().equals("")) {
                hql.append(" and t.folderDate>=?");
                list.add( model.getFolderDate());
                SimpleDateFormat df = new SimpleDateFormat("yyyy");
                hql.append(" and to_char(t.folderDate,'yyyy') =?");
                list.add(df.format(model.getFolderDate()) );
            }
			}
			/** 如果案卷编码不为空，则设置案卷编码过滤条件*/
			if (model.getFolderNo() != null
					&& !model.getFolderNo().equals("")) {
				hql.append(" and t.folderNo like ?");
				model.setFolderNo(model.getFolderNo().trim());
				list.add( "%"+model.getFolderNo() + "%");
//				obj[i] = "%"+model.getFolderNo() + "%";
//				i++;
			}
			/** 如果案卷创建日期不为空，则设置案卷创建日期过滤条件*/
			if (model.getFolderDate() != null && !model.getFolderDate().equals("")) {
				Calendar scal = Calendar.getInstance();//设置查询年度的第一天0时0分0秒
				scal.setTime(model.getFolderDate());
				scal.set(Calendar.MONTH, Calendar.JANUARY);
				scal.set(Calendar.DAY_OF_MONTH, 1);
				scal.set(Calendar.HOUR, 0);
				scal.set(Calendar.MINUTE, 0);
				scal.set(Calendar.SECOND, 0);
//				scal.set(model.getFolderDate().getYear(),1,1,0,0,0);
				hql.append(" and (t.folderDate>=?");
				list.add(scal.getTime());
//				obj[i] = scal.getTime();
//				i++;
				Calendar ecal = Calendar.getInstance();//设置查询年度的最后一天23时59分59秒
				ecal.setTime(scal.getTime());
				ecal.set(Calendar.MONTH, Calendar.DECEMBER);
				ecal.set(Calendar.DAY_OF_MONTH, 31);
				ecal.set(Calendar.HOUR, 23);
				ecal.set(Calendar.MINUTE, 59);
				ecal.set(Calendar.SECOND, 59);
//				ecal.set(model.getFolderDate().getYear(),12,31,23,59,59);
				hql.append(" and t.folderDate<=? )");
				list.add(ecal.getTime());
//				obj[i] = ecal.getTime();
//				i++;
			}
			/** 如果案卷状态不为空，则设置案卷状态过滤条件*/
			if (model.getFolderAuditing() != null
					&& !model.getFolderAuditing().equals("")) {
				hql.append(" and t.folderAuditing=?");
				model.setFolderAuditing(model.getFolderAuditing().trim());
				
				list.add(model.getFolderAuditing());
//				obj[i] = model.getFolderAuditing();
//				i++;
			}
			/** 如果案卷保存期限不为空，则设置案卷保存期限过滤条件*/
			if (model.getFolderLimitId() != null
					&& !model.getFolderLimitId().equals("")) {
				hql.append(" and t.folderLimitId=?");
				model.setFolderLimitId(model.getFolderLimitId().trim());
				list.add(model.getFolderLimitId());
//				obj[i] = model.getFolderLimitId();
//				i++;
			}
			/** 如果案卷创建者不为空，则设置案卷创建者过滤条件*/
			if (model.getFolderCreaterId() != null
					&& !model.getFolderCreaterId().equals("")) {
				hql.append(" and t.folderCreaterId=?");
				model.setFolderCreaterId(model.getFolderCreaterId().trim());
				list.add(model.getFolderCreaterId());
//				obj[i] = model.getFolderCreaterId();
//				i++;
			}
			/** 如果案卷机构不为空，则设置案卷机构过滤条件 */
			if (model.getFolderDepartmentName()!=null&&!model.getFolderDepartmentName().equals("")) {
				hql.append(" and t.folderDepartmentName like ? ");
				model.setFolderDepartmentName(model.getFolderDepartmentName().trim());
				list.add("%"+model.getFolderDepartmentName()+"%");
//				obj[i]=model.getFolderDepartmentName();
//				i++;
			}
			
			User user=userService.getCurrentUser();
			TUumsBaseOrg org= userService.getSupOrgByUserIdByHa(user.getUserId());
			boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
			String orgids="";
			if(!isView){
				if(org!=null){
					hql.append(" and t.orgId=?");
					list.add(org.getOrgId());				
				}
			}else{
				if(org!=null){
					hql.append(" and  t.folderOrgcode like '"+org.getSupOrgCode()+"%'");					
				}
			}
			
			
			hql.append("  order by t.folderDate desc,t.folderNo asc ");
			Object[] obj = new Object[list.size()];
			for(int i=0;i<list.size();i++){
				obj[i]=list.get(i);
			}
			page = archiveDao.find(page, hql.toString(), obj);
			
			
//			if (i == 0) {
//				page = archiveDao.find(page, hql.toString(), sortId);
//			} else if (i == 1) {
//				page = archiveDao.find(page, hql.toString(), sortId, obj[0]);
//			} else if (i == 2) {
//				page = archiveDao
//						.find(page, hql.toString(), sortId, obj[0], obj[1]);
//			} else if (i == 3) {
//				page = archiveDao.find(page, hql.toString(), sortId, obj[0],
//						obj[1], obj[2]);
//			} else if (i == 4) {
//				page = archiveDao.find(page, hql.toString(), sortId, obj[0],
//						obj[1], obj[2], obj[3]);
//			} else if (i == 5) {
//				page = archiveDao.find(page, hql.toString(), sortId, obj[0],
//						obj[1], obj[2], obj[3], obj[4]);
//			} else if (i == 6) {
//				page = archiveDao.find(page, hql.toString(), sortId, obj[0],
//						obj[1], obj[2], obj[3], obj[4], obj[5]);
//			}else if (i==7) {
//				page = archiveDao.find(page, hql.toString(), sortId, obj[0],
//						obj[1], obj[2], obj[3], obj[4], obj[5] ,obj[7]);
//			}else if (i==8) {
//				page = archiveDao.find(page, hql.toString(), sortId, obj[0],
//						obj[1], obj[2], obj[3], obj[4], obj[5] ,obj[7],obj[8]);
//			}
			
			
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"案卷分页列表"});
		}
	}

	
	/**
	 * 获取案卷对象
	 * 
	 * @param folderId
	 *            案卷编号
	 * @return com.strongit.oa.bo.ToaArchiveFolder 案卷对象
	 * @roseuid 494F599902EE
	 */
	public ToaArchiveFolder getFolder(String folderId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			ToaArchiveFolder folder=archiveDao.get(folderId);
			return folder;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"案卷对象"});
		}
	}

	/**
	 * author:zhangli 
	 * description:通过案卷编码查找对应案卷 
	 * @param folderNo
	 *            案卷编码
	 * @return java.util.List 案卷列表
	 */
	public List getFolderByNo(String folderNo,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			return archiveDao.find("from ToaArchiveFolder t where t.folderNo=? order by t.folderDate",
				folderNo);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"案卷列表"});
		}
	}

	
	/**
	 * author:zhangli 
	 * description:通过案卷编码和年度查找对应案卷 
	 * @param folderNo
	 *            案卷编码
	 * @param folderDate
	 *             年度
	 * @return java.util.List 案卷列表
	 * @throws ParseException 
	 */
	public List getFolderByNoandDate(String folderNo,String folderDate,OALogInfo ... loginfos) throws SystemException,ServiceException, ParseException{	
		try{			
			return archiveDao.find("from ToaArchiveFolder t where t.folderNo=? and to_char(t.folderDate,'yyyy')=?",
				folderNo,folderDate);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"案卷列表"});
		}
	}
	
	
	/**
	 * author:zhangli 
	 * description:判断是否有同样编码的案卷存在 
	 * modifyer: 
	 * description:
	 * 
	 * @param folderNo
	 *            案卷编码
	 * @return boolean true:存在编码案卷，false:不存在同编码案卷
	 */
	public boolean hasthesameNo(String folderNo,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			List list = getFolderByNo(folderNo);
			if (list != null && list.size() > 0)
				return true;
			else
				return false;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.judge_error,               
					new Object[] {"是否有同样编码的案卷存在"});
		}
	}

	/**
	 * 保存案卷
	 * 
	 * @param object
	 *            案卷对象
	 * @return java.lang.String 保存结果
	 * @roseuid 494F5CC6033C
	 */
	public String saveFolder(ToaArchiveFolder object,OALogInfo ... loginfos)throws SystemException,ServiceException {
		try{
			String message = null;
//			if (hasthesameNo(object.getFolderNo())) {
//				message = "已存在该案卷编号，不能进行保存！";
//			}
//			else {
			User user=userservice.getCurrentUser();
			TUumsBaseOrg org= userservice.getSupOrgByUserIdByHa(user.getUserId());
			if(org!=null){
				object.setFolderOrgcode(org.getSupOrgCode());
				object.setOrgId(org.getOrgId());
			}
				archiveDao.save(object);
				message = "案卷保存成功！";
//			}
			return message;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"案卷"});
		}
	}

	/**
	 * 批量删除案卷
	 * 
	 * @param folderId
	 *            案卷编号串
	 * @return java.lang.String 删除结果
	 * @roseuid 494F5D0701B5
	 */
	public String delFolder(String folderId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		StringBuffer message = new StringBuffer();
		StringBuffer cannotmsg = new StringBuffer();
		StringBuffer successmsg = new StringBuffer();
		StringBuffer cnmsg = new StringBuffer();
		try {
			String[] str = folderId.split(",");
			for (int i = 0; i < str.length; i++) {
				ToaArchiveFolder foder = getFolder(str[i]);
				List tfilelist = fileManager.getFileByFolderId(folderId);				//获取档案中心，所属案卷ID下的文件列表
				List filelist = archivefilemanager.getAllFile(folderId, null);			//获取档案室，所属案卷ID下的文件列表
				if(foder.getFolderAuditing()!=null&&!foder.getFolderAuditing().equals("0")&&!foder.getFolderAuditing().equals("3")&&!foder.getFolderAuditing().equals("5")){
					cnmsg.append(",").append(foder.getFolderName());
				}
				else if ((tfilelist != null && tfilelist.size() > 0)
						|| (filelist != null && filelist.size() > 0)) {// 是否存在文件的判断需要调用文件manager进行判断
					cannotmsg.append(",").append(foder.getFolderName());
				}else {
					delete(foder);
					successmsg.append(",").append(foder.getFolderName());
				}
			}
			if (cannotmsg.length() > 0)
				message.append("案卷 ").append(cannotmsg.toString().substring(1))
						.append(" 下存在文件，请先删除相应文件！\n");
			if(cnmsg.length()>0){
				message.append("案卷 ").append(cnmsg.toString().substring(1))
				.append(" 已经归档不能删除！\n");
			}
			if (successmsg.length() > 0)
				message.append("案卷 ")
						.append(successmsg.toString().substring(1)).append(
								" 删除成功！\n");
			return message.toString();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"案卷"});
		}
		
	}

	/**
	 * 删除某个案卷
	 * 
	 * @param foder
	 *            案卷对象
	 * @roseuid 494F5D0701B5
	 */
	public void delete(ToaArchiveFolder foder,OALogInfo ... loginfos)throws SystemException,ServiceException{
		try{
			archiveDao.delete(foder);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"案卷"});
		}
	}

	/**
	 * author:zhangli 
	 * description:申请归档 
	 * modifyer: 
	 * description:
	 * @param folderId
	 *            案卷编号
	 * @return java.lang.String 归档结果
	 */
	public String appigeonhole(String folderId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		StringBuffer message = new StringBuffer();  //
		StringBuffer haspigemsg = new StringBuffer();//已归档
		StringBuffer pigeingmsg = new StringBuffer();//审核中
		StringBuffer successmsg = new StringBuffer();//归档申请成功
		StringBuffer destrmsg=new StringBuffer();//销毁审核
		try {
			String[] str = folderId.split(",");
			for (int i = 0; i < str.length; i++) {
				ToaArchiveFolder model = getFolder(str[i]);
				if (YES.equals(model.getFolderAuditing()))
					haspigemsg.append(",").append(model.getFolderName());
				else if (AUDITING.equals(model.getFolderAuditing()))
					pigeingmsg.append(",").append(model.getFolderName());
				else if(DESTR_AUDIT.equals(model.getFolderAuditing())){ 
					destrmsg.append(",").append(model.getFolderName());
				}else{
				
					model.setFolderArchiveTime(new Date()); // 记录归档时间
					model.setFolderAuditing(AUDITING); // 修改状态为【审核中】
					archiveDao.save(model);
					successmsg.append(",").append(model.getFolderName());
				}
			}
			if (haspigemsg.length() > 0)
				message.append("案卷 ")
						.append(haspigemsg.toString().substring(1)).append(
								" 已归档，不需再提交归档！\n");
			if (pigeingmsg.length() > 0)
				message.append("案卷 ")
						.append(pigeingmsg.toString().substring(1)).append(
								" 已提交归档，不需再提交归档！\n");
			if (successmsg.length() > 0)
				message.append("案卷 ")
						.append(successmsg.toString().substring(1)).append(
								" 提交归档申请成功！\n");
			if (destrmsg.length() > 0)
				destrmsg.append("案卷 ")
						.append(successmsg.toString().substring(1)).append(
								" 销毁审核中不可以归档！\n");
			return message.toString();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.apply_error,               
					new Object[] {"案卷归档"});
		}		
	}
	
	/** 
	 * @author zzbsteven
	 * @param folderId //所要归档卷（盒）的ID
	 * @desc 当系统全局开关，设置为直接归档时，对未归档的案卷直接归档
	 * @param loginfos
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String auditArchiveFolder(String folderId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		StringBuffer message = new StringBuffer();  //
		StringBuffer haspigemsg = new StringBuffer();//已归档
		try {
			String[] str = folderId.split(",");
			for (int i = 0; i < str.length; i++) {
				ToaArchiveFolder model = getFolder(str[i]);
				if (YES.equals(model.getFolderAuditing()))
					haspigemsg.append(",").append(model.getFolderName());
			}
			if (haspigemsg.length() > 0)
			{
				message.append("卷(盒) ").append(haspigemsg.toString().substring(1)).append(" 已归档，不需再归档！\n")
				.append("只能对“未归档”和“审核中”的卷盒直接归档。请重新选择你所要直接归档的卷盒！！！");
				return message.toString();				
			}
			else {
				for (int i = 0; i < str.length; i++) {
					ToaArchiveFolder model = getFolder(str[i]);
					if(NO.equals(model.getFolderAuditing()))
					{
						model.setFolderAuditing(AUDITING); // 修改状态为【审核中】
						archiveDao.save(model);						
					}
				}
				return auditpigeonhole(folderId,"1","", "管理员直接归档",new OALogInfo("直接归档"));//直接归案				
			}		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new ServiceException(MessagesConst.apply_error,               
					new Object[] {"卷（盒）直接归档"});
		}
	}
	
	/**
	 * author:zhangli 
	 * description:审核归档申请 
	 * modifyer: 
	 * description:
	 * 
	 * @param folderId
	 *            案卷编号
	 * @param type
	 *            案卷审核状态
	 * @param folderArchiveNo
	 *            归档号
	 * @param content
	 *            审核意见
	 * @return java.lang.String 审核结果
	 */
	public String auditpigeonhole(String folderId, String type,
			String folderArchiveNo, String content,OALogInfo ... loginfos)throws SystemException,ServiceException {
		StringBuffer message = new StringBuffer();
		StringBuffer haspigemsg = new StringBuffer();//已归档
		StringBuffer pigebackmsg = new StringBuffer();//归档驳回
		StringBuffer unpigemsg = new StringBuffer();//不在审核中
		StringBuffer successmsg = new StringBuffer();
		StringBuffer backmsg = new StringBuffer();
		User userinfo = getCurrentUser();
		try {
			String[] str = folderId.split(",");
			for (int i = 0; i < str.length; i++) {
				ToaArchiveFolder model = getFolder(str[i]);//获取案卷
				if (YES.equals(model.getFolderAuditing()))
					haspigemsg.append(",").append(model.getFolderName());
				else if (BACK.equals(model.getFolderAuditing()))
					pigebackmsg.append(",").append(model.getFolderName());
				else if (!AUDITING.equals(model.getFolderAuditing()))
					unpigemsg.append(",").append(model.getFolderName());
				else {
					model.setFolderAuditing(type);// 保存案卷审核状态
					model.setFolderAuditingContent(content);// 保存审核意见
					model.setFolderAuditingTime(new Date());// 保存审核日期
					model.setFolderArchiveNo(folderArchiveNo);// 保存归档号
					if (userinfo != null) {
						model.setFolderAuditingId(userinfo.getUserId());// 保存审核人编号
						model.setFolderAuditingName(userinfo.getUserName());// 保存审核人名称
					}
					archiveDao.save(model);
					if(YES.equals(model.getFolderAuditing())){//如果审核通过则进行以下操作
						insertToFile(str[i]);// 将年内文件保存入档案文件
						deleteTempFile(str[i],new OALogInfo("删除案卷下文件"));// 将年内文件删除
					}
					if(BACK.equals(type))
						backmsg.append(",").append(model.getFolderName());
					else
						successmsg.append(",").append(model.getFolderName());
				}
			}
			if (haspigemsg.length() > 0)
				message.append("案卷 ")
						.append(haspigemsg.toString().substring(1)).append(
								" 已归档，不需再归档！\n");
			if (pigebackmsg.length() > 0)
				message.append("案卷 ").append(
						pigebackmsg.toString().substring(1)).append(
						" 归档申请未通过，请重新提交归档申请！\n");
			if (unpigemsg.length() > 0)
				message.append("案卷 ").append(unpigemsg.toString().substring(1))
						.append(" 未提交归档申请，请先提交归档申请！\n");
			if (successmsg.length() > 0)
				message.append("案卷 ")
						.append(successmsg.toString().substring(1)).append(
								" 归档成功！\n");
			if (backmsg.length() > 0)
				message.append("案卷 ")
						.append(backmsg.toString().substring(1)).append(
								" 被驳回归档申请！\n");
			return message.toString();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.aduit_error,               
					new Object[] {"案卷归档"});
		}
		
	}

	/**
	 * author:zhangli 
	 * description:移动案卷 
	 * modifyer: 
	 * description:
	 * 
	 * @param folderId
	 *            案卷编号
	 * @param sort
	 *            要移动到的类目
	 * @return java.lang.String 移动结果
	 */
	public String removefoder(String folderId, ToaArchiveSort sort,OALogInfo ... loginfos) throws SystemException,ServiceException{
		StringBuffer message = new StringBuffer();
		StringBuffer haspigemsg = new StringBuffer();
		StringBuffer pigeingmsg = new StringBuffer();
		StringBuffer successmsg = new StringBuffer();
		StringBuffer edstrmsg=new StringBuffer();
		try {
			String[] str = folderId.split(",");
			for (int i = 0; i < str.length; i++) {
				ToaArchiveFolder model = getFolder(str[i]);
				if (YES.equals(model.getFolderAuditing()))
					haspigemsg.append(",").append(model.getFolderName());
				else if (AUDITING.equals(model.getFolderAuditing()))
					pigeingmsg.append(",").append(model.getFolderName());
				else if(DESTR_AUDIT.equals(model.getFolderAuditing()))
					edstrmsg.append(",").append(model.getFolderName());
				else {
					model.setToaArchiveSort(sort); // 记录归档时间
					archiveDao.save(model);
					successmsg.append(",").append(model.getFolderName());
				}
			}
			if (haspigemsg.length() > 0)
				message.append("案卷 ")
						.append(haspigemsg.toString().substring(1)).append(
								" 已归档，不可移动！\n");
			if (pigeingmsg.length() > 0)
				message.append("案卷 ")
						.append(pigeingmsg.toString().substring(1)).append(
								" 已提交归档，不可移动！\n");
			if(edstrmsg.length()>0)
				message.append("案卷")
				     .append(edstrmsg.toString().substring(1)).append("已提交销毁审核，不可以移动！\n");
			
			if (successmsg.length() > 0)
				message.append("案卷 ")
						.append(successmsg.toString().substring(1)).append(
								" 移动成功！\n");
			
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.move_error,               
					new Object[] {"案卷"});
		}
		return message.toString();
	}

	/**
	 * author:zhangli 
	 * description:获取当前用户 
	 * modifyer: 
	 * description:
	 * 
	 * @return User 用户对象
	 */
	public User getCurrentUser() throws SystemException,ServiceException{
		try{
			return userservice.getCurrentUser();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"当前用户"});
		}
	}
	
	/**
	 * author:zhangli 
	 * description:获取指定用户的组织机构信息 
	 * modifyer: 
	 * description:
	 * @param userId 用户编号
	 * @return Organization 组织机构对象
	 */
	public Organization getUserDepartmentByUserId(String userId)throws SystemException,ServiceException{
		try{
			return userservice.getUserDepartmentByUserId(userId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"组织机构对象"});
		}
	}
	
	/**
	 * author:zhangli 
	 * description:获取指定用户的组织机构信息 
	 * modifyer: 
	 * description:
	 * @param userId 用户编号
	 * @return Organization 组织机构对象
	 */
	public Organization getDepartmentByOrgId(String orgId)throws SystemException,ServiceException{
		try{
			return userservice.getDepartmentByOrgId(orgId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"组织机构对象"});
		}
	}
	
	/**
	 * author:zhangli 
	 * description:获取所有处室 
	 * modifyer: 
	 * description:
	 * 
	 * @return User 用户对象
	 */
	public List getOrgList(String moduletype) throws SystemException,ServiceException{
		try{
			List<Organization> orglist = new ArrayList();
			List<TempPo> newList = new ArrayList<TempPo>();
			List<String> syscodeList = new ArrayList<String>();
//			if ("pige".equals(moduletype)) {// 如果为处室管理员，只能查看对应处室档案
//				orglist = userservice.getOrgAndChildOrgByCurrentUser();	
//			}else if("wpige".equals(moduletype)){
//				orglist = userservice.getCurrentUserOrgAndDept();
//			}
//			else{
//				boolean isView=userService.isViewChildOrganizationEnabeld();			//是否允许看到下级机构
//				User user=userService.getCurrentUser();
//				if(!isView){
//					Organization org=userservice.getDepartmentByOrgId(user.getOrgId());
//					orglist.add(org);
//				}
//				else{
//					orglist=userservice.getOrgAndChildOrgByCurrentUser();					
//				}
//			}
			orglist = userservice.getOrgAndChildOrgByUserId("6006800BBDF034DFE040007F01001D9F");//超级管理员  任何用户都能看到所有部门
			if(orglist==null||orglist.size()<=0)
				return new ArrayList<TempPo>();
			
			for(int i=0;i<orglist.size();i++){
				Organization org = orglist.get(i);
				syscodeList.add(org.getOrgSyscode());
			}
			for(int i=0;i<orglist.size();i++){
				TempPo po = new TempPo();
				po.setType("org");
				Organization org = orglist.get(i);
				if((org.getRest1()!=null && "0".equals(org.getRest1())) || "001".equals(org.getOrgCode())){	//以"001"判断是否为江西省人民政府(虚机构)
					String orgSysCode = org.getOrgSyscode();
					Organization porg = userservice.getParentDepartmentBySyscode(orgSysCode);
					String porgsyscode = porg.getOrgSyscode();
					if(porg==null||orgSysCode.equals(porgsyscode)||!syscodeList.contains(porgsyscode))
							po.setParentId(null);
					else
						po.setParentId(porg.getOrgId());
					po.setId(org.getOrgId());
					po.setCodeId(orgSysCode);
					po.setName(org.getOrgName());
					newList.add(po);
				}
			}
			return newList;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"当前用户"});
		}
	}
	
	/**
	 * author:dengzc
	 * description:获取父级节点
	 * modifyer:
	 * description:
	 * @param code
	 * @param ruleCode
	 * @param defaultParent
	 * @return
	 */
	@Transactional(readOnly=true)
	public String findFatherCode(String code,String ruleCode,String defaultParent) throws SystemException,ServiceException{
		int length1 = 0;
		int length2 = 0;
		int codeLength = code.length();
		String fatherCode = "0";
		if(defaultParent != null && !"".equals(defaultParent)){
			fatherCode = defaultParent;
		}
		for (int i = 0; i < ruleCode.length(); i++) {
			length1 = length1 + Integer.parseInt(ruleCode.substring(i, i+1));
			if (i > 0) {
				length2 = length2 + Integer.parseInt(ruleCode.substring(i-1, i));
			}
			if (codeLength == length1) {
				if (length2 != 0) {
					fatherCode = code.substring(0, length2);
					break;
				}
			}
		}
		return fatherCode;
	}
	
	/**
	 * author:zhangli 
	 * description:获取对应机构下的所有用户 
	 * modifyer: 
	 * description:
	 * 
	 * @return User 用户对象
	 */
	public List getUserList(String orgId) throws SystemException,ServiceException{
		try{
			return userservice.getUsersByOrgID(orgId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"当前用户"});
		}
	}

	/**
	 * author:zhangli
	 * description:注册用户Service接口
	 * @param archivefilemanager 用户Service接口
	 */
	@Autowired
	public void setUserservice(IUserService userservice) throws SystemException,ServiceException{
		this.userservice = userservice;
	}

	/**
	 * author:zhangli
	 * description:注册档案文件Manager
	 * @param archivefilemanager 档案文件Manager
	 */
	@Autowired
	public void setFileManager(TempFileManager fileManager) throws SystemException,ServiceException{
		this.fileManager = fileManager;
	}
	
	/** 
	 * @author: 郑志斌  2010/04/14 20：57
	 * @desc:根据案卷id获取"ToaArchiveTempfile"文件列表
	 * @param : String folderId 案卷id
	 * @return :文件"ToaArchiveTempfile"列表
	 * */
	public List<ToaArchiveTempfile> getTempfileByForderId(String folderId,OALogInfo ... loginfos)throws SystemException,ServiceException{  
		try{
			return fileManager.getTempfileByForderId(folderId);
		}
		catch (Exception e) {
			// TODO: handle exception
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"年内文件列表"});
		}
		
	}

	/**
	 * @author：pengxq
	 * @time：2008-12-29下午06:14:38
	 * @desc：根据案卷id获取文件列表
	 * @param String
	 *            folderId 案卷id
	 * @return List 文件列表
	 */
	public List getFileByFolderId(String folderId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			List list =fileManager.getFileByFolderId(folderId);
			return list;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"年内文件列表"});
		}
	}
	/**
	 * @author：ganyao
	 * @time：6/27/2013 5:18 PM6/27/2013 5:18 PM
	 * @desc：根据案卷id获取文件列表
	 * @param String
	 *            folderId 案卷id
	 * @return List 文件列表
	 */

	public List getFileByFolderIds(String folderId,
			ToaArchiveTempfile tempfilemodel,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			return fileManager.getFileByFolderIds(folderId, tempfilemodel);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"年内文件列表"});
		}
	}
	/**
	 * @author：ganyao
	 * @time：6/27/2013 5:18 PM6/27/2013 5:18 PM
	 * @desc：根据案卷id获取文件列表
	 * @param String
	 *            folderId 案卷id
	 * @return List 文件列表
	 */

	public List<ToaArchiveTempfile> getFilesByFolderIds(String folderId,
			ToaArchiveTempfile tempfilemodel,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			//String[] str=folderId.split(",");
		    List<ToaArchiveTempfile> tempList=new ArrayList<ToaArchiveTempfile>();
			List<Object> list=new ArrayList<Object>();
			StringBuffer hql=new StringBuffer(" from ToaArchiveTempfile t  where 1=1 ");
			/** 如果档案文件所属案卷过滤条件*/
            if(folderId!=null&&!"".equals(folderId)){
                String[] str = folderId.split(",");
                 if("0".equals(folderId)){
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
            hql.append(" order by t.toaArchiveFolder.folderNo asc,t.tempfileNo asc");
            tempList=tempFileDao.find(hql.toString(),list.toArray());
			return tempList;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"年内文件列表"});
		}
	}
	/**
     * @author：xush
     * @time：7/1/2013 10:11 AM
     * @desc：根据案卷id获取文件list
     * @param String
     *            folderId 案卷id
     * @return List 文件列表
     */

    public List<ToaArchiveTempfile> createPieceNo(String folderId,
            ToaArchiveTempfile tempfilemodel,OALogInfo ... loginfos) throws SystemException,ServiceException{
        try{
            //String[] str=folderId.split(",");
            List<ToaArchiveTempfile> tempfileList=new ArrayList<ToaArchiveTempfile>();
            List<Object> list=new ArrayList<Object>();
            StringBuffer hql=new StringBuffer(" from ToaArchiveTempfile t  where 1=1 ");
            /** 如果档案文件所属案卷过滤条件*/
            if(folderId!=null&&!"".equals(folderId)){
                String[] str = folderId.split(",");
                 if("0".equals(folderId)){
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
            hql.append(" order by t.toaArchiveFolder.folderNo asc,t.tempfileNo asc ");
            tempfileList=tempFileDao.find(hql.toString(),list.toArray());
            return tempfileList;
        } catch (ServiceException e) {
            throw new ServiceException(MessagesConst.find_error,               
                    new Object[] {"年内文件列表"});
        }
    }
    /**
     * @author：xush
     * @time：7/3/2013 1:00 PM
     * @desc：根据案卷id获取文件list
     * @param String
     *            tempfileId 文件id
     * @return
     */

    public void getTempfileById(String tempfileId,String tempPieceNo,OALogInfo ... loginfos) throws SystemException,ServiceException{
        try{
            //String[] str=folderId.split(",");
            List<ToaArchiveTempfile> tempfileList=new ArrayList<ToaArchiveTempfile>();
            List<Object> list=new ArrayList<Object>();
            StringBuffer hql=new StringBuffer(" from ToaArchiveTempfile t  where 1=1 ");
            /** 如果档案文件所属案卷过滤条件*/
            if(tempfileId!=null&&!"".equals(tempfileId)){
                       hql.append(" and t.tempfileId=?");
                            list.add(tempfileId);
                        
                    }
             tempfileList=tempFileDao.find(hql.toString(),list.toArray());
             tempfileList.get(0).setTempfilePieceNo(tempPieceNo);
             tempFileDao.update(tempfileList.get(0));
        } catch (ServiceException e) {
            throw new ServiceException(MessagesConst.find_error,               
                    new Object[] {"年内文件列表"});
        }
    }
    /**
     * @author：xush
     * @time：7/8/2013 2:11 PM
     * @desc：校验在此page页面下是否有相同的件号的list
     * @param String String folderId String tempPieceNo
     *            tempfileId 文件id
     * @return List 文件列表
     */

    public List<ToaArchiveTempfile> getSamePieceNo(String folderId,String tempfileId,String tempPieceNo,OALogInfo ... loginfos) throws SystemException,ServiceException{
        try{
            //String[] str=folderId.split(",");
            List<ToaArchiveTempfile> tempfileList=new ArrayList<ToaArchiveTempfile>();
            List<Object> list=new ArrayList<Object>();
            StringBuffer hql=new StringBuffer(" from ToaArchiveTempfile t  where 1=1 ");
            /** 如果档案文件所属案卷过滤条件*/
            if(tempfileId!=null&&!"".equals(tempfileId)){
                       hql.append(" and t.tempfileId <>?");
                            list.add(tempfileId);
                        
                    }
            if(tempPieceNo!=null&&!"".equals(tempPieceNo)){
                hql.append(" and t.tempfilePieceNo =?");
                     list.add(tempPieceNo);
                 
             }
            /** 如果档案文件所属案卷过滤条件*/
            if(folderId!=null&&!"".equals(folderId)){
                String[] str = folderId.split(",");
                 if("0".equals(folderId)){
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
             tempfileList=tempFileDao.find(hql.toString(),list.toArray());
             return  tempfileList;
        } catch (ServiceException e) {
            throw new ServiceException(MessagesConst.find_error,               
                    new Object[] {"年内文件列表"});
        }
    }
    /**
     * @author：xush
     * @time：7/8/2013 2:11 PM
     * @desc：得到件号
     * @param List<ToaArchiveTempfile> tempfile
     * @return 
     */
    public void createPieceNoTrue(List<ToaArchiveTempfile> tempfile){
        int i=1;
       try {
        for (ToaArchiveTempfile tempfile1 : tempfile) {
            //if(tempfile1.getToaArchiveFolder().getFolderId()!=null&&!"".equals(tempfile1.getToaArchiveFolder().getFolderId()))
            tempfile1.setTempfilePieceNo("" + i);
            i++;
            //tempFileDao.update(tempfile1);
        }
    } catch (Exception e) {
        // TODO: handle exception
        throw new ServiceException();
    }
    }
    /**
     * @author：xush
     * @time：7/8/2013 2:11 PM
     * @desc：修改时保存件号
     * @param List<ToaArchiveTempfile> tempfile  String tempfPieceNo
     * @return 
     */
    public void saveTempfPieceNo(List<ToaArchiveTempfile> tempfile,String tempfPieceNo){
       String[] str=tempfPieceNo.split(",");
       try {
       for(int i=0;i<tempfile.size();i++){
           if("null".equals(str[i])){ 
               tempfile.get(i).setTempfilePieceNo("");
           }else{
               tempfile.get(i).setTempfilePieceNo(str[i]);
           }
           
           tempFileDao.update(tempfile.get(i));
       }
            //if(tempfile1.getToaArchiveFolder().getFolderId()!=null&&!"".equals(tempfile1.getToaArchiveFolder().getFolderId()))
      } catch (Exception e) {
        // TODO: handle exception
        throw new ServiceException();
    }
    }
	/**
	 * @author：pengxq
	 * @time：2009-1-4下午06:43:05
	 * @desc: 案卷归档模块中，查看未归档案卷的文件，按查询条件查询
	 * @param String
	 *            folderId 案卷编号
	 * @param ToaArchiveTempfile
	 *            objs 年内文件对象
	 * @return List 查询结果
	 */
	public List getFileByFolderId(String folderId,
			ToaArchiveTempfile tempfilemodel,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			return fileManager.getFileByFolderId(folderId, tempfilemodel);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"年内文件列表"});
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
	public List getArchiveFileByFolderId(String folderId, ToaArchiveFile model,OALogInfo ... loginfos)
				throws SystemException,ServiceException{
		try{
			return archivefilemanager.getAllFile(folderId, model);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"档案文件列表"});
		}
	}

	/**
	 * 获取档案文件对象
	 * 
	 * @param fileId
	 *            档案文件编号
	 * @return com.strongit.oa.bo.ToaArchiveFile 档案文件对象
	 * @roseuid 495881740119
	 */
	public ToaArchiveFile getToaArchiveFile(String fileId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			return archivefilemanager.getFile(fileId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"档案文件列表"});
		}
	}

	/**
	 * @author：pengxq
	 * @time：2008-12-29下午06:15:28
	 * @desc：对选择的文件组卷
	 * @param ToaArchiveFolder
	 *            toaArchiveFolder 案卷对象
	 * @param String[]
	 *            fileId 文件id数组
	 * @return void
	 */
	public void gropFile(ToaArchiveFolder model, String[] fileId,OALogInfo ... loginfos)
			throws SystemException,ServiceException {
		try{
			fileManager.gropFile(model, fileId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.folder_group_error);
		}
	}

	/**
	 * @author zhengzb
	 * @desc 对归档成功的案卷，中的文件所属附件，添加全文检索索引
	 * @time 2010年5月12日15:59:48
	 */
	/**
	 * author:zhangli 
	 * description: 將整個案卷中的年內文件保存入檔案文件中
	 * @param folderId 案卷编号
	 * @throws SQLException
	 */
	public void insertToFile(String folderId,OALogInfo ... loginfos) throws SystemException,ServiceException {
		try{
//			List list = getFileByFolderId(folderId);
			List<ToaArchiveTempfile> list = getTempfileByForderId(folderId);
			ToaArchiveFolder folder=new ToaArchiveFolder();//所属案卷
			folder.setFolderId(folderId);
			
			for (int i = 0; i < list.size(); i++) {
				ToaArchiveTempfile tfile = (ToaArchiveTempfile) list.get(i);/**获取列表中的年内文件对象*/
				
				ToaArchiveFile file = new ToaArchiveFile();					/**新建一个档案文件对象*/

				/**将年内文件中的值赋给档案文件中*/
				file.setFileAuthor(tfile.getTempfileAuthor());/**作者*/
				file.setFileDate(tfile.getTempfileDate());/**创建日期*/
				file.setFileDepartment(tfile.getTempfileDepartment());/**单位*/
				file.setFileDesc(tfile.getTempfileDesc());/**描述*/
				file.setFileDocId(tfile.getTempfileDocId());/**收发文编号*/
				file.setFileDocType(tfile.getTempfileDocType());/**收发文类型*/
				file.setFileNo(tfile.getTempfileNo());/**文件编号*/
				file.setFilePage(tfile.getTempfilePage());/**文件页数*/
				file.setFileTitle(tfile.getTempfileTitle());/**文件标题*/
				file.setToaArchiveFolder(folder);/**案卷对象*/
				file.setFileYear(tfile.getTempfileYear());
				file.setFileDepartmentName(tfile.getTempfileDepartmentName());//机构
				file.setFileDepartment(tfile.getTempfileDepartment());//机构ID
				file.setFile_sortorder(tfile.getTempfile_sortorder());//顺序号;
				file.setFilePieceNo(tfile.getTempfilePieceNo());//件号
				file.setFileDeadlineId(tfile.getTempfileDeadlineId());//保存期限ID
				file.setFileDeadline(tfile.getTempfileDeadline());//保存期限
				file .setFilePlace(tfile.getTempfilePlace());//地点
				file.setFileFigure(tfile.getTempfileFigure());//人物
				file.setFileReasons(tfile.getTempfileReasons());//事由
				file.setFileAwardsOrgId(tfile.getTempfileAwardsOrgId());//颁奖单位Id
				file.setFileAwardsOrg(tfile.getTempfileAwardsOrg());//颁奖单位
				file.setFileAwardsContent(tfile.getTempfileAwardsContent());//获奖内家
				
				file.setFileOrgcode(tfile.getTempfileOrgCode());			//机构授权CODE                 郑 志斌 2010-12 -20 添加
				file.setFileOrgId(tfile.getTempfileOrgId());							//机构ID
				
				
				
			    file.setFileMonth(tfile.getTempfileMonth());
			    file.setFileFormId(tfile.getTempfileFormId());
				file.setWorkflow(tfile.getWorkflow());
				String [] archiveFile=new String[7];//定义档案文件数组,做全文检索所检索文件的字段用
				archiveFile[0]=file.getFileTitle();//档案文件题名
				archiveFile[1]=file.getFileDeadline();//档案文件保管权限
				archiveFile[2]=file.getFileDepartmentName();//档案文件所属部门
				archiveFile[3]=file.getFileDesc();//档案文件备注
				archiveFile[4]=file.getFileAuthor();//责任者
				archiveFile[5]= getDateTime(file.getFileDate());//档案文件时间
				archiveFile[6]=file.getFileNo();//档案文件文号
				archivefilemanager.saveFile(file,new OALogInfo("保存档案文件"));/**保存案卷文件*/
				List<ToaArchiveTfileAppend> applist=annexManager.getListAnnex(tfile.getTempfileId());
				if(applist.size()==0||applist==null){			//档案文件中不含有附件
					searchManager.delIndex(tfile.getTempfileId());//删除资料中心文件索引指针
					searchManager.saveIndex(file);				  //归档，添加档案文件索引
				}
				for(ToaArchiveTfileAppend tfileappend:applist){
	                		if(tfileappend!=null&&tfileappend.getTempappendId()!=null){
	                			ToaArchiveFileAppend fileappend = new ToaArchiveFileAppend();/**新建一个档案文件附件对象*/

	                			/**将年内文件附件中的值赋给档案文件附件中*/
	                			fileappend.setAppendName(tfileappend.getTempappendName());/**附件名称*/
	                			fileappend.setAppendContent(tfileappend
	                					.getTempappendContent());/**附件内容*/
	                			fileappend.setAppendType(tfileappend.getTempappendType());/**附件类型*/
	                			fileappend.setAppendSize(tfileappend.getTempappendSize());/**附件大小*/
	                			fileappend.setToaArchiveFile(file);/**档案文件对象*/
	                			archivefilemanager.saveFileAppend(fileappend);/**保存案卷文件附件*/
//	                			String ext= (fileappend.getAppendName()).substring((fileappend.getAppendName()).lastIndexOf(".")+1);//获取后缀名
								try {
//									File file2 = FileUtil.byteArray2File(fileappend.getAppendContent());//把附件二进制数组转化为文件。
									File file2=byteArray2File(fileappend.getAppendContent());
									FileInputStream indexfis = new FileInputStream(file2);
									searchManager.saveIndex(fileappend, archiveFile, indexfis);//保存档案文件搜索索引
//									file2.deleteOnExit();
									file2.delete();
								} catch (FileNotFoundException e) {
									// TODO 自动生成 catch 块
									e.printStackTrace();
								} catch (Exception e) {
									// TODO 自动生成 catch 块
									e.printStackTrace();
								}
	                			
	                		}
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException("将整个案卷中的年內文件保存入档案文件中出错！");
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
//			file.deleteOnExit();
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
	 * author:zhangli 
	 * description: 將整個案卷中的年內文件保存入檔案文件中(原生语句方式) 
	 * modifyer:
	 * description:
	 * 
	 * @param folderId 案卷编号
	 * @throws SQLException
	 */
	public void insertToFileBySql(String folderId,OALogInfo ... loginfos) throws SystemException,ServiceException {
		try{
			List list = getFileByFolderId(folderId);
			UUIDGenerator uuidgenerator = new UUIDGenerator();
			String primaryKeyValue = null;
			String childKeyValue = null;
			for (int i = 0; i < list.size(); i++) {
				ToaArchiveTempfile tfile = (ToaArchiveTempfile) list.get(i);
				primaryKeyValue = String.valueOf(uuidgenerator.generate());
				StringBuffer sql = new StringBuffer("insert into T_OA_ARCHIVE_FILE")
						.append(
								"(FILE_ID,FOLDER_ID,FILE_NO,FILE_AUTHOR,FILE_TITLE,FILE_DATE,FILE_PAGE,FILE_DESC,FILE_DEPARTMENT,FILE_DOC_TYPE,FILE_DOC_ID)")
						.append(" values('").append(primaryKeyValue).append("','")
						.append(folderId).append("','").append(
								tfile.getTempfileAuthor()).append("','").append(
								tfile.getTempfileTitle()).append("','").append(
								tfile.getTempfileDate()).append("',").append(
								tfile.getTempfilePage()).append(",'").append(
								tfile.getTempfileDesc()).append("','").append(
								tfile.getTempfileDepartment()).append("','")
						.append(tfile.getTempfileDocType()).append("','").append(
								tfile.getTempfileDocId()).append("')");
				//archiveDao.getConnection().prepareStatement(sql.toString()).execute();
				archiveDao.executeJdbcUpdate(sql.toString());
				Set tfileappendSet = tfile.getToaArchiveTfileAppends();
				Iterator it = tfileappendSet.iterator();

				while (it.hasNext()) {
					ToaArchiveTfileAppend tfileappend = (ToaArchiveTfileAppend) it
							.next();
					sql = new StringBuffer("insert into T_OA_ARCHIVE_FILE_APPEND")
							.append(
									"(APPEND_ID,FILE_ID,APPEND_CONTENT,APPEND_TYPE)")
							.append(" values('").append(childKeyValue)
							.append("','").append(primaryKeyValue).append("','")
							.append(tfileappend.getTempappendName()).append("','")
							.append(tfileappend.getTempappendType()).append("')");
					archiveDao.executeJdbcUpdate(sql.toString());
					//archiveDao.getConnection().prepareStatement(sql.toString()).execute();
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException("将整个案卷中的年內文件保存入档案文件中出错！");
		}
	}

	/**
	 * author:zhangli 
	 * description: 删除对应案卷下的临时文件 
	 * modifyer: 
	 * description:
	 * 
	 * @param folderId
	 * @throws SQLException
	 */
	public void deleteTempFile(String folderId,OALogInfo ... loginfos) throws SystemException,ServiceException {
		try{
			List list = getFileByFolderId(folderId);
			for (int i = 0; i < list.size(); i++) {
			ToaArchiveTempfile tfile = (ToaArchiveTempfile) list.get(i);
				fileManager.deleteTempfile(tfile.getTempfileId());
			}
			
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"年内文件"});
		}
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-3下午03:45:46
	 * @desc：根据案卷id获取案卷的状态
	 * @param String
	 *            folderId 案卷id
	 * @return 案卷状态
	 */
	public String getFolderStatus(String folderId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		String status = "";
		try {
			String hql = "from ToaArchiveFolder t where t.folderId=?";
			List<ToaArchiveFolder> list = archiveDao.find(hql, folderId);
			if (list.size() > 0) {
				status = list.get(0).getFolderAuditing();
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"案卷状态"});
		}
		return status;
	}
	
	public ToaArchiveFileAppend getArchiveFileAppendByAppendId(String appendId,OALogInfo ... loginfos)throws SystemException,ServiceException{
		try {
			ToaArchiveFileAppend obj=new ToaArchiveFileAppend();
			obj=archiveAppendDao.get(appendId);
			return obj;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,               
 					new Object[] {"根据档案文件所属附件Id，查询附件出错"}); 
		}
	}
	
	/**
	 * @author：pengxq
	 * @time：2008-12-29下午06:14:38
	 * @desc：根据案卷id获取文件列表
	 * @param String
	 *            folderId 案卷id
	 * @return List 文件列表
	 */
	public List getFilesByFolderId(String folderId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		try{
			List list =fileManager.getFileByFolderId(folderId);
			return list;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"年内文件列表"});
		}
	}
}
