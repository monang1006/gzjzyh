package com.engine.util;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.commons.dbcp.BasicDataSource;

public class DataSource {
	private static HashMap cqconn = new HashMap();
	//数据库类型,数据库地址,数据库名,用户名,密码,字符集
	public final static Connection getConn(String dbType,String dbPath,String dbName,String dbUser,String dbPass,String dbChar){
		Connection conn=null;
		if(dbUser == null) dbUser="";
		BasicDataSource dataSource = (BasicDataSource)cqconn.get(dbType+"_"+dbPath+"_"+dbUser);
		try {
				if(dataSource==null){
					dataSource = new BasicDataSource();
					dataSource.setDriverClassName(getClassName(dbType));
			        dataSource.setUrl(getDbUrl(dbType,dbPath,dbName,dbChar));
			        if(!"".equals(dbUser))  dataSource.setUsername(dbUser);
			        if(!"".equals(dbPass))  dataSource.setPassword(dbPass);
			        //初始化连接数
			        dataSource.setInitialSize(10);
			        //最大连接数
			        dataSource.setMaxActive(50);
			        //最大空闲连接
			        dataSource.setMaxIdle(20);
			        //最小空闲连接
			        dataSource.setMinIdle(5);
			        //最大等待时间(以毫秒为单位)
			        dataSource.setMaxWait(30000);			        
			        //从连接池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
			        dataSource.setTestOnBorrow(true);	
			        //检验的sql语句
			        dataSource.setValidationQuery("select 1");
			        //指明连接是否被空闲连接回收器(如果有)进行检验.如果检测失败,则连接将被从池中去除
			        dataSource.setTestWhileIdle(true);

			        cqconn.put(dbType+"_"+dbPath+"_"+dbUser, dataSource); 
				}
				conn = dataSource.getConnection();
		} catch (Exception e) {
	        e.printStackTrace();
		}
		return conn;
	}
	private  static String getDbUrl(String dbType,String dbPath,String dbName,String dbChar){
		String dbUrl = "";
		//dbType=derby,access,sql2000,sql2005,mysql,oracle
		if("derby".equals(dbType)){
			dbUrl = "jdbc:derby:"+dbPath;
		}else if("access".equals(dbType)){
			dbUrl = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ="+dbPath;
		}else if("sql2000".equals(dbType)){
			dbUrl = "jdbc:microsoft:sqlserver://"+dbPath+";DatabaseName="+dbName;
		}else if("sql2005".equals(dbType)){
			dbUrl = "jdbc:sqlserver://"+dbPath+";DatabaseName="+dbName;
		}else if("mysql".equals(dbType)){
			dbUrl = "jdbc:mysql://"+dbPath+"?useUnicode=true&characterEncoding="+dbChar;
		}else if("oracle".equals(dbType)){
			dbUrl = "jdbc:oracle:thin:@"+dbPath+":"+dbName;
		}
		return dbUrl;
	}
	private  static String getClassName(String dbType){
		String className = "org.apache.derby.jdbc.EmbeddedDriver";
		if("derby".equals(dbType)){
			className = "org.apache.derby.jdbc.EmbeddedDriver";
		}else if("access".equals(dbType)){
			className = "sun.jdbc.odbc.JdbcOdbcDriver";
		}else if("sql2000".equals(dbType)){
			className = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		}else if("sql2005".equals(dbType)){
			className = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}else if("mysql".equals(dbType)){
			className = "com.mysql.jdbc.Driver";
		}else if("oracle".equals(dbType)){
			className = "oracle.jdbc.driver.OracleDriver";
		}
		return className;
	}
}