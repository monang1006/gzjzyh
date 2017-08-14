package com.engine.tag.gettag;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.engine.tag.Tag;
import com.engine.util.Define;
import com.engine.util.PageNextHandler;
import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongmvc.orm.hibernate.Page;

public class ColumnArticleHot extends Tag { 
	//<GET ColumnArticleHot(id,30)>@数据ID@,@数据标题@,@完整标题@</GET>
	public String parse() {
		//获取配置参数
		String columnId = (String)this.para.get(0);//栏目ID
		int titleLength = Integer.valueOf((String)this.para.get(1)).intValue();//标题长度     
			
		String sql = "select t.columnArticleId,t.toaInfopublishArticle.articlesTitle from ToaInfopublishColumnArticl  t where t.toaInfopublishColumn.clumnId=? and t.columnArticleState = '9' order by  t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC ";
		List list = this.engineDao.find(sql, new Object[]{columnId});
		
		//替换占位符
		String dataId="";//@数据ID@
		String dataTitle="";//@数据标题@
		String allTitle = ""; //@完整标题@
		
		StringBuffer returnHtml=new StringBuffer();
		String temp = "";
		
		if(list != null && list.size()> 0){
			//数据处理
				Object[] column = (Object[]) list.get(0);
				dataId = column[0] == null ?"": column[0].toString();
				allTitle = column[1] == null ?"": column[1].toString();
								
				if(dataTitle.length()> titleLength){
					
					dataTitle = allTitle.substring(0, titleLength)+"...";
				}else{
					dataTitle = allTitle ;
				}
				
				temp = this.htmlContent;
				temp = temp.replaceAll(quote("@数据ID@"),quoteReplacement(dataId));
				temp = temp.replaceAll(quote("@数据标题@"),quoteReplacement(dataTitle));
				temp = temp.replaceAll(quote("@完整标题@"),quoteReplacement(allTitle));
				returnHtml.append(temp);
			
		}
		return returnHtml.toString();
	}
}
