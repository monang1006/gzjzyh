package com.engine.tag.gettag;

import java.util.Date;
import java.util.List;
import com.engine.tag.Tag;
import com.engine.util.Define;
import com.strongit.oa.common.user.util.TimeKit;

public class noticeContent extends Tag { 
	//<GET noticeContent(yyyy-MM-dd)>@文章标题@,@文章作者@,@文章部门@,@文章内容@</GET>
	public String parse() {
		//获取配置参数
		String noticeId = (String)requestMap.get(Define.WEB_KEY);	
		String timeFormat = (String)this.para.get(0);//时间格式	
		//获取数据
		String sql = "select t.afficheTitle,t.afficheAuthor,t.afficheGov,t.afficheDesc,t.afficheTime from ToaAffiche t where t.afficheId = ?";
		List list = this.engineDao.find(sql, new Object[]{noticeId});
		
		//替换占位符
		String dataTitle="";//@文章标题@
		String dataAuthor = "";//@文章作者@
		String dataGov = "";//@文章部门@
		String dataContent = "";//@文章内容@
		String dataTime = "";  //@发布时间@
		
		StringBuffer returnHtml=new StringBuffer();
		String temp = "";
		if(list != null && list.size()> 0){
			//数据处理
			Object[] datas = (Object[])list.get(0);
			dataTitle = datas[0] == null?"":datas[0].toString();
			dataAuthor = datas[1] == null?"":datas[1].toString();
			dataGov = datas[2] == null?"":datas[2].toString();
			dataContent = datas[3] == null?"":datas[3].toString();
			dataTime = datas[4] == null?"":TimeKit.formatDate((Date)datas[4],timeFormat);
		
			dataContent = dataContent.replaceAll(quote("&lt;"),quoteReplacement("<")).replaceAll(quote("&gt;"),quoteReplacement(">")).
			replaceAll(quote("&amp;"),quoteReplacement("&")).replaceAll(quote("&quot;"),quoteReplacement("'"));
			
			temp = this.htmlContent;
		    temp = temp.replaceAll(quote("@文章标题@"),quoteReplacement(dataTitle));
			temp = temp.replaceAll(quote("@文章作者@"),quoteReplacement(dataAuthor));
			temp = temp.replaceAll(quote("@文章部门@"),quoteReplacement(dataGov));
			temp = temp.replaceAll(quote("@文章内容@"),quoteReplacement(dataContent));
			temp = temp.replaceAll(quote("@发布时间@"),quoteReplacement(dataTime));
			
			returnHtml.append(temp);	
		}
		return returnHtml.toString();
	}
}
