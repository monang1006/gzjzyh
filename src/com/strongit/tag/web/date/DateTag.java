package com.strongit.tag.web.date;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.strongit.tag.web.util.ResponseUtils;

/**
 * <p>Title: DateTag.java</p>
 * <p>Description: 日期组件</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2009-10-10 15:45:15
 * @version  1.0
 */
public class DateTag extends TagSupport{
	
	private static final long serialVersionUID = -7536439687400513753L;
	
	private String name;					//日期组件中input的name属性值
	private String id;						//日期组件中input的id属性值
	private String dateform;				//日期控件中日期显示格式
	private String skin;					//日期控件皮肤
	private String width;					//日期控件宽度
	private boolean isicon;					//是否显示日期控件中的小图标
	private String nowvalue;				//以字符串的形式设置值
	private Date   dateobj;					//默认值，多用于从前台回传回来的日期对象
	private boolean isplam;					//是否以平面的方式进行显示
	private String mindate;					//可选日期范围的最小日期
	private String maxdate;					//可选日期范围的最大日期
	private String classtyle;				//CSS样式
	private String title;					//光标移动到其上时显示的提示信息
	private boolean cantinput;				//判断是否能进行输入(true:能 false:不能)
	private boolean disabled;				//是否允许写入编辑，包括点击的时候也不能够弹出选择
	private String datechange;				//是否允许写入编辑，包括点击的时候也不能够弹出选择	
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getClasstyle() {
		return classtyle;
	}
	public void setClasstyle(String classtyle) {
		this.classtyle = classtyle;
	}
	public String getDateform() {
		return dateform;
	}
	public void setDateform(String dateform) {
		this.dateform = dateform;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isIsicon() {
		return isicon;
	}
	public void setIsicon(boolean isicon) {
		this.isicon = isicon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSkin() {
		return skin;
	}
	public void setSkin(String skin) {
		this.skin = skin;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getNowvalue() {
		return nowvalue;
	}
	public void setNowvalue(String nowvalue) {
		this.nowvalue = nowvalue;
	}
	public int doEndTag() throws JspException{
		ResponseUtils.write(pageContext, htmlFactory().toString());
		return EVAL_PAGE;
	}
	
	public void release(){
		super.release();
	}
	
	public Date getDateobj() {
		return dateobj;
	}
	public void setDateobj(Date dateobj) {
		this.dateobj = dateobj;
	}
	
	public boolean isIsplam() {
		return isplam;
	}
	public void setIsplam(boolean isplam) {
		this.isplam = isplam;
	}

	
	public String htmlFactory(){
		StringBuffer str=new StringBuffer();
//		String formDate="";
//		if(dateform!=null&&!"".equals(dateform)){
//			formDate=dateform;
//		}else{
//			formDate="yyyy-MM-dd";
//		}
		if(isplam==true){
			str.append("<div id=div_"+id+"></div>");
			str.append("<script>");
			str.append("	WdatePicker({eCont:'div_"+id+"',onpicked:function(dp){sd_operate(dp)}})");
			str.append("</script>");
		}else{
//			str.append("<script>");
//			str.append("function checkWdatePicker(id){");
//			str.append("	var test=document.getElementById(id).value;");
//			str.append("	if(test==null||test==''){");
//			str.append("		");
//			str.append("	}else{");
//			str.append("		alert('admin');");
//			str.append("	}");
//			str.append("}");
//			str.append("</script>");
//			str.append("<input onselect=\"this.focus();\" maxlength=\""+formDate.length()+"\" style=\"ime-mode:disabled\" type=\"text\" id=\""+id+"\" ");
			if(cantinput){
				str.append("<input type=\"text\" id=\""+id+"\" ");
			}else{
				str.append("<input readonly type=\"text\" id=\""+id+"\" ");
			}
			if(name!=null&&!"".equals(name)){
				str.append("name=\""+name+"\" ");
			}
			if(title!=null&&!"".equals(title)){
				str.append("title=\""+title+"\" ");
			}
			
			if(disabled){
				str.append("disabled=\"disabled\" ");
			}else{
				
			}
//			str.append("onfocus=\"WdatePicker({dateFmt:'");
			str.append("onclick=\"WdatePicker({dateFmt:'");
			if(dateform!=null&&!"".equals(dateform)){
				str.append(dateform);
			}else{
				str.append("yyyy-MM-dd");
			}
			str.append("'");
			if(skin!=null&&!"".equals(skin)){
				str.append(",skin:'"+skin+"'");
			}
			if(mindate!=null&&!"".equals(mindate)){
				str.append(",minDate:'"+mindate+"'");
			}
			if(maxdate!=null&&!"".equals(maxdate)){
				str.append(",maxDate:'"+maxdate+"'");
			}
			str.append("})\" ");
			if(isicon==true){
				str.append("class=\"Wdate");
				if(classtyle!=null&&!"".equals(classtyle)&&!"null".equals(classtyle))
					str.append(" ").append(classtyle);
				str.append("\" ");
			}else if(classtyle!=null&&!"".equals(classtyle)&&!"null".equals(classtyle)){
				str.append("class=\"").append(classtyle).append("\" ");;
			}
		
			if(width!=null&&!"".equals(width)){
				str.append("style=\"width:"+width+";height:20px\" ");
			}else{
				str.append("style=\"width:100px;height:20px\" ");
			}
			
			if(nowvalue!=null&&!"".equals(nowvalue)){
				str.append("value='"+nowvalue+"'");
			}else{
				
				if(dateobj!=null){
					java.text.DateFormat format = new SimpleDateFormat(dateform);
					str.append("value='"+format.format(dateobj)+"'");
				}else{
					
					/** 新建公告显示服务器时间
					java.text.DateFormat format = new SimpleDateFormat(dateform);
					Date date=new Date();
					str.append("value='"+format.format(date)+"'");
					*/
				}
				
			}
			
			if(datechange!=null&&!"".equals(datechange)){
				str.append(" onchange='").append(datechange).append("' ");
			}
			
			
			str.append("/>");
		}
		
		return str.toString();
	}
	public String getMaxdate() {
		return maxdate;
	}
	public void setMaxdate(String maxdate) {
		this.maxdate = maxdate;
	}
	public String getMindate() {
		return mindate;
	}
	public void setMindate(String mindate) {
		this.mindate = mindate;
	}
	public void setCantinput(boolean cantinput) {
		this.cantinput = cantinput;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public String getDatechange() {
		return datechange;
	}
	public void setDatechange(String datechange) {
		this.datechange = datechange;
	}

}
