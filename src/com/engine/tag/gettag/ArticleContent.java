package com.engine.tag.gettag;

import java.util.Date;
import java.util.List;
import com.engine.tag.Tag;
import com.engine.util.Define;
import com.strongit.oa.common.user.util.TimeKit;

public class ArticleContent extends Tag { 
	//<GET ArticleContent(yyyy-MM-dd)>@文章标题@,@文章时间@,@文章作者@,@文章来源@,@文章内容@</GET>
	public String parse() {
		//获取配置参数
		String articlesId = (String)requestMap.get(Define.WEB_KEY);	
		String timeFormat = (String)this.para.get(0);//时间格式	
		//获取数据
		String sql = "select t.toaInfopublishArticle.articlesTitle,t.toaInfopublishArticle.articlesSubtitle,t.toaInfopublishArticle.articlesAutopublishtime,t.toaInfopublishArticle.articlesAuthor,t.toaInfopublishArticle.articlesSource,t.toaInfopublishArticle.articlesArticlecontent " +
				"from ToaInfopublishColumnArticl t where t.columnArticleId = ?  and t.columnArticleState = '9' order by t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC";
		List list = this.engineDao.find(sql, new Object[]{articlesId});
		
		//替换占位符
		String dataTitle="";//@文章标题@
		String subTitle ="";//@文章副标题@
		String dataTime="";//@文章时间@
		String dataAuthor = "";//@文章作者@
		String dataSource = "";//@文章来源@
		String dataContent = "";//@文章内容@
		
		StringBuffer returnHtml=new StringBuffer();
		String temp = "";
		if(list != null && list.size()> 0){
			//数据处理
			Object[] datas = (Object[])list.get(0);
			dataTitle = datas[0] == null?"":datas[0].toString();
			subTitle = datas[1] == null?"":datas[1].toString();
			dataTime = datas[2] == null?"":TimeKit.formatDate((Date)datas[2],timeFormat);
			dataAuthor = datas[3] == null?"":datas[3].toString();
			dataSource = datas[4] == null?"无":datas[4].toString();
			dataContent = datas[5] == null?"":datas[5].toString();
			
			temp = this.htmlContent;
		    temp = temp.replaceAll(quote("@文章标题@"),quoteReplacement(dataTitle));
		    temp = temp.replaceAll(quote("@文章副标题@"),quoteReplacement(subTitle));
			temp = temp.replaceAll(quote("@文章时间@"),quoteReplacement(dataTime));
			temp = temp.replaceAll(quote("@文章作者@"),quoteReplacement(dataAuthor));
			temp = temp.replaceAll(quote("@文章来源@"),quoteReplacement(dataSource));
			temp = temp.replaceAll(quote("@文章内容@"),quoteReplacement(dataContent));
			
			returnHtml.append(temp);	
		}
		return returnHtml.toString();
	}
}
