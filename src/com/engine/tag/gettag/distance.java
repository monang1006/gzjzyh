package com.engine.tag.gettag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.engine.tag.Tag;
import com.engine.util.Define;

public class distance extends Tag { 
	//<GET ArticleContent(yyyy-MM-dd)>@文章标题@,@文章时间@,@文章作者@,@文章来源@,@文章内容@</GET>
	public String parse() {
		String key = (String)this.para.get(0);
		SimpleDateFormat pim = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sim = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date();
		if(key ==null){
			key = pim.format(date);
		}
		try {
			date = pim.parse(key);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		key = sim.format(date)+"领导活动安排";
		StringBuffer returnHtml=new StringBuffer();
		String temp = "";
			
			temp = this.htmlContent;
		    temp = temp.replaceAll(quote("@标题@"),quoteReplacement(key));
			
			returnHtml.append(temp);	
		return returnHtml.toString();
	}
}
