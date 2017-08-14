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

public class ColumnName extends Tag { 
	//<GET ColumnName($key)>@栏目ID@,@栏目名称@</GET>
	public String parse() {
		//获取配置参数
		String columnId = (String)this.para.get(0);//栏目ID

			
		String sql = "select t.clumnId,t.clumnName from ToaInfopublishColumn t where t.clumnId = ?";
		List list = this.engineDao.find(sql, new Object[]{columnId});
		
		//替换占位符
		String dataId="";//@栏目ID@
		String dataTitle="";//@栏目名称@
		
		StringBuffer returnHtml=new StringBuffer();
		String temp = "";

		if(list != null && list.size()> 0){
			//数据处理
				Object[] column = (Object[]) list.get(0);
				dataId = column[0] == null ?"": column[0].toString();
				dataTitle = column[1] == null ?"": column[1].toString();
				
				temp = this.htmlContent;
				temp = temp.replaceAll(quote("@栏目ID@"),quoteReplacement(dataId));
				temp = temp.replaceAll(quote("@栏目名称@"),quoteReplacement(dataTitle));
				returnHtml.append(temp);
			
		}
		return returnHtml.toString();
	}
}
