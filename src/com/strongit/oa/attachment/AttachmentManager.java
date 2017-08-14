/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK:1.5.0_14; Struts:2.1.2; Spring:2.5.6; Hibernate:3.3.1.GA
 * Create Date: 2008-12-25
 * Autour: dengwenqiang
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.attachment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.util.PathUtil;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 附件处理类
 * @author dengwenqiang
 * @version 1.0
 */
@Service
@Transactional
public class AttachmentManager implements IAttachmentService {
	
	private GenericDAOHibernate<ToaAttachment, String> attachDao;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 构造函数
	 */
	public AttachmentManager() {

	}

	/**
	 * 注入DAO的sessionFactory
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		attachDao = new GenericDAOHibernate<ToaAttachment, String>(
				sessionFactory, ToaAttachment.class);
	}

	/**
	 * 保存附件
	 * @param attachName 附件名称
	 * @param attachCon 附件内容(二进制)
	 * @param attachTime 添加时间
	 * @param attachType 文件类型，即扩展名(如：zip、doc等)
	 * @param isPrivacy 是否是私人文件, "0"-公共文件;"1"-私人文件(邮件、内部消息等与个人相关的附件都属于私人文件)
	 * @param attachRemark 附注
	 * @param userId 用户ID
	 * @return java.lang.String 附件ID
	 */
	public String saveAttachment(String attachName, byte[] attachCon,
			Date attachTime, String attachType, String isPrivacy,
			String attachRemark, String userId) {
		ToaAttachment entity = new ToaAttachment();
		entity.setAttachName(attachName);
		if(attachType == null || "".equals(attachType)) {//未设置扩展名
			if(attachName != null && !"".equals(attachName)) {
				attachType = attachName.substring(attachName.lastIndexOf(".") + 1, attachName.length());
			}
		}
		entity.setAttachTime(attachTime);
		entity.setAttachType(attachType);
		entity.setIsPrivacy(isPrivacy);
		entity.setAttachRemark(attachRemark);
		//entity.setAttachCon(attachCon);
		entity.setUserId(userId);
		entity.setAttachSize(""+attachCon.length);
		attachDao.save(entity);

		OutputStream os  = null;
		InputStream is = null;
		try{
			String rootPath = PathUtil.getRootPath();//得到工程根路径
			String requestPath = ServletActionContext.getRequest().getServletPath();
			logger.info("servlet path :{}",requestPath);
			String dir = rootPath + "uploadfile"; 
			File file = new File(dir);
			if(!file.exists()){
				file.mkdir();
			}
			os = new FileOutputStream(dir + File.separator + entity.getAttachId() + "." + entity.getAttachType());
			os.write(attachCon, 0, attachCon.length);
			logger.info("附件'"+attachName+"'生成.");
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
		
		return entity.getAttachId();
	}

	/**
	 * 获取附件
	 * @param attachId 附件ID
	 * @return ToaAttachment 附件对象
	 */
	@Transactional(readOnly = true)
	public ToaAttachment getAttachmentById(String attachId) {
		ToaAttachment toaAttachment = attachDao.get(attachId);
		if(toaAttachment==null){
			return null;
		}
		if(toaAttachment.getAttachCon() != null) {//兼容早期存数据库的数据
			return toaAttachment ;
		}
		ToaAttachment entity = null;
		try {
			entity = toaAttachment.clone();
		} catch (CloneNotSupportedException e1) {
			entity = toaAttachment;
			logger.error("克隆附件对象失败。",e1);
		}
	    String rootPath = PathUtil.getRootPath();
	    File attchFile = null;
	    if(entity.getAttachType() != null && !"".equals(entity.getAttachType())) {
	    	attchFile = new File(rootPath + "uploadfile" + File.separator + entity.getAttachId() + "." + entity.getAttachType());
	    } else {
	    	attchFile = new File(rootPath + "uploadfile" + File.separator + entity.getAttachId());
	    }
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
				entity.setAttachCon(buf);
			}
		} catch (FileNotFoundException e) {
			logger.error("未找到文件" + entity.getAttachName(), e);
		} catch (IOException e) {
			logger.error("发生IO异常。",e);
		} finally {
			try {
				if(bos != null){
					bos.close();
				}
				if(is != null){
					is.close();
				}
			} catch (IOException e) {
				logger.error("关闭流发生异常。",e);
			}
		}
		return entity ;
	}

	/**
	 * 删除附件
	 * @param attachId 附件ID 
	 */
	public void deleteAttachment(String attachId) {
		ToaAttachment toaAttachment = attachDao.get(attachId);
		String rootPath = PathUtil.getRootPath();
		File attchFile = new File(rootPath + "uploadfile" + File.separator + toaAttachment.getAttachId() + "." + toaAttachment.getAttachType());
		if(attchFile.exists()){
			attchFile.delete();
			logger.info("删除文件" + toaAttachment.getAttachName());
		}else{
			logger.error("文件" + toaAttachment.getAttachName() + "不存在！");
		}			
		attachDao.delete(toaAttachment);
	}

	/**
	 * 删除附件
	 */
	public void deleteAttachment(List<ToaAttachment> list) {
		//attachDao.delete(list);
		if(list != null && !list.isEmpty()){
			for(ToaAttachment toaAttachment : list){
				if(toaAttachment.getAttachCon() == null){
					String rootPath = PathUtil.getRootPath();
					File attchFile = new File(rootPath + "uploadfile" + File.separator + toaAttachment.getAttachId() + "." + toaAttachment.getAttachType());
					if(attchFile.exists()){
						attchFile.delete();
						logger.info("删除文件" + toaAttachment.getAttachName());
					}else{
						logger.error("文件" + toaAttachment.getAttachName() + "不存在！");
					}
				}
				attachDao.delete(toaAttachment);
			}
		}
	}
}
