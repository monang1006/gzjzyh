/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: zhangli
 * Version: V1.0
 * Description： 档案文件管理ACTION
 */
package com.strongit.oa.archive.archivefile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.strongit.oa.bo.ToaArchiveFile;
import com.strongit.oa.bo.ToaArchiveFileAppend;
import com.strongit.oa.util.OALogInfo;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "achiveFile.action", type = ServletActionRedirectResult.class) })
public class AchiveFileAction extends BaseActionSupport{
	/** 分页对象*/
	private Page page = new Page<ToaArchiveFile>(FlexTableTag.MAX_ROWS, true);

	/** 档案案卷编号*/
	private String folderId;

	/** 档案文件编号*/
	private String fileId;
	
	/** 档案文件名称*/
	private String fileName;

	/** 跳转页面名称*/
	private String forwardStr;

	/** 档案文件列表*/
	private List fileList;

	/** 档案文件内容*/
	private String content;

	/** 档案文件对象*/
	private ToaArchiveFile model = new ToaArchiveFile();

	/** 档案文件Manager*/
	private ArchiveFileManager manager;
    /** 附件ID*/
	private String appendId;
	/**
	 * @roseuid 4959BDE503A9
	 */
	public AchiveFileAction() {

	}

	/**
	 * Access method for the page property.
	 * 
	 * @return the current value of the page property
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * Access method for the folderId property.
	 * 
	 * @return the current value of the folderId property
	 */
	public java.lang.String getFolderId() {
		return folderId;
	}

	/**
	 * Sets the value of the folderId property.
	 * 
	 * @param aFolderId
	 *            the new value of the folderId property
	 */
	public void setFolderId(java.lang.String aFolderId) {
		folderId = aFolderId;
	}

	/**
	 * Access method for the forwardStr property.
	 * 
	 * @return the current value of the forwardStr property
	 */
	public java.lang.String getForwardStr() {
		return forwardStr;
	}

	/**
	 * Sets the value of the forwardStr property.
	 * 
	 * @param aForwardStr
	 *            the new value of the forwardStr property
	 */
	public void setForwardStr(java.lang.String aForwardStr) {
		forwardStr = aForwardStr;
	}


	/**
	 * Access method for the tempFileList property.
	 * 
	 * @return the current value of the tempFileList property
	 */
	public java.util.List getFileList() {
		return fileList;
	}

	/**
	 * Access method for the content property.
	 * 
	 * @return the current value of the content property
	 */
	public java.lang.String getContent() {
		return content;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaArchiveFile getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(ArchiveFileManager aManager) {
		manager = aManager;
	}

	/**
	 * 获取档案文件列表
	 * 
	 * @return java.lang.String
	 * @roseuid 4959BF2001F4
	 */
	@Override
	public String list() {
		try {
			fileList = manager.getAllFile(folderId, model); // 根据查询条件查询文件
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer returnhtml = new StringBuffer(
				"<script> var scriptroot = '")
				.append(getRequest().getContextPath())
				.append("'</script>")
				.append("<SCRIPT src='")
				.append(getRequest().getContextPath())
				.append(
						"/common/frame/perspective_content/actions_container/js/workservice.js'>")
				.append("</SCRIPT>")
				.append("<SCRIPT src='")
				.append(getRequest().getContextPath())
				.append(
						"/common/frame/perspective_content/actions_container/js/service.js'>")
				.append("</SCRIPT>")
				.append("<script>")
				.append(" location=")
				.append(getRequest().getContextPath())
				.append(
						"/archive/archivefolder/archiveFolder!input.action?forward=searchborrowFile&folderId=")
				.append(folderId).append(";</script>");
		return renderHtml(returnhtml.toString());
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4959BF200222
	 */
	@Override
	public String save() {
		return null;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4959BF200280
	 */
	@Override
	public String delete() {
		return null;
	}

	/**
	 * @roseuid 4959BF2002BF
	 */
	@Override
	protected void prepareModel() {

	}

	/**
	 * author:zhangli 
	 * description:下载档案文件内容 
	 * modifyer: 
	 * description:
	 * @return
	 */
	public String download() {
		HttpServletResponse response = super.getResponse();
		   ToaArchiveFileAppend file = manager.getFileAppendByFile(fileId);
			response.reset();
			response.setContentType("application/x-download");         //windows
			OutputStream output = null;
			try{
				response.addHeader("Content-Disposition", "attachment;filename=" +
				         new String((file.getAppendName()).getBytes("gb2312"),"iso8859-1"));
			    output = response.getOutputStream();
			    output.write(file.getAppendContent());
			    output.flush();
			} catch(Exception e) {
			    e.printStackTrace();
			} finally {		 	    
			    if(output != null){
			      try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			      output = null;
			    }
			}
			return null;
	}
	/**
	 * author:zhangli 
	 * description:下载档案文件内容 
	 * modifyer: 
	 * description:
	 * @return
	 */
	public String downloadAppend() {
		HttpServletResponse response = super.getResponse();
		   ToaArchiveFileAppend file = manager.getFileAppendById(appendId);
			response.reset();
			response.setContentType("application/x-download");         //windows
			OutputStream output = null;
			try{
				response.addHeader("Content-Disposition", "attachment;filename=" +
				         new String((file.getAppendName()).getBytes("gb2312"),"iso8859-1"));
			    output = response.getOutputStream();
			    output.write(file.getAppendContent());
			    output.flush();
			} catch(Exception e) {
			    e.printStackTrace();
			} finally {		 	    
			    if(output != null){
			      try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			      output = null;
			    }
			}
			return null;
	}
	/**
     * @author：pengxq
     * @time：2009-1-5下午01:50:25
     * @desc: 判断附件类型，并读取附件名称
     * @param 
     * @return String
    */
   public String readAnnex(){  
	   String forward="annex";
	   try{
		   if(fileId!=null&&!"".equals(fileId)&&!"null".equals(fileId)){
			    model=manager.getFile(fileId);	//获取年内文件对象
			    ToaArchiveFileAppend obj=null;
			    if(appendId!=""){
			    	 obj=manager.getFileAppendById(appendId); //获取附件对象
			    }
			    else{
			    	
			    	 obj=manager.getFileAppendByFile(fileId); //获取附件对象
			    }
			   	fileName=obj.getAppendName();	//获取附件名称
			   	String ext = null;
			   	if(fileName!=null&&fileName.length()>0){
			    	ext=fileName.substring(fileName.lastIndexOf(".")+1);
			    	fileName = fileName.substring(0,fileName.lastIndexOf("."));
			   	}
			    if(ext!=null&&!"doc".equals(ext)){		//不是word文档
			    	addActionMessage("该文档类型为"+ext+"，只能查看word文档，请下载查看！");//添加提示信息
			    	StringBuffer returnhtml = new StringBuffer("<script> var scriptroot = '").append(getRequest().getContextPath()).append("'</script>")
					   .append("<SCRIPT src='")
					   .append(getRequest().getContextPath())
					   .append("/common/frame/perspective_content/actions_container/js/workservice.js'>")
					   .append("</SCRIPT>")
					   .append("<SCRIPT src='")
					   .append(getRequest().getContextPath())
					   .append("/common/frame/perspective_content/actions_container/js/service.js'>")
					   .append("</SCRIPT>")
					   .append("<script>")
					   .append("alert('")
					   .append(getActionMessages().toString())
					   .append("');window.close();</script>");
					    forward=renderHtml(returnhtml.toString());
			    }
		   }  
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   return forward;
	}
   /**
    * @author：pengxq
    * @time：2009-1-5下午01:50:25
    * @desc: 判断附件类型，并读取附件名称
    * @param 
    * @return String
   */
  public String readAnnexByID(){  
	   String forward="annex";
	   try{
		   if(fileId!=null&&!"".equals(fileId)&&!"null".equals(fileId)){
			    model=manager.getFile(fileId);	//获取年内文件对象
			   	ToaArchiveFileAppend obj=manager.getFileAppendById(appendId); //获取附件对象
			   	fileName=obj.getAppendName();	//获取附件名称
			   	String ext = null;
			   	if(fileName!=null&&fileName.length()>0){
			    	ext=fileName.substring(fileName.lastIndexOf(".")+1);
			    	fileName = fileName.substring(0,fileName.lastIndexOf("."));
			   	}
			    if(ext!=null&&!"doc".equals(ext)){		//不是word文档
			    	addActionMessage("该文档类型为"+ext+"，只能查看word文档，请下载查看！");//添加提示信息
			    	StringBuffer returnhtml = new StringBuffer("<script> var scriptroot = '").append(getRequest().getContextPath()).append("'</script>")
					   .append("<SCRIPT src='")
					   .append(getRequest().getContextPath())
					   .append("/common/frame/perspective_content/actions_container/js/workservice.js'>")
					   .append("</SCRIPT>")
					   .append("<SCRIPT src='")
					   .append(getRequest().getContextPath())
					   .append("/common/frame/perspective_content/actions_container/js/service.js'>")
					   .append("</SCRIPT>")
					   .append("<script>")
					   .append("alert('")
					   .append(getActionMessages().toString())
					   .append("');window.close();</script>");
					    forward=renderHtml(returnhtml.toString());
			    }
		   }  
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   return forward;
	}
   
   
  /**
   * @author：pengxq
   * @time：2009-1-5下午01:53:55
   * @desc: 读取附件内容
   * @param
   * @return String
  */
	public String openAnnex() {
		try{
			HttpServletResponse response = this.getResponse();
			manager.setContentToHttpResponse(response, appendId);	//获取附件对象，并读取附件内容
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAppendId() {
		return appendId;
	}

	public void setAppendId(String appendId) {
		this.appendId = appendId;
	}
}
