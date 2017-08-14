package com.engine.tag.gettag;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.engine.tag.Tag;
import com.engine.util.Define;
import com.engine.util.PageNextHandler;
import com.strongit.oa.common.user.util.TimeKit;
import com.strongmvc.orm.hibernate.Page;

public class ColumnArticleList extends Tag { 
	//<GET ColumnArticleList(id,10,yyyy-MM-dd,30)>@数据ID@,@数据标题@,@数据时间@,@完整标题@,@文章作者@</GET>
	public String parse() {

		StringBuffer returnHtml=new StringBuffer();
		try{
			
			//获取配置参数
			String columnId = (String)this.para.get(0);//栏目ID
			int pageNo = Integer.valueOf((String)this.para.get(1)).intValue();//每页条数
			String timeFormat = (String)this.para.get(2);//时间格式
			int titleLength = Integer.valueOf((String)this.para.get(3)).intValue();//标题长度     
			
			//获取页码
			int curpage = 1; 
			if( requestMap.get(Define.WEB_PAGE) != null && !"".equals(requestMap.get(Define.WEB_PAGE))){
				curpage = Integer.valueOf((String)requestMap.get(Define.WEB_PAGE)).intValue();
			}
			
			//获取数据
			Page page = new Page(pageNo, true);
			page.setPageNo(curpage);
			page.setPageSize(pageNo);
			Date date = new Date();
			String sql = "select t.columnArticleId,t.toaInfopublishArticle.articlesTitle,t.toaInfopublishArticle.articlesCreatedate,t.toaInfopublishArticle.articlesAuthor" +
			" from ToaInfopublishColumnArticl t where t.toaInfopublishColumn.clumnId = ? " +
			"and ( (t.toaInfopublishArticle.articlesAutopublishtime < ? and t.toaInfopublishArticle.articlesAutocancletime > ? ) " +
			"or t.toaInfopublishArticle.articlesAutocancletime is null)and t.columnArticleState = '9' " +
			"order by t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC";
			page = this.engineDao.find(page,sql,new Object[]{columnId,date,date});
			if(page.getTotalCount()==0){
				sql = "select t.columnArticleId,t.toaInfopublishArticle.articlesTitle,t.toaInfopublishArticle.articlesCreatedate,t.toaInfopublishArticle.articlesAuthor" +
				" from ToaInfopublishColumnArticl t where t.toaInfopublishColumn.clumnId in " +
				"( select c.clumnId from ToaInfopublishColumn c where c.clumnParent = ? )"+
				"and ( (t.toaInfopublishArticle.articlesAutopublishtime < ? and t.toaInfopublishArticle.articlesAutocancletime > ? ) " +
				"or t.toaInfopublishArticle.articlesAutocancletime is null)and t.columnArticleState = '9' " +
				"order by t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC";
				page = this.engineDao.find(page,sql,new Object[]{columnId,date,date});
			}
			List list = page.getResult();
			
			//替换占位符
			String dataId="";//@数据ID@
			String dataTitle="";//@数据标题@
			String dataTime = "";//@数据时间@
			String allTitle = ""; //@完整标题@
			String dataAuthor = "";//@文章作者@
			
			String temp = "";
			if(list != null && list.size()> 0){
				//数据处理
				for(int i=0;i<list.size();i++){
					dataId = ((Object[])list.get(i))[0] == null ?"":((Object[])list.get(i))[0].toString();
					allTitle = ((Object[])list.get(i))[1] == null ?"":((Object[])list.get(i))[1].toString();
					allTitle = allTitle.trim();
					double allTitleLength = 0;
					int al = allTitle.length();
					char[] chars=allTitle.toCharArray();
					int l;
					for(l = 0; (l < al)&&(allTitleLength < titleLength); l ++ ){
						int v = (int)chars[l];
						if(v<=255){
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
					dataTime = ((Object[])list.get(i))[2] == null ?"":TimeKit.formatDate((Date)((Object[])list.get(i))[2], timeFormat);
					dataAuthor = ((Object[])list.get(i))[3] == null ?"":((Object[])list.get(i))[3].toString();
					
					temp = this.htmlContent;
					temp = temp.replaceAll(quote("@数据ID@"),quoteReplacement(dataId));
					temp = temp.replaceAll(quote("@数据标题@"),quoteReplacement(dataTitle));
					temp = temp.replaceAll(quote("@完整标题@"),quoteReplacement(allTitle));
					temp = temp.replaceAll(quote("@数据时间@"),quoteReplacement(dataTime));
					temp = temp.replaceAll(quote("@文章作者@"),quoteReplacement(dataAuthor));
					returnHtml.append(temp);
				}
				//翻页处理
				temp = this.pageHtml;
				if(temp!=null && !"".equals(temp)){
					Map map = new HashMap();
					map.put(Define.WEB_KEY, columnId);
					PageNextHandler pageNextHandler = new PageNextHandler();
					temp = temp.replaceAll(quote("#PAGE_HTML#"),quoteReplacement(pageNextHandler.getHTML( new int[]{page.getTotalPages(), page.getTotalCount(),curpage}, map, (String)requestMap.get(Define.WEB_PAGE_TYPE)+".shtml")));
					returnHtml.append(temp);
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnHtml.toString();
	}
}
