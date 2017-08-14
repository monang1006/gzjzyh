package com.engine.tag;

import java.util.List;
import java.util.Map;

import com.strongmvc.orm.hibernate.GenericDAOHibernate;

public abstract class Tag {

public String tagContent;//标签内容
public String htmlContent;//数据格式内容
public String pageHtml;//翻页格式内容
public Map requestMap;//提交参数
public List para;//配置参数
public GenericDAOHibernate engineDao;
public abstract String parse();
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
}