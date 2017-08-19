package com.strongit.xxbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcTest {
	public static String url = "jdbc:oracle:thin:@localhost:1522:orcl11g";

	public static String name = "oracle.jdbc.driver.OracleDriver";

	public static String user = "gzjzyh";

	public static String password = "password";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement pst = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "select * from T_UUMS_BASE_USER t";
		try {
			Class.forName(name);//指定连接类型  
			conn = DriverManager.getConnection(url, user, password);//获取连接  

			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				System.out.println(rs.getString("user_name"));
			}
			rs.close();
			stmt.close();//关闭连接  
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
