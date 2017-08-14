//Source file: F:\\workspace\\StrongOA2.0_DEV\\src\\com\\strongit\\oa\\prsnfldr\\privateprsnfldr\\PrsnfldrFileManager.java

package com.strongit.oa.prsnfldr.privateprsnfldr;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaPrivatePrsnfldrFolder;
import com.strongit.oa.bo.ToaPrsnfldrFile;
import com.strongit.oa.bo.ToaPrsnfldrFolder;
import com.strongit.oa.bo.ToaPublicPrsnfldrFolder;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.paramconfig.ConfigModule;
import com.strongit.oa.paramconfig.ParamConfigService;
import com.strongit.oa.search.SearchManager;
import com.strongit.oa.util.FiltrateContent;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.PathUtil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      文件管理Manager
 */
@Service
@Transactional
public class PrsnfldrFileManager extends BaseManager{
	
	private GenericDAOHibernate<ToaPrsnfldrFile, String> fileDao;
	
	private PrsnfldrFolderManager folderManager;
	
	private SearchManager searchManager;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	static final String UPLOADDIR = "prslfldrfile";

	private ParamConfigService pcService;
	
	@Autowired
	public void setPcService(ParamConfigService pcService) {
		this.pcService = pcService;
	}
	/**
	 * @roseuid 493DD6E100FA
	 */
	public PrsnfldrFileManager() {

	}

	@Transactional(readOnly=true)
	public User getCurrentUser() throws SystemException,ServiceException{
		try {
			return folderManager.getCurrentUser();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"当前用户信息"});
		}
	}
	
	/**
	 * @param sessionFactory
	 * @roseuid 493CC640035B
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		fileDao = new GenericDAOHibernate<ToaPrsnfldrFile,String>(sessionFactory,ToaPrsnfldrFile.class);
	}

	/**
	 * 得到存储在硬盘上的文件
	 * @author:邓志城
	 * @date:2010-9-6 下午03:50:43
	 * @param toaAttachment
	 * @return
	 */
	private File getFile(ToaPrsnfldrFile toaAttachment) {
		String rootPath = PathUtil.getRootPath();
	    File attchFile = new File(rootPath + UPLOADDIR + File.separator + toaAttachment.getFileId() + "." + toaAttachment.getFileExt());
	    return attchFile ;
	}
	
	/**
	 * @param id
	 * @return com.strongit.oa.prsnfldr.privateprsnfldr.ToaPrivatePrsnfldrFile
	 * @roseuid 493CC70402EE
	 */
	@Transactional(readOnly=true)
	public ToaPrsnfldrFile getFileById(String id) throws SystemException,ServiceException {
		try {
			ToaPrsnfldrFile toaAttachment = fileDao.get(id);
		    File attchFile = this.getFile(toaAttachment);
		    InputStream is = null;
		    ByteArrayOutputStream bos = null;
		    try {
				if(attchFile.exists()){
					is = new FileInputStream(attchFile);
					byte[] buf = null;
					byte[] b = new byte[8192];
					int read = 0;
					bos = new ByteArrayOutputStream();
					while((read=is.read(b))!=-1){
						bos.write(b, 0, read);
					}
					buf = bos.toByteArray();
					toaAttachment.setFileContent(buf);
				}
			} catch (FileNotFoundException e) {
				logger.error("未找到文件" + toaAttachment.getFileName(), e);
			} catch (IOException e) {
				
			} finally {
				try {
					if(bos != null){
						bos.close();
					}
					if(is != null){
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return toaAttachment ;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件"});
		} 
	}

	/**
	 * author:dengzc
	 * description:获取复制文件的大小,用于判断粘贴这些文件是否会超出系统分配给用户空间大小
	 * modifyer:
	 * description:
	 * @param files
	 * @return
	 */
	public double getTheCopyFileSize(String[] files) throws SystemException,ServiceException{
		double temp = 0;
		for(int i=0;i<files.length;i++){
			ToaPrsnfldrFile file = getFileById(files[i]);
			File attachFile = this.getFile(file);
			temp += attachFile.length();
		}
		return temp;
	}

	/**
	 * 批量删除文件
	 * @param id
	 * @roseuid 493CC72C0222
	 */
	public void deleteFile(String id) throws SystemException,ServiceException {
		try {
			String[] ids = id.split(",");
			for(int i=0;i<ids.length;i++){
				ToaPrsnfldrFile file = fileDao.get(ids[i]);
				File attachFile = this.getFile(file);
				if(attachFile.exists()){
					attachFile.delete();
					logger.info("删除文件" + file.getFileName());
				}else{
					logger.error("文件" + file.getFileName() + "不存在。");
				}	
				fileDao.delete(file);
				searchManager.delIndex(ids[i]);
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,new Object[] {"文件"});
		}
	}

	/**
	 * author:dengzc
	 * description:删除文件
	 * modifyer:
	 * description:
	 * @param file
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteFile(ToaPrsnfldrFile file) throws SystemException,ServiceException{
		try {
			File attachFile = this.getFile(file);
			if(attachFile.exists()){
				attachFile.delete();
				logger.info("删除文件" + file.getFileName());
			}else{
				logger.error("文件" + file.getFileName() + "不存在。");
			}
			fileDao.delete(file);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件"});
		}
	}
	
	/**
	 * @param file
	 * @roseuid 493CC7480177
	 */
	public void editFile(ToaPrsnfldrFile file) throws SystemException,ServiceException {
		try {
			fileDao.update(file);
			searchManager.updetIndex(file);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,new Object[] {"文件"});
		}
	}

	/**
	 * @param folderId
	 * @return List<ToaPrivatePrsnfldrFile>
	 * @roseuid 493CC77803B9
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Page<ToaPrsnfldrFile> getFiles(Page<ToaPrsnfldrFile> page,String folderId,String searchFileName) throws SystemException,ServiceException {
		try {
			String queryStr = "select file.fileId,file.fileTitle,file.fileCreateTime,file.fileSize,file.fileSortNo,file.fileCreatePerson from ToaPrsnfldrFile as file where file.toaPrsnfldrFolder.folderId=?";
			if(null!=searchFileName){
//				queryStr += " and file.fileTitle like '%"+FiltrateContent.getNewContent(searchFileName)+"%'";
				queryStr += " and file.fileTitle like '%"+searchFileName+"%'";// 文件名含有特殊符号的
			}
			queryStr += " order by file.fileCreateTime desc,file.fileTitle desc";
			Object[] values = {folderId};
			page =  fileDao.find(page, queryStr, values);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件"});
		}
		return page;
	}
	
	/**
	 * @param folderId
	 * @return List<ToaPrivatePrsnfldrFile>
	 * @roseuid 493CC77803B9
	 */
	@SuppressWarnings("unchecked")
	public List<ToaPrsnfldrFile> getFilesByFolderId(String folderId){
		final String hql = "from ToaPrsnfldrFile as file where file.toaPrsnfldrFolder.folderId=? order by file.fileCreateTime desc,file.fileTitle desc";	
		return fileDao.find(hql, folderId);
		
	}

	/**
	 * author:dengzc
	 * description:搜索--自动完成功能
	 * modifyer:
	 * description:
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public StringBuffer searchByAutoComplete(String folderId,String str) throws SystemException,ServiceException{
		StringBuffer sb = new StringBuffer();
		try {
			String queryStr = "select t.fileTitle from ToaPrsnfldrFile as t where t.toaPrsnfldrFolder.folderId=? order by t.fileCreateTime";
			Object[] values = {folderId};
			List<String> l = fileDao.find(queryStr, values);
			for(Iterator<String> it=l.iterator();it.hasNext();){
				String fileName = it.next();
				if(fileName.toLowerCase().indexOf(str)!=-1){
					sb.append(fileName).append("\n");				
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件"});
		}
		return sb;
	}

	/**
	 * 校验在指定的文件夹中,输入的文件标题是否已经存在.
	 * @author:邓志城
	 * @date:2009-11-5 下午04:30:35
	 * @param fileTitle 文件标题
	 * @param folderId 文件所属文件夹id
	 * @return true：存在；false：不存在.
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean isTitleAlreadyExist(String fileTitle,String folderId) throws SystemException,ServiceException {
		try{
			//String queryStr = "select count(*) from ToaPrsnfldrFile as t where t.toaPrsnfldrFolder.folderId=? and t.fileTitle = ?";
			//Long count = fileDao.findLong(queryStr, folderId,fileTitle);
			//return count == 0 ? false:true;
			
			return false;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件"});
		}
	}
	
	/**
	 * @return long
	 * @roseuid 493DD6E10157
	 */
	@Transactional(readOnly=true)
	public long getTotalSize() throws SystemException,ServiceException {
		long total = 0;
		List<ToaPrivatePrsnfldrFolder> folder = folderManager.getAllPrivateFolders();
		for(Iterator<ToaPrivatePrsnfldrFolder> it=folder.iterator();it.hasNext();){
			ToaPrivatePrsnfldrFolder toaFolder = it.next();
			String folderId = toaFolder.getFolderId();
			total += getFileSizeByFolderId(folderId);
		}
		return total;
	}
	
	/**
	 * 获取某文件夹下的文件大小
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public long getFileSizeByFolderId(String folderId) throws SystemException,ServiceException{
		try {
			String hql = "select t.fileSize from ToaPrsnfldrFile as t where t.toaPrsnfldrFolder.folderId=?";
			Object[] values = {folderId};
			List<String> list = fileDao.find(hql, values);
			long total = 0;
			if(list.size()>0){
				for(Iterator<String> it=list.iterator();it.hasNext();){
					String fileSize = it.next();
					double temp = 0;
					if(fileSize.endsWith("k")){//如果此文件单位为K
						temp = Double.parseDouble(fileSize.substring(0,fileSize.indexOf("k")))*1024; 
					}else if(fileSize.endsWith("字节")){
						temp = Double.parseDouble(fileSize.substring(0,fileSize.indexOf("字节")));
					}else if(fileSize.endsWith("MB")){
						temp = Double.parseDouble(fileSize.substring(0,fileSize.indexOf("MB")))*(1024*1024);
					}
					total += temp;
				}
			}
			return total;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件"});
		} 
	}

	
	/**
	 * @param toaPrsnfldrFile
	 * @return com.strongit.oa.prsnfldr.privateprsnfldr.ToaPrivatePrsnfldrFile
	 * @roseuid 493DE2EB0203
	 */
	public ToaPrsnfldrFile addFile(ToaPrsnfldrFile toaPrsnfldrFile,FileInputStream indexFis,boolean isPublicFolder) throws SystemException,ServiceException {
		try {
			fileDao.save(toaPrsnfldrFile);
			OutputStream os  = null;
			InputStream is = null;
			try{
				String rootPath = PathUtil.getRootPath();//得到工程根路径
				String requestPath = ServletActionContext.getRequest().getServletPath();
				logger.info("servlet path :{}",requestPath);
				String dir = rootPath + UPLOADDIR; 
				File file = new File(dir);
				if(!file.exists()){
					file.mkdir();
				}
				os = new FileOutputStream(dir + File.separator + toaPrsnfldrFile.getFileId() + "." + toaPrsnfldrFile.getFileExt());
				byte[] attachCon = toaPrsnfldrFile.getFileContent();
				os.write(attachCon, 0, attachCon.length);
				logger.info("附件'"+toaPrsnfldrFile.getFileName()+"'生成.");
			}catch(Exception e){
				logger.error("保存公文附件失败。",e);
				throw new SystemException(e);
			}finally{
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						logger.error("关闭输出流异常",e);
					}
				}
				if(is != null){
					try {
						is.close();
					} catch (IOException e) {
						logger.error("关闭输入流异常",e);
					}
				}
			}
			
			if(isPublicFolder){//如果是公共文件夹，则加入索引搜索
				searchManager.saveIndex(toaPrsnfldrFile, indexFis);
			}
			return toaPrsnfldrFile;
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"文件"});
		}
	}
	
	/**
	 * @param toaPrsnfldrFile
	 * @return com.strongit.oa.prsnfldr.privateprsnfldr.ToaPrivatePrsnfldrFile
	 * @roseuid 493DE2EB0203
	 */
	public ToaPrsnfldrFile addFile(ToaPrsnfldrFile toaPrsnfldrFile,boolean isPublicFolder) throws SystemException,ServiceException {
		try {
			fileDao.save(toaPrsnfldrFile);
			OutputStream os  = null;
			InputStream is = null;
			try{
				String rootPath = PathUtil.getRootPath();//得到工程根路径
				String requestPath = ServletActionContext.getRequest().getServletPath();
				logger.info("servlet path :{}",requestPath);
				String dir = rootPath + UPLOADDIR; 
				File file = new File(dir);
				if(!file.exists()){
					file.mkdir();
				}
				os = new FileOutputStream(dir + File.separator + toaPrsnfldrFile.getFileId() + "." + toaPrsnfldrFile.getFileExt());
				byte[] attachCon = toaPrsnfldrFile.getFileContent();
				os.write(attachCon, 0, attachCon.length);
				logger.info("附件'"+toaPrsnfldrFile.getFileName()+"'生成.");
			}catch(Exception e){
				logger.error("保存公文附件失败。",e);
				throw new SystemException(e);
			}finally{
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						logger.error("关闭输出流异常",e);
					}
				}
				if(is != null){
					try {
						is.close();
					} catch (IOException e) {
						logger.error("关闭输入流异常",e);
					}
				}
			}
			if(isPublicFolder){//如果是公共文件夹，则加入索引搜索
				searchManager.saveIndex(toaPrsnfldrFile);
			}
			return toaPrsnfldrFile;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"文件"});
		}
	}
	/******************************* 框内为webservice 借口调用方法  begin**********************************************/
	/**
	 * @param folderId
	 * @return List<ToaPrivatePrsnfldrFile>
	 * @roseuid 493CC77803B9
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Page<ToaPrsnfldrFile> getFilesByWeb(Page<ToaPrsnfldrFile> page,String folderId,String fileTitle
			) throws SystemException,ServiceException {
		try {
			List param=new ArrayList();
			String queryStr = "select new ToaPrsnfldrFile(fileId,fileTitle,fileSize,fileCreateTime) from ToaPrsnfldrFile t where t.toaPrsnfldrFolder.folderId=?";
			param.add(folderId);
			if(null!=fileTitle&&!"".equals(fileTitle)){
				queryStr += " and t.fileTitle like ? ";
				param.add("%"+fileTitle+"%");
			}
			if("date".equals(page.getOrderBy())){
				queryStr += " order by t.fileCreateTime "+ page.getOrder();
			}else if("size".equals(page.getOrderBy())){
				queryStr += " order by t.fileSize "+ page.getOrder();
			}else{ 
				queryStr += " order by t.fileCreateTime desc";
			}
			page =  fileDao.find(page, queryStr, param.toArray());
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"文件"});
		}
		return page;
	}
	/**
	 * 获取文件前一个文件id  或 后一个文件id
	 * author  taoji
	 * @param folderId  文件夹id
	 * @param fileId	文件id
	 * @param order		按什么排序   desc  or  asc
	 * @param orderBy		按什么排序   date  or  size
	 * @param mark		前一个id 还是后一个id   ago  or  After
	 * @return
	 * @date 2014-1-11 下午04:07:52
	 */
	public String getFileId(String folderId,String fileId,String order,String orderBy,String mark){
		Object[] values = new Object[10];
		int i=0;
		StringBuffer sb = new StringBuffer();
		sb.append(" from ToaPrsnfldrFile as file where file.toaPrsnfldrFolder.folderId=? ");
		values[i++] = folderId;
		if("date".equals(orderBy)){
			sb.append(" order by file.fileCreateTime "+order);
		}else{
			sb.append(" order by file.fileSize "+order);
		}
		Object[] obj = new Object[i];
		for(int j=0;j<i;j++){
			obj[j]=values[j];
		}
		int m=0;
		List<ToaPrsnfldrFile> list = fileDao.find(sb.toString(), obj);
		if(list!=null&&list.size()>0){
			for(int k=0;k<list.size();k++){
				if(list.get(k).getFileId().equals(fileId)){
					m=k;
					break;
				}
			}
			if("ago".equals(mark)){
				if(m==0){
					return null;
				}else{
					return list.get(m-1).getFileId();
				}
			}else{
				if(m==list.size()-1){
					return null;
				}else{
					return list.get(m+1).getFileId();
				}
			}
		}
		return null;
	}
	/******************************* 框内为webservice 借口调用方法  end**********************************************/
	@Autowired
	public void setFolderManager(PrsnfldrFolderManager folderManager) {
		this.folderManager = folderManager;
	}

	@Autowired
	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}
	
	
	/**
	 * @author:ganyao
	 * @description:获取个人文件柜默认附件大小限制
	 * @date : 2014年3月27日 16:11:30
	 * @description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Long getDefAttSize()throws SystemException,ServiceException{
		try{
			String baseSize=pcService.getConfigSize(ConfigModule.PRSNFLDR);
			return (new Long(baseSize).longValue())*1024*1024;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取新闻公告附件默认大小限制"});
		}
	}
	
}
