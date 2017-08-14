package com.strongit.oa.senddoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.GWYJAttach;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.util.PathUtil;
import com.strongit.oa.util.UUIDGenerator;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;


@Service
@Transactional
public class AttachManager {
	private GenericDAOHibernate<GWYJAttach, String> attachDao;

	@Autowired
	private IEFormService eformService;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Properties formProperties = null;
	
	@Autowired IUserService userService ;	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		attachDao = new GenericDAOHibernate<GWYJAttach, String>(
				sessionFactory, GWYJAttach.class);
	}

	public AttachManager(){
		this.formProperties = new Properties();
		try {
			this.formProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("form.properties"));
		} catch (Exception ex) {
			logger.warn("打开 form.properties 属性文件错误，", ex);
		}		
	}
    
/**
 * 保存附件信息
 * @description	
 * @author  李骏，保存反馈意见word文档
 * @param model
 */
	public void saveConAttach(File word,String docid) {
		GWYJAttach model = new GWYJAttach();
		User user = userService.getCurrentUser();
		String attachPath = "";
		if (model.getAttachId() == null) {// 添加附件
			UUIDGenerator generator = new UUIDGenerator();
			attachPath = generator.generate().toString();
		} 
		model.setDocid(docid);
		model.setAttachPath(attachPath);
		Organization org = userService.getDepartmentByOrgId(user.getOrgId());
		
		String orgName = org.getOrgName();
		System.out.print(orgName);
		model.setAccachName(orgName+"反馈意见.doc");
		
		if(user.getSupOrgCode().equals("001"))
		{
			model.setAccachName("南昌市人民政府反馈意见.doc");
		}
		try {
			
			InputStream attachment = new FileInputStream(word);
			String attachmentPath = PathUtil.getRootPath() + "WEB-INF"+File.separatorChar+"classes"+File.separatorChar+"attachments";
			File file2 = new File(attachmentPath);
			if (!file2.exists()) {
				file2.mkdir();
			}
			
			String attachmentFileName = attachmentPath + File.separatorChar
					+ attachPath;
			File file = new File(attachmentFileName);
			FileOutputStream fos = new FileOutputStream(file);
			int BUFFER_SIZE = 10240;
			byte[] buffer = new byte[BUFFER_SIZE];
			while (true) {
				int len = attachment.read(buffer);
				if (len > 0)
					fos.write(buffer, 0, len);
				else if (len < 0) {
					break;
				}
			}
			fos.close();
			attachment.close();
			// 数据库操作放在文件保存之后,保证文件系统保存成功才执行数据库保存操作
			attachDao.save(model);
			ToaLog log = new ToaLog();
			try {
				InetAddress inet = InetAddress.getLocalHost();
				log.setOpeIp(inet.getHostAddress()); // 操作者IP地址
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
			
			
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}


/**
 * 获取文件目录
 * @description	
 * @author  Jianggb
 * @return
 * @throws ServiceException
 * @throws DAOException
 * @throws SystemException
 */
	private static String getAttachDirectory() throws ServiceException,
			DAOException, SystemException {
		try {
			String dir = PathUtil.getRootPath() + "conattachfile";
			File file = new File(dir);
			if (!file.exists()) {
				file.mkdir();
			}
			return dir;
		} catch (ServiceException ex) {
			// TODO: handle ServiceException
			throw ex;
		} catch (DAOException ex) {
			// TODO: handle DAOException
			throw ex;
		} catch (SystemException ex) {
			// TODO: handle SystemException
			throw ex;
		} catch (Exception ex) {
			// TODO: handle Exception
			throw new SystemException(ex);
		}
	}
	
	/**
	 * 得到反馈附件列表
	 * @param containsAttach 是否返回附件内容
	 */
	@SuppressWarnings("unchecked")
	public List<GWYJAttach> getGwyjAttachsByDocId(String docId,boolean containsAttach) {
		StringBuilder hql = new StringBuilder();
		hql.append("select new GWYJAttach(attachId,docid,accachName,attachPath,personConfigFlag,personOperateDate,personOperater) from GWYJAttach t where t.docid=?");
		List<GWYJAttach> list=attachDao.find(hql.toString(), docId);
		if(containsAttach){
			byte[] attachContent=null;
			InputStream is = null;
			for(int i=0;i<list.size();i++){
				GWYJAttach model=list.get(i);
				try {
					is = eformService.LoadAttachment(model.getAttachPath(), "");
					if(is==null){
						attachContent=null;
					}else{
						attachContent = FileUtil.inputstream2ByteArray(is);
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new SystemException(e);
				} finally {
					if ( is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				model.setAttachContent(attachContent);
			}
		}
		return list;
	}
	
	/**
	 * @description 根据公文id获取反馈附件列表
	 * @author 肖利建
	 * @createTime 2013-3-22  21:07:39
	 * @param docId 公文id
	 * @ return List<WorkflowAttach> 附件列表
	 */
	public List<GWYJAttach> getGwyjAttachsByDocId(String docId) {
		return getGwyjAttachsByDocId(docId, true);
	}
	
	/**
	 * @description 保存反馈意见附件
	 * @author 肖利建
	 * @createTime 2013-3-22 21:14:38
	 * @param model
	 *            附件
	 * @ return void
	 */
	public void saveGwyjAttach(GWYJAttach model) {
		byte[] attachContent = model.getAttachContent();
		String attachPath="";
		if(model.getAttachId()==null){//添加附件
			UUIDGenerator generator = new UUIDGenerator();
			attachPath = generator.generate().toString();	
			model.setAttachPath(attachPath);
		}else{//修改附件
			attachPath=model.getAttachPath();
		}
		model.setAttachPath(attachPath);
		model.setAttachContent(null);
		try {
		  if(attachContent == null){
			  return;
		  }
		  InputStream attachment = FileUtil.ByteArray2InputStream(attachContent);	
	      String defaultAttachmentPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "attachments";
	      String attachmentPath = this.formProperties.getProperty("attachment.upload.path", defaultAttachmentPath) + File.separatorChar + "";
	      File file = new File(attachmentPath);
	      /*if ((((!(file.isDirectory())) || (!(file.exists())))) && (!(file.mkdirs()))) {
	        throw new SystemException("创建 " + attachmentPath + " 目录失败。");
	      }*/
	      if(file.isDirectory()) {
	    	  if(!file.exists()) {
	    		  if(!file.mkdir()) {
	    			  throw new SystemException("创建 " + attachmentPath + " 目录失败。");
	    		  }
	    	  } else {
	    		  logger.info("目录创建成功...");
	    	  }
	      } else {
	    	  throw new SystemException("创建 " + attachmentPath + " 目录失败。");
	      }
	      
	      
	      String attachmentFileName = attachmentPath + File.separatorChar + attachPath;
	      file = new File(attachmentFileName);
	        FileOutputStream fos = new FileOutputStream(file);
	        int BUFFER_SIZE = 10240;
	        byte[] buffer = new byte[BUFFER_SIZE];
	        while (true) {
	          int len = attachment.read(buffer);
	          if (len > 0)
	            fos.write(buffer, 0, len);
	          else if (len < 0) {
	              break;
	            }
	        }
	        fos.close();
	        attachment.close();
	        //数据库操作放在文件保存之后,保证文件系统保存成功才执行数据库保存操作
	        attachDao.save(model);
	    } catch (SystemException ex) {
	      throw ex;
	    } catch (Exception ex) {
	      throw new SystemException(ex);
	    }
	}

	public Properties getFormProperties() {
		return formProperties;
	}
	
}
