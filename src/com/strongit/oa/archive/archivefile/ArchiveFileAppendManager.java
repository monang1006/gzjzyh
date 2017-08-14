/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: zhangli
 * Version: V1.0
 * Description： 档案文件附件管理MANAGER
 */
package com.strongit.oa.archive.archivefile;

import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;
import com.strongit.oa.bo.ToaArchiveFileAppend;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
@OALogger
public class ArchiveFileAppendManager {
	/** 档案文件附件管理Dao*/
	private GenericDAOHibernate<ToaArchiveFileAppend, java.lang.String> archiveDao;

	/**
	 * @roseuid 4958897E006D
	 */
	public ArchiveFileAppendManager() {

	}

	/**
	 * 注册DAO
	 * 
	 * @param sessionFactory
	 * @roseuid 495880BE0280
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		archiveDao = new GenericDAOHibernate<ToaArchiveFileAppend, java.lang.String>(
				sessionFactory, ToaArchiveFileAppend.class);
	}

	/**
	 * 获取所有的档案文件附件
	 * 
	 * @return java.util.List 档案文件附件列表
	 * @roseuid 495881F60167
	 */
	public List getAllFileAppend() {
		return archiveDao.findAll();
	}

	/**
	 * 获取某个文件下的档案文件附件分页列表
	 * 
	 * @param page 分页对象
	 * @param fileId 档案文件编号
	 * @param model 档案文件附件对象
	 * @return com.strongmvc.orm.hibernate.Page 分页对象
	 * @roseuid 4958820303A9
	 */
	public Page<ToaArchiveFileAppend> getAllFileAppend(
			Page<ToaArchiveFileAppend> page, String fileId,
			ToaArchiveFileAppend model,OALogInfo ... loginfos) {
		return archiveDao.find(page,
				"from ToaArchiveFileAppend t where t.toaArchiveFile.fileId=?",
				fileId);
	}

	/**
	 * 获取档案文件附件对象
	 * 
	 * @param appendId 档案文件附件编号
	 * @return com.strongit.oa.bo.ToaArchiveFileAppend 档案文件附件对象
	 * @roseuid 495885090290
	 */
	public ToaArchiveFileAppend getFileAppend(String appendId,OALogInfo ... loginfos) {
		return archiveDao.get(appendId);
	}

	/**
	 * 获取对应档案文件下的第一个档案文件附件对象
	 * 
	 * @param appendId
	 *            档案文件编号
	 * @return com.strongit.oa.bo.ToaArchiveFileAppend 档案文件附件对象
	 * @roseuid 495885090290
	 */
	public ToaArchiveFileAppend getFileAppendByFile(String fileId,OALogInfo ... loginfos) {
		return (ToaArchiveFileAppend) archiveDao.findUnique(
				"from ToaArchiveFileAppend t where t.toaArchiveFile.fileId=?",
				fileId);
	}

	/**
	 * 保存档案文件附件
	 * 
	 * @param model 档案文件附件对象
	 * @return java.lang.String 保存结果
	 * @roseuid 49588519038A
	 */
	public String saveFileAppend(ToaArchiveFileAppend model,OALogInfo ... loginfos) {
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
	 * 批量删除档案文件附件
	 * 
	 * @param appendId 档案文件附件编号
	 * @return java.lang.String 删除结果
	 * @roseuid 495885220157
	 */
	public String delFileAppend(String appendId,OALogInfo ... loginfos) {
		String message = null;
		try {
			String[] str = appendId.split(",");
			for (String id : str) {
				archiveDao.delete(id);
			}
			message = "保存删除成功！";
		} catch (Exception e) {
			message = "保存删除失败！";
		}
		return message;
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
			String appendid) {
		ToaArchiveFileAppend obj = getFileAppend(appendid);
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ obj.getAppendName());
		OutputStream output = null;
		try {
			output = response.getOutputStream();
			output.write(obj.getAppendContent());
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取每个文件的附件数量
	 * @author 胡丽丽
	 * @date 2010-01-20
	 * @return
	 */
	public Map<String,Integer> getCount(){
		String sql = "select t.appendId, t.toaArchiveFile.fileId from ToaArchiveFileAppend t";
		List<Object[]> objsLst = archiveDao.find(sql);
		Map<String,Integer> map = new HashMap<String,Integer>();
		if(objsLst!=null && !objsLst.isEmpty()){
			for(Object[] objs : objsLst){
				if(objs[1]!=null){
					if(!map.containsKey(objs[1])){
						map.put(objs[1].toString(), 1);
					}else{
						map.put(objs[1].toString(), map.get(objs[1])+1);
					}
				}
			}
		}
		return map;
	}
}
