package com.strongit.oa.DBsync;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * 2012-04-07
 * 处理***类
 * 
 * 		3、2012_04_05_同步主办人员信息  by yanjian
 *			该工具是用来流程中发起人信息，同步到主办人员信息表中
 * @Create  Date: 2012-4-7
 * @author luosy
 * @version 1.0
 */
public class Start {
	public static void main(String[] args) {
		TransData transData = new TransData();
		trans(transData);
	}

	public static void trans(TransData transData) {
		System.out.println("开始导入数据");
		PreparedStatement pstat = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			// oracle具体配置
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// oracle IP地址+端口号/SID
			con = DriverManager.getConnection(transData.getUrl(), transData
					.getUserName(), transData.getPassword());

			String selectT_WF_INFO_PROBUSRELATION = "select "
					+ "pi.id_,pi.start_user_id_ "
					+ "from "
					+ "jbpm_processinstance pi "
					+ "where "
					+ "pi.start_user_id_ is not null "
					+ "and not exists (select t.id from t_maina_corconfing t where t.processinstance_id = to_char(pi.id_)) ";
			pstat = con.prepareStatement(selectT_WF_INFO_PROBUSRELATION);
			rs = pstat.executeQuery();
			List<String[]> wr_business_id_list = new LinkedList<String[]>();
			int i = 0;
			while (rs.next()) {
				i = i + 1;
				String[] objs = new String[2];
				objs[0] = rs.getString(1);
				objs[1] = rs.getString(2);
				wr_business_id_list.add(objs);
				System.out.println("第" + i + "条数据：" + rs.getString(1));
			}
			rs.close();
			pstat.close();
			// ResultSet rs = stat.executeQuery("delete from T_JBPM_BUSINESS");

			/*
			 * INSERT INTO 语句 INSERT INTO 语句用于向表格中插入新的行。
			 * 语法 INSERT INTO 表名称 VALUES (值1, 值2,....)我们也可以指定所要插入数据的列：
			 * INSERT INTO table_name (列1, 列2,...) VALUES (值1, 值2,....)
			 */
			String insertT_JBPM_BUSINESS = "insert into T_MAINA_CORCONFING (ID,PROCESSINSTANCE_ID,MAIN_ACTORID) VALUES (?,?,?)";
			con.setAutoCommit(false);
			pstat = con.prepareStatement(insertT_JBPM_BUSINESS);
			UUIDGenerator uuidgenerator = new UUIDGenerator();
			for (int count = 0; count < wr_business_id_list.size(); count++) {
				String[] str = wr_business_id_list.get(count);
				pstat.setString(1, (String) uuidgenerator.generate());
				pstat.setString(2, str[0]);
				pstat.setString(3, str[1]);
				pstat.addBatch();
			}
			pstat.executeBatch();
			con.commit();
			rs.close();
			pstat.close();
			con.close();
			System.out.println("数据导入成功");
		} catch (ClassNotFoundException e) {
			System.out.println("数据导入失败");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("数据导入失败");
			e.printStackTrace();
		}
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
}
