/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：案卷销毁管理manager
 */

package com.strongit.oa.archive.archiveDestr;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.strongit.oa.archive.archivefile.ArchiveFileAppendManager;
import com.strongit.oa.archive.archivefile.ArchiveFileManager;
import com.strongit.oa.archive.archivefolder.ArchiveFolderManager;
import com.strongit.oa.bo.ToaArchiveDestroy;
import com.strongit.oa.bo.ToaArchiveDestroyFile;
import com.strongit.oa.bo.ToaArchiveFile;
import com.strongit.oa.bo.ToaArchiveFileAppend;
import com.strongit.oa.bo.ToaArchiveFileAppendBak;
import com.strongit.oa.bo.ToaArchiveFileBak;
import com.strongit.oa.bo.ToaArchiveFolder;
import com.strongit.oa.bo.ToaArchiveFolderBak;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author pengxq
 * @version 1.0
 */
@Service
@Transactional
@OALogger
public class ArchiveDestrManager {
	private GenericDAOHibernate<ToaArchiveDestroy, java.lang.String> destrDao; // 销毁文件Dao

	private FileDestrManager fileDestrManager; // 销毁文件manager

	private ArchiveFolderManager folderManager; // 案卷manager

	private ArchiveFileManager fileManager; // 文档manager

	private ArchiveFileAppendManager annexManger;// 文档附件manager

	private FolderBackupManager folderBakManager;// 案卷备份manager

	private FileBackupManager fileBakManager; // 文档备份manager

	private AnnexBackupManager annexBakManager; // 文档附件备份manager

	/**
	 * @roseuid 4959D19B000F
	 */
	public ArchiveDestrManager() {

	}

	/**
	 * @param sessionFactory
	 * @roseuid 4959C4F10186
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		destrDao = new GenericDAOHibernate<ToaArchiveDestroy, String>(
				sessionFactory, ToaArchiveDestroy.class);

	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4上午11:43:55
	 * @desc: 根据销毁案卷id获取销毁案卷
	 * @param String
	 *            destroyId 销毁案卷主键值
	 * @return ToaArchiveDestroy 销毁案卷对象
	 */
	public ToaArchiveDestroy getDestrFolder(String destroyId,OALogInfo ... loginfos) throws SystemException,ServiceException{
		ToaArchiveDestroy obj = null;
		try {
			if (destroyId != null && !"".equals(destroyId)
					&& !"null".equals(destroyId))
				obj = destrDao.get(destroyId); // 获取案卷销毁申请单
		} catch (ServiceException e) {
			 throw new ServiceException(MessagesConst.find_error,               
						new Object[] {"销毁案卷对象"});
		}
		return obj;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-8下午04:48:13
	 * @desc: 修改销毁案卷状态,备份案卷、文档、附件到另一张表中，并删除相应的案卷、文档、附件
	 * @param List
	 *            <ToaArchiveDestroy> destFoList 销毁案卷列表
	 * @return void
	 */
	public String save(List<ToaArchiveDestroy> destFoList,OALogInfo ... loginfos) throws SystemException,ServiceException{
		String msg = "审核失败";
		try {
			ToaArchiveDestroy destrArchive = new ToaArchiveDestroy();
			if (destFoList != null && destFoList.size() > 0) {
				for (int k = 0; k < destFoList.size(); k++) {
					destrArchive = destFoList.get(k);
					destrDao.save(destrArchive);
					// 根据销毁案卷的编号获取案卷列表,因为销毁案卷的编号和案卷编号是对应的
					List<ToaArchiveFolder> list = folderManager
							.getFolderByNo(destrArchive.getDestroyFolderNo());
					if (list.size() > 0) {
						ToaArchiveFolder obj = list.get(0); // 案卷对象
						ToaArchiveFolderBak bak = new ToaArchiveFolderBak(); // 创建案卷备份对象
						BeanUtils.copyProperties(bak, obj);
						
						bak.setFolderId(null);
						bak.setToaArchiveFileBaks(null);
						folderBakManager.backupFolder(bak); // 备份

						// 获取案卷下的文档
						List<ToaArchiveFile> fileList = folderManager
								.getArchiveFileByFolderId(obj.getFolderId(),
										null);
						if (fileList.size() > 0) { // 如果案卷下有文档
							for (int j = 0; j < fileList.size(); j++) {
								ToaArchiveFile fileobj = fileList.get(j); // 文档对象

								ToaArchiveFileBak fileBak = new ToaArchiveFileBak(); // 创建文档备份对象
								fileBak.setFileAuthor(fileobj.getFileAuthor());
								fileBak.setFileDate(fileobj.getFileDate());
								fileBak.setFileDepartment(fileobj.getFileDepartment());
								fileBak.setFileDesc(fileobj.getFileDesc());
								fileBak.setFileDocId(fileobj.getFileDocId());
								fileBak.setFileDocType(fileobj.getFileDocType());
								fileBak.setFileNo(fileobj.getFileNo());
								fileBak.setFilePage(fileobj.getFilePage());
								fileBak.setFileTitle(fileobj.getFileTitle());
								//BeanUtils.copyProperties(fileBak, fileobj); // 拷贝文档对象到文档备份对象中
								fileBak.setFileId(null);
								fileBak.setToaArchiveFileAppendBaks(null);
								fileBak.setToaArchiveFolderBak(bak);
								fileBakManager.backupFile(fileBak); // 备份

								if (fileobj!=null&&fileobj.getToaArchiveFileAppends()!=null&&fileobj.getToaArchiveFileAppends().size() > 0) {
									Iterator<ToaArchiveFileAppend> iter=fileobj.getToaArchiveFileAppends().iterator();
									while(iter.hasNext()){
									ToaArchiveFileAppendBak annexBak = new ToaArchiveFileAppendBak(); // 创建文档附件备份对象
									ToaArchiveFileAppend annexObj = iter.next();
									BeanUtils
											.copyProperties(annexBak, annexObj); // 拷贝文档附件对象到文档附件备份对象中
									annexBak.setToaArchiveFileBak(fileBak);
									annexBak.setAppendId(null);
									annexBakManager.backupAnnex(annexBak); // 备份
									annexManger.delFileAppend(annexObj
											.getAppendId()); // 删除文档附件
									}
								}

								fileManager.delFile(fileobj.getFileId()); // 删除文档
							}
						}

						folderManager.delete(obj); // 删除案卷
						msg = "审核成功！";
					}
				}
			}
		} catch (ServiceException e) {
			 throw new ServiceException(MessagesConst.operation_error,               
						new Object[] {"案卷销毁"});
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 拷贝案卷对象到案卷备份对象中
		return msg;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4上午11:45:25
	 * @desc: 保存销毁案卷、销毁文件
	 * @param ToaArchiveDestroy
	 *            destrArchive 销毁案卷对象
	 * @param String
	 *            folderId 销毁文件
	 * @return void
	 */
	public String saveDestrFolder(ToaArchiveDestroy destrArchive,
			String folderId,OALogInfo ... loginfos)throws SystemException,ServiceException {
		String isSuccess = "no";
		try {
			if (folderId != null && !"".equals(folderId)
					&& !"null".equals(folderId)) {
				destrDao.save(destrArchive); // 保存案卷销毁申请单
				fileDestrManager.saveDestrFile(destrArchive, folderId); // 保存该案卷的销毁文件
				isSuccess = "yes";
			}
		} catch (ServiceException e) {
			 throw new ServiceException(MessagesConst.operation_error,               
						new Object[] {"销毁审核"});
		}
		return isSuccess;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4上午11:47:00
	 * @desc: 根据销毁状态获取分页销毁案卷列表
	 * @param Page
	 *            page 分页对象
	 * @param String
	 *            status 销毁案卷状态
	 * @return Page 分页销毁案卷列表
	 */
	@Transactional(readOnly = true)
	public Page<ToaArchiveDestroy> getAllDestrFolder(Page page, String status,OALogInfo ... loginfos)throws SystemException,ServiceException {
		String hql = "";
		try {
			if (status != null && !"".equals(status) && !"null".equals(status)) // 待审核
				hql = "from ToaArchiveDestroy t where t.destroyAuditingType="
						+ status + " order by t.destroyApplyTime desc";
			else
				hql = "from ToaArchiveDestroy t order by  t.destroyAuditingType";
		} catch (ServiceException e) {
			 throw new ServiceException(MessagesConst.find_error,               
						new Object[] {"销毁案卷列表"});
		}
		return destrDao.find(page, hql);
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4上午11:50:07
	 * @desc: 根据查询条件查询销毁案卷
	 * @param Page
	 *            page 分页对象
	 * @param ToaArchiveDestroy
	 *            objs 销毁案卷对象
	 * @param String
	 *            status 案卷状态
	 * @return Page 销毁案卷分页列表
	 */
	public Page<ToaArchiveDestroy> searchDestrFolder(Page<ToaArchiveDestroy> page, ToaArchiveDestroy objs,
			String status,OALogInfo ... loginfos)throws SystemException,ServiceException {
		try{
			Object[] obj = new Object[5];
			int i = 0;
			String hql = "";
			if (status != null && !"".equals(status) && !"null".equals(status)) // 待审核
				hql = "from ToaArchiveDestroy t where t.destroyAuditingType="
						+ status;
			else
				hql = "from ToaArchiveDestroy t where 1=1 ";
			//Query query = destrDao.createQuery(hql);
			// 销毁案卷编号
			if (objs.getDestroyFolderNo() != null
					&& !"".equals(objs.getDestroyFolderNo())
					&& !"null".equals(objs.getDestroyFolderNo())) {
				hql += " and t.destroyFolderNo like ?";
				obj[i] = "%" + objs.getDestroyFolderNo() + "%";
				i++;
			}
			// 销毁案卷名称
			if (objs.getDestroyFolderName() != null
					&& !"".equals(objs.getDestroyFolderName())
					&& !"null".equals(objs.getDestroyFolderName())) {
				hql += " and t.destroyFolderName like ?";
				obj[i] = "%" + objs.getDestroyFolderName() + "%";
				i++;
			}
			// 案卷销毁申请时间
			if (objs.getDestroyApplyTime() != null
					&& !"".equals(objs.getDestroyApplyTime())
					&& !"null".equals(objs.getDestroyApplyTime())) {
				hql += " and t.destroyApplyTime >=?";
				obj[i] = objs.getDestroyApplyTime();
				i++;
			}
			// 销毁案卷创建时间
			if (objs.getDestroyFolderDate() != null
					&& !"".equals(objs.getDestroyFolderDate())
					&& !"null".equals(objs.getDestroyFolderDate())) {
				hql += " and t.destroyFolderDate >= ?";
				obj[i] = objs.getDestroyFolderDate();
				i++;
			}
			hql += " order by  t.destroyAuditingType,t.destroyApplyTime desc";// 按销毁案卷状态和案卷销毁申请时间排序
			// if(i==0){
			// page =this.getAllDestrFolder(page, status);
			// }
			// else if(i==1){
			// query = destrDao.createQuery(hql.toString(), obj[0]);
			// }
			// else if(i==2){
			// query = destrDao.createQuery(hql.toString(),obj[0],obj[1]);
			// }
			// else if(i==3){
			// query = destrDao.createQuery(hql.toString(),obj[0],obj[1],obj[2]);
			// }else if(i==4){
			// query =
			// destrDao.createQuery(hql.toString(),obj[0],obj[1],obj[2],obj[3]);
			// }
			// if(query!=null&&i!=0){
			// List list = query.list();
			// page.setResult(list);
			// page.setTotalCount(list.size());
			// }
			Object[] param = new Object[i];
			for (int k = 0, t = 0; k < obj.length; k++) {
				if (obj[k] != null) {
					param[t] = obj[k];
					t++;
				}
			}
			return destrDao.find(page, hql, param);
		} catch (ServiceException e) {
			 throw new ServiceException(MessagesConst.find_error,               
						new Object[] {"销毁案卷分页列表"});
		}
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4上午11:53:54
	 * @param destroyId 销毁案卷id
	 * @@desc 销毁审核
	 * @return java.lang.String
	 * @roseuid 4959C80C03C8
	 */
	public String auditDestrFolder(String destroyId) {
		return null;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4上午11:53:54
	 * @desc: 根据销毁案卷id获取销毁文件列表
	 * @param String
	 *            destroyId 销毁案卷id
	 * @return List 销毁文件列表
	 */
	public List getDestrFile(String destroyId,OALogInfo ... loginfos)throws SystemException,ServiceException {
		List<ToaArchiveDestroyFile> list = null;
		try {
			list = fileDestrManager.getDestrFile(destroyId);
		} catch (ServiceException e) {
			 throw new ServiceException(MessagesConst.find_error,               
						new Object[] {"销毁文件列表"});
		}
		return list;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4上午11:54:53
	 * @desc: 根据案卷id获取案卷对象
	 * @param String folderId 案卷id
	 * @return ToaArchiveFolder 案卷对象
	 */
	public ToaArchiveFolder getFolder(String folderId,OALogInfo ... loginfos)throws SystemException,ServiceException {
		ToaArchiveFolder obj = null;
		try {
			if (folderId != null && !"".equals(folderId)
					&& !"null".equals(folderId))
				obj = folderManager.getFolder(folderId);
		} catch (ServiceException e) {
			 throw new ServiceException(MessagesConst.find_error,               
						new Object[] {" 案卷对象"});
		}
		return obj;
	}

	/**
	 * @author：pengxq
	 * @time：2009-1-4下午12:18:13
	 * @desc: 更新案卷状态
	 * @param ToaArchiveFolder
	 *            obj 案卷对象
	 * @param String
	 *            status 案卷状态
	 * @return void
	 */
	public String updateStatus(ToaArchiveFolder obj, String status,OALogInfo ... loginfos)throws SystemException,ServiceException {
		String msg="";
		try {
			obj.setFolderAuditing(status);
			folderManager.saveFolder(obj);
			msg="案卷销毁成功！";
		} catch (ServiceException e) {
			msg="案卷销毁失败！";
			throw new ServiceException(MessagesConst.operation_error,               
					new Object[] {" 更改销毁案卷状态"});
		}
		return msg;
	}

	@Autowired
	public void setFileBakManager(FileBackupManager fileBakManager) {
		this.fileBakManager = fileBakManager;
	}

	@Autowired
	public void setAnnexBakManager(AnnexBackupManager annexBakManager) {
		this.annexBakManager = annexBakManager;
	}

	@Autowired
	public void setFileDestrManager(FileDestrManager fileDestrManager) {
		this.fileDestrManager = fileDestrManager;
	}

	@Autowired
	public void setFolderBakManager(FolderBackupManager folderBakManager) {
		this.folderBakManager = folderBakManager;
	}

	@Autowired
	public void setFolderManager(ArchiveFolderManager folderManager) {
		this.folderManager = folderManager;
	}

	@Autowired
	public void setAnnexManger(ArchiveFileAppendManager annexManger) {
		this.annexManger = annexManger;
	}

	@Autowired
	public void setFileManager(ArchiveFileManager fileManager) {
		this.fileManager = fileManager;
	}

	public void update(ToaArchiveDestroy model ,OALogInfo ... loginfos) {
		destrDao.update(model);
	}
}
