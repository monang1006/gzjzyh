package com.engine.tag.gettag;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import com.engine.jdbcPage.Pageable;
import com.engine.jdbcPage.PageableResultSet;
import com.engine.tag.Tag;
import com.engine.util.DataSource;
import com.engine.util.Define;
import com.engine.util.PageNextHandler;
import com.strongit.oa.common.user.util.TimeKit;

public class GovFile extends Tag { 
	//<GET GovFile(10,yyyy-MM-dd,20)>@文号代码@,@文号@,@标题@,@签发时间@,@完整标题@,@文件级别@</GET>
	public String parse() {
		//获取参数
		int pageNo = Integer.valueOf((String)this.para.get(0)).intValue();//每页条数
		String timeFormat = (String)this.para.get(1);//时间格式
		String dataType = (String)requestMap.get("dataType");            //分页条件
		int titleLength = Integer.valueOf((String)this.para.get(2)).intValue();//标题长度    
		String type = (String)requestMap.get("type");            //搜索条件  文件类型
		String newTitle = (String)requestMap.get("newTitle"); 	 //搜索条件  文件标题
		
		if("1".equals(type)){
			type = "赣府发";
		}else if("2".equals(type)){
			type = "赣府字";
		}else if("3".equals(type)){
			type = "赣府厅发";
		} else if("4".equals(type)){
			type = "赣府厅字";
		} else if("5".equals(type)){
			type = "赣府文";
		} else if("6".equals(type)){
			type = "赣府厅文";
		} else if("7".equals(type)){
			type = "省政府令";
		} else if("8".equals(type)){
			type = "赣府厅秘";
		} else if("9".equals(type)){
			type = "赣府阅";
		} else if("10".equals(type)){
			type = "赣府办通报";
		} else if("11".equals(type)){
			type = "参阅文件";
		}  
		
		//高级搜索条件
		Map types = new HashMap();
		types.put("0","null" );
		types.put("1", "赣府发");
		types.put("2", "赣府字");
		types.put("3", "赣府厅发");
		types.put("4", "赣府厅字");
		types.put("5", "赣府文");
		types.put("6", "赣府厅文");
		types.put("7", "省政府令");
		types.put("8", "赣府厅秘");
		types.put("9", "赣府阅");
		types.put("10", "赣府办通报");
		types.put("11", "参阅文件");
		String stype = (String)requestMap.get("stype"); 
		if(type == null  || "null".equals(type) ||"".equals(type)){			
			type = (String) types.get(stype);
		}
		String stitle = (String)requestMap.get("stitle");
		String fwh = (String)requestMap.get("fwh"); 
		String ztc1 = (String)requestMap.get("ztc1");
		String ztc2 = (String)requestMap.get("ztc2");
		String ztc3 = (String)requestMap.get("ztc3");
		String zz = (String)requestMap.get("zz");
		String date1 = (String)requestMap.get("date1");
		String date2 = (String)requestMap.get("date2");
		
		HttpSession session = (HttpSession) requestMap.get(Define.WEB_SESSION);
		String userName = "";
		
		if(session != null){
			try{	
				if(session.getAttribute("userName") != null){				
					userName = session.getAttribute("userName").toString();				
				}			
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		if(newTitle == null)newTitle = "";
		
		//设置当前页
		int curpage = 1; 
		if( requestMap.get(Define.WEB_PAGE) != null && !"".equals(requestMap.get(Define.WEB_PAGE))){
			curpage = Integer.valueOf((String)requestMap.get(Define.WEB_PAGE)).intValue();
		}
		//查询数据
		Connection Conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		InputStream in = null;
		newTitle = newTitle.replaceAll(quote("@"),quoteReplacement("%"));
		try {
			newTitle = java.net.URLDecoder.decode(newTitle,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuffer returnHtml=new StringBuffer();
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
			//Conn = DataSource.getConn("sql2000", "10.1.11.9:1433", "abc", "sa", "password", "utf-8");
			Conn = DataSource.getConn(dbType, dbPath, dbName, dbUesr, dbPass, dbChar);
			//查询数据
			
			StringBuffer sql = new StringBuffer(
					 "select DISTINCT FWHDM,FWH,FBT,FRQ,FROLE from DADATA0 where FROLE >='1' and FROLE <='7' and  FMJ = '' ");
			
			if(dataType != null &&!"null".equals(dataType) && !"".equals(dataType)){
				sql.append(" and FWH like '%" + dataType
						+ "%'");
			}
			
			if(type != null &&!"null".equals(type)&& !"".equals(type)){
				sql.append(" and FWH like '%" + type
						+ "%'");
			}
			if(newTitle !=null &&!"null".equals(newTitle) && !"".equals(newTitle)){
				sql.append("and FBT like '%" + newTitle
						+ "%'");
				
			}
			
			if(fwh !=null && !"".equals(fwh)){
				sql.append(" and FWH like '%" + fwh
						+ "%'");
			}
			
			if(stitle !=null && !"".equals(stitle)){
				sql.append(" and FBT like '%"+ stitle
						+ "%'");
			}
			
			if(ztc1 !=null && !"".equals(ztc1)){
				sql.append(" and FZTC like '%"+ ztc1
						+ "%'");
			}
			
			if(ztc2 !=null && !"".equals(ztc2)){
				sql.append(" and FZTC like '%"+ ztc2
						+ "%'");
			}
			
			if(ztc3 !=null && !"".equals(ztc3)){
				sql.append(" and FZTC like '%"+ ztc3
						+ "%'");
			}
			
			if(zz !=null && !"".equals(zz)){
				sql.append(" and FZZ like '%"+ zz
						+ "%'");
			}
			if(date1 !=null && !"".equals(date1)){
				sql.append(" and FRQ >= '"+ date1
						+"'");
			}
			if(date2 !=null && !"".equals(date2)){
				sql.append(" and FRQ <= '"+ date2
						+"'");
			}
			sql.append(" order by FRQ DESC,FWH DESC");
			
			//String sql = "select FWHDM,FWH,FBT,FRQ from DADATA0 where FROLE='1' and FWH not like '赣府文%'";
			ps = Conn.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//ps.setString(1,"2"); 
			rs  = ps.executeQuery();
			Pageable page = new PageableResultSet(rs);
			page.setPageSize(pageNo);
			page.gotoPage(curpage);
			//当前页数据条数
			int rowsCount = page.getPageRowsCount();
			
			//替换占位符
			String dataId = "";//@文号代码@
			String dataNum = "";//@文号@
			String dataTitle = "";//@标题@
			String dataTime = "";//@签发时间@
			String allTitle= "";//@完整标题@
			String dataRole = ""; //@文件级别@
			String temp = "";
			
			if(rowsCount>0){
					//处理数据
					for(int i=0;i<rowsCount;i++){
												
						dataId = page.getObject(1) == null?"":page.getObject(1).toString();
						dataNum = page.getObject(2) == null?"":page.getObject(2).toString();
						allTitle = page.getObject(3) == null?"":page.getObject(3).toString();
						allTitle = allTitle.trim();
						double allTitleLength = 0;
						int al = allTitle.length();
						char[] chars=allTitle.toCharArray();
						int l;
						for(l = 0; (l < al)&&(allTitleLength < titleLength); l ++ ){
							int v = (int)chars[l];
							if(v<255){
								allTitleLength = allTitleLength+0.5;
							}else{
								allTitleLength++;
							}
						}
						if((allTitle.length() > titleLength)&&(l!=al)){
							dataTitle = allTitle.substring(0, l)+"...";
						}else{
							dataTitle = allTitle;
						}
						dataTime = page.getObject(4) == null?"":TimeKit.formatDate((Date)page.getObject(4), timeFormat);
						if(dataType==null || dataType == ""){
							dataType = type;
						}
						
						dataRole = page.getObject(5) == null?"":page.getObject(5).toString();
						if(dataRole.equals("7") && "".equals(userName)){
							
							dataTitle = dataNum;
							allTitle = dataNum;
						}
						
						if(dataNum.contains("赣府文")&&"".equals(userName)){
							dataTitle = dataNum;
							allTitle = dataNum;
						}
						
						temp = this.htmlContent;
						temp = temp.replaceAll(quote("@文号代码@"),quoteReplacement(dataId));
						temp = temp.replaceAll(quote("@文号@"),quoteReplacement(dataNum));
						temp = temp.replaceAll(quote("@标题@"),quoteReplacement(dataTitle));
						temp = temp.replaceAll(quote("@完整标题@"),quoteReplacement(allTitle));
						temp = temp.replaceAll(quote("@签发时间@"),quoteReplacement(dataTime));
						temp = temp.replaceAll(quote("@文件级别@"),quoteReplacement(dataRole));
						returnHtml.append(temp);
						//取下一页
						page.next();
					}
					
					//处理翻页
					temp = this.pageHtml;
					if(temp!=null && !"".equals(temp)){
						Map map = new HashMap();
						map.put("dataType", dataType);
						map.put("type", type);
						map.put("newTitle",newTitle);
						//高级搜索时的条件
						if(stitle != null && !"".equals(stitle) ){							
							map.put("stitle", stitle);
						}
						if(fwh != null && !"".equals(fwh) ){							
							map.put("fwh", fwh);
						}
						if(ztc1 != null && !"".equals(ztc1) ){							
							map.put("ztc1", ztc1);
						}
						if(ztc2 != null && !"".equals(ztc2) ){							
							map.put("ztc2", ztc2);
						}
						if(ztc3 != null && !"".equals(ztc3) ){							
							map.put("ztc3", ztc3);
						}
						if(zz != null && !"".equals(zz) ){							
							map.put("zz", zz);
						}
						if(date1 != null && !"".equals(date1) ){							
							map.put("date1", date1);
						}
						if(date2 != null && !"".equals(date2) ){							
							map.put("date2", date2);
						}
						
						PageNextHandler pageNextHandler = new PageNextHandler();
						temp = temp.replaceAll(quote("#PAGE_HTML#"),quoteReplacement(pageNextHandler.getHTML( new int[]{page.getPageCount(), page.getRowsCount(),curpage}, map, (String)requestMap.get(Define.WEB_PAGE_TYPE)+".shtml")));
						returnHtml.append(temp);
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{//关闭数据库链接
			try {
				if(in!=null) in.close();
				if(rs!=null) rs.close();
				if(ps!=null) ps.close();
				if(Conn!=null) Conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
		    } catch (IOException e) {
				e.printStackTrace();
			}
		}		
		
		return returnHtml.toString();
	}
}
