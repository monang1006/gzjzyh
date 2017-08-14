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
public class OracleDBSync {
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
		String url = "jdbc:oracle:thin:@14.0.0.14:1521:orcl";
		String userName = "oa_bgt_new";
		String password = "password";
		
		// 先执行脚本 将部门修改为机构
//		update t_uums_base_org t set t.org_isorg='1' where t.org_syscode like '0019990%'
		new OracleDBSync(url,userName,password).trans();
	}

	public OracleDBSync(String url,String userName,String password){
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
			List<Object[]> list = new ArrayList<Object[]>();
			
			String orgSql = "select t.tempfile_id,t.tempfile_doc_id from T_OA_ARCHIVE_TEMPFILE t";
			pstat = con.prepareStatement(orgSql);
			rs = pstat.executeQuery();
			int i = 0;
			while (rs.next()) {
				i = i+1;
				String tempfile_id = rs.getString(1);
				String tempfile_doc_id = rs.getString(2);
//				System.out.println("tempfile_id:"+tempfile_id+"--tempfile_doc_id:|"+tempfile_doc_id+"|");
				if(!"".equals(tempfile_doc_id)&&null!=tempfile_doc_id){
					String[] docInfo = tempfile_doc_id.split(";");
					Object[] obj = {tempfile_id, docInfo[0],docInfo[1],docInfo[2],};
					list.add(obj);
				}
				
			}
			
			
			rs=null;
			pstat=null;

			String szSql ="";
			String docNum = "";
			for (int count = 0; count < list.size(); count++) {
				
				Object[] tempObj = list.get(count);//8a928a703bb11098013bb67815020054 001999001 县区政府及开发区
				
				String tableName =  tempObj[1].toString();
				if("T_OARECVDOC".equals(tableName)){
					docNum = "RECV_NUM";
				}else if("T_OA_SENDDOC".equals(tableName)){
					docNum = "SENDDOC_CODE";
				}else if("T_OA_CONSULTATION".equals(tableName)){
					docNum = "CON_CODE";
				}else{
					docNum = "WORKFLOWCODE";
				}
				szSql = "select "+docNum+" from "+tableName+" where "+tempObj[2]+"='"+tempObj[3]+"'";
				pstat = con.prepareStatement(szSql);
				rs = pstat.executeQuery();
				int j = 0;
				while (rs.next()) {
					docNum = rs.getString(1);
					j = j+1;
					System.out.println(count+"#tempfile_id:"+tempObj[0]+"||docNum:"+docNum);
				}
				
				closePreparedStatement(pstat);
				
				
				szSql = "update T_OA_ARCHIVE_TEMPFILE set TEMPFILE_NO = '"+docNum+"' where tempfile_id = '"+tempObj[0]+"'";
				System.out.println(szSql);
				pstat = con.prepareStatement(szSql);
				rs = pstat.executeQuery();
				closePreparedStatement(pstat);
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
		} finally {
			closeResultSet(rs);
			closePreparedStatement(pstat);
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
