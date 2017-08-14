package com.engine.servlet;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.engine.tag.Tag;
import com.engine.util.Define;
import com.strongit.oa.bo.ToaInfopublishTemplate;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
/**
 * 
 * Project : StrongIPP
 * Copyright : Strong Digital Technology Co. Ltd.
 * All right reserved
 * @author 曹钦
 * @version 5.0, 2011-8-30
 * Description:前台事件响应业务实现类
 */
@Service
public class EngineService implements IEngineService
{
	private GenericDAOHibernate engineDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		engineDao = new GenericDAOHibernate(sessionFactory, Object.class);
	}
	
	public String getParse(Map paras)
	{	
		String template = "";
		String shtmltype = (String)paras.get(Define.WEB_PAGE_TYPE);
		String temp="";
		String temp_1="";
		//根据类型获取模板
        template = getTemplate(shtmltype);
		//分析获取调用模板 {include(top)}
        Pattern pattern = Pattern.compile("\\{INCLUDE\\(.*?\\)PAGE\\}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(template);
       
        while (matcher.find()){
        	temp = matcher.group();
        	temp_1 = temp.substring(temp.indexOf("(")+1);
        	temp_1 = temp_1.substring(0,temp_1.lastIndexOf(")"));
        	temp_1 = getTemplate(temp_1);
        	template = template.replaceAll(quote(temp),quoteReplacement(temp_1));
        }
		//分析<GET class(,,)>@结果@</GET>标签
		pattern = Pattern.compile("<GET .*?\\(.*?\\)>.*?</GET>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(template);
		while (matcher.find()){
			//解析模板
			temp = matcher.group();
			template = template.replaceAll(quote(temp),quoteReplacement(parseHTML(temp,paras)));
		}
		return template;
	}
    
	public String postParse()
	{	
		return "postParseService";
	}
	public String parseHTML(String TagHtml , Map paras) {
		//"<GET .*?\\(.*?\\)>.*?</GET>"
		String temp="";
		String tagHTML = null;
		//分析标签解析类
		temp = TagHtml.substring(5);
		temp = temp.substring(0,temp.indexOf("("));
		try{
			Tag tag = (Tag)Class.forName("com.engine.tag.gettag."+temp).newInstance();
			tag.tagContent = TagHtml;
			tag.requestMap = paras;
			tag.engineDao = engineDao;
			//分析标签参数
			temp = TagHtml.substring(TagHtml.indexOf("(")+1);
			temp = temp.substring(0,temp.indexOf(")"));
			List para = new ArrayList();
			if(!"".equals(temp) && temp!=null){
				String[] tagvalues = temp.split(",");
	    		for(int i=0;i<tagvalues.length;i++){
	    			if(tagvalues[i].indexOf("$")!=-1){
	    				para.add(paras.get(tagvalues[i].substring(1)));
	    			}else{
	    				para.add(tagvalues[i]);
	    			}
	    		}
			}
			tag.para = para;
			temp = TagHtml.substring(TagHtml.indexOf(">")+1);
			temp = temp.substring(0,temp.indexOf("</GET>"));
			tag.htmlContent = temp;
			//获取标签数据格式和翻页格式
			if(temp.indexOf("#P#")!=-1){//配置翻页 #PAGE_HTML#
				tag.pageHtml = temp.substring(temp.indexOf("#P#")+3);
				tag.htmlContent = temp.substring(0,temp.indexOf("#P#"));
			}
			tagHTML = tag.parse();    
		    if (tagHTML == null) tagHTML = "";
		}catch(Exception ex){
			 tagHTML="标签解析异常!";
	    }    
	    return tagHTML;
	}
	public String quote(String s) {
		  int slashEIndex = s.indexOf("\\E");
		  if (slashEIndex == -1) {
		    return "\\Q" + s + "\\E";
		  }
		  StringBuffer sb = new StringBuffer(s.length() * 2);
		  sb.append("\\Q");
		  slashEIndex = 0;
		  int current = 0;
		  while ( (slashEIndex = s.indexOf("\\E", current)) != -1) {
		    sb.append(s.substring(current, slashEIndex));
		    current = slashEIndex + 2;
		    sb.append("\\E\\\\E\\Q");
		  }
		  sb.append(s.substring(current, s.length()));
		  sb.append("\\E");
		  return sb.toString();
	}
	public String quoteReplacement(String s) {
		  if ( (s.indexOf('\\') == -1) && (s.indexOf('$') == -1)) {
		    return s;
		  }
		  StringBuffer sb = new StringBuffer();
		  for (int i = 0; i < s.length(); i++) {
		    char c = s.charAt(i);
		    if (c == '\\') {
		      sb.append('\\');
		      sb.append('\\');
		    }
		    else if (c == '$') {
		      sb.append('\\');
		      sb.append('$');
		    }
		    else {
		      sb.append(c);
		    }
		  }
		  return sb.toString();
		}
	public String getTemplate(String template){
		List list = engineDao.find("select t from ToaInfopublishTemplate t where t.templateDesc = ? ",new Object[]{template});
		if(list.size()==1){
			ToaInfopublishTemplate Template = (ToaInfopublishTemplate)list.get(0);
			template = Template.getTemplateContent();
			return template;
		}
		return "";
	}
}
