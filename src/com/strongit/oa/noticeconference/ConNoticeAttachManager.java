package com.strongit.oa.noticeconference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.TOmConAttach;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.noticeconference.util.FileUtils;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.util.UUIDGenerator;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/*******************************************************************************
 * 通知会议附件管理类
 * 
 * @Description: ConAttachManager.java
 * @Date:Apr 2, 2013
 * @Author 万俊龙
 * @Version: V1.0
 * @Copyright: Jiang Xi Strong Co. Ltd. All right reserved.
 */

@Service
@Transactional
public class ConNoticeAttachManager implements IConNoticeAttachManager {
	private GenericDAOHibernate<TOmConAttach, String> attachDao;
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		attachDao = new GenericDAOHibernate<TOmConAttach, String>(
				sessionFactory, TOmConAttach.class);
	}

	@Autowired
	private IUserService userService;

	@Autowired
	private MyLogManager logService;

	/**
	 * 
	 * 方法简要描述：根据附件主键Id,获取附件实体对象信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param conAttachId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConAttach getConAttachByAttachId(String conAttachId) {
		Assert.notNull(conAttachId);
		TOmConAttach model = this.attachDao.get(conAttachId);
		byte[] attachContent = null;
		InputStream is = null;

		try {
			is = FileUtils.getAttachFileStream(model.getAttachFilePath());
			if (is == null) {
				attachContent = null;
			} else {
				attachContent = FileUtil.inputstream2ByteArray(is);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException(e);
		} finally {
			if (is != null) {
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

	/***************************************************************************
	 * 
	 * 方法简要描述: 获取会议通知的附件（不包括单位回执的附件）
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param attachConId
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @version 1.0
	 * @see
	 */
	private List<TOmConAttach> getConAttachsByAttachConId(String attachConId)
			throws ServiceException, DAOException, SystemException {
		Assert.notNull(attachConId);
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("from TOmConAttach t where t.conferenceId=?");
			List<TOmConAttach> list = attachDao.find(hql.toString(),
					new Object[] { attachConId });
			return list;
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

	/***************************************************************************
	 * 
	 * 方法简要描述：根据会议通知id获取附件
	 * 
	 * @param containsAttach：
	 *            是否附件文件内容[true:包含|false:不包含] 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param attachConId
	 * @param containsAttach：
	 *            是否附件文件内容[true:包含|false:不包含]
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @version 1.0
	 * @see
	 */
	@SuppressWarnings("unchecked")
	public List<TOmConAttach> getAttachsByAttachConId(String attachConId,
			boolean containsAttach) throws ServiceException, DAOException,
			SystemException {
		try {
			List<TOmConAttach> list = getConAttachsByAttachConId(attachConId);
			if (containsAttach) {
				byte[] attachContent = null;
				InputStream is = null;
				for (int i = 0; i < list.size(); i++) {
					TOmConAttach model = list.get(i);
					try {
						is = FileUtils.getAttachFileStream(model
								.getAttachFilePath());
						if (is == null) {
							attachContent = null;
						} else {
							attachContent = FileUtil.inputstream2ByteArray(is);
						}
					} catch (Exception e) {
						e.printStackTrace();
						throw new SystemException(e);
					} finally {
						if (is != null) {
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

	/***************************************************************************
	 * 
	 * 方法简要描述：删除附件信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param id
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @version 1.0
	 * @see
	 */
	public void deleteConAttach(String id) throws ServiceException,
			DAOException, SystemException {
		try {
			deleteConAttach(attachDao.get(id));
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

	/***************************************************************************
	 * 
	 * 方法简要描述：删除附件信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @version 1.0
	 * @see
	 */
	public void deleteConAttach(TOmConAttach entity) throws ServiceException,
			DAOException, SystemException {
		try {
			File attchFile = FileUtils
					.getAttachFile(entity.getAttachFilePath());
			if (attchFile.exists()) {
				attchFile.delete();
				logger.info("删除文件" + entity.getAttachFileName());
			} else {
				logger.error("文件" + entity.getAttachFileName() + "不存在！");
			}
			attachDao.delete(entity);

			ToaLog log = new ToaLog();
			try {
				InetAddress inet = InetAddress.getLocalHost();
				log.setOpeIp(inet.getHostAddress()); // 操作者IP地址
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			log.setOpeUser(userService.getCurrentUser().getUserName()); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo("{删除}会议通知[id=" + entity.getAttachId() + "]的附件文件【"
					+ entity.getAttachFileName() + "】");// 日志信息
			logService.saveObj(log);
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

	/***************************************************************************
	 * 
	 * 方法简要描述：批量删除会议中的附件
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param attachConId
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @version 1.0
	 * @see
	 */
	public void deleteConAttchByAttachConId(String attachConId)
			throws ServiceException, DAOException, SystemException {
		try {
			List<TOmConAttach> list = getConAttachsByAttachConId(attachConId);
			if (list != null && !list.isEmpty()) {
				for (TOmConAttach temp : list) {
					deleteConAttach(temp);
				}
			}
		} catch (ServiceException ex) {
			// s: handle ServiceException
			throw ex;
		} catch (DAOException ex) {
			// s: handle DAOException
			throw ex;
		} catch (SystemException ex) {
			// s: handle SystemException
			throw ex;
		} catch (Exception ex) {
			// s: handle Exception
			throw new SystemException(ex);
		}
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：保存会议通知附件信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param model
	 * @version 1.0
	 * @see
	 */
	public void saveConAttach(TOmConAttach model) {
		byte[] attachContent = model.getAttachContent();
		String attachPath = "";
		if (model.getAttachId() == null) {// 添加附件
			UUIDGenerator generator = new UUIDGenerator();
			attachPath = generator.generate().toString();
		} else {// 修改附件
			attachPath = model.getAttachFilePath();
		}
		model.setAttachFilePath(attachPath);
		model.setAttachContent(null);
		try {
			if (attachContent == null) {
				return;
			}
			InputStream attachment = FileUtil
					.ByteArray2InputStream(attachContent);
			System.out.println();
			System.out.println();
			String attachmentPath = FileUtils.getAttachDirectory();
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
			log.setOpeUser(userService.getCurrentUser().getUserName()); // 操作姓名
			log.setLogState("1"); // 日志状态
			log.setOpeTime(new Date()); // 操作时间
			log.setLogInfo("{添加}会议通知[id=" + model.getAttachId() + "]的附件文件【"
					+ model.getAttachFileName() + "】");// 日志信息

		} catch (SystemException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
	}

}
