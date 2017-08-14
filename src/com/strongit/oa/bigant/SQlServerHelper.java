package com.strongit.oa.bigant;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 连接数据库类
 *	@author 李俊勇
 *	@date Apr 19, 2010 2:16:01 PM
 */
public class SQlServerHelper {
	private static Logger logger = Logger.getLogger(BigantManager.class);
	
	private static String driverClassName;		
	private static String sqlURL;
	private static String username;
	private static String password;

	static{
		Properties properties = new Properties();
		URL url = BigantManager.class.getClassLoader().getResource("bigant.properties");
		try 
		{
			properties.load(new FileInputStream(URLDecoder.decode(url.getFile(), "UTF-8")));
			driverClassName = properties.getProperty("jdbc.driverClassName");
			sqlURL = properties.getProperty("jdbc.url");
			username = properties.getProperty("jdbc.username");
			password = properties.getProperty("jdbc.password");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("无法找到配置文件");
		}
	}
	/**
	 *  获取数据库连接
	 *	@author 李俊勇
	 *	@date 2010 2:17:05 PM
	 * 	@return
	 */
	public static Connection getConnection()
	{
		 Connection dbConn;
		 try {
			 Class.forName(driverClassName);
			 dbConn = DriverManager.getConnection(sqlURL, username, password);
			 return dbConn;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取数据连接Connection出错！");
			return null;
		}
	}
	/**
	 * 关闭数据库连接和游标
	 *	@author 李俊勇
	 *	@date 2010 2:17:25 PM
	 * 	@param con
	 * 	@param rs
	 */
	public static void close(Connection con,ResultSet rs)
	{
		try {
			if(con != null)
			{
				con.close();
				con = null;
			}
			if(rs != null)
			{
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("关闭连接或游标出错！");
		}
	}
}
