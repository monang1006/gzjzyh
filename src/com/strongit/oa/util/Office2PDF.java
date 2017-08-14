package com.strongit.oa.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

/**
 * 利用Openoffice,将Office文档转换为PDF文档
 * 
 * @author 李骏
 * 
 */
public class Office2PDF {

	/**
	 * 服务是否运行
	 */
	public static String serviceRunState="0";
	/**
	 * 环境变量下面的openOffice.properties的绝对路径
	 */
	private static final String RUL_PATH = Thread.currentThread()
			.getContextClassLoader().getResource("").getPath()
			.replace("%20", " ")
			+ "openOffice.properties";

	/**
	 * 将Office文档转换为PDF. 运行该函数需要用到OpenOffice, OpenOffice下载地址为
	 * http://www.openoffice.org/
	 * 
	 * <pre>
	 * 方法示例:
	 * String sourcePath = "d:\\document1.doc";
	 * String destFile = "d:\\test.pdf";
	 * Converter.office2PDF(sourcePath, destFile);
	 * </pre>
	 * 
	 * @param sourceFile
	 *            源文件, 绝对路径. 可以是Office2003-2007全部格式的文档, Office2010的没测试. 包括.doc,
	 *            .docx, .xls, .xlsx, .ppt, .pptx等. 示例: F:\\office\\source.doc
	 * @param destFile
	 *            目标文件. 绝对路径. 示例: F:\\pdf\\dest.pdf
	 * @return 操作成功与否的提示信息. 如果返回 -1, 表示找不到源文件, 或url.properties配置错误; 如果返回 0,
	 *         则表示操作成功; 返回1, 则表示转换失败
	 */
	public static int office2PDF(String sourceFile, String destFile) {
		try {
			File inputFile = new File(sourceFile);
			if (!inputFile.exists()) {
				return -1;// 找不到源文件, 则返回-1
			}

			// 如果目标路径不存在, 则新建该路径
			File outputFile = new File(destFile);
			if (!outputFile.getParentFile().exists()) {
				outputFile.getParentFile().mkdirs();
			}

			/*
			 * 从url.properties文件中读取OpenOffice的安装根目录, OpenOffice_HOME对应的键值.
			 */
			Properties prop = new Properties();
			FileInputStream fis = null;
			fis = new FileInputStream(RUL_PATH);// 属性文件输入流
			prop.load(fis);// 将属性文件流装载到Properties对象中
			fis.close();// 关闭流

			String OpenOffice_HOME = prop.getProperty("OpenOffice_HOME");
			if (OpenOffice_HOME == null)
				return -1;
			// 如果从文件中读取的URL地址最后一个字符不是 '\'，则添加'\'
			if (OpenOffice_HOME.charAt(OpenOffice_HOME.length() - 1) != '\\') {
				//OpenOffice_HOME += "\\";
			}
			// 启动OpenOffice的服务
			String command = OpenOffice_HOME+
					" -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard";
			Process pro = Runtime.getRuntime().exec(command);
			// connect to an OpenOffice.org instance running on port 8100
			OpenOfficeConnection connection = new SocketOpenOfficeConnection(
					"127.0.0.1", 8100);
			connection.connect();

			// convert
			DocumentConverter converter = new OpenOfficeDocumentConverter(
					connection);
//			converter.convert(inputFile, outputFile);
			/*
			 * 四个参数，inputFile问输入流，output为输出流，输入流和输出流的格式都为NULL即可
			 */
			converter.convert(inputFile, null, outputFile, null);
			
			// close the connection
			connection.disconnect();
			// 关闭OpenOffice服务的进程
			pro.destroy();

			return 0;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		return 1;
	}
	

	/**word转PDF：传入doc格式的输入流，返回pdf格式的输出流
	 * @param is
	 * @return
	 * author 申仪玲
	 */
	public static OutputStream office2PDF(InputStream is) {		
		OutputStream os = null;	
		Properties prop = new Properties();
		FileInputStream fis = null;
		OpenOfficeConnection connection = null;
		Process pro = null;		
		try {						
		    //从openOffice.properties文件中读取OpenOffice的安装根目录, OpenOffice_HOME对应的键值.			
			fis = new FileInputStream(RUL_PATH);// 属性文件输入流
			prop.load(fis);// 将属性文件流装载到Properties对象中
			fis.close();// 关闭流

			String OpenOffice_HOME = prop.getProperty("OpenOffice_HOME");
			if (OpenOffice_HOME == null)
				return os;
			/**
			// 如果从文件中读取的URL地址最后一个字符不是 '\'，则添加'\'
			if (OpenOffice_HOME.charAt(OpenOffice_HOME.length() - 1) != '\\') {
				OpenOffice_HOME += "\\";
			}
			*/
			
			boolean b = checkPortInUse(8100);
			if(b == false){
				// 启动OpenOffice的服务
				String command = OpenOffice_HOME+
						" -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard";
				pro = Runtime.getRuntime().exec(command);
			}

			// connect to an OpenOffice.org instance running on port 8100
			connection = new SocketOpenOfficeConnection(
					"127.0.0.1", 8100);
			connection.connect();
			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
			//四个参数，is输入流，os为输出流，输入流和输出流的格式都为NULL即可
			//converter.convert(InputStream, arg1, OutputStream, arg3)
			
			DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry(); 
			DocumentFormat doc = formatReg.getFormatByFileExtension("doc"); 
			DocumentFormat pdf = formatReg.getFormatByFileExtension("pdf"); 
			os = new ByteArrayOutputStream(); 
			converter.convert(is, doc, os, pdf);
			serviceRunState="1";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		}catch(Exception e){
			e.printStackTrace(); 
		}finally{
			
			if(connection != null){
				try{
					//关闭连接
					connection.disconnect();
				}catch(Exception e){
					e.printStackTrace(); 
				}
			}
			if(pro != null){
				try{
					// 关闭OpenOffice服务的进程
					pro.destroy();
				}catch(Exception e){
					e.printStackTrace(); 
				}

			}
		}
		return os;
	}
	
	
	public static boolean checkPortInUse(int port){
		String host = "localhost";
		try {
			InetAddress theAddress = InetAddress.getByName(host);
			try {
				Socket theSocket = new Socket(theAddress, port);
				System.out.println("port " + port + " 已经被占用");
				return true;
			}
			catch (IOException e) {
				System.out.println("port " + port + " 没有被占用");
			}
		}catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return false;
	} 
	
}
