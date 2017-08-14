package com.engine.tag.gettag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.engine.jdbcPage.Pageable;
import com.engine.jdbcPage.PageableResultSet;
import com.engine.tag.Tag;
import com.engine.util.DataSource;
import com.engine.util.Define;
import com.engine.util.PageNextHandler;
import com.strongmvc.orm.hibernate.Page;

public class TestTag extends Tag { 
	//<GET TestTag(10)>@数据ID@,@数据标题@,@数据时间@</GET>
	public String parse() {
		//获取参数
		int pageNo = Integer.valueOf((String)this.para.get(0)).intValue();//每页条数
		
		//设置当前页
		int curpage = 1; 
		if( requestMap.get(Define.WEB_PAGE) != null && !"".equals(requestMap.get(Define.WEB_PAGE))){
			curpage = Integer.valueOf((String)requestMap.get(Define.WEB_PAGE)).intValue();
		}
		//查询数据
		Connection Conn=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuffer returnHtml=new StringBuffer();
		try {
			//获取数据源//数据库类型,数据库地址,数据库名,用户名,密码,字符集
			Conn = DataSource.getConn("oracle", "10.1.11.9:1521", "orcl", "oa_bgt", "password", "utf-8");
			//查询数据
			String sql = "select t.articles_id,t.articles_title,t.articles_createdate from T_OA_INFOPUBLISH_ARTICLES t where t.articles_aticlestate = ?";
			ps = Conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ps.setString(1,"2"); 
			rs  = ps.executeQuery();
			Pageable page = new PageableResultSet(rs);
			page.setPageSize(pageNo);
			page.gotoPage(curpage);
			//当前页数据条数
			int rowsCount = page.getPageRowsCount();
			
			//替换占位符
			String dataId = "";//@数据ID@
			String dataTitle = "";//@数据标题@
			String dataTime = "";//@数据时间@
			String temp = "";
			
			if(rowsCount>0){
					//处理数据
					for(int i=0;i<rowsCount;i++){
						dataId = page.getObject(1) == null?"":page.getObject(1).toString();
						dataTitle = page.getObject(2) == null?"":page.getObject(2).toString();
						dataTime = page.getObject(3) == null?"":page.getObject(3).toString();
						temp = this.htmlContent;
						temp = temp.replaceAll(quote("@数据ID@"),quoteReplacement(dataId));
						temp = temp.replaceAll(quote("@数据标题@"),quoteReplacement(dataTitle));
						temp = temp.replaceAll(quote("@数据时间@"),quoteReplacement(dataTime));
						returnHtml.append(temp);
						//取下一页
						page.next();
					}
					//处理翻页
					temp = this.pageHtml;
					if(temp!=null && !"".equals(temp)){
						PageNextHandler pageNextHandler = new PageNextHandler();
						temp = temp.replaceAll(quote("#PAGE_HTML#"),quoteReplacement(pageNextHandler.getHTML( new int[]{page.getPageCount(), page.getRowsCount(),curpage}, null, (String)requestMap.get(Define.WEB_PAGE_TYPE)+".shtml")));
						returnHtml.append(temp);
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{//关闭数据库链接
			try {
				if(rs!=null) rs.close();
				if(ps!=null) ps.close();
				if(Conn!=null) Conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
		    }
		}
		return returnHtml.toString();
	}
}
