package dbexp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.strongit.oa.util.UUIDGenerator;
/**
 * 2012-02-10
 * 2、将工作流中的业务id保存到新增的数据表T_JBPM_BUSINESS中，
 * 前提是先执行数据库升级脚本，再执行BgtBusnisessTrans.java，避免数据遗漏
 * 
 * 
 * */
public class OracleDBExp {
	private String url;
	private String userName;
	private String password;

	
	/**
	 * author:luosy
	 * description:
	 * modifyer:
	 * description:
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "jdbc:oracle:thin:@192.168.2.88:1521:oracle";
		String userName = "oa_ncbgt_dev";
		String password = "password";
		
		// 先执行脚本 将部门修改为机构
//		update t_uums_base_org t set t.org_isorg='1' where t.org_syscode like '0019990%'
		new OracleDBExp(url,userName,password).trans();
	}

	public OracleDBExp(String url,String userName,String password){
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

			String orgIds = "";
			List<Object[]> orgList = new ArrayList<Object[]>();
			
			String orgSql = "select t.org_id,t.org_syscode,t.org_name from t_uums_base_org t where t.org_syscode like '0019990%' order by t.org_syscode";
			pstat = con.prepareStatement(orgSql);
			rs = pstat.executeQuery();
			int i = 0;
			while (rs.next()) {
				i = i+1;
//				System.out.println("第" + i + "条数据：" + rs.getString(1)+ rs.getString(2)+ rs.getString(3));
				Object[] obj = {rs.getString(1), rs.getString(2), rs.getString(3)};
				orgList.add(obj);
				orgIds += "'"+rs.getString(1)+"',";				
			}
//			orgIds  = orgIds.substring(0,orgIds.length()-1);
//			System.out.println("orgIds：" + orgIds);
			
			
			rs=null;
			pstat=null;

			UUIDGenerator uuidgenerator = new UUIDGenerator();
			for (int count = 0; count < orgList.size(); count++) {
				
				Object[] tempObj = orgList.get(count);//8a928a703bb11098013bb67815020054 001999001 县区政府及开发区
				
				String szSql = "select t.user_id,t.user_name from t_uums_base_user t where t.org_id='"+tempObj[0]+"'";
				pstat = con.prepareStatement(szSql);
				rs = pstat.executeQuery();
				
				String tempOrgCode = "";
				int j = 0;
				while (rs.next()) {
					j = j+1;
					
					String neworgid =  uuidgenerator.generate().toString();
					if(j>9){
						tempOrgCode = tempObj[1]+"0"+j;
					}else{
						tempOrgCode = tempObj[1]+"00"+j;
					}
					
					System.out.println("insert into t_uums_base_org (ORG_ID, ORG_SYSCODE, ORG_CODE, ORG_AREACODE, ORG_NAME, ORG_NATURE, ORG_GRADE, ORG_ADDR, ORG_TEL, ORG_FAX, ORG_ZIP, ORG_SEQUENCE, ORG_DESCRIPTION, ORG_MANAGER, ORG_ISDEL, ORG_PARENT_ID, REST1, REST2, REST3, REST4, ORG_SUPORGCODE, ORG_ISORG) " +
							"values ('"+neworgid+"', '"+tempOrgCode+"', '', '', '"+rs.getString(2)+"', '0', '0', '', '', '', '', 40, '', '', '0', '"+tempObj[0]+"', '0', '', '', '', '001999', '1');\n");
					System.out.println("update t_uums_base_user t set t.org_id='"+neworgid+"' where t.user_id = '"+rs.getString(1)+"';\n\n");
//					Object[] obj = {rs.getString(1), rs.getString(2), rs.getString(3)};
//					orgList.add(obj);
//					orgIds += "'"+rs.getString(1)+"',";				
					tempOrgCode = "";
				}
			}
			
			rs.close();
			pstat.close();
			message += ("数据成功");
			System.out.println("数据成功");
		} catch (ClassNotFoundException e) {
			message += ("数据失败");
			System.out.println("数据失败");
			e.printStackTrace();
		} catch (SQLException e) {
			message += ("数据失败");
			System.out.println("数据失败");
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
