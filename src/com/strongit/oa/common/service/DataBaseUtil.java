package com.strongit.oa.common.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.strongmvc.exception.SystemException;

/**
 * 
 * 获取OA系统应用数据库信息
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Apr 9, 2012 12:01:45 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.service.DataBaseUtil
 */
public class DataBaseUtil {

	/**
	 * 获取数据库类型
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 9, 2012 12:04:36 PM
	 */
	protected static  String getDataBaseType() {
		String dataBaseType = "oracle";
		try {
			Properties	prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("appconfig.properties"));
			String jdbcUrl = prop.getProperty("jdbc.url","");
//			String jdbcUrl = PropertiesUtil.getProperty("jdbc.url",
//					"appconfig.properties");
			if (jdbcUrl.indexOf("oracle") != -1) {
				dataBaseType = "oracle";
			} else if (jdbcUrl.indexOf("mysql") != -1) {
				dataBaseType = "mysql";
			} else if (jdbcUrl.indexOf("microsoft") != -1
				|| jdbcUrl.indexOf("sqlserver") != -1) {
				dataBaseType = "microsoft";
			} else {
				throw new SystemException("不支持的数据库类型！");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			System.err.println("读取配置文件“appconfig.properties”文件失败！");
		}
		return dataBaseType;
	}

	/**
	 * 是否为oracle
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 9, 2012 12:06:51 PM
	 */
	public static boolean isOracle() {
		if ("oracle".equals(getDataBaseType())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否为mysql
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 9, 2012 12:07:06 PM
	 */
	public static boolean isMySql() {
		if ("mysql".equals(getDataBaseType())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否为MicrosoftSQL
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Apr 9, 2012 12:07:14 PM
	 */
	public static boolean isMicrosoftSQL() {
		if ("microsoft".equals(getDataBaseType())) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 查询字段连接字符串
	 * 
	 * @description
	 * @author 严建
	 * @param str1
	 * @param str2
	 * @return
	 * @createTime Apr 9, 2012 2:14:55 PM
	 */
	public static String SqlConcat(String str1, String str2) {
		if (isOracle()) {
			return str1 + "||" + str2;
		}else if(isMicrosoftSQL()){
			return str1 + "+" + str2;
		}else if(isMySql()){
			return "CONCAT("+str1+", "+str2+")";
		}
		return str1;
	}
	/**
	 * 将整数转换成字符串
	 * 
	 * @description
	 * @author 严建
	 * @param str1
	 * @param str2
	 * @return
	 * @createTime Apr 9, 2012 2:14:55 PM
	 */
	public static String SqlNumberToChar(String str1) {
		if (isOracle()) {
			return " to_char("+str1+") ";
		}else if(isMicrosoftSQL()){
			return "LOWER("+str1 +")";
		}else if(isMySql()){
			return "LOWER("+str1 +")";
		}
		return str1;
	}
	
	/**
	 * Clob转换为Char
	 * 
	 * @author 严建
	 * @param str1
	 * @return
	 * @createTime Jul 1, 2012 5:39:27 PM
	 */
	public static String SqlClobToChar(String str1){
		if (isOracle()) {
			return " to_char("+str1+") ";
		}
		return str1;
	}
	/**
	 * 取日期年份
	 * 
	 * @description
	 * @author 严建
	 * @param str1
	 * @param str2
	 * @return
	 * @createTime Apr 9, 2012 2:14:55 PM
	 */
	public static String SqlYearOfDate(String str1) {
		if (isOracle()) {
			return " to_char("+str1+",  'yyyy') ";
		}else if(isMicrosoftSQL()){
			return " YEAR("+str1+") ";
		}else if(isMySql()){
			return " YEAR("+str1+") ";
		}
		return str1;
	}
	
	/**
	 * 
	 * @description
	 * @author 严建
	 * @param Conn
	 * @throws Exception
	 * @createTime May 25, 2012 11:03:35 AM
	 */
	public static void closeConnecton(Connection Conn) {
		if (Conn != null) {
			try {
				Conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Conn = null;
		}

	}

	/**
	 * @description
	 * @author 严建
	 * @param rs
	 * @throws Exception
	 * @createTime May 25, 2012 11:03:43 AM
	 */
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rs = null;
		}

	}

	/**
	 * @description
	 * @author 严建
	 * @param ps
	 * @throws Exception
	 * @createTime May 25, 2012 11:03:48 AM
	 */
	public static void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ps = null;
		}
	}
}
