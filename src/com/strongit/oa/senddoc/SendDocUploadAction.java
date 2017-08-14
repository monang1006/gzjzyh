/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK:1.5.0_14; Struts:2.1.2; Spring:2.5.6; Hibernate:3.3.1.GA
 * Create Date: 2008-12-25
 * Autour: dengwenqiang
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.senddoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.rmi.ServerException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.attachment.AttachmentHelper;
import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaSenddoc;
import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.senddoc.pojo.ProcessedParameter;
import com.strongit.oa.util.PathUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 保存领导批示意见 Action Action
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Dec 21, 2011 6:36:56 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.SendDocUploadAction
 */
@ParentPackage("default")
@Results({ @Result(name = BaseActionSupport.RELOAD, value = "sendDoc.action", type = ServletActionRedirectResult.class) })
public class SendDocUploadAction extends AbstractBaseWorkflowAction<ToaSenddoc> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4374277362794227113L;

	@Autowired
	SendDocUploadManager sendDocUploadManager;
	@Autowired
	SendDocManager sendDocManager;

//	公共附件接口
	@Autowired
	private IAttachmentService attachmentService;

	private Page page = new Page(FlexTableTag.MAX_ROWS, false);

	private String attachName; // 扫描附件

	private String doneSuggestion; // 办结意见

	private File upload;

	//扫描附件(多个)
	private File[] uploads;
	
	private String[] uploadsFileName;
	
	private String processIds;

	private String privilName;
	
	private String isPermitUploadPDF;

	private String pdfPath;
	
	private String smjPath;
	
	private String isPermitUploadSMJ;

	public String getPdfPath() {
		return pdfPath;
	}

	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}

	public String getisPermitUploadPDF() {
		return isPermitUploadPDF;
	}

	public void setisPermitUploadPDF(String isPermitUploadPDF) {
		this.isPermitUploadPDF = isPermitUploadPDF;
	}

	@Override
	protected String getDictType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseManager getManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getModuleType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWorkflowType() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @description 跳转到上传领导意见的页面
	 * @author 严建
	 * @createTime Dec 20, 2011 3:42:57 PM
	 * @return String
	 * @throws Exception
	 */
	public String upload() throws Exception {
		if (bussinessId == null || "".equals(bussinessId)) {
			throw new ServerException("上传数据失败！b业务数据的ussinessId不能为空...");
		} else {
			String fliedNames = "BANJIEYIJIAN,SHAOMIAOFUJIAN_NAME";
			Map map = sendDocUploadManager.loadAttachInfo(bussinessId, fliedNames);
			if (map.get("BANJIEYIJIAN") != null) {
				doneSuggestion = map.get("BANJIEYIJIAN").toString();
			}
			if (map.get("SHAOMIAOFUJIAN_NAME") != null) {
				attachName = map.get("SHAOMIAOFUJIAN_NAME").toString();
			}
		}
		return "upload";
	}

	/**
	 * 加载扫描附件，转换成图片，并返回图片地址
	 * 
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Feb 23, 2012 3:37:48 PM
	 */
	public String showSaoMiaoFuJian() throws Exception {

//		StringBuilder imgPath = new StringBuilder();
//		if (bussinessId != null && !"".equals(bussinessId)) {
//			String fliedNames = "SHAOMIAOFUJIAN";
//			Map map = sendDocUploadManager.loadAttachInfo(bussinessId, fliedNames);
//			byte[] buf = null;
//
//			if (map.get("SHAOMIAOFUJIAN") != null) {
//				buf = (byte[]) map.get("SHAOMIAOFUJIAN");
//			}
//			if (buf != null) {
//				InputStream is = FileUtil.ByteArray2InputStream(buf);
//				String path = AttachmentHelper.saveFile(getRequest().getContextPath(), is, "temp.jpg");
//				logger.info("生成附件：" + path);
//				imgPath.append(path);
//			}
//		}
//		getRequest().setAttribute("imgPath", imgPath.toString());
		
		StringBuilder imgPatherror = new StringBuilder();
		StringBuilder imgPath = new StringBuilder("");
		if (bussinessId != null && !"".equals(bussinessId)) {
			String fliedNames = "SHAOMIAOFUJIAN_NAME,SHAOMIAOFUJIAN";
			Map map = sendDocUploadManager.loadAttachInfo(bussinessId, fliedNames);
			String paths = null;
			byte[] smfjBuf = null;

			if (map.get("SHAOMIAOFUJIAN_NAME") != null) {
				paths = (String) map.get("SHAOMIAOFUJIAN_NAME");
				smfjBuf = (byte[]) map.get("SHAOMIAOFUJIAN");
			}
			byte[] buf = null;
			
			if (smfjBuf==null&&paths != null) {
				String[] attId=null;
				if(paths.indexOf("attachId")<0){
				}else{
					attId = paths.split(",");
					
					for(int i=1;i<attId.length;i++){
						ToaAttachment toaattachment = attachmentService.getAttachmentById(attId[i]);
						buf = toaattachment.getAttachCon();
						if(buf!=null){
							InputStream is = FileUtil.ByteArray2InputStream(buf);
							String path = AttachmentHelper.saveFile(getRequest().getContextPath(), is, attId[i]+".jpg");
							logger.info("生成附件：" + path);
//						imgPath.append(path).append(";");
							
							imgPath.append("<img style=\"width: 210mm;height: 297mm;\" src=\""+path+"\" /><br>");
						}else{
							 String rootPath = PathUtil.getRootPath();
							if(imgPatherror.length() == 0){
								imgPatherror.append("<div align=left>可能由于系统升级时，未保存服务器指定目录"+rootPath + "uploadfile" + File.separator +"下相关文件的附件文件，导致系统找不到指定文件：</div>");
							}
						    imgPatherror.append("<div align=left><font color=red>"+i+"、"+toaattachment.getAttachId() + "." + toaattachment.getAttachType()+"</font></div>");
						}
					}
				}
			}else if(smfjBuf!=null){
				InputStream is = FileUtil.ByteArray2InputStream(smfjBuf);
				String path = AttachmentHelper.saveFile(getRequest().getContextPath(), is, "temp.jpg");
				logger.info("生成附件：" + path);
				imgPath.append("<img style=\"width: 210mm;height: 297mm;\" src=\""+path+"\" /><br>");
			}
		}
		getRequest().setAttribute("imgPath", imgPath.toString());
		getRequest().setAttribute("imgPatherror", imgPatherror.toString());
		
		return "showsaomiaofujian";
	}
	
	/**
	 * 把存储在数据库中的PDF文件转换成保存在服务器上的PDF文件
	 * 
	 * @author 刘皙
	 * @throws Exception
	 * @createTime 2012年3月13日15:28:28
	 */
	public void transformPDFfromDBtoFile() throws Exception {
		HttpServletResponse response = this.getResponse();
		PrintWriter out = response.getWriter();
		System.out.println("生成PDF文件:");
		List<?> list = sendDocUploadManager.getAllBussinessId("t_oarecvdoc", "oarecvdocid");
		String ibussinessId;
		for(int i=0;i<list.size();i++){
			ibussinessId = list.get(i).toString();
			ibussinessId = ibussinessId.substring(ibussinessId.indexOf("=")+1, ibussinessId.indexOf("}"));
			if (ibussinessId != null && !"".equals(ibussinessId)) {
				String fileNames = "pdfcontent";
				Map<?, ?> map = sendDocUploadManager.loadPDFInfo(ibussinessId, fileNames, "t_oarecvdoc", "oarecvdocid");
				byte[] buf = null;
				if (map.get(fileNames) != null) {
					buf = (byte[]) map.get(fileNames);
				}
				if (buf != null) {
					InputStream is = FileUtil.ByteArray2InputStream(buf);
					String path = AttachmentHelper.transformFile("pdfFile", is, ibussinessId+".pdf");
					logger.info("生成附件：" + path);
				}
			}
		}
		System.out.println("已成功导出库中PDF附件。");
		out.println("<script>parent.callback()</script>");
	}

	/**
	 * 加载收文正文PDF并显示
	 * 
	 * @author 刘皙
	 * @return
	 * @throws Exception
	 * @createTime 2012年3月10日9:00:18
	 */
	public String showPDF() throws Exception {
		if(pdfPath!=null&&!(pdfPath).equals("")){
			int i = pdfPath.lastIndexOf(File.separatorChar);
			if(i == -1){
				pdfPath = getRequest().getContextPath() + File.separatorChar + "pdfFile"+ File.separatorChar + pdfPath;
			}
		}
		System.out.println("pdfPath:" + pdfPath + " isPermitUploadPDF:" + isPermitUploadPDF);
		return "uploadpdf";
	}

	/**
	 * 加载收文正文SMJ(扫描件)并显示
	 * 
	 * @author 刘皙
	 * @return
	 * @throws Exception
	 * @createTime 2013-5-9 14:35:38
	 */
	public String showSMJ() throws Exception {
		if(smjPath!=null&&!(smjPath).equals("")){
			int i = smjPath.lastIndexOf(File.separatorChar);
			if(i == -1){
				smjPath = getRequest().getContextPath() + File.separatorChar + "smjFile"+ File.separatorChar + smjPath;
			}
		}
		System.out.println("smjPath:" + smjPath + " isPermitUploadSMJ:" + isPermitUploadSMJ);
		return "uploadsmj";
	}

	/**
	 * @description 保存上传的收文正文PDF
	 * @author 刘皙
	 * @createTime 2012年3月5日15:23:53
	 * @return String
	 */
	public void saveUploadPDF() throws Exception {
		String callback = "true";// 返回页面后的操作
		HttpServletResponse response = this.getResponse();
		PrintWriter out = response.getWriter();
		try {
			String rootPath = PathUtil.getRootPath();// 得到工程根路径
			String uploadDir = "pdfFile";
			String dir = rootPath + uploadDir;
			File file = new File(dir);
			if (!file.exists()) {
				file.mkdir();
			}
			String ext = attachName.substring(attachName.lastIndexOf(".") + 1);
			Long nowTime = System.currentTimeMillis();
			Random random = new Random();
			String randomNum = random.nextInt(9) + "" + random.nextInt(9) + ""
					+ random.nextInt(9) + "" + random.nextInt(9) + ""
					+ random.nextInt(9);
			String fileName = nowTime + randomNum+"." + ext;  // 通过当前格林威治时间和5位随机数参数新的文件名
			pdfPath = dir + File.separatorChar  + fileName;
			FileInputStream is = new FileInputStream(this.upload);
			FileOutputStream os = null;
			try {
				if (is != null) {
					os = new FileOutputStream(dir + File.separatorChar  + fileName);
					int len = 0;
					byte[] battach = new byte[8192];
					while ((len = is.read(battach)) != -1) {
						os.write(battach, 0, len);
					}
					logger.info("附件'" + fileName + "'生成.");
				}
			} catch (Exception e) {
				logger.error("保存公文附件失败。", e);
				throw e;
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						logger.error("关闭输出流异常", e);
					}
				}
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						logger.error("关闭输入流异常", e);
						callback = "false";
					}
				}
			}
			// sendDocUploadManager.saveAttachInfo(bussinessId, attachName,
			// doneSuggestion,upload);
			pdfPath = fileName;
			// pdfPath = URLEncoder.encode(pdfPath, "utf-8");
			// BASE64Encoder encode = new BASE64Encoder();
			// encode.encode(pdfPath.getBytes());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		out.println("<script>parent.callback('" + callback + "','" + pdfPath + "')</script>");
	}

		/**
		 * @description 保存上传的收文正文SMJ(扫描件)
		 * @author 刘皙
		 * @createTime 2013-5-9 14:39:18
		 * @return String
		 */
		public void saveUploadSMJ() throws Exception {
			String callback = "true";// 返回页面后的操作
			HttpServletResponse response = this.getResponse();
			PrintWriter out = response.getWriter();
			try {
				String rootPath = PathUtil.getRootPath();// 得到工程根路径
				String uploadDir = "smjFile";
				String dir = rootPath + uploadDir;
				File file = new File(dir);
				if (!file.exists()) {
					file.mkdir();
				}
				String ext = attachName.substring(attachName.lastIndexOf(".") + 1);
				Long nowTime = System.currentTimeMillis();
				Random random = new Random();
				String randomNum = random.nextInt(9) + "" + random.nextInt(9) + ""
						+ random.nextInt(9) + "" + random.nextInt(9) + ""
						+ random.nextInt(9);
				String fileName = nowTime + randomNum+"." + ext;  // 通过当前格林威治时间和5位随机数参数新的文件名
				smjPath = dir + File.separatorChar  + fileName;
				FileInputStream is = new FileInputStream(this.upload);
				FileOutputStream os = null;
				try {
					if (is != null) {
						os = new FileOutputStream(dir + File.separatorChar  + fileName);
						int len = 0;
						byte[] battach = new byte[8192];
						while ((len = is.read(battach)) != -1) {
							os.write(battach, 0, len);
						}
						logger.info("附件'" + fileName + "'生成.");
					}
				} catch (Exception e) {
					logger.error("保存公文附件失败。", e);
					throw e;
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							logger.error("关闭输出流异常", e);
						}
					}
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							logger.error("关闭输入流异常", e);
							callback = "false";
						}
					}
				}
				// sendDocUploadManager.saveAttachInfo(bussinessId, attachName,
				// doneSuggestion,upload);
				smjPath = fileName;
				// pdfPath = URLEncoder.encode(pdfPath, "utf-8");
				// BASE64Encoder encode = new BASE64Encoder();
				// encode.encode(pdfPath.getBytes());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			out.println("<script>parent.callback('" + callback + "','" + smjPath + "')</script>");
		}

	/**
	 * @description 保存上传的领导批示意见
	 * @author 严建
	 * @createTime Dec 20, 2011 3:49:26 PM
	 * @return String
	 */
	public void saveUpload() throws Exception {
		String callback = "true";// 返回页面后的操作
		HttpServletResponse response = this.getResponse();
		PrintWriter out = response.getWriter();
		try {
			sendDocUploadManager.saveAttachInfo(bussinessId, attachName, doneSuggestion, upload);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			callback = "false";
		}
		out.println("<script>parent.callback('" + callback + "')</script>");
	}


	/**
	 * @author:luosy
	 * @description:保存上传的领导批示意见	(多附件)
	 * @date : 2012-4-6
	 * @modifyer:
	 * @description:
	 * @throws Exception
	 */
	public void saveUploads() throws Exception {
		String callback = "true";// 返回页面后的操作
		HttpServletResponse response = this.getResponse();
		PrintWriter out = response.getWriter();
		try {
			if(uploads!=null){
				long temp = 0;
				for(int i=0;i<uploads.length;i++){
					temp += uploads[i].length();
					if(uploads[i].length() <= 0 ){
						callback = "nullFile";
						out.println("<script>parent.callback('" + callback + "')</script>");
						return;
					}
					temp=0;
				}
			}
			sendDocUploadManager.saveAttachsInfo(bussinessId, uploadsFileName, doneSuggestion, uploads);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			callback = "false";
		}
		out.println("<script>parent.callback('" + callback + "')</script>");
	}
	
	/**
	 * 导出待办文件的excel文件
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 27, 2011 11:21:55 AM
	 * @return String
	 */
	public String exportTodoExcel() {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowName(workflowName);
		parameter.setFormId(formId);
		parameter.setWorkflowType(workflowType);
		parameter.setUserName(userName);
		parameter.setIsBackSpace(isBackSpace);
		parameter.setStartDate(startDate);
		parameter.setEndDate(endDate);
		parameter.setType(type);
		parameter.setTaskIdList(null);
		parameter.setHandleKind(handleKind);
		Object[] objs = sendDocManager.getTodo(page, parameter);
		List[] lists = sendDocUploadManager.exportWorkExcel(objs);
		String excelFileName = "";
		if (privilName != null && !privilName.equals("")) {
			excelFileName = privilName;
		} else if (workflowName != null && !workflowName.equals("")) {
			excelFileName = workflowName;
		} else {
			excelFileName = "Excel导出文件";
		}
		sendDocUploadManager.genExcelFile(getResponse(), lists[0], lists[1], excelFileName);
		return null;
	}

	/**
	 * @description
	 * @author 严建
	 * @createTime Dec 27, 2011 11:22:29 AM
	 * @return String
	 */
	public String exportProcessedExcel() throws Exception {
		ProcessedParameter parameter = new ProcessedParameter();
		parameter.setWorkflowName(workflowName);
		parameter.setFormId(formId);
		parameter.setWorkflowType(workflowType);
		parameter.setUserName(userName);
		parameter.setIsBackSpace(isBackSpace);
		parameter.setStartDate(startDate);
		parameter.setEndDate(endDate);
		parameter.setProcessStatus(state);
		parameter.setSortType("");
		parameter.setTaskIdList(null);
		parameter.setShowSignUserInfo(showSignUserInfo);
		parameter.setFilterSign(filterSign);
		parameter.setFilterYJZX(filterYJZX);
		parameter.setHandleKind(handleKind);
		parameter.setIsSuspended("0");
		Object[] objs = sendDocManager.getProcessed(page, parameter);
		List[] lists = sendDocUploadManager.exportWorkExcel(objs);
		String excelFileName = "";
		if (privilName != null && !privilName.equals("")) {
			excelFileName = privilName;
		} else if (workflowName != null && !workflowName.equals("")) {
			excelFileName = workflowName;
		} else {
			excelFileName = "Excel导出文件";
		}
		sendDocUploadManager.genExcelFile(getResponse(), lists[0], lists[1], excelFileName);
		return null;
	}

	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	public String getDoneSuggestion() {
		return doneSuggestion;
	}

	public void setDoneSuggestion(String doneSuggestion) {
		this.doneSuggestion = doneSuggestion;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getProcessIds() {
		return processIds;
	}

	public void setProcessIds(String processIds) {
		this.processIds = processIds;
	}

	public String getPrivilName() {
		return privilName;
	}

	public void setPrivilName(String privilName) {
		try {
			privilName = java.net.URLDecoder.decode(privilName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.privilName = privilName;
	}

	public File[] getUploads() {
		return uploads;
	}

	public void setUploads(File[] uploads) {
		this.uploads = uploads;
	}

	public String[] getUploadsFileName() {
		return uploadsFileName;
	}

	public void setUploadsFileName(String[] uploadsFileName) {
		this.uploadsFileName = uploadsFileName;
	}

	public String getSmjPath() {
		return smjPath;
	}

	public void setSmjPath(String smjPath) {
		this.smjPath = smjPath;
	}

	public String getIsPermitUploadSMJ() {
		return isPermitUploadSMJ;
	}

	public void setIsPermitUploadSMJ(String isPermitUploadSMJ) {
		this.isPermitUploadSMJ = isPermitUploadSMJ;
	}

	public String getIsPermitUploadPDF()
	{
		return isPermitUploadPDF;
	}

	public void setIsPermitUploadPDF(String isPermitUploadPDF)
	{
		this.isPermitUploadPDF = isPermitUploadPDF;
	}


}
