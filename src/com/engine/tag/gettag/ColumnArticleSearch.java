package com.engine.tag.gettag;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.engine.tag.Tag;
import com.engine.util.Define;
import com.engine.util.PageNextHandler;
import com.strongit.oa.common.user.util.TimeKit;
import com.strongmvc.orm.hibernate.Page;

public class ColumnArticleSearch extends Tag { 
	//<GET ColumnArticleSearch(10,yyyy-MM-dd,20)>@数据ID@,@数据标题@,@数据时间@,@栏目名称@</GET>
	public String parse() {
		//获取配置参数
		int pageNo = Integer.valueOf((String)this.para.get(0)).intValue();//每页条数
		String timeFormat = (String)this.para.get(1);//时间格式
		int titleLength = Integer.valueOf((String)this.para.get(2)).intValue();//标题长度     
		String newTitle = (String)requestMap.get("newTitle");            //搜索条件
		if(newTitle== null) newTitle="";
		//获取页码
		int curpage = 1; 
		if( requestMap.get(Define.WEB_PAGE) != null && !"".equals(requestMap.get(Define.WEB_PAGE))){
			curpage = Integer.valueOf((String)requestMap.get(Define.WEB_PAGE)).intValue();
		}
		
		//获取数据
		Page page = new Page(pageNo, true);
		page.setPageNo(curpage);
		page.setPageSize(pageNo);
		String temp = newTitle;
		temp = temp.replaceAll(quote("@"),quoteReplacement("%"));
		try {
			temp = java.net.URLDecoder.decode(temp,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql = "select t.columnArticleId,t.toaInfopublishArticle.articlesTitle,t.toaInfopublishArticle.articlesCreatedate,t.toaInfopublishColumn.clumnName, t.toaInfopublishColumn.clumnId " +
				"from ToaInfopublishColumnArticl t where t.toaInfopublishArticle.articlesTitle like ? and t.columnArticleState = '9' order by t.toaInfopublishArticle.articlesCreatedate DESC";
		page = this.engineDao.find(page,sql,new Object[]{"%"+temp+"%"});
		List list = page.getResult();
		
		//替换占位符
		String dataId="";//@数据ID@
		String dataTitle="";//@数据标题@
		String title = ""; //@完整标题@
		String dataTime = "";//@数据时间@
		String columnName = "";//@栏目名称@
		String columnId = ""; //@栏目Id@
		StringBuffer returnHtml=new StringBuffer();
		temp = "";
		if(list != null && list.size()> 0){
			//数据处理
			for(int i=0;i<list.size();i++){
				dataId = ((Object[])list.get(i))[0] == null ?"":((Object[])list.get(i))[0].toString();
				title = ((Object[])list.get(i))[1] == null ?"":((Object[])list.get(i))[1].toString();
				dataTitle = title;
				if(dataTitle.length()> titleLength){
					dataTitle = dataTitle.substring(0, titleLength)+"...";
				}
				dataTime = ((Object[])list.get(i))[2] == null ?"":TimeKit.formatDate((Date)((Object[])list.get(i))[2], timeFormat);
				columnName = ((Object[])list.get(i))[3] == null ?"":((Object[])list.get(i))[3].toString();
				columnId = ((Object[])list.get(i))[4] == null ?"":((Object[])list.get(i))[4].toString();
				temp = this.htmlContent;
				temp = temp.replaceAll(quote("@数据ID@"),quoteReplacement(dataId));
				temp = temp.replaceAll(quote("@完整标题@"),quoteReplacement(title));
				temp = temp.replaceAll(quote("@数据标题@"),quoteReplacement(dataTitle));
				temp = temp.replaceAll(quote("@数据时间@"),quoteReplacement(dataTime));
				temp = temp.replaceAll(quote("@栏目名称@"),quoteReplacement(columnName));
				temp = temp.replaceAll(quote("@栏目Id@"),quoteReplacement(columnId));
				returnHtml.append(temp);
			}
			//翻页处理
			temp = this.pageHtml;
			if(temp!=null && !"".equals(temp)){
				Map map = new HashMap();
				map.put("newTitle", newTitle);
				PageNextHandler pageNextHandler = new PageNextHandler();
				temp = temp.replaceAll(quote("#PAGE_HTML#"),quoteReplacement(pageNextHandler.getHTML( new int[]{page.getTotalPages(), page.getTotalCount(),curpage}, map, (String)requestMap.get(Define.WEB_PAGE_TYPE)+".shtml")));
				returnHtml.append(temp);
			}
			
		}
		return returnHtml.toString();
	}
}
