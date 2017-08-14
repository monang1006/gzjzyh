package com.engine.tag.gettag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import com.engine.jdbcPage.Pageable;
import com.engine.jdbcPage.PageableResultSet;
import com.engine.tag.Tag;
import com.engine.util.DataSource;
import com.engine.util.Define;


public class gwContent extends Tag { 
	//<GET gwContent($key)>@数据ID@,@数据内容@</GET>
	public String parse() {
		//获取参数
		String fwhdm = (String)this.para.get(0);
		//查询数据
		Connection Conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null; 
		InputStream in=null;
		StringBuffer returnHtml=new StringBuffer();
		HttpSession session = (HttpSession) requestMap.get(Define.WEB_SESSION);
		String userName = "";
		
		if(session != null){
			try{	
				if(session.getAttribute("userName") != null){				
					userName = session.getAttribute("userName").toString();				
				}			
			}catch(Exception e){
				System.out.println("已经创建新的session!");
			}
		}

		try {
			//读取infopubDB.properties配置文件获取数据源
			in = this.getClass().getClassLoader().getResourceAsStream("infopubDB.properties");    
			Properties p = new Properties(); 
			p.load(in);
			
			String dbType = p.getProperty("jdbc.dbType");
			String dbPath = p.getProperty("jdbc.dbPath");
			String dbName = p.getProperty("jdbc.dbName");
			String dbUesr = p.getProperty("jdbc.dbUser");
			String dbPass = p.getProperty("jdbc.dbPass");
			String dbChar = p.getProperty("jdbc.dbChar");

			//获取数据源//数据库类型,数据库地址,数据库名,用户名,密码,字符集
			Conn = DataSource.getConn(dbType, dbPath, dbName, dbUesr, dbPass, dbChar);
			//查询数据
			String sql = "select t.FWHDM,t.FZW,t.FWH,t.FROLE from DADATA0 as t where t.FWHDM = ? and FROLE >='1' and FROLE <='7' and  FMJ = ''";
			ps = Conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ps.setString(1,fwhdm); 
			rs  = ps.executeQuery();
			Pageable page = new PageableResultSet(rs);
			page.setPageSize(2);
			page.gotoPage(1);
			//当前页数据条数
			int rowsCount = page.getPageRowsCount();
			
			//替换占位符
			Object dataId;//@数据ID@
			Object dataContent;//@数据内容@
			String dataNum = ""; //数据文号
			String dataRole = ""; // 文件级别
			
			String dataContentv= "";
			
			String temp = "";
			if(rowsCount>0){
				//处理数据
				dataId = page.getObject(1);
				if(dataId == null) dataId = "";
				if(dataId instanceof Clob){
					dataId = changeStr((Clob)dataId);
				}
				dataContent = page.getObject(2);
				if(dataContent == null) dataContent = "";
				if(dataContent instanceof Clob){
					dataContent = changeStr((Clob)dataContent);
				}
				Object num = page.getObject(3);
				Object role = page.getObject(4);
				if(num != null){
					
					dataNum = num.toString();
				}
				if(role != null){
					
					dataRole = role.toString();
				}
				
				temp = this.htmlContent;
				temp = temp.replaceAll(quote("@数据ID@"),quoteReplacement(dataId.toString()));
				
				if((dataNum.contains("赣府文") || dataRole.equals("7")) && ("".equals(userName)|| userName == null) ){
					
					dataContentv = "<div align='center'>您没有读取此文件的权限!</div>";
					
				}else{
										
					dataContentv = dataContent.toString().replaceAll("\\Qbgcolor='#999999'\\E","");
					//江西省人民政府文件 <img src=\"resource/images/fwwt/zfej.gif\" style=\"width:520px\" />
//					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>江西省人民政府文件</span>"), 
//							quoteReplacement("<img src=\"resource/images/fwwt/zfej.gif\" style=\"width:520px\" />"+"<br><br><br><br><br><br>"));
					if (!(dataNum.contains("赣府办通报"))){
						dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>江西省人民政府文件</span>"), 
								quoteReplacement("<img src=\"resource/images/fwwt/zfej.gif\" style=\"width:520px\" /><br><br><br><br><br><br>"));
					}else {
						dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>江西省人民政府文件</span>"), 
								quoteReplacement("<img src=\"resource/images/fwwt/gfbtb.gif\" style=\"width:400px\" /><br><br><br><br><br><br>"));
					}
					
					//中共江西省人民政府办公厅党组文件 <img src=\"resource/images/fwwt/bgtdzwj.gif\" style=\"width:680px\"/>
					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>中共江西省人民政府办公厅党组文件</span>"), 
							quoteReplacement("<img src=\"resource/images/fwwt/bgtdzwj.gif\" style=\"width:680px\"/>"+"<br><br><br><br><br><br>"));
					
					//江西省人民政府办公厅 <img src=\"resource/images/fwwt/zfbgt.gif\" style=\"width:620px\" /> 
//					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>江西省人民政府办公厅</span>"), 
//							quoteReplacement("<img src=\"resource/images/fwwt/zfbgt.gif\" style=\"width:620px\" />"+"<br><br><br><br><br><br>"));
					if (!(dataNum.contains("参阅文件"))){
						dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>江西省人民政府办公厅</span>"), 
								quoteReplacement("<img src=\"resource/images/fwwt/zfbgt.gif\" style=\"width:620px\" /><br><br><br><br><br><br>"));
					}else{
						dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>江西省人民政府办公厅</span>"), 
								quoteReplacement("<img src=\"resource/images/fwwt/cywj.gif\" style=\"width:400px\" /><br><br><br><br><br><br>"));
					}
					
					//江西省发电 <img src=\"resource/images/fwwt/sfd.gif\" style=\"width:400px\" />
					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>江西省发电</span>"), 
							quoteReplacement("<img src=\"resource/images/fwwt/sfd.gif\" style=\"width:400px\" />"+"<br><br><br><br><br><br>"));
					
					//中共江西省人民政府党组文件 <img src=\"resource/images/fwwt/zfdzwj.gif\" style=\"width:590px\"/> 
					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>中共江西省人民政府党组文件</span>"), 
							quoteReplacement("<img src=\"resource/images/fwwt/zfdzwj.gif\" style=\"width:590px\"/>"+"<br><br><br><br><br><br>"));
					
					//江西省人民政府办公厅公文处理单 <img src=\"resource/images/fwwt/wjcld.gif\" style=\"width:680px\" />
					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>江西省人民政府办公厅公文处理单</span>"), 
							quoteReplacement("<img src=\"resource/images/fwwt/wjcld.gif\" style=\"width:680px\" />"+"<br><br><br><br><br><br>"));
					
					//江西省人民政府办公厅发文稿纸 <img src=\"resource/images/fwwt/fwgz.gif\" style=\"width:620px\" />
					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>江西省人民政府办公厅发文稿纸</span>"), 
							quoteReplacement("<img src=\"resource/images/fwwt/fwgz.gif\" style=\"width:620px\" />"+"<br><br><br><br><br><br>"));
					
					//江西省人民政府 <img src=\"resource/images/fwwt/srezf.gif\" style=\"width:580px\" />
					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>江西省人民政府</span>"), 
							quoteReplacement("<img src=\"resource/images/fwwt/srezf.gif\" style=\"width:580px\" />"+"<br><br><br><br><br><br>"));
					
					//江西省人民政府办公厅文件 <img src=\"resource/images/fwwt/bgjwj.gif\" style=\"width:640px\" />
					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>江西省人民政府办公厅文件</span>"), 
							quoteReplacement("<img src=\"resource/images/fwwt/bgjwj.gif\" style=\"width:640px\" />"+"<br><br><br><br><br><br>"));
					
					//参阅文件 <img src=\"resource/images/fwwt/bgjwj.gif\" style=\"width:640px\" />
					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>参阅文件</span>"), 
							quoteReplacement("<img src=\"resource/images/fwwt/cywj.gif\" style=\"width:400px\" />"+"<br><br><br><br><br><br>"));
					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>参 阅 文 件</span>"), 
							quoteReplacement("<img src=\"resource/images/fwwt/cywj.gif\" style=\"width:400px\" />"+"<br><br><br><br><br><br>"));

					//赣府办通报 <img src=\"resource/images/fwwt/bgjwj.gif\" style=\"width:640px\" />
					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>赣府办通报</span>"), 
							quoteReplacement("<img src=\"resource/images/fwwt/gfbtb.gif\" style=\"width:400px\" />"+"<br><br><br><br><br><br>"));
					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>赣 府 办 通 报</span>"), 
							quoteReplacement("<img src=\"resource/images/fwwt/gfbtb.gif\" style=\"width:400px\" />"+"<br><br><br><br><br><br>"));
					
					dataContentv = dataContentv.replaceAll(quote("<span  class= 'title'>江西省人民政府令</span>"), 
							quoteReplacement("<img src=\"resource/images/fwwt/zfl.gif\" style=\"width:560px\" /><br><br><br><br><br><br>"));


					
				}
								
				temp = temp.replaceAll(quote("@数据内容@"),quoteReplacement(dataContentv));
				returnHtml.append(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{//关闭数据库链接
			try {
				if(in!=null)in.close();
				if(Conn!=null) Conn.close();
				if(ps!=null) ps.close();
				if(rs!=null) rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
		    } catch (IOException e) {
				e.printStackTrace();
			}
		}
		return returnHtml.toString();
	}
	public String changeStr(Clob content) {
			
			Reader is =null;
			BufferedReader br=null;
			StringBuffer sb = new StringBuffer();
			try {
				is = content.getCharacterStream();// 得到流
				br = new BufferedReader(is);
				String s = br.readLine();
				while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
					sb.append(s);
					s = br.readLine();
				}   
			} catch (Exception e) {
				System.out.println("没有查到对应标签的数据！");
			}finally{
				try{
					is.close();
					br.close();
				}catch (Exception e) {}
			}
			return sb.toString();
		}
}
