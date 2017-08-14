package com.strongit.doc.sends;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.di.exception.SystemException;
import com.strongit.doc.bo.TtransDoc;
import com.strongit.doc.bo.TtransDocAttach;
import com.strongit.doc.sends.util.DESEncrypter;
import com.strongit.oa.util.PathUtil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 公文附件处理.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-8-13 上午10:14:09
 * @version  2.0.7
 * @classpath com.strongit.doc.sends.TransDocAttachManager
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service
@Transactional
public class TransDocAttachManager {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	GenericDAOHibernate<TtransDocAttach, String> DAO ; //DAO处理类
	
	/**
	 * 注入SESSION工厂
	 * @author:邓志城
	 * @date:2010-7-7 下午04:54:30
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		DAO = new GenericDAOHibernate<TtransDocAttach, java.lang.String>(sessionFactory, TtransDocAttach.class);
	}

	/**
	 * 保存附件信息.-----增加加密操作----李骏20121029
	 * @author:邓志城
	 * @date:2010-8-13 下午01:47:30
	 * @param model
	 * @throws ServiceException
	 */
	void save(TtransDocAttach model) throws ServiceException ,Exception{
		OutputStream os  = null;
		InputStream is = null;
		//加密类
		try{
			is = model.getIs();
			String rootPath = PathUtil.getRootPath();//得到工程根路径
			if(is != null){
				File f = new File(rootPath + model.getAttachFilePath()); 
				if(!f.exists()){
					f.createNewFile();
				}
				os=new FileOutputStream(f);
				byte buffer[]=new byte[4*1024];
				int len = 0;
				while((len = is.read(buffer)) != -1){ 
					os.write(buffer,0,len);
				}
				os.flush();
				os.close();
				logger.info("公文附件'"+model.getAttachFilePath()+"'生成.");
			}
			if(!new File(rootPath + model.getAttachFilePath()).exists())
				model.setAttachFilePath(null);
			DAO.save(model);
		}catch(Exception e){
			logger.error("保存公文附件失败。",e);
			throw e;
		}
		
	}

	/**
	 * 删除附件，同时删除数据库中数据和硬盘上的数据.
	 * @author:邓志城
	 * @date:2010-8-13 下午04:45:07
	 * @param id
	 * @throws Exception
	 */
	void delete(String id) throws ServiceException {
		TtransDocAttach model = DAO.get(id);
		if(model != null){
			String filePath = model.getAttachFilePath();
			String rootPath = PathUtil.getRootPath();
			File file = new File(rootPath + filePath);
			if(file.exists()){
				file.delete();
				logger.info("删除文件" + model.getAttachFileName());
			}else{
				logger.error("文件" + filePath + "不存在！");
			}
			DAO.delete(model);			
		}
	}

	/**
	 * 根据附件id得到附件对象.
	 * @author:邓志城
	 * @date:2010-8-13 下午05:31:00
	 * @param id				附件id
	 * @return					附件对象
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	TtransDocAttach getAttach(String id)throws ServiceException {
		return DAO.get(id);
	}
	
}
