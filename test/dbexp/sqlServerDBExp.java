package dbexp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.wsdl.http.UrlEncoded;

import com.strongit.oa.DBsync.UUIDGenerator;

/**
 * 处理类  南航数据迁移数据导出
 * @Create Date: 2012-7-2
 * @author luosy
 * @version 1.0
 */
public class sqlServerDBExp {
	
	private String cluId = "8a81d98e381d55ad01381d8de4060060";//纪委文件
	
	private String url;

	private String userName;

	private String password;

	public static void main(String[] args) {

		String url = "jdbc:sqlserver://sql.oa.nchu.edu.cn;databaseName=OA";
		String userName = "OAServer";
		String password = "OaServer!@#";

		try {
			new sqlServerDBExp(url, userName, password).trans();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public sqlServerDBExp(String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}
	
	
	public String trans() throws Exception{
		System.out.println("url:"+url+"\nuserName:"+userName+"\npassword:"+password);

		UUIDGenerator ug = new UUIDGenerator();
		String artId = "";
		String col_artId = "";
		String message = "";
		message += (this.url + "\n");
		message += (this.userName + "\n");
		message += (this.password + "\n");
		message += "开始查询数据\n";
		System.out.println("开始查询数据");
		PreparedStatement pstat = null;
		Connection con = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(this.url, this.userName, this.password);

			List<String[]> fileList = readFileByLines("e:/newTemp.txt");
			String[] fileTemp = null;
			String title="";//主 题
			String wjlx="";//文件类型
			String cjbm="";//创建部门
			String cjr="";//创建人
			String cjsj="";//创建时间
			String  fjmc = "";//附件名称
			String  fjmcStr = "";//附件名称
			String[] fjmcs=new String[10];//附件名称
			StringBuilder sb = new StringBuilder(""); 
			
			
			if(fileList==null){
				System.out.println("文件没有找到，或者文件内容为空");
				return "导入失败";
			}
			for(int i=0;i<fileList.size();i++){

				artId = (String) ug.generate();
				col_artId = (String) ug.generate();
				fileTemp =  fileList.get(i);
				title=fileTemp[0];//主 题
				wjlx=fileTemp[1];//文件类型
				cjbm=fileTemp[2];//创建部门
				cjr=fileTemp[3];//创建人
				cjsj=fileTemp[4];//创建时间
				fjmc=fileTemp[5];//附件名称
				fjmcs = fjmc.split("=");
				for(int j=0;j<fjmcs.length;j++){
					fjmc=fjmcs[j];//附件名称
					if(fjmc!=null&&!"".equals(fjmc)){
						fjmcStr += "<tr class=tr_list><td class=td_list_1 width=\"10%\">　</td><td class=td_list_1><img src=/oa/oa/image/desktop/littlegif/news_bullet.gif>  <a onclick=exportFile(\""+wjlx.trim()+";"+fjmc+"\") href=\"#\" target=_self>"+fjmc+"</a></td></tr>";
					}
				}
				sb.append("insert into  [OA].[dbo].[T_OA_INFOPUBLISH_ARTICLES] (ARTICLES_ID,ARTICLES_TITLE,ARTICLES_SUBTITLE,ARTICLES_AUTHOR,ARTICLES_KEYWORD,ARTICLES_ARTICLECONTENT,ARTICLES_TITLECOLOR,ARTICLES_TITLEFONT,ARTICLES_ISREDIRECT,ARTICLES_REDIRECTURL,ARTICLES_ATICLESTATE,ARTICLES_ATICLEATTRIBUTE,ARTICLES_ISCANCOMMENT,ARTICLES_ISSHOWCOMMENT,ARTICLES_ISSTANDTOP,ARTICLES_ISHEAD,ARTICLES_ISHOT,ARTICLES_GUIDETYPE,ARTICLES_SOURCE,ARTICLES_EDITOR,ARTICLES_DESC,ARTICLES_CREATEDATE,ARTICLES_PAGINATION,ARTICLES_PAGINATIONNUM,ARTICLES_AUTOPUBLISHTIME,ARTICLES_AUTOCANCLETIME,ARTICLES_STANDTOPSTART,ARTICLES_STANDTOPEND,ARTICLES_HITS,ARTICLES_LATESTUSER,ARTICLES_LATESTCHANGTIME,ARTICLES_ISSHARE,ARTICLES_ISPRIVATE,ARTICLES_COMMENTTIME,ARTICLES_PIC,ARTICLES_AUTHORDEPARTMENT)");
				sb.append("values ('"+artId+"','").append(title).append("','','root','','<TABLE class=list_tab border=1 cellSpacing=1 cellPadding=0 width=\"100%\"><TBODY><TR class=tr_list><TD class=td_list_1 width=\"15%\">主 题</TD><TD class=td_list_1 colSpan=3>").append(title).append("</TD><TR class=tr_list><TD class=td_list_1 width=\"15%\">文件类型</TD><TD class=td_list_1 width=\"35%\">").append(wjlx).append("</TD><TD class=td_list_1 width=\"15%\">创建部门</TD><TD class=td_list_1 width=\"35%\">").append(cjbm).append(" </TD></TR></TR><TR class=tr_list><TD class=td_list_1>创建人</TD><TD class=td_list_1>").append(cjr).append(" </TD><TD class=td_list_1>创建时间</TD><TD class=td_list_1>").append(cjsj).append(" </TD></TR></TBODY></TABLE><TABLE border=1 cellSpacing=0 cellPadding=0 width=\"100%\"><TBODY>").append(fjmcStr).append("</TABLE>','0','0','0','http://','2','','0','','1','','','','','','','").append(cjsj).append("','','10000','").append(cjsj).append("','2015-07-02 13:36:24','','','3','root','2012-07-02 13:36:24','','','','','").append(cjbm).append("');\n");
				sb.append("insert into  [OA].[dbo].[T_OA_INFOPUBLISH_COLUMN_ARTICL] (COLUMN_ARTICLE_ID,ARTICLES_ID,CLUMN_ID,COLUMN_ARTICLE_STATE,COLUMN_ARTICLE__ISSTANDTOP,COLUMN_ARTICLE_GUIDETYPE,COLUMN_ARTICLE_SEQUENCE,COLUMN_ARTICLE_ADDTIME,COLUMN_ARTICLE_ADDUSER,COLUMN_ARTICLE_REMOVETIME,COLUMN_ARTICLE_REMOVEUSER,COLUMN_ARTICLE_OLDATICLESTATE,COLUMN_ARTICLE_LATESTCHANGTIME,COLUMN_ARTICLE_LATESTUSER,PROCESS_INSTANCE_ID)");
				sb.append("values ('"+col_artId+"','"+artId+"','"+cluId+"','9','1','','','").append(cjsj).append("','root','','','2','").append(cjsj).append("','root','');\n");

				fjmcStr="";
			}
			
			
			System.out.println(sb.toString()+"\n\n");
			pstat = con.prepareStatement(sb.toString());
			pstat.execute();
			
			
			
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
		}catch(Exception e){
			message += ("数据导入失败");
			System.out.println("数据导入失败");
			e.printStackTrace();
		}
		
		return message;
	
	}

	
	public static List<String[]> readFileByLines(String fileName) {
		File file = new File(fileName);
		List<String[]> list = new ArrayList<String[]>();
		String[] fileStr = new String[10];
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				System.out.println("line " + line + ": " + tempString);
				line++;
				fileStr = tempString.split(",");
				list.add(fileStr);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return list;
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
