package com.strongit.oa.prsnfldr.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.strongit.oa.util.PropertiesUtil;
import com.strongmvc.exception.SystemException;

public class FileUtil {

	private static String tempext = "2008_.tmp";

	/**
	 * 验证文件大小是否为空
	 * 
	 * @param size
	 * @return
	 */
	public static boolean isEmptyFile(long size) {
		if (size == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 验证文件大小是否超过预先设置的大小(20M)
	 */
	public static boolean isMax(File[] file) {
		long size = 0;
		for (int i = 0; i < file.length; i++) {
			size += file[i].length();
		}
		if (size > 20971520) {
			return true;
		}
		return false;
	}

	/**
	 * 获取文件总大小
	 */
	public static long getTotalSize(File[] file) {
		long temp = 0;
		for (int i = 0; i < file.length; i++) {
			temp += file[i].length();
		}
		return temp;
	}

	/**
	 * 写入文件
	 */
	public static void writeFile(String path, String content, boolean append) {
		BufferedWriter bf = null;
		FileWriter writer = null;
		try {
			String temp = path;
			File file = new File(temp);
			if (!file.exists()) {
				file.createNewFile();
				file = new File(temp);
			}
			writer = new FileWriter(file, append);
			bf = new BufferedWriter(writer);
			bf.write(content);
			bf.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取文件流
	 */
	public static InputStream getInputStream(String path) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path + tempext);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fis;
	}

	/**
	 * 字符串转字节流
	 */
	public static InputStream getInputStreamByString(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}

	/**
	 * 获取上传文件的最大大小[单位B] 若配置文件中未做配置，默认为2097152 {文件柜限制}
	 * 
	 * @author:邓志城
	 * @date:2009-6-23 下午03:57:01
	 * @return
	 * @throws Exception
	 */
	public static int getIntFileUploadMaxSize() throws Exception {
		String maxSize = PropertiesUtil.getPropertyByFileName(
				"struts.multipart.file.maxSize", "struts.properties");
		return (maxSize == null || maxSize == "") ? 2097152 : Integer.parseInt(maxSize);
	}

	/**
	 * 获取上传文件的最大大小【单位M】
	 * 
	 * @author:邓志城
	 * @date:2009-6-23 下午04:01:31
	 * @return
	 * @throws Exception
	 */
	public static String getStringFileUploadMaxSize() throws Exception {
		int maxSize = getIntFileUploadMaxSize();
		String strMaxSize = String.valueOf(Round.round(maxSize / (1024 * 1024),
				2))
				+ "MB";
		return strMaxSize;
	}

	/**
	 * 压缩文件
	 * 
	 * @author:邓志城
	 * @date:2009-7-17 下午05:21:50
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(byte[] input) throws Exception {
		byte[] comp = null;
		try {
			Deflater compressor = new Deflater();
			compressor.setLevel(Deflater.BEST_COMPRESSION);
			compressor.setInput(input);
			compressor.finish();
			ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
			byte[] buf = new byte[8192];
			while (!compressor.finished()) {
				int count = compressor.deflate(buf);
				bos.write(buf, 0, count);
			}
			bos.close();
			comp = bos.toByteArray();
		} catch (Exception e) {
			throw new Exception("压缩文件失败：" + e);
		}
		return comp;
	}

	/**
	 * 解压文件
	 * 
	 * @author:邓志城
	 * @date:2009-7-17 下午05:27:12
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static byte[] decompress(byte[] input) throws Exception {
		byte[] decomp = null;
		try {
			Inflater decompressor = new Inflater();
			decompressor.setInput(input);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
			byte[] buf = new byte[1024];
			while (!decompressor.finished()) {
				int count = decompressor.inflate(buf);
				bos.write(buf, 0, count);
			}
			bos.close();
			decomp = bos.toByteArray();
		} catch (Exception e) {
			throw new Exception("解压文件失败：" + e);
		}
		return decomp;
	}

	/**
	 * 将一个字节数组对象转换成一个文件对象
	 * 
	 * @author:邓志城
	 * @date:2009-7-17 下午05:45:38
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static File byteArray2File(byte[] input) throws Exception {
		File file = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			// C:\DOCUME~1\ADMINI~1\LOCALS~1\Temp\test52933.temp
			file = File.createTempFile("test", ".temp");// 创建临时文件
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(input);
			file.deleteOnExit();
		} catch (Exception e) {
			throw new SystemException("字节数组转成文件异常：" + e);
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return file;
	}

	/**
	 * 将一个输入流转成字节数组
	 * 
	 * @author:邓志城
	 * @date:2009-7-17 下午05:50:34
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static byte[] inputstream2ByteArray(InputStream is) throws Exception {
		byte[] buf = null;
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

	/**
	 * 把Blob类型转换为byte数组类型
	 * 
	 * @param blob
	 * @return
	 */
	public static byte[] blobToBytes(Blob blob) {

		BufferedInputStream is = null;

		try {
			is = new BufferedInputStream(blob.getBinaryStream());
			byte[] bytes = new byte[(int) blob.length()];
			int len = bytes.length;
			int offset = 0;
			int read = 0;

			while (offset < len
					&& (read = is.read(bytes, offset, len - offset)) >= 0) {
				offset += read;
			}
			return bytes;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				return null;
			}
		}
	}

	/**
	 * 将一个字节数组专程输入流
	 * 
	 * @author:邓志城
	 * @date:2009-7-17 下午05:53:31
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static InputStream ByteArray2InputStream(byte[] input)
			throws Exception {
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(input);
		} catch (Exception e) {
			throw new SystemException("字节数组转输入流异常：" + e);
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return is;
	}

	/**
	 * 输入流转到输入流
	 * 
	 * @author:邓志城
	 * @date:2009-7-17 下午06:00:52
	 * @param os
	 * @param is
	 * @throws Exception
	 */
	public static void inputStream2OutputStream(OutputStream os, InputStream is)
			throws Exception {
		try {
			int read = 0;
			byte[] buf = new byte[1024];
			while ((read = is.read(buf)) != -1) {
				os.write(buf, 0, read);
			}
		} catch (Exception e) {
			throw new SystemException("输入流转输入流异常：" + e);
		} finally {
			if (os != null) {
				os.close();
			}
			if (is != null) {
				is.close();
			}
		}
	}
}
