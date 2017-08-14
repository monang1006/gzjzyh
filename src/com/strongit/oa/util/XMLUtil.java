package com.strongit.oa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;



public class XMLUtil  {
	/**
	 * 加载流
	 * 
	 * @param is
	 * @return
	 */
	public static Document LoadXML(InputStream is) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(is);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return document;
	}

	/**
	 * 加载字节数组
	 * 
	 * @param b
	 * @return
	 */
	public static Document LoadXML(byte[] b) {
		Document document = null;
		try {
			document = LoadXML(FileUtil.ByteArray2InputStream(b));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 加载文件
	 * 
	 * @param file
	 * @return
	 */
	public static Document LoadXML(File file) {
		Document document = null;
		try {
			document = LoadXML(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 根据文件路径加载文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static Document LoadXML(String filePath) {
		Document document = null;
		document = LoadXML(new File(filePath));
//		String content = document.asXML();
//		content = content.replaceAll("\\r\\n", "");
//		content = content.replaceAll("\\n", "");
//		document = stringToloadXML(content);
		return document;
	}
	/**
	 * 加载XML文档
	 * 
	 * @author:邓志城
	 * @date:2009-7-8 下午05:20:05
	 * @param strXML
	 *            XML字符
	 */
	public static Document stringToloadXML(String strXML) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(strXML));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}
}
