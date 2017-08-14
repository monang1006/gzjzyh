package com.strongit.oa.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import oracle.sql.BLOB;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.prsnfldr.util.Round;
import com.strongit.oa.util.PathUtil;
import com.strongit.oa.util.UUIDGenerator;
import com.strongmvc.exception.SystemException;

public class PdfTempFileHelper {

	private static Logger logger = LoggerFactory
			.getLogger(PdfTempFileHelper.class);

	private static final int INVALID_TIME = 36000;// 7200000;//失效时间为2个小时

	private static ConcurrentHashMap<String, Long> mapFormPdfTempFile = new ConcurrentHashMap<String, Long>();
	private static final String Pdfpath = File.separatorChar + "pdfFile";
	private static final String PDFpath = File.separatorChar + "pdftemp";

	/**
	 * @author:luosy
	 * @description: 保存PDF临时文件
	 * @date : 2011-12-18
	 * @param path
	 *            PDF临时文件夹
	 * @param is
	 *            PDF文件
	 * @param fileName
	 *            PDF文件名 文件名格式 ： 1、添加附件 数据： T_OARECVDOC;OARECVDOCID;
	 *            411628ef06ab41ab884fc20c04350c31;PDFCONTENT;PDFCONTENT_SIZE;232
	 *            4 2 说明： 表名;主键ID;主键值;pdf文件字段;pdf文件size字段;控件计算出的附件size 1、修改附件
	 *            数据： T_OARECVDOC;OARECVDOCID;
	 *            411628ef06ab41ab884fc20c04350c31;PDFCONTENT;PDFCONTENT_SIZE;232
	 *            4 2 说明： 表名;主键ID;主键值;pdf文件字段;pdf文件size字段;控件计算出的附件size
	 * @return
	 */
	public static synchronized String saveFile(String path, InputStream is,
			String fileName) {
		deleteInvalid();
		// String path = PathUtil.getRootPath() + ATTACHMENT_PATH +
		// File.separatorChar;// 得到相对工程目录的路径
		// String newPath;
		// File file = new File(path);
		// if (!file.exists()) {
		// if(!file.mkdir()) {
		// throw new SystemException("创建目录 " + path + " 目录不存在。");
		// }
		// }
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// String now = sdf.format(new Date());// 当前时间
		// int index = fileName.lastIndexOf(".");
		// String ext = fileName.substring(index, fileName.length());
		// Random random = new Random();
		// String randomNum = random.nextInt(9) + "" + random.nextInt(9) + ""
		// + random.nextInt(9) + "" + random.nextInt(9) + ""
		// + random.nextInt(9);
		// String newFileName = now + randomNum + ext;// 通过当前时间和5位随机数参数新的文件名
		// newPath = cxtPath + File.separatorChar + ATTACHMENT_PATH +
		// File.separatorChar + newFileName;
		// file = new File(path + newFileName);
		//		
		// if (file.exists()) {// 如果还是存在了重复的名称,则通过UUID来生成文件名
		// UUIDGenerator uuid = new UUIDGenerator();
		// String strUUID = uuid.generate().toString();
		// newPath = cxtPath + File.separatorChar + ATTACHMENT_PATH +
		// File.separatorChar + strUUID + ext;
		// file = new File(path + strUUID + ext);
		// }

		path += PDFpath;

		File file = new File(path);
		if (!file.exists()) {
			if (!file.mkdir()) {
				throw new SystemException("创建目录 " + path + " 目录不存在。");
			}
		}
		String PrefileName = "";
		String[] data = fileName.split(";");

		if (data.length < 4) { // 新建表单没有得到业务表ID
			// data=“PDFCONTENT;PDFCONTENT_SIZE”
			UUIDGenerator uuid = new UUIDGenerator();
			String strUUID = uuid.generate().toString();
			PrefileName = strUUID + ";" + fileName;
			fileName = strUUID + ";" + fileName;

		} else { // 在流程中得到业务表ID
			// data=“T_OARECVDOC;OARECVDOCID;411628ef06ab41ab884fc20c04350c31;PDFCONTENT;PDFCONTENT_SIZE;23242”

			String tableName = data[0];
			String pkFieldName = data[1];
			String pkFieldValue = data[2];
			String PDFCONTENT = data[3];
			String PDFCONTENT_SIZE = data[4];
			PrefileName = tableName + ";" + pkFieldName + ";" + pkFieldValue
					+ ";" + PDFCONTENT + ";" + PDFCONTENT_SIZE;
		}

		File inpathfiles = new File(path);
		if (inpathfiles.exists()) {
			String[] list = inpathfiles.list(new DirFilter(PrefileName));
			File oldfile = null;
			for (int i = 0; i < list.length; i++) {
				oldfile = new File(path + File.separatorChar + list[0]);
				if (oldfile.exists()) {
					oldfile.delete();
				}
			}

		}

		String newPath = path + File.separatorChar + fileName;
		file = new File(newPath);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			int BUFFER_SIZE = 8192;
			byte[] buffer = new byte[BUFFER_SIZE];
			while (true) {
				int len = is.read(buffer);
				if (len > 0) {
					fos.write(buffer, 0, len);
				} else if (len < 0) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		mapFormPdfTempFile.put(newPath, System.currentTimeMillis());
		return fileName;
	}

	/***
	 * 获得pdf的大小
	 * 
	 * @param path
	 * @param pdfContentInfo
	 * @return
	 */
	public int getPdfSize(String path, String pdfContentInfo) {
		int filesize = 0;
		InputStream is = null;
		try {
			String pdffileName = "";
			File files = new File(path + Pdfpath);
			if (files.exists()) {
				String[] list = files.list(new DirFilter(pdfContentInfo));
				if (list.length > 0) {
					// pdffileName = list[0];
					// String[] pdfinfo = pdffileName.split(";");
					// filesize = new Integer(pdfinfo[pdfinfo.length-1]);
					is = new FileInputStream(path + Pdfpath
							+ File.separatorChar + pdfContentInfo);
					is.available();
					return is.available();
				}
			}
			File file = new File(path + Pdfpath + File.separatorChar
					+ pdffileName);
			file.deleteOnExit();
		} catch (Exception e) {
			return filesize;
		}
		return filesize;
	}

	/**
	 * 获得流
	 * 
	 * @param path
	 * @param pdfContentInfo
	 * @return
	 */
	public InputStream getPdfFileL(String path, String pdfContentInfo) {
		InputStream is = null;
		try {
			String pdffileName = "";
			File files = new File(path + Pdfpath);
			if (files.exists()) {
				String[] list = files.list(new DirFilter(pdfContentInfo));
				if (list.length > 0) {
					pdffileName = list[0];
					is = new FileInputStream(path + Pdfpath
							+ File.separatorChar + pdffileName);
					return is;
				}
			}
			File file = new File(path + Pdfpath + File.separatorChar
					+ pdffileName);
			file.deleteOnExit();
		} catch (Exception e) {
			return is;
		}
		return is;

	}

	/**
	 * 保存到pdf
	 * 
	 * @param path
	 * @param is
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static synchronized String savePdfFile(String path, InputStream is,
			String fileName) throws Exception {
		FileOutputStream os = null;
		String rootPath = PathUtil.getRootPath();// 得到工程根路径
		String uploadDir = "pdfFile";
		String dir = rootPath + uploadDir;
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdir();
		}
		try {
			if (is != null) {
				os = new FileOutputStream(dir + File.separatorChar + fileName);
				int len = 0;
				byte[] battach = new byte[8192];
				while ((len = is.read(battach)) != -1) {
					os.write(battach, 0, len);
				}
				logger.info("附件'" + fileName + "'生成.");
			}
		} catch (Exception e) {
			logger.error("保存公文附件失败。", e);
			throw e;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error("关闭输出流异常", e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("关闭输入流异常", e);
				}
			}
		}
		return fileName;
	}

	/**
	 * @author:luosy
	 * @description: 删除过期的临时文件
	 * @date : 2011-12-18
	 * @modifyer:
	 * @description:
	 */
	public static void deleteInvalid() {
		try {
			for (Iterator iter = mapFormPdfTempFile.entrySet().iterator(); iter
					.hasNext();) {
				Map.Entry entry = (Map.Entry) iter.next();
				Long UploadTime = (Long) entry.getValue();
				if ((UploadTime != null)
						&& (UploadTime > System.currentTimeMillis())) {
					UploadTime = System.currentTimeMillis();
				} else if ((UploadTime != null)
						&& (System.currentTimeMillis() - UploadTime >= INVALID_TIME)) {
					File file = new File(entry.getKey().toString());
					if ((file != null) && (file.exists()) && (file.delete())) {
						logger.info(file.getAbsolutePath()
								+ " 失效的PDF临时文件已成功删除。");
					}
					iter.remove();
				}
			}
		} catch (Exception e) {
			logger.error("删除失效的PDF临时文件发生错误", e);
		}
	}

	public static String getFileSize(int length_byte) {
		String size = "";
		if (length_byte >= 1024) {
			double length_k = ((double) length_byte) / 1024;
			if (length_k >= 1024) {
				size = Round.round(length_k / 1024, 2) + "MB";
			} else {
				size = length_byte / 1024 + "k";
			}
		} else {
			size = length_byte + "字节";
		}
		return size;
	}

	public int getPdfFileSize(String path, String pdfContentInfo) {
		int filesize = 0;
		try {
			String pdffileName = "";
			File files = new File(path + PDFpath);
			if (files.exists()) {
				String[] list = files.list(new DirFilter(pdfContentInfo));
				if (list.length > 0) {
					pdffileName = list[0];
					String[] pdfinfo = pdffileName.split(";");
					filesize = new Integer(pdfinfo[pdfinfo.length - 1]);
					return filesize;
				}
			}
			File file = new File(path + PDFpath + File.separatorChar
					+ pdffileName);
			file.deleteOnExit();
		} catch (Exception e) {
			return filesize;
		}
		return filesize;
	}

	/**
	 * 获取工程路径下的PDF
	 * 
	 * @param path
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	public static byte[] getPdfContent(String path) throws IOException {
		String rootPath = PathUtil.getRootPath();// 得到工程根路径
		String uploadDir = "pdfFile";
		String dir = rootPath + uploadDir;
		byte[] b = null;
		InputStream is = null;
		try {
			Blob tt = Hibernate.createBlob(new FileInputStream(dir + File.separatorChar + path));
			is = new FileInputStream(dir + File.separatorChar + path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			b = FileUtil.inputstream2ByteArray(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}

	public InputStream getPdfFile(String path, String pdfContentInfo) {
		InputStream is = null;
		try {
			String pdffileName = "";
			File files = new File(path + PDFpath);
			if (files.exists()) {
				String[] list = files.list(new DirFilter(pdfContentInfo));
				if (list.length > 0) {
					pdffileName = list[0];
					is = new FileInputStream(path + PDFpath
							+ File.separatorChar + pdffileName);
					return is;
				}
			}
			File file = new File(path + PDFpath + File.separatorChar
					+ pdffileName);
			file.deleteOnExit();
		} catch (Exception e) {
			return is;
		}
		return is;

	}

	/**
	 * @author:luosy
	 * @description: 保存PDF文件
	 * @date : 2011-12-18
	 * @modifyer:
	 * @description:
	 * @param length_byte
	 * @return
	 */
	public Boolean updatePdfFile(String path, String pdfContentInfo) {
		try {
			String[] data = pdfContentInfo.split(";");
			String tableName = data[0];
			String pkFieldName = data[1];
			String pkFieldValue = data[2];
			String PDFCONTENT = data[3];
			String PDFCONTENT_SIZE = data[4];

			String pdffileName = "";
			File files = new File(path + PDFpath);
			if (files.exists()) {
				String[] list = files.list(new DirFilter(pdfContentInfo));
				if (list.length > 0) {
					pdffileName = list[0];
					String[] pdfinfo = pdffileName.split(";");
					int filesize = new Integer(pdfinfo[5]);
					// System.out.println(path+PDFpath+ File.separatorChar
					// +pdffileName);
					InputStream is = new FileInputStream(path + PDFpath
							+ File.separatorChar + pdffileName);
					PDFreaderManager pdfManager = new PDFreaderManager();
					Boolean ret = pdfManager.SaveFiletoBD(pdfContentInfo,
							filesize, is);
					if (!ret) {
						return false;
					}
				}
			}
			File file = new File(path + PDFpath + File.separatorChar
					+ pdffileName);
			file.deleteOnExit();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static class DirFilter implements FilenameFilter {
		String afn;

		DirFilter(String afn) {
			this.afn = afn;
		}

		public boolean accept(File dir, String name) {
			// Strip path information.
			String f = new File(name).getName();
			return f.indexOf(afn) != -1;
		}
	}
}
