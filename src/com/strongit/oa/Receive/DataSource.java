package com.strongit.oa.Receive;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.bo.ToaRecvdoc;
import com.strongit.oa.util.PropertiesUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;

public class DataSource {
	private static Logger logger = LoggerFactory.getLogger(DataSource.class);

	private static HashMap cqconn = new HashMap();

	private static Properties prop = new Properties();// 获取dataSource.properties属性信息
	private static int initialSize = 10;	//初始化连接数
	private static int minIdle = 4;	//最小空闲数
	private static int maxIdle = 10;	//最小空闲数
	private static long maxWait = 300000;	//超时回收时间(以毫秒为单位)
	private static int maxActive = 80;	//最小空闲数
	public static Properties getDataSoureProperties() {
		return prop;
	}
	static {
		try {
			prop = PropertiesUtil
					.getPropertiesWithFileName("dataSource.properties");
			if(prop.getProperty("initialSize") != null){
				initialSize = Integer.parseInt(prop.getProperty("initialSize"));
			}
			logger.info("initialSize:"+initialSize);
			if(prop.getProperty("minIdle") != null){
				minIdle = Integer.parseInt(prop.getProperty("minIdle"));
			}
			logger.info("minIdle:"+minIdle);
			if(prop.getProperty("maxIdle") != null){
				maxIdle = Integer.parseInt(prop.getProperty("maxIdle"));
			}
			logger.info("maxIdle:"+maxIdle);
			if(prop.getProperty("maxWait") != null){
				maxWait = Long.parseLong(prop.getProperty("maxWait"));
			}
			logger.info("maxWait:"+maxWait);
			if(prop.getProperty("maxActive") != null){
				maxActive = Integer.parseInt(prop.getProperty("maxActive"));
			}
			logger.info("maxActive:"+maxActive);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	// 数据库类型,数据库地址,数据库名,用户名,密码,字符集
	@SuppressWarnings("unchecked")
	public final static Connection getConn(String dbType, String dbPath,
			String dbName, String dbUser, String dbPass, String dbChar) {
		Connection conn = null;
		if (dbUser == null)
			dbUser = "";
		BasicDataSource dataSource = (BasicDataSource) cqconn.get(dbType + "_"
				+ dbPath + "_" + dbUser);
		try {
			if (dataSource == null) {
				dataSource = new BasicDataSource();
				dataSource.setDriverClassName(getClassName(dbType));
				dataSource.setUrl(getDbUrl(dbType, dbPath, dbName, dbChar));
				if (!"".equals(dbUser))
					dataSource.setUsername(dbUser);
				if (!"".equals(dbPass))
					dataSource.setPassword(dbPass);
				// 初始化连接数
				dataSource.setInitialSize(initialSize);
				// 最小空闲连接
				dataSource.setMinIdle(minIdle);
				// 最大空闲连接
				dataSource.setMaxIdle(maxIdle);
				// 超时回收时间(以毫秒为单位)
				dataSource.setMaxWait(maxWait);
				// 最大连接数
				dataSource.setMaxActive(maxActive);
				cqconn.put(dbType + "_" + dbPath + "_" + dbUser, dataSource);
			}
			conn = dataSource.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static String getDbUrl(String dbType, String dbPath, String dbName,
			String dbChar) {
		String dbUrl = "";
		// dbType=derby,access,sql2000,sql2005,mysql,oracle
		if ("derby".equals(dbType)) {
			dbUrl = "jdbc:derby:" + dbPath;
		} else if ("access".equals(dbType)) {
			dbUrl = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ="
					+ dbPath;
		} else if ("sql2000".equals(dbType)) {
			dbUrl = "jdbc:microsoft:sqlserver://" + dbPath + ";DatabaseName="
					+ dbName + ";SelectMethod=cursor";
		} else if ("sql2005".equals(dbType)) {
			dbUrl = "jdbc:sqlserver://" + dbPath + ";DatabaseName=" + dbName;
		} else if ("mysql".equals(dbType)) {
			dbUrl = "jdbc:mysql://" + dbPath
					+ "?useUnicode=true&characterEncoding=" + dbChar;
		} else if ("oracle".equals(dbType)) {
			dbUrl = "jdbc:oracle:thin:@" + dbPath + ":" + dbName;
		}
		return dbUrl;
	}

	public static String getClassName(String dbType) {
		String className = "org.apache.derby.jdbc.EmbeddedDriver";
		if ("derby".equals(dbType)) {
			className = "org.apache.derby.jdbc.EmbeddedDriver";
		} else if ("access".equals(dbType)) {
			className = "sun.jdbc.odbc.JdbcOdbcDriver";
		} else if ("sql2000".equals(dbType)) {
			className = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		} else if ("sql2005".equals(dbType)) {
			className = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		} else if ("mysql".equals(dbType)) {
			className = "com.mysql.jdbc.Driver";
		} else if ("oracle".equals(dbType)) {
			className = "oracle.jdbc.driver.OracleDriver";
		}
		return className;
	}

	/**
	 * 获取conection
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime May 30, 2012 10:50:14 AM
	 */
	public static Connection getConnecton() {
		Properties p = getDataSoureProperties();
		logger.info("dbType:" + p.getProperty("dbType") + ",port:"
				+ p.getProperty("dbPath"));
		String dbType = p.getProperty("dbType");
		String dbPath = p.getProperty("dbPath");
		String dbName = p.getProperty("dbName");
		String dbUser = p.getProperty("dbUser");
		String dbPass = p.getProperty("dbPass");
		String dbChar = p.getProperty("dbChar");
		return DataSource.getConn(dbType, dbPath, dbName, dbUser, dbPass,
				dbChar);
	}
	public static void main(String[] args) {
		int count = 100;
		for(int i=0;i<count;i++){
			long start = System.currentTimeMillis();
			Page<ToaRecvdoc> page = new Page<ToaRecvdoc>(FlexTableTag.MAX_ROWS);
			ToaRecvdoc model = new ToaRecvdoc();
			new ReceiveManage().initPageTotalCount(page, model);
			System.out.println(System.currentTimeMillis()- start);
			System.out.println(i);
		}
	}
}