package com.strongit.tag.web.statictree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import com.strongit.tag.web.util.ResponseUtils;

public class StaticTreeTag extends BodyTagSupport{
	
	private String title;
	private String iconpath;
	private String target;
	private String id="0";
	private JspWriter writer;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public JspWriter getWriter() {
		return writer;
	}
	public void setWriter(JspWriter writer) {
		this.writer = writer;
	}
	
	public String getIconpath() {
		return iconpath;
	}
	public void setIconpath(String iconpath) {
		this.iconpath = iconpath;
	}
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
	public int doStartTag() throws JspException{
		this.writer=pageContext.getOut();
		ResponseUtils.writePrevious(pageContext, scriptBeforeNode().toString());
		return EVAL_BODY_INCLUDE;
	}	
	
	public int doEndTag() throws JspException{
		ResponseUtils.write(pageContext, scriptBehindNode());
		return EVAL_PAGE;
	}
	
	public String getRootPath(){
		HttpServletRequest httpServletRequest = (HttpServletRequest)pageContext.getRequest();
		String rooturl=httpServletRequest.getContextPath();
		return rooturl;
	}
	
	public StringBuffer scriptBeforeNode(){
		StringBuffer str=new StringBuffer();
		str.append("<TABLE cellSpacing=0 cellPadding=2 width=\"100%\" border=0>\n")
		   .append("<TBODY>\n")
		   .append("<TR>\n")
		   .append("<TD width=5></TD>")
		   .append("<TD></TD></TR>")
		   .append("<TR>")
		   .append("<TD width=5></TD>")
		   .append("<TD>");
		str.append("<DIV class=dtree id=treeviewarea></DIV>\n")
		   .append("</TD>")
		   .append("</TR>")
		   .append("</TBODY>")
		   .append("</TABLE>")
		   .append("<SCRIPT language=javascript charset=utf-8>\n")
		   .append("window.tree = new MzTreeView(\"tree\");\n");
		if(iconpath!=null&&!"".equals(iconpath)){
			str.append("tree.setIconPath(\""+getRootPath()+"/"+iconpath+"\");\n");
//			System.out.println("tree.setIconPath(\""+getRootPath()+"/"+iconpath+"/\");\n");
		}else{
			str.append("tree.setIconPath(\""+getRootPath()+"/images/\");\n");
		}
		str.append("tree.icons[\"property\"] = \"property.gif\";\n")
		   .append("tree.icons[\"css\"] = \"collection.gif\";\n")
		   .append("tree.icons[\"book\"]  = \"book.gif\";\n")
		   .append("tree.iconsExpand[\"book\"] = \"bookopen.gif\";\n")
		   .append("tree.nodes[\"0_1\"] = 'text:"+title+";")
		   .append("isChecked:no;");

		str.append("';\n");
		return str;
	}
	
	public String scriptBehindNode(){
		StringBuffer str=new StringBuffer();
		str.append("document.getElementById('treeviewarea').innerHTML = tree.toString();\n");
		str.append("</SCRIPT>");
		return str.toString();
	}
	
	public void release(){
		super.release();
	}

}
