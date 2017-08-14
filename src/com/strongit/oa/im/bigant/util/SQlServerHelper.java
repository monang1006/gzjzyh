package com.strongit.oa.im.bigant.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.strongmvc.exception.SystemException;

/**
 * 连接数据库类
 *	@author 李俊勇
 *	@date Apr 19, 2010 2:16:01 PM
 */
public class SQlServerHelper {
	private static Logger logger = Logger.getLogger(SQlServerHelper.class);
	
	private static String driverClassName;		
	private static String sqlURL;
	private static String username;
	private static String password;

	Connection con ;
	
	static SQlServerHelper instance ;
	
	static{
		Properties properties = new Properties();
		URL url = SQlServerHelper.class.getClassLoader().getResource("bigant.properties");
		try 
		{
			properties.load(new FileInputStream(URLDecoder.decode(url.getFile(), "UTF-8")));
			driverClassName = properties.getProperty("jdbc.driverClassName");
			sqlURL = properties.getProperty("jdbc.url");
			username = properties.getProperty("jdbc.username");
			password = properties.getProperty("jdbc.password");
		} catch (IOException e) {
			logger.error("无法找到配置文件",e);
		}
	}

	/**
	 * 通过单例模式得到对象.
	 * @author:邓志城
	 * @date:2010-6-12 下午02:57:56
	 * @return 对象实例
	 */
	public static SQlServerHelper getInstance() {
		if(instance == null){
			instance = new SQlServerHelper();
		}
		return instance ;
	}
	
	/**
	 * 获取数据库连接
	 * @author:邓志城
	 * @date:2010-6-12 下午02:55:52
	 * @return
	 * @throws SQLException 
	 */
	public Connection getConnection() throws SQLException{ 
		if(con == null || con.isClosed()){
			try {
				Class.forName(driverClassName);
				con = DriverManager.getConnection(sqlURL, username, password);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("获取数据连接Connection出错！",e);
				throw new SystemException("连接数据库失败:" + sqlURL);
			}
		}
		return con ;
	}
	
	/**
	 * 关闭连接
	 * @author:邓志城
	 * @date:2010-6-12 下午03:00:53
	 * @param pstm
	 * @param rs 记录集对象
	 * @param con	连接
	 * @throws SQLException
	 */
	public synchronized void close(PreparedStatement pstm,ResultSet rs,Connection con) throws SystemException {
		try {
			if(pstm != null){
				pstm.close();
			}
			if(rs != null){
				rs.close();
			}
			if(con != null && !con.isClosed()){
				con.close();
				con = null;
			}
		} catch (SQLException e) {
			logger.error("关闭连接异常。", e);
			throw new SystemException(e);
		}
	}
	
}
