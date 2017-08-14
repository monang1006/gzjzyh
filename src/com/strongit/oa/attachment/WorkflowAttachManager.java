package com.strongit.oa.attachment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.util.UUIDGenerator;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 公文附件处理类
 * 
 * @author yanjian
 * @company      Strongit Ltd. (C) copyright
 * @date         May 22, 2012 6:25:38 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.attachment.WorkflowAttachManager
 */
@Service
@Transactional
public class WorkflowAttachManager implements IWorkflowAttachService {
	private GenericDAOHibernate<WorkflowAttach, String> attachDao;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Properties formProperties = null;

	@Autowired
	private IEFormService eformService;
	
	/**
	 * 注入DAO的sessionFactory
	 * 
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		attachDao = new GenericDAOHibernate<WorkflowAttach, String>(
				sessionFactory, WorkflowAttach.class);
	}

	public WorkflowAttachManager() {
		this.formProperties = new Properties();
	    try {
	      this.formProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("form.properties"));
	    } catch (Exception ex) {
	      logger.warn("打开 form.properties 属性文件错误，", ex);
	    }
	}

	/**
	 * 附件历史数据迁移
	 * 每次只查询一条数据,避免数据量过大造成内存溢出.
	 */
	@SuppressWarnings("unchecked")
	public boolean doneHistoryData() throws DAOException,ServiceException,SystemException {
		try {
			Page<WorkflowAttach> page = new Page<WorkflowAttach>(1,false);
			String hql = "from WorkflowAttach t where t.attachPath is null and t.attachContent is not null";
			Page<WorkflowAttach> resultPage = attachDao.find(page,hql);
			List<WorkflowAttach> list = resultPage.getResult();
			if(list != null && !list.isEmpty()) {
				UUIDGenerator generator = new UUIDGenerator();
				String attachPath;
				for(WorkflowAttach attach : list) {
					byte[] content = attach.getAttachContent();
					if(content != null) {
						attachPath = generator.generate().toString();	
						attach.setAttachPath(attachPath);
						this.saveWorkflowAttach(attach);
						logger.info("成功迁移附件'" + attach.getAttachName() + "'在磁盘上生成文件'" + attachPath + "'");
					}
				}
				return true;
			}
		} catch (DAOException ex) {
			//throw ex;
			logger.error(ex.getMessage(),ex);
		} catch (ServiceException ex) {
			//throw ex;
			logger.error(ex.getMessage(),ex);
		} catch (SystemException ex) {
			//throw ex;
			logger.error(ex.getMessage(),ex);
		} catch (Exception ex) {
			//throw new SystemException(ex);
			logger.error(ex.getMessage(),ex);
		}
		return false;
	}
	
	/**
	 * @description 保存公文附件
	 * @author 严建
	 * @createTime Aug 29, 2011
	 * @param model
	 *            附件
	 * @ return void
	 */
	public void saveWorkflowAttach(WorkflowAttach model) {
		byte[] attachContent = model.getAttachContent();
		String attachPath="";
		if(model.getDocattachid()==null){//添加附件
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
	public void savetoAttachs(byte[] b) {
		byte[] attachContent = b;
		String attachPath="";
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
	    } catch (SystemException ex) {
	      throw ex;
	    } catch (Exception ex) {
	      throw new SystemException(ex);
	    }
	}

	
	/**
	 * @description 根据id获取公文附件信息
	 * @author 严建
	 * @createTime Aug 29, 2011
	 * @param id
	 *            附件id
	 * @return @ return WorkflowAttach 公文附件
	 */
	public WorkflowAttach get(String id) {
		WorkflowAttach model=getWithoutContent(id);
		if(model == null) {
			return null;
		}
		InputStream is = null;
		byte[] attachContent;
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
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		model.setAttachContent(attachContent);
		return model;
	}
	//
	public byte[] gets(String ATTACH_PATH) {
		InputStream is = null;
		byte[] attachContent;
		try {
			is = eformService.LoadAttachment(ATTACH_PATH, "");
			if(is==null){
				attachContent=null;
			}else{
				attachContent = FileUtil.inputstream2ByteArray(is);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return attachContent;
	}

	/**
	 * 删除数据库中的附件记录
	 * @param attachId				附件id
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void delete(String attachId) throws DAOException,ServiceException,SystemException {
		try{
			WorkflowAttach attach = attachDao.get(attachId);
			attachDao.delete(attach.getDocattachid());
			eformService.RemoveAttachment(attach.getAttachPath(), "");
			logger.info("Delete attach " + attach.getAttachPath());
		} catch (DAOException ex) {
			throw ex;
		} catch (ServiceException ex) {
			throw ex;
		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

	/**
	 * 得到公文附件列表
	 * @param containsAttach 是否返回附件内容
	 */
	@SuppressWarnings("unchecked")
	public List<WorkflowAttach> getWorkflowAttachsByDocId(String docId,boolean containsAttach) {
		StringBuilder hql = new StringBuilder();
		hql.append("select new WorkflowAttach(docattachid,docId,personConfigFlag,personOperateDate,personOperater,attachName,attachPath) from WorkflowAttach t where t.docId=?");
		List<WorkflowAttach> list=attachDao.find(hql.toString(), docId);
		if(containsAttach){
			byte[] attachContent=null;
			InputStream is = null;
			for(int i=0;i<list.size();i++){
				WorkflowAttach model=list.get(i);
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
	 * @description 根据公文id获取附件列表
	 * @author 严建
	 * @createTime Aug 29, 2011
	 * @param docId 公文id
	 * @ return List<WorkflowAttach> 附件列表
	 */
	public List<WorkflowAttach> getWorkflowAttachsByDocId(String docId) {
		return getWorkflowAttachsByDocId(docId, true);
	}

	/**
	 * @description 获取不包含内容的附件信息
	 * @author 严建
	 * @createTime Sep 2, 2011
	 * @param id 附件id
	 * @ return WorkflowAttach  公文附件 
	 */
	public WorkflowAttach getWithoutContent(String id) {
		StringBuilder hql = new StringBuilder();
		hql.append("select new WorkflowAttach(docattachid,docId,personConfigFlag,personOperateDate,personOperater,attachName,attachPath) from WorkflowAttach t where t.docattachid=?");
		List list = attachDao.find(hql.toString(),id);
		if(list != null && !list.isEmpty()) {
			return (WorkflowAttach)list.get(0);
		}
		return null;
	}

	public Properties getFormProperties() {
		return formProperties;
	}
	
}
