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
 * 1、解决流程实例表中存在bussinessId为空的情况
 * 
 * 
 * */
public class GzgtoaDateTrans {
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
		new GzgtoaDateTrans(url, userName, password).trans();
	}

	public GzgtoaDateTrans(String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	/**
	 * @description
	 * @author 严建
	 * @return
	 * @createTime Feb 14, 2012 1:06:43 PM
	 */
	public String trans() {
		String message = "";
		message += (this.url + "\n");
		message += (this.userName + "\n");
		message += (this.password + "\n");
		message += "开始导入数据\n";
		System.out.println("开始导入数据");
		PreparedStatement pstat = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			// oracle具体配置
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// oracle IP地址+端口号/SID
			con = DriverManager.getConnection(this.url, this.userName,
					this.password);

			String selectT_WF_INFO_PROBUSRELATION = ""
					+ "select "
					+ "distinct t1.wr_pi_id,t1.wr_business_id "
					+ "from "
					+ "t_wf_info_probusrelation t1,"
					+ "(select  t.id_ as pi_id, t.mainform_id_ as pi_form_id  from jbpm_processinstance t where t.business_id is null) p "
					+ "where " + "t1.wr_pi_id = p.pi_id "
					+ "and t1.wr_form_id = p.pi_form_id " + "ORDER BY "
					+ "t1.wr_pi_id";
			pstat = con.prepareStatement(selectT_WF_INFO_PROBUSRELATION);
			rs = pstat.executeQuery();
			List<String> wr_business_id_list = new LinkedList<String>();
			List<Long> wr_pi_id_list = new LinkedList<Long>();
			int i = 0;

			while (rs.next()) {
				i = i + 1;
				wr_pi_id_list.add(rs.getLong(1));
				wr_business_id_list.add(rs.getString(2));
				message += ("第" + i + "条数据：wr_pi_id:" + rs.getString(1)
						+ ",wr_business_id:" + rs.getString(2) + "\n");
				System.out.println("第" + i + "条数据：wr_pi_id:" + rs.getString(1)
						+ ",wr_business_id:" + rs.getString(2) + "\n");
			}
			rs.close();
			pstat.close();
			/**
			 * UPDATE Person SET Address = 'Zhongshan 23', City = 'Nanjing'
			 *	WHERE LastName = 'Wilson'
			 *
			 */

			String updateJBPM_PROCESSINSTANCE = "UPDATE "
					+ "JBPM_PROCESSINSTANCE SET BUSINESS_ID = ? "
					+ "WHERE ID_ = ?";
			con.setAutoCommit(false);
			pstat = con.prepareStatement(updateJBPM_PROCESSINSTANCE);
			for (int count = 0; count < wr_business_id_list.size(); count++) {
				pstat.setString(1, wr_business_id_list.get(count));
				pstat.setLong(2, wr_pi_id_list.get(count));
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


	public static ResultSet select(Connection con, String sql) {
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
