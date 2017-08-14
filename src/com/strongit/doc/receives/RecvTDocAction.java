package com.strongit.doc.receives;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.jspsmart.upload.Request;
import com.strongit.doc.bo.TdocSend;
import com.strongit.doc.bo.TtransDoc;
import com.strongit.doc.bo.TtransDocAttach;
import com.strongit.doc.sends.DocSendManager;
import com.strongit.doc.sends.TransDocManager;
import com.strongit.doc.sends.util.DESEncrypter;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 盖章日志管理Action.
 * 
 * @author
 * @company Strongit Ltd. (C) copyright
 * @date 下午04:52:02
 * @version 2.0.2.3
 * @classpath com.strongit.oa.bookmark.BookMarkAction
 * @comment
 * @email dengzc@strongit.com.cn
 */
@ParentPackage("default")
@Results( {
		@Result(name = BaseActionSupport.RELOAD, value = "recvTDoc.action", type = ServletActionRedirectResult.class),
		@Result(name = "view", value = "/WEB-INF/jsp/receives/archive/archiveDoc-view.jsp", type = ServletDispatcherResult.class),
		@Result(name = "view2", value = "/WEB-INF/jsp/receives/archive/archiveDoc-view2.jsp", type = ServletDispatcherResult.class),
		@Result(name = "viewAttach", value = "/WEB-INF/jsp/receives/archive/archiveDoc-viewAttach.jsp", type = ServletDispatcherResult.class) })
public class RecvTDocAction extends BaseActionSupport {

	TtransDoc model = new TtransDoc(); // 定义MODEL对象.

	@Autowired
	RecvTDocManager manager; // 注入服务类对象

	@Autowired
	RecvDocManager recvDocManager;
	@Autowired
	DocSendManager docSendManager;
	private String archiveId;
	private String docType; // 打开的OFFICE类型，参见千航控件说明：0: 没有文档； 100 =其他文档类型；
	// 1=word；2=Excel.Sheet或者 Excel.Chart ；
	// 3=PowerPoint.Show； 4= Visio.Drawing；
	// 5=MSProject.Project； 6= WPS Doc；
	// 7:Kingsoft Sheet
	private String fileFileName; // 附件名称
	private String archiveAttachId;
	private String forwardStr; // 控制跳转字符串
	private String showType;//

	@Autowired
	public RecvTDocAction() {
	}

	/**
	 * 标签分页列表.
	 */
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
	}

	/**
	 * 保存
	 */
	@Override
	public String save() throws Exception {
		return RELOAD;
	}

	public TtransDoc getModel() {
		return model;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		 String flag=manager.delDocAttachById(archiveAttachId);
			getResponse().setContentType("text/html");
			getResponse().getWriter().write(flag);
			return null;

	}

	@Override
	public String input() throws Exception {
		//archiveId，在本代码中表示的是公文主表ID(DocId) 
		
		model = manager.getDocById(archiveId);
//		if(model.getDocState()!=null&&model.getDocState().equals("0")){
//			model.setDocState("草拟");
//		}else if(model.getDocState()!=null&&model.getDocState().equals("1")){
//			model.setDocState("签章");
//		}else if(model.getDocState()!=null&&model.getDocState().equals("2")){
//			model.setDocState("分发");
//		}else if(model.getDocState()!=null&&model.getDocState().equals("3")){
//			model.setDocState("已发送");
//		}else if(model.getDocState()!=null&&model.getDocState().equals("4")){
//			model.setDocState("退回");
//		}else if(model.getDocState()!=null&&model.getDocState().equals("5")){
//			model.setDocState("回收");
//		}
		forwardStr="doc";
		 showType=getRequest().getParameter("showType");
		
		return "view";
	} 
	
	/**
	 * 再次分发
	 * @author xiaolj
	 * @desc 
	 * @date 下午2013-4-1 14:01:03
	 * @return
	 * @throws Exception
	 */
	public String input2() throws Exception {
		//archiveId，在本代码中表示的是公文主表ID(DocId) 
		model = manager.getDocById(archiveId);
		List<Object[]> objList = docSendManager.getHasSentList(archiveId);
		switch (objList.size()) {
		case 0:
			model.setDocSubmittoDepart("");
			model.setDocSubmittoDepart_id("");
			model.setDocCcDepart("");
			model.setDocCcDepart_id("");
			break;
		case 1:
			model.setDocSubmittoDepart(objList.get(0)[1].toString());
			model.setDocSubmittoDepart_id(objList.get(0)[2].toString());
			model.setDocCcDepart("");
			model.setDocCcDepart_id("");
			break;
		case 2:
			
			//modify by luosy 添加判断主送抄送
			if("1".equals(objList.get(0)[0])){
				model.setDocSubmittoDepart(objList.get(0)[1].toString());
				model.setDocSubmittoDepart_id(objList.get(0)[2].toString());
				model.setDocCcDepart(objList.get(1)[1].toString());
				model.setDocCcDepart_id(objList.get(1)[2].toString());
			}else{
				model.setDocSubmittoDepart(objList.get(1)[1].toString());
				model.setDocSubmittoDepart_id(objList.get(1)[2].toString());
				model.setDocCcDepart(objList.get(0)[1].toString());
				model.setDocCcDepart_id(objList.get(0)[2].toString());
				
			}
			break;
		}
		
		
		
		
//		List<TdocSend> list = manager.getAllInfoList(archiveId);
//		model.setDocCcDepart(null);
//		model.setDocCcDepart_id(null);
//		model.setDocSubmittoDepart(null);
//		model.setDocSubmittoDepart_id(null);
		
		forwardStr="doc";
		return "view2";
	} 
	
	/**
	 * 下载附件
	 * @author zhengzb
	 * @desc 
	 * 2010-8-19 上午09:01:00 ---李骏加入解密方法
	 * @return
	 * @throws Exception
	 */
	public String downloadAttachFile() throws Exception{
		HttpServletResponse response = super.getResponse();
		TtransDocAttach archiveAttach=manager.getDocAttachById(archiveAttachId);
		if(archiveAttach!=null&&archiveAttach.getAttachFilePath()!=null&&!"".equals(archiveAttach.getAttachFilePath())){
			response.reset();
			response.setContentType("application/x-download");         //windows
			response.addHeader("Content-Disposition", "attachment;filename=" +
					new String(archiveAttach.getAttachFileName().getBytes("gb2312"),"iso8859-1"));
			OutputStream output = null;
			try {
				String rootPath = getRequest().getSession().getServletContext().getRealPath(archiveAttach.getAttachFilePath());		//获取附件在服务器上的路径
				output = response.getOutputStream();
				File file = new File(rootPath);
				
				if(file.exists()){
//					byte[] attachContent = FileUtil.inputstream2ByteArray(new FileInputStream(file));								//读取附件内容
					InputStream is = new FileInputStream(file);
//					byte[] attachContent = FileUtil.inputstream2ByteArray(is);
					byte[] attachContent = null;
					byte[] b = new byte[8192];
					int read = 0;
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					while((read=is.read(b))!=-1){
						
						bos.write(b, 0, read);
					}
					attachContent = bos.toByteArray();
					
					output = response.getOutputStream();
					output.write(attachContent);
					output.flush();	 	    
					
				}else {
					rootPath = getRequest().getSession().getServletContext().getRealPath("/empty.doc");
					file = new File(rootPath);
					byte[] attachContent = FileUtil.inputstream2ByteArray(new FileInputStream(file));
					response.addHeader("Content-Disposition", "attachment;filename=" +
							new String(archiveAttach.getAttachFileName().getBytes("gb2312"),"iso8859-1"));
					output = response.getOutputStream();
					output.write(attachContent);
					output.close();
				}
				
			} catch (Exception e) {
				if(logger.isErrorEnabled()){
					logger.error(e.getMessage(),e);
				}
			}finally{				
				if(output != null){
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
						if(logger.isErrorEnabled()){
							logger.error(e.getMessage(),e);
						}
					}
					output = null;
				}
			}
			return null;
		}else{
			return renderHtml("<script>alert('当前档案文件正文为空或不存在,不能查看或下载！'); </script>");
		}
	}
	

	/**
	 * 获取附件类型，返回它的类型值
	 * @author zhengzb
	 * @desc 
	 * 2010-8-18 下午04:28:08 
	 * @return
	 * @throws Exception
	 */
	public String getArchiveFileExt()throws Exception{	
		getRequest().setAttribute("backlocation","javascript:history.back();");		
		 if(archiveId!=null&&!"".equals(archiveId)&&!"null".equals(archiveId)){
			 model=manager.getDocById(archiveId);										//档案文件
			 TtransDocAttach obj=null;											 //获取附件对象
			   if(archiveAttachId!=null&&!"".equals(archiveAttachId)){
				   obj=manager.getDocAttachById(archiveAttachId);
			   }else{
				   List<TtransDocAttach> list=(List<TtransDocAttach>)model.getTtransDocAttaches();
				   if(list.size()>0){
					   obj =list.get(0);
				   }
			   }
			 if(obj!=null){				 
				 fileFileName=obj.getAttachFileName();	//获取附件名称
				 String ext=fileFileName.substring(fileFileName.lastIndexOf(".")+1);//获取附件类型
				 renderText(ext);
			 }else {
				 renderText("");
			}
		 }else{
			 renderText("");
		 }
		 return null;
	}
	
	
	
	
	/**
	 * 兼容2003 - 2007 打开空文档。并加载数据
	 */
	public synchronized void openEmptyDocFromUrl() {
		TtransDoc tdoc = null;
		if (archiveId != null && !archiveId.equals("")) {
			tdoc = manager.getDocById(archiveId);
		}
		HttpServletResponse response = this.getResponse();
		response.reset();
		response.setContentType("application/octet-stream");
		OutputStream output = null;
		String type = ".doc";
		logger.info("文档类型：" + docType);
		try {
			if ("1".equals(docType)) {
				type = ".doc";
			} else if ("2".equals(docType)) {
				type = ".xls";
			} else if ("3".equals(docType)) {
				type = ".ppt";
			} else if ("4".equals(docType)) {
				type = ".vsd";
			} else if ("5".equals(docType)) {
				type = ".mpp";
			} else if ("6".equals(docType)) {
				type = ".wps";
			}
			String rootPath = getRequest().getSession().getServletContext()
					.getRealPath("/empty" + type);
			output = response.getOutputStream();
			File file = new File(rootPath);
			if (!file.exists()) {
				rootPath = getRequest().getSession().getServletContext()
						.getRealPath("/empty.doc");
				file = new File(rootPath);
			}
			logger.info("打开文档:" + rootPath);
			if (tdoc != null && tdoc.getDocContent().length > 0) {
				output.write(tdoc.getDocContent()); // 加载公文正文
			} else {
				byte[] buf = FileUtil
						.inputstream2ByteArray(new FileInputStream(file));
				output.write(buf);
			}
		} catch (Exception e) {
			logger.error("打开文档出错了。", e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}

	/**
	 * 获取附件类型，返回它的类型值
	 * 
	 * @author zhengzb
	 * @desc 2010-8-18 下午04:28:08
	 * @return
	 * @throws Exception
	 */
	public String getDocByIdExt() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back();");
		if (archiveId != null && !"".equals(archiveId)
				&& !"null".equals(archiveId)) {
			model = manager.getDocById(archiveId);
			TtransDocAttach obj = null; // 获取附件对象
			if (archiveAttachId != null && !"".equals(archiveAttachId)) {
				obj = manager.getDocAttachById(archiveAttachId);
			} else {
				List<TtransDocAttach> list = (List<TtransDocAttach>) model
						.getTtransDocAttaches();
				if (list.size() > 0) {
					obj = list.get(0);
				}
			}
			if (obj != null) {
				fileFileName = obj.getAttachFileName(); // 获取附件名称
				String ext = fileFileName.substring(fileFileName
						.lastIndexOf(".") + 1);// 获取附件类型
				renderText(ext);
			} else {
				renderText("");
			}
		} else {
			renderText("");
		}
		return null;
	}

	public String readAnnex() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back();");
		String forward = "viewAttach";
		try {
			if (archiveId != null && !"".equals(archiveId)
					&& !"null".equals(archiveId)) {
				model = manager.getDocById(archiveId);
				TtransDocAttach obj = null; // 获取附件对象
				if (archiveAttachId != null && !"".equals(archiveAttachId)) {
					obj = manager.getDocAttachById(archiveAttachId);
				} else {
					List<TtransDocAttach> list = (List<TtransDocAttach>) model
							.getTtransDocAttaches();
					if (list.size() > 0) {
						obj = list.get(0);
					}
				}
				if (obj != null) {
					fileFileName = obj.getAttachFileName(); // 获取附件名称
					fileFileName = fileFileName.substring(0, fileFileName
							.lastIndexOf(".")); // 附件名称去掉后缀
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		forwardStr="doc";
		return forward;
	}

	/*
	 * 查看附件时 读取附件内容
	 */
	public String openAttach() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:history.back();");
		try {
			if (archiveId != null && !"".equals(archiveId)
					&& !"null".equals(archiveId)) {
				model = manager.getDocById(archiveId);
				TtransDocAttach obj = null; // 获取附件对象
				if (archiveAttachId != null && !"".equals(archiveAttachId)) {
					obj = manager.getDocAttachById(archiveAttachId);
				} else {
					List<TtransDocAttach> list = (List<TtransDocAttach>) model
							.getTtransDocAttaches();
					if (list.size() > 0) {
						obj = list.get(0);
					}
				}
				HttpServletResponse response = this.getResponse();
				response.reset();
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + obj.getAttachFileName());
				OutputStream output = null;
				String rootPath = getRequest().getSession().getServletContext()
						.getRealPath(obj.getAttachFilePath());
				output = response.getOutputStream();
				File file = new File(rootPath);
				byte[] attachContent = FileUtil
						.inputstream2ByteArray(new FileInputStream(file));
				try {
					output = response.getOutputStream();
					output.write(attachContent);
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public RecvTDocManager getManager() {
		return manager;
	}

	public void setManager(RecvTDocManager manager) {
		this.manager = manager;
	}

	public void setModel(TtransDoc model) {
		this.model = model;
	}

	public String getArchiveId() {
		return archiveId;
	}

	public void setArchiveId(String archiveId) {
		this.archiveId = archiveId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getArchiveAttachId() {
		return archiveAttachId;
	}

	public void setArchiveAttachId(String archiveAttachId) {
		this.archiveAttachId = archiveAttachId;
	}

	public String getForwardStr() {
		return forwardStr;
	}

	public void setForwardStr(String forwardStr) {
		this.forwardStr = forwardStr;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}
	

}
