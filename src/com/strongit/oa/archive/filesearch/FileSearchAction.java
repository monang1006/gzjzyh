/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-23
 * 
 * Version: V1.0
 * Description： 搜索ACTION
 */
package com.strongit.oa.archive.filesearch;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.StreamResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.archive.archivefile.ArchiveFileAppendManager;
import com.strongit.oa.archive.archivefile.ArchiveFileManager;
import com.strongit.oa.archive.archivefolder.ArchiveFolderAction;
import com.strongit.oa.archive.tempfile.AnnexManager;
import com.strongit.oa.archive.tempfile.TempFileManager;
import com.strongit.oa.archive.tempfile.TempFileType;
import com.strongit.oa.archive.tempfile.TempfileTree;
import com.strongit.oa.bo.ToaArchiveFile;
import com.strongit.oa.bo.ToaArchiveFolder;
import com.strongit.oa.bo.ToaArchiveTempfile;
import com.strongit.oa.common.user.model.User;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
@Results( { @Result(name = FileSearchAction.SEARCH_TEMP_EXCEL, value = "stream", type = StreamResult.class, params = {
    "contentType", "application/vnd.ms-excel", "inputName", "stream",
    "contentDisposition", "attachment;filename=\"archiveFolder.xls\"",
    "bufferSize", "1024" }),@Result(name = FileSearchAction.SEARCH_FILE_EXCEL, value = "stream", type = StreamResult.class, params = {
        "contentType", "application/vnd.ms-excel", "inputName", "stream",
        "contentDisposition", "attachment;filename=\"archiveFile.xls\"",
        "bufferSize", "1024" })  })
public class FileSearchAction extends BaseActionSupport{
   //保管期限
    private String tempfileDeadline;
    //jiema
    private String jiema;
   //文件编码
	private String fileNo;
	//标题
	private String fileTitle;
	//作者
	private String fileAuthor;
	//创建时间
	private String fileDate;
	//案卷名称
	private String fileFolder;
	//页数
	private String filepage;
	//文件类型
	private String fileType;
	//所属部门
	private String orgId;
	//搜索
	private String disLogo;
	//分组字段
	private String groupType;
	//树结构值
	private String treeValue;

	private List<ToaArchiveFile> fileList;
	private List<ToaArchiveTempfile> tempList;
	private Page<ToaArchiveTempfile> tempPage=new Page<ToaArchiveTempfile>(FlexTableTag.MAX_ROWS,true);
	private Page<ToaArchiveFile> page=new Page<ToaArchiveFile>(FlexTableTag.MAX_ROWS,true);
	private TempFileManager tempManager;//未归档文件manager
	private ArchiveFileManager fileManager;
	private List<String> yearList;
	//文件年份
	private String year;
	private List<String> monthList;
	//文件发文月份
	private String month;
	//年份显示树
	private List<SearchTree> treeList;
	private static final String ONE="1";
	private static final String TWO="2";
	@Autowired ArchiveFileAppendManager appendManager;
	@Autowired AnnexManager annexManger;
	/**
     * 导出Excel数据流.
     */
    private InputStream stream; 
    public static final String SEARCH_TEMP_EXCEL = "searchTempExcel";
    public static final String SEARCH_FILE_EXCEL = "searchFileExcel";
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/**
     * 导出为excel
     * @author xush
     * @date 6/8/2013 1:50 PM
     */
    public String exportExcel()throws Exception{
        OutputStream outStream = null;
        String title="";
         try {
                        //是否资料中心文件
         if(disLogo!=null&&"tempfile".equals(disLogo)){
            ToaArchiveTempfile file=wintempfile();
            //导出Excel数据流.
            stream = tempManager.searchExcel(file,disLogo,"",fileFolder);
                        //getTempFiles();//查询文件列表
            //return SEARCH_TEMP_EXCEL;
            title="资料中心--未归档文件列表.xls";
            
         }else{
            //已归档文件
            ToaArchiveFile file=winfile();
           //导出Excel数据流.
            stream = fileManager.searchExcel(file,disLogo,"",fileFolder);
            //return SEARCH_FILE_EXCEL;   
            title="已归档文件列表.xls";
            }
         byte[] buffer = new byte[1024]; 
         int bytesRead;
         outStream = super.getResponse().getOutputStream();
         //设置文件类型
         super.getResponse().setContentType(ServletUtils.EXCEL_TYPE);
         
         //设置下载弹出对话框
         ServletUtils.setFileDownloadHeader(super.getRequest(),super.getResponse(), title);
         while ((bytesRead = stream.read(buffer)) != -1){
            outStream.write(buffer, 0, bytesRead);
         }
         } catch (RuntimeException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
          }finally{
              if(outStream != null){
                  outStream.close();
              }
          }
          return null;
           
            }
    /**
     * 搜索后展现的列表
     * 
     * @date 6/8/2013 1:50 PM
     */
	@Override
	public String list() throws Exception {
		if(disLogo!=null&&"tempfile".equals(disLogo)){//是否档案中心文件
			if(groupType!=null&&!"".equals(groupType)){//是否分组
				jiami();//加密
				return "tempContent";
			}else{
				getTempFiles();//查询文件列表
				return "tempfile";
			}
		}else{
		    //已归档文件
		  //是否分组
			if(groupType!=null&&!"".equals(groupType)){
			  //加密
				jiami();
				return "fileContent";
			}else{
			  //查询文件列表
				getFiles();
				return "file";
			}
		}
	}
  /**
	 * 查询文件
	 * @return
	 * @throws Exception
	 */
	public String filelist()throws Exception{
	  //解码
		zhuanma();
		//是否档案中心文件
		if(disLogo!=null&&"tempfile".equals(disLogo)){
			getTempFiles();
			return "tempfile";
		}else{
		    //已归档文件
			getFiles();
			return "file";
		}
	}

	/**
	 * 获取档案中心文件列表
	 * @author 胡丽丽
	 */
	private void getTempFiles()throws Exception{
	  //加密
		//zhuanma();
		ToaArchiveTempfile file=wintempfile();
		tempPage= tempManager.search(tempPage, file, "","",fileFolder);
		List<ToaArchiveTempfile> list=tempPage.getResult();
		if(list!=null){
			//查看末归档文件附件个数
			getappendsize(list);
		}
//		tempPage.setResult(list);
	}

	/**
	 * 获取已归档文件列表
	 * @author 胡丽丽
	 * @date 2010-01-08
	 */
	private void getFiles()throws Exception{
		//zhuanma();//转码
		ToaArchiveFile file=winfile();
		page=fileManager.search(page, file,fileFolder);//获取列表
		List<ToaArchiveFile> list=page.getResult();
		try {
			if(list!=null){
				//获取附件个数
				getfileappendsize(list);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 显示分组树
	 * @return
	 * @throws Exception
	 */

	public String temptree() throws Exception{
		zhuanma();//解码
		ToaArchiveTempfile file=wintempfile();
		treeList=new ArrayList<SearchTree>();
		int count=0;
		if(ONE.equals(groupType)){
			List<String> typeList=null;
			//是否档案中心文件
			if(disLogo!=null&&"tempfile".equals(disLogo)){
				typeList=tempManager.getTempfileType(file);
				count=tempManager.gettempfileCountByType(groupType,file);
			}else{//已归档文件
				typeList=fileManager.getFileType(file);
				count=fileManager.getFileCountByType(groupType, file);
			}
			try {
				jiami();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(String type:typeList){
				if(type!=null){
					SearchTree tree=getsearchtree();
					tree.setTreeid(type);
					tree.setTreetype(ONE);
					if(TempFileType.MEETING.equals(type)){
						tree.setTreeName("会议纪要文件");
					}else if(TempFileType.RECVDOC.equals(type)){
						tree.setTreeName("收文文件");
					}else if(TempFileType.SENDDOC.equals(type)){
						tree.setTreeName("发文文件");
					}
					treeList.add(tree);
				}
			}
			if(count>0){
				SearchTree tree1=getsearchtree();
				tree1.setTreeid("0");
				tree1.setTreetype(ONE);
				tree1.setTreeName("手录文件");
				treeList.add(tree1);
			}
			return "typetree";
		}else if(TWO.equals(groupType)){
			List<Object> typeList=null;
			if(disLogo!=null&&"tempfile".equals(disLogo)){
				typeList=tempManager.getTempfileFolder(file);
				count=tempManager.gettempfileCountByType(groupType,file);
			}else{
				typeList=fileManager.getFileFolder(file);
				count=fileManager.getFileCountByType(groupType, file);
			}
			try {
				jiami();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(Object type:typeList){
				if(type!=null){
					Object[] obj= (Object[])type;
					SearchTree tree=getsearchtree();
					tree.setTreeid(obj[0].toString());
					tree.setTreetype(TWO);
					tree.setTreeName(obj[1].toString());
					treeList.add(tree);
				}
			}

			if(count>0){
				SearchTree tree1=getsearchtree();
				tree1.setTreeid("0");
				tree1.setTreetype(TWO);
				tree1.setTreeName("未入卷文件");
				treeList.add(tree1);
			}
			return "foldertree";
		}else{
			yearList=new ArrayList<String>();
			if(disLogo!=null&&"tempfile".equals(disLogo)){
				yearList=tempManager.getTempfileYear(file);
			}else{
				yearList=fileManager.getFileYear(file);
			}
			try {
				jiami();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(String ye:yearList){
				if(ye!=null){
					SearchTree tree=getsearchtree();
					tree.setTreeid(ye);
					tree.setTreeName("年");
					tree.setTreetype("4");
					treeList.add(tree);
					if(disLogo!=null&&"tempfile".equals(disLogo)){
						monthList=tempManager.getTempfileMonth(ye,file);
					}else{
						monthList=fileManager.getFileMonth(ye,file);
					}
					if(monthList!=null){
						for(String mon:monthList){
							SearchTree montree=getsearchtree();
							montree.setTreeid(mon);
							montree.setParentId(ye);
							montree.setTreeName("月份");
							montree.setTreetype("4");
							treeList.add(montree);
						}
					}
				}

			}
			return "tree";
		}
	}
	/**
	 * 得到一个年内文件对象
	 * @return
	 */
	private ToaArchiveTempfile wintempfile() throws Exception {
		ToaArchiveTempfile file=new ToaArchiveTempfile();
		if(!"null".equals(fileAuthor)){
			file.setTempfileAuthor(fileAuthor);
		}
//		if(!"null".equals(year)){
//			
//			file.setTempfileDate(stringToDate(fileDate));
//		}
		if(!"null".equals(year)){
			
			file.setTempfileYear(year);
		}
		if(!"null".equals(month)){
			file.setTempfileMonth(month);
		}
		if(!"null".equals(orgId)){
			file.setTempfileDepartment(orgId);
		}
		if(!"null".equals(fileType)){
			file.setTempfileDocType(fileType);
		}
		
		  if(!"null".equals(fileNo)){
		      if("notjie".equals(jiema)){
		          file.setTempfileNo(fileNo);
		      }else{
			  file.setTempfileNo(URLDecoder.decode(fileNo,"UTF-8"));
			  }
		}
		if(filepage!=null&&!"".equals(filepage)&&!"null".equals(filepage)){
			file.setTempfilePage(Long.parseLong(filepage));
		}
		if(!"null".equals(fileTitle)){
		    if("notjie".equals(jiema)){
		        file.setTempfileTitle(fileTitle);
		    }else{
		        file.setTempfileTitle(URLDecoder.decode(fileTitle,"UTF-8"));
		        }
			
		}
		if(!"null".equals(tempfileDeadline)){
		    if("notjie".equals(jiema)){
		        file.setTempfileDeadline(tempfileDeadline);
		    }else{
                file.setTempfileDeadline(URLDecoder.decode(tempfileDeadline,"UTF-8"));
                }
        }
		ToaArchiveFolder folder=new ToaArchiveFolder();
		//if(!"null".equals(fileFolder)){
			//folder.setFolderId(fileFolder);
		//}

		if(ONE.equals(groupType)){//是否文件类型分组
			if(treeValue!=null&&!"".equals(treeValue))
				file.setTempfileDocType(treeValue);
		}else if(TWO.equals(groupType)){//是否案卷分组
			if(treeValue!=null&&!"".equals(treeValue))
				folder.setFolderId(treeValue);
		}else{//年份分组
			if(treeValue!=null&&!"".equals(treeValue)){
				String[] st=treeValue.split(",");
				if(st!=null&&st.length>0){
					file.setTempfileYear(st[0]);
					if(st.length>1){
						file.setTempfileMonth(st[1]);
					}
				}
			}
		}
		file.setToaArchiveFolder(folder);
		return file;
	}
	/**
	 * 转换日期格式
	 * @param date
	 * @return
	 */
	private  Date stringToDate(String date){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		try {
			if(date==null||"".equals(date)){
				return null;
			}else{
				Date time = format.parse(date);
				return time;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	/**
	 * 组装已归档查询条件
	 * @return
	 */
	private ToaArchiveFile winfile() throws Exception{
		ToaArchiveFile file=new ToaArchiveFile();
		if(!"null".equals(fileAuthor)){
			file.setFileAuthor(fileAuthor);
		}
		if(!"null".equals(fileDate)){
			file.setFileDate(stringToDate(fileDate));
		}
		if(!"null".equals(orgId)){
			file.setFileDepartment(orgId);
		}
		if(!"null".equals(fileType)){
			file.setFileDocType(fileType);
		}
		 if(!"null".equals(fileNo)){
             if("notjie".equals(jiema)){
                 file.setFileNo(fileNo);
             }else{
             file.setFileNo(URLDecoder.decode(fileNo,"UTF-8"));
             }
       }
		 
       if(filepage!=null&&!"".equals(filepage)&&!"null".equals(filepage)){
           file.setFilePage(Long.parseLong(filepage));
       }
       if(!"null".equals(fileTitle)){
           if("notjie".equals(jiema)){
               file.setFileTitle(fileTitle);
           }else{
               file.setFileTitle(URLDecoder.decode(fileTitle,"UTF-8"));
               }
           
       }
       if(!"null".equals(tempfileDeadline)){
           if("notjie".equals(jiema)){
               file.setFileDeadline(tempfileDeadline);
           }else{
               file.setFileDeadline(URLDecoder.decode(tempfileDeadline,"UTF-8"));
               }
       }
		ToaArchiveFolder folder=new ToaArchiveFolder();
		//if(!"null".equals(fileFolder)){
			//folder.setFolderId(fileFolder);
		//}
		if(ONE.equals(groupType)){//是否文件类型分组
			if(treeValue!=null&&!"".equals(treeValue))
				file.setFileDocType(treeValue);
		}else if(TWO.equals(groupType)){//是否案卷分组
			if(treeValue!=null&&!"".equals(treeValue))
				folder.setFolderId(treeValue);
		}else{//年份分组
			if(treeValue!=null&&!"".equals(treeValue)){
				String[] st=treeValue.split(",");
				if(st!=null&&st.length>0){
					file.setFileYear(st[0]);
					if(st.length>1){
						file.setFileMonth(st[1]);
					}
				}
			}
		}
		file.setToaArchiveFolder(folder);
		return file;
	}
	/**
	 * 构成节点基本属性
	 * @return
	 */
	private SearchTree getsearchtree(){
		SearchTree tree=new SearchTree();
		tree.setFileNo(fileNo);
		tree.setFileAuthor(fileAuthor);
		tree.setFileDate(fileDate);
		tree.setFileFolder(fileFolder);
		if(filepage!=null&&!"".equals(filepage)){
			tree.setFilepage(filepage);
		}
		tree.setFileTitle(fileTitle);
		tree.setOrgId(orgId);
		tree.setDisLogo(disLogo);
		return tree;
	}
	/**
	 * 解密
	 * @throws Exception
	 */
	private void zhuanma()throws Exception{      
		if(fileNo!=null)
			fileNo = URLDecoder.decode(fileNo , "UTF-8");
		if(fileTitle!=null)
			fileTitle=URLDecoder.decode(fileTitle , "UTF-8");
		if(fileAuthor!=null)
			fileAuthor=URLDecoder.decode(fileAuthor , "UTF-8");
		if(fileFolder!=null)
			fileFolder=URLDecoder.decode(fileFolder , "UTF-8");
		if(filepage!=null)
			filepage=URLDecoder.decode(filepage , "UTF-8");
		if(fileType!=null)
			fileType=URLDecoder.decode(fileType , "UTF-8");
		if(orgId!=null)
			orgId=URLDecoder.decode(orgId , "UTF-8");
		if(year!=null)
			year=URLDecoder.decode(year,"UTF-8");
		if(month!=null)
			month=URLDecoder.decode(month, "UTF-8");
		
	}
	/**
	 * 加密
	 * @throws Exception
	 */
	private void jiami()throws Exception{
		if(fileNo!=null)
			fileNo = URLEncoder.encode(fileNo , "UTF-8");
		if(fileTitle!=null)
			fileTitle=URLEncoder.encode(fileTitle , "UTF-8");
		if(fileAuthor!=null)
			fileAuthor=URLEncoder.encode(fileAuthor , "UTF-8");
		if(fileFolder!=null)
			fileFolder=URLEncoder.encode(fileFolder , "UTF-8");
		if(filepage!=null)
			filepage=URLEncoder.encode(filepage , "UTF-8");
		if(fileType!=null)
			fileType=URLEncoder.encode(fileType , "UTF-8");
		if(orgId!=null)
			orgId=URLEncoder.encode(orgId , "UTF-8");
	}
	/**
	 * 获取未归档文件附件数量
	 * @param list
	 */
	public void getappendsize(List list){
		if(list!=null){
			Map<String,Integer> map = annexManger.getCount();
			for(Object temp:list){
				ToaArchiveTempfile tf=(ToaArchiveTempfile)temp;
				tf.setAppendsize(map.get(tf.getTempfileId())==null?0:map.get(tf.getTempfileId()));
				//tf.setAppendsize(tf.getToaArchiveTfileAppends().size());
			}
		}
	}
	/**
	 * 获取已归档文件附件数量
	 * @param list
	 */
	public void getfileappendsize(List list){
		if(list!=null){
			Map<String,Integer> map = appendManager.getCount();
			for(Object temp:list){
				ToaArchiveFile tf=(ToaArchiveFile)temp;
				tf.setAppendsize(map.get(tf.getFileId())==null?0:map.get(tf.getFileId()));
			}
		}
	}
	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getModel() {	
		// TODO Auto-generated method stub
		return null;
	}

	public String getFilepage() {
		return filepage;
	}

	public void setFilepage(String filepage) {
		this.filepage = filepage;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Page<ToaArchiveTempfile> getTempPage() {
		return tempPage;
	}

	public void setTempPage(Page<ToaArchiveTempfile> tempPage) {
		this.tempPage = tempPage;
	}

	public Page<ToaArchiveFile> getPage() {
		return page;
	}

	public void setPage(Page<ToaArchiveFile> page) {
		this.page = page;
	}

	public String getTreeValue() {
		return treeValue;
	}

	public void setTreeValue(String treeValue) {
		this.treeValue = treeValue;
	}
	public List<String> getYearList() {
		return yearList;
	}

	public void setYearList(List<String> yearList) {
		this.yearList = yearList;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List<SearchTree> getTreeList() {
		return treeList;
	}

	public void setTreeList(List<SearchTree> treeList) {
		this.treeList = treeList;
	}

	@Autowired
	public void setFileManager(ArchiveFileManager fileManager) {
		this.fileManager = fileManager;
	}

	@Autowired
	public void setTempManager(TempFileManager tempManager) {
		this.tempManager = tempManager;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	public String getFileAuthor() {
		return fileAuthor;
	}

	public void setFileAuthor(String fileAuthor) {
		this.fileAuthor = fileAuthor;
	}

	public String getFileDate() {
		return fileDate;
	}

	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}

	public String getFileFolder() {
		return fileFolder;
	}

	public void setFileFolder(String fileFolder) {
		this.fileFolder = fileFolder;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDisLogo() {
		return disLogo;
	}

	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public List<ToaArchiveFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<ToaArchiveFile> fileList) {
		this.fileList = fileList;
	}

	public List<ToaArchiveTempfile> getTempList() {
		return tempList;
	}

	public void setTempList(List<ToaArchiveTempfile> tempList) {
		this.tempList = tempList;
	}

    public String getTempfileDeadline() {
        return tempfileDeadline;
    }

    public void setTempfileDeadline(String tempfileDeadline) {
        this.tempfileDeadline = tempfileDeadline;
    }

    public InputStream getStream() {
        return stream;
    }

    public String getJiema() {
        return jiema;
    }

    public void setJiema(String jiema) {
        this.jiema = jiema;
    }

}
