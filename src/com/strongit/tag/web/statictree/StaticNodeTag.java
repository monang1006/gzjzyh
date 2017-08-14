package com.strongit.tag.web.statictree;

import java.io.IOException;
import java.security.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import com.strongit.tag.web.util.ResponseUtils;

public class StaticNodeTag extends BodyTagSupport{
	
	private String name;
	private String url;
	private String img;
	private String target;
	private String onclick;
	private JspWriter writer;
	
	private String id;
	private Tag tag;
	private String parentid;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
//		System.out.println(name);
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	public JspWriter getWriter() {
		return writer;
	}
	
	public void setWriter(JspWriter writer) {
		this.writer = writer;
	}
	
	public String getTarget() {
		return target;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	
	public int doStartTag() throws JspException{
		if("test7".equals(name)){
			System.out.println(name);
		}
		tag=super.getParent();
		if("com.strongit.tag.web.statictree.StaticTreeTag".equals(tag.getClass().getName())){
			writer=((StaticTreeTag)tag).getWriter();
			parentid="1";
			target=((StaticTreeTag)tag).getTarget();
		}else{
			writer=((StaticNodeTag)tag).getWriter();
			parentid=((StaticNodeTag)tag).getId();
			target=((StaticNodeTag)tag).getTarget();
		}
		StringBuffer str=new StringBuffer();
		str.append("tree.nodes[\""+parentid+"_"+id+"\"] = 'text:"+name+";valueId:"+id+";");
		if(url!=null&&!"".equals(url)){
			str.append("url:"+getRootPath()+"/"+url+";");
		}
		if(onclick!=null&&!"".equals(onclick)){
			str.append("onclick:"+onclick+";");
		}
			str.append("isChecked:no;");
		if(img!=null&&!"".equals("img")){
			str.append("icon:"+img+";");
		}
		if(target!=null&&!"".equals(target)){
			str.append("target:"+target+";");
		}
		str.append("'\n");
		try {
			writer.print(str.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseUtils.writePrevious(super.pageContext, str.toString());
		return EVAL_PAGE;
	}

	public void release(){
		super.release();
	}
	
	public String getRootPath(){
		HttpServletRequest httpServletRequest = (HttpServletRequest)pageContext.getRequest();
		String rooturl=httpServletRequest.getContextPath();
		return rooturl;
	}
	public String getOnclick() {
		return onclick;
	}
	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
	

}
