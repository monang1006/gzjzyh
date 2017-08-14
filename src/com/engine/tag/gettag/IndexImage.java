package com.engine.tag.gettag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.engine.tag.Tag;
import com.engine.util.Define;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongmvc.orm.hibernate.Page;

public class IndexImage extends Tag { 
	//<GET IndexImage(id,4,20)>@首页图片@</GET>
	public String parse() {
		
		String columnId = (String)this.para.get(0);//栏目ID
		int showNum = Integer.valueOf((String)this.para.get(1)).intValue();//显示条数
		int titleLength = Integer.valueOf((String)this.para.get(2)).intValue();//主题长度     		
		
		//获取数据
		Page page = new Page(showNum, true);
		Date date = new Date();
		String sql = "select t from ToaInfopublishColumnArticl t where t.toaInfopublishColumn.clumnId = ? and t.columnArticleState = '9' " +
					"and t.toaInfopublishArticle.articlesPic is not null and ( (t.toaInfopublishArticle.articlesAutopublishtime < ? and t.toaInfopublishArticle.articlesAutocancletime > ? ) " +
				"or t.toaInfopublishArticle.articlesAutocancletime is null) order by t.toaInfopublishArticle.articlesCreatedate DESC";
		page = this.engineDao.find(page,sql,new Object[]{columnId,date,date});
		List list = page.getResult();
				
		
		StringBuffer innerHtml = new StringBuffer();
		if (list!=null&&list.size() > 0) {
			String src = "";
			String links = "";
			String texts = "";
			for (int i = 0; i<showNum &&i < list.size(); i++) {
				ToaInfopublishColumnArticl columnBo = (ToaInfopublishColumnArticl) list.get(i);
				String annexContent = columnBo.getToaInfopublishArticle()
						.getArticlesPic();
				int j = 0;
				if (!"".equals(annexContent) && annexContent != null) {
					src += "|" + requestMap.get(Define.WEB_CONTEXT_PATH)
							+ annexContent;
					links += "|News.shtml?key="
							+ columnBo.getColumnArticleId();
					String testTitle = "";
					if (columnBo.getToaInfopublishArticle()
							.getArticlesTitle().length() > titleLength)// 如果显示的内容长度大于设置的主题长度，则过滤该长度
						testTitle = columnBo.getToaInfopublishArticle()
								.getArticlesTitle().substring(0, titleLength)
								+ "...";
					else {
						testTitle = columnBo.getToaInfopublishArticle()
								.getArticlesTitle();
					}
					texts += "|" + testTitle;
					j++;
				}
				if (j == 4) {
					break;
				}
			}
			if (!"".equals(src)) {
				src = src.substring(1);
			}
			if (!"".equals(links)) {
				links = links.substring(1);
			}
			if (!"".equals(texts)) {
				texts = texts.substring(1);
			}
			innerHtml
				.append(
					"<object classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000\" codebase=\"http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\" width=\"280\" height=\"240\"> ")
				.append(
					"<param name=\"allowScriptAccess\" value=\"sameDomain\">")
			    .append("<param name=\"movie\" value=\"resource/flash/focus.swf\">")
			    .append(
					"<param name=\"quality\" value=\"high\"> ")
				.append("<param name=\"bgcolor\" value=\"#F0F0F0\"> ")
				.append(
					"<param name=\"menu\" value=\"false\"><param name=wmode value=\"opaque\"> ")
				.append("<param name=\"FlashVars\" value=\"pics=")
				.append(src)
				.append("&links=")
				.append(links)
				.append("&texts=")
				.append(texts)
				.append(
					"&borderwidth=280&borderheight=220&textheight=20\"> ")
				.append(
					"<embed src=\"../resource/flash/focus.swf\" wmode=\"opaque\" FlashVars=\"pics=")
				.append(src)
				.append("&links=")
				.append(links)
				.append("&texts=")
				.append(texts)
				.append(
					"&borderwidth=280&borderheight=220&textheight=20\" menu=\"false\" bgcolor=\"#F0F0F0\" quality=\"high\" width=\"280\" height=\"220\" allowScriptAccess=\"sameDomain\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" /> ")
				.append("</object>");
			

		}
		String temp = innerHtml.toString();
		temp = temp.replaceAll(quote("@首页图片@"),quoteReplacement(temp));
		StringBuffer returnHtml=new StringBuffer();
		returnHtml.append(temp);
		return returnHtml.toString();
	}
}
