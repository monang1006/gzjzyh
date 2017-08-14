package com.strongit.oa.DBsync;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * 2012-02-10
 * 执行BgtTrans之后，仍然有一部分数据存在问题时，再执行该程序，
 * 解决流程实例存在业务id，但是在t_jbpm_business表中不存在相关记录
 * 
 * */
public class BgtTransAfter {
	private String url;
	private String userName;
	private String password;
	/**
	 * @description
	 * @author 严建
	 * @param args
	 * @createTime Feb 10, 2012 11:55:16 AM
	 */
	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("setting.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String url = "jdbc:oracle:thin:@192.168.2.85:1521:orcl";
		String userName = "rbt_oa_update";
		String password = "password";
//		String url = properties.getProperty("url");;
//		String userName = properties.getProperty("userName");
//		String password = properties.getProperty("password");
		new BgtTransAfter(url,userName,password).trans();
	}

	public BgtTransAfter(String url,String userName,String password){
		this.url =url;
		this.userName = userName;
		this.password = password;
	}
	public String trans(){
		String message = "";
		message += (this.url+"\n");
		message += (this.userName+"\n");
		message += (this.password+"\n");
		message += "开始导入数据\n";
		System.out.println("开始导入数据");
		PreparedStatement pstat = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			// oracle具体配置
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// oracle IP地址+端口号/SID
			con = DriverManager.getConnection(this.url,this.userName, this.password);
			//流程实例存在业务id，但在t_jbpm_business表中不存在相关记录
			String select = "select pi.business_id from jbpm_processinstance pi where  not exists (select t.id  from t_jbpm_business t where pi.business_id = t.business_id )";
			pstat = con.prepareStatement(select);
			rs = pstat.executeQuery();
			List<String> wr_business_id_list = new LinkedList<String>();
			int i = 0;
			while (rs.next()) {
				i = i+1;
				wr_business_id_list.add(rs.getString(1));
				message += ("第" + i + "条数据：" + rs.getString(1)+"\n");
				System.out.println("第" + i + "条数据：" + rs.getString(1));
			}
			rs.close();
			pstat.close();
			// ResultSet rs = stat.executeQuery("delete from T_JBPM_BUSINESS");

			/*
			 * INSERT INTO 语句 INSERT INTO 语句用于向表格中插入新的行。
			 * 
			 * 语法 INSERT INTO 表名称 VALUES (值1, 值2,....)我们也可以指定所要插入数据的列：
			 * 
			 * INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
			 */
			String insertT_JBPM_BUSINESS = "insert into T_JBPM_BUSINESS (ID, BUSINESS_ID,PERSON_CONFIG_FLAG) VALUES (?,?,?)";
			con.setAutoCommit(false);
			pstat = con.prepareStatement(insertT_JBPM_BUSINESS);
			UUIDGenerator uuidgenerator = new UUIDGenerator();
			for (int count = 0; count < wr_business_id_list.size(); count++) {
				pstat.setString(1, (String) uuidgenerator.generate());
				pstat.setString(2, wr_business_id_list.get(count));
				pstat.setString(3, "0");
				pstat.addBatch();
			}
			pstat.executeBatch();
			con.commit();
			rs.close();
			pstat.close();
			con.close();
			message += ("数据导入成功");
			System.out.println("数据导入成功");
		} catch (ClassNotFoundException e) {
			message += ("数据导入失败");
			System.out.println("数据导入失败");
			e.printStackTrace();
		} catch (SQLException e) {
			message += ("数据导入失败");
			System.out.println("数据导入失败");
			e.printStackTrace();
		}
		return message;
	}
	
	/*
	 * DELETE 语句 DELETE 语句用于删除表中的行。
	 * 
	 * 语法 DELETE FROM 表名称 WHERE 列名称 = 值
	 */
	public static void delete(Connection con,String sql) {
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {
			pstat = con.prepareStatement(sql);
			rs = pstat.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pstat);
		}

	}
	
	public static ResultSet select(Connection con,String sql) {
		PreparedStatement pstat = null;
		ResultSet rs = null;
		try {
			pstat = con.prepareStatement(sql);
			rs = pstat.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pstat);
		}
		return rs;
	}

	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closePreparedStatement(PreparedStatement pstat) {
		try {
			if (pstat != null) {
				pstat.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
