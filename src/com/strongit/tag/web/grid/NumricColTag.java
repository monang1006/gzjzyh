package com.strongit.tag.web.grid;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public class NumricColTag extends TableColTag {
	private String caption = null ; /**表头显示文字*/
	private String height = null; /**单元格高度*/
	private String width = null; /**单元格宽度*/
	private boolean isCanDrag = true; /**是否可以拖动*/
	private boolean isCanSort = true; /**是否可以排序*/
	
	public NumricColTag(){
		caption = "文本" ;
		height = "22";
		width = "10";
		isCanDrag = true;
	}
	
	public int doStartTag() throws JspException {
		StringBuffer out=new StringBuffer();
		FlexTableTag flextag = (FlexTableTag)pageContext.getAttribute("com.strongit.tag.web.grid.FLEXTABLETAG");
		String headCss = flextag.getHeadCss();
		out.append("<th width=\""+width+"\" height=\""+height+"\" class=\"").append(headCss).append("\" showsize=\"").append(getShowsize()).append("\"");
		String tablename = flextag.getName();
		int footShow = flextag.getFootShow()==null||flextag.getFootShow().equals("null")?0:1;
		if(flextag.getIsCanDrag()&&isCanDrag)
			out.append(" onmousemove=\"moveCol(this,document.getElementById('"+tablename+"_div'))\"");
		if(isCanSort)
			out.append(" onclick=\"sort(this,"+footShow+",'numeric')\"");
		out.append(">").append(caption).append("</th>");
		flextag.getHeadList().add(out);
		flextag.getCellList().add(new TableColHelp(getProperty(),getShowValue(),getValuepos(),getValueshowpos(),getOnclick(),getOndblclick(),this));
		//pageContext.setAttribute("com.strongit.tag.web.grid.TEXTCOLTAG."+caption, ((Object) (this)));
		return EVAL_BODY_INCLUDE;
	}
	
	public String innerHTML(Object valueid,Object showvalue,Object onclick,Object ondbclick,Object map){
		StringBuffer out=new StringBuffer();
		FlexTableTag flextag=(FlexTableTag)pageContext.getAttribute("com.strongit.tag.web.grid.FLEXTABLETAG");
		String tablename=flextag.getName();
		String tdcss=flextag.getDetailCss();
		out.append("<td class=\""+tdcss+"\" onmousemove=\"moveCol(this,document.getElementById('"+tablename+"_div'))\"");
		if(onclick!=null&&!"".equals(onclick)&&!"null".equals(onclick))
			out.append(" onclick=\""+onclick+"\" style=\"cursor: hand;color: blue;\"");
		if(ondbclick!=null&&!"".equals(ondbclick)&&!"null".equals(ondbclick))
			out.append(" ondblclick=\""+ondbclick+"\"");
		out.append(">")	
			.append(showvalue)
			.append("</td>");
		return out.toString();
	}
	
	public int doEndTag() throws JspTagException{
		
		return EVAL_PAGE;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public boolean getIsCanDrag() {
		return isCanDrag;
	}

	public void setIsCanDrag(boolean isCanDrag) {
		this.isCanDrag = isCanDrag;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public boolean getIsCanSort() {
		return isCanSort;
	}

	public void setIsCanSort(boolean isCanSort) {
		this.isCanSort = isCanSort;
	}

}
