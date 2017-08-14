package com.engine.tag.gettag;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.engine.tag.Tag;
import com.engine.util.Define;
import com.engine.util.PageNextHandler;
import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongmvc.orm.hibernate.Page;

public class ColumnArticleNew extends Tag { 
	//<GET ColumnArticleNew(10,yyyy-MM-dd,11)>@数据ID@,@数据标题@,@完整标题@</GET>
	public String parse() {
		//获取配置参数
		int pageNo = Integer.valueOf((String)this.para.get(0)).intValue();//每页条数
		String timeFormat = (String)this.para.get(1);//时间格式
		int titleLength = Integer.valueOf((String)this.para.get(2)).intValue();//标题长度   
		
		//获取页码
		int curpage = 1; 
		if( requestMap.get(Define.WEB_PAGE) != null && !"".equals(requestMap.get(Define.WEB_PAGE))){
			curpage = Integer.valueOf((String)requestMap.get(Define.WEB_PAGE)).intValue();
		}
		
		//获取数据
		Page page = new Page(pageNo, true);
		page.setPageNo(curpage);
		page.setPageSize(pageNo);
			
		String sql = "select t.columnArticleId,t.toaInfopublishArticle.articlesTitle from ToaInfopublishColumnArticl  t " +
				"where t.columnArticleState = '9' order by  t.columnArticleIsstandtop DESC, t.columnArticleAddtime DESC ";
		page = this.engineDao.find(page,sql,new Object[]{});		
		List list = page.getResult();
		
		//替换占位符
		String dataId="";//@数据ID@
		String dataTitle="";//@数据标题@
		String allTitle = ""; //@完整标题@
		
		StringBuffer returnHtml=new StringBuffer();
		String temp = "";
		if(list != null && list.size()> 0){
			//数据处理
			for (int i = 0; i < list.size(); i++) {
				dataId = ((Object[])list.get(i))[0] == null ?"":((Object[])list.get(i))[0].toString();
				allTitle = ((Object[])list.get(i))[1] == null ?"":((Object[])list.get(i))[1].toString();
				if(allTitle.length()> titleLength){
					dataTitle = allTitle.substring(0, titleLength)+"...";
				}else{
					dataTitle = allTitle;
				}				
				temp = this.htmlContent;
				temp = temp.replaceAll(quote("@数据ID@"),quoteReplacement(dataId));
				temp = temp.replaceAll(quote("@数据标题@"),quoteReplacement(dataTitle));
				temp = temp.replaceAll(quote("@完整标题@"),quoteReplacement(allTitle));
				returnHtml.append(temp);
				
			}
		}
		return returnHtml.toString();
	}
}
