package com.strongit.tag.web.checkpsw;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.strongit.tag.web.util.ResponseUtils;

public class CheckPswStrong extends TagSupport{
	
	private String name;
	
	
	public int doEndTag() throws JspException{
		ResponseUtils.write(pageContext, htmlFactory().toString());
		return EVAL_PAGE;
	}
	
	public void release(){
		super.release();
	}
		
	public String htmlFactory(){
		StringBuffer str=new StringBuffer();
		str.append("<input name='").append(name).append("' type='password' value='' tabindex='2' maxlength='16' alt='密码:6-16/有空格/无内容/下划线/有全角/怪字符pwd' onBlur=\"ShowStrong('pswstrong',checkStrong(this.value),1);\"><label id=chk_").append(name).append("></label>");
		str.append("<br>密码强弱提示：<span align='left' id='pswstrong'></span>");
		str.append("<script> fm_ini(); </script>");
		return str.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
