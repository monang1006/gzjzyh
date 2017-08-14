package com.strongit.oa.noticeconference.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.util.Assert;

import com.strongit.oa.util.PathUtil;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/*******************************************************************************
 * 文件服务工具类
 * 
 * @Description: FileUtil.java
 * @Date:Apr 2, 2013
 * @Author 万俊龙
 * @Version: V1.0
 * @Copyright: Jiang Xi Strong Co. Ltd. All right reserved.
 */
public class FileUtils {
	/***************************************************************************
	 * 
	 * 方法简要描述： 获取所在文件的文件流
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param attachFilePath
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @version 1.0
	 * @see
	 */
	public static InputStream getAttachFileStream(String attachFilePath)
			throws ServiceException, DAOException, SystemException {
		try {
			return new FileInputStream(getAttachFile(attachFilePath));
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
	 * 方法简要描述： 获取附件文件
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param attachFilePath
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @version 1.0
	 * @see
	 */
	public static File getAttachFile(String attachFilePath)
			throws ServiceException, DAOException, SystemException {
		Assert.notNull(attachFilePath);
		try {
			return new File(getAttachDirectory() + File.separator
					+ attachFilePath);
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
	 * 方法简要描述：获取附件存放目录地址
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param attachFilePath
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @version 1.0
	 * @see
	 */
	public static String getAttachDirectory() throws ServiceException,
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

	/***
	 * 
	* 方法简要描述
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 23, 2013
	* @Author 万俊龙
	* @param is
	* @return
	* @throws Exception
	* @version	1.0
	* @see
	 */
	public static byte[] inputstream2ByteArray(InputStream is) throws Exception {
		byte[] buf = (byte[]) null;
		ByteArrayOutputStream bos = null;
		try {
			byte[] b = new byte[8192];
			int read = 0;
			bos = new ByteArrayOutputStream();
			while ((read = is.read(b)) != -1) {
				bos.write(b, 0, read);
			}
			buf = bos.toByteArray();
		} catch (Exception e) {
			throw new SystemException("输入流转字节数组异常：" + e);
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (is != null) {
				is.close();
			}
		}
		return buf;
	}

}
