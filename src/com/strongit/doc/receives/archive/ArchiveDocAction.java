package com.strongit.doc.receives.archive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.doc.bo.TdocSend;
import com.strongit.doc.bo.TtransArchive;
import com.strongit.doc.bo.TtransArchiveAttach;
import com.strongit.doc.bo.TtransDoc;
import com.strongit.doc.bo.TtransDocAttach;
import com.strongit.doc.receives.RecvDocManager;
import com.strongit.doc.sends.util.DESEncrypter;
import com.strongit.oa.bo.ToaSysmanageDictitem;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.dict.IDictService;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;
//import com.sun.java_cup.internal.parser;
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "archiveDoc.action", type = ServletActionRedirectResult.class) })
public class ArchiveDocAction extends BaseActionSupport<TtransArchive> {
	

	private TtransArchive model=new TtransArchive();	//档案信息
	
	private Page<TtransArchive> page=new Page<TtransArchive>(20, true);		//分页
	
	private String disLogo;													//标识
	
	private ArchiveDocManager manager;
	
	private RecvDocManager recvDocManager;
	
	private String archiveId;							//档案中心文件ID
	
	private String archiveAttachId;						//档案中心文件附件ID
	
	private String forwardStr;							//控制跳转字符串
	
	private String docType;														//打开的OFFICE类型，参见千航控件说明：0:  没有文档； 100 =其他文档类型；
																					//1=word；2=Excel.Sheet或者 Excel.Chart ；
																					//3=PowerPoint.Show； 4= Visio.Drawing； 
																					//5=MSProject.Project； 6= WPS Doc；
																					//7:Kingsoft Sheet
    private String treeType;								//档案分组，类型值
    
    private List<ArchiveDocTree>treeList;					//年份显示树
    
    private List<String> yearList;
    private String year;									//文件年份f
    private List<String> monthList;
    private String month;									//文件发文月份
    
    
    private IUserService userservice;					//用户接口
    
    @Autowired IDictService dictService ;										//字典服务类
    
    private String treeValue;
    
    private String senddocId;							//已收公文Id，进行归档
    
    /** 打印份数 */
    private String PrintSum;

    /** 已打印份数 */
    private String docHavePrintNum;
    
    private String printNum;						//可打印份数
    
    private String fileNo;								//归档时，档案文号
    
    private String fileFileName;						//附件名称	
    
	private List<ToaSysmanageDictitem> mmdjItems ;								//秘密等级字典项
	
	private List<ToaSysmanageDictitem> jjcdItems ;								//紧急程度字典项
	
	Date startDate ;															//开始日期
	
	Date endDate ;																//结束日期	
	
	private String docId;
	

	
	
	@Override
	public String delete() throws Exception {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public String input() throws Exception {
		prepareModel();
		if(model.getArchiveId()!=null&&!model.getArchiveId().equals("")){
			if(forwardStr!=null&&forwardStr.equals("view")){
//				if(model.getDocState()!=null&&model.getDocState().equals("0")){
//					model.setDocState("草拟");
//				}else if(model.getDocState()!=null&&model.getDocState().equals("1")){
//					model.setDocState("签章");
//				}else if(model.getDocState()!=null&&model.getDocState().equals("2")){
//					model.setDocState("分发");
//				}else if(model.getDocState()!=null&&model.getDocState().equals("3")){
//					model.setDocState("已发送");
//				}else if(model.getDocState()!=null&&model.getDocState().equals("4")){
//					model.setDocState("退回");
//				}else if(model.getDocState()!=null&&model.getDocState().equals("5")){
//					model.setDocState("回收");
//				}
				return "view";
			}else {				
				return "input";
			}
		}else {
			return "input";
		}
	}
	
	/**
	 * 年月份显示树
	 * @return
	 * @throws Exception
	 */
	public String tree() throws Exception{
		try {
				treeList=new ArrayList<ArchiveDocTree>();
				yearList=new ArrayList<String>();//年份列表
				
				User user=userservice.getCurrentUser();
				yearList=manager.getTempfileYear();
				for(String ye:yearList){
					if(ye!=null){
						try {
							
							ArchiveDocTree tree=new ArchiveDocTree();
							tree.setTreeid(ye);
							tree.setTreeName("年");
							tree.setTreetype("4");
							treeList.add(tree);
						} catch (Exception e) {
							e.printStackTrace();
						}
						monthList=manager.getTempfileMonth(ye);
						if(monthList!=null){  //月份列表
							for(String mon:monthList){
								ArchiveDocTree montree=new ArchiveDocTree();
								montree.setTreeid(mon);
								montree.setParentId(ye);
								montree.setTreeName("月份");
								montree.setTreetype("4");
								treeList.add(montree);
							}
						}
					}				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "tree";
	}

	@Override
	public String list() throws Exception {
		mmdjItems = dictService.getItemsByValue("MMDJ");//秘密等级
		jjcdItems = dictService.getItemsByValue("JJCD");//紧急程度
		if(disLogo!=null&&disLogo.equals("search")){
			page=manager.searchByYear(page, model ,startDate,endDate,treeValue);			//根据条件查询档案中心文件列表
		}else{
			page=manager.getPageArchive(page,treeValue);			//获取所有的文件列表
		}
		if(page.getResult()==null){
			page.setTotalCount(0);
		}
		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (archiveId != null&&!"".equals(archiveId)&&!"null".equals(archiveId)) {
			model=manager.getArchiveFile(archiveId);
		}else {
			model=new TtransArchive();
		}
	}
	
	/*
	 * 根据文件Id判断当前文件是否存在
	 */
	public String isHasArchiveFile()throws Exception{
		if (archiveId != null&&!"".equals(archiveId)&&!"null".equals(archiveId)) {
			model=manager.getArchiveFile(archiveId);
			if(model!=null){
				return renderHtml(model.getDocTitle());
			}else {
				return renderHtml("1");
			}
		}else {
			return renderHtml("1");
		}
	}
	
	/**
	 * 下载文件
	 * @author zhengzb
	 * @desc 
	 * 2010-8-12 下午02:06:50 
	 * @return
	 * @throws Exception
	 */
	public String download()throws Exception{
		getRequest().setAttribute("backlocation","javascript:history.back();");	
		HttpServletResponse response = super.getResponse();
		model=manager.getArchiveFile(archiveId);
		if(model!=null&&model.getDocContent()!=null&&!"".equals(model.getDocContent())){
			
			/**
			 * 去除文档中的电子签章
			 */
			ByteArrayInputStream in = new ByteArrayInputStream(model.getDocContent());
			
			POIFSFileSystem fs = new POIFSFileSystem(in);   
			ByteArrayOutputStream ostream = new ByteArrayOutputStream();
			fs.writeFilesystem(ostream);
//			HWPFDocument hdt = new HWPFDocument(in);  
//			hdt.write(ostream);
			
			response.reset();
			response.setContentType("application/x-download");         //windows
			OutputStream output = null;
			response.addHeader("Content-Disposition", "attachment;filename=" +
					new String((model.getDocTitle()+".doc").getBytes("gb2312"),"iso8859-1"));
			output = response.getOutputStream();
			//转换成byte数组下载
			output.write(ostream.toByteArray());
			output.flush();	 	    
			if(output != null){
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				output = null;
			}
//			Set ttransArchiveAttaches=model.getTtransArchiveAttaches();
//			if (ttransArchiveAttaches!=null&&ttransArchiveAttaches.size()>0){
//				for(Iterator<TtransArchiveAttach> iterator=ttransArchiveAttaches.iterator();iterator.hasNext();){
//					TtransArchiveAttach archiveAttach=iterator.next();
//					//downloadAttachFile(archiveAttach,"");
//				}
//			}
			return null;
		}else{
			return renderHtml("<script>alert('当前档案文件正文为空,不需求下载！'); </script>");
		}
	}
	
	/**
	 * 复制附件到指定位置
	 * @author zhengzb
	 * @desc 
	 * 2010-8-12 下午02:59:40 
	 * @param archiveAttach
	 * @return
	 * @throws Exception
	 */
	public String downloadAttachFile(TtransArchiveAttach archiveAttach,String path) throws Exception{
		if(archiveAttach.getAttachFilePath()!=null&&!archiveAttach.getAttachFilePath().equals("")){
			HttpServletResponse response = super.getResponse();
			
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(archiveAttach.getAttachFilePath());	//从project的classpath中读取文件
			byte[] attachContent=com.strongit.oa.prsnfldr.util.FileUtil.inputstream2ByteArray(inputStream);
			File file=com.strongit.oa.prsnfldr.util.FileUtil.byteArray2File(attachContent);
			File newFile=new File(path);
			file.renameTo(newFile);
			return "true";
		}else {
			return "false";
		}
	}
	
	/**
	 * 下载附件
	 * @author zhengzb
	 * @desc 
	 * 2010-8-19 上午09:01:00  ------李骏 20121105 附件解密
	 * @return
	 * @throws Exception
	 */
	public String downloadAttachFile() throws Exception{
		HttpServletResponse response = super.getResponse();
		TtransArchiveAttach archiveAttach=manager.getArchiveAttachById(archiveAttachId);
		if(archiveAttach!=null&&archiveAttach.getAttachFilePath()!=null&&!"".equals(archiveAttach.getAttachFilePath())){
			response.reset();
			response.setContentType("application/x-download");         //windows
			OutputStream output = null;
			try {
				String rootPath = getRequest().getSession().getServletContext().getRealPath(archiveAttach.getAttachFilePath());		//获取附件在服务器上的路径
				output = response.getOutputStream();
				File file = new File(rootPath);
				
				if(file.exists()){
					InputStream is = new FileInputStream(file);
//					byte[] attachContent = FileUtil.inputstream2ByteArray(is);
					//附件解密
					
//					byte[] attachContent = FileUtil.inputstream2ByteArray(new FileInputStream(file));								//读取附件内容
					response.addHeader("Content-Disposition", "attachment;filename=" +
							new String(archiveAttach.getAttachFileName().getBytes("gb2312"),"iso8859-1"));
					
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
			return renderHtml("<script>alert('当前档案文件正文为空,不需求下载！'); </script>");
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
			 model=manager.getArchiveFile(archiveId);											//档案文件
			 TtransArchiveAttach obj=null;											 //获取附件对象
			   if(archiveAttachId!=null&&!"".equals(archiveAttachId)){
				   obj=manager.getArchiveAttachById(archiveAttachId);
			   }else{
				   List<TtransArchiveAttach> list=(List<TtransArchiveAttach>)model.getTtransArchiveAttaches();
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
	
	   public String readAnnex()throws Exception{  
		   getRequest().setAttribute("backlocation","javascript:history.back();");		
		   String forward="viewAttach";   
		   try{
			   if(archiveId!=null&&!"".equals(archiveId)&&!"null".equals(archiveId)){
					 model=manager.getArchiveFile(archiveId);											//档案文件
					 TtransArchiveAttach obj=null;											 //获取附件对象
					   if(archiveAttachId!=null&&!"".equals(archiveAttachId)){
						   obj=manager.getArchiveAttachById(archiveAttachId);
					   }else{
						   List<TtransArchiveAttach> list=(List<TtransArchiveAttach>)model.getTtransArchiveAttaches();
						   if(list.size()>0){
							   obj =list.get(0);
						   }
					   }
				   if(obj!=null){
					   fileFileName=obj.getAttachFileName();	//获取附件名称
					   fileFileName=fileFileName.substring(0,fileFileName.lastIndexOf("."));	//附件名称去掉后缀	  
				   }  
			   }  
		   }catch(Exception e){
			   e.printStackTrace();
		   }
		   return forward;
		}
	/*
	 * 查看附件时
	 * 读取附件内容
	 */
	   public String openAttach() throws Exception {
			getRequest().setAttribute("backlocation","javascript:history.back();");		
			try{
				 if(archiveId!=null&&!"".equals(archiveId)&&!"null".equals(archiveId)){
					 model=manager.getArchiveFile(archiveId);											//档案文件
					 TtransArchiveAttach obj=null;											 //获取附件对象
					   if(archiveAttachId!=null&&!"".equals(archiveAttachId)){
						   obj=manager.getArchiveAttachById(archiveAttachId);
					   }else{
						   List<TtransArchiveAttach> list=(List<TtransArchiveAttach>)model.getTtransArchiveAttaches();
						   if(list.size()>0){
							   obj =list.get(0);
						   }
					   }
					   HttpServletResponse response = this.getResponse();
					   response.reset();
					   response.setContentType("application/octet-stream");
					   response.setHeader("Content-Disposition", "attachment; filename="
							   + obj.getAttachFileName());
					   OutputStream output = null;
					   String rootPath = getRequest().getSession().getServletContext().getRealPath(obj.getAttachFilePath());
					   output = response.getOutputStream();
						File file = new File(rootPath);
						if(file.exists()){
							try {
								byte[] attachContent = FileUtil.inputstream2ByteArray(new FileInputStream(file));
								output = response.getOutputStream();
								output.write(attachContent);
								output.close();
							} catch (Exception e) {
								e.printStackTrace();
							}							
						}else {
							try {								
								rootPath = getRequest().getSession().getServletContext().getRealPath("/empty.doc");
								file = new File(rootPath);
								byte[] attachContent = FileUtil.inputstream2ByteArray(new FileInputStream(file));
								output = response.getOutputStream();
								output.write(attachContent);
								output.close();
							} catch (Exception e) {
								e.printStackTrace();
							}				
						}
				 }
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
	   
	/**
	 * 兼容2003 - 2007 打开空文档。并加载数据
	 */
	public synchronized void openEmptyDocFromUrl() {
		TtransArchive archive=null;
		if(archiveId!=null&&!archiveId.equals("")){
			archive=manager.getArchiveFile(archiveId);			
		}
		HttpServletResponse response = this.getResponse();
		response.reset();
		response.setContentType("application/octet-stream");
		OutputStream output = null;
		String type = ".doc";
		logger.info("文档类型：" + docType);
		try{
			if("1".equals(docType)){
				type = ".doc";
			}else if("2".equals(docType)){
				type = ".xls";
			}else if("3".equals(docType)){
				type = ".ppt";
			}else if("4".equals(docType)){
				type = ".vsd";
			}else if("5".equals(docType)){
				type = ".mpp";
			}else if("6".equals(docType)){
				type = ".wps";
			}
			String rootPath = getRequest().getSession().getServletContext().getRealPath("/empty"+type);
			output = response.getOutputStream();
			File file = new File(rootPath);
			if(!file.exists()){
				rootPath = getRequest().getSession().getServletContext().getRealPath("/empty.doc");
				file = new File(rootPath);
			}
			logger.info("打开文档:"+rootPath);
			if(archive!=null&&archive.getDocContent().length>0){
				output.write(archive.getDocContent());										//加载公文正文
			}else {				
				byte[] buf = FileUtil.inputstream2ByteArray(new FileInputStream(file));
				output.write(buf);
			}
		}catch(Exception e){
			logger.error("打开文档出错了。",e);
		}finally{
			if(output!=null){
				try {
					output.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}
	
	/**
	 * 对已收的公文进行归档
	 * @author zhengzb
	 * @desc 
	 * 2010-8-16 下午04:53:21 
	 * @return
	 * @throws Exception
	 */
	public String audit() throws Exception{
		try {
			TtransArchive ttransArchive=new TtransArchive();
			StringBuffer message = new StringBuffer();
			User user=userservice.getCurrentUser();
			Organization org=new Organization();
			org=userservice.getUserDepartmentByUserId(user.getUserId());
			fileNo = ""+new Date().getTime();
			if(fileNo!=null&&!fileNo.equals("")){
				fileNo=URLDecoder.decode(fileNo, "utf-8");
				if(senddocId!=null&&!senddocId.equals("")){
					TdocSend docSend=recvDocManager.getSendDocById(senddocId);
					String orgCode=org.getOrgSyscode();										//获取机构CODE
					InetAddress ia = InetAddress.getLocalHost(); 
					String IP=ia.getHostAddress();								//获取计算机IP   
					TtransDoc ttransDoc=(TtransDoc)docSend.getTtransDoc();
					String title=ttransDoc.getDocTitle();
					if(ttransDoc!=null&&ttransDoc.getDocId()!=null&&ttransDoc.getDocId().length()>2){
						if(archiveId!=null&&!archiveId.equals("")){										//重新归档时，删除上次归档的公文
							manager.deleteArchiveDoc(archiveId);
						}
						String docId=ttransDoc.getDocId();
						TtransDoc transDoc=recvDocManager.getTtransDocById(docId);
						if(transDoc!=null){
							
							if(transDoc.getDocSendTime()==null){				//发文时间
								transDoc.setDocSendTime(new Date());
							}
							if(transDoc.getDocOfficialTime()==null){			//成文时间
								transDoc.setDocOfficialTime(new Date());
							}
							if(transDoc.getDocSealTime()==null){				//盖章时间
								transDoc.setDocSealTime(new Date());
							}
							if(transDoc.getDocEntryTime()==null){				//录入时间
								transDoc.setDocEntryTime(new Date());
							}
							if(transDoc.getDdocPrintTime()==null){				//印发时间
								transDoc.setDdocPrintTime(new Date());
							}
							
							BeanUtils.copyProperties(ttransArchive, transDoc);			 						// 拷贝公文对象到档案中心
							
							ttransArchive.setDocFilingTime(new Date());						//归档时间
							ttransArchive.setFilingUser(user.getUserId());					//归档人
							Date sendDate=ttransArchive.getDocSendTime();					//获取发文时间
							ttransArchive.setFileNo(fileNo);								//归档文号
							ttransArchive.setOperateIp(IP);									//操作IP
							ttransArchive.setSenddocId(senddocId);							//分发ID
							ttransArchive.setDeptCode(orgCode);									//机构CODE
							
							String year=getDateTime(sendDate, "yyyy");						//通过发文时间获取发文年度
							String month=String.valueOf(sendDate.getMonth()+1);				//通过发文时间获取发文月份
							if(month.length()<2){
								month="0"+month;
							}
							ttransArchive.setDocYear(year);									//备用字段rest1 ，用来存贮年度
							ttransArchive.setDocMonth(month);									//备用字段rest2 ，用来存贮月分
							//获取公文附件
							Set<TtransArchiveAttach> ttransArchiveAttachs=null;
							if(transDoc.getTtransDocAttaches()!=null&&transDoc.getTtransDocAttaches().size()>0){
								ttransArchiveAttachs=setArchiveAttach((Set<TtransDocAttach>)transDoc.getTtransDocAttaches());
							}
							ttransArchive.setTtransArchiveAttaches(ttransArchiveAttachs);						//公文附件
							String 	archiveIdRe="";						//保存档案文件	
							if(archiveId!=null&&!archiveId.equals("")){
								archiveIdRe=manager.saveArchiveDoc(ttransArchive,new OALogInfo(user.getUserName()+" 重新归档已收公文 《"+ttransArchive.getDocTitle()+"》"));	
							}else {
								archiveIdRe=manager.saveArchiveDoc(ttransArchive,new OALogInfo(user.getUserName()+" 归档已收公文 《"+ttransArchive.getDocTitle()+"》"));	
							}
							if(archiveIdRe!=null&&archiveIdRe.length()>2){
								docSend.setFilingUser(user.getUserId());						//归档人
								docSend.setDocFilingTime(new Date());							//归档时间
								recvDocManager.saveSends(docSend);
							}
							
							if(ttransArchiveAttachs!=null&&ttransArchiveAttachs.size()>0){
								for(Iterator<TtransArchiveAttach> iterator=ttransArchiveAttachs.iterator();iterator.hasNext();){
									TtransArchiveAttach tArchiveAttach=iterator.next();
									tArchiveAttach.setTtransArchive(ttransArchive);
									manager.saveAppend(tArchiveAttach);											//保存附件					
								}
							}	
							message.append("档案【 ").append(ttransArchive.getDocTitle()).append(" 】归档成功！\n");
						}else {
							message.append("公文：【 ").append(title).append("】不存在，无法归档！\n");
						}
					}else {
						message.append("所选公文不存在，无法归档！\n");
					}
				}
			}else {
				message.append("档案文号为空，无法归档！\n");
			}
			addActionMessage(message.toString());
			return "temp";
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "公文归档" });
		}
	}
	//已发送公文归档
	public String auditSend() throws Exception{
		try {
			synchronized (this){
				StringBuffer message = new StringBuffer();
				if(isHasSendDoc() == "2" || isHasSendDoc() == "1")
				{
					message.append("公文已归档或不存在！\n");
				}
				TtransArchive ttransArchive=new TtransArchive();
				User user=userservice.getCurrentUser();
				Organization org=new Organization();
				org=userservice.getUserDepartmentByUserId(user.getUserId());
				if(fileNo!=null&&!fileNo.equals("")){
					fileNo=URLDecoder.decode(fileNo, "utf-8");
					if(docId!=null&&!docId.equals("")){
	//					TdocSend docSend=recvDocManager.getSendDocById(senddocId);
						String orgCode=org.getOrgSyscode();										//获取机构CODE
						InetAddress ia = InetAddress.getLocalHost(); 
						String IP=ia.getHostAddress();								//获取计算机IP   
	//					TtransDoc ttransDoc=(TtransDoc)docSend.getTtransDoc();
						TtransDoc ttransDoc=recvDocManager.getTtransDocById(docId);
						String title=ttransDoc.getDocTitle();
						if(ttransDoc!=null&&ttransDoc.getDocId()!=null&&ttransDoc.getDocId().length()>2){
							if(archiveId!=null&&!archiveId.equals("")){										//重新归档时，删除上次归档的公文
								manager.deleteArchiveDoc(archiveId);
							}
							String docId=ttransDoc.getDocId();
							TtransDoc transDoc=recvDocManager.getTtransDocById(docId);
							if(transDoc!=null){
								
								if(transDoc.getDocSendTime()==null){				//发文时间
									transDoc.setDocSendTime(new Date());
								}
								if(transDoc.getDocOfficialTime()==null){			//成文时间
									transDoc.setDocOfficialTime(new Date());
								}
								if(transDoc.getDocSealTime()==null){				//盖章时间
									transDoc.setDocSealTime(new Date());
								}
								if(transDoc.getDocEntryTime()==null){				//录入时间
									transDoc.setDocEntryTime(new Date());
								}
								if(transDoc.getDdocPrintTime()==null){				//印发时间
									transDoc.setDdocPrintTime(new Date());
								}
								
								BeanUtils.copyProperties(ttransArchive, transDoc);			 						// 拷贝公文对象到档案中心
								
								ttransArchive.setDocFilingTime(new Date());						//归档时间
								ttransArchive.setFilingUser(user.getUserId());					//归档人
								Date sendDate=ttransArchive.getDocSendTime();					//获取发文时间
								ttransArchive.setFileNo(fileNo);								//归档文号
								ttransArchive.setOperateIp(IP);									//操作IP
								ttransArchive.setSenddocId(senddocId);							//分发ID
								ttransArchive.setDeptCode(orgCode);									//机构CODE
								
								String year=getDateTime(sendDate, "yyyy");						//通过发文时间获取发文年度
								String month=String.valueOf(sendDate.getMonth()+1);				//通过发文时间获取发文月份
								if(month.length()<2){
									month="0"+month;
								}
								ttransArchive.setDocYear(year);									//备用字段rest1 ，用来存贮年度
								ttransArchive.setDocMonth(month);									//备用字段rest2 ，用来存贮月分
								//获取公文附件
								Set<TtransArchiveAttach> ttransArchiveAttachs=null;
								if(transDoc.getTtransDocAttaches()!=null&&transDoc.getTtransDocAttaches().size()>0){
									ttransArchiveAttachs=setArchiveAttach((Set<TtransDocAttach>)transDoc.getTtransDocAttaches());
								}
								ttransArchive.setTtransArchiveAttaches(ttransArchiveAttachs);						//公文附件
								String 	archiveIdRe="";						//保存档案文件	
								ttransArchive.setRest4("0");  //  发文归档标识
								ttransArchive.setSenddocId("1989");
								if(archiveId!=null&&!archiveId.equals("")){
									archiveIdRe=manager.saveArchiveDoc(ttransArchive,new OALogInfo(user.getUserName()+" 重新归档已收公文 《"+ttransArchive.getDocTitle()+"》"));	
								}else {
									archiveIdRe=manager.saveArchiveDoc(ttransArchive,new OALogInfo(user.getUserName()+" 归档已收公文 《"+ttransArchive.getDocTitle()+"》"));	
								}
	//							if(archiveIdRe!=null&&archiveIdRe.length()>2){
	//								docSend.setFilingUser(user.getUserId());						//归档人
	//								docSend.setDocFilingTime(new Date());							//归档时间
	//								recvDocManager.saveSends(docSend);
	//							}
								
								if(ttransArchiveAttachs!=null&&ttransArchiveAttachs.size()>0){
									for(Iterator<TtransArchiveAttach> iterator=ttransArchiveAttachs.iterator();iterator.hasNext();){
										TtransArchiveAttach tArchiveAttach=iterator.next();
										tArchiveAttach.setTtransArchive(ttransArchive);
										manager.saveAppend(tArchiveAttach);											//保存附件					
									}
								}	
								//已归档的状态设为8
								ttransDoc.setDocState("8");
								recvDocManager.updatTtransDoc(ttransDoc);
								message.append("档案【 ").append(ttransArchive.getDocTitle()).append(" 】归档成功！\n");
							}else {
								message.append("公文：【 ").append(title).append("】不存在，无法归档！\n");
							}
						}else {
							message.append("所选公文不存在，无法归档！\n");
						}
					}
				}else {
					message.append("档案文号为空，无法归档！\n");
				}
				addActionMessage(message.toString());
				return "docSend";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "公文归档" });
		}
		
	}
	
	/*
	 * 把公文中的附件拷贝出来
	 */
	public Set<TtransArchiveAttach>  setArchiveAttach(Set<TtransDocAttach> setDocAttach) throws Exception{
		Set<TtransArchiveAttach> setArchiveAttach=new HashSet<TtransArchiveAttach>();							//SET档案中心公文附件		
		for(Iterator<TtransDocAttach> iterator=setDocAttach.iterator();iterator.hasNext();){
			TtransDocAttach docAttach=iterator.next();													//草拟公文附件
			TtransArchiveAttach archiveAttach=new TtransArchiveAttach();
			if(docAttach!=null){
				BeanUtils.copyProperties(archiveAttach, docAttach);										//拷贝附件
			}
			if(archiveAttach!=null){
				setArchiveAttach.add(archiveAttach);
			}
		}
		return setArchiveAttach;
	}
	
	
	/**
	 * 格式化时间
	 */
	public String getDateTime(Date time,String type) {
		SimpleDateFormat format = new SimpleDateFormat(type);
		String date = format.format(time);
		return date;
	}
	
	/**
	 * 判断档案文号是否存在
	 * @author zhengzb
	 * @desc 
	 * 2010-8-17 下午04:02:37 
	 * @return
	 * @throws Exception
	 */
	public String isHasFileNo() throws Exception{
		if(fileNo!=null&&!fileNo.equals("")){
			int count=manager.getIsFileNo(fileNo);
			if(count>0){
				return renderHtml("1");
			}else {
				
				return renderHtml( "0");
			}
		}else {
			return renderHtml("1");
		}		
	}
	
	public String isHasSendFileNo() throws Exception{
		if(fileNo!=null&&!fileNo.equals("")){
			int count=manager.getSendIsFileNo(fileNo);
			if(count>0){
				return renderHtml("1");
			}else {
				
				return renderHtml( "0");
			}
		}else {
			return renderHtml("1");
		}		
	}
	
	/**
	 * 判断已收公文，是否已归档
	 * @author zhengzb
	 * @desc 
	 * 2010-8-18 下午02:26:48 
	 * @return
	 * @throws Exception
	 */
	public String isHasArchiveDoc() throws Exception{
		if(senddocId!=null&&!"".equals(senddocId)){
			TdocSend docSend=recvDocManager.getSendDocById(senddocId);
			if(docSend!=null){
				String docCode=docSend.getTtransDoc().getDocCode();							//获取发文文号
				TtransArchive result=manager.getIsDocCode(docCode);
				if(result!=null&&result.getArchiveId()!=null&&!result.getArchiveId().equals("")){
					if(docSend.getDocFilingTime()!=null&&!result.getDocFilingTime().equals("")){
						return renderHtml("1");						//已归档
					}else {
						
						return renderHtml("archiveId:"+result.getArchiveId());						//回收公文后，在归档
					}
				}else {
					
//					if(docSend.getDocFilingTime()==null){				//归档时间为NULL
						return renderHtml( "0");					//未归档
//					}else {
//						return renderHtml("1");						//已归档
//					}
				}
			}else {
				return renderHtml("2");					//公文不存在
			}
		}else {
			return renderHtml("2");
		}
	}
	//已发公文 是否归档
	public String isHasSendDoc() throws Exception{
		if(docId!=null&&!"".equals(docId)){
			TtransDoc transDoc=recvDocManager.getTtransDocById(docId);
			if(transDoc!=null){
				TtransArchive result=manager.getIsDocSendCode(transDoc.getDocCode());
				if(result!=null&&result.getArchiveId()!=null&&!result.getArchiveId().equals("")){
					if(result.getDocFilingTime()!=null&&!result.getDocFilingTime().equals("")){
						return renderHtml("1");						//已归档
					}else {
						
						return renderHtml("archiveId:"+result.getArchiveId());						//回收公文后，在归档
					}
				}else {
					
//					if(docSend.getDocFilingTime()==null){				//归档时间为NULL
						return renderHtml( "0");					//未归档
//					}else {
//						return renderHtml("1");						//已归档
//					}
				}
			}else {
				return renderHtml("2");					//公文不存在
			}
		}else {
			return renderHtml("2");
		}
	}
	
	/**
	 * 获取公文打印信息
	 * @author zhengzb
	 * @desc 
	 * 2010-8-20 下午02:21:32 
	 * @return
	 */
	public String gotoPrintConfig(){
		try {
			if(archiveId!=null&&!archiveId.equals("")){
				model=manager.getArchiveFile(archiveId);
				senddocId=model.getSenddocId();				//分发ID
				TdocSend docSend=recvDocManager.getSendDocById(senddocId);	
				PrintSum=docSend.getDocHavePrintSum();
				docHavePrintNum=docSend.getDocHavePrintNum();
				if(PrintSum!=null&&docHavePrintNum!=null&&!PrintSum.equals("0")&&!docHavePrintNum.equals("0")){
					printNum= String.valueOf(Integer.parseInt(PrintSum)-Integer.parseInt(docHavePrintNum));								
				}else {
					printNum=PrintSum;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}		
		return "printConfig";
	}
	/**
	 * 收文---获取公文打印信息
	 * @author lijun
	 * @desc 
	 * @return
	 */
	public String gotoPrintConfig2(){
		try {
			if(archiveId!=null&&!archiveId.equals("")){
				
				TdocSend docSend=recvDocManager.getSendDocById(archiveId);	
				PrintSum=docSend.getDocHavePrintSum();
				docHavePrintNum=docSend.getDocHavePrintNum();
				if(PrintSum!=null&&docHavePrintNum!=null&&!PrintSum.equals("0")&&!docHavePrintNum.equals("0")){
					printNum= String.valueOf(Integer.parseInt(PrintSum)-Integer.parseInt(docHavePrintNum));								
				}else {
					printNum=PrintSum;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}		
		return "printConfig";
	}
	/**
	 * 收文----更新打印信息
	 * @author lijun
	 * @desc 
	 * @return
	 */
	public String changePrintedNum2() {
		User user=userservice.getCurrentUser();
		if(archiveId!=null&&!archiveId.equals("")){
			
			TdocSend docSend=recvDocManager.getSendDocById(archiveId);	
			int print=0;
			if(docSend.getDocHavePrintNum()!=null&&!docSend.getDocHavePrintNum().equals("0")){				
				print=Integer.parseInt(docHavePrintNum)-Integer.parseInt(docSend.getDocHavePrintNum());
			}else {
				print=Integer.parseInt(docHavePrintNum);
			}
			docSend.setDocHavePrintNum(docHavePrintNum);					//更新已打印份数
			manager.changePrintedNum(docSend, new OALogInfo(user.getUserName()+" 打印档案 《"+model.getDocTitle()+"》"+print+"份"));
//			recvDocManager.saveSends(docSend);
			return renderText("true");
		}else {
			return renderText("false");
		}
	}
	/**
	 * 更新打印信息
	 * @author zhengzb
	 * @desc 
	 * 2010-8-20 下午02:25:49 
	 * @return
	 */
	public String changePrintedNum() {
		User user=userservice.getCurrentUser();
		if(archiveId!=null&&!archiveId.equals("")){
			model=manager.getArchiveFile(archiveId);
			senddocId=model.getSenddocId();				//分发ID
			TdocSend docSend=recvDocManager.getSendDocById(senddocId);	
			int print=0;
			if(docSend.getDocHavePrintNum()!=null&&!docSend.getDocHavePrintNum().equals("0")){				
				print=Integer.parseInt(docHavePrintNum)-Integer.parseInt(docSend.getDocHavePrintNum());
			}else {
				print=Integer.parseInt(docHavePrintNum);
			}
			docSend.setDocHavePrintNum(docHavePrintNum);					//更新已打印份数
			manager.changePrintedNum(docSend, new OALogInfo(user.getUserName()+" 打印档案 《"+model.getDocTitle()+"》"+print+"份"));
//			recvDocManager.saveSends(docSend);
			return renderText("true");
		}else {
			return renderText("false");
		}
	}
	
	@Override
	public String save() throws Exception {
		// TODO 自动生成方法存根
		return null;
	}

	public TtransArchive getModel() {
		// TODO 自动生成方法存根
		return model;
	}

	public Page<TtransArchive> getPage() {
		return page;
	}

	public void setPage(Page<TtransArchive> page) {
		this.page = page;
	}

	public String getDisLogo() {
		return disLogo;
	}

	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}



	@Autowired
	public void setManager(ArchiveDocManager amanager) {
		this.manager = amanager;
	}

	public String getArchiveId() {
		return archiveId;
	}

	public void setArchiveId(String archiveId) {
		this.archiveId = archiveId;
	}

	public String getForwardStr() {
		return forwardStr;
	}

	public void setForwardStr(String forwardStr) {
		this.forwardStr = forwardStr;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getTreeType() {
		return treeType;
	}

	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}

	public List<ArchiveDocTree> getTreeList() {
		return treeList;
	}

	public void setTreeList(List<ArchiveDocTree> treeList) {
		this.treeList = treeList;
	}

	public List<String> getYearList() {
		return yearList;
	}

	public void setYearList(List<String> yearList) {
		this.yearList = yearList;
	}

	@Autowired
	public void setUserservice(IUserService userservice) {
		this.userservice = userservice;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getTreeValue() {
		return treeValue;
	}

	public void setTreeValue(String treeValue) {
		this.treeValue = treeValue;
	}

	public String getSenddocId() {
		return senddocId;
	}

	public void setSenddocId(String senddocId) {
		this.senddocId = senddocId;
	}
	@Autowired
	public void setRecvDocManager(RecvDocManager recvDocManager) {
		this.recvDocManager = recvDocManager;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getArchiveAttachId() {
		return archiveAttachId;
	}

	public void setArchiveAttachId(String archiveAttachId) {
		this.archiveAttachId = archiveAttachId;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public List<ToaSysmanageDictitem> getJjcdItems() {
		return jjcdItems;
	}

	public void setJjcdItems(List<ToaSysmanageDictitem> jjcdItems) {
		this.jjcdItems = jjcdItems;
	}

	public List<ToaSysmanageDictitem> getMmdjItems() {
		return mmdjItems;
	}

	public void setMmdjItems(List<ToaSysmanageDictitem> mmdjItems) {
		this.mmdjItems = mmdjItems;
	}

	public String getDocHavePrintNum() {
		return docHavePrintNum;
	}

	public void setDocHavePrintNum(String docHavePrintNum) {
		this.docHavePrintNum = docHavePrintNum;
	}

	public String getPrintNum() {
		return printNum;
	}

	public void setPrintNum(String printNum) {
		this.printNum = printNum;
	}

	public String getPrintSum() {
		return PrintSum;
	}

	public void setPrintSum(String printSum) {
		PrintSum = printSum;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

}
